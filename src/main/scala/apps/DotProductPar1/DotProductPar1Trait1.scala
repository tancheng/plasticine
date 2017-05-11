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
    csbs(0)(1).outSelect(28) = 46
    csbs(0)(1).outSelect(32) = 45
    csbs(0)(1).outSelect(44) = 45
    csbs(0)(1).outSelect(49) = 28
    csbs(0)(1).outSelect(50) = 42
    vsbs(0)(2).outSelect(8) = 0
    ssbs(0)(2).outSelect(4) = 5
    ssbs(0)(2).outSelect(5) = 6
    csbs(0)(2).outSelect(3) = 47
    csbs(0)(2).outSelect(4) = 3
    csbs(0)(2).outSelect(51) = 4
    ssbs(1)(0).outSelect(24) = 5
    vsbs(1)(1).outSelect(13) = 20
    ssbs(1)(1).outSelect(23) = 13
    ssbs(1)(1).outSelect(35) = 15
    csbs(1)(1).outSelect(11) = 42
    csbs(1)(1).outSelect(27) = 43
    csbs(1)(1).outSelect(31) = 42
    csbs(1)(1).outSelect(43) = 42
    csbs(1)(1).outSelect(45) = 39
    csbs(1)(1).outSelect(46) = 11
    csbs(1)(1).outSelect(47) = 59
    csbs(1)(1).outSelect(48) = 27
    csbs(1)(1).outSelect(49) = 25
    csbs(1)(1).outSelect(68) = 42
    csbs(1)(1).outSelect(69) = 27
    vsbs(1)(2).outSelect(12) = 19
    ssbs(1)(2).outSelect(22) = 6
    csbs(1)(2).outSelect(12) = 42
    csbs(1)(2).outSelect(46) = 12
    vsbs(2)(0).outSelect(6) = 19
    ssbs(2)(0).outSelect(30) = 28
    ssbs(2)(0).outSelect(31) = 26
    csbs(2)(0).outSelect(39) = 46
    csbs(2)(0).outSelect(49) = 37
    csbs(2)(0).outSelect(50) = 45
    csbs(2)(1).outSelect(11) = 40
    csbs(2)(1).outSelect(44) = 11
    csbs(2)(1).outSelect(45) = 70
    csbs(2)(1).outSelect(74) = 41
    csbs(2)(1).outSelect(78) = 40
    // Configuring cus(0)(0).asPCUBits <- PipeCU393_x1092_0
    // PipeCU393_x1092_0.udcounters=[MetaPipeCU18_x1094 -> TokBuf827]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf827),None,None,None,None]
    cus(0)(0).asPCUBits.control.udcInit=List(1,-1,-1,-1,-1)
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU393_x1092_0 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm8493[0] -> ScalBuf394 swapWrite=CtrlBox631.done.out
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 6
    cus(0)(0).asPCUBits.control.swapWriteXbar.outSelect(0) = 7
    // ob6105[3] -> CtrlBox648.done.out
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(3) = 6
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(2,-1,-1,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3865[3]),None,None,None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 3
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,None,None,None,Some(pr(st8439[6],reg13[8])),None,None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(5) = 0
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
  }
}
