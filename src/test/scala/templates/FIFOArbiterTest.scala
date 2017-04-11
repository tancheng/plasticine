// See LICENSE for license details.

package plasticine.templates
import plasticine.templates.CommonMain

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue
import scala.collection.mutable.HashMap

/**
 * FIFOArbiter test harness
 */
class FIFOArbiterUnitTester(c: FIFOArbiter)(implicit args: Array[String]) extends ArgsTester(c) {
  val magicData = 12

  val simQ = List.fill(c.numStreams) { Queue[Int]() }
  def expectedQ = Queue(simQ.map { _.toList }.flatten:_*)
  def expectedTag = simQ.zipWithIndex.filter { case (q, idx) => !q.isEmpty }.map { _._2 }.head
  var chainWrite = 0
  var chainRead = 1

  def writeParQ = chainWrite == 0
  def readParQ = chainRead == 0
  val maxQSize = c.d

  def qEmpty = if (expectedQ.isEmpty) 1 else 0
  def qFull = List.tabulate(c.numStreams) { i =>
    if (writeParQ) {
      if ((simQ(i).size + c.v) > maxQSize) 1 else 0
      } else {
      if (simQ(i).size == maxQSize) 1 else 0
    }
  }

  def headDUT = peek(c.io.deq)
  def headQ = expectedQ.head

  def enqueueBoth(qid: Int)(elem: Int) {
    if (writeParQ) {
      c.io.enq(qid) foreach { i => poke(i, elem) }
      (0 until c.v) foreach { i => simQ(qid) += elem }
    } else {
      poke(c.io.enq(qid)(0), elem)
      simQ(qid) += elem
    }
    poke(c.io.enqVld(qid), 1)
    step(1)
    poke(c.io.enqVld(qid), 0)
    step(1) // Required because of additional pipelining
  }

  def dequeueBoth {
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    if (readParQ) {
      var numDequeued = 0
      var qid = 0
      while (numDequeued < c.v) {
        if (simQ(qid).isEmpty) qid += 1
        simQ(qid).dequeue
        numDequeued += 1
      }
//      val old = List.fill(c.v) { expectedQ.dequeue }
    } else {
      var numDequeued = 0
      var qid = 0
      while (numDequeued < 1) {
        if (simQ(qid).isEmpty) qid += 1
        simQ(qid).dequeue
        numDequeued += 1
      }
//      val old = expectedQ.dequeue
    }
    checkQStatus
  }

  def checkQStatus {
    expect(c.io.empty, qEmpty)
    c.io.full.zip(qFull) foreach { case (o, e) => expect(o, e) }
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
  enqueueBoth(c.numStreams-1)(magicData)
  checkQStatus
  dequeueBoth
  checkQStatus

  // Fill up the FIFO
//  var i = 0
//  while (qFull != 1) {
//    enqueueBoth(i)
//    checkQStatus
//    i += 1
//  }
//
//  println(s"Queue: $expectedQ")
//
//  // Pop a few, then fill up again - this tests counter wrap
//  val numToPop = 4
//  for (i <- 0 until numToPop) {
//    dequeueBoth
//    checkQStatus
//  }
//
//  val magicOffset = 100
//  for (i <- 0 until numToPop/c.v) {
//    enqueueBoth(i+magicOffset)
//    checkQStatus
//  }
//
//  // Pop everything
//  while (qEmpty != 1) {
//    dequeueBoth
//    checkQStatus
//  }
//
//  checkQStatus
}

object FIFOArbiterTest extends CommonMain {
  type DUTType = FIFOArbiter
  def dut = () => {
    val w = 32
    val d = 512
    val v = 16
    val numStreams = 2
    new FIFOArbiter(w, d, v, numStreams)
  }
  def tester = { c: DUTType => new FIFOArbiterUnitTester(c) }
}

