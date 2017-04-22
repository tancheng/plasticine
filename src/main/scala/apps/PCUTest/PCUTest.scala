package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.PISADesign
import plasticine.pisa.ir._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import plasticine.templates._
import plasticine.pisa.enums._

object PCUTest extends PISADesign with PCUTestTrait
trait PCUTestTrait {
  def main(args: String*) = {
    val ctrs_0_0 = Array.tabulate(3) { i => CounterRCBits.zeroes(32)}
//    val cc_0_0 = CounterChainBits(List(1, 0), ctrs_0_0)
//    val sts_0_0 = Array.tabulate(20) { i => PipeStageBits.zeroes(16, 32)}
    val cu: CUBits = PCUBits.zeroes(cuParams(0)(0).asInstanceOf[PCUParams])


//    cu.counterChain.counters(0) = CounterRCBits(max=SrcValueTuple(ConstSrc, 10), stride=SrcValueTuple(ConstSrc, 0), min=SrcValueTuple(ConstSrc, 1), par=1)
//    cu.counterChain.counters(1) = CounterRCBits(max=SrcValueTuple(ConstSrc, 20), stride=SrcValueTuple(ConstSrc, 1), min=SrcValueTuple(ConstSrc, 1), par=1)
//    cu.counterChain.counters(2) = CounterRCBits(max=SrcValueTuple(ConstSrc, 30), stride=SrcValueTuple(ConstSrc, 2), min=SrcValueTuple(ConstSrc, 1), par=1)
//
////    cu.stages(0) = PipeStageBits(SrcValueTuple(ScalarFIFOSrc, 0),SrcValueTuple(ConstSrc, 4),SrcValueTuple(), FixAdd, List(SrcValueTuple(CurrStageDst, 10),SrcValueTuple(CurrStageDst, 0)), Array.tabulate(16) { i => SrcValueTuple() })
//    cu.stages(0) = PipeStageBits(SrcValueTuple(ConstSrc, 100), SrcValueTuple(ConstSrc, 200), SrcValueTuple(ConstSrc, 300), FixAdd, List(SrcValueTuple(CurrStageDst, 10),SrcValueTuple(CurrStageDst, 0)), Array.tabulate(16) { i => SrcValueTuple() })
//    cu.stages(1) = PipeStageBits(SrcValueTuple(ScalarFIFOSrc, 2), SrcValueTuple(ScalarFIFOSrc, 3), SrcValueTuple(ScalarFIFOSrc, 4), FixAdd, List(SrcValueTuple(CurrStageDst, 8),SrcValueTuple(CurrStageDst, 4)),  Array.tabulate(16) { i => SrcValueTuple() })
//    cu.stages(2) = PipeStageBits(SrcValueTuple(ConstSrc, 3))
//    cu.stages(3) = PipeStageBits(SrcValueTuple(ConstSrc, 4))
//    cu.stages(4) = PipeStageBits(SrcValueTuple(ConstSrc, 5))
//    cu.stages(5) = PipeStageBits(SrcValueTuple(ConstSrc, 6))
//    cu.stages(0).fwd(0) = SrcValueTuple(ALUSrc, 0)
//    cu.stages(0).fwd(10) = SrcValueTuple(ALUSrc, 0)
//    cu.stages(1).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(2).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(3).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(4).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(5).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(6).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(7).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(8).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(9).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(10).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(11).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(12).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(13).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(14).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(15).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(16).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(17).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(18).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
//    cu.stages(19).fwd(10) = SrcValueTuple(PrevStageSrc, 10)
    cu
  }
}
