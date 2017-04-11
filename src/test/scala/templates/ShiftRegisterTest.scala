// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import plasticine.templates.FF

/**
 * ShiftRegister test harness
 */

class ConfigBundle(val w: Int, val v: Int) extends Bundle {
  val const = UInt(w.W)
  val enables = Vec(v, Bool())

  override def cloneType: this.type = new ConfigBundle(w, v).asInstanceOf[this.type]
}

case class ConfigBits(
  val const: Int,
  val enables: List[Boolean]
)

class ShiftRegisterUnitTester(c: ShiftRegister[ConfigBundle])(implicit args: Array[String]) extends ArgsTester(c) {
  // Create a random bundle
  val w = 4
  val v = 4
  val testBundle = new ConfigBundle(w, v)

  def toBinArray(x: Int, w: Int) = {
    List.tabulate(w) { i => if (BigInt(x).testBit(i)) 1 else 0 }
  }

  // Create some config bits
  val configBits = ConfigBits(10, List.tabulate(v) { i => (rnd.nextInt % 2) == 0})

  println(s"configBits: $configBits")

  // Get the binary version of configuration
  val binConfig = configBits.enables.map { if (_) 1 else 0 } ++ toBinArray(configBits.const, testBundle.const.getWidth)

  // Poke until c.io.out.valid is high
  binConfig.foreach { bit =>
    poke(c.io.in.bits, bit)
    poke(c.io.in.valid, 1)
    step(1)
  }

  poke(c.io.in.valid, 0)
  step(10)

  // Read out config, compare it with expected value
}


object ShiftRegisterTest extends CommonMain {
  type DUTType = ShiftRegister[ConfigBundle]
  def dut = () => new ShiftRegister(new ConfigBundle(4, 4))
  def tester = { c: DUTType => new ShiftRegisterUnitTester(c) }
}
