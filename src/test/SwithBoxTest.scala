package plasticine.test

import Chisel._
import plasticine.templates.SwitchBox

class SwitchBoxTests(c: SwitchBox) extends Tester(c) {
  for (i <- 0 until 16) {
    val north = rnd.nextInt(16)
    val west = rnd.nextInt(16)
    val south = rnd.nextInt(16)
    val east = rnd.nextInt(16)
    val ex_onorth = north
    val ex_owest = west
    val ex_osouth = south
    val ex_oeast = east
    poke(c.io.inorth, north)
    poke(c.io.iwest, west)
    poke(c.io.isouth, south)
    poke(c.io.ieast, east)
    step(1)
    expect(c.io.onorth, ex_onorth)
    expect(c.io.owest, ex_owest)
    expect(c.io.osouth, ex_osouth)
    expect(c.io.oeast, ex_oeast)
  }
}

object SwitchBoxTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new SwitchBox())) {
      c => new SwitchBoxTests(c)
    }
  }
}
