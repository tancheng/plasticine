package plasticine.templates

import Chisel._

import scala.collection.mutable.HashMap
/**
 * CounterChain opcode format
 */
case class CounterChainOpcode(val w: Int) extends OpcodeT {
  var chain = Bool(INPUT)

  override def cloneType(): this.type = {
    new CounterChainOpcode(w).asInstanceOf[this.type]
  }

  def init(inst: HashMap[String, Any]) {
    chain = chain.fromInt(inst("chain").asInstanceOf[Int])
  }
}

/**
 * CounterChain: Chain of perfectly nested counters.
 * @param w: Word width
 * @param d: Counter chain depth
 */
class CounterChain(val w: Int, inst: HashMap[String, Any]) extends ConfigurableModule[CounterChainOpcode] {
  val d = 2 // Counter chain depth
  val io = new ConfigInterface {
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

  val configWires = CounterChainOpcode(w)
  configWires.init(inst)
  val config = RegInit(configWires)

  val counterInsn = inst("counters").asInstanceOf[Seq[HashMap[String,Int]]]
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
    val count = peek(c.io.data)
    val done = peek(c.io.control)
  }
}

object CounterChainTest {

  def main(args: Array[String]): Unit = {

    val bitwidth = 7

    val chainInst = HashMap[String, Any]("chain" -> 1)
    val countersInst = (0 until 2) map { i =>
      HashMap[String, Int]("max" -> 5, "stride" -> 1, "maxConst" -> 1, "strideConst" -> 1)
    }
    chainInst("counters") = countersInst

    chiselMainTest(args, () => Module(new CounterChain(bitwidth, chainInst))) {
      c => new CounterChainTests(c)
    }
  }
}
