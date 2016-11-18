package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class Gather (
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
    val addr = Vec.fill (v) { UInt(INPUT, width=w) }
    val data = Vec.fill(v) { UInt(OUTPUT, width=w) }
    val numGatherVectors = UInt(INPUT, width=w)

    // To/From  MemoryUnit
    val mc = new Bundle {
      val ready = Bool(INPUT)
      val addr = Vec.fill(v) { UInt(OUTPUT, width=w) }
      val addrValid = Bool(OUTPUT)
      val data = Vec.fill(v) { UInt(INPUT, width=w) }
      val dataValid = Bool(INPUT)
    }
  }


  // Command generators
  val allReceived = Bool()
  val gatherIssuedCounter = Module(new Counter(w))
  val commandValid = Reg(Bool(), ~gatherIssuedCounter.io.control.done & io.mc.ready)
  gatherIssuedCounter.io.control.saturate := Bool(true)
  gatherIssuedCounter.io.control.reset := ~io.enable
  gatherIssuedCounter.io.control.enable := io.enable
  gatherIssuedCounter.io.data.max := io.numGatherVectors
  gatherIssuedCounter.io.data.stride := UInt(1)

  // Generate commands and valids
  io.mc.addr := io.addr
  io.mc.addrValid := commandValid

  val gatherReceivedCounter = Module(new Counter(w))
  gatherReceivedCounter.io.control.saturate := Bool(true)
  gatherReceivedCounter.io.control.reset := ~io.enable
  gatherReceivedCounter.io.control.enable := io.enable & io.mc.dataValid
  gatherReceivedCounter.io.data.max := io.numGatherVectors
  gatherReceivedCounter.io.data.stride := UInt(1)
  allReceived := gatherReceivedCounter.io.control.done
  io.done := allReceived
}

class GatherTests(c: Gather) extends Tester(c)

object GatherTest {
  val w = 32
  val v = 16
  def main(args: Array[String]): Unit = {
      chiselMainTest(args, () => Module(new Gather(w, v))) {
        c => { new GatherTests(c) }
    }
  }
}
