package plasticine.templates

import plasticine.misc.Utils
import Chisel._

import plasticine.pisa.ir._

/**
 * CUControlBox config register format
 */
case class CUControlBoxOpcode(val w: Int, val numTokens: Int, config: Option[CUControlBoxConfig] = None) extends OpcodeT {

  val udcInit = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      UInt(config.get.udcInit(i), width=4) // TODO: Remove hardcoded '4' !
    } else {
      UInt(width=4)
    }
  }

  override def cloneType(): this.type = {
    new CUControlBoxOpcode(w, numTokens, config).asInstanceOf[this.type]
  }
}


/**
 * Compute Unit Control Module. Handles incoming tokens, done signals,
 * and outgoing tokens.
 */
class CUControlBox(val w: Int, val numTokens: Int, inst: CUControlBoxConfig) extends ConfigurableModule[CUControlBoxOpcode] {
  val udctrWidth = 4
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    /* Input tokens */
   val tokenIns = Vec.fill(numTokens) { Bool(INPUT)}
    /* Input local 'done' signals */
   val done = Vec.fill(numTokens) { Bool(INPUT)}
    /* Output tokens */
   val tokenOuts = Vec.fill(numTokens) { Bool(OUTPUT)}
    /* Output 'enable' signals */
   val enable = Vec.fill(numTokens) { Bool(OUTPUT)}
  }
  val configType = CUControlBoxOpcode(w, numTokens)
  val configIn = CUControlBoxOpcode(w, numTokens)
  val configInit = CUControlBoxOpcode(w, numTokens, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  // Token out LUTs
  val tokenOutLUTs = List.tabulate(numTokens) { i =>
    val lut = Module(new LUT(1, 1 << io.done.size, inst.tokenOutLUT(i)))
    lut.io.ins := io.done.reverse  // MSB must be in position 0
    io.tokenOuts(i) := lut.io.out
  }
  // Decrement signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val decXbar = Module(new Crossbar(1, numTokens+1, numTokens, inst.decXbar))
  decXbar.io.config_enable := io.config_enable
  decXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.done(i-1) }
  val decs = decXbar.io.outs

  // Increment signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val incXbar = Module(new Crossbar(1, numTokens+1, 2*numTokens, inst.incXbar))
  incXbar.io.config_enable := io.config_enable
  incXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.tokenIns(i-1) }
  val incs = incXbar.io.outs.take(numTokens)
  val inits = incXbar.io.outs.takeRight(numTokens)

  // Up-down counters to handle tokens and credits
  val udCounters = List.tabulate(numTokens) { i =>
    val udc = Module(new UpDownCtr(udctrWidth))
    udc.io.initval := config.udcInit(i)
    udc.io.init := inits(i)
    udc.io.inc := incs(i)
    udc.io.dec := decs(i)
    udc
  }

  val gtzs = Vec.tabulate(numTokens) { i => udCounters(i).io.gtz }

  val enableLUTs = List.tabulate(numTokens) { i =>
    val lut = Module(new LUT(1, 1 << gtzs.size, inst.enableLUT(i)))
    lut.io.ins := gtzs.reverse // MSB must be in position 0
    io.enable(i) := lut.io.out
  }
}

class CUControlBoxTests(c: CUControlBox) extends PlasticineTester(c) {

}

object CUControlBoxTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl CUControlBoxTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[CUControlBoxConfig]]


    val wordSize = 1
    val numTokens = 3

    chiselMainTest(chiselArgs, () => Module(new CUControlBox(wordSize, numTokens, configObj.config))) {
      c => new CUControlBoxTests(c)
    }
  }
}
