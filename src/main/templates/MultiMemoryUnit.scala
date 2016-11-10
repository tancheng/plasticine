package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

abstract class AbstractMultiMemoryUnitTester (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val inst: MemoryUnitConfig) extends Module {
  val numMemoryUnits = 4
  val numChnRankBits = 4
  val wordSize = w/8

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnects = Vec.fill(numMemoryUnits) { new PlasticineMemoryCmdInterface(w, v) }
    val dramChannel = Vec.fill(numMemoryUnits) { new DRAMCmdInterface(w, v) }
  }
}

class MultiMemoryUnitTesterSim (
  override val w: Int,
  override val d: Int,
  override val v: Int,
  override val numOutstandingBursts: Int,
  override val burstSizeBytes: Int,
  override val inst: MemoryUnitConfig) extends AbstractMultiMemoryUnitTester (w, d, v, numOutstandingBursts, burstSizeBytes, inst) {
    this.name = "MultiMemoryUnitTester"
}

class MultiMemoryUnitTester (
  override val w: Int,
  override val d: Int,
  override val v: Int,
  override val numOutstandingBursts: Int,
  override val burstSizeBytes: Int,
  override val inst: MemoryUnitConfig) extends AbstractMultiMemoryUnitTester (w, d, v, numOutstandingBursts, burstSizeBytes, inst) {

  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, inst))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data
      mu.io.interconnect.rdyIn := io.interconnects(i).rdyIn
      mu.io.interconnect.vldIn := io.interconnects(i).vldIn
      io.interconnects(i).rdyOut := mu.io.interconnect.rdyOut
      io.interconnects(i).vldOut := mu.io.interconnect.vldOut
      mu.io.interconnect.addr := io.interconnects(i).addr
      mu.io.interconnect.wdata := io.interconnects(i).wdata
//      mu.io.interconnect.isWr := io.interconnects(i).isWr
      mu.io.interconnect.dataVldIn := io.interconnects(i).dataVldIn
      mu.io.interconnect.size := io.interconnects(i).size
      io.interconnects(i).rdata := mu.io.interconnect.rdata

      io.dramChannel(i).addr := mu.io.dram.addr
      io.dramChannel(i).wdata := mu.io.dram.wdata
      io.dramChannel(i).tagOut := mu.io.dram.tagOut

      io.dramChannel(i).vldOut := mu.io.dram.vldOut
      io.dramChannel(i).rdyOut := mu.io.dram.rdyOut
      io.dramChannel(i).isWr  := mu.io.dram.isWr

      mu.io.dram.rdata := io.dramChannel(i).rdata
      mu.io.dram.vldIn := io.dramChannel(i).vldIn
      mu.io.dram.rdyIn := io.dramChannel(i).rdyIn
      mu.io.dram.tagIn := io.dramChannel(i).tagIn

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
//TODO: test with rdy signal... or is it really needed? 
    mus(id).io.dram.rdata := sims(id).io.rdata
    mus(id).io.dram.vldIn := sims(id).io.vldOut
    mus(id).io.dram.tagIn := sims(id).io.tagOut
  }
}

class MultiMemoryUnitTests(c: AbstractMultiMemoryUnitTester) extends Tester(c) {
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
  val numCmds = 500
  val numChans = 4
  // Test var
  var numTransCompleted = 0

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

  def poke(port: Vec[UInt], value: Seq[Int]) {
    port.zip(value) foreach { case (in, i) => poke(in, i) }
  }

  def peek(port: Vec[UInt]): List[Int] = {
    port.map { peek(_).toInt }.toList
  }

  case class Cmd(chan: Int, addr: Int, data: List[Int], wr: Int, tag: Int) {
    override def equals(that: Any) = that match {
      case t: Cmd =>
        (chan == t.chan) &
        (addr == t.addr) &
        (tag == t.tag) &
        (wr == t.wr) &
        (if (wr > 0) data == t.data else true)
      case _ => false
    }

    override def toString = s"Cmd($chan, $addr, $tag, $wr, ${if (wr > 0) data else List()})"
  }

  var expectedTag = 0
  val expectedOrder = Queue[Cmd]()
  val observedOrder = Queue[Cmd]()

  def observeFor(chan:Int, x: Int) {
    for (i <- 0 until x) {
      if (peek(c.io.dramChannel(chan).vldOut) > 0) {
        val issuedAddr = peek(c.io.dramChannel(chan).addr).toInt
        val data = peek(c.io.dramChannel(chan).wdata)
        val wr = peek(c.io.dramChannel(chan).isWr).toInt
        val tag = peek(c.io.dramChannel(chan).tagOut).toInt
        observedOrder += Cmd(chan, issuedAddr, data, wr, tag)
      }
      step(1)
    }
  }

