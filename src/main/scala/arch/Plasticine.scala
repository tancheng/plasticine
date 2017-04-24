package plasticine.arch

import chisel3._
import chisel3.util._

import plasticine.ArchConfig
import plasticine.templates._
import plasticine.pisa.codegen.ConfigInitializer
import plasticine.pisa.ir._
import plasticine.spade._
import plasticine.config._
import plasticine.misc.Utils._
import fringe._

import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import scala.collection.mutable.ListBuffer
import java.io.PrintWriter
import scala.language.reflectiveCalls

case class PlasticineIO(f: FringeParams) extends Bundle {
  // Scalar IO
  val argIns = Vec(f.numArgIns, Flipped(Decoupled(UInt(f.dataWidth.W))))
  val argOuts = Vec(f.numArgOuts, Decoupled(UInt(f.dataWidth.W)))

  // Memory IO
  val memStreams = Flipped(new AppStreams(f.loadStreamInfo, f.storeStreamInfo))

  // Control IO
  val enable = Input(Bool())
  val done = Output(Bool())

  // Config IO: Shift register
  val config = Flipped(Decoupled(Bool()))
  val configDone = Output(Bool())
}

class Plasticine(val p: PlasticineParams, val f: FringeParams, val initBits: Option[PlasticineBits] = None) extends Module with PlasticineArch {
  val io = IO(PlasticineIO(f))

  val cuParams = p.cuParams
  val vectorParams = p.vectorSwitchParams
  val scalarParams = p.scalarSwitchParams
  val controlParams = p.controlSwitchParams
  val switchCUParams = p.switchCUParams
  val scalarCUParams = p.scalarCUParams

  // Wire up the reconfiguration network: ASIC or CGRA?
  val configType = PlasticineConfig(cuParams, vectorParams, scalarParams, controlParams, switchCUParams, scalarCUParams, p, f)
  val config = Wire(configType)

  if (initBits.isDefined) {
    // ASIC flow
    println(s"initBits defined, ASIC flow")
    val configWire = Wire(configType)
    val configInitializer = new ConfigInitializer()
    configInitializer.init(initBits.get, configWire)
    config := configWire
  } else {
    // CGRA flow
    println(s"initBits undefined, CGRA flow")
    val configSR = Module(new ShiftRegister(configType))
    configSR.io.in.bits := io.config.bits
    configSR.io.in.valid := io.config.valid
    io.configDone := configSR.io.out.valid
    config := configSR.io.config
  }

  val argOutMuxIns = List.tabulate(f.numArgOuts) { i =>
    Wire(Vec(p.numArgOutSelections(i), Flipped(Decoupled(UInt(f.dataWidth.W)))))
  }

  val argOutMuxInData = Array.tabulate(f.numArgOuts) { i =>
    Array.tabulate(p.numArgOutSelections(i)) { j =>
      argOutMuxIns(i)(j)
    }
  }

  val argOutMuxes = List.tabulate(f.numArgOuts) { i =>
    val mux = Module(new MuxN(UInt(f.dataWidth.W), p.numArgOutSelections(i)))
    io.argOuts(i).bits := mux.io.out
//    io.argOuts(i).valid := mux.io.out.valid
    mux.io.ins := Vec(argOutMuxIns(i).map { _.bits })
    mux.io.sel := config.argOutMuxSelect(i)
    mux
  }

  val doneOuts = Wire(Vec(p.numDoneConnections, Bool()))
  io.done := getMux(doneOuts.getElements.toList, config.doneSelect)

  // PCUs, PMUs
  val cus = Array.tabulate(p.numCols) { i =>
      Array.tabulate(p.numRows) { j =>
        cuParams(i)(j) match {
          case p: PCUParams =>
            val cu = Module(new PCU(p))
            cu.io.config := config.cu(i)(j).asInstanceOf[PCUConfig]
            cu
          case p: PMUParams =>
            val cu = Module(new PMU(p))
            cu.io.config := config.cu(i)(j).asInstanceOf[PMUConfig]
            cu
        }
      }
    }

  // Vector switches
  val vsbs = Array.tabulate(vectorParams.size) { i =>
    Array.tabulate(vectorParams(i).size) { j =>
      val switch = Module(new VectorSwitch(vectorParams(i)(j)))
      switch.io.config := config.vectorSwitch(i)(j)
      switch
    }
  }

  // Scalar network
  val ssbs = Array.tabulate(scalarParams.size) { i =>
    Array.tabulate(scalarParams(i).size) { j =>
      val switch = Module(new ScalarSwitch(scalarParams(i)(j)))
      switch.io.config := config.scalarSwitch(i)(j)
      switch
    }
  }

  // Control network
  val csbs = Array.tabulate(controlParams.size) { i =>
    Array.tabulate(controlParams(i).size) { j =>
      val switch = Module(new ControlSwitch(controlParams(i)(j)))
      switch.io.config := config.controlSwitch(i)(j)
      switch
    }
  }

