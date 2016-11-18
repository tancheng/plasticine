package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class TileLoader(
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val numCounters: Int,
  val inst: MemoryUnitConfig) extends Module {

  val io = new Bundle {

    val rows = UInt(INPUT, width=w)
    val size = UInt(INPUT, width=w)
    val colsize = UInt(INPUT, width=w)
    val addr = UInt(INPUT, width=w)
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
  }

  // Instantiate memory controller
  val mc = Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts,
      burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst))

//  val mc = Module(new MemoryUnit(w, d, v, numOutstandingBursts,
//      burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst))


  // Instantiate tile loader, connect it to MC
  val t1 = Module(new TileLoad(w, v))
  mc.io.interconnects(0).addr(0) := t1.io.mc.addr
  mc.io.interconnects(0).size := t1.io.mc.size
  mc.io.interconnects(0).vldIn := t1.io.mc.addrValid
  t1.io.mc.data := mc.io.interconnects(0).rdata
  t1.io.mc.dataValid := mc.io.interconnects(0).receivedCtrWrap
  t1.io.mc.ready := mc.io.interconnects(0).rdyOut

//  mc.addr(0) := t1.io.mc.addr
//  mc.size := t1.io.mc.size
//  mc.vldIn := t1.io.mc.addrValid
//  t1.io.mc.data := mc.rdata
//  t1.io.mc.dataValid := mc.vldOut
//  t1.io.mc.ready := mc.rdyOut

  // Hardcode some constants to load

  t1.io.rows := io.rows
  t1.io.addr := io.addr
  t1.io.size := io.size
  t1.io.colsize := io.colsize

  t1.io.enable := io.enable
  io.done := t1.io.done
}

class TileLoaderTests(c: TileLoader,
  val rows: Int,
  val size: Int,
  val addr: Int,
  val colsize: Int
  ) extends Tester(c) {

  def peekDram() {
    val issuedVldOut = peek(c.mc.mus(0).io.dram.vldOut)
    val issuedAddr = peek(c.mc.mus(0).io.dram.addr)
    val issuedTag = peek(c.mc.mus(0).io.dram.tagOut)
    val addrFifo = peek(c.mc.mus(0).addrFifo.size)
    val received = peek(c.t1.rowReceivedCounter.io.data.out)
    val commandValid = peek(c.t1.commandValid)
    val dataValid = peek(c.t1.io.mc.dataValid)
    val tldone = peek(c.t1.io.done)
    val done = peek(c.io.done)
    val rowIssuedDone = peek(c.t1.rowIssuedCounter.io.control.done)
    val rowReceivedDone = peek(c.t1.rowReceivedCounter.io.control.done)
    val mcready = peek(c.t1.io.mc.ready)
//    val issuedVldOut = peek(c.mc.io.dram.vldOut)
//    val issuedAddr = peek(c.mc.io.dram.addr)
//    val issuedTag = peek(c.mc.io.dram.tagOut)

    println(s"[peekDram] addr = $issuedAddr tag = $issuedTag, addrFifo size = $addrFifo, received = $received, tldone = $tldone, done = $done, dataValid = $dataValid, commandValid = $commandValid, rowIssuedDone: $rowIssuedDone, rowReceivedDone: $rowReceivedDone, mcready: $mcready")
  }
  poke(c.io.rows, rows)
  poke(c.io.size, size)
  poke(c.io.addr, addr)
  poke(c.io.colsize, colsize)


  poke(c.io.enable, 1)
  var done = peek(c.io.done)
  var cycles = 0


  while ((done != 1) & (cycles < 100000)) {
    step(1)
    peekDram
    done = peek(c.io.done)
    cycles += 1
  }
  poke(c.io.enable, 0)

  val totalBytesTransferred = rows * size
  println(s"Done, design ran for $cycles cycles")
  println(s"Memory bandwidth: ${(totalBytesTransferred * 1.0 / cycles) } GB/s")
}

object TileLoaderTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 2) {
      println(s"Usage: <TileLoaderTest> <#rows> <row_size>")
      sys.exit(-1)
    }
    // Test params
    val rows = appArgs(0).toInt
    val size = appArgs(1).toInt
    val addr = 0x1000
    val colsize = 2048

    val numInputs = 2
    val w = 32
    val v = 16
    val d = 512
    val numOutstandingBursts = 1024
    val burstSizeBytes = 64
    val startDelayWidth = 4
    val endDelayWidth = 4
    val numCounters = 6
    val isWr = 0
    val scatterGather = 0
    val config = MemoryUnitConfig(scatterGather, isWr, CounterChainConfig.zeroes(numCounters), CUControlBoxConfig.zeroes(numCounters, numCounters, numCounters))

    chiselMainTest(chiselArgs, () => Module(new TileLoader(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config) )) {
      c => new TileLoaderTests(c, rows, size, addr, colsize)
    }
  }
}
