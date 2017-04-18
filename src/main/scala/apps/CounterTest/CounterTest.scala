package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.PISADesign
import plasticine.pisa.ir._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import plasticine.templates._
import plasticine.pisa.enums._

object CounterTest extends PISADesign with CounterTestTrait
trait CounterTestTrait {

  def main(args: String*) = {
    CounterRCBits(max=SrcValueTuple(ConstSrc, 100), stride=SrcValueTuple(ConstSrc, 5), min=SrcValueTuple(ConstSrc, 1), par=1)
  }
}
