package plasticine.templates

import Chisel._
import plasticine.Globals

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

//  val reg = Module(new FF(w))
  val reg = if (Globals.noModule) new FFL(w) else Module(new FF(w))
  val init = UInt(0, width = w)
  reg.io.data.init := init
  reg.io.control.enable := io.control.reset | io.control.enable

  val count = Cat(UInt(0, width=1), reg.io.data.out)
  val newval = count + io.data.stride
  val isMax = newval >= io.data.max

  when (io.control.reset) {
    reg.io.data.in := init
  } .otherwise {
    reg.io.data.in := Mux(isMax, Mux(io.control.saturate, count, init), newval)
  }

  io.data.out := count
  io.control.done := io.control.enable & isMax
}

class CounterReg(val w: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val max      = UInt(INPUT,  w)
      val stride   = UInt(INPUT,  w)
      val out      = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val reset = Bool(INPUT)
      val enable = Bool(INPUT)
      val saturate = Bool(INPUT)
      val done   = Bool(OUTPUT)
    }
  }

  // Register the inputs
  val maxReg = Module(new FF(w))
  maxReg.io.control.enable := Bool(true)
  maxReg.io.data.in := io.data.max
  val max = maxReg.io.data.out

  val strideReg = Module(new FF(w))
  strideReg.io.control.enable := Bool(true)
  strideReg.io.data.in := io.data.stride
  val stride = strideReg.io.data.out

  val rstReg = Module(new FF(1))
  rstReg.io.control.enable := Bool(true)
  rstReg.io.data.in := io.control.reset
  val rst = rstReg.io.data.out

  val enableReg = Module(new FF(1))
  enableReg.io.control.enable := Bool(true)
  enableReg.io.data.in := io.control.enable
  val enable = enableReg.io.data.out

  val saturateReg = Module(new FF(1))
  saturateReg.io.control.enable := Bool(true)
  saturateReg.io.data.in := io.control.saturate
  val saturate = saturateReg.io.data.out

  // Instantiate counter
  val counter = Module(new Counter(w))
  counter.io.data.max := max
  counter.io.data.stride := stride
  counter.io.control.enable := enable
  counter.io.control.reset := rst
  counter.io.control.enable := enable
  counter.io.control.saturate := saturate

  // Register outputs
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := counter.io.data.out
  io.data.out := outReg.io.data.out
  val doneReg = Module(new FF(1))
  doneReg.io.control.enable := Bool(true)
  doneReg.io.data.in := counter.io.control.done
  io.control.done := doneReg.io.data.out
}


/**
 * Counter test harness
 */
class CounterTests(c: Counter) extends Tester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 6
  val saturate = 0

  poke(c.io.data.max, max)
  poke(c.io.data.stride, stride)
  poke(c.io.control.enable, 1)
  poke(c.io.control.saturate, saturate)
  poke(c.io.control.reset, 0)

  val expectedCounts = 0 until max by stride toList

  var numEnabledCycles = 0
  var expectedCount = 0
  var expectedDone = 0
  def testOneStep() = {
    step(1)
    numEnabledCycles += 1
    val (count, done) = saturate match {
      case 1 =>
        val count = if (numEnabledCycles < expectedCounts.size) expectedCounts(numEnabledCycles) else expectedCounts.last
        val done = if (count == expectedCounts.last) 1 else 0
        (count, done)
      case 0 =>
        val count = expectedCounts(numEnabledCycles % expectedCounts.size)
        val done = if (count == expectedCounts.last)  1 else 0
        (count, done)
    }
    expect(c.io.data.out, count)
    expect(c.io.control.done, done)
    expectedCount = count
    expectedDone = done
  }

  println("[reset = 0, enable = 1]")
  expect(c.io.data.out, expectedCount)
  expect(c.io.control.done, expectedDone)

  for (i <- 1 until (max+10)) {
    testOneStep()
  }

  println("[reset = 1, enable = 1]")
  poke(c.io.control.reset, 1)
  numEnabledCycles = 0
  expect(c.io.data.out, expectedCounts.last)
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
    testOneStep()
  }
  println("[reset = 0, enable = 0]")
  poke(c.io.control.enable, 0)
  for (i <- 0 until 5) {
    step(1)
    expect(c.io.data.out, expectedCount)
    expect(c.io.control.done, expectedDone)
  }

  println("[reset = 0, enable = 1]")
  poke(c.io.control.enable, 1)
  expect(c.io.data.out, expectedCount)
  expect(c.io.control.done, expectedDone)
  for (i <- 1 until max+10) {
    testOneStep()
  }
}


class CounterCharTests(c: CounterReg) extends Tester(c)

object CounterChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: CounterChar <w>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    chiselMainTest(args, () => Module(new CounterReg(w))) {
      c => new CounterCharTests(c)
    }
  }
}

object CounterTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new Counter(4))) {
      c => new CounterTests(c)
    }
  }
}
