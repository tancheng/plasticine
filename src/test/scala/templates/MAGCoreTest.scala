package plasticine.templates
import plasticine.templates.CommonMain

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.Queue
import scala.collection.mutable.HashMap
import fringe._

class MAGCoreTester(c: MAGCore)(implicit args: Array[String]) extends ArgsTester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

  def getBurstAddr(addr: Int) = addr - (addr % burstSizeBytes)

  def poke(port: Vec[UInt], value: Seq[Int]) {
    port.zip(value) foreach { case (in, i) => poke(in, i) }
  }

  def peek(port: Vec[UInt]): List[Int] = {
    port.map { peek(_).toInt }.toList
  }

  case class Cmd(addr: Int, data: List[Int], wr: Int, tag: Int) {
    override def equals(that: Any) = that match {
      case t: Cmd =>
        (addr == t.addr) &
        (tag == t.tag) &
        (wr == t.wr) &
        (if (wr > 0) data == t.data else true)
      case _ => false
    }

    override def toString = f"Cmd(addr: 0x$addr%x, tag: 0x$tag%x, write: $wr, ${if (wr > 0) data else List()})"
  }

  var expectedTag = 0
  val expectedOrder = Queue[Cmd]()
  val observedOrder = Queue[Cmd]()

  def observeFor(x: Int) {
    for (i <- 0 until x) {
      if (peek(c.io.dram.cmd.valid) > 0) {
        val issuedAddr = peek(c.io.dram.cmd.bits.addr).toInt
        val data = peek(c.io.dram.cmd.bits.wdata)
        val wr = peek(c.io.dram.cmd.bits.isWr).toInt
        val tag = peek(c.io.dram.cmd.bits.tag).toInt
        observedOrder += Cmd(issuedAddr, data, wr, tag)
      }
      step(1)
    }
  }

  def incTag {
    expectedTag = (expectedTag + 1) % c.numOutstandingBursts
  }

  def getNumBursts(size: Int) = (size / burstSizeBytes) + (if (size%burstSizeBytes > 0) 1 else 0)
