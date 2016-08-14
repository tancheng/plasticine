package plasticine.templates

import Chisel._
import plasticine.pisa.ir._

/**
 * Scratchpad config register format
 * @param d: Number of words in memory
 */
case class ScratchpadOpcode(val d: Int, config: Option[ScratchpadConfig] = None) extends OpcodeT {
  var mode = if (config.isDefined) UInt(config.get.banking.mode, width=2) else UInt(width=2)
  var strideLog2 = if (config.isDefined) UInt(config.get.banking.strideLog2, width=log2Up(d)) else UInt(width = log2Up(d))

  override def cloneType(): this.type = {
    new ScratchpadOpcode(d).asInstanceOf[this.type]
  }
}

/**
 * Scratchpad memory that supports various banking modes
 * and double buffering
 * @param w: Word width in bits
 * @param d: Total memory size
 * @param v: Vector width
 */
class Scratchpad(val w: Int, val d: Int, val v: Int, inst: ScratchpadConfig) extends ConfigurableModule[ScratchpadOpcode] {
  val addrWidth = log2Up(d)
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val raddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wen = Bool(INPUT)
    val waddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wdata = Vec.fill(v) { Bits(INPUT, width = w) }
    val rdata = Vec.fill(v) { Bits(OUTPUT, width = w) }
  }

  val configType = ScratchpadOpcode(d)
  val configIn = ScratchpadOpcode(d)
  val configInit = ScratchpadOpcode(d, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, reset))
  } .otherwise {
    configIn := config
  }

  def getBitSequences(l: Vec[UInt], size: Int): Vec[UInt] = {
    Vec(
      List.tabulate(l.size-size+1) { i => l.drop(i).take(size) }
        .map { seq => seq.reduce{ Cat(_,_) } }
      )
  }

  def getBitSequences(l: UInt, width: Int, size: Int): Vec[UInt] = {
      val uintAsVec = Vec.tabulate(width) { i => UInt(l(i), width=1) }
      getBitSequences(uintAsVec, size)
  }

  /**
   * Decode given address into a (bankAddr, localAddr) tuple
   */
  def getBankAddr(addr: UInt) = {
    /** Bank address a.k.a. which SRAM? */
    val bankAddrWidth = log2Up(v)
    val bankAddrSlices = getBitSequences(addr, addrWidth, bankAddrWidth)
    val bankAddrMux = Module(new MuxN(bankAddrSlices.size, bankAddrWidth))
    bankAddrMux.io.ins := bankAddrSlices
    bankAddrMux.io.sel := config.strideLog2
    bankAddrMux.io.out
  }
  def getLocalAddr(addr: UInt) = {
    /** Local address a.k.a. which word within an SRAM? */
    val localAddrWidth = log2Up(d/v)
    val localAddrSlices = getBitSequences(addr, addrWidth, localAddrWidth)
    val localAddrMux = Module(new MuxN(localAddrSlices.size, localAddrWidth))
    localAddrMux.io.ins := localAddrSlices
    localAddrMux.io.sel := config.strideLog2
    localAddrMux.io.out
  }

  def decodeAddr(addr: UInt) = {
    (getBankAddr(addr), getLocalAddr(addr))
  }

  // Check for sizes and v
  Predef.assert(d%v == 0, s"Unsupported scratchpad size ($d)/banking($v) combination; $d must be a multiple of $v")
  Predef.assert(isPow2(v), s"Unsupported banking number $v; must be a power-of-2")
  Predef.assert(isPow2(d), s"Unsupported scratchpad size $d; must be a power-of-2")
  val mems = List.fill(v) { Module(new SRAM(w, d/v)) }

  // Address decoding logic
  mems.zipWithIndex.foreach { case (m,i) =>
    // Read address
    val localRaddr = getLocalAddr(io.raddr(i))
    m.io.raddr := localRaddr

    // Write address
    val localWaddr = getLocalAddr(io.waddr(i))
    m.io.waddr := localWaddr

    // Write data
    m.io.wdata := io.wdata(i)

    // Write enable
    m.io.wen := io.wen

    // Read data
    io.rdata(i) := m.io.rdata
  }
}

class ScratchpadTests(c: Scratchpad) extends Tester(c) {

  // Write data
  poke(c.io.wen, 1)
  for (i <- 0 until c.d by c.v) {
    val wdata = Array.tabulate(c.v) { ii => BigInt(i+ii) }
    val waddr = Array.tabulate(c.v) { ii => BigInt(i+ii) }
    poke(c.io.waddr, waddr)
    poke(c.io.wdata, wdata)
    step(1)
  }
  poke(c.io.wen, 0)

  for (i <- 0 until c.d by c.v) {
    val raddr = Array.tabulate(c.v) { ii => BigInt(i+ii) }
    poke(c.io.raddr, raddr)
    step(1)
    expect(c.io.rdata, raddr)
  }
}

object ScratchpadTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl ScratchpadTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Config(pisaFile).asInstanceOf[Config[ScratchpadConfig]]

    val w = 32
    val d = 64
    val v = 2

    chiselMainTest(args, () => Module(new Scratchpad(w, d, v, configObj.config))) {
      c => new ScratchpadTests(c)
    }
  }
}

