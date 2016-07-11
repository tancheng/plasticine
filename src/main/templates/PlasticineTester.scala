package plasticine.templates

import Chisel._

import scala.collection.mutable.HashMap

/**
 * Thin wrapper around Chisel's Tester class with a few additional
 * helper functions.
 */
class PlasticineTester[+T <: Module](c: T, isTrace: Boolean = true, _base: Int = 16,
    testCmd: Option[String] = Driver.testCommand,
    dumpFile: Option[String] = None) extends Tester(c, isTrace, _base, testCmd, dumpFile) {
  type Inst = HashMap[String, Int]

  def set(opcode: OpcodeT, value: Inst) = {
    opcode match {
      case n: CounterOpcode =>
        poke(n.max, value("max"))
        poke(n.stride, value("stride"))
        poke(n.maxConst, value("maxConst"))
        poke(n.strideConst, value("strideConst"))
      case n: CounterChainOpcode =>
        poke(n.chain, value("chain"))
      case _ =>
        throw new Exception(s"Unknown opcode $opcode")
    }
  }
}
