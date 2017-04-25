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
    vsbs(0)(1).outSelect(5) = 10
    ssbs(1)(0).outSelect(4) = 18
    csbs(1)(1).outSelect(31) = 13
    vsbs(1)(2).outSelect(8) = 13
    ssbs(1)(2).outSelect(4) = 13
    ssbs(1)(2).outSelect(18) = 5
    csbs(1)(2).outSelect(24) = 13
    csbs(2)(1).outSelect(3) = 12
    csbs(2)(1).outSelect(9) = 11
    csbs(2)(1).outSelect(16) = 8
    csbs(2)(1).outSelect(17) = 4
    ssbs(2)(2).outSelect(17) = 5
    csbs(2)(2).outSelect(4) = 17
    csbs(2)(2).outSelect(21) = 4
    // Configuring cus(0)(0).asPCUBits <- PipeCU61_x353
    // PipeCU61_x353.udcounters=[SeqCU7_x358 -> TokBuf238]
    // cus(0)(0).asPCUBits.udcs=[Some(TokBuf238),None,None,None,None]
    cus(0)(0).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(0)(0).asPCUBits.control.siblingAndTree = List(1, 0, 0, 0, 0)
    // PipeCU61_x353 isPipelining=true isStreaming=false
    cus(0)(0).asPCUBits.control.streamingMuxSelect = 0
    // sm6088[0] -> ScalBuf62 swapWrite=NotConnected
    cus(0)(0).asPCUBits.control.incrementXbar.outSelect(0) = 3
    cus(0)(0).asPCUBits.control.doneXbar.outSelect(0) = 0
    cus(0)(0).asPCUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(0)(0).asPCUBits.scalarInXbar=[Some(iw3144[2]),None,None,None]
    cus(0)(0).asPCUBits.scalarInXbar.outSelect(0) = 2
    // cus(0)(0).asPCUBits.scalarOutXbar=[None,None,None,None]
    cus(0)(0).asPCUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(0).asPCUBits.counterChain.counters(0) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
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
    // Configuring cus(0)(1).asPMUBits <- MemPipe22_x347_dsp0
    // MemPipe22_x347_dsp0.udcounters=[]
    // cus(0)(1).asPMUBits.udcs=[]
    cus(0)(1).asPMUBits.control.writeFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 1)
    cus(0)(1).asPMUBits.control.readFifoAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    // sm7660[0] -> ScalBuf23 swapWrite=NotConnected
    cus(0)(1).asPMUBits.control.readDoneXbar.outSelect(0) = 11
    cus(0)(1).asPMUBits.control.writeDoneXbar.outSelect(0) = 1
    cus(0)(1).asPMUBits.control.scalarSwapReadSelect = List(-1,-1,-1,-1)
    cus(0)(1).asPMUBits.fifoNbufConfig=List(1,-1,-1,-1)
    // cus(0)(1).asPMUBits.scalarInXbar=[Some(iw3200[1]),None,None,None]
    cus(0)(1).asPMUBits.scalarInXbar.outSelect(0) = 1
    // cus(0)(1).asPMUBits.scalarOutXbar=[None,None,None,None]
    cus(0)(1).asPMUBits.counterChain.chain = List(0,0,0,0,0,0,0)
    cus(0)(1).asPMUBits.counterChain.counters(1) = CounterRCBits(max=SVT(ConstSrc, 64), stride=SVT(ConstSrc, 1), min=SVT(ConstSrc, 0), par=1)
    cus(0)(1).asPMUBits.stages(0).opA = SVT(ScalarFIFOSrc, 0)
    cus(0)(1).asPMUBits.stages(0).opB = SVT()
    cus(0)(1).asPMUBits.stages(0).opC = SVT()
    cus(0)(1).asPMUBits.stages(0).opcode = BypassA
    cus(0)(1).asPMUBits.stages(0).res = List(SVT(ReadAddrDst, -1))
    cus(0)(1).asPMUBits.stages(0).enableSelect.src = ReadEnSrc
    // sram183[0] -> SRAM40
    cus(0)(1).asPMUBits.scratchpad.stride = -1
    cus(0)(1).asPMUBits.scratchpad.numBufs = 1
    cus(0)(1).asPMUBits.wdataSelect = 3
    cus(0)(1).asPMUBits.waddrSelect = SVT(CounterSrc, 1)
    cus(0)(1).asPMUBits.raddrSelect = SVT(ALUSrc, 0)
    // Configuring cus(1)(1).asPCUBits <- PipeCU96_x357
    // PipeCU96_x357.udcounters=[]
    // cus(1)(1).asPCUBits.udcs=[None,None,None,None,None]
    cus(1)(1).asPCUBits.control.tokenInAndTree = List(0, 0, 0, 0, 0, 0, 0, 0)
    cus(1)(1).asPCUBits.control.fifoAndTree = List(0, 0, 0, 0, 1, 0, 0, 0)
    cus(1)(1).asPCUBits.control.siblingAndTree = List(0, 0, 0, 0, 0)
    // PipeCU96_x357 isPipelining=true isStreaming=false
    cus(1)(1).asPCUBits.control.streamingMuxSelect = 0
    // sm6950[0] -> ScalBuf97 swapWrite=NotConnected
    cus(1)(1).asPCUBits.control.tokenOutXbar.outSelect(0) = 4
    cus(1)(1).asPCUBits.control.tokenOutXbar.outSelect(2) = 4
  }
}
