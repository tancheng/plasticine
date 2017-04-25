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

trait SimpleSequentialTrait2 extends SimpleSequentialTrait1 {
  self:SimpleSequentialTrait =>
  def config2:Unit = {
    cus(1)(1).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=16)
    cus(1)(1).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
    cus(1)(1).asPCUBits.stages(0).opB = SVT()
    cus(1)(1).asPCUBits.stages(0).opC = SVT()
    cus(1)(1).asPCUBits.stages(0).opcode = BypassA
    cus(1)(1).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(1)(1).asPCUBits.stages(1).fwd(0) = SVT(PrevStageSrc, 0)
    cus(1)(1).asPCUBits.stages(2).fwd(0) = SVT(PrevStageSrc, 0)
    cus(1)(1).asPCUBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(3).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(3).opC = SVT()
    cus(1)(1).asPCUBits.stages(3).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(3).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(3).fwd(0) = SVT(ALUSrc, 3)
    cus(1)(1).asPCUBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(4).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(4).opC = SVT()
    cus(1)(1).asPCUBits.stages(4).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(4).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(4).fwd(0) = SVT(ALUSrc, 4)
    cus(1)(1).asPCUBits.stages(5).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(5).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(5).opC = SVT()
    cus(1)(1).asPCUBits.stages(5).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(5).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(5).fwd(0) = SVT(ALUSrc, 5)
    cus(1)(1).asPCUBits.stages(6).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(6).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(6).opC = SVT()
    cus(1)(1).asPCUBits.stages(6).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(6).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(6).fwd(0) = SVT(ALUSrc, 6)
    cus(1)(1).asPCUBits.stages(7).opA = SVT(PrevStageSrc, 0)
    cus(1)(1).asPCUBits.stages(7).opB = SVT(CurrStageSrc, 1)
    cus(1)(1).asPCUBits.stages(7).opC = SVT()
    cus(1)(1).asPCUBits.stages(7).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(7).res = List(SVT(CurrStageDst, 1))
    cus(1)(1).asPCUBits.accumInit = 3
    cus(1)(1).asPCUBits.stages(7).fwd(1) = SVT(ALUSrc, 7)
    cus(1)(1).asPCUBits.stages(8).opA = SVT(PrevStageSrc, 1)
    cus(1)(1).asPCUBits.stages(8).opB = SVT()
    cus(1)(1).asPCUBits.stages(8).opC = SVT()
    cus(1)(1).asPCUBits.stages(8).opcode = BypassA
    cus(1)(1).asPCUBits.stages(8).res = List(SVT(CurrStageDst, 8))
    cus(1)(1).asPCUBits.stages(8).fwd(8) = SVT(ALUSrc, 8)
    // Configuring lcus(1)(2) <- SeqCU7_x358
    lcus(1)(2).counterChain.chain = List(0,0,0,0,0)
    // SeqCU7_x358.udcounters=[PipeCU96_x357 -> TokBuf326,Top1_Top -> TokBuf312]
    // lcus(1)(2).udcs=[Some(TokBuf326),Some(TokBuf312),None,None]
    lcus(1)(2).control.childrenAndTree = List(1, 0, 0, 0)
    lcus(1)(2).control.siblingAndTree = List(0, 1, 0, 0)
    lcus(1)(2).control.incrementXbar.outSelect(0) = 7
    lcus(1)(2).control.incrementXbar.outSelect(1) = 6
    lcus(1)(2).control.udcDecSelect=List(1,1,-1,-1)
    lcus(1)(2).control.tokenOutXbar.outSelect(2) = 0
    lcus(1)(2).control.tokenOutXbar.outSelect(3) = 1
    lcus(1)(2).control.doneXbar.outSelect(0) = 0
    lcus(1)(2).control.pulserMax=1
    lcus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
  }
}
