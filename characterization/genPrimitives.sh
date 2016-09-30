#!/bin/bash

W=(1 2 3 4 5 6 8 10 12 16 20 24 32 40 48 64)
#OP=("*" "+" "-" "/" "&" "|" "==" ">" "<")
#OPNAME=("mul" "add" "sub" "div" "and" "or" "eq" "gt" "lt")
OP=(">" "<")
OPNAME=("gt" "lt")

OUT="comparators"
TMPDIR="${OUT}_tmp"
LOG="${OUT}_log.log"
MODULE_NAME=IntPrimitiveChar

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for opIdx in $(seq 0 $(expr ${#OP[@]} - 1)); do
  for wIdx in $(seq 0 $(expr ${#W[@]} - 1)); do
    op=${OP[$opIdx]}
    opName=${OPNAME[$opIdx]}
    w=${W[$wIdx]}
    echo "Generating op '$op' for $w word width"
    bin/sadl --verilog --dest $TMPDIR $MODULE_NAME $w "$op"  >> $LOG 2>&1
    ecode=$?
    if [ $ecode -ne 0 ]; then
      echo "Microbenchmark generation failed, exit code $ecode"
      exit -1
    fi
    mv $TMPDIR/generated/$MODULE_NAME $OUT/int_${opName}_${w}
  done
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
