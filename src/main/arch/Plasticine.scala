package plasticine.templates

import Chisel._
import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import scala.collection.mutable.ListBuffer

import java.io.PrintWriter

trait DirectionOps {
  trait Direction

  trait West extends Direction
  trait North extends Direction
  trait East extends Direction
  trait South extends Direction

  def yOffset(d: Direction) = d match {
    case _:North => 1
    case _: South => -1
    case _ => 0
  }
  def xOffset(d: Direction) = d match {
    case _:East => 1
    case _:West => -1
    case _ => 0
  }

  case class W() extends West
  case class NW() extends North with West
  case class N() extends North
  case class NE() extends North with East
  case class E() extends East
  case class SE() extends South with East
  case class S() extends South
  case class SW() extends South with West

  def reverseDir(d: Direction) = d match {
    case N() => S()
    case S() => N()
    case E() => W()
    case W() => E()
    case NW() => SE()
    case NE() => SW()
    case SW() => NE()
    case SE() => NW()
  }
}

/**
 * InterconnectHelper trait. Contains methods that help connect together
 * the switches, CUs, memory units, and top unit. The interconnection is
 * currently hardcoded to one particular topology. Other topologies
 * need changes in the method implementation
 */
trait InterconnectHelper extends DirectionOps {
  // Absolute direction order
  final val dirOrder: List[Direction] = List(W(), NW(), N(), NE(), E(), SE(), S(), SW())

  /**
   * Values below to be overridden during instantiation
   */
  // Interconnect components
  val cus: ListBuffer[ListBuffer[ComputeUnit]]
  val switches: ListBuffer[ListBuffer[CrossbarVecReg]]
  val mus: List[MemoryUnit]
  val topUnit: TopUnit

  // CU organization
  val rows: Int
  val cols: Int

  // Dot file printer for the graph
  val dot: PrintWriter

  // Config interface
  val config_enable: Bool
  val config_data: Bool

  // Returns if switch at (x,y) is connected to a MU
  def isMUSwitch(x: Int, y: Int) = {
    val mXs = List(0)  // Currently only first column
    val mYs = List.tabulate(mus.size) { i => i }  // Rows 0 .. mus.size-1
    if (mXs.contains(x) && mYs.contains(y)) true else false
  }

