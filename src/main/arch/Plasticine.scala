package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
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
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class Plasticine(val w: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val d: Int,
  val v: Int, rwStages: Int,
  val numTokens: Int,
  val l: Int,
  val r: Int,
  val m: Int,
  val numScratchpads: Int,
  val numStagesAfterReduction: Int,
 inst: PlasticineConfig) extends Module {

  val rows = 4
  val cols = 4

  val io = new Bundle {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val command = Bool(INPUT)
    val status =  Bool(OUTPUT)
  }

  // Main control with command and status
//  val controlBox = Module(new TopUnit())
//  controlBox.io.command := io.command
//  io.status := controlBox.io.statusOut

  // Point-to-point interconnect
  val cus = ListBuffer[ListBuffer[ComputeUnit]]()
  for (i <- 0 until rows) {
    cus.append(new ListBuffer[ComputeUnit]())
    for (j <- 0 until cols) {
      val cu = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, inst.cu(i)))
      cu.io.config_enable := io.config_enable
      cus(i).append(cu)
      if (i == 0) { // First row
        if (j == 0) {  // First column
//           Do nothing
        } else {
          cu.io.dataIn(0) := cus(i)(j-1).io.dataOut
        }
      } else if (j == 0) { // First column
        cu.io.dataIn(1) := cus(i-1)(j).io.dataOut
      } else  {
        cu.io.dataIn(0) := cus(i)(j-1).io.dataOut
        cu.io.dataIn(1) := cus(i-1)(j).io.dataOut
      }
    }
  }
//  controlBox.io.doneTokenIn := cus(rows-1)(cols-1).io.tokenOuts(0)
}

/**
 * ComputeUnit test harness
 */
class PlasticineTests(c: Plasticine) extends PlasticineTester(c) {
  var numCycles = 0

  val a = Array.tabulate(c.m) { i => i}
  val b = Array.tabulate(c.m) { i => i*2}
  val res = Array.tabulate(c.m) { i => a(i) * b(i)}
//  setMem(c.cu0.scratchpads(0), a)
//  setMem(c.cu0.scratchpads(1), b)
//  poke(c.io.command, 1)
  while (numCycles < 100) {
    step(1)
    numCycles += 1
  }

//  expectMem(c.cu1.scratchpads(0), res)
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
    val configObj = Parser(pisaFile).asInstanceOf[PlasticineConfig]

    val bitwidth = 32
    val startDelayWidth = 4
    val endDelayWidth = 4
    val d = 10
    val v = 16
    val l = 0
    val r = 16
    val rwStages = 3
    val numTokens = 4
    val m = 64
    val numScratchpads = 4
    val numStagesAfterReduction = 2
    chiselMainTest(chiselArgs, () => Module(new Plasticine(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, configObj))) {
      c => new PlasticineTests(c)
    }
  }
}
