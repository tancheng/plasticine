package plasticine.pisa

import plasticine.pisa.ir._
import plasticine.config._
import plasticine.pisa.enums._

import scala.collection.mutable.Set

abstract class Traversal() {
  var isInit = false
  val visited = Set[AbstractBits]()
  val debug = false

//  def msg(x: String) = if (Config.quick) () else System.out.println(x)
//  def dbg(x: String) = if (debug && !Config.dse) System.out.println(x)

  def reset() {
    visited.clear()
    isInit = false
  }

  def run(node: AbstractBits, cnode: AbstractConfig) = {
    initPass()
    visitNode(node, cnode)
    finPass()
  }

  def initPass() = {
    isInit = true
  }

  def visitNode(node: AbstractBits, cnode: AbstractConfig) : Unit = {
    throw new Exception(s"Don't know how to visit node combo ($node, $cnode)")
  }

  def finPass() = {}
}
