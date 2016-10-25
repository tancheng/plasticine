package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

/**
 * MemoryUnit config register format
 */
case class MemoryUnitOpcode(config: Option[MemoryUnitConfig] = None) extends OpcodeT {
  val scatterGather = if (config.isDefined) Bool(config.get.scatterGather > 0) else Bool()

  override def cloneType(): this.type = {
    new MemoryUnitOpcode(config).asInstanceOf[this.type]
  }
}

trait RdyVldInterface extends Bundle {
  val rdyIn = Bool(INPUT)
  val rdyOut = Bool(OUTPUT)
  val vldIn = Bool(INPUT)
  val vldOut = Bool(OUTPUT)
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
  val isWr = Bool(dir) // 1
  val rdata = Vec.fill(v) { UInt(reverseDir, width=w) } // v
}

class PlasticineMemoryCmdInterface(w: Int, v: Int) extends AbstractMemoryCmdInterface(w, v, INPUT) {
  val addr = Vec.fill(v) { UInt(INPUT, width=w) }
  val size = UInt(INPUT, width=w)
  val dataVldIn = Bool(INPUT)
}

class DRAMCmdInterface(w: Int, v: Int) extends AbstractMemoryCmdInterface(w, v, OUTPUT) {
  val addr = UInt(OUTPUT, width=w)
  val tagIn = UInt(INPUT, width=w)
  val tagOut = UInt(OUTPUT, width=w)
}

class MemoryUnit(
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val inst: MemoryUnitConfig) extends ConfigurableModule[MemoryUnitOpcode] {

  val burstSizeBytes = 64

  // The data bus width to DRAM is 1-burst wide
  // While it should be possible in the future to add a write combining buffer
  // and decouple Plasticine's data bus width from the DRAM bus width,
  // this is currently left out as a wishful todo.
  // Check and error out if the data bus width does not match DRAM burst size
  Predef.assert(w/8*v == 64,
  s"Unsupported combination of w=$w and v=$v; data bus width must equal DRAM burst size ($burstSizeBytes bytes)")

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
  val addrFifo = Module(new FIFO(w, d, v, FIFOConfig(1-inst.scatterGather, 1)))
  addrFifo.io.config_enable := io.config_enable
  addrFifo.io.config_data := io.config_data
  addrFifo.io.enq := io.interconnect.addr
  addrFifo.io.enqVld := io.interconnect.vldIn

  val burstAddrs = addrFifo.io.deq map { extractBurstAddr(_) }
  val wordOffsets = addrFifo.io.deq map { extractWordOffset(_) }

  // Size FIFO
  val sizeFifo = Module(new FIFO(w, d, v, FIFOConfig(1, 1)))
  sizeFifo.io.config_enable := io.config_enable
  sizeFifo.io.config_data := io.config_data
  sizeFifo.io.enq := Vec.fill(v) { io.interconnect.size }
  sizeFifo.io.enqVld := io.interconnect.vldIn & ~config.scatterGather
  val burstVld = ~sizeFifo.io.empty

  val sizeTop = sizeFifo.io.deq(0)
  val sizeInBursts = extractBurstAddr(sizeTop) + orR(extractBurstOffset(sizeTop))

  // R/W FIFO
  // TODO: Having a 1-bit FIFO is a bit ridiculous. This bit must be merged in with the
  // address FIFO so that each FIFO entry is a tuple
  val rwFifo = Module(new FIFO(1, d, v, FIFOConfig(1, 1)))
  rwFifo.io.config_enable := io.config_enable
  rwFifo.io.config_data := io.config_data
  rwFifo.io.enq := Vec.fill(v) { io.interconnect.isWr }
  rwFifo.io.enqVld := io.interconnect.vldIn

  // Data FIFO
  // Deriving the 'enqVld' signal from the other FIFO states is potentially
  // dangerous because there is no guarantee that the data input pins actually contain
  // valid data. The safest approach is to have separate enables for command (addr, size, rdwr)
  // and data.
  val dataFifo = Module(new FIFO(w, d, v, FIFOConfig(0, 0)))
  dataFifo.io.config_enable := io.config_enable
  dataFifo.io.config_data := io.config_data
  dataFifo.io.enq := io.interconnect.wdata
  dataFifo.io.enqVld := io.interconnect.dataVldIn

  // Burst offset counter
  val burstCounter = Module(new Counter(w))
  burstCounter.io.data.max := Mux(config.scatterGather, UInt(1), sizeInBursts)
  burstCounter.io.data.stride := UInt(1)
  burstCounter.io.control.reset := UInt(0)
  burstCounter.io.control.enable := burstVld
  burstCounter.io.control.saturate := UInt(0)
  addrFifo.io.deqVld := burstCounter.io.control.done
  sizeFifo.io.deqVld := burstCounter.io.control.done
  rwFifo.io.deqVld := burstCounter.io.control.done
  dataFifo.io.deqVld := rwFifo.io.deq(0) & burstVld

  // Burst Tag counter
  val burstTagCounter = Module(new Counter(log2Up(numOutstandingBursts+1)))
  burstTagCounter.io.data.max := UInt(numOutstandingBursts)
  burstTagCounter.io.data.stride := UInt(1)
  burstTagCounter.io.control.reset := UInt(0)
  burstTagCounter.io.control.enable := burstVld
  burstCounter.io.control.saturate := UInt(0)

  io.dram.addr := burstAddrs(0) + burstCounter.io.data.out
  io.dram.tagOut := Mux(config.scatterGather, burstAddrs(0), burstTagCounter.io.data.out)
  io.dram.wdata := dataFifo.io.deq
  io.dram.vldOut := burstVld
  io.dram.isWr := rwFifo.io.deq(0)

  io.interconnect.rdata := io.dram.rdata
  io.interconnect.vldOut := io.dram.vldIn
  io.interconnect.rdyOut := ~addrFifo.io.full & ~dataFifo.io.full
}

