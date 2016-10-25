// See LICENSE.txt for license details.
package simple_counter 

import Chisel._

class counterTop extends Module {
	val io = new Bundle {
		val start = Bool(dir = INPUT)
		val done = Bool(dir = OUTPUT)
	}

	val s_ready :: s_1 :: s_2 :: s_done :: Nil = Enum(UInt(), 4)
	val state = Reg(init = s_ready)
	when ( state === s_ready) {
		when (io.start) {state := s_1}
		.otherwise {state := s_ready}
	}
	.elsewhen (state === s_1) {state := s_2}
	.elsewhen (state === s_2) {state := s_done}
	.elsewhen (state === s_done) {state := s_ready}

	io.done := (state === s_done)
}

class counterTopTests (c: counterTop) extends Tester(c) {
    poke(c.io.start, 1)
	peek(c.io.done)

	step(1)
	poke(c.io.start, 0)

	var a = 0;
	for (a <- 0 to 20) {
		step(1)
		peek(c.io.done)
	}
}

object counterTop {
	def main(args: Array[String]): Unit = {
		chiselMainTest(Array("--debug", "--backend", "c", "--genHarness", "--compile", "--test"), () => Module(new counterTop())) {
//		chiselMainTest(Array("--test", "--backend", "v"), () => Module(new PlasticineControllerTop())) {
			c => new counterTopTests(c)
		}
	}
}
