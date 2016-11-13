#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <file_to_be_chopped>"
  exit -1
fi

FILE=$1
EXE=`grep -o "public mod_t" *.h -r | cut -f1 -d'.'`
EMU_FILE=${EXE}-emulator.cpp
NUM=5000

function labelStartLine() {
  label=$1
  file=$2
  echo $(grep -n "^${label}:" ${file} | cut -f1 -d':')
}

function labelEndLine() {
  label=$1
  file=$2
  labelStart=$(labelStartLine $label $file)
  echo $(grep -n "goto [A-Z][0-9]*;" ${file} | cut -f1 -d':' | sort -n | awk -v threshold="$labelStart" '$1 >= threshold' | head -n1)
}

function extractLabelTxt() {
  label=$1
  file=$2
  labelStart=`grep -n "^${label}:" ${file} | cut -f1 -d':'`
  labelEnd=`grep -n "goto [A-Z][0-9]*;" ${file} | cut -f1 -d':' | sort -n | awk -v threshold="$labelStart" '$1 >= threshold' | head -n1`

  ## Extract body into separate file, add a placeholder to append stuff in
  echo $(sed -n "$(expr $labelStart),$(expr $labelEnd)p" ${file})
}

GLOBALS=globals.h
GLOBALDEFS=globalDefs.h
function processGlobals() {
  echo "Splitting global .h files"
  ## Split the header file
  rm -f $GLOBALS $GLOBALDEFS
  echo "#ifndef __GLOBALS_H__" >> $GLOBALS
  echo "#define __GLOBALS_H__" >> $GLOBALS
  echo "#ifndef __GLOBALDEFS_H__" >> $GLOBALDEFS
  echo "#define __GLOBALDEFS_H__" >> $GLOBALDEFS
  echo "#include \"emulator.h\"" | tee -a $GLOBALS $GLOBALDEFS > /dev/null

  sed -i "s/private/public/" ${EXE}.h
  grep "^[ ][ ]*dat_t<[0-9][0-9]*> .*;" ${EXE}.h | tee -a $GLOBALS $GLOBALDEFS > /dev/null
  grep "^[ ][ ]*mem_t<[0-9][0-9,]*> .*;" ${EXE}.h | tee -a $GLOBALS $GLOBALDEFS > /dev/null
  grep "^[ ][ ]*val_t __rand_seed;" ${EXE}.h | tee -a $GLOBALS $GLOBALDEFS > /dev/null
  grep "^[ ][ ]*clk_t clk;" ${EXE}.h | tee -a $GLOBALS $GLOBALDEFS > /dev/null

  ## Move "const val_t" declarations in .h and .cpp file to globals.h and globalDefs.h
  grep "^[ ][ ]*static const .*;" ${EXE}.h | sed 's/static//' | tee -a $GLOBALS > /dev/null
  grep "const val_t ${EXE}_t::.*" ${FILE} | sed "s/${EXE}_t:://" | tee -a $GLOBALDEFS > /dev/null

  ## Add 'extern' prefix to all declarations in globals.h
  sed -i 's/^[ ][ ]*dat_t/extern dat_t/' $GLOBALS
  sed -i 's/^[ ][ ]*mem_t/extern mem_t/' $GLOBALS
  sed -i 's/^[ ][ ]*clk_t/extern clk_t/' $GLOBALS
  sed -i 's/^[ ][ ]*val_t/extern val_t/' $GLOBALS
  sed -i 's/^[ ][ ]*const/extern const/' $GLOBALS

  ## Delete all moved declarations in .h and .cpp files
  sed -i "s/^[ ][ ]*dat_t<[0-9][0-9]*> .*;//" ${EXE}.h
  sed -i "s/^[ ][ ]*mem_t<[0-9][0-9,]*> .*;//" ${EXE}.h
  sed -i "s/^[ ][ ]*val_t __rand_seed;//" ${EXE}.h
  sed -i "s/^[ ][ ]*clk_t clk;//" ${EXE}.h
  sed -i "s/^[ ][ ]*static const .*;//" ${EXE}.h
  sed -i "s/^[ ]*const .*;//" ${FILE}

  sed -i "3i #include \"$GLOBALS\"" ${EXE}.h
  echo "#endif" | tee -a $GLOBALS $GLOBALDEFS > /dev/null

  sed -i "s/\&mod->/\&/" $FILE
  sed -i 's/module\.clk/clk/g' $EMU_FILE
  sed -i "2i #include \"$GLOBALDEFS\"" ${FILE}
}

function extractRange() {
  start=$1
  end=$2
  src=$3
  output=$4
  echo "[extractRange] output = $output"
  sed -n ${start},${end}p ${src} > $output
}

