package plasticine.pisa.codegen

import plasticine.pisa.ir._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.pisa.Traversal

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
      case n: PipeStageBits   => Predef.assert(cnode.isInstanceOf[PipeStageConfig])
      case n: PlasticineBits  => Predef.assert(cnode.isInstanceOf[PlasticineConfig])
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
    case l: Array[Any] => l.map { e => toBinary(e, w/l.size) }.toList.flatten
    case s: AbstractBits => s.toBinary
    case _ => throw new Exception("Unsupported type for toBinary")
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
  def encodeOneHot(x: List[Int]): Int = x.map {encodeOneHot(_) }.reduce {_|_}

  def genBinary(node: AbstractBits, cnode: AbstractConfig) : List[Int] = {
    checkFriendship(node, cnode)

    (node, cnode) match {
      case (n: CounterChainBits, cn: CounterChainConfig)  =>
        List.tabulate(n.counters.size) { i =>
          genBinary(n.counters(i), cn.counters(i))
        }.flatten ++
        toBinary(n.chain, cn.chain.getWidth)
      case (n: CounterRCBits, cn: CounterConfig)          =>
        genBinary(n.stride, cn.stride) ++
        genBinary(n.max, cn.max)
      case (n: CrossbarBits, cn: CrossbarConfig)          =>
        toBinary(n.outSelect, cn.outSelect.getWidth)
      case (n: PCUBits, cn: PCUConfig)                    =>
        genBinary(n.counterChain, cn.counterChain) ++
        List.tabulate(n.stages.size) { i =>
          genBinary(n.stages(i), cn.stages(i))
        }.flatten
      case (n: PMUBits, cn: PMUConfig)                    =>
        genBinary(n.counterChain, cn.counterChain) ++
        List.tabulate(n.stages.size) { i =>
          genBinary(n.stages(i), cn.stages(i))
        }.flatten
      case (n: PipeStageBits, cn: PipeStageConfig)        =>
        toBinary(encodeOneHot(n.result), cn.result.getWidth) ++
        toBinary(0xFF, cn.opcode.getWidth) ++
        genBinary(n.opC, cn.opC) ++
        genBinary(n.opB, cn.opB) ++
        genBinary(n.opA, cn.opA)
//        List.tabulate(n.productArity) { i =>
//          val idx = n.productArity - 1 - i
//          val elem = n.productElement(idx)
//          val w = cn.elements(classAccessors[PipeStageBits].apply(idx)).getWidth
//          toBinary(elem, w)
//        }.flatten
      case (n: PlasticineBits, cn: PlasticineConfig)      =>
        // argOutMuxSelect
        toBinary(n.argOutMuxSelect, cn.argOutMuxSelect.getWidth) ++
        List.tabulate(n.controlSwitch.size) { i =>
          List.tabulate(n.controlSwitch(i).size) { j =>
            genBinary(n.controlSwitch(i)(j), cn.controlSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(n.scalarSwitch.size) { i =>
          List.tabulate(n.scalarSwitch(i).size) { j =>
            genBinary(n.scalarSwitch(i)(j), cn.scalarSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(n.vectorSwitch.size) { i =>
          List.tabulate(n.vectorSwitch(i).size) { j =>
            genBinary(n.vectorSwitch(i)(j), cn.vectorSwitch(i)(j))
          }.flatten
        }.flatten ++
        List.tabulate(n.cu.size) { i =>
          List.tabulate(n.cu(i).size) { j =>
            genBinary(n.cu(i)(j), cn.cu(i)(j))
          }.flatten
        }.flatten
//        List.tabulate(n.productArity) { i =>
//          val idx = n.productArity - 1 - i
//          val elem = n.productElement(idx)
//          val cnElem = cn.elements(classAccessors[PlasticineBits].apply(idx))
//          genBinary(elem, cnElem)
//        }.flatten
      case (n: SrcValueTuple, cn: SrcValueBundle)         =>
        List.tabulate(n.productArity) { i =>
          val idx = n.productArity - 1 - i
          val elem = n.productElement(idx)
          val w = cn.elements(classAccessors[SrcValueTuple].apply(idx)).getWidth
          elem match {
            case s: SelectSource => toBinary(cn.srcIdx(s), w)
            case _ => toBinary(elem, w)
          }
        }.flatten
     case _ =>
    }
    throw new Exception(s"Don't know how to visit node combo ($node, $cnode)")
  }

}
