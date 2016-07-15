# set SYN /cad/synopsys/dc_shell/latest/libraries/syn/
set TSMC_45 /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/frame_only_HVH_0d5_0/tcbn45gsbwp/LM/
set link_library { * tcbn45gsbwpml.db} 
# set link_library { * tcbn45gsbwpml.db dw_foundation.sldb}
set target_library "tcbn45gsbwpml.db"

set synthetic_library [list  dw_foundation.sldb]
# set dw_lib     $SYN
set sym_lib    $TSMC_45
set target_lib $TSMC_45
set search_path [list ./ ../src/ $target_lib $sym_lib]
set hdlin_auto_save_templates true

# create milkyway library
create_mw_lib -technology /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/HVH_0d5_0/tsmcn45_10lm7X2ZRDL.tf -mw_reference_library /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/frame_only_HVH_0d5_0/tcbn45gsbwp "alu_rtl_LIB"

# read_verilog -rtl [list behavioral_AND8.v]
set_tlu_plus_files -max_tluplus /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p06m+alrdl_rcbest_top1.tluplus\
 -min_tluplus /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p06m+alrdl_rcworst_top1.tluplus\
 -tech2itf_map /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/gdsout_6X2Z.map

open_mw_lib alu_rtl_LIB

import_designs behavioral_AND8.v -format "verilog" -top "behavioral_AND8" -cel "behavioral_AND8" 

read_sdc /home/tianzhao/plasticine_power_area_estimate/aluPowerArea/src/netlist/example.mapped.sdc

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

# create initial design
create_fp_placement

synthesize_fp_rail -nets "VDD VSS" -create_virtual_rails "M1" -synthesize_power_plan -synthesize_power_pads -use_strap_ends_as_pads

commit_fp_rail

# place design
# place_opt

# connect power rails
# preroute_standard_cells -nets {VDD VSS} -connect horizontal -extend_to_boundaries_and_generate_pins

# clock tree synthesis
clock_opt -only_cts -no_clock_route
route_zrt_group -all_clock_nets -reuse_existing_global_route true

# route the remaining nets
route_opt -initial_route_only
route_opt -skip_initial_route -effort low

# insert fillter cells
insert_stdcell_filler -cell_with_metal "SHFILL1 SHFILL2 SHFILL3" -connect_to_power "VDD" -connect_to_ground "VSS"

route_opt -incremental -size_only

### checking timing, power, area 
report_area
report_power
report_timing
exit
