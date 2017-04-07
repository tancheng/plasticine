package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

/**
 * FIFO config register format
 */
case class FIFOOpcode(val d: Int, val v: Int, config: Option[FIFOConfig] = None) extends OpcodeT {
  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor

  var chainWrite = if (config.isDefined) Bool(config.get.chainWrite > 0) else Bool()
  var chainRead = if (config.isDefined) Bool(config.get.chainRead > 0) else Bool()

  override def cloneType(): this.type = {
    new FIFOOpcode(d, v, config).asInstanceOf[this.type]
  }
}

class FIFOCore(val w: Int, val d: Int, val v: Int) extends Module {
  val addrWidth = log2Up(d/v)
  val bankSize = d/v
  // Check for sizes and v
  Predef.assert(d%v == 0, s"Unsupported FIFO size ($d)/banking($v) combination; $d must be a multiple of $v")
  Predef.assert(isPow2(v), s"Unsupported banking number $v; must be a power-of-2")
  Predef.assert(isPow2(d), s"Unsupported FIFO size $d; must be a power-of-2")

  val io = new Bundle {
    val enq = Vec.fill(v) { Bits(INPUT, width = w) }
    val enqVld = Bool(INPUT)
    val deq = Vec.fill(v) { Bits(OUTPUT, width = w) }
    val deqVld = Bool(INPUT)
    val full = Bool(OUTPUT)
    val empty = Bool(OUTPUT)
    val chainWrite = Bool(INPUT)
    val chainRead = Bool(INPUT)
  }

  // Create size register
  val sizeUDC = Module(new UpDownCtr(log2Up(d+1)))
  val size = sizeUDC.io.out
  val empty = size === UInt(0)
  val full = sizeUDC.io.isMax
  sizeUDC.io.initval := UInt(0)
  sizeUDC.io.max := UInt(d)
  sizeUDC.io.init := UInt(0)
  sizeUDC.io.strideInc := Mux(io.chainWrite, UInt(1), UInt(v))
  sizeUDC.io.strideDec := Mux(io.chainRead, UInt(1), UInt(v))
  sizeUDC.io.init := UInt(0)

  val writeEn = io.enqVld & ~full
  val readEn = io.deqVld & ~empty
  sizeUDC.io.inc := writeEn
  sizeUDC.io.dec := readEn

  // Create wptr (tail) counter chain
  val writePtrConfig = List.tabulate(2) { i => i match {
        case 1 => // Localaddr: max = bankSize, stride = 1
          CounterRCConfig(bankSize, 1, 1, 1, 0, 0)
        case 0 => // Bankaddr: max = v, stride = 1
          CounterRCConfig(v, 1, 1, 1, 0, 0)
      }}

  val readPtrConfig = List.tabulate(2) { i => i match {
        case 1 => // Localaddr: max = bankSize, stride = 1
          CounterRCConfig(bankSize, 1, 1, 1, 0, 0)
        case 0 => // Bankaddr: max = v, stride = 1
          CounterRCConfig(v, 1, 1, 1, 0, 0)
      }}

  val wptr = Module(new CounterChainCore(log2Up(bankSize+1), 0, 0, 2, writePtrConfig, true))
  wptr.io.chain(0) := io.chainWrite
  wptr.io.control(0).enable := writeEn & io.chainWrite
  wptr.io.control(1).enable := writeEn
  val tailLocalAddr = wptr.io.data(1).out
  val tailBankAddr = wptr.io.data(0).out

  // Create rptr (head) counter chain
  val rptr = Module(new CounterChainCore(log2Up(bankSize+1), 0, 0, 2, readPtrConfig, true))
  rptr.io.chain(0) := io.chainRead
  rptr.io.control(0).enable := readEn & io.chainRead
  rptr.io.control(1).enable := readEn
  val headLocalAddr = rptr.io.data(1).out
  val nextHeadLocalAddr = Mux(io.chainRead, Mux(rptr.io.control(0).done, rptr.io.data(1).next, rptr.io.data(1).out), rptr.io.data(1).next)
  val headBankAddr = rptr.io.data(0).out
  val nextHeadBankAddr = rptr.io.data(0).next

  // Backing SRAM
  val mems = List.fill(v) { Module(new SRAM(w, bankSize)) }
  mems.zipWithIndex.foreach { case (m, i) =>
    // Read address
    m.io.raddr := Mux(readEn, nextHeadLocalAddr, headLocalAddr)

    // Write address
    m.io.waddr := tailLocalAddr

    // Write data
    val wdata = i match {
      case 0 => io.enq(i)
      case _ => Mux(io.chainWrite, io.enq(0), io.enq(i))
    }
    m.io.wdata := wdata

    // Write enable
    val wen = Mux(io.chainWrite,
                    io.enqVld & tailBankAddr === UInt(i),
                    io.enqVld)
    m.io.wen := wen

    // Read data output
    val rdata = i match {
      case 0 =>
        val rdata0Mux = Module(new MuxN(v, w))
        val addrFF = Module(new FF(log2Up(v)))
        addrFF.io.data.in := Mux(readEn, nextHeadBankAddr, headBankAddr)
        addrFF.io.control.enable := Bool(true)

        rdata0Mux.io.ins := Vec(mems.map {_.io.rdata })
        rdata0Mux.io.sel := Mux(io.chainRead, addrFF.io.data.out, UInt(0))
        rdata0Mux.io.out
      case _ =>
        m.io.rdata
    }
    io.deq(i) := rdata
  }

