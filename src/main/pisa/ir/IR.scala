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
    new OperandConfig(math.abs(Random.nextInt) % 4, math.abs(Random.nextInt) % 4)
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
        math.abs(Random.nextInt) % Opcodes.size,
        math.abs(Random.nextInt) % 4,
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
object ScratchpadConfig {
  def getRandom = {
    new ScratchpadConfig(
      SrcValueTuple(0, 0), // wa
      SrcValueTuple(0, 0), // ra
      0,
      0,
      BankingConfig(0, 1),
      1
    )
  }
}
case class FIFOConfig (
  chainWrite: Int,
  chainRead: Int
) extends AbstractConfig

case class MemoryUnitConfig (
  scatterGather: Int,
  isWr: Int
) extends AbstractConfig
object MemoryUnitConfig {
  def getRandom = {
    MemoryUnitConfig(0, 0)
  }
}
case class ComputeUnitConfig(
  counterChain: CounterChainConfig,
  scratchpads: List[ScratchpadConfig],
  pipeStage: List[PipeStageConfig],
  control: CUControlBoxConfig
//  dataInXbar: CrossbarConfig
) extends AbstractConfig
object ComputeUnitConfig {
  def getRandom(d: Int, numCounters: Int, numTokenIn: Int, numTokenOut: Int, numScratchpads: Int) = {
    new ComputeUnitConfig (
      CounterChainConfig.getRandom(numCounters),
      List.tabulate(numScratchpads) { i => ScratchpadConfig.getRandom },
      List.tabulate(d) { i => PipeStageConfig.getRandom },
      CUControlBoxConfig.getRandom(numTokenIn, numTokenOut, numCounters)
      )
  }
}
case class CUControlBoxConfig(
  tokenOutLUT: List[LUTConfig],
  enableLUT: List[LUTConfig],
  tokenDownLUT: List[LUTConfig],
  udcInit: List[Int],
  decXbar: CrossbarConfig,
  incXbar: CrossbarConfig,
  tokenInXbar: CrossbarConfig,
  doneXbar: CrossbarConfig,
  enableMux: List[Boolean],
  tokenOutMux: List[Boolean],
  syncTokenMux: Int,
  tokenOutXbar: CrossbarConfig
) extends AbstractConfig
object CUControlBoxConfig {
  def getRandom(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
    new CUControlBoxConfig(
        List.fill(numTokenOut-1) { LUTConfig.getRandom(2) }, // tokenOutLUT
        List.fill(numCounters) { LUTConfig.getRandom(numCounters)}, // enableLUT
        List.fill(2) { LUTConfig.getRandom(numCounters+1) }, // tokenDownLUT,
        List.fill(numCounters) { math.abs(Random.nextInt) % 4 }, // udcInit,
        CrossbarConfig.getRandom(numCounters), // decXbar,
        CrossbarConfig.getRandom(2*numCounters), // incXbar,
        CrossbarConfig.getRandom(numCounters), // tokenInXbar,
        CrossbarConfig.getRandom(2*numCounters), // doneXbar,
        List.fill(numCounters) { math.abs(Random.nextInt) % 2 == 0}, // enableMux,
        List.fill(numTokenOut) { math.abs(Random.nextInt) % 2 == 0}, // tokenOutMux,
        math.abs(Random.nextInt) % 2, // syncTokenMux
        CrossbarConfig.getRandom(numCounters) // tokenOutXbar,
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
    new CrossbarConfig(List.fill(1+numOutputs) { math.abs(Random.nextInt) % 2 })
  }
}

/**
 * LUT config information
 */
case class LUTConfig(table: List[Int]) extends AbstractConfig
object LUTConfig {
  def getRandom(numInputs: Int) = {
    new LUTConfig(List.tabulate(1 << numInputs) { i => math.abs(Random.nextInt(2)) })
  }
}

/**
 * Connection box config information
 */
case class ConnBoxConfig(sel: Int) extends AbstractConfig
object ConnBoxConfig {
  def getRandom(numInputs: Int) = {
    new ConnBoxConfig(math.abs(Random.nextInt(numInputs)))
  }
}

/**
 * TopUnitConfig
 */
case class TopUnitConfig(
  doneConnBox: ConnBoxConfig,
  dataVldConnBox: ConnBoxConfig,
  argOutConnBox: ConnBoxConfig
) extends AbstractConfig
object TopUnitConfig {
  def getRandom(numInputs: Int) = {
    new TopUnitConfig(
      ConnBoxConfig.getRandom(numInputs),
      ConnBoxConfig.getRandom(numInputs),
      ConnBoxConfig.getRandom(numInputs)
      )
  }
}


/**
 * Plasticine config information
 */
case class PlasticineConfig(
  cu: List[ComputeUnitConfig],
  dataSwitch: List[CrossbarConfig],
  controlSwitch: List[CrossbarConfig],
  mu: List[MemoryUnitConfig],
  top: TopUnitConfig
) extends AbstractConfig

object PlasticineConfig {
  def getRandom(
    d: Int,
    rows: Int,
    cols: Int,
    numTokenIn: Int,
    numTokenOut: Int,
    numCounters: Int,
    numScratchpads: Int,
    numMemoryUnits: Int
  ) = {
    new PlasticineConfig(
      List.tabulate(rows*cols) { i => ComputeUnitConfig.getRandom(d, numCounters, numTokenIn, numTokenOut, numScratchpads)},
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarConfig.getRandom(8) },
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarConfig.getRandom(8) },
      List.tabulate(numMemoryUnits) { i => MemoryUnitConfig.getRandom },
      TopUnitConfig.getRandom(8))
      }
}
