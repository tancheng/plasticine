package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

trait PlasticineArch extends PlasticineArch1 with PlasticineArch2 with PlasticineArch3 with PlasticineArch4 with PlasticineArch5 with PlasticineArch6 with PlasticineArch7 {
  self:PlasticineArch with Plasticine =>
  def connect(cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]]):Unit =  {
    connect1(cus, vsbs, ssbs, csbs)
    connect2(cus, vsbs, ssbs, csbs)
    connect3(cus, vsbs, ssbs, csbs)
    connect4(cus, vsbs, ssbs, csbs)
    connect5(cus, vsbs, ssbs, csbs)
    connect6(cus, vsbs, ssbs, csbs)
    connect7(cus, vsbs, ssbs, csbs)
  }
}