  io.empty := empty
  io.full := full
}

class FIFO(val w: Int, val d: Int, val v: Int, val inst: FIFOConfig) extends ConfigurableModule[FIFOOpcode] {
  val addrWidth = log2Up(d/v)
  val bankSize = d/v

  // Check for sizes and v
  Predef.assert(d%v == 0, s"Unsupported FIFO size ($d)/banking($v) combination; $d must be a multiple of $v")
  Predef.assert(isPow2(v), s"Unsupported banking number $v; must be a power-of-2")
  Predef.assert(isPow2(d), s"Unsupported FIFO size $d; must be a power-of-2")

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val enq = Vec.fill(v) { Bits(INPUT, width = w) }
    val enqVld = Bool(INPUT)
    val deq = Vec.fill(v) { Bits(OUTPUT, width = w) }
    val deqVld = Bool(INPUT)
    val full = Bool(OUTPUT)
    val empty = Bool(OUTPUT)
  }

  val configType = FIFOOpcode(d, v)
  val configIn = FIFOOpcode(d, v)
  val configInit = FIFOOpcode(d, v, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  val fifo = Module(new FIFOCore(w, d, v))
  fifo.io.chainWrite := config.chainWrite
  fifo.io.chainRead := config.chainRead
  fifo.io.enq := io.enq
  fifo.io.enqVld := io.enqVld
  fifo.io.deqVld := io.deqVld
  io.deq := fifo.io.deq
  io.full := fifo.io.full
  io.empty := fifo.io.empty
}

class FIFOTests(c: FIFO) extends Tester(c) {

  val magicData = 12

  val expectedQ = Queue[Int]()  // Expected queue of size c.d

  val writeParQ = c.inst.chainWrite == 0
  val readParQ = c.inst.chainRead == 0
  val maxQSize = c.d

  def qEmpty = if (expectedQ.isEmpty) 1 else 0
  def qFull = if (writeParQ) {
    if ((expectedQ.size + c.v) > maxQSize) 1 else 0
  } else {
    if (expectedQ.size == maxQSize) 1 else 0
  }

  def headDUT = peek(c.io.deq)
  def headQ = expectedQ.head

  def enqueueBoth(elem: Int) {
    if (writeParQ) {
      c.io.enq foreach { i => poke(i, elem) }
      (0 until c.v) foreach { i => expectedQ += elem }
    } else {
      poke(c.io.enq(0), elem)
      expectedQ += elem
    }
    poke(c.io.enqVld, 1)
    step(1)
    poke(c.io.enqVld, 0)
  }

  def dequeueBoth {
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    if (readParQ) {
      val old = List.fill(c.v) { expectedQ.dequeue }
    } else {
      val old = expectedQ.dequeue
    }
    checkQStatus
  }

  def checkQStatus {
    expect(c.io.empty, qEmpty)
    expect(c.io.full, qFull)
    if (qEmpty == 0) {
      if (readParQ) {
        val expected = List.tabulate(c.v) { expectedQ(_) }
        c.io.deq.zip(expected) foreach { case (o, e) => expect(o, e) }
      } else {
        val expected = expectedQ.head
        expect(c.io.deq(0), expected)
      }
    }
  }

  // Smoke test with one element
  checkQStatus
  enqueueBoth(magicData)
  checkQStatus
  dequeueBoth
  checkQStatus

  // Fill up the FIFO
  var i = 0
  while (qFull != 1) {
    enqueueBoth(i)
    checkQStatus
    i += 1
  }

  println(s"Queue: $expectedQ")

  // Pop a few, then fill up again - this tests counter wrap
  val numToPop = 4
  for (i <- 0 until numToPop) {
    dequeueBoth
    checkQStatus
  }

  val magicOffset = 100
  for (i <- 0 until numToPop/c.v) {
    enqueueBoth(i+magicOffset)
    checkQStatus
  }

  // Pop everything
  while (qEmpty != 1) {
    dequeueBoth
    checkQStatus
  }

  checkQStatus
}

object FIFOTest {
  val w = 16
  val v = 2
  val d = 16

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new FIFO(w, d, v, FIFOConfig(0, 1)))) {
      c => new FIFOTests(c)
    }
  }
}

