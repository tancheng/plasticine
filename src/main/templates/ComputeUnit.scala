package plasticine.templates

import Chisel._
import plasticine.pisa.ir._
import plasticine.Globals

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Scratchpad config bundle
 * @param w: Word width
 * @param m: Memory size
 * @param v: Vector length
 * @param numCounters: Number of counters
 * @param d: Pipeline depth
 */
class ScratchpadBundle(w: Int, m: Int, v: Int, numCounters: Int, d: Int, config: Option[ScratchpadConfig] = None) extends Bundle {
  var raSrc = if (config.isDefined) UInt(config.get.ra.src, width=1) else UInt(width=1)
  var raValue = if (config.isDefined) UInt(config.get.ra.value, width=log2Up(math.max(numCounters, d))) else UInt(width=log2Up(math.max(numCounters, d)))
  var waSrc = if (config.isDefined) UInt(config.get.wa.src, width=2) else UInt(width=2)
  var waValue = if (config.isDefined) UInt(config.get.wa.value, width=log2Up(math.max(numCounters, d))) else UInt(width=log2Up(math.max(numCounters, d)))
  var wdSrc = if (config.isDefined) UInt(config.get.wd, width=1) else UInt(width=1)
  var wen = if (config.isDefined) UInt(config.get.wen, width=log2Up(numCounters+1)) else UInt(width=log2Up(numCounters+1))

  override def cloneType(): this.type = {
    new ScratchpadBundle(w, m, v, numCounters, d, config).asInstanceOf[this.type]
  }
}

/**
 * Bundle to specify operand information to functional units
 * @param l: Number of local registers
 * @param r: Number of remote registers
 * @param w: Word width
 */
class OperandBundle(l: Int, r: Int, w: Int, config: Option[OperandConfig] = None) extends Bundle {
  var dataSrc = if (config.isDefined) UInt(config.get.dataSrc, width=3) else UInt(width=3)
  var value = if (config.isDefined) UInt(config.get.value, width=log2Up(w)) else UInt(width=log2Up(w))

  override def cloneType(): this.type = {
    new OperandBundle(l, r, w).asInstanceOf[this.type]
  }
}

/**
 * Bundle specifying mux configurations for opcode and operands of
 * one pipeline stage in the CU
 * @param l: Number of local registers
 * @param r: Number of remote registers
 * @param w: Word width
 */
class PipeStageBundle(l: Int, r: Int, w: Int, config: Option[PipeStageConfig] = None) extends Bundle {
  var opA = if (config.isDefined) new OperandBundle(l, r, w, Some(config.get.opA)) else new OperandBundle(l, r, w)
  var opB = if (config.isDefined) new OperandBundle(l, r, w, Some(config.get.opB)) else new OperandBundle(l, r, w)
  var opcode = if (config.isDefined) UInt(config.get.opcode, width=log2Up(Opcodes.size)) else UInt(width=log2Up(Opcodes.size))
  var result = if (config.isDefined) UInt(config.get.result, width=l+r) else UInt(width=l+r) // One-hot encoded
  var fwd = if (config.isDefined) {
    Vec.tabulate(r) { i => Bool(config.get.fwd.getOrElse(i, 0) > 0) }
    } else {
      Vec.tabulate(r) { i => Bool() }
    }

  override def cloneType(): this.type = {
    new PipeStageBundle(l,r,w).asInstanceOf[this.type]
  }
}

/**
 * ComputeUnit opcode format
 * @param w: Word width
 * @param d: Pipeline depth
 * @param rwStages: Read-write stages (at the beginning)
 * @param l: Number of local registers
 * @param r: Number of remote registers
 * @param m: Scratchpad size in words
 * @param v: Vector length
 * @param numCounters: Number of counters
 * @param numScratchpads: Number of scratchpads
 */
case class ComputeUnitOpcode(val w: Int, val d: Int, rwStages: Int, val l: Int, val r: Int, val m: Int, val v: Int, val numCounters: Int, val numScratchpads: Int, config: Option[ComputeUnitConfig] = None) extends OpcodeT {

  val scratchpads = Vec.tabulate(numScratchpads) { i =>
    if (config.isDefined) {
      new ScratchpadBundle(w, m, v, numCounters, d, Some(config.get.scratchpads(i)))
    } else {
      new ScratchpadBundle(w, m, v, numCounters, d, None)
    }
  }
  var pipeStage = Vec.tabulate(d) { i =>
    if (config.isDefined) {
      new PipeStageBundle(l, r, w, Some(config.get.pipeStage(i)))
    } else {
      new PipeStageBundle(l, r, w)
    }
  }

  override def cloneType(): this.type = {
    new ComputeUnitOpcode(w, d, rwStages, l, r, m, v, numCounters, numScratchpads, config).asInstanceOf[this.type]
  }
}

