// See LICENSE.txt for license details.
package plasticineController

import Chisel._

class IsWrFifo extends Module {
	val io = new Bundle {
		val enq_val = Bool(INPUT)
		val enq_rdy = Bool(OUTPUT)
		val deq_val = Bool(OUTPUT)
		val deq_rdy = Bool(INPUT)
		val enq_dat = UInt(INPUT, 1)
		val deq_dat = UInt(OUTPUT, 1)
	}

	val enq_ptr = Reg(init = UInt(0, 2))
	val deq_ptr = Reg(init = UInt(0, 2))
	val is_full = Reg(init = Bool(false))
	val do_enq = io.enq_rdy && io.enq_val
	val do_deq = io.deq_rdy && io.deq_val
	val is_empty = !is_full && (enq_ptr === deq_ptr)
	val deq_ptr_inc = deq_ptr + UInt(1)
	val enq_ptr_inc = enq_ptr + UInt(1)
	val is_full_next =
	Mux(do_enq && ~do_deq && (enq_ptr_inc === deq_ptr), Bool(true), Mux(do_deq && is_full, Bool(false), is_full))

	enq_ptr := Mux(do_enq, enq_ptr_inc, enq_ptr)
	deq_ptr := Mux(do_deq, deq_ptr_inc, deq_ptr)
	is_full := is_full_next

	val ram = Mem(UInt(width = 64), 4)
	when (do_enq) {
		ram(enq_ptr) := io.enq_dat
	}

	io.enq_rdy := !is_full
	io.deq_val := !is_empty
	ram(deq_ptr) <> io.deq_dat
}

class AddrFifo extends Module {
	val io = new Bundle {
		val enq_val = Bool(INPUT)
		val enq_rdy = Bool(OUTPUT)
		val deq_val = Bool(OUTPUT)
		val deq_rdy = Bool(INPUT)
		val enq_dat = UInt(INPUT, 32)
		val deq_dat = UInt(OUTPUT, 32)
	}

	val enq_ptr = Reg(init = UInt(0, 2))
	val deq_ptr = Reg(init = UInt(0, 2))
	val is_full = Reg(init = Bool(false))
	val do_enq = io.enq_rdy && io.enq_val
	val do_deq = io.deq_rdy && io.deq_val
	val is_empty = !is_full && (enq_ptr === deq_ptr)
	val deq_ptr_inc = deq_ptr + UInt(1)
	val enq_ptr_inc = enq_ptr + UInt(1)
	val is_full_next =
	Mux(do_enq && ~do_deq && (enq_ptr_inc === deq_ptr), Bool(true), Mux(do_deq && is_full, Bool(false), is_full))

	enq_ptr := Mux(do_enq, enq_ptr_inc, enq_ptr)
	deq_ptr := Mux(do_deq, deq_ptr_inc, deq_ptr)
	is_full := is_full_next

	val ram = Mem(UInt(width = 64), 4)
	when (do_enq) {
		ram(enq_ptr) := io.enq_dat
	}

	io.enq_rdy := !is_full
	io.deq_val := !is_empty
	ram(deq_ptr) <> io.deq_dat
}

class DRAMIO extends Bundle {
	val tx_enq = Bool(dir = INPUT)
	tx_enq.setName("TX_ENQ")
	val tx_comp = Bool(dir = OUTPUT)
	tx_comp.setName("TX_COMP")
	val isWR = Bool(dir = INPUT)
	isWR.setName("IS_WR")
	val addr = UInt(INPUT, 64)
	addr.setName("ADDR")
}

class DRAMSim2 extends BlackBox {
	val io = new DRAMIO()
    io.tx_comp := io.tx_enq
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


	// val enq_val = Bool(INPUT)
	// 	val enq_rdy = Bool(OUTPUT)
	// 	val deq_val = Bool(OUTPUT)
	// 	val deq_rdy = Bool(INPUT)
	// 	val enq_dat = UInt(INPUT, 1)
	// 	val deq_dat = UInt(OUTPUT, 1)

class PlasticineControllerTop extends Module {
	val io = new Bundle {
		val enq_val = Bool(dir = INPUT)
		val enq_rdy = Bool(dir = OUTPUT)
	//	val done = Bool(dir = OUTPUT)
		val isWR = Bool(dir = INPUT)
		val addr = UInt(INPUT, 64)
		val dataIn = Vec.fill(16){UInt(INPUT, 32)}
		val dataOut = Vec.fill(16){UInt(OUTPUT, 32)}
	}