  def observeAllForOneCycle() {
    for (chan <- 0 until numChans) {
      if (peek(c.io.dramChannel(chan).vldOut) > 0) {
        numTransCompleted = numTransCompleted + 1
      }
//      step(1)
    }

    step(1)
  }

  def incTag {
    expectedTag = (expectedTag + 1) % c.numOutstandingBursts
  }

  def getNumBursts(size: Int) = (size / burstSizeBytes) + (if (size%burstSizeBytes > 0) 1 else 0)

  def issueCmd(chan: Int, burstAddr: Int, isWr: Int, data: List[Int]) {
    val cmd = Cmd(chan, burstAddr, data, isWr, expectedTag)
    expectedOrder += cmd
    incTag
  }

  def updateExpected(chan: Int, addr: Int, size: Int, isWr: Int, data: List[Int]) {
    val dataInBursts = getDataInBursts(data)
    val baseAddr = addr / burstSizeBytes
    val numBursts = getNumBursts(size)
    for (i <- 0 until numBursts) {
      issueCmd(chan, baseAddr+i, isWr, if (dataInBursts.isEmpty) List() else dataInBursts.dequeue)
    }
  }

  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  def verifyAndDequeue: Boolean = {
    if (expectedOrder.size  != observedOrder.size) {
      printFail(s"Queue size mismatch: expected ${expectedOrder.size}, found ${observedOrder.size}")
      printFail(s"Expected: $expectedOrder")
      printFail(s"Observed: $observedOrder")
      false
    } else {
      val res = expectedOrder.zip(observedOrder).map { case (e, o) =>
        if (e != o) printFail(s"Expected $e, observed $o")
        e == o
      }.reduce{_ & _}
      expectedOrder.clear
      observedOrder.clear
      res
    }
  }

  def check(s: String) = verifyAndDequeue match {
    case true => printPass(s)
    case _ => printFail(s)
  }

  def enqueueCmd(chan: Int, addr: Int, size: Int, isWr: Int, data: List[Int] = List[Int]()) {
    poke(c.io.interconnects(chan).vldIn, 1)
    poke(c.io.interconnects(chan).addr(0), addr)
    poke(c.io.interconnects(chan).size, size)
//    poke(c.io.interconnects(chan).isWr, isWr)

    // If it is a write, enqueue first burst
    val dataInBursts = getDataInBursts(data)
    if (isWr > 0) {
      poke(c.io.interconnects(chan).wdata, dataInBursts.dequeue)
      poke(c.io.interconnects(chan).dataVldIn, 1)
    }
//    observeFor(chan, 1)
    observeAllForOneCycle()
    poke(c.io.interconnects(chan).vldIn, 0)
    if (isWr > 0) {
      while (!dataInBursts.isEmpty) {
        poke(c.io.interconnects(chan).wdata, dataInBursts.dequeue)
//        observeFor(chan, 1)
          observeAllForOneCycle()
      }
    }

    poke(c.io.interconnects(chan).dataVldIn, 0)
    updateExpected(chan, addr, size, isWr, data)
  }

  def enqueueBurstRead(chan: Int, addr: Int, size: Int) {
    enqueueCmd(chan, addr, size, 0)
  }

  def enqueueBurstWrite(chan: Int, addr: Int, size: Int, data: List[Int]) {
    enqueueCmd(chan, addr, size, 1, data)
  }

  println("start testing")
  for (x <- 0 to numCmds) {
    print("numCmds = "); print(numCmds); print("; numTranscompleted = "); println(numTransCompleted)
    enqueueBurstWrite(chan0, waddr1 + x, burstSizeBytes, wdata)
    print("enqueue a burst write at ")
    println(waddr1 + x)
  }


  print("numCmds = "); print(numCmds); print("; numTranscompleted = "); println(numTransCompleted)
  while (numTransCompleted != numCmds) {
    print("num of transaction completed = ");
    print(numTransCompleted);
    print(", num of transaction issued = ");
    println(numCmds)
    step(1)
  }
}

object MultiMemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16
  val burstSizeBytes = 64
  val scatterGather = 0
  val isWr = 1
  val config = MemoryUnitConfig(scatterGather, isWr)
  def main(args: Array[String]): Unit = {
    val testMode = args.contains("--test")
    if (testMode) {
      println("In test mode")
      chiselMainTest(args, () => Module(new MultiMemoryUnitTesterSim(w, d, v, numOutstandingBursts, burstSizeBytes, config)).asInstanceOf[AbstractMultiMemoryUnitTester]) {
        c => { new MultiMemoryUnitTests(c) }
      }
    } else {
      chiselMainTest(args, () => Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts, burstSizeBytes, config)).asInstanceOf[AbstractMultiMemoryUnitTester]) {
        c => { new MultiMemoryUnitTests(c) }
      }
    }
  }
}
