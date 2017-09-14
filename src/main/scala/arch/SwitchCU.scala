package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

import plasticine.spade._
import plasticine.config._
import plasticine.templates._

/**
 * Switch Compute Unit
 */
class SwitchCU(val p: SwitchCUParams)(row: Int, col: Int) extends CU {
  override def desiredName = this.getClass.getName.split('.').last + s"_${row}_${col}"
  val io = IO(CUIO(p, () => SwitchCUConfig(p)))

  // Switch CU only has scalarIn, controlIn, controlOut

  // Scalar input FIFOs
  val scalarFIFOs = List.tabulate(p.numScalarIn) { i =>
    val fifo = Module(new FIFOCore(p.w, p.scalarFIFODepth, 1))
    val config = Wire(FIFOConfig(p.scalarFIFODepth, p.v))
    config.chainWrite := true.B
    config.chainRead := true.B
    fifo.io.config := config
    fifo.io.enq(0) := io.scalarIn(i).bits
    fifo
  }

  // Counter chain
  val counterChain = Module(new CounterChainCore(p.w, p.numCounters))
  counterChain.io.config := io.config.counterChain

  // Connect max and stride
  val ctrMaxStrideSources = Vec(scalarFIFOs.zipWithIndex.map { case (fifo, i) => Mux(io.config.fifoNbufConfig(i) === 1.U, io.scalarIn(i).bits, fifo.io.deq(0)) })
  for (i <- 0 until p.numCounters) {
    // max
    val maxMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    maxMux.io.ins := ctrMaxStrideSources
    maxMux.io.sel := io.config.counterChain.counters(i).max.value
    counterChain.io.max(i) := maxMux.io.out

    // stride
    val strideMux = Module(new MuxN(UInt(p.w.W), ctrMaxStrideSources.size))
    strideMux.io.ins := ctrMaxStrideSources
    strideMux.io.sel := io.config.counterChain.counters(i).stride.value
    counterChain.io.stride(i) := strideMux.io.out
  }

//  // Control Block
  val cbox = Module(new SwitchCUControlBox(p))
  cbox.io.controlIn := io.controlIn
  io.controlOut := cbox.io.controlOut

  // Connect enable to only the zeroth counter, as presumably the compiler
  // always assigns counters that way
  counterChain.io.enable.zipWithIndex.foreach {case (en, idx) => if (idx == 0) en := cbox.io.enable else en := false.B }

  cbox.io.done := counterChain.io.done

  cbox.io.config := io.config.control
}
