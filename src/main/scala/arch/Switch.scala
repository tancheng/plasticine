package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import plasticine.templates.CrossbarCore
import plasticine.config.{CrossbarConfig}
import plasticine.spade._

class VectorSwitch(val p: VectorSwitchParams) extends Module {
  val io = IO(new Bundle {
    // Vector IO
    val ins = Vec(p.numIns, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
    val outs = Vec(p.numOuts, Decoupled(Vec(p.v, UInt(p.w.W))))
    val config = Input(CrossbarConfig(p))
  })

  // Crossbar for ready
//  val readyCrossbar = Module(new CrossbarCore(Bool(), VectorSwitchParams(p.numOuts, p.numIns, p.w, p.v), registerOutput = true))
//  readyCrossbar.io.config := io.config
//  readyCrossbar.io.ins := Vec(io.outs map {_.ready})

  def vecToUInt(v: Vec[UInt]) = v.reverse.reduce{Cat(_,_)}
  def uintToVec(u: UInt) = Vec(List.tabulate(p.v) { i => u(i*p.w+p.w-1, i*p.w)})

  // Crossbar for ins and valid
  val validCrossbar = Module(new CrossbarCore(UInt((p.w*p.v+1).W), p, registerOutput = true))
  validCrossbar.io.config := io.config
  validCrossbar.io.ins.zip(io.ins) foreach { case (in, i) =>
    in := Cat(vecToUInt(i.bits), i.valid)
  }

  io.outs.zipWithIndex.foreach { case (o, idx) =>
    o.bits := uintToVec(validCrossbar.io.outs(idx)(p.w*p.v, 1))
    o.valid := validCrossbar.io.outs(0)
  }

//  io.ins.zipWithIndex.foreach { case (in, idx) =>
//    in.ready := readyCrossbar.io.outs(idx)
//  }
}

class ScalarSwitch(val p: ScalarSwitchParams) extends Module {
  val io = IO(new Bundle {
    //// Scalar IO
    val ins = Vec(p.numIns, Flipped(Decoupled(UInt(p.w.W))))
    val outs = Vec(p.numOuts, Decoupled(UInt(p.w.W)))
    val config = Input(CrossbarConfig(p))
  })

  // Crossbar for ready
//  val readyCrossbar = Module(new CrossbarCore(Bool(), ScalarSwitchParams(p.numOuts, p.numIns, p.w), registerOutput = true))
//  readyCrossbar.io.config := io.config
//  readyCrossbar.io.ins := Vec(io.outs map {_.ready})

  // Crossbar for ins and valid
  val validCrossbar = Module(new CrossbarCore(UInt((p.w+1).W), p, registerOutput = true))
  validCrossbar.io.config := io.config
  validCrossbar.io.ins.zip(io.ins) foreach { case (in, i) =>
    in := Cat(i.bits, i.valid)
  }

  io.outs.zipWithIndex.foreach { case (o, idx) =>
    o.bits := validCrossbar.io.outs(idx)(p.w-1, 1)
    o.valid := validCrossbar.io.outs(0)
  }

//  io.ins.zipWithIndex.foreach { case (in, idx) =>
//    in.ready := readyCrossbar.io.outs(idx)
//  }
}

class ControlSwitch(val p: ControlSwitchParams) extends Module {
  val io = IO(new Bundle {
    //// Scalar IO
    val ins = Input(Vec(p.numIns, Bool()))
    val outs = Output(Vec(p.numOuts, Bool()))
    val config = Input(CrossbarConfig(p))
  })

  val crossbar = Module(new CrossbarCore(io.ins(0).cloneType, p, registerOutput = true))
  crossbar.io.config := io.config
  crossbar.io.ins := io.ins
  io.outs := crossbar.io.outs
}

//class Switch(p: SwitchParams) extends Module {
  //val io = IO(new Bundle {
    //// Vector IO
    //val ins = Vec(p.numVectorIn, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
    //val outs = Vec(p.numVectorOut, Decoupled(Vec(p.v, UInt(p.w.W))))

    //// Scalar IO
    //val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))
    //val scalarOut = Vec(p.numScalarOut, Decoupled(UInt(p.w.W)))

    //// Control IO
    //val controlIn = Input(Vec(p.numControlIn, Bool()))
    //val controlOut = Output(Vec(p.numControlOut, Bool()))
  //})
//}


