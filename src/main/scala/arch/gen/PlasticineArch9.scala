package plasticine.arch
import chisel3._
import chisel3.util._
import templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch9 extends PlasticineArch8  {
  def connect9(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit =  {
    csbs(2)(1).io.outs(19) <> scus(1)(1).io.controlIn(2)
    csbs(2)(1).io.outs(20) <> scus(1)(1).io.controlIn(3)
    csbs(2)(1).io.outs(21) <> mcs(1)(1).io.plasticine.controlIn(0)
    csbs(2)(1).io.outs(22) <> csbs(2)(0).io.ins(5)
    csbs(2)(1).io.outs(23) <> csbs(2)(0).io.ins(6)
    csbs(2)(1).io.outs(24) <> csbs(2)(0).io.ins(7)
    csbs(2)(1).io.outs(25) <> csbs(2)(0).io.ins(8)
    csbs(2)(1).io.outs(26) <> csbs(2)(0).io.ins(9)
    csbs(2)(1).io.outs(27) <> csbs(2)(0).io.ins(10)
    csbs(2)(1).io.outs(28) <> csbs(2)(0).io.ins(11)
    csbs(2)(1).io.outs(29) <> csbs(2)(0).io.ins(12)
    csbs(2)(1).io.outs(30) <> cus(1)(0).io.controlIn(1)
    csbs(2)(2).io.outs(0) <> csbs(1)(2).io.ins(5)
    csbs(2)(2).io.outs(1) <> csbs(1)(2).io.ins(6)
    csbs(2)(2).io.outs(2) <> csbs(1)(2).io.ins(7)
    csbs(2)(2).io.outs(3) <> csbs(1)(2).io.ins(8)
    csbs(2)(2).io.outs(4) <> doneOuts(5)
    csbs(2)(2).io.outs(5) <> lcus(2)(2).io.controlIn(0)
    csbs(2)(2).io.outs(6) <> lcus(2)(2).io.controlIn(1)
    csbs(2)(2).io.outs(7) <> lcus(2)(2).io.controlIn(2)
    csbs(2)(2).io.outs(8) <> lcus(2)(2).io.controlIn(3)
    csbs(2)(2).io.outs(9) <> scus(1)(2).io.controlIn(0)
    csbs(2)(2).io.outs(10) <> scus(1)(2).io.controlIn(1)
    csbs(2)(2).io.outs(11) <> scus(1)(2).io.controlIn(2)
    csbs(2)(2).io.outs(12) <> scus(1)(2).io.controlIn(3)
    csbs(2)(2).io.outs(13) <> mcs(1)(2).io.plasticine.controlIn(0)
    csbs(2)(2).io.outs(14) <> csbs(2)(1).io.ins(5)
    csbs(2)(2).io.outs(15) <> csbs(2)(1).io.ins(6)
    csbs(2)(2).io.outs(16) <> csbs(2)(1).io.ins(7)
    csbs(2)(2).io.outs(17) <> csbs(2)(1).io.ins(8)
    csbs(2)(2).io.outs(18) <> csbs(2)(1).io.ins(9)
    csbs(2)(2).io.outs(19) <> csbs(2)(1).io.ins(10)
    csbs(2)(2).io.outs(20) <> csbs(2)(1).io.ins(11)
    csbs(2)(2).io.outs(21) <> csbs(2)(1).io.ins(12)
    csbs(2)(2).io.outs(22) <> cus(1)(1).io.controlIn(1)
  }
}
