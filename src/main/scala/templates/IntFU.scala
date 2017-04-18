package plasticine.templates

import chisel3._
import chisel3.util._
//import plasticine.templates.hardfloat._

import plasticine.templates.Utils.log2Up

import scala.collection.immutable.Map
import plasticine.pisa.enums._

/**
 * Integer Functional Unit module. Represents the basic workhorse
 * within a Compute Unit (CU) in Plasticine.
 * All operations are designed to take a single cycle.
 * @param w: Word width
 */

object Opcodes {
  // HACK: Duplicated with IntFU. Need to refactor this into
  // a separate Decode table
  private var _opcodes = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixAdd , (a,b,c)    => a+b),
    (FixSub , (a,b,c)    => a-b),
    (FixMul , (a,b,c)    => a*b),
    (FixDiv , (a,b,c)    => a*b),  // No divider temporarily
    (FixAnd , (a,b,c)    => a&b),
    (FixOr , (a,b,c)    => a|b),
    (FixEql , (a,b,c)   => a===b),
    (FixGt , (a,b,c)   => a>b),
    (FixLt , (a,b,c)   => a<b),
    (FixSHL , (a,b,c)   => a<<b),
    (FixSHR , (a,b,c)   => a>>b),
    (FltLt , (a,b,c)   => UInt(0)),
    (FltEql , (a,b,c)  => UInt(0)),
    (FltGt , (a,b,c) => UInt(0)),
    (FltMul , (a,b,c) => UInt(0)),
    (FltAdd , (a,b,c) => UInt(0)),
    (MuxOp , (a,b,c) => Mux(c(0), a, b)),
    (FixMin , (a,b,c) => Mux(a<b, a, b)),
    (FixMax , (a,b,c) => Mux(a>b, a, b)),
    (BypassA , (a,b,c) => a),
    (BypassB , (a,b,c) => b),
    (BypassC , (a,b,c) => c)
  )
  def opcodes = _opcodes
  def opcodes_=(x: List[(Opcode, (UInt, UInt, UInt) => UInt)]) { _opcodes = x }

  def size = opcodes.size

  def getCode(op: Opcode): Int = {
    op match {
      case XOp => 0
      case _ => opcodes.indexWhere (_._1 == op)
    }
  }

  def getOp(i: Int, a: UInt, b: UInt, c: UInt = UInt(0)): UInt = {
    opcodes(i)._2(a, b, c)
  }

  def getOpLambda(op: Opcode) = {
    opcodes(getCode(op))._2
  }

}

class IntFU(val w: Int, useFMA: Boolean = true, useFPComp: Boolean = true) extends Module {
  val io = IO(new Bundle {
    val a      = Input(UInt(w.W))
    val b      = Input(UInt(w.W))
    val c      = Input(UInt(w.W))
    val opcode = Input(UInt(log2Up(Opcodes.opcodes.size).W))
    val out = Output(UInt(w.W))
  })
  io.out := UInt(0) // <-- Required because otherwise Chisel thinks no one writes to io.out

  // Instantiate floating point units:
  // Comparator
  val fpGt = Wire(UInt(1.W))
  val fpEq = Wire(UInt(1.W))
  val fpLt = Wire(UInt(1.W))
//  if (useFPComp) {
//    val fpComparator = Module(new CompareRecFN(8, 24))
//    fpComparator.io.a := io.a
//    fpComparator.io.b := io.b
//    fpGt := fpComparator.io.gt
//    fpEq := fpComparator.io.eq
//    fpLt := fpComparator.io.lt
//  } else {
    fpGt :=0.U
    fpEq :=0.U
    fpLt :=0.U
//  }

  // FMA
  val fmaOut = Wire(UInt(w.W))

