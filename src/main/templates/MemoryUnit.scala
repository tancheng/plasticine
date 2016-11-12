package plasticine.templates

import scala.collection.mutable.Queue
import scala.collection.mutable.HashMap
import Chisel._

import plasticine.pisa.ir._

/**
 * MemoryUnit config register format
 */
case class MemoryUnitOpcode(config: Option[MemoryUnitConfig] = None) extends OpcodeT {
  val scatterGather = if (config.isDefined) Bool(config.get.scatterGather > 0) else Bool()
  val isWr = if (config.isDefined) Bool(config.get.isWr > 0) else Bool()

  override def cloneType(): this.type = {
    new MemoryUnitOpcode(config).asInstanceOf[this.type]
  }
}

trait RdyVldInterface extends Bundle {
  val rdyIn = Bool(INPUT)     // rdata
  val vldOut = Bool(OUTPUT)   // rdata
  val rdyOut = Bool(OUTPUT)   // Command
  val vldIn = Bool(INPUT)     // Command
}

abstract class AbstractMemoryCmdInterface(w: Int, v: Int, dir: IODirection) extends Bundle
  with RdyVldInterface {
  def reverseDir = dir match {
    case INPUT => OUTPUT
    case OUTPUT => INPUT
    case NODIR => NODIR
  }

  val addr: Data // Can be 1 (for output) or Vec(v) (for input)
  val wdata = Vec.fill(v) { UInt(dir, width=w) } // v
  val rdata = Vec.fill(v) { UInt(reverseDir, width=w) } // v
}

class PlasticineMemoryCmdInterface(w: Int, v: Int) extends AbstractMemoryCmdInterface(w, v, INPUT) {
  val addr = Vec.fill(v) { UInt(INPUT, width=w) }
  val size = UInt(INPUT, width=w)
  val dataRdyOut = Bool(OUTPUT)
  val dataVldIn = Bool(INPUT)
}

class DRAMCmdInterface(w: Int, v: Int) extends AbstractMemoryCmdInterface(w, v, OUTPUT) {
  val addr = UInt(OUTPUT, width=w)
  val isWr = Bool(OUTPUT) // 1
  val tagIn = UInt(INPUT, width=w)
  val tagOut = UInt(OUTPUT, width=w)
}

abstract class AbstractMemoryUnit(
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val numCounters: Int,
  val inst: MemoryUnitConfig) extends ConfigurableModule[MemoryUnitOpcode] {

  val wordSizeBytes = w/8
  val burstSizeWords = burstSizeBytes / wordSizeBytes

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnect = new PlasticineMemoryCmdInterface(w, v)
    val dram = new DRAMCmdInterface(w, v) // Should not have to pass vector width; must be DRAM burst width
  }

  val configType = MemoryUnitOpcode()
  val configIn = MemoryUnitOpcode()
  val configInit = MemoryUnitOpcode(Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

}

class MemoryUnitSim(
  override val w: Int,
  override val d: Int,
  override val v: Int,
  override val numOutstandingBursts: Int,
  override val burstSizeBytes: Int,
  override val startDelayWidth: Int,
  override val endDelayWidth: Int,
  override val numCounters: Int,
  override val inst: MemoryUnitConfig) extends AbstractMemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst) {

  // Empty module
  this.name = "MemoryUnit"
}

