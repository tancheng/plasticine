package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

/**
 * FIFO config register format
 */
case class FIFOOpcode(val d: Int, val v: Int, config: Option[FIFOConfig] = None) extends OpcodeT {
  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor

  var chain = if (config.isDefined) Bool(config.get.chain > 0) else Bool()

  override def cloneType(): this.type = {
    new FIFOOpcode(d, v, config).asInstanceOf[this.type]
  }
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

  // Create size register
  val sizeUDC = Module(new UpDownCtr(log2Up(d+1)))
  val size = sizeUDC.io.out
  val empty = size === UInt(0)
  val full = size === Mux(config.chain, UInt(d), UInt(bankSize))
  sizeUDC.io.initval := UInt(0)
  sizeUDC.io.init := UInt(0)

  val writeEn = io.enqVld & ~full
  val readEn = io.deqVld & ~empty
  sizeUDC.io.inc := writeEn
  sizeUDC.io.dec := readEn

  // Create wptr (tail) counter chain
  val ptrConfig = CounterChainConfig(
      List(inst.chain),
      List.tabulate(2) { i => i match {
        case 0 => // Localaddr: max = bankSize, stride = 1
          CounterRCConfig(bankSize, 1, 1, 1, 0, 0)
        case 1 => // Bankaddr: max = v, stride = 1
          CounterRCConfig(v, 1, 1, 1, 0, 0)
      }}
    )
  val wptr = Module(new CounterChain(log2Up(bankSize+1), 0, 0, 2, ptrConfig, true))
  wptr.io.control(0).enable := writeEn
  val tailLocalAddr = wptr.io.data(0).out
  val tailBankAddr = wptr.io.data(1).out

  // Create rptr (head) counter chain
  val rptr = Module(new CounterChain(log2Up(bankSize+1), 0, 0, 2, ptrConfig, true))
  rptr.io.control(0).enable := readEn
  val headLocalAddr = rptr.io.data(0).out
  val headBankAddr = rptr.io.data(1).out

  // Backing SRAM
  val mems = List.fill(v) { Module(new SRAM(w, bankSize)) }
  mems.zipWithIndex.foreach { case (m, i) =>
    // Read address
    m.io.raddr := headLocalAddr

    // Write address
    m.io.waddr := tailLocalAddr

    // Write data
    val wdata = i match {
      case 0 => io.enq(i)
      case _ => Mux(config.chain, io.enq(0), io.enq(i))
    }
    m.io.wdata := wdata

    // Write enable
    val wen = Mux(config.chain,
                    io.enqVld & tailBankAddr === UInt(i),
                    io.enqVld)
    m.io.wen := wen

    // Read data output
    val rdata = i match {
      case 0 =>
        val rdata0Mux = Module(new MuxN(v, w))
        val addrFF = Module(new FF(log2Up(v)))
        addrFF.io.data.in := headBankAddr
        addrFF.io.control.enable := Bool(true)

        rdata0Mux.io.ins := Vec(mems.map {_.io.rdata })
        rdata0Mux.io.sel := addrFF.io.data.out
        rdata0Mux.io.out
      case _ =>
        m.io.rdata
    }
    io.deq(i) := rdata
  }

  io.empty := empty
  io.full := full
}

class FIFOTests(c: FIFO) extends Tester(c) {

  val magicData = 10

  val expectedQ = Queue[Int]()  // Expected queue of size c.d

  val parQ = c.inst.chain == 0
  val maxQSize = if (parQ) c.d/c.v else c.d

  def qEmpty = if (expectedQ.isEmpty) 1 else 0
  def qFull = if (expectedQ.size == maxQSize) 1 else 0

  def headDUT = peek(c.io.deq)
  def headQ = expectedQ.head

  def enqueueBoth(elem: Int) {
    c.io.enq foreach { i => poke(i, elem) }
    poke(c.io.enqVld, 1)
    step(1)
    expectedQ += elem
    poke(c.io.enqVld, 0)
  }

  def dequeueBoth {
    poke(c.io.deqVld, 1)
    step(1)
    poke(c.io.deqVld, 0)
    val expected = expectedQ.dequeue
    if (parQ) {
      c.io.deq foreach { o => expect(o, expected) }
    } else {
      expect(c.io.deq(0), expected)
    }
  }

  def checkQStatus {
    expect(c.io.empty, qEmpty)
    expect(c.io.full, qFull)
  }

  // Smoke test with one element
  checkQStatus
  enqueueBoth(magicData)
  checkQStatus
  dequeueBoth
  checkQStatus

  // Fill up the FIFO
  for (i <- 0 until maxQSize) {
    enqueueBoth(i)
    checkQStatus
  }

  println(s"Queue: $expectedQ")

  // Pop a few, then fill up again - this tests counter wrap
  val numToPop = 5
  for (i <- 0 until numToPop) {
    dequeueBoth
    checkQStatus
  }

  val magicOffset = 100
  for (i <- 0 until numToPop) {
    enqueueBoth(i+magicOffset)
    checkQStatus
  }

  // Pop everything
  for (i <- 0 until maxQSize) {
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
    chiselMainTest(args, () => Module(new FIFO(w, d, v, FIFOConfig(0)))) {
      c => new FIFOTests(c)
    }
  }
}

