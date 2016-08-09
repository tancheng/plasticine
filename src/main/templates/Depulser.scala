package plasticine.templates

import Chisel._

/**
 * Depulser: 1-cycle pulse to a steady high signal
 */
class Depulser() extends Module {
  val io = new Bundle {
    val in = Bool(INPUT)
    val rst = Bool(INPUT)
    val out = Bool(OUTPUT)
  }

  val r = Module(new FF(1))
  r.io.data.in := Mux(io.rst, UInt(0), io.in)
  r.io.data.init := UInt(0)
  r.io.control.enable := io.in | io.rst
  io.out := r.io.data.out
}

/**
 * Depulser test harness
 */
class DepulserTests(c: Depulser) extends Tester(c) {
  poke(c.io.in, 1)
  for (i <- 0 until 5) {
    peek(c.io.out)
    val expectedVal = if (i == 0) 0 else 1
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.in, 0)
  for (i <- 0 until 5) {
    val expectedVal = 1
    expect(c.io.out, expectedVal)
    step(1)
  }
  poke(c.io.rst, 1)
  step(1)
  expect(c.io.out, 0)
  step(5)
  poke(c.io.in, 1)
  step(1)
  expect(c.io.out, 0)
  poke(c.io.rst, 0)
  step(1)
  expect(c.io.out, 1)
  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 1)
}

object DepulserTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    chiselMainTest(chiselArgs, () => Module(new Depulser())) {
      c => new DepulserTests(c)
    }
  }
}
