package plasticine.templates

import Chisel._

/**
 * TopUnit: Command and Status registers that generate the first token,
 * and wait for the last token respectively.
 */
class TopUnit() extends Module {
  val io = new Bundle {
//    val configEnable = UInt(INPUT, width=1)
    val command = UInt(INPUT, width=1)
    val doneTokenIn = Bool(INPUT)
    val startTokenOut = UInt(OUTPUT, width=1)
    val statusOut = UInt(OUTPUT, width=1)
//    val configEnableOut = UInt(OUTPUT, width=1)
  }

  val commandReg = Reg(UInt(1), io.command, UInt(0))
  val pulser = Module(new Pulser())
  pulser.io.in := commandReg
  io.startTokenOut := pulser.io.out

  val depulser = Module(new Depulser())
  depulser.io.in := io.doneTokenIn
  depulser.io.rst := ~io.command

  val statusReg = Reg(Bits(1), depulser.io.out, UInt(0))
  io.statusOut := statusReg
//  io.configEnableOut := io.configEnable
}

/**
 * TopUnit test harness
 */
class TopUnitTests(c: TopUnit) extends Tester(c) {
  step(10)
  poke(c.io.command, 1)
  expect(c.io.startTokenOut, 0)
  step(1)
  expect(c.io.startTokenOut, 1)
  step(1)
  expect(c.io.startTokenOut, 0)
  step(20)
  poke(c.io.doneTokenIn, 1)
  step(1)
  poke(c.io.doneTokenIn, 0)
  expect(c.io.statusOut, 0)
  step(1)
  expect(c.io.statusOut, 1)
  step(5)
  expect(c.io.statusOut, 1)
  poke(c.io.command, 0)
  step(1)
  expect(c.io.statusOut, 1)
  step(1)
  expect(c.io.statusOut, 0)
}

object TopUnitTest {

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new TopUnit())) {
      c => new TopUnitTests(c)
    }
  }
}
