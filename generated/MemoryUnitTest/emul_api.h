// Header for Chisel emulator API
#ifndef __IS_EMULATOR_API__
#define __IS_EMULATOR_API__

#include "sim_api.h"
#include "emulator.h"
#include <DRAMSim.h>

// TODO: need to free the allocated memory
#include <map>
#include <vector>

using namespace DRAMSim;
// API base (non width templated) class for API accessors to dat_t
class dat_api_base { 
public:
  dat_api_base(size_t w) {
    size_t rem = w % 64;
    mask = rem ? (1L << rem) - 1 : -1L;
  }
  virtual std::string get_value() = 0;
  virtual bool put_value(std::string &value) = 0;
  virtual size_t get_value(val_t* values) = 0;
  virtual size_t put_value(val_t* values) = 0;
  virtual size_t get_width() = 0;
  virtual size_t get_num_words() = 0;
protected:
  val_t mask;
};

template<int w> class dat_api : public dat_api_base {
public:
  dat_api(dat_t<w>* new_dat): dat_api_base(w), dat_ptr(new_dat) { }
  inline std::string get_value() { 
    return dat_ptr->to_str(); 
  }
  inline bool put_value(std::string &value) { 
    return dat_from_hex<w>(value, *dat_ptr); 
  }
  inline size_t get_value(val_t* values) {
    size_t i = 0;
    for ( ; i < get_num_words() ; i++) {
      val_t value = dat_ptr->values[i];
      values[i] = (i == (get_num_words()-1)) ? value & mask : value;
    }
    return i;
  }
  inline size_t put_value(val_t* values) {
    size_t i = 0;
    for ( ; i < get_num_words() ; i++) {
      val_t value = values[i];
      dat_ptr->values[i] = (i == (get_num_words()-1)) ? value & mask : value;
    }
    return i; 
  }
  inline size_t get_width() { return w; }
  inline size_t get_num_words() { return dat_ptr->n_words_of(); }

private:
  dat_t<w>* dat_ptr;
};

inline std::string itos(int in, bool is_hex = true) {
  std::stringstream out;
  if (is_hex) out << std::hex;
  out << in;
  return out.str();
}

class clk_api: public dat_api_base {
public:
  clk_api(clk_t* new_clk): dat_api_base(1), clk_ptr(new_clk) { }
  inline std::string get_value() { return itos(clk_ptr->len); }
  inline bool put_value(std::string &value) { return false; }
  inline size_t get_value(val_t* values) {
   values[0] = (val_t) clk_ptr->len; 
   return 1; 
  }
  inline size_t put_value(val_t* values) { 
    clk_ptr->len = (size_t) values[0];
    clk_ptr->cnt = (size_t) values[0];
    return 1; 
  }
  inline size_t get_width() { return 8*sizeof(size_t); }
  inline size_t get_num_words() { return 1; }

private:
  clk_t* clk_ptr;
};

class emul_api_t: public sim_api_t<dat_api_base*> {
public:
  emul_api_t(mod_t* m) {
    module = m;

	// register calls for dramsim2
  	TransactionCompleteCB *read_cb = new Callback<emul_api_t, void, unsigned, uint64_t, uint64_t>(this, &emul_api_t::read_complete);
  	TransactionCompleteCB *write_cb = new Callback<emul_api_t, void, unsigned, uint64_t, uint64_t>(this, &emul_api_t::write_complete);
  	mcMem = getMemorySystemInstance("ini/DDR3_micron_64M_8B_x4_sg15.ini", "system.ini", "..", "MemoryTester", 65536);

  	uint64_t cpuClkFreqHz = 622222222;
  	mcMem->setCPUClockSpeed(cpuClkFreqHz);
  	mcMem->RegisterCallbacks(read_cb, write_cb, power_callback);

    is_exit = false;
  }
  inline bool exit() { return is_exit; }

protected:
  mod_t* module;
  MultiChannelMemorySystem *mcMem;
  std::map<uint64_t, vector<dat_t<32> > > dataMap;
  std::map<uint64_t, dat_t<32> > tagMap;

private:
  static void power_callback(double a, double b, double c, double d)
  {
  	// not implemented for now
  }

