rm grepLogs/*
grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldOut <- 0x1" > io_dram_vldOut.gLog
grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldIn <- 0x1" > io_dram_vldIn.gLog
grep -nr runLog -e "read complete" > rd.gLog
grep -nr runLog -e "write complete" > wr.gLog
grep -nr runLog -e "addTransaction" > addTrans.gLog
grep -nr runLog -e "start step" > startStep.gLog
grep -nr runLog -e "transmitting" > addr_tag.gLog
grep -nr runLog -e "DRAM transaction " > tag.gLog
mv *.gLog grepLogs
