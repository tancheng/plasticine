package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import plasticine.Globals

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
 * Core logic inside a crossbar
 */
class CrossbarCore(val w: Int, val numInputs: Int, val numOutputs: Int) extends Module {
  val io = new Bundle {
    val config_enable = Bool(INPUT)
    val ins = Vec.fill(numInputs) { Bits(INPUT,  width = w) }
    val outs = Vec.fill(numOutputs) { Bits(OUTPUT,  width = w) }
    val config = Vec.fill(numOutputs) { Bits(INPUT, width = log2Up(numInputs)) }
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
    val outMux = Module(new MuxN(numInputs, w))
    outMux.io.ins := io.ins
    outMux.io.sel := io.config(i)
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
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
//    val outMux = Module(new MuxN(numInputs, w))
    val outMux = if (Globals.noModule) new MuxNL(numInputs, w) else Module(new MuxN(numInputs, w))
    outMux.io.ins := io.ins
    outMux.io.sel := config.outSelect(i)
    out := outMux.io.out
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
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
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


class CrossbarVecReg(val w: Int, val v: Int, val numInputs: Int, val numOutputs: Int, val inst: CrossbarConfig) extends ConfigurableModule[CrossbarOpcode] {
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
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
    val outMux = Module(new MuxVec(numInputs, v, w))

    outMux.io.ins := io.ins
    outMux.io.sel := config.outSelect(i)

    val outFFs = List.tabulate(v) { i =>
      val ff = Module(new FF(w))
      ff.io.control.enable := UInt(1)
      ff.io.data.in := outMux.io.out(i)
      ff
    }

    val outVec = Vec.tabulate(v) { i => outFFs(i).io.data.out }

    out := outVec
  }
}

class CrossbarReg(val w: Int, val numInputs: Int, val numOutputs: Int, val inst: CrossbarConfig) extends ConfigurableModule[CrossbarOpcode] {
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
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  io.outs.zipWithIndex.foreach { case(out,i) =>
    val outMux = Module(new MuxN(numInputs, w))

    outMux.io.ins := io.ins
    outMux.io.sel := config.outSelect(i)

    val outFF = Module(new FF(w))
    outFF.io.control.enable := UInt(1)
    outFF.io.data.in := outMux.io.out(i)

    out := outFF.io.data.out
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
    val configObj = Parser(pisaFile).asInstanceOf[CrossbarConfig]
    val bitwidth = 8
    val inputs = 4
    val outputs = 8

    chiselMainTest(args, () => Module(new Crossbar(bitwidth, inputs, outputs, configObj))) {
      c => new CrossbarTests(c)
    }
  }
}

class CrossbarVecTests(c: CrossbarVecReg) extends PlasticineTester(c) { }
object CrossbarVecTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 4) {
      println("Usage: bin/sadl CrossbarTest <pisa config>")
      sys.exit(-1)
    }

    val w = appArgs(0).toInt
    val v = appArgs(1).toInt
    val inputs = appArgs(2).toInt
    val outputs = appArgs(3).toInt

    val configObj = CrossbarConfig.getRandom(outputs)
    chiselMainTest(args, () => Module(new CrossbarVecReg(w, v, inputs, outputs, configObj))) {
      c => new CrossbarVecTests(c)
    }
  }
}
