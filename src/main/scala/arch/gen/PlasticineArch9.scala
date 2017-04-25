package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch9 extends PlasticineArch8 {
  def connect9(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    csbs(2)(2).io.outs(5) <> lcus(2)(2).io.controlIn(0)
    csbs(2)(2).io.outs(6) <> lcus(2)(2).io.controlIn(1)
    csbs(2)(2).io.outs(7) <> lcus(2)(2).io.controlIn(2)
    csbs(2)(2).io.outs(8) <> lcus(2)(2).io.controlIn(3)
    csbs(2)(2).io.outs(9) <> lcus(2)(2).io.controlIn(4)
    csbs(2)(2).io.outs(10) <> lcus(2)(2).io.controlIn(5)
    csbs(2)(2).io.outs(11) <> lcus(2)(2).io.controlIn(6)
    csbs(2)(2).io.outs(12) <> lcus(2)(2).io.controlIn(7)
    csbs(2)(2).io.outs(13) <> scus(1)(2).io.controlIn(0)
    csbs(2)(2).io.outs(14) <> scus(1)(2).io.controlIn(1)
    csbs(2)(2).io.outs(15) <> scus(1)(2).io.controlIn(2)
    csbs(2)(2).io.outs(16) <> scus(1)(2).io.controlIn(3)
    csbs(2)(2).io.outs(17) <> mcs(1)(2).io.plasticine.controlIn(0)
    csbs(2)(2).io.outs(18) <> csbs(2)(1).io.ins(5)
    csbs(2)(2).io.outs(19) <> csbs(2)(1).io.ins(6)
    csbs(2)(2).io.outs(20) <> csbs(2)(1).io.ins(7)
    csbs(2)(2).io.outs(21) <> csbs(2)(1).io.ins(8)
    csbs(2)(2).io.outs(22) <> cus(1)(1).io.controlIn(2)
    csbs(2)(2).io.outs(23) <> cus(1)(1).io.controlIn(3)
  }
}
