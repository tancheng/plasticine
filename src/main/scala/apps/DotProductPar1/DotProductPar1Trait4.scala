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

trait DotProductPar1Trait4 extends DotProductPar1Trait3 {
  self:DotProductPar1Trait =>
  def config4:Unit = {
    lcus(0)(1).control.doneXbar.outSelect(0) = 0
    lcus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(1)(0) <- StreamCtrler202_x1072
    lcus(1)(0).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler202_x1072.udcounters=[MemoryController258_x1064 -> TokBuf834,SRAM85 -> CredBuf827,MetaPipeCU18_x1094 -> TokBuf815]
    // lcus(1)(0).udcs=[Some(TokBuf834),Some(CredBuf827),Some(TokBuf815),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(0).control.udcInit=List(0,2,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(0).control.childrenAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(0).control.siblingAndTree = List(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // ib6425[3] -> CredBuf827.inc(from:CtrlBox631.done.out at PipeCU296_x1087_0)
    // ib6427[4] -> TokBuf815.inc(from:MetaPipeCU18_x1094.pulserSM at MetaPipeCU18_x1094)
    // ib6429[5] -> TokBuf834.inc(from:CtrlBox611.done at MemoryController258_x1064)
    lcus(1)(0).control.incrementXbar.outSelect(0) = 5
    lcus(1)(0).control.incrementXbar.outSelect(1) = 3
    lcus(1)(0).control.incrementXbar.outSelect(2) = 4
    lcus(1)(0).control.udcDecSelect=List(0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6395[2] -> CtrlBox701.done.out(to:CtrlBox536.writeDone.in at MemPipe73_x1034_dsp0,TokBuf815.dec at StreamCtrler202_x1072,CredBuf827.dec at StreamCtrler202_x1072)
    // ob6397[3] -> AndTree702_SiblingAndTree.out(to:AndTree604_TokenInAndTree.in at PipeCU203_x1063_0)
    lcus(1)(0).control.tokenOutXbar.outSelect(2) = 0
    lcus(1)(0).control.tokenOutXbar.outSelect(3) = 3
    lcus(1)(0).control.doneXbar.outSelect(0) = 0
    lcus(1)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring scus(0)(0) <- PipeCU109_x1044_0
    // PipeCU109_x1044_0.udcounters=[]
    // scus(0)(0).udcs=[None,None,None,None]
    scus(0)(0).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(0)(0).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(0)(0).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU109_x1044_0 isPipelining=false isStreaming=true
    scus(0)(0).control.streamingMuxSelect = 1
    // sm11411[0] -> ScalBuf111 swapWrite=NotConnected
    // sm11418[1] -> ScalBuf497 swapWrite=NotConnected
    // ib6741[3] -> AndTree565_TokenInAndTree.in(from:AndTree690_SiblingAndTree.out at StreamCtrler108_x1053)
    // ib6955[4] -> AndTree565_TokenInAndTree.in(from:ScalarFIFO174_offset.notFull at MemoryController164_x1045)
    // ib6957[5] -> AndTree565_TokenInAndTree.in(from:ScalarFIFO165_size.notFull at MemoryController164_x1045)
    // ob6717[3] -> CtrlBox554.en.out(to:Ctr119.en at PipeCU109_x1044_0,ScalarFIFO165_size.enqueueEnable at MemoryController164_x1045,ScalarFIFO174_offset.enqueueEnable at MemoryController164_x1045)
    scus(0)(0).control.tokenOutXbar.outSelect(3) = 7
    scus(0)(0).control.doneXbar.outSelect(0) = 0
    scus(0)(0).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
    // scus(0)(0).scalarInXbar=[Some(iw4127[4]),Some(iw4129[5]),None,None,None,None]
    scus(0)(0).scalarInXbar.outSelect(0) = 4
    scus(0)(0).scalarInXbar.outSelect(1) = 5
    // scus(0)(0).scalarOutXbar=[None,None,None,None,Some(pr(st11357[5],reg437[8])),Some(pr(st11357[5],reg438[9]))]
    scus(0)(0).scalarOutXbar.outSelect(4) = 0
    scus(0)(0).scalarOutXbar.outSelect(5) = 1
    scus(0)(0).counterChain.chain = List(0,0,0,0,0)
    scus(0)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    scus(0)(0).stages(0).opA = SVT(CounterSrc, 0)
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
    scus(0)(0).stages(1).res = List(SVT(CurrStageDst, 8))
    scus(0)(0).stages(1).fwd(8) = SVT(ALUSrc, 1)
    scus(0)(0).stages(2).opA = SVT(ConstSrc, 12800)
    scus(0)(0).stages(2).opB = SVT()
    scus(0)(0).stages(2).opC = SVT()
    scus(0)(0).stages(2).opcode = BypassA
    scus(0)(0).stages(2).res = List(SVT(CurrStageDst, 9))
    scus(0)(0).stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(2).fwd(9) = SVT(ALUSrc, 2)
    scus(0)(0).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(0).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(0).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(0).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring scus(1)(0) <- PipeCU203_x1063_0
    // PipeCU203_x1063_0.udcounters=[]
    // scus(1)(0).udcs=[None,None,None,None]
    scus(1)(0).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(1)(0).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(1)(0).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU203_x1063_0 isPipelining=false isStreaming=true
    scus(1)(0).control.streamingMuxSelect = 1
    // sm13475[0] -> ScalBuf205 swapWrite=NotConnected
    // sm13482[1] -> ScalBuf508 swapWrite=NotConnected
  }
}
