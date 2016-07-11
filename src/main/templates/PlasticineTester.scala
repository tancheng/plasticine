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

}
