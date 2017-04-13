package plasticine.spade
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

trait GeneratedParams1 {
  self:PlasticineParams =>
//  import plasticineParams._

  def doIt() {
    cuParams(0)(0) = GeneratedPMUParams(4, 4, 4, 4, 8, 4)
    cuParams(0)(1) = GeneratedPCUParams(4, 4, 4, 4, 8, 4)
    cuParams(1)(0) = GeneratedPCUParams(4, 4, 4, 4, 8, 4)
    cuParams(1)(1) = GeneratedPMUParams(4, 4, 4, 4, 8, 4)
    vectorSwitchParams(0)(0) = VectorSwitchParams(numIns=9, numOuts=9, v=16, w=32)
    scalarSwitchParams(0)(0) = ScalarSwitchParams(numIns=14, numOuts=14, w=32)
    controlSwitchParams(0)(0) = ControlSwitchParams(numIns=13, numOuts=19)
    vectorSwitchParams(0)(1) = VectorSwitchParams(numIns=14, numOuts=14, v=16, w=32)
    scalarSwitchParams(0)(1) = ScalarSwitchParams(numIns=16, numOuts=18, w=32)
    controlSwitchParams(0)(1) = ControlSwitchParams(numIns=17, numOuts=24)
    vectorSwitchParams(0)(2) = VectorSwitchParams(numIns=9, numOuts=9, v=16, w=32)
    scalarSwitchParams(0)(2) = ScalarSwitchParams(numIns=14, numOuts=14, w=32)
    controlSwitchParams(0)(2) = ControlSwitchParams(numIns=13, numOuts=19)
    vectorSwitchParams(1)(0) = VectorSwitchParams(numIns=14, numOuts=14, v=16, w=32)
    scalarSwitchParams(1)(0) = ScalarSwitchParams(numIns=19, numOuts=19, w=32)
    controlSwitchParams(1)(0) = ControlSwitchParams(numIns=18, numOuts=25)
    vectorSwitchParams(1)(1) = VectorSwitchParams(numIns=20, numOuts=20, v=16, w=32)
    scalarSwitchParams(1)(1) = ScalarSwitchParams(numIns=22, numOuts=24, w=32)
    controlSwitchParams(1)(1) = ControlSwitchParams(numIns=23, numOuts=32)
    vectorSwitchParams(1)(2) = VectorSwitchParams(numIns=14, numOuts=14, v=16, w=32)
    scalarSwitchParams(1)(2) = ScalarSwitchParams(numIns=19, numOuts=19, w=32)
    controlSwitchParams(1)(2) = ControlSwitchParams(numIns=18, numOuts=25)
    vectorSwitchParams(2)(0) = VectorSwitchParams(numIns=9, numOuts=9, v=16, w=32)
    scalarSwitchParams(2)(0) = ScalarSwitchParams(numIns=14, numOuts=14, w=32)
    controlSwitchParams(2)(0) = ControlSwitchParams(numIns=13, numOuts=19)
    vectorSwitchParams(2)(1) = VectorSwitchParams(numIns=14, numOuts=14, v=16, w=32)
    scalarSwitchParams(2)(1) = ScalarSwitchParams(numIns=16, numOuts=18, w=32)
    controlSwitchParams(2)(1) = ControlSwitchParams(numIns=17, numOuts=24)
    vectorSwitchParams(2)(2) = VectorSwitchParams(numIns=9, numOuts=9, v=16, w=32)
    scalarSwitchParams(2)(2) = ScalarSwitchParams(numIns=14, numOuts=14, w=32)
    controlSwitchParams(2)(2) = ControlSwitchParams(numIns=13, numOuts=19)

  }
}
