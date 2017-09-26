package fringe

import scala.language.reflectiveCalls
import chisel3._
import chisel3.util.{Cat, EnqIO, Decoupled}
import templates._
import templates.Utils.log2Up
import plasticine.arch.CUIO
import plasticine.spade.CUParams

/**
 * FringeCU: Shell around all CUs
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class FringeCU[+T<:Bundle](val p: CUParams, cuConfig: () => T) extends Module {
  val addrWidth = 32
  val regWidth = 64 // Force 64-bit registers
  val commandReg = 0  // TODO: These vals are used in test only, logic below does not use them.
  val statusReg = 1   //       Changing these values alone has no effect on the logic below.
  val numOutstandingBursts = 1024
  val burstSizeBytes = 64
  val blockingDRAMIssue = false
  val numChannels = 1

  val numArgIOs = 0

  // Some constants (mostly MAG-related) that will later become module parameters
  val v = 16 // Number of words in the same stream
  val d = 512 // FIFO depth: Controls FIFO sizes for address, size, and wdata

  val io = IO(new Bundle {
    // Host scalar interface
    val regIO = new RegFilePureInterface(addrWidth, regWidth)

    // DRAM interface
    val dram = Vec(numChannels, new DRAMStream(32, 16))

    // CU interface
    val cuio = Flipped(CUIO(p, cuConfig))

    // Config interface - named from the perspective of the design
    val configIn = Decoupled(UInt(1.W))
    val configOut = Flipped(Decoupled(UInt(1.W)))

    // Design reset
    val designReset = Output(Bool())
  })

  val linfo = List(StreamParInfo(32, 16))
  val sinfo = List(StreamParInfo(32, 16))
  val mag = Module(new MAGCore(32, d, 16, linfo, sinfo, numOutstandingBursts, burstSizeBytes, blockingDRAMIssue))

  val numDebugs = mag.numDebugs

  // Handle address space here
  // TODO: Can this be done automatically when RegFile is created?
  // TODO: Read the size configs from an external config file?
  val addrMapper = new AddressSpaceManager()
  val (controlStart, controlSize) = addrMapper.createMapping("CONTROL", 65536)
  val (scalarStart, scalarSize)   = addrMapper.createMapping("SCALAR", 65536)
  val (configStart, configSize)   = addrMapper.createMapping("CONFIG", 32768)

  // Scalar registers
  val numScalarRegs = p.numScalarIn + p.numScalarOut + numDebugs
  val scalarRegs = Module(new RegFile(regWidth, numScalarRegs, p.numScalarIn, p.numScalarOut+numDebugs, numArgIOs))

  // Control registers
  val numControlRegs = p.numControlIn + p.numControlOut
  val controlRegs = Module(new RegFile(regWidth, numControlRegs, p.numControlIn, p.numControlOut, numArgIOs))

  // Configuration controller
  val configController = Module(new ConfigController(addrWidth, regWidth))
  io.configIn <> configController.io.configIn
  io.configOut <> configController.io.configOut
  addrMapper.addOffsets(configController.offsetExports, "CONFIG")

  // Address Decoder
  val addrDecoder = Module(new AddressDecoder(
    RegFileParams(addrWidth, regWidth),  // Input
    List(
      RegFileParams(addrWidth, regWidth, scalarStart, scalarSize),
      RegFileParams(addrWidth, regWidth, controlStart, controlSize),
      RegFileParams(addrWidth, regWidth, configStart, configSize)
    )                                   // Outputs
  ))
  addrDecoder.io.regIn <> io.regIO
  scalarRegs.io.addrInterface <> addrDecoder.io.regOuts(0)
  controlRegs.io.addrInterface <> addrDecoder.io.regOuts(1)
  configController.io.regIO <> addrDecoder.io.regOuts(2)
  io.designReset := configController.io.designReset // TODO: Must be resettable from controlRegs too
  scalarRegs.io.argOuts.take(p.numScalarOut).zipWithIndex.foreach { case (argOutReg, i) =>
    argOutReg.bits := io.cuio.scalarOut(i).bits
    argOutReg.valid := io.cuio.scalarOut(i).valid
  }
  controlRegs.io.argOuts.zipWithIndex.foreach { case (argOutReg, i) =>
    argOutReg.bits := io.cuio.controlOut(i)
    argOutReg.valid := true.B
  }

  // TODO: Remove this
  io.cuio.controlIn.foreach { _ := 0.U }

  // Hardware time out (for debugging)
//  val timeoutCycles = 12000000000L
//  val timeoutCtr = Module(new Counter(40))
//  timeoutCtr.io.reset := 0.U
//  timeoutCtr.io.saturate := 1.U
//  timeoutCtr.io.max := timeoutCycles.U
//  timeoutCtr.io.stride := 1.U
//  timeoutCtr.io.enable := localEnable
//  io.argIns.zipWithIndex.foreach{case (p, i) => p := regs.io.argIns(i+2)}

//  val depulser = Module(new Depulser())
//  depulser.io.in := io.done | timeoutCtr.io.done
//  depulser.io.rst := ~command
//  val status = Wire(EnqIO(UInt(regWidth.W)))
//  status.bits := Cat(command(0) & timeoutCtr.io.done, command(0) & depulser.io.out.asUInt)
//  status.valid := depulser.io.out

  // Memory address generator
  val magConfig = Wire(new MAGOpcode())
  magConfig.scatterGather := false.B
  mag.io.config := magConfig
  mag.io.enable := configController.io.configEnabled

  mag.io.app.loads(0) <> configController.io.loadStream
  mag.io.dram <> io.dram(0)

  addrMapper.createHeader("generated_addrOffsetMap.h")
}
