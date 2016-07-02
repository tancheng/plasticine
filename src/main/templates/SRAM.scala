package plasticine.templates

import Chisel._

class SRAM(val w: Int, val d: Int) extends Module {
  val io = new Bundle {
    val raddr = UInt(INPUT, width = w)
    val wen = Bool(INPUT)
    val waddr = UInt(INPUT, width = w)
    val wdata = Bits(INPUT, width = w)
    val rdata = Bits(OUTPUT, width = w)
  }
  val mem = Mem(Bits(width = w), d, seqRead = true)
  io.rdata := Bits(0)
  val raddr_reg = Reg(Bits(width = w))
  when (io.wen) {
    mem(io.waddr) := io.wdata
  } .otherwise  {
    raddr_reg := io.raddr
  }
  io.rdata := mem(raddr_reg)
}
