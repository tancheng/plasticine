package templates

import chisel3._
import chisel3.util._

import templates.Utils.log2Up

import scala.collection.immutable.Map
import plasticine.pisa.enums._
import scala.language.reflectiveCalls
import plasticine.misc.Utils._
import hardfloat._

/**
 * Integer Functional Unit module. Represents the basic workhorse
 * within a Compute Unit (CU) in Plasticine.
 * All operations are designed to take a single cycle.
 * @param w: Word width
 */

object Opcodes {
  // HACK: Duplicated with FU. Need to refactor this into
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
    (MuxOp , (a,b,c) => Mux(c(0), a, b)),
    (FixMin , (a,b,c) => Mux(a<b, a, b)),
    (FixMax , (a,b,c) => Mux(a>b, a, b)),
    (BypassA , (a,b,c) => a),
    (BypassB , (a,b,c) => b),
    (BypassC , (a,b,c) => c),
//    (FixSHL , (a,b,c)   => a << b(18,0)),  // Chisel does not allow shift amount to be >20 bits
//    (FixSHR , (a,b,c)   => a >> b(18,0)),
    (FltMul , (a,b,c) => 0.U),
    (FltAdd , (a,b,c) => 0.U),
    (FltLt , (a,b,c)   => 0.U),
    (FltEql , (a,b,c)  => 0.U),
    (FltGt , (a,b,c) => 0.U),
    (FltMul16 , (a,b,c) => 0.U),
    (FltAdd16 , (a,b,c) => 0.U),
    (FltLt16 , (a,b,c)   => 0.U),
    (FltEql16 , (a,b,c)  => 0.U),
    (FltGt16 , (a,b,c) => 0.U),
    (FltMul16 , (a,b,c) => 0.U),
    (FltAdd16 , (a,b,c) => 0.U),
    (FltLt8 , (a,b,c)   => 0.U),
    (FltEql8 , (a,b,c)  => 0.U),
    (FltGt8 , (a,b,c) => 0.U),
    (FixMul16, (a,b,c)    => List.tabulate(2) { i => (a(i*16+16-1, i*16) * b(i*16+16-1, i*16))(15, 0) }.reduce { Cat(_,_) }),
    (FixAdd16, (a,b,c)    => List.tabulate(2) { i => (a(i*16+16-1, i*16) + b(i*16+16-1, i*16))(15, 0) }.reduce { Cat(_,_) }),
    (FixMul8, (a,b,c)    => List.tabulate(4) { i => (a(i*8+8-1, i*8) * b(i*8+8-1, i*8))(7, 0) }.reduce { Cat(_,_) }),
    (FixAdd8, (a,b,c)    => List.tabulate(4) { i => (a(i*8+8-1, i*8) + b(i*8+8-1, i*8))(7, 0) }.reduce { Cat(_,_) })
  )

//  private var _opcodes = List[(Opcode, (UInt, UInt, UInt) => UInt)](
//    (FixAdd , (a,b,c)    => a+b),
////    (FixSub , (a,b,c)    => a-b)
//    (FixMul , (a,b,c)    => a*b),
////    (FixDiv , (a,b,c)    => a*b),  // No divider temporarily
////    (FixAnd , (a,b,c)    => a&b),
////    (FixOr , (a,b,c)    => a|b),
////    (FixEql , (a,b,c)   => a===b),
////    (FixGt , (a,b,c)   => a>b),
////    (FixLt , (a,b,c)   => a<b),
////    (FixSHL , (a,b,c)   => a<<b),
////    (FixSHR , (a,b,c)   => a>>b),
////    (FltLt , (a,b,c)   => 0.U),
////    (FltEql , (a,b,c)  => 0.U),
////    (FltGt , (a,b,c) => 0.U),
////    (FltMul , (a,b,c) => 0.U),
////    (FltAdd , (a,b,c) => 0.U),
////    (MuxOp , (a,b,c) => Mux(c(0), a, b)),
////    (FixMin , (a,b,c) => Mux(a<b, a, b)),
////    (FixMax , (a,b,c) => Mux(a>b, a, b)),
//    (BypassA , (a,b,c) => a)
////    (BypassB , (a,b,c) => b),
////    (BypassC , (a,b,c) => c)
//  )
  def opcodes = _opcodes
  def opcodes_=(x: List[(Opcode, (UInt, UInt, UInt) => UInt)]) { _opcodes = x }

  def size = opcodes.size

  def getCode(op: Opcode): Int = {
    op match {
      case XOp => 0
      case _ => opcodes.indexWhere (_._1 == op)
    }
  }

  def getOp(i: Int, a: UInt, b: UInt, c: UInt = 0.U): UInt = {
    opcodes(i)._2(a, b, c)
  }

  def getOpLambda(op: Opcode) = {
    opcodes(getCode(op))._2
  }

}

