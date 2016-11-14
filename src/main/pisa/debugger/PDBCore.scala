package plasticine.pisa.debugger

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._
import plasticine.templates._
import plasticine.ArchConfig
import scala.collection.mutable.Set

class PDBTester(c: Module) extends Tester(c)

trait PDBGlobals {
  private var _pisaFile: String = _
  def pisaFile = _pisaFile
  def pisaFile_=(f: String) { _pisaFile = f }

  private var _pisaConfig: PlasticineConfig = _
  def pisaConfig = _pisaConfig
  def pisaConfig_=(c: PlasticineConfig) { _pisaConfig = c }

  private var _simulator: Option[String] = None
  def simulator = if (_simulator.isDefined) _simulator.get else ""
  def simulator_=(h: String) { _simulator = Some(h) }

  private var _hw: Plasticine = _
  def hw = _hw
  def hw_=(h: Plasticine) { _hw = h }

  private var _tester: PlasticinePDBTester = _
  def tester = _tester
  def tester_=(h: PlasticinePDBTester) { _tester = h }
}

trait PDBBase

case class SimulationConfig(
  alertInterval: Int
  )

class PlasticinePDBTester(module: Plasticine, config: PlasticineConfig) extends PlasticineTester(module, isTrace = false, dumpFile=Some("pdbDump.txt")) with PDBGlobals {

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
      case m: CrossbarReg  =>
        val c = cfg.asInstanceOf[CrossbarConfig]
        m.config.outSelect.zip(c.outSelect) foreach { case (c, i) => poke(c, i) }

      case m: CrossbarVec =>
        val c = cfg.asInstanceOf[CrossbarConfig]
        m.config.outSelect.zip(c.outSelect) foreach { case (c, i) => poke(c, i) }
      case m: CrossbarVecReg  =>
        val c = cfg.asInstanceOf[CrossbarConfig]
        m.config.outSelect.zip(c.outSelect) foreach { case (c, i) => poke(c, i) }

      case m: ConnBox =>
        val c = cfg.asInstanceOf[ConnBoxConfig]
        poke(m.config.sel, c.sel)

      case m: TopUnit =>
        val c = cfg.asInstanceOf[TopUnitConfig]
        setConfig(m.doneConnBox, c.doneConnBox)
//        setConfig(m.dataVldConnBox, c.dataVldConnBox)
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
  val signalsToWatch = Set[UInt]()
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

  def breakOnHigh(signals: List[UInt]) {
    var sigs = signals.map { s => peek(s).toInt }
    var anyHigh = sigs.reduce { (a,b) => a | b }
    while (anyHigh < 1) {
      observeFor(1)
      sigs = signals.map { s => peek(s).toInt }
      anyHigh = sigs.reduce { (a,b) => a | b }
    }
    signals.zip(sigs).foreach { case (signal, v) =>
      println(s"[breakOnHigh] $signal = $v, at cycle $cycleCount")
    }
  }

  def breakOnHigh(signal: UInt) {
    breakOnHigh(List(signal))
  }

  def break(signals: Seq[UInt]) { signals.foreach { s =>
    println(s"[break] Set on signal ${s.name}")
  signalsToWatch += s } }
  def break(signal: UInt) { break(List(signal)) }

  def clear(signals: Seq[UInt]) { signals.foreach { signalsToWatch -= _ }}
  def clear(signal: UInt = null) { if (signal == null) signalsToWatch.clear else clear(Seq(signal)) }

  // Run design by poking the start signal
  def start {
    cycleCount = 0
    val commandReg = module.top.commandRegIdx
    writeReg(commandReg, 1)
  }

  def observeFor(numCycles: Int) = {
    var localCycles = 0

    var anyHigh = 0
    while ((localCycles < numCycles) &(anyHigh < 1)) {
      // Advance time
      step(1)
      localCycles += 1
      cycleCount += 1
      if ((cycleCount % alertInterval) == 0) println(s"[cycleCount $cycleCount]")

      val signals = signalsToWatch.toList
      val watchVals = signals.map { s =>
        val v = peek(s).toInt
        if (v < 0) 0 else v
      }
      anyHigh = if (watchVals.size == 0) 0 else watchVals.reduce { (a,b) => a | b }

      // Peek every signal in signalsToWatch
      if (anyHigh != 0) {
          signals.zip(watchVals).foreach { case (signal, v) =>
          if (v != 0) println(s"[watch] ${signal.name} = $v, at cycle $cycleCount")
        }
      }
    }
    anyHigh
  }

