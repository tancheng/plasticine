package plasticine.pisa

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.mutable.HashMap
import scala.util.Random
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import plasticine.templates.Opcodes
import Chisel._

object Frontend {
  def main(args: Array[String]) = {
    if (args.size <= 2) {
      println("Usage: Frontend <JSON file>")
      sys.exit(-1)
    }

    val file = args(0)
    val parsedNode: AbstractConfig = Parser(file)
    println(s"Parsed successfully, parsedNode = $parsedNode")
  }

}