  // Define valid IOs for switches along each edge of the 2D array
  val baseIODirs = Set(N(), S())
  def topIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(W(), E(), SW())
      case OUTPUT => Set(W(), E(), SE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  def botIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(W(), E(), NW())
      case OUTPUT => Set(W(), E(), NE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  def rightIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(NW(), W(), SW())
      case OUTPUT => Set(W())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  def leftIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(E())
      case OUTPUT => Set(E(), NE(), SE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  // A regular switch has the union of all of the special cases above
  def regularIO(iodir: IODirection): Set[Direction] = topIO(iodir) ++ botIO(iodir) ++ leftIO(iodir) ++ rightIO(iodir)

  // Returns the set of valid IO directions for a switch
  // at position (x, y) in cartesian co-ordinate system
  def getValidIO(x: Int, y: Int, iodir: IODirection): Set[Direction] = {
    var validIO = regularIO(iodir)

    validIO = x match {
      case 0 => validIO.intersect(leftIO(iodir))
      case `cols` => validIO.intersect(rightIO(iodir))
      case _ => validIO
    }
    validIO = y match {
      case 0 => validIO.intersect(botIO(iodir))
      case `cols` => validIO.intersect(topIO(iodir))
      case _ => validIO
    }

    if (isMUSwitch(x, y)) {
      validIO = x match {
        case 0 => // Leftmost column
          iodir match {
            case INPUT => validIO ++ Set(W())
            case OUTPUT => validIO ++ Set(W())
          }
        case `cols` => // Rightmost column
          iodir match {
            case INPUT => validIO ++ Set(E())
            case OUTPUT => validIO ++ Set(E())
          }
        case _ => throw new Exception(s"Invalid MU switch position ($x, $y): MU can only be attached to col 0 or col $cols")
      }
    }
    validIO
  }

  // Number of links in each direction
  val defaultNumLinks = 1
  def getNumLinks(x: Int, y: Int, d: Direction, iodir: IODirection) = {
    d match {
        case W() => iodir match {
          case INPUT => if (isMUSwitch(x, y) & (x == 0)) 1 else defaultNumLinks
          case OUTPUT => if (isMUSwitch(x, y) & (x == 0)) 3 else defaultNumLinks
        }
        case E() => iodir match {
          case INPUT => if (isMUSwitch(x, y) & (x == cols)) 1 else defaultNumLinks
          case OUTPUT => if (isMUSwitch(x, y) & (x == cols)) 3 else defaultNumLinks
        }
        case _ => defaultNumLinks
    }
  }

  // Direction -> index conversion
  def getIdxBase(x: Int, y: Int, d: Direction, validDirs: Set[Direction], iodir: IODirection) = {
    Predef.assert(validDirs.contains(d), s"[getIdx, switch $x $y] Direction '$d' not in validDirs set ($validDirs))")
    val validDirOrder = dirOrder.filter(validDirs.contains(_))
    val previousDirs = validDirOrder.splitAt(validDirOrder.indexOf(d))._1
    previousDirs.map { getNumLinks(x, y, _, iodir) }.sum
  }

  def getIdxs(x: Int, y: Int, d: Direction, iodir: IODirection) = {
    val validDirs = getValidIO(x, y, iodir)
    val baseIdx = getIdxBase(x, y, d, validDirs, iodir)
    List.tabulate(getNumLinks(x, y, d, iodir)) { i => baseIdx + i }
  }

  def getDirAssignment(x: Int, y: Int, iodir: IODirection) = {
    println(s"[switch $x $y $iodir] Switch directions and indices:")
    val validDirs = getValidIO(x, y, iodir)
    println(s"\tvalidDirs: $validDirs")
    validDirs.foreach { d =>
      val baseIdx = getIdxBase(x, y, d, validDirs, iodir)
      val idxs = getIdxs(x, y, d, iodir)
      println(s"\t $d: baseIdx = $baseIdx, idxs = $idxs")
    }
  }

  // CU Input order: West: 0, NW: 1, S: 2, SW: 3
  def getCUInputIdx(d: Direction) = d match {
    case W() => 0
    case NW() => 1
    case S() => 2
    case SW() => 3
    case _ => throw new Exception(s"Unsupported input direction $d for CU")
  }

  def genSwitchArray(wordWidth: Int, v: Int, inst: List[CrossbarConfig]) = {
    ListBuffer.tabulate(cols+1) { x =>
      ListBuffer.tabulate(rows+1) { y =>
        val validInDirs = getValidIO(x, y, INPUT)
        val validOutDirs = getValidIO(x, y, OUTPUT)
        val numIns = validInDirs.toList.map { getNumLinks(x, y, _, INPUT) }.sum
        val numOuts = validOutDirs.toList.map { getNumLinks(x, y, _, OUTPUT) }.sum
        println(s"[switch $x $y] $numIns (${validInDirs}) x $numOuts (${validOutDirs}) ")
        val c = Module(new CrossbarVecReg(wordWidth, v, numIns, numOuts, inst(x*(cols+1)+y)))
        c.io.config_enable := config_enable
        c.io.config_data := config_data
        c
      }
    }
  }

  def connectSwitchArray {
    for (x <- 0 to cols) {
      for (y <- 0 to rows) {
        dot.println(s"""s${x}${y} [ shape=square style=filled fillcolor=red fontcolor=white color=white fillcolor=red pos= "${x*2},${y*2}!"]""")
        val validInputDirs = getValidIO(x, y, INPUT).intersect(Set(W(), N(), E(), S()))
//        getDirAssignment(x, y, INPUT)
        if (y == rows) { List(N(), NE(), NW()) foreach { validInputDirs -= _ } }
        if (y == 0) { List(S(), SE(), SW()) foreach { validInputDirs -= _ } }
        if (x == 0) { List(W(), NW(), SW()) foreach { validInputDirs -= _ } }
        if (x == cols) { List(E(), SE(), NE()) foreach { validInputDirs -= _ } }
        validInputDirs.foreach { case d =>
          val inIdxs = getIdxs(x, y, d, INPUT) // Indices of input ports
          val r = yOffset(d)
          val c = xOffset(d)
          val outIdxs = getIdxs(x+c, y+r, reverseDir(d), OUTPUT)  // Indices of output that connects to idxs
          println(s"[switch $x $y $d] : inIdxs: $inIdxs, outSwitch: [${x+c} ${y+r}], outSwitchDir: ${reverseDir(d)}, outIdxs: $outIdxs")
          inIdxs.zip(outIdxs) foreach { case (inIdx, outIdx) =>
            switches(x)(y).io.ins(inIdx) := switches(x+c)(y+r).io.outs(outIdx)
            dot.println(s"s${(x+c)}${(y+r)} -> s${x}${y}")
          }
        }
      }
    }
  }

  def connectTopUnit = {
    for (y <- List(0, rows)) {
      val d = if (y == 0) S() else N()
      for (x <- 0 to cols) {
        val switchOut = getIdxs(x, y, d, OUTPUT)(0)
        val switchIn = getIdxs(x, y, d, INPUT)(0)
        switches(x)(y).io.ins(switchIn) := topUnit.io.out
        val topInIdx = if (y > 0) cols + x else x
        topUnit.io.ins(topInIdx) := switches(x)(y).io.outs(switchOut)
        dot.println(s"top -> s${x}${y}")
        dot.println(s"s${x}${y} -> top")
      }
    }
  }

  def connectCUArray {
    for (x <- 0 until cols) {
      for (y <- 0 until rows) {
        dot.println(s"""${x}${y} [pos="${x*2+1},${y*2+1}!"]""")
        val validInputs = Set(W(), S())

        // Connect CUs together (inputs 0 (W) and 2(S))
        if (y == 0) { validInputs -= S() }
        if (x == 0) { validInputs -= W() }
        validInputs.foreach { case in =>
          val inIdx = getCUInputIdx(in) // Indices of input ports
          val r = yOffset(in)
          val c = xOffset(in)
          println(s"[cu $x $y] input dir: $in, inIdx = $inIdx, input CU = cu ${x+c}${y+r}")
          cus(x)(y).io.dataIn(inIdx) := cus(x+c)(y+r).io.dataOut
            dot.println(s"${(x+c)}${(y+r)} -> ${x}${y}")
        }
      }
    }
  }

  def connectCUAndSwitch = {
    for (i <- 0 until cols) {
      for (j <- 0 until rows) {
        // Connect NW (1) and SW(3) inputs for CUs
        val validDirs = List(NW(), SW())
        validDirs.foreach { dir =>
          val (switchx, switchy) = dir match {
            case NW() => (i, j+1)
            case SW() => (i, j)
            case _ => throw new Exception(s"Unsupported CU input dir $dir to connect switches")
          }

          // Currently we support only a single link in each of these directions
          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), INPUT) == 1, s"Num links in $dir direction must be 1")
          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), OUTPUT) == 1, s"Num links in $dir direction must be 1")

          val switchOutIdx = getIdxs(switchx, switchy, reverseDir(dir), OUTPUT)(0)
          val cuIdx = getCUInputIdx(dir)
          cus(i)(j).io.dataIn(cuIdx) := switches(switchx)(switchy).io.outs(switchOutIdx)
          dot.println(s"s${(switchx)}${(switchy)} -> ${i}${j}")
        }

