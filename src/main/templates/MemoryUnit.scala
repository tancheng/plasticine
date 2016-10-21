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
  Predef.assert(w/8*v == 64, s"Unsupported combination of w=$w and v=$v; data bus width must equal DRAM burst size ($burstSizeBytes bytes)")

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
  val dataFifo = Module(new FIFO(w, d, v, FIFOConfig(0, 0)))
  dataFifo.io.config_enable := io.config_enable
  dataFifo.io.config_data := io.config_data
  dataFifo.io.enq := io.interconnect.wdata
  dataFifo.io.enqVld := io.interconnect.vldIn & io.interconnect.isWr

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

class MemoryUnitTests(c: MemoryUnit) extends Tester(c) {

  def enqueueCmd(addr: Int, size: Int, isWr: Int, data: List[Int] = List[Int]()) {
    poke(c.io.interconnect.vldIn, 1)
    poke(c.io.interconnect.addr(0), addr)
    poke(c.io.interconnect.size, size)
    poke(c.io.interconnect.isWr, isWr)

    if (isWr > 0) c.io.interconnect.wdata.zip(data) foreach { case (in, i) => poke(in, i) }
    step(1)
    poke(c.io.interconnect.vldIn, 0)
  }

  def enqueueBurstRead(addr: Int, size: Int) {
    enqueueCmd(addr, size, 0)
  }

  def enqueueBurstWrite(addr: Int, size: Int, data: List[Int]) {
    enqueueCmd(addr, size, 1, data)
  }

  // Test burst mode
  // 1. If queue is empty, there must be a 'ready' signal sent to the interconnect
  expect(c.io.interconnect.rdyOut, 1)

  // 2a. Smoke test, read: Single burst with a single burst size
  val addr = 0x1000
  val size = 64
  enqueueBurstRead(addr, size)
  step(1)
  // TODO: Add verification conditions here!

  // 2b. Smoke test, write: Single burst with a single burst size
  val waddr = 0x2000
  val wdata = List.tabulate(c.v) { i => i + 0xcafe }
  enqueueBurstWrite(waddr, size, wdata)
  step(1)

  // 3a. Bigger smoke test, read: Single burst address with multi-burst size
  // 3b. Bigger smoke test, write: Single burst address with multi-burst size
  // 4. Multiple commands - read
  // 5. Fill up address queue
  // 6. Fill up data queue
}

object MemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new MemoryUnit(w, d, v, numOutstandingBursts, MemoryUnitConfig(0)))) {
      c => new MemoryUnitTests(c)
    }
  }
}

