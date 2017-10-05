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

object SimplePCU extends PISADesign with SimplePCUTrait
trait SimplePCUTrait {
  val pcuBits = PCUBits.zeroes(cuParams(0)(0).asInstanceOf[PCUParams])

  // Helper methods to create datapath
  // Datapath has operand sources, opcodes, destinations, forwarded results
  // What is the format I want, that captures the above?

  // Pain points:
  // 1. Datapath too verbose
  // 2. Passing forward values is annoying
  // 3. Control is almost impossible

  // Counters
  pcuBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 256), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=8)

  // Drive udc with controlIn(0). Drive PCU's enable on udc0
  // controlIn(0) -> udc(0) -> enable
  pcuBits.control.incrementXbar.outSelect(0) = 0
  pcuBits.control.udcEnable(0) = 1
  pcuBits.control.siblingAndTree(0) = 1
  pcuBits.control.streamingMuxSelect = 0

  // PCU is done when counter0 is done
  // counter(0).done -> controlOut(0)
  pcuBits.control.doneXbar.outSelect(0) = 0
  pcuBits.control.tokenOutXbar.outSelect(0) = 4

  // Datapath
  pcuBits.stages(0).opA = SVT(CounterSrc, 0)
  pcuBits.stages(0).opB = SVT(CounterSrc, 0)
  pcuBits.stages(0).opC = SVT()
  pcuBits.stages(0).opcode = FixMul
  pcuBits.stages(0).res = List(SVT(CurrStageDst, 0))

  pcuBits.stages(1).fwd(0) = SVT(PrevStageSrc, 0)
  pcuBits.stages(2).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(2).opB = SVT(PrevStageSrc, 0)
  pcuBits.stages(2).opC = SVT()
  pcuBits.stages(2).opcode = FixAdd
  pcuBits.stages(2).res = List(SVT(CurrStageDst, 0))

  pcuBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(3).opB = SVT(PrevStageSrc, 0)
  pcuBits.stages(3).opC = SVT()
  pcuBits.stages(3).opcode = FixAdd
  pcuBits.stages(3).res = List(SVT(CurrStageDst, 0))

  pcuBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(4).opB = SVT(PrevStageSrc, 0)
  pcuBits.stages(4).opC = SVT()
  pcuBits.stages(4).opcode = FixAdd
  pcuBits.stages(4).res = List(SVT(CurrStageDst, 0))

  pcuBits.stages(5).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(5).opB = SVT(PrevStageSrc, 0)
  pcuBits.stages(5).opC = SVT()
  pcuBits.stages(5).opcode = FixAdd
  pcuBits.stages(5).res = List(SVT(CurrStageDst, 0))

  pcuBits.stages(6).opA = SVT(PrevStageSrc, 0)
  pcuBits.stages(6).opB = SVT(CurrStageSrc, 1)
  pcuBits.stages(6).opC = SVT()
  pcuBits.stages(6).opcode = FixAdd
  pcuBits.stages(6).res = List(SVT(CurrStageDst, 1))
  pcuBits.accumInit = 0

  pcuBits.stages(7).opA = SVT(PrevStageSrc, 1)
  pcuBits.stages(7).opB = SVT()
  pcuBits.stages(7).opC = SVT()
  pcuBits.stages(7).opcode = BypassA
  pcuBits.stages(7).res = List(SVT(CurrStageDst, 2))

  // Route scalar to output
  pcuBits.scalarOutXbar.outSelect(1) = 2

//  pcuBits.control.udcInit=List(2,-1,-1,-1,-1)
//  pcuBits.control.tokenInAndTree = List(0, 0, 0, 0)
//  pcuBits.control.fifoAndTree = List(0, 0, 0, 0, 1, 1, 0, 0)
//  pcuBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
//  pcuBits.control.streamingMuxSelect = 0
//  pcuBits.control.incrementXbar.outSelect(0) = 1
//  pcuBits.control.tokenOutXbar.outSelect(0) = 4
//  pcuBits.control.tokenOutXbar.outSelect(1) = 4
//  pcuBits.control.doneXbar.outSelect(0) = 0
//  pcuBits.fifoNbufConfig=List(-1,-1,-1,-1)
//  pcuBits.scalarOutXbar.outSelect(1) = 0

  def main(args: String*) = pcuBits
}