        // Connect CU output to SW (i, j+1) and NW (i+1, j+1)
        val swIdxSwitch = getIdxs(i+1, j+1, SW(), INPUT)(0)
        val nwIdxSwitch = getIdxs(i+1, j, NW(), INPUT)(0)
        switches(i+1)(j+1).io.ins(swIdxSwitch) := cus(i)(j).io.dataOut
        switches(i+1)(j).io.ins(nwIdxSwitch) := cus(i)(j).io.dataOut
        dot.println(s"${(i)}${(j)} -> s${i+1}${j+1}")
        dot.println(s"${(i)}${(j)} -> s${i+1}${j}")
      }
    }
  }

  // Connect first 'numMemoryUnits' switches in first column to memory units
   def connectMUs = {
     for (i <- 0 until mus.size) {
      dot.println(s"""m${i} [shape=rectangle style=filled fillcolor=gray color=white pos="-1,${i*2}!"]""")
      val switchx = 0
      val switchy = i
      val westOutputs = getIdxs(switchx, switchy, W(), OUTPUT).map { switches(switchx)(switchy).io.outs(_) }
      val westInputs = getIdxs(switchx, switchy, W(), INPUT).map { switches(switchx)(switchy).io.ins(_) }
      val addr = westOutputs(0)
      val size = westOutputs(1)
      val wdata = westOutputs(2)
      val rdata = westInputs(0)

      rdata := mus(i).io.interconnect.rdata
      mus(i).io.interconnect.wdata := wdata
      mus(i).io.interconnect.addr :=  addr
      mus(i).io.interconnect.size :=  size(0)  // Size is a scalar

      dot.println(s"m${i} -> s${0}${i}")
      dot.println(s"s${0}${i} -> m${i}")
      dot.println(s"s${0}${i} -> m${i}")
      dot.println(s"s${0}${i} -> m${i}")
    }
  }

  def performConnections = {
    connectSwitchArray
    connectTopUnit
    connectMUs
    connectCUArray
    connectCUAndSwitch
  }

  def connectAll = {
    dot.println("digraph G {")
    dot.println(s"""top [pos="${rows},-1!"]""")
    performConnections
    dot.println("}")
    dot.close
  }
}

