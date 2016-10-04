// See LICENSE.txt for license details.
package plasticineController

import Chisel._

class DRAMIO extends Bundle {
	val tx_enq = Bool(dir = INPUT)
	tx_enq.setName("TX_ENQ")
	val tx_comp = Bool(dir = OUTPUT)
	tx_comp.setName("TX_COMP")
	val isWR = Bool(dir = INPUT)
	isWR.setName("IS_WR")
	val isWROut = Bool(dir = OUTPUT)
	isWROut.setName("IS_WR_OUT")
	val addr = UInt(INPUT, 64)
	addr.setName("ADDR")
	val addrOut = UInt(OUTPUT, 64)
	addrOut.setName("ADDR_OUT")
}

class DRAMSim2 extends BlackBox {
	val io = new DRAMIO()
	io.tx_comp := io.tx_enq
	io.isWROut := io.isWR
	io.addrOut := io.addr
}

class PlasticineController extends Module {
	val io = new Bundle {
		val start = Bool(dir = INPUT)
		val tx_comp = Bool(dir = INPUT)
		val tx_enq = Bool(dir = OUTPUT)
		val done = Bool(dir = OUTPUT)
	}

	val s_ready :: s_enque :: s_wait :: s_done :: Nil = Enum(UInt(), 4)
	val state = Reg(init = s_ready)
	when (state === s_ready) {
		when (io.start) {state := s_enque}
		.otherwise {state := s_ready}
	}
	.elsewhen (state === s_enque) {
		state := s_wait
	}
	.elsewhen (state === s_wait) {
		when (io.tx_comp) {state := s_done}
		.otherwise {state := s_wait}
	}
	.elsewhen (state === s_done) {
		state := s_ready
	}

	io.tx_enq := (state === s_enque)
	io.done := (state === s_done)
}

class PlasticineControllerTop extends Module {
	val io = new Bundle {
		val start = Bool(dir = INPUT)
		val done = Bool(dir = OUTPUT)
		val isWR = Bool(dir = INPUT)
		val isWROut = Bool(dir = OUTPUT)
		val addr = UInt(INPUT, 64)
		val addrOut = UInt(OUTPUT, 64)
	}

	val controller = Module(new PlasticineController())
	val dramsim = Module(new DRAMSim2())

	// for controller
	controller.io.start := io.start
	io.done := controller.io.done
	controller.io.tx_comp := dramsim.io.tx_comp
	dramsim.io.tx_enq := controller.io.tx_enq

	// for blackbox
	dramsim.io.isWR := io.isWR
	io.isWROut := dramsim.io.isWROut
	dramsim.io.addr := io.addr
	io.addrOut := dramsim.io.addrOut
}

class PlasticineControllerTopTests(c: PlasticineControllerTop) extends Tester(c) {
    poke(c.io.start, 0)
	step(1)
	poke(c.io.start, 1)
	peek(c.io.done)

	var a = 0;
	for (a <- 0 to 10) {
		step(1)
		peek(c.io.done)
	}

	poke(c.io.start, 1)
	peek(c.io.done)
    step(1)
    poke(c.io.start, 0)
	peek(c.io.done)
	for (a <- 0 to 10) {
		step(1)
		peek(c.io.done)
	}

	poke(c.io.start, 1)
	peek(c.io.done)
    step(1)
    poke(c.io.start, 0)
	peek(c.io.done)
	for (a <- 0 to 10) {
		step(1)
		peek(c.io.done)
	}
}

object PlasticineControllerTop {
	def main(args: Array[String]): Unit = {
//		chiselMainTest(Array("--debug", "--backend", "c", "--genHarness"), () => Module(new PlasticineControllerTop())) {
		chiselMainTest(Array("--test", "--backend", "v"), () => Module(new PlasticineControllerTop())) {
			c => new PlasticineControllerTopTests(c)
		}
	}
}
