package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.templates.Utils.log2Up

class PMU(val p: PMUParams) extends CU {
  val io = IO(CUIO(p, PMUConfig(p)))

  // Scalar input FIFOs
  val scalarFIFOs = List.tabulate(p.numScalarIn) { i =>
    val fifo = Module(new FIFOCore(p.w, p.scalarFIFODepth, 1))
    val config = Wire(FIFOConfig(p.scalarFIFODepth, p.v))
    config.chainWrite := true.B
    config.chainRead := true.B
    fifo.io.config := config
    fifo.io.enq(0) := io.scalarIn(i).bits
//    fifo.io.enqVld := io.scalarIn(i).valid
    fifo
  }

  val scalarInXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.numScalarIn, p.numEffectiveScalarIn, p.w)))
  scalarInXbar.io.config := io.config.scalarInXbar
  scalarInXbar.io.ins := Vec(scalarFIFOs.map { _.io.deq(0) })
  val scalarIns = scalarInXbar.io.outs
//  val scalarIns = Vec(scalarFIFOs.map { _.io.deq(0)})

  // Vector input FIFOs
  val vectorFIFOs = List.tabulate(p.numVectorIn) { i =>
    val fifo = Module(new FIFOCore(p.w, p.vectorFIFODepth, p.v))
    val config = Wire(FIFOConfig(p.vectorFIFODepth, p.v))
    config.chainWrite := false.B
    config.chainRead := false.B
    fifo.io.config := config
    fifo.io.enq := io.vecIn(i).bits
    fifo.io.enqVld := io.vecIn(i).valid
    io.vecIn(i).ready := ~(fifo.io.almostFull | fifo.io.full)
    fifo
  }

  // Counter chain
  val counterChain = Module(new CounterChainCore(p.w, p.numCounters))
  counterChain.io.config := io.config.counterChain

  // Connect max and stride
  val ctrMaxStrideSources = scalarIns
  for (i <- 0 until p.numCounters) {
    // max
    val maxMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    maxMux.io.ins := ctrMaxStrideSources
    maxMux.io.sel := io.config.counterChain.counters(i).max.value
    counterChain.io.max(i) := maxMux.io.out

    // stride
    val strideMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    strideMux.io.ins := ctrMaxStrideSources
    strideMux.io.sel := io.config.counterChain.counters(i).stride.value
    counterChain.io.stride(i) := strideMux.io.out
  }

  // Control Block
  val cbox = Module(new PMUControlBox(p))
  cbox.io.controlIn := io.controlIn
  io.controlOut := cbox.io.controlOut

  cbox.io.fifoNotFull := Vec(scalarFIFOs.map { ~_.io.full })
  cbox.io.fifoNotEmpty := Vec(scalarFIFOs.map { ~_.io.empty } ++ vectorFIFOs.map { ~_.io.empty })

  scalarFIFOs.zip(cbox.io.scalarFifoDeqVld) foreach { case (fifo, deqVld) => fifo.io.deqVld := deqVld }
  scalarFIFOs.zip(cbox.io.scalarFifoEnqVld) foreach { case (fifo, enqVld) => fifo.io.enqVld := enqVld }

  counterChain.io.enable := cbox.io.enable

  // Connect enable to deqVld of all vector FIFOs.
  // Deq only if there is a valid element in the FIFO
  // TODO: Add deqVld muxes to select the stage that drives the deqVld signal
