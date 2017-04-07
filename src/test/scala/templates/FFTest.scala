// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * FF test harness
 */
class FFUnitTester(c: FF)(implicit args: Array[String]) extends ArgsTester(c) {
  val initval = 10
  poke(c.io.init, initval)
  step(1)
  reset(1)
  expect(c.io.out, initval)

  val con = new scala.tools.jline.console.ConsoleReader()
  println( con.readLine() )
  // Poke internal register directly
  println("Poking internal register directly")
  poke(c.ff, 1000)
  val out = peek(c.io.out)
  println(s"out = $out, poked 1000")

  val numCycles = 10
  for (i <- 0 until numCycles) {
    val newenable = rnd.nextInt(2)
    val oldout = peek(c.io.out)
    poke(c.io.in, i)
    poke(c.io.enable, 0)
    poke(c.io.enable, newenable)
    step(1)
    val out = peek(c.io.out)
    println(s"in = $i, enable = $newenable, out = $out")
    if (newenable == 1) expect(c.io.out, i) else expect(c.io.out, oldout)
  }
}

class TFFUnitTester(c: TFF)(implicit args: Array[String]) extends ArgsTester(c) {
  step(1)
  reset(1)
  expect(c.io.out, 0)
  val numCycles = 10
  for (i <- 0 until numCycles) {
    val newenable = rnd.nextInt(2)
    val oldout = peek(c.io.out)
    poke(c.io.enable, newenable)
    step(1)
    if (newenable == 1) expect(c.io.out, ~oldout) else expect(c.io.out, oldout)
  }
}

object FFTest extends CommonMain {
  type DUTType = FF
  def dut = () => new FF(args(0).toInt)
  def tester = { c: DUTType => new FFUnitTester(c) }
}

