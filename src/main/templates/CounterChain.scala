package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
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

class CounterChainCore(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val numCounters: Int, counterConfigs: List[CounterRCConfig], val harden: Boolean = false) extends Module {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val chain = Vec.fill(numCounters-1) { Bool(INPUT) }
    val data = Vec.fill(numCounters) { new Bundle {
        val max      = UInt(INPUT,  w)
        val stride   = UInt(INPUT,  w)
        val configuredMax = UInt(OUTPUT,  w)
        val out      = UInt(OUTPUT, w)
        val next     = UInt(OUTPUT, w)
      }
    }
    val control = Vec.fill(numCounters) { new Bundle {
        val enable = Bool(INPUT)
        val enableWithDelay = Bool(OUTPUT)
        val done   = Bool(OUTPUT)
      }
    }
  }

  val counterInsn = counterConfigs
  val startWidth = if (harden) 0 else startDelayWidth
  val endWidth = if (harden) 0 else endDelayWidth
  val counters = (0 until numCounters) map { i =>
    val c = Module(new CounterRC(w, startWidth, endWidth, counterInsn(i), harden))
    c.io.config_enable := io.config_enable
    c.io.config_data := io.config_data
    c.io.data.max := io.data(i).max
    io.data(i).configuredMax := c.io.data.configuredMax
    c.io.data.stride := io.data(i).stride
    io.data(i).out := c.io.data.out
    io.data(i).next := c.io.data.next
    c
  }

  // Create chain reconfiguration logic
  (0 until numCounters) foreach { i: Int =>
    // Enable-done chain
    if (i == 0) {
      counters(i).io.control.enable := io.control(i).enable
    } else {
      counters(i).io.control.enable := Mux(io.chain(i-1),
        counters(i-1).io.control.done,
        io.control(i).enable)
    }

    // waitIn - waitOut chain
    if (i == numCounters-1) {
      counters(i).io.control.waitIn := Bool(false)
    } else {
      counters(i).io.control.waitIn := Mux(io.chain(i),
                                      counters(i+1).io.control.waitOut,
                                      Bool(false))
    }
    io.control(i).done := counters(i).io.control.done
    io.control(i).enableWithDelay := counters(i).io.control.enableWithDelay
  }
}


/**
 * CounterChain: Chain of perfectly nested counters.
 * @param w: Word width
 * @param startDelayWidth: Width of start delay counters
 * @param endDelayWidth: Width of end delay counters
 * @param numCounters: Number of counters
 */
class CounterChain(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val numCounters: Int, inst: CounterChainConfig, val harden: Boolean = false) extends ConfigurableModule[CounterChainOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = Vec.fill(numCounters) { new Bundle {
        val max      = UInt(INPUT,  w)
        val stride   = UInt(INPUT,  w)
        val configuredMax = UInt(OUTPUT,  w)
        val out      = UInt(OUTPUT, w)
        val next     = UInt(OUTPUT, w)
      }
    }
    val control = Vec.fill(numCounters) { new Bundle {
        val enable = Bool(INPUT)
        val enableWithDelay = Bool(OUTPUT)
        val done   = Bool(OUTPUT)
      }
    }
  }

  val configType = CounterChainOpcode(w, numCounters)
  val configIn = CounterChainOpcode(w, numCounters)
  val configInit = CounterChainOpcode(w, numCounters, Some(inst))
  val config = if (harden) configInit else Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }


  val counterInsn = inst.counters
  val startWidth = if (harden) 0 else startDelayWidth
  val endWidth = if (harden) 0 else endDelayWidth
  val counters = (0 until numCounters) map { i =>
    val c = Module(new CounterRC(w, startWidth, endWidth, counterInsn(i), harden))
    c.io.config_enable := io.config_enable
    c.io.config_data := io.config_data
    c.io.data.max := io.data(i).max
    io.data(i).configuredMax := c.io.data.configuredMax
    c.io.data.stride := io.data(i).stride
    io.data(i).out := c.io.data.out
    io.data(i).next := c.io.data.next
    c
  }

  // Create chain reconfiguration logic
  (0 until numCounters) foreach { i: Int =>
    // Enable-done chain
    if (i == 0) {
      counters(i).io.control.enable := io.control(i).enable
    } else {
      counters(i).io.control.enable := Mux(config.chain(i-1),
        counters(i-1).io.control.done,
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
    io.control(i).enableWithDelay := counters(i).io.control.enableWithDelay
  }
}

class CounterChainReg(val w: Int, val startDelayWidth: Int, val endDelayWidth: Int, val numCounters: Int, inst: CounterChainConfig) extends Module {
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

  val maxs = List.tabulate(numCounters) { i =>
    val maxff = Module(new FF(w))
    maxff.io.control.enable := Bool(true)
    maxff.io.data.in := io.data(i).max
    maxff.io.data.out
  }

  val strides = List.tabulate(numCounters) { i =>
    val strideff = Module(new FF(w))
    strideff.io.control.enable := Bool(true)
    strideff.io.data.in := io.data(i).stride
    strideff.io.data.out
  }

  val enables = List.tabulate(numCounters) { i =>
    val enableff = Module(new FF(1))
    enableff.io.control.enable := Bool(true)
    enableff.io.data.in := io.control(i).enable
    enableff.io.data.out
  }

  val cchain = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst))
  cchain.io.config_enable := io.config_enable
  cchain.io.config_data := io.config_data
  cchain.io.data.zipWithIndex.foreach { case (d, i) =>
    d.max := maxs(i)
    d.stride := strides(i)
  }
  cchain.io.control.zipWithIndex.foreach { case (c, i) =>
    c.enable := enables(i)
  }

  (0 until numCounters) foreach  { i =>
    val outff = Module(new FF(w))
    outff.io.control.enable := Bool(true)
    outff.io.data.in := cchain.io.data(i).out
    io.data(i).out := outff.io.data.out
    val doneff = Module(new FF(1))
    doneff.io.control.enable := Bool(true)
    doneff.io.data.in := cchain.io.control(i).done
    io.control(i).done := doneff.io.data.out
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

class CounterChainCharTests(c: CounterChainReg) extends Tester(c)

object CounterChainChar {
  def main(args: Array[String]): Unit = {
    val appArgs = args.take(args.indexOf("end"))
    if (appArgs.size < 2) {
      println("Usage: CounterChainChar <w> <numCounters>")
      sys.exit(-1)
    }
    val w = appArgs(0).toInt
    val numCounters = appArgs(1).toInt
    val startDelayWidth = 4
    val endDelayWidth = 4
    val config = CounterChainConfig.getRandom(numCounters)
    chiselMainTest(args, () => Module(new CounterChainReg(w, startDelayWidth, endDelayWidth, numCounters, config))) {
      c => new CounterChainCharTests(c)
    }
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
    val configObj = Parser(pisaFile).asInstanceOf[CounterChainConfig]
    val bitwidth = 8
    val numCounters = 8
    val startDelayWidth = 4
    val endDelayWidth = 4

    chiselMainTest(args, () => Module(new CounterChain(bitwidth, startDelayWidth, endDelayWidth, numCounters, configObj))) {
      c => new CounterChainTests(c)
    }
  }
}
