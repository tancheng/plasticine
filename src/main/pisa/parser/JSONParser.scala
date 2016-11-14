package plasticine.pisa.parser

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.mutable.HashMap
import scala.util.Random
import plasticine.ArchConfig
import plasticine.pisa.ir._
import plasticine.templates.Opcodes
import plasticine.templates.{DirectionOps, InterconnectHelper, CtrlInterconnectHelper}
import Chisel._

object Parser {
  def apply(path: String) = {
    val file = new File(path)
    assert(file.isFile, path+ " does not exist")
    val contents = scala.io.Source.fromFile(file).mkString
    import JSON._

    phrase(root)(new lexical.Scanner(contents)) match {
      case Success(result,_) => buildFromParsedJSON(resolveType(result))
      case f@NoSuccess(_,_) => throw new RuntimeException("Couldn't parse PISA file:\n" + f.toString)
    }
  }

  def parseRaw(path: String) = {
    val file = new File(path)
    assert(file.isFile, path+ " does not exist")
    val contents = scala.io.Source.fromFile(file).mkString
    import JSON._

    phrase(root)(new lexical.Scanner(contents)) match {
      case Success(result,_) => resolveType(result) match {
        case m: Map[Any, Any] => m
        case _ => throw new RuntimeException(s"Couldn't parse PISA file: $path")
      }
      case f@NoSuccess(_,_) => throw new RuntimeException("Couldn't parse PISA file:\n" + f.toString)
    }
  }

  def buildFromParsedJSON(json: Any) = {
    json match {
      case m: Map[Any, Any] =>
        val pisam = getFieldMap(m, "PISA")
        parsePISAMap(pisam)
      case err@_ => mapNotFound(err)
    }
  }

  def parsePISAMap(m: Map[Any, Any]) = {
    val vs = getFieldDouble(m, "version")
    val configMap = getFieldMap(m, "topconfig")
    val configObj = parseConfigMap(configMap)
    configObj
  }

  def parseCounterRC(m: Map[Any, Any]): CounterRCConfig = {
    isDontCare(m) match {
      case true =>
        CounterRCConfig.zeroes
      case false =>
        val maxStr = Parser.getFieldString(m, "max")
        val strideStr = Parser.getFieldString(m, "stride")

        def checkAndCastToInt(s: String): Int = s.last match {
          case 'i' => s.dropRight(1).toInt
          case _ => throw new Exception(s"Invalid const type ${s.last}")
        }

        def parseSrcValue(s: String) = {
          s(0) match {
            case 'x' => (0, 0)   // Don't care
            case 'c' => (checkAndCastToInt(s.drop(1)), 1) // Constant value
            case 'e' => (checkAndCastToInt(s.drop(1)), 0) // From empty stage
            case _ => throw new Exception("Unknown source for max/stride " + s(0))
          }
        }

        val (max, maxConst) = parseSrcValue(maxStr)
        val (stride, strideConst) = parseSrcValue(strideStr)

        val startDelay: Int = Parser.getFieldString(m, "startDelay") match {
          case "x" => 0 // Don't care
          case n@_ => 1 + n.toInt
        }

        val endDelay: Int = Parser.getFieldString(m, "endDelay") match {
          case "x" => 0 // Don't care
          case n@_ => 1 + n.toInt
        }
        CounterRCConfig(max, stride, maxConst, strideConst, startDelay, endDelay)
    }

  }

  def parseCounterChain(m: Map[Any, Any]): CounterChainConfig = {
    val chain: List[Int] = Parser.getFieldList(m, "chain")
                                        .asInstanceOf[List[String]]
                                        .map { i => i.toInt }

    val counters: List[CounterRCConfig] = Parser.getFieldListOfMaps(m, "counters")
                                         .map { parseCounterRC(_) }
    CounterChainConfig(chain, counters)
  }

  def parseValue(x: String, incByOne: Boolean = false):Int = x(0) match {
    case 'x' => 0
    case _ => if (incByOne) Integer.parseInt(x) + 1 else Integer.parseInt(x)
  }

  def encodeOneHot(x: Int) = 1 << x

