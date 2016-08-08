package plasticine.pisa.ir

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes

object Config {
  def apply(path: String) = Parser(path)
}

class Config[T<:AbstractConfig](val config: T) {
  private var _v: Double = 0
  def v = _v
  def v_=(x: Double) = { _v = x }

  override def toString = {
s"""PISA version: $v
config: $config"""
  }
}

/**
 * Base class for all Config nodes
 */
abstract class AbstractConfig {
  protected def encodeOneHot(x: Int) = 1 << x
  protected def getRegNum(s: String) = if (s.size <= 1) 0 else s.drop(1).toInt
}

/**
 * Parsed config information for a single counter
 */
case class CounterRCConfig(config: Map[Any, Any]) extends AbstractConfig {
  /** Counter max */
  private var _max: Int = Parser.getFieldInt(config, "max")
  def max = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = Parser.getFieldInt(config, "stride")
  def stride = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = Parser.getFieldInt(config, "maxConst")
  def maxConst = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = Parser.getFieldInt(config, "strideConst")
  def strideConst = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  /** Delay the start of counter */
  private var _startDelay: Int = 1 + Parser.getFieldInt(config, "startDelay")
  def startDelay = _startDelay
  def startDelay_=(x: Int) { _startDelay = x }

  /** Delay raising the counter 'done' signal */
  private var _endDelay: Int = 1 + Parser.getFieldInt(config, "endDelay")
  def endDelay = _endDelay
  def endDelay_=(x: Int) { _endDelay = x }
}


/**
 * CounterChain config information
 */
case class CounterChainConfig(config: Map[Any, Any]) extends AbstractConfig {
  // To chain or not?
  private var _chain: List[Int] = Parser.getFieldList(config, "chain")
                                        .asInstanceOf[List[Double]]
                                        .map { i => i.toInt }

  def chain = _chain
  def chain_=(x: List[Int]) { _chain = x }

  // Configuration for individual counters
  private var _counters: Seq[CounterRCConfig] = Parser.getFieldListOfMaps(config, "counters")
                                                      .map { h => new CounterRCConfig(h) }
  def counters = _counters
  def counters_=(x: Seq[CounterRCConfig]) { _counters = x }
}


/**
 * Parsed configuration information for ComputeUnit
 */
case class OperandConfig(config: String) extends AbstractConfig {
  private def getDataSrc = config(0) match {
    case 'l' => 0 // Local register
    case 'r' => 1 // Remote register
    case 'c' => 2 // Constant
    case 'i' => 3 // Iterator / counter
    case 'm' => 4 // Memory
    case _ => throw new Exception(s"Unknown data source '${config(0)}'. Must be l, r, c, i, or m")
  }
  private var _dataSrc = getDataSrc
  def dataSrc() = _dataSrc
  def dataSrc_=(x: Int) { _dataSrc = x }

  private var _value = getRegNum(config)
  def value() = _value
  def value_=(x: Int) { _value = x }

  override def toString = {
    s"dataSrc: $dataSrc\n" +
    s"value: $value\n"
  }
}

case class PipeStageConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _opA = new OperandConfig(Parser.getFieldString(config, "opA"))
  def opA() = _opA
  def opA_=(x: OperandConfig) { _opA = x }

  private var _opB = new OperandConfig(Parser.getFieldString(config, "opB"))
  def opB() = _opB
  def opB_=(x: OperandConfig) { _opB = x }

  private var _opcode = Opcodes.getCode(Parser.getFieldString(config, "opcode"))
  def opcode() = _opcode
  def opcode_=(x: Int) { _opcode = x }

  private var _result = encodeOneHot(getRegNum(Parser.getFieldString(config, "result")))
  def result() = _result
  def result_=(x: Int) { _result = x }

  // TODO: Remove hardcoded constant!
  val r = 4
  private var _fwd: List[Int] = {
    val m = Parser.getFieldMap(config, "fwd")
    List.tabulate(r) { i =>
      val regName = s"r$i"
      if (m.contains(regName)) {
        m(regName) match {
          case "counter" => 1
          case "memory" => 1
          case _ => throw new Exception(s"Unknown forward source location ${m(regName)}. It must either be 'counter' or 'memory'")
        }
      } else 0
    }
  }
  def fwd = _fwd
  def fwd_=(x: List[Int]) { _fwd = x }


  override def toString = {
    s"opA:\n ${opA.toString}" + "\n" +
    s"opB:\n ${opB.toString}" + "\n" +
    s"opcode: $opcode" + "\n" +
    s"result: $result"
  }
}

