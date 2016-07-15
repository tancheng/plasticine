# Simple syhthesis script to use TSMC libraries
#
# Runs a top-down (entire chip in one compile) compile
# This is NOT suitable for big chips.

#
# set Libraries, work location, path - .synopsys_dc stuff really.
###################################################################
define_design_lib work -path ./work
file mkdir reports
file mkdir netlist

remove_design -all

#####################
# Path Variables
#####################
set SYN /cad/synopsys/dc_shell/latest/libraries/syn/
set TSMC_45 /cad/synopsys_EDK2/TSMCHOME/digital/Front_End/timing_power_noise/NLDM/tcbn45gsbwp_110a/

# Create milkyway database
create_mw_lib -technology /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/HVH_0d5_0/tsmcn45_10lm7X2ZRDL.tf -mw_reference_library /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/frame_only_HVH_0d5_0/tcbn45gsbwp "alu_rtl_LIB"

open_mw_lib "alu_rtl_LIB"

####################
# Set Design Library
#####################

#TSMC 45nm Library
set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set dw_lib     $SYN
set sym_lib    $TSMC_45
set target_lib $TSMC_45

set search_path [list ./ ../src/ $dw_lib $target_lib $sym_lib]

#
set hdlin_auto_save_templates true
# read in verilog
read_verilog -rtl [list behavioral_AND8.v]

elaborate behavioral_AND8
# set top level design
current_design behavioral_AND8

# link it to resolve references
link

# Build separate instance for multiply-instantiated sub-modules
uniquify

#
# Set technology constraints
###################################

#set_operating_conditions WCCOM
set_dont_use tcbn45gsbwpml/*D0BWP

# Wireload
#set_wire_load_selection_group "WireAreaFsgCon"
set_wire_load_mode top

#
# set what is driving the inputs
####################################
# option 1: driven by a standard flo (or a scanchain flop in this example):
set_driving_cell -library tcbn45gsbwpml -lib_cell INVD0BWP -pin ZN [ get_ports "*" -filter {@port_direction == in} ]

# option 2: or set input pin capacitance
#(cap is in same units as the library: use 'list_libs' and 'report_lib <libname>' to find units)
#set_load 0.00250 {In[0] In[1] In[2] In[3] In[4] In[5] In[6] In[7]}

#set output pin capacitance
# option 1: assume that you are driving a simple flop:
set_load [expr 400 * [load_of tcbn45gsbwpml/INVD0BWP/I]] [get_ports "*" -filter {@port_direction == out} ]

# option2: manualy set the load 
#set_load 0.0025 {Out}

# Instead-of, or in-addition-to setting the load, we can set the fanout
#set_fanout_load 4 [get_ports "*" -filter {@port_direction == out} ]


#
# Timing / Area constraints
###############################
# Since this is a combinational circuit with no clock, we'll just require 
# delay for input to output rather than clock period
set_max_delay -from [all_inputs] -to [all_outputs] 0.20

# We also don't want any indevidual net to take too long.
set_max_transition 0.1 [all_outputs]

# The clock line is unnecessary in this project... :)
#create_clock -period 30 [get_ports clk]

#set_clock_uncertainty -setup 0 [get_clocks clk]
#set_clock_uncertainty -hold 0 [get_clocks clk]

# set INTERNAL clock tree latency (modeling the clock tree)
#set_clock_latency 0 [get_clocks clk]

# Dont use set_propagated_clock until we have post-route data

# set input and output delays
set input_delay 0

#set_input_delay -max $input_delay -clock clk [get_ports "*" -filter {@port_direction == in} ]

set output_delay 0

#set_output_delay -max $output_delay -clock clk [get_ports "*" -filter {@port_direction == out} ]

#should try to set this to 90% of real area
set_max_area 80

# See the setup
report_design

check_design


###################################################
# Compile & Report
###################################################

current_design behavioral_AND8

set wire_load_mode top

# enable power calculation
saif_map -start
set_power_prediction
set_dont_touch data_reg
compile_ultra

###################################################
# Analyze Design
###################################################
redirect "reports/design_report" { report_design }
check_design
redirect "reports/design_check" {check_design }
#report_constraint -all_violators
#redirect "reports/constraint_report" {report_constraint -all_violators}
report_area
redirect "reports/area_report" { report_area }
#redirect "reports/area_hier_report" { report_area -hier }
report_power
redirect "reports/power_report" { report_power -analysis_effort hi }
report_timing
redirect "reports/timing_report_maxsm" { report_timing -significant_digits 4 }

check_error
redirect "reports/error_checking_report" { check_error }
redirect "reports"

#
# Save the Design Database
#
change_names -rules verilog -hierarchy
write -format ddc -hierarchy -output netlist/example.mapped.ddc
write -f verilog -hierarchy -output netlist/example.mapped.v
write_sdf netlist/example.mapped.sdf
write_sdc -nosplit netlist/example.mapped.sdc


# save scripts
write_script -hier -format dctcl -o AND8.wscr 
exit