//  vectorFIFOs.foreach { fifo => fifo.io.deqVld := cbox.io.enable & ~fifo.io.empty }

  cbox.io.done := counterChain.io.done

  cbox.io.config := io.config.control

  // Pipeline with FUs and registers
  def getPipeRegs: List[FF] = List.tabulate(p.r) { i =>
    val ff = Module(new FF(p.w))
    ff.io.init := 0.U
    ff
  }

  val pipeStages = ListBuffer[FU]()
  val pipeRegs = ListBuffer[List[FF]]()

  for (i <- 0 until p.d) {
    val fu = Module(new FU(p.w, false, false))
    val regs = getPipeRegs
    val stageConfig = io.config.stages(i)

    def getSourcesMux(s: SelectSource) = {
      val sources = s match {
        case CounterSrc => counterChain.io.out
        case ScalarFIFOSrc => scalarIns
        case VectorFIFOSrc => Vec(vectorFIFOs.map { _.io.deq(0) })
        case PrevStageSrc => if (i == 0) Vec(List.fill(2) {0.U}) else Vec(pipeRegs.last.map {_.io.out})
        case CurrStageSrc => Vec(regs.map {_.io.out})
        case _ => throw new Exception("Unsupported operand source!")
      }
      val mux = Module(new MuxN(sources(0).cloneType, sources.size))
      mux.io.ins := sources
      mux
    }

    def getOperand(opConfig: SrcValueBundle) = {
      val sources = opConfig.nonXSources map { s => s match {
        case ConstSrc => opConfig.value
        case _ =>
          val m = getSourcesMux(s)
          m.io.sel := opConfig.value
          m.io.out
      }}
      val mux = Module(new MuxN(UInt(p.w.W), sources.size))
      mux.io.ins := Vec(sources)
      mux.io.sel := opConfig.src
      mux.io.out
    }

    fu.io.a := getOperand(stageConfig.opA)
    fu.io.b := getOperand(stageConfig.opB)
    fu.io.c := getOperand(stageConfig.opC)
    fu.io.opcode := stageConfig.opcode

    // Handle writing to regs
    var counterPtr = -1
    var scalarInPtr = -1
    var vectorInPtr = -1
    regs.zipWithIndex.foreach { case (reg, idx) =>
      if (i != 0) {
        val prevRegs = pipeRegs.last
        reg.io.in := Mux(stageConfig.result(idx), fu.io.out, prevRegs(idx).io.out)
      } else {
        // Use regcolor to pick the right thing to forward
        val validFwds = Set[RegColor](CounterReg, ScalarInReg, VecInReg)
        val colors = p.regColors(idx).filter { validFwds.contains(_) }
        Predef.assert(colors.size == 1, s"ERROR: Invalid number of valid regcolors (should be 1): $colors")
        val fwd = colors(0) match {
          case CounterReg =>
            counterPtr += 1
            counterChain.io.out(counterPtr)
          case ScalarInReg =>
            scalarInPtr += 1
            scalarFIFOs(scalarInPtr).io.deq(0)
          case VecInReg =>
            vectorInPtr += 1
            vectorFIFOs(vectorInPtr).io.deq(0)
          case _ => throw new Exception("Unsupported fwd color for first stage!")
        }
        reg.io.in := Mux(stageConfig.result(idx), fu.io.out, fwd)
      }
      reg.io.enable := stageConfig.regEnables(idx)
    }
    pipeStages.append(fu)
    pipeRegs.append(regs)
  }

  val addrs = pipeRegs.map { _(p.outputRegID).io.out }.toList

  // Scratchpad
  def getMux[T<:Data](ins: List[T], sel: UInt): T = {
    val srcMux = Module(new MuxN(ins(0).cloneType, ins.size))
    srcMux.io.ins := Vec(ins)
    srcMux.io.sel := sel
    srcMux.io.out
  }

  val scratchpad = Module(new Scratchpad(p))
  scratchpad.io.config := io.config.scratchpad
  scratchpad.io.wdata := getMux(vectorFIFOs.map {_.io.deq}, io.config.wdataSelect)
  scratchpad.io.waddr := getMux(addrs, io.config.waddrSelect)
  scratchpad.io.raddr := getMux(addrs, io.config.raddrSelect)
  scratchpad.io.wen := false.B  // TODO: Fix after threading enable signals through pipeline

  // Output assignment
  def getSourcesMux(s: SelectSource) = {
    val sources = s match {
      case EnableSrc => counterChain.io.enable
      case DoneSrc => counterChain.io.done
      case _ => throw new Exception("Unsupported operand source!")
    }
    val mux = Module(new MuxN(sources(0).cloneType, sources.size))
    mux.io.ins := sources
    mux
  }

  def getValids(opConfig: SrcValueBundle) = {
    val sources = opConfig.nonXSources map { s => s match {
      case _ =>
        val m = getSourcesMux(s)
        m.io.sel := opConfig.value
        m.io.out
    }}
    val mux = Module(new MuxN(UInt(p.w.W), sources.size))
    mux.io.ins := Vec(sources)
    mux.io.sel := opConfig.src
    mux.io.out
  }

  val scalarOutXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.numEffectiveScalarOut, p.numScalarOut, p.w)))
  scalarOutXbar.io.config := io.config.scalarOutXbar
  val scalarOutIns = Vec(pipeRegs.last.take(p.numPipelineScalarOuts).map { _.io.out } ++ scratchpad.io.rdata.getElements.take(p.numScratchpadScalarOuts))
  scalarOutXbar.io.ins := scalarOutIns
//  scalarOutXbar.io.ins.zipWithIndex.foreach { case (in, i) =>
//    in := scalarOutIns
//    in := pipeRegs.last(i).io.out // TODO: Modify to comment above after introducing Scratchpad
//  }

  io.scalarOut.zip(scalarOutXbar.io.outs).foreach { case (out, xbarOut) =>
    out.bits := xbarOut
    out.valid := cbox.io.enable(0)   // Output produced by read addr logic only, which is controlledby enable(0)
                                     // TODO: Fix after threading enable signals
//    out.valid := getValids(io.config.scalarValidOut(i))
  }

  io.vecOut.zipWithIndex.foreach { case (out, i) =>
    out.bits := scratchpad.io.rdata
    out.valid := cbox.io.enable(0) & io.config.rdataEnable(i) // Output produced by read addr logic only, which is controlledby enable(0)
                                                              // TODO: Fix after threading enable signals
//    out.valid := getValids(io.config.vectorValidOut(i))
  }

}
