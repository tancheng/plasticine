package plasticine.templates

import Chisel._
import plasticine.Globals

import scala.collection.mutable.ListBuffer
/**
 * Register Block module, used with each functional unit
 * @param w: Word width
 * @param l: Local registers
 * @param r: Remote/Forwarding registers
 */
class RegisterBlock(val w: Int, val l: Int, val r: Int) extends Module {
  val io = new Bundle {
    val writeData       = UInt(INPUT,  w)
    val passData        = Vec.fill(r) { UInt(INPUT,  w) }
    val writeSel        = UInt(INPUT, l+r)  // One-hot encoded, else a decoder is needed
    val readLocalASel   = UInt(INPUT, log2Up(l+r))  // Binary encoded
    val readLocalBSel   = UInt(INPUT, log2Up(l+r))  // Binary encoded
    val readLocalCSel   = UInt(INPUT, log2Up(l+r))  // Binary encoded
    val readRemoteASel  = UInt(INPUT, log2Up(r)) // Binary encoded
    val readRemoteBSel  = UInt(INPUT, log2Up(r)) // Binary encoded
    val readRemoteCSel  = UInt(INPUT, log2Up(r)) // Binary encoded
    val readLocalA      = UInt(OUTPUT, w)
    val readLocalB      = UInt(OUTPUT, w)
    val readLocalC      = UInt(OUTPUT, w)
    val readRemoteA     = UInt(OUTPUT, w)
    val readRemoteB     = UInt(OUTPUT, w)
    val readRemoteC     = UInt(OUTPUT, w)
    val passDataOut     = Vec.fill(r) { UInt(OUTPUT, w) }
  }

//  val regs = List.fill(l+r) { Module(new FF(w)) }
  val regs = List.fill(l+r) { if (Globals.noModule) new FFL(w) else Module(new FF(w)) }
  val localRegs = regs.take(l)
  val remoteRegs = regs.takeRight(r)

  // Local registers - control and data inputs
  (0 until l) foreach { i =>
    localRegs(i).io.control.enable := io.writeSel(i)
    localRegs(i).io.data.init := UInt(0, width=w)
    localRegs(i).io.data.in := io.writeData
  }

  // Remote registers - control and data inputs
  (0 until r) foreach { i =>
    remoteRegs(i).io.control.enable := Bool(true) // always enabled. TODO: Multi-context
    remoteRegs(i).io.data.init := UInt(0, width=w)
    remoteRegs(i).io.data.in := Mux(io.writeSel(l+i), io.writeData, io.passData(i))
  }

  // Output assignments: to local FU
//  val readLocalAMux = Module(new MuxN(l+r, w))
  val readLocalAMux = if (Globals.noModule) new MuxNL(l+r, w) else Module(new MuxN(l+r, w))
  readLocalAMux.io.ins := Vec.tabulate(l+r) { i => regs(i).io.data.out }
  readLocalAMux.io.sel := io.readLocalASel
  io.readLocalA := readLocalAMux.io.out
//  val readLocalBMux = Module(new MuxN(l+r, w))
  val readLocalBMux = if (Globals.noModule) new MuxNL(l+r, w) else Module(new MuxN(l+r, w))
  readLocalBMux.io.ins := Vec.tabulate(l+r) { i => regs(i).io.data.out }
  readLocalBMux.io.sel := io.readLocalBSel
  io.readLocalB := readLocalBMux.io.out
  val readLocalCMux = if (Globals.noModule) new MuxNL(l+r, w) else Module(new MuxN(l+r, w))
  readLocalBMux.io.ins := Vec.tabulate(l+r) { i => regs(i).io.data.out }
  readLocalBMux.io.sel := io.readLocalCSel
  io.readLocalC := readLocalCMux.io.out

