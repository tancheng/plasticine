package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
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

  val io = new Bundle {
    val raddr = UInt(INPUT, width = w)
    val readEn = Bool(INPUT)
    val rdata = Bits(OUTPUT, width = burstSizeBytes * 8)
    val rmetaData = Bits(OUTPUT, width = burstSizeWords * 8)
    val waddr = UInt(INPUT, width = w)
    val wen = Bool(INPUT)
    val wdata = Bits(INPUT, width = w)
    val position = UInt(INPUT, width = log2Up(burstSizeWords))
    val isScatter = Bool(INPUT)
    val miss = Bool(OUTPUT)
    val full = Bool(OUTPUT)
  }

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

  val writeIdx = UInt(width = log2Up(d))
  val readHit = Vec.fill(d) { Bool() }
  val writeHit = Vec.fill(d) { Bool() }
  val full = Bool()
  val miss = Bool()

  // Create valid array
  val valid = List.tabulate(d) { i =>
    val vld = Module(new FF(1))
    vld.io.data.init := UInt(0) // Initialize to invalid (0) state
    vld.io.data.in := Mux(readHit(i), UInt(0), UInt(1)) // Read + evict
    vld.io.control.enable := (io.wen & ~full & writeIdx === UInt(i)) | readHit(i) // Read + evict
    vld
  }

  val wburstAddr = extractBurstAddr(io.waddr)
  val rburstAddr = extractBurstAddr(io.raddr)
  // Create tag array, populate readHit table
  val tags = List.tabulate(d) { i =>
    val ff = Module(new FF(taglen))
    ff.io.data.in := wburstAddr
    ff.io.control.enable := (io.wen & writeIdx === UInt(i))
    readHit(i) := io.readEn & valid(i).io.data.out & (rburstAddr === ff.io.data.out)
    writeHit(i) := io.wen & valid(i).io.data.out & (wburstAddr === ff.io.data.out)
    ff
  }

  val freeBitVector = valid.map { ~_.io.data.out }.reverse.reduce { Cat(_,_) }
  val writeHitIdx = PriorityEncoder(writeHit.map { UInt(_) }.reverse.reduce { Cat(_,_) })
  val writeMissIdx = PriorityEncoder(freeBitVector)
  writeIdx := Mux(miss, writeMissIdx, writeHitIdx)
  val readIdx = PriorityEncoder(readHit.map { UInt(_) }.reverse.reduce { Cat(_,_) })

  miss := io.wen & ~(writeHit.reduce {_|_})
  full := valid.map { _.io.data.out }.reduce {_&_}
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
  val woffset = UInt(extractWordOffset(io.waddr), width=log2Up(burstSizeWords))
  val md = Cat(UInt(1, width = (8 - log2Up(burstSizeWords))), woffset)
  val mdShifted = UInt(md << Cat(io.position, UInt(0, width=3)), width=burstSizeWords*8)
  metadata.io.wdata := mdShifted

  // Scatter data SRAM. Word size: 64 bytes
  val scatterData = Module(new SRAMByteEnable(burstSizeBytes*8, d))
  scatterData.io.raddr := readIdx
  io.rdata := scatterData.io.rdata
  scatterData.io.waddr := writeIdx
  scatterData.io.wen := io.wen & ~full & io.isScatter
  scatterData.io.mask := Vec.tabulate(burstSizeBytes) { i => UIntToOH(io.position)(i/wordSizeBytes) }
  scatterData.io.wdata := io.wdata << Cat(io.position, UInt(0, width=3))
}

class CoalescingCacheTests(c: CoalescingCache) extends Tester(c) {
  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  def expect(e: BigInt, o: Int, msg: String = "") = {
    if (e == o) printPass(msg) else printFail(msg)
  }

  val expectedCache = HashMap[Int, ListBuffer[(Int, Int)]]()
  val issuedLoads = Set[Int]()