/**
 * Compute Unit module
 * @param w: Word width
 * @param startDelayWidth: Start delay width
 * @param endDelayWidth: End delay width
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param rwStages: Read-write stages (at the beginning)
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class ComputeUnit(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val d: Int, val v: Int, rwStages: Int, val numTokens: Int, val l: Int, val r: Int, val m: Int, inst: ComputeUnitConfig) extends ConfigurableModule[ComputeUnitOpcode] {

  // Currently, numCounters == numTokens
  val numCounters = numTokens

  val numScratchpads = 4 // TODO: Remove hardcoded number!
  val numReduceStages = log2Up(v)
  val numStagesAfterReduction = 2

  // Sanity check parameters for validity
  Predef.assert(d >= (rwStages),
    s"""#stages $d < read-write stages ($rwStages)!""")

  // #remoteRegs == numCounters + v in current impl
  Predef.assert(r >= (numCounters+numScratchpads),
    s"""#Unsupported number of remote registers $r; $r must be >= $numCounters + $numScratchpads (numCounters + numScratchpads))""")

  Predef.assert(d >= (numReduceStages + numStagesAfterReduction), s"Invalid number of stages ($d); must be >= numReduceStages + numStagesAfterReduction ($numReduceStages + $numStagesAfterReduction)")

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val tokenIns = Vec.fill(numTokens) { Bool(INPUT) }
    val tokenOuts = Vec.fill(numTokens) { Bool(OUTPUT) }

    /* Data interface: Vector of buses in, one bus out */
   val dataIn = Vec.fill(numScratchpads) { Vec.fill(v) { UInt(INPUT, w)} }
   val dataOut = Vec.fill(v) { UInt(OUTPUT, w)}
  }

  val configType = ComputeUnitOpcode(w, d, rwStages, l, r, m, v, numCounters, numScratchpads)
  val configIn = ComputeUnitOpcode(w, d, rwStages, l, r, m, v, numCounters, numScratchpads)
  val configInit = ComputeUnitOpcode(w, d, rwStages, l, r, m, v, numCounters, numScratchpads, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  // Crossbar across input buses
  val inputXbar = Module(new CrossbarVec(w, v, numScratchpads, numScratchpads, inst.dataInXbar))
  inputXbar.io.ins := io.dataIn
  val remoteWriteData = inputXbar.io.outs

  // Scratchpads
  val scratchpads = List.tabulate(numScratchpads) { i =>
    Module(new Scratchpad(w, m, v, inst.scratchpads(i)))
  }
  val waStagesMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(rwStages, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).waValue
    mux
  }
  val waCountersMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(numCounters, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).waValue
    mux
  }
  val waSrcMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(3, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).waSrc
    mux
  }
  val raStagesMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(rwStages, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).raValue
    mux
  }
  val raCountersMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(numCounters, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).raValue
    mux
  }
  val raSrcMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(2, v, log2Up(m)))
    mux.io.sel := config.scratchpads(i).raSrc
    mux
  }
  val wdMux = List.tabulate(numScratchpads) { i =>
    val mux = Module(new MuxVec(2,v,w))
    mux.io.sel := config.scratchpads(i).wdSrc
    mux
  }
  val wenMux = List.tabulate(numScratchpads) { i =>
    val mux = if (Globals.noModule) new MuxNL(numCounters+1, 1) else Module(new MuxN(numCounters+1, 1))
    mux.io.sel := config.scratchpads(i).wen
    mux
  }
  scratchpads.zipWithIndex.foreach { case (s, i) =>
    s.io.waddr := waSrcMux(i).io.out
    s.io.wdata := wdMux(i).io.out
    s.io.raddr := raSrcMux(i).io.out
    s.io.wen   := wenMux(i).io.out
  }
  val rdata = scratchpads.map { _.io.rdata }

  // CounterChain
  val counterChain = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst.counterChain))
  val counterEnables = Vec.fill(numCounters) { Bool() }
  counterChain.io.control.zipWithIndex.foreach { case (c,i) =>
   c.enable := counterEnables(i)
  }
  val counters = counterChain.io.data map { _.out }  // List of counters

  // Control block
  val controlBlock = Module(new CUControlBox(w, numCounters, inst.control))
  controlBlock.io.config_enable := io.config_enable
  controlBlock.io.tokenIns := io.tokenIns
  controlBlock.io.done := counterChain.io.control map { _.done}
  counterEnables := controlBlock.io.enable
  io.tokenOuts := controlBlock.io.tokenOuts

  // Empty pipe stage generation for each lane
  // Values passed in - Currently it is counters ++ first element of all dataIn buses
  val counterVecs = List.tabulate(v) { i =>
    counters.map { c => c + UInt(i) }
  }
  val initRegblocks = List.tabulate(v) { i =>
    val remotesList =  counterVecs(i) ++ io.dataIn.map { _(0) }
    val regBlock = Module(new RegisterBlock(w, 0 /*local*/,  r /*remote*/))
    regBlock.io.writeData := UInt(0) // Not connected to any ALU output
    regBlock.io.readLocalASel := UInt(0) // No local reads
    regBlock.io.readLocalBSel := UInt(0) // No local reads
    regBlock.io.passData.zipWithIndex.foreach { case (in, i) =>
      val input = if (i < remotesList.size) remotesList(i)
                  else UInt(0)
      in := input
    }
    regBlock
  }

  // TODO: Get counter maxs and strides from the first lane's empty stage
  counterChain.io.data.zipWithIndex.foreach { case (c, i) =>
    c.max    := UInt(0, w)
    c.stride := UInt(0,w)
  }

  // Pipe stages generation
  val pipeStages = ListBuffer[List[IntFU]]()
  val pipeRegs = ListBuffer[List[RegisterBlock]]()
  pipeRegs.append(initRegblocks)

  // Reduction stages
  val reduceStages = (0 until d).dropRight(numStagesAfterReduction).takeRight(numReduceStages)
  for (i <- 0 until d) {
    val stage = List.fill(v) { Module(new IntFU(w)) }
    val regblockStage = List.fill(v) { Module(new RegisterBlock(w, l, r)) }
    val stageConfig = config.pipeStage(i)

    // Reduction tree specific:
    // If this stage is one of the reduction stages, get its position within the reduce tree stages.
    val isReduceStage = reduceStages.contains(i)
    val reduceStageNum = reduceStages.indexOf(i)
    // Lanes in the current stage that need to be forwarded values from previous stage
    val reduceLanes = (0 until v by (1 << (reduceStageNum+1))).toList
    // Forward lanes for each lane
    val fwdLaneMap = new HashMap[Int, Int]()
    reduceLanes.foreach { r =>
      fwdLaneMap(r) = r + (1 << reduceStageNum)
    }
    println(s"stage $i: fwdLaneMap $fwdLaneMap")
    (0 until v) foreach { ii =>
      val fu = stage(ii)
      val regblock = regblockStage(ii)

      // Local and remote (previous pipe stage) registers
      val localA = regblock.io.readLocalA
      val rA = pipeRegs.last(ii).io.readRemoteA

      val dataSrcA = if (i <= rwStages) {
        // Forwarded memories
        val memAMux = if (Globals.noModule) new MuxNL(numScratchpads, w) else Module(new MuxN(numScratchpads, w))
        memAMux.io.ins := Vec(rdata map {_(ii)}) // Get the ii'th element from each Scratchpad's rdata vector
        memAMux.io.sel := stageConfig.opA.value

        if (i == rwStages) {
          Vec(localA, rA, stageConfig.opA.value, UInt(0, width=w), memAMux.io.out)
        } else {
          // Forwarded counters
          val counterAMux = if (Globals.noModule) new MuxNL(numCounters, w) else Module(new MuxN(numCounters, w))
          counterAMux.io.ins := Vec(counterVecs(ii))
          counterAMux.io.sel := stageConfig.opA.value
          Vec(localA, rA, stageConfig.opA.value, counterAMux.io.out, memAMux.io.out)
        }
      } else {
        Vec(localA, rA, stageConfig.opA.value)
      }
      val rB = pipeRegs.last(ii).io.readRemoteB
      val localB = regblock.io.readLocalB
      val dataSrcB = if (i <= rwStages) {
        // Forwarded memories
        val memBMux = if (Globals.noModule) new MuxNL(numScratchpads, w) else Module(new MuxN(numScratchpads, w))
        memBMux.io.ins := Vec(rdata map {_(ii)}) // Get the ii'th element from each Scratchpad's rdata vector
        memBMux.io.sel := stageConfig.opB.value

        if (i == rwStages) {
          Vec(localB, rB, stageConfig.opB.value, UInt(0, width=w), memBMux.io.out)
        } else {
          // Forwarded counters
          val counterBMux = if (Globals.noModule) new MuxNL(numCounters, w) else Module(new MuxN(numCounters, w))
          counterBMux.io.ins := Vec(counterVecs(ii))
          counterBMux.io.sel := stageConfig.opB.value
          Vec(localB, rB, stageConfig.opB.value, counterBMux.io.out, memBMux.io.out)
        }
      } else if (isReduceStage && fwdLaneMap.contains(ii)) {
        val prevRegBlock = pipeRegs.last(fwdLaneMap(ii))
        val reduceVal = prevRegBlock.io.passDataOut(0)
        Vec(localB, rB, stageConfig.opB.value, reduceVal)
      } else {
        Vec(localB, rB, stageConfig.opB.value)
      }

      val dataSrcAMux = if (Globals.noModule) new MuxNL(dataSrcA.size, w) else Module(new MuxN(dataSrcA.size, w))
      dataSrcAMux.io.ins := dataSrcA
      dataSrcAMux.io.sel := stageConfig.opA.dataSrc
      val dataSrcBMux = if (Globals.noModule) new MuxNL(dataSrcB.size, w) else Module(new MuxN(dataSrcB.size, w))
      dataSrcBMux.io.ins := dataSrcB
      dataSrcBMux.io.sel := stageConfig.opB.dataSrc

      val inA = dataSrcAMux.io.out
      val inB = dataSrcBMux.io.out
      fu.io.a := inA
      fu.io.b := inB
      fu.io.opcode := stageConfig.opcode
      regblock.io.writeData := fu.io.out

      // Produce pass data values from counters and regs for the first rwStages
      val passData = if (i < rwStages) {
        val passCounterMuxes = List.tabulate(numCounters) { j =>
          Mux(stageConfig.fwd(j), counterVecs(ii)(j), pipeRegs.last(ii).io.passDataOut(j))
        }
        val passScratchpadMuxes = List.tabulate(numScratchpads) { j =>
          Mux(stageConfig.fwd(numCounters+j), rdata(j)(ii), pipeRegs.last(ii).io.passDataOut(numCounters+j))
        }
        passCounterMuxes ++ passScratchpadMuxes ++ pipeRegs.last(ii).io.passDataOut.drop(numCounters+numScratchpads)
      } else {
        pipeRegs.last(ii).io.passDataOut
      }
      regblock.io.passData := passData
      regblock.io.writeSel := stageConfig.result
      regblock.io.readLocalASel := stageConfig.opA.value
      regblock.io.readLocalBSel := stageConfig.opB.value
      pipeRegs.last(ii).io.readRemoteASel := stageConfig.opA.value
      pipeRegs.last(ii).io.readRemoteBSel := stageConfig.opB.value
    }
    pipeStages.append(stage)
    pipeRegs.append(regblockStage)
  }

  def getStageOut(stage: List[IntFU]): Vec[Bits]  = {
    Vec.tabulate(v) { i => stage(i).io.out }
  }
  def getStageOut(i: Int) : Vec[Bits] = {
    getStageOut(pipeStages(i))
  }


  // Connect scratchpad raddr, waddr, wdata ports
  val rwStagesOut = pipeStages.take(rwStages) map { getStageOut(_) }
  val wStagesOut = getStageOut(pipeStages.last)
  val lastStageWaddr = Vec.tabulate(v) { i => pipeRegs.last(i).io.passDataOut(0) }  // r0 in last stage is local waddr
  val lastStageWdata = Vec.tabulate(v) { i => pipeRegs.last(i).io.passDataOut(1) }  // r1 in last stage in local wdata
