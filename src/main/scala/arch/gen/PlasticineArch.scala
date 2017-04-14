package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import plasticine.templates.MuxN

trait PlasticineArch extends PlasticineArch7 {
  self:PlasticineArch with Plasticine =>
  def connect(io:PlasticineIO, argOutMuxes:List[MuxN], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[PMU]]):Unit =  {
    connect1(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect2(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect3(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect4(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect5(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect6(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
    connect7(io, argOutMuxes, cus, vsbs, ssbs, csbs, lcus)
  }
}