  def getBurstAddr(addr: Int) = addr / c.burstSizeBytes
  def getWordOffset(addr: Int) = (addr % c.burstSizeBytes) / c.wordSizeBytes
  def writeGatherAddr(addr: Int, pos: Int) = {
    val burstAddr = getBurstAddr(addr)
    val wordOffset = getWordOffset(addr)
    poke(c.io.waddr, addr)
    poke(c.io.wen, 1)
    poke(c.io.position, pos)
    poke(c.io.isScatter, 0)

    val expectedMiss = if (expectedCache.contains(burstAddr)) 0 else 1
    if (!expectedCache.contains(burstAddr)) {
      expectedCache(burstAddr) = ListBuffer.fill(c.burstSizeWords) { (0,0) }
    }
    expectedCache(burstAddr)(pos) = (1, wordOffset)
    val miss = peek(c.io.miss).toInt
    step(1)
    poke(c.io.wen, 0)
    expect(expectedMiss, miss, s"[writeGatherAddr] addr=$addr, pos=$pos, expectedMiss = $expectedMiss, miss = $miss")
    if (expectedMiss > 0) issuedLoads += addr
    miss
  }

  def readMetadata(addr: Int) = {
    poke(c.io.raddr, addr)
    poke(c.io.readEn, 1)
    step(1)
    poke(c.io.readEn, 0)
    val expected = expectedCache(getBurstAddr(addr))
    val observed = parseMetadataLine(peek(c.io.rmetaData))
    println(s"[readMetadata] addr=$addr, expected: $expected, observed: $observed")
    expected.zip(observed) foreach { case (e, o) =>
      expect(BigInt(e._1), o._1, s"[readMetadata] addr=$addr, expected $e, observed $o")
      expect(BigInt(e._2), o._2, s"[readMetadata] addr=$addr, expected $e, observed $o")
    }
    expectedCache -= getBurstAddr(addr)
  }

  def parseMetadataLine(m: BigInt) = {
    // One Metadata line contains one byte per burst word
    // => burstSizeWords bytes
    val metadataList = List.tabulate(c.burstSizeWords) { i => ((m >> (i*8)) & 0xFF).toInt }
    metadataList.map { parseMetadata(_) }
  }

  def parseMetadata(m: Int) = {
    // Metadata is an 8-bit word
    // Last 'wordOffset' bits contain the word offset
    val wordOffsetMask = (1 << log2Up(c.burstSizeWords)) - 1
    val lsb = m & 0xFF
    val wordOffset = lsb & wordOffsetMask
    val valid = lsb >> log2Up(c.burstSizeWords)
//    println(s"[parseMetadata] m = $m, lsb = $lsb, wordOffsetMask = $wordOffsetMask, wordOffset = $wordOffset, valid = $valid")
    (valid, wordOffset)
  }

  // Simple write + read
  var miss = writeGatherAddr(0x1004, 0)
  var rmetadata = readMetadata(0x1004)
  issuedLoads.clear

  // Multiple same addresses - should all hit in the cache line
  {
    val gatherVector = List.tabulate(c.burstSizeWords) { i => 0x1030 }
    gatherVector.zipWithIndex.foreach { case (g, i) => writeGatherAddr(g, i) }
    issuedLoads.foreach { addr => readMetadata(addr) }
    issuedLoads.clear
  }
  {
    val gatherVector = List.tabulate(c.burstSizeWords) { i => 0x1000 + math.abs(rnd.nextInt % 0x1000) }
    gatherVector.zipWithIndex.foreach { case (g, i) => writeGatherAddr(g, i) }
    issuedLoads.foreach { addr => readMetadata(addr) }
    issuedLoads.clear
  }
}

object CoalescingCacheTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val w = 32
    val d = 16
    val v = 16
//    val d = 64
//    val v = 4

    chiselMainTest(args, () => Module(new CoalescingCache(w, v, d))) {
      c => new CoalescingCacheTests(c)
    }
  }
}

