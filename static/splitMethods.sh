#!/bin/bash

EXE=`grep -o "public mod_t" *.h -r | cut -f1 -d'.'`

NUM=20000
## Split the header file
GLOBALS=globals.h
GLOBALDEFS=globalDefs.h
rm -f $GLOBALS $GLOBALDEFS
echo "#ifndef __GLOBALS_H__" >> $GLOBALS
echo "#define __GLOBALS_H__" >> $GLOBALS
echo "#include \"emulator.h\"" >> $GLOBALS
echo "#ifndef __GLOBALDEFS_H__" >> $GLOBALDEFS
echo "#define __GLOBALDEFS_H__" >> $GLOBALDEFS
echo "#include \"emulator.h\"" >> $GLOBALDEFS

sed -i "s/private/public/" ${EXE}.h
grep "^[ ][ ]*dat_t<[0-9][0-9]*> .*;" ${EXE}.h >> $GLOBALS
grep "^[ ][ ]*mem_t<[0-9][0-9,]*> .*;" ${EXE}.h >> $GLOBALS
grep "^[ ][ ]*val_t __rand_seed;" ${EXE}.h >> $GLOBALS
grep "^[ ][ ]*clk_t clk;" ${EXE}.h >> $GLOBALS
grep "^[ ][ ]*dat_t<[0-9][0-9]*> .*;" ${EXE}.h >> $GLOBALDEFS
grep "^[ ][ ]*mem_t<[0-9][0-9,]*> .*;" ${EXE}.h >> $GLOBALDEFS
grep "^[ ][ ]*val_t __rand_seed;" ${EXE}.h >> $GLOBALDEFS
grep "^[ ][ ]*clk_t clk;" ${EXE}.h >> $GLOBALDEFS
sed -i 's/^[ ][ ]*dat_t/extern dat_t/' $GLOBALS
sed -i 's/^[ ][ ]*mem_t/extern mem_t/' $GLOBALS
sed -i 's/^[ ][ ]*clk_t/extern clk_t/' $GLOBALS
sed -i 's/^[ ][ ]*val_t/extern val_t/' $GLOBALS

sed -i "s/^[ ][ ]*dat_t<[0-9][0-9]*> .*;//" ${EXE}.h
sed -i "s/^[ ][ ]*mem_t<[0-9][0-9,]*> .*;//" ${EXE}.h
sed -i "s/^[ ][ ]*val_t __rand_seed;//" ${EXE}.h
sed -i "s/^[ ][ ]*clk_t clk;//" ${EXE}.h

sed -i "3i #include \"$GLOBALS\"" ${EXE}.h
echo "#endif" >> $GLOBALS
echo "#endif" >> $GLOBALDEFS

FILE=${EXE}.cpp
EMU_FILE=${EXE}-emulator.cpp
FILECPY=$FILE
#FILECPY=${EXE}_copy.cpp

#cp $FILE $FILECPY
sed -i "s/\&mod->/\&/" $FILECPY
sed -i 's/module\.clk/clk/g' $EMU_FILE
sed -i "2i #include \"$GLOBALDEFS\"" ${FILECPY}

## Get starting and ending lines
initStart=`grep -n "void ${EXE}_t::init" ${FILE} | cut -f1 -d':'`
initEnd=`grep -n "^}$" ${FILE} | cut -f1 -d':' | sort -n | awk -v threshold="$initStart" '$1 > threshold' | head -n1`

## Extract body into separate file, add a placeholder to append stuff in
sed -n "$(expr $initStart + 2),$(expr $initEnd - 1)p" ${FILE} > init.txt

foo=$(expr $initStart + 1)
## Delete chopped up lines from file
sed -i "${foo}i\//splitter" ${FILECPY}
sed -i -e "$(expr $initStart + 3), $(expr $initEnd)d" ${FILECPY}

## Chop up file into several pieces
split -l ${NUM} --additional-suffix="init.cpp" --numeric-suffixes init.txt

## Append common header files, method call names for each file
## Insert method calls in place (don't forget semicolons)
METHODS=methods.txt
HEADER=init.h
rm -f $METHODS $HEADER
for file in `ls x[0-9]*init.cpp`; do
  f=`echo $file | cut -f1 -d'.'`
  sed -i "1i #include \"${EXE}.h\"" $file
  sed -i "2i #include \"${GLOBALS}\"" $file
  sed -i "3i void $f() {" $file
  echo "}" >> $file
  echo "$f();" >> $METHODS
  echo "void $f();" >> $HEADER
done

sed -i "3i #include\"$HEADER\"" ${FILECPY}
sed -i "/\/\/splitter/r ${METHODS}" ${FILECPY}

#
#
clockLoStart=`grep -n "void ${EXE}_t::clock_lo" ${FILE} | cut -f1 -d':'`
clockLoEnd=`grep -n "^}$" ${FILE} | cut -f1 -d':' | sort -n | awk -v threshold="$clockLoStart" '$1 > threshold' | head -n1`

## Extract body into separate file, add a placeholder to append stuff in
sed -n $(expr $clockLoStart + 1),$(expr $clockLoEnd - 1)p ${FILE} > clockLo.txt

foo=$(expr $clockLoStart + 1)
sed -i "${foo}i\//clockLoSplitter" ${FILECPY}

