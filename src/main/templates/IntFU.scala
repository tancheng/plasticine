package plasticine.templates
import plasticine.Globals

import scala.collection.immutable.Map
import Chisel._
import plasticine.templates.hardfloat._

/**
 * Integer Functional Unit module. Represents the basic workhorse
 * within a Compute Unit (CU) in Plasticine.
 * All operations are designed to take a single cycle.
 * @param w: Word width
 */

object Opcodes {
  // HACK: Duplicated with IntFU. Need to refactor this into
  // a separate Decode table
  private var _opcodes = List[(String, (UInt, UInt) => UInt)](
    ("+" , (a,b)    => a+b),
    ("-" , (a,b)    => a-b),
    ("*" , (a,b)    => a*b),
    ("/" , (a,b)    => a*b),  // No divider temporarily
    ("&" , (a,b)    => a&b),
    ("|" , (a,b)    => a|b),
    ("==" , (a,b)   => a===b),
    (">" , (a,b)   => a>b),
    ("<" , (a,b)   => a<b),
    ("<<" , (a,b)   => a<<b),
    (">>" , (a,b)   => a>>b),
    ("f<" , (a,b)   => UInt(0)),
    ("f==" , (a,b)  => UInt(0)),
    ("f>" , (a,b) => UInt(0)),
    ("f*" , (a,b) => UInt(0)),
    ("f+" , (a,b) => UInt(0)),
    ("passA" , (a,b) => a),
    ("passB" , (a,b) => b)
  )
  def opcodes = _opcodes
  def opcodes_=(x: List[(String, (UInt, UInt) => UInt)]) { _opcodes = x }

  def size = opcodes.size

  def getCode(op: String): Int = {
    opcodes.indexWhere (_._1 == op)
  }

  def getOp(i: Int, a: UInt, b: UInt): UInt = {
    opcodes(i)._2(a, b)
  }

  def getOpLambda(op: String) = {
    opcodes(getCode(op))._2
  }

}

class IntFU(val w: Int, useFMA: Boolean = true, useFPComp: Boolean = true) extends Module {
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
    val opcode = UInt(INPUT,  log2Up(Opcodes.opcodes.size))
    val out = UInt(OUTPUT, w)
  }
  io.out := UInt(0) // <-- Required because otherwise Chisel thinks no one writes to io.out

  // Instantiate floating point units:
  // Comparator
  val fpGt = UInt(width=1)
  val fpEq = UInt(width=1)
  val fpLt = UInt(width=1)
  if (useFPComp) {
    val fpComparator = Module(new CompareRecFN(8, 24))
    fpComparator.io.a := io.a
    fpComparator.io.b := io.b
    fpGt := fpComparator.io.gt
    fpEq := fpComparator.io.eq
    fpLt := fpComparator.io.lt
  } else {
    fpGt := UInt(0, width=1)
    fpEq := UInt(0, width=1)
    fpLt := UInt(0, width=1)
  }

  // FMA
  val fmaOut = UInt(width=w)

  // Populate opcode table
  Opcodes.opcodes = List[(String, (UInt, UInt) => UInt)](
    ("+" , (a,b)    => a+b),
    ("-" , (a,b)    => a-b),
    ("*" , (a,b)    => a*b),
    ("/" , (a,b)    => a*b),  // No divider temporarily
    ("&" , (a,b)    => a&b),
    ("|" , (a,b)    => a|b),
    ("==" , (a,b)   => a===b),
    (">" , (a,b)   => a>b),
    ("<" , (a,b)   => a<b),
    ("<<" , (a,b)   => a<<b),
    (">>" , (a,b)   => a>>b),
    ("f<" , (a,b)   => fpLt),
    ("f==" , (a,b)  => fpEq),
    ("f>" , (a,b) => fpGt),
    ("f*" , (a,b) => fmaOut),
    ("f+" , (a,b) => fmaOut),
    ("passA" , (a,b) => a),
    ("passB" , (a,b) => b)
  )

  if (useFMA) {
    println(s"Instantiating FMA")
    val fmulCode = UInt(Opcodes.getCode("f*"))
    val faddCode = UInt(Opcodes.getCode("f+"))
    val fma = Module(new MulAddRecFN(8, 24))
    fma.io.a := recFNFromFN(8, 24, io.a)
    fma.io.b := recFNFromFN(8, 24, Mux(fmulCode === io.opcode, io.b, Flo(1.0f)))
    fma.io.c := recFNFromFN(8, 24, Mux(faddCode === io.opcode, io.b, Flo(1.0f)))
    fmaOut := fNFromRecFN(8, 24, fma.io.out)
  } else {
    println(s"No FMA")
    fmaOut := UInt(0, width=w)
  }

  // Instantiate result mux
  val ins = Vec.tabulate(Opcodes.opcodes.size) { i =>
    Opcodes.getOp(i, io.a, io.b)
  }
  val m = if (Globals.noModule) new MuxNL(Opcodes.opcodes.size, w) else Module(new MuxN(Opcodes.opcodes.size, w))
  m.io.ins.zip(ins).foreach { case (in, i) =>
    in := i
  }
  m.io.sel := io.opcode
  io.out := m.io.out
}


