package plasticine.pisa.codegen

import plasticine.pisa.ir._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.pisa.Traversal
import templates.Opcodes
import templates.Utils.log2Up

import scala.reflect.runtime.universe._
import scala.collection.mutable.Set

class BinaryCodegen() extends Traversal {

  def checkFriendship(node: AbstractBits, cnode: AbstractConfig): Unit = {
    node match {
      case n: CounterChainBits => Predef.assert(cnode.isInstanceOf[CounterChainConfig])
      case n: CounterRCBits   => Predef.assert(cnode.isInstanceOf[CounterConfig])
      case n: CrossbarBits    => Predef.assert(cnode.isInstanceOf[CrossbarConfig])
      case n: PCUBits         => Predef.assert(cnode.isInstanceOf[PCUConfig])
      case n: PMUBits         => Predef.assert(cnode.isInstanceOf[PMUConfig])
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

  // Get names of case class fields
  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.reverse.map {_.name.toString}

  // Get width of x'th field of this class using corresponding 't' field
//  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth

  def toBinary[TP<:Any](x: TP, w: Int): List[Int] = x match {
    case num: Int => List.tabulate(w) { j => if (BigInt(num).testBit(j)) 1 else 0 }
    case num: Float => toBinary(java.lang.Float.floatToRawIntBits(num), w)
    case l: List[Any] => l.map { e => toBinary(e, w/l.size) }.flatten
    case l: Array[Int] => l.map { e => toBinary(e, w/l.size) }.toList.flatten
//    case s: AbstractBits => s.toBinary
    case f@_ => throw new Exception(s"Unsupported type for toBinary: (${x.getClass()})")
  }

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

  def encodeOneHot(x: Int): Int = 1 << x
  def encodeOneHot(x: List[Int]): Int = if (x.size == 0) 0 else x.map {encodeOneHot(_) }.reduce {_|_}

  def genBinary(node: AbstractBits, cnode: AbstractConfig) : List[Int] = {
    checkFriendship(node, cnode)

    (node, cnode) match {
      case (n: CounterChainBits, cn: CounterChainConfig)  =>
        List.tabulate(cn.counters.size) { i =>
          genBinary(n.counters(i), cn.counters(i))
        }.flatten ++
        toBinary(n.chain, cn.chain.getWidth)
      case (n: CounterRCBits, cn: CounterConfig)          =>
        toBinary(n.par, cn.par.getWidth) ++
        genBinary(n.stride, cn.stride) ++
        genBinary(n.max, cn.max)
      case (n: CrossbarBits, cn: CrossbarConfig)          =>
        toBinary(n.outSelect, cn.outSelect.getWidth)
      case (n: PCUBits, cn: PCUConfig)                    =>
        val accumBits = toBinary(n.accumInit, cn.accumInit.getWidth)
        val fifoNbufBits = toBinary(n.fifoNbufConfig, cn.fifoNbufConfig.getWidth)
        val scalarOutXbarBits = genBinary(n.scalarOutXbar, cn.scalarOutXbar)
        val scalarInXbarBits = genBinary(n.scalarInXbar, cn.scalarInXbar)
        val controlBits = genBinary(n.control, cn.control)
        val vecValidBits = List.tabulate(cn.vectorValidOut.size) { i => genBinary(n.vectorValidOut(i), cn.vectorValidOut(i)) }.flatten
        val scalarValidBits = List.tabulate(cn.scalarValidOut.size) { i => genBinary(n.scalarValidOut(i), cn.scalarValidOut(i)) }.flatten
        val counterBits = genBinary(n.counterChain, cn.counterChain)
        val stageBits = List.tabulate(cn.stages.size) { i =>
          genBinary(n.stages(i), cn.stages(i))
        }.flatten
//        println(s"[PCUBits] counterBits = $counterBits")
//        println(s"[PCUBits] stageBits = $stageBits")
        accumBits ++ fifoNbufBits ++ scalarOutXbarBits ++ scalarInXbarBits ++ controlBits ++ vecValidBits ++ scalarValidBits ++ counterBits ++ stageBits
      case (n: PMUBits, cn: PMUConfig)                    =>
        toBinary(n.fifoNbufConfig, cn.fifoNbufConfig.getWidth) ++
        toBinary(n.rdataEnable, cn.rdataEnable.getWidth) ++
        genBinary(n.raddrSelect, cn.raddrSelect) ++
        genBinary(n.waddrSelect, cn.waddrSelect) ++
        toBinary(n.wdataSelect, cn.wdataSelect.getWidth) ++
        genBinary(n.scratchpad, cn.scratchpad) ++
        genBinary(n.scalarOutXbar, cn.scalarOutXbar) ++
        genBinary(n.scalarInXbar, cn.scalarInXbar) ++
        genBinary(n.control, cn.control) ++
        genBinary(n.counterChain, cn.counterChain) ++
        List.tabulate(cn.stages.size) { i =>
          genBinary(n.stages(i), cn.stages(i))
        }.flatten
      case (n: SwitchCUBits, cn: SwitchCUConfig)                    =>
        toBinary(n.fifoNbufConfig, cn.fifoNbufConfig.getWidth) ++
        genBinary(n.control, cn.control) ++
        genBinary(n.counterChain, cn.counterChain)

      case (n: ScalarCUBits, cn: ScalarCUConfig)                    =>
        toBinary(n.fifoNbufConfig, cn.fifoNbufConfig.getWidth) ++
        genBinary(n.scalarOutXbar, cn.scalarOutXbar) ++
        genBinary(n.scalarInXbar, cn.scalarInXbar) ++
        genBinary(n.control, cn.control) ++
        genBinary(n.counterChain, cn.counterChain) ++
        List.tabulate(cn.stages.size) { i =>
          genBinary(n.stages(i), cn.stages(i))
        }.flatten

      case (n: PipeStageBits, cn: PipeStageConfig)        =>
//        toBinary(encodeOneHot(n.result), cn.result.getWidth) ++
//        toBinary(5, cn.opcode.getWidth)
      val valueBin = genBinary(n.enableSelect, cn.enableSelect) ++ toBinary(encodeOneHot(n.regEnables), cn.regEnables.getWidth) ++ toBinary(encodeOneHot(n.result), cn.result.getWidth) ++ toBinary(Opcodes.getCode(n.opcode), cn.opcode.getWidth) ++ genBinary(n.opC, cn.opC) ++ genBinary(n.opB, cn.opB) ++ genBinary(n.opA, cn.opA)

//      println(s"[PipeStageBits] $valueBin")
      valueBin
//      valueBin ++ toBinary(0, cn.opA.src.getWidth)

      case (n: ScratchpadBits, cn: ScratchpadConfig)        =>
        val bankSize= cn.p.d / cn.p.v
        toBinary(n.fifoSize, cn.fifoSize.getWidth) ++
        toBinary((if (n.numBufs == 0) 0 else (bankSize - (bankSize % n.numBufs))), cn.localRaddrMax.getWidth) ++
        toBinary((if (n.numBufs == 0) 0 else (bankSize - (bankSize % n.numBufs))), cn.localWaddrMax.getWidth) ++
        toBinary((if (n.numBufs == 0) 1 else (math.max(1, cn.p.d / (cn.p.v*n.numBufs)))), cn.bufSize.getWidth) ++
        toBinary(n.isWriteFifo, cn.isWriteFifo.getWidth) ++
        toBinary(n.isReadFifo, cn.isReadFifo.getWidth) ++
        toBinary((if (n.stride == 1) 0 else log2Up(n.stride)), cn.strideLog2.getWidth) ++
        toBinary(n.mode, cn.mode.getWidth)

      case (n: PCUControlBoxBits, cn: PCUControlBoxConfig)      =>
        toBinary(n.udcInit, cn.udcInit.getWidth) ++
        genBinary(n.tokenOutXbar, cn.tokenOutXbar) ++
        genBinary(n.swapWriteXbar, cn.swapWriteXbar) ++
        genBinary(n.doneXbar, cn.doneXbar) ++
        genBinary(n.incrementXbar, cn.incrementXbar) ++
        toBinary(n.streamingMuxSelect, cn.streamingMuxSelect.getWidth) ++
        toBinary(n.siblingAndTree, cn.siblingAndTree.getWidth) ++
        toBinary(n.fifoAndTree, cn.fifoAndTree.getWidth) ++
        toBinary(n.tokenInAndTree, cn.tokenInAndTree.getWidth)

      case (n: PMUControlBoxBits, cn: PMUControlBoxConfig)      =>
        genBinary(n.tokenOutXbar, cn.tokenOutXbar) ++
        genBinary(n.swapWriteXbar, cn.swapWriteXbar) ++
        genBinary(n.readDoneXbar, cn.readDoneXbar) ++
        genBinary(n.writeDoneXbar, cn.writeDoneXbar) ++
        toBinary(n.scalarSwapReadSelect, cn.scalarSwapReadSelect.getWidth) ++
        toBinary(n.readFifoAndTree, cn.readFifoAndTree.getWidth) ++
        toBinary(n.writeFifoAndTree, cn.writeFifoAndTree.getWidth)

      case (n: SwitchCUControlBoxBits, cn: SwitchCUControlBoxConfig)      =>
        toBinary(n.udcInit, cn.udcInit.getWidth) ++
        toBinary(n.pulserMax, cn.pulserMax.getWidth) ++
        genBinary(n.tokenOutXbar, cn.tokenOutXbar) ++
        genBinary(n.swapWriteXbar, cn.swapWriteXbar) ++
        genBinary(n.doneXbar, cn.doneXbar) ++
        genBinary(n.incrementXbar, cn.incrementXbar) ++
        toBinary(if (n.udcDecSelect == -1) 0 else n.udcDecSelect, cn.udcDecSelect.getWidth) ++
        toBinary(n.childrenAndTree, cn.childrenAndTree.getWidth) ++
        toBinary(n.siblingAndTree, cn.siblingAndTree.getWidth)

      case (n: ScalarCUControlBoxBits, cn: ScalarCUControlBoxConfig)      =>
        genBinary(n.tokenOutXbar, cn.tokenOutXbar) ++
        genBinary(n.swapWriteXbar, cn.swapWriteXbar) ++
        genBinary(n.doneXbar, cn.doneXbar) ++
        genBinary(n.incrementXbar, cn.incrementXbar) ++
        toBinary(n.streamingMuxSelect, cn.streamingMuxSelect.getWidth) ++
        toBinary(n.siblingAndTree, cn.siblingAndTree.getWidth) ++
        toBinary(n.fifoAndTree, cn.fifoAndTree.getWidth) ++
        toBinary(n.tokenInAndTree, cn.tokenInAndTree.getWidth)

      case (n: MemoryChannelBits, cn: MemoryChannelConfig)      =>
        genBinary(n.tokenOutXbar, cn.tokenOutXbar) ++
        genBinary(n.tokenInXbar, cn.tokenInXbar) ++
        genBinary(n.scalarInXbar, cn.scalarInXbar)

      case (n: PlasticineBits, cn: PlasticineConfig)      =>
        // argOutMuxSelect
        toBinary(n.doneSelect, cn.doneSelect.getWidth) ++
        toBinary(n.argOutMuxSelect, cn.argOutMuxSelect.getWidth) ++
        List.tabulate(cn.memoryChannel.size) { i =>
          List.tabulate(cn.memoryChannel(i).size) { j =>
            genBinary(n.memoryChannel(i)(j), cn.memoryChannel(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.scalarCU.size) { i =>
          List.tabulate(cn.scalarCU(i).size) { j =>
            genBinary(n.scalarCU(i)(j), cn.scalarCU(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.switchCU.size) { i =>
          List.tabulate(cn.switchCU(i).size) { j =>
            genBinary(n.switchCU(i)(j), cn.switchCU(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.controlSwitch.size) { i =>
          List.tabulate(cn.controlSwitch(i).size) { j =>
            genBinary(n.controlSwitch(i)(j), cn.controlSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.scalarSwitch.size) { i =>
          List.tabulate(cn.scalarSwitch(i).size) { j =>
            genBinary(n.scalarSwitch(i)(j), cn.scalarSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.vectorSwitch.size) { i =>
          List.tabulate(cn.vectorSwitch(i).size) { j =>
            genBinary(n.vectorSwitch(i)(j), cn.vectorSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(cn.cu.size) { i =>
          List.tabulate(cn.cu(i).size) { j =>
            genBinary(n.cu(i)(j), cn.cu(i)(j))
          }.flatten
        }.flatten
//        List.tabulate(n.productArity) { i =>
//          val idx = n.productArity - 2 - i
//          val elem = n.productElement(idx)
//          val cnElem = cn.elements(classAccessors[PlasticineBits].apply(idx))
//          genBinary(elem, cnElem)
//        }.flatten
      case (n: SrcValueTuple, cn: SrcValueBundle)         =>
        // First value, then src
//        println(s"[SrcValueTuple] src = ${cn.srcIdx(n.src.asInstanceOf[SelectSource])}, value = ${n.value}")
        toBinary(n.value, cn.value.getWidth) ++
        (n.src match {
          case s: SelectSource => toBinary(cn.srcIdx(s), cn.src.getWidth)
          case _ => List[Int]()
        })
//        List.tabulate(n.productArity) { i =>
//          val idx = n.productArity - 1 - i
//          val elem = n.productElement(idx)
//          println(s"[SrcValueTuple] elements = ${cn.elements}")
//          val w = cn.elements(classAccessors[SrcValueTuple].apply(idx)).getWidth
//          elem match {
//            case s: SelectSource => toBinary(cn.srcIdx(s), w)
//            case _ => toBinary(elem, w)
//          }
//        }.flatten
     case _ =>
        throw new Exception(s"Don't know how to visit node combo ($node, $cnode)")
    }
  }

}
