package fringe

import scala.language.reflectiveCalls
import util._
import chisel3._
//import chisel3.util.{Cat, EnqIO, Decoupled, PriorityEncoder, isPow2, Enum, switch, is}
import chisel3.util._
import templates._
import templates.Utils.log2Up
import plasticine.arch.CUIO
import plasticine.misc.Utils.{getFF, getMux, getCounter, getTimer}

/**
 * ConfigController: Handles loading configuration bits from DRAM and shifting it through the config network
 */
class ConfigController(val regAddrWidth: Int, val regDataWidth: Int, val dramAddrWidth: Int, val dramDataWidth: Int) extends Module {
  val io = IO(new Bundle {
    val regIO = new RegFilePureInterface(regAddrWidth, regDataWidth)
    val loadStream = new LoadStream(StreamParInfo(32, 16))
    val archReset = Output(Bool())
    val configIn = Decoupled(UInt(1.W))
    val configOut = Flipped(Decoupled(UInt(1.W)))
  })

  // Command, Status, addr, size registers
  val numRegs = 1 + 1 + 1 + 1
  val regFile = Module(new RegFile(regDataWidth, numRegs, 3, 1, 0))
  regFile.io.addrInterface <> io.regIO

  val command = regFile.io.argIns(0)
  val addr = regFile.io.argIns(1)
  val size = regFile.io.argIns(2)
  val status = regFile.io.argOuts(0).bits
  regFile.io.argOuts(0).valid := true.B

  val enable = command(0)

  // FSM to load config bits and shift it through the network
  val numStates = 5  // READY, RESET, LOAD, CONFIG, DONE
  val state_READY :: state_RESET :: state_LOAD :: state_CONFIG :: state_DONE :: Nil = Enum(numStates)
  val nextState = Wire(UInt(log2Up(numStates).W))
  val transitionNow = Wire(Bool())
  val state = getFF(nextState, transitionNow)

  val numResetCycles = 1000
  val enableReset = state === state_RESET
  val resetTimerDone = getTimer(numResetCycles.U, enableReset, 16)
  switch(state) {
    is (state_READY) {
      status(0) := false.B
      nextState := state_RESET
      transitionNow := enable
    }
    is (state_RESET) {
      nextState := state_LOAD
      transitionNow := resetTimerDone
    }
    is (state_LOAD) {
      nextState := state_CONFIG
      io.loadStream.cmd.bits.addr := addr
      io.loadStream.cmd.bits.size := size
      io.loadStream.cmd.bits.isWr := false.B
      io.loadStream.cmd.bits.isSparse := false.B
      io.loadStream.cmd.valid := true.B
      transitionNow := io.loadStream.cmd.ready
    }
    is (state_CONFIG) {
      nextState := state_DONE
      transitionNow := io.configOut.valid
    }
    is (state_DONE) {
      nextState := state_READY
      status(0) := true.B
      transitionNow := ~enable
    }
  }
  // Pseudo-code:
  // state match {
  //   case READY => // Just wait for enable to go high
  //     if (enable) nextState := RESET, archReset := false, transitionNow := true
  //     else nextState := READY, archReset := false, transitionNow := false
  //   case RESET =>
  //      for (i <- 0 until maxResetCycles) archReset := true
  //      nextState := LOAD, archReset := false, transitionNow := true
  //   case LOAD =>
  //      issueCmd(addr, size, isWr = false)
  //      nextState := CONFIG, transitionNow := true
  //   case CONFIG =>
  //      ready_rdata := true
  //      if (valid_rdata & ready_rdata) {
  //        ready_rdata := false
  //        latch rdata into registers for pipelining
  //        Start counter to shift loaded into 'configIn'. When counter done, ready_rdata := true
  //      }
  //      if (configOut.valid) { transitionNow := true, nextState := DONE }
  //   case DONE =>
  //     status(0) := true (done)
  //     nextState := READY
  //     if (~enable) transitionNow := true
  //     else transitionNow := false
  // }


}
