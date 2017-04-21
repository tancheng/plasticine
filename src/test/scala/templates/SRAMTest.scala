// See LICENSE for license details.

package plasticine.templates
//import plasticine.templates.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.language.reflectiveCalls

class SRAMUnitTester(c: SRAM)(implicit args: Array[String]) extends ArgsTester(c) {

  /**
   * Note: The 'pokeAt' method can be used to set memory to magic values
   * Usage shown below, but not used:
   * for (i <- 0 until c.d) {
   *   pokeAt(c.mem, BigInt(i), i)
   * }
   */

  poke(c.io.wen, 1)
  for (n <- 0 until c.d) {
        poke(c.io.waddr, n)
        poke(c.io.wdata, n)
        step(1)
  }
  poke(c.io.wen, 0)

  step(1)

  for (n <- 0 until c.d) {
        poke(c.io.raddr, n)
        val rdataBefore = peek(c.io.rdata)
        step(1)
        val rdataAfter = peek(c.io.rdata)
        expect(c.io.rdata, n)
  }
}

//class SRAMByteEnableTests(c: SRAMByteEnable) extends Tester(c) {
//
//  def write(addr: Int, data: Int, mask: List[Int]) {
//    poke(c.io.waddr, addr)
//    poke(c.io.wdata, data)
//    poke(c.io.wen, 1)
//    c.io.mask.zip(mask) foreach { case (in, i) => poke(in, i) }
//    step(1)
//    poke(c.io.wen, 0)
//  }
//
//  def read(addr: Int) = {
//    poke(c.io.raddr, addr)
//    step(1)
//    peek(c.io.rdata).toInt
//  }
//
//  // Set memory
//  for (i <- 0 until c.d) {
//    val x1 = if (i%2 == 0) BigInt("A", 16) else BigInt("B", 16)
//    pokeAt(c.mem, x1, i)
//  }
//
//  for (n <- 0 until c.d) {
//        poke(c.io.raddr, n)
//        val rdataBefore = peek(c.io.rdata)
//        step(1)
//        val rdataAfter = peek(c.io.rdata)
//  }
//
//  // Perform a masked write followed by a read
//  val rdataBefore = read(0)
//  write(0, 0x0000EE00, List(0, 1, 0, 0))
//  val rdataAfter = read(0)
//  println(s"rdataBefore = ${Integer.toHexString(rdataBefore)}, rdataAfter = ${Integer.toHexString(rdataAfter)}")
//}


object SRAMTest extends CommonMain {
  type DUTType = SRAM
  def dut = () => new SRAM(args(0).toInt, args(1).toInt)
  def tester = { c: DUTType => new SRAMUnitTester(c) }
}

//object SRAMByteEnableTest {
//  val w = 32
//  val d = 8
//
//  def main(args: Array[String]): Unit = {
//    chiselMainTest(args, () => Module(new SRAMByteEnable(w, d))) {
//      c => new SRAMByteEnableTests(c)
//    }
//  }
//}
