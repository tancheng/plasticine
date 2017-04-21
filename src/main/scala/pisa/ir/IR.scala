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

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(src: SelectType = XSrc, value: AnyVal = -1)
extends AbstractBits {
//  // Check if 'src' is in the list of valid sources
//  Predef.assert(t.validSources.contains(src), s"ERROR: Invalid source $src in SrcValueTuple (allowed sources: ${t.validSources})")

//  // Get names of case class fields
//  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
//      case m: MethodSymbol if m.isCaseAccessor => m
//  }.toList.reverse.map {_.name.toString}
//
//  // Get width of x'th field of this class using corresponding 't' field
//  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth
//
//  override def toBinary(): List[Int] = {
//    // Iterate through list elements backwards, convert to binary
//    List.tabulate(this.productArity) { i =>
//      val idx = this.productArity - 1 - i
//      val elem = this.productElement(idx)
//      val w = widthOf(idx)
//      println(s"Width of $idx = $w")
//      elem match {
//        case s: SelectSource => toBinary(t.srcIdx(s), w)
//        case _ => toBinary(elem, w)
//      }
//    }.flatten
//  }
}
object SrcValueTuple {
  def zeroes(width: Int) = new SrcValueTuple(src = XSrc, value = 0)
}



/**
 * Parsed config information for a single counter
 */
case class CounterRCBits(
  max: SrcValueTuple,
  stride: SrcValueTuple
//  min: SrcValueTuple,
//  par:Int = 1
  //maxConst: Int = 0,
  //strideConst: Int = 0,
  //startDelay: Int = 0,
  //endDelay: Int = 0,
  //onlyDelay: Int = 0
) extends AbstractBits {
}
object CounterRCBits {
  def zeroes(width: Int) = {
    new CounterRCBits(
      SrcValueTuple.zeroes(width),
      SrcValueTuple.zeroes(width)
//      SrcValueTuple.zeroes(width)
      )
  }
  def apply(
    max: SrcValueTuple,
    stride: SrcValueTuple,
    min: SrcValueTuple,
    par:Int = 1
  ):CounterRCBits = {
    CounterRCBits(max, stride) //TODO: add min, par back
  }
}

/**
 * CounterChain config information
 */
case class CounterChainBits(chain: List[Int], counters: Array[CounterRCBits]) extends AbstractBits
object CounterChainBits {
  def zeroes(width: Int, numCounters: Int) = {
    new CounterChainBits(
      List.fill(numCounters-1) { 0 },
      Array.fill(numCounters) { CounterRCBits.zeroes(width) }
    )
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
  opA: SrcValueTuple,
  opB: SrcValueTuple,
  opC: SrcValueTuple,
  opcode: Opcode = XOp,
  res: List[SrcValueTuple] = Nil,
  fwd: Array[SrcValueTuple]
)
extends AbstractBits {
  val result = res.map{
    case SrcValueTuple(CurrStageDst, idx:Int) => idx
    case _ => 0
  }

  val regEnables = result ++ fwd.map { t =>
    val n = t.value.asInstanceOf[Int]
    if (n == -1) 0 else n
  }

  // Get names of case class fields
//  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
//      case m: MethodSymbol if m.isCaseAccessor => m
//  }.toList.reverse.map {_.name.toString}
//
//  // Get width of x'th field of this class using corresponding 't' field
//  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth
//
//  override def toBinary(): List[Int] = {
//    // Iterate through list elements backwards, convert to binary
//    List.tabulate(this.productArity) { i =>
//      val idx = this.productArity - 1 - i
//      val elem = this.productElement(idx)
//      val w = widthOf(idx)
//      println(s"Width of $idx = $w")
//      toBinary(elem, w)
//    }.flatten
//  }
}
object PipeStageBits {
  def zeroes(numRegs: Int, width: Int) = {
    new PipeStageBits(
      opA = SrcValueTuple.zeroes(width),
      opB = SrcValueTuple.zeroes(width),
      opC = SrcValueTuple.zeroes(width),
      fwd=Array.fill(numRegs)(SrcValueTuple.zeroes(width))
    )
  }
}

case class ScratchpadBits(
  mode: Int = 0,
  stride: Int = 0,
  numBufs: Int = 0,
  isReadFifo: Int = 0,
  isWriteFifo: Int = 0,
  fifoSize: Int = 0
) extends AbstractBits
object ScratchpadBits {
  def zeroes = {
    new ScratchpadBits()
  }
}

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

trait CUBits extends AbstractBits {
  def stages: Array[PipeStageBits]
  def counterChain: CounterChainBits
}

case class PCUBits(
  var stages: Array[PipeStageBits],
  var counterChain: CounterChainBits,
  var scalarValidOut: Array[SrcValueTuple],
  var vectorValidOut: Array[SrcValueTuple],
  var control: PCUControlBoxBits,
  var scalarInXbar: CrossbarBits,
  var scalarOutXbar: CrossbarBits
) extends CUBits
object PCUBits {
  def zeroes(p: PCUParams) = {
    new PCUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters),
      Array.fill(p.numScalarOut) { SrcValueTuple.zeroes(p.w) },
      Array.fill(p.numVectorOut) { SrcValueTuple.zeroes(p.w) },
      PCUControlBoxBits.zeroes(p),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numScalarIn, p.numEffectiveScalarIn, p.w)),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numEffectiveScalarOut, p.numScalarOut, p.w))
    )
  }
}

