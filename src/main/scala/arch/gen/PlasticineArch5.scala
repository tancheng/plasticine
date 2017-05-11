package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch5 extends PlasticineArch4 {
  def connect5(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    ssbs(0)(1).io.outs(17) <> ssbs(0)(2).io.ins(22)
    ssbs(0)(1).io.outs(18) <> ssbs(0)(2).io.ins(23)
    ssbs(0)(1).io.outs(19) <> ssbs(0)(2).io.ins(24)
    ssbs(0)(1).io.outs(20) <> ssbs(0)(2).io.ins(25)
    ssbs(0)(1).io.outs(21) <> ssbs(0)(2).io.ins(26)
    ssbs(0)(1).io.outs(22) <> ssbs(0)(2).io.ins(27)
    ssbs(0)(1).io.outs(23) <> cus(0)(1).io.scalarIn(3)
    ssbs(0)(1).io.outs(24) <> ssbs(1)(1).io.ins(0)
    ssbs(0)(1).io.outs(25) <> ssbs(1)(1).io.ins(1)
    ssbs(0)(1).io.outs(26) <> ssbs(1)(1).io.ins(2)
    ssbs(0)(1).io.outs(27) <> ssbs(1)(1).io.ins(3)
    ssbs(0)(1).io.outs(28) <> lcus(0)(1).io.scalarIn(0)
    ssbs(0)(1).io.outs(29) <> lcus(0)(1).io.scalarIn(1)
    ssbs(0)(1).io.outs(30) <> lcus(0)(1).io.scalarIn(2)
    ssbs(0)(1).io.outs(31) <> lcus(0)(1).io.scalarIn(3)
    ssbs(0)(1).io.outs(32) <> cus(0)(0).io.scalarIn(0)
    ssbs(0)(1).io.outs(33) <> ssbs(0)(0).io.ins(4)
    ssbs(0)(1).io.outs(34) <> ssbs(0)(0).io.ins(5)
    ssbs(0)(1).io.outs(35) <> ssbs(0)(0).io.ins(6)
    ssbs(0)(1).io.outs(36) <> ssbs(0)(0).io.ins(7)
    ssbs(0)(1).io.outs(37) <> ssbs(0)(0).io.ins(8)
    ssbs(0)(1).io.outs(38) <> ssbs(0)(0).io.ins(9)
    ssbs(0)(1).io.outs(39) <> ssbs(0)(0).io.ins(10)
    ssbs(0)(1).io.outs(40) <> ssbs(0)(0).io.ins(11)
    ssbs(0)(1).io.outs(41) <> ssbs(0)(0).io.ins(12)
    ssbs(0)(1).io.outs(42) <> ssbs(0)(0).io.ins(13)
    ssbs(0)(1).io.outs(43) <> ssbs(0)(0).io.ins(14)
    ssbs(0)(1).io.outs(44) <> ssbs(0)(0).io.ins(15)
    ssbs(0)(1).io.outs(45) <> ssbs(0)(0).io.ins(16)
    ssbs(0)(1).io.outs(46) <> ssbs(0)(0).io.ins(17)
    ssbs(0)(1).io.outs(47) <> ssbs(0)(0).io.ins(18)
    ssbs(0)(1).io.outs(48) <> ssbs(0)(0).io.ins(19)
    ssbs(0)(2).io.outs(0) <> scus(0)(2).io.scalarIn(0)
    ssbs(0)(2).io.outs(1) <> scus(0)(2).io.scalarIn(1)
    ssbs(0)(2).io.outs(2) <> scus(0)(2).io.scalarIn(2)
    ssbs(0)(2).io.outs(3) <> scus(0)(2).io.scalarIn(3)
    ssbs(0)(2).io.outs(4) <> scus(0)(2).io.scalarIn(4)
    ssbs(0)(2).io.outs(5) <> scus(0)(2).io.scalarIn(5)
    ssbs(0)(2).io.outs(6) <> mcs(0)(2).io.plasticine.scalarIn(2)
    ssbs(0)(2).io.outs(7) <> argOutMuxIns(0)(3)
    ssbs(0)(2).io.outs(7) <> argOutMuxIns(1)(3)
    ssbs(0)(2).io.outs(7) <> argOutMuxIns(2)(3)
    ssbs(0)(2).io.outs(8) <> ssbs(1)(2).io.ins(0)
    ssbs(0)(2).io.outs(9) <> ssbs(1)(2).io.ins(1)
    ssbs(0)(2).io.outs(10) <> ssbs(1)(2).io.ins(2)
    ssbs(0)(2).io.outs(11) <> ssbs(1)(2).io.ins(3)
    ssbs(0)(2).io.outs(12) <> lcus(0)(2).io.scalarIn(0)
    ssbs(0)(2).io.outs(13) <> lcus(0)(2).io.scalarIn(1)
    ssbs(0)(2).io.outs(14) <> lcus(0)(2).io.scalarIn(2)
    ssbs(0)(2).io.outs(15) <> lcus(0)(2).io.scalarIn(3)
    ssbs(0)(2).io.outs(16) <> cus(0)(1).io.scalarIn(0)
    ssbs(0)(2).io.outs(17) <> ssbs(0)(1).io.ins(4)
    ssbs(0)(2).io.outs(18) <> ssbs(0)(1).io.ins(5)
    ssbs(0)(2).io.outs(19) <> ssbs(0)(1).io.ins(6)
    ssbs(0)(2).io.outs(20) <> ssbs(0)(1).io.ins(7)
    ssbs(0)(2).io.outs(21) <> ssbs(0)(1).io.ins(8)
    ssbs(0)(2).io.outs(22) <> ssbs(0)(1).io.ins(9)
    ssbs(0)(2).io.outs(23) <> ssbs(0)(1).io.ins(10)
    ssbs(0)(2).io.outs(24) <> ssbs(0)(1).io.ins(11)
    ssbs(0)(2).io.outs(25) <> ssbs(0)(1).io.ins(12)
    ssbs(0)(2).io.outs(26) <> ssbs(0)(1).io.ins(13)
    ssbs(0)(2).io.outs(27) <> ssbs(0)(1).io.ins(14)
    ssbs(0)(2).io.outs(28) <> ssbs(0)(1).io.ins(15)
    ssbs(0)(2).io.outs(29) <> ssbs(0)(1).io.ins(16)
    ssbs(0)(2).io.outs(30) <> ssbs(0)(1).io.ins(17)
    ssbs(0)(2).io.outs(31) <> ssbs(0)(1).io.ins(18)
    ssbs(0)(2).io.outs(32) <> ssbs(0)(1).io.ins(19)
    ssbs(1)(0).io.outs(0) <> ssbs(0)(0).io.ins(21)
    ssbs(1)(0).io.outs(1) <> ssbs(0)(0).io.ins(22)
    ssbs(1)(0).io.outs(2) <> ssbs(0)(0).io.ins(23)
    ssbs(1)(0).io.outs(3) <> ssbs(0)(0).io.ins(24)
    ssbs(1)(0).io.outs(4) <> cus(0)(0).io.scalarIn(2)
    ssbs(1)(0).io.outs(5) <> ssbs(1)(1).io.ins(19)
    ssbs(1)(0).io.outs(6) <> ssbs(1)(1).io.ins(20)
    ssbs(1)(0).io.outs(7) <> ssbs(1)(1).io.ins(21)
    ssbs(1)(0).io.outs(8) <> ssbs(1)(1).io.ins(22)
    ssbs(1)(0).io.outs(9) <> ssbs(1)(1).io.ins(23)
    ssbs(1)(0).io.outs(10) <> ssbs(1)(1).io.ins(24)
    ssbs(1)(0).io.outs(11) <> ssbs(1)(1).io.ins(25)
    ssbs(1)(0).io.outs(12) <> ssbs(1)(1).io.ins(26)
    ssbs(1)(0).io.outs(13) <> cus(1)(0).io.scalarIn(3)
    ssbs(1)(0).io.outs(14) <> ssbs(2)(0).io.ins(0)
    ssbs(1)(0).io.outs(15) <> ssbs(2)(0).io.ins(1)
    ssbs(1)(0).io.outs(16) <> ssbs(2)(0).io.ins(2)
    ssbs(1)(0).io.outs(17) <> ssbs(2)(0).io.ins(3)
    ssbs(1)(0).io.outs(18) <> lcus(1)(0).io.scalarIn(0)
    ssbs(1)(0).io.outs(19) <> lcus(1)(0).io.scalarIn(1)
    ssbs(1)(0).io.outs(20) <> lcus(1)(0).io.scalarIn(2)
    ssbs(1)(0).io.outs(21) <> lcus(1)(0).io.scalarIn(3)
    ssbs(1)(0).io.outs(22) <> argOutMuxIns(0)(1)
  }
}
