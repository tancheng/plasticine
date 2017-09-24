package plasticine.pisa.codegen

import plasticine.pisa.ir._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.pisa.Traversal
import templates.Opcodes
import templates.Utils.log2Up

import scala.reflect.runtime.universe._
import scala.collection.mutable.Set

import chisel3._

class ConfigInitializer() extends Traversal {

  def checkFriendship(node: AbstractBits, cnode: AbstractConfig): Unit = {
    node match {
      case n: CounterChainBits => Predef.assert(cnode.isInstanceOf[CounterChainConfig])
      case n: CounterRCBits   => Predef.assert(cnode.isInstanceOf[CounterConfig])
      case n: CrossbarBits    => Predef.assert(cnode.isInstanceOf[CrossbarConfig])
      case n: PCUBits         => Predef.assert(cnode.isInstanceOf[PCUConfig])
      case n: PMUBits         => Predef.assert(cnode.isInstanceOf[PMUConfig])
      case n: DummyPMUBits         => Predef.assert(cnode.isInstanceOf[DummyPMUConfig])
      case n: ScratchpadBits         => Predef.assert(cnode.isInstanceOf[ScratchpadConfig])
      case n: PipeStageBits   => Predef.assert(cnode.isInstanceOf[PipeStageConfig])
      case n: PlasticineBits  => Predef.assert(cnode.isInstanceOf[PlasticineConfig])
      case n: PCUControlBoxBits => Predef.assert(cnode.isInstanceOf[PCUControlBoxConfig])
      case n: PMUControlBoxBits => Predef.assert(cnode.isInstanceOf[PMUControlBoxConfig])
      case n: SwitchCUControlBoxBits => Predef.assert(cnode.isInstanceOf[SwitchCUControlBoxConfig])
      case n: ScalarCUControlBoxBits => Predef.assert(cnode.isInstanceOf[ScalarCUControlBoxConfig])
      case n: SwitchCUBits => Predef.assert(cnode.isInstanceOf[SwitchCUConfig])
      case n: ScalarCUBits => Predef.assert(cnode.isInstanceOf[ScalarCUConfig])
      case n: MemoryChannelBits => Predef.assert(cnode.isInstanceOf[MemoryChannelConfig])
      case n: SrcValueTuple   => Predef.assert(cnode.isInstanceOf[SrcValueBundle])
      case _ => throw new Exception(s"Unknown node $node")
    }
  }

  def encodeOneHot(x: Int): Int = 1 << x
  def encodeOneHot(x: List[Int]): Int = if (x.size == 0) 0 else x.map {encodeOneHot(_) }.reduce {_|_}

  def toBinary[TP<:Any](x: TP, w: Int): List[Int] = x match {
    case num: Int => List.tabulate(w) { j => if (BigInt(num).testBit(j)) 1 else 0 }
    case num: Float => toBinary(java.lang.Float.floatToRawIntBits(num), w)
    case l: List[Any] => l.map { e => toBinary(e, w/l.size) }.flatten
    case l: Array[Int] => l.map { e => toBinary(e, w/l.size) }.toList.flatten
//    case s: AbstractBits => s.toBinary
    case f@_ => throw new Exception(s"Unsupported type for toBinary: (${x.getClass()})")
  }

