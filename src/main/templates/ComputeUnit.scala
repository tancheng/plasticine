package plasticine.templates

import Chisel._

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
 * @param l: Number of local registers
 * @param r: Number of remote registers
 */
case class ComputeUnitOpcode(val w: Int, val d: Int, val l: Int, val r: Int, config: Option[ComputeUnitConfig] = None) extends OpcodeT {
  var remoteMux0 = if (config.isDefined) UInt(config.get.remoteMux0, width=1) else UInt(width=1)
  var remoteMux1 = if (config.isDefined) UInt(config.get.remoteMux1, width=1) else UInt(width=1)

  var pipeStage = Vec.tabulate(d) { i =>
    if (config.isDefined) {
      new PipeStageBundle(l, r, Some(config.get.pipeStage(i)))
    } else {
      new PipeStageBundle(l, r)
    }
  }

  override def cloneType(): this.type = {
    new ComputeUnitOpcode(w, d, l, r, config).asInstanceOf[this.type]
  }
}

/**
 * Parsed configuration information for ComputeUnit
 */
class OperandConfig(config: HashMap[String, Any]) {
  private var _isLocal = config("isLocal").asInstanceOf[Boolean]
  def isLocal() = _isLocal
  def isLocal_=(x: Boolean) { _isLocal = x }

  private var _regLocal = config("regLocal").asInstanceOf[Int]
  def regLocal() = _regLocal
  def regLocal_=(x: Int) { _regLocal = x }

  private var _regRemote = config("regRemote").asInstanceOf[Int]
  def regRemote() = _regRemote
  def regRemote_=(x: Int) { _regRemote = x }

  override def toString = {
    s"isLocal: $isLocal\n" +
    s"regLocal: $regLocal\n" +
    s"regRemote: $regRemote"
  }
}

class PipeStageConfig(config: HashMap[String, Any]) {
  private var _opA = new OperandConfig(config("opA").asInstanceOf[HashMap[String, Any]])
  def opA() = _opA
  def opA_=(x: OperandConfig) { _opA = x }

  private var _opB = new OperandConfig(config("opB").asInstanceOf[HashMap[String, Any]])
  def opB() = _opB
  def opB_=(x: OperandConfig) { _opB = x }

  private var _opcode = config("opcode").asInstanceOf[Int]
  def opcode() = _opcode
  def opcode_=(x: Int) { _opcode = x }

  private var _result = config("result").asInstanceOf[Int]
  def result() = _result
  def result_=(x: Int) { _result = x }

  override def toString = {
    s"opA:\n ${opA.toString}" + "\n" +
    s"opB:\n ${opB.toString}" + "\n" +
    s"opcode: $opcode" + "\n" +
    s"result: $result"
  }
}

class ComputeUnitConfig(config: HashMap[String, Any]) {
  /* CounterChain config */
  private var _counterChain = new CounterChainConfig(config("counterChain").asInstanceOf[HashMap[String, Any]])
  def counterChain() = _counterChain
  def counterChain_=(x: CounterChainConfig) { _counterChain = x }

  /* Remote mux configs */
  private var _remoteMux0 = config("remoteMux0").asInstanceOf[Int]
  def remoteMux0() = _remoteMux0
  def remoteMux0_=(x: Int) { _remoteMux0 = x }

  private var _remoteMux1 = config("remoteMux1").asInstanceOf[Int]
  def remoteMux1() = _remoteMux1
  def remoteMux1_=(x: Int) { _remoteMux1 = x }

  /* Pipe stages config */
  private var _pipeStage = config("pipeStage")
                            .asInstanceOf[Seq[HashMap[String, Any]]]
                            .map { h => new PipeStageConfig(h) }
  def pipeStage() = _pipeStage
  def pipeStage(i: Int) = _pipeStage(i)
  def pipeStage_=(x: Seq[PipeStageConfig]) { _pipeStage = x }

  override def toString = {
    s"remoteMux0: $remoteMux0" + "\n" +
    s"remoteMux1: $remoteMux1" + "\n" +
    s"pipeStage:\n" +
    pipeStage.map { _.toString }.reduce {_+_}
  }
}

/**
 * Compute Unit module
 * @param w: Word width
 * @param d: Pipeline depth
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 */
class ComputeUnit(val w: Int, val d: Int, val l: Int, val r: Int, inst: ComputeUnitConfig) extends ConfigurableModule[ComputeUnitOpcode] {
  val io = new ConfigInterface {
    val enable      = Bool(INPUT)
    val done        = Bool(OUTPUT)
    val scalarOut   = UInt(OUTPUT, w)
    val config_enable = Bool(INPUT)
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

  val configType = ComputeUnitOpcode(w, d, l, r)
  val configIn = ComputeUnitOpcode(w, d, l, r)
  val configInit = ComputeUnitOpcode(w, d, l, r, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }


  /** CounterChain */
  val counterChain = Module(new CounterChain(w, inst.counterChain))
  // TODO: Getting this info from config temporarily
  counterChain.io.data.foreach { d =>
    d.max := UInt(0, w)
    d.stride := UInt(0,w)
  }
  counterChain.io.control.foreach { c =>
   c.enable := io.enable
  }

  val remotes = Vec.tabulate(counterChain.io.data.size) { i => counterChain.io.data(i).out }
  val remoteMux0 = Module(new MuxN(remotes.size, w))
  remoteMux0.io.ins := remotes
  remoteMux0.io.sel := config.remoteMux0

  val remoteMux1 = Module(new MuxN(remotes.size, w))
  remoteMux1.io.ins := remotes
  remoteMux1.io.sel := config.remoteMux1

  val passValues = remotes

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
    val passData = if (i == 0) remotes else pipeRegs.last.io.passDataOut
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

    val bitwidth = 7
    val d = 2
    val l = 2
    val r = 2
    // Counter chain
    val chainInst = HashMap[String, Any]("chain" -> 0)
    val countersInst = (0 until 2) map { i =>
      HashMap[String, Int]("max" -> 5, "stride" -> 1, "maxConst" -> 1, "strideConst" -> 1)
    }
    chainInst("counters") = countersInst

    val cuConfig = HashMap[String, Any]("counterChain" -> chainInst)

    // Remote muxes
    cuConfig("remoteMux0") = 0
    cuConfig("remoteMux1") = 1

    // Pipeline stages
    val pipeStage0 = HashMap[String, Any](
      "opA" -> HashMap[String, Any]("isLocal" -> false, "regLocal" -> 0, "regRemote" -> 0),
      "opB" -> HashMap[String, Any]("isLocal" -> false, "regLocal" -> 0, "regRemote" -> 1),
      "opcode" -> 2,
      "result" -> 8 // One-hot encoded
    )
    val pipeStage1 = HashMap[String, Any](
      "opA" -> HashMap[String, Any]("isLocal" -> false, "regLocal" -> 0, "regRemote" -> 1),
      "opB" -> HashMap[String, Any]("isLocal" -> true, "regLocal" -> 1, "regRemote" -> 0),
      "opcode" -> 0,
      "result" -> 2
    )

    cuConfig("pipeStage") = Seq(pipeStage0, pipeStage1)

    val config = new ComputeUnitConfig(cuConfig)
    println(s"config = $config")
    chiselMainTest(args, () => Module(new ComputeUnit(bitwidth, d, l, r, config))) {
      c => new ComputeUnitTests(c)
    }
  }
}
