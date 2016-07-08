package plasticine.templates

import plasticine.misc.Utils
import Chisel._

class MuxN(val numInputs: Int, w: Int) extends Module {
  val numSelectBits = Utils.log2(numInputs)
  val io = new Bundle {
    val ins = Vec.fill(numInputs) { Bits(INPUT,  width = w) }
    val sel = Bits(INPUT,  numSelectBits)
    val out = Bits(OUTPUT, width = w)
  }

  if (numInputs == 2) {
    val mux2 = Module(new Mux2(w))
    mux2.io.in0 := io.ins(0)
    mux2.io.in1 := io.ins(1)
    mux2.io.sel := io.sel
    io.out := mux2.io.out
  } else {
    val mux0 = Module(new MuxN(numInputs/2, w))
    mux0.io.ins := Vec.tabulate(numInputs/2) { i => io.ins(i) }
    mux0.io.sel := io.sel(numSelectBits-2, 0)
    val mux0Out = mux0.io.out

    val mux1 = Module(new MuxN(numInputs/2, w))
    mux1.io.ins := Vec.tabulate(numInputs/2) { i => io.ins(numInputs/2 + i) }
    mux1.io.sel := io.sel(numSelectBits-2, 0)
    val mux1Out = mux1.io.out

    val mux2 = Module(new Mux2(w))
    mux2.io.in0 := mux0Out
    mux2.io.in1 := mux1Out
    mux2.io.sel := io.sel(numSelectBits-1)
    io.out := mux2.io.out
  }
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

