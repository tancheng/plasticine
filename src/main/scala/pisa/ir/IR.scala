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
case class SrcValueTuple(var src: SelectType = XSrc, var value: AnyVal = -1)
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
  override def toString = {
    val srcStr = src match {
      case CounterSrc => "CounterSrc"
      case ScalarFIFOSrc => "ScalarFIFOSrc"
      case VectorFIFOSrc => "VectorFIFOSrc"
      case ConstSrc => "ConstSrc"
      case PrevStageSrc => "PrevStageSrc"
      case CurrStageSrc => "CurrStageSrc"
      case ALUSrc => "ALUSrc"
      case XSrc => "XSrc"
      case EnableSrc => "EnableSrc"
      case DoneSrc => "DoneSrc"
      case _ => "<unk>"
    }

    val valueStr = value.toString
    s"($srcStr  $valueStr)"
  }
}
object SrcValueTuple {
  def zeroes(width: Int) = new SrcValueTuple(src = XSrc, value = 0)
}



/**
 * Parsed config information for a single counter
 */
case class CounterRCBits(
  max: SrcValueTuple,
  stride: SrcValueTuple,
//  min: SrcValueTuple,
  par:Int = 1
) extends AbstractBits {
}
object CounterRCBits {
  def zeroes(width: Int) = {
    new CounterRCBits(
      SrcValueTuple.zeroes(width),
      SrcValueTuple.zeroes(width),
      1
//      SrcValueTuple.zeroes(width)
      )
  }
  def apply(
    max: SrcValueTuple,
    stride: SrcValueTuple,
    min: SrcValueTuple,
    par:Int
  ):CounterRCBits = {
    CounterRCBits(max, stride, par) //TODO: add min, par back
  }
}

/**
 * CounterChain config information
 */
case class CounterChainBits(var chain: List[Int], counters: Array[CounterRCBits]) extends AbstractBits
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
  var opA: SrcValueTuple,
  var opB: SrcValueTuple,
  var opC: SrcValueTuple,
  var opcode: Opcode = XOp,
  var res: List[SrcValueTuple] = Nil,
  fwd: Array[SrcValueTuple] = Array[SrcValueTuple](),
  var enableSelect: SrcValueTuple
)
extends AbstractBits {
  lazy val result = res.map{
    case SrcValueTuple(CurrStageDst, idx:Int) => idx
    case _ => 0
  }

  lazy val regEnables = result ++ fwd.map { t =>
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
      fwd=Array.fill(numRegs)(SrcValueTuple.zeroes(width)),
      enableSelect = SrcValueTuple.zeroes(width)
    )
  }
}

case class ScratchpadBits(
  var mode: Int = 0,
  var stride: Int = 0,
  var numBufs: Int = 0,
  var isReadFifo: Int = 0,
  var isWriteFifo: Int = 0,
  var fifoSize: Int = 0
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
  def asPCUBits:PCUBits = this.asInstanceOf[PCUBits]
  def asPMUBits:PMUBits = this.asInstanceOf[PMUBits]
}

case class PCUBits(
  var stages: Array[PipeStageBits],
  var counterChain: CounterChainBits,
  var scalarValidOut: Array[SrcValueTuple],
  var vectorValidOut: Array[SrcValueTuple],
  var control: PCUControlBoxBits,
  var scalarInXbar: CrossbarBits,
  var scalarOutXbar: CrossbarBits,
  var fifoNbufConfig: List[Int],
  var accumInit: AnyVal
) extends CUBits
object PCUBits {
  def zeroes(p: PCUParams) = {
    new PCUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters),
      Array.fill(p.numScalarOut) { SrcValueTuple.zeroes(p.w) },
      Array.fill(p.numVectorOut) { SrcValueTuple.zeroes(p.w) },
      PCUControlBoxBits.zeroes(p),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numScalarIn, p.getNumRegs(ScalarInReg), p.w)),
      CrossbarBits.zeroes(ScalarSwitchParams(p.getNumRegs(ScalarOutReg), p.numScalarOut, p.w)),
      List.fill(p.getNumRegs(ScalarInReg)) { 0 },
      0
    )
  }
}

case class PMUBits(
  var stages: Array[PipeStageBits],
  var counterChain: CounterChainBits,
  var control: PMUControlBoxBits,
  var scalarInXbar: CrossbarBits,
  var scalarOutXbar: CrossbarBits,
  var scratchpad: ScratchpadBits,
  var wdataSelect: Int,
  var waddrSelect: SrcValueTuple,
  var raddrSelect: SrcValueTuple,
  var rdataEnable: List[Int],
  var fifoNbufConfig: List[Int]

) extends CUBits
object PMUBits {
  def zeroes(p: PMUParams) = {
    new PMUBits (
      Array.tabulate(p.d) { i => PipeStageBits.zeroes(p.r, p.w) },
      CounterChainBits.zeroes(p.w, p.numCounters),
      PMUControlBoxBits.zeroes(p),
      CrossbarBits.zeroes(ScalarSwitchParams(p.numScalarIn, p.getNumRegs(ScalarInReg), p.w)),
      CrossbarBits.zeroes(ScalarSwitchParams(p.getNumRegs(ScalarOutReg) + p.numScratchpadScalarOuts, p.numScalarOut, p.w)),
      ScratchpadBits.zeroes,
      0,
      SrcValueTuple.zeroes(p.w),
      SrcValueTuple.zeroes(p.w),
      List.fill(p.numVectorOut) { 0 },
      List.fill(p.getNumRegs(ScalarInReg)) { 0 }
    )
  }
}

case class SwitchCUBits(
  var counterChain: CounterChainBits,
  var control: SwitchCUControlBoxBits,
  var fifoNbufConfig: List[Int]
) extends CUBits {
  def stages = Array[PipeStageBits]()
}
object SwitchCUBits {
  def zeroes(p: SwitchCUParams) = {
    new SwitchCUBits (
      CounterChainBits.zeroes(p.w, p.numCounters),
      SwitchCUControlBoxBits.zeroes(p),
      List.fill(p.numScalarIn) { 0 }
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
        List.fill(p.numUDCs) { 0 }, // siblingAndTree
        0,   // streamingMuxSelect
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numUDCs)),  // incrementXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numCounters, 1)),  // doneXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numControlIn, p.numScalarIn)), // swapWriteXbar
        CrossbarBits.zeroes(ControlSwitchParams(p.numScalarIn + 2, p.numControlOut)) // tokenOutXbar
      )
  }
}

case class PMUControlBoxBits(
  var writeFifoAndTree: List[Int],
  var readFifoAndTree: List[Int],
  var scalarSwapReadSelect: List[Int],
  var writeDoneXbar: CrossbarBits,
  var readDoneXbar: CrossbarBits,
  var swapWriteXbar: CrossbarBits,
  var tokenOutXbar: CrossbarBits
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
  var siblingAndTree: List[Int],
  var childrenAndTree: List[Int],
  var udcDecSelect: List[Int],
  var incrementXbar: CrossbarBits,
  var doneXbar: CrossbarBits,
  var swapWriteXbar: CrossbarBits,
  var tokenOutXbar: CrossbarBits,
  var pulserMax: Int
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
        CrossbarBits.zeroes(ControlSwitchParams(4, p.numControlOut)), // tokenOutXbar
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
    new CrossbarBits(Array.fill(p.numOuts) { -1 })
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
  argOutMuxSelect: List[Int],
  doneSelect: Int
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
      List.fill(f.numArgOuts) { 0 },
      0
//      List.tabulate(numMemoryUnits) { i => MemoryUnitBits.zeroes },
  )}
}
