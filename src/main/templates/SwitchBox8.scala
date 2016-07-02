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

  val nmux = Module(new Mux8(w))
  nmux.io.in0 := io.in
  nmux.io.in1 := io.inw
  nmux.io.in2 := io.iw
  nmux.io.in3 := io.isw
  nmux.io.in4 := io.is
  nmux.io.in5 := io.ise
  nmux.io.in6 := io.ie
  nmux.io.in7 := io.ine
  nmux.io.sel := configT.n
  io.on := nmux.io.out

  val nwmux = Module(new Mux8(w))
  nwmux.io.in0 := io.in
  nwmux.io.in1 := io.inw
  nwmux.io.in2 := io.iw
  nwmux.io.in3 := io.isw
  nwmux.io.in4 := io.is
  nwmux.io.in5 := io.ise
  nwmux.io.in6 := io.ie
  nwmux.io.in7 := io.ine
  nwmux.io.sel := configT.nw
  io.onw := nwmux.io.out

  val wmux = Module(new Mux8(w))
  wmux.io.in0 := io.in
  wmux.io.in1 := io.inw
  wmux.io.in2 := io.iw
  wmux.io.in3 := io.isw
  wmux.io.in4 := io.is
  wmux.io.in5 := io.ise
  wmux.io.in6 := io.ie
  wmux.io.in7 := io.ine
  wmux.io.sel := configT.w
  io.ow := wmux.io.out

  val swmux = Module(new Mux8(w))
  swmux.io.in0 := io.in
  swmux.io.in1 := io.inw
  swmux.io.in2 := io.iw
  swmux.io.in3 := io.isw
  swmux.io.in4 := io.is
  swmux.io.in5 := io.ise
  swmux.io.in6 := io.ie
  swmux.io.in7 := io.ine
  swmux.io.sel := configT.sw
  io.osw := swmux.io.out

  val smux = Module(new Mux8(w))
  smux.io.in0 := io.in
  smux.io.in1 := io.inw
  smux.io.in2 := io.iw
  smux.io.in3 := io.isw
  smux.io.in4 := io.is
  smux.io.in5 := io.ise
  smux.io.in6 := io.ie
  smux.io.in7 := io.ine
  smux.io.sel := configT.s
  io.os := smux.io.out

  val semux = Module(new Mux8(w))
  semux.io.in0 := io.in
  semux.io.in1 := io.inw
  semux.io.in2 := io.iw
  semux.io.in3 := io.isw
  semux.io.in4 := io.is
  semux.io.in5 := io.ise
  semux.io.in6 := io.ie
  semux.io.in7 := io.ine
  semux.io.sel := configT.se
  io.ose := semux.io.out

  val emux = Module(new Mux8(w))
  emux.io.in0 := io.in
  emux.io.in1 := io.inw
  emux.io.in2 := io.iw
  emux.io.in3 := io.isw
  emux.io.in4 := io.is
  emux.io.in5 := io.ise
  emux.io.in6 := io.ie
  emux.io.in7 := io.ine
  emux.io.sel := configT.e
  io.oe := emux.io.out

  val nemux = Module(new Mux8(w))
  nemux.io.in0 := io.in
  nemux.io.in1 := io.inw
  nemux.io.in2 := io.iw
  nemux.io.in3 := io.isw
  nemux.io.in4 := io.is
  nemux.io.in5 := io.ise
  nemux.io.in6 := io.ie
  nemux.io.in7 := io.ine
  nemux.io.sel := configT.ne
  io.one := nemux.io.out
}
