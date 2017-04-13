package plasticine.spade
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

object GeneratedTopParams extends TopParams { // with GeneratedParams {
  override val fringeParams = new FringeParams {
    override val numArgIns = 3
    override val numArgOuts = 3
    override val dataWidth = 32
  }
  val plasticineParams = new PlasticineParams with GeneratedParams1 {
    override val w = 32
    override val numRows = 2
    override val numCols = 2
    override val cuParams = Array.fill(2)(Array.ofDim[CUParams](2))
    val vectorSwitchParams = Array.fill(3)(Array.ofDim[VectorSwitchParams](3))
    val scalarSwitchParams = Array.fill(3)(Array.ofDim[ScalarSwitchParams](3))
    val controlSwitchParams = Array.fill(3)(Array.ofDim[ControlSwitchParams](3))
    val numArgOutSelections = List(6,6,6)

    doIt()
  }
}
case class GeneratedPCUParams(override val numScalarIn:Int, override val numScalarOut:Int, override val numVectorIn:Int, override val numVectorOut:Int, override val numControlIn:Int, override val numControlOut:Int) extends PCUParams {
  override val w = 32
  override val v = 16
  override val numCounters = 8
  override val numUDCs = 5
  regColors += List(CounterReg,ReduceReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(ScalarInReg,ScalarOutReg)
  regColors += List(ScalarInReg,ScalarOutReg)
  regColors += List(ScalarInReg,ScalarOutReg)
  regColors += List(ScalarInReg,ScalarOutReg)
  regColors += List(VecInReg,VecOutReg)
  regColors += List(VecInReg,VecOutReg)
  regColors += List(VecInReg,VecOutReg)
  regColors += List(VecInReg,VecOutReg)
  override val d = 20
  override val r = regColors.size
}

case class GeneratedPMUParams(override val numScalarIn:Int, override val numScalarOut:Int, override val numVectorIn:Int, override val numVectorOut:Int, override val numControlIn:Int, override val numControlOut:Int) extends PMUParams {
  override val w = 32
  override val v = 16
  override val numCounters = 8
  override val numUDCs = 0
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg)
  regColors += List(CounterReg,ReadAddrReg)
  regColors += List(ScalarInReg,WriteAddrReg)
  regColors += List(ScalarInReg)
  regColors += List(ScalarInReg)
  regColors += List(ScalarInReg)
  regColors += List(VecInReg)
  regColors += List(VecInReg)
  regColors += List(VecInReg)
  regColors += List(VecInReg)
  override val d = 6
  override val wd = 3
  override val r = regColors.size
}
//trait GeneratedParams extends GeneratedParams1 {
//  self:TopParams =>
//  import plasticineParams._
//}