class MemoryUnit(
  override val w: Int,
  override val d: Int,
  override val v: Int,
  override val numOutstandingBursts: Int,
  override val burstSizeBytes: Int,
  override val startDelayWidth: Int,
  override val endDelayWidth: Int,
  override val numCounters: Int,
  override val inst: MemoryUnitConfig) extends AbstractMemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst) {

  // The data bus width to DRAM is 1-burst wide
  // While it should be possible in the future to add a write combining buffer
  // and decouple Plasticine's data bus width from the DRAM bus width,
  // this is currently left out as a wishful todo.
  // Check and error out if the data bus width does not match DRAM burst size
  Predef.assert(w/8*v == 64,
  s"Unsupported combination of w=$w and v=$v; data bus width must equal DRAM burst size ($burstSizeBytes bytes)")

  def extractBurstAddr(addr: UInt) = {
    val burstOffset = log2Up(burstSizeBytes)
    addr(addr.getWidth-1, burstOffset)
  }

  def extractBurstOffset(addr: UInt) = {
    val burstOffset = log2Up(burstSizeBytes)
    addr(burstOffset-1, 0)
  }

  def extractWordOffset(addr: UInt) = {
    val burstOffset = log2Up(burstSizeBytes)
    val wordOffset = log2Up(w)
    addr(burstOffset, wordOffset)
  }

  // Addr FIFO
  val addrFifo = Module(new FIFOCore(w, d, v))
  addrFifo.io.chainRead := UInt(1)
  addrFifo.io.chainWrite := ~config.scatterGather
  addrFifo.io.enq := io.interconnect.addr
  addrFifo.io.enqVld := io.interconnect.vldIn

  val burstAddrs = addrFifo.io.deq map { extractBurstAddr(_) }
  val wordOffsets = addrFifo.io.deq map { extractWordOffset(_) }

  // Size FIFO
  val sizeFifo = Module(new FIFOCore(w, d, v))
  sizeFifo.io.chainWrite := UInt(1)
  sizeFifo.io.chainRead := UInt(1)
  sizeFifo.io.enq := Vec.fill(v) { io.interconnect.size }
  sizeFifo.io.enqVld := io.interconnect.vldIn & ~config.scatterGather
  val burstVld = ~sizeFifo.io.empty

  val sizeTop = sizeFifo.io.deq(0)
  val sizeInBursts = extractBurstAddr(sizeTop) + orR(extractBurstOffset(sizeTop))

  // Data FIFO
  // Deriving the 'enqVld' signal from the other FIFO states is potentially
  // dangerous because there is no guarantee that the data input pins actually contain
  // valid data. The safest approach is to have separate enables for command (addr, size, rdwr)
  // and data.
  val dataFifo = Module(new FIFOCore(w, d, v))
  dataFifo.io.chainWrite := UInt(0)
  dataFifo.io.chainRead := config.scatterGather
  dataFifo.io.enq := io.interconnect.wdata
  dataFifo.io.enqVld := io.interconnect.dataVldIn

  // Burst offset counter
  val burstCounter = Module(new Counter(w))
  burstCounter.io.data.max := Mux(config.scatterGather, UInt(1), sizeInBursts)
  burstCounter.io.data.stride := UInt(1)
  burstCounter.io.control.reset := UInt(0)
  burstCounter.io.control.enable := Mux(config.scatterGather, ~addrFifo.io.empty, burstVld)
  burstCounter.io.control.saturate := UInt(0)
  sizeFifo.io.deqVld := burstCounter.io.control.done

  // Burst Tag counter
  val burstTagCounter = Module(new Counter(log2Up(numOutstandingBursts+1)))
  burstTagCounter.io.data.max := UInt(numOutstandingBursts)
  burstTagCounter.io.data.stride := UInt(1)
  burstTagCounter.io.control.reset := UInt(0)
  burstTagCounter.io.control.enable := burstVld | ~addrFifo.io.empty
  burstCounter.io.control.saturate := UInt(0)
  val elementID = burstTagCounter.io.data.out(log2Up(v)-1, 0)

  // Coalescing cache
  val ccache = Module(new CoalescingCache(w, d, v))
  ccache.io.raddr := Cat(io.dram.tagIn, UInt(0, width=log2Up(burstSizeBytes)))
  ccache.io.readEn := config.scatterGather & io.dram.vldIn
  ccache.io.waddr := addrFifo.io.deq(0)
  ccache.io.wen := config.scatterGather & ~addrFifo.io.empty
  ccache.io.position := elementID
  ccache.io.wdata := dataFifo.io.deq(0)
  ccache.io.isScatter := Bool(false) // TODO: Remove this restriction once ready

  addrFifo.io.deqVld := burstCounter.io.control.done & ~ccache.io.full
  dataFifo.io.deqVld := Mux(config.scatterGather, burstCounter.io.control.done & ~ccache.io.full, config.isWr & burstVld)


  // Parse Metadata line
  def parseMetadataLine(m: UInt) = {
    // m is burstSizeWords * 8 wide. Each byte 'i' has the following format:
    // | 7 6 5 4     | 3    2   1    0     |
    // | x x x <vld> | <crossbar_config_i> |
    val validAndConfig = List.tabulate(burstSizeWords) { i =>
      parseMetadata(m(i*8+8-1, i*8))
    }

    val valid = validAndConfig.map { _._1 }
    val crossbarConfig = validAndConfig.map { _._2 }
    (valid, crossbarConfig)
  }

  def parseMetadata(m: UInt) = {
    // m is an 8-bit word. Each byte has the following format:
    // | 7 6 5 4     | 3    2   1    0     |
    // | x x x <vld> | <crossbar_config_i> |
    val valid = m(log2Up(burstSizeWords))
    val crossbarConfig = m(log2Up(burstSizeWords)-1, 0)
    (valid, crossbarConfig)
  }

  val (validMask, crossbarConfig) = parseMetadataLine(ccache.io.rmetaData)

  val registeredData = Vec(io.dram.rdata.map { d => Reg(UInt(width=w), d) })
  val registeredVld = Reg(UInt(width=1), io.dram.vldIn)

  // Gather crossbar
  val gatherCrossbar = Module(new CrossbarCore(w, v, v))
  gatherCrossbar.io.ins := registeredData
  gatherCrossbar.io.config := Vec(crossbarConfig)

  // Gather buffer
  val gatherBuffer = List.tabulate(burstSizeWords) { i =>
    val ff = Module(new FF(w))
    ff.io.control.enable := registeredVld & validMask(i)
    ff.io.data.in := gatherCrossbar.io.outs(i)
    ff
  }
  val gatherData = Vec.tabulate(burstSizeWords) { gatherBuffer(_).io.data.out }

  // Completion mask
  val completed = UInt()
  val completionMask = List.tabulate(burstSizeWords) { i =>
    val ff = Module(new FF(1))
    ff.io.control.enable := completed | (validMask(i) & registeredVld)
    ff.io.data.in := validMask(i)
    ff
  }
  completed := completionMask.map { _.io.data.out }.reduce { _ & _ }

  // FIFO for sizes to be received
  val receivedFifo = Module(new FIFOCore(w, d, v))
  receivedFifo.io.chainWrite := UInt(1)
  receivedFifo.io.chainRead := UInt(1)
  receivedFifo.io.enq := Vec.fill(v) { sizeInBursts }
  receivedFifo.io.enqVld := burstVld & burstCounter.io.data.out === UInt(0)

  val maxReceivedSizeBursts = receivedFifo.io.deq(0)

  // Burst received counter
  val receivedCounter = Module(new Counter(w))
  receivedCounter.io.data.max := maxReceivedSizeBursts
  receivedCounter.io.data.stride := UInt(1)
  receivedCounter.io.control.reset := UInt(0)
  receivedCounter.io.control.enable := Mux(config.scatterGather, UInt(0), io.dram.vldIn)
  receivedCounter.io.control.saturate := UInt(0)
  receivedFifo.io.deqVld := receivedCounter.io.control.done

  // Counter chain, where innermost counter is chained to receivedCounter
  val counterChain = Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, inst.counterChain))

  io.dram.addr := Cat((burstAddrs(0) + burstCounter.io.data.out), UInt(0, width=log2Up(burstSizeBytes)))
  io.dram.tagOut := Mux(config.scatterGather, burstAddrs(0), burstTagCounter.io.data.out)
  io.dram.wdata := dataFifo.io.deq
  io.dram.vldOut := Mux(config.scatterGather, ccache.io.miss, burstVld)
  io.dram.isWr := config.isWr

  io.interconnect.rdata := Mux(config.scatterGather, gatherData, io.dram.rdata)
  io.interconnect.vldOut := Mux(config.scatterGather, completed, io.dram.vldIn)
  io.interconnect.rdyOut := ~addrFifo.io.full & ~dataFifo.io.full
}