case class PMUBits(
  stages: Array[PipeStageBits],
  counterChain: CounterChainBits,
  control: PMUControlBoxBits,
  scalarInXbar: CrossbarBits,
  scalarOutXbar: CrossbarBits,
  scratchpad: ScratchpadBits,
  wdataSelect: Int,
  waddrSelect: Int,
  raddrSelect: Int,
  rdataEnable: List[Int]

) extends CUBits
object PMUBits {
  def zeroes(p: PMUParams) = {
    new PMUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters),
      PMUControlBoxBits.zeroes(p),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numScalarIn, p.numEffectiveScalarIn, p.w)),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numEffectiveScalarOut, p.numScalarOut, p.w)),
      ScratchpadBits.zeroes,
      0,
      0,
      0,
      List.fill(p.numVectorOut) { 0 }
    )
  }
}

case class SwitchCUBits(
  counterChain: CounterChainBits,
  control: SwitchCUControlBoxBits
) extends CUBits {
  def stages = Array[PipeStageBits]()
}
object SwitchCUBits {
  def zeroes(p: SwitchCUParams) = {
    new SwitchCUBits (
      CounterChainBits.zeroes(p.w, p.numCounters),
      SwitchCUControlBoxBits.zeroes(p)
    )
  }
}

case class PCUControlBoxBits(
  var tokenInAndTree: List[Int],
  var fifoAndTree: List[Int],
  var siblingAndTree: List[Int],
  var streamingMuxSelect: Int,
  var incrementXbar: CrossbarBits,
  var doneXbar: CrossbarBits,
  var swapWriteXbar: CrossbarBits,
  var tokenOutXbar: CrossbarBits
) extends AbstractBits
object PCUControlBoxBits {
  def zeroes(p: PCUParams) = {
    new PCUControlBoxBits(
        List.fill(p.numControlIn) { 0 },   // tokenInAndTree
        List.fill(p.numScalarIn + p.numVectorIn) { 0 }, // fifoAndTree
        List.fill(p.numUDCs + 1) { 0 }, // siblingAndTree
        0,   // streamingMuxSelect
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numUDCs)),  // incrementXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numCounters, 1)),  // doneXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numScalarIn)), // swapWriteXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numScalarIn + 2, p.numControlOut)) // tokenOutXbar
      )
  }
}

