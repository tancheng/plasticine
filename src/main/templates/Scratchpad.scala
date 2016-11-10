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
  var strideLog2 = if (config.isDefined) UInt(config.get.banking.strideLog2, width=log2Up(log2Up(d)-log2Up(v))) else UInt(width = log2Up(log2Up(d) - log2Up(v)))
  var bufSize = if (config.isDefined) {
    // Clamp bufSize value to be at least 1
    // If bufSize is 1, the Scratchpad is configured as a FIFO
    // The JSON specifies the "numBufs" field - this should be set to at least (d/v) to configure scratchpad as FIFO
    // It can be set to an outrageous number (say INT_MAX) to be safe
    if (config.get.numBufs == 0) {
      UInt(1, width=log2Up(d/v)+1)
    } else UInt(math.max(1, d / (v*config.get.numBufs)), width=log2Up(d/v)+1)
  } else UInt(width = log2Up(d/v))

  override def cloneType(): this.type = {
    new ScratchpadOpcode(d, v, config).asInstanceOf[this.type]
  }
}


/**
 * Scratchpad Address decoder: For an input address,
 * instantiate decode logic which outputs a bank address
 * and local address
 */
class AddrDecoder(val d: Int, val v: Int) extends Module {
  val addrWidth = log2Up(d)
  val bankAddrWidth = log2Up(v)
  val localAddrWidth = log2Up(d/v)
  val strideLog2Width = log2Up(log2Up(d) - log2Up(v))
  val io = new Bundle {
    val addr = UInt(INPUT, width = addrWidth)
    val strideLog2 = UInt(INPUT, width = strideLog2Width)
    val bankAddr = UInt(OUTPUT, width = bankAddrWidth)
    val localAddr = UInt(OUTPUT, width = localAddrWidth)
  }

  implicit def uintToVec(x: UInt, width: Int): Vec[UInt] = {
    Vec.tabulate(width) { i => UInt(x(i), width=1) }
  }

  implicit def vecToUInt(x: Vec[UInt]): UInt = {
    x.reduce{ Cat(_,_) }
  }

  def getBankAddrIndices(l: Vec[UInt], size: Int) = {
    List.tabulate(l.size-size+1) { i => List.tabulate(size) { j => i + j } }
  }


  def getLocalAddrSlices(l: Vec[UInt], size: Int): Vec[UInt] = {
    Vec(getBankAddrIndices(l, size).map { idxs => vecToUInt(Vec(l.zipWithIndex.filterNot { i => idxs.contains(i._2) }.map { _._1 }.reverse)) }.reverse )
  }

  def getBankAddrSlices(l: Vec[UInt], size: Int): Vec[UInt] = {
    Vec(getBankAddrIndices(l, size).map { idxs => vecToUInt(Vec(l.zipWithIndex.filter { i => idxs.contains(i._2) }.map { _._1 }.reverse)) })
  }

  def getBankAddr(addr: UInt) = {
    val bankAddrSlices = getBankAddrSlices(uintToVec(addr, addrWidth), bankAddrWidth)
    val bankAddrMux = Module(new MuxN(bankAddrSlices.size, bankAddrWidth))
    bankAddrMux.io.ins := bankAddrSlices
    bankAddrMux.io.sel := io.strideLog2
    bankAddrMux.io.out
  }

  def getLocalAddr(addr: UInt) = {
    val localAddrSlices = getLocalAddrSlices(uintToVec(addr, addrWidth), bankAddrWidth)
    val localAddrMux = Module(new MuxN(localAddrSlices.size, localAddrWidth))
    localAddrMux.io.ins := localAddrSlices.reverse
    localAddrMux.io.sel := io.strideLog2
    localAddrMux.io.out
  }

  io.bankAddr := getBankAddr(io.addr)
  io.localAddr := getLocalAddr(io.addr)
}

class AddrDecoderReg(val d: Int, val v: Int) extends Module {
  val addrWidth = log2Up(d)
  val bankAddrWidth = log2Up(v)
  val localAddrWidth = log2Up(d/v)
  val strideLog2Width = log2Up(log2Up(d) - log2Up(v))
  val io = new Bundle {
    val addr = UInt(INPUT, width = addrWidth)
    val strideLog2 = UInt(INPUT, width = strideLog2Width)
    val bankAddr = UInt(OUTPUT, width = bankAddrWidth)
    val localAddr = UInt(OUTPUT, width = localAddrWidth)
  }

