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

  val iters = 10
  val tileSize = 768
  val NP = 7680
  val numEdges = 16

  // Instantiate memory controller
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 16
  val burstSizeBytes = 64
  val startDelayWidth = 4
  val endDelayWidth = 4
  val numCounters = 6
  val isWr = 0
  val scatterGather = 0
  val config = MemoryUnitConfig(scatterGather, isWr, CounterChainConfig.zeroes(numCounters), CUControlBoxConfig.zeroes(numCounters, numCounters, numCounters))

  val mc = Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts,
      burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))

  //  Sequential s1 {
  //    Sequential s2 {
  //      TileLoad() tl1 Ctr
  //      TileLoad() tl2 Ctr
  //      TileLoad() tl3 Ctr
  //      Sequential s3 {
  //        Pipe p1
  //        Parallel par1
  //          TileLoad() tl4 ctr
  //          TileLoad() tl5 ctr
  //        Sequential s4 {
  //          Pipe p2
  //          Pipe p3
  //          Pipe p4
  //          Pipe p5
  //        }
  //        Pipe p6
  //        Gather g1
  //        Pipe p7 // Reduce
  //        Pipe p8
  //      }
  //      tileSt ts1
  //    }
  //  }

  val s1 = Module(new Sequential(1))
  s1.io.numIter := UInt(iters)
  s1.io.enable := io.enable
  io.done := s1.io.done

    val s2 = Module(new Sequential(5))
    s2.io.numIter := UInt(NP/tileSize)
    s2.io.enable := s1.io.stageEnable(0)
    s1.io.stageDone(0) := s2.io.done

