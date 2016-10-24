package plasticine.templates

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import Chisel._

import plasticine.pisa.ir._

class DRAMCmdIn(w: Int, v: Int) extends AbstractMemoryCmdInterface(w, v, INPUT) {
  val addr = UInt(INPUT, width=w)
  val tagIn = UInt(INPUT, width=w)
  val tagOut = UInt(OUTPUT, width=w)
}

class IdealMemory(
  val w: Int,
  val burstSizeBytes: Int,
  val pageSizeBytes: Int
) extends Module {

  val wordSize = w/8
  val pageSizeWords = pageSizeBytes / (wordSize)

  val io = new DRAMCmdIn(w, burstSizeBytes/(w/8))

  // Rudimentary page table with (pageAddr, contents)
  val pageTable = HashMap[Int, ListBuffer[Int]]()

  def getPageAddr(addr: Int) = (addr / pageSizeBytes) * pageSizeBytes

  // Set data
  def setPage(addr: Int, data: List[Int]) {
    // Check if more than one page's worth of data is being set
    Predef.assert(data.size <= pageSizeWords, s"setPage can only be called to set data in a single page")

    // Check if input data is null
    Predef.assert(data.size > 0, s"setPage passed with data of zero length")

    // Check if address spans multiple pages
    val maxAddr = addr + (data.size * wordSize) - 1
    Predef.assert(getPageAddr(addr) == getPageAddr(maxAddr),
      s"($addr to $maxAddr) Start page ${getPageAddr(addr)} and end page ${getPageAddr(maxAddr)} of setPage are not the same")

    val baseAddr = getPageAddr(addr)
    val offset = (addr - baseAddr) / wordSize

    // If page exists, update existing page
    if (pageTable.contains(baseAddr)) {
      val pageData = pageTable(baseAddr)
      for (i <- offset until data.size) {
        pageData(i) = data(i)
      }
    } else {
      // Create new page
      val pageData = ListBuffer.tabulate(pageSizeWords) { i =>
        if (i >= offset) {
          if ((i-offset) >= data.size) 0 else data(i-offset)
        } else {
          0
        }
      }
      pageTable(baseAddr) = pageData
    }
  }

  // Maintain a 'page table'
  def setMem(addr: Int, data: List[Int], size: Int) {
    Predef.assert(data.size * wordSize == size, s"setMem: Byte size and size of data array don't match up")

    val basePageAddr = getPageAddr(addr)
    val offset = addr - basePageAddr

    // Split the address into (current page, contiguous pages, last page)
    // Then make recursive calls to set them
    val startPage = getPageAddr(addr)
    val endPage = getPageAddr(addr + size - 1)
    var curAddr = addr
    val endAddr = addr + size - 1
    while (curAddr <= endAddr) {
      val nextPage = curAddr + (pageSizeBytes - curAddr % pageSizeBytes)
      val sizeToCopy = math.min(nextPage - curAddr, endAddr - curAddr + 1)
      val elemsToCopy = sizeToCopy / wordSize
      val startWord = (curAddr - addr) / wordSize
      setPage(curAddr, data.slice(startWord, startWord+elemsToCopy))
      curAddr += sizeToCopy
    }
  }

  def getMem(addr: Int, size: Int) = {
    val startPage = getPageAddr(addr)
    val endPage = getPageAddr(addr + size - 1)
    val pageRange = startPage to endPage by pageSizeBytes
    val pageData = pageRange.map { pageTable(_) }.flatten

    val startOffset = (addr - startPage) / wordSize
    val endOffset = (pageSizeBytes - (addr + size - endPage)) / wordSize
    pageData.drop(startOffset).dropRight(endOffset)
  }

  def getBurst(burstAddr: Int) = {
    getMem(burstAddr * burstSizeBytes, burstSizeBytes)
  }

  def setBurst(burstAddr: Int, burstData: List[Int]) {
    setMem(burstAddr * burstSizeBytes, burstData, burstSizeBytes)
  }
}

class IdealMemoryTests(c: IdealMemory) extends Tester(c) {

  def randomData(size: Int) = List.fill(size) { rnd.nextInt }

  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  {
    // Set one page and retrieve it
    val testName = "singlePage"
    val addr = 0x1000
    val data = randomData(c.pageSizeWords)
    c.setMem(addr, data, c.pageSizeBytes)
    val observedData = c.getMem(addr, c.pageSizeBytes)
    if (observedData == data) printPass(testName) else printFail(testName)
  }

  {
    // Multiple pages
    val testName = "multiPage"
    val addr = 0x1000
    val numPages = 10
    val data = randomData(numPages * c.pageSizeWords)
    val size = numPages * c.pageSizeBytes
    c.setMem(addr, data, size)
    val observedData = c.getMem(addr, size)
    if (observedData == data) printPass(testName) else printFail(testName)
  }

  {
    // Crossing page boundary
    val testName = "pageBoundary"
    val addr = 0x1500
    val numPages = 2
    val data = randomData(numPages * c.pageSizeWords)
    val size = numPages * c.pageSizeBytes
    c.setMem(addr, data, size)
    val observedData = c.getMem(addr, size)
    if (observedData == data) printPass(testName) else printFail(testName)
  }

  {
    // Update existing value
    val testName = "updateExisting"
    val addr = 0x10
    val numPages = 3
    val size = numPages * c.pageSizeBytes
    val data = randomData(numPages * c.pageSizeWords)
    c.setMem(addr, data, size)
    val updateAddr = 0x20
    val updateSize = 5
    val updateData = List.tabulate(updateSize) { i => i }
    c.setMem(updateAddr, updateData, updateSize * c.wordSize)
    val observedData = c.getMem(addr, size)
    val expectedData = data.take((updateAddr - addr)/ c.wordSize) ++
                       updateData ++
                       data.takeRight(data.size - updateSize - (updateAddr - addr) / c.wordSize)
    if (observedData == expectedData) printPass(testName) else printFail(testName)
  }


}

object IdealMemoryTest {

  val w = 32
  val burstSizeBytes = 64
  val pageSizeBytes = 16

  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new IdealMemory(w, burstSizeBytes, pageSizeBytes))) {
      c => new IdealMemoryTests(c)
    }
  }
}

