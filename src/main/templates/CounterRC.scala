package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

/**
 * Counter config register format
 */
case class CounterOpcode(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, config: Option[CounterRCConfig] = None) extends OpcodeT {
  var max = if (config.isDefined) UInt(config.get.max, width=w) else UInt(width = w )
  var stride = if (config.isDefined) UInt(config.get.stride, width=w) else UInt(width = w)
  var maxConst = if (config.isDefined) Bool(config.get.maxConst > 0) else Bool()
  var strideConst = if (config.isDefined) Bool(config.get.strideConst > 0) else Bool()
  var startDelay = if (config.isDefined) UInt(config.get.startDelay % (1 << startDelayWidth), width=startDelayWidth) else UInt(width = startDelayWidth)
  var endDelay = if (config.isDefined) UInt(config.get.endDelay % (1 << endDelayWidth), width=endDelayWidth) else UInt(width = endDelayWidth)

  override def cloneType(): this.type = {
    new CounterOpcode(w, startDelayWidth, endDelayWidth, config).asInstanceOf[this.type]
  }
}

/**
 * CounterRC: Wrapper around counter module with reconfig muxes.
 * @param w: Word width
 * @param startDelayWidth: Width of start delay counter
 * @param endDelayWidth: Width of end delay counter
 */
class CounterRC(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, inst: CounterRCConfig) extends ConfigurableModule[CounterOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = new Bundle {
      val max      = UInt(INPUT,  w)
      val stride   = UInt(INPUT,  w)
      val out      = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable    = Bool(INPUT)
      val waitIn    = Bool(INPUT)
      val waitOut    = Bool(OUTPUT)
      val done   = Bool(OUTPUT)
    }
  }

  val configType = CounterOpcode(w, startDelayWidth, endDelayWidth)
  val configIn = CounterOpcode(w, startDelayWidth, endDelayWidth)
  val configInit = CounterOpcode(w, startDelayWidth, endDelayWidth, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  // Start delay counter
  val startDelayCounter = Module(new Counter(startDelayWidth))
  startDelayCounter.io.data.max := config.startDelay
  startDelayCounter.io.data.stride := UInt(1, width=startDelayWidth)
  startDelayCounter.io.control.saturate := Bool(true)

  // Actual counter
  val counter = Module(new Counter(w))
  counter.io.data.max := Mux(config.maxConst, config.max, io.data.max)
  counter.io.data.stride := Mux(config.strideConst, config.stride, io.data.stride)
  io.data.out := counter.io.data.out

  // End delay counter
  val endDelayCounter = Module(new Counter(endDelayWidth))
  endDelayCounter.io.data.max := config.endDelay
  endDelayCounter.io.data.stride := UInt(1, width=endDelayWidth)
  counter.io.control.reset := Bool(false)
  endDelayCounter.io.control.saturate := Bool(false)

  // Control signal wiring up
  val localEnable = io.control.enable & ~io.control.waitIn
  startDelayCounter.io.control.enable := localEnable
  startDelayCounter.io.control.reset := counter.io.control.done
  val depulser = Module(new Depulser())
  depulser.io.in := counter.io.control.done
  endDelayCounter.io.control.enable := depulser.io.out | counter.io.control.done
  depulser.io.rst := endDelayCounter.io.control.done
  counter.io.control.enable := startDelayCounter.io.control.done & ~depulser.io.out
  counter.io.control.saturate := endDelayCounter.io.control.enable
  counter.io.control.reset := endDelayCounter.io.control.done
  io.control.done := endDelayCounter.io.control.done
//  io.control.waitOut := depulser.io.out | counter.io.control.done | endDelayCounter.io.control.enable
  io.control.waitOut := io.control.waitIn | depulser.io.out
}

class CounterRCReg(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, inst: CounterRCConfig) extends Module {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = new Bundle {
      val max      = UInt(INPUT,  w)
      val stride   = UInt(INPUT,  w)
      val out      = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val enable    = Bool(INPUT)
      val waitIn    = Bool(INPUT)
      val waitOut    = Bool(OUTPUT)
      val done   = Bool(OUTPUT)
    }
  }

  // Register the inputs
  val maxReg = Module(new FF(w))
  maxReg.io.control.enable := Bool(true)
  maxReg.io.data.in := io.data.max
  val max = maxReg.io.data.out

  val strideReg = Module(new FF(w))
  strideReg.io.control.enable := Bool(true)
  strideReg.io.data.in := io.data.stride
  val stride = strideReg.io.data.out

  val enableReg = Module(new FF(1))
  enableReg.io.control.enable := Bool(true)
  enableReg.io.data.in := io.control.enable
  val enable = enableReg.io.data.out

  val waitInReg = Module(new FF(1))
  waitInReg.io.control.enable := Bool(true)
  waitInReg.io.data.in := io.control.waitIn
  val waitIn = waitInReg.io.data.out

  // Instantiate counter
  val counter = Module(new CounterRC(w, startDelayWidth, endDelayWidth, inst))
  counter.io.config_enable := io.config_enable
  counter.io.config_data := io.config_data
  counter.io.data.max := max
  counter.io.data.stride := stride
  counter.io.control.enable := enable
  counter.io.control.waitIn := waitIn

  // Register outputs
  val outReg = Module(new FF(w))
  outReg.io.control.enable := Bool(true)
  outReg.io.data.in := counter.io.data.out
  io.data.out := outReg.io.data.out
  val waitOutReg = Module(new FF(1))
  waitOutReg.io.control.enable := Bool(true)
  waitOutReg.io.data.in := counter.io.control.waitOut
  io.control.waitOut := waitOutReg.io.data.out
  val doneReg = Module(new FF(1))
  doneReg.io.control.enable := Bool(true)
  doneReg.io.data.in := counter.io.control.done
  io.control.done := doneReg.io.data.out
}

/**
 * CounterRC test harness
 */
class CounterRCTests(c: CounterRC) extends PlasticineTester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 2

  poke(c.io.data.max, max)
  poke(c.io.data.stride, stride)

  poke(c.io.control.enable, 1)
  val count = peek(c.io.data.out)
  val done = peek(c.io.control.done)
  for (i <- 0 until 50) {
    step(1)
    val count = peek(c.io.data.out)
    val done = peek(c.io.control.done)
  }
}

class CounterRCCharTests(c: CounterRCReg) extends Tester(c)

object CounterRCTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl CounterRCTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Parser(pisaFile).asInstanceOf[CounterRCConfig]
    val bitwidth = 7
    val startDelayWidth = 4
    val endDelayWidth = 4

    // Configuration passed to design as register initial values
    // When the design is reset, config is set
    println(s"parsed configObj: $configObj")

    chiselMainTest(chiselArgs, () => Module(new CounterRC(bitwidth, startDelayWidth, endDelayWidth, configObj))) {
      c => new CounterRCTests(c)
    }
  }
}

object CounterRCChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 1) {
      println("Usage: CounterRCChar <w>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val startDelayWidth = 4
    val endDelayWidth = 4
    val config = CounterRCConfig.getRandom
    chiselMainTest(args, () => Module(new CounterRCReg(w, startDelayWidth, endDelayWidth, config))) {
      c => new CounterRCCharTests(c)
    }
  }
}

