#!/bin/bash

if [ $# -ne 2 ]; then
  echo "Usage: $0 <proj_path> <top_module>"
  exit -1
fi

PROJ_PATH=`readlink -f $1`
PROJ_NAME=$2
PROJ_SRC_PATH=$PROJ_PATH
PROJ_SOURCES=`ls $PROJ_SRC_PATH/*.v`

FILENAME=param.tcl

## Settings
declare -A SETTINGS
SETTINGS["CLK_PERIOD"]=1.3
SETTINGS["PROJECT_NAME"]=$PROJ_NAME
SETTINGS["MILKYWAY_LIB_NAME"]=$PROJ_NAME
SETTINGS["PROJECT_PATH"]=$PROJ_PATH
SETTINGS["PRE_PR_NETLIST_PATH"]=$PROJ_PATH/pre_pr_gatelevel_netlist
SETTINGS["POST_PR_NETLIST_PATH"]=$PROJ_PATH/post_pr_gatelevel_netlist
SETTINGS["SRC_PATH"]=$PROJ_SRC_PATH
SETTINGS["DC_SOURCE_PATH"]=$PROJ_SRC_PATH
SETTINGS["VERILOG_LIST"]=$PROJ_SOURCES
SETTINGS["TSMC_45"]=/cad/synopsys_EDK2/TSMCHOME/digital/Front_End/timing_power_noise/NLDM/tcbn45gsbwp_110a/
SETTINGS["SYN"]=/cad/synopsys/dc_shell/latest/libraries/syn/
SETTINGS["MILKYWAY_TF"]=/cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/HVH_0d5_0/tsmcn45_10lm7X2ZRDL.tf
SETTINGS["MILKYWAY_REF"]=/cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/frame_only_HVH_0d5_0/tcbn45gsbwp
SETTINGS["TLUPLUS_MAX"]=/cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p10m+alrdl_rcbest_top2.tluplus
SETTINGS["TLUPLUS_MIN"]=/cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/cln45gs_1p10m+alrdl_rcworst_top2.tluplus
SETTINGS["TECH2ITF_MAP"]=/cad/synopsys_EDK2/TSMCHOME/digital/Back_End/milkyway/tcbn45gsbwp_110a/techfiles/tluplus/star.map_10M

## Emit 'set $KEY $VALUE'
rm -f $PROJ_PATH/$FILENAME
for key in "${!SETTINGS[@]}"; do
  echo "set $key ${SETTINGS[$key]}" >> $PROJ_PATH/$FILENAME
done
