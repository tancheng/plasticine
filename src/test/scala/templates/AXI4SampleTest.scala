// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * AXI4Sample test harness
 */
class AXI4SampleUnitTester(c: AXI4Sample)(implicit args: Array[String]) extends ArgsTester(c) {
}


object AXI4SampleTest extends CommonMain {
  type DUTType = AXI4Sample
  def dut = () => {
    val w = 32
    new AXI4Sample(w)
  }
  def tester = { c: DUTType => new AXI4SampleUnitTester(c) }
}
