source param.tcl
file mkdir $PROJECT_PATH/pt_reports

#TSMC 45nm Library
set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set dw_lib     $SYN
set sym_lib    $TSMC_45
set target_lib $TSMC_45

set search_path [list ./ $SRC_PATH  $dw_lib $target_lib $sym_lib]

set power_enable_analysis "true"
read_verilog $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.v  

current_design $PROJECT_NAME
link

# Do static power analysis
set power_analysis_mode "averaged"
read_saif trace.saif -strip_path $SRC_PATH
report_switching_activity -list_not_annotated
read_parasitics -increment -format sbpf $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.sbpf  
report_power -verbose -hierarchy
redirect $PROJECT_PATH/pt_reports/pt_averaged_power_report {report_power -verbose -hierarchy}

# Do time-based power analysis
set power_analysis_mode "time_based"
read_vcd $PROJECT_PATH/trace.vcd 
report_switching_activity -list_not_annotated
read_parasitics -increment -format sbpf $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.sbpf  

report_power -verbose -hierarchy
redirect $PROJECT_PATH/pt_reports/pt_switching_power_report {report_power -verbose -hierarchy}
exit
