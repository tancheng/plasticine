package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class Parallel(val numInputs: Int) extends Module {
  val io = new Bundle {
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
    val stageEnable = Vec.fill(numInputs) { Bool(OUTPUT) }
    val stageDone = Vec.fill(numInputs) { Bool(INPUT) }
  }

  // 0: INIT, 1: RESET, 2..2+numInputs-1: stages, numInputs: DONE
  val initState = 0
  val steadyState = initState + 1
  val doneState = steadyState + 1

  val stateFF = Module(new FF(32))
  stateFF.io.control.enable := Bool(true)
  stateFF.io.data.init := UInt(0)
  val state = stateFF.io.data.out

  val doneClear = UInt(width=1)
  val doneFF = List.tabulate(numInputs) { i =>
    val ff = Module(new FF(1))
    ff.io.control.enable := io.stageDone(i) | doneClear
    ff.io.data.init := UInt(0)
    ff.io.data.in := Mux(io.stageDone(i), io.stageDone(i), UInt(0))
    ff
  }

  val doneMask = doneFF.map { _.io.data.out }

  // Provide default value for enable and doneClear
  io.stageEnable.foreach { _ := UInt(0) }
  doneClear := UInt(0)

  when(io.enable) {
    when(state === UInt(initState)) {   // INIT -> RESET
      stateFF.io.data.in := UInt(steadyState)
    }.elsewhen (state === UInt(steadyState)) {  // STEADY
      io.stageEnable.zip(doneMask).foreach { case (en, done) => en := ~done }

      val doneTree = Bool(doneMask.reduce {_&_})
      doneClear := doneTree
      when(doneTree) {
        stateFF.io.data.in := UInt(doneState)
      }.otherwise {
        stateFF.io.data.in := UInt(state)
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


class ParallelTests(c: Parallel) extends PlasticineTester(c) {
  val numIter = 5
  val stageIterCount = List.tabulate(c.numInputs) { i => math.abs(rnd.nextInt) % 10 }
  println(s"stageIterCount: $stageIterCount")

  def executeStages(s: List[Int]) {
    val numCycles = s.map { stageIterCount(_) }
    var elapsed = 0
    var done: Int = 0
    while (done != s.size) {
      c.io.stageDone.foreach { poke(_, 0) }
      step(1)
      elapsed += 1
      for (i <- 0 until s.size) {
        if (numCycles(i) == elapsed) {
          println(s"[Stage ${s(i)} Finished execution at $elapsed")
          poke(c.io.stageDone(s(i)), 1)
          done += 1
        }
      }
    }
    c.io.stageDone.foreach { poke(_, 1) }
  }

  def handleStageEnables = {
    val stageEnables = c.io.stageEnable.map { peek(_).toInt }
    val activeStage = stageEnables.zipWithIndex.filter { _._1 == 1 }.map { _._2 }
//    executeStage(activeStage)
  }

  // Start
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


object ParallelTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val numInputs = 2
    chiselMainTest(chiselArgs, () => Module(new Parallel(numInputs))) {
      c => new ParallelTests(c)
    }
  }
}
