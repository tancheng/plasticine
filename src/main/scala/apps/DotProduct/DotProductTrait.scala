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

object DotProduct extends PISADesign with DotProductTrait
trait DotProductTrait extends DotProductTrait1 with DotProductTrait2 with DotProductTrait3 with DotProductTrait4 with DotProductTrait5 {
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
    argOutMuxSelect=List(4,-1,-1),
    doneSelect=1
  )  
  // Top1024.ow4079[0] -> ScalOut519
  // Top1024.ow4083[1] -> ScalOut517
  // Top1024.ow4087[2] -> ScalOut515
  def main(args: String*) = plasticineBits
  def config:Unit = {
    config1
    config2
    config3
    config4
    config5
  }
  config
}
