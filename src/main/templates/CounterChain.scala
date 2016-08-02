package plasticine.templates

import Chisel._

import plasticine.pisa.ir._
import scala.collection.mutable.HashMap

/**
 * CounterChain config register format
 */
case class CounterChainOpcode(val w: Int, config: Option[CounterChainConfig] = None) extends OpcodeT {
  var chain = if (config.isDefined) Bool(config.get.chain > 0) else Bool()

  override def cloneType(): this.type = {
    new CounterChainOpcode(w).asInstanceOf[this.type]
  }

  def init(inst: CounterChainConfig) {
    chain = chain.fromInt(inst.chain)
  }
}

/**
 * CounterChain: Chain of perfectly nested counters.
 * @param w: Word width
 * @param d: Counter chain depth
 */
class CounterChain(val w: Int, inst: CounterChainConfig) extends ConfigurableModule[CounterChainOpcode] {
  val d = 2 // Counter chain depth
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val data = Vec.fill(d) { new Bundle {
        val max      = UInt(INPUT,  w)
        val stride   = UInt(INPUT,  w)
        val out      = UInt(OUTPUT, w)
      }
    }
    val control = Vec.fill(d) { new Bundle {
        val enable = Bool(INPUT)
        val done   = Bool(OUTPUT)
      }
    }
  }

  val configType = CounterChainOpcode(w)
  val configIn = CounterChainOpcode(w)
  val configInit = CounterChainOpcode(w, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }


  val counterInsn = inst.counters
  val counters = (0 until d) map { i =>
    val c = Module(new CounterRC(w, counterInsn(i)))
    c.io.data.max := io.data(i).max
    c.io.data.stride := io.data(i).stride
    io.data(i).out := c.io.data.out
    c.io.control.reset := Bool(false)
    c.io.control.saturate := Bool(false)
    c
  }

  // Create chain reconfiguration logic
  (0 until d) foreach { i: Int =>
    if (i == 0) {
      counters(i).io.control.enable := io.control(i).enable
      io.control(i).done := counters(i).io.control.done
    } else {
      counters(i).io.control.enable := Mux(config.chain,
        io.control(i).enable & counters(i-1).io.control.done,
        io.control(i).enable)
        val doneSignals = (0 to i) map { k => counters(k).io.control.done }
        val andTree = doneSignals.reduce {_ & _}
      io.control(i).done := Mux(config.chain,
        andTree,
        counters(i).io.control.done)
    }
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

  (0 until c.counters.size) map { i =>
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

    if (appArgs.size != 2) {
      println("Usage: bin/sadl CounterChainTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[CounterChainConfig]]
    val bitwidth = 7

    chiselMainTest(args, () => Module(new CounterChain(bitwidth, configObj.config))) {
      c => new CounterChainTests(c)
    }
  }
}
