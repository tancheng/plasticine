package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class TestCmdInterface(w: Int, v: Int) extends Bundle {
  val wdata = Vec.fill(v) { UInt(INPUT, width=w) }
  val addr = Vec.fill(v) { UInt(INPUT, width=w) }
  val size = UInt(INPUT, width=w)
  val dataVldIn = Bool(INPUT)
  val vldIn = Bool(INPUT)
  val rdata = Vec.fill(v) { UInt(OUTPUT, width=w) }
  val vldOut = Bool(OUTPUT)
  val rdyOut = Bool(OUTPUT)
}

class MultiMemoryUnitTester (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val numCounters: Int,
  val inst: MemoryUnitConfig) extends Module {

  val numMemoryUnits = 4
  val numChnRankBits = 4
  val wordSize = w/8

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
//    val interconnects = Vec.fill(numMemoryUnits) { new PlasticineMemoryCmdInterface(w, v) }
    val interconnects = Vec.fill(numMemoryUnits) { new TestCmdInterface(w,v) }
  }

  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data

      mu.vldIn := io.interconnects(i).vldIn
      mu.addr := io.interconnects(i).addr
      mu.wdata := io.interconnects(i).wdata
      mu.size := io.interconnects(i).size
      mu.dataVldIn := io.interconnects(i).dataVldIn

      io.interconnects(i).vldOut := mu.vldOut
      io.interconnects(i).rdyOut := mu.rdyOut
      io.interconnects(i).rdata := mu.rdata
      mu
    }
  }

  def genDRAMSims = {
    List.tabulate(numMemoryUnits) { j =>
      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))

      DRAMSimulator
    }
  }

  val mus = genMemoryUnits
  val sims = genDRAMSims
  // connection example
  val chnRanksIdBits = Vec(UInt(0), UInt(4), UInt(8), UInt(12)) { UInt(width=4) }
  for (id <- 0 to numMemoryUnits - 1) {
    sims(id).io.addr := Cat(chnRanksIdBits(id), mus(id).io.dram.addr)
    sims(id).io.wdata := mus(id).io.dram.wdata
    sims(id).io.tagIn := mus(id).io.dram.tagOut
    sims(id).io.vldIn := mus(id).io.dram.vldOut
    sims(id).io.isWr := mus(id).io.dram.isWr

    mus(id).io.dram.rdata := sims(id).io.rdata
    mus(id).io.dram.vldIn := sims(id).io.vldOut
    mus(id).io.dram.tagIn := sims(id).io.tagOut
  }
}

class MultiMemoryUnitTests(c: MultiMemoryUnitTester) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  // Test constants
  val addr1 = 0x1000
  val waddr1 = 0x2000
	val addr2 = 0x1000
	val waddr2 = 0x2000
  val chan0 = 0
  val chan1 = 1
	val chan2 = 2
	val chan3 = 3
  val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
  val numCmds = c.numOutstandingBursts
  val numChans = 4
  var numTransCompleted = 0

  def peekOnChan(chan: Int) {
    val dramTagIn = peek(c.mus(chan).io.dram.tagIn).toInt
    val dramVldIn = peek(c.mus(chan).io.dram.vldIn).toInt
    val dramTagOut = peek(c.mus(chan).io.dram.tagOut).toInt
    val dramVldOut = peek(c.mus(chan).io.dram.vldOut).toInt

    if (dramVldIn > 0) {
      numTransCompleted = numTransCompleted + 1
    }
  }

//  val numCommands = numCmds

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

  val numCommands = 500
  val k = chan0
  val writeMode = peek(c.mus(k).config.isWr).toInt

  if (writeMode > 0) {
    println("start testing writes")
    val waddrs = List.tabulate(numCommands) { i => addr1 + i * 0x40 }
    val wsizes = List.tabulate(numCommands) { i => burstSizeBytes }
    waddrs.zip(wsizes) foreach {
      case (waddr, wsize) => {
        poke(c.io.interconnects(k).vldIn, 1)
        poke(c.io.interconnects(k).addr(0), waddr)
        poke(c.io.interconnects(k).size, wsize)
//        poke(c.io.interconnects(k).wdata, Vec(wdata))
        c.io.interconnects(k).wdata.zip(wdata) foreach { case(in, i ) => poke(in,i) }
        poke(c.mus(k).dataVldIn, 1)
        step(1)
        peekOnChan(chan0)
      }
    }

    poke(c.io.interconnects(k).vldIn, 0)
    poke(c.io.interconnects(k).addr(0), 0x0000)
    poke(c.io.interconnects(k).dataVldIn, 0)

    while (numTransCompleted != numCommands) {
      println("numTransCompleted = " + numTransCompleted)
      step(1)
      peekOnChan(chan0)
    }

  } else {
    println("start testing writes")
    val raddrs = List.tabulate(numCommands) { i => addr1 + i * 0x40 }
    val rsizes = List.tabulate(numCommands) { i => burstSizeBytes }
    raddrs.zip(rsizes) foreach {
      case (raddr, rsize) => {
        poke(c.io.interconnects(k).vldIn, 1)
        poke(c.io.interconnects(k).addr(0), raddr)
        poke(c.io.interconnects(k).size, rsize)
        step(1)
        peekOnChan(chan0)
      }
    }

    poke(c.io.interconnects(k).vldIn, 0)
    poke(c.io.interconnects(k).addr(0), 0x0000)
    poke(c.io.interconnects(k).dataVldIn, 0)

    while (numTransCompleted != numCommands) {
      println("numTransCompleted = " + numTransCompleted)
      step(1)
      peekOnChan(chan0)
    }
  }
}

object MultiMemoryUnitTest {
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
  def main(args: Array[String]): Unit = {
      chiselMainTest(args, () => Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))) {
        c => { new MultiMemoryUnitTests(c) }
    }
  }
}