  // Populate opcode table
  Opcodes.opcodes = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixAdd , (a,b,c)    => a+b),
    (FixSub , (a,b,c)    => a-b),
    (FixMul , (a,b,c)    => a*b),
    (FixDiv , (a,b,c)    => a*b),  // No divider temporarily
    (FixAnd , (a,b,c)    => a&b),
    (FixOr , (a,b,c)    => a|b),
    (FixEql , (a,b,c)   => a===b),
    (FixGt , (a,b,c)   => a>b),
    (FixLt , (a,b,c)   => a<b),
    (FixSHL , (a,b,c)   => a<<b),
    (FixSHR , (a,b,c)   => a>>b),
    (FltLt , (a,b,c)   => fpLt),
    (FltEql , (a,b,c)  => fpEq),
    (FltGt , (a,b,c) => fpGt),
    (FltMul , (a,b,c) => fmaOut),
    (FltAdd , (a,b,c) => fmaOut),
    (MuxOp , (a,b,c) => Mux(c(0), a, b)),
    (FixMin , (a,b,c) => Mux(a<b, a, b)),
    (FixMax , (a,b,c) => Mux(a>b, a, b)),
    (BypassA , (a,b,c) => a),
    (BypassB , (a,b,c) => b),
    (BypassC , (a,b,c) => c)
  )

//  if (useFMA) {
//    val fmulCode = UInt(Opcodes.getCode("f*"))
//    val faddCode = UInt(Opcodes.getCode("f+"))
//    val fma = Module(new MulAddRecFN(8, 24))
//    fma.io.a := recFNFromFN(8, 24, io.a)
//    fma.io.b := recFNFromFN(8, 24, Mux(fmulCode === io.opcode, io.b, Flo(1.0f)))
//    fma.io.c := recFNFromFN(8, 24, Mux(faddCode === io.opcode, io.b, Flo(1.0f)))
//    fmaOut := fNFromRecFN(8, 24, fma.io.out)
//  } else {
    fmaOut := 0.U
//  }

  // Instantiate result mux
  val ins = Vec.tabulate(Opcodes.opcodes.size) { i =>
    Opcodes.getOp(i, io.a, io.b, io.c)
  }
  val m = Module(new MuxN(UInt(w.W), Opcodes.opcodes.size))
  m.io.ins.zip(ins).foreach { case (in, i) =>
    in := i
  }
  m.io.sel := io.opcode
  io.out := m.io.out
}


//class FPCompReg(val exponent: Int = 8, mantissa: Int = 24) extends Module {
//  val w = exponent + mantissa
//  val io = new Bundle {
//    val a      = Input(UInt(w.W))
//    val b      = Input(UInt(w.W))
//    val out = Output(UInt(w.W))
//  }
//
//  // Register the inputs
//  val aReg = Module(new FF(w))
//  aReg.io.enable := Bool(true)
//  aReg.io.in := io.a
//  val a = aReg.io.out
//
//  val bReg = Module(new FF(w))
//  bReg.io.enable := Bool(true)
//  bReg.io.in := io.b
//  val b = bReg.io.out
//
//  val fpComparator = Module(new CompareRecFN(8, 24))
//  fpComparator.io.a := a
//  fpComparator.io.b := b
//
//  // Register the output
//  val outReg = Module(new FF(w))
//  outReg.io.enable := Bool(true)
//  outReg.io.in := Cat(fpComparator.io.lt, fpComparator.io.eq, fpComparator.io.gt)
//  io.out := outReg.io.out
//}
//
//class FMAReg(val exponent: Int = 8, mantissa: Int = 24) extends Module {
//  val w = exponent + mantissa
//  val io = new Bundle {
//    val a      = Input(UInt(w.W))
//    val b      = Input(UInt(w.W))
//    val out = Output(UInt(w.W))
//  }
//
//  // Register the inputs
//  val aReg = Module(new FF(w))
//  aReg.io.enable := Bool(true)
//  aReg.io.in := io.a
//  val a = aReg.io.out
//
//  val bReg = Module(new FF(w))
//  bReg.io.enable := Bool(true)
//  bReg.io.in := io.b
//  val b = bReg.io.out
//
//  val fma = Module(new MulAddRecFN(exponent, mantissa))
//  fma.io.a := a
//  fma.io.b := b
//  fma.io.c := b
//
//  // Register the output
//  val outReg = Module(new FF(w))
//  outReg.io.enable := Bool(true)
//  outReg.io.in := fma.io.out
//  io.out := outReg.io.out
//}

