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

  io.onorth := io.inorth
  io.owest := io.iwest
  io.osouth := io.isouth
  io.oeast := io.ieast
}
