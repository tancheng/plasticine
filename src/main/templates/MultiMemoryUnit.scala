package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class MultiMemoryUnitTest (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val inst: MemoryUnitConfig) extends Module {

  val numMemoryUnits = 4
  val numChnRankBits = 4
  val wordSize = w/8

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnects = Vec.fill(numMemoryUnits) { new PlasticineMemoryCmdInterface(w, v) }
    val dramChannel = Vec.fill(numMemoryUnits) { new DRAMCmdInterface(w, v) }
  }

  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, m, v, numOutstandingBursts, MemoryUnitConfig(0)))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data
      mu.io.interconnect.rdyIn := io.interconnects(i).rdyIn
      mu.io.interconnect.vldIn := io.interconnects(i).vldIn
      io.interconnects(i).rdyOut := mu.io.interconnect.rdyOut
      io.interconnects(i).vldOut := mu.io.interconnect.vldOut
      mu.io.interconnect.addr := io.interconnects(i).addr
      mu.io.interconnect.wdata := io.interconnects(i).wdata
      mu.io.interconnect.isWr := io.interconnects(i).isWr
      mu.io.interconnect.dataVldIn := io.interconnects(i).dataVldIn
      mu.io.interconnect.size := io.interconnects(i).size
      io.interconnects(i).rdata := mu.io.interconnect.rdata

      io.dramChannel(i).addr := mu.io.dram.addr
      io.dramChannel(i).wdata := mu.io.dram.wdata
      io.dramChannel(i).tagOut := mu.io.dram.tagOut

      io.dramChannel(i).vldOut := mu.io.dram.vldOut
      io.dramChannel(i).rdyOut := mu.io.dram.rdyOut
      io.dramChannel(i).isWr  := mu.io.dram.isWr

      mu.io.dram.rdata := io.dramChannel(i).rdata
      mu.io.dram.vldIn := io.dramChannel(i).vldIn
      mu.io.dram.rdyIn := io.dramChannel(i).rdyIn
      mu.io.dram.tagIn := io.dramChannel(i).tagIn

      mu
    }
  }

  def genDRAMSims = {
    List.tabulate(numMemoryUnits) { j =>
      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))

      DRAMSimulator
    }
  }

  val mus = genMemoryUnits
  val sims = genDRAMSims
  val chnRanksIdBits = Vec(UInt(0), UInt(4), UInt(8), UInt(12)) { UInt(width=4) }
  for (id <- 0 to numMemoryUnits) {
    sims(id).io.addr := Cat(chnRanksIdBits(id), mus(id).io.dram.addr)
    sims(id).io.wdata := mus(id).io.dram.wdata
    sims(id).io.tagIn := mus(id).io.dram.tagOut
    sims(id).io.vldIn := mus(id).io.dram.vldOut
    sims(id).io.isWr := mus(id).io.dram.isWr

    mus(id).io.dram.rdata := sims(id).io.rdata
    mus(id).io.dram.vldIn := sims(id).io.vldOut
    mus(id).io.dram.tagIn := sims(id).io.tagOut
  }
}

class MultiMemoryUnitTests(c: MultiMemoryUnitTest) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  def poke(port: Vec[UInt], value: Seq[Int]) {
    port.zip(value) foreach { case (in, i) => poke(in, i) }
  }

  def peek(port: Vec[UInt]): List[Int] = {
    port.map { peek(_).toInt }.toList
  }


}

object MultiMemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16
  val burstSizeBytes = 64

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new MultiMemoryUnitTest(w, d, v, numOutstandingBursts, burstSizeBytes, MemoryUnitCnfig(0)))) {
      c => new MultiMemoryUnitTests(c)
    }
  }
}
