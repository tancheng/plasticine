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

  def getCounter(m: Int, s: Int, p: Int) = CounterRCBits(max=SrcValueTuple(ConstSrc, m), stride=SrcValueTuple(ConstSrc, s), par=p)

  def main(args: String*) = {
    val cu = PCUBits.zeroes(cuParams(0)(0).asInstanceOf[PCUParams])

    // Datapath
    cu.counterChain.counters(0) = getCounter(256000, 1, 16)

    cu.stages(0).opA = SrcValueTuple(CounterSrc, 0)
    cu.stages(0).opB = SrcValueTuple(ConstSrc, 100)
    cu.stages(0).opcode = FltMul
    cu.stages(0).res = List(SrcValueTuple(CurrStageDst, 0))
    cu.stages(0).enableSelect = SrcValueTuple(PrevStageSrc, 0)

    cu.stages(1).opA = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(1).opB = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(1).opcode = FltMul
    cu.stages(1).res = List(SrcValueTuple(CurrStageDst, 1))
    cu.stages(1).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(1).fwd(0) = SrcValueTuple(PrevStageSrc, 0)

    cu.stages(2).opA = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(2).opB = SrcValueTuple(PrevStageSrc, 1)
    cu.stages(2).opcode = FltMul
    cu.stages(2).res = List(SrcValueTuple(CurrStageDst, 0))
    cu.stages(2).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(2).fwd(1) = SrcValueTuple(PrevStageSrc, 1)

    cu.stages(3).opA = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(3).opB = SrcValueTuple(PrevStageSrc, 1)
    cu.stages(3).opcode = FltMul
    cu.stages(3).res = List(SrcValueTuple(CurrStageDst, 1))
    cu.stages(3).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(3).fwd(0) = SrcValueTuple(PrevStageSrc, 0)

    cu.stages(4).opA = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(4).opB = SrcValueTuple(PrevStageSrc, 1)
    cu.stages(4).opcode = FltMul
    cu.stages(4).res = List(SrcValueTuple(CurrStageDst, 0))
    cu.stages(4).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(4).fwd(0) = SrcValueTuple(PrevStageSrc, 0)

    cu.stages(1).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(2).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(3).enableSelect = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(4).enableSelect = SrcValueTuple(PrevStageSrc, 0)

    cu.stages(3).fwd(0) = SrcValueTuple(PrevStageSrc, 0)
    cu.stages(4).fwd(0) = SrcValueTuple(PrevStageSrc, 0)

    // Control
    cu.control.incrementXbar.outSelect(0) = 0
    cu.control.doneXbar.outSelect(0) = 0
    cu.control.siblingAndTree = List(1, 0, 0, 0, 0)
    cu.control.streamingMuxSelect = 0

    cu.control.tokenOutXbar.outSelect(0) = 4
    cu
  }
}
