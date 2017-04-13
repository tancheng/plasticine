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
}

class ScalarSwitch(val p: ScalarSwitchParams) extends Module {
  val io = IO(new Bundle {
    //// Scalar IO
    val ins = Vec(p.numIns, Flipped(Decoupled(UInt(p.w.W))))
    val outs = Vec(p.numOuts, Decoupled(UInt(p.w.W)))
    val config = Input(CrossbarConfig(p))
  })
}

class ControlSwitch(val p: ControlSwitchParams) extends Module {
  val io = IO(new Bundle {
    //// Scalar IO
    val ins = Input(Vec(p.numIns, Bool()))
    val outs = Output(Vec(p.numOuts, Bool()))
    val config = Input(CrossbarConfig(p))
  })
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


