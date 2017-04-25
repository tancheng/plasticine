package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

import plasticine.templates._
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import plasticine.templates.Utils.log2Up
import plasticine.misc.Utils._
import fringe._

case class PlasticineMemoryInterface(p: MemoryChannelParams) extends Bundle {
  // Scalar IO
  val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))

  // Control IO
  val controlIn = Input(Vec(p.numControlIn, Bool()))
  val controlOut = Output(Vec(p.numControlOut, Bool()))

  // Write data
  val vecIn = Flipped(Decoupled(Vec(p.v, UInt(p.w.W))))
  val vecOut = Decoupled(Vec(p.v, UInt(p.w.W)))
}


case class MemoryChannelIO(p: MemoryChannelParams) extends Bundle {
  // Plasticine
  val plasticine = PlasticineMemoryInterface(p)
  val dram = new AppStreams(List(StreamParInfo(p.w, p.v)), List(StreamParInfo(p.w, p.v)))
  val config = Input(MemoryChannelConfig(p))
}

class MemoryChannel(val p: MemoryChannelParams) extends Module {
  val io = IO(MemoryChannelIO(p))

  // Scalar in Xbar
  val numEffectiveScalarIns = 4

  val scalarInXbar = Module(new CrossbarCore(UInt(p.w.W), ScalarSwitchParams(p.numScalarIn, numEffectiveScalarIns, p.w)))
  scalarInXbar.io.config := io.config.scalarInXbar
  scalarInXbar.io.ins := Vec(io.plasticine.scalarIn.map { _.bits })


  // Scalar input FIFOs
  val scalarFIFOs = List.tabulate(numEffectiveScalarIns) { i =>
    val fifo = Module(new FIFOCore(p.w, p.scalarFIFODepth, 1))
    val config = Wire(FIFOConfig(p.scalarFIFODepth, p.v))
    config.chainWrite := true.B
    config.chainRead := true.B
    fifo.io.config := config
    fifo.io.enq(0) := scalarInXbar.io.outs(i)
//    fifo.io.enqVld := io.scalarIn(i).valid
    fifo
  }

  val scalarIns = scalarFIFOs.map { _.io.deq(0) }

  val raddr = scalarIns(0)
  val waddr = scalarIns(1)
  val rsize = scalarIns(2)
  val wsize = scalarIns(3)

//  val readCtr = Module(new Counter(p.w.W))
//  readCtr.io.reset := false.B
}
