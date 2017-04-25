package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch6 extends PlasticineArch5 {
  def connect6(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    lcus(0)(0).io.controlOut(1) <> csbs(0)(0).io.ins(15)
    lcus(0)(0).io.controlOut(2) <> csbs(0)(0).io.ins(16)
    lcus(0)(0).io.controlOut(3) <> csbs(0)(0).io.ins(17)
    lcus(0)(1).io.controlOut(0) <> csbs(0)(1).io.ins(14)
    lcus(0)(1).io.controlOut(1) <> csbs(0)(1).io.ins(15)
    lcus(0)(1).io.controlOut(2) <> csbs(0)(1).io.ins(16)
    lcus(0)(1).io.controlOut(3) <> csbs(0)(1).io.ins(17)
    lcus(0)(2).io.controlOut(0) <> csbs(0)(2).io.ins(10)
    lcus(0)(2).io.controlOut(1) <> csbs(0)(2).io.ins(11)
    lcus(0)(2).io.controlOut(2) <> csbs(0)(2).io.ins(12)
    lcus(0)(2).io.controlOut(3) <> csbs(0)(2).io.ins(13)
    lcus(1)(0).io.controlOut(0) <> csbs(1)(0).io.ins(14)
    lcus(1)(0).io.controlOut(1) <> csbs(1)(0).io.ins(15)
    lcus(1)(0).io.controlOut(2) <> csbs(1)(0).io.ins(16)
    lcus(1)(0).io.controlOut(3) <> csbs(1)(0).io.ins(17)
    lcus(1)(1).io.controlOut(0) <> csbs(1)(1).io.ins(14)
    lcus(1)(1).io.controlOut(1) <> csbs(1)(1).io.ins(15)
    lcus(1)(1).io.controlOut(2) <> csbs(1)(1).io.ins(16)
    lcus(1)(1).io.controlOut(3) <> csbs(1)(1).io.ins(17)
    lcus(1)(2).io.controlOut(0) <> csbs(1)(2).io.ins(9)
    lcus(1)(2).io.controlOut(1) <> csbs(1)(2).io.ins(10)
    lcus(1)(2).io.controlOut(2) <> csbs(1)(2).io.ins(11)
    lcus(1)(2).io.controlOut(3) <> csbs(1)(2).io.ins(12)
    lcus(2)(0).io.controlOut(0) <> csbs(2)(0).io.ins(9)
    lcus(2)(0).io.controlOut(1) <> csbs(2)(0).io.ins(10)
    lcus(2)(0).io.controlOut(2) <> csbs(2)(0).io.ins(11)
    lcus(2)(0).io.controlOut(3) <> csbs(2)(0).io.ins(12)
    lcus(2)(1).io.controlOut(0) <> csbs(2)(1).io.ins(9)
    lcus(2)(1).io.controlOut(1) <> csbs(2)(1).io.ins(10)
    lcus(2)(1).io.controlOut(2) <> csbs(2)(1).io.ins(11)
    lcus(2)(1).io.controlOut(3) <> csbs(2)(1).io.ins(12)
    lcus(2)(2).io.controlOut(0) <> csbs(2)(2).io.ins(5)
    lcus(2)(2).io.controlOut(1) <> csbs(2)(2).io.ins(6)
    lcus(2)(2).io.controlOut(2) <> csbs(2)(2).io.ins(7)
    lcus(2)(2).io.controlOut(3) <> csbs(2)(2).io.ins(8)
    mcs(0)(0).io.plasticine.controlOut(0) <> scus(0)(0).io.controlIn(4)
    mcs(0)(0).io.plasticine.controlOut(1) <> scus(0)(0).io.controlIn(5)
    mcs(0)(0).io.plasticine.controlOut(2) <> csbs(0)(0).io.ins(4)
    mcs(0)(1).io.plasticine.controlOut(0) <> scus(0)(1).io.controlIn(4)
    mcs(0)(1).io.plasticine.controlOut(1) <> scus(0)(1).io.controlIn(5)
    mcs(0)(1).io.plasticine.controlOut(2) <> csbs(0)(1).io.ins(4)
    mcs(0)(2).io.plasticine.controlOut(0) <> scus(0)(2).io.controlIn(4)
    mcs(0)(2).io.plasticine.controlOut(1) <> scus(0)(2).io.controlIn(5)
    mcs(0)(2).io.plasticine.controlOut(2) <> csbs(0)(2).io.ins(4)
    mcs(1)(0).io.plasticine.controlOut(0) <> csbs(2)(0).io.ins(17)
    mcs(1)(0).io.plasticine.controlOut(1) <> scus(1)(0).io.controlIn(4)
    mcs(1)(0).io.plasticine.controlOut(2) <> scus(1)(0).io.controlIn(5)
    mcs(1)(1).io.plasticine.controlOut(0) <> csbs(2)(1).io.ins(17)
    mcs(1)(1).io.plasticine.controlOut(1) <> scus(1)(1).io.controlIn(4)
    mcs(1)(1).io.plasticine.controlOut(2) <> scus(1)(1).io.controlIn(5)
    mcs(1)(2).io.plasticine.controlOut(0) <> csbs(2)(2).io.ins(13)
    mcs(1)(2).io.plasticine.controlOut(1) <> scus(1)(2).io.controlIn(4)
    mcs(1)(2).io.plasticine.controlOut(2) <> scus(1)(2).io.controlIn(5)
    csbs(0)(0).io.outs(0) <> scus(0)(0).io.controlIn(0)
    csbs(0)(0).io.outs(1) <> scus(0)(0).io.controlIn(1)
    csbs(0)(0).io.outs(2) <> scus(0)(0).io.controlIn(2)
    csbs(0)(0).io.outs(3) <> scus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(4) <> mcs(0)(0).io.plasticine.controlIn(0)
    csbs(0)(0).io.outs(5) <> csbs(0)(1).io.ins(19)
    csbs(0)(0).io.outs(6) <> csbs(0)(1).io.ins(20)
    csbs(0)(0).io.outs(7) <> csbs(0)(1).io.ins(21)
    csbs(0)(0).io.outs(8) <> csbs(0)(1).io.ins(22)
    csbs(0)(0).io.outs(9) <> cus(0)(0).io.controlIn(6)
    csbs(0)(0).io.outs(10) <> cus(0)(0).io.controlIn(7)
    csbs(0)(0).io.outs(11) <> csbs(1)(0).io.ins(0)
    csbs(0)(0).io.outs(12) <> csbs(1)(0).io.ins(1)
    csbs(0)(0).io.outs(13) <> csbs(1)(0).io.ins(2)
    csbs(0)(0).io.outs(14) <> csbs(1)(0).io.ins(3)
    csbs(0)(0).io.outs(15) <> lcus(0)(0).io.controlIn(0)
    csbs(0)(0).io.outs(16) <> lcus(0)(0).io.controlIn(1)
    csbs(0)(0).io.outs(17) <> lcus(0)(0).io.controlIn(2)
    csbs(0)(0).io.outs(18) <> lcus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(19) <> lcus(0)(0).io.controlIn(4)
    csbs(0)(0).io.outs(20) <> lcus(0)(0).io.controlIn(5)
    csbs(0)(0).io.outs(21) <> lcus(0)(0).io.controlIn(6)
    csbs(0)(0).io.outs(22) <> lcus(0)(0).io.controlIn(7)
    csbs(0)(0).io.outs(23) <> doneOuts(0)
    csbs(0)(1).io.outs(0) <> scus(0)(1).io.controlIn(0)
    csbs(0)(1).io.outs(1) <> scus(0)(1).io.controlIn(1)
    csbs(0)(1).io.outs(2) <> scus(0)(1).io.controlIn(2)
    csbs(0)(1).io.outs(3) <> scus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(4) <> mcs(0)(1).io.plasticine.controlIn(0)
    csbs(0)(1).io.outs(5) <> csbs(0)(2).io.ins(15)
    csbs(0)(1).io.outs(6) <> csbs(0)(2).io.ins(16)
    csbs(0)(1).io.outs(7) <> csbs(0)(2).io.ins(17)
    csbs(0)(1).io.outs(8) <> csbs(0)(2).io.ins(18)
    csbs(0)(1).io.outs(9) <> cus(0)(1).io.controlIn(6)
    csbs(0)(1).io.outs(10) <> cus(0)(1).io.controlIn(7)
    csbs(0)(1).io.outs(11) <> csbs(1)(1).io.ins(0)
    csbs(0)(1).io.outs(12) <> csbs(1)(1).io.ins(1)
  }
}
