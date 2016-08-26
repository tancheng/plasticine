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
    ("/" , (a,b)    => a/b),
    ("&" , (a,b)    => a&b),
    ("|" , (a,b)    => a|b),
    ("==" , (a,b)   => a===b),
    ("f<" , (a,b)   => UInt(0)),
    ("f==" , (a,b)  => UInt(0)),
    ("f>" , (a,b) => UInt(0)),
    ("f*" , (a,b) => UInt(0)),
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
}

class IntFU(val w: Int) extends Module {
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
    val opcode = UInt(INPUT,  log2Up(Opcodes.opcodes.size))
    val out = UInt(OUTPUT, w)
  }
  io.out := UInt(0) // <-- Required because otherwise Chisel thinks no one writes to io.out

  // Instantiate floating point units:
  // Comparator
  val fpComparator = Module(new CompareRecFN(8, 24))
  fpComparator.io.a := io.a
  fpComparator.io.b := io.b

  // FMA
//  val fma = Module(new MulAddRecFN(8, 24))
//  fma.io.a := io.a
//  fma.io.b := io.b
//  fma.io.c := io.b

  // Populate opcode table
  Opcodes.opcodes = List[(String, (UInt, UInt) => UInt)](
    ("+" , (a,b)    => a+b),
    ("-" , (a,b)    => a-b),
    ("*" , (a,b)    => a*b),
    ("/" , (a,b)    => a/b),
    ("&" , (a,b)    => a&b),
    ("|" , (a,b)    => a|b),
    ("==" , (a,b)   => a===b),
    ("f<" , (a,b)   => UInt(fpComparator.io.lt, width=w)),
    ("f==" , (a,b)  => UInt(fpComparator.io.eq, width=w)),
    ("f>" , (a,b) => UInt(fpComparator.io.gt, width=w)),
//    ("f*" , (a,b) => fma.io.out),
    ("f*" , (a,b) => a*b),
    ("passA" , (a,b) => a),
    ("passB" , (a,b) => b)
  )

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

/**
 * IntFU test harness
 */
class IntFUTests(c: IntFU) extends Tester(c) {
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

object IntFUTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new IntFU(4))) {
      c => new IntFUTests(c)
    }
  }
}
