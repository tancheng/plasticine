package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.PISADesign
import plasticine.pisa.ir.{SrcValueTuple => SVT, _}
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import plasticine.templates._
import plasticine.pisa.enums._

object SimpleSequential extends PISADesign with SimpleSequentialTrait
trait SimpleSequentialTrait extends SimpleSequentialTrait1 with SimpleSequentialTrait2 {
  val cus:Array[Array[CUBits]] = Array.tabulate(2, 2) {    case (i,j) =>
    cuParams(i)(j) match {
      case p:PCUParams => PCUBits.zeroes(p)
      case p:PMUParams => PMUBits.zeroes(p)
    }
  }
  val csbs = Array.tabulate(3, 3) {    case (i,j) =>
    CrossbarBits.zeroes(controlSwitchParams(i)(j))
  }
  val ssbs = Array.tabulate(3, 3) {    case (i,j) =>
    CrossbarBits.zeroes(scalarSwitchParams(i)(j))
  }
  val vsbs = Array.tabulate(3, 3) {    case (i,j) =>
    CrossbarBits.zeroes(vectorSwitchParams(i)(j))
  }
  val lcus = Array.tabulate(3, 3) {    case (i,j) =>
    SwitchCUBits.zeroes(switchCUParams(i)(j))
  }
  val plasticineBits = PlasticineBits (
    cu=cus,
    vectorSwitch=vsbs,
    scalarSwitch=ssbs,
    controlSwitch=csbs,
    switchCU=lcus,
    argOutMuxSelect=List(4,-1,-1),
    doneSelect=3
  )  
  // ow2768[1] -> ScalOut201(ArgIn6_x343)
  // ow2770[2] -> ScalOut197(ArgIn3_x342)
  def main(args: String*) = plasticineBits
  def config:Unit =  {
    config1
    config2
  }
  config
}
