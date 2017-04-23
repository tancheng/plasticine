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

trait InOutArgTrait1 {
  self:InOutArgTrait =>
  def config1:Unit = {
    csbs(0)(1).outSelect(3) = 10
    csbs(0)(1).outSelect(16) = 3
    csbs(0)(1).outSelect(17) = 12
    csbs(0)(1).outSelect(19) = 11
    csbs(0)(2).outSelect(0) = 12
    csbs(0)(2).outSelect(18) = 0
    ssbs(1)(0).outSelect(4) = 18
    ssbs(1)(0).outSelect(18) = 4
    // Configuring cus(0)(0).asPCUBits <- PipeCU20_x192
    // PipeCU20_x192.udcounters=[SeqCU4_x193 -> TokBuf102]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf102),None,None,None,None]
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU20_x192 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 1
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(0) = 4
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3234[2]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 2
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,Some(pr(st5512[8],reg13[8])),None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT(ConstSrc, 4)
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0),SVT(CurrStageDst, 8))
    cus(0)(0).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(0).fwd(8) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(6).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(7).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(8).fwd(8) = SVT(PrevStageSrc, 8)
    // Configuring lcus(0)(1) <- SeqCU4_x193
    lcus(0)(1).counterChain.chain = List(0,0,0,0,0)
    // SeqCU4_x193.udcounters=[Top1_Top -> TokBuf96,PipeCU20_x192 -> TokBuf108]
    // lcus(0)(1).udcs=[Some(TokBuf96),Some(TokBuf108),None,None]
    lcus(0)(1).control.childrenAndTree = List(0, 1, 0, 0, 0)
    lcus(0)(1).control.siblingAndTree = List(1, 0, 0, 0, 0)
    lcus(0)(1).control.incrementXbar.outSelect(0) = 6
    lcus(0)(1).control.incrementXbar.outSelect(1) = 7
    lcus(0)(1).control.udcDecSelect=List(1,1,-1,-1)
    lcus(0)(1).control.tokenOutXbar.outSelect(1) = 0
    lcus(0)(1).control.tokenOutXbar.outSelect(2) = 1
    lcus(0)(1).control.doneXbar.outSelect(0) = 0
    lcus(0)(1).control.pulserMax=1
    lcus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
  }
}
