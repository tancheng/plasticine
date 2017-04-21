// See LICENSE for license details.

package plasticine.templates
//import plasticine.templates.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.language.reflectiveCalls
/**
 * UpDownCtr test harness
 */
class UpDownCtrUnitTester(c: UpDownCtr)(implicit args: Array[String]) extends ArgsTester(c) {
  poke(c.io.initval, 2)
  poke(c.io.strideInc, 1)
  poke(c.io.strideDec, 1)
  poke(c.io.init, 1)
  step(1)
  expect(c.io.gtz, 1)
  poke(c.io.init, 0)

  poke(c.io.dec, 1)
  step(1) // 1
  expect(c.io.gtz, 1)
  step(1)  // 0
  expect(c.io.gtz, 0)

  poke(c.io.inc, 1) // both inc and dec = 1, unchanged
  step(1)
  expect(c.io.gtz, 0)
  poke(c.io.dec, 0)
  step(1)  // 1
  expect(c.io.gtz, 1)
}

object UpDownCtrTest extends CommonMain {
  type DUTType = UpDownCtr
  def dut = () => new UpDownCtr(args(0).toInt)
  def tester = { c: DUTType => new UpDownCtrUnitTester(c) }
}