trait CtrlInterconnectHelper extends InterconnectHelper {

  // Define valid IOs for switches along each edge of the 2D array
  override def topIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(W(), E(), SW(), SE())
      case OUTPUT => Set(W(), E(), SW(), SE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  override def botIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(W(), E(), NW(), NE())
      case OUTPUT => Set(W(), E(), NW(), NE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  override def rightIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(W(), NW(), SW())
      case OUTPUT => Set(W(), NW(), SW())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  override def leftIO(iodir: IODirection): Set[Direction] = {
    (iodir match {
      case INPUT => Set(E(), NE(), SE())
      case OUTPUT => Set(E(), NE(), SE())
      case _ => throw new Exception(s"Unknown direction $iodir")
    }) ++ baseIODirs
  }

  // Returns the set of valid IO directions for a switch
  // at position (x, y) in cartesian co-ordinate system
  override def getValidIO(x: Int, y: Int, iodir: IODirection): Set[Direction] = {
    var validIO = regularIO(iodir)

    validIO = x match {
      case 0 => validIO.intersect(leftIO(iodir))
      case `cols` => validIO.intersect(rightIO(iodir))
      case _ => validIO
    }
    validIO = y match {
      case 0 => validIO.intersect(botIO(iodir))
      case `cols` => validIO.intersect(topIO(iodir))
      case _ => validIO
    }

    if (isMUSwitch(x, y)) {
      validIO = x match {
        case 0 => // Leftmost column
          iodir match {
            case INPUT => validIO ++ Set(W())
            case OUTPUT => validIO ++ Set(W())
          }
        case `cols` => // Rightmost column
          iodir match {
            case INPUT => validIO ++ Set(E())
            case OUTPUT => validIO ++ Set(E())
          }
        case _ => throw new Exception(s"Invalid MU switch position ($x, $y): MU can only be attached to col 0 or col $cols")
      }
    }
    validIO
  }

  override def getNumLinks(x: Int, y: Int, d: Direction, iodir: IODirection) = {
    d match {
        case W() => if (isMUSwitch(x, y) & (x == 0)) 3 else defaultNumLinks
        case E() => if (isMUSwitch(x, y) & (x == cols)) 3 else defaultNumLinks
        case _ => defaultNumLinks
    }
  }

  override def connectSwitchArray {
    for (x <- 0 to cols) {
      for (y <- 0 to rows) {
        dot.println(s"""s${x}${y} [ shape=square style=filled fillcolor=red fontcolor=white color=white fillcolor=red pos= "${x*2},${y*2}!"]""")
        val validInputDirs = getValidIO(x, y, INPUT).intersect(Set(W(), N(), E(), S()))
        getDirAssignment(x, y, INPUT)
        if (x == 0) { List(W(), NW(), SW()) foreach { validInputDirs -= _ } }
        if (x == cols) { List(E(), SE(), NE()) foreach { validInputDirs -= _ } }
        if (y == 0) { List(S(), SE(), SW()) foreach { validInputDirs -= _ } }
        if (y == rows) { List(N(), NE(), NW()) foreach { validInputDirs -= _ } }
        validInputDirs.foreach { case d =>
          val inIdxs = getIdxs(x, y, d, INPUT) // Indices of input ports
          val r = yOffset(d)
          val c = xOffset(d)
          val outIdxs = getIdxs(x+c, y+r, reverseDir(d), OUTPUT)  // Indices of output that connects to idxs
          println(s"[switch $x $y $d] : inIdxs: $inIdxs, outSwitch: [${x+c} ${y+r}], outSwitchDir: ${reverseDir(d)}, outIdxs: $outIdxs")
          inIdxs.zip(outIdxs) foreach { case (inIdx, outIdx) =>
            switches(x)(y).io.ins(inIdx)(0) := switches(x+c)(y+r).io.outs(outIdx)(0)
            dot.println(s"s${(x+c)}${(y+r)} -> s${x}${y}")
          }
        }
      }
    }
  }

  override def connectTopUnit = {
    for (y <- List(0, rows)) {
      val d = if (y == 0) S() else N()
      for (x <- 0 to cols) {
        val switchOut = getIdxs(x, y, d, OUTPUT)(0)
        val switchIn = getIdxs(x, y, d, INPUT)(0)
        switches(x)(y).io.ins(switchIn)(0) := topUnit.io.startTokenOut
        val topInIdx = if (y > 0) cols + x else x  // Row 0 gets 0..cols-1, last row gets cols..
        topUnit.io.ctrlIns(topInIdx) := switches(x)(y).io.outs(switchOut)(0)
        dot.println(s"top -> s${x}${y}")
        dot.println(s"s${x}${y} -> top")
      }
    }
  }

  // Connect first 'numMemoryUnits' switches in first column to memory units
  override def connectMUs = {
     for (i <- 0 until mus.size) {
      dot.println(s"""m${i} [shape=rectangle style=filled fillcolor=gray color=white pos="-1,${i*2}!"]""")
      val switchx = 0
      val switchy = i
      val westOutputs = getIdxs(switchx, switchy, W(), OUTPUT).map { switches(switchx)(switchy).io.outs(_)(0) }
      val westInputs = getIdxs(switchx, switchy, W(), INPUT).map { switches(switchx)(switchy).io.ins(_)(0) }
      val rdyIn = westOutputs(0)
      val vldIn = westOutputs(1)
      val dataVldIn = westOutputs(2)
      val rdyOut = westInputs(0)
      val dataRdyOut = westInputs(1)
      val vldOut = westInputs(2)

      mus(i).io.interconnect.rdyIn := rdyIn
      mus(i).io.interconnect.vldIn := vldIn
      mus(i).io.interconnect.dataVldIn := dataVldIn
      dot.println(s"s${switchx}${switchy} -> m${i}")
      dot.println(s"s${switchx}${switchy} -> m${i}")
      dot.println(s"s${switchx}${switchy} -> m${i}")

      rdyOut := mus(i).io.interconnect.rdyOut
      dataRdyOut := mus(i).io.interconnect.dataRdyOut
      vldOut := mus(i).io.interconnect.vldOut
      dot.println(s"m${i} -> s${switchx}${switchy}")
      dot.println(s"m${i} -> s${switchx}${switchy}")
      dot.println(s"m${i} -> s${switchx}${switchy}")
    }
  }

  // CU Input order: All 8 directions valid for control
  override def getCUInputIdx(d: Direction) = dirOrder.indexOf(d)

  override def connectCUArray {
    for (x <- 0 until cols) {
      for (y <- 0 until rows) {
        dot.println(s"""${x}${y} [pos="${x*2+1},${y*2+1}!"]""")
        val validInputs = Set(W(), N(), E(), S())

        // Connect CUs together (inputs 0 (W) and 2(S))
        if (y == 0) { validInputs -= S() }
        if (y == rows-1) { validInputs -= N() }
        if (x == 0) { validInputs -= W() }
        if (x == cols-1) { validInputs -= E() }
        validInputs.foreach { case in =>
          val inIdx = getCUInputIdx(in) // Indices of input ports
          val outIdx = getCUInputIdx(reverseDir(in))
          val r = yOffset(in)
          val c = xOffset(in)
          println(s"[cu $x $y] $in ($inIdx) = cu [${x+c}${y+r}] ${reverseDir(in)} $outIdx")

          val pipelineReg = Module(new FF(1))
          pipelineReg.io.control.enable := Bool(true)
          pipelineReg.io.data.in := cus(x+c)(y+r).io.tokenOuts(outIdx)
          cus(x)(y).io.tokenIns(inIdx) := pipelineReg.io.data.out
            dot.println(s"${(x+c)}${(y+r)} -> ${x}${y}")
        }
      }
    }
  }

  override def connectCUAndSwitch = {
    for (i <- 0 until cols) {
      for (j <- 0 until rows) {
        // Connect NW (1), NE(3), SE(5), SW(7) inputs for CUs
        val validDirs = List(NW(), NE(), SE(), SW())
        validDirs.foreach { dir =>
          val (switchx, switchy) = dir match {
            case NW() => (i, j+1)
            case NE() => (i+1, j+1)
            case SE() => (i+1, j)
            case SW() => (i, j)
            case _ => throw new Exception(s"Unsupported CU input direction $dir to connect switches")
          }

          // Currently we support only a single link in each of these directions
          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), INPUT) == 1, s"Num links in $dir direction must be 1")
          Predef.assert(getNumLinks(switchx, switchy, reverseDir(dir), OUTPUT) == 1, s"Num links in $dir direction must be 1")

          val switchOutIdx = getIdxs(switchx, switchy, reverseDir(dir), OUTPUT)(0)
          val switchInIdx = getIdxs(switchx, switchy, reverseDir(dir), INPUT)(0)
          val cuIdx = getCUInputIdx(dir)  // Same idx for both input and output
          cus(i)(j).io.tokenIns(cuIdx) := switches(switchx)(switchy).io.outs(switchOutIdx)(0)
          switches(switchx)(switchy).io.ins(switchInIdx)(0) := cus(i)(j).io.tokenOuts(cuIdx)
          dot.println(s"s${(switchx)}${(switchy)} -> ${i}${j}")
          dot.println(s" ${i}${j} -> s${(switchx)}${(switchy)}")
        }
      }
    }
  }

  override def performConnections = {
    connectSwitchArray
    connectTopUnit
    connectMUs
    connectCUArray
    connectCUAndSwitch
  }
}


