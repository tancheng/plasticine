package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import plasticine.spade._
import plasticine.config._

/**
 * Plasticine Memory Unit
 * @param w: Word width
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param r: Pipeline registers per lane per stage
 * @param numCounters: Number of counters
 * @param numScalarIn: Input scalars
 * @param numScalarOut: Output scalars
 * @param numVectorIn: Input vectors
 * @param numVectorOut: Output vectors
 * @param numControlIn: Input controls
 * @param numControlOut: Output controls
 * @param wd: Number of stages that can be configured to write address calculation
 */
class PMU(val p: PMUParams) extends CU {
  val io = IO(CUIO(p, PMUConfig(p)))

  // Scalar input FIFOs
  val scalarFIFOs = List.tabulate(p.numScalarIn) { i =>
    val fifo = Module(new FIFOCore(p.w, p.scalarFIFODepth, 1))
    val config = Wire(FIFOConfig(p.scalarFIFODepth, p.v))
    config.chainWrite := true.B
    config.chainRead := true.B
    fifo.io.config := config
    fifo.io.enq(0) := io.scalarIn(i).bits
//    fifo.io.enqVld := io.scalarIn(i).valid
    fifo
  }
  val scalarIns = Vec(scalarFIFOs.map { _.io.deq(0)})

  // Vector input FIFOs
  val vectorFIFOs = List.tabulate(p.numVectorIn) { i =>
    val fifo = Module(new FIFOCore(p.w, p.vectorFIFODepth, p.v))
    val config = Wire(FIFOConfig(p.vectorFIFODepth, p.v))
    config.chainWrite := false.B
    config.chainRead := false.B
    fifo.io.config := config
    fifo.io.enq := io.vecIn(i).bits
    fifo.io.enqVld := io.vecIn(i).valid
    io.vecIn(i).ready := ~(fifo.io.almostFull | fifo.io.full)
    fifo
  }

  // Counter chain
  val counterChain = Module(new CounterChainCore(p.w, p.numCounters))
  counterChain.io.config := io.config.counterChain

  // Connect max and stride
  val ctrMaxStrideSources = scalarIns
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

  // Control Block
  val cbox = Module(new PMUControlBox(p))
  cbox.io.controlIn := io.controlIn
  io.controlOut := cbox.io.controlOut

  cbox.io.fifoNotFull := Vec(scalarFIFOs.map { ~_.io.full })
  cbox.io.fifoNotEmpty := Vec(scalarFIFOs.map { ~_.io.empty } ++ vectorFIFOs.map { ~_.io.empty })

  scalarFIFOs.zip(cbox.io.scalarFifoDeqVld) foreach { case (fifo, deqVld) => fifo.io.deqVld := deqVld }
  scalarFIFOs.zip(cbox.io.scalarFifoEnqVld) foreach { case (fifo, enqVld) => fifo.io.enqVld := enqVld }

  counterChain.io.enable := cbox.io.enable

  // Connect enable to deqVld of all vector FIFOs.
  // Deq only if there is a valid element in the FIFO
//  vectorFIFOs.foreach { fifo => fifo.io.deqVld := cbox.io.enable & ~fifo.io.empty }

  cbox.io.done := counterChain.io.done

  cbox.io.config := io.config.control

}
