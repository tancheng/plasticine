package plasticine.templates
import plasticine.pisa.parser.Parser
import plasticine.misc.Utils
import Chisel._

import plasticine.pisa.ir._

/**
 * CUControlBox config register format
 */
case class CUControlBoxOpcode(val numTokens: Int, config: Option[CUControlBoxConfig] = None) extends OpcodeT {

  val udcInit = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      UInt(config.get.udcInit(i), width=4) // TODO: Remove hardcoded '4' !
    } else {
      UInt(width=4)
    }
  }

  val enableMux = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      Bool(config.get.enableMux(i))
    } else {
      Bool()
    }
  }

  val tokenOutMux = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      Bool(config.get.tokenOutMux(i))
    } else {
      Bool()
    }
  }

  val syncTokenMux = if (config.isDefined) UInt(config.get.syncTokenMux, width=log2Up(numTokens)) else UInt(width=log2Up(numTokens))
  override def cloneType(): this.type = {
    new CUControlBoxOpcode(numTokens, config).asInstanceOf[this.type]
  }
}


/**
 * Compute Unit Control Module. Handles incoming tokens, done signals,
 * and outgoing tokens.
 */
class CUControlBox(val numTokens: Int, inst: CUControlBoxConfig) extends ConfigurableModule[CUControlBoxOpcode] {
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
  val configType = CUControlBoxOpcode(numTokens)
  val configIn = CUControlBoxOpcode(numTokens)
  val configInit = CUControlBoxOpcode(numTokens, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  // Done crossbar - connects done signals from counter chains to LUTs
  val doneXbar = Module(new Crossbar(1, numTokens, 2*numTokens, inst.doneXbar))
  doneXbar.io.config_enable := io.config_enable
  doneXbar.io.config_data := io.config_data
  doneXbar.io.ins.zip(io.done.reverse).foreach { case (in, done) => in := done } // MSB must be in position 0

  // Token out LUTs
  val tokenOutLUTs = List.tabulate(numTokens-1) { i =>
    val lut = Module(new LUT(1, 1 << 2, inst.tokenOutLUT(i)))
    lut.io.config_enable := io.config_enable
    lut.io.config_data := io.config_data
    lut.io.ins := doneXbar.io.outs.drop(i*2).take(2)
    io.tokenOuts(i+1) := lut.io.out
    lut
  }

  // Decrement signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val decXbar = Module(new Crossbar(1, numTokens+1, numTokens, inst.decXbar))
  decXbar.io.config_enable := io.config_enable
  decXbar.io.config_data := io.config_data
  decXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.done(i-1) }
  val decs = decXbar.io.outs

  // Increment signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val incXbar = Module(new Crossbar(1, numTokens+1, 2*numTokens, inst.incXbar))
  incXbar.io.config_enable := io.config_enable
  incXbar.io.config_data := io.config_data
  incXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.tokenIns(i-1) }
  val incs = incXbar.io.outs.take(numTokens)
  val inits = incXbar.io.outs.takeRight(numTokens)

  // Token In Xbar: Used to route enable signals for duplicate counter's remote write signals
  val tokenInXbar = Module(new Crossbar(1, numTokens+1, numTokens, inst.tokenInXbar))
  tokenInXbar.io.config_enable := io.config_enable
  tokenInXbar.io.config_data := io.config_data
  tokenInXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.tokenIns(i-1) }

  // Up-down counters to handle tokens and credits
  val udCounters = List.tabulate(numTokens) { i =>
    val udc = Module(new UpDownCtr(udctrWidth))
    udc.io.initval := config.udcInit(i)
    udc.io.strideInc := UInt(1)
    udc.io.strideDec := UInt(1)
    udc.io.init := inits(i)
    udc.io.inc := incs(i)
    udc.io.dec := decs(i)
    udc
  }

  val gtzs = Vec.tabulate(numTokens) { i => udCounters(i).io.gtz }

  // TokenDown LUT: Used to handle route-through tokens from parents to children,
  // as well as barrier cases where more than one token must be received before sending
  // a token out
  val syncTokenMux = Module(new MuxN(numTokens, 1))
  syncTokenMux.io.ins := io.tokenIns
  syncTokenMux.io.sel := config.syncTokenMux
  val syncToken = syncTokenMux.io.out
  val tokenDownLUT = Module(new LUT(1, 1 << (numTokens+1), inst.tokenDownLUT(0)))
  tokenDownLUT.io.config_enable := io.config_enable
  tokenDownLUT.io.config_data := io.config_data
  tokenDownLUT.io.ins.zipWithIndex.foreach { case (in, i) =>
    if (i == 0) in := syncToken
    else in := gtzs(i-1)
  }
  val pulser = Module(new Pulser())
  pulser.io.in := tokenDownLUT.io.out

  // Enable Mux
  val enableLUTs = List.tabulate(numTokens) { i =>
    val m = Module(new LUT(1, 1 << gtzs.size, inst.enableLUT(i)))
    m.io.config_enable := io.config_enable
    m.io.config_data := io.config_data
    m
  }
  enableLUTs.foreach { _.io.ins := gtzs.reverse } // MSB must be in position 0
  val enableMux = List.tabulate(numTokens) { i => Mux(config.enableMux(i), tokenInXbar.io.outs(i), enableLUTs(i).io.out) }
  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }

  // Token out xbar
  val tokenOutXbar = Module(new Crossbar(1, 2*numTokens, numTokens, inst.tokenOutXbar))
  tokenOutXbar.io.config_enable := io.config_enable
  tokenOutXbar.io.config_data := io.config_data
  tokenOutXbar.io.ins := Vec.tabulate(2*numTokens) { i =>
    if (i == 0) pulser.io.out
    else if (i < numTokens) tokenOutLUTs(i-1).io.out
    else enableLUTs(i-numTokens).io.out
  }

  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }
  io.tokenOuts.zip(tokenOutXbar.io.outs) foreach { case (tout, out) => tout := out }
}

class CUControlBoxTests(c: CUControlBox) extends PlasticineTester(c) {

}

object CUControlBoxTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

//    if (appArgs.size != 1) {
//      println("Usage: bin/sadl CUControlBoxTest <pisa config>")
//      sys.exit(-1)
//    }

//    val pisaFile = appArgs(0)
//    val configObj = Parser(pisaFile).asInstanceOf[CUControlBoxConfig]
    val numTokens = 8
    val config = CUControlBoxConfig.getRandom(numTokens, numTokens, numTokens)


    chiselMainTest(chiselArgs, () => Module(new CUControlBox(numTokens, config))) {
      c => new CUControlBoxTests(c)
    }
  }
}
