package plasticine.arch

import chisel3._
import chisel3.util._

import plasticine.ArchConfig
import templates._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import plasticine.spade._
import plasticine.config._
import fringe._

import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls
import java.io.PrintWriter

class PCUWrapper(val p: PCUParams) extends Module {
  val io = IO(new Bundle {
    // Vector IO
    val vecIn = Vec(p.numVectorIn, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
    val vecOut = Vec(p.numVectorOut, Decoupled(Vec(p.v, UInt(p.w.W))))

    // Scalar IO
    val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))
    val scalarOut = Vec(p.numScalarOut, Decoupled(UInt(p.w.W)))

    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Config IO: Shift register
    val config = Flipped(Decoupled(Bool()))
    val configDone = Output(Bool())
  })


  // Wire up the reconfiguration network: ASIC or CGRA?
  val configSR = Module(new ShiftRegister(new PCUConfig(p)))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  io.configDone := configSR.io.out.valid

  val config = configSR.io.config

  val pcu = Module(new PCU(p)(0,0))
  pcu.io.vecIn      <> io.vecIn
  pcu.io.vecOut     <> io.vecOut
  pcu.io.scalarIn   <> io.scalarIn
  pcu.io.scalarOut  <> io.scalarOut
  pcu.io.controlIn  <> io.controlIn
  pcu.io.controlOut <> io.controlOut
  pcu.io.config := config
}