  def c {
    var status = readReg(module.top.statusRegIdx)
    var breakpoint = 0
    while ((status != 1) & (breakpoint != 1)) {
      status = readReg(module.top.statusRegIdx)
      breakpoint = observeFor(1)
    }
    if (status == 1) println(s"Done, design ran for $cycleCount cycles")
  }

  def printScalarRegs {
   val numRegs = module.v + 2
   for (i <- 0 until numRegs) {
     println(s"ScalarReg$i = ${readReg(i)}")
   }
  }

  def watchcu(x: Int, y: Int) {
    val cuModule = module.computeUnits(x)(y)
    val tokenIns = cuModule.io.tokenIns
    val tokenOuts = cuModule.io.tokenOuts
    break(tokenIns ++ tokenOuts)
  }

  def watchcs(x: Int, y: Int) {
    val ctrlSwitch = module.controlSwitch(x)(y)
    val tokenIns = ctrlSwitch.io.ins map { _(0) }
    val tokenOuts = ctrlSwitch.io.outs map { _(0) }
    break(tokenIns ++ tokenOuts)
  }

  var alertInterval = 10000
  def setAlertInterval(i: Int) { alertInterval = i }

  val ctrlNetwork = new CtrlInterconnectHelper {
    val rows = ArchConfig.numRows
    val cols = ArchConfig.numCols
    val mus = List.fill(ArchConfig.numMemoryUnits) { null }
    val cus = null
    val topUnit = null
    val config_enable = null
    val config_data = null
    val dot = null
    val switches = null
  }

  def printLUT(mod: LUT, name: String = "") {
    println(s"--------------------------------")
    println(s"LUT: $name")

        println(s"   |       Input Bits         |             Output       ")
        println(s"___|__________________________|__________________________")
    val table = List.tabulate(mod.size) { i =>
      val idxStr = if (peek(mod.sel).toInt == i) "-->" else "   "
        println(s"$idxStr | " + String.format(s"%${log2Up(mod.size)}s", Integer.toBinaryString(i)).replace(' ', '0') + " | " + peek(mod.config.table(i)))
    }

        println(s"_________________________________________________________")

  }
  def printXbar(mod: Crossbar, name: String = "") {
    println(s"--------------------------------")
    println(s"Xbar: $name")

    val invals = mod.io.ins map { in =>
      val v = if (mod.w == 1) peek(in(0)) else peek(in)
      if (v < 0) "-" else s"$v"
    }
    println(invals.mkString("   "))
    for (i <- 0 until mod.numOutputs) {
      val outSelect = peek(mod.config.outSelect(i)).toInt
      val configStr = List.tabulate(mod.numInputs) { i => if (i == outSelect) "x" else " " }.mkString("   ")
      val output = if (mod.w == 1) peek(mod.io.outs(i)(0)).toInt else peek(mod.io.outs(i)).toInt
      println(configStr + " | " + output)
    }
    println(s"--------------------------------")

  }