function extractFunctionTxt() {
  funcName=$1
  offset=$2
  namespace=$3
  funcStart=`grep -n "void ${EXE}_${3}t::${funcName} " ${FILE} | cut -f1 -d':'`
  funcEnd=`grep -n "^}$" ${FILE} | cut -f1 -d':' | sort -n | awk -v threshold="$funcStart" '$1 > threshold' | head -n1`

  if [ $(expr $funcStart + $offset + 1) -lt $(expr $funcEnd) ]; then
    ## Extract body into separate file
  #  sed -n $(expr $funcStart + $offset + 1),$(expr $funcEnd - 1)p ${FILE} > ${funcName}.txt
    extractRange $(expr $funcStart + $offset + 1) $(expr $funcEnd - 1) $FILE ${funcName}.txt

    ## Add a placeholder into the file
    placeHolderLine=$(expr $funcStart + $offset + 1)
    sed -i "${placeHolderLine}i\//${funcName}Splitter" ${FILE}

    ## Delete chopped up lines from file
    sed -i -e "$(expr $funcStart + $offset + 2), $(expr $funcEnd)d" ${FILE}
  fi
}


headerIdx=3
function getHeaderIdx() {
  headerIdx=$(expr $headerIdx + 1)
  echo $headerIdx
}

function chopCommon() {
  funcName=$1
  argType=$2
  argName=$3

  if [ -e ${funcName}.txt ]; then
    ## Chop up file into several pieces
    split -l ${NUM} --additional-suffix="${funcName}.cpp" --numeric-suffixes ${funcName}.txt

    ## Append common header files, method call names for each file
    ## Insert method calls in place (don't forget semicolons)
    METHODS=methods.txt
    HEADER=${funcName}.h
    rm -f $METHODS $HEADER
    for file in `ls x[0-9]*${funcName}.cpp`; do
      f=`echo $file | cut -f1 -d'.'`
      sed -i "1i #include \"${EXE}.h\"" $file
      sed -i "2i #include \"${GLOBALS}\"" $file
      sed -i "3i void $f($argType $argName) {" $file
      echo "}" >> $file
      echo "$f($argName);" >> $METHODS
      echo "void $f($argType $argName);" >> $HEADER
    done

    sed -i "$(getHeaderIdx)i #include\"$HEADER\"" ${FILE}
    sed -i "/\/\/${funcName}Splitter/r ${METHODS}" ${FILE}
  fi
}

function processInitFile() {
  echo "Processing init function"
  chopCommon "init"
}

function processClockHiFile() {
  echo "Processing clock_hi function"
  chopCommon "clock_hi"
}


function processDumpInitFile() {
  echo "Processing dump_init function"
  chopCommon "dump_init" "FILE*" "f"
}

function processInitSimDataFile () {
  echo "Processing init_sim_data function"
  PREV_NUM=$NUM
  NUM=20000

#  chopCommon "init_sim_data" "${EXE}_t*" "mod"
  funcName=init_sim_data
  argType=${EXE}_t*
  argName=mod

  ## Chop up file into several pieces
  split -l ${NUM} --additional-suffix="${funcName}.cpp" --numeric-suffixes ${funcName}.txt

  ## Append common header files, method call names for each file
  ## Insert method calls in place (don't forget semicolons)
  METHODS=methods.txt
  HEADER=${EXE}.h
  headerLine=$(grep -n "${funcName}()" $HEADER | cut -f1 -d':')
  headerLine=$(expr $headerLine + 1)
  rm -f $METHODS
  for file in `ls x[0-9]*${funcName}.cpp`; do
    f=`echo $file | cut -f1 -d'.'`
    sed -i "1i #include \"${EXE}.h\"" $file
    sed -i "2i #include \"${GLOBALS}\"" $file
    sed -i "3i void ${EXE}_api_t::$f($argType $argName) {" $file
    echo "}" >> $file
    echo "$f($argName);" >> $METHODS
    sed -i "${headerLine} i void $f($argType $argName);" $HEADER
    headerLine=$(expr $headerLine + 1)
  done

  sed -i "/\/\/${funcName}Splitter/r ${METHODS}" ${FILE}
  NUM=$PREV_NUM
}