class AbstractPlasticine(val w: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val d: Int,
  val v: Int, rwStages: Int,
  val numTokens: Int,
  val l: Int,
  val r: Int,
  val m: Int,
  val numScratchpads: Int,
  val numStagesAfterReduction: Int,
  val numMemoryUnits: Int,
  inst: PlasticineConfig)
extends Module {

  val io = new ConfigInterface {
    /* Configuration interface */
    val config_enable = Bool(INPUT)

    /* Register interface */
    val addr = UInt(INPUT, width=w)
    val wdata = UInt(INPUT, width=w)
    val wen = Bool(INPUT)
    val dataVld = Bool(INPUT)
    val rdata = UInt(OUTPUT, width=w)

    /* Memory interface */
    val dramChannel = Vec.fill(numMemoryUnits) { new DRAMCmdInterface(w, v) }
  }



}


class PlasticineSim(override val w: Int,
  override val startDelayWidth: Int,
  override val endDelayWidth: Int,
  override val d: Int,
  override val v: Int, rwStages: Int,
  override val numTokens: Int,
  override val l: Int,
  override val r: Int,
  override val m: Int,
  override val numScratchpads: Int,
  override val numStagesAfterReduction: Int,
  override val numMemoryUnits: Int,
  inst: PlasticineConfig)
