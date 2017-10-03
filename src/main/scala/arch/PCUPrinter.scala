package plasticine.arch

import chisel3._
import chisel3.util._
import templates.Opcodes
import plasticine.ArchConfig
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

import templates._
import plasticine.spade._
import plasticine.config._
import plasticine.pisa.enums._
import templates.Utils.log2Up
import plasticine.misc.Utils.getCounter

trait PCUPrinter extends Module { self: PCU =>
  lazy val dotCycleCount = getCounter(localEnable | stageEnables.map {_.io.out(0)}.reduce {_|_})

  def prefix =
p"""
  digraph {
    node [pin=true];
    label="Cycle $dotCycleCount"
"""

  def suffix =
p"""
} // digraph $dotCycleCount

"""

	val height = 20
	val width = 70

  def nodeName(stage: Int, lane: Int) = {
    s"s${stage}_l${lane}"
  }

  def regPortName(reg: Int) = s"r${reg}"

  def getRow(stage: Int, lane: Int, reg: Int) = {
p"""<TR><TD HEIGHT="${height}" WIDTH="${width}" FIXEDSIZE="true" PORT="${regPortName(reg)}">${pipeRegs(stage)(lane)(reg)}"</TD></TR>
"""

  }

  def printRegFile(stage: Int, lane: Int) {
    val tablePrefix = s"""<TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0">"""
    val tableSuffix = s"""</TABLE>"""
    printf(
p"""s${stage}_l${lane} [shape=plaintext, pos="$stage, $lane!", label=<
  $tablePrefix
""")
    for (r <- 0 until p.r) {
      printf(getRow(stage, lane, r))
    }
    printf(
p"""$tableSuffix
>];
""")
  }

  def printRegFiles {
    (0 until p.d) foreach { stage =>
      (0 until p.v) foreach { lane =>
        printRegFile(stage, lane)
      }
    }
  }
  def connectRegFiles {
    (1 until p.d) foreach { stage =>
      (0 until p.v) foreach { lane =>
        (0 until p.r) foreach { reg =>
          printf(s"${nodeName(stage-1, lane)}:${regPortName(reg)} -> ${nodeName(stage-1, lane)}:${regPortName(reg)}\n")
        }
      }
    }
  }

  // Conditional printing
  def printDot {
    when (localEnable) {
      printf(prefix)
      printRegFiles
      connectRegFiles
      printf(suffix)
    }
  }
}
