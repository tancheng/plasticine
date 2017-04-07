package plasticine.templates

import Chisel._

class SwitchBox8(val w: Int) extends Module {
  val io = new Bundle {
    val in = Bits(INPUT,  width = w)
    val inw = Bits(INPUT,  width = w)
    val iw = Bits(INPUT,  width = w)
    val isw = Bits(INPUT,  width = w)
    val is = Bits(INPUT,  width = w)
    val ise = Bits(INPUT,  width = w)
    val ie = Bits(INPUT,  width = w)
    val ine = Bits(INPUT,  width = w)
    val on = Bits(OUTPUT,  width = w)
    val onw = Bits(OUTPUT,  width = w)
    val ow = Bits(OUTPUT,  width = w)
    val osw = Bits(OUTPUT,  width = w)
    val os = Bits(OUTPUT,  width = w)
    val ose = Bits(OUTPUT,  width = w)
    val oe = Bits(OUTPUT,  width = w)
    val one = Bits(OUTPUT,  width = w)
  }

  val numConfigBits = 8 * 3
  val config = Reg(Bits(width = numConfigBits))
  val configT = new Bundle {
    val n = config(0, 3)
    val nw = config(3, 6)
    val w = config(6, 9)
    val sw = config(9, 12)
    val s = config(12, 15)
    val se = config(15, 18)
    val e = config(18, 21)
    val ne = config(21, 24)
  }

  val nmux = Module(new MuxN(8,w))
  nmux.io.ins(0) := io.in
  nmux.io.ins(1) := io.inw
  nmux.io.ins(2) := io.iw
  nmux.io.ins(3) := io.isw
  nmux.io.ins(4) := io.is
  nmux.io.ins(5) := io.ise
  nmux.io.ins(6) := io.ie
  nmux.io.ins(7) := io.ine
  nmux.io.sel := configT.n
  io.on := nmux.io.out

  val nwmux = Module(new MuxN(8,w))
  nwmux.io.ins(0) := io.in
  nwmux.io.ins(1) := io.inw
  nwmux.io.ins(2) := io.iw
  nwmux.io.ins(3) := io.isw
  nwmux.io.ins(4) := io.is
  nwmux.io.ins(5) := io.ise
  nwmux.io.ins(6) := io.ie
  nwmux.io.ins(7) := io.ine
  nwmux.io.sel := configT.nw
  io.onw := nwmux.io.out

  val wmux = Module(new MuxN(8,w))
  wmux.io.ins(0) := io.in
  wmux.io.ins(1) := io.inw
  wmux.io.ins(2) := io.iw
  wmux.io.ins(3) := io.isw
  wmux.io.ins(4) := io.is
  wmux.io.ins(5) := io.ise
  wmux.io.ins(6) := io.ie
  wmux.io.ins(7) := io.ine
  wmux.io.sel := configT.w
  io.ow := wmux.io.out

  val swmux = Module(new MuxN(8,w))
  swmux.io.ins(0) := io.in
  swmux.io.ins(1) := io.inw
  swmux.io.ins(2) := io.iw
  swmux.io.ins(3) := io.isw
  swmux.io.ins(4) := io.is
  swmux.io.ins(5) := io.ise
  swmux.io.ins(6) := io.ie
  swmux.io.ins(7) := io.ine
  swmux.io.sel := configT.sw
  io.osw := swmux.io.out

  val smux = Module(new MuxN(8,w))
  smux.io.ins(0) := io.in
  smux.io.ins(1) := io.inw
  smux.io.ins(2) := io.iw
  smux.io.ins(3) := io.isw
  smux.io.ins(4) := io.is
  smux.io.ins(5) := io.ise
  smux.io.ins(6) := io.ie
  smux.io.ins(7) := io.ine
  smux.io.sel := configT.s
  io.os := smux.io.out

  val semux = Module(new MuxN(8,w))
  semux.io.ins(0) := io.in
  semux.io.ins(1) := io.inw
  semux.io.ins(2) := io.iw
  semux.io.ins(3) := io.isw
  semux.io.ins(4) := io.is
  semux.io.ins(5) := io.ise
  semux.io.ins(6) := io.ie
  semux.io.ins(7) := io.ine
  semux.io.sel := configT.se
  io.ose := semux.io.out

  val emux = Module(new MuxN(8,w))
  emux.io.ins(0) := io.in
  emux.io.ins(1) := io.inw
  emux.io.ins(2) := io.iw
  emux.io.ins(3) := io.isw
  emux.io.ins(4) := io.is
  emux.io.ins(5) := io.ise
  emux.io.ins(6) := io.ie
  emux.io.ins(7) := io.ine
  emux.io.sel := configT.e
  io.oe := emux.io.out

  val nemux = Module(new MuxN(8,w))
  nemux.io.ins(0) := io.in
  nemux.io.ins(1) := io.inw
  nemux.io.ins(2) := io.iw
  nemux.io.ins(3) := io.isw
  nemux.io.ins(4) := io.is
  nemux.io.ins(5) := io.ise
  nemux.io.ins(6) := io.ie
  nemux.io.ins(7) := io.ine
  nemux.io.sel := configT.ne
  io.one := nemux.io.out
}
