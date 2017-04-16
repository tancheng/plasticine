package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import plasticine.templates.MuxN

trait PlasticineArch extends PlasticineArch1 with PlasticineArch2 with PlasticineArch3 with PlasticineArch4 with PlasticineArch5 with PlasticineArch6 with PlasticineArch7 {
  self:PlasticineArch with Plasticine =>
  def connect(io:PlasticineIO, argOutMuxes:Array[Array[DecoupledIO[UInt]]], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit =  {
    connect1(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect2(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect3(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect4(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect5(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect6(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect7(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
  }
}
