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
    ssbs(1)(0).outSelect(4) = 18
    ssbs(1)(0).outSelect(18) = 4
    csbs(1)(0).outSelect(5) = 16
    csbs(1)(0).outSelect(22) = 17
    csbs(1)(0).outSelect(23) = 4
    csbs(1)(0).outSelect(24) = 15
    // Configuring cus(0)(0).asPCUBits <- PipeCU20_x192
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU20_x192 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 5
    cus(0)(0).asPCUBits.control.tokenOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3234[2]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 2
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,Some(pr(st5512[8],reg13[8])),None]
    cus(0)(0).asPCUBits.scalarOutXbar.outSelect(2) = 0
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT(ConstSrc, 4)
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 8),SVT(CurrStageDst, 0))
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
    // Configuring lcus(1)(0) <- SeqCU4_x193
    lcus(1)(0).counterChain.chain = List(0,0,0,0,0)
    lcus(1)(0).control.childrenAndTree = List(0, 0, 0, 0)
    lcus(1)(0).control.siblingAndTree = List(0, 0, 0, 0)
    lcus(1)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
  }
}
