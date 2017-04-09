// See LICENSE for license details.

package plasticine.templates
import plasticine.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * Pulser test harness
 */
class PulserUnitTester(c: Pulser)(implicit args: Array[String]) extends ArgsTester(c) {
  poke(c.io.in, 1)
  for (i <- 0 until 5) {
    peek(c.io.out)
    val expectedVal = if (i == 0) 1 else 0
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 0)
  for (i <- 0 until 5) {
    val expectedVal = 0
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 1)
  expect(c.io.out, 1)
  step(1)
  expect(c.io.out, 0)
  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 0)
}

object PulserTest extends CommonMain {
  type DUTType = Pulser
  def dut = () => new Pulser
  def tester = { c: DUTType => new PulserUnitTester(c) }
}

