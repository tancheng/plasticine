package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

abstract class AbstractPlasticineTop (
  val w: Int,
	val startDelayWidth: Int,
	val endDelayWidth: Int,
	val d: Int,
	val v: Int,
  val rwStages: Int,
	val numTokens: Int,
	val l: Int,
	val r: Int,
	val m: Int,
	val numScratchpads: Int,
	val numStagesAfterReduction: Int,
  val numMemoryUnits: Int,
  val inst: PlasticineConfig) extends Module with DirectionOps {

  val numRows = 4
  val numCols = 4
  val burstSizeBytes = 64

  val io = new ConfigInterface {
    /* Configuration interface */
    val config_enable = Bool(INPUT)

    /* Register interface */
    val addr = UInt(INPUT, width=w)
    val wdata = UInt(INPUT, width=w)
    val wen = Bool(INPUT)
    val dataVld = Bool(INPUT)
    val rdata = UInt(OUTPUT, width=w)
  }
}

class PlasticineTopSim(
  override val w: Int,
	override val startDelayWidth: Int,
	override val endDelayWidth: Int,
	override val d: Int,
	override val v: Int,
  override val rwStages: Int,
	override val numTokens: Int,
	override val l: Int,
	override val r: Int,
	override val m: Int,
	override val numScratchpads: Int,
	override val numStagesAfterReduction: Int,
  override val numMemoryUnits: Int,
  override val inst: PlasticineConfig) extends AbstractPlasticineTop (w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst) {
    this.name = "PlasticineTop"
}

class PlasticineTop(
  override val w: Int,
	override val startDelayWidth: Int,
	override val endDelayWidth: Int,
	override val d: Int,
	override val v: Int,
  override val rwStages: Int,
	override val numTokens: Int,
	override val l: Int,
	override val r: Int,
	override val m: Int,
	override val numScratchpads: Int,
	override val numStagesAfterReduction: Int,
  override val numMemoryUnits: Int,
  override val inst: PlasticineConfig) extends AbstractPlasticineTop (w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst) {

  def genDRAMSims = {
    List.tabulate(numMemoryUnits) { j =>
      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))

      DRAMSimulator
    }
  }

	val sims = genDRAMSims
  val chnRanksIdBits = Vec(UInt(0), UInt(4), UInt(8), UInt(12)) { UInt(width=4) }
	val pl = Module(new Plasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst))
	for (id <- 0 to numMemoryUnits - 1) {
    sims(id).io.addr := Cat(chnRanksIdBits(id), pl.io.dramChannel(id).addr)
    sims(id).io.wdata := pl.io.dramChannel(id).wdata
    sims(id).io.tagIn := pl.io.dramChannel(id).tagOut
    sims(id).io.vldIn := pl.io.dramChannel(id).vldOut
    sims(id).io.isWr := pl.io.dramChannel(id).isWr
    // need to assign the signal ready...

    pl.io.dramChannel(id).rdata := sims(id).io.rdata
    pl.io.dramChannel(id).vldIn := sims(id).io.vldOut
    pl.io.dramChannel(id).tagIn := sims(id).io.tagOut
	}
}

class PlasticineTopTests(c: AbstractPlasticineTop) extends PlasticineTester(c) {
  val config_enable = 1
  val w = 32
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (w / 8)
  val addr1 = 0x1000
//  val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }  
  val wdata = 0xcafe

  step(1)
  poke(c.io.dataVld, 1)
  poke(c.io.wen, 1)
  poke(c.io.wdata, wdata)

  step(0)
  poke(c.io.dataVld, 0)
  poke(c.io.wen, 0)
  poke(c.io.wdata, 0x0000)

  step(100)

//  for (s <- 0 until 99) {
//    val vldSig = peek(c.asInstanceOf[PlasticineTop].pl.io.dramChannel(2).vldOut).toInt
//    if (vldSig > 0) {
//      println(">>>>>>>>>> vld signal at channel 2")
//    }
//
//    step(1)
//  }
}

object PlasticineTopTest {
  val bitwidth = 32
  val startDelayWidth = 4
  val endDelayWidth = 4
  val d = 10
  val v = 16
  val l = 0
  val r = 16
  val rwStages = 3
  val numTokens = 8
  val m = 64
  val numScratchpads = 4
  val numStagesAfterReduction = 2
  val rows = 4
  val cols = 4
  val numMemoryUnits = 4
  val configObj = PlasticineConfig.getReadNoScatterGatherConfig(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads, numMemoryUnits)

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
    val testMode = args.contains("--test")
    if (testMode) {
      println("In test mode")
      chiselMainTest(args, () => Module(new PlasticineTopSim(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, configObj)).asInstanceOf[AbstractPlasticineTop]) {
        c => { new PlasticineTopTests(c) }
      }
    } else {
      chiselMainTest(args, () => Module(new PlasticineTop(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, configObj)).asInstanceOf[AbstractPlasticineTop]) {
        c => { new PlasticineTopTests(c) }
      }
    }
  }
}