   //Switch CUs
  val switchCUs = Array.tabulate(switchCUParams.size) { i =>
    Array.tabulate(switchCUParams(i).size) { j =>
      val cu = Module(new SwitchCU(switchCUParams(i)(j)))
      cu.io.config := config.switchCU(i)(j)
      cu
    }
  }

   //Switch CUs
  val scalarCUs = Array.tabulate(scalarCUParams.size) { i =>
    Array.tabulate(scalarCUParams(i).size) { j =>
      val cu = Module(new ScalarCU(scalarCUParams(i)(j)))
      cu.io.config := config.scalarCU(i)(j)
      cu
    }
  }


  connect(io, argOutMuxInData, Array.tabulate(doneOuts.size) { doneOuts(_) }, cus, vsbs, ssbs, csbs, switchCUs)
}

//trait DirectionOps {
//  trait Direction
//
//  trait West extends Direction
//  trait North extends Direction
//  trait East extends Direction
//  trait South extends Direction
//
//  def yOffset(d: Direction) = d match {
//    case _:North => 1
//    case _: South => -1
//    case _ => 0
//  }
//  def xOffset(d: Direction) = d match {
//    case _:East => 1
//    case _:West => -1
//    case _ => 0
//  }
//
//  case class W() extends West {
//    override def toString = "W "
//  }
//
//  case class NW() extends North with West {
//    override def toString = "NW"
//  }
//  case class N() extends North {
//    override def toString = "N "
//  }
//  case class NE() extends North with East {
//    override def toString = "NE"
//  }
//  case class E() extends East {
//    override def toString = "E "
//  }
//  case class SE() extends South with East {
//    override def toString = "SE"
//  }
//  case class S() extends South {
//    override def toString = "S "
//  }
//  case class SW() extends South with West {
//    override def toString = "SW"
//  }
//
//  def reverseDir(d: Direction) = d match {
//    case N() => S()
//    case S() => N()
//    case E() => W()
//    case W() => E()
//    case NW() => SE()
//    case NE() => SW()
//    case SW() => NE()
//    case SE() => NW()
//  }
//}

/**
 * InterconnectHelper trait. Contains methods that help connect together
 * the switches, CUs, memory units, and top unit. The interconnection is
 * currently hardcoded to one particular topology. Other topologies
 * need changes in the method implementation
 */
