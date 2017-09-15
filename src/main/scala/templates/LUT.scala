package templates

import plasticine.pisa.parser.Parser
import plasticine.misc.Utils
import templates.Utils.log2Up

import chisel3._

import plasticine.pisa.ir._

/**
 * LUT config register format
 */
//case class LUTOpcode(val w: Int, val size: Int, config: Option[LUTConfig] = None) extends OpcodeT {
//
//  val table = Vec.tabulate(size) { i =>
//    if (config.isDefined) {
//      UInt(config.get.table(i), width=w) // TODO: Remove hardcoded '4' !
//    } else {
//      UInt(width=w)
//    }
//  }
//
//  override def cloneType(): this.type = {
//    new LUTOpcode(w, size, config).asInstanceOf[this.type]
//  }
//}
//
//object LUT {
//
//  // Given n inputs, give all permutations.
//  // E.g: getInputs(4) = List(
//  //                        List(0, 0)
//  //                        List(0, 1)
//  //                        List(1, 0)
//  //                        List(1, 1)
//  //                    )
//  def getInputs(n: Int) = {
//    val size = 1 << n
//    List.tabulate(size)  { i =>
//      List.tabulate(n) { ii =>
//        (i & (1 << ii)) >> ii
//      }.reverse
//    }
//  }
//
//  def getConfig(size: Int, f: List[Int] => Int): List[Int] = {
//    val inputs = getInputs(size).toList
//    inputs.map { i =>
//      f(i)
//    }
//  }
//}
//
///**
// * Combinational Look-up table using Chisel Vec. If the Vec is only read in the design,
// * Chisel automatically converts them to ROM types.
// * https://chisel.eecs.berkeley.edu/2.2.0/chisel-tutorial.pdf
// */
//class LUT(val w: Int, val size: Int, val inst: LUTConfig) extends ConfigurableModule[LUTOpcode]  {
//
//  if (!isPow2(size)) {
//    throw new Exception(s"Invalid LUT size $size: LUT sizes must be powers of two")
//  }
//  val numSelectBits = log2Up(size)
//  val io = new ConfigInterface {
//    val config_enable = Bool(INPUT)
//    val ins = Vec.fill(numSelectBits) { UInt(INPUT, width=1) }
//    val out = Bits(OUTPUT, width = w)
//  }
//
//  val configType = LUTOpcode(w, size)
//  val configIn = LUTOpcode(w, size)
//  val configInit = LUTOpcode(w, size, Some(inst))
//  val config = Reg(configType, configIn, configInit)
//  when (io.config_enable) {
//    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
//  } .otherwise {
//    configIn := config
//  }
//
//  // NOTE: io.ins is a scala collection where MSB is in position 0
//  // Users of LUTs must ensure that MSB of inputs must be in position 0
//  val sel = io.ins.reduce{Cat(_,_)}
//  val lut = config.table
//  io.out := lut(sel)
//}
//
//class LUTTests(c: LUT) extends PlasticineTester(c) {
//    def getBitsFor(num: Int, nbits: Int) = {
//      Array.tabulate(nbits) { i =>
//        BigInt((num & (1 << nbits-i-1)) >> (nbits-i-1))
//      }
//    }
//
//    (0 until c.size) foreach { in =>
//      val inbits = getBitsFor(in, c.numSelectBits)
//      poke(c.io.ins, getBitsFor(in, c.numSelectBits))
//      val out = c.inst.table(in)
//      expect(c.io.out, out) // Combinational LUT
//      step(1)
//    }
//}
//
//object LUTTest {
//  def main(args: Array[String]): Unit = {
//
//   val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
//
//    if (appArgs.size != 1) {
//      println("Usage: bin/sadl LUTTest <pisa config>")
//      sys.exit(-1)
//    }
//
//    val pisaFile = appArgs(0)
//    val configObj = Parser(pisaFile).asInstanceOf[LUTConfig]
//
//    val wordSize = 1
//    val size = 8
//
//    chiselMainTest(args, () => Module(new LUT(wordSize, size, configObj))) {
//      c => new LUTTests(c)
//    }
//  }
//}

