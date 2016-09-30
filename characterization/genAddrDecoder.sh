#!/bin/bash

D=(256 512 1024 2048 4096 8192)
V=(1 2 4 8 16 32)

OUT="AddrDecoder"
TMPDIR="${OUT}_tmp"
LOG="${OUT}_log.log"
MODULE_NAME=AddrDecoderChar

rm -rf $OUT $TMPDIR $LOG
mkdir $OUT $TMPDIR

for depthIdx in $(seq 0 $(expr ${#D[@]} - 1)); do
  for vIdx in $(seq 0 $(expr ${#V[@]} - 1)); do
    d=${D[$depthIdx]}
    v=${V[$vIdx]}
    echo "Generating decoder for depth '$d' and banks '$v'"
    bin/sadl --verilog --dest $TMPDIR $MODULE_NAME $d $v  >> $LOG 2>&1
    ecode=$?
    if [ $ecode -ne 0 ]; then
      echo "Microbenchmark generation failed, exit code $ecode"
      exit -1
    fi
    mv $TMPDIR/generated/$MODULE_NAME $OUT/${MODULE_NAME}_${d}_${v}
  done
done

cp scripts/launchJobs.sh $OUT/
rm -rf $TMPDIR
