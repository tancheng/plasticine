package plasticine.apps
import plasticine.arch._
import chisel3._
import plasticine.spade._
import plasticine.pisa.ir._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import GeneratedTopParams.plasticineParams._
import GeneratedTopParams._
import plasticine.templates._
import plasticine.pisa.enums._

trait InOutArg extends InOutArg1 {
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
  val plasticineConfig = PlasticineConfig(cuParams, vectorSwitchParams, scalarSwitchParams, controlSwitchParams, plasticineParams, fringeParams)
  val plasticineBits = PlasticineBits (
    cu=cus,
    vectorSwitch=vsbs,
    scalarSwitch=ssbs,
    controlSwitch=csbs,
    argOutMuxSelect=List(1,-1,-1)
  )  (plasticineConfig)
}
