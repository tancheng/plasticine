package plasticine.templates

import Chisel._

class Mux8(val w: Int) extends Module {
  val io = new Bundle {
    val in0 = Bits(INPUT,  width = w)
    val in1 = Bits(INPUT,  width = w)
    val in2 = Bits(INPUT,  width = w)
    val in3 = Bits(INPUT,  width = w)
    val in4 = Bits(INPUT,  width = w)
    val in5 = Bits(INPUT,  width = w)
    val in6 = Bits(INPUT,  width = w)
    val in7 = Bits(INPUT,  width = w)
    val sel = Bits(INPUT,  3)
    val out = Bits(OUTPUT, width = w)
  }

  val m0 = Module(new Mux4(w))
  m0.io.sel := Cat(io.sel(0), io.sel(1))
  m0.io.in0 := io.in0
  m0.io.in1 := io.in1
  m0.io.in2 := io.in2
  m0.io.in3 := io.in3

  val m1 = Module(new Mux4(w))
  m0.io.sel := Cat(io.sel(0), io.sel(1))
  m1.io.in0 := io.in4
  m1.io.in1 := io.in5
  m1.io.in2 := io.in6
  m1.io.in3 := io.in7

  val m2 = Module(new Mux2(w))
  m2.io.sel := io.sel(2)
  m2.io.in0 := m0.io.out
  m2.io.in1 := m1.io.out

  io.out := m2.io.out
}

class Mux8Tests(c: Mux8) extends Tester(c) {
  for (s <- 0 until 8) {
    for(i0 <- 0 until 2) {
      for(i1 <- 0 until 2) {
        for(i2 <- 0 until 2) {
          for(i3 <- 0 until 2) {
            for(i4 <- 0 until 2) {
              for(i5 <- 0 until 2) {
                for(i6 <- 0 until 2) {
                  for(i7 <- 0 until 2) {
                    poke(c.io.sel, s)
                    poke(c.io.in0, i0)
                    poke(c.io.in1, i1)
                    poke(c.io.in2, i2)
                    poke(c.io.in3, i3)
                    poke(c.io.in4, i4)
                    poke(c.io.in5, i5)
                    poke(c.io.in6, i6)
                    poke(c.io.in7, i7)
                    step(1)
                    val out = s match {
                      case 0 => i0
                      case 1 => i1
                      case 2 => i2
                      case 3 => i3
                      case 4 => i4
                      case 5 => i5
                      case 6 => i6
                      case 7 => i7
                    }
                    expect(c.io.out, out)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
