package plasticine.arch

import chisel3._
import chisel3.util._
import fringe._

import plasticine.templates.Utils.log2Up
import plasticine.spade._


class VerilatorInterface(p: TopParams) extends Bundle {
  // Host scalar interface
  val raddr = Input(UInt(p.fringeParams.addrWidth.W))
  val wen  = Input(Bool())
  val waddr = Input(UInt(p.fringeParams.addrWidth.W))
  val wdata = Input(Bits(p.fringeParams.dataWidth.W))
  val rdata = Output(Bits(p.fringeParams.dataWidth.W))

  // DRAM interface - currently only one stream
  val dram = new DRAMStream(32, 16)

  // Configuration interface
  val config = Flipped(Decoupled(Bool()))
  val configDone = Output(Bool())
}

/**
 * Top: Top module including Fringe and Accel
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class Top(p: TopParams) extends Module {
  val io = p.target match {
    case "vcs"        => IO(new VerilatorInterface(p))
    case _ => throw new Exception(s"Unknown target '${p.target}'")
  }

  // Accel
  val plasticine = Module(new Plasticine(p.plasticineParams, p.fringeParams))

  p.target match {
    case "verilator" | "vcs" =>
      // Simulation Fringe
      val blockingDRAMIssue = false
      val fringe = Module(new Fringe(
        p.fringeParams.dataWidth,
        p.fringeParams.numArgIns,
        p.fringeParams.numArgOuts,
        p.fringeParams.loadStreamInfo,
        p.fringeParams.storeStreamInfo,
        List[StreamParInfo](),  // streamIns
        List[StreamParInfo](),  // streamOuts
        blockingDRAMIssue))

      val topIO = io.asInstanceOf[VerilatorInterface]

      // Fringe <-> Host connections
      fringe.io.raddr := topIO.raddr
      fringe.io.wen   := topIO.wen
      fringe.io.waddr := topIO.waddr
      fringe.io.wdata := topIO.wdata
      topIO.rdata := fringe.io.rdata

      // Fringe <-> DRAM connections
      topIO.dram <> fringe.io.dram

      plasticine.io.argIns.zip(fringe.io.argIns) foreach { case (pArgIn, fArgIn) => pArgIn.bits := fArgIn }
      fringe.io.argOuts.zip(plasticine.io.argOuts) foreach { case (fringeArgOut, accelArgOut) =>
          fringeArgOut.bits := accelArgOut.bits
          fringeArgOut.valid := 1.U
      }
      fringe.io.memStreams <> plasticine.io.memStreams
      plasticine.io.enable := fringe.io.enable
      fringe.io.done := plasticine.io.done

      plasticine.io.config <> topIO.config
      topIO.configDone := plasticine.io.configDone
    case _ =>
      throw new Exception(s"Unknown target '${io.target}'")
  }
}
