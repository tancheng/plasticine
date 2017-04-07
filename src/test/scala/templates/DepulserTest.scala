// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * Depulser test harness
 */
class DepulserUnitTester(c: Depulser)(implicit args: Array[String]) extends ArgsTester(c) {
  poke(c.io.in, 1)
  for (i <- 0 until 5) {
    peek(c.io.out)
    val expectedVal = if (i == 0) 0 else 1
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 0)
  for (i <- 0 until 5) {
    val expectedVal = 1
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.rst, 1)
  step(1)
  expect(c.io.out, 0)
  step(5)
  poke(c.io.in, 1)
  step(1)
  expect(c.io.out, 0)
  poke(c.io.rst, 0)
  step(1)
  expect(c.io.out, 1)
  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 1)
}

object DepulserTest extends CommonMain {
  type DUTType = Depulser
  def dut = () => new Depulser
  def tester = { c: DUTType => new DepulserUnitTester(c) }
}