//trait InterconnectHelper extends DirectionOps {
//  // Absolute direction order
//  final val dirOrder: List[Direction] = List(W(), NW(), N(), NE(), E(), SE(), S(), SW())
//
//  /**
//   * Values below to be overridden during instantiation
//   */
//  // Interconnect components
//  val cus: ListBuffer[ListBuffer[ComputeUnit]]
//  val switches: ListBuffer[ListBuffer[CrossbarVecReg]]
//  val mus: List[MemoryUnit]
//  val topUnit: TopUnit
//
//  // CU organization
//  val rows: Int
//  val cols: Int
//
//  // Dot file printer for the graph
//  val dot: PrintWriter
//
//  // Config interface
//  val config_enable: Bool
//  val config_data: Bool
//
//  // Returns if switch at (x,y) is connected to a MU
//  def isMUSwitch(x: Int, y: Int) = {
//    val mXs = List(0)  // Currently only first column
//    val mYs = List.tabulate(mus.size) { i => i }  // Rows 0 .. mus.size-1
//    if (mXs.contains(x) && mYs.contains(y)) true else false
//  }
//
//  // Define valid IOs for switches along each edge of the 2D array
//  val baseIODirs = Set(N(), S())
//  def topIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(W(), E(), SW())
//      case OUTPUT => Set(W(), E(), SE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  def botIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(W(), E(), NW())
//      case OUTPUT => Set(W(), E(), NE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  def rightIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(NW(), W(), SW())
//      case OUTPUT => Set(W())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  def leftIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(E())
//      case OUTPUT => Set(E(), NE(), SE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  // A regular switch has the union of all of the special cases above
//  def regularIO(iodir: IODirection): Set[Direction] = topIO(iodir) ++ botIO(iodir) ++ leftIO(iodir) ++ rightIO(iodir)
//
//  // Returns the set of valid IO directions for a switch
//  // at position (x, y) in cartesian co-ordinate system
//  def getValidIO(x: Int, y: Int, iodir: IODirection): Set[Direction] = {
//    var validIO = regularIO(iodir)
//
//    validIO = x match {
//      case 0 => validIO.intersect(leftIO(iodir))
//      case `cols` => validIO.intersect(rightIO(iodir))
//      case _ => validIO
//    }
//    validIO = y match {
//      case 0 => validIO.intersect(botIO(iodir))
//      case `cols` => validIO.intersect(topIO(iodir))
//      case _ => validIO
//    }
//
//    if (isMUSwitch(x, y)) {
//      validIO = x match {
//        case 0 => // Leftmost column
//          iodir match {
//            case INPUT => validIO ++ Set(W())
//            case OUTPUT => validIO ++ Set(W())
//          }
//        case `cols` => // Rightmost column
//          iodir match {
//            case INPUT => validIO ++ Set(E())
//            case OUTPUT => validIO ++ Set(E())
//          }
//        case _ => throw new Exception(s"Invalid MU switch position ($x, $y): MU can only be attached to col 0 or col $cols")
//      }
//    }
//    validIO
//  }
//
//  // Number of links in each direction
//  val defaultNumLinks = 1
//  def getNumLinks(x: Int, y: Int, d: Direction, iodir: IODirection) = {
//    d match {
//        case W() => iodir match {
//          case INPUT => if (isMUSwitch(x, y) & (x == 0)) 1 else defaultNumLinks
//          case OUTPUT => if (isMUSwitch(x, y) & (x == 0)) 4 else defaultNumLinks
//        }
//        case E() => iodir match {
//          case INPUT => if (isMUSwitch(x, y) & (x == cols)) 1 else defaultNumLinks
//          case OUTPUT => if (isMUSwitch(x, y) & (x == cols)) 4 else defaultNumLinks
//        }
//        case _ => defaultNumLinks
//    }
//  }
//
//  // Direction -> index conversion
//  def getIdxBase(x: Int, y: Int, d: Direction, validDirs: Set[Direction], iodir: IODirection) = {
//    Predef.assert(validDirs.contains(d), s"[getIdx, switch $x $y] Direction '$d' not in validDirs set ($validDirs))")
//    val validDirOrder = dirOrder.filter(validDirs.contains(_))
//    val previousDirs = validDirOrder.splitAt(validDirOrder.indexOf(d))._1
//    previousDirs.map { getNumLinks(x, y, _, iodir) }.sum
//  }
//
//  def getIdxs(x: Int, y: Int, d: Direction, iodir: IODirection) = {
//    val validDirs = getValidIO(x, y, iodir)
//    val baseIdx = getIdxBase(x, y, d, validDirs, iodir)
//    List.tabulate(getNumLinks(x, y, d, iodir)) { i => baseIdx + i }
//  }
//
//  def getDirAssignment(x: Int, y: Int, iodir: IODirection) = {
//    println(s"[switch $x $y $iodir] Switch directions and indices:")
//    val validDirs = getValidIO(x, y, iodir)
//    println(s"\tvalidDirs: $validDirs")
//    validDirs.foreach { d =>
//      val baseIdx = getIdxBase(x, y, d, validDirs, iodir)
//      val idxs = getIdxs(x, y, d, iodir)
//      println(s"\t $d: baseIdx = $baseIdx, idxs = $idxs")
//    }
//  }
//
//  // CU Input order: West: 0, NW: 1, S: 2, SW: 3
//  def getCUInputIdx(d: Direction) = d match {
//    case W() => 0
//    case NW() => 1
//    case S() => 2
//    case SW() => 3
//    case _ => throw new Exception(s"Unsupported input direction $d for CU")
//  }
//
//  def genSwitchArray(wordWidth: Int, v: Int, inst: List[CrossbarConfig]) = {
//    ListBuffer.tabulate(cols+1) { x =>
//      ListBuffer.tabulate(rows+1) { y =>
//        val validInDirs = getValidIO(x, y, INPUT)
//        val validOutDirs = getValidIO(x, y, OUTPUT)
//        val numIns = validInDirs.toList.map { getNumLinks(x, y, _, INPUT) }.sum
//        val numOuts = validOutDirs.toList.map { getNumLinks(x, y, _, OUTPUT) }.sum
//        println(s"[switch $x $y] $numIns (${validInDirs}) x $numOuts (${validOutDirs}) ")
//        val c = Module(new CrossbarVecReg(wordWidth, v, numIns, numOuts, inst(x*(cols+1)+y)))
//        c.io.config_enable := config_enable
//        c.io.config_data := config_data
//        c
//      }
//    }
//  }
//
//  def connectSwitchArray {
//    for (x <- 0 to cols) {
//      for (y <- 0 to rows) {
//        dot.println(s"""s${x}${y} [ shape=square style=filled fillcolor=red fontcolor=white color=white fillcolor=red pos= "${x*2},${y*2}!"]""")
//        val validInputDirs = getValidIO(x, y, INPUT).intersect(Set(W(), N(), E(), S()))
////        getDirAssignment(x, y, INPUT)
//        if (y == rows) { List(N(), NE(), NW()) foreach { validInputDirs -= _ } }
//        if (y == 0) { List(S(), SE(), SW()) foreach { validInputDirs -= _ } }
//        if (x == 0) { List(W(), NW(), SW()) foreach { validInputDirs -= _ } }
//        if (x == cols) { List(E(), SE(), NE()) foreach { validInputDirs -= _ } }
//        validInputDirs.foreach { case d =>
//          val inIdxs = getIdxs(x, y, d, INPUT) // Indices of input ports
//          val r = yOffset(d)
//          val c = xOffset(d)
//          val outIdxs = getIdxs(x+c, y+r, reverseDir(d), OUTPUT)  // Indices of output that connects to idxs
//          println(s"[switch $x $y $d] : inIdxs: $inIdxs, outSwitch: [${x+c} ${y+r}], outSwitchDir: ${reverseDir(d)}, outIdxs: $outIdxs")
//          inIdxs.zip(outIdxs) foreach { case (inIdx, outIdx) =>
//            switches(x)(y).io.ins(inIdx) := switches(x+c)(y+r).io.outs(outIdx)
//            dot.println(s"s${(x+c)}${(y+r)} -> s${x}${y}")
//          }
//        }
//      }
//    }
//  }
//
//  def connectTopUnit = {
//    for (y <- List(0, rows)) {
//      val d = if (y == 0) S() else N()
//      for (x <- 0 to cols) {
//        val switchOut = getIdxs(x, y, d, OUTPUT)(0)
//        val switchIn = getIdxs(x, y, d, INPUT)(0)
//        switches(x)(y).io.ins(switchIn) := topUnit.io.out
//        val topInIdx = if (y > 0) cols + 1 + x else x
//        topUnit.io.ins(topInIdx) := switches(x)(y).io.outs(switchOut)
//        dot.println(s"top -> s${x}${y}")
//        dot.println(s"s${x}${y} -> top")
//      }
//    }
//  }
//
//  def connectCUArray {
//    for (x <- 0 until cols) {
//      for (y <- 0 until rows) {
//        dot.println(s"""${x}${y} [pos="${x*2+1},${y*2+1}!"]""")
//        val validInputs = Set(W(), S())
//
//        // Connect CUs together (inputs 0 (W) and 2(S))
//        if (y == 0) { validInputs -= S() }
//        if (x == 0) { validInputs -= W() }
//        validInputs.foreach { case in =>
//          val inIdx = getCUInputIdx(in) // Indices of input ports
//          val r = yOffset(in)
//          val c = xOffset(in)
//          println(s"[cu $x $y] input dir: $in, inIdx = $inIdx, input CU = cu ${x+c}${y+r}")
//          cus(x)(y).io.dataIn(inIdx) := cus(x+c)(y+r).io.dataOut
//            dot.println(s"${(x+c)}${(y+r)} -> ${x}${y}")
//        }
//      }
//    }
//  }
//
//  def connectCUAndSwitch = {
//    for (i <- 0 until cols) {
//      for (j <- 0 until rows) {
//        // Connect NW (1) and SW(3) inputs for CUs
//        val validDirs = List(NW(), SW())
//        validDirs.foreach { dir =>
//          val (switchx, switchy) = dir match {
//            case NW() => (i, j+1)
//            case SW() => (i, j)
//            case _ => throw new Exception(s"Unsupported CU input dir $dir to connect switches")
//          }
//
//          // Currently we support only a single link in each of these directions
//          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), INPUT) == 1, s"Num links in $dir direction must be 1")
//          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), OUTPUT) == 1, s"Num links in $dir direction must be 1")
//
//          val switchOutIdx = getIdxs(switchx, switchy, reverseDir(dir), OUTPUT)(0)
//          val cuIdx = getCUInputIdx(dir)
//          cus(i)(j).io.dataIn(cuIdx) := switches(switchx)(switchy).io.outs(switchOutIdx)
//          dot.println(s"s${(switchx)}${(switchy)} -> ${i}${j}")
//        }
//
//        // Connect CU output to SW (i, j+1) and NW (i+1, j+1)
//        val swIdxSwitch = getIdxs(i+1, j+1, SW(), INPUT)(0)
//        val nwIdxSwitch = getIdxs(i+1, j, NW(), INPUT)(0)
//        switches(i+1)(j+1).io.ins(swIdxSwitch) := cus(i)(j).io.dataOut
//        switches(i+1)(j).io.ins(nwIdxSwitch) := cus(i)(j).io.dataOut
//        dot.println(s"${(i)}${(j)} -> s${i+1}${j+1}")
//        dot.println(s"${(i)}${(j)} -> s${i+1}${j}")
//      }
//    }
//  }
//
//  // Connect first 'numMemoryUnits' switches in first column to memory units
//   def connectMUs = {
//     for (i <- 0 until mus.size) {
//      dot.println(s"""m${i} [shape=rectangle style=filled fillcolor=gray color=white pos="-1,${i*2}!"]""")
//      val switchx = 0
//      val switchy = i
//      val westOutputs = getIdxs(switchx, switchy, W(), OUTPUT).map { switches(switchx)(switchy).io.outs(_) }
//      val westInputs = getIdxs(switchx, switchy, W(), INPUT).map { switches(switchx)(switchy).io.ins(_) }
//      val addr = westOutputs(0)
//      val size = addr(1) // Size is the second word in addr bus
//      val wdata = westOutputs(1)
//      val rdata = westInputs(0)
//
//      rdata := mus(i).rdata
//      mus(i).wdata := wdata
//      mus(i).addr :=  addr
//      mus(i).size :=  size
//      mus(i).io.interconnect.dataIns(2) := westOutputs(2)
//      mus(i).io.interconnect.dataIns(3) := westOutputs(3)
//
//      dot.println(s"m${i} -> s${0}${i}")
//      dot.println(s"s${0}${i} -> m${i}")
//      dot.println(s"s${0}${i} -> m${i}")
//      dot.println(s"s${0}${i} -> m${i}")
//      dot.println(s"s${0}${i} -> m${i}")
//    }
//  }
//
//  def performConnections = {
//    connectSwitchArray
//    connectTopUnit
//    connectMUs
//    connectCUArray
//    connectCUAndSwitch
//  }
//
//  def connectAll = {
//    dot.println("digraph G {")
//    dot.println(s"""top [pos="${rows},-1!"]""")
//    performConnections
//    dot.println("}")
//    dot.close
//  }
//}

