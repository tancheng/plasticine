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

trait SimpleSequentialTrait1 {
  self:SimpleSequentialTrait =>
  def config1:Unit = {
    vsbs(0)(1).outSelect(4) = 9
    csbs(0)(1).outSelect(19) = 3
    csbs(0)(2).outSelect(0) = 7
    csbs(0)(2).outSelect(11) = 0
    csbs(0)(2).outSelect(12) = 4
    csbs(0)(2).outSelect(18) = 8
    ssbs(1)(0).outSelect(4) = 18
    vsbs(1)(2).outSelect(8) = 13
    ssbs(1)(2).outSelect(4) = 13
    ssbs(1)(2).outSelect(18) = 5
    csbs(1)(2).outSelect(3) = 13
    // Configuring cus(0)(0).asPCUBits <- PipeCU62_x353
    // PipeCU62_x353.udcounters=[SeqCU7_x358 -> TokBuf320]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf320),None,None,None,None]
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU62_x353 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm5602[0] -> ScalBuf64 swapWrite=NotConnected
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 1
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3234[2]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 2
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,None,None]
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(0).asPCUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opB = SVT(CounterSrc, 0)
    cus(0)(0).asPCUBits.stages(0).opC = SVT()
    cus(0)(0).asPCUBits.stages(0).opcode = FixMul
    cus(0)(0).asPCUBits.stages(0).res = List(SVT(CurrStageDst, 12))
    cus(0)(0).asPCUBits.stages(0).fwd(12) = SVT(ALUSrc, 0)
    cus(0)(0).asPCUBits.stages(1).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(2).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(3).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(4).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(5).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(6).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(7).fwd(12) = SVT(PrevStageSrc, 12)
    cus(0)(0).asPCUBits.stages(8).fwd(12) = SVT(PrevStageSrc, 12)
    // Configuring cus(0)(1).asPMUBits <- MemPipe23_x347_dsp0
    // MemPipe23_x347_dsp0.udcounters=[]
    // cus(0)(1).asPMUBits.udcs=[]
    cus(0)(1).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 1)
    cus(0)(1).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    // sm7174[0] -> ScalBuf25 swapWrite=NotConnected
    cus(0)(1).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(0)(1).asPMUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(0)(1).asPMUBits.scalarInXbar=[Some(iw3290[1]),None,None,None]
    cus(0)(1).asPMUBits.scalarInXbar.outSelect(0) = 1
    // cus(0)(1).asPMUBits.scalarOutXbar=[None,None,None,None]
    cus(0)(1).asPMUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(1).asPMUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=16)
    cus(0)(1).asPMUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(1).asPMUBits.stages(0).opB = SVT()
    cus(0)(1).asPMUBits.stages(0).opC = SVT()
    cus(0)(1).asPMUBits.stages(0).opcode = Bypass
    cus(0)(1).asPMUBits.stages(0).res = List(SVT(ReadAddrDst, -1))
    cus(0)(1).asPMUBits.stages(0).enableSelect = ReadEn
    // sram183[0] -> SRAM42
    cus(0)(1).asPMUBits.scratchpad.stride = 1
    cus(0)(1).asPMUBits.scratchpad.numBufs = 1
    cus(0)(1).asPMUBits.scratchpad.wdataSelect = 3
    cus(0)(1).asPMUBits.scratchpad.waddrSelect = SVT(CounterSrc, 0)
    cus(0)(1).asPMUBits.scratchpad.raddrSelect = SVT(ALUSrc, 0)
    // Configuring cus(1)(1).asPCUBits <- PipeCU96_x357
    // PipeCU96_x357.udcounters=[]
    // cus(1)(1).asPCUBits.udcs=[None,None,None,None,None]
    cus(1)(1).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(1).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 1, 0, 0, 0)
    cus(1)(1).asPCUBits.control.siblingAndTree = List(0, 0, 0, 0, 0)
    // PipeCU96_x357 isPipelining=true isStreaming=false
    cus(1)(1).asPCUBits.control.streamingMuxSelect = 0
    cus(1)(1).asPCUBits.control.tokenOutXbar.outSelect(0) = 4
    cus(1)(1).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.fifoNbufConfig=List(-1,-1,-1,-1)
    // cus(1)(1).asPCUBits.scalarInXbar=[None,None,None,None]
    // cus(1)(1).asPCUBits.scalarOutXbar=[Some(pr(st6410[8],reg513[8])),None,None,None]
    cus(1)(1).asPCUBits.scalarOutXbar.outSelect(0) = 0
    cus(1)(1).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(1)(1).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 1), par=16)
  }
}