extends AbstractPlasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst) {

  // Empty module
  this.name = "Plasticine"
}

/**
 * Plasticine Top
 * @param w: Word width
 * @param startDelayWidth: Start delay width
 * @param endDelayWidth: End delay width
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param rwStages: Read-write stages (at the beginning)
 * @param numTokens: Number of input (and output) tokens
 * @param l: Number of local pipeline registers
 * @param r: Number of remote pipeline registers
 * @param m: Scratchpad size in words
 */
class Plasticine(override val w: Int,
  override val startDelayWidth: Int,
  override val endDelayWidth: Int,
  override val d: Int,
  override val v: Int, rwStages: Int,
  override val numTokens: Int,
  override val l: Int,
  override val r: Int,
  override val m: Int,
  override val numScratchpads: Int,
  override val numStagesAfterReduction: Int,
  override val numMemoryUnits: Int,
  inst: PlasticineConfig) extends AbstractPlasticine(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, inst)
with DirectionOps {

  val numRows = 4
  val numCols = 4
  val burstSizeBytes = 64

  val numOutstandingBursts = 16
  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, m, v, numOutstandingBursts, burstSizeBytes, inst.mu(i)))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data

      io.dramChannel(i).addr := mu.io.dram.addr
      io.dramChannel(i).wdata := mu.io.dram.wdata
      io.dramChannel(i).tagOut := mu.io.dram.tagOut

      io.dramChannel(i).vldOut := mu.io.dram.vldOut
      io.dramChannel(i).rdyOut := mu.io.dram.rdyOut
      io.dramChannel(i).isWr  := mu.io.dram.isWr

      mu.io.dram.rdata := io.dramChannel(i).rdata
      mu.io.dram.vldIn := io.dramChannel(i).vldIn
      mu.io.dram.rdyIn := io.dramChannel(i).rdyIn
      mu.io.dram.tagIn := io.dramChannel(i).tagIn

      mu
    }
  }

  def genDataArray(inst: List[ComputeUnitConfig]) = {
    ListBuffer.tabulate(numCols) { i =>
      ListBuffer.tabulate(numRows) { j =>
        val cu = Module(new ComputeUnit(w, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, inst(i*numCols+j)))
        cu.io.config_enable := io.config_enable
        cu.io.config_data := io.config_data
        cu
      }
    }
  }

  val memoryUnits = genMemoryUnits

  val top = Module(new TopUnit(w, v, (numCols+1) * 2, inst.top))
  top.io.config_enable := io.config_enable
  top.io.config_data := io.config_data
  top.io.addr := io.addr
  top.io.wdata := io.wdata
  top.io.wen:= io.wen
  io.rdata := top.io.rdata

  val computeUnits = genDataArray(inst.cu)

  val dataInterconnect = new InterconnectHelper {
    val rows = numRows
    val cols = numCols
    val mus = memoryUnits
    val cus = computeUnits
    val topUnit = top
    val config_enable = io.config_enable
    val config_data = io.config_data
    val dot = new PrintWriter("data.dot")
    val switches = genSwitchArray(w, v, inst.dataSwitch)
  }
  dataInterconnect.connectAll

  val ctrlInterconnect = new CtrlInterconnectHelper {
    val rows = numRows
    val cols = numCols
    val cus = computeUnits
    val mus = memoryUnits
    val topUnit = top
    val config_enable = io.config_enable
    val config_data = io.config_data
    val dot = new PrintWriter("control.dot")
    val switches = genSwitchArray(1, 1, inst.controlSwitch)
  }
  ctrlInterconnect.connectAll
}

