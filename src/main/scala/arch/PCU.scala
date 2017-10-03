package plasticine.arch

import chisel3._
import chisel3.util._
import templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

import templates._
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import templates.Utils.log2Up
import plasticine.misc.Utils.getCounter

case class DummyCUIO[+T<:Bundle](p:CUParams, cuConfig: () => T) extends Bundle {
    // Vector IO
  val vecIn = Vec(p.numVectorIn, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
  val vecOut = Vec(p.numVectorOut, Decoupled(Vec(p.v, UInt(p.w.W))))

  // Scalar IO
  val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))
  val scalarOut = Vec(p.numScalarOut, Decoupled(UInt(p.w.W)))

  // Control IO
  val controlIn = Input(Vec(p.numControlIn, Bool()))
  val controlOut = Output(Vec(p.numControlOut, Bool()))

  val config = Input(cuConfig())
}

class DummyPCU(val p: PCUParams)(row: Int, col: Int) extends Module {
  val io = IO(DummyCUIO(p, () => DummyPCUConfig(p)))
//  val io = IO(new Bundle{
//    val config = Input(DummyPCUConfig(p))
//  })
}

class PCU(val p: PCUParams)(row: Int, col: Int) extends CU {
  println(s"PCUParams: $p")
  override def desiredName = this.getClass.getName.split('.').last + s"_${row}_${col}"

  val io = IO(CUIO(p, () => PCUConfig(p)))

  val configSR = Module(new ShiftRegister(PCUConfig(p)))
  configSR.io.in <> io.configIn
  configSR.reset := io.configReset
  io.configOut <> configSR.io.out
  val localConfig = configSR.io.config

  val numReduceStages = log2Up(p.v)

  // Which stages contain the fused multiply-add (FMA) unit?
  val fmaStages = (p.d/2 until p.d).toList

  // Sanity check parameters for validity
  // #stages: Currently there must be at least one 'regular' stage
  // after 'rwStages' and before 'reduction' stages because of the way
  // the dataSrc muxes are created.
  // i.e., d >= rwStages + 1 + numReduceStages + numStagesAfterReduction
  val numStagesAfterReduction = 1
  Predef.assert(p.d >= (1 + numReduceStages + numStagesAfterReduction), s"""#stages ${p.d} < min. legal stages (1 + $numReduceStages + $numStagesAfterReduction)!""")

  def getMux[T<:Data](ins: List[T], sel: UInt): T = {
    val srcMux = Module(new MuxN(ins(0).cloneType, ins.size))
    srcMux.io.ins := Vec(ins)
    srcMux.io.sel := sel
    srcMux.io.out
  }

  val scalarInRegs = p.getRegIDs(ScalarInReg)
  val scalarOutRegs = p.getRegIDs(ScalarOutReg)
  val vectorInRegs = p.getRegIDs(VecInReg)
  val vectorOutRegs = p.getRegIDs(VecOutReg)

  // Scalar in Xbar
  val numEffectiveScalarIns = p.getNumRegs(ScalarInReg)
  val scalarInXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.numScalarIn, numEffectiveScalarIns, p.w)))
  scalarInXbar.io.config := localConfig.scalarInXbar
  scalarInXbar.io.ins := Vec(io.scalarIn.map { _.bits })

  Predef.assert(numEffectiveScalarIns > 0, s"numEffectiveScalarIns == $numEffectiveScalarIns (must be >0)!")

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
  val scalarIns = scalarFIFOs.zipWithIndex.map { case (fifo, i) => Mux(localConfig.fifoNbufConfig(i) === 1.U, scalarInXbar.io.outs(i), fifo.io.deq(0)) }

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
  counterChain.io.config := localConfig.counterChain

  // Connect max and stride
  val ctrMaxStrideSources = scalarIns
  val ctrStrides = Wire(Vec(p.numCounters, UInt(p.w.W)))

  for (i <- 0 until p.numCounters) {
    // max
    val maxMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    maxMux.io.ins := ctrMaxStrideSources
    maxMux.io.sel := localConfig.counterChain.counters(i).max.value
    counterChain.io.max(i) := maxMux.io.out

    // stride
    val strideMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    strideMux.io.ins := ctrMaxStrideSources
    strideMux.io.sel := localConfig.counterChain.counters(i).stride.value
    counterChain.io.stride(i) := strideMux.io.out
    ctrStrides(i) := Mux(localConfig.counterChain.counters(i).stride.is(ConstSrc), localConfig.counterChain.counters(i).stride.value,  strideMux.io.out)
  }

  // Control Block
  val cbox = Module(new PCUControlBox(p))
  cbox.io.config := localConfig.control
  cbox.io.controlIn := io.controlIn
  io.controlOut := cbox.io.controlOut

  cbox.io.fifoNotFull := Vec(scalarFIFOs.map { ~_.io.full })
  cbox.io.fifoNotEmpty := Vec(scalarFIFOs.map { ~_.io.empty } ++ vectorFIFOs.map { ~_.io.empty })

  scalarFIFOs.zip(cbox.io.scalarFifoDeqVld) foreach { case (fifo, deqVld) => fifo.io.deqVld := deqVld }
  scalarFIFOs.zip(cbox.io.scalarFifoEnqVld) foreach { case (fifo, enqVld) => fifo.io.enqVld := enqVld }

  // Connect enable to only the zeroth counter, as presumably the compiler
  // always assigns counters that way
  counterChain.io.enable.zipWithIndex.foreach {case (en, idx) => if (idx == 0) en := cbox.io.enable else en := false.B }

  // Connect enable to deqVld of all vector FIFOs.
  // Deq only if there is a valid element in the FIFO
  vectorFIFOs.foreach { fifo => fifo.io.deqVld := cbox.io.enable & ~fifo.io.empty }

  // TODO: Testing this
  val localEnable = Wire(Bool())
  localEnable := cbox.io.enable

  val localDone = Wire(Bool())
  localDone := getMux(counterChain.io.done.getElements.toList, localConfig.control.doneXbar.outSelect(0) - 1.U)
  cbox.io.done := localDone

