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

trait DotProductTrait3 extends DotProductTrait2 {
  self:DotProductTrait =>
  def config3:Unit = {
    cus(1)(1).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 3)
    cus(1)(1).asPCUBits.stages(0).opB = SVT(VectorFIFOSrc, 0)
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
    cus(1)(1).asPCUBits.stages(7).fwd(8) = SVT(PrevStageSrc, 8)
    cus(1)(1).asPCUBits.stages(8).fwd(8) = SVT(PrevStageSrc, 8)
    // Configuring lcus(1)(1) <- MetaPipeCU32_x1094
    lcus(1)(1).counterChain.chain = List(0,0,0,0,0)
    // MetaPipeCU32_x1094.udcounters=[Top1_Top -> TokBuf845,PipeCU463_x1092 -> TokBuf883]
    // lcus(1)(1).udcs=[Some(TokBuf845),Some(TokBuf883),None,None]
    lcus(1)(1).control.childrenAndTree = List(0, 1, 0, 0)
    lcus(1)(1).control.siblingAndTree = List(1, 0, 0, 0)
    // sm12627[0] -> ScalBuf33 swapWrite=NotConnected
    lcus(1)(1).control.incrementXbar.outSelect(0) = 6
    lcus(1)(1).control.incrementXbar.outSelect(1) = 7
    lcus(1)(1).control.udcDecSelect=List(1,1,-1,-1)
    // ob3933[2] -> CtrlBox761.done.out
    // ob3935[3] -> MetaPipeCU32_x1094.pulserSM
    lcus(1)(1).control.tokenOutXbar.outSelect(2) = 0
    lcus(1)(1).control.tokenOutXbar.outSelect(3) = 1
    lcus(1)(1).control.doneXbar.outSelect(0) = 0
    lcus(1)(1).control.pulserMax=3
    lcus(1)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 0), stride=SVT(ConstSrc, 320), min=SVT(ConstSrc, 0), par=1)
    // Configuring lcus(1)(2) <- StreamCtrler244_x1072
    lcus(1)(2).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler244_x1072.udcounters=[MemoryController328_x1064 -> TokBuf903,MetaPipeCU32_x1094 -> TokBuf877,SRAM99 -> CredBuf909]
    // lcus(1)(2).udcs=[Some(TokBuf903),Some(TokBuf877),Some(CredBuf909),None]
    lcus(1)(2).control.childrenAndTree = List(1, 0, 0, 0)
    lcus(1)(2).control.siblingAndTree = List(0, 1, 1, 0)
    lcus(1)(2).control.incrementXbar.outSelect(0) = 7
    lcus(1)(2).control.incrementXbar.outSelect(1) = 5
    lcus(1)(2).control.incrementXbar.outSelect(2) = 6
    lcus(1)(2).control.udcDecSelect=List(0,1,1,-1)
    // ob4077[2] -> AndTree786_SiblingAndTree.out
    // ob4079[3] -> CtrlBox785.done.out
    lcus(1)(2).control.tokenOutXbar.outSelect(2) = 3
    lcus(1)(2).control.tokenOutXbar.outSelect(3) = 0
    lcus(1)(2).control.doneXbar.outSelect(0) = 0
    lcus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    // Configuring lcus(2)(1) <- StreamCtrler122_x1053
    lcus(2)(1).counterChain.chain = List(0,0,0,0,0)
    // StreamCtrler122_x1053.udcounters=[MetaPipeCU32_x1094 -> TokBuf871,SRAM64 -> CredBuf896,MemoryController206_x1045 -> TokBuf890]
    // lcus(2)(1).udcs=[Some(TokBuf871),Some(CredBuf896),Some(TokBuf890),None]
    lcus(2)(1).control.childrenAndTree = List(0, 0, 1, 0)
  }
}
