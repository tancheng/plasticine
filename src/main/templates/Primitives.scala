package plasticine.templates

import Chisel._

class IntPrimitiveModule(val w: Int, op: (UInt, UInt) => UInt) extends Module {
  val io = new Bundle {
    val a = UInt(INPUT, w)
    val b = UInt(INPUT, w)
    val out = UInt(OUTPUT, w)
  }

  io.out := op(io.a, io.b)
}

class IntPrimitiveReg(val w: Int, op: (UInt, UInt) => UInt) extends Module {
  val io = new Bundle {
    val a = UInt(INPUT, w)
    val b = UInt(INPUT, w)
    val out = UInt(OUTPUT, w)
  }

  // Register the inputs
  val aReg = Module(new FF(w))
  aReg.io.control.enable := Bool(true)
  aReg.io.data.in := io.a
  val a = aReg.io.data.out

  val bReg = Module(new FF(w))
  bReg.io.control.enable := Bool(true)
  bReg.io.data.in := io.b
  val b = bReg.io.data.out

  val fu = Module(new IntPrimitiveModule(w, op))
  fu.io.a := a
  fu.io.b := b

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := fu.io.out
  io.out := outReg.io.data.out
}

class IntPrimitiveTests(c: IntPrimitiveReg) extends Tester(c) {
}

object IntPrimitiveTest {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: IntPrimitiveTest <wordWidth> <op>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val op = Opcodes.getOpLambda(appArgs(1))
    chiselMainTest(args, () => Module(new IntPrimitiveReg(w, op))) {
      c => new IntPrimitiveTests(c)
    }
  }
}
