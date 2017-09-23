package plasticine.misc

import scala.language.reflectiveCalls
import chisel3._
import chisel3.util.Cat
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

  def getCounter(en: UInt, width: Int = 32) = {
    val ctr = Module(new Counter(width))
    ctr.io.reset := 0.U
    ctr.io.saturate := 0.U
    ctr.io.max := ((1.toLong << width) - 1).U
    ctr.io.stride := 1.U
    ctr.io.enable := en
    ctr.io.out
  }

  def getTimer(max: UInt, en: UInt, width: Int = 32) = {
    val ctr = Module(new Counter(width))
    ctr.io.reset := 0.U
    ctr.io.saturate := 0.U
    ctr.io.max := max
    ctr.io.stride := 1.U
    ctr.io.enable := en
    ctr.io.done
  }

  def addWhen(en: UInt, value: UInt) = {
    val ff = Module(new FF(32))
    //ff.io.init := 15162342.U
    ff.io.init := 0.U
    ff.io.enable := en
    ff.io.in := ff.io.out + value
    ff.io.out
  }

  def convertVec(inVec: Vec[UInt], outw: Int, outv: Int) = {
    // 1. Cat everything
    val unifiedUInt = inVec.reverse.reduce { Cat(_,_) }

    // 2. Split apart
    val out = Vec(List.tabulate(outv) { i =>
      unifiedUInt(i*outw+outw-1, i*outw)
    })
    out
  }


  def getFloatBits(num: Float) = java.lang.Float.floatToRawIntBits(num)
  def red(x: String): String = Console.BLACK + Console.RED_B + x + Console.RESET
}