class MemoryTester (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val numCounters: Int,
  val inst: MemoryUnitConfig) extends Module {

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnect = new PlasticineMemoryCmdInterface(w, v)
    val dram = new DRAMCmdInterface(w, v) // Should not have to pass vector width; must be DRAM burst width
  }

  val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, inst))
  mu.io.config_enable := io.config_enable
  mu.io.config_data := io.config_data
  mu.io.interconnect.rdyIn := io.interconnect.rdyIn
  mu.io.interconnect.vldIn := io.interconnect.vldIn
  io.interconnect.rdyOut := mu.io.interconnect.rdyOut
  io.interconnect.vldOut := mu.io.interconnect.vldOut
  mu.io.interconnect.addr := io.interconnect.addr
  mu.io.interconnect.wdata := io.interconnect.wdata
  mu.io.interconnect.dataVldIn := io.interconnect.dataVldIn
  mu.io.interconnect.size := io.interconnect.size
  io.interconnect.rdata := mu.io.interconnect.rdata

  io.dram.addr := mu.io.dram.addr
  io.dram.wdata := mu.io.dram.wdata
  io.dram.tagOut := mu.io.dram.tagOut
  io.dram.vldOut := mu.io.dram.vldOut
  io.dram.isWr := mu.io.dram.isWr

  val idealMem = Module(new IdealMemory(w, burstSizeBytes))
  idealMem.io.addr := mu.io.dram.addr
  idealMem.io.wdata := mu.io.dram.wdata
  idealMem.io.tagIn := mu.io.dram.tagOut
  idealMem.io.vldIn := mu.io.dram.vldOut
  idealMem.io.isWr := mu.io.dram.isWr
  mu.io.dram.rdata := idealMem.io.rdata
  mu.io.dram.vldIn := idealMem.io.vldOut
  mu.io.dram.tagIn := idealMem.io.tagOut
}


