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

trait DotProductPar1Trait3 extends DotProductPar1Trait2 {
  self:DotProductPar1Trait =>
  def config3:Unit = {
    cus(1)(1).asPCUBits.stages(0).opB = SVT(VectorFIFOSrc, 3)
    cus(1)(1).asPCUBits.stages(0).opC = SVT()
    cus(1)(1).asPCUBits.stages(0).opcode = FixMul
    cus(1)(1).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(1)(1).asPCUBits.stages(1).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(1).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(1).opC = SVT()
    cus(1)(1).asPCUBits.stages(1).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(1).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(1).fwd(0) = SVT(ALUSrc, 1)
    cus(1)(1).asPCUBits.stages(2).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(2).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(2).opC = SVT()
    cus(1)(1).asPCUBits.stages(2).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(2).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(2).fwd(0) = SVT(ALUSrc, 2)
    cus(1)(1).asPCUBits.stages(3).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(3).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(3).opC = SVT()
    cus(1)(1).asPCUBits.stages(3).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(3).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(3).fwd(0) = SVT(ALUSrc, 3)
    cus(1)(1).asPCUBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(4).opB = SVT(ReduceTreeSrc, 0)
    cus(1)(1).asPCUBits.stages(4).opC = SVT()
    cus(1)(1).asPCUBits.stages(4).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(4).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(4).fwd(0) = SVT(ALUSrc, 4)
    cus(1)(1).asPCUBits.stages(5).opA = SVT(PrevStageSrc, 0)
    cus(1)(1).asPCUBits.stages(5).opB = SVT(CurrStageSrc, 1)
    cus(1)(1).asPCUBits.stages(5).opC = SVT()
    cus(1)(1).asPCUBits.stages(5).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(5).res = List(SVT(CurrStageDst, 1))
    cus(1)(1).asPCUBits.accumInit = 0
    cus(1)(1).asPCUBits.stages(5).fwd(1) = SVT(ALUSrc, 5)
    cus(1)(1).asPCUBits.stages(6).opA = SVT(PrevStageSrc, 1)
    cus(1)(1).asPCUBits.stages(6).opB = SVT()
    cus(1)(1).asPCUBits.stages(6).opC = SVT()
    cus(1)(1).asPCUBits.stages(6).opcode = BypassA
    cus(1)(1).asPCUBits.stages(6).res = List(SVT(CurrStageDst, 8))
    cus(1)(1).asPCUBits.stages(6).fwd(8) = SVT(ALUSrc, 6)
    // Configuring lcus(0)(1) <- StreamCtrler108_x1053
    lcus(0)(1).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler108_x1053.udcounters=[MemoryController164_x1045 -> TokBuf869,MetaPipeCU18_x1094 -> TokBuf809]
    // lcus(0)(1).udcs=[Some(TokBuf869),Some(TokBuf809),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(0)(1).control.udcInit=List(1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(1).control.childrenAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.siblingAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.incrementXbar.outSelect(0) = 4
    lcus(0)(1).control.incrementXbar.outSelect(1) = 5
    lcus(0)(1).control.udcDecSelect=List(0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6603[2] -> CtrlBox689.done.out
    // ob6605[3] -> AndTree690_SiblingAndTree.out
    lcus(0)(1).control.tokenOutXbar.outSelect(2) = 0
    lcus(0)(1).control.tokenOutXbar.outSelect(3) = 9
    lcus(0)(1).control.doneXbar.outSelect(0) = 0
    lcus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(1)(1) <- MetaPipeCU18_x1094
    lcus(1)(1).counterChain.chain = List(1,0,0,0,0)
    // MetaPipeCU18_x1094.udcounters=[StreamCtrler202_x1072 -> TokBuf839,Top1_Top -> TokBuf803,StreamCtrler108_x1053 -> TokBuf833,PipeCU296_x1087_0 -> TokBuf845,PipeCU393_x1092_0 -> TokBuf851]
    // lcus(1)(1).udcs=[Some(TokBuf839),Some(TokBuf803),Some(TokBuf833),Some(TokBuf845),Some(TokBuf851),None,None,None,None,None,None,None,None,None,None]
    lcus(1)(1).control.udcInit=List(1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(1).control.childrenAndTree = List(1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(1).control.siblingAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // sm16369[0] -> ScalBuf19 swapWrite=NotConnected
    lcus(1)(1).control.incrementXbar.outSelect(0) = 1
    lcus(1)(1).control.incrementXbar.outSelect(1) = 5
    lcus(1)(1).control.incrementXbar.outSelect(2) = 2
    lcus(1)(1).control.incrementXbar.outSelect(3) = 4
    lcus(1)(1).control.incrementXbar.outSelect(4) = 3
    lcus(1)(1).control.udcDecSelect=List(1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6643[2] -> MetaPipeCU18_x1094.pulserSM
    // ob6645[3] -> CtrlBox677.done.out
    lcus(1)(1).control.tokenOutXbar.outSelect(2) = 7
    lcus(1)(1).control.tokenOutXbar.outSelect(3) = 0
    lcus(1)(1).control.doneXbar.outSelect(0) = 0
    lcus(1)(1).control.pulserMax=2
    lcus(1)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 0), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    lcus(1)(1).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(2)(1) <- StreamCtrler202_x1072
    lcus(2)(1).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler202_x1072.udcounters=[MemoryController258_x1064 -> TokBuf860,MetaPipeCU18_x1094 -> TokBuf815]
  }
}
