rm grepLogs/*
grep -nr runLog -e "read complete" > rd.gLog
grep -nr runLog -e "write complete" > wr.gLog
grep -nr runLog -e "write complete: 0" > wr_ch0.gLog
grep -nr runLog -e "write complete: 1" > wr_ch1.gLog
grep -nr runLog -e "write complete: 2" > wr_ch2.gLog
grep -nr runLog -e "write complete: 3" > wr_ch3.gLog
grep -nr runLog -e "read complete: channel=0" > rd_ch0.gLog
grep -nr runLog -e "read complete: channel=1" > rd_ch1.gLog
grep -nr runLog -e "read complete: channel=2" > rd_ch2.gLog
grep -nr runLog -e "read complete: channel=3" > rd_ch3.gLog
grep -nr runLog -e "addTransaction" > addTrans.gLog
grep -nr runLog -e "one read-modify-write completed at channel " > compTrans.gLog
grep -nr runLog -e "one read-modify-write completed at channel 0" > compTrans.gLog
grep -nr runLog -e "one read-modify-write completed at channel 1" > compTrans.gLog
grep -nr runLog -e "one read-modify-write completed at channel 2" > compTrans.gLog
grep -nr runLog -e "one read-modify-write completed at channel 3" > compTrans.gLog
grep -nr runLog -e "i = " > dataHandling.gLog
mv *.gLog grepLogs
