package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class Metapipe(val numInputs: Int) extends Module {
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
  val fillState = resetState + 1
  val steadyState = fillState + numInputs - 1
  val drainState = steadyState + 1
  val doneState = drainState+numInputs-1
//  val lastState = doneState - 1

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
  ctr.io.control.enable := io.enable & io.stageDone(0)
  ctr.io.control.reset := (state === UInt(doneState))
  ctr.io.control.saturate := Bool(false)
  ctr.io.data.max := max
  ctr.io.data.stride := UInt(1)
  val iter = ctr.io.data.out

  val doneFF = List.tabulate(numInputs) { i =>
    val ff = Module(new FF(1))
    ff.io.control.enable := io.enable
    ff.io.data.init := UInt(0)
    ff.io.data.in := io.stageDone(i)
    ff
  }

  val doneMask = doneFF.map { _.io.data.out }

  def clearDone = doneFF.foreach { _.io.data.in := UInt(0) }

  when(io.enable) {
    when(state === UInt(initState)) {   // INIT -> RESET
      stateFF.io.data.in := UInt(resetState)
    }.elsewhen (state === UInt(resetState)) {  // RESET -> FILL
      stateFF.io.data.in := UInt(fillState)
    }.elsewhen (state < UInt(steadyState)) {  // FILL -> STEADY
      for ( i <- fillState until steadyState) {
        val fillStateID = i - fillState
        io.stageEnable.zip(doneMask).take(fillStateID+1).foreach { case (en, done) => en := ~done }
        io.stageEnable.drop(fillStateID+1).foreach { en => en := UInt(0) }

        val doneTree = io.stageDone.take(fillStateID+1).reduce {_&_}
        when ((state === UInt(i) & doneTree)) {
          stateFF.io.data.in := UInt(i+1)
          clearDone
        }.otherwise {
          stateFF.io.data.in := state
        }
      }
    }.elsewhen (state === UInt(steadyState)) {  // STEADY
      io.stageEnable.zip(doneMask).foreach { case (en, done) => en := ~done }

      val doneTree = io.stageDone.reduce {_&_}
      when(doneTree) {
        when(ctr.io.control.done) {
          stateFF.io.data.in := UInt(drainState)
        }.otherwise {
          stateFF.io.data.in := UInt(state)
        }
        clearDone
      }.otherwise {
        stateFF.io.data.in := state
      }
    }.elsewhen (state < UInt(doneState)) {   // DRAIN
      for ( i <- drainState until doneState) {
        val drainStateID = i - drainState
        io.stageEnable.zip(doneMask).takeRight(numInputs - drainStateID - 1).foreach { case (en, done) => en := ~done }
        io.stageEnable.dropRight(numInputs - drainStateID - 1).foreach { en => en := UInt(0) }

        val doneTree = io.stageDone.takeRight(numInputs - drainStateID - 1).reduce {_&_}
        when ((state === UInt(i) & doneTree)) {
          stateFF.io.data.in := UInt(i+1)
          clearDone
        }.otherwise {
          stateFF.io.data.in := state
        }
      }
    }.elsewhen (state === UInt(doneState)) {  // DONE
      stateFF.io.data.in := UInt(initState)
    }.otherwise {
      stateFF.io.data.in := state
    }
  }.otherwise {
    stateFF.io.data.in := UInt(initState)
  }

  // Output logic
  io.done := state === UInt(doneState)
}


class MetapipeTests(c: Metapipe) extends PlasticineTester(c) {
  val numIter = 5
  val stageIterCount = List.tabulate(c.numInputs) { i => math.abs(rnd.nextInt) % 10 }
  println(s"stageIterCount: $stageIterCount")

  def executeStage(s: Int) {
    val numCycles = stageIterCount(s)
    println(s"[stage $s] Executing for $numCycles")
    step(numCycles)
    println(s"[stage $s] Done")
    poke(c.io.stageDone(s), 1)
    step(1)
    poke(c.io.stageDone(s), 0)
  }

  def handleStageEnables = {
    val stageEnables = c.io.stageEnable.map { peek(_).toInt }
    val activeStage = stageEnables.zipWithIndex.filter { _._1 == 1 }.map { _._2 }
//    executeStage(activeStage)
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


object MetapipeTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val numInputs = 2
    chiselMainTest(chiselArgs, () => Module(new Metapipe(numInputs))) {
      c => new MetapipeTests(c)
    }
  }
}
