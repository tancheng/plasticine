include Makefile.common
EXE=psim.bin

CC=g++
VCS_OPTS=-full64 -quiet -timescale=1ns/1ps -sverilog -debug_pp -Mdir=${TOP}.csrc +v2k +vcs+lic+wait +vcs+initreg+random +define+CLOCK_PERIOD=0.5 +lint=TFIPC-L
#CC_OPTS=-LDFLAGS "-L../ -ldramsim -lstdc++ -Wl,-rpath=../" -CFLAGS "-O0 -g -I${VCS_HOME}/include -I../sw -I../hw/DRAMSim2 -I../ -I../hw -fPIC -std=c++11 -L../ -ldramsim -lstdc++ -Wl,-rpath=../"
CC_OPTS=-LDFLAGS "-L${CURDIR}/ -ldramsim -lstdc++ -Wl,-rpath=../" -CFLAGS "-O0 -g -I${VCS_HOME}/include -I${CURDIR}/sw -I${CURDIR}/hw/DRAMSim2 -I${CURDIR}/ -I${CURDIR}/hw -fPIC -std=c++11 -L${CURDIR}/ -ldramsim -lstdc++ -Wl,-rpath=../"

all: dram sim

sim:
	export LM_LICENSE_FILE=27000@cadlic0.stanford.edu
	vcs ${VCS_OPTS} -cpp ${CC} ${CC_OPTS} -o ${EXE} ${TOP}.v hw/SRAMVerilogSim.v hw/${TOP}-harness.sv hw/sim.cpp

dram:
	make -C hw/DRAMSim2 libdramsim.so -j8
	ln -sf hw/DRAMSim2/libdramsim.so .

clean:
	make -C hw/DRAMSim2 clean
	rm -rf *.o *.csrc *.daidir ${TOP} simv ucli.key *.cmd *.in *.out *.vcd *.vpd ${EXE} vc_hdrs.h libdramsim.so
