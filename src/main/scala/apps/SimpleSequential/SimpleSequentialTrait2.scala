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
    cus(1)(1).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(1)(1).asPCUBits.scalarInXbar=[Some(iw3232[1]),None,None,None]
    cus(1)(1).asPCUBits.scalarInXbar.outSelect(0) = 1
    // cus(1)(1).asPCUBits.scalarOutXbar=[Some(pr(st6896[8],reg513[8])),None,None,None]
    cus(1)(1).asPCUBits.scalarOutXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(1)(1).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
    cus(1)(1).asPCUBits.stages(0).opB = SVT()
    cus(1)(1).asPCUBits.stages(0).opC = SVT()
    cus(1)(1).asPCUBits.stages(0).opcode = BypassA
    cus(1)(1).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 8))
    cus(1)(1).asPCUBits.stages(0).fwd(8) = SVT(ALUSrc, 0)
    cus(1)(1).asPCUBits.stages(1).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(6).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(7).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(8).fwd(8) = SVT(PrevStageSrc, 8)
    // Configuring lcus(2)(1) <- SeqCU7_x358
    lcus(2)(1).counterChain.chain = List(0,0,0,0,0)
    // SeqCU7_x358.udcounters=[Top1_Top -> TokBuf230,PipeCU96_x357 -> TokBuf244]
    // lcus(2)(1).udcs=[Some(TokBuf230),Some(TokBuf244),None,None]
    lcus(2)(1).control.childrenAndTree = List(0, 1, 0, 0)
    lcus(2)(1).control.siblingAndTree = List(1, 0, 0, 0)
    lcus(2)(1).control.incrementXbar.outSelect(0) = 6
    lcus(2)(1).control.incrementXbar.outSelect(1) = 7
    lcus(2)(1).control.udcDecSelect=List(1,1,-1,-1)
    lcus(2)(1).control.tokenOutXbar.outSelect(2) = 0
    lcus(2)(1).control.tokenOutXbar.outSelect(3) = 1
    lcus(2)(1).control.doneXbar.outSelect(0) = 0
    lcus(2)(1).control.pulserMax=1
    lcus(2)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
  }
}
