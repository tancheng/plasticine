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
import plasticine.templates._
import plasticine.spade._
import plasticine.pisa.enums._
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
    case num: Float => toBinary(java.lang.Float.floatToRawIntBits(num), w)
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
  max: SrcValueTuple = SrcValueTuple(),
  stride: SrcValueTuple = SrcValueTuple(),
  min: SrcValueTuple = SrcValueTuple(),
  par:Int = 1
  //maxConst: Int = 0,
  //strideConst: Int = 0,
  //startDelay: Int = 0,
  //endDelay: Int = 0,
  //onlyDelay: Int = 0
)(t: CounterConfig) extends AbstractBits {
}
object CounterRCBits {
  def zeroes(width: Int) = {
    val config = new CounterConfig(width, 0, 0)
    new CounterRCBits()(config)
  }
}

/**
 * CounterChain config information
 */
case class CounterChainBits(chain: List[Int], counters: Array[CounterRCBits])(t: CounterChainConfig) extends AbstractBits
object CounterChainBits {
  def zeroes(width: Int, numCounters: Int) = {
    val config = new CounterChainConfig(width, numCounters, 0, 0)
    new CounterChainBits(
      List.fill(numCounters) { 0 },
      Array.fill(numCounters) { CounterRCBits.zeroes(width) }
    )(config)
  }
}

//case class OperandBits(src: SrcValueTuple = SrcValueTuple())(t: OperandBundle)
//extends AbstractBits {
  //// Get names of case class fields
  //def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
      //case m: MethodSymbol if m.isCaseAccessor => m
  //}.toList.reverse.map {_.name.toString}

  //// Get width of x'th field of this class using corresponding 't' field
  //def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth

  //override def toBinary(): List[Int] = {
    //// Iterate through list elements backwards, convert to binary
    //List.tabulate(this.productArity) { i =>
      //val idx = this.productArity - 1 - i
      //val elem = this.productElement(idx)
      //val w = widthOf(idx)
      //println(s"Width of $idx = $w")
      //toBinary(elem, w)
    //}.flatten
  //}

//}
//object OperandBits {
  //def zeroes(width: Int) = {
    //new OperandBits()(new OperandBundle(width))
  //}
//}

case class PipeStageBits(
  opA: SrcValueTuple = SrcValueTuple(),
  opB: SrcValueTuple = SrcValueTuple(),
  opC: SrcValueTuple = SrcValueTuple(),
  opcode: Opcode = XOp,
  result: Array[SrcValueTuple] = Array(),
  fwd: Array[SrcValueTuple] 
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
  def zeroes(numRegs: Int, width: Int) = {
    val config = new PipeStageBundle(numRegs, width)
    new PipeStageBits(fwd=Array.fill(numRegs)(SrcValueTuple()))(config)
  }
}

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(src: SelectSource=XSrc, value: AnyVal = -1)

//case class BankingBits(mode: Int = 0, strideLog2: Int = 0) extends AbstractBits
//
//case class ScratchpadBits(
//  wa: SrcValueTuple = SrcValueTuple(),
//  ra: SrcValueTuple = SrcValueTuple(),
//  wd: Int = 0,
//  wen: Int = 0,
//  wswap: Int = 0,
//  rswap: Int = 0,
//  enqEn: Int = 0,
//  deqEn: Int = 0,
//  banking: BankingBits = BankingBits(),
//  numBufs: Int = 0,
//  isReadFifo: Int = 0,
//  isWriteFifo: Int = 0,
//  fifoSize: Int = 0
//) extends AbstractBits
//object ScratchpadBits {
//  def zeroes = {
//    new ScratchpadBits()
//  }
//}
//
//case class FIFOBits (
//  chainWrite: Int = 0,
//  chainRead: Int = 0
//) extends AbstractBits
//
//case class MemoryUnitBits (
//  scatterGather: Int = 0,
//  isWr: Int = 0,
//  counterChain: CounterChainBits,
//  control: CUControlBoxBits
//) extends AbstractBits
//object MemoryUnitBits {
//  def getRandom = {
//    MemoryUnitBits(0, 0, CounterChainBits.getRandom(ArchConfig.numCounters),
//      CUControlBoxBits.getRandom(ArchConfig.numTokens, ArchConfig.numTokens, ArchConfig.numCounters)
//      )
//  }
//  def zeroes = {
//    new MemoryUnitBits(0, 0, CounterChainBits.zeroes(ArchConfig.numCounters),
//      CUControlBoxBits.zeroes(ArchConfig.numTokens, ArchConfig.numTokens, ArchConfig.numCounters)
//      )
//  }
//}

