package plasticine.arch

import chisel3._
import chisel3.util.Decoupled
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

  val vecIn = Flipped(Decoupled(Vec(p.v, UInt(p.w.W))))
  val vecOut = Decoupled(Vec(p.v, UInt(p.w.W)))
}


case class MemoryChannelIO(p: MemoryChannelParams) extends Bundle {
  // Plasticine
  val plasticine = PlasticineMemoryInterface(p)
  val dramLoad = Flipped(new LoadStream(StreamParInfo(p.w, p.v)))
  val dramStore    = Flipped(new StoreStream(StreamParInfo(p.w, p.v)))
  val config = Input(MemoryChannelConfig(p))
}

class MemoryChannel(val p: MemoryChannelParams) extends Module {
  val io = IO(MemoryChannelIO(p))

  // Scalar in Xbar
  val numEffectiveScalarIns = 4

  // Control inputs
  val tokenInXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, numEffectiveScalarIns)))
  tokenInXbar.io.config := io.config.tokenInXbar
  tokenInXbar.io.ins := io.plasticine.controlIn

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
    fifo.io.enqVld := tokenInXbar.io.outs(i)
    fifo
  }

  val scalarIns = scalarFIFOs.map { _.io.deq(0) }

  val READ = 0
  val WRITE = 1
  val RSIZE = 2
  val WSIZE = 3

  val raddr = scalarIns(READ)
  val waddr = scalarIns(WRITE)
  val rsize = scalarIns(RSIZE)
  val wsize = scalarIns(WSIZE)

  // Command is valid only when both addrFifo and sizeFifo are not empty
  val readCmdValid = ~scalarFIFOs(READ).io.empty & ~scalarFIFOs(READ+1).io.empty
  val writeCmdValid = ~scalarFIFOs(WRITE).io.empty & ~scalarFIFOs(WRITE+1).io.empty
  scalarFIFOs(READ).io.deqVld := readCmdValid
  scalarFIFOs(WRITE).io.deqVld := writeCmdValid

  io.dramLoad.cmd.bits.addr := raddr
  io.dramLoad.cmd.bits.size := rsize
  io.dramLoad.cmd.bits.isWr := 0.U
  io.dramLoad.cmd.valid := readCmdValid

  io.dramStore.cmd.bits.addr := waddr
  io.dramStore.cmd.bits.size := wsize
  io.dramStore.cmd.bits.isWr := 1.U
  io.dramStore.cmd.valid := writeCmdValid


  val readCtr = Module(new Counter(p.w))
  readCtr.io.enable := io.dramLoad.rdata.valid
  readCtr.io.reset := false.B
  readCtr.io.max := rsize
  readCtr.io.stride := 1.U
  readCtr.io.saturate := 0.U
  scalarFIFOs(RSIZE).io.deqVld := readCtr.io.done

  val writeCtr = Module(new Counter(p.w))
  writeCtr.io.enable := io.dramStore.wresp.bits
  writeCtr.io.reset := false.B
  writeCtr.io.max := wsize
  writeCtr.io.stride := 1.U
  writeCtr.io.saturate := 0.U
  scalarFIFOs(WSIZE).io.deqVld := writeCtr.io.done

  val tokenOutXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(numEffectiveScalarIns+2, p.numControlOut)))
  tokenOutXbar.io.config := io.config.tokenOutXbar
  tokenOutXbar.io.ins := Vec(scalarFIFOs.map { ~_.io.full } ++ List(readCtr.io.done, writeCtr.io.done))
  io.plasticine.controlOut := tokenOutXbar.io.outs

  io.dramLoad.rdata <> io.plasticine.vecOut
  io.plasticine.vecIn <> io.dramStore.wdata
}
