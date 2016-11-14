package plasticine

import plasticine.pisa.parser._

object ArchConfig {
  // Defaults
  var w = 32
  var startDelayWidth = 4
  var endDelayWidth = 4
  var d = 10
  var v = 16
  var l = 0
  var r = 16
  var rwStages = 3
  var numTokens = 8
  var numCounters = 8
  var m = 64
  var numScratchpads = 4
  var numStagesAfterReduction = 2
  var numRows = 2
  var numCols = 2
  var numMemoryUnits = 2
  var burstSizeBytes = 64
  var numOutstandingBursts = 16
  var numTopInputs = 2 * numCols
  var numScalarIO = 4
  var numScalarRegisters = 8
  def printConfig {
     println("Plasticine Configuration:")
     println(s"w =  $w")
     println(s"startDelayWidth = $startDelayWidth")
     println(s"endDelayWidth = $endDelayWidth")
     println(s"d =  $d")
     println(s"v =  $v")
     println(s"l =   $l")
     println(s"r =  $r")
     println(s"rwStages = $rwStages")
     println(s"numTokens = $numTokens")
     println(s"numCounters = $numCounters")
     println(s"m = $m")
     println(s"numScratchpads = $numScratchpads")
     println(s"numStagesAfterReduction = $numStagesAfterReduction")
     println(s"numRows = $numRows")
     println(s"numCols = $numCols")
     println(s"numMemoryUnits = $numMemoryUnits")
     println(s"burstSizeBytes = $burstSizeBytes")
     println(s"numOutstandingBursts =  $numOutstandingBursts")
     println(s"numTopInputs = $numTopInputs")
     println(s"numScalarIO = $numScalarIO")
     println(s"numScalarRegisters = $numScalarRegisters")
  }

  def setConfig(file: String) {
    val map = Parser.parseRaw(file)
    w =                     Parser.getFieldInt(map, "w")
    startDelayWidth =        Parser.getFieldInt(map, "startDelayWidth")
    endDelayWidth =          Parser.getFieldInt(map, "endDelayWidth")
    d =                     Parser.getFieldInt(map, "d")
    v =                     Parser.getFieldInt(map, "v")
    l =                      Parser.getFieldInt(map, "l")
    r =                     Parser.getFieldInt(map, "r")
    rwStages =               Parser.getFieldInt(map, "rwStages")
    numTokens =              Parser.getFieldInt(map, "numTokens")
    numCounters =            Parser.getFieldInt(map, "numCounters")
    m =                   Parser.getFieldInt(map, "m")
    numScratchpads =         Parser.getFieldInt(map, "numScratchpads")
    numStagesAfterReduction = Parser.getFieldInt(map, "numStagesAfterReduction")
    numRows =                Parser.getFieldInt(map, "numRows")
    numCols =                Parser.getFieldInt(map, "numCols")
    numMemoryUnits =         Parser.getFieldInt(map, "numMemoryUnits")
    burstSizeBytes =        Parser.getFieldInt(map, "burstSizeBytes")
    numOutstandingBursts =  Parser.getFieldInt(map, "numOutstandingBursts")
    numTopInputs =          Parser.getFieldInt(map, "numTopInputs")
    numScalarIO =          Parser.getFieldInt(map, "numScalarIO")
    numScalarRegisters =          Parser.getFieldInt(map, "numScalarRegisters")

    printConfig
  }
}
