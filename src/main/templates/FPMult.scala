package plasticine.templates

import Chisel._

/**
 * FPMult: Wrapper around Chisel's floating point multiplier.
 * Note that the "Flo" and "Dbl" types are a bit misleading
 * as the actual hardware
 * For a full floating point unit, see: https://github.com/ucb-bar/berkeley-hardfloat
 */
class FPMult extends Module {
  val io = new Bundle {
    val a = Flo(INPUT)
    val b = Flo(INPUT)
    val out  = Flo(OUTPUT)
  }

  io.out := io.a * io.b
}

/**
 * FPMult test harness
 */
class FPMultTests(c: FPMult) extends Tester(c) {

  val numCycles = 10
  for (i <- 0 until numCycles) {
    val a = 2.4f
    val b = 3.5f
    val out = a * b
    poke(c.io.a, a)
    poke(c.io.b, b)

    expect(c.io.out, out)
    step(1)
  }
}

object FPMultTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new FPMult)) {
      c => new FPMultTests(c)
    }
  }
}