//trait CtrlInterconnectHelper extends InterconnectHelper {
//
//  // Define valid IOs for switches along each edge of the 2D array
//  override def topIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(W(), E(), SW(), SE())
//      case OUTPUT => Set(W(), E(), SW(), SE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  override def botIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(W(), E(), NW(), NE())
//      case OUTPUT => Set(W(), E(), NW(), NE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  override def rightIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(W(), NW(), SW())
//      case OUTPUT => Set(W(), NW(), SW())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  override def leftIO(iodir: IODirection): Set[Direction] = {
//    (iodir match {
//      case INPUT => Set(E(), NE(), SE())
//      case OUTPUT => Set(E(), NE(), SE())
//      case _ => throw new Exception(s"Unknown direction $iodir")
//    }) ++ baseIODirs
//  }
//
//  // Returns the set of valid IO directions for a switch
//  // at position (x, y) in cartesian co-ordinate system
//  override def getValidIO(x: Int, y: Int, iodir: IODirection): Set[Direction] = {
//    var validIO = regularIO(iodir)
//
//    validIO = x match {
//      case 0 => validIO.intersect(leftIO(iodir))
//      case `cols` => validIO.intersect(rightIO(iodir))
//      case _ => validIO
//    }
//    validIO = y match {
//      case 0 => validIO.intersect(botIO(iodir))
//      case `cols` => validIO.intersect(topIO(iodir))
//      case _ => validIO
//    }
//
//    if (isMUSwitch(x, y)) {
//      validIO = x match {
//        case 0 => // Leftmost column
//          iodir match {
//            case INPUT => validIO ++ Set(W())
//            case OUTPUT => validIO ++ Set(W())
//          }
//        case `cols` => // Rightmost column
//          iodir match {
//            case INPUT => validIO ++ Set(E())
//            case OUTPUT => validIO ++ Set(E())
//          }
//        case _ => throw new Exception(s"Invalid MU switch position ($x, $y): MU can only be attached to col 0 or col $cols")
//      }
//    }
//    validIO
//  }
//
//  override def getNumLinks(x: Int, y: Int, d: Direction, iodir: IODirection) = {
//    val interSwitchLinks = 8
//    d match {
//        case N() => interSwitchLinks
//        case S() => interSwitchLinks
//        case W() => if (isMUSwitch(x, y)) { iodir match {
//            case OUTPUT => 8
//            case INPUT => 9
//            case _ => throw new Exception(s"Unknown direction $iodir")
//          }
//        } else interSwitchLinks
//        case E() => if (isMUSwitch(x, y)) { iodir match {
//            case OUTPUT => 8
//            case INPUT => 9
//            case _ => throw new Exception(s"Unknown direction $iodir")
//          }
//        } else interSwitchLinks
//        case _ => defaultNumLinks
//    }
//  }
//
//  override def connectSwitchArray {
//    for (x <- 0 to cols) {
//      for (y <- 0 to rows) {
//        dot.println(s"""s${x}${y} [ shape=square style=filled fillcolor=red fontcolor=white color=white fillcolor=red pos= "${x*2},${y*2}!"]""")
//        val validInputDirs = getValidIO(x, y, INPUT).intersect(Set(W(), N(), E(), S()))
//        getDirAssignment(x, y, INPUT)
//        if (x == 0) { List(W(), NW(), SW()) foreach { validInputDirs -= _ } }
//        if (x == cols) { List(E(), SE(), NE()) foreach { validInputDirs -= _ } }
//        if (y == 0) { List(S(), SE(), SW()) foreach { validInputDirs -= _ } }
//        if (y == rows) { List(N(), NE(), NW()) foreach { validInputDirs -= _ } }
//        validInputDirs.foreach { case d =>
//          val inIdxs = getIdxs(x, y, d, INPUT) // Indices of input ports
//          val r = yOffset(d)
//          val c = xOffset(d)
//          val outIdxs = getIdxs(x+c, y+r, reverseDir(d), OUTPUT)  // Indices of output that connects to idxs
//          println(s"[switch $x $y $d] : inIdxs: $inIdxs, outSwitch: [${x+c} ${y+r}], outSwitchDir: ${reverseDir(d)}, outIdxs: $outIdxs")
//          inIdxs.zip(outIdxs) foreach { case (inIdx, outIdx) =>
//            switches(x)(y).io.ins(inIdx)(0) := switches(x+c)(y+r).io.outs(outIdx)(0)
//            dot.println(s"s${(x+c)}${(y+r)} -> s${x}${y}")
//          }
//        }
//      }
//    }
//  }
//
//  override def connectTopUnit = {
//    for (y <- List(0, rows)) {
//      val d = if (y == 0) S() else N()
//      for (x <- 0 to cols) {
//        val switchOuts = getIdxs(x, y, d, OUTPUT)
//        val switchIn = getIdxs(x, y, d, INPUT)(0)
//        switches(x)(y).io.ins(switchIn)(0) := topUnit.io.startTokenOut
//        val topInIdx = if (y > 0) (cols+1 + x) * 8 else x*8  // Row 0 gets 0..cols-1, last row gets cols..
//
//        switchOuts.zipWithIndex.foreach { case (outIdx, i) =>
//          topUnit.io.ctrlIns(topInIdx+i) := switches(x)(y).io.outs(outIdx)(0)
//          dot.println(s"s${x}${y} -> top")
//        }
//        dot.println(s"top -> s${x}${y}")
//      }
//    }
//  }
//
//  // Connect first 'numMemoryUnits' switches in first column to memory units
//  override def connectMUs = {
//     for (i <- 0 until mus.size) {
//      dot.println(s"""m${i} [shape=rectangle style=filled fillcolor=gray color=white pos="-1,${i*2}!"]""")
//      val switchx = 0
//      val switchy = i
//      val westOutputs = getIdxs(switchx, switchy, W(), OUTPUT).map { switches(switchx)(switchy).io.outs(_)(0) }
//      val westInputs = getIdxs(switchx, switchy, W(), INPUT).map { switches(switchx)(switchy).io.ins(_)(0) }
////      val rdyIn = westOutputs(0)
//      val vldIn = westOutputs(0)
//      val dataVldIn = westOutputs(1)
//      val tokenIns = westOutputs.drop(2)
//
//      val rdyOut = westInputs(0)
//      val dataRdyOut = westInputs(1)
//      val vldOut = westInputs(2)
//      val tokenOuts = westInputs.drop(3)
//
////      mus(i).rdyIn := rdyIn
//      mus(i).vldIn := vldIn
//      mus(i).dataVldIn := dataVldIn
//      mus(i).tokenIns.zip(tokenIns) foreach { case (in, i) => in := i }
//      dot.println(s"s${switchx}${switchy} -> m${i}")
//      dot.println(s"s${switchx}${switchy} -> m${i}")
//      for (link <- 0 until mus(i).tokenIns.size) { dot.println(s"s${switchx}${switchy} -> m${i}") }
//
//      rdyOut := mus(i).rdyOut
//      dataRdyOut := mus(i).dataRdyOut
//      vldOut := mus(i).vldOut
//      tokenOuts.zip(mus(i).tokenOuts)foreach { case (out, o) => out := o }
//      dot.println(s"m${i} -> s${switchx}${switchy}")
//      dot.println(s"m${i} -> s${switchx}${switchy}")
//      dot.println(s"m${i} -> s${switchx}${switchy}")
//      for (link <- 0 until mus(i).tokenOuts.size) { dot.println(s"m${i} -> s${switchx}${switchy}") }
//    }
//  }
//
//  // CU Input order: All 8 directions valid for control
//  override def getCUInputIdx(d: Direction) = dirOrder.indexOf(d)
//
//  override def connectCUArray {
//    for (x <- 0 until cols) {
//      for (y <- 0 until rows) {
//        dot.println(s"""${x}${y} [pos="${x*2+1},${y*2+1}!"]""")
//        val validInputs = Set(W(), N(), E(), S())
//
//        // Connect CUs together (inputs 0 (W) and 2(S))
//        if (y == 0) { validInputs -= S() }
//        if (y == rows-1) { validInputs -= N() }
//        if (x == 0) { validInputs -= W() }
//        if (x == cols-1) { validInputs -= E() }
//        validInputs.foreach { case in =>
//          val inIdx = getCUInputIdx(in) // Indices of input ports
//          val outIdx = getCUInputIdx(reverseDir(in))
//          val r = yOffset(in)
//          val c = xOffset(in)
//          println(s"[ctrl][connectCUArray][cu $x $y] $in ($inIdx) = cu [${x+c}${y+r}] ${reverseDir(in)} $outIdx")
//
//          val pipelineReg = Module(new FF(1))
//          pipelineReg.io.control.enable := Bool(true)
//          pipelineReg.io.data.init := UInt(0)
//          pipelineReg.io.data.in := cus(x+c)(y+r).io.tokenOuts(outIdx)
//          cus(x)(y).io.tokenIns(inIdx) := pipelineReg.io.data.out
//            dot.println(s"${(x+c)}${(y+r)} -> ${x}${y}")
//        }
//      }
//    }
//  }
//
//  override def connectCUAndSwitch = {
//    for (i <- 0 until cols) {
//      for (j <- 0 until rows) {
//        // Connect NW (1), NE(3), SE(5), SW(7) inputs for CUs
//        val validDirs = List(NW(), NE(), SE(), SW())
//        validDirs.foreach { dir =>
//          val (switchx, switchy) = dir match {
//            case NW() => (i, j+1)
//            case NE() => (i+1, j+1)
//            case SE() => (i+1, j)
//            case SW() => (i, j)
//            case _ => throw new Exception(s"Unsupported CU input direction $dir to connect switches")
//          }
//
//          // Currently we support only a single link in each of these directions
//          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), INPUT) == 1, s"Num links in $dir direction must be 1")
//          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), OUTPUT) == 1, s"Num links in $dir direction must be 1")
//
//          val switchOutIdx = getIdxs(switchx, switchy, reverseDir(dir), OUTPUT)(0)
//          val switchInIdx = getIdxs(switchx, switchy, reverseDir(dir), INPUT)(0)
//          val cuIdx = getCUInputIdx(dir)  // Same idx for both input and output
//          cus(i)(j).io.tokenIns(cuIdx) := switches(switchx)(switchy).io.outs(switchOutIdx)(0)
//          switches(switchx)(switchy).io.ins(switchInIdx)(0) := cus(i)(j).io.tokenOuts(cuIdx)
//          dot.println(s"s${(switchx)}${(switchy)} -> ${i}${j}")
//          dot.println(s" ${i}${j} -> s${(switchx)}${(switchy)}")
//        }
//      }
//    }
//  }
//
//  override def performConnections = {
//    connectSwitchArray
//    connectTopUnit
//    connectMUs
//    connectCUArray
//    connectCUAndSwitch
//  }
//}