case class PMUControlBoxBits(
  writeFifoAndTree: List[Int],
  readFifoAndTree: List[Int],
  scalarSwapReadSelect: List[Int],
  writeDoneXbar: CrossbarBits,
  readDoneXbar: CrossbarBits,
  swapWriteXbar: CrossbarBits,
  tokenOutXbar: CrossbarBits
) extends AbstractBits
object PMUControlBoxBits {
  def zeroes(p: PMUParams) = {
    new PMUControlBoxBits(
        List.fill(p.numScalarIn + p.numVectorIn) { 0 }, // writeFifoAndTree
        List.fill(p.numScalarIn + p.numVectorIn) { 0 }, // readFifoAndTree
        List.fill(p.numScalarIn) { 0 }, // scalarSwapReadSelect
        CrossbarBits.zeroes(ControlSwitchParams(p.numCounters + p.numControlIn, 1)),  // writeDoneXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numCounters + p.numControlIn, 1)),  // readDoneXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numScalarIn)), // swapWriteXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numScalarIn + 2, p.numControlOut)) // tokenOutXbar
      )
  }
}

case class SwitchCUControlBoxBits(
  siblingAndTree: List[Int],
  childrenAndTree: List[Int],
  udcDecSelect: List[Int],
  incrementXbar: CrossbarBits,
  doneXbar: CrossbarBits,
  swapWriteXbar: CrossbarBits,
  tokenOutXbar: CrossbarBits,
  pulserMax: Int
) extends AbstractBits
object SwitchCUControlBoxBits {
  def zeroes(p: SwitchCUParams) = {
    new SwitchCUControlBoxBits(
        List.fill(p.numUDCs) { 0 }, // siblingAndTree
        List.fill(p.numUDCs) { 0 }, // childrenAndTree
        List.fill(p.numUDCs) { 0 }, // udcDecSelect
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numUDCs)),  // incrementXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numCounters, 1)),  // doneXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numScalarIn)), // swapWriteXbar
        CrossbarBits.zeroes(ControlSwitchParams(3, p.numControlOut)), // tokenOutXbar
        0
      )
  }
}

/**
 * Crossbar config information
 * @param incByOne: Set to true if crossbar's '0' corresponds to the value 0.
 * In this case, the user specifies 'x' for don't care, and 0,1,.. for actual values.
 * Add 1 to each value that is non-'x' if set to true.
 */
case class CrossbarBits(outSelect: Array[Int]) extends AbstractBits
object CrossbarBits {
  def zeroes(p: SwitchParams) = {
    new CrossbarBits(Array.fill(1+p.numOuts) { 0 })
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
  switchCU: Array[Array[SwitchCUBits]], //TODO
  argOutMuxSelect: List[Int]
) extends AbstractBits

object PlasticineBits {
  def zeroes(
      cuParams:    Array[Array[CUParams]],
      vectorParams: Array[Array[VectorSwitchParams]],
      scalarParams: Array[Array[ScalarSwitchParams]],
      controlParams: Array[Array[ControlSwitchParams]],
      switchCUParams:    Array[Array[SwitchCUParams]],
      p: PlasticineParams,
      f: FringeParams
  ) = {
    new PlasticineBits(
      Array.tabulate(p.numRows, p.numCols) { case (i, j) => cuParams(i)(j) match {
        case pcu: PCUParams => PCUBits.zeroes(pcu)
        case pmu: PMUParams => PMUBits.zeroes(pmu)
      }},
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(vectorParams(i)(j)) },
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(scalarParams(i)(j)) },
      Array.tabulate((p.numRows+1), (p.numCols+1)) { case (i, j) => CrossbarBits.zeroes(controlParams(i)(j)) },
      Array.tabulate(p.numRows+1, p.numCols+1) { case (i, j) => { SwitchCUBits.zeroes(switchCUParams(i)(j)) }}, //TODO
      List.fill(f.numArgOuts) { 0 }
//      List.tabulate(numMemoryUnits) { i => MemoryUnitBits.zeroes },
  )}
}