//  val donePulser = Module(new Pulser())
//  donePulser.io.in := localCounterDone
//  localDone := donePulser.io.out

//  cbox.io.done := counterChain.io.done

  // Pipeline with FUs and registers
  def getPipeRegs: List[FF] = List.tabulate(p.r) { i =>
    val ff = Module(new FF(p.w))
    ff.io.init := 0.U
    ff
  }

  val pipeStages = ListBuffer[List[FU]]()
  val pipeRegs = ListBuffer[List[List[FF]]]()
  val stageEnables = ListBuffer[FF]()
  val fwdLanes = ListBuffer[HashMap[Int, Int]]()

  // Reduction stages
  val reduceStages = (0 until p.d).dropRight(2).takeRight(numReduceStages)  // Leave 1 stage to do in-place accumulation, 1 stage to move data to correct register

  val unrolledCounters = List.tabulate(p.v) { lane =>
    Vec(List.tabulate(p.numCounters) { ctr => counterChain.io.out(ctr) + lane.U * ctrStrides(ctr) })
  }

  for (i <- 0 until p.d) {
//    val fus = List.fill(p.v) { Module(new FU(p.w, fmaStages.contains(i), true)) }
    val fus = List.fill(p.v) { Module(new FU(p.w, false, false)) } // TODO :Change after Float support
    val stageRegs = List.fill(p.v) { getPipeRegs }
    val stageConfig = localConfig.stages(i)
    val stageEnableFF = Module(new FF(2)) // MSB: 'Done/Last iter', LSB: 'Enable'
    stageEnableFF.io.init := 0.U
    stageEnableFF.io.enable := 1.U

    // Reduction tree specific:
    // If this stage is one of the reduction stages, get its position within the reduce tree stages.
    val isReduceStage = reduceStages.contains(i)
    val reduceStageNum = reduceStages.indexOf(i)
    // Lanes in the current stage that need to be forwarded values from previous stage
    val reduceLanes = (0 until p.v by (1 << (reduceStageNum+1))).toList
    // Forward lanes for each lane
    val fwdLaneMap = new HashMap[Int, Int]()
    reduceLanes.foreach { r =>
      fwdLaneMap(r) = r + (1 << reduceStageNum)
    }
    fwdLanes.append(fwdLaneMap)

//    println(s"stage $i: [reduce: $isReduceStage] fwdLaneMap $fwdLaneMap")
    (0 until p.v) foreach { lane =>
      val fu = fus(lane)
      val regs = stageRegs(lane)

      def getSourcesMux(s: SelectSource) = {
        val sources = s match {
          case CounterSrc => unrolledCounters(lane)
          case ScalarFIFOSrc => scalarIns
          case VectorFIFOSrc => Vec(vectorFIFOs.map { _.io.deq(lane) })
          case PrevStageSrc => if (i == 0) Vec(List.fill(2) {0.U}) else Vec(pipeRegs.last(lane).map {_.io.out})
          case CurrStageSrc => Vec(regs.map {_.io.out})
          case EnableSrc => counterChain.io.enable
          case DoneSrc => counterChain.io.done
          case _ => throw new Exception("Unsupported operand source!")
        }
        val mux = Module(new MuxN(sources(0).cloneType, sources.size))
        mux.io.ins := sources
        mux
      }

      def getOperand(opConfig: SrcValueBundle, allowReduce: Boolean = false) = {
        val sources = opConfig.nonXSources.map { s => s match {
          case ConstSrc => List(opConfig.value)
          case ReduceTreeSrc =>
            if (allowReduce) {
              Predef.assert(isReduceStage, s"Stage $i is not a reduce stage!")
              if (fwdLaneMap.contains(lane)) {
                val prevRegBlock = pipeRegs.last(fwdLaneMap(lane))
                val reduceReg = p.getRegIDs(ReduceReg)
                Predef.assert(reduceReg.size == 1, s"ERROR: Only single reduceReg supported! reduceRegs: $reduceReg")
                List(prevRegBlock(reduceReg(0)).io.out)
              } else {
                List()
              }
            } else {
              List()
            }
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

      fu.io.a := getOperand(stageConfig.opA, allowReduce = isReduceStage) // reduction op is only opA
      fu.io.b := getOperand(stageConfig.opB)
      fu.io.c := getOperand(stageConfig.opC)
      fu.io.opcode := stageConfig.opcode

      // Handle writing to regs
      var counterPtr = -1
      var scalarInPtr = -1
      var vectorInPtr = -1
      regs.zipWithIndex.foreach { case (reg, idx) =>
        if (i != 0) {
          val prevRegs = pipeRegs.last(lane)
          reg.io.in := Mux(stageConfig.result(idx), fu.io.out, prevRegs(idx).io.out)
        } else {
          // Use regcolor to pick the right thing to forward
          val validFwds = Set[RegColor](CounterReg, ScalarInReg, VecInReg)
          val colors = p.regColors(idx).filter { validFwds.contains(_) }
          val fwd = if (colors.size > 0) { // Issue #23
            colors(0) match {
              case CounterReg =>
                counterPtr += 1
                counterChain.io.out(counterPtr)
              case ScalarInReg =>
                scalarInPtr += 1
                scalarIns(scalarInPtr)
              case VecInReg =>
                vectorInPtr += 1
                vectorFIFOs(vectorInPtr).io.deq(lane)
              case _ => throw new Exception("Unsupported fwd color for first stage!")
            }
          } else 0.U
          reg.io.in := Mux(stageConfig.result(idx), fu.io.out, fwd)
        }
        reg.io.enable := (if (i == 0) localEnable else stageEnables.last.io.out(0)) & stageConfig.regEnables(idx)
      }
    }
    pipeStages.append(fus)
    pipeRegs.append(stageRegs)

    // Assign stageEnable
    if (i == 0) {  // Pick from counter enables
//      stageEnableFF.io.in := cbox.io.enable
      stageEnableFF.io.in := Cat(localDone, localEnable)
    } else {
      val sources = stageConfig.enableSelect.nonXSources map { m => m match {
        case PrevStageSrc => stageEnables.last.io.out(0)
        case ConstSrc => stageConfig.enableSelect.value
        case _ => throw new Exception(s"[PCU] Unsupported source type $m for stage enables")
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

//  cbox.io.done.foreach { d => d := delayedDone }

//  def getStageOut(stage: List[FU]): Vec[UInt]  = Vec(stage.map { _.io.out })
//  def getStageOut(i: Int) : Vec[UInt] = getStageOut(pipeStages(i))

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
  scalarOutXbar.io.config := localConfig.scalarOutXbar
  val scalarOutIns = scalarOutRegs.map { r => pipeRegs.last(0)(r).io.out }
  scalarOutXbar.io.ins := scalarOutIns
//  scalarOutXbar.io.ins.zipWithIndex.foreach { case (in, i) =>
//    in := pipeRegs.last(0)(i).io.out
//  }

  io.scalarOut.zip(scalarOutXbar.io.outs).foreach { case (out, xbarOut) =>
    out.bits := xbarOut
    out.valid := stageEnables.last.io.out(0)
//    out.valid := getValids(localConfig.scalarValidOut(i))
  }

  io.vecOut.zipWithIndex.foreach { case (out, i) =>
    out.bits := Vec(pipeRegs.last.map { _(vectorOutRegs(i)).io.out })
    out.valid := stageEnables.last.io.out(0)
//    out.valid := getValids(localConfig.vectorValidOut(i))
  }


  /***************************************************************************************
   *
   *                               DOT DEBUGGING
   *
   **************************************************************************************/
  val captureDotValues = localEnable | stageEnables.map {_.io.out(0)}.reduce {_|_}
  val dotCycleCount = getCounter(captureDotValues)

  def prefix =
p"""
digraph PCU${row}${col} {  // Cycle ${dotCycleCount}
    node [pin=true];
    label="Cycle $dotCycleCount"
"""

  def suffix =
p"""
} // Cycle ${dotCycleCount}
"""

	val height = 30
	val width = 70
  val regsToPrint = 4
  val lanesToPrint = p.v
  val xoffset:Float = 3
  val yoffset:Float = 2

  def nodeName(stage: Int, lane: Int) = s"s${stage}_l${lane}"
  def opName(stage: Int, lane: Int) = s"op${stage}_l${lane}"
  def regPortName(reg: Int) = s"r${reg}"

  def getRow(stage: Int, lane: Int, reg: Int, active: Boolean) = {
    val bgcolor = if (active) "white" else "gray"
p"""<TR><TD HEIGHT="${height}" WIDTH="${width}" FIXEDSIZE="true" BGCOLOR="$bgcolor" PORT="${regPortName(reg)}">${pipeRegs(stage)(lane)(reg).io.out}</TD></TR>
"""
  }

  def printRegFile(stage: Int, lane: Int) {
    val tablePrefix = s"""<TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0">"""
    val tableSuffix = s"""</TABLE>"""
    printf(
p"""${nodeName(stage, lane)} [shape=plaintext, pos="${stage*xoffset}, ${lane*yoffset}!", label=<
  $tablePrefix
""")
    for (r <- 0 until regsToPrint) {
      when (localConfig.stages(stage).regEnables(r)) {
        printf(getRow(stage, lane, r, true))
      }.otherwise {
        printf(getRow(stage, lane, r, false))
      }
    }
    printf(
p"""$tableSuffix
>];
""")
    printf(p"""${opName(stage, lane)} [shape=square, label="OP"]""")
    printf(p"""${opName(stage, lane)} [shape=square, pos="${stage*xoffset-xoffset/2}, ${lane*yoffset}!", label="OP"]""")
  }

  def printRegFiles {
    (0 until p.d) foreach { stage =>
      (0 until lanesToPrint) foreach { lane =>
        printRegFile(stage, lane)
      }
    }
  }

  def doOperandStuff(opConfig: SrcValueBundle, stage: Int, lane: Int) = {
    val rhs = opName(stage, lane)
//    when (opConfig.is(ConstSrc)) {
//      printf(p"""c_${nodeName(stage, lane)} [shape=square, label="${opConfig.value}"]""")
//      printf(p"c_${nodeName(stage, lane)} -> ${rhs}\n")
//    }
    when (opConfig.is(ReduceTreeSrc)) {
      val srcStage = stage - 1
      if (fwdLanes(stage).contains(lane)) {
        val srcLane = fwdLanes(stage)(lane)
        val reg = p.getRegIDs(ReduceReg)
        printf(p"${nodeName(srcStage, srcLane)}:${regPortName(reg(0))} -> ${rhs}\n")
      }
    }
    when (opConfig.is(PrevStageSrc)) {
      val srcStage = stage - 1
      val srcLane = lane
      val reg = opConfig.value
      val str = s"${nodeName(srcStage, srcLane)}:r%d -> ${rhs}\n"
      printf(str, reg)
    }
    when (opConfig.is(CurrStageSrc)) {
      val srcStage = stage
      val srcLane = lane
      val reg = opConfig.value
      val str = s"${nodeName(srcStage, srcLane)}:r%d -> ${rhs}\n"
      printf(str, reg)
    }
  }

  def connectRegFiles {
    (0 until p.d) foreach { stage =>
      (0 until lanesToPrint) foreach { lane =>
//        (0 until regsToPrint) foreach { reg =>
          if (stage > 0) {
            // Print operand (stuff -> op) connections: get (stage, lane, reg) numbers based on opConfigs
            doOperandStuff(localConfig.stages(stage).opA, stage, lane)
            doOperandStuff(localConfig.stages(stage).opB, stage, lane)
            doOperandStuff(localConfig.stages(stage).opC, stage, lane)

//            printf(s"${nodeName(stage-1, lane)}:${regPortName(reg)} -> ${opName(stage, lane)}\n")
          }
          // Print output (op -> stuff) connections
          (0 until math.min(p.r, regsToPrint)) foreach { r =>
            when (localConfig.stages(stage).result(r)) {
              printf(s"${opName(stage, lane)} -> ${nodeName(stage, lane)}:${regPortName(r)}\n")
            }
          }
        }
//      }
    }
  }

  // Conditional printing
  def printDot {
    when (captureDotValues) {
      printf(prefix)
      printRegFiles
      connectRegFiles
      printf(suffix)
    }
  }

  printDot
}
