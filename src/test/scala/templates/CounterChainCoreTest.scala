// See LICENSE for license details.

package plasticine.templates
import plasticine.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * FF test harness
 */
class CounterChainUnitTester(c: CounterChainCore)(implicit args: Array[String]) extends ArgsTester(c)

object CounterChainCoreTest extends CommonMain {
  type DUTType = CounterChainCore
  def dut = () => new CounterChainCore(args(0).toInt, args(1).toInt)
  def tester = { c: DUTType => new CounterChainUnitTester(c) }
}

