#mples of Synopsys Design Compiler
# synthesis script for OR1200 IP core

#create_mw_lib -open -technology ../nangate.tf -mw_reference_library ../nangate_lib argus_lib

#design

set TOPLEVEL "rpc_asm"
 
open_mw_lib ${TOPLEVEL}_lib
#set_tlu_plus_files -max_tluplus ../max.tlu -tech2itf_map ../NCSU.map
set_tlu_plus_files -max_tluplus ../nangate_files/nangate_max.tlu -tech2itf_map ../nangate_files/nangate.map

#To generate power measurements
set power_preserve_rtl_hier_names true
#For multi-core synthesis
set_host_options -max_cores 16

set WRITE_VERILOG  1
set WRITE_VHDL  0
#set TOPLEVEL "cc_top"
#set TOPLEVEL "argus1400_mem_hierarchy"
#set TOPLEVEL "L1_cache"


#icc

#vtvt, nangate and stm90nm are the only valid TECHs
set TECH "nangate"
set CLK  "clk"
set RST  "reset"
set CLK_PERIOD  20
# Push hard 
set MAX_AREA  0		
# none, low, medium, high, ultra
set EFFORT  "ultra"
# yes, no 
set DO_VERIFY "no"
set DO_FLATTEN "yes" 
#100ps
set CLK_UNCERTAINTY  5
# Clk to Q in technology time units 
#set DFF_CKQ  0.2		
# Setup time in technology time units 
set DFF_SETUP  0.1		

# Starting timestamp
sh date

# Set some basic variables related to environment

# Enable Verilog HDL preprocessor
set hdlin_enable_vpp "true"

# Set log path 
set LOG_PATH "../log/"

# Set gate-level netlist path 
set GATE_PATH "../out/"

# Optimize adders
set synlib_model_map_effort high
set hlo_share_effort low

set STAGE final

# Synthetic libraries
set snps [get_unix_variable "SYNOPSYS"]

set synthetic_library [concat $snps/libraries/syn/dw_foundation.sldb]

if {$TECH == "nangate"} {

set search_path { "." "../nangate_files" }

set target_library { "nangate_lib.db" }
set symbol_library { "generic.sdb" }
#set additional_libraries { "" }

}

if {$TECH == "vtvt" } {

# Search paths 

  set search_path { "." "/libs/Artisan/aci/sc-x/synopsys/" \
 "/libs/Artisan/aci/sc-x/symbols/synopsys/" \
 "/libs/art_rams/" \
 "/libs/vs_rams/ /usr/dc/libraries/syn/" \
 "/libs/Virtual_silicon/UMCL18U250D2_2.1/design_compiler/" \
 "../../quartus/dc_library/" }

  set target_library { "vtvtlib25.db" }
  set symbol_library { "generic.sdb" }
}
if {$TECH == "stm90nm"} {

  set search_path { "." "/usr/research/arch1/stm_cmos90/cmos090_61/CORE90GPSVT_SNPS-AVT-CDS_2.2/SYNOPSYS/" \
                  "/usr/research/arch1/stm_cmos90/cmos090_61/CORE90GPSVT_SNPS-AVT-CDS_2.2/libs/" \
                  "/usr/research/arch1/stm_cmos90/cmos090_61/CORX90GPSVT_SNPS-AVT-CDS_4.3/libs/" }
  set target_library { "CORE90GPSVT_nom_1.00V_25C.db" }
  set additional_libraries { "CORX90GPSVT_nom_1.00V_25C.db" }
}
if {$TECH == "stm90nm"} {
  set link_library [concat { "*" } $target_library $synthetic_library $additional_libraries]
} else {
  set link_library [concat { "*" } $target_library $synthetic_library ]
}

#read_ddc final_rf_${TOPLEVEL}.ddc
read_ddc final_${TOPLEVEL}.ddc


#
# Load HDL source files
#

# Set search path for verilog include files 
set search_path [concat $search_path $GATE_PATH]


# Set design top
current_design $TOPLEVEL

#
# Apply constraints
#

#Set Technology Specifications
set DFF_CELL DFF_X2
set LIB_DFF_D "nangate_lib/$DFF_CELL/D"
#set OPER_COND WCCOM

create_clock $CLK -period $CLK_PERIOD
set_clock_uncertainty $CLK_UNCERTAINTY [all_clocks]

#This is commented because We aren't actually creating a clock tree, so optimize the clock
#If you are creating a clock tree w/ Astro, uncomment this
set_dont_touch_network [all_clocks]

# Reset constraints 
# TODO
remove_driving_cell $RST
set_drive 0 $RST
set_dont_touch_network $RST

# All inputs except reset and clock 
set all_inputs_wo_rst_clk [remove_from_collection [remove_from_collection [all_inputs] [get_port $CLK]] [get_port $RST]]

# Set output delays and load for output signals
#
# All outputs are assumed to go directly into
# external flip-flops for the purpose of this
# synthesis with a FO4 load

set_output_delay $DFF_SETUP -clock $CLK [all_outputs]
set_load [expr [load_of $LIB_DFF_D] * 4] [all_outputs]
extract_physical_constraints "./${TOPLEVEL}.def"
# Input delay and driving cell of all inputs
#
# All these signals are assumed to come directly from
# flip-flops for the purpose of this synthesis
#
#
#set_input_delay DFF_CKQ -clock CLK all_inputs_wo_rst_clk
set_driving_cell -lib_cell $DFF_CELL -pin Q $all_inputs_wo_rst_clk 

#Prevent assignment statements in the Verilog netlist
set_fix_multiple_port_nets -all -buffer_constants $TOPLEVEL
#To stop the clock from giving us REALLY high power numbers
set high_fanout_net_threshold 0
set high_fanout_net_pin_capacitance 0.001

# Set design fanout 

set_max_fanout 5 $TOPLEVEL
set_max_capacitance 0.7 [all_inputs]
#read_file -format ddc final_rf_${TOPLEVEL}.ddc
extract_physical_constraints "${TOPLEVEL}.def"
#Optimize all near-critical paths to give extra slack for layout
#set c_range [$CLK_PERIOD * 0.10]
#group_path -critical_range [expr $CLK_PERIOD * 0.10] -name $CLK -to $CLK


# #set_structure -boolean false -timing true

current_design $TOPLEVEL

#Link all blocks and uniquify them 
link 
uniquify

read_saif -input ${TOPLEVEL}.saif -instance_name test_tb/U/Unit
#
# Report Power
#
set_power_prediction
set_max_dynamic_power 0.1 W
set_max_leakage_power 9 mW
compile_ultra -incremental 
# if { $WRITE_VERILOG == 1 } {
#   set outfile $GATE_PATH
#   append outfile $STAGE "_" $TOPLEVEL ".v"
#   write -hierarchy -format verilog -output $outfile
# }
# set outfile $GATE_PATH
# append outfile $STAGE "_" $TOPLEVEL "_drc_fixed" ".ddc"
# write -hierarchy -format ddc -output $outfile


set POWER_PATH $LOG_PATH
append POWER_PATH power_report
report_power -nosplit > ${POWER_PATH}

set POWER_PATH $LOG_PATH
append POWER_PATH detailed_power_report
report_power -hier -verbose -analysis_effort high -net -cell > ${POWER_PATH}

set SAIF_PATH $LOG_PATH
append SAIF_PATH "saif_report"
report_saif -hier -missing > ${SAIF_PATH}

set AREA_PATH $LOG_PATH
append AREA_PATH "area_report"
report_area > ${AREA_PATH}

sh date

 exit
