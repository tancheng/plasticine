package plasticine.arch

import chisel3._
import chisel3.util._

import plasticine.ArchConfig
import plasticine.templates._
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
import plasticine.pisa.codegen.ConfigInitializer

class PCUWrapper(val p: PCUParams, val initBits: Option[PCUBits] = None) extends Module {
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
  val configType = PCUConfig(p)
  val config = Wire(configType)

  val configSR = Module(new ShiftRegister(configType))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  io.configDone := configSR.io.out.valid
  config := configSR.io.config

  if (initBits.isDefined) {
    val configWire = Wire(configType)
    val configInitializer = new ConfigInitializer()
    configInitializer.init(initBits.get, configWire)
    configSR.io.init := configWire
  } else {
    configSR.io.init := 0.U
  }

//  if (initBits.isDefined) {
//    // ASIC flow
//    println(s"initBits defined, ASIC flow")
//    val configWire = Wire(configType)
//    val configInitializer = new ConfigInitializer()
//    configInitializer.init(initBits.get, configWire)
//    config := configWire
//  } else {
//    // CGRA flow
//    println(s"initBits undefined, CGRA flow")
//    val configSR = Module(new ShiftRegister(configType))
//    configSR.io.in.bits := io.config.bits
//    configSR.io.in.valid := io.config.valid
//    io.configDone := configSR.io.out.valid
//    config := configSR.io.config
//  }

  val pcu = Module(new PCU(p)(0,0))
  pcu.io.vecIn      <> io.vecIn
  pcu.io.vecOut     <> io.vecOut
  pcu.io.scalarIn   <> io.scalarIn
  pcu.io.scalarOut  <> io.scalarOut
  pcu.io.controlIn  <> io.controlIn
  pcu.io.controlOut <> io.controlOut
  pcu.io.config := config
}
