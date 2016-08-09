package plasticine.templates

import Chisel._

import plasticine.pisa.parser._
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

/**
 * Counter config register format
 */
case class CounterOpcode(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, config: Option[CounterRCConfig] = None) extends OpcodeT {
  var max = if (config.isDefined) UInt(config.get.max, width=w) else UInt(width = w)
  var stride = if (config.isDefined) UInt(config.get.stride, width=w) else UInt(width = w)
  var maxConst = if (config.isDefined) Bool(config.get.maxConst > 0) else Bool()
  var strideConst = if (config.isDefined) Bool(config.get.strideConst > 0) else Bool()
  var startDelay = if (config.isDefined) UInt(config.get.startDelay, width=startDelayWidth) else UInt(width = startDelayWidth)
  var endDelay = if (config.isDefined) UInt(config.get.endDelay, width=endDelayWidth) else UInt(width = endDelayWidth)

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
    configIn := configType
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

object CounterRCTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl CounterRCTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[CounterRCConfig]]
    val bitwidth = 7
    val startDelayWidth = 4
    val endDelayWidth = 4

    // Configuration passed to design as register initial values
    // When the design is reset, config is set
    println(s"parsed configObj: $configObj")

    chiselMainTest(chiselArgs, () => Module(new CounterRC(bitwidth, startDelayWidth, endDelayWidth, configObj.config))) {
      c => new CounterRCTests(c)
    }
  }
}
