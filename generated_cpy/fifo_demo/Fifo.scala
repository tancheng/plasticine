// See LICENSE.txt for license details.
package fifo_demo

import Chisel._

class Fifo extends Module {
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

class FifoTest(c: Fifo) extends Tester(c) {
	
	poke(c.io.deq_rdy, 0)
	step(1)
	// add three elements
	poke(c.io.enq_val, 1)
	step(1)
	poke(c.io.enq_val, 0)
	step(1)

	poke(c.io.enq_val, 1)
	step(1)
	poke(c.io.enq_val, 0)
	step(1)

	poke(c.io.enq_val, 1)
	step(1)
	poke(c.io.enq_val, 0)
	step(1)

	poke(c.io.enq_val, 1)
	step(1)
	poke(c.io.enq_val, 0)
	step(1)

	// deque three elements
	step(20)
	poke(c.io.deq_rdy, 1)
	step(1)
	poke(c.io.deq_rdy, 0)
	step(1)

	poke(c.io.deq_rdy, 1)
	step(1)
	poke(c.io.deq_rdy, 0)
	step(1)

	poke(c.io.deq_rdy, 1)
	step(1)
	poke(c.io.deq_rdy, 0)
	step(1)


	poke(c.io.deq_rdy, 1)
	step(1)
	poke(c.io.deq_rdy, 0)
	step(1)

}

object Fifo {
	def main(args: Array[String]): Unit = {
		chiselMainTest(Array("--compile", "--test", "--debug", "--backend", "c", "--genHarness", "--vcd"), () => Module(new Fifo())) {
			c => new FifoTest(c)
		}
	}

}