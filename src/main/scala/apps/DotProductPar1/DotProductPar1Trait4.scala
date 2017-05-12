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
    lcus(1)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(1)(1) <- MetaPipeCU18_x1094
    lcus(1)(1).counterChain.chain = List(1,0,0,0,0)
    // MetaPipeCU18_x1094.udcounters=[Top1_Top -> TokBuf803,PipeCU393_x1092_0 -> TokBuf821]
    // lcus(1)(1).udcs=[Some(TokBuf803),Some(TokBuf821),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(1).control.udcInit=List(0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(1).control.childrenAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(1).control.siblingAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // sm15801[0] -> ScalBuf19 swapWrite=NotConnected
    // ib6547[4] -> TokBuf821.inc(from:CtrlBox648.done.out at PipeCU393_x1092_0)
    // ib6549[5] -> TokBuf803.inc(from:CtrlBox747.command at Top1_Top)
    lcus(1)(1).control.incrementXbar.outSelect(0) = 5
    lcus(1)(1).control.incrementXbar.outSelect(1) = 4
    lcus(1)(1).control.udcDecSelect=List(1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6515[2] -> MetaPipeCU18_x1094.pulserSM(to:TokBuf809.inc at StreamCtrler108_x1053,TokBuf815.inc at StreamCtrler202_x1072)
    // ob6517[3] -> CtrlBox677.done.out(to:TokBuf803.dec at MetaPipeCU18_x1094,CtrlBox747.status at Top1_Top,TokBuf821.dec at MetaPipeCU18_x1094)
    lcus(1)(1).control.tokenOutXbar.outSelect(2) = 1
    lcus(1)(1).control.tokenOutXbar.outSelect(3) = 0
    lcus(1)(1).control.doneXbar.outSelect(0) = 0
    lcus(1)(1).control.pulserMax=3
    lcus(1)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 0), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    lcus(1)(1).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring scus(0)(2) <- PipeCU109_x1044_0
    // PipeCU109_x1044_0.udcounters=[]
    // scus(0)(2).udcs=[None,None,None,None]
    scus(0)(2).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(0)(2).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(0)(2).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU109_x1044_0 isPipelining=false isStreaming=true
    scus(0)(2).control.streamingMuxSelect = 1
    // sm12787[0] -> ScalBuf111 swapWrite=NotConnected
    // sm12794[1] -> ScalBuf497 swapWrite=NotConnected
    // ib6869[3] -> AndTree565_TokenInAndTree.in(from:AndTree690_SiblingAndTree.out at StreamCtrler108_x1053)
    // ib6971[4] -> AndTree565_TokenInAndTree.in(from:ScalarFIFO165_size.notFull at MemoryController164_x1045)
    // ib6973[5] -> AndTree565_TokenInAndTree.in(from:ScalarFIFO174_offset.notFull at MemoryController164_x1045)
    // ob6845[3] -> CtrlBox554.en.out(to:Ctr119.en at PipeCU109_x1044_0,ScalarFIFO165_size.enqueueEnable at MemoryController164_x1045,ScalarFIFO174_offset.enqueueEnable at MemoryController164_x1045)
    scus(0)(2).control.tokenOutXbar.outSelect(3) = 7
    scus(0)(2).control.doneXbar.outSelect(0) = 0
    scus(0)(2).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
    // scus(0)(2).scalarInXbar=[Some(iw4287[4]),Some(iw4289[5]),None,None,None,None]
    scus(0)(2).scalarInXbar.outSelect(0) = 4
    scus(0)(2).scalarInXbar.outSelect(1) = 5
    // scus(0)(2).scalarOutXbar=[None,None,None,None,Some(pr(st12733[5],reg628[9])),Some(pr(st12733[5],reg627[8]))]
    scus(0)(2).scalarOutXbar.outSelect(4) = 1
    scus(0)(2).scalarOutXbar.outSelect(5) = 0
    scus(0)(2).counterChain.chain = List(0,0,0,0,0)
    scus(0)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    scus(0)(2).stages(0).opA = SVT(CounterSrc, 0)
    scus(0)(2).stages(0).opB = SVT(ConstSrc, 4)
    scus(0)(2).stages(0).opC = SVT()
    scus(0)(2).stages(0).opcode = FixMul
    scus(0)(2).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(0)(2).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(0)(2).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(0)(2).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(0)(2).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(0)(2).stages(1).opC = SVT()
    scus(0)(2).stages(1).opcode = FixAdd
    scus(0)(2).stages(1).res = List(SVT(CurrStageDst, 9))
    scus(0)(2).stages(1).fwd(9) = SVT(ALUSrc, 1)
    scus(0)(2).stages(2).opA = SVT(ConstSrc, 12800)
    scus(0)(2).stages(2).opB = SVT()
    scus(0)(2).stages(2).opC = SVT()
    scus(0)(2).stages(2).opcode = BypassA
    scus(0)(2).stages(2).res = List(SVT(CurrStageDst, 8))
    scus(0)(2).stages(2).fwd(8) = SVT(ALUSrc, 2)
    scus(0)(2).stages(2).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(2).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(2).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(2).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(2).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(2).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(2).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
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
