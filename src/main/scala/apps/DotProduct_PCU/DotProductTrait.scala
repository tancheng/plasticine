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
import plasticine.templates._
import plasticine.pisa.enums._

object DotProduct_PCU extends PISADesign with DotProductTrait_PCU
trait DotProductTrait_PCU {
  val pcuBits = PCUBits.zeroes(cuParams(0)(0).asInstanceOf[PCUParams])
  pcuBits.control.udcInit=List(2,-1,-1,-1,-1)
  pcuBits.control.tokenInAndTree = List(0, 0, 0, 0)
  pcuBits.control.fifoAndTree = List(0, 0, 0, 0, 1, 1, 0, 0)
  pcuBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
  pcuBits.control.streamingMuxSelect = 0
  pcuBits.control.incrementXbar.outSelect(0) = 1
  pcuBits.control.tokenOutXbar.outSelect(0) = 4
  pcuBits.control.tokenOutXbar.outSelect(1) = 4
  pcuBits.control.doneXbar.outSelect(0) = 0
  pcuBits.fifoNbufConfig=List(-1,-1,-1,-1)
  pcuBits.scalarOutXbar.outSelect(1) = 0
  pcuBits.counterChain.chain = List(0,0,0,0)
  pcuBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
  pcuBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
  pcuBits.stages(0).opB = SVT(VectorFIFOSrc, 1)
  pcuBits.stages(0).opC = SVT()
  pcuBits.stages(0).opcode = FixMul
  pcuBits.stages(0).res = List(SVT(CurrStageDst, 0))
  pcuBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
  pcuBits.stages(1).fwd(0) = SVT(PrevStageSrc, 0)
  pcuBits.stages(2).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(2).opB = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(2).opC = SVT()
  pcuBits.stages(2).opcode = FixAdd
  pcuBits.stages(2).res = List(SVT(CurrStageDst, 0))
  pcuBits.stages(2).fwd(0) = SVT(ALUSrc, 2)
  pcuBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(3).opB = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(3).opC = SVT()
  pcuBits.stages(3).opcode = FixAdd
  pcuBits.stages(3).res = List(SVT(CurrStageDst, 0))
  pcuBits.stages(3).fwd(0) = SVT(ALUSrc, 3)
  pcuBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(4).opB = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(4).opC = SVT()
  pcuBits.stages(4).opcode = FixAdd
  pcuBits.stages(4).res = List(SVT(CurrStageDst, 0))
  pcuBits.stages(4).fwd(0) = SVT(ALUSrc, 4)
  pcuBits.stages(5).opA = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(5).opB = SVT(ReduceTreeSrc, 0)
  pcuBits.stages(5).opC = SVT()
  pcuBits.stages(5).opcode = FixAdd
  pcuBits.stages(5).res = List(SVT(CurrStageDst, 0))
  pcuBits.stages(5).fwd(0) = SVT(ALUSrc, 5)
  pcuBits.stages(6).opA = SVT(PrevStageSrc, 0)
  pcuBits.stages(6).opB = SVT(CurrStageSrc, 1)
  pcuBits.stages(6).opC = SVT()
  pcuBits.stages(6).opcode = FixAdd
  pcuBits.stages(6).res = List(SVT(CurrStageDst, 1))
  pcuBits.accumInit = 0
  pcuBits.stages(6).fwd(1) = SVT(ALUSrc, 6)
  pcuBits.stages(7).opA = SVT(PrevStageSrc, 1)
  pcuBits.stages(7).opB = SVT()
  pcuBits.stages(7).opC = SVT()
  pcuBits.stages(7).opcode = BypassA
  pcuBits.stages(7).res = List(SVT(CurrStageDst, 5))
  pcuBits.stages(7).fwd(5) = SVT(ALUSrc, 7)

  def main(args: String*) = pcuBits
}
