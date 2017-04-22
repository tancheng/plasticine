package plasticine.spade

import java.io.File

import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes
import scala.collection.mutable.HashMap
import scala.util.Random

import scala.collection.mutable.ListBuffer
import fringe._

trait Params

trait CUParams extends Params {
  val w: Int = 32
  val v: Int = 16
  val numCounters: Int = 8
  val udCtrWidth = 4
  val numUDCs: Int = 5
  val regColors = ListBuffer[List[RegColor]]()
  val d: Int = 8
  val r: Int = 16
  val numScalarIn: Int = 4
//  val numEffectiveScalarIn: Int = 2
  val numScalarOut: Int = 4
//  val numEffectiveScalarOut: Int = 2
  val numVectorIn: Int = 4
  val numVectorOut: Int = 4
  val numControlIn: Int = 8
  val numControlOut: Int = 8

  val scalarFIFODepth: Int = 32 // Depth of each scalar FIFO
  val vectorFIFODepth: Int = 8 * 16 // Depth of each vector FIFO

  def getRegIDs(c: RegColor) = {
    regColors.zipWithIndex.filter { _._1.contains(c) }.map { _._2}
  }
  def getNumRegs(c: RegColor) = {
    getRegIDs(c).size
  }
}

trait PCUParams extends CUParams

trait PMUParams extends CUParams {
  val wd = 5
  val scratchpadSizeBytes = 32768  // 32 KB scratchpad
//  val outputRegID = 0  // register number that is connected to Scratchpad raddr and waddr
//  val numPipelineScalarOuts = 1  // Number of output scalars from last stage of pipeline
  val numScratchpadScalarOuts = 1 // Number of output scalars from Scratchpad rdata
  def scratchpadSizeWords = scratchpadSizeBytes / (w / 8)
}

trait SwitchCUParams extends CUParams {
  override val v = 0
  override val numVectorIn = 0
  override val numVectorOut = 0
  override val vectorFIFODepth = 0
}

trait PlasticineParams extends Params {
  val w: Int = 32
  val numRows: Int = 2
  val numCols: Int = 2
  val cuParams: Array[Array[CUParams]]
  val switchCUParams: Array[Array[SwitchCUParams]] //TODO
  val vectorSwitchParams:Array[Array[VectorSwitchParams]]
  val scalarSwitchParams:Array[Array[ScalarSwitchParams]]
  val controlSwitchParams:Array[Array[ControlSwitchParams]]
  val numArgOutSelections: List[Int]   //  = List(6,6,6)
}

trait FringeParams extends Params {
  val addrWidth: Int = 32
  val dataWidth: Int = 32
  val numArgIns: Int = 16
  val numArgOuts: Int = 8
  val loadStreamInfo = List[StreamParInfo]()
  val storeStreamInfo = List[StreamParInfo]()
}

trait TopParams {
  val plasticineParams: PlasticineParams
  val fringeParams: FringeParams
  val target:String = "vcs"
}

abstract class SwitchParams(val numIns: Int, val numOuts: Int) extends Params

case class VectorSwitchParams(override val numIns:Int, override val numOuts:Int, w:Int, v:Int) extends SwitchParams(numIns, numOuts)
case class ScalarSwitchParams(override val numIns:Int, override val numOuts:Int, w:Int) extends SwitchParams(numIns, numOuts)
case class ControlSwitchParams(override val numIns:Int, override val numOuts:Int) extends SwitchParams(numIns, numOuts)


