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

class CounterWrapper(val w: Int, val startDelayWidth: Int = 0, val endDelayWidth: Int = 0) extends Module {
  val io = IO(new Bundle {
    val max      = Input(UInt(w.W))
    val stride   = Input(UInt(w.W))
    val configuredMax = Output(UInt(w.W))
    val out      = Output(UInt(w.W))
    val next     = Output(UInt(w.W))
    val enable    = Input(Bool())
    val enableWithDelay = Output(Bool())
    val waitIn    = Input(Bool())
    val waitOut    = Output(Bool())
    val done   = Output(Bool())
    val isMax  = Output(Bool())

    // Config IO: Shift register
    val config = Flipped(Decoupled(Bool()))
    val configDone = Output(Bool())

  })

  // Wire up the reconfiguration network: ASIC or CGRA?
  val configSR = Module(new ShiftRegister(new CounterConfig(w, startDelayWidth, endDelayWidth)))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  io.configDone := configSR.io.out.valid

  val config = configSR.io.config

  val counter = Module(new CounterCore(w, startDelayWidth, endDelayWidth))
  counter.io.max             <> io.max
  counter.io.stride          <> io.stride
  counter.io.configuredMax   <> io.configuredMax
  counter.io.out             <> io.out
  counter.io.next            <> io.next
  counter.io.enable          <> io.enable
  counter.io.enableWithDelay <> io.enableWithDelay
  counter.io.waitIn          <> io.waitIn
  counter.io.waitOut         <> io.waitOut
  counter.io.done            <> io.done
  counter.io.isMax           <> io.isMax
  counter.io.config := config
}
