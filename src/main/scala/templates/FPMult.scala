package plasticine.templates

import chisel3._
import hardfloat._

/**
 * FPMult: Wrapper around Chisel's floating point multiplier.
 * For a full floating point unit, see: https://github.com/ucb-bar/berkeley-hardfloat
 */
class FPMult extends Module {
  val io = IO(new Bundle {
    val a = Input(Vec(16, Bits(32.W)))
    val b = Input(Vec(16, Bits(32.W)))
    val c = Input(Vec(16, Bits(32.W)))
    val out  = Output(Vec(16, Bits(32.W)))
  })

  for (i <- 0 until 16) {
    val fma = Module(new MulAddRecFN(8, 24))

    // Convert to Berkeley's special floating point format
    fma.io.a := recFNFromFN(8, 24, io.a(i))
    fma.io.b := recFNFromFN(8, 24, io.b(i))
    fma.io.c := recFNFromFN(8, 24, io.c(i))

    // Convert back
    io.out(i) := fNFromRecFN(8, 24, fma.io.out)
  }
}
