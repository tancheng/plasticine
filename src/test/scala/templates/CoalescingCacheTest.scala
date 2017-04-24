package plasticine.templates
import plasticine.templates.CommonMain

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.Set

class CoalescingCacheTester(c: CoalescingCache)(implicit args: Array[String]) extends ArgsTester(c) {

  def expect2(e: BigInt, o: Int, msg: String = "") = {
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
    expect2(expectedMiss, miss, s"[writeGatherAddr] addr=$addr, pos=$pos, expectedMiss = $expectedMiss, miss = $miss")
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
      expect2(BigInt(e._1), o._1, s"[readMetadata] addr=$addr, expected $e, observed $o")
      expect2(BigInt(e._2), o._2, s"[readMetadata] addr=$addr, expected $e, observed $o")
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
  // Multiple random addresses
  {
    val gatherVector = List.tabulate(c.burstSizeWords) { i => 0x1000 + math.abs(rnd.nextInt % 0x1000) }
    gatherVector.zipWithIndex.foreach { case (g, i) => writeGatherAddr(g, i) }
    issuedLoads.foreach { addr => readMetadata(addr) }
    issuedLoads.clear
  }
  // Fill up cache
  {
    val gatherVector = List.tabulate(c.burstSizeWords) { i => 0x1000 * 10 * i }
    gatherVector.zipWithIndex.foreach { case (g, i) => writeGatherAddr(g, i) }
    expect2(peek(c.io.full).toInt, 1)
    issuedLoads.foreach { addr => readMetadata(addr) }
    expect2(peek(c.io.full).toInt, 0)
    issuedLoads.clear
  }
}

object CoalescingCacheTest extends CommonMain {
  type DUTType = CoalescingCache

  def dut = () => {
    val w = args(0).toInt
    val d = args(1).toInt
    val v = args(2).toInt
    new CoalescingCache(w, d, v)
  }

  def tester = { c: DUTType => new CoalescingCacheTester(c) }
}

