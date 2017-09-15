//package templates
//
//import Chisel._
//
//import scala.collection.mutable.HashMap
//
///**
// * Thin wrapper around Chisel's Tester class with a few additional
// * helper functions.
// */
//class PlasticineTester[+T <: Module](c: T, isTrace: Boolean = true, _base: Int = 16,
//    testCmd: Option[String] = Driver.testCommand,
//    dumpFile: Option[String] = None) extends Tester(c, isTrace, _base, testCmd, dumpFile) {
//  type Inst = HashMap[String, Int]
//
//  def setMem[A <: Bits](dst: Mem[A], src: Array[Int]) {
//    for (i <- 0 until src.size) {
//      pokeAt(dst, BigInt(src(i)), i)
//    }
//  }
//
//  def setMem(dst: SRAM, src: Array[Int]) {
//    for (i <- 0 until src.size) {
//      pokeAt(dst.mem, BigInt(src(i)), i)
//    }
//  }
//
//  def getMem[A <: Bits](src: Mem[A], size: Int): Array[Int] = {
//    Array.tabulate(size) { i =>
//      peekAt(src, i).toInt
//    }
//  }
//
//  def getMem(src: SRAM): Array[Int] = {
//    Array.tabulate(src.d) { i =>
//      peekAt(src.mem, i).toInt
//    }
//  }
//
//  def expectMem(src: SRAM, gold: Array[Int]) = {
//    val test = getMem(src)
//    (0 until test.size) foreach { i =>
//      if (test(i) != gold(i)) {
//        Console.println(s"ERROR: Addr $i, expected ${gold(i)}, found ${test(i)}")
//      } else {
//        Console.println(s"PASS: Addr $i, ${gold(i)} == ${test(i)}")
//      }
//    }
//  }
//}