  def showSwitch(x: Int, y: Int) {
    def getNeighbor(d: ctrlNetwork.Direction, io: IODirection) = {
      val valid = ctrlNetwork.getValidIO(x, y, io)
      if (valid.contains(d)) {
        d match {
        case ctrlNetwork.W() =>
            if ((x == 0) & ctrlNetwork.isMUSwitch(x,y)) s"MU${x}${y}"
            else s"S${ctrlNetwork.xOffset(d)}${ctrlNetwork.yOffset(d)}"
        case ctrlNetwork.E() =>
            if ((x == ctrlNetwork.cols) & ctrlNetwork.isMUSwitch(x,y)) s"MU${x}${y}"
            else s"S${ctrlNetwork.xOffset(d)}${ctrlNetwork.yOffset(d)}"
        case ctrlNetwork.NW() =>
          s"CU${x-1}${y}"
        case ctrlNetwork.N() =>
          if (y == ctrlNetwork.rows) "Top" else s"S${x}${y+1}"
        case ctrlNetwork.NE() =>
          s"CU${x}${y}"
        case ctrlNetwork.SE() =>
          s"CU${x}${y-1}"
        case ctrlNetwork.S() =>
          if (x == 0) "Top" else s"S${x}${y-1}"
        case ctrlNetwork.SW() =>
          s"CU${x-1}${y-1}"
        }
      } else "Unconnected"
    }
    val switch = module.controlSwitch(x)(y)
    println(s"--------------------------------")
    println(s"Control Switch $x, $y:")
    val indirs = ctrlNetwork.getValidIO(x, y, INPUT).map { d =>
      ctrlNetwork.getIdxs(x, y, d, INPUT).map{ (_, d) }
    }.toList.flatten.sortBy(_._1).map { _._2 }
    val outdirs = ctrlNetwork.getValidIO(x, y, OUTPUT).map { d =>
      ctrlNetwork.getIdxs(x, y, d, OUTPUT).map{ (_, d) }
    }.toList.flatten.sortBy(_._1).map { _._2 }

    val invals = switch.io.ins map { in =>
      val v = peek(in(0))
      if (v < 0) "-" else s"$v"
    }
    val cfg = List.tabulate(switch.numOutputs) { i => peek(switch.config.outSelect(i)).toInt }
    println(s"Config: $cfg, Inst: ${switch.inst.outSelect}")
    println(indirs.mkString("   "))
    println(invals.mkString("   "))
    for (i <- 0 until switch.numOutputs) {
      val outSelect = peek(switch.config.outSelect(i)).toInt
      val configStr = List.tabulate(switch.numInputs) { i => if (i == outSelect) "x" else " " }.mkString("   ")
      val output = peek(switch.io.outs(i)(0)).toInt
      println(configStr + " | " + output + " " + outdirs(i) + " " + getNeighbor(outdirs(i), OUTPUT))
    }
    println(s"--------------------------------")
  }

  def showTop {
    val top = module.top
    println(s"--------------------------------")
    println(s"Top")
    val tokenIns = top.io.ctrlIns.map { i => peek(i).toInt }
    val tokenOut = peek(top.io.startTokenOut).toInt
    println(s"  TokenOut: $tokenOut")
    println(s"  TokenIns: $tokenIns")
    println(s"  Done token in: ${peek(top.doneConnBox.config.sel).toInt}")
    println(s"--------------------------------")
  }
  def showCU(x: Int, y: Int, options: String*) {
    val cuModule = module.computeUnits(x)(y)
    // Print counters, ALU operands, results
    // Format:
    // Compute Unit x, y:
    //   Counters: 0(*) 1(*) 2 3 4 5 6 7, * for enabled
    //   Datapath:
    //       Stage0 (ocpode):
    //              [opA] ......
    //              [opB] ......
    //              [opC] ......
    //              [res] ......

    println(s"--------------------------------")
    println(s"COMPUTE UNIT $x, $y:")
    println(s"-- Counters")
    val counterChain = cuModule.counterChain
    for (i <- 0 until counterChain.numCounters) {
      val ctrEn = peek(counterChain.io.control(i).enable)
      val ctrDone = peek(counterChain.io.control(i).done)
      val ctrVal = peek(counterChain.io.data(i).out)
      val enStr = if (ctrEn == 1) "*" else ""
      val doneStr = if (ctrDone == 1) "*" else ""
      val chainLink = if (i == 0) 0 else peek(counterChain.config.chain(i-1)).toInt
      val chainLinkStr = if (chainLink == 1) "-" else " "
      print(s"  $chainLinkStr $ctrVal [$enStr $doneStr]")
    }
    println("")
    println("-- Control")
    print("tokenIns: "); cuModule.io.tokenIns foreach { t => print(peek(t) + " ") }; println("")
    print("tokenOuts: "); cuModule.io.tokenOuts foreach { t => print(peek(t) + " ") }; println("")
    printXbar(cuModule.controlBlock.incXbar, "incXbar")

    if (options.contains("udc")) {
      println("----UpDownCtrs")
      cuModule.controlBlock.udCounters.zipWithIndex.foreach { case (udc, i) =>
        println(s"----[$i] init: ${peek(udc.io.init)}, inc: ${peek(udc.io.inc)}, dec: ${peek(udc.io.dec)}, gtz: ${peek(udc.io.gtz)}")
      }
    }

    if (options.contains("elut0")) {
      printLUT(cuModule.controlBlock.enableLUTs(0))
    }

    if (options.contains("incXbar")) {
      printXbar(cuModule.controlBlock.incXbar)
    }

    if (options.contains("doneXbar")) {
      printXbar(cuModule.controlBlock.doneXbar)
    }

    if (options.contains("datapath")) {
      println("-- Datapath")
      for (i <- 0 until cuModule.d) {
        println(s"---- Stage $i (${Opcodes.opcodes(peek(cuModule.config.pipeStage(i).opcode).toInt)._1})")
        val stageFUs = cuModule.pipeStages(i)
        val opAs = stageFUs.map { fu => peek(fu.io.a) }
        val opBs = stageFUs.map { fu => peek(fu.io.b) }
        val opCs = stageFUs.map { fu => peek(fu.io.c) }
        val res = stageFUs.map { fu => peek(fu.io.out) }
        println(s"---- opA: $opAs")
        println(s"---- opB: $opBs")
        println(s"---- opC: $opCs")
        println(s"---- res: $res")
      }
      println(s"--------------------------------")
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
    case c: PlasticineConfig => Module(new Plasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, numRows, numCols, numScalarIO, c))
    case _ => throw new Exception(s"Unsupported config type $config")
  }

