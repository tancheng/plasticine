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

trait DotProductPar1Trait5 extends DotProductPar1Trait4 {
  self:DotProductPar1Trait =>
  def config5:Unit = {
    // ib6773[3] -> AndTree604_TokenInAndTree.in(from:AndTree702_SiblingAndTree.out at StreamCtrler202_x1072)
    // ib6979[4] -> AndTree604_TokenInAndTree.in(from:ScalarFIFO259_size.notFull at MemoryController258_x1064)
    // ib6981[5] -> AndTree604_TokenInAndTree.in(from:ScalarFIFO268_offset.notFull at MemoryController258_x1064)
    // ob6749[3] -> CtrlBox593.en.out(to:Ctr213.en at PipeCU203_x1063_0,ScalarFIFO259_size.enqueueEnable at MemoryController258_x1064,ScalarFIFO268_offset.enqueueEnable at MemoryController258_x1064)
    scus(1)(0).control.tokenOutXbar.outSelect(3) = 7
    scus(1)(0).control.doneXbar.outSelect(0) = 0
    scus(1)(0).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
    // scus(1)(0).scalarInXbar=[Some(iw4169[5]),Some(iw4167[4]),None,None,None,None]
    scus(1)(0).scalarInXbar.outSelect(0) = 5
    scus(1)(0).scalarInXbar.outSelect(1) = 4
    // scus(1)(0).scalarOutXbar=[None,None,None,None,Some(pr(st13421[5],reg722[8])),Some(pr(st13421[5],reg723[9]))]
    scus(1)(0).scalarOutXbar.outSelect(4) = 0
    scus(1)(0).scalarOutXbar.outSelect(5) = 1
    scus(1)(0).counterChain.chain = List(0,0,0,0,0)
    scus(1)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    scus(1)(0).stages(0).opA = SVT(CounterSrc, 0)
    scus(1)(0).stages(0).opB = SVT(ConstSrc, 4)
    scus(1)(0).stages(0).opC = SVT()
    scus(1)(0).stages(0).opcode = FixMul
    scus(1)(0).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(1)(0).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(1)(0).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(1)(0).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(1)(0).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(1)(0).stages(1).opC = SVT()
    scus(1)(0).stages(1).opcode = FixAdd
    scus(1)(0).stages(1).res = List(SVT(CurrStageDst, 8))
    scus(1)(0).stages(1).fwd(8) = SVT(ALUSrc, 1)
    scus(1)(0).stages(2).opA = SVT(ConstSrc, 12800)
    scus(1)(0).stages(2).opB = SVT()
    scus(1)(0).stages(2).opC = SVT()
    scus(1)(0).stages(2).opcode = BypassA
    scus(1)(0).stages(2).res = List(SVT(CurrStageDst, 9))
    scus(1)(0).stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(0).stages(2).fwd(9) = SVT(ALUSrc, 2)
    scus(1)(0).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(0).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(0).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(0).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(0).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(0).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring mcs(0)(2) <- MemoryController164_x1045
    // mctpe=TileLoad
    // mcs(0)(2).scalarInXbar=[Some(iw4375[0]),None,Some(iw4377[1]),None]
    mcs(0)(2).scalarInXbar.outSelect(0) = 0
    mcs(0)(2).scalarInXbar.outSelect(2) = 1
    // sm15285[0] -> ScalarFIFO174_offset 
    // sm15299[2] -> ScalarFIFO165_size 
    // ib6941[0] -> ScalarFIFO165_size.enqueueEnable(from:CtrlBox554.en.out at PipeCU109_x1044_0),ScalarFIFO174_offset.enqueueEnable(from:CtrlBox554.en.out at PipeCU109_x1044_0)
    mcs(0)(2).tokenInXbar.outSelect(0) = 0
    mcs(0)(2).tokenInXbar.outSelect(2) = 0
    // ob6967[0] -> ScalarFIFO165_size.notFull(to:AndTree565_TokenInAndTree.in at PipeCU109_x1044_0)
    // ob6969[1] -> ScalarFIFO174_offset.notFull(to:AndTree565_TokenInAndTree.in at PipeCU109_x1044_0)
    // ob6935[2] -> CtrlBox572.done(to:TokBuf849.inc at StreamCtrler108_x1053)
    mcs(0)(2).tokenOutXbar.outSelect(0) = 2
    mcs(0)(2).tokenOutXbar.outSelect(1) = 0
    mcs(0)(2).tokenOutXbar.outSelect(2) = 4
    // Configuring mcs(1)(0) <- MemoryController258_x1064
    // mctpe=TileLoad
    // mcs(1)(0).scalarInXbar=[Some(iw4383[1]),None,Some(iw4385[2]),None]
    mcs(1)(0).scalarInXbar.outSelect(0) = 1
    mcs(1)(0).scalarInXbar.outSelect(2) = 2
    // sm15320[0] -> ScalarFIFO268_offset 
    // sm15334[2] -> ScalarFIFO259_size 
    // ib6917[0] -> ScalarFIFO259_size.enqueueEnable(from:CtrlBox593.en.out at PipeCU203_x1063_0),ScalarFIFO268_offset.enqueueEnable(from:CtrlBox593.en.out at PipeCU203_x1063_0)
    mcs(1)(0).tokenInXbar.outSelect(0) = 0
    mcs(1)(0).tokenInXbar.outSelect(2) = 0
    // ob6911[0] -> CtrlBox611.done(to:TokBuf834.inc at StreamCtrler202_x1072)
    // ob6975[1] -> ScalarFIFO259_size.notFull(to:AndTree604_TokenInAndTree.in at PipeCU203_x1063_0)
    // ob6977[2] -> ScalarFIFO268_offset.notFull(to:AndTree604_TokenInAndTree.in at PipeCU203_x1063_0)
    mcs(1)(0).tokenOutXbar.outSelect(0) = 4
    mcs(1)(0).tokenOutXbar.outSelect(1) = 2
    mcs(1)(0).tokenOutXbar.outSelect(2) = 0
  }
}
