package plasticine.misc

import scala.language.reflectiveCalls
import chisel3._
import templates._

object Utils {
  def log2(x: Int) = (math.log(x) / math.log(2)).toInt

  def getMux[T<:Data](ins: List[T], sel: UInt): T = {
    val srcMux = Module(new MuxN(ins(0).cloneType, ins.size))
    srcMux.io.ins := Vec(ins)
    srcMux.io.sel := sel
    srcMux.io.out
  }

  def getFF[T<: chisel3.core.Data](sig: T, en: UInt) = {
    val in = sig match {
      case v: Vec[UInt] => v.reverse.reduce { chisel3.util.Cat(_,_) }
      case u: UInt => u
    }

    val ff = Module(new FF(sig.getWidth))
    ff.io.init := 0.U
    ff.io.in := in
    ff.io.enable := en
    ff.io.out
  }

  def getFloatBits(num: Float) = java.lang.Float.floatToRawIntBits(num)
  def red(x: String): String = Console.BLACK + Console.RED_B + x + Console.RESET
}
