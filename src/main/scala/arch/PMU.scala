package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import plasticine.spade._

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
 * @param wd: Number of stages that can be configured to write address calculation
 */
case class PMUConfig(p: PMUParams) extends Bundle {

  override def cloneType(): this.type = {
    new PMUConfig(p).asInstanceOf[this.type]
  }
}

class PMU(val p: PMUParams) extends CU {
  val io = IO(CUIO(p, PMUConfig(p)))
}