case class ComputeUnitConfig(config: Map[Any, Any]) extends AbstractConfig {
  /* CounterChain config */
  private var _counterChain = new CounterChainConfig(
    Parser.getFieldMap(config, "counterChain"))
  def counterChain() = _counterChain
  def counterChain_=(x: CounterChainConfig) { _counterChain = x }

  private var _mem0wa = Parser.getFieldInt(config, "mem0wa")
  def mem0wa() = _mem0wa
  def mem0wa_=(x: Int) { _mem0wa = x }

  private var _mem0wd = Parser.getFieldInt(config, "mem0wd")
  def mem0wd() = _mem0wd
  def mem0wd_=(x: Int) { _mem0wd = x }

  private var _mem0ra = Parser.getFieldInt(config, "mem0ra")
  def mem0ra() = _mem0ra
  def mem0ra_=(x: Int) { _mem0ra = x }

  private var _mem1wa = Parser.getFieldInt(config, "mem1wa")
  def mem1wa() = _mem1wa
  def mem1wa_=(x: Int) { _mem1wa = x }

  private var _mem1wd = Parser.getFieldInt(config, "mem1wd")
  def mem1wd() = _mem1wd
  def mem1wd_=(x: Int) { _mem1wd = x }

  private var _mem1ra = Parser.getFieldInt(config, "mem1ra")
  def mem1ra() = _mem1ra
  def mem1ra_=(x: Int) { _mem1ra = x }

  /* Pipe stages config */
  private var _pipeStage = Parser.getFieldListOfMaps(config, "pipeStage")
                            .map { h => new PipeStageConfig(h) }
  def pipeStage() = _pipeStage
  def pipeStage(i: Int) = _pipeStage(i)
  def pipeStage_=(x: List[PipeStageConfig]) { _pipeStage = x }

  private var _control = CUControlBoxConfig(Parser.getFieldMap(config, "control"))
  def control() = _control
  def control_=(x: CUControlBoxConfig) { _control = x }


  override def toString = {
    s"pipeStage:\n" +
    pipeStage.map { _.toString }.reduce {_+_}
  }
}

/**
 * CUControlBox config information
 */
case class CUControlBoxConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _tokenOutLUT: List[LUTConfig] = Parser.getFieldListOfMaps(config, "tokenOutLUT")
                                                    .map { LUTConfig(_) }
  def tokenOutLUT = _tokenOutLUT
  def tokenOutLUT_=(x: List[LUTConfig]) { _tokenOutLUT = x }

  private var _enableLUT: List[LUTConfig] = Parser.getFieldListOfMaps(config, "enableLUT")
                                                    .map { LUTConfig(_) }
  def enableLUT = _enableLUT
  def enableLUT_=(x: List[LUTConfig]) { _enableLUT = x }

  private var _udcInit: List[Int] = Parser.getFieldList(config, "udcInit")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }
  def udcInit = _udcInit
  def udcInit_=(x: List[Int]) { _udcInit = x }

  private var _decXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "decXbar"))
  def decXbar = _decXbar
  def decXbar_=(x: CrossbarConfig) { _decXbar = x }

  private var _incXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "incXbar"))
  def incXbar = _incXbar
  def incXbar_=(x: CrossbarConfig) { _incXbar = x }
}

/**
 * Crossbar config information
 */
case class CrossbarConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _outSelect: List[Int] = Parser.getFieldList(config, "outSelect")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }

  def outSelect = _outSelect
  def outSelect_=(x: List[Int]) { _outSelect = x }
}

/**
 * LUT config information
 */
case class LUTConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _table: List[Int] = Parser.getFieldList(config, "table")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }

  def table = _table
  def table_=(x: List[Int]) { _table = x }
}

/**
 * Plasticine config information
 */
case class PlasticineConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _cu: List[ComputeUnitConfig] = Parser.getFieldListOfMaps(config, "cu")
                                        .map { ComputeUnitConfig(_) }

  def cu = _cu
  def cu_=(x: List[ComputeUnitConfig]) { _cu = x }
}
