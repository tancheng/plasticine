package plasticine.pisa.ir

import java.io.File
//import _root_.scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import plasticine.ArchConfig
import plasticine.pisa.parser._
import plasticine.templates.Opcodes
import scala.collection.mutable.HashMap
import scala.util.Random
import scala.reflect.runtime.universe._
import plasticine.arch._

//import Chisel._

/**
 * Base class for all Config nodes
 */
abstract class AbstractBits {
  protected def encodeOneHot(x: Int) = 1 << x
  protected def getRegNum(s: String) = if (s.size <= 1) 0 else s.drop(1).toInt
  def parseValue(x: String):Int = x(0) match {
    case 'x' => 0
    case _ => Integer.parseInt(x)
  }

  def toBinary[TP<:Any](x: TP, w: Int): List[Int] = x match {
    case num: Int => List.tabulate(w) { j => if (BigInt(num).testBit(j)) 1 else 0 }
    case num: Float => toBinary(java.lang.Float.floatToRawIntBits(num), 32)
    case l: List[Any] => l.map { e => toBinary(e, w/l.size) }.flatten
    case s: AbstractBits => s.toBinary
    case _ => throw new Exception("Unsupported type for toBinary")
  }

  def toBinary(): List[Int] = {
    throw new Exception("Not implemented!")
    List[Int]()
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
case class CounterRCBits(
  max: Int = 0,
  stride: Int = 0,
  maxConst: Int = 0,
  strideConst: Int = 0,
  startDelay: Int = 0,
  endDelay: Int = 0,
  onlyDelay: Int = 0
) extends AbstractBits {
}
object CounterRCBits {
  def getRandom = {
    new CounterRCBits(
        math.abs(Random.nextInt(16)),
        math.abs(Random.nextInt(16)),
        Random.nextInt(2),
        Random.nextInt(2),
        Random.nextInt(16),
        Random.nextInt(16),
        math.abs(Random.nextInt(1))
      )
  }
  def zeroes = {
    new CounterRCBits()
  }
}

/**
 * CounterChain config information
 */
case class CounterChainBits(chain: List[Int], counters: List[CounterRCBits]) extends AbstractBits
object CounterChainBits {
  def getRandom(numCounters: Int) = {
    new CounterChainBits(
      List.fill(numCounters) { Random.nextInt(16) },
      List.fill(numCounters) { CounterRCBits.getRandom }
      )
  }
  def zeroes(numCounters: Int) = {
    new CounterChainBits(
      List.fill(numCounters) { 0 },
      List.fill(numCounters) { CounterRCBits.zeroes }
      )
  }
}

case class OperandBits(dataSrc: Int = 0, value: Int = 0)(t: OperandBundle)
extends AbstractBits {
  // Get names of case class fields
  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.reverse.map {_.name.toString}

  // Get width of x'th field of this class using corresponding 't' field
  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth

  override def toBinary(): List[Int] = {
    // Iterate through list elements backwards, convert to binary
    List.tabulate(this.productArity) { i =>
      val idx = this.productArity - 1 - i
      val elem = this.productElement(idx)
      val w = widthOf(idx)
      println(s"Width of $idx = $w")
      toBinary(elem, w)
    }.flatten
  }

}
object OperandBits {
  def getRandom = {
    new OperandBits(math.abs(Random.nextInt) % 4, math.abs(Random.nextInt) % 4)(new OperandBundle(4))
  }
  def zeroes = {
    new OperandBits()(new OperandBundle(4))
  }

}

case class PipeStageBits(
  opA: OperandBits = OperandBits()(null),
  opB: OperandBits = OperandBits()(null),
  opC: OperandBits = OperandBits()(null),
  opcode: Int = 0,
  result: Int = 0,
  fwd: Map[Int, Int] = Map[Int, Int]()
)(t: PipeStageBundle)
extends AbstractBits {
  // Get names of case class fields
  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.reverse.map {_.name.toString}

  // Get width of x'th field of this class using corresponding 't' field
  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth

  override def toBinary(): List[Int] = {
    // Iterate through list elements backwards, convert to binary
    List.tabulate(this.productArity) { i =>
      val idx = this.productArity - 1 - i
      val elem = this.productElement(idx)
      val w = widthOf(idx)
      println(s"Width of $idx = $w")
      toBinary(elem, w)
    }.flatten
  }

}
object PipeStageBits {
  def getRandom = {
    new PipeStageBits(
        OperandBits.getRandom,
        OperandBits.getRandom,
        OperandBits.getRandom,
        math.abs(Random.nextInt) % Opcodes.size,
        math.abs(Random.nextInt) % 4,
        Map[Int,Int]()
      )(null)
  }
  def zeroes = {
    new PipeStageBits()(null)
  }
}

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(val src: Int = 0, val value: Int = 0)

case class BankingBits(mode: Int = 0, strideLog2: Int = 0) extends AbstractBits

case class ScratchpadBits(
  wa: SrcValueTuple = SrcValueTuple(),
  ra: SrcValueTuple = SrcValueTuple(),
  wd: Int = 0,
  wen: Int = 0,
  wswap: Int = 0,
  rswap: Int = 0,
  enqEn: Int = 0,
  deqEn: Int = 0,
  banking: BankingBits = BankingBits(),
  numBufs: Int = 0,
  isReadFifo: Int = 0,
  isWriteFifo: Int = 0,
  fifoSize: Int = 0
) extends AbstractBits
object ScratchpadBits {
  def getRandom = {
    new ScratchpadBits(
      SrcValueTuple(0, 0), // wa
      SrcValueTuple(0, 0), // ra
      0,
      0,
      0,
      0,
      0,
      0,
      BankingBits(0, 1),
      1,
      0,
      0,
      0
    )
  }
  def zeroes = {
    new ScratchpadBits()
  }
}
case class FIFOBits (
  chainWrite: Int = 0,
  chainRead: Int = 0
) extends AbstractBits

case class MemoryUnitBits (
  scatterGather: Int = 0,
  isWr: Int = 0,
  counterChain: CounterChainBits,
  control: CUControlBoxBits
) extends AbstractBits
object MemoryUnitBits {
  def getRandom = {
    MemoryUnitBits(0, 0, CounterChainBits.getRandom(ArchConfig.numCounters),
      CUControlBoxBits.getRandom(ArchConfig.numTokens, ArchConfig.numTokens, ArchConfig.numCounters)
      )
  }
  def zeroes = {
    new MemoryUnitBits(0, 0, CounterChainBits.zeroes(ArchConfig.numCounters),
      CUControlBoxBits.zeroes(ArchConfig.numTokens, ArchConfig.numTokens, ArchConfig.numCounters)
      )
  }
}
case class ComputeUnitBits(
  counterChain: CounterChainBits,
  scalarXbar: CrossbarBits,
  scratchpads: List[ScratchpadBits],
  pipeStage: List[PipeStageBits],
  control: CUControlBoxBits,
  scalarInMux: List[Int],
  scalarOutMux: Int
//  dataInXbar: CrossbarBits
) extends AbstractBits
object ComputeUnitBits {
  def getRandom(d: Int, numCounters: Int, numTokenIn: Int, numTokenOut: Int, numScratchpads: Int) = {
    new ComputeUnitBits (
      CounterChainBits.getRandom(numCounters),
      CrossbarBits.getRandom(ArchConfig.numScalarRegisters),
      List.tabulate(numScratchpads) { i => ScratchpadBits.getRandom },
      List.tabulate(d) { i => PipeStageBits.getRandom },
      CUControlBoxBits.getRandom(numTokenIn, numTokenOut, numCounters),
      List.fill(numScratchpads) { 0 },
      0
      )
  }
  def zeroes(d: Int, numCounters: Int, numTokenIn: Int, numTokenOut: Int, numScratchpads: Int) = {
    new ComputeUnitBits (
      CounterChainBits.zeroes(numCounters),
      CrossbarBits.zeroes(ArchConfig.numScalarRegisters),
      List.tabulate(numScratchpads) { i => ScratchpadBits.zeroes },
      List.tabulate(d) { i => PipeStageBits.zeroes },
      CUControlBoxBits.zeroes(numTokenIn, numTokenOut, numCounters),
      List.fill(numScratchpads) { 0 },
      0
      )
  }

}
case class CUControlBoxBits(
  tokenOutLUT: List[LUTBits],
  enableLUT: List[LUTBits],
  tokenDownLUT: List[LUTBits],
  udcInit: List[Int],
  udcInitAtConfig: List[Int],
  decXbar: CrossbarBits,
  incXbar: CrossbarBits,
  tokenInXbar: CrossbarBits,
  doneXbar: CrossbarBits,
  enableMux: List[Boolean],
  syncTokenMux: List[Int],
  tokenOutXbar: CrossbarBits,
  fifoAndTree: List[Int],
  tokenInAndTree: List[Int],
  fifoMux: List[Int]
) extends AbstractBits
object CUControlBoxBits {
  def getRandom(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
    new CUControlBoxBits(
        List.fill(numTokenOut) { LUTBits.getRandom(2) }, // tokenOutLUT
        List.fill(numCounters) { LUTBits.getRandom(numCounters)}, // enableLUT
        List.fill(8) { LUTBits.getRandom(numCounters+1) }, // tokenDownLUT,
        List.fill(numCounters) { math.abs(Random.nextInt) % 4 }, // udcInit,
        List.fill(numCounters) { math.abs(Random.nextInt) % 1 }, // udcInitAtBits,
        CrossbarBits.getRandom(numCounters), // decXbar,
        CrossbarBits.getRandom(2*numCounters), // incXbar,
        CrossbarBits.getRandom(numCounters), // tokenInXbar,
        CrossbarBits.getRandom(2*numCounters), // doneXbar,
        List.fill(numCounters) { math.abs(Random.nextInt) % 2 == 0}, // enableMux,
        List.fill(8) { math.abs(Random.nextInt) % 2 }, // syncTokenMux
        CrossbarBits.getRandom(numCounters), // tokenOutXbar,
        List.fill(ArchConfig.numScratchpads) { 0 },  // fifoAndTree
        List.fill(numTokenIn) { 0 },  // tokenInAndTree
        List.fill(numCounters) { 0 }  // fifoMux
      )
  }
  def zeroes(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
    new CUControlBoxBits(
        List.fill(numTokenOut) { LUTBits.zeroes(2) }, // tokenOutLUT
        List.fill(numCounters) { LUTBits.zeroes(numCounters)}, // enableLUT
        List.fill(8) { LUTBits.zeroes(numCounters+1) }, // tokenDownLUT,
        List.fill(numCounters) { 0 }, // udcInit,
        List.fill(numCounters) { 0 }, // udcInitAtConfig,
        CrossbarBits.zeroes(numCounters), // decXbar,
        CrossbarBits.zeroes(2*numCounters), // incXbar,
        CrossbarBits.zeroes(numCounters), // tokenInXbar,
        CrossbarBits.zeroes(2*numCounters), // doneXbar,
        List.fill(numCounters) { false }, // enableMux,
        List.fill(8) { 0 }, // syncTokenMux
        CrossbarBits.zeroes(numCounters), // tokenOutXbar,
        List.fill(ArchConfig.numScratchpads) { 0 }, // fifoAndTree
        List.fill(numTokenIn) { 0 },  // tokenInAndTree
        List.fill(numCounters) { 0 }  // fifoMux
      )
  }
}
/**
 * Crossbar config information
 * @param incByOne: Set to true if crossbar's '0' corresponds to the value 0.
 * In this case, the user specifies 'x' for don't care, and 0,1,.. for actual values.
 * Add 1 to each value that is non-'x' if set to true.
 */
case class CrossbarBits(outSelect: List[Int]) extends AbstractBits
object CrossbarBits {
  def getRandom(numOutputs: Int) = {
    new CrossbarBits(List.fill(1+numOutputs) { math.abs(Random.nextInt) % 2 })
  }
  def zeroes(numOutputs: Int) = {
    new CrossbarBits(List.fill(1+numOutputs) { 0 })
  }
}

/**
 * LUT config information
 */
case class LUTBits(table: List[Int]) extends AbstractBits
object LUTBits {
  def getRandom(numInputs: Int) = {
    new LUTBits(List.tabulate(1 << numInputs) { i => math.abs(Random.nextInt(2)) })
  }
  def zeroes(numInputs: Int) = {
    new LUTBits(List.fill(1 << numInputs) { 0 })
  }
}

/**
 * Connection box config information
 */
case class ConnBoxBits(sel: Int = 0) extends AbstractBits
object ConnBoxBits {
  def getRandom(numInputs: Int) = {
    new ConnBoxBits(math.abs(Random.nextInt(numInputs)))
  }
  def zeroes(numInputs: Int) = {
    new ConnBoxBits()
  }
}

/**
 * TopUnitBits
 */
case class TopUnitBits(
  doneConnBox: ConnBoxBits,
//  dataVldConnBox: ConnBoxBits,
  argOutConnBox: ConnBoxBits
) extends AbstractBits
object TopUnitBits {
  def getRandom(numInputs: Int) = {
    new TopUnitBits(
      ConnBoxBits.getRandom(numInputs),
//      ConnBoxBits.getRandom(numInputs),
      ConnBoxBits.getRandom(numInputs)
      )
  }
  def zeroes(numInputs: Int) = {
    new TopUnitBits(
      ConnBoxBits.zeroes(numInputs),
//      ConnBoxBits.zeroes(numInputs),
      ConnBoxBits.zeroes(numInputs)
      )
  }
}


/**
 * Plasticine config information
 */
case class PlasticineBits(
  cu: List[ComputeUnitBits],
  dataSwitch: List[CrossbarBits],
  controlSwitch: List[CrossbarBits],
  mu: List[MemoryUnitBits],
  top: TopUnitBits
) extends AbstractBits

object PlasticineBits {
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
    new PlasticineBits(
      List.tabulate(rows*cols) { i => ComputeUnitBits.getRandom(d, numCounters, numTokenIn, numTokenOut, numScratchpads)},
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarBits.getRandom(8) },
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarBits.getRandom(8) },
      List.tabulate(numMemoryUnits) { i => MemoryUnitBits.getRandom },
      TopUnitBits.getRandom(8))
      }

  def zeroes(
    d: Int,
    rows: Int,
    cols: Int,
    numTokenIn: Int,
    numTokenOut: Int,
    numCounters: Int,
    numScratchpads: Int,
    numMemoryUnits: Int
  ) = {
    new PlasticineBits(
      List.tabulate(rows*cols) { i => ComputeUnitBits.zeroes(d, numCounters, numTokenIn, numTokenOut, numScratchpads)},
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarBits.zeroes(100) },
      List.tabulate((rows+1)*(cols+1)) { i => CrossbarBits.zeroes(100) },
      List.tabulate(numMemoryUnits) { i => MemoryUnitBits.zeroes },
      TopUnitBits.zeroes(8))
      }
}
