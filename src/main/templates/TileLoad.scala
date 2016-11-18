package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class TileLoad (
  val w: Int,
  val v: Int
  ) extends Module {

  val numMemoryUnits = 4
  val numChnRankBits = 4
  val wordSize = w/8

  val io = new Bundle {
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

  val maxRows = io.rows

  // Command generators
  val allReceived = Bool()
  val rowIssuedCounter = Module(new Counter(w))
  val commandValid = Reg(Bool(), ~rowIssuedCounter.io.control.done & io.mc.ready)
  rowIssuedCounter.io.control.saturate := Bool(true)
  rowIssuedCounter.io.control.reset := ~io.enable
  rowIssuedCounter.io.control.enable := io.enable
  rowIssuedCounter.io.data.max := maxRows
  rowIssuedCounter.io.data.stride := UInt(1)

  // Generate commands and valids
  io.mc.addr := io.addr + rowIssuedCounter.io.data.out * io.colsize
  io.mc.size := io.size
  io.mc.addrValid := commandValid

  val rowReceivedCounter = Module(new Counter(w))
  rowReceivedCounter.io.control.saturate := Bool(true)
  rowReceivedCounter.io.control.reset := ~io.enable
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
  def main(args: Array[String]): Unit = {
      chiselMainTest(args, () => Module(new TileLoad(w, v))) {
        c => { new TileLoadTests(c) }
    }
  }
}
