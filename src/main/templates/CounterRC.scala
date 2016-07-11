package plasticine.templates

import Chisel._

import scala.collection.mutable.HashMap

/**
 * Counter opcode format
 */
case class CounterOpcode(val w: Int) extends OpcodeT {
  val max = UInt(INPUT, width = w)
  val stride = UInt(INPUT, width = w)
  val maxConst = Bool(INPUT)
  val strideConst = Bool(INPUT)

  override def cloneType(): this.type = {
    new CounterOpcode(w).asInstanceOf[this.type]
  }
}

/**
 * CounterRC: Wrapper around counter module with reconfig muxes.
 * @param w: Word width
 */
class CounterRC(val w: Int) extends ConfigurableModule[CounterOpcode] {
  val io = new ConfigInterface(CounterOpcode(w)) {
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

  val config = RegInit(io.opcode)

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

  // Set configuration
//  poke(c.io.bits.max, 96)
//  poke(c.io.bits.stride, 1)
//  poke(c.io.bits.maxConst, 1)
//  poke(c.io.bits.strideConst, 0)

//  poke(c.io.opcode, Array[BigInt](1, 1, 1, 96))
  val inst = HashMap[String, Int]( "max" -> 16, "stride" -> 4, "maxConst" -> 0, "strideConst" -> 1)
  set(c.io.opcode, inst)
  step(1)
  reset(10)

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

    chiselMainTest(args, () => Module(new CounterRC(bitwidth))) {
      c => new CounterRCTests(c)
    }
  }
}