  virtual inline void put_value(dat_api_base* &sig, std::string& value, bool force=false) {
    sig->put_value(value);
  }

  virtual inline size_t put_value(dat_api_base* &sig, uint64_t* data, bool force=false) {
    return sig->put_value(data);
  }

  virtual inline std::string get_value(dat_api_base* &sig) {
    return sig->get_value();
  }
  
  virtual inline size_t get_value(dat_api_base* &sig, uint64_t* data) {
    return sig->get_value(data);
  }

  virtual inline size_t get_chunk(dat_api_base* &sig) {
    return sig->get_num_words();
  } 

  virtual inline void reset() {
    module->clock(LIT<1>(1));
    // FIXME: should call twice to get the output for now
    module->clock_lo(LIT<1>(0), false);
  }

  virtual inline void start() { }

  bool is_exit;
  virtual inline void finish() {
    module->clock(LIT<1>(0)); // to vcd-dump the last cycle
    is_exit = true; 
  }

  void read_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
	  // check keys....
	  cout << "checking keys stored in dataMap" << endl;
	  cout << "requested address = " << address << endl;
	  for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
	  {
	  	cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
	  }

    MemoryTester_t *pctrl = (MemoryTester_t *)module;
	  pctrl->MemoryTester_DRAMSimulator__io_vldOut.values[0] = 1;
  	printf("[Callback] read complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
	  printf("starting getting tags \n");
	  pctrl->MemoryTester_DRAMSimulator__io_tagOut = tagMap[address];
	  printf("starting getting rdataVec from dataMap \n");
	  vector<dat_t<32> > rdataVec = dataMap[address];
	  printf("size of rdataVec is %lu \n", rdataVec.size());

    if (rdataVec.size() == 0)
    {
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_0.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_1.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_2.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_3.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_4.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_5.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_6.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_7.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_8.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_9.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_10.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_11.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_12.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_13.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_14.values[0] = 0;
    	pctrl->MemoryTester_DRAMSimulator__io_rdata_15.values[0] = 0;
    }
    else
    {
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_0 = rdataVec.at(0);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_1 = rdataVec.at(1);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_2 = rdataVec.at(2);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_3 = rdataVec.at(3);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_4 = rdataVec.at(4);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_5 = rdataVec.at(5);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_6 = rdataVec.at(6);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_7 = rdataVec.at(7);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_8 = rdataVec.at(8);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_9 = rdataVec.at(9);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_10 = rdataVec.at(10);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_11 = rdataVec.at(11);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_12 = rdataVec.at(12);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_13 = rdataVec.at(13);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_14 = rdataVec.at(14);
	    pctrl->MemoryTester_DRAMSimulator__io_rdata_15 = rdataVec.at(15);
    }
  }

  void write_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
  	MemoryTester_t *pctrl = (MemoryTester_t *)module;
	pctrl->MemoryTester_DRAMSimulator__io_vldOut.values[0] = 1;
    printf("[Callback] write complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
	pctrl->MemoryTester_DRAMSimulator__io_tagOut = tagMap[address];
  }

