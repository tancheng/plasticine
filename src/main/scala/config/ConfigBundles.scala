package plasticine.config

import util.HVec
import chisel3._
import chisel3.util._

import plasticine.spade._
import plasticine.pisa.enums._
import plasticine.templates.Opcodes
import plasticine.templates.Utils.log2Up

abstract class AbstractConfig extends Bundle

/**
 * SrcValueBundle: Generic tuple that holds a list of sources and a value
 * @param validSources: List of valid/allowed 'SelectSource' Enums for this instance.
 *                      The order of validSources determines the index order given to each source
 * @param valueWidth:   Width in bits of value field
 */
case class SrcValueBundle(validSources: List[SelectSource], valueWidth: Int) extends AbstractConfig {
  // XSrc must be a member of validSources all the time
  Predef.assert(validSources.contains(XSrc), s"ERROR: Xsrc must be among validSources for every SrcValueBundle! validSources: ${validSources}")

  val nonXSources = validSources.filterNot { _ == XSrc }
  val srcWidth = log2Up(nonXSources.size)

  val src = UInt(srcWidth.W)
  val value = UInt(valueWidth.W)

  def isValidSource(s: SelectSource) = {
    validSources.contains(s)
  }

  // Get the index of a given source for this SrcValueBundle
  def srcIdx(s: SelectSource) = {
    Predef.assert(isValidSource(s), s"ERROR: Source $s not present in validSources: $validSources")
    s match {
      case XSrc => 0
      case _ => nonXSources.indexOf(s)
    }
  }

  // Convenience method that creates a comparator that checks if 'src' is a given SelectSource value
  // Used during mux creation based on source
  def is(s: SelectSource) = {
    val idx = srcIdx(s)
    (src === idx.U)
  }

  override def cloneType(): this.type = {
    new SrcValueBundle(validSources, valueWidth).asInstanceOf[this.type]
  }
}

/**
 * Counter configuration format
 */
case class CounterConfig(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int) extends AbstractConfig {

  private val validSources = List[SelectSource](XSrc, ConstSrc, ScalarFIFOSrc)
  val max = SrcValueBundle(validSources, w)
  val stride = SrcValueBundle(validSources, w)

//  val maxConst = Bool()
//  val strideConst = Bool()
//  val startDelay = {
//    val delayWidth = math.max(startDelayWidth, 1)
//    UInt(delayWidth.W)
//  }
//  var endDelay = {
//    val delayWidth = math.max(startDelayWidth, 1)
//    UInt(delayWidth.W)
//  }
//
//  val onlyDelay = Bool()

  override def cloneType(): this.type = {
    new CounterConfig(w, startDelayWidth, endDelayWidth).asInstanceOf[this.type]
  }
}



/**
 * CounterChain config register format
 */
case class CounterChainConfig(val w: Int, val numCounters: Int, val startDelayWidth: Int = 0, val endDelayWidth: Int = 0) extends AbstractConfig {
  val chain = Vec(numCounters-1, Bool())
  val counters = Vec(numCounters, CounterConfig(w, startDelayWidth, endDelayWidth))

  override def cloneType(): this.type = {
    new CounterChainConfig(w, numCounters, startDelayWidth, endDelayWidth).asInstanceOf[this.type]
  }
}

//class OperandBundle(w: Int) extends AbstractConfig {
//  var dataSrc = UInt(3.W)  // TODO: 3 ?
//  var value = UInt(log2Up(w).W)
//
//  override def cloneType(): this.type = {
//    new OperandBundle(w).asInstanceOf[this.type]
//  }
//}

class PipeStageConfig(r: Int, w: Int) extends AbstractConfig {
  val operandSources = List[SelectSource](XSrc, ConstSrc, CounterSrc, ScalarFIFOSrc, VectorFIFOSrc, PrevStageSrc, CurrStageSrc)
  val opA = SrcValueBundle(operandSources, w)
  val opB = SrcValueBundle(operandSources, w)
  val opC = SrcValueBundle(operandSources, w)

  val opcode = UInt(log2Up(Opcodes.size).W)
  val result = UInt(r.W) // One-hot encoded
  var regEnables = UInt(r.W)    // One-hot encoded

  override def cloneType(): this.type = {
    new PipeStageConfig(r,w).asInstanceOf[this.type]
  }
}

