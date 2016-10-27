package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import scala.collection.mutable.ListBuffer

import java.io.PrintWriter

trait DirectionOps {
  trait Direction

  trait West extends Direction
  trait North extends Direction
  trait East extends Direction
  trait South extends Direction

  def rowOffset(d: Direction) = d match {
    case _:North => -1
    case _: South => 1
    case _ => 0
  }
  def colOffset(d: Direction) = d match {
    case _:East => 1
    case _:West => -1
    case _ => 0
  }

  case class W() extends West
  case class NW() extends North with West
  case class N() extends North
  case class NE() extends North with East
  case class E() extends East
  case class SE() extends South with East
  case class S() extends South
  case class SW() extends South with West

  def reverseDir(d: Direction) = d match {
    case N() => S()
    case S() => N()
    case E() => W()
    case W() => E()
    case NW() => SE()
    case NE() => SW()
    case SW() => NE()
    case SE() => NW()
  }
}

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
 inst: PlasticineConfig) extends Module with DirectionOps {

  val rows = 4
  val cols = 4

  val io = new Bundle {
    val config_enable = Bool(INPUT) // Reconfiguration interface

    /* Control interface */
    val command = Bool(INPUT)
    val status =  Bool(OUTPUT)
  }

  def genSwitchArray(radix: Int, inst: List[CrossbarConfig]) = {
    ListBuffer.tabulate(rows+1) { i =>
      ListBuffer.tabulate(cols+1) { j =>
        Module(new CrossbarVecReg(w, v, radix, radix, inst(i*cols+j)))
      }
    }
  }

  // Absolute direction ordering - assumes a max of '8' directions for now
  // Supporting more directions will need some other way of representing directions
  val dirOrder: List[Direction] = List(W(), NW(), N(), NE(), E(), SE(), S(), SW())

  val dataNumLinksPerDir = 1
  val dataSwitchValidIn: Set[Direction] = Set(N(), S(), E(), W(), NW(), SW())
  val dataSwitchValidOut: Set[Direction] = Set(W(), N(), NE(), E(), SE(), S())

  val ctrlNumLinksPerDir = 4
  val ctrlSwitchValidIn = dataSwitchValidIn
  val ctrlSwitchValidOut = dataSwitchValidOut

  def getIdxBase(d: Direction, validDirs: Set[Direction], numLinksPerDir: Int) = {
    Predef.assert(validDirs.contains(d), s"[getIdx] Direction '$d' not in validDirs set ($validDirs))")
    dirOrder.filter(validDirs.contains(_)).indexOf(d) * numLinksPerDir
  }

  def getIdxs(d: Direction, validDirs: Set[Direction], numLinksPerDir: Int) = {
    val baseIdx = getIdxBase(d, validDirs, numLinksPerDir)
    List.tabulate(numLinksPerDir) { i => baseIdx + i }
  }

  def getCtrlInputIdxs(d: Direction) = {
    getIdxs(d, ctrlSwitchValidIn, ctrlNumLinksPerDir)
  }

  def getCtrlOutputIdxs(d: Direction) = {
    getIdxs(d, ctrlSwitchValidOut, ctrlNumLinksPerDir)
  }

  def getDataInputIdxs(d: Direction) = {
    getIdxs(d, dataSwitchValidIn, dataNumLinksPerDir)
  }

  def getDataOutputIdxs(d: Direction) = {
    getIdxs(d, dataSwitchValidOut, dataNumLinksPerDir)
  }

  def connectSwitchArray(switches: ListBuffer[ListBuffer[CrossbarVecReg]]) {
    val pw = new PrintWriter("switchDot.dot")
    pw.println("digraph G {")
    for (i <- 0 to rows) {
      for (j <- 0 to cols) {
        pw.println(s"""${i}${j} [ pos= "$i,$j!"]""")
        val validInputs = Set(W(), N(), E(), S())
        if (i == 0) { List(N(), NE(), NW()) foreach { validInputs -= _ } }
        if (i == rows) { List(S(), SE(), SW()) foreach { validInputs -= _ } }
        if (j == 0) { List(W(), NW(), SW()) foreach { validInputs -= _ } }
        if (j == rows) { List(E(), SE(), NE()) foreach { validInputs -= _ } }
        validInputs.foreach { case in =>
          val inIdxs = getDataInputIdxs(in) // Indices of input ports
          val outIdxs = getDataOutputIdxs(reverseDir(in))  // Indices of output that connects to idxs
          val r = rowOffset(in)
          val c = colOffset(in)
          inIdxs.zip(outIdxs) foreach { case (inIdx, outIdx) =>
            switches(i)(j).io.ins(inIdx) := switches(i+r)(j+c).io.outs(outIdx)
            pw.println(s"${(i+r)}${(j+c)} -> ${i}${j}")
          }
//          println(s"Assigning ${i+r}, ${j+c}, ${reverseIdx} (${switches(i+r)(j+c).io.outs(reverseIdx)}) to (${i}, ${j}, ${idx}) (${switches(i)(j).io.ins(idx)})")
        }
      }
    }
    pw.println("}")
    pw.close
  }


  Predef.assert(dataSwitchValidIn.size == dataSwitchValidOut.size,
    s"Data switch has unequal number of inputs and outputs, which is currently unsupported")
  val dataSwitches = genSwitchArray(dataSwitchValidIn.size, inst.dataSwitch)
  connectSwitchArray(dataSwitches)

  // Main control with command and status
//  val controlBox = Module(new TopUnit())
//  controlBox.io.command := io.command
//  io.status := controlBox.io.statusOut

  // Point-to-point interconnect
//  val cus = ListBuffer[ListBuffer[ComputeUnit]]()
//  for (i <- 0 until rows) {
//    cus.append(new ListBuffer[ComputeUnit]())
//    for (j <- 0 until cols) {
//      val cu = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, inst.cu(i)))
//      cu.io.config_enable := io.config_enable
//      cus(i).append(cu)
//      if (i == 0) { // First row
//        if (j == 0) {  // First column
////           Do nothing
//        } else {
//          cu.io.dataIn(0) := cus(i)(j-1).io.dataOut
//        }
//      } else if (j == 0) { // First column
//        cu.io.dataIn(1) := cus(i-1)(j).io.dataOut
//      } else  {
//        cu.io.dataIn(0) := cus(i)(j-1).io.dataOut
//        cu.io.dataIn(1) := cus(i-1)(j).io.dataOut
//      }
//    }
//  }
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

//    if (appArgs.size != 1) {
//      println("Usage: bin/sadl PlasticineTest <pisa config>")
//      sys.exit(-1)
//    }
//
//    val pisaFile = appArgs(0)
//    val configObj = Parser(pisaFile).asInstanceOf[PlasticineConfig]

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
    val rows = 4
    val cols = 4
    val configObj = PlasticineConfig.getRandom(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads)
    chiselMainTest(chiselArgs, () => Module(new Plasticine(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, configObj))) {
      c => new PlasticineTests(c)
    }
  }
}
