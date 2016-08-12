package plasticine.templates

import Chisel._
import plasticine.pisa.ir._

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
      Vec.tabulate(r) { i => UInt(config.get.fwd.getOrElse(i, 0), width=log2Up(r)) }
    } else {
      Vec.tabulate(r) { i => UInt(width=log2Up(r)) }
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
 * @param wStages: Write stages (at the end)
 * @param l: Number of local registers
 * @param r: Number of remote registers
 * @param m: Scratchpad size in words
 * @param v: Vector length
 * @param numCounters: Number of counters
 * @param numScratchpads: Number of scratchpads
 */
case class ComputeUnitOpcode(val w: Int, val d: Int, rwStages: Int, wStages: Int, val l: Int, val r: Int, val m: Int, val v: Int, val numCounters: Int, val numScratchpads: Int, config: Option[ComputeUnitConfig] = None) extends OpcodeT {

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
    new ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, v, numCounters, numScratchpads, config).asInstanceOf[this.type]
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
 * @param wStages: Write stages (at the end)
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class ComputeUnit(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val d: Int, val v: Int, rwStages: Int, wStages: Int, val numTokens: Int, val l: Int, val r: Int, val m: Int, inst: ComputeUnitConfig) extends ConfigurableModule[ComputeUnitOpcode] {

  // Currently, numCounters == numTokens
  val numCounters = numTokens

  val numScratchpads = 2 // TODO: Remove hardcoded number!

  // Sanity check parameters for validity
  Predef.assert(d >= (rwStages+wStages),
    s"""#stages $d < read-write stages ($rwStages) + write stages ($wStages)!""")

  // #remoteRegs == numCounters + v in current impl
  Predef.assert(r > (numCounters+numScratchpads),
    s"""#Unsupported number of remote registers $r; $r must be >= $numCounters + $numScratchpads (numCounters + numScratchpads))""")

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val tokenIns = Vec.fill(numTokens) { Bool(INPUT) }
    val tokenOuts = Vec.fill(numTokens) { Bool(OUTPUT) }

    /* Data interface: Vector of buses in, one bus out */
   val dataIn = Vec.fill(numScratchpads) { Vec.fill(v) { UInt(INPUT, w)} }
   val dataOut = Vec.fill(v) { UInt(OUTPUT, w)}
  }

  val configType = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, v, numCounters, numScratchpads)
  val configIn = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, v, numCounters, numScratchpads)
  val configInit = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, v, numCounters, numScratchpads, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  // Crossbar across input buses
  val inputXbar = Module(new CrossbarVec(w, v, numScratchpads, numScratchpads, inst.dataInXbar))
  inputXbar.io.ins := io.dataIn
  val remoteWriteData = inputXbar.io.outs

  // Scratchpads. TODO: Replace hardcoded logic with numScratchpads
  val mem0 = Module(new SRAM(w, m))
  val (mem0waStagesMux, mem0waCountersMux, mem0waSrcMux) = (
                                              Module(new MuxVec(rwStages, v, log2Up(m))),
                                              Module(new MuxVec(numCounters, v, log2Up(m))),
                                              Module(new MuxVec(3, v, log2Up(m)))
                                            )
  mem0waStagesMux.io.sel := config.scratchpads(0).waValue
  mem0waCountersMux.io.sel := config.scratchpads(0).waValue
  mem0waSrcMux.io.sel := config.scratchpads(0).waSrc

  val (mem0raStagesMux, mem0raCountersMux, mem0raSrcMux) = (
      Module(new MuxVec(rwStages, v, log2Up(m))),
      Module(new MuxVec(numCounters, v, log2Up(m))),
      Module(new MuxVec(2, v, log2Up(m)))
    )
  mem0raStagesMux.io.sel := config.scratchpads(0).raValue
  mem0raCountersMux.io.sel := config.scratchpads(0).raValue
  mem0raSrcMux.io.sel := config.scratchpads(0).raSrc

  val mem0wdMux = Module(new MuxVec(2, v, w)) // TODO: Number of dataIns must be equal to scratchpads
  mem0wdMux.io.sel := config.scratchpads(0).wdSrc
  val mem0wenMux = Module(new MuxN(numCounters+1, 1))
  mem0wenMux.io.sel := config.scratchpads(0).wen

  mem0.io.waddr := mem0waSrcMux.io.out(0)
  mem0.io.wdata := mem0wdMux.io.out(0)
  mem0.io.raddr := mem0raSrcMux.io.out(0)
  mem0.io.wen   := mem0wenMux.io.out  // TODO: 'wen' should be driven by a counter enable

  val mem1 = Module(new SRAM(w, m))
  val (mem1waStagesMux, mem1waCountersMux, mem1waSrcMux) = (
                                              Module(new MuxVec(rwStages, v, log2Up(m))),
                                              Module(new MuxVec(numCounters, v, log2Up(m))),
                                              Module(new MuxVec(3, v, log2Up(m)))
                                            )
  mem1waStagesMux.io.sel := config.scratchpads(0).waValue
  mem1waCountersMux.io.sel := config.scratchpads(0).waValue
  mem1waSrcMux.io.sel := config.scratchpads(0).waSrc

  val (mem1raStagesMux, mem1raCountersMux, mem1raSrcMux) = (
      Module(new MuxVec(rwStages, v, log2Up(m))),
      Module(new MuxVec(numCounters, v, log2Up(m))),
      Module(new MuxVec(2, v, log2Up(m)))
    )
  mem1raStagesMux.io.sel := config.scratchpads(0).raValue
  mem1raCountersMux.io.sel := config.scratchpads(0).raValue
  mem1raSrcMux.io.sel := config.scratchpads(0).raSrc

  val mem1wdMux = Module(new MuxVec(2, v, w)) // TODO: Number of dataIns must be equal to scratchpads
  mem1wdMux.io.sel := config.scratchpads(0).wdSrc
  val mem1wenMux = Module(new MuxN(numCounters+1, 1))
  mem1wenMux.io.sel := config.scratchpads(1).wen

  mem1.io.waddr := mem1waSrcMux.io.out(0)
  mem1.io.wdata := mem1wdMux.io.out(0)
  mem1.io.raddr := mem1raSrcMux.io.out(0)
  mem1.io.wen   := mem1wenMux.io.out
  val rdata = List(mem0.io.rdata, mem1.io.rdata)

  // CounterChain
  val counterChain = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst.counterChain))
  val counterEnables = Vec.fill(numCounters) { Bool() }
  counterChain.io.control.zipWithIndex.foreach { case (c,i) =>
   c.enable := counterEnables(i)
  }
  val counters = counterChain.io.data map { _.out }

  // Control block
  val controlBlock = Module(new CUControlBox(w, numCounters, inst.control)) // TODO: Hardcoded const!
  controlBlock.io.config_enable := io.config_enable
  controlBlock.io.tokenIns := io.tokenIns
  controlBlock.io.done := counterChain.io.control map { _.done}
  counterEnables := controlBlock.io.enable
  io.tokenOuts := controlBlock.io.tokenOuts

  // Empty pipe stage generation for each lane
  // Values passed in - Currently it is counters ++ first element of all dataIn buses
  val remotesList =  counters ++ io.dataIn.map { _(0) }
  val initRegblocks = List.fill(v) {
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

  // TODO: Get this from the first lane's empty stage
  counterChain.io.data.zipWithIndex.foreach { case (c, i) =>
    c.max    := UInt(0, w)
    c.stride := UInt(0,w)
  }

  // Pipe stages generation
  val pipeStages = ListBuffer[List[IntFU]]()
  val pipeRegs = ListBuffer[List[RegisterBlock]]()
  pipeRegs.append(initRegblocks)
  for (i <- 0 until d) {
    val stage = List.fill(v) { Module(new IntFU(w)) }
    val regblockStage = List.fill(v) { Module(new RegisterBlock(w, l, r)) }
    val stageConfig = config.pipeStage(i)
    (0 until v) foreach { ii =>
      val fu = stage(ii)
      val regblock = regblockStage(ii)

      // Local and remote (previous pipe stage) registers
      val localA = regblock.io.readLocalA
      val rA = pipeRegs.last(ii).io.readRemoteA

      val dataSrcA = if (i <= rwStages) {
        // Forwarded memories
        val memAMux = Module(new MuxN(numScratchpads, w))
        memAMux.io.ins := Vec(rdata)
        memAMux.io.sel := stageConfig.opA.value

        if (i == rwStages) {
          Vec(localA, rA, stageConfig.opA.value, UInt(0, width=w), memAMux.io.out)
        } else {
          // Forwarded counters
          val counterAMux = Module(new MuxN(numCounters, w))
          counterAMux.io.ins := Vec(counters)
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
        val memBMux = Module(new MuxN(numScratchpads, w))
        memBMux.io.ins := Vec(rdata)
        memBMux.io.sel := stageConfig.opB.value

        if (i == rwStages) {
          Vec(localB, rB, stageConfig.opB.value, UInt(0, width=w), memBMux.io.out)
        } else {
          // Forwarded counters
          val counterBMux = Module(new MuxN(numCounters, w))
          counterBMux.io.ins := Vec(counters)
          counterBMux.io.sel := stageConfig.opB.value
          Vec(localB, rB, stageConfig.opB.value, counterBMux.io.out, memBMux.io.out)
        }
      } else {
        Vec(localB, rB, stageConfig.opB.value)
      }

      val dataSrcAMux = Module(new MuxN(dataSrcA.size, w))
      dataSrcAMux.io.ins := dataSrcA
      dataSrcAMux.io.sel := stageConfig.opA.dataSrc
      val dataSrcBMux = Module(new MuxN(dataSrcB.size, w))
      dataSrcBMux.io.ins := dataSrcB
      dataSrcBMux.io.sel := stageConfig.opB.dataSrc

      val inA = dataSrcAMux.io.out
      val inB = dataSrcBMux.io.out
      fu.io.a := inA
      fu.io.b := inB
      fu.io.opcode := stageConfig.opcode
      regblock.io.writeData := fu.io.out

      val passData = pipeRegs.last(ii).io.passDataOut
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
  val wStagesOut = pipeStages.takeRight(wStages) map { getStageOut(_) }
  val lastStageWaddr = Vec.tabulate(v) { i => pipeRegs.last(i).io.passDataOut(0) }  // r0 in last stage is local waddr
  val lastStageWdata = Vec.tabulate(v) { i => pipeRegs.last(i).io.passDataOut(1) }  // r1 in last stage in local wdata
  val countersAsVecs = counters map { c => Vec.fill(v) {c} }  // TODO: Fix when vectorization is enabled

  mem0raStagesMux.io.ins := Vec (rwStagesOut)
  mem0raCountersMux.io.ins := Vec (countersAsVecs)
  mem0raSrcMux.io.ins := Vec(mem0raStagesMux.io.out, mem0raCountersMux.io.out)
  mem0waStagesMux.io.ins := Vec(rwStagesOut)
  mem0waCountersMux.io.ins := Vec(countersAsVecs)
  mem0waSrcMux.io.ins := Vec(mem0waStagesMux.io.out, mem0waCountersMux.io.out, lastStageWaddr)
  mem0wdMux.io.ins(0) := lastStageWdata
  mem0wdMux.io.ins(1) := remoteWriteData(0)
  mem0wenMux.io.ins.zipWithIndex.foreach { case(in, i) =>
    if (i == 0) in := UInt(0, width=1)
    else in := counterEnables(i-1)
  }

  mem1raStagesMux.io.ins := Vec (rwStagesOut)
  mem1raCountersMux.io.ins := Vec (countersAsVecs)
  mem1raSrcMux.io.ins := Vec(mem1raStagesMux.io.out, mem1raCountersMux.io.out)
  mem1waStagesMux.io.ins := Vec(rwStagesOut)
  mem1waCountersMux.io.ins := Vec(countersAsVecs)
  mem1waSrcMux.io.ins := Vec(mem1waStagesMux.io.out, mem1waCountersMux.io.out, lastStageWaddr)
  mem1wdMux.io.ins(0) := lastStageWdata
  mem1wdMux.io.ins(1) := remoteWriteData(1)
  mem1wenMux.io.ins.zipWithIndex.foreach { case(in, i) =>
    if (i == 0) in := UInt(0, width=1)
    else in := counterEnables(i-1)
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
    val rwStages = 3
    val wStages = 1
    val numTokens = 4
    val m = 64
    chiselMainTest(chiselArgs, () => Module(new ComputeUnit(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, wStages, numTokens, l, r, m, configObj.config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
