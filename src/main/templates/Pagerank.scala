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
  //        Parallel()
  //        Pipe()
  //        Tilest() Ctr
  //        Pipe
  //        Pipe
  //      }
  //    }
  //    TileSt
  //  }

  val top = Module(new Sequential(2))
  top.io.enable := io.enable
  io.done := top.io.done

  val m1 = Module(new Metapipe(3))
  m1.io.enable := top.io.stageEnable(0)
  top.io.stageDone(0) := m1.io.done

  val m2 = Module(new Metapipe(5))
  m2.io.enable := top.io.stageEnable(1)
  top.io.stageDone(1) := m2.io.done

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
