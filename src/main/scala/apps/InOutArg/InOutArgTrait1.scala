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

trait InOutArgTrait1 {
  self:InOutArgTrait =>
  def config1:Unit = {
    ssbs(1)(0).outSelect(4) = 18
    ssbs(1)(0).outSelect(18) = 4
    csbs(1)(0).outSelect(5) = 16
    csbs(1)(0).outSelect(22) = 17
    csbs(1)(0).outSelect(23) = 4
    csbs(1)(0).outSelect(24) = 15
    // Configuring cus(0)(0) <- PipeCU20_x192
    val ctrs_0_0 = Array.tabulate(8) { i => CounterRCBits.zeroes(32)}
    val cc_0_0 = CounterChainBits(List(0,0,0,0,0,0,0), ctrs_0_0)
    val sts_0_0 = Array.tabulate(9) { i => PipeStageBits.zeroes(16, 32)}
    val scalarValidOuts = Array.tabulate(4) { i => SVT.zeroes(32) }
    val vectorValidOuts = Array.tabulate(4) { i => SVT.zeroes(32) }
    val control = PCUControlBoxBits.zeroes(cuParams(0)(0).asInstanceOf[PCUParams])
    val scalarInXbar = CrossbarBits.zeroes(ScalarSwitchParams(4, 2, 32))
    val scalarOutXbar = CrossbarBits.zeroes(ScalarSwitchParams(2, 4, 32))
    cus(0)(0) = PCUBits(counterChain=cc_0_0, stages=sts_0_0, scalarValidOut=scalarValidOuts, vectorValidOut=vectorValidOuts, control = control, scalarInXbar = scalarInXbar, scalarOutXbar = scalarOutXbar)
    cus(0)(0).counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=1)
    cus(0)(0).stages(0) = PipeStageBits(SVT(ScalarFIFOSrc, 0),SVT(ConstSrc, 4),SVT(), FixAdd, List(SVT(CurrStageDst, 0),SVT(CurrStageDst, 8)), Array.tabulate(16) { i => SVT() })
    cus(0)(0).stages(0).fwd(0) = SVT(ALUSrc, 0)
    cus(0)(0).stages(0).fwd(8) = SVT(ALUSrc, 0)
    cus(0)(0).stages(1).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(2).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(3).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(4).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(5).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(6).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(7).fwd(8) = SVT(PrevStageSrc, 8)
    cus(0)(0).stages(8).fwd(8) = SVT(PrevStageSrc, 8)
  }
}
