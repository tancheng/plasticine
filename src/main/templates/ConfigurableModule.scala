package plasticine.templates

import Chisel._

import scala.collection.mutable.HashMap

/**
 * abstract configurable interface
 */
abstract class OpcodeT extends Bundle

abstract class ConfigInterface[T<:OpcodeT](chiselType: T) extends Bundle {
  val opcode = chiselType
}

/**
 * Abstract 'ConfigurableModule' class. This class is a thin wrapper
 * around a standard Chisel Module with a few extra parameters
 * and methods related to reconfiguration logic to be defined.
 */
abstract class ConfigurableModule[M<:OpcodeT] extends Module {
  val io: ConfigInterface[M]

  /** Storage for configuration bits */
  val config : M

}
