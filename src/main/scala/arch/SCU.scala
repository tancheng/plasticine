package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

import plasticine.templates._
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.templates.Utils.log2Up
import plasticine.misc.Utils._

class ScalarCU(val p: ScalarCUParams) extends CU {
  val io = IO(CUIO(p, () => ScalarCUConfig(p)))

  // Sanity check parameters for validity

  val scalarInRegs = p.getRegIDs(ScalarInReg)
  val scalarOutRegs = p.getRegIDs(ScalarOutReg)

  // Scalar in Xbar
  val numEffectiveScalarIns = p.getNumRegs(ScalarInReg)
  val scalarInXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.numScalarIn, numEffectiveScalarIns, p.w)))
  scalarInXbar.io.config := io.config.scalarInXbar
  scalarInXbar.io.ins := Vec(io.scalarIn.map { _.bits })


  // Scalar input FIFOs
  val scalarFIFOs = List.tabulate(numEffectiveScalarIns) { i =>
    val fifo = Module(new FIFOCore(p.w, p.scalarFIFODepth, 1))
    val config = Wire(FIFOConfig(p.scalarFIFODepth, p.v))
    config.chainWrite := true.B
    config.chainRead := true.B
    fifo.io.config := config
    fifo.io.enq(0) := scalarInXbar.io.outs(i)
//    fifo.io.enqVld := io.scalarIn(i).valid
    fifo
  }

