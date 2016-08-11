package plasticine.templates

import Chisel._

import plasticine.pisa.ir._

/**
 * Crossbar config register format
 */
case class CrossbarOpcode(val w: Int, val numInputs: Int, val numOutputs: Int, config: Option[CrossbarConfig] = None) extends OpcodeT {
  var outSelect = if (config.isDefined) {
    Vec.tabulate(numOutputs) { i => UInt(config.get.outSelect(i), width=log2Up(numInputs)) }
  } else {
    Vec.fill(numOutputs) { UInt(width=log2Up(numInputs)) }
  }

  override def cloneType(): this.type = {
    new CrossbarOpcode(w, numInputs, numOutputs).asInstanceOf[this.type]
  }
}


/**
 * Crossbar of Vecs that connects every input to every output
 */
class CrossbarVec(val w: Int, val v: Int, val numInputs: Int, val numOutputs: Int, val inst: CrossbarConfig) extends ConfigurableModule[CrossbarOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val ins = Vec.fill(numInputs) { Vec.fill(v) { Bits(INPUT,  width = w) } }
    val outs = Vec.fill(numOutputs) { Vec.fill(v) { Bits(OUTPUT,  width = w) } }
  }

  val configType = CrossbarOpcode(w, numInputs, numOutputs)
  val configIn = CrossbarOpcode(w, numInputs, numOutputs)
  val configInit = CrossbarOpcode(w, numInputs, numOutputs, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
    val outMux = Module(new MuxVec(numInputs, v, w))
    outMux.io.ins := io.ins
    outMux.io.sel := config.outSelect(i)
    out := outMux.io.out
  }
}


/**
 * Crossbar that connects every input to every output
 */
class Crossbar(val w: Int, val numInputs: Int, val numOutputs: Int, val inst: CrossbarConfig) extends ConfigurableModule[CrossbarOpcode] {
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val ins = Vec.fill(numInputs) { Bits(INPUT,  width = w) }
    val outs = Vec.fill(numOutputs) { Bits(OUTPUT,  width = w) }
  }

  val configType = CrossbarOpcode(w, numInputs, numOutputs)
  val configIn = CrossbarOpcode(w, numInputs, numOutputs)
  val configInit = CrossbarOpcode(w, numInputs, numOutputs, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType
  } .otherwise {
    configIn := config
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
    val outMux = Module(new MuxN(numInputs, w))
    outMux.io.ins := io.ins
    outMux.io.sel := config.outSelect(i)
    out := outMux.io.out
  }
}

class CrossbarTests(c: Crossbar) extends PlasticineTester(c) {
  val config = c.inst.outSelect
  val ins = Array.fill(c.numInputs) { BigInt(rnd.nextInt(c.w)) }
  poke(c.io.ins, ins)
//  c.io.ins.zip(ins) foreach {case(inp, in) => poke(inp, in) }
  val outs = Array.tabulate(c.numOutputs) { i => ins(config(i))}
  expect(c.io.outs, outs)
//  c.io.outs.zip(outs) foreach {case(out, exp) => expect(out, exp)}
}

object CrossbarTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl CrossbarTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[CrossbarConfig]]
    val bitwidth = 8
    val inputs = 4
    val outputs = 8

    chiselMainTest(args, () => Module(new Crossbar(bitwidth, inputs, outputs, configObj.config))) {
      c => new CrossbarTests(c)
    }
  }
}
