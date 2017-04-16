package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.ir._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import plasticine.templates._
import plasticine.pisa.enums._

trait InOutArg1 {
  self:InOutArg =>
  ssbs(1)(0).outSelect(4) = 18
  ssbs(1)(0).outSelect(18) = 4
  csbs(1)(0).outSelect(5) = 16
  csbs(1)(0).outSelect(22) = 17
  csbs(1)(0).outSelect(23) = 4
  csbs(1)(0).outSelect(24) = 15
  // Configuring cus(0)(0) <- PipeCU20_x192 {
    val ctrs_0_0 = Array.tabulate(8) { i => CounterRCBits.zeroes(32)}
    val cc_0_0 = CounterChainBits(List(0,0,0,0,0,0,0), ctrs_0_0)
    val sts_0_0 = Array.tabulate(20) { i => PipeStageBits.zeroes(16, 32)}
    cus(0)(0) = PCUBits(counterChain=cc_0_0, stages=sts_0_0)
    cus(0)(0).counterChain.counters(0) = CounterRCBits(max=SrcValueTuple(ConstSrc, 1), stride=SrcValueTuple(ConstSrc, 1), min=SrcValueTuple(ConstSrc, 1), par=1)
    cus(0)(0).stages(0) = PipeStageBits(SrcValueTuple(ScalarFIFOSrc, 0),SrcValueTuple(ConstSrc, 4),SrcValueTuple(), FixAdd, List(SrcValueTuple(CurrStageDst, 10),SrcValueTuple(CurrStageDst, 0)), Array.tabulate(16) { i => SrcValueTuple() })
    cus(0)(0).stages(0).fwd(0) = SrcValueTuple(ALUSrc, 0)
    cus(0)(0).stages(0).fwd(10) = SrcValueTuple(ALUSrc, 0)
    cus(0)(0).stages(1).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(2).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(3).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(4).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(5).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(6).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(7).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(8).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(9).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(10).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(11).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(12).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(13).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(14).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(15).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(16).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(17).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(18).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cus(0)(0).stages(19).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
  //  }
}
