package templates
import chisel3._
import scala.language.reflectiveCalls
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._

/**
 * Compute Unit Control Module. Handles incoming tokens, done signals,
 * and outgoing tokens.
 */
class PMUControlBox(val p: PMUParams) extends Module {
  val io = IO(new Bundle {
    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    // Local FIFO Inputs
    val fifoNotFull = Input(Vec(p.getNumRegs(ScalarInReg), Bool()))
    val fifoNotEmpty = Input(Vec(p.getNumRegs(ScalarInReg)+p.numVectorIn, Bool()))

    // Local FIFO Outputs
    val scalarFifoDeqVld = Output(Vec(p.getNumRegs(ScalarInReg), Bool()))
    val scalarFifoEnqVld = Output(Vec(p.getNumRegs(ScalarInReg), Bool()))

    // Counter IO
    val enable = Output(Vec(p.numCounters, Bool()))
    val done = Input(Vec(p.numCounters, Bool()))

    // SRAM IO
    val sramSwapWrite = Output(Bool())
    val sramSwapRead = Output(Bool())

    // Config
    val config = Input(PMUControlBoxConfig(p))
  })

  // Increment crossbar
  val writeDoneXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numCounters + p.numControlIn, 1)))
  writeDoneXbar.io.config := io.config.writeDoneXbar
  writeDoneXbar.io.ins := Vec(io.done.getElements ++ io.controlIn.getElements)
  val writeDone = writeDoneXbar.io.outs(0)
  io.sramSwapWrite := writeDone

  val readDoneXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numCounters + p.numControlIn, 1)))
  readDoneXbar.io.config := io.config.readDoneXbar
  readDoneXbar.io.ins := Vec(io.done.getElements ++ io.controlIn.getElements)
  val readDone = readDoneXbar.io.outs(0)
  io.sramSwapRead := readDone

  io.scalarFifoDeqVld.zipWithIndex.foreach { case (deq, i) =>
    deq := Mux(io.config.scalarSwapReadSelect(i), readDone, writeDone)
  }

  // Swap Write crossbar
  val swapWriteXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(p.numControlIn, p.getNumRegs(ScalarInReg))))
  swapWriteXbar.io.config := io.config.swapWriteXbar
  swapWriteXbar.io.ins := io.controlIn

  io.scalarFifoEnqVld := swapWriteXbar.io.outs

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

  val writeEn = createAndTree(io.fifoNotEmpty, io.config.writeFifoAndTree)
  val readEn = createAndTree(io.fifoNotEmpty, io.config.readFifoAndTree)

  val readWriteUDC = Module(new UpDownCtr(p.udCtrWidth))
  readWriteUDC.io.init := false.B
  readWriteUDC.io.initval := 0.U
  readWriteUDC.io.strideInc := 1.U
  readWriteUDC.io.strideDec := 1.U
  readWriteUDC.io.inc := writeDone
  readWriteUDC.io.dec := readDone

  val localReadEnable = readEn & readWriteUDC.io.gtz
  io.enable.zipWithIndex.foreach { case (en, i) =>
    if (i == 0) en := localReadEnable else en := writeEn
  }

  // Token out crossbar
  val tokenOutXbar = Module(new CrossbarCore(Bool(), ControlSwitchParams(2+io.fifoNotFull.size, p.numControlOut)))
  tokenOutXbar.io.config := io.config.tokenOutXbar
  for (i <- 0 until io.fifoNotFull.size) {
    tokenOutXbar.io.ins(i) := io.fifoNotFull(i)
  }
  tokenOutXbar.io.ins(io.fifoNotFull.size) := writeDone
  tokenOutXbar.io.ins(io.fifoNotFull.size + 1) := readDone
  io.controlOut := tokenOutXbar.io.outs
}
