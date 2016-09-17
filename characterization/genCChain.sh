#!/bin/bash

W="1 4 8 16 32 64"
NUMCOUNTERS="1 2 3 4 5 6 8 10"

MODULE=CounterChain
RUNNER_NAME="${MODULE}Char"
OUT=$RUNNER_NAME
TMPDIR="char_tmp"
LOG="${OUT}_log.log"

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for w in $W; do
  for num in $NUMCOUNTERS; do
    echo "Generating $RUNNER_NAME for $w word width, $num counters"
    bin/sadl --verilog --dest $TMPDIR $RUNNER_NAME $w $num 2>&1 >> $LOG
    if [ $? -ne 0 ]; then
      echo "Microbenchmark generation failed, exit code $?"
      exit -1
    fi
    mv $TMPDIR/generated/$RUNNER_NAME $OUT/${MODULE}_${w}_${num}
  done
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
