package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class TileLoad (
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
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)

    // To/From design
    val rows = UInt(INPUT, width=w)
    val addr = UInt(INPUT, width=w)
    val size = UInt(INPUT, width=w)
    val colsize = UInt(INPUT, width=w)
    val data = Vec.fill(v) { UInt(OUTPUT, width=w) }

    // To/From  MemoryUnit
    val mc = new Bundle {
      val ready = Bool(INPUT)
      val addr = UInt(OUTPUT, width=w)
      val size = UInt(OUTPUT, width=w)
      val addrValid = Bool(OUTPUT)
      val data = Vec.fill(v) { UInt(INPUT, width=w) }
      val dataValid = Bool(INPUT)
    }
  }

  val rowReg = Module(new FF(w))
  rowReg.io.control.enable := io.enable
  rowReg.io.data.in := io.rows
  val maxRows = rowReg.io.data.out

  // Command generators
  val commandValid = io.enable & io.mc.ready
  val allReceived = Bool()
  val rowIssuedCounter = Module(new Counter(w))
  rowIssuedCounter.io.control.saturate := Bool(true)
  rowIssuedCounter.io.control.reset := Bool(allReceived)
  rowIssuedCounter.io.control.enable := commandValid
  rowIssuedCounter.io.data.max := maxRows
  rowIssuedCounter.io.data.stride := UInt(1)

  // Generate commands and valids
  io.mc.addr := io.addr + rowIssuedCounter.io.data.out * io.colsize
  io.mc.size := io.size
  io.mc.addrValid := commandValid

  val rowReceivedCounter = Module(new Counter(w))
  rowReceivedCounter.io.control.saturate := Bool(false)
  rowReceivedCounter.io.control.reset := Bool(false)
  rowReceivedCounter.io.control.enable := io.enable & io.mc.dataValid
  rowReceivedCounter.io.data.max := maxRows
  rowReceivedCounter.io.data.stride := UInt(1)
  allReceived := rowReceivedCounter.io.control.done
  io.done := allReceived
}

class TileLoadTests(c: TileLoad) extends Tester(c)

object TileLoadTest {
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
      chiselMainTest(args, () => Module(new TileLoad(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))) {
        c => { new TileLoadTests(c) }
    }
  }
}