class MemoryUnitTests(c: AbstractMemoryUnit) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

  def getBurstAddr(addr: Int) = addr - (addr % burstSizeBytes)

  def poke(port: Vec[UInt], value: Seq[Int]) {
    port.zip(value) foreach { case (in, i) => poke(in, i) }
  }

  def peek(port: Vec[UInt]): List[Int] = {
    port.map { peek(_).toInt }.toList
  }

  case class Cmd(addr: Int, data: List[Int], wr: Int, tag: Int) {
    override def equals(that: Any) = that match {
      case t: Cmd =>
        (addr == t.addr) &
        (tag == t.tag) &
        (wr == t.wr) &
        (if (wr > 0) data == t.data else true)
      case _ => false
    }

    override def toString = s"Cmd($addr, $tag, $wr, ${if (wr > 0) data else List()})"
  }

  var expectedTag = 0
  val expectedOrder = Queue[Cmd]()
  val observedOrder = Queue[Cmd]()

  def observeFor(x: Int) {
    for (i <- 0 until x) {
      if (peek(c.io.dram.vldOut) > 0) {
        val issuedAddr = peek(c.io.dram.addr).toInt
        val data = peek(c.io.dram.wdata)
        val wr = peek(c.io.dram.isWr).toInt
        val tag = peek(c.io.dram.tagOut).toInt
        observedOrder += Cmd(issuedAddr, data, wr, tag)
      }
      step(1)
    }
  }

  def incTag {
    expectedTag = (expectedTag + 1) % c.numOutstandingBursts
  }

  def getNumBursts(size: Int) = (size / burstSizeBytes) + (if (size%burstSizeBytes > 0) 1 else 0)
  def writeMode = peek(c.config.isWr).toInt
  def sgMode = peek(c.config.scatterGather).toInt
  def resetAll {
    reset(10)
    expectedTag = 0
  }

  def issueCmd(burstAddr: Int, isWr: Int, data: List[Int]) {
    val tag = if (sgMode > 0) burstAddr / burstSizeBytes else expectedTag
    val cmd = Cmd(burstAddr, data, isWr, tag)
    expectedOrder += cmd
    incTag
  }

  def updateExpected(addr: Int, size: Int, data: List[Int]) {
    val dataInBursts = getDataInBursts(data)
    val baseAddr = addr - (addr % burstSizeBytes)
    val numBursts = getNumBursts(size)
    val isWr = writeMode
    for (i <- 0 until numBursts) {
      issueCmd(baseAddr+i*burstSizeBytes, isWr, if (dataInBursts.isEmpty) List() else dataInBursts.dequeue)
    }
  }

  // Scatter-gather issue
  def updateExpected(addr: List[Int], data: List[Int]) {
    val dataInBursts = getDataInBursts(data)
    val baseAddresses = addr.map { a => a - (a % burstSizeBytes) }
    val uniqueBaseAddresses = baseAddresses.distinct
    val isWr = writeMode
    uniqueBaseAddresses.foreach { a => issueCmd(a, isWr, if (isWr == 0) List() else data) }
  }

  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  def verifyAndDequeue: Boolean = {
    if (expectedOrder.size  != observedOrder.size) {
      printFail(s"Queue size mismatch: expected ${expectedOrder.size}, found ${observedOrder.size}")
      printFail(s"Expected: $expectedOrder")
      printFail(s"Observed: $observedOrder")
      false
    } else {
      val res = expectedOrder.zip(observedOrder).map { case (e, o) =>
        if (e != o) printFail(s"Expected $e, observed $o")
        e == o
      }.reduce{_ & _}
      expectedOrder.clear
      observedOrder.clear
      res
    }
  }

  def check(s: String) = verifyAndDequeue match {
    case true => printPass(s)
    case _ => printFail(s)
  }

  def enqueueCmd(addr: Int, size: Int, data: List[Int] = List[Int]()) {
    poke(c.io.interconnect.vldIn, 1)
    poke(c.io.interconnect.addr(0), addr)
    poke(c.io.interconnect.size, size)
    val isWr = writeMode
    // If it is a write, enqueue first burst
    val dataInBursts = getDataInBursts(data)
    if (isWr > 0) {
      poke(c.io.interconnect.wdata, dataInBursts.dequeue)
      poke(c.io.interconnect.dataVldIn, 1)
    }
    observeFor(1)
    poke(c.io.interconnect.vldIn, 0)
    if (isWr > 0) {
      while (!dataInBursts.isEmpty) {
        poke(c.io.interconnect.wdata, dataInBursts.dequeue)
        observeFor(1)
      }
    }
    poke(c.io.interconnect.dataVldIn, 0)
    updateExpected(addr, size, data)
  }

  def enqueueCmd(addr: List[Int], data: List[Int]) {
    val isWr = writeMode
    poke(c.io.interconnect.vldIn, 1)
    c.io.interconnect.addr.zip(addr) foreach { case (in, i) => poke(in, i) }

    // If it is a write, enqueue first burst
    if (isWr > 0) {
      poke(c.io.interconnect.wdata, data)
      poke(c.io.interconnect.dataVldIn, 1)
    }
    observeFor(1)
    poke(c.io.interconnect.vldIn, 0)
    poke(c.io.interconnect.dataVldIn, 0)
    updateExpected(addr, data)
  }


  def enqueueBurstRead(addr: Int, size: Int) {
    enqueueCmd(addr, size)
  }

  def enqueueBurstWrite(addr: Int, size: Int, data: List[Int]) {
    enqueueCmd(addr, size, data)
  }

  def enqueueGather(addr: List[Int]) {
    enqueueCmd(addr, List())
  }

  def enqueueScatter(addr: List[Int], data: List[Int]) {
    enqueueCmd(addr, data)
  }

  def pokeDramResponse(tag: Int, data: List[Int]) {
    poke(c.io.dram.tagIn, tag)
    poke(c.io.dram.vldIn, 1)
    if (data.size > 0) poke(c.io.dram.rdata, data)
    observeFor(1)
    poke(c.io.dram.vldIn, 0)
  }


  def testBurstRead {
    resetAll
    poke(c.config.scatterGather, 0)
    poke(c.config.isWr, 0)

    // Test constants
    val addr = 0x1000
    val waddr = 0x2000

    // 2a. Smoke test, read: Single burst with a single burst size
    enqueueBurstRead(addr, burstSizeBytes)
    observeFor(1)
    check("Single burst read")

    val numBursts = 10
    // 3a. Bigger smoke test, read: Single burst address with multi-burst size
    enqueueBurstRead(waddr, numBursts * burstSizeBytes)
    observeFor(numBursts+8)
    check("Single Multi-burst read")

    // 4. Multiple commands - read
    val numCommands = c.numOutstandingBursts
    val maxSizeBursts = 9 // arbitrary
    val raddrs = List.tabulate(numCommands) { i => addr + i * 0x1000 }
    val rsizes = List.tabulate(numCommands) { i => burstSizeBytes * (math.abs(rnd.nextInt) % maxSizeBursts + 1) }
    raddrs.zip(rsizes) foreach { case (raddr, rsize) => enqueueBurstRead(raddr, rsize) }
    val cyclesToWait = rsizes.map { size =>
      (size / burstSizeBytes) + (if ((size % burstSizeBytes) > 0) 1 else 0)
    }.sum
    observeFor(cyclesToWait*5)
    check("Multiple multi-burst read")
  }

  def testBurstWrite {
    resetAll
    poke(c.config.scatterGather, 0)
    poke(c.config.isWr, 1)

    // Test constants
    val addr = 0x1000
    val waddr = 0x2000

    // 2b. Smoke test, write: Single burst with a single burst size
    val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
    enqueueBurstWrite(waddr, burstSizeBytes, wdata)
    observeFor(1)
    check("Single burst write")

    val numBursts = 10
    // 3b. Bigger smoke test, write: Single burst address with multi-burst size
    val bigWdata = List.tabulate(numBursts * wordsPerBurst) { i => 0xf00d + i }
    enqueueBurstWrite(waddr, numBursts * burstSizeBytes, bigWdata)
    observeFor(numBursts+5)
    check("Single Multi-burst write")

    // 5. Multiple commands - write
    val numCommands = c.numOutstandingBursts
    val maxSizeBursts = 9 // arbitrary
    val waddrs = List.tabulate(numCommands) { i => addr + i * 0x1000 }
    val wsizes = List.tabulate(numCommands) { i => burstSizeBytes * (math.abs(rnd.nextInt) % maxSizeBursts + 1) }
    waddrs.zip(wsizes) foreach { case (waddr, wsize) =>
      val wdata = List.tabulate(wsize / (c.w/8)) { i => i }
      enqueueBurstWrite(waddr, wsize, wdata)
    }
    val wCyclesToWait = wsizes.map { size =>
      (size / burstSizeBytes) + (if ((size % burstSizeBytes) > 0) 1 else 0)
    }.sum
    observeFor(wCyclesToWait)
    check("Multiple multi-burst write")
  }

  def testBurstMode {
    // Test burst mode
    // 1. If queue is empty, there must be a 'ready' signal sent to the interconnect
    expect(c.io.interconnect.rdyOut, 1)

    testBurstRead
    testBurstWrite

    // 5. Fill up address queue

    // 6. Fill up data queue
  }

  def testGather {
    resetAll
    poke(c.config.scatterGather, 1)
    poke(c.config.isWr, 0)

    def getTagFor(addr: Int) = {
      observedOrder.filter(_.addr == addr).head.tag
    }

    // 1. Single gather issue where all addresses are the same
    val g = List.tabulate(c.v) { i => 0x1000 }
    val expectedData = 0x2000
    enqueueGather(g)
    observeFor(c.v * 2)
    pokeDramResponse(getTagFor(0x1000), List.fill(c.v) { 0x2000 })
    check("Single gather with same address")

    // 2. Single gather, multiple addresses
    val addrs = List.tabulate(c.v) { i => if (i == 0) 0x1000 else (0x1000 + (rnd.nextInt) % 0x1000) & ((Integer.MAX_VALUE >> log2Up(c.wordSizeBytes)) << log2Up(c.wordSizeBytes)) }
    val burstData = HashMap[Int, List[Int]]()
    addrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      if (!burstData.contains(baseAddr)) burstData(baseAddr) = List.fill(c.burstSizeWords) { math.abs(rnd.nextInt(0x1000)) }
    }
    enqueueGather(addrs)
    observeFor(c.v * 2) // Checks for issue

    val shuffledAddrs = rnd.shuffle((0 until addrs.size).toList).map { addrs(_) }

    shuffledAddrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      // Start replying randomly with different addresses
      pokeDramResponse(getTagFor(baseAddr), burstData(baseAddr))
      observeFor(1)
    }
    step(10)
    check("Single gather, multiple addresses")
    val expectedGather = addrs.map { a =>
      val baseAddr = a - (a % burstSizeBytes)
      val burstOffset = a % burstSizeBytes
      val wordOffset = burstOffset / c.wordSizeBytes
      val data = burstData(baseAddr)
      data(wordOffset)
    }
    println(s"Expected gather: $expectedGather")
    println(s"Gather addresses")
    addrs.foreach { a =>
      val baseAddr = a - (a % burstSizeBytes)
      val burstOffset = a % burstSizeBytes
      val wordOffset = burstOffset / c.wordSizeBytes
      val data = burstData(baseAddr)
      println(s"addr:$a, base:$baseAddr, wordOffset:$wordOffset, data:$data")
    }
  }

  testBurstRead
  testBurstWrite
  testGather
}

object MemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16
  val burstSizeBytes = 64
  val startDelayWidth = 4
  val endDelayWidth = 4
  val numCounters = 8

  val scatterGather = 1
  val isWr = 0
  val config = MemoryUnitConfig(scatterGather, isWr, CounterChainConfig.zeroes(numCounters))
  def main(args: Array[String]): Unit = {
    val testMode = args.contains("--test")
    if (testMode) {
      chiselMainTest(args, () => Module(new MemoryUnitSim(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config)).asInstanceOf[AbstractMemoryUnit]) {
            c => { new MemoryUnitTests(c) }
      }
    } else {
      chiselMainTest(args, () => Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config)).asInstanceOf[AbstractMemoryUnit]) {
            c => { new MemoryUnitTests(c) }
      }
    }
  }
}

