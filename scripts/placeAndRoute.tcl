set_host_options -max_cores 16

set TOPLEVEL "rpc_asm"

set search_path { "." "../nangate_files" }
 
set target_library { "nangate_lib.db" }
set symbol_library { "generic.sdb" }
set link_library $target_library
#set additional_libraries { "" }

set link_library [concat { "*" } $target_library $synthetic_library ]
open_mw_lib ${TOPLEVEL}_lib

#read_sverilog final_rf_${TOPLEVEL}.v
read_sverilog final_${TOPLEVEL}.v

initialize_floorplan

#push_down_fp_objects -allow_feedthroughs -connect_objects_to_child_nets -create_pins
#flatten_fp_black_boxes $blackBoxes_reg_32

#save_mw_cel -hierarchy
#place_fp_pins
#save_mw_cel -hierarchy
push_down_fp_objects -connect_objects_to_child_nets -create_pins
place_fp_pins
#save_mw_cel -hierarchy
allocate_fp_budgets
#save_mw_cel -hierarchy

#import_fp_black_boxes $reg_64Files
#estimate_fp_black_boxes -sm_size {8.273361e-45 4.310973e-314} $reg_64Files
#import_fp_black_boxes $mem_localFiles
#estimate_fp_black_boxes -sm_size {140.897232 201.587997} $mem_localFiles
#import_fp_black_boxes $mem_globalFiles
#estimate_fp_black_boxes -sm_size {205.378505 437.859297} $mem_globalFiles
#import_fp_black_boxes $imemFiles
#estimate_fp_black_boxes -sm_size {293.969470 379.129458} $imemFiles
#read_ddc final_shader_core.ddc
#list_designs
#current_design shader_core

save_mw_cel -hierarchy
initialize_floorplan
create_fp_placement
legalize_fp_placement
place_fp_pins
#create_fp_placement
#create_fp_placement
legalize_fp_placement
route_zrt_global
#added by rn19
#create_plan_groups
#shape_fp_block
#synthesize_fp_rail
#analyze_fp_rail
#set_route_zrt_common_options -plan_group_aware all_routing
#route_global -effort high
#optimize_fp_timing
#compile_fp_clock_plan
#place_fp_pins
#allocate_fp_budgets

#clock_opt -area_recovery -power
#place_opt -effort high
#route_opt -effort high
report_area > ../log/${TOPLEVEL}_report_area.log
#estimate_fp_area -max_width 10000 -max_height 10000 > ../log/estimatedFPArea.log
#estimate_fp_area -max_width 10000 -max_height 10000
write_def -output ${TOPLEVEL}.def
exit