## Delete chopped up lines from file
sed -i -e "$(expr $clockLoStart + 2), $(expr $clockLoEnd)d" ${FILECPY}

## Move "val_t T<num>;" declarations into a global header file
CLOCKLO_GLOBALS=clockLoGlobals.h
CLOCKLO_GLOBALDEFS=clockLoGlobalDefs.h
rm -f $CLOCKLO_GLOBALS $CLOCKLO_GLOBALDEFS
echo "#ifndef __CLOCKLO_GLOBALS_H__"    >> $CLOCKLO_GLOBALS
echo "#define __CLOCKLO_GLOBALS_H__"    >> $CLOCKLO_GLOBALS
echo "#include \"emulator.h\""  >> $CLOCKLO_GLOBALS
echo "#ifndef __CLOCKLO_GLOBALDEFS_H__" >> $CLOCKLO_GLOBALDEFS
echo "#define __CLOCKLO_GLOBALDEFS_H__" >> $CLOCKLO_GLOBALDEFS
echo "#include \"emulator.h\""  >> $CLOCKLO_GLOBALDEFS
grep "^[ ][ ]*val_t T[0-9]*;" clockLo.txt >> $CLOCKLO_GLOBALS
grep "^[ ][ ]*val_t T[0-9]*;" clockLo.txt >> $CLOCKLO_GLOBALDEFS
sed -i "s/^[ ][ ]*val_t T[0-9]*;//" clockLo.txt
sed -i 's/^[ ][ ]*val_t/extern val_t/' $CLOCKLO_GLOBALS
echo "#endif" >> $CLOCKLO_GLOBALS
echo "#endif" >> $CLOCKLO_GLOBALDEFS

sed -i "4i #include\"$CLOCKLO_GLOBALDEFS\"" ${FILECPY}

## Replace "this" with "ptr"
sed -i "s/this/ptr/g" clockLo.txt

## Chop up file into several pieces
split -l ${NUM} --additional-suffix="clockLo.cpp" --numeric-suffixes clockLo.txt

## Append common header files, method call names for each file
METHODS=methods.txt
HEADER=clockLo.h
rm -f $METHODS $HEADER
for file in `ls x[0-9]*clockLo.cpp`; do
  f=`echo $file | cut -f1 -d'.'`
  sed -i "1i #include \"${EXE}.h\"" $file
  sed -i "2i #include \"${CLOCKLO_GLOBALS}\"" $file
  sed -i "3i #include \"${GLOBALS}\"" $file
  sed -i "4i void $f(${EXE}_t *ptr, dat_t<1> reset, bool assert_fire) {" $file
  echo "}" >> $file
  echo "$f(this, reset, assert_fire);" >> $METHODS
  echo "void $f(${EXE}_t *ptr, dat_t<1> reset, bool assert_fire);" >> $HEADER
done

sed -i "5i #include\"$HEADER\"" ${FILECPY}
sed -i "/\/\/clockLoSplitter/r ${METHODS}" ${FILECPY}


## dump_init
## Get starting and ending lines
dumpInitStart=`grep -n "void ${EXE}_t::dump_init" ${FILE} | cut -f1 -d':'`
dumpInitEnd=`grep -n "^}$" ${FILE} | cut -f1 -d':' | sort -n | awk -v threshold="$dumpInitStart" '$1 > threshold' | head -n1`

if [ $(expr $dumpInitStart + 1) -lt $(expr $dumpInitEnd) ]; then
  ## Extract body into separate file, add a placeholder to append stuff in
  sed -n "$(expr $dumpInitStart + 1),$(expr $dumpInitEnd - 1)p" ${FILE} > dumpInit.txt

  foo=$(expr $dumpInitStart + 1)
  ## Delete chopped up lines from file
  sed -i "${foo}i\//dumpInitSplitter" ${FILECPY}
  sed -i -e "$(expr $dumpInitStart + 2), $(expr $dumpInitEnd)d" ${FILECPY}

  ## Chop up file into several pieces
  split -l ${NUM} --additional-suffix="dumpInit.cpp" --numeric-suffixes dumpInit.txt

  ## Append common header files, method call names for each file
  ## Insert method calls in place (don't forget semicolons)
  METHODS=methods.txt
  HEADER=dumpInit.h
  rm -f $METHODS $HEADER
  for file in `ls x[0-9]*dumpInit.cpp`; do
    f=`echo $file | cut -f1 -d'.'`
    sed -i "1i #include \"${EXE}.h\"" $file
    sed -i "2i void $f(FILE *f) {" $file
    echo "}" >> $file
    echo "$f(f);" >> $METHODS
    echo "void $f(FILE *f);" >> $HEADER
  done

  sed -i "6i #include\"$HEADER\"" ${FILECPY}
  sed -i "/\/\/dumpInitSplitter/r ${METHODS}" ${FILECPY}
fi
#
#clockHiStart=`grep -n "void ${exe}_t::clock_hi" ${FILE} | cut -f1 -d':'`
#clockHiEnd=`grep -n "^}$" ${FILE} | cut -f1 -d':' | sort -n | awk -v threshold="$clockHiStart" '$1 > threshold' | head -n1`
#sed -n $(expr $clockHiStart + 1),$(expr $clockHiEnd - 1)p ${FILE} > clockHi.txt

