# Examples of Synopsys Design Compiler
# synthesis script for OR1200 IP core

set width 16
set TOPLEVEL "rpc_asm"
set PTOPLEVEL ${TOPLEVEL}_width${width}

create_mw_lib -open -technology ../nangate_files/nangate.tf -mw_reference_library ../nangate_files/nangate_lib ${TOPLEVEL}_lib
set_tlu_plus_files -max_tluplus ../nangate_files/nangate_max.tlu -tech2itf_map ../nangate_files/nangate.map

#To generate power measurements
set power_preserve_rtl_hier_names true
#For multi-core synthesis
set_host_options -max_cores 16

set WRITE_VERILOG  1
set WRITE_VHDL  1

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

# Set RAMS_PATH 
set RAMS_PATH "../../../lib/"

# Set RTL source path 
set VHDL_PATH [concat "/autofs/home/yz138/PrattFellow/rpc/hardware/src/" \
                      "/autofs/home/yz138/PrattFellow/rpc/hardware/src/rpc_asm/" ]

# Optimize adders
set synlib_model_map_effort high
set hlo_share_effort low

set STAGE final

# Source files 

set vhdl_files [concat { \
"rp_AddSub_2.vhd" \
"rp_comp_31b.vhd" \
"rp_expBias_9b.vhd" \
"rp_expAddSub_9b_1.vhd" \
"rp_expAddSub_9b_2.vhd" \
"rp_fracAddSub_32b.vhd" \
"rp_lshift_32b.vhd" \
"rp_LZ_detect.vhd" \
"rp_oprfmta.vhd" \
"rp_oprfmtb.vhd" \
"rp_ResFormat.vhd" \
"rp_Round1.vhd" \
"rp_Round2.vhd" \
"rp_RSAgen.vhd" \
"rp_rshift_32b.vhd" \
"rp_stage1_ctrl.vhd" \
"rp_stage2_ctrl.vhd" \
"rp_stage3_ctrl.vhd" \
"rp_stage4_ctrl.vhd" \
"rp_stage5_ctrl.vhd" \
"rp_sticky_1.vhd" \
"rp_sticky_2.vhd" \
"rp_tmpIX.vhd" \
"rpc_asm.vhd" } ]

# Generated Files

# Top-level controllers

# Storage Structuers commented out to remove them from synthesis flattening


# Files called with parameters must also be analyzed and turned into a template 

#Storage parameter files

# Commented out to make RFRAM a blackbox

#set report_modules { "argus_mult_checker" \
 "argus_controlflow" "argus_version_regs_vw1" \
 "argus_simple_alu_checker" "argus_execute_checker" \
 "argus_lsu" "argus_alu" "argus_operandmuxes" \
 "argus_checker_alu" \
 "argus_wbmux" "argus_shift_checker" \
 "shift_extend_mask" "bitmask32" "argus_edc" \
 "argus_adder_checker" "MERSENNE_MOD_32_5" \
 "argus_dgs_compare" "argus_instruction_signature" \
 "argus_tag_checker" \
 "crc5x5" "crc5x6" "or1200_cpu" }

#
# Load libraries
#

# Synthetic libraries
set snps [get_unix_variable "SYNOPSYS"]

set synthetic_library [concat $snps/libraries/syn/dw_foundation.sldb]
#set dw_prefer_mc_inside true

if {$TECH == "nangate"} {

set search_path { "." "../nangate_files" }

set target_library { "nangate_lib.db" }
set symbol_library { "generic.sdb" }
set link_library $target_library
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
  #set additional_libraries { "" }
}
if {$TECH == "stm90nm"} {

  set search_path { "." "/usr/research/arch1/stm_cmos90/cmos090_61/CORE90GPSVT_SNPS-AVT-CDS_2.2/SYNOPSYS/" \
                  "/usr/research/arch1/stm_cmos90/cmos090_61/CORE90GPSVT_SNPS-AVT-CDS_2.2/libs/" \
                  "/usr/research/arch1/stm_cmos90/cmos090_61/CORX90GPSVT_SNPS-AVT-CDS_4.3/libs/" }
  set target_library { "CORE90GPSVT_nom_1.00V_25C.db" }
  set additional_libraries { "CORX90GPSVT_nom_1.00V_25C.db" }
  set symbol_library { "CORE90GPSVT.sdb" }
}