class IntFUReg(val w: Int, useFMA: Boolean, useFPComp: Boolean) extends Module {
  val io = new Bundle {
    val a      = Input(UInt(w.W))
    val b      = Input(UInt(w.W))
    val c      = Input(UInt(w.W))
    val opcode = Input(UInt(log2Up(Opcodes.opcodes.size).W))
    val out = Output(UInt(w.W))
  }

  // Register the inputs
  val aReg = Module(new FF(w))
  aReg.io.enable := Bool(true)
  aReg.io.in := io.a
  val a = aReg.io.out

  val bReg = Module(new FF(w))
  bReg.io.enable := Bool(true)
  bReg.io.in := io.b
  val b = bReg.io.out

  val cReg = Module(new FF(w))
  cReg.io.enable := Bool(true)
  cReg.io.in := io.c
  val c = bReg.io.out

  val opcodeReg = Module(new FF(log2Up(Opcodes.opcodes.size)))
  opcodeReg.io.enable := Bool(true)
  opcodeReg.io.in := io.opcode
  val op = opcodeReg.io.out


  val fu = Module(new IntFU(w, useFMA, useFPComp))
  fu.io.a := a
  fu.io.b := b
  fu.io.b := c
  fu.io.opcode := op

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.enable := Bool(true)
  outReg.io.in := fu.io.out
  io.out := outReg.io.out
}


/**
 * IntFU test harness
 */
//class IntFUTests(c: IntFUReg) extends Tester(c) {
//  for (n <- 0 until 64) {
//    val a      = rnd.nextInt(16)
//    val b      = rnd.nextInt(16)
//    val opcode = rnd.nextInt(8)
//    var output = 0
//    if (opcode == 0) {
//      output = ((a+b) & 0xF)
//    } else if (opcode == 1) {
//      output = ((a-b) & 0xF)
//    } else if (opcode == 2) {
//      output = ((a*b) & 0xF)
//    } else if (opcode == 3) {
//      output = ((a/b) & 0xF)
//    } else if (opcode == 4) {
//      output = (a & b)
//    } else if (opcode == 5) {
//      output = (a | b)
//    } else if (opcode == 6) {
//      output = if (a == b) 1 else 0
//    } else if (opcode == 7) {
//      output = a
//    } else {
//      output = b
//    }
//    println(s"$output = $a ${Opcodes.opcodes(opcode)._1} $b")
//    poke(c.io.a, a)
//    poke(c.io.b, b)
//    poke(c.io.opcode, opcode)
//    step(1)
//    expect(c.io.out, output)
//  }
//}
//
//class FMATests(c: FMAReg) extends Tester(c)
//class FPCompTests(c: FPCompReg) extends Tester(c)
//
//object IntFUTest {
//  def main(args: Array[String]): Unit = {
//    val w = 32
//    val appArgs = args.take(args.indexOf("end"))
//    if (appArgs.size < 2) {
//      println("Usage: IntFUTest <useFMA> <useFPComp>")
//      sys.exit(-1)
//    }
//    val useFMA = appArgs(0).toBoolean
//    val useFPComp = appArgs(1).toBoolean
//    chiselMainTest(args, () => Module(new IntFUReg(w, useFMA, useFPComp))) {
//      c => new IntFUTests(c)
//    }
//  }
//}
//
//object FMATest {
//  def main(args: Array[String]): Unit = {
//    val exponent = 8
//    val mantissa = 24
//    chiselMainTest(args, () => Module(new FMAReg(exponent, mantissa))) {
//      c => new FMATests(c)
//    }
//  }
//}
//
//object FPCompTest {
//  def main(args: Array[String]): Unit = {
//    val exponent = 8
//    val mantissa = 24
//    chiselMainTest(args, () => Module(new FPCompReg(exponent, mantissa))) {
//      c => new FPCompTests(c)
//    }
//  }
//}
