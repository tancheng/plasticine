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

class PCUControlBoxWrapper(val p: PCUParams) extends Module {
  val numScalarFIFOs = p.getNumRegs(ScalarInReg)
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Inputs
    val fifoNotFull = Input(Vec(numScalarFIFOs, Bool()))
    val fifoNotEmpty = Input(Vec(numScalarFIFOs+p.numVectorIn, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(numScalarFIFOs, Bool()))
    val scalarFifoEnqVld = Output(Vec(numScalarFIFOs, Bool()))

    // Counter IO
    val enable = Output(Bool())
    val delayedEnable = Input(Bool())
    val done = Input(Bool())
    val delayedDone = Input(Bool())

    // Config
    // Config IO: Shift register
    val config = Flipped(Decoupled(Bool()))
    val configDone = Output(Bool())
  })

  // Wire up the reconfiguration network: ASIC or CGRA?
  val configSR = Module(new ShiftRegister(new PCUControlBoxConfig(p)))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  val config = configSR.io.config

  val cbox = Module(new PCUControlBox(p))
    cbox.io.controlIn         <>  io.controlIn
    cbox.io.controlOut        <>  io.controlOut
    cbox.io.fifoNotFull       <>  io.fifoNotFull
    cbox.io.fifoNotEmpty      <>  io.fifoNotEmpty
    cbox.io.scalarFifoDeqVld  <>  io.scalarFifoDeqVld
    cbox.io.scalarFifoEnqVld  <>  io.scalarFifoEnqVld
    cbox.io.enable            <>  io.enable
    cbox.io.delayedEnable     <>  io.delayedEnable
    cbox.io.done              <>  io.done
    cbox.io.delayedDone       <>  io.delayedDone
    cbox.io.config := config
}
