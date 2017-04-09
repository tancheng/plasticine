// See LICENSE for license details.

package plasticine.templates
import plasticine.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * Dummy test harness
 */
class DummyUnitTester(c: Dummy)(implicit args: Array[String]) extends ArgsTester(c)

object DummyTest extends CommonMain {
  type DUTType = Dummy
  def dut = () => new Dummy(args(0).toInt)
  def tester = { c: DUTType => new DummyUnitTester(c) }
}
