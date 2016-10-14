package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

class FIFO(val w: Int, val d: Int) extends BlackBox {
  val addrWidth = log2Up(d)
  val io = new Bundle {
    val enq = Bits(INPUT, width = w)
    val enqVld = Bool(INPUT)
    val deq = Bits(OUTPUT, width = w)
    val deqVld = Bool(INPUT)
    val full = Bool(OUTPUT)
    val empty = Bool(OUTPUT)
  }

  // Create size register
  val sizeUDC = Module(new UpDownCtr(log2Up(d+1)))
  val size = sizeUDC.io.out
  val empty = size === UInt(0)
  val full = size === UInt(d)
  sizeUDC.io.initval := UInt(0)
  sizeUDC.io.init := UInt(0)

  val writeEn = io.enqVld & ~full
  val readEn = io.deqVld & ~empty
  sizeUDC.io.inc := writeEn
  sizeUDC.io.dec := readEn

  // Create wptr (tail) and rptr (head)
  val wptr = Module(new Counter(log2Up(d+1)))
  wptr.io.data.max := UInt(d, width=log2Up(d+1))
  wptr.io.data.stride := UInt(1)
  wptr.io.control.reset := Bool(false)
  wptr.io.control.saturate := Bool(false)
  wptr.io.control.enable := writeEn
  val tail = wptr.io.data.out

  val rptr = Module(new Counter(log2Up(d+1)))
  rptr.io.data.max := UInt(d, width=log2Up(d+1))
  rptr.io.data.stride := UInt(1)
  rptr.io.control.reset := Bool(false)
  rptr.io.control.saturate := Bool(false)
  rptr.io.control.enable := readEn
  val head = rptr.io.data.out

  // Backing SRAM
  val mem = Module(new SRAM(w, d))
  mem.io.wdata := io.enq
  io.deq := mem.io.rdata
  mem.io.wen := io.enqVld
  mem.io.waddr := tail
  mem.io.raddr := head

  io.empty := empty
  io.full := full
}

class FIFOTests(c: FIFO) extends Tester(c) {

  val magicData = 10

  val expectedQ = Queue[Int]()  // Expected queue of size c.d

  def qEmpty = if (expectedQ.isEmpty) 1 else 0
  def qFull = if (expectedQ.size == c.d) 1 else 0

  def headDUT = peek(c.io.deq)
  def headQ = expectedQ.head

  def enqueueBoth(elem: Int) {
    poke(c.io.enq, elem)
    poke(c.io.enqVld, 1)
    step(1)
    expectedQ += elem
    poke(c.io.enqVld, 0)
  }

  def dequeueBoth {
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    expect(c.io.deq, expectedQ.dequeue)
  }

  def checkQStatus {
    expect(c.io.empty, qEmpty)
    expect(c.io.full, qFull)
//    if (!expectedQ.isEmpty) expect(c.io.deq, expectedQ.head)
  }

  // Smoke test with one element
  checkQStatus
  enqueueBoth(magicData)
  checkQStatus
  dequeueBoth
  checkQStatus

  // Fill up the FIFO
  for (i <- 0 until c.d) {
    enqueueBoth(i)
    checkQStatus
  }

  println(s"Queue: $expectedQ")

  // Pop a few, then fill up again - this tests counter wrap
  val numToPop = 5
  for (i <- 0 until numToPop) {
    dequeueBoth
    checkQStatus
  }

  val magicOffset = 100
  for (i <- 0 until numToPop) {
    enqueueBoth(i+magicOffset)
    checkQStatus
  }

  // Pop everything
  for (i <- 0 until numToPop) {
    dequeueBoth
    checkQStatus
  }
}

object FIFOTest {
  val w = 16
  val d = 8

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new FIFO(w, d))) {
      c => new FIFOTests(c)
    }
  }
}

