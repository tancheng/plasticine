// See LICENSE for license details.

package fringe

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
 * AXI4LiteToRFBridge test harness
 */
class AXI4LiteToRFBridgeUnitTester(c: AXI4LiteToRFBridge)(implicit args: Array[String]) extends ArgsTester(c) {
}


object AXI4LiteToRFBridgeTest extends CommonMain {
  type DUTType = AXI4LiteToRFBridge
  def dut = () => {
    val addrWidth = 32
    val dataWidth = 32
    new AXI4LiteToRFBridge(addrWidth, dataWidth)
  }
  def tester = { c: DUTType => new AXI4LiteToRFBridgeUnitTester(c) }
}
