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
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(1)(1).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
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
    cus(1)(1).asPCUBits.stages(6).res = List(SVT(CurrStageDst, 5))
    cus(1)(1).asPCUBits.stages(6).fwd(5) = SVT(ALUSrc, 6)
    // Configuring lcus(0)(1) <- StreamCtrler108_x1053
    lcus(0)(1).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler108_x1053.udcounters=[SRAM50 -> CredBuf842,MemoryController164_x1045 -> TokBuf849,MetaPipeCU18_x1094 -> TokBuf809]
    // lcus(0)(1).udcs=[Some(CredBuf842),Some(TokBuf849),Some(TokBuf809),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(0)(1).control.udcInit=List(2,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(1).control.childrenAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.siblingAndTree = List(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // ib6505[3] -> TokBuf809.inc(from:MetaPipeCU18_x1094.pulserSM at MetaPipeCU18_x1094)
    // ib6507[4] -> CredBuf842.inc(from:CtrlBox631.done.out at PipeCU296_x1087_0)
    // ib6509[5] -> TokBuf849.inc(from:CtrlBox572.done at MemoryController164_x1045)
    lcus(0)(1).control.incrementXbar.outSelect(0) = 4
    lcus(0)(1).control.incrementXbar.outSelect(1) = 5
    lcus(0)(1).control.incrementXbar.outSelect(2) = 3
    lcus(0)(1).control.udcDecSelect=List(1,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6475[2] -> CtrlBox689.done.out(to:CtrlBox518.writeDone.in at MemPipe38_x1033_dsp0,TokBuf809.dec at StreamCtrler108_x1053,CredBuf842.dec at StreamCtrler108_x1053)
    // ob6477[3] -> AndTree690_SiblingAndTree.out(to:AndTree565_TokenInAndTree.in at PipeCU109_x1044_0)
    lcus(0)(1).control.tokenOutXbar.outSelect(2) = 0
    lcus(0)(1).control.tokenOutXbar.outSelect(3) = 3
    lcus(0)(1).control.doneXbar.outSelect(0) = 0
    lcus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(1)(0) <- StreamCtrler202_x1072
    lcus(1)(0).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler202_x1072.udcounters=[SRAM85 -> CredBuf827,MetaPipeCU18_x1094 -> TokBuf815,MemoryController258_x1064 -> TokBuf834]
    // lcus(1)(0).udcs=[Some(CredBuf827),Some(TokBuf815),Some(TokBuf834),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(0).control.udcInit=List(2,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(0).control.childrenAndTree = List(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(0).control.siblingAndTree = List(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // ib6425[3] -> TokBuf815.inc(from:MetaPipeCU18_x1094.pulserSM at MetaPipeCU18_x1094)
    // ib6427[4] -> TokBuf834.inc(from:CtrlBox611.done at MemoryController258_x1064)
    // ib6429[5] -> CredBuf827.inc(from:CtrlBox631.done.out at PipeCU296_x1087_0)
    lcus(1)(0).control.incrementXbar.outSelect(0) = 5
    lcus(1)(0).control.incrementXbar.outSelect(1) = 3
    lcus(1)(0).control.incrementXbar.outSelect(2) = 4
    lcus(1)(0).control.udcDecSelect=List(1,1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6395[2] -> CtrlBox701.done.out(to:CtrlBox536.writeDone.in at MemPipe73_x1034_dsp0,TokBuf815.dec at StreamCtrler202_x1072,CredBuf827.dec at StreamCtrler202_x1072)
    // ob6397[3] -> AndTree702_SiblingAndTree.out(to:AndTree604_TokenInAndTree.in at PipeCU203_x1063_0)
    lcus(1)(0).control.tokenOutXbar.outSelect(2) = 0
    lcus(1)(0).control.tokenOutXbar.outSelect(3) = 3
    lcus(1)(0).control.doneXbar.outSelect(0) = 0
  }
}
