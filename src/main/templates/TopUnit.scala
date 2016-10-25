package plasticine.templates

import Chisel._

/**
 * TopUnit: Command and Status registers that generate the first token,
 * and wait for the last token respectively.
 */
class TopUnit(val w: Int, val v: Int) extends Module {
  val io = new Bundle {
    val addr = UInt(INPUT, width=w)
    val wdata = UInt(INPUT, width=w)
    val wen = UInt(INPUT, width=1)
    val rdata = UInt(OUTPUT, width=w)
    val doneTokenIn = Bool(INPUT)
    val startTokenOut = UInt(OUTPUT, width=1)
    val out = Vec.fill(v) { UInt(OUTPUT, width=w) }
  }

  val commandRegIdx = 0
  val statusRegIdx = commandRegIdx + 1
  val dataRegIdx = statusRegIdx + 1

  val depulser = Module(new Depulser())
  depulser.io.in := io.doneTokenIn

  val regs = List.tabulate(v+2) { i =>
    val ff = Module(new FF(w))
    val en = i match {
      case `statusRegIdx` => UInt(1)
      case _ => (io.addr === UInt(i)) & io.wen
    }
    ff.io.control.enable := en

    val wdata = i match {
      case `statusRegIdx` => depulser.io.out
      case _ => io.wdata
    }
    ff.io.data.in := wdata
    ff
  }

  // Given a List l and and idx, drop l(idx) and return rest as a list
  def dropIdx[T](l: List[T], idx: Int) = {
    val t = l.splitAt(idx)
    t._1 ++ t._2.drop(1)
  }

  val command = regs(commandRegIdx).io.data.out(0)
  val pulser = Module(new Pulser())
  pulser.io.in := command
  depulser.io.rst := ~command

  val rdatas = regs.map { _.io.data.out }
  val rdataMux = Module(new MuxN(v+2, w))
  rdataMux.io.ins := rdatas
  rdataMux.io.sel := io.addr
  io.rdata := rdataMux.io.out

  io.startTokenOut := pulser.io.out
  io.out := Vec(rdatas.slice(dataRegIdx, dataRegIdx + v))
}

/**
 * TopUnit test harness
 */
class TopUnitTests(c: TopUnit) extends Tester(c) {
  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  def write(reg: Int, value: Int) = {
    poke(c.io.addr, reg)
    poke(c.io.wdata, value)
    poke(c.io.wen, 1)
    step(1)
    poke(c.io.wen, 0)
  }

  def read(reg: Int) = {
    poke(c.io.addr, reg)
    val value = peek(c.io.rdata).toInt
    value
  }

  def writeCmd(value: Int) = write(c.commandRegIdx, value)
  def setCmd = writeCmd(0x1)
  def resetCmd = writeCmd(0x0)
  def readCmd = read(c.commandRegIdx)
  def readStatus = read(c.statusRegIdx)

  def pokeDone {
    poke(c.io.doneTokenIn, 1)
    step(1)
    poke(c.io.doneTokenIn, 0)
  }

  def expect(observed: Int, expected: Int, msg: String = "") = {
    if (observed == expected) printPass(s"$msg: ($observed == $expected)")
    else printFail(s"$msg: ($observed != $expected)")
  }


  // Write to data registers
  val writeVals = List.tabulate(c.v) { i => 0xCAFE + i }
  for (r <- c.dataRegIdx until (c.dataRegIdx + c.v)) {
    write(r, writeVals(r - c.dataRegIdx))
  }

  // Read from data registers
  for (r <- c.dataRegIdx until (c.dataRegIdx + c.v)) {
    val regVal = read(r)
    val expectedVal = writeVals(r - c.dataRegIdx)
    expect(regVal, expectedVal, "DataRegRW")
  }
  // Test output data bus
  for (i <- 0 until c.v) {
    val observed = peek(c.io.out(i)).toInt
    val expected = writeVals(i)
    expect(observed, expected, "DataBus")
  }

  // Write and read from cmd register, test startTokenOut
  setCmd
  var commandReg = readCmd
  var expectedCmd = 0x1
  expect(commandReg, expectedCmd, "CmdRegRW")
  expect(peek(c.io.startTokenOut).toInt, 0x1, "TokenOutigh")
  step(1)
  expect(peek(c.io.startTokenOut).toInt, 0x0, "TokenOutLow(1)")
  step(1)
  expect(peek(c.io.startTokenOut).toInt, 0x0, "TokenOutLow(2)")
  resetCmd
  commandReg = readCmd
  expectedCmd = 0x0
  expect(commandReg, expectedCmd, "CmdRegRW")
  expect(peek(c.io.startTokenOut).toInt, 0x0, "TokenOutLow(3)")

  // Poke done signal, test status register read/write
  write(c.statusRegIdx, 0xF00)
  expect(read(c.statusRegIdx), 0x0, "StatusWrite")
  setCmd  // Done does not go high unless command is set!
  pokeDone
  expect(read(c.statusRegIdx), 0x0, "StatusRead - single cycle delay after done (1)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - done (2)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - done (3)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - done (4)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - done (5)")
  resetCmd
  expect(read(c.statusRegIdx), 0x1, "StatusRead - ready after cmd reset (1)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - ready after cmd reset (2)")
  setCmd
  expect(read(c.statusRegIdx), 0x0, "StatusRead - next cmd running (1)")
  pokeDone
  expect(read(c.statusRegIdx), 0x0, "StatusRead - single cycle delay after done (1)")
  step(1)
  expect(read(c.statusRegIdx), 0x1, "StatusRead - done (2)")
}

object TopUnitTest {

  val w = 32
  val v = 16

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new TopUnit(w, v))) {
      c => new TopUnitTests(c)
    }
  }
}
