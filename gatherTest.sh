rm grepLogs/*
grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldOut <- 0x1" > io_dram_vldOut.gLog
grep -nr runLog -e "MultiMemoryUnitTester.MemoryUnit.io_dram_vldIn <- 0x1" > io_dram_vldIn.gLog
grep -nr runLog -e "read complete" > rd.gLog
grep -nr runLog -e "addTransaction" > addTrans.gLog
grep -nr runLog -e "start step" > startStep.gLog
mv *.gLog grepLogs
