package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class Pagerank(val numInputs: Int) extends Module {
  val io = new Bundle {
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
  }

  //  Sequential top {
  //    Metapipe m1 {
  //      TileLoad() Ctr
  //      TileLoad() Ctr
  //      Metapipe m2 {
  //        Parallel p1()
  //        Pipe p2()
  //        Tilest() Ctr
  //        Pipe p3
  //        Pipe p4
  //      }
  //    }
  //    TileSt
  //  }

//  val top = Module(new Sequential(2))
//  top.io.numIter := UInt(10)
//  top.io.enable := io.enable
//  io.done := top.io.done

//  val m1 = Module(new Metapipe(3))
//  m1.io.enable := top.io.stageEnable(0)
//  top.io.stageDone(0) := m1.io.done

//  val m2 = Module(new Metapipe(5))
//  m2.io.enable := top.io.stageEnable(1)
//  top.io.stageDone(1) := m2.io.done

  val m1 = Module(new Metapipe(3))
  m1.io.numIter := UInt(10)
  m1.io.enable := io.enable
  io.done := m1.io.done

  val p2 = Module(new Counter(32))
  p2.io.control.saturate := Bool(false)
  p2.io.control.reset := Bool(false)
  p2.io.data.max := UInt(5)
  p2.io.data.stride := UInt(1)
  p2.io.control.enable := m1.io.stageEnable(0)
  m1.io.stageDone(0) := p2.io.control.done

  val p3 = Module(new Counter(32))
  p3.io.control.saturate := Bool(false)
  p3.io.control.reset := Bool(false)
  p3.io.data.max := UInt(8)
  p3.io.data.stride := UInt(1)
  p3.io.control.enable := m1.io.stageEnable(1)
  m1.io.stageDone(1) := p3.io.control.done

  val p4 = Module(new Counter(32))
  p4.io.control.saturate := Bool(false)
  p4.io.control.reset := Bool(false)
  p4.io.data.max := UInt(5)
  p4.io.data.stride := UInt(3)
  p4.io.control.enable := m1.io.stageEnable(2)
  m1.io.stageDone(2) := p4.io.control.done
}


class PagerankTests(c: Pagerank) extends PlasticineTester(c) {
  val maxCycles = 100
  var done = 0
  var elapsed = 0

  poke(c.io.enable, 1)
  while ((done != 1) & elapsed < maxCycles) {
    step(1)
    elapsed += 1
    done = peek(c.io.done).toInt
  }

}


object PagerankTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val numInputs = 2
    chiselMainTest(chiselArgs, () => Module(new Pagerank(numInputs))) {
      c => new PagerankTests(c)
    }
  }
}
