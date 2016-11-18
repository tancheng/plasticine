package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class BFS(val numInputs: Int) extends Module {
  val io = new Bundle {
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
  }

  val tileSize = 23040
  val layers = 8

  //  Pipe top { // Container pipe
  //    Parallel par1 {
  //      TileLoad() tl1
  //      TileLoad() tl2
  //    }
  //    Sequential s1
  //      meta m1 {
  //        Pipe p3
  //        Pipe p4
  //        TileLoad() tl3
  //        Pipe p5
  //        Pipe p6
  //        Pipe p7 // accum
  //      }
  //      Pipe p8
  //      Pipe p9
  //      Scatter scat1 
  //      Pipe p10
  //    }
  //  }


  val top = Module(new Metapipe(2))
  top.io.numIter := UInt(1)
  top.io.enable := io.enable
  io.done := top.io.done

    val par1 = Module(new Parallel(2))
    par1.io.numIter := UInt(1)
    par1.io.enable := top.io.stageEnable(0)
    top.io.stageDone(0) := par1.io.done

      val tl1 = Module(new Counter(32))
      tl1.io.control.saturate := Bool(false)
      tl1.io.control.reset := Bool(false)
      tl1.io.data.max := UInt(tileSize)
      tl1.io.data.stride := UInt(1)
      tl1.io.control.enable := par1.io.stageEnable(0)
      par1.io.stageDone(0) := tl1.io.control.done

      val tl2 = Module(new Counter(32))
      tl2.io.control.saturate := Bool(false)
      tl2.io.control.reset := Bool(false)
      tl2.io.data.max := UInt(tileSize)
      tl2.io.data.stride := UInt(1)
      tl2.io.control.enable := par1.io.stageEnable(1)
      par1.io.stageDone(1) := tl2.io.control.done

    val s1 = Module(new Sequential(5))
    s1.io.numIter := UInt(layers)
    s1.io.enable := top1.io.stageEnable(1)
    top1.io.stageDone(1) := s1.io.done

      val m1 = Module(new Metapipe(6))
      m1.io.numIter := UInt(64) // data-dependent
      m1.io.enable := s1.io.stageEnable(0)
      s1.io.stageDone := m1.io.done

        val p3 = Module(new Counter(32))
        p3.io.control.saturate := Bool(false)
        p3.io.control.reset := Bool(false)
        p3.io.data.max := UInt(1)
        p3.io.data.stride := UInt(1)
        p3.io.control.enable := m1.io.stageEnable(0)
        m1.io.stageDone(0) := p3.io.control.done

        val p4 = Module(new Counter(32))
        p4.io.control.saturate := Bool(false)
        p4.io.control.reset := Bool(false)
        p4.io.data.max := UInt(1)
        p4.io.data.stride := UInt(1)
        p4.io.control.enable := m1.io.stageEnable(1)
        m1.io.stageDone(1) := p4.io.control.done

        val tl3 = Module(new Counter(32))
        tl3.io.control.saturate := Bool(false)
        tl3.io.control.reset := Bool(false)
        tl3.io.data.max := UInt(64) // data-depedent
        tl3.io.data.stride := UInt(1)
        tl3.io.control.enable := m1.io.stageEnable(2)
        m1.io.stageDone(2) := tl3.io.control.done

        val p5 = Module(new Counter(32))
        p5.io.control.saturate := Bool(false)
        p5.io.control.reset := Bool(false)
        p5.io.data.max := UInt(64) // data-dependent
        p5.io.data.stride := UInt(1)
        p5.io.control.enable := m1.io.stageEnable(3)
        m1.io.stageDone(3) := p5.io.control.done

        val p6 = Module(new Counter(32))
        p6.io.control.saturate := Bool(false)
        p6.io.control.reset := Bool(false)
        p6.io.data.max := UInt(64) // data-dependent
        p6.io.data.stride := UInt(1)
        p6.io.control.enable := m1.io.stageEnable(4)
        m1.io.stageDone(4) := p6.io.control.done

        val p7 = Module(new Counter(32))
        p7.io.control.saturate := Bool(false)
        p7.io.control.reset := Bool(false)
        p7.io.data.max := UInt(64) // data-dependent
        p7.io.data.stride := UInt(1)
        p7.io.control.enable := m1.io.stageEnable(5)
        m1.io.stageDone(5) := p7.io.control.done

      val p8 = Module(new Counter(32))
      p8.io.control.saturate := Bool(false)
      p8.io.control.reset := Bool(false)
      p8.io.data.max := UInt(64) // data-dependent
      p8.io.data.stride := UInt(1)
      p8.io.control.enable := s1.io.stageEnable(1)
      s1.io.stageDone(1) := p8.io.control.done

      val p9 = Module(new Counter(32))
      p9.io.control.saturate := Bool(false)
      p9.io.control.reset := Bool(false)
      p9.io.data.max := UInt(64) // data-dependent
      p9.io.data.stride := UInt(1)
      p9.io.control.enable := s1.io.stageEnable(2)
      s1.io.stageDone(2) := p9.io.control.done

      val scat1 = Module(new Counter(32))
      scat1.io.control.saturate := Bool(false)
      scat1.io.control.reset := Bool(false)
      scat1.io.data.max := UInt(64) // scatter
      scat1.io.data.stride := UInt(1)
      scat1.io.control.enable := s1.io.stageEnable(3)
      s1.io.stageDone(3) := scat1.io.control.done

      val p10 = Module(new Counter(32))
      p10.io.control.saturate := Bool(false)
      p10.io.control.reset := Bool(false)
      p10.io.data.max := UInt(1)
      p10.io.data.stride := UInt(1)
      p10.io.control.enable := s1.io.stageEnable(4)
      s1.io.stageDone(4) := p10.io.control.done

}