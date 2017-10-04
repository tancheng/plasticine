#!/bin/bash

#TOP=PMU
source simulator.mk
export SIM_DIR=${PLASTICINE_HOME}/sim_out/$TOP
export SIM_PATH=${SIM_DIR}/psim.bin
export DRAMSIM_HOME=${SIM_DIR}/hw/DRAMSim2
export LD_LIBRARY_PATH=$DRAMSIM_HOME:$LD_LIBRARY_PATH
export USE_IDEAL_DRAM=0
export DRAM_DEBUG=0
export DRAM_NUM_OUTSTANDING_BURSTS=-1
export VPD_ON=1
export VCD_ON=1

./Top $@ 2>&1 | tee sim.log

bash digraphExtractor.sh sim.log

