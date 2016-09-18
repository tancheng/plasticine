package plasticine.pisa.ir

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes
import scala.collection.mutable.HashMap
import scala.util.Random
import Chisel._

/**
 * Base class for all Config nodes
 */
abstract class AbstractConfig {
  protected def encodeOneHot(x: Int) = 1 << x
  protected def getRegNum(s: String) = if (s.size <= 1) 0 else s.drop(1).toInt
  def parseValue(x: String):Int = x(0) match {
    case 'x' => 0
    case _ => Integer.parseInt(x)
  }
}

/**
 * Parsed config information for a single counter
 */
case class CounterRCConfig(config: Map[Any, Any], randomize: Boolean = false) extends AbstractConfig {
  /** Counter max */
  private var _max: Int = Parser.getFieldInt(config, "max", randomize)
  def max = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = Parser.getFieldInt(config, "stride", randomize)
  def stride = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = Parser.getFieldInt(config, "maxConst", randomize)
  def maxConst = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = Parser.getFieldInt(config, "strideConst", randomize)
  def strideConst = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  /** Delay the start of counter */
  private var _startDelay: Int = 1 + Parser.getFieldInt(config, "startDelay", randomize)
  def startDelay = _startDelay
  def startDelay_=(x: Int) { _startDelay = x }

  /** Delay raising the counter 'done' signal */
  private var _endDelay: Int = 1 + Parser.getFieldInt(config, "endDelay", randomize)
  def endDelay = _endDelay
  def endDelay_=(x: Int) { _endDelay = x }
}

/**
 * CounterChain config information
 */
case class CounterChainConfig(config: Map[Any, Any], randomize: Boolean = false) extends AbstractConfig {
  // To chain or not?
  private var _chain: List[Int] = if (randomize) {
    List.tabulate(100) { i => i % 2 } // HACK: Fix this by adding template-specific config nodes
  } else {
    Parser.getFieldList(config, "chain")
                                        .asInstanceOf[List[Double]]
                                        .map { i => i.toInt }
  }

  def chain = _chain
  def chain_=(x: List[Int]) { _chain = x }

  // Configuration for individual counters
  private var _counters: Seq[CounterRCConfig] = if (randomize) {
    List.fill(100) { CounterRCConfig(Map(), true) }
  } else {
    Parser.getFieldListOfMaps(config, "counters")
       .map { h => new CounterRCConfig(h) }
  }
  def counters = _counters
  def counters_=(x: Seq[CounterRCConfig]) { _counters = x }
}


/**
 * Parsed configuration information for ComputeUnit
 */
