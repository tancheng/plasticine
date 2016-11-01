package plasticine.templates

import Chisel._

class SRAM(val w: Int, val d: Int) extends BlackBox {
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
  }
  raddr_reg := io.raddr

  io.rdata := mem(raddr_reg)
}

/**
 * SRAM with byte enable: TO BE USED IN SIMULATION ONLY
 * @param mask: Vec of Bool mask in little-to-big-byte order
 * I.e., mask(0) corresponds to byte enable of least significant byte
 */
class SRAMByteEnable(val w: Int, val d: Int) extends BlackBox {
  val addrWidth = log2Up(d)
  val numBytes = w/8
  Predef.assert(w%8 == 0, s"Invalid word width $w in SRAMByteEnable: Must be a multiple of 8")
  val io = new Bundle {
    val raddr = UInt(INPUT, width = addrWidth)
    val wen = Bool(INPUT)
    val mask = Vec.fill(numBytes) { Bool(INPUT) }
    val waddr = UInt(INPUT, width = addrWidth)
    val wdata = Bits(INPUT, width = w)
    val rdata = Bits(OUTPUT, width = w)
  }
  val mem = Mem(Bits(width = w), d, seqRead = true)
  io.rdata := Bits(0)
  val raddr_reg = Reg(Bits(width = addrWidth))
  when (io.wen) {
    val tmp = mem(io.waddr)
    val tmpVec = Vec.tabulate(numBytes) { i => tmp(i*8+8-1, i*8) }
    val newVec = Vec.tabulate(numBytes) { i => io.wdata(i*8+8-1, i*8) }
    val wdata = Vec.tabulate(numBytes) { i =>
      val byteEn = io.mask(i)
      Mux(byteEn, newVec(i), tmpVec(i))
    }.reverse.reduce { Cat(_,_) }
    mem(io.waddr) := wdata
  }
  raddr_reg := io.raddr

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

class SRAMByteEnableTests(c: SRAMByteEnable) extends Tester(c) {

  def write(addr: Int, data: Int, mask: List[Int]) {
    poke(c.io.waddr, addr)
    poke(c.io.wdata, data)
    poke(c.io.wen, 1)
    c.io.mask.zip(mask) foreach { case (in, i) => poke(in, i) }
    step(1)
    poke(c.io.wen, 0)
  }

  def read(addr: Int) = {
    poke(c.io.raddr, addr)
    step(1)
    peek(c.io.rdata).toInt
  }

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

  // Perform a masked write followed by a read
  val rdataBefore = read(0)
  write(0, 0x0000EE00, List(0, 1, 0, 0))
  val rdataAfter = read(0)
  println(s"rdataBefore = ${Integer.toHexString(rdataBefore)}, rdataAfter = ${Integer.toHexString(rdataAfter)}")
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

object SRAMByteEnableTest {
  val w = 32
  val d = 8

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new SRAMByteEnable(w, d))) {
      c => new SRAMByteEnableTests(c)
    }
  }
}
