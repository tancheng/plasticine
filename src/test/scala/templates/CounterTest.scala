// See LICENSE for license details.

package plasticine.templates
//import plasticine.templates.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.language.reflectiveCalls
import scala.language.postfixOps

/**
 * Counter test harness
 */
class CounterUnitTester(c: Counter)(implicit args: Array[String]) extends ArgsTester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 6
  val saturate = 0

  poke(c.io.max, max)
  poke(c.io.stride, stride)
  poke(c.io.enable, 1)
  poke(c.io.saturate, saturate)
  poke(c.io.reset, 0)

  val expectedCounts = 0 until max by stride toList

  var numEnabledCycles = 0
  var expectedCount = 0
  var expectedDone = 0
  def testOneStep() = {
    step(1)
    numEnabledCycles += 1
    val (count, done) = saturate match {
      case 1 =>
        val count = if (numEnabledCycles < expectedCounts.size) expectedCounts(numEnabledCycles) else expectedCounts.last
        val done = if (count == expectedCounts.last) 1 else 0
        (count, done)
      case 0 =>
        val count = expectedCounts(numEnabledCycles % expectedCounts.size)
        val done = if (count == expectedCounts.last)  1 else 0
        (count, done)
    }
    expect(c.io.out, count)
    expect(c.io.done, done)
    expectedCount = count
    expectedDone = done
  }

  println("[reset = 0, enable = 1]")
  expect(c.io.out, expectedCount)
  expect(c.io.done, expectedDone)

  for (i <- 1 until (max+10)) {
    testOneStep()
  }

  println("[reset = 1, enable = 1]")
  poke(c.io.reset, 1)
  numEnabledCycles = 0
  expect(c.io.out, expectedCounts.last)
  expect(c.io.done, 1)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.out, 0)
    expect(c.io.done, 0)
  }

  println("[reset = 1, enable = 0]")
  poke(c.io.enable, 0)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.out, 0)
    expect(c.io.done, 0)
  }

  println("[reset = 0, enable = 1]")
  poke(c.io.reset, 0)
  poke(c.io.enable, 1)
  expect(c.io.out, 0)
  expect(c.io.done, 0)
  for (i <- 1 until 5) {
    testOneStep()
  }
  println("[reset = 0, enable = 0]")
  poke(c.io.enable, 0)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.out, expectedCount)
    expect(c.io.done, expectedDone)
  }

  println("[reset = 0, enable = 1]")
  poke(c.io.enable, 1)
  expect(c.io.out, expectedCount)
  expect(c.io.done, expectedDone)
  for (i <- 1 until max+10) {
    testOneStep()
  }
}


class CounterCharUnitTester(c: CounterReg)(implicit args: Array[String]) extends ArgsTester(c)

object CounterTest extends CommonMain {
  type DUTType = Counter
  def dut = () => new Counter(args(0).toInt)
  def tester = { c: DUTType => new CounterUnitTester(c) }
}

object CounterChar extends CommonMain {
  type DUTType = CounterReg
  def dut = () => new CounterReg(args(0).toInt)
  def tester = { c: DUTType => new CounterCharUnitTester(c) }
}
