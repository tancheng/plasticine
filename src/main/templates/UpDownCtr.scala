package plasticine.templates
import plasticine.Globals

import Chisel._

/**
 * UpDownCtr: 1-dimensional counter. Counts upto 'max', each time incrementing
 * by 'stride', beginning at zero.
 * @param w: Word width
 */
class UpDownCtr(val w: Int) extends Module {
  val io = new Bundle {
    val initval  = UInt(INPUT, w)
    val max      = UInt(INPUT, w)
    val strideInc   = UInt(INPUT, w)
    val strideDec   = UInt(INPUT, w)
    val initAtConfig     = Bool(INPUT)
    val init     = Bool(INPUT)
    val inc      = Bool(INPUT)
    val dec      = Bool(INPUT)
    val gtz      = Bool(OUTPUT)  // greater-than-zero
    val isMax    = Bool(OUTPUT)
    val out      = UInt(OUTPUT, w)
  }

//  val reg = Module(new FF(w))
  val reg = if (Globals.noModule) new FFL(w) else Module(new FF(w))
  val configInit = Mux(io.initAtConfig, io.initval, UInt(0, width = w))
  reg.io.data.init := configInit

  // If inc and dec go high at the same time, the counter
  // should be unaffected. Catch that with an xor
//  reg.io.control.enable := (io.inc ^ io.dec) | io.init
  reg.io.control.enable := io.inc | io.dec | io.init

  val incval = reg.io.data.out + UInt(io.strideInc)
  val decval = reg.io.data.out - UInt(io.strideDec)

  io.isMax := incval > io.max

  // Default value
  reg.io.data.in := reg.io.data.out

  when(io.init) {
    reg.io.data.in := io.initval
  }.elsewhen (io.inc & io.dec) {
    reg.io.data.in := reg.io.data.out + io.strideInc - io.strideDec
  }.elsewhen (io.inc & ~io.dec) {
    reg.io.data.in := incval
  }.elsewhen (~io.inc & io.dec) {
    reg.io.data.in := decval
  }.otherwise {
    reg.io.data.in := reg.io.data.out
  }

//  reg.io.data.in := Mux (io.init, io.initval, Mux(io.incMux(io.inc, incval, decval))
  io.gtz := (reg.io.data.out > UInt(0))
  io.out := reg.io.data.out
}

/**
 * UpDownCtr test harness
 */
class UpDownCtrTests(c: UpDownCtr) extends Tester(c) {
  poke(c.io.initval, 2)
  poke(c.io.init, 1)
  step(1)
  expect(c.io.gtz, 1)
  poke(c.io.init, 0)

  poke(c.io.dec, 1)
  step(1) // 1
  expect(c.io.gtz, 1)
  step(1)  // 0
  expect(c.io.gtz, 0)

  poke(c.io.inc, 1) // both inc and dec = 1, unchanged
  step(1)
  expect(c.io.gtz, 0)
  poke(c.io.dec, 0)
  step(1)  // 1
  expect(c.io.gtz, 1)
}

object UpDownCtrTest {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args, () => Module(new UpDownCtr(4))) {
      c => new UpDownCtrTests(c)
    }
  }
}