  def init(node: AbstractBits, cnode: AbstractConfig) : Unit = {
    checkFriendship(node, cnode)

    (node, cnode) match {
      case (n: CounterChainBits, cn: CounterChainConfig)  =>
        for(i <- 0 until cn.counters.size) { init(n.counters(i), cn.counters(i)) }
        cn.chain.zip(n.chain) foreach { case (wire, value) => wire := value.U }
      case (n: CounterRCBits, cn: CounterConfig)          =>
        cn.par := n.par.U
        init(n.stride, cn.stride)
        init(n.max, cn.max)
      case (n: CrossbarBits, cn: CrossbarConfig)          =>
        cn.outSelect.zip(n.outSelect) foreach { case (wire, value) => wire := (1+value).U }  // 0'th input is always 0
      case (n: PCUBits, cn: PCUConfig)                    =>
        cn.accumInit := (n.accumInit match {
          case i: Int => i.U
          case f: Float => java.lang.Float.floatToRawIntBits(f).U
          case b: Boolean => if (b) 1.U else 0.U
          case _ => throw new Exception(s"[ERROR] Unsupported accumulator type ${n.accumInit}")
        })
        cn.fifoNbufConfig.zip(n.fifoNbufConfig) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        init(n.scalarOutXbar, cn.scalarOutXbar)
        init(n.scalarInXbar, cn.scalarInXbar)
        init(n.control, cn.control)
        for (i <- 0 until cn.vectorValidOut.size) init(n.vectorValidOut(i), cn.vectorValidOut(i))
        for (i <- 0 until cn.scalarValidOut.size) init(n.scalarValidOut(i), cn.scalarValidOut(i))
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }
      case (n: PMUBits, cn: PMUConfig)                    =>
        cn.fifoNbufConfig.zip(n.fifoNbufConfig) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        cn.rdataEnable.zip(n.rdataEnable) foreach { case (wire, value) => wire := (value > 0).B }
        init(n.raddrSelect, cn.raddrSelect)
        init(n.waddrSelect, cn.waddrSelect)
        cn.wdataSelect := n.wdataSelect.U
        init(n.scratchpad, cn.scratchpad)
        init(n.scalarOutXbar, cn.scalarOutXbar)
        init(n.scalarInXbar, cn.scalarInXbar)
        init(n.control, cn.control)
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }
      case (n: DummyPMUBits, cn: DummyPMUConfig)                    =>
        cn.fifoNbufConfig.zip(n.fifoNbufConfig) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        cn.rdataEnable.zip(n.rdataEnable) foreach { case (wire, value) => wire := (value > 0).B }
        init(n.raddrSelect, cn.raddrSelect)
        init(n.waddrSelect, cn.waddrSelect)
        cn.wdataSelect := n.wdataSelect.U
        init(n.scratchpad, cn.scratchpad)
        init(n.scalarOutXbar, cn.scalarOutXbar)
        init(n.scalarInXbar, cn.scalarInXbar)
        init(n.control, cn.control)
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }
      case (n: SwitchCUBits, cn: SwitchCUConfig)                    =>
        cn.fifoNbufConfig.zip(n.fifoNbufConfig) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        init(n.control, cn.control)
        init(n.counterChain, cn.counterChain)

      case (n: ScalarCUBits, cn: ScalarCUConfig)                    =>
        cn.fifoNbufConfig.zip(n.fifoNbufConfig) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        init(n.scalarOutXbar, cn.scalarOutXbar)
        init(n.scalarInXbar, cn.scalarInXbar)
        init(n.control, cn.control)
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }

      case (n: PipeStageBits, cn: PipeStageConfig)        =>
        init(n.enableSelect, cn.enableSelect)
        cn.regEnables := encodeOneHot(n.regEnables).U
        cn.result := encodeOneHot(n.result).U
        cn.opcode := Opcodes.getCode(n.opcode).U
        init(n.opC, cn.opC)
        init(n.opB, cn.opB)
        init(n.opA, cn.opA)

      case (n: ScratchpadBits, cn: ScratchpadConfig)        =>
        val bankSize= cn.p.d / cn.p.v
        cn.fifoSize := n.fifoSize.U
        cn.localRaddrMax := (if (n.numBufs == 0) 0.U else (bankSize - (bankSize % n.numBufs)).U)
        cn.localWaddrMax := (if (n.numBufs == 0) 0.U else (bankSize - (bankSize % n.numBufs)).U)
        cn.bufSize := (if (n.numBufs == 0) 1.U else (math.max(1, cn.p.d / (cn.p.v*n.numBufs))).U)
        cn.isWriteFifo := (n.isWriteFifo > 0).B
        cn.isReadFifo := (n.isReadFifo > 0).B
        cn.strideLog2 := (if (n.stride == 1) 0.U else log2Up(n.stride).U)
        cn.mode := n.mode.U

      case (n: PCUControlBoxBits, cn: PCUControlBoxConfig)      =>
        cn.udcInit.zip(n.udcInit) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.doneXbar, cn.doneXbar)
        init(n.incrementXbar, cn.incrementXbar)
        cn.streamingMuxSelect := n.streamingMuxSelect.U
        cn.siblingAndTree.zip(n.siblingAndTree) foreach { case (wire, value) => wire := value.U }
        cn.fifoAndTree.zip(n.fifoAndTree) foreach { case (wire, value) => wire := value.U }
        cn.tokenInAndTree.zip(n.tokenInAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: PMUControlBoxBits, cn: PMUControlBoxConfig)      =>
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.readDoneXbar, cn.readDoneXbar)
        init(n.writeDoneXbar, cn.writeDoneXbar)
        cn.scalarSwapReadSelect.zip(n.scalarSwapReadSelect) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        cn.readFifoAndTree.zip(n.readFifoAndTree) foreach { case (wire, value) => wire := value.U }
        cn.writeFifoAndTree.zip(n.writeFifoAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: SwitchCUControlBoxBits, cn: SwitchCUControlBoxConfig)      =>
        cn.udcInit.zip(n.udcInit) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        cn.pulserMax := n.pulserMax.U
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.doneXbar, cn.doneXbar)
        init(n.incrementXbar, cn.incrementXbar)
        cn.udcDecSelect.zip(n.udcDecSelect) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        cn.childrenAndTree.zip(n.childrenAndTree) foreach { case (wire, value) => wire := value.U }
        cn.siblingAndTree.zip(n.siblingAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: ScalarCUControlBoxBits, cn: ScalarCUControlBoxConfig)      =>
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.doneXbar, cn.doneXbar)
        init(n.incrementXbar, cn.incrementXbar)
        cn.streamingMuxSelect := n.streamingMuxSelect.U
        cn.siblingAndTree.zip(n.siblingAndTree) foreach { case (wire, value) => wire := value.U }
        cn.fifoAndTree.zip(n.fifoAndTree) foreach { case (wire, value) => wire := value.U }
        cn.tokenInAndTree.zip(n.tokenInAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: MemoryChannelBits, cn: MemoryChannelConfig)      =>
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.tokenInXbar, cn.tokenInXbar)
        init(n.scalarInXbar, cn.scalarInXbar)
      case (n: PlasticineBits, cn: PlasticineConfig)      =>
        // argOutMuxSelect
        cn.doneSelect := n.doneSelect.U
        cn.argOutMuxSelect.zip(n.argOutMuxSelect) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
        for(i <- 0 until cn.memoryChannel.size) {
          for(j <- 0 until cn.memoryChannel(i).size) {
            init(n.memoryChannel(i)(j), cn.memoryChannel(i)(j))
          }
        }
        for(i <- 0 until cn.scalarCU.size) {
          for(j <- 0 until cn.scalarCU(i).size) {
            init(n.scalarCU(i)(j), cn.scalarCU(i)(j))
          }
        }
        for(i <- 0 until cn.switchCU.size) {
          for(j <- 0 until cn.switchCU(i).size) {
            init(n.switchCU(i)(j), cn.switchCU(i)(j))
          }
        }
        for(i <- 0 until cn.controlSwitch.size) {
          for(j <- 0 until cn.controlSwitch(i).size) {
            init(n.controlSwitch(i)(j), cn.controlSwitch(i)(j))
          }
        }
        for(i <- 0 until cn.scalarSwitch.size) {
          for(j <- 0 until cn.scalarSwitch(i).size) {
            init(n.scalarSwitch(i)(j), cn.scalarSwitch(i)(j))
          }
        }
        for(i <- 0 until cn.vectorSwitch.size) {
          for(j <- 0 until cn.vectorSwitch(i).size) {
            init(n.vectorSwitch(i)(j), cn.vectorSwitch(i)(j))
          }
        }
        for(i <- 0 until cn.cu.size) {
          for(j <- 0 until cn.cu(i).size) {
            init(n.cu(i)(j), cn.cu(i)(j))
          }
        }
      case (n: SrcValueTuple, cn: SrcValueBundle)         =>
        // First value, then src
        n.value match {
          case num: Int => cn.value := (if(num == -1) 0.U else num.U)
          case num: Float => cn.value := java.lang.Float.floatToRawIntBits(num).U
          case _ => throw new Exception(s"Unsupported initializer value ${n.value}")
        }
        (n.src match {
          case s: SelectSource => cn.src := cn.srcIdx(s).U
          case _ => throw new Exception(s"Initializer is not a SelectSource")
        })
     case _ =>
        throw new Exception(s"Don't know how to visit node combo ($node, $cnode)")
    }
  }

}
