package plasticine.arch

import Chisel._
import plasticine.templates._

class Compute(override val w: Int) extends In4Out4(w) {

  // Instantiate MUXes that control each input to IntFU
  val aluMux0 = Module(new MuxN(8, w))
  val aluMux1 = Module(new MuxN(8, w))

  // Instantiate Register at output of IntFU
  val reg_out = Reg(Bits(width = w))

  // Instantiate Constant Register
  val reg_const = Reg(Bits(width = w))

  // Instantiate IntFU
  val alu = Module(new IntFU(w))
  alu.io.a := aluMux0.io.out
  alu.io.a := aluMux1.io.out

  // Wire up the inputs to each mux
  aluMux0.io.ins(0) := io.in0
  aluMux0.io.ins(1) := io.in1
  aluMux0.io.ins(2) := io.in2
  aluMux0.io.ins(3) := io.in3
  aluMux0.io.ins(4) := reg_const
  aluMux0.io.ins(5) := reg_out
  aluMux0.io.ins(6) := UInt(0)
  aluMux0.io.ins(7) := UInt(0)

  // Wire up the inputs to each mux
  aluMux1.io.ins(0) := io.in0
  aluMux1.io.ins(1) := io.in1
  aluMux1.io.ins(2) := io.in2
  aluMux1.io.ins(3) := io.in3
  aluMux1.io.ins(4) := reg_const
  aluMux1.io.ins(5) := reg_out
  aluMux1.io.ins(6) := UInt(0)
  aluMux1.io.ins(7) := UInt(0)

  // Config bits ("instruction"):
  // w+8       w+6      w+3        w          0
  // |----------|--------|---------|----------|
  // | Opcode   | Mux1   |  Mux0   | Const    |
  // |----------|--------|---------|----------|
  val numConfigBits = w + 3 * 2 + 2
  val config = Reg(Bits(width = numConfigBits))
  val const = config(0, w)
  val mux0sel = config(w, w+3)
  val mux1sel = config(w+3, w+6)
  val opcode = config(w+6, w+8)

  reg_const := const
  aluMux0.io.sel := mux0sel
  aluMux1.io.sel := mux1sel
  alu.io.opcode := opcode

  io.out0 := alu.io.out
  io.out1 := alu.io.out
  io.out2 := alu.io.out
  io.out3 := alu.io.out
}