class FU(
  val w: Int,
  useFMA: Boolean       = true,
  useFPComp: Boolean    = true,
  useFix16: Boolean     = false,
  useFP16: Boolean      = false,
  useFix8: Boolean      = false,
  useFP8: Boolean       = false
) extends Module {
  val io = IO(new Bundle {
    val a      = Input(UInt(w.W))
    val b      = Input(UInt(w.W))
    val c      = Input(UInt(w.W))
    val opcode = Input(UInt(log2Up(Opcodes.opcodes.size).W))
    val out = Output(UInt(w.W))
  })
  io.out := 0.U // <-- Required because otherwise Chisel thinks no one writes to io.out

  lazy val suffix = "base" + (if (useFMA) "_fma32" else "") +
               (if (useFP16) "_fma16x2" else "") +
               (if (useFP8) "_fma8x4" else "") +
               (if (useFPComp) "_fpcomp" else "") +
               (if (useFix16) "_fix16" else "") +
               (if (useFix8) "_fix8" else "")

  override def desiredName = this.getClass.getName.split('.').last + suffix

  // Instantiate floating point units:
  // Comparator
  val fpGt = Wire(UInt(w.W))
  val fpEq = Wire(UInt(w.W))
  val fpLt = Wire(UInt(w.W))
  val fpGt16 = Wire(UInt(w.W))
  val fpEq16 = Wire(UInt(w.W))
  val fpLt16 = Wire(UInt(w.W))
  val fpGt8 = Wire(UInt(w.W))
  val fpEq8 = Wire(UInt(w.W))
  val fpLt8 = Wire(UInt(w.W))

  // FMA
  val fmaOut = Wire(UInt(w.W))
  val fma16Out = Wire(UInt(w.W))
  val fma8Out = Wire(UInt(w.W))

  // Populate opcode table
  val opcodesBase = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixAdd , (a,b,c)    => a+b),
    (FixSub , (a,b,c)    => a-b),
    (FixMul , (a,b,c)    => a*b),
    (FixDiv , (a,b,c)    => a*b),  // No divider temporarily
    (FixAnd , (a,b,c)    => a&b),
    (FixOr , (a,b,c)    => a|b),
    (FixEql , (a,b,c)   => a===b),
    (FixGt , (a,b,c)   => a>b),
    (FixLt , (a,b,c)   => a<b),
    (MuxOp , (a,b,c) => Mux(c(0), a, b)),
    (FixMin , (a,b,c) => Mux(a<b, a, b)),
    (FixMax , (a,b,c) => Mux(a>b, a, b)),
    (BypassA , (a,b,c) => a),
    (BypassB , (a,b,c) => b),
    (BypassC , (a,b,c) => c)
