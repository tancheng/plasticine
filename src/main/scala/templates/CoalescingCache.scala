package plasticine.templates

import chisel3._
import chisel3.util._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

/**
 * CoalescingCache memory that supports various banking modes
 * and double buffering
 * @param w: Word width in bits
 * @param d: Depth
 * @param v: Vector width
 */
class CoalescingCache(val w: Int, val d: Int, val v: Int) extends Module {
  val addrWidth = log2Up(d)
  val wordSizeBytes = w/8
  val burstSizeBytes = 64
  val burstSizeWords = burstSizeBytes / wordSizeBytes
  val taglen = w - log2Up(burstSizeBytes)

  val io = IO(new Bundle {
    val raddr = Input(UInt(w.W))
    val readEn = Input(Bool())
    val rdata = Output(Bits((burstSizeBytes * 8).W))
    val rmetaData = Output(Bits((burstSizeWords * 8).W))
    val waddr = Input(UInt(w.W))
    val wen = Input(Bool())
    val wdata = Input(Bits(w.W))
    val position = Input(UInt(log2Up(burstSizeWords).W))
    val isScatter = Input(Bool())
    val miss = Output(Bool())
    val full = Output(Bool())
  })

  // Check for sizes and v
  Predef.assert(isPow2(d), s"Unsupported scratchpad size $d; must be a power-of-2")
  Predef.assert(wordSizeBytes * v == burstSizeBytes, s"wordSize * parallelism must be equal to burst size in bytes")
  Predef.assert(isPow2(v), s"Unsupported banking number $v; must be a power-of-2")

  // Split raddr and waddr into (burstAddr, wordOffset)
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
    val wordOffset = log2Up(wordSizeBytes)
    addr(burstOffset-1, wordOffset)
  }

  val writeIdx = Wire(UInt(width = log2Up(d)))
  val readHit = Wire(Vec(d, Bool()))
  val writeHit = Wire(Vec(d, Bool()))
  val full = Wire(Bool())
  val miss = Wire(Bool())

  // Create valid array
  val valid = List.tabulate(d) { i =>
    val vld = Module(new FF(1))
    vld.io.init := UInt(0) // Initialize to invalid (0) state
    vld.io.in := Mux(readHit(i), UInt(0), UInt(1)) // Read + evict
    vld.io.enable := (io.wen & ~full & writeIdx === UInt(i)) | readHit(i) // Read + evict
    vld
  }

  val wburstAddr = extractBurstAddr(io.waddr)
  val rburstAddr = extractBurstAddr(io.raddr)
  // Create tag array, populate readHit table
  val tags = List.tabulate(d) { i =>
    val ff = Module(new FF(taglen))
    ff.io.in := wburstAddr
    ff.io.enable := (io.wen & writeIdx === UInt(i))
    readHit(i) := io.readEn & valid(i).io.out & (rburstAddr === ff.io.out)
    writeHit(i) := io.wen & valid(i).io.out & (wburstAddr === ff.io.out)
    ff
  }

  val freeBitVector = valid.map { ~_.io.out }.reverse.reduce { Cat(_,_) }
  val writeHitIdx = PriorityEncoder(writeHit.map { _.asUInt }.reverse.reduce { Cat(_,_) })
  val writeMissIdx = PriorityEncoder(freeBitVector)
  writeIdx := Mux(miss, writeMissIdx, writeHitIdx)
  val readIdx = PriorityEncoder(readHit.map { _.asUInt }.reverse.reduce { Cat(_,_) })

  miss := io.wen & ~(writeHit.reduce {_|_})
  full := valid.map { _.io.out }.reduce {_&_}
  io.miss := miss
  io.full := full

  // Metadata SRAM
  // Word size: 16 bytes (1 byte per burst)
  val metadata = Module(new SRAMByteEnable(burstSizeWords*8, d))
  metadata.io.raddr := readIdx
  io.rmetaData := metadata.io.rdata
  metadata.io.waddr := writeIdx
  metadata.io.wen := io.wen & ~full
  // If this is a write miss, wipe out entire line to avoid stale entries
  metadata.io.mask := Vec.tabulate(burstSizeWords) { i => miss | UIntToOH(io.position)(i) }
  val woffset = Cat("b00".U, extractWordOffset(io.waddr).asUInt)
  //val woffset = extractWordOffset(io.waddr).asUInt(log2Up(burstSizeWords).W)
  //val md = Cat(UInt(1, width = (8 - log2Up(burstSizeWords))), woffset)
  val md = Cat("b0001".U, woffset)
  val temp = md << Cat(io.position, "b000".U)
  //val mdShifted = UInt(md << Cat(io.position, UInt(0, width=3)), width=burstSizeWords*8)
  //val mdShifted = temp(burstSizeWords * 8 - 1, 0)
  //metadata.io.wdata := mdShifted
  metadata.io.wdata := temp

  // Scatter data SRAM. Word size: 64 bytes
  val scatterData = Module(new SRAMByteEnable(burstSizeBytes*8, d))
  scatterData.io.raddr := readIdx
  io.rdata := scatterData.io.rdata
  scatterData.io.waddr := writeIdx
  scatterData.io.wen := io.wen & ~full & io.isScatter
  scatterData.io.mask := Vec.tabulate(burstSizeBytes) { i => UIntToOH(io.position)(i/wordSizeBytes) }
  scatterData.io.wdata := io.wdata << Cat(io.position, UInt(0, width=3))
}