case class PCUConfig(p: PCUParams) extends AbstractConfig {
  val stages = Vec(p.d, new PipeStageConfig(p.r, p.w))
  val counterChain = CounterChainConfig(p.w, p.numCounters)

  val validOutSources = List[SelectSource](XSrc, EnableSrc, DoneSrc)
  val scalarValidOut = Vec(p.numScalarOut, SrcValueBundle(validOutSources, log2Up(p.numCounters)))
  val vectorValidOut = Vec(p.numVectorOut, SrcValueBundle(validOutSources, log2Up(p.numCounters)))
  val control = PCUControlBoxConfig(p)
  override def cloneType(): this.type = {
    new PCUConfig(p).asInstanceOf[this.type]
  }
}
object PCUConfig {
  def apply(p:CUParams):PCUConfig = {
    PCUConfig(p.asInstanceOf[PCUParams])
  }
}

case class PMUConfig(p: PMUParams) extends AbstractConfig {
  val stages = Vec(p.d, new PipeStageConfig(p.r, p.w))
  val counterChain = CounterChainConfig(p.w, p.numCounters)

  override def cloneType(): this.type = {
    new PMUConfig(p).asInstanceOf[this.type]
  }
}

case class SwitchCUConfig(p: SwitchCUParams) extends AbstractConfig {
  val counterChain = CounterChainConfig(p.w, p.numCounters)
  val control = SwitchCUControlBoxConfig(p)

  override def cloneType(): this.type = {
    new SwitchCUConfig(p).asInstanceOf[this.type]
  }
}

/**
 * Crossbar config register format
 */
case class CrossbarConfig(p: SwitchParams) extends AbstractConfig {
  var outSelect = Vec(p.numOuts, UInt(log2Up(p.numIns).W))

  override def cloneType(): this.type = {
    new CrossbarConfig(p).asInstanceOf[this.type]
  }
}

/**
 * FIFO config register format
 */
case class FIFOConfig(val d: Int, val v: Int) extends AbstractConfig {
  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor

  var chainWrite = Bool()
  var chainRead = Bool()

  override def cloneType(): this.type = {
    new FIFOConfig(d, v).asInstanceOf[this.type]
  }
}

//class ScratchpadBundle(p: PCUParams) extends AbstractConfig {
//  var raSrc = UInt(width=1)
//  var raValue = UInt(width=log2Up(math.max(p.numCounters, p.d)))
//  var waSrc = UInt(width=2)
//  var waValue = UInt(width=log2Up(math.max(p.numCounters, p.d)))
//  var wdSrc = UInt(width=1)
//  var wen = UInt(width=log2Up(p.numCounters+1))
//  val wswap = UInt(width=log2Up(p.numCounters+1))
//  val rswap = UInt(width=log2Up(p.numCounters+1))
//  val enqEn = UInt(width=log2Up(p.numCounters+1))
//  val deqEn = UInt(width=log2Up(p.numCounters+1))
//
//
//  override def cloneType(): this.type = {
//    new ScratchpadBundle(p).asInstanceOf[this.type]
//  }
//}

/**
 * Scratchpad config register format
 */
case class ScratchpadConfig(val p: PMUParams) extends AbstractConfig {
//  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor
//
//  var mode = if (config.isDefined) UInt(config.get.banking.mode, width=2) else UInt(width=2)
//  var strideLog2 = if (config.isDefined) UInt(config.get.banking.strideLog2, width=log2Up(log2Up(d)-log2Up(v))) else UInt(width = log2Up(log2Up(d) - log2Up(v)))
//  var bufSize = if (config.isDefined) {
//    // Clamp bufSize value to be at least 1
//    // If bufSize is 1, the Scratchpad is configured as a FIFO
//    // The JSON specifies the "numBufs" field - this should be set to at least (d/v) to configure scratchpad as FIFO
//    // It can be set to an outrageous number (say INT_MAX) to be safe
//    if (config.get.numBufs == 0) {
//      UInt(1, width=log2Up(d/v)+1)
//    } else UInt(math.max(1, d / (v*config.get.numBufs)), width=log2Up(d/v)+1)
//  } else UInt(width = log2Up(d/v))
//
//  val isReadFifo = if (config.isDefined) Bool(config.get.isReadFifo > 0) else Bool()
//  val isWriteFifo = if (config.isDefined) Bool(config.get.isWriteFifo > 0) else Bool()
  val mode = UInt(2.W)  // TODO: 2 ?
  val strideLog2 = UInt((log2Up(log2Up(p.d) - log2Up(p.v))).W)
  val isReadFifo = Bool()
  val isWriteFifo = Bool()
  val bufSize = UInt(log2Up(p.d/p.v).W)
  val localWaddrMax = UInt((log2Up(p.d/p.v)+1).W) // localWaddrMax = if (inst.numBufs == 0) 0 else bankSize - (bankSize % inst.numBufs)
  val localRaddrMax = UInt((log2Up(p.d/p.v)+1).W) // if (inst.numBufs == 0) 0 else bankSize - (bankSize % inst.numBufs)
  val fifoSize = UInt((log2Up(p.d)+1).W)
//  val fifoSize = UInt((log2Up(p.d)+1).W)
  override def cloneType(): this.type = new ScratchpadConfig(p).asInstanceOf[this.type]
}

