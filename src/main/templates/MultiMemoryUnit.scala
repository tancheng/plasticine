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
  val masksIn = Vec.fill(v) { Bool(INPUT) }
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
    val interconnects = Vec.fill(numMemoryUnits) { new TestCmdInterface(w,v) }
  }

  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data
      mu.io.masksIn := io.interconnects(i).masksIn

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
    sims(id).io.masksIn := mus(id).io.dram.masksOut

    mus(id).io.dram.rdata := sims(id).io.rdata
    mus(id).io.dram.vldIn := sims(id).io.vldOut
    mus(id).io.dram.tagIn := sims(id).io.tagOut
  }
}

class MultiMemoryUnitTests(c: MultiMemoryUnitTester) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  val numChans = 4
  val numBurstsPerCmd = 1
  val writeMode = peek(c.mus(0).config.isWr).toInt

  val waddr = 0x1000
  val wsize = burstSizeBytes * numBurstsPerCmd
  val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
  val masks = List.tabulate(wordsPerBurst * numBurstsPerCmd) { i => i % 2 }
  val k = 0

  var counts = 0
  println("start testing read_modify_write")
  println("words per burst = " + wordsPerBurst)

	def peekSet(k: Int) {
 		peek(c.mus(k).io.dram.vldOut).toInt
 		peek(c.mus(k).io.dram.tagOut)
 		peek(c.mus(k).io.dram.addr)
		val vldIn = peek(c.mus(k).io.dram.vldIn).toInt
		val tagIn = peek(c.mus(k).io.dram.tagIn).toInt
    if (vldIn > 0) {
      println("one read-modify-write-completed at channel "+ k + ", tag = " + tagIn)
      counts = counts + 1
    }
	}

	def testOneStepOnAllChan(iter: Int) {
		for (k <- 0 to 3) {
 			poke(c.io.interconnects(k).vldIn, 1)
 			poke(c.io.interconnects(k).addr(0), waddr + 0x80 * iter)
 			poke(c.io.interconnects(k).size, wsize)
 			c.io.interconnects(k).wdata.zip(wdata) foreach { case(in, i ) => poke(in,i) }
 			c.io.interconnects(k).masksIn.zip(masks) foreach { case(in, i ) => poke(in,i) }
 			poke(c.io.interconnects(k).dataVldIn, 1)

			peekSet(k)

 			step(1)
 			poke(c.io.interconnects(k).vldIn, 0)
 			poke(c.io.interconnects(k).addr(0), 0x0000)
 			poke(c.io.interconnects(k).size, 0)
 			c.io.interconnects(k).wdata.zip(wdata) foreach { case(in, i ) => poke(in,0) }
 			c.io.interconnects(k).masksIn.zip(masks) foreach { case(in, i ) => poke(in,0) }
 			poke(c.io.interconnects(k).dataVldIn, 0)
			peekSet(k)
		}
	}

	for (i <- 0 to 50) {
		testOneStepOnAllChan(i)
	}

  for (i <- 0 to 800)
  {
    step(1)
		for (k <- 0 to 3) {
			peekSet(k)
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
  val isWr = 1
  val scatterGather = 0
  val config = MemoryUnitConfig(scatterGather, isWr, CounterChainConfig.zeroes(numCounters), CUControlBoxConfig.zeroes(numCounters, numCounters, numCounters))
  def main(args: Array[String]): Unit = {
      chiselMainTest(args, () => Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))) {
        c => { new MultiMemoryUnitTests(c) }
    }
  }
}
