package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch15 extends PlasticineArch14 {
  def connect15(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    csbs(2)(2).io.outs(29) <> csbs(2)(1).io.ins(19)
    csbs(2)(2).io.outs(30) <> csbs(2)(1).io.ins(20)
    csbs(2)(2).io.outs(31) <> csbs(2)(1).io.ins(21)
    csbs(2)(2).io.outs(32) <> csbs(2)(1).io.ins(22)
    csbs(2)(2).io.outs(33) <> csbs(2)(1).io.ins(23)
    csbs(2)(2).io.outs(34) <> csbs(2)(1).io.ins(24)
    csbs(2)(2).io.outs(35) <> csbs(2)(1).io.ins(25)
    csbs(2)(2).io.outs(36) <> csbs(2)(1).io.ins(26)
    csbs(2)(2).io.outs(37) <> csbs(2)(1).io.ins(27)
    csbs(2)(2).io.outs(38) <> csbs(2)(1).io.ins(28)
    csbs(2)(2).io.outs(39) <> csbs(2)(1).io.ins(29)
    csbs(2)(2).io.outs(40) <> csbs(2)(1).io.ins(30)
    csbs(2)(2).io.outs(41) <> csbs(2)(1).io.ins(31)
    csbs(2)(2).io.outs(42) <> csbs(2)(1).io.ins(32)
    csbs(2)(2).io.outs(43) <> csbs(2)(1).io.ins(33)
    csbs(2)(2).io.outs(44) <> csbs(2)(1).io.ins(34)
    csbs(2)(2).io.outs(45) <> csbs(2)(1).io.ins(35)
    csbs(2)(2).io.outs(46) <> csbs(2)(1).io.ins(36)
    csbs(2)(2).io.outs(47) <> csbs(2)(1).io.ins(37)
    csbs(2)(2).io.outs(48) <> cus(1)(1).io.controlIn(4)
    csbs(2)(2).io.outs(49) <> cus(1)(1).io.controlIn(5)
    csbs(2)(2).io.outs(50) <> cus(1)(1).io.controlIn(6)
    csbs(2)(2).io.outs(51) <> cus(1)(1).io.controlIn(7)
  }
}
