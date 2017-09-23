package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.PISADesign
import plasticine.pisa.ir.{SrcValueTuple => SVT, _}
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import templates._
import plasticine.pisa.enums._

object SimplePMU extends PISADesign with SimplePMUTrait
trait SimplePMUTrait {
  val pmuBits = DummyPMUBits.zeroes(cuParams(0)(1).asInstanceOf[PMUParams])

//  pmuBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 1)
//  pmuBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
//  pmuBits.control.readDoneXbar.outSelect(0) = 0
//  pmuBits.control.writeDoneXbar.outSelect(0) = 2
//  pmuBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
//  pmuBits.fifoNbufConfig=List(-1,-1,-1,-1)
//  pmuBits.counterChain.chain = List(0,1,0,0,0,0,0)
//  pmuBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
//  pmuBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
//  pmuBits.counterChain.counters(2) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
//  pmuBits.scratchpad.stride = 1
//  pmuBits.scratchpad.numBufs = 2
  pmuBits.wdataSelect = 3
//  pmuBits.waddrSelect = SVT(CounterSrc, 1)
//  pmuBits.raddrSelect = SVT(CounterSrc, 0)

  def main(args: String*) = pmuBits
}