  val addrReg = Module(new FF(addrWidth))
  addrReg.io.control.enable := Bool(true)
  addrReg.io.data.in := io.addr
  val addr = addrReg.io.data.out

  val strideLog2Reg = Module(new FF(strideLog2Width))
  strideLog2Reg.io.control.enable := Bool(true)
  strideLog2Reg.io.data.in := io.strideLog2
  val strideLog2 = strideLog2Reg.io.data.out

  val decoder = Module(new AddrDecoder(d, v))
  decoder.io.addr := addr
  decoder.io.strideLog2 := strideLog2

  val bankAddrReg = Module(new FF(bankAddrWidth))
  bankAddrReg.io.control.enable := Bool(true)
  bankAddrReg.io.data.in := decoder.io.bankAddr
  io.bankAddr := bankAddrReg.io.data.out

  val localAddrReg = Module(new FF(localAddrWidth))
  localAddrReg.io.control.enable := Bool(true)
  localAddrReg.io.data.in := decoder.io.localAddr
  io.localAddr := localAddrReg.io.data.out
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

  // Check for sizes and v
  Predef.assert(d%v == 0, s"Unsupported scratchpad size ($d)/banking($v) combination; $d must be a multiple of $v")
  Predef.assert(isPow2(v), s"Unsupported banking number $v; must be a power-of-2")
  Predef.assert(isPow2(d), s"Unsupported scratchpad size $d; must be a power-of-2")

  // Create 'v' individual SRAM banks
  val bankSize = d/v
  val mems = List.fill(v) { Module(new SRAM(w, bankSize)) }

  // Create size register
  val isFifo = config.bufSize === UInt(1)
  val sizeUDC = Module(new UpDownCtr(log2Up(d+1)))
  val size = sizeUDC.io.out
  val empty = size === UInt(0)
  val full = sizeUDC.io.isMax
  sizeUDC.io.initval := UInt(0)
  sizeUDC.io.max := (if (inst.numBufs == 0) UInt(0) else UInt(d - v * (bankSize % inst.numBufs)))
  sizeUDC.io.init := UInt(0)
//  sizeUDC.io.strideInc := Mux(config.chainWrite, UInt(1), UInt(v))
  sizeUDC.io.strideInc := UInt(v)
//  sizeUDC.io.strideDec := Mux(config.chainRead, UInt(1), UInt(v))
  sizeUDC.io.strideDec := UInt(v)
  sizeUDC.io.init := UInt(0)

  val writeEn = io.wdone & ~full
  val readEn = io.rdone & ~empty
  sizeUDC.io.inc := writeEn
  sizeUDC.io.dec := readEn


  // Create wptr (tail) counter chain
  val writePtrConfig = CounterChainConfig(
      List(0), //  List(inst.chainWrite),
      List.tabulate(2) { i => i match {
        case 1 => // Localaddr: max = bankSize, stride = 1
          val ctrMax = if (inst.numBufs == 0) 0 else bankSize - (bankSize % inst.numBufs)
          CounterRCConfig(ctrMax, 1, 1, 0, 0, 0)
        case 0 => // Bankaddr: max = v, stride = 1
          CounterRCConfig(v, 1, 1, 1, 0, 0)
      }}
    )

  val readPtrConfig = CounterChainConfig(
      List(0), // List(inst.chainRead),
      List.tabulate(2) { i => i match {
        case 1 => // Localaddr: max = bankSize, stride = 1
          val ctrMax = if (inst.numBufs == 0) 0 else bankSize - (bankSize % inst.numBufs)
          CounterRCConfig(ctrMax, 1, 1, 0, 0, 0)
        case 0 => // Bankaddr: max = v, stride = 1
          CounterRCConfig(v, 1, 1, 1, 0, 0)
      }}
    )

  // Create wptr and rptr
  val wptr = Module(new CounterChain(log2Up(bankSize+1), 0, 0, 2, writePtrConfig, true))
  wptr.io.control(0).enable := UInt(0) // writeEn & config.chainWrite
  wptr.io.control(1).enable := writeEn
  wptr.io.data(1).stride := config.bufSize
  val tailLocalAddr = wptr.io.data(1).out
  val tailBankAddr = wptr.io.data(0).out

