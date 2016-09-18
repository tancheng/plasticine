package plasticine.pisa.parser

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.mutable.HashMap
import scala.util.Random
import plasticine.pisa.ir._

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

  def buildFromParsedJSON(json: Any) = {
    json match {
      case m: Map[Any, Any] =>
        val pisam = getFieldMap(m, "PISA")
        println(s"PISA map: $pisam")
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
    val max: Int = Parser.getFieldInt(m, "max")

    val stride: Int = Parser.getFieldInt(m, "stride")

    val maxConst: Int = Parser.getFieldInt(m, "maxConst")

    val strideConst: Int = Parser.getFieldInt(m, "strideConst")

    val startDelay: Int = 1 + Parser.getFieldInt(m, "startDelay")

    val endDelay: Int = 1 + Parser.getFieldInt(m, "endDelay")
    CounterRCConfig(max, stride, maxConst, strideConst, startDelay, endDelay)
  }

  def parseCounterChain(m: Map[Any, Any]): CounterChainConfig = {
    val chain: List[Int] = Parser.getFieldList(m, "chain")
                                        .asInstanceOf[List[Double]]
                                        .map { i => i.toInt }
    // Configuration for individual counters
    val counters: List[CounterRCConfig] = Parser.getFieldListOfMaps(m, "counters")
                                         .map { parseCounterRC(_) }
    CounterChainConfig(chain, counters)
  }

  def parseValue(x: String, incByOne: Boolean = false):Int = x(0) match {
    case 'x' => 0
    case _ => if (incByOne) Integer.parseInt(x) + 1 else Integer.parseInt(x)
  }

  def parseLUT(m: Map[Any, Any]): LUTConfig = {
    val table: List[Int] = Parser.getFieldList(m, "table")
                                        .asInstanceOf[List[String]]
                                        .map { parseValue(_) }
    LUTConfig(table)
  }

  def parseCU(m: Map[Any, Any]): ComputeUnitConfig = {
    val counterChain = parseCounterChain(Parser.getFieldMap(m, "counterChain"))

    val scratchpads =  Parser.getFieldListOfMaps(m, "scratchpads")
                                      .map { new ScratchpadConfig(_) }

    /* Pipe stages config */
    val pipeStage = Parser.getFieldListOfMaps(m, "pipeStage")
                              .map { h => new PipeStageConfig(h) }

    val control = parseControlBox(Parser.getFieldMap(m, "control"))

    val dataInXbar = parseCrossbar(Parser.getFieldMap(m, "dataInXbar"))
    ComputeUnitConfig(counterChain, scratchpads, pipeStage, control, dataInXbar)
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
    val tokenDownLUT: LUTConfig = parseLUT(Parser.getFieldMap(m, "tokenDownLUT"))
    val udcInit: List[Int] = Parser.getFieldList(m, "udcInit")
                                .asInstanceOf[List[String]]
                                .map { parseValue(_) }
    val decXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "decXbar"), true)
    val incXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "incXbar"), true)
    val doneXbar: CrossbarConfig  = parseCrossbar(Parser.getFieldMap(m, "doneXbar"))
    val enableMux: List[Boolean] = Parser.getFieldList(m, "enableMux")
                                .asInstanceOf[List[String]]
                                .map { parseValue(_) > 0 }
    val tokenOutMux: List[Boolean] = Parser.getFieldList(m, "tokenOutMux")
                                .asInstanceOf[List[String]]
                                 .map { parseValue(_) > 0 }
    val syncTokenMux: Int = parseValue(Parser.getFieldString(m, "syncTokenMux"))
    CUControlBoxConfig(tokenOutLUT, enableLUT, tokenDownLUT, udcInit, decXbar, incXbar, doneXbar, enableMux, tokenOutMux, syncTokenMux)
  }

  def parsePlasticine(m: Map[Any, Any]): PlasticineConfig = {
    val cu: List[ComputeUnitConfig] = Parser.getFieldListOfMaps(m, "cu")
                                          .map { parseCU(_) }
    PlasticineConfig(cu)
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
        new ScratchpadConfig(config)
      case "cuControl" =>
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
        case err@_ => listNotFound(err)
      }
    }
  }

  def getFieldMap(map: Map[Any, Any], field: String): Map[Any,Any] = {
    map.get(field) match {
      case Some(field) => field match {
        case map: Map[Any,Any] => map
        case err@_ => mapNotFound(err)
      }
      case None => fieldNotFound(field, map)
    }
  }

  def fieldNotFound(field: String, obj: Any) = throw new RuntimeException("Expecting field [" + field + "], found: " + obj )
  def mapNotFound(err:Any) = throw new RuntimeException("Expecting a Map object, found: " + err)
  def listNotFound(err:Any) = throw new RuntimeException("Expecting a List object, found: " + err)
}