//    (FixSHL , (a,b,c)   => a << b(18,0)),  // Chisel does not allow shift amount to be >20 bits
//    (FixSHR , (a,b,c)   => a >> b(18,0)),
  )

  val fp32Base = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltMul , (a,b,c) => fmaOut),
    (FltAdd , (a,b,c) => fmaOut)
  )
  val fp32Comp = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltLt , (a,b,c)   => fpLt),
    (FltEql , (a,b,c)  => fpEq),
    (FltGt , (a,b,c) => fpGt)
  )
  val fp16Base = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltMul16 , (a,b,c) => fma16Out),
    (FltAdd16 , (a,b,c) => fma16Out)
  )
  val fp16Comp = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltLt16 , (a,b,c)   => fpLt16),
    (FltEql16 , (a,b,c)  => fpEq16),
    (FltGt16 , (a,b,c) => fpGt16)
  )
  val fp8Base = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltMul16 , (a,b,c) => fma8Out),
    (FltAdd16 , (a,b,c) => fma8Out)
  )
  val fp8Comp = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FltLt8 , (a,b,c)   => fpLt8),
    (FltEql8 , (a,b,c)  => fpEq8),
    (FltGt8 , (a,b,c) => fpGt8)
  )

  val fix16Base = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixMul16, (a,b,c)    => List.tabulate(2) { i => (a(i*16+16-1, i*16) * b(i*16+16-1, i*16))(15, 0) }.reduce { Cat(_,_) }),
    (FixAdd16, (a,b,c)    => List.tabulate(2) { i => (a(i*16+16-1, i*16) + b(i*16+16-1, i*16))(15, 0) }.reduce { Cat(_,_) })
  )
  val fix16Comp = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixEql16, (a,b,c) => List.tabulate(2) { i => Cat(Fill(15, 0.U), a(i*16+16-1, i*16) === b(i*16+16-1, i*16)) }.reduce { Cat(_,_) }),
    (FixGt16, (a,b,c)  => List.tabulate(2) { i => Cat(Fill(15, 0.U), a(i*16+16-1, i*16) > b(i*16+16-1, i*16)) }.reduce { Cat(_,_) }),
    (FixLt16, (a,b,c)  => List.tabulate(2) { i => Cat(Fill(15, 0.U), a(i*16+16-1, i*16) < b(i*16+16-1, i*16)) }.reduce { Cat(_,_) })
  )

  val fix8Base = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixMul8, (a,b,c)    => List.tabulate(4) { i => (a(i*8+8-1, i*8) * b(i*8+8-1, i*8))(7, 0) }.reduce { Cat(_,_) }),
    (FixAdd8, (a,b,c)    => List.tabulate(4) { i => (a(i*8+8-1, i*8) + b(i*8+8-1, i*8))(7, 0) }.reduce { Cat(_,_) })
  )
  val fix8Comp = List[(Opcode, (UInt, UInt, UInt) => UInt)](
    (FixEql8, (a,b,c) => List.tabulate(4) { i => Cat(Fill(7, 0.U), a(i*8+8-1, i*8) === b(i*8+8-1, i*8)) }.reduce { Cat(_,_) }),
    (FixGt8, (a,b,c)  => List.tabulate(4) { i => Cat(Fill(7, 0.U), a(i*8+8-1, i*8) > b(i*8+8-1, i*8)) }.reduce { Cat(_,_) }),
    (FixLt8, (a,b,c)  => List.tabulate(4) { i => Cat(Fill(7, 0.U), a(i*8+8-1, i*8) < b(i*8+8-1, i*8)) }.reduce { Cat(_,_) })
  )

  val empty = List[(Opcode, (UInt, UInt, UInt) => UInt)]()

  val fpOpcodes = (if (useFMA) { fp32Base ++ (if (useFPComp) fp32Comp else empty) } else empty) ++
                  (if (useFP16) { fp16Base ++ (if (useFPComp) fp16Comp else empty) } else empty) ++
                  (if (useFP8) { fp8Base ++ (if (useFPComp) fp8Comp else empty) } else empty)

  val fixOpcodes = (if (useFix16) { fix16Base ++ (if (useFPComp) fix16Comp else empty) } else empty) ++
                  (if (useFix8) { fix8Base ++ (if (useFPComp) fix8Comp else empty) } else empty)

  Opcodes.opcodes = opcodesBase ++ fpOpcodes ++ fixOpcodes

  // 32-bit
  val exponent = 8
  val frac = 24
  if (useFMA) {
    val fmulCode = Opcodes.getCode(FltMul).U
    val faddCode = Opcodes.getCode(FltAdd).U
    val fma = Module(new MulAddRecFN(exponent, frac - 1))
    fma.io.a := recFNFromFN(exponent, frac - 1, io.a)
    fma.io.b := recFNFromFN(exponent, frac - 1, Mux(fmulCode === io.opcode, io.b.asSInt, getFloatBits(1.0f).S))
    fma.io.c := recFNFromFN(exponent, frac - 1, Mux(faddCode === io.opcode, io.b.asSInt, getFloatBits(0.0f).S))
    fmaOut := fNFromRecFN(exponent, frac - 1, fma.io.out)

    if (useFPComp) {
      val fpComparator = Module(new CompareRecFN(exponent, frac - 1))
      fpComparator.io.a := io.a
      fpComparator.io.b := io.b
      fpGt := Cat(Fill(31, 0.U), fpComparator.io.gt)
      fpEq := Cat(Fill(31, 0.U), fpComparator.io.eq)
      fpLt := Cat(Fill(31, 0.U), fpComparator.io.lt)
    } else {
      fpGt := 0.U
      fpEq := 0.U
      fpLt := 0.U
    }
  } else {
    fmaOut := 0.U
    fpGt := 0.U
    fpEq := 0.U
    fpLt := 0.U
  }

  // 16-bit units
  if (useFP16) {
    val fmulCode16 = Opcodes.getCode(FltMul16).U
    val faddCode16 = Opcodes.getCode(FltAdd16).U
    val width = 16
    val localExponent = 5
    val localFrac = 10
//    val width = 32
//    val localExponent = 8
//    val localFrac = 23

    val numUnits = 32 / width
    val outs16 = List.tabulate(numUnits) { i =>
      val fma16 = Module(new MulAddRecFN(localExponent, localFrac))
      fma16.io.a := recFNFromFN(localExponent, localFrac, io.a(i*width+width-1, i*width))
      fma16.io.b := recFNFromFN(localExponent, localFrac, Mux(fmulCode16 === io.opcode, io.b(i*width+width-1, i*width).asSInt, getFloatBits(0.0f).S))
      fma16.io.c := recFNFromFN(localExponent, localFrac, Mux(faddCode16 === io.opcode, io.b(i*width+width-1, i*width).asSInt, getFloatBits(0.0f).S))
      fNFromRecFN(localExponent, localFrac, fma16.io.out)
    }
    fma16Out := outs16.reduce { Cat(_,_) }

    if (useFPComp) {
      val outs16 = List.tabulate(numUnits) { i =>
        val fpComparator = Module(new CompareRecFN(localExponent, localFrac))
        fpComparator.io.a := io.a(i*width+width-1, i*width)
        fpComparator.io.b := io.b(i*width+width-1, i*width)
        (fpComparator.io.gt, fpComparator.io.eq, fpComparator.io.lt)
      }

      fpGt16 := outs16.map { s => Cat(Fill(width-1, 0.U), s._1) }.reduce { Cat(_,_) }
      fpEq16 := outs16.map { s => Cat(Fill(width-1, 0.U), s._2) }.reduce { Cat(_,_) }
      fpLt16 := outs16.map { s => Cat(Fill(width-1, 0.U), s._3) }.reduce { Cat(_,_) }
    } else {
      fpGt16 := 0.U
      fpEq16 := 0.U
      fpLt16 := 0.U
    }
  } else {
    fma16Out := 0.U
    fpGt16 := 0.U
    fpEq16 := 0.U
    fpLt16 := 0.U
  }

  // 8-bit units
  if (useFP8) {
    val fmulCode8 = Opcodes.getCode(FltMul8).U
    val faddCode8 = Opcodes.getCode(FltAdd8).U
    val width = 8
    val localExponent = 4
    val localFrac = 3

    val numUnits = 32 / width
    val outs8 = List.tabulate(numUnits) { i =>
      val fma8 = Module(new MulAddRecFN(localExponent, localFrac))
      fma8.io.a := recFNFromFN(localExponent, localFrac, io.a(i*width+width-1, i*width))
      fma8.io.b := recFNFromFN(localExponent, localFrac, Mux(fmulCode8 === io.opcode, io.b(i*width+width-1, i*width).asSInt, getFloatBits(0.0f).S))
      fma8.io.c := recFNFromFN(localExponent, localFrac, Mux(faddCode8 === io.opcode, io.b(i*width+width-1, i*width).asSInt, getFloatBits(0.0f).S))
      fNFromRecFN(localExponent, localFrac, fma8.io.out)
    }
    fma8Out := outs8.reduce { Cat(_,_) }

    if (useFPComp) {
      val outs8 = List.tabulate(numUnits) { i =>
        val fpComparator = Module(new CompareRecFN(localExponent, localFrac))
        fpComparator.io.a := io.a(i*width+width-1, i*width)
        fpComparator.io.b := io.b(i*width+width-1, i*width)
        (fpComparator.io.gt, fpComparator.io.eq, fpComparator.io.lt)
      }

      fpGt8 := outs8.map { s => Cat(Fill(width-1, 0.U), s._1) }.reduce { Cat(_,_) }
      fpEq8 := outs8.map { s => Cat(Fill(width-1, 0.U), s._2) }.reduce { Cat(_,_) }
      fpLt8 := outs8.map { s => Cat(Fill(width-1, 0.U), s._3) }.reduce { Cat(_,_) }
    } else {
      fpGt8 := 0.U
      fpEq8 := 0.U
      fpLt8 := 0.U
    }
  } else {
    fma8Out := 0.U
    fpGt8 := 0.U
    fpEq8 := 0.U
    fpLt8 := 0.U
  }

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
//  aReg.io.enable := true.B
//  aReg.io.in := io.a
//  val a = aReg.io.out
//
//  val bReg = Module(new FF(w))
//  bReg.io.enable := true.B
//  bReg.io.in := io.b
//  val b = bReg.io.out
//
//  val fpComparator = Module(new CompareRecFN(8, 24))
//  fpComparator.io.a := a
//  fpComparator.io.b := b
//
//  // Register the output
//  val outReg = Module(new FF(w))
//  outReg.io.enable := true.B
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
//  aReg.io.enable := true.B
//  aReg.io.in := io.a
//  val a = aReg.io.out
//
//  val bReg = Module(new FF(w))
//  bReg.io.enable := true.B
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
//  outReg.io.enable := true.B
//  outReg.io.in := fma.io.out
//  io.out := outReg.io.out
//}