//  val countersAsVecs = counters map { c => Vec.fill(v) {c} }  // TODO: Fix when vectorization is enabled

  // Wire stage outputs into scratchpad address and data
  val stridedCounters = counters.map { c =>
    Vec.tabulate(v) { i => c + UInt(i) }
  }

  (0 until numScratchpads) foreach { i =>
    raStagesMux(i).io.ins := Vec (rwStagesOut)
    raCountersMux(i).io.ins := Vec (stridedCounters)
    raSrcMux(i).io.ins := Vec(raStagesMux(i).io.out, raCountersMux(i).io.out)
    waStagesMux(i).io.ins := Vec(rwStagesOut)
    waCountersMux(i).io.ins := Vec(stridedCounters)
    waSrcMux(i).io.ins := Vec(waStagesMux(i).io.out, waCountersMux(i).io.out, lastStageWaddr)
    wdMux(i).io.ins(0) := lastStageWdata // local write data
    wdMux(i).io.ins(1) := remoteWriteData(0) // remote write data - currently pick only the first bus
    wenMux(i).io.ins.zipWithIndex.foreach { case(in, ii) =>
      if (ii == 0) in := UInt(0, width=1)
      else in := counterEnables(ii-1)
    }
  }

  io.dataOut := lastStageWdata
}

/**
 * ComputeUnit test harness
 */
class ComputeUnitTests(c: ComputeUnit) extends PlasticineTester(c) {
  var numCycles = 0

  while (numCycles < 100) {
    step(1)
    numCycles += 1
  }

  println(s"Done, design ran for $numCycles cycles")
}


object ComputeUnitTest {
  def main(args: Array[String]): Unit = {

    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl ComputeUnitTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[ComputeUnitConfig]]

    val bitwidth = 32
    val startDelayWidth = 4
    val endDelayWidth = 4
    val d = 10
    val v = 16
    val l = 0
    val r = 16
    val rwStages = 8
    val numTokens = 8
    val m = 64
    chiselMainTest(chiselArgs, () => Module(new ComputeUnit(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, configObj.config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
