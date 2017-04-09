// See LICENSE for license details.

package plasticine.templates
import plasticine.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.ListBuffer

/**
 * Fringe test harness
 */
class FringeUnitTester(c: Fringe)(implicit args: Array[String]) extends ArgsTester(c)

object FringeTest extends CommonMain {
  type DUTType = Fringe
  def dut = () => {
    val w = 32
    val numArgIns = 5
    val numArgOuts = 3
    val loadStreamInfo = List[StreamParInfo]()
    val storeStreamInfo = List[StreamParInfo]()
    new Fringe(w, numArgIns, numArgOuts, loadStreamInfo, storeStreamInfo)
  }
  def tester = { c: DUTType => new FringeUnitTester(c) }
}

