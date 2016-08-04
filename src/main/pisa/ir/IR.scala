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
  private var _max: Int = 0
  def max = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = 0
  def stride = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = 0
  def maxConst = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = 0
  def strideConst = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  // Construct the class here
  max = Parser.getFieldInt(config, "max")
  stride = Parser.getFieldInt(config, "stride")
  maxConst = Parser.getFieldInt(config, "maxConst")
  strideConst = Parser.getFieldInt(config, "strideConst")
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
    case 'm' => 3 // Memory
    case _ => throw new Exception(s"Unknown data source '${config(0)}'. Must be l, r, c, or m")
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

  /* Remote mux configs */
  private var _remoteMux0 = Parser.getFieldInt(config, "remoteMux0")
  def remoteMux0() = _remoteMux0
  def remoteMux0_=(x: Int) { _remoteMux0 = x }

  private var _remoteMux1 = Parser.getFieldInt(config, "remoteMux1")
  def remoteMux1() = _remoteMux1
  def remoteMux1_=(x: Int) { _remoteMux1 = x }

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

  override def toString = {
    s"remoteMux0: $remoteMux0" + "\n" +
    s"remoteMux1: $remoteMux1" + "\n" +
    s"pipeStage:\n" +
    pipeStage.map { _.toString }.reduce {_+_}
  }
}

/**
 * CUControlBox config information
 */
case class CUControlBoxConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _tokenOutLUT: List[List[Int]] = Parser.getFieldList(config, "tokenOutLUT")
                                        .asInstanceOf[List[List[Double]]]
                                        .map { l => l map {_.toInt} }
  def tokenOutLUT = _tokenOutLUT
  def tokenOutLUT_=(x: List[List[Int]]) { _tokenOutLUT = x }

  private var _enableLUT: List[List[Int]] = Parser.getFieldList(config, "enableLUT")
                                        .asInstanceOf[List[List[Double]]]
                                        .map { l => l map {_.toInt} }
  def enableLUT = _enableLUT
  def enableLUT_=(x: List[List[Int]]) { _enableLUT = x }


  private var _udcInit: List[Int] = Parser.getFieldList(config, "udcInit")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }
  def udcInit = _udcInit
  def udcInit_=(x: List[Int]) { _udcInit = x }
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