trait CUBits extends AbstractBits

case class PCUBits(
  stages: Array[PipeStageBits],
  counterChain: CounterChainBits
//  control: CUControlBoxBits
)(t: PCUConfig) extends CUBits
object PCUBits {
  def zeroes(p: PCUParams) = {
    val config = new PCUConfig(p)
    new PCUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters)
//      CUControlBoxBits.zeroes(numTokenIn, numTokenOut, numCounters),
    )(config)
  }
}

case class PMUBits(
  stages: Array[PipeStageBits],
  counterChain: CounterChainBits
//  control: CUControlBoxBits
)(t: PMUConfig) extends CUBits
object PMUBits {
  def zeroes(p: PMUParams) = {
    val config = new PMUConfig(p)
    new PMUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters)
//      CUControlBoxBits.zeroes(numTokenIn, numTokenOut, numCounters),
    )(config)
  }
}

//case class CUControlBoxBits(
//  tokenOutLUT: List[LUTBits],
//  enableLUT: List[LUTBits],
//  tokenDownLUT: List[LUTBits],
//  udcInit: List[Int],
//  udcInitAtConfig: List[Int],
//  decXbar: CrossbarBits,
//  incXbar: CrossbarBits,
//  tokenInXbar: CrossbarBits,
//  doneXbar: CrossbarBits,
//  enableMux: List[Boolean],
//  syncTokenMux: List[Int],
//  tokenOutXbar: CrossbarBits,
//  fifoAndTree: List[Int],
//  tokenInAndTree: List[Int],
//  fifoMux: List[Int]
//) extends AbstractBits
//object CUControlBoxBits {
//  def getRandom(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
//    new CUControlBoxBits(
//        List.fill(numTokenOut) { LUTBits.getRandom(2) }, // tokenOutLUT
//        List.fill(numCounters) { LUTBits.getRandom(numCounters)}, // enableLUT
//        List.fill(8) { LUTBits.getRandom(numCounters+1) }, // tokenDownLUT,
//        List.fill(numCounters) { math.abs(Random.nextInt) % 4 }, // udcInit,
//        List.fill(numCounters) { math.abs(Random.nextInt) % 1 }, // udcInitAtBits,
//        CrossbarBits.getRandom(numCounters), // decXbar,
//        CrossbarBits.getRandom(2*numCounters), // incXbar,
//        CrossbarBits.getRandom(numCounters), // tokenInXbar,
//        CrossbarBits.getRandom(2*numCounters), // doneXbar,
//        List.fill(numCounters) { math.abs(Random.nextInt) % 2 == 0}, // enableMux,
//        List.fill(8) { math.abs(Random.nextInt) % 2 }, // syncTokenMux
//        CrossbarBits.getRandom(numCounters), // tokenOutXbar,
//        List.fill(ArchConfig.numScratchpads) { 0 },  // fifoAndTree
//        List.fill(numTokenIn) { 0 },  // tokenInAndTree
//        List.fill(numCounters) { 0 }  // fifoMux
//      )
//  }
//  def zeroes(numTokenIn: Int, numTokenOut: Int, numCounters: Int) = {
//    new CUControlBoxBits(
//        List.fill(numTokenOut) { LUTBits.zeroes(2) }, // tokenOutLUT
//        List.fill(numCounters) { LUTBits.zeroes(numCounters)}, // enableLUT
//        List.fill(8) { LUTBits.zeroes(numCounters+1) }, // tokenDownLUT,
//        List.fill(numCounters) { 0 }, // udcInit,
//        List.fill(numCounters) { 0 }, // udcInitAtConfig,
//        CrossbarBits.zeroes(numCounters), // decXbar,
//        CrossbarBits.zeroes(2*numCounters), // incXbar,
//        CrossbarBits.zeroes(numCounters), // tokenInXbar,
//        CrossbarBits.zeroes(2*numCounters), // doneXbar,
//        List.fill(numCounters) { false }, // enableMux,
//        List.fill(8) { 0 }, // syncTokenMux
//        CrossbarBits.zeroes(numCounters), // tokenOutXbar,
//        List.fill(ArchConfig.numScratchpads) { 0 }, // fifoAndTree
//        List.fill(numTokenIn) { 0 },  // tokenInAndTree
//        List.fill(numCounters) { 0 }  // fifoMux
//      )
//  }
//}

