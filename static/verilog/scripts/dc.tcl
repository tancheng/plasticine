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

set search_path [list ./ $DC_SOURCE_PATH $dw_lib $target_lib $sym_lib]

# milkyway lib creation
create_mw_lib -technology $MILKYWAY_TF -mw_reference_library $MILKYWAY_REF $MILKYWAY_LIB_NAME

open_mw_lib $MILKYWAY_LIB_NAME
check_library
define_design_lib WORK -path ./work

# read in verilog using analyze, elaborate, link and check design
read_verilog -rtl $VERILOG_LIST
analyze -format verilog $VERILOG_LIST

## SRAMs are in the Scratchpad module, set_dont_touch them
current_design PCUWrapper
current_design PCU
current_design FIFOCore
set srams [get_instances SRAM]
foreach sram $srams {
  set_dont_touch $sram
}
list_designs
current_design FIFOCore_4
set srams [get_instances SRAM_4]
foreach sram $srams {
  set_dont_touch $sram
}
current_design Scratchpad
set srams [get_instances SRAM_68]
foreach sram $srams {
  set_dont_touch $sram
}


# set top level design
current_design $PROJECT_NAME
elaborate $PROJECT_NAME
link
# uniquify

#set_operating_conditions WCCOM
set_dont_use tcbn45gsbwpml/*D0BWP

# Wireload
# set_wire_load_mode top
# set_driving_cell -library tcbn45gsbwpml -lib_cell INVD0BWP -pin ZN [ get_ports "*" -filter {@port_direction == in} ]
# set_load [expr 400 * [load_of tcbn45gsbwpml/INVD0BWP/I]] [get_ports "*" -filter {@port_direction == out} ]

# Timing / Area constraints
# set_max_delay -from [all_inputs] -to [all_outputs] 0.20

# We also don't want any indevidual net to take too long.
# set_max_transition 0.1 [all_outputs]

# set input and output delays
# set input_delay 0

# Area constraints
# currently we are not meeting timing design. design compiler prioritize slack over area. so we
# don't optimize for area for now.

###################################################


# define clock
# create_clock clk -name ideal_clock1 -period 1
create_clock clock -name clk -period $CLK_PERIOD
# compile_ultra -gate_clock
# disable OPT-1206
# set compile_seqmap_propagate_constants false
# disable OPT-1215
# set compile_enable_register_merging false
compile_ultra -gate_clock
###################################################
# Analyze Design
###################################################
redirect $PROJECT_PATH/dc_reports/dc_design_report { report_design }
redirect $PROJECT_PATH/dc_reports/dc_design_check {check_design }
redirect $PROJECT_PATH/dc_reports/dc_area_report { report_area }
redirect $PROJECT_PATH/dc_reports/dc_power_report { report_power -analysis_effort hi }
redirect $PROJECT_PATH/dc_reports/dc_timing_report { report_timing -significant_digits 4 }
redirect $PROJECT_PATH/dc_reports/dc_error_checking_report { check_error }

change_names -rules verilog -hierarchy

write -format ddc -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.ddc
write -f verilog -hierarchy -output $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.v
write_sdf $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdf
write_sdc -nosplit $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdc

# save scripts
write_script -hier -format dctcl -o $PRE_PR_NETLIST_PATH/CU_dc_ver1.wscr
exit