if {$TECH == "stm90nm"} {
  set link_library [concat { "*" } $target_library $synthetic_library $additional_libraries]
} else {
  set link_library [concat { "*" } $target_library $synthetic_library ]
}


#
# Load HDL source files
#

# Set search path for verilog include files 
set search_path [concat $search_path $VHDL_PATH ]

# foreach file $parameter_files {
# 	set ANALYZE_PATH $LOG_PATH
# 	append ANALYZE_PATH "analyze_file_" $file ".log"
# 	analyze -f vhdl $file > ${ANALYZE_PATH}
# }


foreach file $vhdl_files {
  set ANALYZE_PATH $LOG_PATH
  append ANALYZE_PATH "analyze_file_" $file ".log"
#   echo $file
# read_vhdl $file > ${ANALYZE_PATH}
  analyze -f vhdl $file > ${ANALYZE_PATH}
}

#foreach file $verilog_files {
#  set ANALYZE_PATH $LOG_PATH
#  append ANALYZE_PATH "analyze_file_" $file ".log"
## read_sverilog $file > ${ANALYZE_PATH}  
#  analyze -f sverilog $file > ${ANALYZE_PATH}  
# }

elaborate $TOPLEVEL -architecture rtl -parameter "width=$width"

# analyze designs with parameters 
#foreach file $parameter_files {
	#set ANALYZE_PATH $LOG_PATH
  #append ANALYZE_PATH "analyze_file_" $file ".log"
  #analyze -f sverilog  $file > ${ANALYZE_PATH}
#}

# Set design top
current_design $PTOPLEVEL

set_power_prediction
#
# Apply constraints
#

#Set Technology Specifications
set LIB_NAME "nangate_lib"
set DFF_CELL DFF_X2
set LIB_DFF_D "nangate_lib/$DFF_CELL/D"
#set OPER_COND WCCOM

#set CLOCKS [concat $CLK { "nc0_dwb_clk_i" "nc0_iwb_clk_i" "nc1_dwb_clk_i" "nc1_iwb_clk_i" "cc0_dwb_clk_i" "cc0_iwb_clk_i"} ]

create_clock $CLK -period $CLK_PERIOD
set_clock_uncertainty $CLK_UNCERTAINTY [all_clocks]

#Dont touch the clock tree
set_dont_touch_network [all_clocks]

# Reset constraints 
remove_driving_cell $RST
set_drive 0 $RST
#set_dont_touch_network $RST

# All inputs except reset and clock 
set all_inputs_wo_rst_clk [remove_from_collection [remove_from_collection [all_inputs] [get_port $CLK]] [get_port $RST]]

#Set default FF and Latch
set_register_type -flip_flop $DFF_CELL
# -latch LD1QSVT 

# Set output delays and load for output signals
#
# All outputs are assumed to go directly into
# external flip-flops for the purpose of this
# synthesis with a FO4 load
set_output_delay $DFF_SETUP -clock $CLK [all_outputs]

# Input delay and driving cell of all inputs
#
# All these signals are assumed to come directly from
# flip-flops for the purpose of this synthesis
#
#
#set_input_delay DFF_CKQ -clock CLK all_inputs_wo_rst_clk
set_driving_cell -lib_cell $DFF_CELL -pin Q $all_inputs_wo_rst_clk 

#Prevent assignment statements in the Verilog netlist
set_fix_multiple_port_nets -all -buffer_constants $PTOPLEVEL
#To stop the clock from giving us REALLY high power numbers
set high_fanout_net_threshold 0
set high_fanout_net_pin_capacitance 0.001

# Set design fanout 

set_max_fanout 5 $PTOPLEVEL
#set_max_capacitance 0.7 [all_inputs]
set_max_capacitance 0.5 [all_inputs]

#Optimize all near-critical paths to give extra slack for layout
#set c_range [$CLK_PERIOD * 0.10]
#group_path -critical_range [expr $CLK_PERIOD * 0.10] -name $CLK -to $CLK

# Operating conditions
#set_operating_conditions OPER_COND

#set_wire_load "10x10"*/

#set_operating_conditions WCCOM
# #set_wire_load_model -name "tc6a120m2"
#set_wire_load_model -name "10x10"

#set_ultra_optimization -f
#compile_new_optimization = true

# #set_structure -boolean false -timing true

current_design $PTOPLEVEL

#Link all blocks and uniquify them
set LINK_REPORT $LOG_PATH
append LINK_REPORT "link_design_" $PTOPLEVEL ".log" 
link > ${LINK_REPORT}
uniquify

