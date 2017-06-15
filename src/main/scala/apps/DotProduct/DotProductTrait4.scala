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

trait DotProductTrait4 extends DotProductTrait3  {
  self:DotProductTrait =>
  def config4:Unit = {
    // ocu2888[1,2].ib7995[2] -> TokBuf908.inc(from:CtrlBox783.en.out at MetaPipeCU32_x1094)
    // ocu2888[1,2].ib7999[3] -> CredBuf920.inc(from:PipeCU366_x1087_0.doneOut at PipeCU366_x1087_0)
    // ocu2888[1,2].ob7959[1] -> CtrlBox807.en.out(to:Ctr251.en at StreamCtrler244_x1072,AndTree679_TokenInAndTree.in at PipeCU259_x1063_0)
    // StreamCtrler244_x1072.udcounters=[MetaPipeCU32_x1094 -> TokBuf908,MemoryController328_x1064 -> TokBuf927,SRAM99 -> CredBuf920]
    // lcus(1)(2).udcs=[Some(TokBuf908),Some(TokBuf927),Some(CredBuf920),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(2).control.udcInit=List(0,0,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(2).control.childrenAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(2).control.siblingAndTree = List(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(2).control.incrementXbar.outSelect(0) = 2
    lcus(1)(2).control.incrementXbar.outSelect(1) = 1
    lcus(1)(2).control.incrementXbar.outSelect(2) = 3
    lcus(1)(2).control.udcDecSelect=List(1,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(2).control.tokenOutXbar.outSelect(1) = 1
    lcus(1)(2).control.doneXbar.outSelect(0) = 0
    lcus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    // Configuring scus(0)(0) <- PipeCU137_x1044_0
    // scu1785[-1,0].ib8111[3] -> AndTree619_TokenInAndTree.in(from:CtrlBox795.en.out at StreamCtrler122_x1053)
    // scu1785[-1,0].ib8539[4] -> AndTree619_TokenInAndTree.in(from:ScalarFIFO207_size.notFull at MemoryController206_x1045)
    // scu1785[-1,0].ib8543[5] -> AndTree619_TokenInAndTree.in(from:ScalarFIFO216_offset.notFull at MemoryController206_x1045)
    // scu1785[-1,0].ob8063[3] -> PipeCU137_x1044_0.enOut(to:ScalarFIFO207_size.enqueueEnable at MemoryController206_x1045,ScalarFIFO216_offset.enqueueEnable at MemoryController206_x1045)
    // PipeCU137_x1044_0.udcounters=[]
    // scus(0)(0).udcs=[None,None,None,None]
    scus(0)(0).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(0)(0).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(0)(0).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU137_x1044_0 isPipelining=false isStreaming=true
    scus(0)(0).control.streamingMuxSelect = 1
    // sm42503[0] -> ScalBuf139 swapWrite=NotConnected
    // sm42522[1] -> ScalBuf549 swapWrite=NotConnected
    scus(0)(0).control.tokenOutXbar.outSelect(3) = 7
    scus(0)(0).control.doneXbar.outSelect(0) = 0
    scus(0)(0).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
    // scus(0)(0).scalarInXbar=[Some(iw5811),Some(iw5815),None,None,None,None]
    scus(0)(0).scalarInXbar.outSelect(0) = 2
    scus(0)(0).scalarInXbar.outSelect(1) = 3
    // scus(0)(0).scalarOutXbar=[None,None,None,None,Some(pr(st42309[5],reg1795[9])),Some(pr(st42309[5],reg1794[8]))]
    scus(0)(0).scalarOutXbar.outSelect(4) = 1
    scus(0)(0).scalarOutXbar.outSelect(5) = 0
    scus(0)(0).counterChain.chain = List(1,1,0,0,0)
    scus(0)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    scus(0)(0).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    scus(0)(0).counterChain.counters(2) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 32), min=SVT(ConstSrc, 0), par=1)
    scus(0)(0).stages(0).opA = SVT(CounterSrc, 2)
    scus(0)(0).stages(0).opB = SVT(ConstSrc, 4)
    scus(0)(0).stages(0).opC = SVT()
    scus(0)(0).stages(0).opcode = FixMul
    scus(0)(0).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(0)(0).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(0)(0).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(0)(0).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(0)(0).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(0)(0).stages(1).opC = SVT()
    scus(0)(0).stages(1).opcode = FixAdd
    scus(0)(0).stages(1).res = List(SVT(CurrStageDst, 9))
    scus(0)(0).stages(1).fwd(9) = SVT(ALUSrc, 1)
    scus(0)(0).stages(2).opA = SVT(ConstSrc, 128)
    scus(0)(0).stages(2).opB = SVT()
    scus(0)(0).stages(2).opC = SVT()
    scus(0)(0).stages(2).opcode = BypassA
    scus(0)(0).stages(2).res = List(SVT(CurrStageDst, 8))
    scus(0)(0).stages(2).fwd(8) = SVT(ALUSrc, 2)
    scus(0)(0).stages(2).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(0).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(0).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(0).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring scus(1)(2) <- PipeCU259_x1063_0
    // scu2320[2,2].ib8431[3] -> AndTree679_TokenInAndTree.in(from:CtrlBox807.en.out at StreamCtrler244_x1072)
    // scu2320[2,2].ib8619[4] -> AndTree679_TokenInAndTree.in(from:ScalarFIFO338_offset.notFull at MemoryController328_x1064)
    // scu2320[2,2].ib8623[5] -> AndTree679_TokenInAndTree.in(from:ScalarFIFO329_size.notFull at MemoryController328_x1064)
    // scu2320[2,2].ob8383[3] -> PipeCU259_x1063_0.enOut(to:ScalarFIFO329_size.enqueueEnable at MemoryController328_x1064,ScalarFIFO338_offset.enqueueEnable at MemoryController328_x1064)
    // PipeCU259_x1063_0.udcounters=[]
    // scus(1)(2).udcs=[None,None,None,None]
    scus(1)(2).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(1)(2).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(1)(2).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU259_x1063_0 isPipelining=false isStreaming=true
    scus(1)(2).control.streamingMuxSelect = 1
    // sm52008[0] -> ScalBuf261 swapWrite=NotConnected
    // sm52027[1] -> ScalBuf560 swapWrite=NotConnected
    scus(1)(2).control.tokenOutXbar.outSelect(3) = 7
    scus(1)(2).control.doneXbar.outSelect(0) = 0
    scus(1)(2).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
  }
}
