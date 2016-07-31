package plasticine.templates

import plasticine.misc.Utils
import Chisel._

class MuxN(val numInputs: Int, w: Int) extends Module {
  val numSelectBits = log2Up(numInputs)
  val io = new Bundle {
    val ins = Vec.fill(numInputs) { Bits(INPUT,  width = w) }
    val sel = Bits(INPUT,  numSelectBits)
    val out = Bits(OUTPUT, width = w)
  }

  io.out := io.ins(io.sel)
}

class MuxNTests(c: MuxN) extends Tester(c) {
    val ins = List.tabulate(c.numInputs) { i =>
      math.abs(scala.util.Random.nextInt % (1 << 4))
    }
    c.io.ins zip ins foreach { case (cin, in) => poke(cin, in) }

    (0 until c.numInputs) foreach { sel =>
      poke(c.io.sel, sel)
      step(1)
      val out = ins(sel)
      expect(c.io.out, out)
    }
}

object MuxNTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new MuxN(16, 12))) {
      c => new MuxNTests(c)
    }
  }
}

