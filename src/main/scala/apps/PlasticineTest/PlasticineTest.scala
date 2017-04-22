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

object PlasticineTest extends PISADesign with PlasticineTestTrait
trait PlasticineTestTrait {
  def main(args: String*) = {
    val cus:Array[Array[CUBits]] = Array.tabulate(2, 2) {    case (i,j) =>
      cuParams(i)(j) match {
        case p:PCUParams => PCUBits.zeroes(p)
        case p:PMUParams => PMUBits.zeroes(p)
      }
    }
    val csbs = Array.tabulate(3, 3) {    case (i,j) =>
      CrossbarBits.zeroes(controlSwitchParams(i)(j))
    }
    val ssbs = Array.tabulate(3, 3) {    case (i,j) =>
      CrossbarBits.zeroes(scalarSwitchParams(i)(j))
    }
    val vsbs = Array.tabulate(3, 3) {    case (i,j) =>
      CrossbarBits.zeroes(vectorSwitchParams(i)(j))
    }
    val lcus = Array.tabulate(3, 3) {    case (i,j) =>
      SwitchCUBits.zeroes(switchCUParams(i)(j))
    }
    val plasticineBits = PlasticineBits (
      cu=cus,
      vectorSwitch=vsbs,
      scalarSwitch=ssbs,
      controlSwitch=csbs,
      switchCU=lcus,
      argOutMuxSelect=List(1,-1,-1)
    )

//    cus(0)(0).counterChain.counters(0) = CounterRCBits(max=SrcValueTuple(ConstSrc, 10), stride=SrcValueTuple(ConstSrc, 0), min=SrcValueTuple(ConstSrc, 1), par=1)
//    cus(0)(0).counterChain.counters(1) = CounterRCBits(max=SrcValueTuple(ConstSrc, 20), stride=SrcValueTuple(ConstSrc, 1), min=SrcValueTuple(ConstSrc, 1), par=1)
//    cus(0)(0).counterChain.counters(2) = CounterRCBits(max=SrcValueTuple(ConstSrc, 30), stride=SrcValueTuple(ConstSrc, 2), min=SrcValueTuple(ConstSrc, 1), par=1)
//
////    cu.stages(0) = PipeStageBits(SrcValueTuple(ScalarFIFOSrc, 0),SrcValueTuple(ConstSrc, 4),SrcValueTuple(), FixAdd, List(SrcValueTuple(CurrStageDst, 10),SrcValueTuple(CurrStageDst, 0)), Array.tabulate(16) { i => SrcValueTuple() })
//    cus(0)(0).stages(0) = PipeStageBits(SrcValueTuple(ConstSrc, 100), SrcValueTuple(ConstSrc, 200), SrcValueTuple(ConstSrc, 300), FixAdd, List(SrcValueTuple(CurrStageDst, 10),SrcValueTuple(CurrStageDst, 0)), Array.tabulate(16) { i => SrcValueTuple() })
//    cus(0)(0).stages(1) = PipeStageBits(SrcValueTuple(ScalarFIFOSrc, 2), SrcValueTuple(ScalarFIFOSrc, 3), SrcValueTuple(ScalarFIFOSrc, 4), FixAdd, List(SrcValueTuple(CurrStageDst, 8),SrcValueTuple(CurrStageDst, 4)), Array.tabulate(16) { i => SrcValueTuple() })
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
    plasticineBits
  }
}
