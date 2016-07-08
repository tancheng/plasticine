package plasticine.templates

import Chisel._

/**
 * Vector Functional Unit: A vector container for an integer
 * Functional Unit module (for now). This module will eventually
 * be expanded to be able to create vectors of arbitrary
 * modules
 * @param vl: Vector length
 * @param w: Word width
 */
class VecFU(val vl: Int, val w: Int) extends Module {
  val io = new Bundle {
    val a      = Vec.fill(vl) { UInt(INPUT,  w) }
    val b      = Vec.fill(vl) { UInt(INPUT,  w) }
    val opcode = UInt(INPUT,  4)
    val out    = Vec.fill(vl) { UInt(OUTPUT, w) }
  }

  (0 until vl) foreach { i =>
    val m = Module(new IntFU(w))
    m.io.a := io.a(i)
    m.io.b := io.b(i)
    m.io.opcode := io.opcode
    io.out(i) := m.io.out
  }
}

/**
 * VecFU test harness
 */
class VecFUTests(c: VecFU) extends Tester(c) {
  val a      = List.fill[Int](c.vl) { rnd.nextInt(1 << c.w) }
  val b      = List.fill[Int](c.vl) { rnd.nextInt(1 << c.w) }
  for (opcode <- 0 until 8) {
    val output = opcode match {
      case 0 => a zip b map { case (ai, bi) => (ai + bi) & (1 << c.w+1)-1 }
      case 1 => a zip b map { case (ai, bi) => (ai - bi) & (1 << c.w+1)-1 }
      case 2 => a zip b map { case (ai, bi) => (ai * bi) & (1 << c.w+1)-1 }
      case 3 => a zip b map { case (ai, bi) => (ai / bi) & (1 << c.w+1)-1 }
      case 4 => a zip b map { case (ai, bi) => (ai & bi) }
      case 5 => a zip b map { case (ai, bi) => (ai | bi) }
      case 6 => a zip b map { case (ai, bi) => if (ai == bi) 1 else 0 }
      case 7 => a zip b map { case (ai, bi) => ai }
      case _ => a zip b map { case (ai, bi) => bi }
    }

    (c.io.a zip a) foreach { case (in, test) => poke(in, test) }
    (c.io.b zip b) foreach { case (in, test) => poke(in, test) }
    poke(c.io.opcode, opcode)
    step(1)
    (c.io.out zip output) foreach { case (out, expected) => expect(out, expected) }
//    expect(c.io.out, output)
  }
}

object VecFUTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new VecFU(4, 17))) {
      c => new VecFUTests(c)
    }
  }
}
