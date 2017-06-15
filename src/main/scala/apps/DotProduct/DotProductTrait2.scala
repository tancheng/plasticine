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

trait DotProductTrait2 extends DotProductTrait1  {
  self:DotProductTrait =>
  def config2:Unit = {
    cus(0)(0).asPCUBits.stages(4).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(4).fwd(0) = SVT(ALUSrc, 4)
    cus(0)(0).asPCUBits.stages(5).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(5).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(5).opC = SVT()
    cus(0)(0).asPCUBits.stages(5).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(5).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(5).fwd(0) = SVT(ALUSrc, 5)
    cus(0)(0).asPCUBits.stages(6).opA = SVT(PrevStageSrc, 0)
    cus(0)(0).asPCUBits.stages(6).opB = SVT(CurrStageSrc, 1)
    cus(0)(0).asPCUBits.stages(6).opC = SVT()
    cus(0)(0).asPCUBits.stages(6).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(6).res = List(SVT(CurrStageDst, 1))
    cus(0)(0).asPCUBits.accumInit = 0
    cus(0)(0).asPCUBits.stages(6).fwd(1) = SVT(ALUSrc, 6)
    cus(0)(0).asPCUBits.stages(7).opA = SVT(PrevStageSrc, 1)
    cus(0)(0).asPCUBits.stages(7).opB = SVT()
    cus(0)(0).asPCUBits.stages(7).opC = SVT()
    cus(0)(0).asPCUBits.stages(7).opcode = BypassA
    cus(0)(0).asPCUBits.stages(7).res = List(SVT(CurrStageDst, 5))
    cus(0)(0).asPCUBits.stages(7).fwd(5) = SVT(ALUSrc, 7)
    // Configuring cus(0)(1).asPMUBits <- MemPipe52_x1033_dsp0
    // MemPipe52_x1033_dsp0.udcounters=[]
    // cus(0)(1).asPMUBits.udcs=[None]
    cus(0)(1).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 1)
    cus(0)(1).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(1).asPMUBits.control.readDoneXbar.outSelect(0) = 0
    cus(0)(1).asPMUBits.control.writeDoneXbar.outSelect(0) = 2
    cus(0)(1).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(0)(1).asPMUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(0)(1).asPMUBits.scalarInXbar=[None,None,None,None]
    // cus(0)(1).asPMUBits.scalarOutXbar=[None,None,None,None]
    cus(0)(1).asPMUBits.counterChain.chain = List(0,1,0,0,0,0,0)
    cus(0)(1).asPMUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(1).asPMUBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(1).asPMUBits.counterChain.counters(2) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    // sram1209[0] -> SRAM64
    cus(0)(1).asPMUBits.scratchpad.stride = 1
    cus(0)(1).asPMUBits.scratchpad.numBufs = 2
    cus(0)(1).asPMUBits.wdataSelect = 3
    cus(0)(1).asPMUBits.waddrSelect = SVT(CounterSrc, 1)
    cus(0)(1).asPMUBits.raddrSelect = SVT(CounterSrc, 0)
    // Configuring cus(1)(0).asPMUBits <- MemPipe87_x1034_dsp0
    // MemPipe87_x1034_dsp0.udcounters=[]
    // cus(1)(0).asPMUBits.udcs=[None]
    cus(1)(0).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 1, 0, 0)
    cus(1)(0).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(0).asPMUBits.control.readDoneXbar.outSelect(0) = 0
    cus(1)(0).asPMUBits.control.writeDoneXbar.outSelect(0) = 2
    cus(1)(0).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(1)(0).asPMUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(1)(0).asPMUBits.scalarInXbar=[None,None,None,None]
    // cus(1)(0).asPMUBits.scalarOutXbar=[None,None,None,None]
    cus(1)(0).asPMUBits.counterChain.chain = List(0,1,0,0,0,0,0)
    cus(1)(0).asPMUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(1)(0).asPMUBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 32), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(1)(0).asPMUBits.counterChain.counters(2) = CounterRCBits(max=SVT(ConstSrc, 1), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    // sram1422[0] -> SRAM99
    cus(1)(0).asPMUBits.scratchpad.stride = 1
    cus(1)(0).asPMUBits.scratchpad.numBufs = 2
    cus(1)(0).asPMUBits.wdataSelect = 1
    cus(1)(0).asPMUBits.waddrSelect = SVT(CounterSrc, 1)
    cus(1)(0).asPMUBits.raddrSelect = SVT(CounterSrc, 0)
    // Configuring cus(1)(1).asPCUBits <- PipeCU463_x1092_0
    // cu1618[1,1].ib7607[3] -> TokBuf962.inc(from:PipeCU366_x1087_0.doneOut at PipeCU366_x1087_0),ScalBuf464.swapWrite(from:PipeCU366_x1087_0.doneOut at PipeCU366_x1087_0)
    // cu1618[1,1].ob7571[3] -> PipeCU463_x1092_0.doneOut(to:TokBuf914.inc at MetaPipeCU32_x1094,CredBuf956.inc at PipeCU366_x1087_0)
    // PipeCU463_x1092_0.udcounters=[ScalBuf464 -> TokBuf962]
    // cus(1)(1).asPCUBits.udcs=[Some(TokBuf962),None,None,None,None]
    cus(1)(1).asPCUBits.control.udcInit=List(0,-1,-1,-1,-1)
    cus(1)(1).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0)
    cus(1)(1).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(1).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU463_x1092_0 isPipelining=true isStreaming=false
    cus(1)(1).asPCUBits.control.streamingMuxSelect = 0
    // sm35311[0] -> ScalBuf464 swapWrite=PipeCU366_x1087_0.doneOut
    cus(1)(1).asPCUBits.control.incrementXbar.outSelect(0) = 3
    cus(1)(1).asPCUBits.control.swapWriteXbar.outSelect(0) = 3
    cus(1)(1).asPCUBits.control.tokenOutXbar.outSelect(3) = 4
    cus(1)(1).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.fifoNbufConfig=List(2,-1,-1,-1)
    // cus(1)(1).asPCUBits.scalarInXbar=[Some(iw5455),None,None,None]
    cus(1)(1).asPCUBits.scalarInXbar.outSelect(0) = 3
    // cus(1)(1).asPCUBits.scalarOutXbar=[Some(pr(st34577[7],reg1624[5])),None,None,None]
    cus(1)(1).asPCUBits.scalarOutXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.counterChain.chain = List(0,0,0,0)
  }
}
