package plasticine.templates

import Chisel._

import plasticine.pisa.ir._
import scala.collection.mutable.HashMap

/**
 * Counter config register format
 */
case class CounterOpcode(val w: Int, config: Option[CounterRCConfig] = None) extends OpcodeT {
  var max = if (config.isDefined) UInt(config.get.max, width=w) else UInt(width = w)
  var stride = if (config.isDefined) UInt(config.get.stride, width=w) else UInt(width = w)
  var maxConst = if (config.isDefined) Bool(config.get.maxConst > 0) else Bool()
  var strideConst = if (config.isDefined) Bool(config.get.strideConst > 0) else Bool()

  override def cloneType(): this.type = {
    new CounterOpcode(w, config).asInstanceOf[this.type]
  }
}

/**
 * CounterRC: Wrapper around counter module with reconfig muxes.
 * @param w: Word width
 */
class CounterRC(val w: Int, inst: CounterRCConfig) extends ConfigurableModule[CounterOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = new Bundle {
      val max      = UInt(INPUT,  w)
      val stride   = UInt(INPUT,  w)
      val out      = UInt(OUTPUT, w)
    }
    val control = new Bundle {
      val reset  = Bool(INPUT)
      val enable = Bool(INPUT)
      val saturate = Bool(INPUT)
      val done   = Bool(OUTPUT)
    }
  }

  val configType = CounterOpcode(w)
  val configIn = CounterOpcode(w)
  val configInit = CounterOpcode(w, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  val counter = Module(new Counter(w))

  counter.io.data.max := Mux(config.maxConst, config.max, io.data.max)
  counter.io.data.stride := Mux(config.strideConst, config.stride, io.data.stride)
  io.data.out := counter.io.data.out

  counter.io.control.reset := io.control.reset
  counter.io.control.enable := io.control.enable
  counter.io.control.saturate := io.control.saturate
  io.control.done := counter.io.control.done
}

/**
 * CounterRC test harness
 */
class CounterRCTests(c: CounterRC) extends PlasticineTester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 2
  val saturate = 0

  poke(c.io.data.max, max)
  poke(c.io.data.stride, stride)

  poke(c.io.control.saturate, saturate)
  poke(c.io.control.reset, 0)
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

    val bitwidth = 7

    // Configuration passed to design as register initial values
    // When the design is reset, config is set
    val inst = HashMap[String, Any]( "max" -> 16, "stride" -> 3, "maxConst" -> 1, "strideConst" -> 0)
    val config = new CounterRCConfig(inst)

    chiselMainTest(args, () => Module(new CounterRC(bitwidth, config))) {
      c => new CounterRCTests(c)
    }
  }
}
