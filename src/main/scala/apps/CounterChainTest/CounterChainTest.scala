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

object CounterChainTest extends PISADesign with CounterChainTestTrait
trait CounterChainTestTrait {

  def main(args: String*) = {
    val counters = Array.tabulate(3) { i =>
      CounterRCBits(max=SrcValueTuple(ConstSrc, 10+i*10), stride=SrcValueTuple(ConstSrc, i), min=SrcValueTuple(ConstSrc, 1), par=1)
    }
    CounterChainBits(List(0,0), counters)

  }
}
