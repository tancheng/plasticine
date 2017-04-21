package plasticine.templates
import chisel3._
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import scala.language.reflectiveCalls

/**
 * Compute Unit Control Module. Handles incoming tokens, done signals,
 * and outgoing tokens.
 */
class PCUControlBox(val p: PCUParams) extends Module {
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Inputs
    val fifoNotFull = Input(Vec(p.numScalarIn+p.numVectorIn, Bool()))
    val fifoNotEmpty = Input(Vec(p.numScalarIn+p.numVectorIn, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(p.numScalarIn, Bool()))
    val scalarFifoEnqVld = Output(Vec(p.numScalarIn, Bool()))

    // Counter IO
    val enable = Output(Bool())
    val done = Input(Vec(p.numCounters, Bool()))

    // Config
    val config = Input(PCUControlBoxConfig(p))
  })

  // Increment crossbar
  val incrementXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, p.numUDCs)))
  incrementXbar.io.config := io.config.incrementXbar
  incrementXbar.io.ins := io.controlIn

  // Done crossbar
  val doneXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numCounters, 1)))
  doneXbar.io.config := io.config.doneXbar
  doneXbar.io.ins := io.done

  io.scalarFifoDeqVld.foreach { deq => deq := doneXbar.io.outs(0) }

  // Swap Write crossbar
  val swapWriteXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, p.numScalarIn)))
  swapWriteXbar.io.config := io.config.swapWriteXbar
  swapWriteXbar.io.ins := io.controlIn

  io.scalarFifoEnqVld := swapWriteXbar.io.outs

  // Up-down counters to handle tokens and credits
  val udCounters = List.tabulate(p.numUDCs) { i =>
    val udc = Module(new UpDownCtr(p.udCtrWidth))
    udc.io.initval := 0.U
    udc.io.strideInc := 1.U
    udc.io.strideDec := 1.U
    udc.io.inc := incrementXbar.io.outs(i)
    udc.io.dec := doneXbar.io.outs(0)
    udc
  }

  // Create all the trees
  def createAndTree(signals: Vec[Bool], config: Vec[Bool]) = {
    val muxedSignals = signals.zip(config) map { case (sig, cfg) => Mux(cfg, sig, true.B) }
    muxedSignals.reduce { _ & _ }
  }
  val siblingAndTree = createAndTree(Vec(udCounters.map { _.io.gtz}), io.config.siblingAndTree)
  val tokenInAndTree = createAndTree(io.controlIn, io.config.tokenInAndTree)
  val fifoAndTree = createAndTree(io.fifoNotEmpty, io.config.fifoAndTree)
  val andTreeTop = createAndTree(Vec(tokenInAndTree, fifoAndTree), io.config.andTreeTop)

  val localEnable = Mux(io.config.streamingMuxSelect, andTreeTop, siblingAndTree)
  io.enable := localEnable

  // Token out crossbar
  val tokenOutXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(3+io.fifoNotFull.size, p.numControlOut)))
  tokenOutXbar.io.config := io.config.tokenOutXbar
  for (i <- 0 until tokenOutXbar.io.ins.size) { i match {
    case 0 => tokenOutXbar.io.ins(i) := doneXbar.io.outs(0)
    case 1 => tokenOutXbar.io.ins(i) := siblingAndTree
    case 2 => tokenOutXbar.io.ins(i) := localEnable
    case _ => tokenOutXbar.io.ins(i) := io.fifoNotFull(i-3)
  }}

  io.controlOut := tokenOutXbar.io.outs
}