  // Create rptr (head) counter chain
  val rptr = Module(new CounterChain(log2Up(bankSize+1), 0, 0, 2, readPtrConfig, true))
  rptr.io.control(0).enable := UInt(0) // readEn & config.chainRead
  rptr.io.control(1).enable := readEn
  rptr.io.data(1).stride := config.bufSize
  val headLocalAddr = rptr.io.data(1).out
//  val nextHeadLocalAddr = Mux(config.chainRead, Mux(rptr.io.control(0).done, rptr.io.data(1).next, rptr.io.data(1).out), rptr.io.data(1).next)
  val nextHeadLocalAddr = rptr.io.data(1).next
  val headBankAddr = rptr.io.data(0).out
  val nextHeadBankAddr = rptr.io.data(0).next

  io.empty := empty
  io.full := full

  // Address decoding logic
  mems.zipWithIndex.foreach { case (m,i) =>
    // Get the lane raddr and waddr
    val laneRaddr = io.raddr(i)
    val laneWaddr = io.waddr(i)

    // Read address
    val raddrDecoder = Module(new AddrDecoder(d, v))
    raddrDecoder.io.addr := laneRaddr
    raddrDecoder.io.strideLog2 := config.strideLog2
    val localRaddr = raddrDecoder.io.localAddr
    m.io.raddr := Mux(isFifo, Mux(readEn, nextHeadLocalAddr, headLocalAddr), localRaddr + headLocalAddr)

    // Write address
    val waddrDecoder = Module(new AddrDecoder(d, v))
    waddrDecoder.io.addr := laneWaddr
    waddrDecoder.io.strideLog2 := config.strideLog2
    val localWaddr = waddrDecoder.io.localAddr
    m.io.waddr := Mux(isFifo, tailLocalAddr, localWaddr + tailLocalAddr)

    // Write data
    m.io.wdata := io.wdata(i)

    // Write enable
    m.io.wen := io.wen | io.wdone // wdone becomes the 'enable' in FIFO mode

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

  val startDelayWidth = 4
  val endDelayWidth = 4
  val numCounters = 2
  val inst = CounterChainConfig (
    List(1, 1),
    List.fill(numCounters) { CounterRCConfig(0, 0, 0, 0, 0, 0) }
  )
  val c = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst))
  c.io.data(0).max := io.max
  c.io.data(0).stride := UInt(v) * io.stride
  c.io.data(1).max := io.stride
  c.io.data(1).stride := UInt(1)
  c.io.control(0).enable := io.en
  c.io.control(1).enable := io.en // This should not be necessary
  io.done := c.io.control(1).done

  io.raddr := Vec.tabulate(v) { i => c.io.data(0).out + c.io.data(1).out + UInt(i) * io.stride }
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

  val startDelayWidth = 4
  val endDelayWidth = 4
  val numCounters = 2
  val inst = CounterChainConfig (
    List(1, 1),
    List.fill(numCounters) { CounterRCConfig(0, 0, 0, 0, 0, 0) }
  )
  val c = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst))
  c.io.data(0).max := io.max
  c.io.data(0).stride := UInt(v) * io.stride
  c.io.data(1).max := io.stride
  c.io.data(1).stride := UInt(1)
  c.io.control(0).enable := io.en
  c.io.control(1).enable := io.en // This should not be necessary
  io.done := c.io.control(1).done

  val waddr = Vec.tabulate(v) { i => c.io.data(0).out + c.io.data(1).out + UInt(i) * io.stride }
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

class AddrDecoderCharTests(c: AddrDecoderReg) extends Tester(c)

class AddrDecoderTests(c: AddrDecoder) extends Tester(c) {
  val addrWidth = log2Up(c.d)
  val bankAddrWidth = log2Up(c.v)
  val localAddrWidth = log2Up(c.d/c.v)

  val numSupportedStrides = log2Up(c.d) - log2Up(c.v)  // Only powers-of-2 are supported
  val strides = List.tabulate(numSupportedStrides) { i => 1 << i }

  strides foreach { i =>
    val addrs = List.tabulate(c.d) { i => i }
    val strideLog2 = if (i == 1) 0 else log2Up(i)
    poke(c.io.strideLog2, strideLog2)

    addrs foreach { addr =>
      poke(c.io.addr, addr)
      val expectedBankAddr = (addr >> strideLog2) & ((1 << bankAddrWidth)-1)
      val expectedLocalAddr = ((addr >> (strideLog2+bankAddrWidth)) << strideLog2) | (addr & ((1 << strideLog2) - 1))
      expect(c.io.bankAddr, expectedBankAddr)
      expect(c.io.localAddr, expectedLocalAddr)
      step(1)
    }
  }
}

