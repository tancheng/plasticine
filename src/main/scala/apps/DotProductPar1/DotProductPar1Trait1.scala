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

trait DotProductPar1Trait1 {
  self:DotProductPar1Trait =>
  def config1:Unit = {
    vsbs(0)(0).outSelect(12) = 0
    ssbs(0)(0).outSelect(4) = 26
    ssbs(0)(0).outSelect(5) = 27
    ssbs(0)(0).outSelect(31) = 27
    csbs(0)(0).outSelect(3) = 28
    csbs(0)(0).outSelect(4) = 3
    csbs(0)(0).outSelect(27) = 45
    csbs(0)(0).outSelect(28) = 4
    csbs(0)(0).outSelect(44) = 45
    csbs(0)(0).outSelect(49) = 30
    csbs(0)(0).outSelect(50) = 47
    csbs(0)(0).outSelect(51) = 46
    vsbs(0)(1).outSelect(13) = 32
    csbs(0)(1).outSelect(32) = 46
    csbs(0)(1).outSelect(48) = 42
    csbs(0)(1).outSelect(49) = 71
    csbs(0)(1).outSelect(50) = 72
    csbs(0)(1).outSelect(78) = 45
    ssbs(1)(0).outSelect(22) = 4
    csbs(1)(0).outSelect(31) = 42
    csbs(1)(0).outSelect(43) = 43
    csbs(1)(0).outSelect(47) = 25
    csbs(1)(0).outSelect(48) = 11
    csbs(1)(0).outSelect(49) = 39
    vsbs(1)(1).outSelect(13) = 20
    ssbs(1)(1).outSelect(31) = 13
    csbs(1)(1).outSelect(11) = 27
    csbs(1)(1).outSelect(31) = 59
    csbs(1)(1).outSelect(65) = 27
    csbs(1)(1).outSelect(69) = 27
    vsbs(1)(2).outSelect(12) = 19
    vsbs(2)(0).outSelect(6) = 19
    ssbs(2)(0).outSelect(29) = 27
    ssbs(2)(0).outSelect(30) = 25
    csbs(2)(0).outSelect(11) = 46
    csbs(2)(0).outSelect(49) = 11
    csbs(2)(0).outSelect(50) = 45
    // Configuring cus(0)(0).asPCUBits <- PipeCU393_x1092_0
    // PipeCU393_x1092_0.udcounters=[ScalBuf394 -> TokBuf867]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf867),None,None,None,None]
    cus(0)(0).asPCUBits.control.udcInit=List(0,-1,-1,-1,-1)
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU393_x1092_0 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm8341[0] -> ScalBuf394 swapWrite=CtrlBox631.done.out
    // ib6029[7] -> TokBuf867.inc(from:CtrlBox631.done.out at PipeCU296_x1087_0),ScalBuf394.swapWrite(from:CtrlBox631.done.out at PipeCU296_x1087_0)
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 7
    cus(0)(0).asPCUBits.control.swapWriteXbar.outSelect(0) = 7
    // ob5977[3] -> CtrlBox648.done.out(to:ScalBuf394.swapRead at PipeCU393_x1092_0,TokBuf821.inc at MetaPipeCU18_x1094,CredBuf861.inc at PipeCU296_x1087_0,TokBuf867.dec at PipeCU393_x1092_0)
    // ob5985[7] -> CtrlBox648.done.out(to:ScalBuf394.swapRead at PipeCU393_x1092_0,TokBuf821.inc at MetaPipeCU18_x1094,CredBuf861.inc at PipeCU296_x1087_0,TokBuf867.dec at PipeCU393_x1092_0)
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(3) = 4
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(7) = 4
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(2,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3841[1]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 1
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,Some(pr(st8287[6],reg10[5])),None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT()
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = BypassA
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(1).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(1).opC = SVT()
    cus(0)(0).asPCUBits.stages(1).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(1).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(1).fwd(0) = SVT(ALUSrc, 1)
    cus(0)(0).asPCUBits.stages(2).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(2).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(2).opC = SVT()
    cus(0)(0).asPCUBits.stages(2).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(2).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(2).fwd(0) = SVT(ALUSrc, 2)
    cus(0)(0).asPCUBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(3).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(3).opC = SVT()
  }
}
