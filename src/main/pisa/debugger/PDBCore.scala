package plasticine.pisa.debugger

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import plasticine.templates._
import plasticine.ArchConfig

class PDBTester(c: Module) extends Tester(c)

trait PDBGlobals {
  private var _pisaFile: String = _
  def pisaFile = _pisaFile
  def pisaFile_=(f: String) { _pisaFile = f }

  private var _pisaConfig: PlasticineConfig = _
  def pisaConfig = _pisaConfig
  def pisaConfig_=(c: PlasticineConfig) { _pisaConfig = c }

  private var _hw: Plasticine = _
  def hw = _hw
  def hw_=(h: Plasticine) { _hw = h }

  private var _tester: PlasticinePDBTester = _
  def tester = _tester
  def tester_=(h: PlasticinePDBTester) { _tester = h }

}

trait PDBBase

class PlasticinePDBTester(module: Plasticine, config: PlasticineConfig) extends PlasticineTester(module, isTrace = false, dumpFile=Some("pdbDump.txt")) {

  def roundUpDivide(num: Int, divisor: Int) = (num + divisor - 1) / divisor

  // Set the hardware configuration
  def setConfig(mod: Module, cfg: AbstractConfig) {
    mod match {
      case m: CounterRC =>
        val c = cfg.asInstanceOf[CounterRCConfig]
        poke(m.config.max, c.max)
        poke(m.config.stride, c.stride)
        poke(m.config.maxConst, c.maxConst)
        poke(m.config.strideConst, c.strideConst)
        poke(m.config.startDelay, c.startDelay)
        poke(m.config.endDelay, c.endDelay)
      case m: CounterChain =>
        val c = cfg.asInstanceOf[CounterChainConfig]
        m.config.chain.zip(c.chain) foreach { case (c, i) => poke(c, i) }
        m.counters.zip(c.counters) foreach { case (c, i) => setConfig(c, i) }
      case m: AbstractMemoryUnit =>
        val c = cfg.asInstanceOf[MemoryUnitConfig]
      case m: Scratchpad =>
        val c = cfg.asInstanceOf[ScratchpadConfig]
        poke(m.config.mode, c.banking.mode)
        poke(m.config.strideLog2, c.banking.strideLog2)
        val bufSize = if (c.numBufs == 0) 1 else math.max(1, m.d / (m.v*c.numBufs))
        poke(m.config.bufSize, bufSize)
      case m: ComputeUnit =>
        val c = cfg.asInstanceOf[ComputeUnitConfig]
        setConfig(m.counterChain, c.counterChain)
        setConfig(m.scalarXbar, c.scalarXbar)
        m.config.scratchpads.zip(c.scratchpads) foreach { case (c, i) =>
          poke(c.raSrc, i.ra.src)
          poke(c.raValue, i.ra.value)
          poke(c.waSrc, i.wa.src)
          poke(c.waValue, i.wa.value)
          poke(c.wdSrc, i.wd)
          poke(c.wen, i.wen)
        }
        m.scratchpads.zip(c.scratchpads) foreach { case (c, i) =>
          setConfig(c, i)
        }
        m.config.pipeStage.zip(c.pipeStage) foreach { case (c, i) =>
          poke(c.opA.dataSrc, i.opA.dataSrc)
          poke(c.opB.dataSrc, i.opB.dataSrc)
          poke(c.opC.dataSrc, i.opC.dataSrc)
          poke(c.opA.value, i.opA.value)
          poke(c.opB.value, i.opB.value)
          poke(c.opC.value, i.opC.value)
          poke(c.opcode, i.opcode)
          poke(c.result, i.result)
          c.fwd.zipWithIndex.foreach { case (fwdBit, idx) =>
            poke(fwdBit, i.fwd.getOrElse(idx, 0) > 0)
          }
        }
        setConfig(m.controlBlock, c.control)
      case m: CUControlBox =>
        val c = cfg.asInstanceOf[CUControlBoxConfig]
        m.config.udcInit.zip(c.udcInit) foreach { case (c, i) => poke(c, i) }
        m.config.enableMux.zip(c.enableMux) foreach { case (c, i) => poke(c, i) }
        m.config.syncTokenMux.zip(c.syncTokenMux) foreach { case (c, i) => poke(c, i) }
        m.tokenOutLUTs.zip(c.tokenOutLUT) foreach { case (c, i) => setConfig(c, i) }
        m.enableLUTs.zip(c.enableLUT) foreach { case (c, i) => setConfig(c, i) }
        m.tokenDownLUTs.zip(c.tokenDownLUT) foreach { case (c, i) => setConfig(c, i) }
        setConfig(m.decXbar, c.decXbar)
        setConfig(m.incXbar, c.incXbar)
        setConfig(m.tokenInXbar, c.tokenInXbar)
        setConfig(m.doneXbar, c.doneXbar)
        setConfig(m.tokenOutXbar, c.tokenOutXbar)
      case m: LUT =>
        val c = cfg.asInstanceOf[LUTConfig]
        m.config.table.zip(c.table) foreach { case (c, i) => poke(c, i) }
      case m: Crossbar =>
        val c = cfg.asInstanceOf[CrossbarConfig]
        m.config.outSelect.zip(c.outSelect) foreach { case (c, i) => poke(c, i) }
      case m: ConnBox =>
        val c = cfg.asInstanceOf[ConnBoxConfig]
        poke(m.config.sel, c.sel)

      case m: TopUnit =>
        val c = cfg.asInstanceOf[TopUnitConfig]
        setConfig(m.doneConnBox, c.doneConnBox)
        setConfig(m.dataVldConnBox, c.dataVldConnBox)
        setConfig(m.argOutConnBox, c.argOutConnBox)
      case m: Plasticine =>
        val c = cfg.asInstanceOf[PlasticineConfig]

        // Compute units
        c.cu.zipWithIndex.foreach { case (c, idx) =>
          val cuModule = m.computeUnits(idx/m.numCols)(idx%m.numCols)
          setConfig(cuModule, c)
        }

        // Data switches
        c.dataSwitch.zipWithIndex.foreach { case (dconfig, idx) =>
          val switch = m.dataSwitch(idx/(m.numCols+1))(idx%(m.numCols+1))
          setConfig(switch, dconfig)
        }

        // Control switches
        c.controlSwitch.zipWithIndex.foreach { case (cconfig, idx) =>
          val switch = m.controlSwitch(idx/(m.numCols+1))(idx%(m.numCols+1))
          setConfig(switch, cconfig)
        }

        // Memory units
        m.memoryUnits.zip(c.mu) foreach { case (mu, i) => setConfig(mu, i) }

        // Top unit
        setConfig(m.top, c.top)
    }
    step(1)
  }

