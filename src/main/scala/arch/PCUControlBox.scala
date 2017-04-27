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
  val numScalarFIFOs = p.getNumRegs(ScalarInReg)
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Inputs
    val fifoNotFull = Input(Vec(numScalarFIFOs, Bool()))
    val fifoNotEmpty = Input(Vec(numScalarFIFOs+p.numVectorIn, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(numScalarFIFOs, Bool()))
    val scalarFifoEnqVld = Output(Vec(numScalarFIFOs, Bool()))

    // Counter IO
    val enable = Output(Bool())
    val delayedEnable = Input(Bool())
    val done = Input(Bool())
    val delayedDone = Input(Bool())

    // Config
    val config = Input(PCUControlBoxConfig(p))
  })

  // Increment crossbar
  val incrementXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, p.numUDCs)))
  incrementXbar.io.config := io.config.incrementXbar
  incrementXbar.io.ins := io.controlIn

  // Done crossbar
//  val doneXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numCounters, 1)))
//  doneXbar.io.config := io.config.doneXbar
//  doneXbar.io.ins := io.done

//  io.scalarFifoDeqVld.foreach { deq => deq := doneXbar.io.outs(0) }
  io.scalarFifoDeqVld.foreach { deq => deq := io.done }

  // Swap Write crossbar
  val swapWriteXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, numScalarFIFOs)))
  swapWriteXbar.io.config := io.config.swapWriteXbar
  swapWriteXbar.io.ins := io.controlIn

  io.scalarFifoEnqVld := swapWriteXbar.io.outs

  // Up-down counters to handle tokens and credits
  val udCounters = List.tabulate(p.numUDCs) { i =>
    val udc = Module(new UpDownCtr(p.udCtrWidth))
    udc.io.initAtConfig := true.B
    udc.io.init := false.B
    udc.io.initval := io.config.udcInit(i)
    udc.io.strideInc := 1.U
    udc.io.strideDec := 1.U
    udc.io.inc := incrementXbar.io.outs(i)
    udc.io.dec := io.done
    val max = ((1 << p.udCtrWidth) - 1).U
    udc.io.max := max

    // asserts
    assert(~udc.io.inc | (udc.io.inc & (udc.io.out < max)) | (udc.io.inc & udc.io.dec), s"UDC $i overflow!")
    assert(~udc.io.dec | (udc.io.dec & (udc.io.out > 0.U)) | (udc.io.inc & udc.io.dec), s"UDC $i underflow!")
    udc
  }

  // Create all the trees
  def createAndTree(signals: Vec[Bool], config: Vec[Bool]) = {
    val muxedSignals = signals.zip(config) map { case (sig, cfg) => Mux(cfg, sig, true.B) }
    muxedSignals.reduce { _ & _ }
  }
  // Require this overloaded method because chisel3 makes it surprisingly hard to create
  // Vecs out of arbitrary signals of the same type
  def createAndTree(signals: List[Bool], config: Vec[Bool]) = {
    val muxedSignals = signals.zip(config) map { case (sig, cfg) => Mux(cfg, sig, true.B) }
    muxedSignals.reduce { _ & _ }
  }

  val fifoAndTree = createAndTree(io.fifoNotEmpty, io.config.fifoAndTree)
  val tokenInAndTree = createAndTree(io.controlIn, io.config.tokenInAndTree)
  val andTreeTop = tokenInAndTree & fifoAndTree
  val siblingAndTree = createAndTree(udCounters.map { _.io.gtz}, io.config.siblingAndTree)

  val localEnable = Mux(io.config.streamingMuxSelect, andTreeTop, siblingAndTree & fifoAndTree)
  io.enable := localEnable

  // Token out crossbar
  val tokenOutXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(2+io.fifoNotFull.size, p.numControlOut)))
  tokenOutXbar.io.config := io.config.tokenOutXbar
  for (i <- 0 until io.fifoNotFull.size) {
    tokenOutXbar.io.ins(i) := io.fifoNotFull(i)
  }
  tokenOutXbar.io.ins(io.fifoNotFull.size) := io.delayedDone
  tokenOutXbar.io.ins(io.fifoNotFull.size + 1) := io.delayedEnable

  io.controlOut := tokenOutXbar.io.outs
}