case class OperandConfig(config: String) extends AbstractConfig {
  private def getDataSrc = config(0) match {
    case 'x' => 0 // Don't care (must eventually be turned off)
    case 'l' => 0 // Local register
    case 'r' => 1 // Previous pipe stage register
    case 'c' => 2 // Constant
    case 'i' => 3 // Iterator / counter
    case 't' => 3 // Cross-stage value for reduction
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
  // Map (regNum -> muxconfig)
  private var _fwd: Map[Int, Int] = {
    val fwdMap = Parser.getFieldMap(config, "fwd")
    val t = HashMap[Int, Int]()
    fwdMap.keys.foreach { reg =>
      val source = fwdMap(reg)
      val regNum = Integer.parseInt(reg.toString.drop(1))
      t(regNum) = source match {
        case "i" => 1 // Same number because counter and memory contents don't overlap
        case "m" => 1
        case "e" => 1
        case _ => 0
      }
    }
    t.toMap
  }
  def fwd = _fwd
  def fwd_=(x: Map[Int, Int]) { _fwd = x }

  override def toString = {
    s"opA:\n ${opA.toString}" + "\n" +
    s"opB:\n ${opB.toString}" + "\n" +
    s"opcode: $opcode" + "\n" +
    s"result: $result"
  }
}

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(val src: Int, val value: Int)

case class BankingConfig(config: String) extends AbstractConfig {
  def parseMode = config(0) match {
    case 'x' => 0 // No banking
    case 'b' => 1 // Strided
    case 'd' => 2 // Diagonal
    case _ => throw new Exception(s"Unsupported banking mode ''$config'")
  }
  def parseStride = {
    if (config.size == 1) 0
    else log2Up(Integer.parseInt(config.drop(1)))
  }
  private var _mode = parseMode
  def mode = _mode
  def mode_=(x: Int) { _mode = x }

  private var _strideLog2 = parseStride
  def strideLog2 = _strideLog2
  def strideLog2_=(x: Int) { _strideLog2 = x }
}

case class ScratchpadConfig(config: Map[Any, Any]) extends AbstractConfig {
  // Banking stride
  private def parseAddrSource(x: String) = {
    val src = x(0) match {
      case 'x' => 0  // Don't care
      case 's' => 0  // Stage
      case 'i' => 1  // Iterator
      case 'l' => 2  // Last stage - only for write addr. TODO: Error out for reads
      case _ => throw new Exception(s"Unknwon address source ${x(0)}; must be one of s, i, or l")
   }
   SrcValueTuple(src, if (x == "x") 0 else x.drop(1).toInt)
  }

  private var _wa = parseAddrSource(Parser.getFieldString(config, "wa"))
  def wa = _wa
  def wa_=(x: SrcValueTuple) { _wa = x }

  private var _ra = parseAddrSource(Parser.getFieldString(config, "ra"))
  def ra = _ra
  def ra_=(x: SrcValueTuple) { _ra = x }

  private var _wd = Parser.getFieldString(config, "wd") match {
    case "x" => 0 // Don't care
    case "local" => 0
    case "remote" => 1
    case _ => throw new Exception(s"Unknown write data source; must be either local or remote")
  }
  def wd = _wd
  def wd_=(x: Int) { _wd = x }

  private var _wen = Parser.getFieldString(config, "wen") match {
    case "x" => 0
    case n@_ => n.drop(1).toInt + 1
  }
  def wen = _wen
  def wen_=(x: Int) { _wen = x }

  private var _banking = BankingConfig(Parser.getFieldString(config, "banking"))
  def banking = _banking
  def banking_=(x: BankingConfig) { _banking = x }
}

case class ComputeUnitConfig(config: Map[Any, Any]) extends AbstractConfig {
  /* CounterChain config */
  private var _counterChain = new CounterChainConfig(
    Parser.getFieldMap(config, "counterChain"))
  def counterChain() = _counterChain
  def counterChain_=(x: CounterChainConfig) { _counterChain = x }


  private var _scratchpads =  Parser.getFieldListOfMaps(config, "scratchpads")
                                    .map { ScratchpadConfig(_) }
  def scratchpads = _scratchpads
  def scratchpads_=(x: List[ScratchpadConfig]) { _scratchpads = x }

  /* Pipe stages config */
  private var _pipeStage = Parser.getFieldListOfMaps(config, "pipeStage")
                            .map { h => new PipeStageConfig(h) }
  def pipeStage = _pipeStage
  def pipeStage_=(x: List[PipeStageConfig]) { _pipeStage = x }

  private var _control = CUControlBoxConfig(Parser.getFieldMap(config, "control"))
  def control = _control
  def control_=(x: CUControlBoxConfig) { _control = x }

  private var _dataInXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "dataInXbar"))
  def dataInXbar = _dataInXbar
  def dataInXbar_=(x: CrossbarConfig) { _dataInXbar = x }

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

  private var _tokenDownLUT: LUTConfig = LUTConfig(Parser.getFieldMap(config, "tokenDownLUT"))
  def tokenDownLUT = _tokenDownLUT
  def tokenDownLUT_=(x: LUTConfig) { _tokenDownLUT = x }


  private var _udcInit: List[Int] = Parser.getFieldList(config, "udcInit")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_) }
  def udcInit = _udcInit
  def udcInit_=(x: List[Int]) { _udcInit = x }

  private var _decXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "decXbar"), true)
  def decXbar = _decXbar
  def decXbar_=(x: CrossbarConfig) { _decXbar = x }

  private var _incXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "incXbar"), true)
  def incXbar = _incXbar
  def incXbar_=(x: CrossbarConfig) { _incXbar = x }

  private var _doneXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "doneXbar"))
  def doneXbar = _doneXbar
  def doneXbar_=(x: CrossbarConfig) { _doneXbar = x }

  private var _enableMux: List[Boolean] = Parser.getFieldList(config, "enableMux")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_) > 0 }
  def enableMux = _enableMux
  def enableMux_=(x: List[Boolean]) { _enableMux = x }

  private var _tokenOutMux: List[Boolean] = Parser.getFieldList(config, "tokenOutMux")
                                        .asInstanceOf[List[String]]
                                         .map { parseValue(_) > 0 }
  def tokenOutMux = _tokenOutMux
  def tokenOutMux_=(x: List[Boolean]) { _tokenOutMux = x }

  private var _syncTokenMux: Int = parseValue(Parser.getFieldString(config, "syncTokenMux"))
  def syncTokenMux = _syncTokenMux
  def syncTokenMux_=(x: Int) { _syncTokenMux = x }


}

/**
 * Crossbar config information
 * @param incByOne: Set to true if crossbar's '0' corresponds to the value 0.
 * In this case, the user specifies 'x' for don't care, and 0,1,.. for actual values.
 * Add 1 to each value that is non-'x' if set to true.
 */
case class CrossbarConfig(config: Map[Any, Any], incByOne: Boolean = false) extends AbstractConfig {
  private var _outSelect: List[Int] = Parser.getFieldList(config, "outSelect")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_) }

  def outSelect = _outSelect
  def outSelect_=(x: List[Int]) { _outSelect = x }

  override def parseValue(x: String):Int = x(0) match {
    case 'x' => 0
    case _ => if (incByOne) 1 + Integer.parseInt(x) else Integer.parseInt(x)
  }
}

/**
 * LUT config information
 */
case class LUTConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _table: List[Int] = Parser.getFieldList(config, "table")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_) }
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
