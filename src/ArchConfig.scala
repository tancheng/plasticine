package plasticine

object ArchConfig {
  val w = 32
  val startDelayWidth = 4
  val endDelayWidth = 4
  val d = 10
  val v = 16
  val l = 0
  val r = 16
  val rwStages = 3
  val numTokens = 8
  val numCounters = 8
  val m = 2048
  val numScratchpads = 4
  val numStagesAfterReduction = 2
  val numRows = 2
  val numCols = 2
  val numMemoryUnits = 2
  val burstSizeBytes = 64
  val numOutstandingBursts = 16
  val numTopInputs = 2 * numCols
}
