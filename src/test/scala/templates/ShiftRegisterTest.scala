// See LICENSE for license details.

package templates

import chisel3.core.Module
import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import plasticine.arch._
import plasticine.pisa.ir._
import scala.reflect.runtime.universe._
import scala.language.reflectiveCalls

/**
 * ShiftRegister test harness
 */
class ConfigBundle(val w: Int, val v: Int) extends Bundle {
  val const = UInt(w.W)
  val fpConst = UInt(32.W)
  val enables = Vec(v, UInt(w.W))

  override def cloneType: this.type = new ConfigBundle(w, v).asInstanceOf[this.type]
}

abstract class Foo[T<:Bundle](t: T) {
  def toBinary[TP<:Any](x: TP, w: Int): List[Int] = x match {
    case num: Int => List.tabulate(w) { j => if (BigInt(num).testBit(j)) 1 else 0 }
    case num: Float => toBinary(java.lang.Float.floatToRawIntBits(num), 32)
    case l: List[Any] => l.map { e => toBinary(e, w/l.size) }.flatten
    case _ => throw new Exception("Unsupported type for toBinary")
  }
}

case class ConfigBits(
  val const: Int,
  val fpConst: Float,
  val enables: List[Int]
)(val t: ConfigBundle) extends Foo(t) {
  // Get names of case class fields
  def classAccessors[T: TypeTag]: List[String] = typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.reverse.map {_.name.toString}

  // Get width of x'th field of this class using corresponding 't' field
  def widthOf(x: Int) = t.elements(classAccessors[this.type].apply(x)).getWidth

  def toBinary(): List[Int] = {
    // Iterate through list elements backwards, convert to binary
    List.tabulate(this.productArity) { i =>
      val idx = this.productArity - 1 - i
      val elem = this.productElement(idx)
      val w = widthOf(idx)
      println(s"Width of $idx = $w")
      toBinary(elem, w)
    }.flatten
  }

}

class ShiftRegisterUnitTester(c: ShiftRegister[ConfigBundle])(implicit args: Array[String]) extends ArgsTester(c) {
  // Create a random bundle
  val w = c.t.w
  val v = c.t.v

  // Create some config bits
  val configBits = ConfigBits(math.abs(rnd.nextInt) % 64, 3.5f, List.tabulate(v) { i => math.abs(rnd.nextInt) % 64})(c.t)
  println(s"configBits: $configBits")
  println(s"ConfigBundle elements: ${c.t.elements}")

  // Get the binary version of configuration
  val binConfig = configBits.toBinary

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
  def dut = () => new ShiftRegister(new ConfigBundle(6, 4))
  def tester = { c: DUTType => new ShiftRegisterUnitTester(c) }
}
