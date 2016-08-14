package plasticine.templates

import Chisel._

import plasticine.pisa.ir._
import scala.collection.mutable.HashMap

/**
 * CounterChain config register format
 */
case class CounterChainOpcode(val w: Int, val numCounters: Int, config: Option[CounterChainConfig] = None) extends OpcodeT {
  var chain = if (config.isDefined) {
    Vec.tabulate(numCounters-1) { i => Bool(config.get.chain(i) > 0) }
  } else {
    Vec.fill(numCounters-1) { Bool() }
  }

  override def cloneType(): this.type = {
    new CounterChainOpcode(w, numCounters).asInstanceOf[this.type]
  }
}

/**
 * CounterChain: Chain of perfectly nested counters.
 * @param w: Word width
 * @param startDelayWidth: Width of start delay counters
 * @param endDelayWidth: Width of end delay counters
 * @param numCounters: Number of counters
 */
class CounterChain(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val numCounters: Int, inst: CounterChainConfig) extends ConfigurableModule[CounterChainOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = Vec.fill(numCounters) { new Bundle {
        val max      = UInt(INPUT,  w)
        val stride   = UInt(INPUT,  w)
        val out      = UInt(OUTPUT, w)
      }
    }
    val control = Vec.fill(numCounters) { new Bundle {
        val enable = Bool(INPUT)
        val done   = Bool(OUTPUT)
      }
    }
  }

  val configType = CounterChainOpcode(w, numCounters)
  val configIn = CounterChainOpcode(w, numCounters)
  val configInit = CounterChainOpcode(w, numCounters, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, reset))
  } .otherwise {
    configIn := config
  }


  val counterInsn = inst.counters
  val counters = (0 until numCounters) map { i =>
    val c = Module(new CounterRC(w, startDelayWidth, endDelayWidth, counterInsn(i)))
    c.io.data.max := io.data(i).max
    c.io.data.stride := io.data(i).stride
    io.data(i).out := c.io.data.out
    c
  }

  // Create chain reconfiguration logic
  (0 until numCounters) foreach { i: Int =>
    // Enable-done chain
    if (i == 0) {
      counters(i).io.control.enable := io.control(i).enable
    } else {
      counters(i).io.control.enable := Mux(config.chain(i-1),
        io.control(i).enable & counters(i-1).io.control.done,
        io.control(i).enable)
    }

    // waitIn - waitOut chain
    if (i == numCounters-1) {
      counters(i).io.control.waitIn := Bool(false)
    } else {
      counters(i).io.control.waitIn := Mux(config.chain(i),
                                      counters(i+1).io.control.waitOut,
                                      Bool(false))
    }
    io.control(i).done := counters(i).io.control.done
  }
}

/**
 * CounterChain test harness
 */
class CounterChainTests(c: CounterChain) extends PlasticineTester(c) {
  val saturationVal = (1 << c.w) - 1  // Based on counter width

  val max = 10 % saturationVal
  val stride = 2
  val saturate = 0

  (0 until c.numCounters) map { i =>
    poke(c.io.data(i).max, max)
    poke(c.io.data(i).stride, stride)
    poke(c.io.control(i).enable, 1)
  }

  val counts = peek(c.io.data)
  val done = peek(c.io.control)
  for (i <- 0 until 50) {
    step(1)
    c.io.data foreach { d => peek(d.out) }
    val done = peek(c.io.control)
  }
}

object CounterChainTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl CounterChainTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[CounterChainConfig]]
    val bitwidth = 8
    val numCounters = 4
    val startDelayWidth = 4
    val endDelayWidth = 4

    chiselMainTest(args, () => Module(new CounterChain(bitwidth, startDelayWidth, endDelayWidth, numCounters, configObj.config))) {
      c => new CounterChainTests(c)
    }
  }
}
