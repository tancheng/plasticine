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
class SwitchCUControlBox(val p: SwitchCUParams) extends Module {
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(p.numScalarIn, Bool()))
    val scalarFifoEnqVld = Output(Vec(p.numScalarIn, Bool()))

    // Counter IO
    val enable = Output(Bool())
    val done = Input(Vec(p.numCounters, Bool()))

    // Config
    val config = Input(SwitchCUControlBoxConfig(p))
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
  val udcDec = Wire(Vec(p.numUDCs, Bool()))
  val udCounters = List.tabulate(p.numUDCs) { i =>
    val udc = Module(new UpDownCtr(p.udCtrWidth))
    udc.io.initval := 0.U
    udc.io.strideInc := 1.U
    udc.io.strideDec := 1.U
    udc.io.inc := incrementXbar.io.outs(i)
    udc.io.dec := udcDec(i)
    udc
  }

  // Create all the trees
  def createAndTree(signals: Vec[Bool], config: Vec[Bool]) = {
    val muxedSignals = signals.zip(config) map { case (sig, cfg) => Mux(cfg, sig, true.B) }
    muxedSignals.reduce { _ & _ }
  }
  val siblingAndTree = createAndTree(Vec(udCounters.map { _.io.gtz}), io.config.siblingAndTree)
  val childrenAndTree = createAndTree(Vec(udCounters.map { _.io.gtz}), io.config.childrenAndTree)

  udcDec.zipWithIndex.foreach { case (dec, idx) =>
    dec := Mux(io.config.udcDecSelect(idx), childrenAndTree, doneXbar.io.outs(0))
  }

  val localEnable = childrenAndTree
  io.enable := localEnable

  // Pulser
  val pulser = Module(new PulserSM(p.udCtrWidth))
  pulser.io.in := siblingAndTree
  pulser.io.max := io.config.pulserMax

  val tokenDownOut = Mux(childrenAndTree, childrenAndTree, pulser.io.out)

  // Token out crossbar
  val tokenOutXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(3, p.numControlOut)))
  tokenOutXbar.io.config := io.config.tokenOutXbar
  tokenOutXbar.io.ins(0) := doneXbar.io.outs(0)
  tokenOutXbar.io.ins(1) := tokenDownOut
  tokenOutXbar.io.ins(2) := siblingAndTree

  io.controlOut := tokenOutXbar.io.outs
}
