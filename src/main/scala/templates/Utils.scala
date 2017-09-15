// See LICENSE.txt for license details.
package templates

import chisel3._
import chisel3.util.{log2Ceil, isPow2}
import chisel3.internal.sourceinfo._

object Utils {
  def log2Up[T](number:T): Int = {
    number match {
      case n: Int => 1 max log2Ceil(1 max n)
      case n: scala.math.BigInt => 1 max log2Ceil(1.asInstanceOf[scala.math.BigInt] max n)
    }
  }

  def getFloatBits(num: Float) = java.lang.Float.floatToRawIntBits(num)

  def delay[T <: chisel3.core.Data](sig: T, length: Int):T = {
    if (length == 0) {
      sig
    } else {
      val regs = (0 until length).map { i => RegInit(0.U) } // TODO: Make this type T
      sig match {
        case s:Bool =>
          regs(0) := Mux(s, 1.U, 0.U)
          (length-1 until 0 by -1).map { i =>
            regs(i) := regs(i-1)
          }
          (regs(length-1) === 1.U).asInstanceOf[T]
        case s:UInt =>
          regs(0) := s
          (length-1 until 0 by -1).map { i =>
            regs(i) := regs(i-1)
          }
          (regs(length-1)).asInstanceOf[T]
      }
    }
  }

  // def toFix[T <: chisel3.core.Data](a: T): FixedPoint = {
  //   a match {
  //     case aa: FixedPoint => Mux(aa > bb, a, b)
  //     case a => a // TODO: implement for other types
  //   }
  // }
}
