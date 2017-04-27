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

trait DotProductTrait4 extends DotProductTrait3 {
  self:DotProductTrait =>
  def config4:Unit = {
    // StreamCtrler122_x1053.udcounters=[MetaPipeCU32_x1094 -> TokBuf871,MemoryController206_x1045 -> TokBuf890,SRAM64 -> CredBuf896]
    // lcus(2)(1).udcs=[Some(TokBuf871),Some(TokBuf890),Some(CredBuf896),None]
    lcus(2)(1).control.udcInit=List(1,1,2,-1)
    lcus(2)(1).control.childrenAndTree = List(0, 1, 0, 0)
    lcus(2)(1).control.siblingAndTree = List(1, 0, 1, 0)
    lcus(2)(1).control.incrementXbar.outSelect(0) = 5
    lcus(2)(1).control.incrementXbar.outSelect(1) = 6
    lcus(2)(1).control.incrementXbar.outSelect(2) = 7
    lcus(2)(1).control.udcDecSelect=List(1,0,1,-1)
    // ob3981[2] -> CtrlBox773.done.out
    // ob3983[3] -> AndTree774_SiblingAndTree.out
    lcus(2)(1).control.tokenOutXbar.outSelect(2) = 0
    lcus(2)(1).control.tokenOutXbar.outSelect(3) = 3
    lcus(2)(1).control.doneXbar.outSelect(0) = 0
    lcus(2)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    // Configuring scus(0)(1) <- PipeCU259_x1063
    // PipeCU259_x1063.udcounters=[]
    // scus(0)(1).udcs=[None,None,None,None]
    scus(0)(1).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(0)(1).control.fifoAndTree = List(0, 0, 0, 0)
    scus(0)(1).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU259_x1063 isPipelining=false isStreaming=true
    scus(0)(1).control.streamingMuxSelect = 1
    // sm9393[0] -> ScalBuf261 swapWrite=NotConnected
    // sm9400[1] -> ScalBuf592 swapWrite=NotConnected
    // ob4239[3] -> CtrlBox677.en.out
    scus(0)(1).control.tokenOutXbar.outSelect(3) = 5
    scus(0)(1).control.doneXbar.outSelect(0) = 0
    scus(0)(1).fifoNbufConfig=List(1,1,-1,-1)
    // scus(0)(1).scalarInXbar=[Some(iw2987[3]),Some(iw2985[2]),None,None]
    scus(0)(1).scalarInXbar.outSelect(0) = 3
    scus(0)(1).scalarInXbar.outSelect(1) = 2
    // scus(0)(1).scalarOutXbar=[None,None,None,None,Some(pr(st9339[5],reg575[9])),Some(pr(st9339[5],reg574[8]))]
    scus(0)(1).scalarOutXbar.outSelect(4) = 1
    scus(0)(1).scalarOutXbar.outSelect(5) = 0
    scus(0)(1).counterChain.chain = List(1,1,0,0,0)
    scus(0)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    scus(0)(1).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    scus(0)(1).counterChain.counters(2) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 320), min=SVT(ConstSrc, 0), par=1)
    scus(0)(1).stages(0).opA = SVT(CounterSrc, 2)
    scus(0)(1).stages(0).opB = SVT(ConstSrc, 4)
    scus(0)(1).stages(0).opC = SVT()
    scus(0)(1).stages(0).opcode = FixMul
    scus(0)(1).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(0)(1).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(0)(1).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(0)(1).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(0)(1).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(0)(1).stages(1).opC = SVT()
    scus(0)(1).stages(1).opcode = FixAdd
    scus(0)(1).stages(1).res = List(SVT(CurrStageDst, 9))
    scus(0)(1).stages(1).fwd(9) = SVT(ALUSrc, 1)
    scus(0)(1).stages(2).opA = SVT(ConstSrc, 1280)
    scus(0)(1).stages(2).opB = SVT()
    scus(0)(1).stages(2).opC = SVT()
    scus(0)(1).stages(2).opcode = BypassA
    scus(0)(1).stages(2).res = List(SVT(CurrStageDst, 8))
    scus(0)(1).stages(2).fwd(8) = SVT(ALUSrc, 2)
    scus(0)(1).stages(2).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(1).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(1).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(1).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(1).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(0)(1).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(0)(1).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring scus(1)(1) <- PipeCU137_x1044
    // PipeCU137_x1044.udcounters=[]
    // scus(1)(1).udcs=[None,None,None,None]
    scus(1)(1).control.tokenInAndTree = List(0, 0, 0, 1, 1, 1)
    scus(1)(1).control.fifoAndTree = List(0, 0, 0, 0)
    scus(1)(1).control.siblingAndTree = List(0, 0, 0, 0)
    // PipeCU137_x1044 isPipelining=false isStreaming=true
    scus(1)(1).control.streamingMuxSelect = 1
    // sm11229[0] -> ScalBuf139 swapWrite=NotConnected
    // sm11236[1] -> ScalBuf581 swapWrite=NotConnected
    // ob4271[3] -> CtrlBox638.en.out
    scus(1)(1).control.tokenOutXbar.outSelect(3) = 5
    scus(1)(1).control.doneXbar.outSelect(0) = 0
    scus(1)(1).fifoNbufConfig=List(1,1,-1,-1)
    // scus(1)(1).scalarInXbar=[Some(iw3017[2]),Some(iw3019[3]),None,None]
    scus(1)(1).scalarInXbar.outSelect(0) = 2
    scus(1)(1).scalarInXbar.outSelect(1) = 3
    // scus(1)(1).scalarOutXbar=[None,None,None,None,Some(pr(st11175[5],reg860[9])),Some(pr(st11175[5],reg859[8]))]
  }
}
