package fringe.fringeZynq

import scala.language.reflectiveCalls
import chisel3._
import chisel3.util._
import axi4._
import fringe._
import templates._
import plasticine.arch.CUIO
import plasticine.spade.CUParams
import templates.Utils.log2Up

class FringeCUZynq[+T<:Bundle](
  val p: CUParams,
  val cuConfig: () => T,
  val numChannels: Int,
  val axiLiteParams: AXI4BundleParameters,
  val axiParams: AXI4BundleParameters
) extends Module {

  // Some constants (mostly MAG-related) that will later become module parameters
  val v = 16 // Number of words in the same stream
  val numOutstandingBursts = 1024  // Picked arbitrarily
  val burstSizeBytes = 64
  val d = 16 // FIFO depth: Controls FIFO sizes for address, size, and wdata. Rdata is not buffered

  val io = IO(new Bundle {
    // Host scalar interface
    val S_AXI = Flipped(new AXI4Lite(axiLiteParams))

    // DRAM interface
    val M_AXI = Vec(numChannels, new AXI4Inlined(axiParams))

    // CU interface
    val cuio = Flipped(CUIO(p, cuConfig))

    // Config interface - named from the perspective of the design
    val configIn = Decoupled(UInt(1.W))
    val configOut = Flipped(Decoupled(UInt(1.W)))

    // Design reset
    val designReset = Output(Bool())
  })

  // Common Fringe
  val fringeCommon = Module(new FringeCU(p, cuConfig, numChannels))
  fringeCommon.io.cuio <> io.cuio
  io.designReset := fringeCommon.io.designReset
  io.configIn <> fringeCommon.io.configIn
  io.configOut <> fringeCommon.io.configOut

  // AXI-lite bridge
  if (FringeGlobals.target == "zynq") {
    val axiLiteBridge = Module(new AXI4LiteToRFBridge(axiLiteParams.addrBits, axiLiteParams.dataBits))
    axiLiteBridge.io.S_AXI <> io.S_AXI

    fringeCommon.reset := ~reset.toBool
    fringeCommon.io.regIO.raddr := axiLiteBridge.io.raddr
    fringeCommon.io.regIO.wen   := axiLiteBridge.io.wen
    fringeCommon.io.regIO.waddr := axiLiteBridge.io.waddr
    fringeCommon.io.regIO.wdata := axiLiteBridge.io.wdata
    axiLiteBridge.io.rdata := fringeCommon.io.regIO.rdata
  }

  // AXI bridge
  io.M_AXI.zipWithIndex.foreach { case (maxi, i) =>
//    val axiBridge = Module(new MAGToAXI4Bridge(axiParams, fringeCommon.mags(i).tagWidth))
    val axiBridge = Module(new MAGToAXI4Bridge(axiParams, fringeCommon.mag.tagWidth))
    axiBridge.io.in <> fringeCommon.io.dram(i)
    maxi <> axiBridge.io.M_AXI
  }
}
