#!/bin/bash

W=(1 2 3 4 5 6 8 10 12 16 20 24 32 40 48 64)

MODULE=CounterRC
RUNNER_NAME="${MODULE}Char"
OUT=$RUNNER_NAME
TMPDIR="char_tmp"
LOG="${OUT}_log.log"

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for wIdx in $(seq 0 $(expr ${#W[@]} - 1)); do
  w=${W[$wIdx]}
  echo "Generating $RUNNER_NAME for $w word width"
  bin/sadl --verilog --dest $TMPDIR $RUNNER_NAME $w >> $LOG 2>&1
  if [ $? -ne 0 ]; then
    echo "Microbenchmark generation failed, exit code $?"
    exit -1
  fi
  mv $TMPDIR/generated/$RUNNER_NAME $OUT/${MODULE}_${w}
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