//  def writeMode = peek(c.io.config.isWr).toInt
  var writeMode = 0
  def sgMode = peek(c.io.config.scatterGather).toInt
  def resetAll {
    reset(10)
    expectedTag = 0
  }

  def issueCmd(burstAddr: Int, isWr: Int, data: List[Int], streamID: Int) {
    val stream = if (isWr > 0) c.loadStreamInfo.size + streamID else streamID
    val tag = if (sgMode > 0) burstAddr / burstSizeBytes else expectedTag
    val streamTag = (stream & ((1 << c.tagWidth)-1)) << (c.w - c.tagWidth) | tag
    val cmd = Cmd(burstAddr, data, isWr, streamTag)
    expectedOrder += cmd
    incTag
  }

  def updateExpected(addr: Int, size: Int, data: List[Int], streamID: Int) {
    val dataInBursts = getDataInBursts(data)
    val baseAddr = addr - (addr % burstSizeBytes)
    val numBursts = getNumBursts(size)
    val isWr = writeMode
    for (i <- 0 until numBursts) {
      issueCmd(baseAddr+i*burstSizeBytes, isWr, if (dataInBursts.isEmpty) List() else dataInBursts.dequeue, streamID)
    }
  }

  // Scatter-gather issue
  def updateExpected(addr: List[Int], data: List[Int], streamID: Int) {
    val dataInBursts = getDataInBursts(data)
    val baseAddresses = addr.map { a => a - (a % burstSizeBytes) }
    val uniqueBaseAddresses = baseAddresses.distinct
    val isWr = writeMode
    uniqueBaseAddresses.foreach { a => issueCmd(a, isWr, if (isWr == 0) List() else data, streamID) }
  }

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

  def enqueueCmd(addr: Int, size: Int, data: List[Int] = List[Int]())(streamID: Int){
    val isWr = writeMode
    // If it is a write, enqueue first burst
    val dataInBursts = getDataInBursts(data)
    if (isWr > 0) {
      poke(c.io.app.stores(streamID).cmd.valid, 1)
      poke(c.io.app.stores(streamID).cmd.bits.addr, addr)
      poke(c.io.app.stores(streamID).cmd.bits.size, size)
      poke(c.io.app.stores(streamID).cmd.bits.isWr, isWr)

      poke(c.io.app.stores(streamID).wdata.bits, dataInBursts.dequeue)
      poke(c.io.app.stores(streamID).wdata.valid, 1)
    } else {
      poke(c.io.app.loads(streamID).cmd.valid, 1)
      poke(c.io.app.loads(streamID).cmd.bits.addr, addr)
      poke(c.io.app.loads(streamID).cmd.bits.size, size)
      poke(c.io.app.loads(streamID).cmd.bits.isWr, isWr)
    }
    observeFor(1)
    if (isWr > 0) {
      poke(c.io.app.stores(streamID).cmd.valid, 0)
    } else {
      poke(c.io.app.loads(streamID).cmd.valid, 0)
    }

    if (isWr > 0) {
      while (!dataInBursts.isEmpty) {
        poke(c.io.app.stores(streamID).wdata.bits, dataInBursts.dequeue)
        observeFor(1)
      }
    }
    poke(c.io.app.stores(streamID).wdata.valid, 0)
    observeFor(1)  // Extra level of pipelining requires another cycle
    updateExpected(addr, size, data, streamID)
  }

  def enqueueCmd(addr: List[Int], data: List[Int])(streamID: Int) {
    val isWr = writeMode

    val dataInBursts = getDataInBursts(data)
    addr.zipAll(dataInBursts.toList, 0, List[Int](0)).foreach{ case (a, d) =>
      if (isWr > 0) {
        poke(c.io.app.stores(streamID).cmd.valid, 1)
        poke(c.io.app.stores(streamID).cmd.bits.addr, a)
        poke(c.io.app.stores(streamID).cmd.bits.size, burstSizeBytes)
        poke(c.io.app.stores(streamID).cmd.bits.isWr, isWr)

        poke(c.io.app.stores(streamID).wdata.bits, d)
        poke(c.io.app.stores(streamID).wdata.valid, 1)
      } else {
        poke(c.io.app.loads(streamID).cmd.valid, 1)
        poke(c.io.app.loads(streamID).cmd.bits.addr, a)
        poke(c.io.app.loads(streamID).cmd.bits.size, burstSizeBytes)
        poke(c.io.app.loads(streamID).cmd.bits.isWr, isWr)
      }
      observeFor(1)
    }
    if (isWr > 0) {
      poke(c.io.app.stores(streamID).cmd.valid, 0)
    } else {
      poke(c.io.app.loads(streamID).cmd.valid, 0)
    }
    poke(c.io.app.stores(streamID).wdata.valid, 0)
    observeFor(1)
    updateExpected(addr, data, streamID)
  }


  def enqueueBurstRead(addr: Int, size: Int)(streamID: Int) {
    enqueueCmd(addr, size)(streamID)
  }

  def enqueueBurstWrite(addr: Int, size: Int, data: List[Int])(streamID: Int) {
    enqueueCmd(addr, size, data)(streamID)
  }

  def enqueueGather(addr: List[Int])(streamID: Int) {
    enqueueCmd(addr, List[Int]())(streamID)
  }

  def enqueueScatter(addr: List[Int], data: List[Int])(streamID: Int) {
    enqueueCmd(addr, data)(streamID)
  }

  def pokeDramResponse(tag: Int, data: List[Int]) {
    poke(c.io.dram.resp.bits.tag, tag)
    poke(c.io.dram.resp.valid, 1)
    if (data.size > 0) poke(c.io.dram.resp.bits.rdata, data)
    observeFor(1)
    poke(c.io.dram.resp.valid, 0)
  }


  def testBurstRead {
    resetAll
    poke(c.io.config.scatterGather, 0)
    writeMode = 0
//    poke(c.io.config.isWr, 0)

    // Test constants
    val addr = 0x1000
    val waddr = 0x2000

    // 2a. Smoke test, read: Single burst with a single burst size
    enqueueBurstRead(addr, burstSizeBytes)(0)
    observeFor(5)
    check("Single burst read")

    val numBursts = 10
    // 3a. Bigger smoke test, read: Single burst address with multi-burst size
    enqueueBurstRead(waddr, numBursts * burstSizeBytes)(0)
    observeFor(numBursts+8)
    check("Single Multi-burst read")

    // 4. Multiple commands - read
    val numCommands = c.numOutstandingBursts
    val maxSizeBursts = 9 // arbitrary
    val raddrs = List.tabulate(numCommands) { i => addr + i * 0x1000 }
    val rsizes = List.tabulate(numCommands) { i => burstSizeBytes * (math.abs(rnd.nextInt) % maxSizeBursts + 1) }
    raddrs.zip(rsizes) foreach { case (raddr, rsize) => enqueueBurstRead(raddr, rsize)(0) }
    val cyclesToWait = rsizes.map { size =>
      (size / burstSizeBytes) + (if ((size % burstSizeBytes) > 0) 1 else 0)
    }.sum
    observeFor(cyclesToWait*5)
    check("Multiple multi-burst read")
  }

  def testBurstWrite {
    resetAll
    poke(c.io.config.scatterGather, 0)
    writeMode = 1
//    poke(c.io.config.isWr, 1)

    // Test constants
    val addr = 0x1000
    val waddr = 0x2000

    // 2b. Smoke test, write: Single burst with a single burst size
    val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
    enqueueBurstWrite(waddr, burstSizeBytes, wdata)(0)
    observeFor(1)
    check("Single burst write")

    val numBursts = 10
    // 3b. Bigger smoke test, write: Single burst address with multi-burst size
    val bigWdata = List.tabulate(numBursts * wordsPerBurst) { i => 0xf00d + i }
    enqueueBurstWrite(waddr, numBursts * burstSizeBytes, bigWdata)(0)
    observeFor(numBursts+5)
    check("Single Multi-burst write")

    // 5. Multiple commands - write
    val numCommands = c.numOutstandingBursts
    val maxSizeBursts = 9 // arbitrary
    val waddrs = List.tabulate(numCommands) { i => addr + i * 0x1000 }
    val wsizes = List.tabulate(numCommands) { i => burstSizeBytes * (math.abs(rnd.nextInt) % maxSizeBursts + 1) }
    waddrs.zip(wsizes) foreach { case (waddr, wsize) =>
      val wdata = List.tabulate(wsize / (c.w/8)) { i => i }
      enqueueBurstWrite(waddr, wsize, wdata)(0)
    }
    val wCyclesToWait = wsizes.map { size =>
      (size / burstSizeBytes) + (if ((size % burstSizeBytes) > 0) 1 else 0)
    }.sum
    observeFor(wCyclesToWait)
    check("Multiple multi-burst write")
  }

  def testGather {
    resetAll
    poke(c.io.config.scatterGather, 1)
    writeMode = 0

    def getTagFor(addr: Int) = {
      observedOrder.filter(_.addr == addr).head.tag
    }

    // 1. Single gather issue where all addresses are the same
    val g = List.tabulate(c.v) { i => 0x1000 }
    val expectedData = 0x2000
    enqueueGather(g)(0)
    observeFor(c.v * 2)
    pokeDramResponse(getTagFor(0x1000), List.fill(c.v) { 0x2000 })
    check("Single gather with same address")

    // 2. Single gather, multiple addresses
    val addrs = List.tabulate(c.v) { i =>
      if (i == 0) 0x1000 else {
        (0x1000 + (rnd.nextInt) % 0x1000) & ((Integer.MAX_VALUE >> log2Up(c.wordSizeBytes)) << log2Up(c.wordSizeBytes))
      }
    }
    val burstData = HashMap[Int, List[Int]]()
    addrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      if (!burstData.contains(baseAddr)) burstData(baseAddr) = List.fill(c.burstSizeWords) { math.abs(rnd.nextInt(0x1000)) }
    }
    enqueueGather(addrs)(0)
    observeFor(c.v * 2) // Checks for issue

    val shuffledAddrs = rnd.shuffle((0 until addrs.size).toList).map { addrs(_) }

    shuffledAddrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      // Start replying randomly with different addresses
      pokeDramResponse(getTagFor(baseAddr), burstData(baseAddr))
      observeFor(1)
    }
    step(10)
    check("Single gather, multiple addresses")
    val expectedGather = addrs.map { a =>
      val baseAddr = a - (a % burstSizeBytes)
      val burstOffset = a % burstSizeBytes
      val wordOffset = burstOffset / c.wordSizeBytes
      val data = burstData(baseAddr)
      data(wordOffset)
    }
    println(s"Expected gather: $expectedGather")
    println(s"Gather addresses")
    addrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      val burstOffset = a % burstSizeBytes
      val wordOffset = burstOffset / c.wordSizeBytes
      val data = burstData(baseAddr)
      println(f"addr:0x$a%x, base: 0x$baseAddr%x, wordOffset: 0x$wordOffset%x, data: $data")
    }
  }

  // Setup: Set dram ready to always be high
  poke(c.io.dram.cmd.ready, 1)

  //testBurstRead
  //testBurstWrite
  testGather
}

object MAGCoreTest extends CommonMain {
  type DUTType = MAGCore

  def dut = () => {
    val w = args(0).toInt
    val d = args(1).toInt
    val v = args(2).toInt
    val loadStreamInfo = List[StreamParInfo](StreamParInfo(w, 16))
    val storeStreamInfo = List[StreamParInfo](StreamParInfo(w, 16))
    val numOutstandingBursts = 16
    val burstSizeBytes = 64
    new MAGCore(w, d, v, loadStreamInfo, storeStreamInfo, numOutstandingBursts, burstSizeBytes)
  }

  def tester = { c: DUTType => new MAGCoreTester(c) }
}
