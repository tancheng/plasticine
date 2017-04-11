package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

trait Params

case class CUIO(p:PCUParams, cuConfig:Bundle) extends Bundle {
    // Vector IO
  val vecIn = Vec(p.numVectorIn, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
  val vecOut = Vec(p.numVectorOut, Decoupled(Vec(p.v, UInt(p.w.W))))

  // Scalar IO
  val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))
  val scalarOut = Vec(p.numScalarOut, Decoupled(UInt(p.w.W)))

  // Control IO
  val controlIn = Input(Vec(p.numControlIn, Bool()))
  val controlOut = Output(Vec(p.numControlOut, Bool()))

  val config = cuConfig
}

trait CU extends Module {
  def io : CUIO
  def p  : PCUParams
}
