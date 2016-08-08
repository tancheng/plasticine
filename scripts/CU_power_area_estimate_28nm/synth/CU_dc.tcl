define_design_lib work -path ./work
source param.tcl

remove_design -all

file mkdir $PROJECT_PATH/dc_reports
file mkdir $PROJECT_PATH/pre_pr_gatelevel_netlist

set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set dw_lib     $SYN
set sym_lib    $TSMC_45
set target_lib $TSMC_45

set search_path [list $DC_SOURCE_PATH $dw_lib $target_lib $sym_lib]

set hdlin_auto_save_templates true
# preserve sequential logic
set hdlin_preserve_sequential true
# preserve unloaded sequential cells
set compile_delete_unloaded_sequential_cells false

# read in verilog
read_verilog -rtl $VERILOG_LIST
# set top level design
current_design $PROJECT_NAME
elaborate $PROJECT_NAME

# Build separate instance for multiply-instantiated sub-modules
uniquify

#set_operating_conditions WCCOM
set_dont_use tcbn45gsbwpml/*D0BWP

# Wireload
set_wire_load_mode top

set_driving_cell -library tcbn45gsbwpml -lib_cell INVD0BWP -pin ZN [ get_ports "*" -filter {@port_direction == in} ]

set_load [expr 400 * [load_of tcbn45gsbwpml/INVD0BWP/I]] [get_ports "*" -filter {@port_direction == out} ]

# Timing / Area constraints
set_max_delay -from [all_inputs] -to [all_outputs] 0.20

# We also don't want any indevidual net to take too long.
set_max_transition 0.1 [all_outputs]

# set input and output delays
set input_delay 0

###################################################
set wire_load_mode top

# enable power calculation
saif_map -start
# define clock
create_clock clk -name ideal_clock1 -period 1
compile_ultra

###################################################
# Analyze Design
###################################################
report_design
redirect $PROJECT_PATH/dc_reports/dc_design_report { report_design }
check_design
redirect $PROJECT_PATH/dc_reports/dc_design_check {check_design }
report_area
redirect $PROJECT_PATH/dc_reports/dc_area_report { report_area }
report_power
redirect $PROJECT_PATH/dc_reports/dc_power_report { report_power -analysis_effort hi }
report_timing
redirect $PROJECT_PATH/dc_reports/dc_timing_report_maxsm { report_timing -significant_digits 4 }
check_error
redirect $PROJECT_PATH/dc_reports/dc_error_checking_report { check_error }

change_names -rules verilog -hierarchy

write -format ddc -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.ddc
write -f verilog -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.v
write_sdf $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdf
write_sdc -nosplit $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdc

# save scripts
write_script -hier -format dctcl -o $PRE_PR_NETLIST_PATH/AND8.wscr
exit
