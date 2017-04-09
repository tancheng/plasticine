//package plasticine.templates
//
//import Chisel._
//
//class SwitchBox(val w: Int) extends Module {
//  val io = new Bundle {
//    val inorth = Bits(INPUT,  width = w)
//    val iwest = Bits(INPUT,  width = w)
//    val isouth = Bits(INPUT,  width = w)
//    val ieast = Bits(INPUT,  width = w)
//    val onorth = Bits(OUTPUT,  width = w)
//    val owest = Bits(OUTPUT,  width = w)
//    val osouth = Bits(OUTPUT,  width = w)
//    val oeast = Bits(OUTPUT,  width = w)
//  }
//
//  val config = new Bundle {
//    val north = UInt(0)
//    val west = UInt(0)
//    val south = UInt(0)
//    val east = UInt(0)
//  }
//
//  val nmux = Module(new MuxN(4,w))
//  nmux.io.ins(0) := io.inorth
//  nmux.io.ins(1) := io.iwest
//  nmux.io.ins(2) := io.isouth
//  nmux.io.ins(3) := io.ieast
//  nmux.io.sel := config.north
//  io.onorth := nmux.io.out
//
//  val wmux = Module(new MuxN(4,w))
//  wmux.io.ins(0) := io.inorth
//  wmux.io.ins(1) := io.iwest
//  wmux.io.ins(2) := io.isouth
//  wmux.io.ins(3) := io.ieast
//  wmux.io.sel := config.west
//  io.owest := wmux.io.out
//
//  val smux = Module(new MuxN(4,w))
//  smux.io.ins(0) := io.inorth
//  smux.io.ins(1) := io.iwest
//  smux.io.ins(2) := io.isouth
//  smux.io.ins(3) := io.ieast
//  smux.io.sel := config.south
//  io.osouth := smux.io.out
//
//  val emux = Module(new MuxN(4,w))
//  emux.io.ins(0) := io.inorth
//  emux.io.ins(1) := io.iwest
//  emux.io.ins(2) := io.isouth
//  emux.io.ins(3) := io.ieast
//  emux.io.sel := config.east
//  io.oeast := emux.io.out
//}
//
//class SwitchBoxTests(c: SwitchBox) extends Tester(c) {
//  for (i <- 0 until 16) {
//    val north = i
//    val west = rnd.nextInt(16)
//    val south = rnd.nextInt(16)
//    val east = rnd.nextInt(16)
//    val ex_onorth = north
//    val ex_owest = west
//    val ex_osouth = south
//    val ex_oeast = east
//    poke(c.io.inorth, north)
//    poke(c.io.iwest, west)
//    poke(c.io.isouth, south)
//    poke(c.io.ieast, east)
//    step(1)
//    expect(c.io.onorth, ex_onorth)
//    expect(c.io.owest, ex_onorth)
//    expect(c.io.osouth, ex_onorth)
//    expect(c.io.oeast, ex_onorth)
//  }
//}
//
//object SwitchBoxTest {
//  def main(args: Array[String]): Unit = {
//    chiselMainTest(args, () => Module(new SwitchBox(4))) {
//      c => new SwitchBoxTests(c)
//    }
//  }
//}