//  val scalarIns = scalarInXbar.io.outs
  val scalarIns = scalarFIFOs.zipWithIndex.map { case (fifo, i) => Mux(io.config.fifoNbufConfig(i) === 1.U, scalarInXbar.io.outs(i), fifo.io.deq(0)) }

  // Counter chain
  val counterChain = Module(new CounterChainCore(p.w, p.numCounters))
  counterChain.io.config := io.config.counterChain

  // Connect max and stride
  val ctrMaxStrideSources = scalarIns
  val ctrStrides = Wire(Vec(p.numCounters, UInt(p.w.W)))

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
    ctrStrides(i) := Mux(io.config.counterChain.counters(i).stride.is(ConstSrc), io.config.counterChain.counters(i).stride.value,  strideMux.io.out)
  }

  // Control Block
  val cbox = Module(new ScalarCUControlBox(p))
  cbox.io.config := io.config.control
  cbox.io.controlIn := io.controlIn
  io.controlOut := cbox.io.controlOut

  cbox.io.fifoNotFull := Vec(scalarFIFOs.map { ~_.io.full })
  cbox.io.fifoNotEmpty := Vec(scalarFIFOs.map { ~_.io.empty })

  scalarFIFOs.zip(cbox.io.scalarFifoDeqVld) foreach { case (fifo, deqVld) => fifo.io.deqVld := deqVld }
  scalarFIFOs.zip(cbox.io.scalarFifoEnqVld) foreach { case (fifo, enqVld) => fifo.io.enqVld := enqVld }

  // Connect enable to only the zeroth counter, as presumably the compiler
  // always assigns counters that way
  counterChain.io.enable.zipWithIndex.foreach {case (en, idx) => if (idx == 0) en := cbox.io.enable else en := false.B }

  // TODO: Testing this
  val localEnable = Wire(Bool())
  localEnable := cbox.io.enable

  val localDone = Wire(Bool())
  localDone := getMux(counterChain.io.done.getElements.toList, io.config.control.doneXbar.outSelect(0) - 1.U)
  cbox.io.done := localDone

  // Pipeline with FUs and registers
  def getPipeRegs: List[FF] = List.tabulate(p.r) { i =>
    val ff = Module(new FF(p.w))
    ff.io.init := 0.U
    ff
  }

  val pipeStages = ListBuffer[FU]()
  val pipeRegs = ListBuffer[List[FF]]()
  val stageEnables = ListBuffer[FF]()

  for (i <- 0 until p.d) {
    val fu = Module(new FU(p.w, false, false))
    val stageRegs = getPipeRegs
    val stageConfig = io.config.stages(i)
    val stageEnableFF = Module(new FF(2)) // MSB: 'Done/Last iter', LSB: 'Enable'
    stageEnableFF.io.init := 0.U
    stageEnableFF.io.enable := 1.U

    val scalarInsWire = Wire(Vec(scalarIns.size, scalarIns.head.cloneType))
    scalarInsWire := scalarIns

    def getSourcesMux(s: SelectSource) = {
      val sources = s match {
        case CounterSrc => counterChain.io.out
        case ScalarFIFOSrc => Vec(scalarIns)
        case PrevStageSrc => if (i == 0) Vec(List.fill(2) {0.U}) else Vec(pipeRegs.last.map {_.io.out})
        case CurrStageSrc => Vec(stageRegs.map {_.io.out})
        case EnableSrc => counterChain.io.enable
        case DoneSrc => counterChain.io.done
        case _ => throw new Exception("Unsupported operand source!")
      }
      val mux = Module(new MuxN(sources(0).cloneType, sources.size))
      mux.io.ins := sources
      mux
    }

    def getOperand(opConfig: SrcValueBundle) = {
      val sources = opConfig.nonXSources.map { s => s match {
        case ConstSrc => List(opConfig.value)
        case _ =>
          val m = getSourcesMux(s)
          m.io.sel := opConfig.value
          List(m.io.out)
      }}.flatten
      val mux = Module(new MuxN(UInt(p.w.W), sources.size))
      mux.io.ins := Vec(sources)
      mux.io.sel := opConfig.src
      mux.io.out
    }

    fu.io.a := getOperand(stageConfig.opA) // reduction op is only opA
    fu.io.b := getOperand(stageConfig.opB)
    fu.io.c := getOperand(stageConfig.opC)
    fu.io.opcode := stageConfig.opcode

    // Handle writing to regs
    var counterPtr = -1
    var scalarInPtr = -1
    var vectorInPtr = -1
    stageRegs.zipWithIndex.foreach { case (reg, idx) =>
      if (i != 0) {
        val prevRegs = pipeRegs.last
        reg.io.in := Mux(stageConfig.result(idx), fu.io.out, prevRegs(idx).io.out)
      } else {
        // Use regcolor to pick the right thing to forward
        val validFwds = Set[RegColor](CounterReg, ScalarInReg)
        val colors = p.regColors(idx).filter { validFwds.contains(_) }
        val fwd = if (colors.size == 0) 0.U else colors(0) match {
          case CounterReg =>
            counterPtr += 1
            counterChain.io.out(counterPtr)
          case ScalarInReg =>
            scalarInPtr += 1
            scalarIns(scalarInPtr)
          case _ => throw new Exception("Unsupported fwd color for first stage!")
        }
        reg.io.in := Mux(stageConfig.result(idx), fu.io.out, fwd)
      }
      reg.io.enable := (if (i == 0) localEnable else stageEnables.last.io.out(0)) & stageConfig.regEnables(idx)
    }

    pipeStages.append(fu)
    pipeRegs.append(stageRegs)

    // Assign stageEnable
    if (i == 0) {  // Pick from counter enables
      stageEnableFF.io.in := Cat(localDone, localEnable)
    } else {
      val sources = stageConfig.enableSelect.nonXSources map { m => m match {
        case PrevStageSrc => stageEnables.last.io.out(0)
        case ConstSrc => stageConfig.enableSelect.value
        case _ => throw new Exception(s"[ScalarCU] Unsupported source type $m for stage enables")
      }}
      stageEnableFF.io.in := Cat(stageEnables.last.io.out(1), getMux(sources, stageConfig.enableSelect.src))
    }
    stageEnables.append(stageEnableFF)
  }

  val delayedDone = Wire(Bool())
  delayedDone := stageEnables.last.io.out(1)
  val delayedEnable = Wire(Bool())
  delayedEnable := stageEnables.last.io.out(0)

  cbox.io.delayedDone := delayedDone
  cbox.io.delayedEnable := delayedEnable

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

  val scalarOutXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.getNumRegs(ScalarOutReg), p.numScalarOut, p.w)))
  scalarOutXbar.io.config := io.config.scalarOutXbar
  val scalarOutIns = scalarOutRegs.map { r => pipeRegs.last(r).io.out }
  scalarOutXbar.io.ins := scalarOutIns

  io.scalarOut.zip(scalarOutXbar.io.outs).foreach { case (out, xbarOut) =>
    out.bits := xbarOut
    out.valid := stageEnables.last.io.out(0)
  }
}