function processClockLoFile() {
  echo "Processing clock_lo function"
  ## Move "val_t T<num>;" declarations into a global header file
  CLOCKLO_GLOBALS=clock_loGlobals.h
  CLOCKLO_GLOBALDEFS=clock_loGlobalDefs.h
  rm -f $CLOCKLO_GLOBALS $CLOCKLO_GLOBALDEFS
  echo "#ifndef __CLOCKLO_GLOBALS_H__"    >> $CLOCKLO_GLOBALS
  echo "#define __CLOCKLO_GLOBALS_H__"    >> $CLOCKLO_GLOBALS
  echo "#ifndef __CLOCKLO_GLOBALDEFS_H__" >> $CLOCKLO_GLOBALDEFS
  echo "#define __CLOCKLO_GLOBALDEFS_H__" >> $CLOCKLO_GLOBALDEFS
  echo "#include \"emulator.h\""  | tee -a $CLOCKLO_GLOBALS $CLOCKLO_GLOBALDEFS > /dev/null
  grep "^[ ][ ]*val_t T[0-9]*.*;" clock_lo.txt | tee -a $CLOCKLO_GLOBALS $CLOCKLO_GLOBALDEFS > /dev/null
  sed -i "s/^[ ][ ]*val_t T[0-9]*;//" clock_lo.txt
  sed -i 's/^[ ][ ]*val_t/extern val_t/' $CLOCKLO_GLOBALS
  echo "#endif" | tee -a $CLOCKLO_GLOBALS $CLOCKLO_GLOBALDEFS > /dev/null

  sed -i "$(getHeaderIdx)i #include\"$CLOCKLO_GLOBALDEFS\"" ${FILE}

  ## Replace "this" with "ptr"
  sed -i "s/this/ptr/g" clock_lo.txt

  ## Chop up file into several pieces
  split -l ${NUM} --additional-suffix="clock_lo.cpp" --numeric-suffixes clock_lo.txt

  ## Append common header files, method call names for each file
  METHODS=methods.txt
  HEADER=clock_lo.h
  rm -f $METHODS $HEADER
  for file in `ls x[0-9]*clock_lo.cpp`; do
    f=`echo $file | cut -f1 -d'.'`
    sed -i "1i #include \"${EXE}.h\"" $file
    sed -i "2i #include \"${CLOCKLO_GLOBALS}\"" $file
    sed -i "3i #include \"${GLOBALS}\"" $file
    sed -i "4i void $f(${EXE}_t *ptr, dat_t<1> reset, bool assert_fire) {" $file
    echo "}" >> $file
    echo "$f(this, reset, assert_fire);" >> $METHODS
    echo "void $f(${EXE}_t *ptr, dat_t<1> reset, bool assert_fire);" >> $HEADER
  done

  sed -i "$(getHeaderIdx)i #include\"$HEADER\"" ${FILE}
  sed -i "/\/\/clock_loSplitter/r ${METHODS}" ${FILE}
}

function min() {
  echo $(($1<$2?$1:$2));
}

maxLabelsPerFile=200
function processKLCommon() {
  file=$1
  num=$2
  sed -i "s/^[KL]\([0-9][0-9]*\):/case \1:/" $file
  sed -i "s/goto \([KL]\)\([0-9][0-9]*\);/ \1${num}(\2, f, t, reset); break;/" $file
}

function processKfile() {
  file=$1
  num=$2
  sed -i "s/if (.*)/\0 {/" $file
  processKLCommon $1 $2
  sed -i "s/break;/\0 }/" $file
}
function processLfile() {
  processKLCommon $1 $2
}

