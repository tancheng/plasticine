#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <sim_log>"
  exit -1
fi

FILE=$1
OUTDIR=dot

function labelStartLine() {
  label=$1
  file=$2
  echo $(grep -n "^${label}" ${file} | cut -f1 -d':')
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

  labelStart=$(labelStartLine $label $file)
  labelEnd=$(labelEndLine $label $file)

  ## Extract body into separate file, add a placeholder to append stuff in
  echo $(sed -n "$(expr $labelStart),$(expr $labelEnd)p" ${file})
}

function extractLine() {
  number=$1
  file=$2
  echo $(sed -n "$(expr $number),$(expr $number)p" $file)  # 
}

function extractComment() {
  line=$1
#  echo "[extractComment] line=$line"
#  t1=$(echo $line | grep -o "// .*")
#  echo "[extractComment] $t1"
  echo $(echo $line | grep -o "// .*" | cut -f3 -d'/')
}

function extractCommentLabel() {
  line=$1
  cycleNum=$(echo $line | grep -o "// .*" | grep -o [0-9]*)
  echo "$cycleNum"
}

function extractGraphName() {
  line=$1
  graph=$(echo $line | cut -f2 -d' ')
  echo "$graph"
}

function extractDigraphName() {
  number=$1
  file=$2
  line=$(extractLine $number "$file")
  graph=$(extractGraphName "$line")
  cycleNum=$(extractCommentLabel "$line")
  echo "${graph}_$cycleNum"
}

starts=$(grep -n "digraph .* {" sim.log | cut -f1 -d':')

rm -rf $OUTDIR
mkdir -p $OUTDIR
cp buildDot.sh $OUTDIR
cp dot.mk $OUTDIR

for s in $starts; do
  l="$(extractLine $s $FILE)"
  comment=$(extractComment "$l")
  label=$(extractCommentLabel "$l")
  end=$(grep -n "} //[a-zA-Z \t]*${label}$" $FILE | cut -f1 -d':')
  out=$OUTDIR/$(extractDigraphName $s $FILE).dot
  echo "Digraph $l: $s to $end, into $out"
  sed -n "$(expr $s),$(expr $end)p" $FILE > $out
done

pushd $OUTDIR && ./buildDot.sh && popd

#files=$(for s in $starts; do echo $(extractDigraphName $s $FILE); done)
#echo "FILES: $files"
#sorted=$(echo $files | sort -n)
#echo "SORTED: $sorted"

