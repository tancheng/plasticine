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
    // Configuring lcus(0)(0) <- MetaPipeCU18_x1094
    lcus(0)(0).counterChain.chain = List(1,0,0,0,0)
    // MetaPipeCU18_x1094.udcounters=[Top1_Top -> TokBuf803,PipeCU393_x1092_0 -> TokBuf821]
    // lcus(0)(0).udcs=[Some(TokBuf803),Some(TokBuf821),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(0)(0).control.udcInit=List(0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(0).control.childrenAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(0).control.siblingAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // sm15425[0] -> ScalBuf19 swapWrite=NotConnected
    // ib6387[4] -> TokBuf821.inc(from:CtrlBox648.done.out at PipeCU393_x1092_0)
    // ib6389[5] -> TokBuf803.inc(from:CtrlBox747.command at Top1_Top)
    lcus(0)(0).control.incrementXbar.outSelect(0) = 5
    lcus(0)(0).control.incrementXbar.outSelect(1) = 4
    lcus(0)(0).control.udcDecSelect=List(1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6355[2] -> MetaPipeCU18_x1094.pulserSM(to:TokBuf809.inc at StreamCtrler108_x1053,TokBuf815.inc at StreamCtrler202_x1072)
    // ob6357[3] -> CtrlBox677.done.out(to:TokBuf803.dec at MetaPipeCU18_x1094,CtrlBox747.status at Top1_Top,TokBuf821.dec at MetaPipeCU18_x1094)
    lcus(0)(0).control.tokenOutXbar.outSelect(2) = 1
    lcus(0)(0).control.tokenOutXbar.outSelect(3) = 0
    lcus(0)(0).control.doneXbar.outSelect(0) = 0
    lcus(0)(0).control.pulserMax=3
    lcus(0)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 0), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    lcus(0)(0).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, -1), stride=SVT(ConstSrc, -1), min=SVT(ConstSrc, -1), par=1)
    // Configuring lcus(0)(1) <- StreamCtrler108_x1053
    lcus(0)(1).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler108_x1053.udcounters=[SRAM50 -> CredBuf842,MetaPipeCU18_x1094 -> TokBuf809,MemoryController164_x1045 -> TokBuf849]
    // lcus(0)(1).udcs=[Some(CredBuf842),Some(TokBuf809),Some(TokBuf849),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(0)(1).control.udcInit=List(2,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(1).control.childrenAndTree = List(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.siblingAndTree = List(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // ib6505[3] -> CredBuf842.inc(from:CtrlBox631.done.out at PipeCU296_x1087_0)
    // ib6507[4] -> TokBuf809.inc(from:MetaPipeCU18_x1094.pulserSM at MetaPipeCU18_x1094)
    // ib6509[5] -> TokBuf849.inc(from:CtrlBox572.done at MemoryController164_x1045)
    lcus(0)(1).control.incrementXbar.outSelect(0) = 3
    lcus(0)(1).control.incrementXbar.outSelect(1) = 4
    lcus(0)(1).control.incrementXbar.outSelect(2) = 5
    lcus(0)(1).control.udcDecSelect=List(1,1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    // ob6475[2] -> AndTree690_SiblingAndTree.out(to:AndTree565_TokenInAndTree.in at PipeCU109_x1044_0)
    // ob6477[3] -> CtrlBox689.done.out(to:CtrlBox518.writeDone.in at MemPipe38_x1033_dsp0,TokBuf809.dec at StreamCtrler108_x1053,CredBuf842.dec at StreamCtrler108_x1053)
    lcus(0)(1).control.tokenOutXbar.outSelect(2) = 3
    lcus(0)(1).control.tokenOutXbar.outSelect(3) = 0
  }
}
