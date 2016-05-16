package plasticine.test

import Chisel._
import plasticine.templates.Parity

class ParityTests(c: Parity) extends Tester(c) {
  var isOdd = 0
  for (t <- 0 until 10) {
    val bit = rnd.nextInt(2)
    poke(c.io.in, bit)
    step(1)
    isOdd = (isOdd + bit) % 2;
    expect(c.io.out, isOdd)
  }
}

object ParityTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new Parity())) {
      c => new ParityTests(c)
    }
  }
}

