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

trait DotProductTrait1 {
  self:DotProductTrait =>
  def config1:Unit = {
    vsbs(0)(1).outSelect(5) = 0
    ssbs(0)(1).outSelect(2) = 5
    ssbs(0)(1).outSelect(3) = 6
    ssbs(0)(1).outSelect(16) = 7
    csbs(0)(1).outSelect(3) = 8
    csbs(0)(1).outSelect(4) = 3
    csbs(0)(1).outSelect(6) = 16
    csbs(0)(1).outSelect(7) = 17
    csbs(0)(1).outSelect(8) = 4
    csbs(0)(1).outSelect(14) = 17
    csbs(0)(1).outSelect(21) = 7
    csbs(0)(1).outSelect(22) = 18
    ssbs(0)(2).outSelect(15) = 6
    ssbs(0)(2).outSelect(16) = 4
    ssbs(0)(2).outSelect(17) = 6
    csbs(0)(2).outSelect(5) = 16
    csbs(0)(2).outSelect(15) = 17
    csbs(0)(2).outSelect(16) = 14
    csbs(0)(2).outSelect(17) = 18
    csbs(0)(2).outSelect(19) = 13
    csbs(0)(2).outSelect(22) = 5
    csbs(0)(2).outSelect(23) = 12
    ssbs(1)(0).outSelect(18) = 4
    vsbs(1)(1).outSelect(9) = 14
    ssbs(1)(1).outSelect(23) = 9
    csbs(1)(1).outSelect(11) = 23
    csbs(1)(1).outSelect(15) = 16
    csbs(1)(1).outSelect(21) = 3
    csbs(1)(1).outSelect(22) = 13
    csbs(1)(1).outSelect(23) = 18
    csbs(1)(1).outSelect(25) = 17
    csbs(1)(1).outSelect(31) = 9
    vsbs(1)(2).outSelect(8) = 13
    vsbs(2)(1).outSelect(14) = 9
    ssbs(2)(1).outSelect(15) = 7
    ssbs(2)(1).outSelect(16) = 8
    csbs(2)(1).outSelect(3) = 17
    csbs(2)(1).outSelect(21) = 3
    csbs(2)(1).outSelect(22) = 16
    ssbs(2)(2).outSelect(15) = 5
    ssbs(2)(2).outSelect(16) = 6
    // Configuring cus(0)(0).asPCUBits <- PipeCU463_x1092
    // PipeCU463_x1092.udcounters=[ScalBuf464 -> TokBuf865]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf865),None,None,None,None]
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU463_x1092 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm6094[0] -> ScalBuf464 swapWrite=CtrlBox715.done.out
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 3
    cus(0)(0).asPCUBits.control.swapWriteXbar.outSelect(0) = 3
    // ob4116[0] -> CtrlBox732.done.out
    // ob4120[1] -> CtrlBox732.done.out
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(0) = 4
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(1) = 4
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(2,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3142[1]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 1
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,Some(pr(st6040[8],reg13[8])),None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT()
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = BypassA
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).fwd(0) = SVT(PrevStageSrc, 0)
    cus(0)(0).asPCUBits.stages(2).fwd(0) = SVT(PrevStageSrc, 0)
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
    cus(0)(0).asPCUBits.stages(4).res = List(SVT(CurrStageDst, 0))
  }
}
