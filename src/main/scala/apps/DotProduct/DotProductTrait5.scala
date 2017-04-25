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

trait DotProductTrait5 extends DotProductTrait4 {
  self:DotProductTrait =>
  def config5:Unit = {
    // scus(1)(1).scalarOutXbar=[None,None,None,None,Some(pr(st11746[5],reg1251[8])),Some(pr(st11746[5],reg1252[9]))]
    scus(1)(1).scalarOutXbar.outSelect(4) = 0
    scus(1)(1).scalarOutXbar.outSelect(5) = 1
    scus(1)(1).counterChain.chain = List(1,1,0,0,0)
    scus(1)(1).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    scus(1)(1).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    scus(1)(1).counterChain.counters(2) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 320), min=SVT(ConstSrc, 0), par=1)
    scus(1)(1).stages(0).opA = SVT(CounterSrc, 2)
    scus(1)(1).stages(0).opB = SVT(ConstSrc, 4)
    scus(1)(1).stages(0).opC = SVT()
    scus(1)(1).stages(0).opcode = FixMul
    scus(1)(1).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(1)(1).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(1)(1).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(1)(1).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(1)(1).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(1)(1).stages(1).opC = SVT()
    scus(1)(1).stages(1).opcode = FixAdd
    scus(1)(1).stages(1).res = List(SVT(CurrStageDst, 8))
    scus(1)(1).stages(1).fwd(8) = SVT(ALUSrc, 1)
    scus(1)(1).stages(2).opA = SVT(ConstSrc, 1280)
    scus(1)(1).stages(2).opB = SVT()
    scus(1)(1).stages(2).opC = SVT()
    scus(1)(1).stages(2).opcode = BypassA
    scus(1)(1).stages(2).res = List(SVT(CurrStageDst, 9))
    scus(1)(1).stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(1).stages(2).fwd(9) = SVT(ALUSrc, 2)
    scus(1)(1).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(1).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(1).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(1).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(1).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(1).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring mcs(0)(1) <- MemoryController328_x1064
    // mctpe=TileLoad
    // mcs(0)(1).scalarInXbar=[Some(iw3668[0]),None,Some(iw3670[1]),None]
    mcs(0)(1).scalarInXbar.outSelect(0) = 0
    mcs(0)(1).scalarInXbar.outSelect(2) = 1
    // sm12735[0] -> ScalarFIFO338_offset 
    // sm12749[2] -> ScalarFIFO329_size 
    mcs(0)(1).tokenInXbar.outSelect(0) = 0
    mcs(0)(1).tokenInXbar.outSelect(2) = 0
    // ob4988[0] -> ScalarFIFO329_size.notFull
    // ob4990[1] -> ScalarFIFO338_offset.notFull
    // ob4948[2] -> CtrlBox695.done
    mcs(0)(1).tokenOutXbar.outSelect(0) = 2
    mcs(0)(1).tokenOutXbar.outSelect(1) = 0
    mcs(0)(1).tokenOutXbar.outSelect(2) = 4
    // Configuring mcs(1)(1) <- MemoryController206_x1045
    // mctpe=TileLoad
    // mcs(1)(1).scalarInXbar=[Some(iw3692[0]),None,Some(iw3694[1]),None]
    mcs(1)(1).scalarInXbar.outSelect(0) = 0
    mcs(1)(1).scalarInXbar.outSelect(2) = 1
    // sm12840[0] -> ScalarFIFO216_offset 
    // sm12854[2] -> ScalarFIFO207_size 
    mcs(1)(1).tokenInXbar.outSelect(0) = 0
    mcs(1)(1).tokenInXbar.outSelect(2) = 0
    // ob4956[0] -> CtrlBox656.done
    // ob5012[1] -> ScalarFIFO216_offset.notFull
    // ob5014[2] -> ScalarFIFO207_size.notFull
    mcs(1)(1).tokenOutXbar.outSelect(0) = 4
    mcs(1)(1).tokenOutXbar.outSelect(1) = 0
    mcs(1)(1).tokenOutXbar.outSelect(2) = 2
  }
}