class FUReg(val w: Int, useFMA: Boolean, useFPComp: Boolean) extends Module {
  val io = new Bundle {
    val a      = Input(UInt(w.W))
    val b      = Input(UInt(w.W))
    val c      = Input(UInt(w.W))
    val opcode = Input(UInt(log2Up(Opcodes.opcodes.size).W))
    val out = Output(UInt(w.W))
  }

  // Register the inputs
  val aReg = Module(new FF(w))
  aReg.io.enable := true.B
  aReg.io.in := io.a
  val a = aReg.io.out

  val bReg = Module(new FF(w))
  bReg.io.enable := true.B
  bReg.io.in := io.b
  val b = bReg.io.out

  val cReg = Module(new FF(w))
  cReg.io.enable := true.B
  cReg.io.in := io.c
  val c = bReg.io.out

  val opcodeReg = Module(new FF(log2Up(Opcodes.opcodes.size)))
  opcodeReg.io.enable := true.B
  opcodeReg.io.in := io.opcode
  val op = opcodeReg.io.out


  val fu = Module(new FU(w, useFMA, useFPComp))
  fu.io.a := a
  fu.io.b := b
  fu.io.b := c
  fu.io.opcode := op

  // Register the output
  val outReg = Module(new FF(w))
  outReg.io.enable := true.B
  outReg.io.in := fu.io.out
  io.out := outReg.io.out
}


/**
 * FU test harness
 */
//class FUTests(c: FUReg) extends Tester(c) {
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
//object FUTest {
//  def main(args: Array[String]): Unit = {
//    val w = 32
//    val appArgs = args.take(args.indexOf("end"))
//    if (appArgs.size < 2) {
//      println("Usage: FUTest <useFMA> <useFPComp>")
//      sys.exit(-1)
//    }
//    val useFMA = appArgs(0).toBoolean
//    val useFPComp = appArgs(1).toBoolean
//    chiselMainTest(args, () => Module(new FUReg(w, useFMA, useFPComp))) {
//      c => new FUTests(c)
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
