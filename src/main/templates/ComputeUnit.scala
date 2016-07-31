package plasticine.templates

import Chisel._
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Bundle to specify operand information to functional units
 * @param l: Number of local registers
 * @param r: Number of remote registers
 */
class OperandBundle(l: Int, r: Int, config: Option[OperandConfig] = None) extends Bundle {
  var isLocal = if (config.isDefined) Bool(config.get.isLocal) else Bool()
  var regLocal = if (config.isDefined) UInt(config.get.regLocal, width=log2Up(l+r)) else UInt(width=log2Up(l+r))
  var regRemote = if (config.isDefined) UInt(config.get.regRemote, width=log2Up(r)) else UInt(width=log2Up(r))

  override def cloneType(): this.type = {
    new OperandBundle(l, r).asInstanceOf[this.type]
  }
}

/**
 * Bundle specifying mux configurations for opcode and operands of
 * one pipeline stage in the CU
 * @param l: Number of local registers
 * @param r: Number of remote registers
 */
class PipeStageBundle(l: Int, r: Int, config: Option[PipeStageConfig] = None) extends Bundle {
  var opA = if (config.isDefined) new OperandBundle(l, r, Some(config.get.opA)) else new OperandBundle(l, r)
  var opB = if (config.isDefined) new OperandBundle(l, r, Some(config.get.opB)) else new OperandBundle(l, r)
  var opcode = if (config.isDefined) UInt(config.get.opcode, width=4) else UInt(width=4) // TODO: Remove hardcoded number!
  var result = if (config.isDefined) UInt(config.get.result, width=l+r) else UInt(width=l+r) // One-hot encoded

  override def cloneType(): this.type = {
    new PipeStageBundle(l,r).asInstanceOf[this.type]
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
      new PipeStageBundle(l, r, Some(config.get.pipeStage(i)))
    } else {
      new PipeStageBundle(l, r)
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
 * @param rwStages: Read-write stages (at the beginning)
 * @param wStages: Write stages (at the end)
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class ComputeUnit(val w: Int, val d: Int, rwStages: Int, wStages: Int, val l: Int, val r: Int, val m: Int, inst: ComputeUnitConfig) extends ConfigurableModule[ComputeUnitOpcode] {

  // Sanity check parameters for validity
  Predef.assert(d >= (rwStages+wStages),
    s"""#stages $d < read-write stages ($rwStages) + write stages ($wStages)!""")

  val io = new ConfigInterface {
    val enable      = Bool(INPUT)
    val done        = Bool(OUTPUT)
    val scalarOut   = UInt(OUTPUT, w)
    val config_enable = Bool(INPUT)

    // Temporary for debugging
    val rmux0 = UInt(OUTPUT, 1)
    val rmux1 = UInt(OUTPUT, 1)
    val opcode = UInt(OUTPUT, w)
    val opA_isLocal = UInt(OUTPUT, 1)
    val opA_local = UInt(OUTPUT, w)
    val opA_remote = UInt(OUTPUT, w)
    val opB_isLocal = UInt(OUTPUT, 1)
    val opB_local = UInt(OUTPUT, w)
    val opB_remote = UInt(OUTPUT, w)
    val result = UInt(OUTPUT, w)
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
  val counterChain = Module(new CounterChain(w, inst.counterChain))
  // TODO: Getting this info from config temporarily
  counterChain.io.data.foreach { d =>
    d.max := UInt(0, w)
    d.stride := UInt(0,w)
  }
  counterChain.io.control.foreach { c =>
   c.enable := io.enable
  }
  val counters = counterChain.io.data map { _.out }

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

    val inA = Mux(config.pipeStage(i).opA.isLocal, localA, rA)
    val inB = Mux(config.pipeStage(i).opB.isLocal, localB, rB)
    fu.io.a := inA
    fu.io.b := inB
    fu.io.opcode := config.pipeStage(i).opcode
    regblock.io.writeData := fu.io.out
    val passData = if (i == 0) passDataIn else pipeRegs.last.io.passDataOut
    regblock.io.passData := passData
    regblock.io.writeSel := config.pipeStage(i).result
    regblock.io.readLocalASel := config.pipeStage(i).opA.regLocal
    regblock.io.readLocalBSel := config.pipeStage(i).opB.regLocal
    if (i > 0) {
      pipeRegs.last.io.readRemoteASel := config.pipeStage(i).opA.regRemote
      pipeRegs.last.io.readRemoteBSel := config.pipeStage(i).opB.regRemote
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
  io.done := counterChain.io.control(1).done

  // Temporary, for debugging
  io.rmux0       := config.remoteMux0
  io.rmux1       := config.remoteMux1
  io.opcode      := config.pipeStage(0).opcode
  io.opA_isLocal := config.pipeStage(0).opA.isLocal
  io.opA_local   := config.pipeStage(0).opA.regLocal
  io.opA_remote  := config.pipeStage(0).opA.regRemote
  io.opB_isLocal := config.pipeStage(0).opB.isLocal
  io.opB_local   := config.pipeStage(0).opB.regLocal
  io.opB_remote  := config.pipeStage(0).opB.regRemote
  io.result      := config.pipeStage(0).result
}

/**
 * ComputeUnit test harness
 */
class ComputeUnitTests(c: ComputeUnit) extends PlasticineTester(c) {
  var numCycles = 0

  poke(c.io.enable, 1)

  var done = peek(c.io.done)
  while (done != 1) {
    step(1)
    numCycles += 1
    done = peek(c.io.done)
//    peek(c.io.rmux0      )
//    peek(c.io.rmux1      )
//    peek(c.io.opcode     )
//    peek(c.io.opA_isLocal)
//    peek(c.io.opA_local  )
//    peek(c.io.opA_remote )
//    peek(c.io.opB_isLocal)
//    peek(c.io.opB_local  )
//    peek(c.io.opB_remote )
//    peek(c.io.result     )
    peek(c.io.scalarOut)
  }
  poke(c.io.enable, 0)

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
    val l = 2
    val r = 4
    val rwStages = 1
    val wStages = 1
    val m = 16
    chiselMainTest(chiselArgs, () => Module(new ComputeUnit(bitwidth, d, rwStages, wStages, l, r, m, configObj.config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