//class AbstractPlasticine(val w: Int,
//  val startDelayWidth: Int,
//  val endDelayWidth: Int,
//  val d: Int,
//  val v: Int, rwStages: Int,
//  val numTokens: Int,
//  val l: Int,
//  val r: Int,
//  val m: Int,
//  val numScratchpads: Int,
//  val numStagesAfterReduction: Int,
//  val numMemoryUnits: Int,
//  inst: PlasticineConfig)
//extends Module {
//
//  val io = new ConfigInterface {
//    /* Configuration interface */
//    val config_enable = Bool(INPUT)
//
//    /* Register interface */
//    val addr = UInt(INPUT, width=w)
//    val wdata = UInt(INPUT, width=w)
//    val wen = Bool(INPUT)
//    val dataVld = Bool(INPUT)
//    val rdata = UInt(OUTPUT, width=w)
//
//    /* Memory interface */
//    val dramChannel = Vec.fill(numMemoryUnits) { new DRAMCmdInterface(w, v) }
//  }
//}
//
///**
// * Plasticine Top
// * @param w: Word width
// * @param startDelayWidth: Start delay width
// * @param endDelayWidth: End delay width
// * @param d: Pipeline depth
// * @param v: Vector length
// * @param rwStages: Read-write stages (at the beginning)
// * @param numTokens: Number of input (and output) tokens
// * @param l: Number of local pipeline registers
// * @param r: Number of remote pipeline registers
// * @param m: Scratchpad size in words
// */
//class Plasticine(override val w: Int,
//  override val startDelayWidth: Int,
//  override val endDelayWidth: Int,
//  override val d: Int,
//  override val v: Int, rwStages: Int,
//  override val numTokens: Int,
//  override val l: Int,
//  override val r: Int,
//  override val m: Int,
//  override val numScratchpads: Int,
//  override val numStagesAfterReduction: Int,
//  override val numMemoryUnits: Int,
//  val numRows: Int,
//  val numCols: Int,
//  val numScalarIO: Int,
//  val numScalarRegisters: Int,
//  inst: PlasticineConfig) extends AbstractPlasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst)
//with DirectionOps {
//
////  val numRows = 2
////  val numCols = 2
//  val burstSizeBytes = 64
//
//  val numOutstandingBursts = 16
//  def genMemoryUnits = {
//    val numMUCounters = 6
//    List.tabulate(numMemoryUnits) { i =>
//      val mu = Module(new MemoryUnit(w, m, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numMUCounters, inst.mu(i)))
//      mu.io.config_enable := io.config_enable
//      mu.io.config_data := io.config_data
//
//      io.dramChannel(i).addr := mu.io.dram.addr
//      io.dramChannel(i).wdata := mu.io.dram.wdata
//      io.dramChannel(i).tagOut := mu.io.dram.tagOut
//
//      io.dramChannel(i).vldOut := mu.io.dram.vldOut
//      io.dramChannel(i).rdyOut := mu.io.dram.rdyOut
//      io.dramChannel(i).isWr  := mu.io.dram.isWr
//
//      mu.io.dram.rdata := io.dramChannel(i).rdata
//      mu.io.dram.vldIn := io.dramChannel(i).vldIn
//      mu.io.dram.rdyIn := io.dramChannel(i).rdyIn
//      mu.io.dram.tagIn := io.dramChannel(i).tagIn
//
//      mu
//    }
//  }
//
//  def genDataArray(inst: List[ComputeUnitConfig]) = {
//    ListBuffer.tabulate(numCols) { i =>
//      ListBuffer.tabulate(numRows) { j =>
//        val cu = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numScalarIO, numScalarRegisters, inst(i*numCols+j)))
//        cu.io.config_enable := io.config_enable
//        cu.io.config_data := io.config_data
//        cu
//      }
//    }
//  }
//
//  val memoryUnits = genMemoryUnits
//
//  val top = Module(new TopUnit(w, v, (numCols+1) * 2 * 8, inst.top))
//  top.io.config_enable := io.config_enable
//  top.io.config_data := io.config_data
//  top.io.addr := io.addr
//  top.io.wdata := io.wdata
//  top.io.wen:= io.wen
//  io.rdata := top.io.rdata
//
//  val computeUnits = genDataArray(inst.cu)
//
//  println("-- Generating Data Interconnect --")
//  val dataInterconnect = new InterconnectHelper {
//    val rows = numRows
//    val cols = numCols
//    val mus = memoryUnits
//    val cus = computeUnits
//    val topUnit = top
//    val config_enable = io.config_enable
//    val config_data = io.config_data
//    val dot = new PrintWriter("data.dot")
//    val switches = genSwitchArray(w, v, inst.dataSwitch)
//  }
//  dataInterconnect.connectAll
//  val dataSwitch = dataInterconnect.switches
//
//  println("-- Generating Control Interconnect --")
//  val ctrlInterconnect = new CtrlInterconnectHelper {
//    val rows = numRows
//    val cols = numCols
//    val cus = computeUnits
//    val mus = memoryUnits
//    val topUnit = top
//    val config_enable = io.config_enable
//    val config_data = io.config_data
//    val dot = new PrintWriter("control.dot")
//    val switches = genSwitchArray(1, 1, inst.controlSwitch)
//  }
//  ctrlInterconnect.connectAll
//  val controlSwitch = ctrlInterconnect.switches
//}

