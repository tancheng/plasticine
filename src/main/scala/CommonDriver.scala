// See LICENSE for license details.

package plasticine

import chisel3.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import plasticine.arch._
import plasticine.spade._
import plasticine.config._
import templates._
import fringe._

trait CommonDriver {
  /**
   * Some implicit variables available to modules that declare them as 'implicit val' in their definitions
   * TODO: Is using a var the best way to handle this?
   */
  implicit var args: Array[String] = _
  implicit var target: String = _

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
    val targetDir = if (this.args.contains("--outdir")) this.args(this.args.indexOf("--outdir") + 1) else s"genVerilog/${moduleName}"
    this.target = if (this.args.contains("--target")) this.args(this.args.indexOf("--target") + 1) else "vcs"
    chisel3.Driver.execute(Array[String]("--target-dir", targetDir), dut)
  }
}

object PlasticineGen extends CommonDriver {
  type DUTType = Plasticine
  override val moduleName = "Plasticine"
  def dut = () => new Plasticine(GeneratedTopParams.plasticineParams, GeneratedTopParams.fringeParams)
}

object DummyPCUGen extends CommonDriver {
  type DUTType = DummyPCU
  override val moduleName = "DummyPCU"
  val params = new PCUParams { }
  def dut = () => new DummyPCU(params)(0,0)
}

object PCUGen extends CommonDriver {
  type DUTType = PCU
  override val moduleName = "PCU"
  val params = GeneratedTopParams.plasticineParams.cuParams(0)(0).asInstanceOf[PCUParams]
  def dut = () => new PCU(params)(0,0)
}

object PMUGen extends CommonDriver {
  type DUTType = PMU
  override val moduleName = "PMU"
  val params = GeneratedTopParams.plasticineParams.cuParams(0)(1).asInstanceOf[PMUParams]
  def dut = () => new PMU(params)(0,0)
}

abstract class CUSim[P <: CUParams, D <: CU, C <: AbstractConfig]() extends CommonDriver {
  type DUTType = TopCU[P, D, C]
  def params: P
  def cu: D
  def config: C
  def dut = () => new TopCU(() => params, () => cu, () => config)
}

object PMUSim extends CUSim[PMUParams, PMU, PMUConfig] {
  override val moduleName = "TopPMU"
  def params = GeneratedTopParams.plasticineParams.cuParams(0)(1).asInstanceOf[PMUParams]
  def cu = new PMU(params)(0,0)
  def config = new PMUConfig(params)
}

object PCUSim extends CUSim[PCUParams, PCU, PCUConfig] {
  override val moduleName = "TopPCU"
  def params = GeneratedTopParams.plasticineParams.cuParams(0)(0).asInstanceOf[PCUParams]
  def cu = new PCU(params)(0,0)
  def config = new PCUConfig(params)
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

//object FringeGen extends CommonDriver {
//  type DUTType = Fringe
//  override val moduleName = "Fringe"
//  val params = new FringeParams { }
//  def dut = () => new Fringe(
//    params.dataWidth,
//    params.numArgIns,
//    params.numArgOuts,
//    params.loadStreamInfo,
//    params.storeStreamInfo,
//    List[StreamParInfo](),
//    List[StreamParInfo](),
//    false)
//}

object TopGen extends CommonDriver {
  type DUTType = Top
  override val moduleName = "Top"
  def dut = () => new Top(GeneratedTopParams)
}


object FUGen extends CommonDriver {
  type DUTType = FU
  override val moduleName = "FU"
  def dut = () => new FU(32)
}