  virtual inline void step() {
    mcMem->update();
  	MemoryTester_t *pctrl = (MemoryTester_t *)module;

  	// NOTES: in the generated simulation code, all the output of the DRAMSimulator module would be reset to 
  	// 0 at posedge of every clock. The callback of write / read complete is triggerred before step() function 
  	// is called.
  	cout << "before clock, DRAMSimulator_vldOut = " << pctrl->MemoryTester_DRAMSimulator__io_vldOut.values[0] << endl;
  	// tmp values
  	dat_t<1> tmpVldOut = pctrl->MemoryTester_DRAMSimulator__io_vldOut;
  	dat_t<32> tmpTagIn = pctrl->MemoryTester_DRAMSimulator__io_tagOut;
  	dat_t<32> tmpRd0 = pctrl->MemoryTester_DRAMSimulator__io_rdata_0;
  	dat_t<32> tmpRd1 = pctrl->MemoryTester_DRAMSimulator__io_rdata_1;
  	dat_t<32> tmpRd2 = pctrl->MemoryTester_DRAMSimulator__io_rdata_2;
  	dat_t<32> tmpRd3 = pctrl->MemoryTester_DRAMSimulator__io_rdata_3;
  	dat_t<32> tmpRd4 = pctrl->MemoryTester_DRAMSimulator__io_rdata_4;
  	dat_t<32> tmpRd5 = pctrl->MemoryTester_DRAMSimulator__io_rdata_5;
  	dat_t<32> tmpRd6 = pctrl->MemoryTester_DRAMSimulator__io_rdata_6;
  	dat_t<32> tmpRd7 = pctrl->MemoryTester_DRAMSimulator__io_rdata_7;
  	dat_t<32> tmpRd8 = pctrl->MemoryTester_DRAMSimulator__io_rdata_8;
  	dat_t<32> tmpRd9 = pctrl->MemoryTester_DRAMSimulator__io_rdata_9;
  	dat_t<32> tmpRd10 = pctrl->MemoryTester_DRAMSimulator__io_rdata_10;
  	dat_t<32> tmpRd11 = pctrl->MemoryTester_DRAMSimulator__io_rdata_11;
  	dat_t<32> tmpRd12 = pctrl->MemoryTester_DRAMSimulator__io_rdata_12;
  	dat_t<32> tmpRd13 = pctrl->MemoryTester_DRAMSimulator__io_rdata_13;
  	dat_t<32> tmpRd14 = pctrl->MemoryTester_DRAMSimulator__io_rdata_14;
  	dat_t<32> tmpRd15 = pctrl->MemoryTester_DRAMSimulator__io_rdata_15;

    module->clock(LIT<1>(0));
    // FIXME: should call twice to get the output for now
    module->clock_lo(LIT<1>(0), false);
  	pctrl->MemoryTester_DRAMSimulator__io_vldOut = tmpVldOut;
  	pctrl->MemoryTester_DRAMSimulator__io_tagOut = tmpTagIn;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_0 = tmpRd0;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_1 = tmpRd1;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_2 = tmpRd2;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_3 = tmpRd3;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_4 = tmpRd4;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_5 = tmpRd5;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_6 = tmpRd6;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_7 = tmpRd7;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_8 = tmpRd8;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_9 = tmpRd9;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_10 = tmpRd10;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_11 = tmpRd11;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_12 = tmpRd12;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_13 = tmpRd13;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_14 = tmpRd14;
  	pctrl->MemoryTester_DRAMSimulator__io_rdata_15 = tmpRd15;
  	cout << "after clock, DRAMSimulator_vldOut = " << pctrl->MemoryTester_DRAMSimulator__io_vldOut.values[0] << endl;

  	if (pctrl->MemoryTester_DRAMSimulator__io_vldIn.values[0] > 0)
  	{
  	  dat_t<32> transTag = pctrl->MemoryTester_DRAMSimulator__io_tagIn;
  	  bool isWR = pctrl->MemoryTester_DRAMSimulator__io_isWr.values[0];
  	  uint64_t addr = pctrl->MemoryTester_DRAMSimulator__io_addr.values[0];
  	  cout << "isWr: writing to addr = " << addr << endl;
  	  tagMap[addr] = transTag;
  	  bool transSuccess = mcMem->addTransaction(isWR, addr);
  	  // TODO: need to take the case where a transaction
  	  // cannot be completed due to size limit

  	  if (isWR)
  	  {
  	    cout << ">>>>>>>>>> isWR <<<<<<<<<<" << endl;
  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_0);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_1);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_2);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_3);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_4);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_5);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_6);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_7);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_8);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_9);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_10);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_11);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_12);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_13);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_14);
  	  	wdataVec.push_back(pctrl->MemoryTester_DRAMSimulator__io_wdata_15);
    	 	dataMap[addr] = wdataVec;
        //
    		// check keys....
    		cout << "checking keys stored in dataMap" << endl;
    		for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
    		{
    			cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
    		}
  	  }
  	}

    // reset the control signals at falling edge... that way control signals
    // are 1 clock cycle wide
    // TODO: need to set rdyOut as well...
    pctrl->MemoryTester_DRAMSimulator__io_vldOut.values[0] = 0;
  }

  virtual inline void update() {
    module->clock_lo(LIT<1>(0), false);
  }
};

#endif
