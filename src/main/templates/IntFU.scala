package plasticine.templates

import Chisel._

/**
 * Integer Functional Unit module. Represents the basic workhorse
 * within a Compute Unit (CU) in Plasticine.
 * All operations are designed to take a single cycle.
 * @param w: Word width
 */
class IntFU(val w: Int) extends Module {
  val io = new Bundle {
    val a      = UInt(INPUT,  w)
    val b      = UInt(INPUT,  w)
    val opcode = UInt(INPUT,  4)
    val out = UInt(OUTPUT, w)
  }
  io.out := UInt(0) // <-- Required because otherwise Chisel thinks no one writes to io.out

  when (io.opcode === UInt(0)) {
    io.out := io.a + io.b // ADD
  } .elsewhen (io.opcode === UInt(1)) {
    io.out := io.a - io.b // SUB
  } .elsewhen (io.opcode === UInt(2)) {
    io.out := io.a * io.b // MUL
  } .elsewhen (io.opcode === UInt(3)) {
    io.out := io.a / io.b // DIV
  } .elsewhen (io.opcode === UInt(4)) {
    io.out := io.a & io.b
  } .elsewhen (io.opcode === UInt(5)) {
    io.out := io.a | io.b
  } .elsewhen (io.opcode === UInt(6)) {
    io.out := io.a === io.b
  } .elsewhen (io.opcode === UInt(7)) {
    io.out := io.a  	     // PASS A
  } .otherwise {
    io.out := io.b         // PASS B
  }
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