/**
 * ComputeUnit test harness
 */
//class PlasticineTests(c: AbstractPlasticine) extends PlasticineTester(c) {
//  var numCycles = 0
//
//  val a = Array.tabulate(c.m) { i => i }
//  val b = Array.tabulate(c.m) { i => i*2 }
//  val res = Array.tabulate(c.m) { i => a(i) * b(i) }
////  setMem(c.cu0.scratchpads(0), a)
////  setMem(c.cu0.scratchpads(1), b)
////  poke(c.io.command, 1)
//  while (numCycles < 100) {
//    step(1)
//    numCycles += 1
//  }
//
////  expectMem(c.cu1.scratchpads(0), res)
//  println(s"Done, design ran for $numCycles cycles")
//}
//
//
//object PlasticineTest {
//
//  def main(args: Array[String]): Unit = {
//
//    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
//
//    if (appArgs.size != 2) {
//      println("Usage: bin/sadl PlasticineTest <spade config> <pisa config>")
//      sys.exit(-1)
//    }
//
//    val spadeFile = appArgs(0)
//    val pisaFile = appArgs(1)
//
//    ArchConfig.setConfig(spadeFile)
//    val config =  PlasticineConfig.zeroes(
//        ArchConfig.d,
//        ArchConfig.numRows,
//        ArchConfig.numCols,
//        ArchConfig.numTokens,
//        ArchConfig.numTokens,
//        ArchConfig.numTokens,
//        ArchConfig.numScratchpads,
//        ArchConfig.numMemoryUnits
////        ArchConfig.numScalarIO
////        ArchConfig.numScalarRegisters
//      )
////    val config = Parser(pisaFile).asInstanceOf[PlasticineConfig]
////    val config = PlasticineConfig.getRandom(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads, numMemoryUnits)
//
//    val bitwidth = ArchConfig.w
//    val startDelayWidth = ArchConfig.startDelayWidth
//    val endDelayWidth = ArchConfig.endDelayWidth
//    val d = ArchConfig.d
//    val v = ArchConfig.v
//    val l = ArchConfig.l
//    val r = ArchConfig.r
//    val rwStages = ArchConfig.rwStages
//    val numTokens = ArchConfig.numTokens
//    val m = ArchConfig.m
//    val numScratchpads = ArchConfig.numScratchpads
//    val numStagesAfterReduction = ArchConfig.numStagesAfterReduction
//    val numRows = ArchConfig.numRows
//    val numCols = ArchConfig.numCols
//    val numMemoryUnits = ArchConfig.numMemoryUnits
//    val numScalarIO = ArchConfig.numScalarIO
//    val numScalarRegisters = ArchConfig.numScalarRegisters
//
//      chiselMainTest(chiselArgs, () => Module(new Plasticine(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, numRows, numCols, numScalarIO, numScalarRegisters, config)).asInstanceOf[AbstractPlasticine]) {
//        c => new PlasticineTests(c)
//      }
//
//  }
//}