  def getRegNum(s: String) = if (s.size <= 1) 0 else {
    val value = s.drop(1)
    value.last match {
      case 'i' => Integer.parseInt(value.dropRight(1))
//      case 'f' => java.lang.Float.parseFloat(value.dropRight(1))
//      case 'd' => java.lang.Double.parseDouble(value.dropRight(1))
      case _ => value.toInt
    }
  }


  def parseLUT(m: Map[Any, Any]): LUTConfig = isDontCare(m) match {
    case true => LUTConfig.zeroes(10) // Some random hardcoded number
    case false =>
      val values = Parser.getFieldList(m, "table")
      isDontCare(values) match {
        case true => LUTConfig.zeroes(10) // Some random hardcoded number
        case false =>
          val table: List[Int] = values.asInstanceOf[List[String]]
                                       .map { s => if (isDontCare(s)) 0 else parseValue(s) }
          LUTConfig(table)

      }
  }

  def parseOperandConfig(s: String): OperandConfig = {

    // (0..rwStages-1)
    // x l r c i m e
    // (rwStages)
    // x l r c - m e
    // (normal stages)
    // x l r c
    // (reduction stages)
    // x l r c t
    def getDataSrc = s(0) match {
      case 'x' => 0 // Don't care (must eventually be turned off)
      case 'l' => 0 // Local register
      case 'r' => 1 // Previous pipe stage register
      case 'c' => 2 // Constant
      case 'i' => 3 // Iterator / counter
      case 't' => 3 // Cross-stage value for reduction tree
      case 'm' => 4 // Memory
      case 'e' => 5 // Empty stage
      case _ => throw new Exception(s"Unknown data source '${s(0)}'. Must be l, r, c, i, or m")
    }
    val dataSrc = getDataSrc
    val value = getRegNum(s)
    OperandConfig(dataSrc, value)
  }

  def parsePipeStage(m: Map[Any, Any]): PipeStageConfig = {
    val opA = parseOperandConfig(Parser.getFieldString(m, "opA"))
    val opB = parseOperandConfig(Parser.getFieldString(m, "opB"))
    val opC = parseOperandConfig(Parser.getFieldString(m, "opC"))
    val opcode = {
      val o = Parser.getFieldString(m, "opcode")
      if (o == "x") 0 else Opcodes.getCode(o)
    }
    val result = (((Parser.getFieldList(m, "result")
                  .asInstanceOf[List[String]])
                  .map { r => encodeOneHot(getRegNum(r)) })
                  ++ List(0)) // To handle empty /don't care stages
                  .reduce {_|_}

    // Map (regNum -> muxconfig)
    val fwd: Map[Int, Int] = {
      val fwdMap = Parser.getFieldMap(m, "fwd")
      val t = HashMap[Int, Int]()
      fwdMap.keys.foreach { reg =>
        val source = fwdMap(reg)
        val regNum = getRegNum(reg.toString)
        t(regNum) = source match {
          case "i" => 1 // Same number because counter and memory contents don't overlap
          case "m" => 1
          case "e" => 1
          case _ => 0
        }
      }
      t.toMap
    }
    PipeStageConfig(opA, opB, opC, opcode, result, fwd)
  }

  def parseCU(m: Map[Any, Any]): ComputeUnitConfig = isDontCare(m) match {
    case true =>
      ComputeUnitConfig.zeroes(10, 8, 8, 8, 4) // hardcoded config values
    case false =>
      val counterChain = parseCounterChain(Parser.getFieldMap(m, "counterChain"))
      val scalarXbar = parseCrossbar(Parser.getFieldMap(m, "scalarInXbar"))

      val scratchpads =  Parser.getFieldListOfMaps(m, "scratchpads")
                                        .map { parseScratchpad(_) }

      val pipeStage = Parser.getFieldListOfMaps(m, "pipeStage")
                                .drop(1)
                                .map { h => parsePipeStage(h) }

      val control = parseControlBox(Parser.getFieldMap(m, "control"))

      ComputeUnitConfig(counterChain, scalarXbar, scratchpads, pipeStage, control)
  }

