package fringe

import scala.language.reflectiveCalls
import util._
import chisel3._
//import chisel3.util.{Cat, EnqIO, Decoupled, PriorityEncoder, isPow2, Enum, switch, is}
import chisel3.util._
import templates._
import templates.Utils.log2Up
import plasticine.arch.CUIO
import plasticine.misc.Utils.{getFF, getMux, getCounter, getTimer, convertVec}
import scala.collection.mutable.HashMap

/**
 * ConfigController: Handles loading configuration bits from DRAM and shifting it through the config network
 */
class ConfigController(val regAddrWidth: Int, val regDataWidth: Int) extends Module {
  val io = IO(new Bundle {
    val regIO = new RegFilePureInterface(regAddrWidth, regDataWidth)
    val loadStream = Flipped(new LoadStream(StreamParInfo(32, 16)))
    val designReset = Output(Bool())
    val configIn = Decoupled(UInt(1.W))
    val configOut = Flipped(Decoupled(UInt(1.W)))
    val configEnabled = Output(Bool())
  })

  // Command, Status, addr, size registers
  val numRegs = 1 + 1 + 1 + 1
  val regFile = Module(new RegFile(regDataWidth, numRegs, 4, 1, 0))
  regFile.io.addrInterface <> io.regIO

  val commandReg = 0
  val statusReg = 1
  val addrReg = 2
  val sizeReg = 3
  def offsetExports = {
    val m = HashMap[String, Int]()
    m += "ADDR"    -> addrReg
    m += "SIZE"    -> sizeReg
    m
  }

  val command = regFile.io.argIns(regFile.regIdx2ArgIn(commandReg))
  val addr = regFile.io.argIns(regFile.regIdx2ArgIn(addrReg))
  val size = regFile.io.argIns(regFile.regIdx2ArgIn(sizeReg))
  val status = regFile.io.argOuts(regFile.regIdx2ArgOut(statusReg)).bits
  regFile.io.argOuts(regFile.regIdx2ArgOut(statusReg)).valid := true.B

  val enable = command(0)
  io.configEnabled := enable

  // FSM to load config bits and shift it through the network
  val numStates = 5  // READY, RESET, LOAD, CONFIG, DONE
  val state_READY :: state_RESET :: state_LOAD :: state_CONFIG :: state_DONE :: Nil = Enum(numStates)
  val nextState = Wire(UInt(log2Up(numStates).W))
  val transitionNow = Wire(Bool())
  val state = getFF(nextState, transitionNow)

  val numResetCycles = 100
  val enableReset = state === state_RESET
  val resetTimerDone = getTimer(numResetCycles.U, enableReset, 16)

  val piso = Module(new PISO(UInt(1.W), 512))
  piso.io.in.bits   := convertVec(io.loadStream.rdata.bits, 1, 512)
  piso.io.in.valid     := io.loadStream.rdata.valid
  io.loadStream.rdata.ready := piso.io.in.ready
  io.configIn.bits  := piso.io.out.bits
  piso.io.out.ready := ~io.configOut.valid

  switch(state) {
    is (state_READY) {
      status := 0.U
      nextState := state_RESET
      transitionNow := enable
      io.designReset := ~io.configOut.valid
      io.loadStream.cmd.valid := false.B
      piso.io.clear := true.B
      io.configIn.valid := false.B
    }
    is (state_RESET) {
      nextState := state_LOAD
      transitionNow := resetTimerDone
      io.designReset := true.B
      io.loadStream.cmd.valid := false.B
      piso.io.clear := true.B
      io.configIn.valid := false.B
    }
    is (state_LOAD) {
      nextState := state_CONFIG
      io.loadStream.cmd.bits.addr := addr
      io.loadStream.cmd.bits.size := size
      io.loadStream.cmd.bits.isWr := false.B
      io.loadStream.cmd.bits.isSparse := false.B
      io.loadStream.cmd.valid := true.B
      transitionNow := io.loadStream.cmd.ready
      io.designReset := true.B
      piso.io.clear := true.B
      io.configIn.valid := false.B
    }
    is (state_CONFIG) {
      nextState := state_DONE
      transitionNow := io.configOut.valid
      io.loadStream.cmd.valid := false.B
      io.designReset := true.B
      piso.io.clear := false.B
      io.configIn.valid := piso.io.out.valid
    }
    is (state_DONE) {
      nextState := state_READY
      status := 1.U
      transitionNow := ~enable
      io.designReset := true.B
      io.loadStream.cmd.valid := false.B
      piso.io.clear := false.B
      io.configIn.valid := false.B
    }
  }
  // Pseudo-code:
  // state match {
  //   case READY => // Just wait for enable to go high
  //     if (enable) nextState := RESET, designReset := false, transitionNow := true
  //     else nextState := READY, designReset := false, transitionNow := false
  //   case RESET =>
  //      for (i <- 0 until maxResetCycles) designReset := true
  //      nextState := LOAD, designReset := false, transitionNow := true
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