#Output design errors to a seperate file
set CHKDESIGN_PATH $LOG_PATH
append CHKDESIGN_PATH "check_design_" $PTOPLEVEL ".log"
check_design > ${CHKDESIGN_PATH}

if {$DO_FLATTEN == "yes"} {
  set_flatten -effort medium -minimize single_output
} {
  set_flatten false
}

# Lets do basic synthesis
#TODO rn19 (try to do this to see what's going to happen
ungroup -all -flatten
# set_flatten true -effort high 

#Make sure we are using multicore
set HOST_OP_PATH $LOG_PATH
append HOST_OPT_PATH "report_host_options"
report_host_options > ${HOST_OPT_PATH}

set COMPIL_PATH $LOG_PATH
append COMPIL_PATH "compilation_report"

if {$EFFORT == "ultra"} {
  #compile_ultra -timing_high_effort This is for the final run
 #set compile_ultra_ungroup_dw true
  compile_ultra > ${COMPIL_PATH}
} {
  if {$EFFORT == "high"} {
    compile -ungroup_all -exact_map -area_effort high -power_effort high -map_effort high > ${COMPIL_PATH}
  } {
    if {$EFFORT == "medium"} {
      compile -ungroup_all -exact_map -area_effort medium -power_effort medium -map_effort medium > ${COMPIL_PATH}
    } {
      if {$EFFORT == "low"} {
        compile -ungroup_all -exact_map -area_effort low -power_effort low -map_effort low > ${COMPIL_PATH}
      } {
        if {$EFFORT == "none"} {
          compile -ungroup_all -exact_map -area_effort none -power_effort none -map_effort none > ${COMPIL_PATH}
        }
      }
    }
  }
}

#exit
# Save current design using synopsys format */
#set verilogout_single_bit true
# Save current design using sverilog format
if { $WRITE_VERILOG == 1 } {
  set outfile $GATE_PATH
  append outfile $STAGE "_" $TOPLEVEL ".v"
  write -hierarchy -format verilog -output $outfile
}

if { $WRITE_VHDL == 1 } {
  set outfile $GATE_PATH
  append outfile $STAGE "_" $TOPLEVEL ".vhd"
  write -hierarchy -format vhdl -output $outfile
}

#Write out DDC File to load in later
set outfile $GATE_PATH
append outfile $STAGE "_" $TOPLEVEL ".ddc"
write -format ddc -hierarchy -output $outfile


# Basic reports for individual components
#foreach module $report_modules {
#  if { $module != $TOPLEVEL } {
#     current_design module
#     check_design
#     link
#     compile -boundary_optimization -map_effort medium -ungroup_all
#    report_area                     > LOG_PATH + STAGE + _ + module + _area.log
#    report_timing -nworst 10        > LOG_PATH + STAGE + _ + module + _timing.log
#     report_area
#     report_timing -nworst 10
#  }
#}

current_design $PTOPLEVEL

# Basic reports
#report_timing -nworst 10         > $LOG_PATH$STAGE "_" $TOPLEVEL " _timing.log"
#report_hierarchy                > LOG_PATH + STAGE + _ + TOPLEVEL + _hierarchy.log
#report_resources                > LOG_PATH + STAGE + _ + TOPLEVEL + _resources.log
#report_constraint               > LOG_PATH + STAGE + _ + TOPLEVEL + _constraint.log
#report_compile_options          > LOG_PATH + STAGE + _ + TOPLEVEL + _options.log

#report_ultra_optimizations      > LOG_PATH + STAGE + _ + TOPLEVEL + _ultra_optimizations.log
#report_references               > LOG_PATH + STAGE + _ + TOPLEVEL + _references.log
set POWER_PATH $LOG_PATH
append POWER_PATH ${STAGE} "_" $TOPLEVEL "_power.log"
report_power -nosplit > ${POWER_PATH}

set POWER_PATH $LOG_PATH
append POWER_PATH ${STAGE} "_" $TOPLEVEL "_powerWSAIF.log"
report_power -nosplit > ${POWER_PATH}

set AREA_PATH $LOG_PATH
append AREA_PATH ${STAGE} "_" $TOPLEVEL "_area.log"
report_area -nosplit > ${AREA_PATH}

# Verify design
if {$DO_VERIFY == "yes"} {
#  compile -no_map -verify              > LOG_PATH + verify_ + TOPLEVEL + .log
  compile -no_map -verify
}

sh date
exit