  def setSim(rows: Int, cols: Int) = {
    simulator = s"Plasticine${rows}${cols}"
    println(s"Simulator set to $simulator.")
    println(s"Path for simulator binary: ${simulator}/generated/PlasticineTest/Simulator")
  }

  def init(file: String) = {
    if (simulator == "") {
      println("Simulator not set. Use the 'setSim(rows, cols)' method to set it before calling init")
    } else {
      ArchConfig.setConfig(s"configs/${simulator}.json")

      pisaFile = file

      // 1. Load and parse the JSON
      println("[PDB INIT] Parsing JSON")
      pisaConfig = Parser(pisaFile).asInstanceOf[PlasticineConfig]

      // 4. Static Chisel args
      val chiselArgs = Array("--targetDir", "/dev/null", "--backend", "null", "--test", "--testCommand", s"$simulator/generated/PlasticineTest/Simulator")
      println("[PDB INIT] Creating a hardware instance")
      val module = Driver(chiselArgs, () => getHardwareInstance(pisaConfig), true)
      // 3. Create a tester instance with the hardware module
      hw = module

      println("[PDB INIT] Creating a tester instance")
      tester = new PlasticinePDBTester(module, pisaConfig)

      println("[PDB INIT] Done")
      }
      val plasticineConfig="Plasticine22"
  }
}

object PDB extends PDBCore {
  def reconfig(file: String) = {
    pisaConfig = Parser(pisaFile).asInstanceOf[PlasticineConfig]
    tester.reset(10)
    tester.setConfig(hw, pisaConfig)
  }
  def writeReg(reg: Int, data: Int) = tester.writeReg(reg, data)
  def readReg(reg: Int) = tester.readReg(reg)
  def start = tester.start
  def n = s(1)
  def s(numCycles: Int = 1) = tester.observeFor(numCycles)
  def scalars = tester.printScalarRegs
  def breakOnHigh(signal: UInt) = tester.breakOnHigh(signal)
  def poke(signal: Bits, data: Int) = tester.poke(signal, data)
  def peek(signal: UInt) = tester.peek(signal)
  def break(signal: UInt) = tester.break(signal)
  def break(signals: Seq[UInt]) = tester.break(signals)
  def clear(signal: UInt = null) = tester.clear(signal)
  def clear(signals: Seq[UInt]) = tester.clear(signals)
  def cu(x: Int, y: Int, options: String*) = tester.showCU(x, y, options:_*)
  def cs(x: Int, y: Int) = tester.showSwitch(x, y)
  def showTop = tester.showTop
  def watchcu(x: Int, y: Int) = tester.watchcu(x, y)
  def watchcs(x: Int, y: Int) = tester.watchcs(x, y)
  def xbar(mod: Crossbar) = tester.printXbar(mod)
  def lut(mod: LUT) = tester.printLUT(mod)
  def cycles = tester.cycleCount
  def c = tester.c
  def alert(x: Int) { tester.setAlertInterval(x) }
}
