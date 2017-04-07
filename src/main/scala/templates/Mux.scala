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

class MuxNL(val numInputs1: Int, w1: Int) extends MuxN(numInputs1, w1) {
  override val numSelectBits = log2Up(numInputs1)
  override val io = new Bundle {
    val ins = Vec.fill(numInputs1) { Bits(INPUT,  width = w1) }
    val sel = Bits(INPUT,  numSelectBits)
    val out = Bits(OUTPUT, width = w1)
  }

  io.out := io.ins(io.sel)
}


class MuxVec(val numInputs: Int, v: Int, w: Int) extends Module {
  val numSelectBits = log2Up(numInputs)
  val io = new Bundle {
    val ins = Vec.fill(numInputs) { Vec.fill(v) { Bits(INPUT,  width = w) } }
    val sel = Bits(INPUT,  numSelectBits)
    val out = Vec.fill(v) { Bits(OUTPUT, width = w) }
  }

  io.out := io.ins(io.sel)
}

class MuxNReg(val numInputs: Int, w: Int) extends Module {
  val numSelectBits = log2Up(numInputs)
  val io = new Bundle {
    val ins = Vec.fill(numInputs) { Bits(INPUT,  width = w) }
    val sel = Bits(INPUT,  numSelectBits)
    val out = Bits(OUTPUT, width = w)
  }

  // Register the inputs
  val ffins = List.tabulate(numInputs) { i =>
    val ff = Module(new FF(w))
    ff.io.control.enable := Bool(true)
    ff.io.data.in := io.ins(i)
    ff
  }

  val ffsel = Module(new FF(numSelectBits))
  ffsel.io.control.enable := Bool(true)
  ffsel.io.data.in := io.sel
  val sel = ffsel.io.data.out

  val mux = Module(new MuxN(numInputs, w))
  mux.io.ins := Vec.tabulate(numInputs) { i => ffins(i).io.data.out }
  mux.io.sel := sel

  // Register the output
  val ff = Module(new FF(w))
  ff.io.control.enable := Bool(true)
  ff.io.data.in := mux.io.out
  io.out := ff.io.data.out
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

class MuxNCharTests(c: MuxNReg) extends Tester(c)

object MuxNTest {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: MuxNTest <wordWidth> <numInputs>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val numInputs = appArgs(1).toInt
    chiselMainTest(args, () => Module(new MuxN(numInputs, w))) {
      c => new MuxNTests(c)
    }
  }
}

object MuxNChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: MuxNChar <wordWidth> <numInputs>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val numInputs = appArgs(1).toInt
    chiselMainTest(args, () => Module(new MuxNReg(numInputs, w))) {
      c => new MuxNCharTests(c)
    }
  }
}

object MuxNAreaChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: MuxNAreaChar <wordWidth> <numInputs>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val numInputs = appArgs(1).toInt
    chiselMainTest(args, () => Module(new MuxN(numInputs, w))) {
      c => new MuxNTests(c)
    }
  }
}
