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

  // TokenDown counter: Used to handle route-through tokens from parents to children,
  // as well as barrier cases where more than one token must be received before sending
  // a token out
  val syncTokenMux = Module(new MuxN(numTokens, w))
  syncTokenMux.io.ins := io.tokenIns
  syncTokenMux.io.sel := config.syncTokenMux
  val syncToken = syncTokenMux.io.out
  val tokenDownLUT = Module(new LUT(1, 1 << (numTokens+1), inst.tokenDownLUT))
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
  val enableMux = List.tabulate(numTokens) { i => Mux(config.enableMux(i), io.tokenIns(i), enableLUTs(i).io.out) }
  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }

  // Token out Mux
  val tokenOutMux = List.tabulate(numTokens) { i =>
    Mux(config.tokenOutMux(i),
        enableLUTs(i).io.out,
        if (i == 0) pulser.io.out else tokenOutLUTs(i-1).io.out)
  }

  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }
  io.tokenOuts.zip(tokenOutMux) foreach { case (tout, mux) => tout := mux }
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
