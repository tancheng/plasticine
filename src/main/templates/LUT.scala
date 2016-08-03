package plasticine.templates

import plasticine.misc.Utils
import Chisel._
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object LUT {

  // Given n inputs, give all permutations.
  // E.g: getInputs(4) = List(
  //                        List(0, 0)
  //                        List(0, 1)
  //                        List(1, 0)
  //                        List(1, 1)
  //                    )
  def getInputs(n: Int) = {
    val numInputs = 1 << n
    List.tabulate(numInputs)  { i =>
      List.tabulate(n) { ii =>
        (i & (1 << ii)) >> ii
      }.reverse
    }
  }

  def getConfig(numInputs: Int, f: List[Int] => Int): List[Int] = {
    val inputs = getInputs(numInputs).toList
    inputs.map { i =>
      f(i)
    }
  }
}

/**
 * Combinational Look-up table using Chisel Vec. If the Vec is only read in the design,
 * Chisel automatically converts them to ROM types.
 * https://chisel.eecs.berkeley.edu/2.2.0/chisel-tutorial.pdf
 */
class LUT(val w: Int, val config: List[Int]) extends Module {

  val tree = q"val x = 3; println(x)"
  val tb = runtimeMirror(getClass.getClassLoader).mkToolBox()
  val code = tb.compile(tree)
  code()

  val size = config.size
  val numSelectBits = log2Up(config.size)
  val io = new Bundle {
    val ins = Vec.fill(numSelectBits) { UInt(INPUT, width=1) }
    val out = Bits(OUTPUT, width = w)
  }

  val sel = io.ins.reduce{Cat(_,_)}
  val lut = Vec.tabulate(size) { i => UInt(config(i), width=w) }
  io.out := lut(sel)
}

class LUTTests(c: LUT) extends Tester(c) {
    def getBitsFor(num: Int, nbits: Int) = {
      Array.tabulate(nbits) { i =>
        BigInt((num & (1 << nbits-i-1)) >> (nbits-i-1))
      }
    }

    (0 until c.size) foreach { in =>
      val inbits = getBitsFor(in, c.numSelectBits)
      poke(c.io.ins, getBitsFor(in, c.numSelectBits))
      val out = c.config(in)
      expect(c.io.out, out) // Combinational LUT
      step(1)
    }
}

object LUTTest {
  def main(args: Array[String]): Unit = {
    val wordSize = 1
    val size = 16
    val config = List.tabulate(size) { i =>
      math.abs(scala.util.Random.nextInt % (1 << wordSize))
    }

    chiselMainTest(args, () => Module(new LUT(wordSize, config))) {
      c => new LUTTests(c)
    }
  }
}

