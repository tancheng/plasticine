// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.ListBuffer

/**
 * WidthConverterFIFO test harness
 */
class WidthConverterFIFOUnitTester(c: WidthConverterFIFO)(implicit args: Array[String]) extends ArgsTester(c) {

  def enq(x: Int) {
    for (i <- 0 until c.vin) {
      poke(c.io.enq(i), x+i)
    }
    poke(c.io.enqVld, 1)
    step(1)
    poke(c.io.enqVld, 0)
  }

  def deq = {
    val deq = peek(c.io.deq)
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    deq
  }

  if (c.inWidth < c.outWidth) {
    // FIFO fill up
    // Fifo must be 'empty' until it has c.inWidth / c.outWidth elements
    val minelems = c.outWidth / c.inWidth
    for (i <- 0 until minelems-1) {
      enq(i+1)
      expect(c.io.empty, 1)
    }
    enq(minelems)
    expect(c.io.empty, 0)

    // FIFO deq
    // Does it do the job?
    val elem = deq
    step(10)
  } else if (c.inWidth > c.outWidth) {
    // FIFO fill up
    // Fifo must be 'empty' until it has c.inWidth / c.outWidth elements
    val minelems = c.inWidth / c.outWidth
    for (i <- 0 until minelems-1) {
      enq(i+1)
      expect(c.io.empty, 1)
    }
    enq(minelems)
    expect(c.io.empty, 0)

    // FIFO deq
    // Does it do the job?
    val elem = deq
    step(10)
  }

  }

object WidthConverterFIFOTest extends CommonMain {
  type DUTType = WidthConverterFIFO
  def dut = () => {
    val win = 16
    val vin = 4
    val wout = 32
    val vout = 16
    val d = 512
    new WidthConverterFIFO(win, vin, wout, vout, d)
  }
  def tester = { c: DUTType => new WidthConverterFIFOUnitTester(c) }
}

