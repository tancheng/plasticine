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
import java.io.PrintWriter

class CounterChainWrapper(val w: Int, val numCounters: Int = 0) extends Module {
  val io = IO(new Bundle {
    val max      = Input(Vec(numCounters, UInt(w.W)))
    val stride   = Input(Vec(numCounters, UInt(w.W)))
    val configuredMax = Output(Vec(numCounters, UInt(w.W)))
    val out      = Output(Vec(numCounters, UInt(w.W)))
    val next     = Output(Vec(numCounters, UInt(w.W)))

    val enable = Input(Vec(numCounters, Bool()))
    val enableWithDelay = Output(Vec(numCounters, Bool()))
    val done   = Output(Vec(numCounters, Bool()))

    // Config IO: Shift register
    val config = Flipped(Decoupled(Bool()))
    val configDone = Output(Bool())
  })


  // Wire up the reconfiguration network: ASIC or CGRA?
  val configSR = Module(new ShiftRegister(new CounterChainConfig(w, numCounters)))
  configSR.io.in.bits := io.config.bits
  configSR.io.in.valid := io.config.valid
  io.configDone := configSR.io.out.valid

  val config = configSR.io.config

  val counter = Module(new CounterChainCore(w, numCounters))
  counter.io.max            <> io.max
  counter.io.stride         <> io.stride
  counter.io.configuredMax  <> io.configuredMax
  counter.io.out            <> io.out
  counter.io.next           <> io.next
  counter.io.enable         <> io.enable
  counter.io.enableWithDelay<> io.enableWithDelay
  counter.io.done           <> io.done
  counter.io.config := config
}
