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

abstract class DataSource
case class DONTCARE() extends DataSource
case class EMPTY() extends DataSource
case class STAGE() extends DataSource
case class FWD() extends DataSource
case class REMOTE() extends DataSource
case class CONST() extends DataSource
case class COUNTER() extends DataSource
case class MEMORY() extends DataSource
case class TREE() extends DataSource

/**
 * Parsed config information for a single counter
 */
case class CounterRCConfig(max: Int, stride: Int, maxConst: Int, strideConst: Int, startDelay: Int, endDelay: Int) extends AbstractConfig
object CounterRCConfig {
  def getRandom = {
    new CounterRCConfig(
        math.abs(Random.nextInt(16)),
        math.abs(Random.nextInt(16)),
        Random.nextInt(2),
        Random.nextInt(2),
        Random.nextInt(16),
        Random.nextInt(16)
      )
  }
}

/**
 * CounterChain config information
 */
case class CounterChainConfig(chain: List[Int], counters: List[CounterRCConfig]) extends AbstractConfig
object CounterChainConfig {
  def getRandom(numCounters: Int) = {
    new CounterChainConfig(
      List.fill(numCounters) { Random.nextInt(16) },
      List.fill(numCounters) { CounterRCConfig.getRandom }
      )
  }
}

case class OperandConfig(dataSrc: Int, value: Int) extends AbstractConfig
object OperandConfig {
  def getRandom = {
    new OperandConfig(Random.nextInt, Random.nextInt)
  }
}

case class PipeStageConfig(
  opA: OperandConfig,
  opB: OperandConfig,
  opcode: Int,
  result: Int,
  fwd: Map[Int, Int]
) extends AbstractConfig
object PipeStageConfig {
  def getRandom = {
    new PipeStageConfig(
        OperandConfig.getRandom,
        OperandConfig.getRandom,
        Random.nextInt % Opcodes.size,
        Random.nextInt,
        Map[Int,Int]()
      )
  }
}

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(val src: Int, val value: Int)

case class BankingConfig(mode: Int, strideLog2: Int) extends AbstractConfig

case class ScratchpadConfig(
  wa: SrcValueTuple,
  ra: SrcValueTuple,
  wd: Int,
  wen: Int,
  banking: BankingConfig,
  numBufs: Int
) extends AbstractConfig

case class FIFOConfig (
  chainRead: Int,
  chainWrite: Int
) extends AbstractConfig


case class ComputeUnitConfig(
  counterChain: CounterChainConfig,
  scratchpads: List[ScratchpadConfig],
  pipeStage: List[PipeStageConfig],
  control: CUControlBoxConfig
//  dataInXbar: CrossbarConfig
) extends AbstractConfig

case class CUControlBoxConfig(
  tokenOutLUT: List[LUTConfig],
  enableLUT: List[LUTConfig],
  tokenDownLUT: List[LUTConfig],
  udcInit: List[Int],
  decXbar: CrossbarConfig,
  incXbar: CrossbarConfig,
  doneXbar: CrossbarConfig,
  enableMux: List[Boolean],
  tokenOutMux: List[Boolean],
  syncTokenMux: Int
) extends AbstractConfig
object CUControlBoxConfig {
  def getRandom(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
    new CUControlBoxConfig(
        List.fill(numTokenOut-1) { LUTConfig.getRandom(2) }, // tokenOutLUT
        List.fill(numCounters) { LUTConfig.getRandom(numCounters)}, // enableLUT
        List.fill(2) { LUTConfig.getRandom(numCounters+1) }, // tokenDownLUT,
        List.fill(numCounters) { Random.nextInt }, // udcInit,
        CrossbarConfig.getRandom(numCounters), // decXbar,
        CrossbarConfig.getRandom(2*numCounters), // incXbar,
        CrossbarConfig.getRandom(numCounters), // doneXbar,
        List.fill(numCounters) { Random.nextInt % 2 == 0}, // enableMux,
        List.fill(numTokenOut-1) { Random.nextInt % 2 == 0}, // tokenOutMux,
        Random.nextInt % 2 // syncTokenMux
      )
  }
}
/**
 * Crossbar config information
 * @param incByOne: Set to true if crossbar's '0' corresponds to the value 0.
 * In this case, the user specifies 'x' for don't care, and 0,1,.. for actual values.
 * Add 1 to each value that is non-'x' if set to true.
 */
case class CrossbarConfig(outSelect: List[Int]) extends AbstractConfig
object CrossbarConfig {
  def getRandom(numOutputs: Int) = {
    new CrossbarConfig(List.fill(numOutputs) { Random.nextInt })
  }
}

/**
 * LUT config information
 */
case class LUTConfig(table: List[Int]) extends AbstractConfig
object LUTConfig {
  def getRandom(numInputs: Int) = {
    new LUTConfig(List.tabulate(1 << numInputs) { i => Random.nextInt(2) })
  }
}

/**
 * Plasticine config information
 */
case class PlasticineConfig(cu: List[ComputeUnitConfig]) extends AbstractConfig
