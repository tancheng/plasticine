package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import plasticine.templates.MuxN

trait PlasticineArch2 extends PlasticineArch1{
  def connect2(io:PlasticineIO, argOutMuxes:List[MuxN], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]]):Unit = {
    vsbs(1)(2).io.outs(8) <> cus(1)(1).io.vecIn(0)
    vsbs(1)(2).io.outs(9) <> vsbs(1)(1).io.ins(5)
    vsbs(1)(2).io.outs(10) <> vsbs(1)(1).io.ins(6)
    vsbs(1)(2).io.outs(11) <> vsbs(1)(1).io.ins(7)
    vsbs(1)(2).io.outs(12) <> vsbs(1)(1).io.ins(8)
    vsbs(1)(2).io.outs(13) <> cus(0)(1).io.vecIn(1)
    vsbs(2)(0).io.outs(0) <> vsbs(1)(0).io.ins(10)
    vsbs(2)(0).io.outs(1) <> vsbs(1)(0).io.ins(11)
    vsbs(2)(0).io.outs(2) <> vsbs(1)(0).io.ins(12)
    vsbs(2)(0).io.outs(3) <> vsbs(1)(0).io.ins(13)
    vsbs(2)(0).io.outs(4) <> cus(1)(0).io.vecIn(2)
    vsbs(2)(0).io.outs(5) <> vsbs(2)(1).io.ins(9)
    vsbs(2)(0).io.outs(6) <> vsbs(2)(1).io.ins(10)
    vsbs(2)(0).io.outs(7) <> vsbs(2)(1).io.ins(11)
    vsbs(2)(0).io.outs(8) <> vsbs(2)(1).io.ins(12)
    vsbs(2)(1).io.outs(0) <> vsbs(1)(1).io.ins(10)
    vsbs(2)(1).io.outs(1) <> vsbs(1)(1).io.ins(11)
    vsbs(2)(1).io.outs(2) <> vsbs(1)(1).io.ins(12)
    vsbs(2)(1).io.outs(3) <> vsbs(1)(1).io.ins(13)
    vsbs(2)(1).io.outs(4) <> cus(1)(1).io.vecIn(2)
    vsbs(2)(1).io.outs(5) <> vsbs(2)(2).io.ins(4)
    vsbs(2)(1).io.outs(6) <> vsbs(2)(2).io.ins(5)
    vsbs(2)(1).io.outs(7) <> vsbs(2)(2).io.ins(6)
    vsbs(2)(1).io.outs(8) <> vsbs(2)(2).io.ins(7)
    vsbs(2)(1).io.outs(9) <> vsbs(2)(0).io.ins(5)
    vsbs(2)(1).io.outs(10) <> vsbs(2)(0).io.ins(6)
    vsbs(2)(1).io.outs(11) <> vsbs(2)(0).io.ins(7)
    vsbs(2)(1).io.outs(12) <> vsbs(2)(0).io.ins(8)
    vsbs(2)(1).io.outs(13) <> cus(1)(0).io.vecIn(1)
    vsbs(2)(2).io.outs(0) <> vsbs(1)(2).io.ins(4)
    vsbs(2)(2).io.outs(1) <> vsbs(1)(2).io.ins(5)
    vsbs(2)(2).io.outs(2) <> vsbs(1)(2).io.ins(6)
    vsbs(2)(2).io.outs(3) <> vsbs(1)(2).io.ins(7)
    vsbs(2)(2).io.outs(4) <> vsbs(2)(1).io.ins(5)
    vsbs(2)(2).io.outs(5) <> vsbs(2)(1).io.ins(6)
    vsbs(2)(2).io.outs(6) <> vsbs(2)(1).io.ins(7)
    vsbs(2)(2).io.outs(7) <> vsbs(2)(1).io.ins(8)
    vsbs(2)(2).io.outs(8) <> cus(1)(1).io.vecIn(1)
    // ScalarNetwork Connection
    io.argIns(0) <> ssbs(0)(0).io.ins(11).bits
    io.argIns(0) <> ssbs(1)(0).io.ins(16).bits
    io.argIns(0) <> ssbs(2)(0).io.ins(11).bits
    io.argIns(0) <> ssbs(0)(2).io.ins(0).bits
    io.argIns(0) <> ssbs(1)(2).io.ins(4).bits
    io.argIns(0) <> ssbs(2)(2).io.ins(4).bits
    io.argIns(1) <> ssbs(0)(0).io.ins(12).bits
    io.argIns(1) <> ssbs(1)(0).io.ins(17).bits
    io.argIns(1) <> ssbs(2)(0).io.ins(12).bits
    io.argIns(1) <> ssbs(0)(2).io.ins(1).bits
    io.argIns(1) <> ssbs(1)(2).io.ins(5).bits
    io.argIns(1) <> ssbs(2)(2).io.ins(5).bits
    io.argIns(2) <> ssbs(0)(0).io.ins(13).bits
    io.argIns(2) <> ssbs(1)(0).io.ins(18).bits
    io.argIns(2) <> ssbs(2)(0).io.ins(13).bits
    io.argIns(2) <> ssbs(0)(2).io.ins(2).bits
    io.argIns(2) <> ssbs(1)(2).io.ins(6).bits
    io.argIns(2) <> ssbs(2)(2).io.ins(6).bits
    cus(0)(0).io.scalarOut(0) <> ssbs(0)(1).io.ins(11)
    cus(0)(0).io.scalarOut(1) <> ssbs(1)(1).io.ins(21)
    cus(0)(0).io.scalarOut(2) <> ssbs(1)(0).io.ins(4)
    cus(0)(0).io.scalarOut(3) <> ssbs(0)(0).io.ins(4)
    cus(1)(1).io.scalarOut(0) <> ssbs(1)(2).io.ins(13)
    cus(1)(1).io.scalarOut(1) <> ssbs(2)(2).io.ins(13)
    cus(1)(1).io.scalarOut(2) <> ssbs(2)(1).io.ins(4)
    cus(1)(1).io.scalarOut(3) <> ssbs(1)(1).io.ins(9)
    cus(0)(1).io.scalarOut(0) <> ssbs(0)(2).io.ins(9)
    cus(0)(1).io.scalarOut(1) <> ssbs(1)(2).io.ins(18)
    cus(0)(1).io.scalarOut(2) <> ssbs(1)(1).io.ins(4)
    cus(0)(1).io.scalarOut(3) <> ssbs(0)(1).io.ins(4)
    cus(1)(0).io.scalarOut(0) <> ssbs(1)(1).io.ins(16)
    cus(1)(0).io.scalarOut(1) <> ssbs(2)(1).io.ins(15)
    cus(1)(0).io.scalarOut(2) <> ssbs(2)(0).io.ins(4)
    cus(1)(0).io.scalarOut(3) <> ssbs(1)(0).io.ins(9)
    cus(0)(0).io.scalarOut(0) <> ssbs(0)(0).io.ins(9)
    cus(0)(0).io.scalarOut(1) <> ssbs(0)(0).io.ins(10)
    cus(0)(1).io.scalarOut(0) <> ssbs(0)(1).io.ins(9)
    cus(0)(1).io.scalarOut(1) <> ssbs(0)(1).io.ins(10)
//    cus(0)(2).io.scalarOut(0) <> ssbs(0)(2).io.ins(7)
//    cus(0)(2).io.scalarOut(1) <> ssbs(0)(2).io.ins(8)
    cus(1)(0).io.scalarOut(0) <> ssbs(1)(0).io.ins(14)
    cus(1)(0).io.scalarOut(1) <> ssbs(1)(0).io.ins(15)
    cus(1)(1).io.scalarOut(0) <> ssbs(1)(1).io.ins(14)
    cus(1)(1).io.scalarOut(1) <> ssbs(1)(1).io.ins(15)
//    cus(1)(2).io.scalarOut(0) <> ssbs(1)(2).io.ins(11)
//    cus(1)(2).io.scalarOut(1) <> ssbs(1)(2).io.ins(12)
//    cus(2)(0).io.scalarOut(0) <> ssbs(2)(0).io.ins(9)
//    cus(2)(0).io.scalarOut(1) <> ssbs(2)(0).io.ins(10)
//    cus(2)(1).io.scalarOut(0) <> ssbs(2)(1).io.ins(9)
//    cus(2)(1).io.scalarOut(1) <> ssbs(2)(1).io.ins(10)
//    cus(2)(2).io.scalarOut(0) <> ssbs(2)(2).io.ins(7)
//    cus(2)(2).io.scalarOut(1) <> ssbs(2)(2).io.ins(8)
  }
}
