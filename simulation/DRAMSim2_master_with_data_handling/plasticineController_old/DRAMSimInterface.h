#pragma once
#include <stdint.h>
#include <DRAMSim.h>
#include "PlasticineControllerTop.h"

class DRAMSimInterface
{
	public:
    	DRAMSimInterface(PlasticineControllerTop_t* pctrl);
		void read_complete(unsigned, uint64_t, uint64_t);
		void write_complete(unsigned, uint64_t, uint64_t);
  private:
	    PlasticineControllerTop_t* pctrl;
};
