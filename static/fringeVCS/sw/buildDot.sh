#!/bin/bash

uniqModules=$(ls *.dot | cut -f1 -d'_' | uniq)

for m in $uniqModules; do
  rm -rf $m && mkdir -p $m
  cp dot.mk $m/Makefile
  for f in "$m"*.dot; do
    sed -i "s/r[ ]*\([0-9][0-9]*\)/r\1/" $f
    cp $f $m/$(echo $f | cut -f2 -d'_');
  done
  make -C $m -j8
done