//      val tl1 = Module(new Counter(32))
//      tl1.io.control.saturate := Bool(false)
//      tl1.io.control.reset := Bool(false)
//      tl1.io.data.max := UInt(tileSize)
//      tl1.io.data.stride := UInt(1)
//      tl1.io.control.enable := s2.io.stageEnable(0)
//      s2.io.stageDone(0) := tl1.io.control.done


      // Instantiate tile loader, connect it to MC
      val tl1 = Module(new TileLoad(w, v))
      mc.io.interconnects(0).addr(0) := tl1.io.mc.addr
      mc.io.interconnects(0).size := tl1.io.mc.size
      mc.io.interconnects(0).vldIn := tl1.io.mc.addrValid
      tl1.io.mc.data := mc.io.interconnects(0).rdata
      tl1.io.mc.dataValid := mc.io.interconnects(0).receivedCtrWrap
      tl1.io.mc.ready := mc.io.interconnects(0).rdyOut

      tl1.io.rows := UInt(1)
      tl1.io.addr := UInt(0x1000)
      tl1.io.size := UInt(tileSize)
      tl1.io.colsize := UInt(1)

      tl1.io.enable := s2.io.stageEnable(0)
      s2.io.stageDone(0) := tl1.io.done


      val tl2 = Module(new Counter(32))
      tl2.io.control.saturate := Bool(false)
      tl2.io.control.reset := Bool(false)
      tl2.io.data.max := UInt(tileSize)
      tl2.io.data.stride := UInt(1)
      tl2.io.control.enable := s2.io.stageEnable(1)
      s2.io.stageDone(1) := tl2.io.control.done

      val tl3 = Module(new Counter(32))
      tl3.io.control.saturate := Bool(false)
      tl3.io.control.reset := Bool(false)
      tl3.io.data.max := UInt(tileSize)
      tl3.io.data.stride := UInt(1)
      tl3.io.control.enable := s2.io.stageEnable(2)
      s2.io.stageDone(2) := tl3.io.control.done

      val s3 = Module(new Sequential(7))
      s3.io.numIter := UInt(tileSize)
      s3.io.enable := s2.io.stageEnable(3)
      s2.io.stageDone(3) := s3.io.done

        val p1 = Module(new Counter(32))
        p1.io.control.saturate := Bool(false)
        p1.io.control.reset := Bool(false)
        p1.io.data.max := UInt(1)
        p1.io.data.stride := UInt(1)
        p1.io.control.enable := s3.io.stageEnable(0)
        s3.io.stageDone(0) := p1.io.control.done

        val par1 = Module(new Parallel(2))
        par1.io.enable := s3.io.stageEnable(1)
        s3.io.stageDone(1) := par1.io.done

          val tl5 = Module(new Counter(32))
          tl5.io.control.saturate := Bool(false)
          tl5.io.control.reset := Bool(false)
          tl5.io.data.max := UInt(numEdges) // data-dependent
          tl5.io.data.stride := UInt(1)
          tl5.io.control.enable := par1.io.stageEnable(0)
          par1.io.stageDone(0) := tl5.io.control.done

          val tl6 = Module(new Counter(32))
          tl6.io.control.saturate := Bool(false)
          tl6.io.control.reset := Bool(false)
          tl6.io.data.max := UInt(numEdges) // data-dependent
          tl6.io.data.stride := UInt(1)
          tl6.io.control.enable := s2.io.stageEnable(1)
          par1.io.stageDone(1) := tl6.io.control.done

        val s4 = Module(new Sequential(4))
        s4.io.numIter := UInt(numEdges) // data-dependent
        s4.io.enable := s3.io.stageEnable(2)
        s3.io.stageDone(2) := s4.io.done
        
          val p2 = Module(new Counter(32))
          p2.io.control.saturate := Bool(false)
          p2.io.control.reset := Bool(false)
          p2.io.data.max := UInt(1)
          p2.io.data.stride := UInt(1)
          p2.io.control.enable := s4.io.stageEnable(0)
          s4.io.stageDone(0) := p2.io.control.done

          val p3 = Module(new Counter(32))
          p3.io.control.saturate := Bool(false)
          p3.io.control.reset := Bool(false)
          p3.io.data.max := UInt(1)
          p3.io.data.stride := UInt(1)
          p3.io.control.enable := s4.io.stageEnable(1)
          s4.io.stageDone(1) := p3.io.control.done

          val p4 = Module(new Counter(32))
          p4.io.control.saturate := Bool(false)
          p4.io.control.reset := Bool(false)
          p4.io.data.max := UInt(1)
          p4.io.data.stride := UInt(1)
          p4.io.control.enable := s4.io.stageEnable(2)
          s4.io.stageDone(2) := p4.io.control.done

          val p5 = Module(new Counter(32))
          p5.io.control.saturate := Bool(false)
          p5.io.control.reset := Bool(false)
          p5.io.data.max := UInt(1)
          p5.io.data.stride := UInt(1)
          p5.io.control.enable := s4.io.stageEnable(3)
          s4.io.stageDone(3) := p5.io.control.done

        val p6 = Module(new Counter(32))
        p6.io.control.saturate := Bool(false)
        p6.io.control.reset := Bool(false)
        p6.io.data.max := UInt(numEdges) // data-dependent
        p6.io.data.stride := UInt(1)
        p6.io.control.enable := s3.io.stageEnable(3)
        s3.io.stageDone(3) := p6.io.control.done
        
        val g1 = Module(new Counter(32))
        g1.io.control.saturate := Bool(false)
        g1.io.control.reset := Bool(false)
        g1.io.data.max := UInt(64) // gather
        g1.io.data.stride := UInt(1)
        g1.io.control.enable := s3.io.stageEnable(4)
        s3.io.stageDone(4) := g1.io.control.done

        val p7 = Module(new Counter(32))
        p7.io.control.saturate := Bool(false)
        p7.io.control.reset := Bool(false)
        p7.io.data.max := UInt(numEdges) // data-dependent
        p7.io.data.stride := UInt(1)
        p7.io.control.enable := s3.io.stageEnable(5)
        s3.io.stageDone(5) := p7.io.control.done

        val p8 = Module(new Counter(32))
        p8.io.control.saturate := Bool(false)
        p8.io.control.reset := Bool(false)
        p8.io.data.max := UInt(1)
        p8.io.data.stride := UInt(1)
        p8.io.control.enable := s3.io.stageEnable(6)
        s3.io.stageDone(6) := p8.io.control.done
        
      val ts1 = Module(new Counter(32))
      ts1.io.control.saturate := Bool(false)
      ts1.io.control.reset := Bool(false)
      ts1.io.data.max := UInt(tileSize)
      ts1.io.data.stride := UInt(1)
      ts1.io.control.enable := s2.io.stageEnable(4)
      s2.io.stageDone(4) := ts1.io.control.done

}
class PagerankTests(c: Pagerank) extends Tester(c) {
  poke(c.io.enable, 1)
  var done = peek(c.io.done).toInt
  var cycles = 0

  def peekSeqential(seq: Sequential, str: String="") {
    val s1_en = peek(seq.io.enable)
    val s1_done = peek(seq.io.done)
    val s1_iter = peek(seq.iter)
    val max = peek(seq.io.numIter)
    val stageEnables = seq.io.stageEnable.map { peek(_).toInt }.mkString(" ")
    val stageDones = seq.io.stageDone.map { peek(_).toInt }.mkString(" ")
    val state = peek(seq.state).toInt
    println(s"[$cycles] [Sequential $str] (en = $stageEnables) (done = $stageDones) (state = $state) en = $s1_en, done = $s1_done, iter = $s1_iter (max $max)")
  }

  def peekVals {
    peekSeqential(c.s1, "S1")
    peekSeqential(c.s2, "S2")
    peekSeqential(c.s3, "S3")
    peekSeqential(c.s4, "S4")
  }

  while((cycles < 1000) & (done != 1)) {
    step(1)
    cycles += 1
    done = peek(c.io.done).toInt
    peekVals
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
