package plasticine.templates

import Chisel._

//class Plasticine(val w: Int) extends Module {
//
//  private def floorMod(x: Int, divisor: Int) = {
//    val res = x % divisor
//    if (res < 0) res + divisor else res
//  }
//
//  // Instantiate two Compute units
//  val c00 = Module(new Compute(w))
//  val c01 = Module(new Compute(w))
//  val s00 = Module(new SwitchBox8(w))
//  val s01 = Module(new SwitchBox8(w))
//  val s02 = Module(new SwitchBox8(w))
//  val s10 = Module(new SwitchBox8(w))
//  val s11 = Module(new SwitchBox8(w))
//  val s12 = Module(new SwitchBox8(w))
//
//  // Wire up switches
//  s00.io.in := s10.io.os
//  s00.io.inw :=
//  s00.io.iw :=
//  s00.io.isw :=
//  s00.io.is :=
//  s00.io.ise :=
//  s00.io.ie :=
//  s00.io.ine :=
//}
