#!/bin/bash

#W="1 2 4 8 16 32 64 128 256 512 1024"
W="32"
NUMINPUTS="2 3 4 6 8 10 16 32 64 128 256 512 1024"

MODULE=MuxNArea
RUNNER_NAME="${MODULE}Char"
OUT=$RUNNER_NAME
TMPDIR="char_tmp"
LOG="${OUT}_log.log"

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for w in $W; do
  for inputs in $NUMINPUTS; do
    echo "Generating $RUNNER_NAME for $w word width, $inputs inputs"
    bin/sadl --verilog --dest $TMPDIR $RUNNER_NAME $w $inputs >> $LOG 2>&1
    if [ $? -ne 0 ]; then
      echo "Microbenchmark generation failed, exit code $?"
      exit -1
    fi
    mv $TMPDIR/generated/$RUNNER_NAME $OUT/${MODULE}_${w}_${inputs}
  done
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