function processDumpFile() {
  dumpFile=dump.txt

  ## Rearrange labels to better suit chopping
  allLabels=$(grep -o "^[A-Z][0-9]*:" ${dumpFile})
  maxlabel=$(grep -o "^L[0-9]*:" ${dumpFile} | cut -f1 -d':' | tail -n1 | cut -f2 -d'L')
  kheader="k.h"
  lheader="l.h"
  echo "#ifndef __K_H__" > $kheader
  echo "#define __K_H__" >> $kheader
  echo "#ifndef __L_H__" > $lheader
  echo "#define __L_H__" >> $lheader

  ## Extract z0 text into a separate file
  extractLabelTxt "Z0" $dumpFile > z0.txt

  ## Replace "goto C0" with "return" in dump.txt
  sed -i "s/goto C0/return/" $dumpFile
  sed -i "s/goto C0/return/" z0.txt

  for num in $(seq 0 $maxLabelsPerFile $maxlabel); do
    startLabel=$num
    endLabel=$(expr $startLabel + $(min $(expr $maxLabelsPerFile - 1) $(expr $maxlabel - $num)))
    echo "Labels $startLabel to $endLabel"
    kstart=$(labelStartLine K$startLabel $dumpFile)
    kend=$(labelEndLine K$endLabel $dumpFile)
    lstart=$(labelStartLine L$startLabel $dumpFile)
    lend=$(labelEndLine L$endLabel $dumpFile)

    kfile="k${startLabel}".cpp
    lfile="l${startLabel}".cpp
    echo "void K${startLabel}(int idx, FILE* f, val_t t, dat_t<1> reset);" >> $kheader
    echo "void L${startLabel}(int idx, FILE* f, val_t t, dat_t<1> reset);" >> $lheader
    extractRange $kstart $kend $dumpFile "$kfile"
    extractRange $lstart $lend $dumpFile "$lfile"
    sed -i "1 i #include \"$GLOBALS\"" $kfile
    sed -i "2 i #include \"${EXE}.h\"" $kfile
    sed -i "3 i #include \"$kheader\"" $kfile
    sed -i "4 i #include \"$lheader\"" $kfile
    sed -i "1 i #include \"$GLOBALS\"" $lfile
    sed -i "2 i #include \"${EXE}.h\"" $lfile
    sed -i "3 i #include \"$kheader\"" $lfile
    sed -i "4 i #include \"$lheader\"" $lfile

      sed -i "5 i void K${startLabel}(int idx, FILE* f, val_t t, dat_t<1> reset) {" $kfile
      sed -i "6 i switch(idx) {" $kfile
      sed -i "5 i void L${startLabel}(int idx, FILE* f, val_t t, dat_t<1> reset) {" $lfile
      sed -i "6 i switch(idx) {" $lfile
      processKfile $kfile $startLabel
      processLfile $lfile $startLabel

      if [ ${endLabel} -lt ${maxlabel} ]; then
        # Replace the last line in kfile with next call to L
        sed -i '$s/L[0-9][0-9]*(\([0-9][0-9]*\)/L\1(\1/' $kfile
      else
        ## Inline the Z0 label in the last K file
        echo "}" | tee -a $kfile > /dev/null
        echo "return;" | tee -a $kfile > /dev/null
        cat z0.txt >> $kfile
      fi
      echo "}" | tee -a $kfile $lfile > /dev/null
      echo "}" | tee -a $kfile $lfile > /dev/null


      sed -i -e "${lstart},${lend}d" ${dumpFile}
      sed -i -e "${kstart},${kend}d" ${dumpFile}

  #    for offset in $(seq 0 $(min $maxLabelsPerFile $(expr $maxlabel - $num))); do
  #      labelNum=$(expr $num + $offset)
  #      llabel="L$labelNum"
  #      klabel="K$labelNum"
  #      kstart=$(labelStartLine $klabel $dumpFile)
  #      echo "$llabel must be inserted at line $(expr $kstart)"
  #      sed -i "$(expr $kstart) i $(extractLabelTxt $llabel $dumpFile)" ${dumpFile}
  #    done
    done
    echo "#endif" | tee -a $kheader $lheader > /dev/null

    ## Replace the first 'goto' with method call
    sed -i "1s/goto \([KL]\)\([0-9][0-9]*\);/ \10(\2, f, t, reset);/" $dumpFile


    ## Add all 'K' method calls in line 2
    lineNum=2
    for num in $(seq 0 $maxLabelsPerFile $maxlabel); do
      sed -i "${lineNum} i K${num}(${num}, f, t, reset);" $dumpFile
      lineNum=$(expr $lineNum + 1)
    done

    ## Add all 'L' method calls after C0, before Z0
  c0Line=$(labelStartLine "C0" $dumpFile)
  lineNum=$(expr $c0Line + 1)
  for num in $(seq 0 $maxLabelsPerFile $maxlabel); do
    sed -i "${lineNum} i L${num}(${num}, f, t, reset);" $dumpFile
    lineNum=$(expr $lineNum + 1)
  done

  # Stitch the generated headers and dump.txt back into the original file
  sed -i "$(getHeaderIdx)i #include\"k.h\"" ${FILE}
  sed -i "$(getHeaderIdx)i #include\"l.h\"" ${FILE}
  sed -i "/\/\/dumpSplitter/r dump.txt" ${FILE}
}


processGlobals

funcs=(init clock_lo clock_hi dump_init dump init_sim_data)
offsets=(1 0 0 0 2 5)
namespace=("" "" "" "" "" "api_")

funcs=(init clock_lo clock_hi dump_init init_sim_data)
offsets=(1 0 0 0 5)
namespace=("" "" "" "" "api_")

#funcs=(dump) # init_sim_data)
#offsets=(2) # 5)
#namespace=("") # "api_")
for i in $(seq 0 $(expr ${#funcs[@]} - 1)); do
  f=${funcs[$i]}
  offset=${offsets[$i]}
  namespaceSuffix=${namespace[$i]}
  echo "Extracting function $f"
  extractFunctionTxt $f $offset $namespaceSuffix
done

processInitFile
processDumpInitFile
processClockLoFile
processClockHiFile
#processDumpFile
processInitSimDataFile
