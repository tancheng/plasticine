#!/bin/bash

W="1 2 4 8 16 32 64 128 256 512 1024"

OUT="ffchar"
TMPDIR="char_tmp"
LOG="${OUT}_log.log"

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR
MODULE_NAME=FFTest

for w in $W; do
  echo "Generating ff for $inputs inputs, $w word width"
  bin/sadl --verilog --dest $TMPDIR $MODULE_NAME $w 2>&1 >> $LOG
  if [ $? -ne 0 ]; then
    echo "Microbenchmark generation failed, exit code $?"
    exit -1
  fi
  mv $TMPDIR/generated/$MODULE_NAME $OUT/ff_${w}
done

rm -rf $TMPDIR
