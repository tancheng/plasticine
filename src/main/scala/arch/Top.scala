package plasticine.arch

import chisel3._
import chisel3.util._
import fringe._

import templates.Utils.log2Up
import plasticine.spade._
import plasticine.pisa.ir._
import plasticine.config.PMUConfig
import scala.language.reflectiveCalls

// import AccelTop
abstract class TopInterface extends Bundle {
  // Host scalar interface
//  var raddr = Input(UInt(1.W))
//  var wen  = Input(Bool())
//  var waddr = Input(UInt(1.W))
//  var wdata = Input(Bits(1.W))
//  var rdata = Output(Bits(1.W))
}

class VerilatorInterface(p: TopParams) extends Bundle {
  // Host scalar interface
  val regIO = new RegFilePureInterface(p.fringeParams.addrWidth, p.fringeParams.dataWidth)
//  val raddr = Input(UInt(p.fringeParams.addrWidth.W))
//  val wen  = Input(Bool())
//  val waddr = Input(UInt(p.fringeParams.addrWidth.W))
//  val wdata = Input(Bits(p.fringeParams.dataWidth.W))
//  val rdata = Output(Bits(p.fringeParams.dataWidth.W))

  // DRAM interface - currently only one stream
  val dram = Vec(p.fringeParams.numChannels, new DRAMStream(32, 16))

  // Configuration interface
  val config = Flipped(Decoupled(Bool()))
  val configDone = Output(Bool())
  val configIn = Flipped(Decoupled(UInt(1.W)))
  val configOut = Decoupled(UInt(1.W))
}

class VerilatorCUInterface(p: CUParams) extends Bundle {
  // Host scalar interface
  val regIO = new RegFilePureInterface(p.w, p.w)

  // DRAM interface - currently only one stream
  val dram = Vec(1, new DRAMStream(32, 16))

  // Configuration interface
  val configIn = Flipped(Decoupled(UInt(1.W)))
  val configOut = Decoupled(UInt(1.W))
}

/**
 * Top: Top module including Fringe and Accel
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class Top(p: TopParams, initBits: Option[AbstractBits] = None) extends Module {
  val io = p.target match {
    case "vcs"        => IO(new VerilatorInterface(p))
    case _ => throw new Exception(s"Unknown target '${p.target}'")
  }

  // Accel
  val plasticine = Module(new Plasticine(p.plasticineParams, p.fringeParams, initBits.asInstanceOf[Option[PlasticineBits]]))
//  val plasticine = Module(new CounterWrapper(p.plasticineParams.w, 0, 0))
//  val plasticine = Module(new CounterChainWrapper(p.plasticineParams.w, 8))
//  val plasticine = Module(new PCUWrapper(p.plasticineParams.cuParams(0)(0).asInstanceOf[PCUParams]))

  // Memory IO
  private val numMemoryChannels = p.plasticineParams.memoryChannelParams.flatten.size
  private val streamPar = StreamParInfo(32, 16)
  private val streamParams = List.fill(numMemoryChannels) { streamPar }

  p.target match {
    case "vcs" =>
      // Simulation Fringe
      val blockingDRAMIssue = false
      val fringe = Module(new Fringe(
        p.fringeParams.dataWidth,
        p.fringeParams.numArgIns,
        p.fringeParams.numArgOuts,
        p.fringeParams.numArgIOs,
        p.fringeParams.numChannels,
        p.fringeParams.numArgInstrs,
        streamParams,
        streamParams,
        List[StreamParInfo](),  // streamIns
        List[StreamParInfo](),  // streamOuts
        blockingDRAMIssue))

      val topIO = io.asInstanceOf[VerilatorInterface]

      // Fringe <-> Host connections
      fringe.io.regIO <> topIO.regIO
//      fringe.io.raddr := topIO.raddr
//      fringe.io.wen   := topIO.wen
//      fringe.io.waddr := topIO.waddr
//      fringe.io.wdata := topIO.wdata
//      topIO.rdata := fringe.io.rdata

      // Fringe <-> DRAM connections
      topIO.dram <> fringe.io.dram

      plasticine.io.argIns.zip(fringe.io.argIns) foreach { case (pArgIn, fArgIn) => pArgIn.bits := fArgIn }
      fringe.io.argOuts.zip(plasticine.io.argOuts) foreach { case (fringeArgOut, accelArgOut) =>
          fringeArgOut.bits := accelArgOut.bits
          fringeArgOut.valid := accelArgOut.valid
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

// PMU testing
class TopPMU(p: PMUParams) extends Module {
  val io = IO(new VerilatorCUInterface(p))

  // CU
  val pmu = Module(new PMU(p))

  val blockingDRAMIssue = false
  val fringe = Module(new FringeCU(p, () => PMUConfig(p)))

  val topIO = io.asInstanceOf[VerilatorCUInterface]

  // Fringe <-> Host
  fringe.io.regIO <> topIO.regIO

  // Fringe <-> DRAM
  topIO.dram <> fringe.io.dram

  // Fringe <-> CU data and control IO
  pmu.io <> fringe.io.cuio

  // Fringe <-> CU reset config IO
  pmu.reset := reset | fringe.io.designReset
  pmu.io.configIn <> fringe.io.configIn
  pmu.io.configOut <> fringe.io.configOut
}
