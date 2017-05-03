package plasticine.templates

import chisel3._
import hardfloat._

/**
 * FPComp: Wrapper around Chisel's floating point multiplier.
 * For a full floating point unit, see: https://github.com/ucb-bar/berkeley-hardfloat
 */
class FPComp extends Module {
  val io = IO(new Bundle {
    val a = Input(Vec(16, Bits(32.W)))
    val b = Input(Vec(16, Bits(32.W)))
    val fpGt = Output(Vec(16, Bits(1.W)))
    val fpEq = Output(Vec(16, Bits(1.W)))
    val fpLt = Output(Vec(16, Bits(1.W)))
  })

  for (i <- 0 until 16) {
    val fpComparator = Module(new CompareRecFN(8, 24))
    fpComparator.io.a := recFNFromFN(8, 24, io.a(i))
    fpComparator.io.b := recFNFromFN(8, 24, io.b(i))
    io.fpGt(i) := fpComparator.io.gt
    io.fpEq(i) := fpComparator.io.eq
    io.fpLt(i) := fpComparator.io.lt
  }
}
