package plasticine.test

import Chisel._
import plasticine.templates.SRAM

class SRAMTests(c: SRAM) extends Tester(c) {

  // Set memory
  for (i <- 0 until c.d) {
    val x1 = if (i%2 == 0) BigInt("A", 16) else BigInt("B", 16)
    pokeAt(c.mem, x1, i)
  }

  for (n <- 0 until c.d) {
        poke(c.io.raddr, n)
        val rdataBefore = peek(c.io.rdata)
        step(1)
        val rdataAfter = peek(c.io.rdata)
  }
}

object SRAMTest {
  val w = 4
  val d = 8

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new SRAM(w, d))) {
      c => new SRAMTests(c)
    }
  }
}