  var cycleCount = 0
  def writeReg(reg: Int, data: Int) = {
    poke(module.io.addr, reg)
    poke(module.io.wdata, data)
    poke(module.io.wen, 1)
    step(1)
    poke(module.io.wen, 0)
  }

  def readReg(reg: Int) = {
    poke(module.io.addr, reg)
    peek(module.io.rdata).toInt
  }

  // Run design by poking the start signal
  def start {
    cycleCount = 0
    val commandReg = module.top.commandRegIdx
    writeReg(commandReg, 1)
  }

  def observeFor(numCycles: Int) {
    cycleCount += 1
    step(1)
  }

  def runToFinish {
    var status = readReg(module.top.statusRegIdx)
    while (status != 1) {
      status = readReg(module.top.statusRegIdx)
      observeFor(1)
    }
    finish
  }

  def printScalarRegs {
   val numRegs = module.v + 2
   for (i <- 0 until numRegs) {
     println(s"ScalarReg$i = ${readReg(i)}")
   }
  }
}

trait PDBCore extends PDBBase with PDBGlobals {
  import ArchConfig._

  def help {
    println("Plasticine Debugger Commands:")
    println(s"init (<PISA file>)  : Load PISA file")
  }

  def getHardwareInstance(config: AbstractConfig) = config match {
//    case c: CounterRCConfig => Module(new CounterRC(w, startDelayWidth, endDelayWidth, c))
//    case c: CounterChainConfig => Module(new CounterChain(w, startDelayWidth, endDelayWidth, numCounters, c))
//    case c: ScratchpadConfig => Module(new Scratchpad(w, d, v, c))
//    case c: MemoryUnitConfig => Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, c))
//    case c: ComputeUnitConfig => Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, c))
//    case c: CUControlBoxConfig => Module(new CUControlBox(numTokens, c))
//    case c: TopUnitConfig => Module(new TopUnit(w, v, numTopInputs, c))
    case c: PlasticineConfig => Module(new Plasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, numRows, numCols, c))
    case _ => throw new Exception(s"Unsupported config type $config")
  }

  def init(file: String) = {
    pisaFile = file

    // 1. Load and parse the JSON
    pisaConfig = Parser(pisaFile).asInstanceOf[PlasticineConfig]

    // 4. Static Chisel args
    val chiselArgs = Array("--targetDir", "/dev/null", "--backend", "null", "--test", "--testCommand", "generated/PlasticineTest/Simulator")
    val module = Driver(chiselArgs, () => getHardwareInstance(pisaConfig), true)
    // 3. Create a tester instance with the hardware module
    tester = new PlasticinePDBTester(module, pisaConfig)
  }
}

object PDB extends PDBCore {
  def writeReg(reg: Int, data: Int) = tester.writeReg(reg, data)
  def readReg(reg: Int) = tester.readReg(reg)
  def start = tester.start
  def observeFor(numCycles: Int) = tester.observeFor(numCycles)
  def runToFinish = tester.runToFinish
  def printScalarRegs = tester.printScalarRegs
}
