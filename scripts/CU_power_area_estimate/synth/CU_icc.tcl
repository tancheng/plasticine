source param.tcl

file mkdir $PROJECT_PATH/icc_reports
file mkdir $PROJECT_PATH/post_pr_gatelevel_netlist
set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
set sym_lib    $TSMC_45
set target_lib $TSMC_45
set search_path [list ./ $SRC_PATH $target_lib $sym_lib]
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

# create_floorplan
create_floorplan \
	-control_type aspect_ratio \
    -core_aspect_ratio 1 \
    -core_utilization 0.7 \
    -left_io2core 30 \
    -bottom_io2core 30 \
    -right_io2core 30 \
    -top_io2core 30 \
    -start_first_row

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
place_opt

# connect power rails
preroute_standard_cells -nets {VDD VSS} -connect horizontal -extend_to_boundaries_and_generate_pins

# clock tree synthesis
clock_opt
# check timing
report_clock_tree
report_timing

# routing
route_opt -effort high

# fix DRC errors
route_search_repair -rerun_drc -loop "10"

# check/fix LVS errors
route_zrt_eco -max_detail_route_iterations 5 verify_lvs -check_open_locator -check_short_locator

# adding filler cells
insert_stdcell_filler\
    -cell_without_metal "SHFILL128_RVT SHFILL64_RVT SHFILL3_RVT SHFILL2_RVT SHFILL1_RVT"\
    -connect_to_power {VDD}
    -connect_to_ground {VSS}

foreach net {VDD} {derive_pg_connection -power_net $net -power_pin $net -create_ports top}
foreach net {VSS} {derive_pg_connection -ground_net $net -ground_pin $net -create_ports top}

# last check for drc and lvs error check
verify_lvs

###################################################
# Analyze Design
###################################################
report_area
redirect $PROJECT_PATH/icc_reports/icc_area_report { report_area }
report_power
redirect $PROJECT_PATH/icc_reports/icc_power_report { report_power -analysis_effort hi }
# report_saif
# redirct $PROJECT_PATH/icc_reports/icc_saif_report { report_saif }
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
