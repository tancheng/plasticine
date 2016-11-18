rm grepLogs/*
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldOut <- 0x1" > io_dram_vldOut_ch0.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldIn <- 0x1" > io_dram_vldIn_ch0.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_1.io_dram_vldOut <- 0x1" > io_dram_vldOut_ch1.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_1.io_dram_vldIn <- 0x1" > io_dram_vldIn_ch1.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_2.io_dram_vldOut <- 0x1" > io_dram_vldOut_ch2.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_2.io_dram_vldIn <- 0x1" > io_dram_vldIn_ch2.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_3.io_dram_vldOut <- 0x1" > io_dram_vldOut_ch3.gLog
# grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit_3.io_dram_vldIn <- 0x1" > io_dram_vldIn_ch3.gLog
grep -nr run.log -e "read complete" > rd.gLog
grep -nr run.log -e "io.dram.vldIn is signaled" > dramVldIn.gLog
grep -nr run.log -e "write complete" > wr.gLog
grep -nr run.log -e "addTransaction" > addTrans.gLog
grep -nr run.log -e "start step" > startStep.gLog
grep -nr run.log -e "transmitting" > addr_tag.gLog
grep -nr run.log -e "DRAM transaction " > tag.gLog
mv *.gLog grepLogs
