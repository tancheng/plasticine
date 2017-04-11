// See LICENSE for license details.

package plasticine.templates
import plasticine.templates.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * FF test harness
 */
class CounterCoreUnitTester(c: CounterCore)(implicit args: Array[String]) extends ArgsTester(c)

object CounterCoreTest extends CommonMain {
  type DUTType = CounterCore
  def dut = () => new CounterCore(args(0).toInt)
  def tester = { c: DUTType => new CounterCoreUnitTester(c) }
}