/**
 * PCUControlBox config register format
 */
case class PCUControlBoxConfig(val p: PCUParams) extends AbstractConfig {

  val tokenInAndTree = Vec(p.numControlIn, Bool())
  val fifoAndTree = Vec(p.numScalarIn+p.numVectorIn, Bool())
  val andTreeTop = Vec(2, Bool())
  val siblingAndTree = Vec(p.numUDCs, Bool())
  val streamingMuxSelect = Bool()
  val incrementXbar = CrossbarConfig(ControlSwitchParams(p.numControlIn, p.numUDCs))
  val doneXbar = CrossbarConfig(ControlSwitchParams(p.numCounters, 1))
  val swapWriteXbar = CrossbarConfig(ControlSwitchParams(p.numControlIn, p.numScalarIn))
  val tokenOutXbar = CrossbarConfig(ControlSwitchParams(3, p.numControlOut))

  override def cloneType(): this.type = {
    new PCUControlBoxConfig(p).asInstanceOf[this.type]
  }
}

case class SwitchCUControlBoxConfig(val p: SwitchCUParams) extends AbstractConfig {

  val siblingAndTree = Vec(p.numUDCs, Bool())
  val childrenAndTree = Vec(p.numUDCs, Bool())
  val udcDecSelect = Vec(p.numUDCs, Bool())
  val incrementXbar = CrossbarConfig(ControlSwitchParams(p.numControlIn, p.numUDCs))
  val doneXbar = CrossbarConfig(ControlSwitchParams(p.numCounters, 1))
  val swapWriteXbar = CrossbarConfig(ControlSwitchParams(p.numControlIn, p.numScalarIn))
  val tokenOutXbar = CrossbarConfig(ControlSwitchParams(3, p.numControlOut))
  val pulserMax = UInt(p.udCtrWidth.W)

  override def cloneType(): this.type = {
    new SwitchCUControlBoxConfig(p).asInstanceOf[this.type]
  }
}


case class PlasticineConfig(
  cuParams:    Array[Array[CUParams]],
  vectorParams: Array[Array[VectorSwitchParams]],
  scalarParams: Array[Array[ScalarSwitchParams]],
  controlParams: Array[Array[ControlSwitchParams]],
  switchCUParams: Array[Array[SwitchCUParams]],
  p: PlasticineParams,
  f: FringeParams) extends AbstractConfig {

  val cu = HVec.tabulate(cuParams.size) { i => HVec.tabulate(cuParams(i).size) { j =>
    cuParams(i)(j) match {
      case p:PCUParams => new PCUConfig(p)
      case p:PMUParams => new PMUConfig(p)
    }
  } }

  // Dummy placeholders for real switch config interface
  val vectorSwitch = HVec.tabulate(vectorParams.size) { i => HVec.tabulate(vectorParams(i).size) { j => new CrossbarConfig(vectorParams(i)(j)) } }
  val scalarSwitch = HVec.tabulate(scalarParams.size) { i => HVec.tabulate(scalarParams(i).size) { j => new CrossbarConfig(scalarParams(i)(j)) } }
  val controlSwitch = HVec.tabulate(controlParams.size) { i => HVec.tabulate(controlParams(i).size) { j => new CrossbarConfig(controlParams(i)(j)) } }

  val switchCU = HVec.tabulate(switchCUParams.size) { i => HVec.tabulate(switchCUParams(i).size) { j => new SwitchCUConfig(switchCUParams(i)(j)) } }

  val argOutMuxSelect = HVec.tabulate(f.numArgOuts) { i => UInt(log2Up(p.numArgOutSelections(i)).W) }
  override def cloneType(): this.type = {
    new PlasticineConfig(cuParams, vectorParams, scalarParams, controlParams, switchCUParams, p, f).asInstanceOf[this.type]
  }
}