/**
 * ComputeUnit test harness
 */
class PlasticineTests(c: AbstractPlasticine) extends PlasticineTester(c) {
  var numCycles = 0

  val a = Array.tabulate(c.m) { i => i }
  val b = Array.tabulate(c.m) { i => i*2 }
  val res = Array.tabulate(c.m) { i => a(i) * b(i) }
//  setMem(c.cu0.scratchpads(0), a)
//  setMem(c.cu0.scratchpads(1), b)
//  poke(c.io.command, 1)
  while (numCycles < 100) {
    step(1)
    numCycles += 1
  }

//  expectMem(c.cu1.scratchpads(0), res)
  println(s"Done, design ran for $numCycles cycles")
}


object PlasticineTest {

  val bitwidth = 32
  val startDelayWidth = 4
  val endDelayWidth = 4
  val d = 10
  val v = 16
  val l = 0
  val r = 16
  val rwStages = 3
  val numTokens = 8
  val m = 64
  val numScratchpads = 4
  val numStagesAfterReduction = 2
  val rows = 2
  val cols = 2
  val numMemoryUnits = 2

  def main(args: Array[String]): Unit = {

    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

//    if (appArgs.size != 1) {
//      println("Usage: bin/sadl PlasticineTest <pisa config>")
//      sys.exit(-1)
//    }
//
//    val pisaFile = appArgs(0)
//    val configObj = Parser(pisaFile).asInstanceOf[PlasticineConfig]

    val config = PlasticineConfig.getRandom(d, rows, cols, numTokens, numTokens, numTokens, numScratchpads, numMemoryUnits)

    val testMode = args.contains("--test")

    if (testMode) {
      chiselMainTest(chiselArgs, () => Module(new PlasticineSim(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, config)).asInstanceOf[AbstractPlasticine]) {
        c => new PlasticineTests(c)
      }
    } else {
      chiselMainTest(chiselArgs, () => Module(new Plasticine(bitwidth, startDelayWidth, endDelayWidth, d, v, rwStages, numTokens, l, r, m, numScratchpads, numStagesAfterReduction, numMemoryUnits, config)).asInstanceOf[AbstractPlasticine]) {
        c => new PlasticineTests(c)
      }
    }


  }
}