class FPCompReg(val exponent: Int = 8, mantissa: Int = 24) extends Module {
  val w = exponent + mantissa
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
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

  val fpComparator = Module(new CompareRecFN(8, 24))
  fpComparator.io.a := a
  fpComparator.io.b := b

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := Cat(fpComparator.io.lt, fpComparator.io.eq, fpComparator.io.gt)
  io.out := outReg.io.data.out
}

class FMAReg(val exponent: Int = 8, mantissa: Int = 24) extends Module {
  val w = exponent + mantissa
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
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

  val fma = Module(new MulAddRecFN(exponent, mantissa))
  fma.io.a := a
  fma.io.b := b
  fma.io.c := b

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := fma.io.out
  io.out := outReg.io.data.out
}

class IntFUReg(val w: Int, useFMA: Boolean, useFPComp: Boolean) extends Module {
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
    val opcode = UInt(INPUT,  log2Up(Opcodes.opcodes.size))
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

  val opcodeReg = Module(new FF(log2Up(Opcodes.opcodes.size)))
  opcodeReg.io.control.enable := Bool(true)
  opcodeReg.io.data.in := io.opcode
  val op = opcodeReg.io.data.out


  val fu = Module(new IntFU(w, useFMA, useFPComp))
  fu.io.a := a
  fu.io.b := b
  fu.io.opcode := op

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := fu.io.out
  io.out := outReg.io.data.out
}


/**
 * IntFU test harness
 */
class IntFUTests(c: IntFUReg) extends Tester(c) {
  for (n <- 0 until 64) {
    val a      = rnd.nextInt(16)
    val b      = rnd.nextInt(16)
    val opcode = rnd.nextInt(8)
    var output = 0
    if (opcode == 0) {
      output = ((a+b) & 0xF)
    } else if (opcode == 1) {
      output = ((a-b) & 0xF)
    } else if (opcode == 2) {
      output = ((a*b) & 0xF)
    } else if (opcode == 3) {
      output = ((a/b) & 0xF)
    } else if (opcode == 4) {
      output = (a & b)
    } else if (opcode == 5) {
      output = (a | b)
    } else if (opcode == 6) {
      output = if (a == b) 1 else 0
    } else if (opcode == 7) {
      output = a
    } else {
      output = b
    }
    println(s"$output = $a ${Opcodes.opcodes(opcode)._1} $b")
    poke(c.io.a, a)
    poke(c.io.b, b)
    poke(c.io.opcode, opcode)
    step(1)
    expect(c.io.out, output)
  }
}

class FMATests(c: FMAReg) extends Tester(c) {
}

class FPCompTests(c: FPCompReg) extends Tester(c) {
}


object IntFUTest {
  def main(args: Array[String]): Unit = {
    val w = 32
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: IntFUTest <useFMA> <useFPComp>")
      sys.exit(-1)
    }
    val useFMA = appArgs(0).toBoolean
    val useFPComp = appArgs(1).toBoolean
    chiselMainTest(args, () => Module(new IntFUReg(w, useFMA, useFPComp))) {
      c => new IntFUTests(c)
    }
  }
}

object FMATest {
  def main(args: Array[String]): Unit = {
    val exponent = 8
    val mantissa = 24
    chiselMainTest(args, () => Module(new FMAReg(exponent, mantissa))) {
      c => new FMATests(c)
    }
  }
}

object FPCompTest {
  def main(args: Array[String]): Unit = {
    val exponent = 8
    val mantissa = 24
    chiselMainTest(args, () => Module(new FPCompReg(exponent, mantissa))) {
      c => new FPCompTests(c)
    }
  }
}
