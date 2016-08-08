# clean up old design values
function cleanup {
	rm *.log
	rm *.txt
	rm *.svf
	rm *.vcd
	rm Plasticine_sim
	rm -rf Plasticine
	rm -rf log
	rm -rf alib-52
	rm -rf icc_reports
	rm -rf dc_reports
	rm -rf pt_reports
	rm -rf pre_pr_gatelevel_netlist
	rm -rf post_pr_gatelevel_netlist
	rm -rf work
}

while read cmd var val
do
#	echo $cmd $var $val
	if $cmd == "set"
	then
		echo "export $var=$val"
		eval "export $var=$val"
	fi
done < ./param.tcl
cleanup
mkdir log
echo "Running Design Compiler..."
dc_shell-t -f synth/CU_dc_ver1.tcl > dc.log
echo "Running IC Compiler..."
icc_shell -f synth/CU_icc.tcl > icc.log
# echo "Running iverilog simulation..."
# iverilog -o ${PROJECT_NAME}_sim  post_pr_gatelevel_netlist/${PROJECT_NAME}.output.v verif/${PROJECT_NAME}_tb.v /cad/synopsys_EDK2/TSMCHOME/digital/Front_End/verilog/tcbn45gsbwp_110b/tcbn45gsbwp.v
# vvp ${PROJECT_NAME}_sim > sim.log
# echo "Generating saif file for averaged power analysis..."
# vcd2saif -input trace.vcd -output trace.saif
# echo "Running PrimeTime power estimator..."
# pt_shell -f synth/CU_pt.tcl > pt.log
mv *.log log
mv *.txt log
mv *.svf log
exit