class MemoryTester (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val inst: MemoryUnitConfig) extends Module {

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnect = new PlasticineMemoryCmdInterface(w, v)
    val dram = new DRAMCmdInterface(w, v) // Should not have to pass vector width; must be DRAM burst width
  }

  val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, inst))
  mu.io.config_enable := io.config_enable
  mu.io.config_data := io.config_data
  mu.io.interconnect.rdyIn := io.interconnect.rdyIn
  mu.io.interconnect.vldIn := io.interconnect.vldIn
  io.interconnect.rdyOut := mu.io.interconnect.rdyOut
  io.interconnect.vldOut := mu.io.interconnect.vldOut
  mu.io.interconnect.addr := io.interconnect.addr
  mu.io.interconnect.wdata := io.interconnect.wdata
  mu.io.interconnect.isWr := io.interconnect.isWr
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


class MemoryUnitTests(c: MemoryTester) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

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

  def issueCmd(burstAddr: Int, isWr: Int, data: List[Int]) {
    val cmd = Cmd(burstAddr, data, isWr, expectedTag)
    expectedOrder += cmd
    incTag
  }

  def updateExpected(addr: Int, size: Int, isWr: Int, data: List[Int]) {
    val dataInBursts = getDataInBursts(data)
    val baseAddr = addr / burstSizeBytes
    val numBursts = getNumBursts(size)
    for (i <- 0 until numBursts) {
      issueCmd(baseAddr+i, isWr, if (dataInBursts.isEmpty) List() else dataInBursts.dequeue)
    }
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

  def enqueueCmd(addr: Int, size: Int, isWr: Int, data: List[Int] = List[Int]()) {
    poke(c.io.interconnect.vldIn, 1)
    poke(c.io.interconnect.addr(0), addr)
    poke(c.io.interconnect.size, size)
    poke(c.io.interconnect.isWr, isWr)

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
    updateExpected(addr, size, isWr, data)
  }

  def enqueueBurstRead(addr: Int, size: Int) {
    enqueueCmd(addr, size, 0)
  }

  def enqueueBurstWrite(addr: Int, size: Int, data: List[Int]) {
    enqueueCmd(addr, size, 1, data)
  }

  // Test constants
  val addr = 0x1000
  val waddr = 0x2000

  // Test burst mode
  // 1. If queue is empty, there must be a 'ready' signal sent to the interconnect
  expect(c.io.interconnect.rdyOut, 1)

  // 2b. Smoke test, write: Single burst with a single burst size
  val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
  enqueueBurstWrite(waddr, burstSizeBytes, wdata)
  observeFor(1)
  check("Single burst write")

  // 2a. Smoke test, read: Single burst with a single burst size
  enqueueBurstRead(addr, burstSizeBytes)
  observeFor(1)
  check("Single burst read")


  val numBursts = 10
  // 3a. Bigger smoke test, read: Single burst address with multi-burst size
  enqueueBurstRead(waddr, numBursts * burstSizeBytes)
  observeFor(numBursts+8)
  check("Single Multi-burst read")

  // 3b. Bigger smoke test, write: Single burst address with multi-burst size
  val bigWdata = List.tabulate(numBursts * wordsPerBurst) { i => 0xf00d + i }
  enqueueBurstWrite(waddr, numBursts * burstSizeBytes, bigWdata)
  observeFor(numBursts+5)
  check("Single Multi-burst write")

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

  // 5. Multiple commands - write
  val waddrs = List.tabulate(numCommands) { i => addr + i * 0x1000 }
  val wsizes = List.tabulate(numCommands) { i => burstSizeBytes * (math.abs(rnd.nextInt) % maxSizeBursts + 1) }
  waddrs.zip(rsizes) foreach { case (waddr, wsize) =>
    val wdata = List.tabulate(wsize / (c.w/8)) { i => i }
    enqueueBurstWrite(waddr, wsize, wdata)
  }
  val wCyclesToWait = wsizes.map { size =>
    (size / burstSizeBytes) + (if ((size % burstSizeBytes) > 0) 1 else 0)
  }.sum
  observeFor(wCyclesToWait)
  check("Multiple multi-burst write")


  // 5. Fill up address queue

  // 6. Fill up data queue
}

object MemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16
  val burstSizeBytes = 64

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new MemoryTester(w, d, v, numOutstandingBursts, burstSizeBytes, MemoryUnitConfig(0)))) {
      c => new MemoryUnitTests(c)
    }
  }
}