  // Output assignments: to remote FU
//  val readRemoteAMux = Module(new MuxN(r, w))
  val readRemoteAMux = if (Globals.noModule) new MuxNL(r, w) else Module(new MuxN(r, w))
  readRemoteAMux.io.ins := Vec.tabulate(r) { i => remoteRegs(i).io.data.out }
  readRemoteAMux.io.sel := io.readRemoteASel
  io.readRemoteA := readRemoteAMux.io.out
//  val readRemoteBMux = Module(new MuxN(r, w))
  val readRemoteBMux = if (Globals.noModule) new MuxNL(r, w) else Module(new MuxN(r, w))
  readRemoteBMux.io.ins := Vec.tabulate(r) { i => remoteRegs(i).io.data.out }
  readRemoteBMux.io.sel := io.readRemoteBSel
  io.readRemoteB := readRemoteBMux.io.out
  val readRemoteCMux = if (Globals.noModule) new MuxNL(r, w) else Module(new MuxN(r, w))
  readRemoteCMux.io.ins := Vec.tabulate(r) { i => remoteRegs(i).io.data.out }
  readRemoteCMux.io.sel := io.readRemoteCSel
  io.readRemoteC := readRemoteCMux.io.out

  // Output assignments: passthrough
  (0 until r) foreach { i =>
    io.passDataOut(i) := remoteRegs(i).io.data.out
  }
}

/**
 * RegisterBlock test harness
 */
class RegisterBlockTests(c: RegisterBlock) extends Tester(c) {
  val testRegBlock = ListBuffer.fill(c.l+c.r) { 0 }

  for (n <- 0 until 4) { // Cycles
    val writeData      = rnd.nextInt(1 << c.w)
    val passDataIn     = Array.fill(c.r) { rnd.nextInt(1 << c.w) }
    val writeSel       = rnd.nextInt(1 << log2Up(c.l + c.r))
    val writeSelOneHot = 1 << (writeSel)
    val readLocalASel  = rnd.nextInt(1 << log2Up(c.l + c.r))
    val readLocalBSel  = rnd.nextInt(1 << log2Up(c.l + c.r))
    val readRemoteASel  = rnd.nextInt(1 << log2Up(c.r))
    val readRemoteBSel  = rnd.nextInt(1 << log2Up(c.r))

    poke(c.io.writeData, writeData)
    c.io.passData zip passDataIn foreach { case (in, test) => poke(in, test) }
    poke(c.io.writeSel, writeSelOneHot)
    poke(c.io.readLocalASel, readLocalASel)
    poke(c.io.readLocalBSel, readLocalBSel)
    poke(c.io.readRemoteASel, readRemoteASel)
    poke(c.io.readRemoteBSel, readRemoteBSel)

    step(1)

    // Perform writes on simulation regfile
    (0 until c.l+c.r) foreach { i =>
      if (i == writeSel) {
        testRegBlock(i) = writeData
      } else if (i >= c.l) {
        testRegBlock(i) = passDataIn(i - c.l)
      }
    }
    // Perform reads on simulation regfile
    val expectedLocalA = testRegBlock(readLocalASel)
    val expectedLocalB = testRegBlock(readLocalBSel)
    val expectedPassDataOut = testRegBlock.takeRight(c.r)
    val expectedRemoteA = expectedPassDataOut(readRemoteASel)
    val expectedRemoteB = expectedPassDataOut(readRemoteBSel)

    expect(c.io.readLocalA, expectedLocalA)
    expect(c.io.readLocalB, expectedLocalB)
    expect(c.io.readRemoteA, expectedRemoteA)
    expect(c.io.readRemoteB, expectedRemoteB)
    c.io.passDataOut zip expectedPassDataOut foreach { case (out, expected) => expect(out, expected) }
  }
}

object RegisterBlockTest {
  def main(args: Array[String]): Unit = {
    val bitwidth = 4
    val localregs = 2
    val remoteregs = 2
    chiselMainTest(args, () => Module(new RegisterBlock(4, 2, 2))) {
      c => new RegisterBlockTests(c)
    }
  }
}
