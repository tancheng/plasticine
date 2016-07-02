package plasticine.templates

import Chisel._

abstract class In4Out4(val w: Int) extends Module {
  val io = new Bundle {
    val in0 = Bits(INPUT, width = w)
    val in1 = Bits(INPUT, width = w)
    val in2 = Bits(INPUT, width = w)
    val in3 = Bits(INPUT, width = w)
    val out0 = Bits(OUTPUT, width = w)
    val out1 = Bits(OUTPUT, width = w)
    val out2 = Bits(OUTPUT, width = w)
    val out3 = Bits(OUTPUT, width = w)
  }
}
