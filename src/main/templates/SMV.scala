package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class SMV(val numInputs: Int) extends Module {
  val io = new Bundle {
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
  }

  val tileSize = 3840
  val N = 384000

  //  meta m1 {              
  //    TileLoad() tl1            
  //    Meta m2 { // Written as seq but can be meta        
  //      Pipe p1          
  //      Pipe p2       
  //      Parallel par1 {  
  //        TileLoad() tl2          
  //        TileLoad() tl3          
  //      }
  //      Gather g1          
  //      Pipe p3 // reduce          
  //      Pipe p4          
  //    }                             
  //    TileSt() ts1             
  //  }              
  //                 



  val m1 = Module(new Metapipe(3))
  m1.io.numIter := UInt(N/tileSize)
  m1.io.enable := io.enable
  io.done := m1.io.done

    val tl1 = Module(new Counter(32))
    tl1.io.control.saturate := Bool(false)
    tl1.io.control.reset := Bool(false)
    tl1.io.data.max := UInt(tileSize)
    tl1.io.data.stride := UInt(1)
    tl1.io.control.enable := m1.io.stageEnable(0)
    m1.io.stageDone(0) := tl1.io.control.done

    val m2 = Module(new Sequential(6))
    m2.io.numIter := UInt(tileSize)
    m2.io.enable := m1.io.stageEnable(1)
    m1.io.stageDone(1) := m2.io.done

      val p1 = Module(new Counter(32))
      p1.io.control.saturate := Bool(false)
      p1.io.control.reset := Bool(false)
      p1.io.data.max := UInt(1)
      p1.io.data.stride := UInt(1)
      p1.io.control.enable := m2.io.stageEnable(0)
      m2.io.stageDone(0) := p1.io.control.done

      val p2 = Module(new Counter(32))
      p2.io.control.saturate := Bool(false)
      p2.io.control.reset := Bool(false)
      p2.io.data.max := UInt(1)
      p2.io.data.stride := UInt(1)
      p2.io.control.enable := m2.io.stageEnable(1)
      m2.io.stageDone(1) := p2.io.control.done

      val par1 = Module(new Parallel(2))
      par1.io.enable := m2.io.stageEnable(2)
      m2.io.stageDone(2) := par1.io.done

        val tl2 = Module(new Counter(32))
        tl2.io.control.saturate := Bool(false)
        tl2.io.control.reset := Bool(false)
        tl2.io.data.max := UInt(64) // data-dependent
        tl2.io.data.stride := UInt(1)
        tl2.io.control.enable := par1.io.stageEnable(0)
        par1.io.stageDone(0) := tl2.io.control.done

        val tl3 = Module(new Counter(32))
        tl3.io.control.saturate := Bool(false)
        tl3.io.control.reset := Bool(false)
        tl3.io.data.max := UInt(64) // data-dependent
        tl3.io.data.stride := UInt(1)
        tl3.io.control.enable := par1.io.stageEnable(1)
        par1.io.stageDone(1) := tl3.io.control.done

      val g1 = Module(new Counter(32))
      g1.io.control.saturate := Bool(false)
      g1.io.control.reset := Bool(false)
      g1.io.data.max := UInt(64) // gather
      g1.io.data.stride := UInt(1)
      g1.io.control.enable := m2.io.stageEnable(3)
      m2.io.stageDone(3) := g1.io.control.done

      val p3 = Module(new Counter(32))
      p3.io.control.saturate := Bool(false)
      p3.io.control.reset := Bool(false)
      p3.io.data.max := UInt(64) // data-dependent
      p3.io.data.stride := UInt(1)
      p3.io.control.enable := m2.io.stageEnable(4)
      m2.io.stageDone(4) := p3.io.control.done

      val p4 = Module(new Counter(32))
      p4.io.control.saturate := Bool(false)
      p4.io.control.reset := Bool(false)
      p4.io.data.max := UInt(1)
      p4.io.data.stride := UInt(1)
      p4.io.control.enable := m2.io.stageEnable(5)
      m2.io.stageDone(5) := p4.io.control.done

    val ts1 = Module(new Counter(32))
    ts1.io.control.saturate := Bool(false)
    ts1.io.control.reset := Bool(false)
    ts1.io.data.max := UInt(tileSize)
    ts1.io.data.stride := UInt(1)
    ts1.io.control.enable := m1.io.stageEnable(1)
    m1.io.stageDone(1) := ts1.io.control.done


}
