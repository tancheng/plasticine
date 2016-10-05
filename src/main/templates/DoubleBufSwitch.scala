package plasticine.templates
import plasticine.Globals

import Chisel._

/**
 * BufferSwitch: Cycle through 'numBuffers' states after all 'numInputs' inputs
 * pulsed at least once. Output has a latency of 1 (because there is a
 * register in the Synchronizer module)
 * */
class BufferSwitch(val numInputs: Int, val numBuffers: Int) extends Module {
  val outWidth = log2Up(numBuffers)
  val io = new Bundle {
    val in = Vec.fill(numInputs) { Bool(INPUT) }
    val out = Bool(OUTPUT)
  }

  val sync = Module(new Synchronizer(numInputs))
  sync.io.in := io.in

  val tff = Module(new TFF(1))
  tff.io.control.enable := sync.io.out

  io.out := Mux(sync.io.out, ~tff.io.data.out, tff.io.data.out)
}

/**
 * BufferSwitch test harness
 */
class BufferSwitchTests(c: BufferSwitch) extends Tester(c) {
  val numInputs = c.numInputs

  // All ones
  expect(c.io.out, 0)
  c.io.in.foreach { case i => poke(i, 1)}
  step(1)
  expect(c.io.out, 1)
  c.io.in.foreach { case i => poke(i, 0)}
  step(1)
  expect(c.io.out, 1)
  step(1)
  c.io.in.foreach { case i => poke(i, 1)}
  step(1)
  expect(c.io.out, 0)
  c.io.in.foreach { case i => poke(i, 0)}
  step(1)
  expect(c.io.out, 0)

  // Set individual wires independently
  for (i <- 0 until numInputs) {
    poke(c.io.in(i), 1)
    step(1)
    poke(c.io.in(i), 0)
    if (i == numInputs-1) expect (c.io.out, 1) else expect(c.io.out, 0)
  }
  step(1)
  expect(c.io.out, 1)
}

object BufferSwitchTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    chiselMainTest(chiselArgs, () => Module(new BufferSwitch(2, 4))) {
      c => new BufferSwitchTests(c)
    }
  }
}
