package plasticine.arch

import chisel3._
import chisel3.util._
import plasticine.templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import plasticine.spade._
import plasticine.config._

/**
 * Switch Compute Unit
 */
class SwitchCU(val p: SwitchCUParams) extends CU {
  val io = IO(CUIO(p, SwitchCUConfig(p)))
}
