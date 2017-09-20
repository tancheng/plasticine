package fringe

import scala.language.reflectiveCalls
import java.io.{File, PrintWriter}
import scala.collection.mutable.HashMap

class AddressSpaceManager {
  private var curAddr = 0
  private val m = HashMap[String, (Int, Int)]()  // Addr, size

  def createMapping(label: String, size: Int) = {
    Predef.assert(!m.contains(label), s"ERROR: Mapping $label -> ${m(label)} already exists!")
    val start = curAddr
    m += label -> (start, size)
    curAddr += size
    (start, size)
  }

  def getAddr(label: String) = m(label)._1
  def getSize(label: String) = m(label)._2

  def createHeader(file: String) {
    val pw = new PrintWriter(new File(file))
    pw.println(s"""
  #ifndef __ADDR_OFFSET_MAP_H__
  #define __ADDR_OFFSET_MAP_H__

  // This file is auto-generated; edits are overwritten during Chisel compilation!
  """)

    m.keys.foreach { k =>
      pw.println(s"// $k address offsets")
      pw.println(f"#define ${k}_START \t0x${getAddr(k)}%08X")  // Scalar String formatting FTW !
      pw.println(f"#define ${k}_SIZE  \t0x${getSize(k)}%08X")
      pw.println(s"")
    }

    pw.println("#endif // __ADDR_OFFSET_MAP_H__")
    pw.close
  }
}
