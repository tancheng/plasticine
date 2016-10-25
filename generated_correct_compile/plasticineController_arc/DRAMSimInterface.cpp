#include <stdio.h>
#include "DRAMSimInterface.h"

using namespace DRAMSim;

/* constructors */
DRAMSimInterface::DRAMSimInterface(PlasticineControllerTop_t* pctrl)
{
  this->pctrl = pctrl;
}

/* callback functors */
void DRAMSimInterface::read_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
{
  this->pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1;
	printf("[Callback] read complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
}

void DRAMSimInterface::write_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
{
  this->pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1;
	printf("[Callback] write complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
}
