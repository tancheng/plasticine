package plasticine.templates

import Chisel._

/**
 * Counter: 1-dimensional counter. Counts upto 'max', each time incrementing
 * by 'stride', beginning at zero.
 * @param w: Word width
 */
class Counter(val w: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val max      = UInt(INPUT,  w)
      val stride   = UInt(INPUT,  w)
      val out      = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val reset  = Bool(INPUT)
      val enable = Bool(INPUT)
      val done   = Bool(OUTPUT)
    }
  }

  // register wires - should this be another module?
  val init = UInt(0, width = w)
  val d = UInt(width = w)
  val count = Reg(Bits(w), d, init)

//  val feedbackval = Mux(io.control.enable, count, init)
  val newval = count + io.data.stride
  val isMax = count === io.data.max
  when (io.control.reset) {
    d := init
  } .elsewhen (io.control.enable) {
    d := Mux(isMax, io.data.max, newval)
  } .otherwise {
    d := count
  }
//  d := Mux(io.control.reset, init, Mux(io.control.enable, Mux(isMax, io.data.max, newval), count))

  io.data.out := count
  io.control.done := isMax
}

/**
 * Counter test harness
 */
class CounterTests(c: Counter) extends Tester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 1
  poke(c.io.data.max, max)
  poke(c.io.data.stride, stride)
  poke(c.io.control.enable, 1)
  poke(c.io.control.reset, 0)

  println("[reset = 0, enable = 1]")
  expect(c.io.data.out, 0)
  expect(c.io.control.done, 0)
  for (i <- 1 until (max+10)) {
    step(1)
    val expectedCount = if (i <= max) i else max
    val expectedDone = if (i < max) 0 else 1
    expect(c.io.data.out, expectedCount)
    expect(c.io.control.done, expectedDone)
  }

  println("[reset = 1, enable = 1]")
  poke(c.io.control.reset, 1)
  expect(c.io.data.out, max)
  expect(c.io.control.done, 1)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.data.out, 0)
    expect(c.io.control.done, 0)
  }

  println("[reset = 1, enable = 0]")
  poke(c.io.control.enable, 0)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.data.out, 0)
    expect(c.io.control.done, 0)
  }

  println("[reset = 0, enable = 1]")
  poke(c.io.control.reset, 0)
  poke(c.io.control.enable, 1)
  expect(c.io.data.out, 0)
  expect(c.io.control.done, 0)
  for (i <- 1 until 5) {
    step(1)
    val expectedCount = if (i <= max) i else max
    val expectedDone = if (i < max) 0 else 1
    expect(c.io.data.out, expectedCount)
    expect(c.io.control.done, expectedDone)
  }
  val currentCount = 4

  println("[reset = 0, enable = 0]")
  poke(c.io.control.enable, 0)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.data.out, currentCount)
    expect(c.io.control.done, 0)
  }

  println("[reset = 0, enable = 1]")
  poke(c.io.control.enable, 1)
  expect(c.io.data.out, currentCount)
  expect(c.io.control.done, 0)
  for (i <- currentCount+1 until max+10) {
    step(1)
    val expectedCount = if (i <= max) i else max
    val expectedDone = if (i < max) 0 else 1
    expect(c.io.data.out, expectedCount)
    expect(c.io.control.done, expectedDone)
  }
}

object CounterTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new Counter(4))) {
      c => new CounterTests(c)
    }
  }
}
