set PROJECT_NAME ComputeUnit
set TSMC_45 /cad/synopsys_EDK2/TSMCHOME/digital/Front_End/timing_power_noise/NLDM/tcbn45gsbwp_110a/
set PROJECT_PATH [pwd] 
set PRE_PR_NETLIST_PATH $PROJECT_PATH/pre_pr_gatelevel_netlist
set POST_PR_NETLIST_PATH $PROJECT_PATH/post_pr_gatelevel_netlist
set SRC_PATH $PROJECT_PATH/src
set DC_SOURCE_PATH $PROJECT_PATH/src
set VERILOG_LIST [list ComputeUnit.v]
set SYN /cad/synopsys/dc_shell/latest/libraries/syn/
set MILKYWAY_TF /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/HVH_0d5_0/tsmcn45_10lm7X2ZRDL.tf 
set MILKYWAY_REF /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/frame_only_HVH_0d5_0/tcbn45gsbwp
set MILKYWAY_LIB_NAME $PROJECT_NAME
set TLUPLUS_MAX /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p10m+alrdl_rcbest_top2.tluplus
set TLUPLUS_MIN /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p10m+alrdl_rcworst_top2.tluplus
set TECH2ITF_MAP /cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/star.map_10M
