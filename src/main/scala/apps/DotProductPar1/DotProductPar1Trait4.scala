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
    // StreamCtrler108_x1053.udcounters=[MemoryController164_x1045 -> TokBuf869,MetaPipeCU18_x1094 -> TokBuf809]
    // lcus(1)(2).udcs=[Some(TokBuf869),Some(TokBuf809),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(2).control.udcInit=List(1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(2).control.childrenAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(2).control.siblingAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(2).control.incrementXbar.outSelect(0) = 4
    lcus(1)(2).control.incrementXbar.outSelect(1) = 5
    lcus(1)(2).control.udcDecSelect=List(0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6635[2] -> CtrlBox689.done.out
    // ob6637[3] -> AndTree690_SiblingAndTree.out
    lcus(1)(2).control.tokenOutXbar.outSelect(2) = 0
    lcus(1)(2).control.tokenOutXbar.outSelect(3) = 9
    lcus(1)(2).control.doneXbar.outSelect(0) = 0
    lcus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
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
    // ob6749[3] -> CtrlBox593.en.out
    scus(1)(0).control.tokenOutXbar.outSelect(3) = 13
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
    // Configuring scus(1)(2) <- PipeCU109_x1044_0
    // PipeCU109_x1044_0.udcounters=[]
    // scus(1)(2).udcs=[None,None,None,None]
    scus(1)(2).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(1)(2).control.fifoAndTree = List(0, 0, 0, 0, 0, 0)
    scus(1)(2).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU109_x1044_0 isPipelining=false isStreaming=true
    scus(1)(2).control.streamingMuxSelect = 1
    // sm14851[0] -> ScalBuf111 swapWrite=NotConnected
    // sm14858[1] -> ScalBuf497 swapWrite=NotConnected
    // ob6877[3] -> CtrlBox554.en.out
    scus(1)(2).control.tokenOutXbar.outSelect(3) = 13
    scus(1)(2).control.doneXbar.outSelect(0) = 0
    scus(1)(2).fifoNbufConfig=List(1,1,-1,-1,-1,-1)
    // scus(1)(2).scalarInXbar=[Some(iw4327[4]),Some(iw4329[5]),None,None,None,None]
    scus(1)(2).scalarInXbar.outSelect(0) = 4
    scus(1)(2).scalarInXbar.outSelect(1) = 5
    // scus(1)(2).scalarOutXbar=[None,None,None,None,Some(pr(st14797[5],reg912[8])),Some(pr(st14797[5],reg913[9]))]
    scus(1)(2).scalarOutXbar.outSelect(4) = 0
    scus(1)(2).scalarOutXbar.outSelect(5) = 1
    scus(1)(2).counterChain.chain = List(0,0,0,0,0)
  }
}
