package templates

import chisel3._

import scala.language.reflectiveCalls
/**
 * FF: Flip-flop with the ability to set enable and init
 * value as IO
 * @param w: Word width
 */
class FF(val w: Int) extends Module {
  val io = IO(new Bundle {
    val in   = Input(UInt(w.W))
    val init = Input(UInt(w.W))
    val out  = Output(UInt(w.W))
    val enable = Input(Bool())
  })

  val d = Wire(UInt(w.W))
  val ff = RegNext(d, io.init)
  when (io.enable) {
    d := io.in
  } .otherwise {
    d := ff
  }
  io.out := ff
}

class TFF(val w: Int) extends Module {
  val io = new Bundle {
    val out  = Output(UInt(w.W))
    val enable = Input(Bool())
  }

  val d = Wire(UInt(w.W))
  val ff = RegNext(d, 0.U(w.W))
  when (io.enable) {
    d := ~ff
  } .otherwise {
    d := ff
  }
  io.out := ff
}

class SRFF(val strongReset: Boolean = false) extends Module {

  // Overload with null string input for testing
  def this(n: String) = this()

  val io = IO(new Bundle {
    val input = new Bundle {
      val set = Input(Bool()) // Set overrides reset.  Asyn_reset overrides both
      val reset = Input(Bool())
      val asyn_reset = Input(Bool())
    }
    val output = new Bundle {
      val data = Output(Bool())
    }
  })

  if (!strongReset) { // Set + reset = on
    val ff = RegInit(false.B)
    ff := Mux(io.input.asyn_reset, false.B, Mux(io.input.set,
                                    true.B, Mux(io.input.reset, false.B, ff)))
    io.output.data := Mux(io.input.asyn_reset, false.B, ff)
  } else { // Set + reset = off
    val ff = RegInit(false.B)
    ff := Mux(io.input.asyn_reset, false.B, Mux(io.input.reset,
                                    false.B, Mux(io.input.set, true.B, ff)))
    io.output.data := Mux(io.input.asyn_reset, false.B, ff)

  }
}
