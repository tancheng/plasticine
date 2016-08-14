package plasticine.templates

import Chisel._

/**
 * Scratchpad memory that supports various banking modes
 * and double buffering
 * @param w: Word width in bits
 * @param d: Total memory size
 * @param v: Vector width
 */
class Scratchpad(val w: Int, val d: Int, val v: Int) extends Module {
  val addrWidth = log2Up(d)
  val io = new Bundle {
    val raddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wen = Bool(INPUT)
    val waddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wdata = Vec.fill(v) { Bits(INPUT, width = w) }
    val rdata = Vec.fill(v) { Bits(OUTPUT, width = w) }
  }
  val mems = List.fill(v) { Module(new SRAM(w, d)) }
  mems.map { _.io.raddr }.zip(io.raddr) foreach { case (in, i) => in := i }
  mems.map { _.io.waddr }.zip(io.waddr) foreach { case (in, i) => in := i }
  mems.map { _.io.wdata }.zip(io.wdata) foreach { case (in, i) => in := i }
  mems.map { _.io.wen } foreach { case in => in := io.wen }
  io.rdata.zip { mems.map { _.io.rdata }} foreach { case (out, o) => out := o }
}

class ScratchpadTests(c: Scratchpad) extends Tester(c) {

  // Write data
  poke(c.io.wen, 1)
  for (i <- 0 until c.d) {
    val wdata = Array.fill(c.v) { BigInt(i) }
    val waddr = Array.fill(c.v) { BigInt(i) }
    poke(c.io.waddr, waddr)
    poke(c.io.wdata, wdata)
    step(1)
  }
  poke(c.io.wen, 0)

  for (i <- 0 until c.d) {
    val raddr = Array.fill(c.v) { BigInt(i) }
    poke(c.io.raddr, raddr)
    step(1)
    expect(c.io.rdata, raddr)
  }
}

object ScratchpadTest {
  val w = 32
  val d = 64
  val v = 2

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new Scratchpad(w, d, v))) {
      c => new ScratchpadTests(c)
    }
  }
}

