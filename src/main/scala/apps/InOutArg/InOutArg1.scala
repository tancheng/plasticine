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
  val ctrs_0_0 = Array.tabulate(8) { i => CounterRCBits.zeroes(32)}
  val cc_0_0 = CounterChainBits(List(0,0,0,0,0,0,0), ctrs_0_0)
  ctrs_0_0(0) = CounterRCBits(max=SrcValueTuple(ConstSrc, 1), stride=SrcValueTuple(ConstSrc, 1))
  val sts_0_0 = Array.tabulate(20) { i => PipeStageBits.zeroes(16, 32)}
//  val fwd0_0_0 = Array.tabulate(16) { i => SrcValueTuple() }
//  fwd0_0_0(0) = SrcValueTuple(ALUSrc, 0)
//  fwd0_0_0(10) = SrcValueTuple(ALUSrc, 0)
  sts_0_0(0) = PipeStageBits(
    SrcValueTuple(ScalarFIFOSrc, 0),
    SrcValueTuple(ConstSrc, 4),
    SrcValueTuple(),
    FixAdd,
    List(10,0)
  )
  cus(0)(0) = PCUBits(counterChain=cc_0_0, stages=sts_0_0)
}
