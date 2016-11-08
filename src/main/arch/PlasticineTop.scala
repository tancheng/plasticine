package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

// import scala.collection.mutable.HashMap
// import scala.collection.mutable.Set
// import scala.collection.mutable.ListBuffer

class PlasticineTop(val w: Int,
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

  val numMemoryUnits = 4
  val burstSizeBytes = 64
  val rows = 4
  val cols = 4
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

	// TODO: need to parameterize Plasticine for 4 mus
  def genDRAMSims = {
    List.tabulate(numMemoryUnits) { j =>
      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))

      DRAMSimulator
    }
  }

	val sims = genDRAMSims
	val pl = Module(new Plasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, inst))
	for (id <- 0 to numMemoryUnits - 1) {
    sims(id).io.addr := pl.io.dramChannel(id).addr
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

class PlasticineTopTests(c: PlasticineTop) extends PlasticineTester(c) {

}

object PlasticineTopTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
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
    val configObj = PlasticineConfig.getRandom(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads)
    chiselMainTest(chiselArgs, () => Module(new PlasticineTop(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, configObj))) {
      c => new PlasticineTopTests(c)
    }
  }
}