	// log data using c++ simulation code
	io.dataOut := io.dataIn

	val controller = Module(new PlasticineController())
	val dramsim = Module(new DRAMSim2())
	val isWrFifo = Module(new IsWrFifo())
	val addrFifo = Module(new AddrFifo())

	// connection from Top to the fifos
	isWrFifo.io.enq_val := io.enq_val
	isWrFifo.io.enq_dat := io.isWR
	isWrFifo.io.deq_rdy := controller.io.done
	addrFifo.io.enq_val := io.enq_val
	addrFifo.io.enq_dat := io.addr
	addrFifo.io.deq_rdy := controller.io.done


	// connection from the fifos to dramsim
	dramsim.io.isWR := isWrFifo.io.deq_dat
	dramsim.io.addr := addrFifo.io.deq_dat


	// for controller
	controller.io.start := isWrFifo.io.deq_val & addrFifo.io.deq_val
	// io.done := controller.io.done
	controller.io.tx_comp := dramsim.io.tx_comp
	dramsim.io.tx_enq := controller.io.tx_enq

	// for blackbox
	dramsim.io.isWR := io.isWR
	dramsim.io.addr := io.addr
}

class PlasticineControllerTopTests(c: PlasticineControllerTop) extends Tester(c) {
    poke(c.io.isWR, 0)
  	step(1)

  	// try storing one piece of data
  	poke(c.io.isWR, 1)
  	poke(c.io.enq_val, 1)
  	poke(c.io.addr, 0x900000)
//  	val testVec = Array(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
	val testVec = Array(UInt(0),UInt(1),UInt(2),UInt(3),UInt(4),UInt(5),UInt(6),UInt(7),UInt(8),UInt(9),UInt(10),UInt(11),UInt(12),UInt(13),UInt(14),UInt(15))
  	poke(c.io.dataIn, testVec)

  	// sequential read-write tests
	// do sequential write tests
	// var a = 0;
	// for (a <- 0 to 100) {
	// 	poke(c.io.enq_val ,1)
	// 	poke(c.io.addr, 0x900000 + 64 * a)
	// 	poke(c.io.isWR, 1)
	// 	step(1)
	// 	poke(c.io.enq_val, 0)
	// 	poke(c.io.addr, 0)
	// 	poke(c.io.isWR, 0)
	// 	step(30)
	// }

	// // do sequential read tests
	// a = 0;
	// for (a <- 0 to 100) {
	// 	poke(c.io.enq_val ,1)
	// 	poke(c.io.addr, 0x900000 + 64 * a)
	// 	poke(c.io.isWR, 0)
	// 	step(1)
	// 	poke(c.io.enq_val, 0)
	// 	poke(c.io.addr, 0)
	// 	poke(c.io.isWR, 0)
	// 	step(30)
	// }


 //    // do a write
 //    poke(c.io.enq_val, 1)
 //    poke(c.io.addr, 0x900000)
 //    poke(c.io.isWR, 1)
	// step(1)
 //    poke(c.io.enq_val, 0)
 //    poke(c.io.addr, 0)
 //    poke(c.io.isWR, 0)

	// step(1)

 //    // do a read
	// poke(c.io.enq_val, 0)
 //    poke(c.io.enq_val, 1)
 //    poke(c.io.addr, 0x900020)
 //    poke(c.io.isWR, 1)
	// step(1)
 //    poke(c.io.enq_val, 0)
 //    poke(c.io.addr, 0)
 //    poke(c.io.isWR, 0)


    step(500)
}

object PlasticineControllerTop {
	def main(args: Array[String]): Unit = {
//		chiselMainTest(Array("--debug", "--backend", "c", "--genHarness", "--vcd"), () => Module(new PlasticineControllerTop())) {
		chiselMainTest(Array("--test", "--backend", "v"), () => Module(new PlasticineControllerTop())) {
			c => new PlasticineControllerTopTests(c)
		}
	}
}
