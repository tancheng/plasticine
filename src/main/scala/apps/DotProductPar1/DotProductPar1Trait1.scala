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
    ssbs(1)(0).outSelect(22) = 4
    csbs(1)(0).outSelect(27) = 42
    csbs(1)(0).outSelect(31) = 42
    csbs(1)(0).outSelect(43) = 43
    csbs(1)(0).outSelect(48) = 25
    csbs(1)(0).outSelect(49) = 39
    vsbs(1)(1).outSelect(13) = 20
    ssbs(1)(1).outSelect(21) = 12
    ssbs(1)(1).outSelect(31) = 13
    csbs(1)(1).outSelect(26) = 43
    csbs(1)(1).outSelect(27) = 42
    csbs(1)(1).outSelect(31) = 43
    csbs(1)(1).outSelect(45) = 57
    csbs(1)(1).outSelect(46) = 24
    csbs(1)(1).outSelect(47) = 59
    csbs(1)(1).outSelect(48) = 25
    csbs(1)(1).outSelect(49) = 27
    csbs(1)(1).outSelect(65) = 43
    csbs(1)(1).outSelect(68) = 43
    csbs(1)(1).outSelect(69) = 27
    vsbs(1)(2).outSelect(12) = 19
    vsbs(1)(2).outSelect(19) = 11
    ssbs(1)(2).outSelect(21) = 6
    csbs(1)(2).outSelect(12) = 42
    csbs(1)(2).outSelect(24) = 28
    csbs(1)(2).outSelect(29) = 24
    csbs(1)(2).outSelect(30) = 41
    csbs(1)(2).outSelect(45) = 27
    csbs(1)(2).outSelect(46) = 12
    csbs(1)(2).outSelect(50) = 27
    vsbs(2)(0).outSelect(6) = 19
    ssbs(2)(0).outSelect(29) = 27
    ssbs(2)(0).outSelect(30) = 25
    csbs(2)(0).outSelect(11) = 46
    csbs(2)(0).outSelect(49) = 11
    csbs(2)(0).outSelect(50) = 45
    vsbs(2)(2).outSelect(5) = 6
    ssbs(2)(2).outSelect(13) = 5
    ssbs(2)(2).outSelect(14) = 6
    csbs(2)(2).outSelect(11) = 21
    csbs(2)(2).outSelect(22) = 11
    csbs(2)(2).outSelect(23) = 20
    // Configuring cus(0)(0).asPCUBits <- PipeCU393_x1092_0
    // PipeCU393_x1092_0.udcounters=[MetaPipeCU18_x1094 -> TokBuf827]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf827),None,None,None,None]
    cus(0)(0).asPCUBits.control.udcInit=List(1,-1,-1,-1,-1)
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU393_x1092_0 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm8341[0] -> ScalBuf394 swapWrite=CtrlBox631.done.out
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 6
    cus(0)(0).asPCUBits.control.swapWriteXbar.outSelect(0) = 7
    // ob5977[3] -> CtrlBox648.done.out
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(3) = 4
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
  }
}
