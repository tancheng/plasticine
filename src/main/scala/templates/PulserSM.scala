package templates

import chisel3._
import scala.language.reflectiveCalls

/**
 * PulserSM: Hold a signal high for a few (configured) number of cycles
 */
class PulserSM(val w: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Bool())
    val out = Output(UInt(1.W))
    val max = Input(UInt(w.W))
  })

  val WAIT = 0
  val RUN = 1

  // State FF
  val stateFF = Module(new FF(1))
  stateFF.io.init := WAIT.U
  stateFF.io.enable := 1.U
  val state = stateFF.io.out

  // Counter
  val counter = Module(new Counter(w))
  counter.io.max := io.max
  counter.io.stride := 1.U
  counter.io.saturate := true.B

  // Next state and output logic
  when (state === WAIT.U) {
    stateFF.io.in := Mux(io.in, RUN.U, WAIT.U)
    counter.io.enable := 0.U
    counter.io.reset := true.B
    io.out := Mux(io.in, 1.U, 0.U)
  }.elsewhen (state === RUN.U) {
    counter.io.reset := false.B
    counter.io.enable := 1.U
    stateFF.io.in := Mux(counter.io.done & ~io.in, WAIT.U, RUN.U)
    io.out := ~counter.io.done
  }.otherwise {
    stateFF.io.in  := state
  }

}
