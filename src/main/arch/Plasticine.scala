package plasticine.templates

import Chisel._
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Plasticine Top
 * @param w: Word width
 * @param startDelayWidth: Start delay width
 * @param endDelayWidth: End delay width
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param rwStages: Read-write stages (at the beginning)
 * @param wStages: Write stages (at the end)
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class Plasticine(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val d: Int, val v: Int, rwStages: Int, wStages: Int, val numTokens: Int, val l: Int, val r: Int, val m: Int, inst: PlasticineConfig) extends Module {

  val io = new Bundle {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val command = Bool(INPUT)
    val status =  Bool(OUTPUT)
  }

  // Main control with command and status
  val controlBox = Module(new MainControlBox())
  controlBox.io.command := io.command
  io.status := controlBox.io.statusOut

  // Compute Units
  val cu0 = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, wStages, numTokens, l, r, m, inst.cu(0)))
  cu0.io.config_enable := io.config_enable
  cu0.io.tokenIns.zipWithIndex.foreach { case(in, i) =>
    if (i == 0) {
      in := controlBox.io.startTokenOut  // Route the 'begin' token to the first CU
    } else {
      in := Bool(false)
    }
  }

  val cu1 = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, wStages, numTokens, l, r, m, inst.cu(1)))
  cu1.io.config_enable := io.config_enable
  cu1.io.tokenIns.zipWithIndex.foreach { case(in, i) =>
    if (i == 0) {
      in := controlBox.io.startTokenOut  // Route begin signal to second CU (for write) as well
    } else {
      in := cu0.io.tokenOuts(i)
    }
  }

  cu1.io.dataIn := cu0.io.dataOut
  cu0.io.dataIn := cu1.io.dataOut

  controlBox.io.doneTokenIn := cu0.io.tokenOuts(0)
}

/**
 * ComputeUnit test harness
 */
class PlasticineTests(c: Plasticine) extends PlasticineTester(c) {
  var numCycles = 0

  val a = Array.tabulate(c.m) { i => i}
  val b = Array.tabulate(c.m) { i => i*2}
  val res = Array.tabulate(c.m) { i => a(i) * b(i)}
  setMem(c.cu0.mem0, a)
  setMem(c.cu0.mem1, b)
  poke(c.io.command, 1)
  while (numCycles < 100) {
    step(1)
    numCycles += 1
  }

  expectMem(c.cu1.mem0, res)
  println(s"Done, design ran for $numCycles cycles")
}


object PlasticineTest {
  def main(args: Array[String]): Unit = {

    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl PlasticineTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[PlasticineConfig]]

    val bitwidth = 16
    val startDelayWidth = 4
    val endDelayWidth = 4
    val d = 2
    val v = 1
    val l = 1
    val r = 4
    val rwStages = 1
    val wStages = 1
    val numTokens = 2
    val m = 64
    chiselMainTest(chiselArgs, () => Module(new Plasticine(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, wStages, numTokens, l, r, m, configObj.config))) {
      c => new PlasticineTests(c)
    }
  }
}
