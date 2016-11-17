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
  //    Sequential m1 {
  //      TileLoad() tl1 Ctr
  //      TileLoad() tl2 Ctr
  //      TileLoad() tl3 Ctr
  //      Sequential m2 {
  //        Pipe
  //        TileLoad() tl4 ctr
  //        TileLoad() tl5 ctr
  //        Pipe
  //        Pipe
  //        Gather
  //        Pipe
  //        Pipe
  //      }
  //      tileSt
  //    }
  //  }

  val m1 = Module(new Metapipe(3))
  m1.io.numIter := UInt(10)
  m1.io.enable := io.enable
  io.done := m1.io.done

  /** To be replaced with actual tile load stuff that talks to the MU */
  val t1 = Module(new Counter(32))
  t1.io.control.saturate := Bool(false)
  t1.io.control.reset := Bool(false)
  t1.io.data.max := UInt(5)
  t1.io.data.stride := UInt(1)
  t1.io.control.enable := m1.io.stageEnable(0)
  m1.io.stageDone(0) := t1.io.control.done

  val t2 = Module(new Counter(32))
  t2.io.control.saturate := Bool(false)
  t2.io.control.reset := Bool(false)
  t2.io.data.max := UInt(5)
  t2.io.data.stride := UInt(1)
  t2.io.control.enable := m1.io.stageEnable(1)
  m1.io.stageDone(1) := t2.io.control.done

  val m2 = Module(new Metapipe(5))
  m2.io.numIter := UInt(10)
  m2.io.enable := m1.io.stageEnable(2)
  m1.io.stageDone(2) := m2.io.done

    val p1 = Module(new Counter(32))
    p1.io.control.saturate := Bool(false)
    p1.io.control.reset := Bool(false)
    p1.io.data.max := UInt(5)
    p1.io.data.stride := UInt(1)
    p1.io.control.enable := m2.io.stageEnable(0)
    m2.io.stageDone(0) := p1.io.control.done

    val p2 = Module(new Counter(32))
    p2.io.control.saturate := Bool(false)
    p2.io.control.reset := Bool(false)
    p2.io.data.max := UInt(5)
    p2.io.data.stride := UInt(1)
    p2.io.control.enable := m2.io.stageEnable(1)
    m2.io.stageDone(1) := p2.io.control.done

    val t3 = Module(new Counter(32))
    t3.io.control.saturate := Bool(false)
    t3.io.control.reset := Bool(false)
    t3.io.data.max := UInt(5)
    t3.io.data.stride := UInt(1)
    t3.io.control.enable := m2.io.stageEnable(2)
    m2.io.stageDone(2) := t3.io.control.done


    val p3 = Module(new Counter(32))
    p3.io.control.saturate := Bool(false)
    p3.io.control.reset := Bool(false)
    p3.io.data.max := UInt(8)
    p3.io.data.stride := UInt(1)
    p3.io.control.enable := m2.io.stageEnable(3)
    m2.io.stageDone(3) := p3.io.control.done

    val p4 = Module(new Counter(32))
    p4.io.control.saturate := Bool(false)
    p4.io.control.reset := Bool(false)
    p4.io.data.max := UInt(5)
    p4.io.data.stride := UInt(3)
    p4.io.control.enable := m2.io.stageEnable(4)
    m2.io.stageDone(4) := p4.io.control.done
}
class PagerankTests(c: Pagerank) extends Tester(c) {}
object PagerankTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    val numInputs = 2
    chiselMainTest(chiselArgs, () => Module(new Pagerank(numInputs))) {
      c => new PagerankTests(c)
    }
  }
}
