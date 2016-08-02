package plasticine.templates

import scala.collection.immutable.Map
import Chisel._

/**
 * Integer Functional Unit module. Represents the basic workhorse
 * within a Compute Unit (CU) in Plasticine.
 * All operations are designed to take a single cycle.
 * @param w: Word width
 */

object Opcodes {
  val opcodes = List[(String, (UInt, UInt) => UInt)](
    ("+" , (a,b) => a+b),
    ("-" , (a,b) => a-b),
    ("*" , (a,b) => a*b),
    ("/" , (a,b) => a/b),
    ("&" , (a,b) => a&b),
    ("|" , (a,b) => a|b),
    ("==" , (a,b) => a===b),
    ("passA" , (a,b) => a),
    ("passB" , (a,b) => b)
  )

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

  val ins = Vec.tabulate(Opcodes.opcodes.size) { i =>
    Opcodes.getOp(i, io.a, io.b)
  }

  val m = Module(new MuxN(Opcodes.opcodes.size, w))
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
