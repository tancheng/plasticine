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

object FFTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new FF(4))) {
      c => new FFTests(c)
    }
  }
}
