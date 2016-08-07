source param.tcl

remove_design -all

file mkdir $PROJECT_PATH/dc_reports
file mkdir $PROJECT_PATH/pre_pr_gatelevel_netlist

# set up linking libraries
set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set dw_lib     $SYN
set sym_lib    $TSMC_45
set target_lib $TSMC_45

set search_path [list $DC_SOURCE_PATH $dw_lib $target_lib $sym_lib]

# milkyway lib creation
create_mw_lib -technology $MILKYWAY_TF -mw_reference_library $MILKYWAY_REF $MILKYWAY_LIB_NAME

open_mw_lib $MILKYWAY_LIB_NAME
check_library
define_design_lib WORK -path ./work

# preserve sequential logics
set hdlin_auto_save_templates true
set hdlin_preserve_sequential true
set compile_delete_unloaded_sequential_cells false

# read in verilog using analyze, elaborate, link and check design
read_verilog -rtl $VERILOG_LIST
analyze -format verilog $VERILOG_LIST
# remove SRAM as we don't need to elaborate the interface
remove_design [list SRAM]
list_designs
# set top level design
current_design $PROJECT_NAME
elaborate $PROJECT_NAME

link
check_design
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

# enable power calculation
saif_map -start

# define clock
# create_clock clk -name ideal_clock1 -period 1
create_clock clk -name ideal_clock4ghz -period $CLK_PERIOD
compile_ultra -gate_clock

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
redirect $PROJECT_PATH/dc_reports/dc_timing_report { report_timing -significant_digits 4 }
check_error
redirect $PROJECT_PATH/dc_reports/dc_error_checking_report { check_error }

change_names -rules verilog -hierarchy

write -format ddc -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.ddc
write -f verilog -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.v
write_sdf $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdf
write_sdc -nosplit $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdc

# save scripts
write_script -hier -format dctcl -o $PRE_PR_NETLIST_PATH/CU_dc_ver1.wscr
exit
