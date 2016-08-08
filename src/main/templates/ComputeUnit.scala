package plasticine.templates

import Chisel._
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Bundle to specify operand information to functional units
 * @param l: Number of local registers
 * @param r: Number of remote registers
 * @param w: Word width
 */
class OperandBundle(l: Int, r: Int, w: Int, config: Option[OperandConfig] = None) extends Bundle {
  var dataSrc = if (config.isDefined) UInt(config.get.dataSrc, width=2) else UInt(width=2)
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
      Vec.tabulate(r) { i => UInt(config.get.fwd(i), width=log2Up(r)) }
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
 */
case class ComputeUnitOpcode(val w: Int, val d: Int, rwStages: Int, wStages: Int, val l: Int, val r: Int, val m: Int, config: Option[ComputeUnitConfig] = None) extends OpcodeT {

  var mem0wa = if (config.isDefined) UInt(config.get.mem0wa, width=1) else UInt(width=1)
  var mem0wd = if (config.isDefined) UInt(config.get.mem0wd, width=1) else UInt(width=1)
  var mem0ra = if (config.isDefined) UInt(config.get.mem0ra, width=1) else UInt(width=1)

  var mem1wa = if (config.isDefined) UInt(config.get.mem1wa, width=1) else UInt(width=1)
  var mem1wd = if (config.isDefined) UInt(config.get.mem1wd, width=1) else UInt(width=1)
  var mem1ra = if (config.isDefined) UInt(config.get.mem1ra, width=1) else UInt(width=1)

  var pipeStage = Vec.tabulate(d) { i =>
    if (config.isDefined) {
      new PipeStageBundle(l, r, w, Some(config.get.pipeStage(i)))
    } else {
      new PipeStageBundle(l, r, w)
    }
  }

  override def cloneType(): this.type = {
    new ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, config).asInstanceOf[this.type]
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
  Predef.assert(r > (numCounters+v),
    s"""#Unsupported number of remote registers $r; $r must be >= $numCounters (numCounters) + $v (vector width)""")

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val tokenIns = Vec.fill(numTokens) { Bool(INPUT) }
    val tokenOuts = Vec.fill(numTokens) { Bool(OUTPUT) }
    val scalarOut   = UInt(OUTPUT, w)

    /* Data interface */
   val dataIn = Vec.fill(v) { UInt(INPUT, w)}
   val dataOut = Vec.fill(v) { UInt(OUTPUT, w)}
  }

  val configType = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m)
  val configIn = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m)
  val configInit = ComputeUnitOpcode(w, d, rwStages, wStages, l, r, m, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  // Scratchpads
  val mem0 = Module(new SRAM(w, m))
  val mem0waMux = Module(new MuxVec(rwStages+wStages, v, log2Up(m)))
  val mem0wdMux = Module(new MuxVec(2, v, w))
  val mem0raMux = Module(new MuxVec(rwStages, v, log2Up(m)))
  mem0waMux.io.sel := config.mem0wa
  mem0wdMux.io.sel := config.mem0wd
  mem0raMux.io.sel := config.mem0ra
  mem0.io.waddr := mem0waMux.io.out(0)
  mem0.io.wdata := mem0wdMux.io.out(0)
  mem0.io.raddr := mem0raMux.io.out(0)
  mem0.io.wen   := Bool(true)  // Temporary as we have no control flow

  val mem1 = Module(new SRAM(w, m))
  val mem1waMux = Module(new MuxVec(rwStages+wStages, v, log2Up(m)))
  val mem1wdMux = Module(new MuxVec(2, v, w))
  val mem1raMux = Module(new MuxVec(rwStages, v, log2Up(m)))
  mem1waMux.io.sel := config.mem1wa
  mem1wdMux.io.sel := config.mem1wd
  mem1raMux.io.sel := config.mem1ra
  mem1.io.waddr := mem1waMux.io.out(0)
  mem1.io.wdata := mem1wdMux.io.out(0)
  mem1.io.raddr := mem1raMux.io.out(0)
  mem1.io.wen   := Bool(true)  // Temporary as we have no control flow
  val rdata = List(mem0.io.rdata, mem1.io.rdata)

  // CounterChain
  val counterChain = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst.counterChain)) // TODO: Hardcoded constant!
  // TODO: Getting this info from config temporarily
  counterChain.io.data.foreach { d =>
    d.max := UInt(0, w)
    d.stride := UInt(0,w)
  }
  val counterEnables = Vec.fill(numCounters) { Bool() } // TODO: Hardcoded constant!
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

  // Values passed in - Currently it is counters ++ all values from input bus
  val remotesList =  counters ++ io.dataIn
  // Initial register blocks for each lane
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

  // Pipeline generation
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

  val dataIn = Vec.tabulate(v) { i => io.dataIn(i) }

  // Connect scratchpad raddr, waddr, wdata ports
  val rwStagesOut = pipeStages.take(rwStages) map { getStageOut(_) }
  val wStagesOut = pipeStages.takeRight(wStages) map { getStageOut(_) }
  mem0raMux.io.ins := Vec (rwStagesOut)
  mem0waMux.io.ins := Vec (rwStagesOut ++ wStagesOut)
  mem0wdMux.io.ins(0) := getStageOut(pipeStages.last)
  mem0wdMux.io.ins(1) := io.dataIn
  mem1raMux.io.ins := Vec (rwStagesOut)
  mem1waMux.io.ins := Vec (rwStagesOut ++ wStagesOut)
  mem1wdMux.io.ins(0) := getStageOut(pipeStages.last)
  mem1wdMux.io.ins(1) := io.dataIn

  io.scalarOut := getStageOut(pipeStages.last)(0)
  io.dataOut := getStageOut(pipeStages.last)
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

    val bitwidth = 7
    val startDelayWidth = 4
    val endDelayWidth = 4
    val d = 2
    val v = 1
    val l = 1
    val r = 4
    val rwStages = 1
    val wStages = 1
    val numTokens = 2
    val m = 16
    chiselMainTest(chiselArgs, () => Module(new ComputeUnit(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, wStages, numTokens, l, r, m, configObj.config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
