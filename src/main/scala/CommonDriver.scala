// See LICENSE for license details.

package plasticine

import chisel3.core._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import plasticine.arch._
import plasticine.spade._
import plasticine.templates._
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
    val targetDir = if (this.args.contains("--outdir")) this.args(this.args.indexOf("--outdir") + 1) else s"genVerilog/${moduleName}"
    chisel3.Driver.execute(Array[String]("--target-dir", targetDir), dut)
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
  def dut = () => new PCU(params)(0,0)
}

object PCUWrapperGen extends CommonDriver {
  type DUTType = PCUWrapper
  override val moduleName = "PCUWrapper"
  val params = new PCUParams { }
  def dut = () => new PCUWrapper(params)
}

object PMUWrapperGen extends CommonDriver {
  type DUTType = PMUWrapper
  override val moduleName = "PMUWrapper"
  val params = GeneratedPMUParams(4, 4, 4, 4, 8, 4)
  def dut = () => new PMUWrapper(params)
}

object PMUControlBoxGen extends CommonDriver {
  type DUTType = PMUControlBoxWrapper
  override val moduleName = "PMUControlBoxWrapper"
  val params = GeneratedPMUParams(4, 4, 4, 4, 8, 4)
  def dut = () => new PMUControlBoxWrapper(params)
}

object PMUGen extends CommonDriver {
  type DUTType = PMU
  override val moduleName = "PMU"
  val params = new PMUParams { }
  def dut = () => new PMU(params)
}

object ScalarCUWrapperGen extends CommonDriver {
  type DUTType = ScalarCUWrapper
  override val moduleName = "ScalarCUWrapper"
  val params = GeneratedTopParams.plasticineParams.scalarCUParams(0)(0)
  def dut = () => new ScalarCUWrapper(params)
}

object SwitchCUGen extends CommonDriver {
  type DUTType = SwitchCUWrapper
  override val moduleName = "SwitchCUWrapper"
  val params = GeneratedTopParams.plasticineParams.switchCUParams(0)(0)
  def dut = () => new SwitchCUWrapper(params)
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


object FUGen extends CommonDriver {
  type DUTType = FU
  override val moduleName = "FU"
  def dut = () => new FU(32, false, false)
}

object MuxNGen extends CommonDriver {
  type DUTType = MuxN[UInt]
  override val moduleName = "MuxN"
  def dut = () => new MuxN(UInt(32.W), 2)
}

object FFGen extends CommonDriver {
  type DUTType = FF
  override val moduleName = "FF"
  def dut = () => new FF(32)
}

object FFNoInitGen extends CommonDriver {
  type DUTType = FFNoInit
  override val moduleName = "FFNoInit"
  def dut = () => new FFNoInit(32)
}

object RegFileGen extends CommonDriver {
  type DUTType = RegFile
  override val moduleName = "RegFile"
  def dut = () => new RegFile(32, 16, 10, 10)
}

object FPMultGen extends CommonDriver {
  type DUTType = FPMult
  override val moduleName = "FPMult"
  def dut = () => new FPMult()
}

object FPAddGen extends CommonDriver {
  type DUTType = FPAdd
  override val moduleName = "FPAdd"
  def dut = () => new FPAdd()
}

object FPCompGen extends CommonDriver {
  type DUTType = FPComp
  override val moduleName = "FPComp"
  def dut = () => new FPComp()
}

object PrimitiveGen extends CommonDriver {
  type DUTType = IntPrimitiveModule
  override val moduleName = "IntPrimitiveModule"
  def getLambda(x: String): (UInt, UInt, UInt) => UInt = x match {
    case "+" => (a,b, c) => a + b
    case "-" => (a,b, c) => a - b
    case "mul" => (a,b, c) => a * b
    case "div" => (a,b, c) => a / b
    case "lt" => (a,b, c) => a < b
    case "le" => (a,b, c) => a <= b
    case "%" => (a,b, c) => a % b
    case "===" => (a,b, c) => a === b
    case "!==" => (a,b, c) => a != b
    case "min" => (a,b,c) => Mux(a<b, a, b)
    case "max" => (a,b,c) => Mux(a>b, a, b)
    case "neg" => (a,b,c) => ~a
    case "bit_and" => (a,b,c) => a & b
    case "bit_or" => (a,b,c) => a | b
    case _ => throw new Exception(s"Unknown operator $x")
  }

  def dut = () => new IntPrimitiveModule(32, getLambda(args(0)))
}
