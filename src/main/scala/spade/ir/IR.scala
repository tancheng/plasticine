package plasticine.spade.ir

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes
import scala.collection.mutable.HashMap
import scala.util.Random
import Chisel._

/**
 * Base class for all Params nodes
 */
abstract class AbstractParams extends Product

/**
 * Parsed config information for a single counter
 */
case class CounterRCParams(
  val w: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int
) extends AbstractParams

/**
 * CounterChain config information
 */
case class CounterChainParams(
  val numCounters: Int,
  val counter: CounterRCParams
) extends AbstractParams

case class ScratchpadParams(
  val w: Int,
  val d: Int,
  val v: Int
) extends AbstractParams

case class ComputeUnitParams(
  val w: Int,
  val startDelayWidth: Int,
  val endDelayWidth: Int,
  val d: Int,
  val v: Int, rwStages: Int,
  val numTokens: Int,
  val l: Int,
  val r: Int,
  val m: Int,
  val numScratchpads: Int,
  val numStagesAfterReduction: Int
) extends AbstractParams

case class CUControlBoxParams(
  val w: Int,
  val numTokens: Int,
  val udctrWidth: Int
) extends AbstractParams

case class CrossbarParams(
  val w: Int,
  val v: Int,
  val numInputs: Int,
  val numOutputs: Int
) extends AbstractParams


case class LUTParams(
  val w: Int,
  val size: Int
) extends AbstractParams

case class PlasticineParams(
  val cuParams: List[List[ComputeUnitParams]],
  val numArgs: Int
) extends AbstractParams
