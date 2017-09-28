package templates

import chisel3._
import templates.Utils.log2Up
import chisel3.util.Decoupled
import scala.language.reflectiveCalls

/**
 * PISO: Parallel-in serial-out module
 * @param numInputs: Number of parallel input writes of type T
 */
class PISO[T<:Data](val t: T, val numInputs: Int) extends Module {
  val io = IO(new Bundle {
    val in   = Flipped(Decoupled((Vec(numInputs, t.cloneType))))
    val out  = Decoupled(t.cloneType)
    val clear = Input(Bool())
  })

  val shift = io.out.valid & io.out.ready

  val shiftReg = List.tabulate(numInputs) { i =>
    val ff = Module(new FF(t.getWidth))
    ff.io.init := 0.U
    ff
  }

  for (i <- 0 until numInputs) {
    shiftReg(i).io.in := Mux(io.clear, 0.U, Mux(shift, (if (i == numInputs-1) 0.U else shiftReg(i+1).io.out), io.in.bits(i)))
    shiftReg(i).io.enable := shift | (io.in.ready & io.in.valid)
  }

  val count = Module(new UpDownCtr((log2Up(numInputs+1))))
  count.io.enable := 1.U
  count.io.initval := 0.U
  count.io.max := numInputs.U
  count.io.strideInc := numInputs.U
  count.io.strideDec := 1.U
  count.io.initAtConfig := 0.U
  count.io.init := io.clear
  count.io.inc := io.in.valid & io.in.ready
  count.io.dec := shift

  io.out.bits := shiftReg(0).io.out
  io.out.valid := count.io.gtz
  io.in.ready := count.io.out === 0.U

}
