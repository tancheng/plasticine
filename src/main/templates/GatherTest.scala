package plasticine.templates

import Chisel._

import plasticine.pisa.parser.Parser
import plasticine.pisa.ir._

import scala.collection.mutable.HashMap

class GatherApp(
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val numCounters: Int) extends Module {

  val io = new Bundle {
    val addr = Vec.fill(v) { UInt(INPUT, width=w) }
    val numGatherVectors = UInt(INPUT, width=w)
    val enable = Bool(INPUT)
    val done = Bool(OUTPUT)
  }

  val scatterGather = 1
  val isWr = 0
  val config = MemoryUnitConfig(scatterGather, isWr, CounterChainConfig.zeroes(numCounters), CUControlBoxConfig.zeroes(numCounters, numCounters, numCounters))

  // Instantiate memory controller
  val mc = Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts,
      burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))

//  val mc = Module(new MemoryUnit(w, d, v, numOutstandingBursts,
//      burstSizeBytes, startDelayWidth, endDelayWidth, numCounters, config))


  // Instantiate gather unit, connect it to MC
  val t1 = Module(new Gather(w, v))
//  mc.addr := t1.io.mc.addr
//  mc.size := UInt(0)
//  mc.vldIn := t1.io.mc.addrValid
//  t1.io.mc.data := mc.rdata
//  t1.io.mc.dataValid := mc.vldOut
//  t1.io.mc.ready := mc.rdyOut

  mc.io.interconnects(0).addr := t1.io.mc.addr
  mc.io.interconnects(0).size := UInt(0)
  mc.io.interconnects(0).vldIn := t1.io.mc.addrValid
  t1.io.mc.data := mc.io.interconnects(0).rdata
  t1.io.mc.dataValid := mc.io.interconnects(0).vldOut
  t1.io.mc.ready := mc.io.interconnects(0).rdyOut

  // Inputs required for gather control handling
  t1.io.addr := io.addr
  t1.io.numGatherVectors := io.numGatherVectors
  t1.io.enable := io.enable
  io.done := t1.io.done
}

class GatherAppTests(c: GatherApp,
  val numGatherVectors: Int
  ) extends Tester(c) {

  def peekDram() {
    val issuedVldOut = peek(c.mc.mus(0).io.dram.vldOut)
    val receivedVldIn = peek(c.mc.mus(0).io.dram.vldIn)
    val issuedAddr = peek(c.mc.mus(0).io.dram.addr)
    val issuedTag = peek(c.mc.mus(0).io.dram.tagOut)
    val addrFifo = peek(c.mc.mus(0).addrFifo.size)
    val completionMaskIn = c.mc.mus(0).completionMask.map { s => peek(s.io.data.in) }
    val completionMaskOut = c.mc.mus(0).completionMask.map { s => peek(s.io.data.out) }

//    val issuedVldOut = peek(c.mc.io.dram.vldOut)
//    val receivedVldIn = peek(c.mc.io.dram.vldIn)
//    val issuedAddr = peek(c.mc.io.dram.addr)
//    val issuedTag = peek(c.mc.io.dram.tagOut)
//    val addrFifo = peek(c.mc.addrFifo.size)
//    val completionMaskIn = c.mc.completionMask.map { s => peek(s.io.data.in) }
//    val completionMaskOut = c.mc.completionMask.map { s => peek(s.io.data.out) }
//

    val received = peek(c.t1.gatherReceivedCounter.io.data.out)
    val commandValid = peek(c.t1.commandValid)
    val dataValid = peek(c.t1.io.mc.dataValid)
    val tldone = peek(c.t1.io.done)
    val done = peek(c.io.done)
    val gatherIssuedDone = peek(c.t1.gatherIssuedCounter.io.control.done)
    val gatherReceivedDone = peek(c.t1.gatherReceivedCounter.io.control.done)
    val mcready = peek(c.t1.io.mc.ready)
//    val issuedVldOut = peek(c.mc.io.dram.vldOut)
//    val issuedAddr = peek(c.mc.io.dram.addr)
//    val issuedTag = peek(c.mc.io.dram.tagOut)

    println(s"[peekDram] issuedVldOut = $issuedVldOut, receivedVldIn = $receivedVldIn, addr = $issuedAddr tag = $issuedTag, addrFifo size = $addrFifo, received = $received, tldone = $tldone, done = $done, dataValid = $dataValid, commandValid = $commandValid, gatherIssuedDone: $gatherIssuedDone, gatherReceivedDone: $gatherReceivedDone, mcready: $mcready")
    println(s"[peekDram] completionMaskOut: $completionMaskOut")
    println(s"[peekDram] completionMaskIn: $completionMaskIn")
  }

  var cycles = 0
  poke(c.io.enable, 1)
  poke(c.io.numGatherVectors, numGatherVectors)
  // Issue all gather addresses
  for (i <- 0 until numGatherVectors) {
    val gatherAddr = List.tabulate(c.v) { i => math.abs(rnd.nextInt) % 8000 }
    c.io.addr.zip(gatherAddr) foreach { case (a, ia) => poke(a, ia) }
    step(1)
    cycles += 1
    peekDram
  }

  var done = peek(c.io.done)
  while ((done != 1) & (cycles < 100000)) {
    step(1)
    peekDram
    done = peek(c.io.done)
    cycles += 1
  }
  poke(c.io.enable, 0)

  val totalBytesTransferred = numGatherVectors * c.v * c.w/8
  println(s"Done, design ran for $cycles cycles")
  println(s"Memory bandwidth: ${(totalBytesTransferred * 1.0 / cycles) } GB/s")
}

object GatherAppTest {

  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

    if (appArgs.size != 1) {
      println(s"Usage: <GatherAppTest> <#gatherVectors>")
      sys.exit(-1)
    }
    // Test params
    val numGatherVectors = appArgs(0).toInt

    val numInputs = 2
    val w = 32
    val v = 16
    val d = 512
    val numOutstandingBursts = 1024
    val burstSizeBytes = 64
    val startDelayWidth = 4
    val endDelayWidth = 4
    val numCounters = 6
    val isWr = 0
    val scatterGather = 0

    chiselMainTest(chiselArgs, () => Module(new GatherApp(w, d, v, numOutstandingBursts, burstSizeBytes, startDelayWidth, endDelayWidth, numCounters) )) {
      c => new GatherAppTests(c, numGatherVectors)
    }
  }
}
