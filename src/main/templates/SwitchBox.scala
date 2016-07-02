package plasticine.templates

import Chisel._

class SwitchBox(val w: Int) extends Module {
  val io = new Bundle {
    val inorth = Bits(INPUT,  width = w)
    val iwest = Bits(INPUT,  width = w)
    val isouth = Bits(INPUT,  width = w)
    val ieast = Bits(INPUT,  width = w)
    val onorth = Bits(OUTPUT,  width = w)
    val owest = Bits(OUTPUT,  width = w)
    val osouth = Bits(OUTPUT,  width = w)
    val oeast = Bits(OUTPUT,  width = w)
  }

  val config = new Bundle {
    val north = UInt(0)
    val west = UInt(0)
    val south = UInt(0)
    val east = UInt(0)
  }

  val nmux = Module(new Mux4(w))
  nmux.io.in0 := io.inorth
  nmux.io.in1 := io.iwest
  nmux.io.in2 := io.isouth
  nmux.io.in3 := io.ieast
  nmux.io.sel := config.north
  io.onorth := nmux.io.out

  val wmux = Module(new Mux4(w))
  wmux.io.in0 := io.inorth
  wmux.io.in1 := io.iwest
  wmux.io.in2 := io.isouth
  wmux.io.in3 := io.ieast
  wmux.io.sel := config.west
  io.owest := wmux.io.out

  val smux = Module(new Mux4(w))
  smux.io.in0 := io.inorth
  smux.io.in1 := io.iwest
  smux.io.in2 := io.isouth
  smux.io.in3 := io.ieast
  smux.io.sel := config.south
  io.osouth := smux.io.out

  val emux = Module(new Mux4(w))
  emux.io.in0 := io.inorth
  emux.io.in1 := io.iwest
  emux.io.in2 := io.isouth
  emux.io.in3 := io.ieast
  emux.io.sel := config.east
  io.oeast := emux.io.out
}
