package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

trait PlasticineArch7{
  self:PlasticineArch with Plasticine =>
  csbs(2)(2).io.outs(16) <> csbs(2)(1).io.ins(8)
  csbs(2)(2).io.outs(17) <> cus(1)(1).io.controlIn(2)
  csbs(2)(2).io.outs(18) <> cus(1)(1).io.controlIn(3)
}
