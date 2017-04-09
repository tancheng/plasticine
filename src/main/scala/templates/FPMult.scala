//package plasticine.templates
//
//import Chisel._
//import plasticine.templates.hardfloat._
//
///**
// * FPMult: Wrapper around Chisel's floating point multiplier.
// * For a full floating point unit, see: https://github.com/ucb-bar/berkeley-hardfloat
// */
//class FPMult extends Module {
//  val io = new Bundle {
//    val a = Bits(INPUT, width=32)
//    val b = Bits(INPUT, width=32)
//    val c = Bits(INPUT, width=32)
//    val out  = Bits(OUTPUT, width=32)
//  }
//
//  val fma = Module(new MulAddRecFN(8, 24))
//
//  // Convert to Berkeley's special floating point format
//  fma.io.a := recFNFromFN(8, 24, io.a)
//  fma.io.b := recFNFromFN(8, 24, io.b)
//  fma.io.c := recFNFromFN(8, 24, io.c)
//
//  // Convert back
//  io.out := fNFromRecFN(8, 24, fma.io.out)
//}
//
///**
// * FPMult test harness
// */
//class FPMultTests(c: FPMult) extends Tester(c) {
//  val a = 2.4f
//  val b = 3.5f
//  val d = 0.0f
//  val out = a * b
//  poke(c.io.a, java.lang.Float.floatToRawIntBits(a))
//  poke(c.io.b, java.lang.Float.floatToRawIntBits(b))
//  poke(c.io.c, java.lang.Float.floatToRawIntBits(d))
//
//  expect(c.io.out, out)
//  step(1)
//}
//
//object FPMultTest {
//  def main(args: Array[String]): Unit = {
//    chiselMainTest(args, () => Module(new FPMult)) {
//      c => new FPMultTests(c)
//    }
//  }
//}
