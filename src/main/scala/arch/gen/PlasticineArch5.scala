package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch5 extends PlasticineArch4 {
  def connect5(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    ssbs(2)(0).io.outs(2) <> ssbs(1)(0).io.ins(12)
    ssbs(2)(0).io.outs(3) <> ssbs(1)(0).io.ins(13)
    ssbs(2)(0).io.outs(4) <> cus(1)(0).io.scalarIn(2)
    ssbs(2)(0).io.outs(5) <> ssbs(2)(1).io.ins(15)
    ssbs(2)(0).io.outs(6) <> ssbs(2)(1).io.ins(16)
    ssbs(2)(0).io.outs(7) <> ssbs(2)(1).io.ins(17)
    ssbs(2)(0).io.outs(8) <> ssbs(2)(1).io.ins(18)
    ssbs(2)(0).io.outs(9) <> lcus(2)(0).io.scalarIn(0)
    ssbs(2)(0).io.outs(10) <> lcus(2)(0).io.scalarIn(1)
    ssbs(2)(0).io.outs(11) <> lcus(2)(0).io.scalarIn(2)
    ssbs(2)(0).io.outs(12) <> lcus(2)(0).io.scalarIn(3)
    ssbs(2)(0).io.outs(13) <> scus(1)(0).io.scalarIn(0)
    ssbs(2)(0).io.outs(14) <> scus(1)(0).io.scalarIn(1)
    ssbs(2)(0).io.outs(15) <> scus(1)(0).io.scalarIn(2)
    ssbs(2)(0).io.outs(16) <> scus(1)(0).io.scalarIn(3)
    ssbs(2)(0).io.outs(17) <> argOutMuxIns(0)(2)
    ssbs(2)(0).io.outs(17) <> argOutMuxIns(1)(2)
    ssbs(2)(0).io.outs(17) <> argOutMuxIns(2)(2)
    ssbs(2)(1).io.outs(0) <> ssbs(1)(1).io.ins(10)
    ssbs(2)(1).io.outs(1) <> ssbs(1)(1).io.ins(11)
    ssbs(2)(1).io.outs(2) <> ssbs(1)(1).io.ins(12)
    ssbs(2)(1).io.outs(3) <> ssbs(1)(1).io.ins(13)
    ssbs(2)(1).io.outs(4) <> cus(1)(1).io.scalarIn(2)
    ssbs(2)(1).io.outs(5) <> ssbs(2)(2).io.ins(13)
    ssbs(2)(1).io.outs(6) <> ssbs(2)(2).io.ins(14)
    ssbs(2)(1).io.outs(7) <> ssbs(2)(2).io.ins(15)
    ssbs(2)(1).io.outs(8) <> ssbs(2)(2).io.ins(16)
    ssbs(2)(1).io.outs(9) <> lcus(2)(1).io.scalarIn(0)
    ssbs(2)(1).io.outs(10) <> lcus(2)(1).io.scalarIn(1)
    ssbs(2)(1).io.outs(11) <> lcus(2)(1).io.scalarIn(2)
    ssbs(2)(1).io.outs(12) <> lcus(2)(1).io.scalarIn(3)
    ssbs(2)(1).io.outs(13) <> scus(1)(1).io.scalarIn(0)
    ssbs(2)(1).io.outs(14) <> scus(1)(1).io.scalarIn(1)
    ssbs(2)(1).io.outs(15) <> scus(1)(1).io.scalarIn(2)
    ssbs(2)(1).io.outs(16) <> scus(1)(1).io.scalarIn(3)
    ssbs(2)(1).io.outs(17) <> ssbs(2)(0).io.ins(5)
    ssbs(2)(1).io.outs(18) <> ssbs(2)(0).io.ins(6)
    ssbs(2)(1).io.outs(19) <> ssbs(2)(0).io.ins(7)
    ssbs(2)(1).io.outs(20) <> ssbs(2)(0).io.ins(8)
    ssbs(2)(1).io.outs(21) <> cus(1)(0).io.scalarIn(1)
    ssbs(2)(2).io.outs(0) <> ssbs(1)(2).io.ins(7)
    ssbs(2)(2).io.outs(1) <> ssbs(1)(2).io.ins(8)
    ssbs(2)(2).io.outs(2) <> ssbs(1)(2).io.ins(9)
    ssbs(2)(2).io.outs(3) <> ssbs(1)(2).io.ins(10)
    ssbs(2)(2).io.outs(4) <> argOutMuxIns(0)(5)
    ssbs(2)(2).io.outs(4) <> argOutMuxIns(1)(5)
    ssbs(2)(2).io.outs(4) <> argOutMuxIns(2)(5)
    ssbs(2)(2).io.outs(5) <> lcus(2)(2).io.scalarIn(0)
    ssbs(2)(2).io.outs(6) <> lcus(2)(2).io.scalarIn(1)
    ssbs(2)(2).io.outs(7) <> lcus(2)(2).io.scalarIn(2)
    ssbs(2)(2).io.outs(8) <> lcus(2)(2).io.scalarIn(3)
    ssbs(2)(2).io.outs(9) <> scus(1)(2).io.scalarIn(0)
    ssbs(2)(2).io.outs(10) <> scus(1)(2).io.scalarIn(1)
    ssbs(2)(2).io.outs(11) <> scus(1)(2).io.scalarIn(2)
    ssbs(2)(2).io.outs(12) <> scus(1)(2).io.scalarIn(3)
    ssbs(2)(2).io.outs(13) <> ssbs(2)(1).io.ins(5)
    ssbs(2)(2).io.outs(14) <> ssbs(2)(1).io.ins(6)
    ssbs(2)(2).io.outs(15) <> ssbs(2)(1).io.ins(7)
    ssbs(2)(2).io.outs(16) <> ssbs(2)(1).io.ins(8)
    ssbs(2)(2).io.outs(17) <> cus(1)(1).io.scalarIn(1)
    // ControlNetwork Connection
    io.enable <> csbs(0)(0).io.ins(18)
    io.enable <> csbs(1)(0).io.ins(18)
    io.enable <> csbs(2)(0).io.ins(18)
    io.enable <> csbs(0)(2).io.ins(5)
    io.enable <> csbs(1)(2).io.ins(4)
    io.enable <> csbs(2)(2).io.ins(4)
    cus(0)(0).io.controlOut(0) <> csbs(0)(1).io.ins(18)
    cus(0)(0).io.controlOut(1) <> csbs(1)(1).io.ins(23)
    cus(0)(0).io.controlOut(2) <> csbs(1)(0).io.ins(4)
    cus(0)(0).io.controlOut(3) <> csbs(0)(0).io.ins(9)
    cus(1)(1).io.controlOut(0) <> csbs(1)(2).io.ins(13)
    cus(1)(1).io.controlOut(1) <> csbs(2)(2).io.ins(18)
    cus(1)(1).io.controlOut(2) <> csbs(2)(1).io.ins(4)
    cus(1)(1).io.controlOut(3) <> csbs(1)(1).io.ins(9)
    cus(0)(1).io.controlOut(0) <> csbs(0)(2).io.ins(14)
    cus(0)(1).io.controlOut(1) <> csbs(1)(2).io.ins(18)
    cus(0)(1).io.controlOut(2) <> csbs(1)(1).io.ins(4)
    cus(0)(1).io.controlOut(3) <> csbs(0)(1).io.ins(9)
    cus(1)(0).io.controlOut(0) <> csbs(1)(1).io.ins(18)
    cus(1)(0).io.controlOut(1) <> csbs(2)(1).io.ins(22)
    cus(1)(0).io.controlOut(2) <> csbs(2)(0).io.ins(4)
    cus(1)(0).io.controlOut(3) <> csbs(1)(0).io.ins(9)
    scus(0)(0).io.controlOut(0) <> csbs(0)(0).io.ins(0)
    scus(0)(0).io.controlOut(1) <> csbs(0)(0).io.ins(1)
    scus(0)(0).io.controlOut(2) <> csbs(0)(0).io.ins(2)
    scus(0)(0).io.controlOut(3) <> csbs(0)(0).io.ins(3)
    scus(0)(1).io.controlOut(0) <> csbs(0)(1).io.ins(0)
    scus(0)(1).io.controlOut(1) <> csbs(0)(1).io.ins(1)
    scus(0)(1).io.controlOut(2) <> csbs(0)(1).io.ins(2)
  }
}
