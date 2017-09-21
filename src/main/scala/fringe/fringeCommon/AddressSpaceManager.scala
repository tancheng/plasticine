package fringe

import scala.language.reflectiveCalls
import java.io.{File, PrintWriter}
import scala.collection.mutable.HashMap

class AddressSpaceManager {
  private var curAddr = 0
  private val addrMap = HashMap[String, Int]()
  private val sizeMap = HashMap[String, Int]()

  def createMapping(label: String, size: Int) = {
    Predef.assert(!addrMap.contains(label), s"ERROR: Mapping $label -> ${addrMap(label)} already exists!")
    val start = curAddr
    addrMap += s"${label}_BASE" -> start
    sizeMap += s"${label}_SIZE" -> size
    curAddr += size
    (start, size)
  }

  def addOffset(label: String, offset: Int) { addrMap += s"${label}_OFFSET" -> offset}

  def addOffsets(m: HashMap[String, Int], prefix: String = "") {
    m.keys.foreach { k => addOffset(prefix + "_" + k, m(k)) }
  }

  def getAddr(label: String) = addrMap(label)
  def getSize(label: String) = sizeMap(label)

  def createHeader(file: String) {
    val pw = new PrintWriter(new File(file))
    pw.println(s"""
#ifndef __ADDR_OFFSET_MAP_H__
#define __ADDR_OFFSET_MAP_H__

#define COMMAND_OFFSET 0
#define STATUS_OFFSET  1
// This file is auto-generated; edits are overwritten during Chisel compilation!
""")

    // Addresses
    pw.println(s"// Address map")
    addrMap.keys.foreach { k =>
      pw.println(f"#define ${k} \t0x${getAddr(k)}%08X")  // Scalar String formatting FTW !
    }
    pw.println(s"")

    // Sizes
    pw.println(s"// Sizes")
    sizeMap.keys.foreach { k =>
      pw.println(f"#define ${k} \t0x${getSize(k)}%08X")
    }
    pw.println(s"")

    pw.println("#endif // __ADDR_OFFSET_MAP_H__")
    pw.close
  }

}
