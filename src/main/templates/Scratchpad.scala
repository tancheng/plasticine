package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import scala.collection.mutable.ListBuffer

/**
 * Scratchpad config register format
 * @param d: Number of words in memory
 */
case class ScratchpadOpcode(val d: Int, val v: Int, config: Option[ScratchpadConfig] = None) extends OpcodeT {
  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor

  var mode = if (config.isDefined) UInt(config.get.banking.mode, width=2) else UInt(width=2)
  var strideLog2 = if (config.isDefined) UInt(config.get.banking.strideLog2, width=log2Up(d)) else UInt(width = log2Up(d))
  var bufSize = if (config.isDefined) UInt(d / (v*config.get.numBufs), width=log2Up(d/v)+1) else UInt(width = log2Up(d/v))

  override def cloneType(): this.type = {
    new ScratchpadOpcode(d, v, config).asInstanceOf[this.type]
  }
}

/**
 * Scratchpad memory that supports various banking modes
 * and double buffering
 * @param w: Word width in bits
 * @param d: Total memory size
 * @param v: Vector width
 */
class Scratchpad(val w: Int, val d: Int, val v: Int, val inst: ScratchpadConfig) extends ConfigurableModule[ScratchpadOpcode] {
  val addrWidth = log2Up(d)
  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val raddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wen = Bool(INPUT)
    val waddr = Vec.fill(v) { UInt(INPUT, width = addrWidth) }
    val wdata = Vec.fill(v) { Bits(INPUT, width = w) }
    val rdata = Vec.fill(v) { Bits(OUTPUT, width = w) }
    val rdone = Bool(INPUT)
    val wdone = Bool(INPUT)
    val empty = Bool(OUTPUT)
    val full = Bool(OUTPUT)
  }

  val configType = ScratchpadOpcode(d, v)
  val configIn = ScratchpadOpcode(d, v)
  val configInit = ScratchpadOpcode(d, v, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  def getBitSequences(l: Vec[UInt], size: Int): Vec[UInt] = {
    Vec(
      List.tabulate(l.size-size+1) { i => l.drop(i).take(size).reverse }
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
    localAddrMux.io.ins := localAddrSlices.reverse
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

  // Create 'v' individual SRAM banks
  val bankSize = d/v
  val mems = List.fill(v) { Module(new SRAM(w, bankSize)) }

  // Create wptr and rptr
  val wptr = Module(new Counter(log2Up(bankSize)))
  wptr.io.data.max := UInt(bankSize-1-(bankSize % inst.numBufs), width=log2Up(bankSize))
  wptr.io.data.stride := config.bufSize
  wptr.io.control.reset := Bool(false)
  wptr.io.control.saturate := Bool(false)
  wptr.io.control.enable := io.wdone

  val rptr = Module(new Counter(log2Up(bankSize)))
  rptr.io.data.max := UInt(bankSize-1-(bankSize % inst.numBufs), width=log2Up(bankSize))
  rptr.io.data.stride := config.bufSize
  rptr.io.control.reset := Bool(false)
  rptr.io.control.saturate := Bool(false)
  rptr.io.control.enable := io.rdone

  io.empty := wptr.io.data.out === rptr.io.data.out
  io.full := (wptr.io.data.out - rptr.io.data.out) === config.bufSize

  // Address decoding logic
  mems.zipWithIndex.foreach { case (m,i) =>
    // Get the lane raddr and waddr
    val laneRaddr = io.raddr(i)
    val laneWaddr = io.waddr(i)

    // Read address
    val localRaddr = getLocalAddr(laneRaddr)
    m.io.raddr := localRaddr + rptr.io.data.out


    // Write address
    val localWaddr = getLocalAddr(laneWaddr)
    m.io.waddr := localWaddr + wptr.io.data.out

    // Write data
    m.io.wdata := io.wdata(i)

    // Write enable
    m.io.wen := io.wen

    // Read data
    io.rdata(i) := m.io.rdata
  }
}

class DummyReader(w: Int, v: Int) extends Module {
  val io = new Bundle {
    val max = UInt(INPUT, width=w)
    val stride = UInt(INPUT, width=w)
    val en = Bool(INPUT)
    val done = Bool(OUTPUT)
    val raddr = Vec.fill(v) { UInt(OUTPUT, width=w) }
  }

  val c = Module(new Counter(w))
  c.io.control.enable := io.en
  c.io.control.reset := Bool(false)
  c.io.control.saturate := Bool(false)
  io.done := c.io.control.done
  c.io.data.max := io.max
  c.io.data.stride := io.stride* UInt(v)

  io.raddr := Vec.tabulate(v) { i => c.io.data.out + UInt(i) * io.stride }
}

class DummyWriter(w: Int, v: Int, dataGen: (UInt,UInt) => UInt) extends Module {
  val io = new Bundle {
    val max = UInt(INPUT, width=w)
    val stride = UInt(INPUT, width=w)
    val en = Bool(INPUT)
    val done = Bool(OUTPUT)
    val const = UInt(INPUT, width=w)
    val waddr = Vec.fill(v) { UInt(OUTPUT, width=w) }
    val wdata = Vec.fill(v) { UInt(OUTPUT, width=w) }
    val wen = Bool(OUTPUT)
  }

  val c = Module(new Counter(w))
  c.io.control.enable := io.en
  c.io.control.reset := Bool(false)
  c.io.control.saturate := Bool(false)
  io.done := c.io.control.done
  c.io.data.max := io.max
  c.io.data.stride := io.stride * UInt(v)

  val waddr = Vec.tabulate(v) { i => c.io.data.out + UInt(i) * io.stride }
  io.waddr := waddr
  io.wen := io.en
  io.wdata := Vec.tabulate(v) { i => dataGen(waddr(i), io.const) }
}

class ScratchpadTestHarness(val w: Int, val v: Int, val d: Int, val inst: ScratchpadConfig, val dataGen: (UInt, UInt) => UInt) extends Module {
  val io = new Bundle {
    val max = UInt(INPUT, width=w)
    val stride = UInt(INPUT, width=w)
    val writerEn = Bool(INPUT)
    val writerConst = UInt(INPUT, width=w)
    val writerDone = Bool(OUTPUT)
    val readerEn = Bool(INPUT)
    val readerDone = Bool(OUTPUT)
    val rdata = Vec.fill(v) { UInt(OUTPUT, width=w) }
  }

  val writer = Module(new DummyWriter(w, v, dataGen))
  writer.io.max := io.max
  writer.io.stride := io.stride
  writer.io.en := io.writerEn
  io.writerDone := writer.io.done
  writer.io.const := io.writerConst

  val waddr = writer.io.waddr
  val wdata = writer.io.wdata
  val wen = writer.io.wen

  val reader = Module(new DummyReader(w, v))
  reader.io.max := io.max
  reader.io.stride := io.stride
  reader.io.en := io.readerEn
  io.readerDone := reader.io.done
  val raddr = reader.io.raddr

  val scratchpad = Module(new Scratchpad(w, d, v, inst))
  scratchpad.io.raddr := raddr
  scratchpad.io.wen := wen
  scratchpad.io.waddr := waddr
  scratchpad.io.wdata := wdata
  scratchpad.io.rdone := reader.io.done
  scratchpad.io.wdone := writer.io.done

  val empty = scratchpad.io.empty
  val full = scratchpad.io.full
  io.rdata := scratchpad.io.rdata
}


class ScratchpadTests(c: ScratchpadTestHarness, dataGen: (Int, Int) => Int) extends Tester(c) {
  val bankSize = c.d/c.v
  val bufSize = bankSize - (bankSize % c.inst.numBufs)/ c.inst.numBufs
  val numBufs = c.inst.numBufs

  // This is the formula for max block size as a function of d, v, and numBufs
  val blockSize = (c.d / (c.v * numBufs)) * c.v

  val numIter = 6
  poke(c.io.max, blockSize)
  poke(c.io.stride, 1 << c.inst.banking.strideLog2)
  println(s"strideLog2 = ${c.inst.banking.strideLog2}, log2Up(1) = ${log2Up(1)}")

  val expectedData: ListBuffer[List[Int]] = ListBuffer()
  val observedData: ListBuffer[List[Int]] = ListBuffer()
  var iter = 0

  if (numBufs > 1) {  // Pipelined test
    val numFill = numBufs-1
    val numDrain = numBufs-1
    val numSteady = numIter - numFill - numDrain

    // Fill stages
    for (i <- 0 until numFill) {
      poke(c.io.writerConst, iter)
      poke(c.io.writerEn, 1)
      while (peek(c.io.writerDone) != 1) {
        step(1)
      }
      expectedData.append (
          List.tabulate(blockSize) { i => dataGen(i, iter) }
        )
      iter += 1
      step(1)
      poke(c.io.writerEn, 0)
    }

    // Steady stages
    for (i <- 0 until numSteady) {
      poke(c.io.writerConst, iter)
      poke(c.io.writerEn, 1)
      poke(c.io.readerEn, 1)
      var rdone = false
      var wdone = false
      val readData = ListBuffer[Int]()
      while (!(rdone && wdone)) {
        step(1)
        peek(c.io.rdata).reverse map {_.toInt } foreach { e => readData.append(e) }
        rdone = peek(c.io.readerDone) > 0
        wdone = peek(c.io.writerDone) > 0
      }
      step(1)
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      poke(c.io.readerEn, 0)
      poke(c.io.writerEn, 0)
      // Drop the last few elements if the block size is not a multiple of numbanks
      observedData.append(readData.dropRight(blockSize % c.v).toList)
      expectedData.append (
          List.tabulate(blockSize) { i => dataGen(i, iter) }
        )
      iter += 1
    }
    step(1)

    // Drain stages
    for (i <- 0 until numDrain) {
      val readData = ListBuffer[Int]()
      poke(c.io.readerEn, 1)
      while (peek(c.io.readerDone) != 1) {
        step(1)
        peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      }
      step(1)
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      poke(c.io.readerEn, 0)
      observedData.append(readData.dropRight(blockSize % c.v).toList)
      iter += 1
    }
  } else {  // Unpipelined test
    for (i <- 0 until 1) {
      poke(c.io.writerConst, iter)
      poke(c.io.writerEn, 1)
      while (peek(c.io.writerDone) != 1) {
        val writerDone = peek(c.io.writerDone)
        println(s"dummyWriter done: $writerDone")
        step(1)
      }
      println("dummyWriter done")
      step(1)
      poke(c.io.writerEn, 0)

      poke(c.io.readerEn, 1)
      val readData = ListBuffer[Int]()
      while (peek(c.io.readerDone) != 1) {
        step(1)
        peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      }
      step(1)
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      println("dummyReader done")
      poke(c.io.readerEn, 0)

      observedData.append(readData.dropRight(blockSize % c.v).toList)
      expectedData.append (
          List.tabulate(blockSize) { i => dataGen(i, iter) }
        )
      iter += 1
    }

  }

  println(s"After $numIter iterations:")
  expectedData.zip(observedData) foreach { case (e, o) =>
    println(s"Expected: $e")
    println(s"Observed: $o")
  }
  if (expectedData != observedData) {
    println(s"Test FAILED")
  } else {
    println(s"Test PASSED")
  }
}

object ScratchpadTest {

  // Dumb, but cannot use type parameters
  def dataGen(iter: UInt, const: UInt) = iter * (const + UInt(1))
  def dataGen(iter: Int, const: Int) = iter * (const + 1)

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println("Usage: bin/sadl ScratchpadTest <pisa config>")
      sys.exit(-1)
    }

    val pisaFile = appArgs(0)
    val configObj = Parser(pisaFile).asInstanceOf[ScratchpadConfig]

    val w = 32
    val d = 64
    val v = 2

    chiselMainTest(args, () => Module(new ScratchpadTestHarness(w, v, d, configObj, dataGen))) {
      c => new ScratchpadTests(c, dataGen)
    }
  }
}

