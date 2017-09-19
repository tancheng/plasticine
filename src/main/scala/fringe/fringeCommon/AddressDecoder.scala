package fringe

import scala.language.reflectiveCalls
import util._
import chisel3._
import chisel3.util.{Cat, EnqIO, Decoupled, PriorityEncoder, isPow2}
import templates._
import templates.Utils.log2Up
import plasticine.arch.CUIO
import plasticine.misc.Utils.{getFF, getMux}

/**
 * Address Decoder: Route incoming requests to appropriate output based on address map
 */
case class RegFileParams(
  val addrWidth: Int,
  val dataWidth: Int,
  val startAddr: Int = -1,
  val size: Int = -1
)

class AddressDecoder(val pin: RegFileParams, val pout: List[RegFileParams]) extends Module {

  val io = IO(new Bundle {
    // Host scalar interface
    val regIn = new RegFilePureInterface(pin.addrWidth, pin.dataWidth)
    val regOuts = HVec.tabulate(pout.size) { i => Flipped(new RegFilePureInterface(pout(i).addrWidth, pout(i).dataWidth)) }
  })

  // Check for collisions
  val startAddrs = pout.map {_.startAddr}
  Predef.assert(startAddrs.distinct == startAddrs, s"ERROR: Attempting to map two or more outputs to same starting address!")
  Predef.assert(startAddrs.map{a => (a == 0) | isPow2(a)}.reduce{_&_}, s"ERROR: At least one start address is not a power-of-2")

  // Sort output order from greatest to least start addr
  val outputOrder = startAddrs.zipWithIndex.sortBy { _._1 }.reverse.map { _._2 }

  // Pick 'deciding bit' logic for each output in outputOrder
  val decidingBitWaddr = outputOrder.map { i =>
    if (startAddrs(i) == 0) true.B
    else io.regIn.waddr(log2Up(startAddrs(i)))
  }
  val decidingBitRaddr = outputOrder.map { i =>
    if (startAddrs(i) == 0) true.B
    else io.regIn.raddr(log2Up(startAddrs(i)))
  }

  val masks = outputOrder.map { i => (1 << (log2Up(pout(i).size + startAddrs(i)))) - 1 }

  val tag = getFF(PriorityEncoder(Vec(decidingBitRaddr)), true.B)

	val rdata = getMux(io.regOuts.map{_.rdata}.toList, tag)

  // Convert deciding bits to select signals
  outputOrder.foreach { i =>
    io.regOuts(i).wen := io.regIn.wen & (if (i == 0) true.B else ((0 until i).toList.map { j => ~decidingBitWaddr(j) }.reduce{_&_})) & decidingBitWaddr(i)
    io.regOuts(i).waddr := io.regIn.waddr & masks(i).U
    io.regOuts(i).raddr := io.regIn.raddr & masks(i).U
    io.regOuts(i).wdata := io.regIn.wdata
    io.regIn.rdata := rdata
  }
}
