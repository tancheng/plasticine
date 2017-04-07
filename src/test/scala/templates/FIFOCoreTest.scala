// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.collection.mutable.Queue

class FIFOCoreUnitTester(c: FIFOCore)(implicit args: Array[String]) extends ArgsTester(c) {

  val magicData = 12

  val expectedQ = Queue[Int]()  // Expected queue of size c.d

  var chainWrite = 0
  var chainRead = 1

  def writeParQ = chainWrite == 0
  def readParQ = chainRead == 0
  val maxQSize = c.d

  def qEmpty = if (expectedQ.isEmpty) 1 else 0
  def qFull = if (writeParQ) {
    if ((expectedQ.size + c.v) > maxQSize) 1 else 0
  } else {
    if (expectedQ.size == maxQSize) 1 else 0
  }

  def headDUT = peek(c.io.deq)
  def headQ = expectedQ.head

  def enqueueBoth(elem: Int) {
    if (writeParQ) {
      c.io.enq foreach { i => poke(i, elem) }
      (0 until c.v) foreach { i => expectedQ += elem }
    } else {
      poke(c.io.enq(0), elem)
      expectedQ += elem
    }
    poke(c.io.enqVld, 1)
    step(1)
    poke(c.io.enqVld, 0)
  }

  def dequeueBoth {
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    if (readParQ) {
      val old = List.fill(c.v) { expectedQ.dequeue }
    } else {
      val old = expectedQ.dequeue
    }
    checkQStatus
  }

  def checkQStatus {
    expect(c.io.empty, qEmpty)
    expect(c.io.full, qFull)
    if (qEmpty == 0) {
      if (readParQ) {
        val expected = List.tabulate(c.v) { expectedQ(_) }
        c.io.deq.zip(expected) foreach { case (o, e) => expect(o, e) }
      } else {
        val expected = expectedQ.head
        expect(c.io.deq(0), expected)
      }
    }
  }

  poke(c.io.config.chainWrite, chainWrite > 0)
  poke(c.io.config.chainRead, chainRead > 0)

  // Smoke test with one element
  checkQStatus
  enqueueBoth(magicData)
  checkQStatus
  dequeueBoth
  checkQStatus

  // Fill up the FIFO
  var i = 0
  while (qFull != 1) {
    enqueueBoth(i)
    checkQStatus
    i += 1
  }

  println(s"Queue: $expectedQ")

  // Pop a few, then fill up again - this tests counter wrap
  val numToPop = 4
  for (i <- 0 until numToPop) {
    dequeueBoth
    checkQStatus
  }

  val magicOffset = 100
  for (i <- 0 until numToPop/c.v) {
    enqueueBoth(i+magicOffset)
    checkQStatus
  }

  // Pop everything
  while (qEmpty != 1) {
    dequeueBoth
    checkQStatus
  }

  checkQStatus
}

object FIFOCoreTest extends CommonMain {
  type DUTType = FIFOCore
  def dut = () => {
    val w = args(0).toInt
    val d = args(1).toInt
    val v = args(2).toInt
    new FIFOCore(w, d, v)
  }
  def tester = { c: DUTType => new FIFOCoreUnitTester(c) }
}

