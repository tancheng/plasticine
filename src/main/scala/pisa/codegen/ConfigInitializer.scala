package plasticine.pisa.codegen

import plasticine.pisa.ir._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.pisa.Traversal
import plasticine.templates.Opcodes

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
      case n: PipeStageBits   => Predef.assert(cnode.isInstanceOf[PipeStageConfig])
      case n: PlasticineBits  => Predef.assert(cnode.isInstanceOf[PlasticineConfig])
      case n: PCUControlBoxBits => Predef.assert(cnode.isInstanceOf[PCUControlBoxConfig])
      case n: SwitchCUControlBoxBits => Predef.assert(cnode.isInstanceOf[SwitchCUControlBoxConfig])
      case n: SwitchCUBits => Predef.assert(cnode.isInstanceOf[SwitchCUConfig])
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
        init(n.stride, cn.stride)
        init(n.max, cn.max)
      case (n: CrossbarBits, cn: CrossbarConfig)          =>
        cn.outSelect.zip(n.outSelect) foreach { case (wire, value) => wire := value.U }
      case (n: PCUBits, cn: PCUConfig)                    =>
        init(n.control, cn.control)
        for (i <- 0 until cn.vectorValidOut.size) init(n.vectorValidOut(i), cn.vectorValidOut(i))
        for (i <- 0 until cn.scalarValidOut.size) init(n.scalarValidOut(i), cn.scalarValidOut(i))
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }
      case (n: PMUBits, cn: PMUConfig)                    =>
        init(n.counterChain, cn.counterChain)
        for(i <- 0 until cn.stages.size) { init(n.stages(i), cn.stages(i)) }
      case (n: SwitchCUBits, cn: SwitchCUConfig)                    =>
        init(n.control, cn.control)
        init(n.counterChain, cn.counterChain)

      case (n: PipeStageBits, cn: PipeStageConfig)        =>
        cn.regEnables := encodeOneHot(n.regEnables).U
        cn.result := encodeOneHot(n.result).U
        cn.opcode := Opcodes.getCode(n.opcode).U
        init(n.opC, cn.opC)
        init(n.opB, cn.opB)
        init(n.opA, cn.opA)

      case (n: PCUControlBoxBits, cn: PCUControlBoxConfig)      =>
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.doneXbar, cn.doneXbar)
        init(n.incrementXbar, cn.incrementXbar)
        cn.streamingMuxSelect := n.streamingMuxSelect.U
        cn.siblingAndTree.zip(n.siblingAndTree) foreach { case (wire, value) => wire := value.U }
        cn.andTreeTop.zip(n.andTreeTop) foreach { case (wire, value) => wire := value.U }
        cn.fifoAndTree.zip(n.fifoAndTree) foreach { case (wire, value) => wire := value.U }
        cn.tokenInAndTree.zip(n.tokenInAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: SwitchCUControlBoxBits, cn: SwitchCUControlBoxConfig)      =>
        init(n.tokenOutXbar, cn.tokenOutXbar)
        init(n.swapWriteXbar, cn.swapWriteXbar)
        init(n.doneXbar, cn.doneXbar)
        init(n.incrementXbar, cn.incrementXbar)
        cn.udcDecSelect.zip(n.udcDecSelect) foreach { case (wire, value) => wire := value.U }
        cn.childrenAndTree.zip(n.childrenAndTree) foreach { case (wire, value) => wire := value.U }
        cn.siblingAndTree.zip(n.siblingAndTree) foreach { case (wire, value) => wire := value.U }

      case (n: PlasticineBits, cn: PlasticineConfig)      =>
        // argOutMuxSelect
        cn.argOutMuxSelect.zip(n.argOutMuxSelect) foreach { case (wire, value) => wire := (if (value == -1) 0.U else value.U) }
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
