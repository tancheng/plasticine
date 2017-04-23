package plasticine.misc
import chisel3._
import plasticine.templates._

object Utils {
  def log2(x: Int) = (math.log(x) / math.log(2)).toInt

  def getMux[T<:Data](ins: List[T], sel: UInt): T = {
    val srcMux = Module(new MuxN(ins(0).cloneType, ins.size))
    srcMux.io.ins := Vec(ins)
    srcMux.io.sel := sel
    srcMux.io.out
  }

}
