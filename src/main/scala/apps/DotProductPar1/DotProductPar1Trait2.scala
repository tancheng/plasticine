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

trait DotProductPar1Trait2 extends DotProductPar1Trait1 {
  self:DotProductPar1Trait =>
  def config2:Unit = {
    cus(0)(0).asPCUBits.stages(3).opC = SVT()
    cus(0)(0).asPCUBits.stages(3).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(3).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(3).fwd(0) = SVT(ALUSrc, 3)
    cus(0)(0).asPCUBits.stages(4).opA = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(4).opB = SVT(ReduceTreeSrc, 0)
    cus(0)(0).asPCUBits.stages(4).opC = SVT()
    cus(0)(0).asPCUBits.stages(4).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(4).res = List(SVT(CurrStageDst, 0))
    cus(0)(0).asPCUBits.stages(4).fwd(0) = SVT(ALUSrc, 4)
    cus(0)(0).asPCUBits.stages(5).opA = SVT(PrevStageSrc, 0)
    cus(0)(0).asPCUBits.stages(5).opB = SVT(CurrStageSrc, 1)
    cus(0)(0).asPCUBits.stages(5).opC = SVT()
    cus(0)(0).asPCUBits.stages(5).opcode = FixAdd
    cus(0)(0).asPCUBits.stages(5).res = List(SVT(CurrStageDst, 1))
    cus(0)(0).asPCUBits.accumInit = 0
    cus(0)(0).asPCUBits.stages(5).fwd(1) = SVT(ALUSrc, 5)
    cus(0)(0).asPCUBits.stages(6).opA = SVT(PrevStageSrc, 1)
    cus(0)(0).asPCUBits.stages(6).opB = SVT()
    cus(0)(0).asPCUBits.stages(6).opC = SVT()
    cus(0)(0).asPCUBits.stages(6).opcode = BypassA
    cus(0)(0).asPCUBits.stages(6).res = List(SVT(CurrStageDst, 8))
    cus(0)(0).asPCUBits.stages(6).fwd(8) = SVT(ALUSrc, 6)
    // Configuring cus(0)(1).asPMUBits <- MemPipe38_x1033_dsp0
    // MemPipe38_x1033_dsp0.udcounters=[]
    // cus(0)(1).asPMUBits.udcs=[]
    cus(0)(1).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 1, 0, 0, 0)
    cus(0)(1).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(1).asPMUBits.control.readDoneXbar.outSelect(0) = 0
    cus(0)(1).asPMUBits.control.writeDoneXbar.outSelect(0) = 23
    cus(0)(1).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(0)(1).asPMUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(0)(1).asPMUBits.scalarInXbar=[None,None,None,None]
    // cus(0)(1).asPMUBits.scalarOutXbar=[None,None,None,None,None,None,None,None]
    cus(0)(1).asPMUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(1).asPMUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(1).asPMUBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    // sram113[0] -> SRAM50
    cus(0)(1).asPMUBits.scratchpad.stride = 1
    cus(0)(1).asPMUBits.scratchpad.numBufs = 2
    cus(0)(1).asPMUBits.wdataSelect = 0
    cus(0)(1).asPMUBits.waddrSelect = SVT(CounterSrc, 1)
    cus(0)(1).asPMUBits.raddrSelect = SVT(CounterSrc, 0)
    // Configuring cus(1)(0).asPMUBits <- MemPipe73_x1034_dsp0
    // MemPipe73_x1034_dsp0.udcounters=[]
    // cus(1)(0).asPMUBits.udcs=[]
    cus(1)(0).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 0, 1, 0)
    cus(1)(0).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(0).asPMUBits.control.readDoneXbar.outSelect(0) = 0
    cus(1)(0).asPMUBits.control.writeDoneXbar.outSelect(0) = 15
    cus(1)(0).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(1)(0).asPMUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(1)(0).asPMUBits.scalarInXbar=[None,None,None,None]
    // cus(1)(0).asPMUBits.scalarOutXbar=[None,None,None,None,None,None,None,None]
    cus(1)(0).asPMUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(1)(0).asPMUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(1)(0).asPMUBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    // sram233[0] -> SRAM85
    cus(1)(0).asPMUBits.scratchpad.stride = 1
    cus(1)(0).asPMUBits.scratchpad.numBufs = 2
    cus(1)(0).asPMUBits.wdataSelect = 2
    cus(1)(0).asPMUBits.waddrSelect = SVT(CounterSrc, 1)
    cus(1)(0).asPMUBits.raddrSelect = SVT(CounterSrc, 0)
    // Configuring cus(1)(1).asPCUBits <- PipeCU296_x1087_0
    // PipeCU296_x1087_0.udcounters=[MetaPipeCU18_x1094 -> TokBuf821]
    // cus(1)(1).asPCUBits.udcs=[Some(TokBuf821),None,None,None,None]
    cus(1)(1).asPCUBits.control.udcInit=List(1,-1,-1,-1,-1)
    cus(1)(1).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(1).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 1, 0, 0, 1)
    cus(1)(1).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU296_x1087_0 isPipelining=true isStreaming=false
    cus(1)(1).asPCUBits.control.streamingMuxSelect = 0
    cus(1)(1).asPCUBits.control.incrementXbar.outSelect(0) = 15
    // ob6401[7] -> CtrlBox631.done.out
    cus(1)(1).asPCUBits.control.tokenOutXbar.outSelect(7) = 6
    cus(1)(1).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.fifoNbufConfig=List(-1,-1,-1,-1,-1,-1)
    // cus(1)(1).asPCUBits.scalarInXbar=[None,None,None,None,None,None]
    // cus(1)(1).asPCUBits.scalarOutXbar=[None,None,None,None,None,None,None,Some(pr(st9337[6],reg345[8]))]
    cus(1)(1).asPCUBits.scalarOutXbar.outSelect(7) = 0
    cus(1)(1).asPCUBits.counterChain.chain = List(0,0,0,0)
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 3200), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(1)(1).asPCUBits.stages(0).opA = SVT(VectorFIFOSrc, 0)
  }
}
