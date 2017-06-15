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

trait DotProductTrait5 extends DotProductTrait4  {
  self:DotProductTrait =>
  def config5:Unit = {
    // scus(1)(2).scalarInXbar=[Some(iw6135),Some(iw6131),None,None,None,None]
    scus(1)(2).scalarInXbar.outSelect(0) = 3
    scus(1)(2).scalarInXbar.outSelect(1) = 2
    // scus(1)(2).scalarOutXbar=[None,None,None,None,Some(pr(st51814[5],reg2330[9])),Some(pr(st51814[5],reg2329[8]))]
    scus(1)(2).scalarOutXbar.outSelect(4) = 1
    scus(1)(2).scalarOutXbar.outSelect(5) = 0
    scus(1)(2).counterChain.chain = List(1,1,0,0,0)
    scus(1)(2).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    scus(1)(2).counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    scus(1)(2).counterChain.counters(2) = CounterRCBits(max=SVT(ScalarFIFOSrc, 1), stride=SVT(ConstSrc, 32), min=SVT(ConstSrc, 0), par=1)
    scus(1)(2).stages(0).opA = SVT(CounterSrc, 2)
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
    scus(1)(2).stages(1).res = List(SVT(CurrStageDst, 9))
    scus(1)(2).stages(1).fwd(9) = SVT(ALUSrc, 1)
    scus(1)(2).stages(2).opA = SVT(ConstSrc, 128)
    scus(1)(2).stages(2).opB = SVT()
    scus(1)(2).stages(2).opC = SVT()
    scus(1)(2).stages(2).opcode = BypassA
    scus(1)(2).stages(2).res = List(SVT(CurrStageDst, 8))
    scus(1)(2).stages(2).fwd(8) = SVT(ALUSrc, 2)
    scus(1)(2).stages(2).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(2).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(3).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(2).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(4).fwd(9) = SVT(PrevStageSrc, 9)
    scus(1)(2).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    scus(1)(2).stages(5).fwd(9) = SVT(PrevStageSrc, 9)
    // Configuring mcs(0)(0) <- MemoryController206_x1045
    // mctpe=TileLoad
    // mcs(0)(0).scalarInXbar=[Some(iw6195),None,Some(iw6199),None]
    mcs(0)(0).scalarInXbar.outSelect(0) = 0
    mcs(0)(0).scalarInXbar.outSelect(2) = 1
    // sm52745[0] -> ScalarFIFO216_offset 
    // sm52783[2] -> ScalarFIFO207_size 
    mcs(0)(0).tokenInXbar.outSelect(0) = 0
    mcs(0)(0).tokenInXbar.outSelect(2) = 0
    mcs(0)(0).tokenOutXbar.outSelect(0) = 2
    mcs(0)(0).tokenOutXbar.outSelect(1) = 0
    // Configuring mcs(1)(2) <- MemoryController328_x1064
    // mctpe=TileLoad
    // mcs(1)(2).scalarInXbar=[Some(iw6275),None,Some(iw6279),None]
    mcs(1)(2).scalarInXbar.outSelect(0) = 1
    mcs(1)(2).scalarInXbar.outSelect(2) = 2
    // sm53625[0] -> ScalarFIFO338_offset 
    // sm53663[2] -> ScalarFIFO329_size 
    mcs(1)(2).tokenInXbar.outSelect(0) = 0
    mcs(1)(2).tokenInXbar.outSelect(2) = 0
    mcs(1)(2).tokenOutXbar.outSelect(1) = 0
    mcs(1)(2).tokenOutXbar.outSelect(2) = 2
  }
}
