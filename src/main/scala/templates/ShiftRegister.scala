package fringe

import util.GenericParameterizedBundle
import util.HVec
import chisel3._
import chisel3.core.Record
import chisel3.util._
import scala.collection.immutable.ListMap

class ShiftRegister[T<:Data](val t: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(Bool()))
    val out = Decoupled(Bool())
    val config = Output(t.cloneType)
  })

  val w = t.getWidth
  val sr = List.fill(w) { Module(new FF(2)) }

  for (i <- 0 until w) {
    sr(i).io.init := 0.U
    if (i == 0) {
      sr(i).io.in := Cat(io.in.bits, io.in.valid)
      sr(i).io.enable := io.in.valid
    } else {
      sr(i).io.in := sr(i-1).io.out
      sr(i).io.enable := sr(i-1).io.out(0)
    }
    if (i == w-1) {
      io.out.bits := sr(i-1).io.out(1)
      io.out.valid := sr(i-1).io.out(0)
    }
  }

  val configBits = sr.map { _.io.out }.reduce {Cat(_,_)}

  // Collect output of all FFs, assign to config
  io.config := t.cloneType.fromBits(configBits)
}
