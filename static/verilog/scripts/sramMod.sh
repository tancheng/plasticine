#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <verilogSrc>"
  exit -1
fi

FILE=$1

function getAllModules() {
  file=$1
  echo $(grep "module .*(" $FILE | cut -f2 -d' ' | cut -f1 -d'(')
}

function getModules() {
  module=$1
  file=$2
  echo $(grep "module $module" $FILE | cut -f2 -d' ' | cut -f1 -d'(')
}

function getModuleStart() {
  module=$1
  file=$2
  echo $(grep -n "module ${module}(" ${file} | cut -f1 -d':')
}

function getModuleBodyStart() {
  module=$1
  file=$2
  moduleStart=$(getModuleStart $module $file)
  moduleDeclEnd=$(grep -n ");" ${file} | cut -f1 -d':' | sort -n | awk -v threshold="$moduleStart" '$1 >= threshold' | head -n1)
  echo $(expr $moduleDeclEnd + 1)
}

function getModuleEnd() {
  module=$1
  file=$2
  moduleStart=$(getModuleStart $module $file)
  echo $(grep -n "endmodule" ${file} | cut -f1 -d':' | sort -n | awk -v threshold="$moduleStart" '$1 >= threshold' | head -n1)
}

function extractModule() {
  module=$1
  file=$2
  moduleStart=$(getModuleStart $module $file)
  moduleEnd=$(getModuleEnd $module $file)

  ## Extract body into separate file, add a placeholder to append stuff in
  echo $(sed -n "$moduleStart,${moduleEnd}p" ${file})
}

function makeBlackBox() {
  module=$1
  file=$2

  moduleStart=$(getModuleBodyStart $module $file)
  trOffLine=$(expr $moduleStart)
  sed -i "${trOffLine}i\//synopsys translate_off" $file

  moduleEnd=$(getModuleEnd $module $file)
  trOnLine=$(expr $moduleEnd)
  sed -i "${trOnLine}i\//synopsys translate_on" $file
}

modules=$(getModules SRAM $FILE)

for s in $modules; do
  echo "Module $s"
  $(makeBlackBox $s $FILE)
done