/**
 * Crossbar config information
 * @param incByOne: Set to true if crossbar's '0' corresponds to the value 0.
 * In this case, the user specifies 'x' for don't care, and 0,1,.. for actual values.
 * Add 1 to each value that is non-'x' if set to true.
 */
case class CrossbarBits(outSelect: Array[Int])(t: CrossbarConfig) extends AbstractBits
object CrossbarBits {
  def zeroes(p: SwitchParams) = {
    new CrossbarBits(Array.fill(1+p.numOuts) { 0 })(new CrossbarConfig(p))
  }
}

/**
 * LUT config information
 */
//case class LUTBits(table: List[Int]) extends AbstractBits
//object LUTBits {
//  def getRandom(numInputs: Int) = {
//    new LUTBits(List.tabulate(1 << numInputs) { i => math.abs(Random.nextInt(2)) })
//  }
//  def zeroes(numInputs: Int) = {
//    new LUTBits(List.fill(1 << numInputs) { 0 })
//  }
//}

/**
 * Connection box config information
 */
//case class ConnBoxBits(sel: Int = 0) extends AbstractBits
//object ConnBoxBits {
//  def getRandom(numInputs: Int) = {
//    new ConnBoxBits(math.abs(Random.nextInt(numInputs)))
//  }
//  def zeroes(numInputs: Int) = {
//    new ConnBoxBits()
//  }
//}

/**
 * Plasticine config information
 */
case class PlasticineBits(
  cu: Array[Array[CUBits]],
  vectorSwitch: Array[Array[CrossbarBits]],
  scalarSwitch: Array[Array[CrossbarBits]],
  controlSwitch: Array[Array[CrossbarBits]],
  argOutMuxSelect: List[Int]
)(t: PlasticineConfig) extends AbstractBits

object PlasticineBits {
  def zeroes(
      cuParams:    Array[Array[CUParams]],
      vectorParams: Array[Array[VectorSwitchParams]],
      scalarParams: Array[Array[ScalarSwitchParams]],
      controlParams: Array[Array[ControlSwitchParams]],
      p: PlasticineParams,
      f: FringeParams
  ) = {
    val config = PlasticineConfig(cuParams, vectorParams, scalarParams, controlParams, p, f)
    new PlasticineBits(
      Array.tabulate(p.numRows, p.numCols) { case (i, j) => cuParams(i)(j) match {
        case pcu: PCUParams => PCUBits.zeroes(pcu)
        case pmu: PMUParams => PMUBits.zeroes(pmu)
      }},
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(vectorParams(i)(j)) },
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(scalarParams(i)(j)) },
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(controlParams(i)(j)) },
      List.fill(f.numArgOuts) { 0 }
//      List.tabulate(numMemoryUnits) { i => MemoryUnitBits.zeroes },
  )(config)}
}