class ScratchpadTests(c: ScratchpadTestHarness, dataGen: (Int, Int) => Int) extends Tester(c) {
  val bankSize = c.d/c.v
  val bufSize = bankSize - (bankSize % c.inst.numBufs)/ c.inst.numBufs
  val numBufs = c.inst.numBufs
  val stride = 1 << c.inst.banking.strideLog2
  // This is the formula for max block size as a function of d, v, and numBufs
  val blockSizeMax = (c.d / (c.v * numBufs)) * c.v
  val blockSize = blockSizeMax - (blockSizeMax % (c.v*stride))

  // Block size must be a multiple

  val numIter = 1
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
      val readAddr = ListBuffer[Int]()
      val readData = ListBuffer[Int]()
      while (!(rdone && wdone)) {
        peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
        step(1)
        peek(c.io.rdata).reverse map {_.toInt } foreach { e => readData.append(e) }
        rdone = peek(c.io.readerDone) > 0
        wdone = peek(c.io.writerDone) > 0
      }
      peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
      step(1)
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      poke(c.io.readerEn, 0)
      poke(c.io.writerEn, 0)

      val reassembledData = readAddr.zip(readData).sortBy(_._1).map(_._2)

      // Drop the last few elements if the block size is not a multiple of numbanks
      observedData.append(reassembledData.dropRight(blockSize % c.v).toList)
      expectedData.append (
          List.tabulate(blockSize) { i => dataGen(i, iter) }
        )
      iter += 1
    }
    step(1)

    // Drain stages
    for (i <- 0 until numDrain) {
      val readAddr = ListBuffer[Int]()
      val readData = ListBuffer[Int]()
      poke(c.io.readerEn, 1)
      while (peek(c.io.readerDone) != 1) {
        peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
        step(1)
        peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      }
      peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
      step(1)
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      poke(c.io.readerEn, 0)

      val reassembledData = readAddr.zip(readData).sortBy(_._1).map(_._2)
      observedData.append(reassembledData.dropRight(blockSize % c.v).toList)
      iter += 1
    }
  } else {  // Unpipelined test
    var cycles = 0
    for (i <- 0 until 1) {
      poke(c.io.writerConst, iter)
      poke(c.io.writerEn, 1)
      while (peek(c.io.writerDone) != 1) {
        val writerDone = peek(c.io.writerDone)
        println(s"dummyWriter done: $writerDone")
        step(1); cycles += 1
      }
      println("dummyWriter done")
      step(1); cycles += 1
      poke(c.io.writerEn, 0)

      poke(c.io.readerEn, 1)
      val readData = ListBuffer[Int]()
      val readAddr = ListBuffer[Int]()
      while (peek(c.io.readerDone) != 1) {
        peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
        step(1); cycles += 1
        peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      }
      peek(c.reader.io.raddr).reverse map {_.toInt } foreach { readAddr.append(_) }
      step(1); cycles += 1
      peek(c.io.rdata).reverse map {_.toInt } foreach { readData.append(_) }
      println("dummyReader done")
      poke(c.io.readerEn, 0)

      val reassembledData = readAddr.zip(readData).sortBy(_._1).map(_._2)
      observedData.append(reassembledData.dropRight(blockSize % c.v).toList)
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

object AddrDecoderChar {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 2) {
      println("Usage: bin/sadl AddrDecoderChar <sramDepth> <banking>")
      sys.exit(-1)
    }

    val d = appArgs(0).toInt
    val v = appArgs(1).toInt

    chiselMainTest(args, () => Module(new AddrDecoderReg(d, v))) {
      c => new AddrDecoderCharTests(c)
    }
  }
}

object AddrDecoderTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 2) {
      println("Usage: bin/sadl AddrDecoderTest <sramDepth> <banking>")
      sys.exit(-1)
    }

    val d = appArgs(0).toInt
    val v = appArgs(1).toInt

    chiselMainTest(args, () => Module(new AddrDecoder(d, v))) {
      c => new AddrDecoderTests(c)
    }
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
    val d = 4096
    val v = 16
//    val d = 64
//    val v = 4

    chiselMainTest(args, () => Module(new ScratchpadTestHarness(w, v, d, configObj, dataGen))) {
      c => new ScratchpadTests(c, dataGen)
    }
  }
}

