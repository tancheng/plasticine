package plasticine.templates

import Chisel._

class SwitchBox extends Module {
  val io = new Bundle {
    val inorth = Bits(INPUT,  4)
    val iwest = Bits(INPUT,  4)
    val isouth = Bits(INPUT,  4)
    val ieast = Bits(INPUT,  4)
    val onorth = Bits(OUTPUT,  4)
    val owest = Bits(OUTPUT,  4)
    val osouth = Bits(OUTPUT,  4)
    val oeast = Bits(OUTPUT,  4)
  }

  val config = new Bundle {
    val north = UInt(0)
    val west = UInt(0)
    val south = UInt(0)
    val east = UInt(0)
  }

  val nmux = Module(new Mux4())
  nmux.io.in0 := io.inorth
  nmux.io.in1 := io.iwest
  nmux.io.in2 := io.isouth
  nmux.io.in3 := io.ieast
  nmux.io.sel := config.north
  io.onorth := nmux.io.out

  val wmux = Module(new Mux4())
  wmux.io.in0 := io.inorth
  wmux.io.in1 := io.iwest
  wmux.io.in2 := io.isouth
  wmux.io.in3 := io.ieast
  wmux.io.sel := config.west
  io.owest := wmux.io.out

  val smux = Module(new Mux4())
  smux.io.in0 := io.inorth
  smux.io.in1 := io.iwest
  smux.io.in2 := io.isouth
  smux.io.in3 := io.ieast
  smux.io.sel := config.south
  io.osouth := smux.io.out

  val emux = Module(new Mux4())
  emux.io.in0 := io.inorth
  emux.io.in1 := io.iwest
  emux.io.in2 := io.isouth
  emux.io.in3 := io.ieast
  emux.io.sel := config.east
  io.oeast := emux.io.out
}
