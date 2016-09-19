package plasticine.templates

import Chisel._

/**
 * FF: Flip-flop with the ability to set enable and init
 * value as IO
 * @param w: Word width
 */
class FF(val w: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val in   = UInt(INPUT,  w)
      val init = UInt(INPUT,  w)
      val out  = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable = Bool(INPUT)
    }
  }

  val d = UInt(width = w)
  val ff = Reg(Bits(w), d, io.data.init)
  when (io.control.enable) {
    d := io.data.in
  } .otherwise {
    d := ff
  }
  io.data.out := ff
}

class FFNoInit(val w: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val in   = UInt(INPUT,  w)
      val out  = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable = Bool(INPUT)
    }
  }

  val d = UInt(width = w)
  val ff = Reg(Bits(w), d)
  when (io.control.enable) {
    d := io.data.in
  } .otherwise {
    d := ff
  }
  io.data.out := ff
}

class TFF(val w: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val out  = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable = Bool(INPUT)
    }
  }

  val d = UInt(width = w)
  val ff = Reg(Bits(w), d, UInt(0, width=w))
  when (io.control.enable) {
    d := ~ff
  } .otherwise {
    d := ff
  }
  io.data.out := ff
}


class FFWrapper(val w: Int, val initVal: Int) extends Module {
  val io = new Bundle {
    val data = new Bundle {
      val in   = UInt(INPUT,  w)
      val out  = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable = Bool(INPUT)
    }
  }

  val init = UInt(initVal % (1 << w), width=w)
  val ff = Module(new FF(w))
  ff.io.data.in := io.data.in
  ff.io.data.init := init
  ff.io.control.enable := io.control.enable
  io.data.out := ff.io.data.out
}

class FFL(val w1: Int) extends FF(w1) {
  override val io = new Bundle {
    val data = new Bundle {
      val in   = UInt(INPUT,  w)
      val init = UInt(INPUT,  w)
      val out  = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable = Bool(INPUT)
    }
  }

  override val d = UInt(width = w)
  override val ff = Reg(Bits(w), d, io.data.init)
  when (io.control.enable) {
    d := io.data.in
  } .otherwise {
    d := ff
  }
  io.data.out := ff
}

/**
 * FF test harness
 */
class FFTests(c: FF) extends Tester(c) {
  val initval = 10
  poke(c.io.data.init, initval)
  step(1)
  reset(1)
  expect(c.io.data.out, initval)

  val numCycles = 10
  for (i <- 0 until numCycles) {
    val newenable = rnd.nextInt(2)
    val oldout = peek(c.io.data.out)
    poke(c.io.data.in, i)
    poke(c.io.control.enable, newenable)
    step(1)
    if (newenable == 1) expect(c.io.data.out, i) else expect(c.io.data.out, oldout)
  }
}

class TFFTests(c: TFF) extends Tester(c) {
  step(1)
  reset(1)
  expect(c.io.data.out, 0)
  val numCycles = 10
  for (i <- 0 until numCycles) {
    val newenable = rnd.nextInt(2)
    val oldout = peek(c.io.data.out)
    poke(c.io.control.enable, newenable)
    step(1)
    if (newenable == 1) expect(c.io.data.out, ~oldout) else expect(c.io.data.out, oldout)
  }
}

class FFNoInitTests(c: FFNoInit) extends Tester(c)
class FFWrapperTests(c: FFWrapper) extends Tester(c)

object FFTest {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: IntPrimitiveTest <wordWidth>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    chiselMainTest(args, () => Module(new FF(w))) {
      c => new FFTests(c)
    }
  }
}

object TFFTest {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: FFTest <wordWidth>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    chiselMainTest(args, () => Module(new TFF(w))) {
      c => new TFFTests(c)
    }
  }
}


object FFNoInitChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: FFNoInitChar <wordWidth>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    chiselMainTest(args, () => Module(new FFNoInit(w))) {
      c => new FFNoInitTests(c)
    }
  }
}

object FFWrapperTest {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: FFWrapperTest <wordWidth>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val init = 1
    chiselMainTest(args, () => Module(new FFWrapper(w, init))) {
      c => new FFWrapperTests(c)
    }
  }
}