  def parseCrossbar(m: Map[Any, Any], incByOne: Boolean = false): CrossbarConfig = {
    val outSelect: List[Int] = Parser.getFieldList(m, "outSelect")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_, incByOne) }

    CrossbarConfig(outSelect)
  }

  def parseControlBox(m: Map[Any, Any]): CUControlBoxConfig = {
    val tokenOutLUT: List[LUTConfig] = Parser.getFieldListOfMaps(m, "tokenOutLUT")
                                            .map { parseLUT(_) }
    val enableLUT: List[LUTConfig] = Parser.getFieldListOfMaps(m, "enableLUT")
                                            .map { parseLUT(_) }
    val tokenDownLUT: List[LUTConfig] =Parser.getFieldListOfMaps(m, "tokenDownLUT")
                                            .map { parseLUT(_) }
    val udcInit: List[Int] = Parser.getFieldList(m, "udcInit")
                                .asInstanceOf[List[String]]
                                .map { parseValue(_) }
    val decXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "decXbar"), true)
    val incXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "incXbar"), true)
    val tokenInXbar: CrossbarConfig = parseCrossbar(Parser.getFieldMap(m, "tokenInXbar"))
    val doneXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "doneXbar"))
    val enableMux: List[Boolean] = Parser.getFieldList(m, "enableMux")
                                .asInstanceOf[List[String]]
                                .map { parseValue(_) > 0 }

    val syncTokenMux: List[Int] = Parser.getFieldListOfMaps(m, "tokenDownLUT")
      .map { tokenDownMap => isDontCare(tokenDownMap) match {
          case true => parseValue("x")
          case false => parseValue(Parser.getFieldString(tokenDownMap, "syncTokenMux"))
        }
      }
    val tokenOutXbar: CrossbarConfig = parseCrossbar(Parser.getFieldMap(m, "tokenOutXbar"), true)
    CUControlBoxConfig(tokenOutLUT, enableLUT, tokenDownLUT, udcInit, decXbar, incXbar, tokenInXbar, doneXbar, enableMux, syncTokenMux, tokenOutXbar)
  }

  def parseBankingConfig(s: String): BankingConfig = {
    def parseMode = s(0) match {
      case 'x' => 0 // No banking
      case 'b' => 1 // Strided
      case 'd' => 2 // Diagonal
      case _ => throw new Exception(s"Unsupported banking mode ''$s'")
    }
    def parseStride = {
      if (s.size == 1) 0
      else {
        val stride = Integer.parseInt(s.drop(1))
        if (stride == 1) 0 else log2Up(stride)
      }
    }
     val mode = parseMode
     val strideLog2 = parseStride
    BankingConfig(mode, strideLog2)
  }

  def isDontCare(value: Any): Boolean = value match {
    case m: Map[Any,Any] => m.get("dontCare") match {
      case Some(field) => true
      case _ => false
    }
    case l: List[Any] => l.contains("dontCare")
    case s: String => s == "dontCare"
    case _ => throw new Exception(s"Don't know how to check don't care for value $value")
  }

  def parseScratchpad(m: Map[Any, Any]): ScratchpadConfig = {
    isDontCare(m) match {
      case true =>
        ScratchpadConfig.zeroes
      case false =>
        // Banking stride
        def parseAddrSource(x: String) = {
          val src = x(0) match {
            case 'x' => 0  // Don't care
            case 's' => 0  // Stage
            case 'i' => 1  // Iterator
            case 'l' => 2  // Last stage - only for write addr. TODO: Error out for reads
            case _ => throw new Exception(s"Unknwon address source ${x(0)}; must be one of s, i, or l")
         }
         SrcValueTuple(src, if (x == "x") 0 else x.drop(1).toInt)
        }

        val wa = parseAddrSource(Parser.getFieldString(m, "wa"))

        val ra = parseAddrSource(Parser.getFieldString(m, "ra"))

        val wd = Parser.getFieldString(m, "wd") match {
          case "x" => 0 // Don't care
          case "local" => 0
          case "remote" => 1
          case _ => throw new Exception(s"Unknown write data source; must be either local or remote")
        }

        val wen = Parser.getFieldString(m, "wen") match {
          case "x" => 0
          case n@_ => n.drop(1).toInt + 1
        }

        val banking = parseBankingConfig(Parser.getFieldString(m, "banking"))
        val numBufs = Parser.getFieldInt(m, "numBufs")
        ScratchpadConfig(wa, ra, wd, wen, banking, numBufs)
    }
  }

  def parseTopUnit(m: Map[Any, Any]): TopUnitConfig = isDontCare(m) match {
    case true =>
      TopUnitConfig.zeroes(10) // Some random number
    case false =>
      val doneConnBox = ConnBoxConfig(Parser.getFieldInt(m, "done"))
//      val dataVldConnBox = ConnBoxConfig(Parser.getFieldInt(m, "dataVldConnBox"))
      val argOutConnBox = ConnBoxConfig(Parser.getFieldInt(m, "argOut"))
      TopUnitConfig(doneConnBox, argOutConnBox)
  }

  def parseMemoryUnit(m: Map[Any, Any]): MemoryUnitConfig = isDontCare(m) match {
    case true =>
      MemoryUnitConfig.zeroes
    case false =>
      val scatterGather = parseValue(Parser.getFieldString(m, "scatteGather"))
      val isWr = parseValue(Parser.getFieldString(m, "isWr"))
      val counterChain = parseCounterChain(Parser.getFieldMap(m, "counterChain"))
      val control = parseControlBox(Parser.getFieldMap(m, "control"))
      MemoryUnitConfig(scatterGather, isWr, counterChain, control)
  }

  def parsePlasticine(m: Map[Any, Any]): PlasticineConfig = {
    val cu: List[ComputeUnitConfig] = Parser.getFieldListOfMaps(m, "cu")
                                          .map { parseCU(_) }
    val dataNetwork = new InterconnectHelper {
      val rows = ArchConfig.numRows
      val cols = ArchConfig.numCols
      val mus = List.fill(ArchConfig.numMemoryUnits) { null }
      val cus = null
      val topUnit = null
      val config_enable = null
      val config_data = null
      val dot = null
      val switches = null
    }
    val ctrlNetwork = new CtrlInterconnectHelper {
      val rows = ArchConfig.numRows
      val cols = ArchConfig.numCols
      val mus = List.fill(ArchConfig.numMemoryUnits) { null }
      val cus = null
      val topUnit = null
      val config_enable = null
      val config_data = null
      val dot = null
      val switches = null
    }

    def parseSwitches(ml: List[Map[Any, Any]], helper: InterconnectHelper) = {
      List.tabulate (helper.cols+1) { x =>
        List.tabulate(helper.rows+1) { y =>
          val map = ml(x*(helper.rows+1) + y)
          val outSelectRaw = Parser.getFieldList(map, "outSelect")
                                        .asInstanceOf[List[String]]
          val outSelect = outSelectRaw.map { str =>
//          println(s"[parseSwitches $x $y] $str")
            val split = if (str == "x") Array("x","0") else str.split("_")
            val (dir, offset) = (split(0), split(1))
            val directionOp = dir match {
              case "w" => helper.W()
              case "nw" => helper.NW()
              case "n" =>  helper.N()
              case "ne" => helper.NE()
              case "e" =>  helper.E()
              case "se" => helper.SE()
              case "s" =>  helper.S()
              case "sw" => helper.SW()
              case "x" =>  helper.N() // don't care direction is N (as every switch has N)
              case _ => throw new Exception(s"[parseSwitch $x $y] Unknown direction $dir")
            }
            val baseIdx = helper.getIdxBase(x, y, directionOp, helper.getValidIO(x, y, INPUT), INPUT)
            val validIdxs = helper.getIdxs(x, y, directionOp, INPUT)
//            val validIdxsW = helper.getIdxs(x, y, helper.W(), INPUT)
//            val validIdxsNW = helper.getIdxs(x, y, helper.NW(), INPUT)
//            val validIdxsN = helper.getIdxs(x, y, helper.N(), INPUT)
//            val validIdxsNE = helper.getIdxs(x, y, helper.NE(), INPUT)
//              validIdxsW: $validIdxsW
//              validIdxsN: $validIdxsN
//              validIdxsNE: $validIdxsNE

            val idx = baseIdx + Integer.parseInt(offset)
            if (!validIdxs.contains(idx)) {
              throw new Exception(s"""
[parseSwitch $x $y] Invalid config $str: translates to invalid index $idx (valid indices: $validIdxs)")
""")
            }
            idx
          }
          println(s"[parseSwitch $x $y] outSelectRaw = $outSelectRaw, outSelect = $outSelect")
          CrossbarConfig(outSelect)
        }
      }.flatten
    }

    println(s"[parsePlasticine] Parsing dataSwitch")
    val dataSwitch: List[CrossbarConfig] = parseSwitches(Parser.getFieldListOfMaps(m, "dataSwitch"),
                                                  dataNetwork)


    println(s"[parsePlasticine] Parsing controlSwitch")
    val controlSwitch: List[CrossbarConfig] = parseSwitches(Parser.getFieldListOfMaps(m, "controlSwitch"),
                                                  ctrlNetwork)


    val mu: List[MemoryUnitConfig] = Parser.getFieldListOfMaps(m, "mu")
                                    .map { parseMemoryUnit(_) }

    val top: TopUnitConfig = parseTopUnit(Parser.getFieldMap(m, "top"))

    PlasticineConfig(cu, dataSwitch, controlSwitch, mu, top)
  }

  def parseConfigMap(m: Map[Any, Any]) = {
    val t = getFieldString(m, "type")
    val config = getFieldMap(m, "config")
    t match {
      case "counter" =>
        parseCounterRC(config)
      case "counterChain" =>
        parseCounterChain(config)
      case "crossbar" =>
        parseCrossbar(config)
      case "lut" =>
        parseLUT(config)
      case "scratchpad" =>
        parseScratchpad(config)
      case "control" =>
        parseControlBox(config)
      case "cu" =>
        parseCU(config)
      case "plasticine" =>
        parsePlasticine(config)
      case _ => throw new Exception(s"not handled yet: $t")
    }
  }

  def getFieldString(map: Map[Any, Any], field:String): String = {
    map.get(field) match {
      case Some(field) => field.asInstanceOf[String].trim
      case None => fieldNotFound(field, map)
    }
  }

  def getFieldDouble(map: Map[Any, Any], field: String, randomize: Boolean = false): Double = {
    map.get(field) match {
      case Some(field) => java.lang.Double.parseDouble(field.toString)
      case None => if (randomize) Random.nextDouble else fieldNotFound(field, map)
    }
  }

  def getFieldInt(map: Map[Any, Any], field: String, randomize: Boolean = false): Int = {
    getFieldDouble(map, field, randomize).toInt
  }

  def getFieldBoolean(map: Map[Any, Any], field: String): Boolean = {
    map.get(field) match {
      case Some(field) => java.lang.Boolean.parseBoolean(field.toString)
      case None => fieldNotFound(field, map)
    }
  }

  def getFieldList(map: Map[Any, Any], field: String): List[Any] = {
    map.get(field) match {
      case Some(field) => field match {
        case list: Seq[Any] => list.toList
        case s: String => List("dontCare")
        case err@_ => listNotFound(err)
      }
      case None => fieldNotFound(field, map)
    }
  }

  def getFieldListOfMaps(map: Map[Any, Any], field: String): List[Map[Any,Any]] = {
    val l = getFieldList(map, field)
    l.map { m => m match {
        case map: Map[Any,Any] => map
        case hmap: HashMap[Any, Any] => hmap.toMap
        case s: String => Map("dontCare" -> "dontCare").asInstanceOf[Map[Any, Any]]
        case err@_ => listNotFound(err)
      }
    }
  }

  def getFieldMap(map: Map[Any, Any], field: String): Map[Any,Any] = {
    map.get(field) match {
      case Some(field) => field match {
        case map: Map[Any,Any] => map
        case s: String => Map("dontCare" -> "dontCare")
        case err@_ => mapNotFound(err)
      }
      case None => fieldNotFound(field, map)
    }
  }

  def fieldNotFound(field: String, obj: Any) = throw new RuntimeException("Expecting field [" + field + "], found: " + obj )
  def mapNotFound(err:Any) = throw new RuntimeException("Expecting a Map object, found: " + err)
  def listNotFound(err:Any) = throw new RuntimeException("Expecting a List object, found: " + err)
}
