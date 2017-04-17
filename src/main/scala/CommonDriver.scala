// See LICENSE for license details.

package plasticine

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import plasticine.arch._
import plasticine.spade._
import fringe._

trait CommonDriver {
  /**
   * 'args' variable that holds commandline arguments
   * TODO: Is using a var the best way to handle this?
   */
  implicit var args: Array[String] = _
  case class SplitArgs(chiselArgs: Array[String], testArgs: Array[String])

  type DUTType <: Module
  def dut: () => DUTType
  val moduleName: String

  def separateChiselArgs(args: Array[String]) = {
    val argSeparator = "--testArgs"
    val (chiselArgs, otherArgs) = if (args.contains("--testArgs")) {
      args.splitAt(args.indexOf("--testArgs"))
    } else {
      (args, Array[String]())
    }
    val actualChiselArgs = if (chiselArgs.size == 0) Array("--help") else chiselArgs
    val testArgs = otherArgs.drop(1)
    SplitArgs(actualChiselArgs, testArgs)
  }

  def main(args: Array[String]) {
    val splitArgs = separateChiselArgs(args)
    this.args = splitArgs.testArgs
    chisel3.Driver.execute(Array[String]("--target-dir", s"genVerilog/${moduleName}"), dut)
  }
}

object PlasticineGen extends CommonDriver {
  type DUTType = Plasticine
  override val moduleName = "Plasticine"
  def dut = () => new Plasticine(GeneratedTopParams.plasticineParams, GeneratedTopParams.fringeParams)
}

object PCUGen extends CommonDriver {
  type DUTType = PCU
  override val moduleName = "PCU"
  val params = new PCUParams { }
  def dut = () => new PCU(params)
}

object PMUGen extends CommonDriver {
  type DUTType = PMU
  override val moduleName = "PMU"
  val params = new PMUParams { }
  def dut = () => new PMU(params)
}

object VectorSwitchGen extends CommonDriver {
  type DUTType = VectorSwitch
  override val moduleName = "VectorSwitch"
  val params = VectorSwitchParams(8, 8, 32, 16)
  def dut = () => new VectorSwitch(params)
}

object ScalarSwitchGen extends CommonDriver {
  type DUTType = ScalarSwitch
  override val moduleName = "ScalarSwitch"
  val params = ScalarSwitchParams(8, 8, 32)
  def dut = () => new ScalarSwitch(params)
}

object ControlSwitchGen extends CommonDriver {
  type DUTType = ControlSwitch
  override val moduleName = "ControlSwitch"
  val params = ControlSwitchParams(8, 8)
  def dut = () => new ControlSwitch(params)
}

object FringeGen extends CommonDriver {
  type DUTType = Fringe
  override val moduleName = "Fringe"
  val params = new FringeParams { }
  def dut = () => new Fringe(
    params.dataWidth,
    params.numArgIns,
    params.numArgOuts,
    params.loadStreamInfo,
    params.storeStreamInfo,
    List[StreamParInfo](),
    List[StreamParInfo](),
    false)
}

object TopGen extends CommonDriver {
  type DUTType = Top
  override val moduleName = "Top"
  def dut = () => new Top(GeneratedTopParams)
}

