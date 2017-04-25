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
    ssbs(1)(0).outSelect(4) = 16
    ssbs(1)(0).outSelect(18) = 4
    csbs(1)(0).outSelect(5) = 18
    csbs(1)(0).outSelect(24) = 4
    // Configuring cus(0)(0).asPCUBits <- PipeCU19_x228
    // PipeCU19_x228.udcounters=[Top1_Top -> TokBuf103]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf103),None,None,None,None]
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU19_x228 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm6022[0] -> ScalBuf20 swapWrite=NotConnected
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 5
    // ob4056[2] -> CtrlBox60.done.out
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(2) = 4
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3150[2]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 2
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,Some(pr(st5968[8],reg13[8])),None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT(ConstSrc, 4)
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 8))
    cus(0)(0).asPCUBits.stages(0).fwd(8) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(6).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(7).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).asPCUBits.stages(8).fwd(8) = SVT(PrevStageSrc, 8)
  }
}
