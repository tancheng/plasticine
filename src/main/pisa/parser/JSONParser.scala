package plasticine.parser

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.mutable.HashMap

class ParsedFile(x: Map[Any, Any])

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
      case pisam: Map[Any,Any] =>
        println("PISA map:")
        println(pisam)
        new ParsedFile(pisam)
        // parseDEGMap(pisam)
      case err@_ => mapNotFound(err)
    }
  }

  def mapNotFound(err:Any) = throw new RuntimeException("Expecting a Map object, found: " + err)

}
