package plasticine.pisa.ir

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.mutable.HashMap

/**
 * Parsed config information for a single counter
 */
class CounterRCConfig(config: HashMap[String, Any]) {
  /** Counter max */
  private var _max: Int = 0
  def max() = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = 0
  def stride() = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = 0
  def maxConst() = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = 0
  def strideConst() = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  // Construct the class here
  _max = config("max").asInstanceOf[Int]
  _stride = config("stride").asInstanceOf[Int]
  _maxConst = config("maxConst").asInstanceOf[Int]
  _strideConst = config("strideConst").asInstanceOf[Int]
}


/**
 * CounterChain config information
 */
class CounterChainConfig(config: HashMap[String, Any]) {
  // To chain or not?
  private var _chain: Int = 0
  def chain() = _chain
  def chain_=(x: Int) { _chain = x }

  // Configuration for individual counters
  private var _counters: Seq[CounterRCConfig] = Seq[CounterRCConfig]()
  def counters() = _counters
  def counters_=(x: Seq[CounterRCConfig]) { _counters = x }

  // Construct class here
  _chain = config("chain").asInstanceOf[Int]
  _counters = config("counters")
                .asInstanceOf[Seq[HashMap[String, Any]]]
                .map { h => new CounterRCConfig(h) }
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
