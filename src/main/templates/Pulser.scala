package plasticine.templates

import Chisel._

/**
 * Pulser: Converts a rising edge to a 1-cycle pulse
 */
class Pulser() extends Module {
  val io = new Bundle {
    val in = Bool(INPUT)
    val out = Bool(OUTPUT)
  }

  val commandReg = Reg(Bits(1), io.in, UInt(0))
  io.out := io.in & (commandReg ^ io.in)
}

/**
 * Pulser test harness
 */
class PulserTests(c: Pulser) extends Tester(c) {
  poke(c.io.in, 1)
  for (i <- 0 until 5) {
    peek(c.io.out)
    val expectedVal = if (i == 0) 1 else 0
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 0)
  for (i <- 0 until 5) {
    val expectedVal = 0
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 1)
  expect(c.io.out, 1)
  step(1)
  expect(c.io.out, 0)
  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 0)
}

object PulserTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    chiselMainTest(chiselArgs, () => Module(new Pulser())) {
      c => new PulserTests(c)
    }
  }
}
