source param.tcl

file mkdir $PROJECT_PATH/icc_reports
file mkdir $PROJECT_PATH/post_pr_gatelevel_netlist
set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set sym_lib    $TSMC_45
set target_lib $TSMC_45
set search_path [list ./ $SRC_PATH $target_lib $sym_lib]
# can be disabled...
set hdlin_auto_save_templates true

# create milkyway library
# create_mw_lib -technology $MILKYWAY_TF -mw_reference_library $MILKYWAY_REF $MILKYWAY_LIB_NAME
set_tlu_plus_files -max_tluplus $TLUPLUS_MAX -min_tluplus $TLUPLUS_MIN -tech2itf_map $TECH2ITF_MAP
open_mw_lib $MILKYWAY_LIB_NAME
import_designs $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.v -format "verilog" -top $PROJECT_NAME -cel $PROJECT_NAME
read_sdc $PRE_PR_NETLIST_PATH/$PROJECT_NAME.mapped.sdc
set power "VDD"
set ground "VSS"
set powerPort "VDD"
set groundPort "VSS"
foreach net {VDD} {derive_pg_connection -power_net $net -create_ports top}
foreach net {VSS} {derive_pg_connection -ground_net $net -create_ports top}
derive_pg_connection -tie

set_switching_activity -toggle_rate 0.125 [all_inputs]

create_floorplan \
      -core_utilization 0.70 \
	  -core_aspect_ratio 1.0 \
	  -left_io2core 5 \
      -bottom_io2core 5 \
      -right_io2core 5 \
	  -top_io2core 5

## create_floorplan
#create_floorplan \
#	-control_type aspect_ratio \
#    -core_aspect_ratio 1 \
#    -core_utilization 0.7 \
#    -left_io2core 30 \
#    -bottom_io2core 30 \
#    -right_io2core 30 \
#    -top_io2core 30 \
#    -start_first_row
#
# create power rings
create_rectangular_rings \
    -nets  {VDD VSS} \
	-left_offset 0.2 \
	-left_segment_width 1 \
	-right_offset 0.2 \
	-right_segment_width 1 \
	-bottom_offset 0.2 \
	-bottom_segment_width 1 \
	-top_offset 0.2 \
	-top_segment_width 1

# place design
create_fp_placement
synthesize_fp_rail \
	-power_budget "1000" -voltage_supply "1.2" -target_voltage_drop "250" \
	-output_dir "./pna_output" -nets "VDD VSS" -create_virtual_rails "M1" \
	-synthesize_power_plan -synthesize_power_pads -use_strap_ends_as_pads

commit_fp_rail

clock_opt -only_cts -no_clock_route
route_opt -initial_route_only
route_opt -skip_initial_route -effort low

insert_stdcell_filler \
	-cell_with_metal "SHFILL1 SHFILL2 SHFILL3" \
	-connect_to_power "VDD" -connect_to_ground "VSS"

route_opt -incremental -size_only

# last check for drc and lvs error check
verify_lvs

###################################################
# Analyze Design
###################################################
report_area
redirect $PROJECT_PATH/icc_reports/icc_area_report { report_area }
report_power
redirect $PROJECT_PATH/icc_reports/icc_power_report { report_power -analysis_effort hi }
report_saif
redirct $PROJECT_PATH/icc_reports/icc_saif_report { report_saif }
check_error
redirect $PROJECT_PATH/icc_reports/icc_error_checking_report { check_error }

###################################################
# Save the design database
###################################################
change_names -rules verilog -hierarchy
write_verilog $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.v
write_sdf $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.sdf
write_sdc $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.sdc
extract_rc -coupling_cap
write_parasitics -format SBPF -output $POST_PR_NETLIST_PATH/$PROJECT_NAME.output.sbpf

save_mw_cel
close_mw_cel
exit
