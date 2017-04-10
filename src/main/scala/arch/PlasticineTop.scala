//package plasticine.templates
//
//import Chisel._
//import plasticine.ArchConfig
//import plasticine.pisa.parser.Parser
//import plasticine.pisa.ir._
//
//class PlasticineTop(
//  val w: Int,
//	val startDelayWidth: Int,
//	val endDelayWidth: Int,
//	val d: Int,
//	val v: Int,
//  val rwStages: Int,
//	val numTokens: Int,
//	val l: Int,
//	val r: Int,
//	val m: Int,
//	val numScratchpads: Int,
//	val numStagesAfterReduction: Int,
//  val numMemoryUnits: Int,
//	val numRows: Int,
//	val numCols: Int,
//  val numScalarIO: Int,
//  val numScalarRegisters: Int,
//  val inst: PlasticineConfig) extends Module with DirectionOps {
//
//  val burstSizeBytes = 64
//
//  val io = new ConfigInterface {
//    /* Configuration interface */
//    val config_enable = Bool(INPUT)
//
//    /* Register interface */
//    val addr = UInt(INPUT, width=w)
//    val wdata = UInt(INPUT, width=w)
//    val wen = Bool(INPUT)
//    val dataVld = Bool(INPUT)
//    val rdata = UInt(OUTPUT, width=w)
//  }
//
//  def genDRAMSims = {
//    List.tabulate(numMemoryUnits) { j =>
//      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))
//
//      DRAMSimulator
//    }
//  }
//
//	val sims = genDRAMSims
//  val chnRanksIdBits = Vec(UInt(0), UInt(4), UInt(8), UInt(12)) { UInt(width=4) }
//	val pl = Module(new Plasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, numRows, numCols, numScalarIO, numScalarRegisters, inst))
//
//	for (id <- 0 to numMemoryUnits - 1) {
//    sims(id).io.addr := Cat(chnRanksIdBits(id), pl.io.dramChannel(id).addr)
//    sims(id).io.wdata := pl.io.dramChannel(id).wdata
//    sims(id).io.tagIn := pl.io.dramChannel(id).tagOut
//    sims(id).io.vldIn := pl.io.dramChannel(id).vldOut
//    sims(id).io.isWr := pl.io.dramChannel(id).isWr
//
//    pl.io.dramChannel(id).rdata := sims(id).io.rdata
//    pl.io.dramChannel(id).vldIn := sims(id).io.vldOut
//    pl.io.dramChannel(id).tagIn := sims(id).io.tagOut
//	}
//}
//
//class PlasticineTopTests(c: PlasticineTop) extends PlasticineTester(c) {
//  val config_enable = 1
//  val w = 32
//  val size = 64
//  val burstSizeBytes = 64
//  val wordsPerBurst = burstSizeBytes / (w / 8)
//  val addr1 = 0x1000
//  val wdata = 0xcafe
//
////  step(1)
////  poke(c.io.dataVld, 1)
////  poke(c.io.wen, 1)
////  poke(c.io.wdata, wdata)
////
////  step(0)
////  poke(c.io.dataVld, 0)
////  poke(c.io.wen, 0)
////  poke(c.io.wdata, 0x0000)
////
////
////  for (s <- 0 until 99) {
////    val vldSig = peek(c.pl.io.dramChannel(2).vldOut).toInt
////    if (vldSig > 0) {
////      println(">>>>>>>>>> vld signal at channel 2")
////    }
////
////    step(1)
////  }
//}
//
//object PlasticineTopTest {
//  def main(args: Array[String]): Unit = {
//
//    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
//
//    if (appArgs.size != 2) {
//      println("Usage: bin/sadl PlasticineTopTest <spade config> <pisa config>")
//      sys.exit(-1)
//    }
//
//    val spadeFile = appArgs(0)
//    val pisaFile = appArgs(1)
//
//    ArchConfig.setConfig(spadeFile)
//    val config =  PlasticineConfig.zeroes(
//        ArchConfig.d,
//        ArchConfig.numRows,
//        ArchConfig.numCols,
//        ArchConfig.numTokens,
//        ArchConfig.numTokens,
//        ArchConfig.numTokens,
//        ArchConfig.numScratchpads,
//        ArchConfig.numMemoryUnits
//      ) // Parser(pisaFile).asInstanceOf[PlasticineConfig]
////    val config = PlasticineConfig.getRandom(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads, numMemoryUnits)
//
//    val bitwidth = ArchConfig.w
//    val startDelayWidth = ArchConfig.startDelayWidth
//    val endDelayWidth = ArchConfig.endDelayWidth
//    val d = ArchConfig.d
//    val v = ArchConfig.v
//    val l = ArchConfig.l
//    val r = ArchConfig.r
//    val rwStages = ArchConfig.rwStages
//    val numTokens = ArchConfig.numTokens
//    val m = ArchConfig.m
//    val numScratchpads = ArchConfig.numScratchpads
//    val numStagesAfterReduction = ArchConfig.numStagesAfterReduction
//    val numRows = ArchConfig.numRows
//    val numCols = ArchConfig.numCols
//    val numMemoryUnits = ArchConfig.numMemoryUnits
//    val numScalarIO = ArchConfig.numScalarIO
//    val numScalarRegisters = ArchConfig.numScalarRegisters
//
//
//    chiselMainTest(chiselArgs, () => Module(new PlasticineTop(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, numRows, numCols, numScalarIO, numScalarRegisters, config)).asInstanceOf[AbstractPlasticine]) {
//        c => new PlasticineTests(c)
//    }
//  }
//}
