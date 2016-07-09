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
      val saturate = Bool(INPUT)
      val done   = Bool(OUTPUT)
    }
  }

  val reg = Module(new FF(w))
  val init = UInt(0, width = w)
  reg.io.data.init := init
  reg.io.control.enable := io.control.enable

  val count = reg.io.data.out
  val newval = count + io.data.stride
  val isMax = newval === io.data.max

  when (io.control.reset) {
    reg.io.data.in := init
  } .otherwise {
    reg.io.data.in := Mux(isMax, Mux(io.control.saturate, count, init), newval)
  }

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
  val saturate = 0
  poke(c.io.data.max, max)
  poke(c.io.data.stride, stride)
  poke(c.io.control.enable, 1)
  poke(c.io.control.saturate, saturate)
  poke(c.io.control.reset, 0)

  println("[reset = 0, enable = 1]")
  expect(c.io.data.out, 0)
  expect(c.io.control.done, 0)

  def testOneStep(i: Int) = {
    step(1)
    val (expectedCount, expectedDone) = saturate match {
      case 1 =>
        val count = if (i < max) i else max-1
        val done = if (i < max-1) 0 else 1
        (count, done)
      case 0 =>
        val count = i % max
        val done = if (math.abs(i%max - (max-1)) < stride)  1 else 0
        (count, done)
    }
    expect(c.io.data.out, expectedCount)
    expect(c.io.control.done, expectedDone)
  }
  for (i <- 1 until (max+10)) {
    testOneStep(i)
  }

  println("[reset = 1, enable = 1]")
  poke(c.io.control.reset, 1)
  expect(c.io.data.out, max-1)
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
    testOneStep(i)
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
    testOneStep(i)
  }
}

object CounterTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new Counter(4))) {
      c => new CounterTests(c)
    }
  }
}
