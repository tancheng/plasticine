package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer
import plasticine.spade._

//trait GeneratedPCUParams(numScalarIn:Int, numScalarOut:Int, numVectorIn:Int, numVectorOut:Int, numControlIn:Int, numControlOut:Int) extends PCUParams {
//  val w = 32
//  val v = 16
//  val numCounters = 8
//  val numUDCs = 5
//  val regColors = ListBuffer[List[RegColor]]()
//  regColors += List(CounterReg,ReduceReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(ScalarInReg,ScalarOutReg)
//  regColors += List(ScalarInReg,ScalarOutReg)
//  regColors += List(ScalarInReg,ScalarOutReg)
//  regColors += List(ScalarInReg,ScalarOutReg)
//  regColors += List(VecInReg,VecOutReg)
//  regColors += List(VecInReg,VecOutReg)
//  regColors += List(VecInReg,VecOutReg)
//  regColors += List(VecInReg,VecOutReg)
//  val d = 20
//  val r = regColors.size
//}
//
//case class GeneratedPMUParams(numScalarIn:Int, numScalarOut:Int, numVectorIn:Int, numVectorOut:Int, numControlIn:Int, numControlOut:Int) extends PMUParams {
//  val w = 32
//  val v = 16
//  val numCounters = 8
//  val numUDCs = 0
//  val regColors = ListBuffer[List[RegColor]]()
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg)
//  regColors += List(CounterReg,ReadAddrReg)
//  regColors += List(ScalarInReg,WriteAddrReg)
//  regColors += List(ScalarInReg)
//  regColors += List(ScalarInReg)
//  regColors += List(ScalarInReg)
//  regColors += List(VecInReg)
//  regColors += List(VecInReg)
//  regColors += List(VecInReg)
//  regColors += List(VecInReg)
//  val d = 6
//  val wd = 3
//  val r = regColors.size
//}
//
trait PlasticineArch extends PlasticineArch0 with PlasticineArch1 with PlasticineArch2 with PlasticineArch3 with PlasticineArch4 with PlasticineArch5 {
  self:Plasticine =>
  val cus = Array.fill(2)(Array.ofDim[CU](2))
  val csbs = Array.fill(3)(Array.ofDim[ControlSwitch](3))
  val vsbs = Array.fill(3)(Array.ofDim[VectorSwitch](3))
  val ssbs = Array.fill(3)(Array.ofDim[ScalarSwitch](3))
}
//trait GeneratedFringeParams {
//  self:FringeParams =>
//  val numArgIns = 3
//  val numArgOuts = 3
//  val numArgOutSelections = List(6,6,6)
//}
