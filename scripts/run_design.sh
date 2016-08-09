
TOPLEVEL="rpc_asm"
width=16

get_OPCODE(){
    if [ $OPT = "ADD" ]
    then
        OPCODE_p="4'b0000"
    elif [ $OPT = "SUB" ]
    then
        OPCODE_p="4'b0001"
    elif [ $OPT = "MUL" ]
    then
        OPCODE_p="4'b0110"
    elif [ $OPT = "DIV" ]
    then
        OPCODE_p="4'b0111"
    elif [ $OPT = "SQR" ]
    then
        OPCODE_p="4'b1000"
    fi
}

mkdir -p out
mkdir -p log
cd out/

  # Design Compiler 
# rm ../log/*
# rm -rf *
# dc_shell -topo -f ../../scripts/synthesize.tcl > ../log/dc.log
  sed -i "s/${TOPLEVEL}_width${width}/${TOPLEVEL}/g" final_${TOPLEVEL}.v
# find . -type f ! -name 'final_'${TOPLEVEL}'.v' -delete
# rm -rf */
# dc_shell -topo -f ../../scripts/${TOPLEVEL}_synthesize_rf.tcl > ../log/dc_rf.log

#  # ICC Compiler
  icc_shell -f ../../scripts/placeAndRoute.tcl > ../log/icc.log

 ## VCS
  OPTS=( "ADD" "SUB" "MUL" "DIV" "SQR" )
  cp final_${TOPLEVEL}.v ../../power/src/${TOPLEVEL}.v
  cd ../../power/src/

  sed -i '1i\`timescale 1ns \/ 1ns' ${TOPLEVEL}.v
  chmod u+wrx run_vcs.sh

  
  for OPT in "${OPTS[@]}"
  do
      mkdir -p ../scratch_$OPT/
      cd ../scratch_$OPT
      rm -r *
      cp ../src/* .
      get_OPCODE
      sed -i "s/parameter OPCODE.*/parameter OPCODE = $OPCODE_p;/g" controller.v
      ./run_vcs.sh test_tb > postsim_$OPT.log &
  done
  wait
  echo "finish vcs power simulation"

  # Power script
  cd ../../testbench/out/

  for OPT in "${OPTS[@]}"
  do
      vcd2saif -input ../../power/scratch_$OPT/${TOPLEVEL}.vcd -output ${TOPLEVEL}_$OPT.saif > ../log/vcd2saif_$OPT.log &
  done

  wait
      
  for OPT in "${OPTS[@]}"
  do
      rm ../../power/scratch_$OPT/${TOPLEVEL}.vcd
      sed -i "s/read_saif.*/read_saif -input \${TOPLEVEL}_$OPT.saif -instance_name test_tb\/U\/Unit/g" ../../scripts/power.tcl
      sed -i "s/ power_report.*/ power_report_${OPT}/g" ../../scripts/power.tcl
      sed -i "s/detailed_power_report.*/detailed_power_report_${OPT}/g" ../../scripts/power.tcl
      dc_shell -topo -f ../../scripts/power.tcl > ../log/dc_power_$OPT.log   
  done
