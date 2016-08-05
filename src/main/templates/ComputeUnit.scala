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
  var remoteMux0 = if (config.isDefined) UInt(config.get.remoteMux0, width=2) else UInt(width=2)
  var remoteMux1 = if (config.isDefined) UInt(config.get.remoteMux1, width=2) else UInt(width=2)

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
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param rwStages: Read-write stages (at the beginning)
 * @param wStages: Write stages (at the end)
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class ComputeUnit(val w: Int, val d: Int, val v: Int, rwStages: Int, wStages: Int, val numTokens: Int, val l: Int, val r: Int, val m: Int, inst: ComputeUnitConfig) extends ConfigurableModule[ComputeUnitOpcode] {

  // Sanity check parameters for validity
  Predef.assert(d >= (rwStages+wStages),
    s"""#stages $d < read-write stages ($rwStages) + write stages ($wStages)!""")

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
  val mem0waMux = Module(new MuxN(rwStages+wStages, log2Up(m)))
  val mem0wdMux = Module(new MuxN(rwStages+wStages, w))
  val mem0raMux = Module(new MuxN(rwStages, log2Up(m)))
  mem0waMux.io.sel := config.mem0wa
  mem0wdMux.io.sel := config.mem0wd
  mem0raMux.io.sel := config.mem0ra
  mem0.io.waddr := mem0waMux.io.out
  mem0.io.wdata := mem0wdMux.io.out
  mem0.io.raddr := mem0raMux.io.out
  mem0.io.wen   := Bool(true)  // Temporary as we have no control flow

  val mem1 = Module(new SRAM(w, m))
  val mem1waMux = Module(new MuxN(rwStages+wStages, log2Up(m)))
  val mem1wdMux = Module(new MuxN(rwStages+wStages, w))
  val mem1raMux = Module(new MuxN(rwStages, log2Up(m)))
  mem1waMux.io.sel := config.mem1wa
  mem1wdMux.io.sel := config.mem1wd
  mem1raMux.io.sel := config.mem1ra
  mem1.io.waddr := mem1waMux.io.out
  mem1.io.wdata := mem1wdMux.io.out
  mem1.io.raddr := mem1raMux.io.out
  mem1.io.wen   := Bool(true)  // Temporary as we have no control flow

  // CounterChain
  val counterChain = Module(new CounterChain(w, 2, inst.counterChain)) // TODO: Hardcoded constant!
  // TODO: Getting this info from config temporarily
  counterChain.io.data.foreach { d =>
    d.max := UInt(0, w)
    d.stride := UInt(0,w)
  }
  val counterEnables = Vec.fill(2) { Bool() } // TODO: Hardcoded constant!
  counterChain.io.control.zipWithIndex.foreach { case (c,i) =>
   c.enable := counterEnables(i)
  }
  val counters = counterChain.io.data map { _.out }

  // Control block
  val controlBlock = Module(new CUControlBox(w, 2, inst.control)) // TODO: Hardcoded const!
  controlBlock.io.config_enable := io.config_enable
  controlBlock.io.tokenIns := io.tokenIns
  controlBlock.io.done := counterChain.io.control map { _.done}
  counterEnables := controlBlock.io.enable
  io.tokenOuts := controlBlock.io.tokenOuts

  // Remote MUXes to feed the two inputs
  val remotes0List =  counters ++ List(mem0.io.rdata)
  val remoteMux0 = Module(new MuxN(remotes0List.size, w))
  remoteMux0.io.ins := Vec(remotes0List)
  remoteMux0.io.sel := config.remoteMux0
  val remotes1List =  counters ++ List(mem1.io.rdata)
  val remoteMux1 = Module(new MuxN(remotes1List.size, w))
  remoteMux1.io.ins := Vec(remotes1List)
  remoteMux1.io.sel := config.remoteMux1

  // Values written to the first set of remote registers
  // Order is fixed by the list
  val passDataIn = counters ++ List(mem0.io.rdata, mem1.io.rdata)

  // Pipeline generation
  val fus = ListBuffer[IntFU]()
  val pipeRegs = ListBuffer[RegisterBlock]()
  for (i <- 0 until d) {
    val fu = Module(new IntFU(w))
    val regblock = Module(new RegisterBlock(w, l, r))

    val rA = if (i == 0) remoteMux0.io.out else pipeRegs.last.io.readRemoteA
    val rB = if (i == 0) remoteMux1.io.out else pipeRegs.last.io.readRemoteB
    val localA = regblock.io.readLocalA
    val localB = regblock.io.readLocalB

    val dataSrcA = if (i < rwStages) Vec(localA, rA, config.pipeStage(i).opA.value, mem0.io.rdata) else Vec(localA, rA, config.pipeStage(i).opA.value)
    val dataSrcAMux = Module(new MuxN(dataSrcA.size, w))
    dataSrcAMux.io.ins := dataSrcA
    dataSrcAMux.io.sel := config.pipeStage(i).opA.dataSrc
    val inA = dataSrcAMux.io.out

    val dataSrcB = if (i < rwStages) Vec(localB, rB, config.pipeStage(i).opB.value, mem1.io.rdata) else Vec(localB, rB, config.pipeStage(i).opB.value)
    val dataSrcBMux = Module(new MuxN(dataSrcB.size, w))
    dataSrcBMux.io.ins := dataSrcB
    dataSrcBMux.io.sel := config.pipeStage(i).opB.dataSrc
    val inB = dataSrcBMux.io.out
    fu.io.a := inA
    fu.io.b := inB
    fu.io.opcode := config.pipeStage(i).opcode
    regblock.io.writeData := fu.io.out
    val passData = if (i == 0) passDataIn else pipeRegs.last.io.passDataOut
    regblock.io.passData := passData
    regblock.io.writeSel := config.pipeStage(i).result
    regblock.io.readLocalASel := config.pipeStage(i).opA.value
    regblock.io.readLocalBSel := config.pipeStage(i).opB.value
    if (i > 0) {
      pipeRegs.last.io.readRemoteASel := config.pipeStage(i).opA.value
      pipeRegs.last.io.readRemoteBSel := config.pipeStage(i).opB.value
    }
    fus.append(fu)
    pipeRegs.append(regblock)
  }

  // Connect scratchpad raddr, waddr, wdata ports
  val rwStagesOut = fus.take(rwStages) map { _.io.out }
  val wStagesOut = fus.takeRight(wStages) map { _.io.out }
  mem0raMux.io.ins := Vec (rwStagesOut)
  mem0waMux.io.ins := Vec (rwStagesOut ++ wStagesOut)
  mem0wdMux.io.ins := Vec (rwStagesOut ++ wStagesOut)
  mem1raMux.io.ins := Vec (rwStagesOut)
  mem1waMux.io.ins := Vec (rwStagesOut ++ wStagesOut)
  mem1wdMux.io.ins := Vec (rwStagesOut ++ wStagesOut)

  io.scalarOut := fus.last.io.out
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
    val d = 2
    val v = 1
    val l = 2
    val r = 4
    val rwStages = 1
    val wStages = 1
    val numTokens = 2
    val m = 16
    chiselMainTest(chiselArgs, () => Module(new ComputeUnit(bitwidth, d, v, rwStages, wStages, numTokens, l, r, m, configObj.config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
