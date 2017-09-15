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

trait DotProductTrait1 {
  self:DotProductTrait =>
  def config1:Unit = {
    vsbs(0)(0).outSelect(8) = 0
    ssbs(0)(0).outSelect(2) = 18
    ssbs(0)(0).outSelect(3) = 19
    csbs(0)(0).outSelect(3) = 12
    csbs(0)(0).outSelect(4) = 3
    csbs(0)(0).outSelect(12) = 4
    vsbs(0)(1).outSelect(9) = 22
    vsbs(0)(1).outSelect(14) = 9
    csbs(0)(1).outSelect(19) = 17
    csbs(0)(1).outSelect(20) = 28
    csbs(0)(1).outSelect(21) = 20
    csbs(0)(1).outSelect(30) = 19
    csbs(1)(0).outSelect(8) = 14
    csbs(1)(0).outSelect(16) = 8
    csbs(1)(0).outSelect(17) = 16
    csbs(1)(0).outSelect(18) = 15
    vsbs(1)(1).outSelect(19) = 14
    ssbs(1)(1).outSelect(9) = 19
    ssbs(1)(1).outSelect(17) = 8
    csbs(1)(1).outSelect(3) = 14
    csbs(1)(1).outSelect(7) = 14
    csbs(1)(1).outSelect(8) = 21
    csbs(1)(1).outSelect(9) = 21
    csbs(1)(1).outSelect(16) = 9
    csbs(1)(1).outSelect(17) = 20
    csbs(1)(1).outSelect(22) = 15
    csbs(1)(1).outSelect(23) = 9
    ssbs(1)(2).outSelect(4) = 11
    ssbs(1)(2).outSelect(17) = 6
    csbs(1)(2).outSelect(8) = 10
    csbs(1)(2).outSelect(10) = 8
    csbs(1)(2).outSelect(11) = 14
    csbs(1)(2).outSelect(12) = 15
    vsbs(2)(1).outSelect(22) = 12
    vsbs(2)(2).outSelect(12) = 4
    ssbs(2)(2).outSelect(11) = 6
    ssbs(2)(2).outSelect(12) = 4
    csbs(2)(2).outSelect(3) = 11
    csbs(2)(2).outSelect(12) = 3
    csbs(2)(2).outSelect(13) = 10
    // Configuring cus(0)(0).asPCUBits <- PipeCU366_x1087_0
    // cu1025[0,0].ib7407[1] -> CredBuf956.inc(from:PipeCU463_x1092_0.doneOut at PipeCU463_x1092_0)
    // cu1025[0,0].ob7363[0] -> PipeCU366_x1087_0.doneOut(to:ScalBuf464.swapWrite at PipeCU463_x1092_0,CredBuf920.inc at StreamCtrler244_x1072,CredBuf936.inc at StreamCtrler122_x1053,TokBuf962.inc at PipeCU463_x1092_0)
    // cu1025[0,0].ob7371[1] -> PipeCU366_x1087_0.doneOut(to:ScalBuf464.swapWrite at PipeCU463_x1092_0,CredBuf920.inc at StreamCtrler244_x1072,CredBuf936.inc at StreamCtrler122_x1053,TokBuf962.inc at PipeCU463_x1092_0)
    // PipeCU366_x1087_0.udcounters=[ScalBuf464 -> CredBuf956]
    // cus(0)(0).asPCUBits.udcs=[Some(CredBuf956),None,None,None,None]
    cus(0)(0).asPCUBits.control.udcInit=List(2,-1,-1,-1,-1)
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 1, 1, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU366_x1087_0 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 1
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(0) = 4
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(1) = 4
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[None,None,None,None]
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,Some(pr(st26841[7],reg1031[5])),None,None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(1) = 0
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT(VectorFIFOSrc, 1)
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = FixMul
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).fwd(0) = SVT(PrevStageSrc, 0)
    cus(0)(0).asPCUBits.stages(2).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(2).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(2).opC = SVT()
    cus(0)(0).asPCUBits.stages(2).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(2).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(2).fwd(0) = SVT(ALUSrc, 2)
    cus(0)(0).asPCUBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(3).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(3).opC = SVT()
    cus(0)(0).asPCUBits.stages(3).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(3).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(3).fwd(0) = SVT(ALUSrc, 3)
    cus(0)(0).asPCUBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(4).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(4).opC = SVT()
    cus(0)(0).asPCUBits.stages(4).opcode = FixAdd
  }
}
