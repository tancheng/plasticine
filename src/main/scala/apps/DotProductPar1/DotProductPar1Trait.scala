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

object DotProductPar1 extends PISADesign with DotProductPar1Trait
trait DotProductPar1Trait extends DotProductPar1Trait1 with DotProductPar1Trait2 with DotProductPar1Trait3 with DotProductPar1Trait4 with DotProductPar1Trait5 {
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
  val scus = Array.tabulate(2, 3) {    case (i,j) =>
    ScalarCUBits.zeroes(scalarCUParams(i)(j))
  }
  val mcs = Array.tabulate(2, 3) {    case (i,j) =>
    MemoryChannelBits.zeroes(memoryChannelParams(i)(j))
  }
  val plasticineBits = PlasticineBits (
    cu=cus,
    vectorSwitch=vsbs,
    scalarSwitch=ssbs,
    controlSwitch=csbs,
    switchCU=lcus,
    scalarCU=scus,
    memoryChannel=mcs,
    argOutMuxSelect=List(1,-1,-1),
    doneSelect=0
  )  
  // ow2933[0] -> ScalOut465(ArgIn12_x1056)
  // ow2935[1] -> ScalOut467(ArgIn13_x1037)
  // ow2937[2] -> ScalOut463(ArgIn6_x1019)
  def main(args: String*) = plasticineBits
  def config:Unit =  {
    config1
    config2
    config3
    config4
    config5
  }
  config
}
