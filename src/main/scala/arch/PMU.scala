package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Plasticine Memory Unit
 * @param w: Word width
 * @param d: Pipeline depth
 * @param v: Vector length
 * @param r: Pipeline registers per lane per stage
 * @param numCounters: Number of counters
 * @param numScalarIn: Input scalars
 * @param numScalarOut: Output scalars
 * @param numVectorIn: Input vectors
 * @param numVectorOut: Output vectors
 * @param numControlIn: Input controls
 * @param numControlOut: Output controls
 */
case class PMUParams(
  val w: Int,
  val d: Int,
  val v: Int,
  val r: Int,
  val numCounters: Int,
  val numScalarIn: Int,
  val numScalarOut: Int,
  val numVectorIn: Int,
  val numVectorOut: Int,
  val numControlIn: Int,
  val numControlOut: Int
)

case class PMUConfig(p: PMUParams) extends Bundle {

  override def cloneType(): this.type = {
    new PMUConfig(p).asInstanceOf[this.type]
  }
}

class PMU(p: PMUParams) extends Module {

  val io = IO(new Bundle {
    // Vector IO
    val vecIn = Vec(p.numVectorIn, Flipped(Decoupled(Vec(p.v, UInt(p.w.W)))))
    val vecOut = Vec(p.numVectorOut, Decoupled(Vec(p.v, UInt(p.w.W))))

    // Scalar IO
    val scalarIn = Vec(p.numScalarIn, Flipped(Decoupled(UInt(p.w.W))))
    val scalarOut = Vec(p.numScalarOut, Decoupled(UInt(p.w.W)))

    // Control IO
    val controlIn = Input(Vec(p.numControlIn, Bool()))
    val controlOut = Output(Vec(p.numControlOut, Bool()))

    val config = Input(PMUConfig(p))
  })

}
