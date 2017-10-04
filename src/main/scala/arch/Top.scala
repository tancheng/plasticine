package plasticine.arch

import chisel3._
import chisel3.util._
import fringe._

import templates.Utils.log2Up
import plasticine.spade._
import plasticine.pisa.ir._
import plasticine.config._
import scala.language.reflectiveCalls

// import AccelTop
abstract class TopInterface extends Bundle

class VerilatorInterface(p: TopParams) extends TopInterface {
  // Host scalar interface
  val regIO = new RegFilePureInterface(p.fringeParams.addrWidth, p.fringeParams.dataWidth)

  // DRAM interface - currently only one stream
  val dram = Vec(p.fringeParams.numChannels, new DRAMStream(32, 16))

  // Configuration interface
  val config = Flipped(Decoupled(Bool()))
  val configDone = Output(Bool())
  val configIn = Flipped(Decoupled(UInt(1.W)))
  val configOut = Decoupled(UInt(1.W))
}

class VerilatorCUInterface[+C<:AbstractConfig](config: C) extends TopInterface {
  // Host scalar interface
  val regIO = new RegFilePureInterface(32, 64)

  // DRAM interface - currently only one stream
  val dram = Vec(1, new DRAMStream(32, 16))

  // Configuration interface
  val configIn = Flipped(Decoupled(UInt(1.W)))
  val configOut = Decoupled(UInt(1.W))

  val configTest = Output(config.cloneType)
}

trait SynthModule extends Module {
  implicit val target: String
}

/**
 * Top: Top module including Fringe and Accel
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class Top(p: TopParams, initBits: Option[AbstractBits] = None)(implicit val target: String) extends SynthModule {
  val io = target match {
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

  target match {
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

// CU testing
class TopCU[+P<:CUParams, +D<:CU, +C<:AbstractConfig](params: () => P, dut: () => D, config: () => C)(implicit val target: String) extends SynthModule {

  val io = target match {
    case "vcs"        => IO(new VerilatorCUInterface(config()))
    case _            => throw new Exception(s"Unknown target '${target}'")
  }

  target match {
    case "vcs" =>
      // CU
      val cu = Module(dut())

      val blockingDRAMIssue = false
      val fringe = Module(new FringeCU(params(), config))

      val topIO = io.asInstanceOf[VerilatorCUInterface[C]]

      // Fringe <-> Host
      fringe.io.regIO <> topIO.regIO

      // Fringe <-> DRAM
      topIO.dram <> fringe.io.dram

      // Fringe <-> CU data and control IO
      cu.io <> fringe.io.cuio

      // Fringe <-> CU reset config IO
      cu.reset := reset | fringe.io.designReset
      cu.io.configIn <> fringe.io.configIn
      cu.io.configOut <> fringe.io.configOut
      cu.io.configReset := reset
      io.configTest <> cu.io.configTest

    case _ =>
      throw new Exception(s"Unknown target '${target}'")
  }


  // Print out total config bits
  val pcuParams = GeneratedTopParams.plasticineParams.cuParams(0)(0).asInstanceOf[PCUParams]
  val pmuParams = GeneratedTopParams.plasticineParams.cuParams(0)(1).asInstanceOf[PMUParams]
  val vswitchParams = GeneratedTopParams.plasticineParams.vectorSwitchParams(0)(0)
  val sswitchParams = GeneratedTopParams.plasticineParams.scalarSwitchParams(0)(0)
  val cswitchParams = GeneratedTopParams.plasticineParams.controlSwitchParams(0)(0)
  val switchCUParams = GeneratedTopParams.plasticineParams.switchCUParams(0)(0)
  val scalarCUParams = GeneratedTopParams.plasticineParams.scalarCUParams(0)(0)
  val memChannelParams = GeneratedTopParams.plasticineParams.memoryChannelParams(0)(0)

  val numRows = 16
  val numCols = 8
  val numPCUs = numRows * numCols / 2
  val numPMUs = numRows * numCols / 2
  val numScalarCUs = (numRows+1) * 2
  val numSwitches = (numRows+1) * (numCols+1)

  val width = numPCUs * PCUConfig(pcuParams).getWidth +
              numPMUs * PMUConfig(pmuParams).getWidth +
              numSwitches * CrossbarConfig(vswitchParams).getWidth +
              numSwitches * CrossbarConfig(sswitchParams).getWidth +
              numSwitches * CrossbarConfig(cswitchParams).getWidth +
              numSwitches * SwitchCUConfig(switchCUParams).getWidth +
              numScalarCUs * ScalarCUConfig(scalarCUParams).getWidth +
              numScalarCUs * MemoryChannelConfig(memChannelParams).getWidth

  println(
s"""Plasticine Configuration:
  Grid Size   : $numRows x $numCols (${numRows * numCols})
  # PCUs      : $numPCUs
  # PMUs      : $numPMUs
  # switches  : $numSwitches
  # scalarCUs : $numScalarCUs

  Total configuration bits size (bits) : $width
                              (bytes)  : ${width / 8.0f}
                              (KB)     : ${width / (8.0f * 1024)}

  Breakdown (bits / component):
      # PCU       : ${PCUConfig(pcuParams).getWidth}, ${numPCUs * PCUConfig(pcuParams).getWidth} total
      # PMU       : ${PMUConfig(pmuParams).getWidth}, ${numPMUs * PMUConfig(pmuParams).getWidth} total
      # VSwitch   : ${CrossbarConfig(vswitchParams).getWidth}, ${numSwitches * CrossbarConfig(vswitchParams).getWidth} total
      # SSwitch   : ${CrossbarConfig(sswitchParams).getWidth}, ${numSwitches * CrossbarConfig(sswitchParams).getWidth} total
      # CSwitch   : ${CrossbarConfig(cswitchParams).getWidth}, ${numSwitches * CrossbarConfig(cswitchParams).getWidth} total
      # SwitchCU  : ${SwitchCUConfig(switchCUParams).getWidth}, ${numSwitches * SwitchCUConfig(switchCUParams).getWidth} total
      # Scalar CU : ${ScalarCUConfig(scalarCUParams).getWidth}, ${numScalarCUs * ScalarCUConfig(scalarCUParams).getWidth} total
      # MemChan   : ${MemoryChannelConfig(memChannelParams).getWidth}, ${numScalarCUs * MemoryChannelConfig(memChannelParams).getWidth} total
""")
}
