package plasticine.templates

import Chisel._

class SRAM(val w: Int, val d: Int) extends Module {
  val addrWidth = log2Up(d)
  val io = new Bundle {
    val raddr = UInt(INPUT, width = addrWidth)
    val wen = Bool(INPUT)
    val waddr = UInt(INPUT, width = addrWidth)
    val wdata = Bits(INPUT, width = w)
    val rdata = Bits(OUTPUT, width = w)
  }
  val mem = Mem(Bits(width = w), d, seqRead = true)
  io.rdata := Bits(0)
  val raddr_reg = Reg(Bits(width = addrWidth))
  when (io.wen) {
    mem(io.waddr) := io.wdata
  } .otherwise  {
    raddr_reg := io.raddr
  }
  io.rdata := mem(raddr_reg)
}

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

