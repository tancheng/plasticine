package plasticine.pisa

import plasticine.spade._
import plasticine.pisa.ir._
import plasticine.pisa.codegen._
//import plasticine.spade.GeneratedTopParams.plasticineParams._
import plasticine.spade.GeneratedTopParams
import plasticine.config._

import java.io._

import scala.collection.mutable.ListBuffer

trait ConfigFileInterface {

  // Pack elements in 'data' (1-bit values) to a wider (8-bit) type
  // so that it can be written to a config file
  def pack(data: List[Int]): List[Int] = {
    // First, padd data to make it a multiple of 8
    val padded = data ++ List.fill(8 - data.size % 8) { 0 }

    // Pack from LSB to MSB
    List.tabulate(padded.size / 8) { i =>
      val bits = padded.slice(i*8, i*8+8)
      bits.zipWithIndex.map { case (bit, idx) => bit << idx }.reduce {_|_}
    }
  }

  // UnPack 8-bit elements in 'data' to 1-bit values
  def unpack(data: List[Int]): List[Int] = {
    // Unpack from LSB to MSB
    List.tabulate(data.size) { i =>
      List.tabulate(8) { j => (data(i) >> j) & 0x1 }
    }.flatten
  }

  def toFile(outFileName: String, data: List[Int]) {
    val packed = pack(data)
    val os = new DataOutputStream(new FileOutputStream(outFileName))
    packed.foreach { os.writeByte(_) }
    os.flush
    os.close
  }

  def fromFile(inFileName: String): List[Int] = {
    val istream = new DataInputStream(new FileInputStream(inFileName))
    val lb = ListBuffer[Int]()
    var eof = false
    while (!eof) {
      try {
        lb.append(istream.readUnsignedByte())
      } catch {
        case e: EOFException => eof = true // EOF reached
      }
    }
    istream.close
    lb.toList
  }
}

trait PISADesign extends ConfigFileInterface {
  def main(args: String*): AbstractBits

  def getPlasticineConfigTop = {
    val p = GeneratedTopParams.plasticineParams
    val f = GeneratedTopParams.fringeParams
    val cuParams = p.cuParams
    val vectorParams = p.vectorSwitchParams
    val scalarParams = p.scalarSwitchParams
    val controlParams = p.controlSwitchParams
    val switchCUParams = p.switchCUParams

    new PlasticineConfig(cuParams, vectorParams, scalarParams, controlParams, switchCUParams, p, f)
  }

  def getConfigTop(node: AbstractBits): AbstractConfig = node match {
    case p: PlasticineBits => getPlasticineConfigTop
    case p: CounterRCBits  => new CounterConfig(GeneratedTopParams.plasticineParams.w, 0, 0)
    case p: PCUBits        => new PCUConfig(GeneratedTopParams.plasticineParams.cuParams(0)(0).asInstanceOf[PCUParams])
    case _ => throw new Exception(s"[getConfigTop] Unsupported '$node'")
  }

	def main(args: Array[String]): Unit = {
		val top = main(args:_*)
		val configTop = getConfigTop(top)

		// Generate binary
    val binaryGen = new BinaryCodegen()
		val binData = binaryGen.genBinary(top, configTop)

    val fileName = "accel.bit.bin"

    // Print to file
    toFile(fileName, binData)

    // Read from file
//    val readBinData = unpack(fromFile(fileName))
//
//
//    println(s"binData[size = ${binData.size}] = $binData")
//    println(s"readBinData[size = ${readBinData.size}] = $readBinData")
//
//    // Check: Print both
//    binData.zipWithIndex.foreach { case (bit, i) =>
//      if (bit != readBinData(i)) {
//        println(s"ERROR at pos $i: Expected $bit, found ${readBinData(i)}")
//      }
//    }
	}
}
