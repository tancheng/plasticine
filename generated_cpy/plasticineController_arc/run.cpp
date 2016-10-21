// #include "PlasticineControllerTop.h"
#include "DRAMSimInterface.h"

using namespace DRAMSim;

/* FIXME: this may be broken, currently */
void power_callback(double a, double b, double c, double d)
{
//	printf("power callback: %0.3f, %0.3f, %0.3f, %0.3f\n",a,b,c,d);
}

int main (int argc, char* argv[]) {
  PlasticineControllerTop_t *pctrl = new PlasticineControllerTop_t();
 // DRAMSimInterface *dram = new DRAMSimInterface(pctrl);
 // TransactionCompleteCB *read_cb = new Callback<DRAMSimInterface, void, unsigned, uint64_t, uint64_t>(dram, &DRAMSimInterface::read_complete);
 // TransactionCompleteCB *write_cb = new Callback<DRAMSimInterface, void, unsigned, uint64_t, uint64_t>(dram, &DRAMSimInterface::write_complete);
 // // TODO: switch it to a micron DDR3 module after testing
 // MultiChannelMemorySystem *mcMem = getMemorySystemInstance("ini/DDR2_micron_16M_8b_x8_sg3E.ini", "system.ini", "..", "example_app", 16384);
 // uint64_t cpuClkFreqHz = 622222222;
 // mcMem->setCPUClockSpeed(cpuClkFreqHz);
 // mcMem->RegisterCallbacks(read_cb, write_cb, power_callback);
 // uint64_t addr = 1LL<<33;
 // bool isWrite = false;
  int lim = 100;
  pctrl->init();
  for (int t = 0; t < lim; t ++) {
  	dat_t<1> reset = LIT<1>(t == 0);
//    pctrl->PlasticineControllerTop__io_start = LIT<1>(t == 0);
    // TODO: debugging
    if (t == 1) cout << "DEBUG: start enquing" << endl;

//    cout << "[main emulator] t=" << t
//         << ", reset=" << reset.values[0]
//         << ", top_start=" << pctrl->PlasticineControllerTop__io_start.values[0]
//         << ", tx_enq=" << pctrl->PlasticineControllerTop_controller__io_tx_enq.values[0]
//         << ", tx_comp=" << pctrl->PlasticineControllerTop_controller__io_tx_comp.values[0]
//         << endl;
//
    // add a transaction if TX_ENQ is signaled
//    if (pctrl->PlasticineControllerTop_dramsim__TX_ENQ.values[0] > 0 && t == 0)
//    {
//      pctrl->PlasticineControllerTop__io_start = LIT<1>(t == 1);
//      mcMem->addTransaction(false, 1LL<<33);
//    }
//
//    if (pctrl->PlasticineControllerTop_dramsim__TX_ENQ.values[0] > 0 && t == 2)
//    {
//      pctrl->PlasticineControllerTop__io_start = LIT<1>(t == 3);
//      mcMem->addTransaction(true, 0x900012);
//    }
//
//    if (pctrl->PlasticineControllerTop_dramsim__TX_ENQ.values[0] > 0 && t == 4)
//    {
//      pctrl->PlasticineControllerTop__io_start = LIT<1>(t == 5);
//      mcMem->addTransaction(false, 0x900012);
//    }
//    if (pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] > 0)
//    {
//      pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 0; // reset the signal
//    }

	  pctrl->clock_lo(reset);
//    mcMem->update();
    if (t == 2)
    {
      pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1;
    }

    cout << "TX_COMP = " <<
      pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] << endl;
	  pctrl->clock_hi(reset);
  	pctrl->print(stdout);
  }

  return 0;
}
