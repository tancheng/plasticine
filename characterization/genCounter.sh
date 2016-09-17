#!/bin/bash

W="1 2 4 8 16 32 64"

MODULE=Counter
RUNNER_NAME="${MODULE}Char"
OUT=$RUNNER_NAME
TMPDIR="char_tmp"
LOG="${OUT}_log.log"

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for w in $W; do
  echo "Generating $RUNNER_NAME for $w word width"
  bin/sadl --verilog --dest $TMPDIR $RUNNER_NAME $w 2>&1 >> $LOG
  if [ $? -ne 0 ]; then
    echo "Microbenchmark generation failed, exit code $?"
    exit -1
  fi
  mv $TMPDIR/generated/$RUNNER_NAME $OUT/${MODULE}_${w}
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
