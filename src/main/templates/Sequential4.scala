package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class Sequential4(val numInputs: Int) extends Module {
  val io = new Bundle {
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
    val numIter = UInt(INPUT, width=32)
    val stageEnable = Vec.fill(numInputs) { Bool(OUTPUT) }
    val stageDone = Vec.fill(numInputs) { Bool(INPUT) }
  }

  // 0: INIT, 1: RESET, 2..2+numInputs-1: stages, numInputs: DONE
  val initState = 0
  val resetState = 1
  val firstState = resetState + 1
  val doneState = firstState + numInputs
  val lastState = doneState - 1

  val stateFF = Module(new FF(32))
  stateFF.io.control.enable := Bool(true)
  stateFF.io.data.init := UInt(0)
  val state = stateFF.io.data.out

  // Counter for num iterations
  val maxFF = Module(new FF(32))
  maxFF.io.control.enable := io.enable
  maxFF.io.data.in := io.numIter
  val max = maxFF.io.data.out

  val ctr = Module(new Counter(32))
  ctr.io.control.enable := io.enable & io.stageDone(lastState-2)
  ctr.io.control.reset := (state === UInt(doneState))
  ctr.io.control.saturate := Bool(false)
  ctr.io.data.max := max
  ctr.io.data.stride := UInt(1)
  val iter = ctr.io.data.out

  // Next state logic
//  val nextStateMux = Module(new MuxNOH(numInputs+3, 1))
//  val states = Vec.tabulate(numInputs+3) { i => UInt(i) }
//  val muxSel = Vec.tabulate(numInputs+3) { i =>
//    if (i == initState) {  // INIT enable logic
//     ~io.enable | (io.enable & (state === UInt(doneState)))
//    } else if (i == resetState) { // RESET logic
//      io.enable & (state === UInt(initState))
//    } else if (i == firstState) { // First state
//      io.enable &
//          ((state === UInt(resetState)) |
//           ((state === UInt(lastState)) & io.stageDone.last & ~ctr.io.control.done))
//    } else if (i < 2+numInputs) { // Worker state logic
//      io.enable & (state === UInt(i-2-1)) & io.stageDone(i-2-1)
//    } else { // Done state logic
//      io.enable & (state === UInt(lastState)) & ctr.io.control.done
//    }
//  }
//  nextStateMux.io.ins := states
//  nextStateMux.io.sel := muxSel
  when(io.enable) {
    when(state === UInt(initState)) {
      stateFF.io.data.in := UInt(resetState)
    }.elsewhen (state === UInt(resetState)) {
      stateFF.io.data.in := UInt(firstState)
    }.elsewhen (state < UInt(lastState)) {
      when((state === UInt(2)) & io.stageDone(0)) {
        stateFF.io.data.in := UInt(3)
      }.elsewhen((state === UInt(3)) & io.stageDone(1)) {
        stateFF.io.data.in := UInt(4)
      }.elsewhen((state === UInt(4)) & io.stageDone(2)) {
        stateFF.io.data.in := UInt(5)
      }.elsewhen((state === UInt(5)) & io.stageDone(3)) {
        stateFF.io.data.in := UInt(6)
      }.otherwise {
        stateFF.io.data.in := state
      }
    }.elsewhen (state === UInt(lastState)) {
      when(io.stageDone(lastState-2)) {
        when(ctr.io.control.done) {
          stateFF.io.data.in := UInt(doneState)
        }.otherwise {
          stateFF.io.data.in := UInt(firstState)
        }
      }.otherwise {
        stateFF.io.data.in := state
      }

    }.elsewhen (state === UInt(doneState)) {
      stateFF.io.data.in := UInt(initState)
    }.otherwise {
      stateFF.io.data.in := state
    }
  }.otherwise {
    stateFF.io.data.in := UInt(initState)
  }
//  stateFF.io.data.in := nextStateMux.io.out

  // Output logic
  io.done := state === UInt(doneState)
  io.stageEnable.zipWithIndex.foreach { case (en, i) => en := (state === UInt(i+2)) }
}


class Sequential4Tests(c: Sequential4) extends PlasticineTester(c) {
  val numIter = 5
  val stageIterCount = List.tabulate(c.numInputs) { i => math.abs(rnd.nextInt) % 10 + 1}
  println(s"stageIterCount: $stageIterCount")

  def executeStage(s: Int) {
    val numCycles = stageIterCount(s)
    // println(s"[stage $s] Executing for $numCycles")
    step(numCycles)
    // println(s"[stage $s] Done")
    poke(c.io.stageDone(s), 1)
    step(1)
    poke(c.io.stageDone(s), 0)
  }

  def handleStageEnables = {
    val stageEnables = c.io.stageEnable.map { peek(_).toInt }
    val activeStage = stageEnables.indexOf(1)
    println(s"active stage $activeStage")
    if (activeStage != -1) executeStage(activeStage)
  }

  // Start
  poke(c.io.numIter, numIter)
  poke(c.io.enable, 1)

  var done = peek(c.io.done).toInt
  var numCycles = 0
  while ((done != 1) & (numCycles < 100)) {
    handleStageEnables
    done = peek(c.io.done).toInt
    step(1)
    numCycles += 1
  }
}


object Sequential4Test {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val numInputs = 5
    chiselMainTest(chiselArgs, () => Module(new Sequential4(numInputs))) {
      c => new Sequential4Tests(c)
    }
  }
}
