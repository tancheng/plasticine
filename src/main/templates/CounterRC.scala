package plasticine.templates

import Chisel._

import scala.collection.mutable.HashMap

/**
 * Counter config register format
 */
case class CounterOpcode(val w: Int) extends OpcodeT {
  var max = UInt(width = w)
  var stride = UInt(width = w)
  var maxConst = Bool()
  var strideConst = Bool()

  override def cloneType(): this.type = {
    new CounterOpcode(w).asInstanceOf[this.type]
  }

  def init(inst: CounterRCConfig) {
    max = max.fromInt(inst.max)
    stride = stride.fromInt(inst.stride)
    maxConst = maxConst.fromInt(inst.maxConst)
    strideConst = strideConst.fromInt(inst.strideConst)
  }
}

/**
 * Parsed config information for a single counter
 */
class CounterRCConfig(config: HashMap[String, Any]) {
  /** Counter max */
  private var _max: Int = 0
  def max() = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = 0
  def stride() = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = 0
  def maxConst() = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = 0
  def strideConst() = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  // Construct the class here
  _max = config("max").asInstanceOf[Int]
  _stride = config("stride").asInstanceOf[Int]
  _maxConst = config("maxConst").asInstanceOf[Int]
  _strideConst = config("strideConst").asInstanceOf[Int]
}

/**
 * CounterRC: Wrapper around counter module with reconfig muxes.
 * @param w: Word width
 */
class CounterRC(val w: Int, inst: CounterRCConfig = null) extends ConfigurableModule[CounterOpcode] {
  val io = new ConfigInterface {
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

  val configWires = CounterOpcode(w)
  configWires.init(inst)
  val config = RegInit(configWires)

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
