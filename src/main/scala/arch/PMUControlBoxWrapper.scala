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

class PMUControlBoxWrapper(val p: PMUParams) extends Module {
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Inputs
    val fifoNotFull = Input(Vec(p.getNumRegs(ScalarInReg), Bool()))
    val fifoNotEmpty = Input(Vec(p.getNumRegs(ScalarInReg)+p.numVectorIn, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(p.getNumRegs(ScalarInReg), Bool()))
    val scalarFifoEnqVld = Output(Vec(p.getNumRegs(ScalarInReg), Bool()))

    // Counter IO
    val enable = Output(Vec(p.numCounters, Bool()))
    val done = Input(Vec(p.numCounters, Bool()))

    // SRAM IO
    val sramSwapWrite = Output(Bool())
    val sramSwapRead = Output(Bool())

    // Config IO: Shift register
    val config = Flipped(Decoupled(Bool()))
    val configDone = Output(Bool())
  })

  // Wire up the reconfiguration network: ASIC or CGRA?
  val configSR = Module(new ShiftRegister(new PMUControlBoxConfig(p)))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  io.configDone := configSR.io.out.valid

  val config = configSR.io.config

  val pcu = Module(new PMUControlBox(p))
  pcu.io.controlIn        <> io.controlIn
  pcu.io.controlOut       <> io.controlOut
  pcu.io.fifoNotFull      <> io.fifoNotFull
  pcu.io.fifoNotEmpty     <> io.fifoNotEmpty
  pcu.io.scalarFifoDeqVld <> io.scalarFifoDeqVld
  pcu.io.scalarFifoEnqVld <> io.scalarFifoEnqVld
  pcu.io.enable           <> io.enable
  pcu.io.done             <> io.done
  pcu.io.sramSwapWrite    <> io.sramSwapWrite
  pcu.io.sramSwapRead     <> io.sramSwapRead

  pcu.io.config := config
}
