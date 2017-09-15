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
import templates._
import plasticine.pisa.enums._

trait DotProductTrait3 extends DotProductTrait2  {
  self:DotProductTrait =>
  def config3:Unit = {
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    cus(1)(1).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(1)(1).asPCUBits.stages(0).opB = SVT()
    cus(1)(1).asPCUBits.stages(0).opC = SVT()
    cus(1)(1).asPCUBits.stages(0).opcode = BypassA
    cus(1)(1).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 0))
    cus(1)(1).asPCUBits.stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(1)(1).asPCUBits.stages(1).opA = SVT(PrevStageSrc, 0)
    cus(1)(1).asPCUBits.stages(1).opB = SVT(CurrStageSrc, 1)
    cus(1)(1).asPCUBits.stages(1).opC = SVT()
    cus(1)(1).asPCUBits.stages(1).opcode = FixAdd
    cus(1)(1).asPCUBits.stages(1).res = List(SVT(CurrStageDst, 1))
    cus(1)(1).asPCUBits.accumInit = 0
    cus(1)(1).asPCUBits.stages(1).fwd(1) = SVT(ALUSrc, 1)
    cus(1)(1).asPCUBits.stages(2).opA = SVT(PrevStageSrc, 1)
    cus(1)(1).asPCUBits.stages(2).opB = SVT()
    cus(1)(1).asPCUBits.stages(2).opC = SVT()
    cus(1)(1).asPCUBits.stages(2).opcode = BypassA
    cus(1)(1).asPCUBits.stages(2).res = List(SVT(CurrStageDst, 5))
    cus(1)(1).asPCUBits.stages(2).fwd(5) = SVT(ALUSrc, 2)
    cus(1)(1).asPCUBits.stages(3).fwd(5) = SVT(PrevStageSrc, 5)
    cus(1)(1).asPCUBits.stages(4).fwd(5) = SVT(PrevStageSrc, 5)
    cus(1)(1).asPCUBits.stages(5).fwd(5) = SVT(PrevStageSrc, 5)
    cus(1)(1).asPCUBits.stages(6).fwd(5) = SVT(PrevStageSrc, 5)
    cus(1)(1).asPCUBits.stages(7).fwd(5) = SVT(PrevStageSrc, 5)
    // Configuring lcus(0)(1) <- StreamCtrler122_x1053
    lcus(0)(1).counterChain.chain = List(0,0,0,0,0)
    // ocu2524[0,1].ib7799[1] -> TokBuf902.inc(from:CtrlBox783.en.out at MetaPipeCU32_x1094)
    // ocu2524[0,1].ib7803[2] -> TokBuf943.inc(from:CtrlBox630.doneOut at MemoryController206_x1045)
    // ocu2524[0,1].ib7807[3] -> CredBuf936.inc(from:PipeCU366_x1087_0.doneOut at PipeCU366_x1087_0)
    // ocu2524[0,1].ob7767[1] -> CtrlBox795.en.out(to:Ctr129.en at StreamCtrler122_x1053,AndTree619_TokenInAndTree.in at PipeCU137_x1044_0)
    // StreamCtrler122_x1053.udcounters=[MemoryController206_x1045 -> TokBuf943,MetaPipeCU32_x1094 -> TokBuf902,SRAM64 -> CredBuf936]
    // lcus(0)(1).udcs=[Some(TokBuf943),Some(TokBuf902),Some(CredBuf936),None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(0)(1).control.udcInit=List(0,0,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(1).control.childrenAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.siblingAndTree = List(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(0)(1).control.incrementXbar.outSelect(0) = 2
    lcus(0)(1).control.incrementXbar.outSelect(1) = 1
    lcus(0)(1).control.incrementXbar.outSelect(2) = 3
    lcus(0)(1).control.udcDecSelect=List(0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(0)(1).control.tokenOutXbar.outSelect(1) = 1
    lcus(0)(1).control.doneXbar.outSelect(0) = 0
    lcus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    // Configuring lcus(1)(0) <- SeqCU17_x1098
    lcus(1)(0).counterChain.chain = List(0,0,0,0,0)
    // ocu2706[1,0].ib7707[2] -> TokBuf896.inc(from:MetaPipeCU32_x1094.doneOut at MetaPipeCU32_x1094)
    // ocu2706[1,0].ib7711[3] -> TokBuf884.inc(from:CtrlBox881.command at Top1_Top)
    // ocu2706[1,0].ob7667[0] -> CtrlBox771.en.out(to:Ctr24.en at SeqCU17_x1098,TokBuf890.inc at MetaPipeCU32_x1094)
    // ocu2706[1,0].ob7671[1] -> SeqCU17_x1098.doneOut(to:CtrlBox881.status at Top1_Top)
    // SeqCU17_x1098.udcounters=[Top1_Top -> TokBuf884,MetaPipeCU32_x1094 -> TokBuf896]
    // lcus(1)(0).udcs=[Some(TokBuf884),Some(TokBuf896),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(0).control.udcInit=List(0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(0).control.childrenAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(0).control.siblingAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(0).control.incrementXbar.outSelect(0) = 3
    lcus(1)(0).control.incrementXbar.outSelect(1) = 2
    lcus(1)(0).control.udcDecSelect=List(1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(0).control.tokenOutXbar.outSelect(0) = 1
    lcus(1)(0).control.tokenOutXbar.outSelect(1) = 0
    lcus(1)(0).control.doneXbar.outSelect(0) = 0
    lcus(1)(0).control.pulserMax=1
    lcus(1)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    // Configuring lcus(1)(1) <- MetaPipeCU32_x1094
    lcus(1)(1).counterChain.chain = List(0,0,0,0,0)
    // ocu2797[1,1].ib7851[2] -> TokBuf914.inc(from:PipeCU463_x1092_0.doneOut at PipeCU463_x1092_0)
    // ocu2797[1,1].ib7855[3] -> TokBuf890.inc(from:CtrlBox771.en.out at SeqCU17_x1098)
    // ocu2797[1,1].ob7811[0] -> CtrlBox783.en.out(to:Ctr44.en at MetaPipeCU32_x1094,TokBuf902.inc at StreamCtrler122_x1053,TokBuf908.inc at StreamCtrler244_x1072)
    // ocu2797[1,1].ob7815[1] -> MetaPipeCU32_x1094.doneOut(to:TokBuf896.inc at SeqCU17_x1098)
    // MetaPipeCU32_x1094.udcounters=[PipeCU463_x1092_0 -> TokBuf914,SeqCU17_x1098 -> TokBuf890]
    // lcus(1)(1).udcs=[Some(TokBuf914),Some(TokBuf890),None,None,None,None,None,None,None,None,None,None,None,None,None]
    lcus(1)(1).control.udcInit=List(0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(1).control.childrenAndTree = List(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    lcus(1)(1).control.siblingAndTree = List(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    // sm55849[0] -> ScalBuf33 swapWrite=NotConnected
    lcus(1)(1).control.incrementXbar.outSelect(0) = 2
    lcus(1)(1).control.incrementXbar.outSelect(1) = 3
    lcus(1)(1).control.udcDecSelect=List(0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    lcus(1)(1).control.tokenOutXbar.outSelect(0) = 1
    lcus(1)(1).control.tokenOutXbar.outSelect(1) = 0
    lcus(1)(1).control.doneXbar.outSelect(0) = 0
    lcus(1)(1).control.pulserMax=3
    lcus(1)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 0), stride=SVT(ConstSrc, 32), min=SVT(ConstSrc, 0), par=1)
    // Configuring lcus(1)(2) <- StreamCtrler244_x1072
    lcus(1)(2).counterChain.chain = List(0,0,0,0,0)
    // ocu2888[1,2].ib7991[1] -> TokBuf927.inc(from:CtrlBox690.doneOut at MemoryController328_x1064)
  }
}
