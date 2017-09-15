//package templates
//import plasticine.Globals
//
//import Chisel._
//
///**
// * Synchronizer: Synchronize multiple signals and emit a 1-bit pulse
// * only after all of them have gone high at least once
// */
//class Synchronizer(val numInputs: Int) extends Module {
//  val io = new Bundle {
//    val in = Vec.fill(numInputs) { Bool(INPUT) }
//    val out = Bool(OUTPUT)
//  }
//
//  val zero = UInt(0, width=1)
//
//  val ffs = List.fill(numInputs) {
//    val ff = Module(new FF(1))
//    ff.io.data.init := zero
//    ff.io.control.enable := Bool(true)
//    ff
//  }
//
//  val treeAnd = Bool()
//
//  ffs.zip(io.in) foreach { case(ff, i) =>
//    ff.io.data.in := Mux(treeAnd, zero, ff.io.data.out | i)
//  }
//
//  treeAnd := ffs.map {_.io.data.out}.reduce{_&_}
//  io.out := treeAnd
//}
//
///**
// * Synchronizer test harness
// */
//class SynchronizerTests(c: Synchronizer) extends Tester(c) {
//  val numInputs = c.numInputs
//
//  // All ones
//  c.io.in.foreach { case i => poke(i, 1)}
//  step(1)
//  expect(c.io.out, 1)
//  step(1)
//  expect(c.io.out, 0)
//  c.io.in.foreach { case i => poke(i, 0)}
//  step(1)
//
//  // Set individual wires independently
//  for (i <- 0 until numInputs) {
//    poke(c.io.in(i), 1)
//    step(1)
//    poke(c.io.in(i), 0)
//    if (i == numInputs-1) expect (c.io.out, 1) else expect(c.io.out, 0)
//  }
//  step(1)
//  expect(c.io.out, 0)
//}
//
//object SynchronizerTest {
//  def main(args: Array[String]): Unit = {
//    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))
//
//    chiselMainTest(chiselArgs, () => Module(new Synchronizer(4))) {
//      c => new SynchronizerTests(c)
//    }
//  }
//}
