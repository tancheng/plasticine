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
    scus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 3200), min=SVT(ConstSrc, 0), par=1)
    scus(1)(2).stages(0).opA = SVT(CounterSrc, 0)
    scus(1)(2).stages(0).opB = SVT(ConstSrc, 4)
    scus(1)(2).stages(0).opC = SVT()
    scus(1)(2).stages(0).opcode = FixMul
    scus(1)(2).stages(0).res = List(SVT(CurrStageDst, 0))
    scus(1)(2).stages(0).fwd(0) = SVT(ALUSrc, 0)
    scus(1)(2).stages(0).fwd(7) = SVT(ScalarFIFOSrc, 0)
    scus(1)(2).stages(1).opA = SVT(PrevStageSrc, 0)
    scus(1)(2).stages(1).opB = SVT(PrevStageSrc, 7)
    scus(1)(2).stages(1).opC = SVT()
    scus(1)(2).stages(1).opcode = FixAdd
    scus(1)(2).stages(1).res = List(SVT(CurrStageDst, 8))
    scus(1)(2).stages(1).fwd(8) = SVT(ALUSrc, 1)
    scus(1)(2).stages(2).opA = SVT(ConstSrc, 12800)
    scus(1)(2).stages(2).opB = SVT()
    scus(1)(2).stages(2).opC = SVT()
    scus(1)(2).stages(2).opcode = BypassA
    scus(1)(2).stages(2).res = List(SVT(CurrStageDst, 9))
    scus(1)(2).stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(2).fwd(9) = SVT(ALUSrc, 2)
    scus(1)(2).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(2).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(2).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring mcs(1)(0) <- MemoryController258_x1064
    // mctpe=TileLoad
    // mcs(1)(0).scalarInXbar=[Some(iw4383[1]),None,Some(iw4385[2]),None]
    mcs(1)(0).scalarInXbar.outSelect(0) = 1
    mcs(1)(0).scalarInXbar.outSelect(2) = 2
    // sm15320[0] -> ScalarFIFO268_offset 
    // sm15334[2] -> ScalarFIFO259_size 
    mcs(1)(0).tokenInXbar.outSelect(0) = 0
    mcs(1)(0).tokenInXbar.outSelect(2) = 0
    // ob6911[0] -> CtrlBox611.done
    // ob6975[1] -> ScalarFIFO259_size.notFull
    // ob6977[2] -> ScalarFIFO268_offset.notFull
    mcs(1)(0).tokenOutXbar.outSelect(0) = 4
    mcs(1)(0).tokenOutXbar.outSelect(1) = 2
    mcs(1)(0).tokenOutXbar.outSelect(2) = 0
    // Configuring mcs(1)(2) <- MemoryController164_x1045
    // mctpe=TileLoad
    // mcs(1)(2).scalarInXbar=[Some(iw4399[1]),None,Some(iw4401[2]),None]
    mcs(1)(2).scalarInXbar.outSelect(0) = 1
    mcs(1)(2).scalarInXbar.outSelect(2) = 2
    // sm15390[0] -> ScalarFIFO174_offset 
    // sm15404[2] -> ScalarFIFO165_size 
    mcs(1)(2).tokenInXbar.outSelect(0) = 0
    mcs(1)(2).tokenInXbar.outSelect(2) = 0
    // ob6943[0] -> CtrlBox572.done
    // ob6991[1] -> ScalarFIFO174_offset.notFull
    // ob6993[2] -> ScalarFIFO165_size.notFull
    mcs(1)(2).tokenOutXbar.outSelect(0) = 4
    mcs(1)(2).tokenOutXbar.outSelect(1) = 0
    mcs(1)(2).tokenOutXbar.outSelect(2) = 2
  }
}
