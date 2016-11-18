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
  	mcMem = getMemorySystemInstance("ini/DDR3_micron_64M_8B_x4_sg15.ini", "system.ini", "..", "MultiMemoryUnitTester", 65536);

  	uint64_t cpuClkFreqHz = 1000000000;// 622222222;
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
	std::map<uint64_t, vector<dat_t<32> > > rmwDataMap;
	std::map<uint64_t, vector<dat_t<1> > > rmwMasksMap;
	std::map<uint64_t, dat_t<32> > rmwTagMap;

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
		mcMem->printStats(true);
  }

  void read_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
	  MultiMemoryUnitTester_t *pctrl = (MultiMemoryUnitTester_t *)module;
	  // check for channel
	  if (id == 0)
	  {
			// check if this is a read_modify_write
			if (rmwTagMap.count(address))
			{
				vector<dat_t<32> > rmwRdataVec = dataMap[address];
				dat_t<32> rmwTag = rmwTagMap[address];
				vector<dat_t<32> > rmwWdataVec = rmwDataMap[address];
				vector<dat_t<1> > rmwMasksVec = rmwMasksMap[address];

				vector<dat_t<32> > writeInData;
				if (rmwRdataVec.size() == 0)
				{
					for (int i = 0; i < 16; i ++)
					{
						dat_t<36> zeroData;
						zeroData.values[0] = 0;
						writeInData.push_back(zeroData);
					}
				}
				else
				{
					for (int i = 0; i < 16; i ++)
					{
						writeInData.push_back(rmwRdataVec[i]);
					}
				}

				for (int i = 0; i < 16; i ++)
				{
					if (rmwMasksVec[i].values[0] == 0)
					{
						rmwWdataVec[i] = writeInData[i];
					}
				}

				dataMap[address] = rmwWdataVec;
				// print to check data...
				for (int i = 0; i < 16; i ++)
				{
					cout << "i = " << i << ", rmwWdataVec[i] = " << rmwWdataVec[i] << endl;
				}

				tagMap[address] = transTag;
				rmwTagMap.erase(address);
				rmwMasksMap.erase(address);
				rmwDataMap.erase(address);
				bool transSuccess = mcMem->addTransaction(true; address);
				return;
			}

      MultiMemoryUnitTester_DRAMSimulator__io_vldOut.values[0] = 1;
     	printf("[Callback] read complete: channel = %d, address = 0x%lx, cycle = %lu\n", id, address, clock_cycle);
     	printf("starting getting tags \n");
     	MultiMemoryUnitTester_DRAMSimulator__io_tagOut = tagMap[address];
     	printf("starting getting rdataVec from dataMap \n");
     	vector<dat_t<32> > rdataVec = dataMap[address];
     	printf("size of rdataVec is %lu \n", rdataVec.size());

      if (rdataVec.size() == 0)
      {
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_0.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_1.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_2.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_3.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_4.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_5.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_6.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_7.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_8.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_9.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_10.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_11.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_12.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_13.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_14.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator__io_rdata_15.values[0] = 0;
      }
      else
      {
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_0 = rdataVec.at(0);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_1 = rdataVec.at(1);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_2 = rdataVec.at(2);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_3 = rdataVec.at(3);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_4 = rdataVec.at(4);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_5 = rdataVec.at(5);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_6 = rdataVec.at(6);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_7 = rdataVec.at(7);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_8 = rdataVec.at(8);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_9 = rdataVec.at(9);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_10 = rdataVec.at(10);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_11 = rdataVec.at(11);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_12 = rdataVec.at(12);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_13 = rdataVec.at(13);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_14 = rdataVec.at(14);
    	  MultiMemoryUnitTester_DRAMSimulator__io_rdata_15 = rdataVec.at(15);
      }
	  }

		if (id == 1)
	  {
      MultiMemoryUnitTester_DRAMSimulator_1__io_vldOut.values[0] = 1;
     	printf("[Callback] read complete: channel = %d, address = 0x%lx, cycle = %lu\n", id, address, clock_cycle);
     	printf("starting getting tags \n");
     	MultiMemoryUnitTester_DRAMSimulator_1__io_tagOut = tagMap[address];
     	printf("starting getting rdataVec from dataMap \n");
     	vector<dat_t<32> > rdataVec = dataMap[address];
     	printf("size of rdataVec is %lu \n", rdataVec.size());

      if (rdataVec.size() == 0)
      {
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_0.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_1.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_2.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_3.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_4.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_5.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_6.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_7.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_8.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_9.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_10.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_11.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_12.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_13.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_14.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_15.values[0] = 0;
      }
      else
      {
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_0 = rdataVec.at(0);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_1 = rdataVec.at(1);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_2 = rdataVec.at(2);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_3 = rdataVec.at(3);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_4 = rdataVec.at(4);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_5 = rdataVec.at(5);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_6 = rdataVec.at(6);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_7 = rdataVec.at(7);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_8 = rdataVec.at(8);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_9 = rdataVec.at(9);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_10 = rdataVec.at(10);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_11 = rdataVec.at(11);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_12 = rdataVec.at(12);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_13 = rdataVec.at(13);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_14 = rdataVec.at(14);
    	  MultiMemoryUnitTester_DRAMSimulator_1__io_rdata_15 = rdataVec.at(15);
      }
	  }

	  if (id == 2)
	  {
      MultiMemoryUnitTester_DRAMSimulator_2__io_vldOut.values[0] = 1;
     	printf("[Callback] read complete: channel = %d, address = 0x%lx, cycle = %lu\n", id, address, clock_cycle);
     	printf("starting getting tags \n");
     	MultiMemoryUnitTester_DRAMSimulator_2__io_tagOut = tagMap[address];
     	printf("starting getting rdataVec from dataMap \n");
     	vector<dat_t<32> > rdataVec = dataMap[address];
     	printf("size of rdataVec is %lu \n", rdataVec.size());

      if (rdataVec.size() == 0)
      {
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_0.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_1.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_2.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_3.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_4.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_5.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_6.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_7.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_8.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_9.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_10.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_11.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_12.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_13.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_14.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_15.values[0] = 0;
      }
      else
      {
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_0 = rdataVec.at(0);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_1 = rdataVec.at(1);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_2 = rdataVec.at(2);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_3 = rdataVec.at(3);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_4 = rdataVec.at(4);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_5 = rdataVec.at(5);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_6 = rdataVec.at(6);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_7 = rdataVec.at(7);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_8 = rdataVec.at(8);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_9 = rdataVec.at(9);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_10 = rdataVec.at(10);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_11 = rdataVec.at(11);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_12 = rdataVec.at(12);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_13 = rdataVec.at(13);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_14 = rdataVec.at(14);
    	  MultiMemoryUnitTester_DRAMSimulator_2__io_rdata_15 = rdataVec.at(15);
      }
	  }

	  if (id == 3)
	  {
      MultiMemoryUnitTester_DRAMSimulator_3__io_vldOut.values[0] = 1;
     	printf("[Callback] read complete: channel = %d, address = 0x%lx, cycle = %lu\n", id, address, clock_cycle);
     	printf("starting getting tags \n");
     	MultiMemoryUnitTester_DRAMSimulator_3__io_tagOut = tagMap[address];
     	printf("starting getting rdataVec from dataMap \n");
     	vector<dat_t<32> > rdataVec = dataMap[address];
     	printf("size of rdataVec is %lu \n", rdataVec.size());

      if (rdataVec.size() == 0)
      {
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_0.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_1.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_2.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_3.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_4.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_5.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_6.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_7.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_8.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_9.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_10.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_11.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_12.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_13.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_14.values[0] = 0;
      	MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_15.values[0] = 0;
      }
      else
      {
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_0 = rdataVec.at(0);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_1 = rdataVec.at(1);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_2 = rdataVec.at(2);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_3 = rdataVec.at(3);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_4 = rdataVec.at(4);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_5 = rdataVec.at(5);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_6 = rdataVec.at(6);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_7 = rdataVec.at(7);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_8 = rdataVec.at(8);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_9 = rdataVec.at(9);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_10 = rdataVec.at(10);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_11 = rdataVec.at(11);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_12 = rdataVec.at(12);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_13 = rdataVec.at(13);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_14 = rdataVec.at(14);
    	  MultiMemoryUnitTester_DRAMSimulator_3__io_rdata_15 = rdataVec.at(15);
      }
	  }
  }

  void write_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
  	MultiMemoryUnitTester_t *pctrl = (MultiMemoryUnitTester_t *)module;
		if (id == 0)
		{
			MultiMemoryUnitTester_DRAMSimulator__io_vldOut.values[0] = 1;
			MultiMemoryUnitTester_DRAMSimulator__io_tagOut = tagMap[address];
    	printf("[Callback] write complete: %d 0x%lx cycle=%lu tag=%lu\n", id, address, clock_cycle, MultiMemoryUnitTester_DRAMSimulator__io_tagOut.values[0]);
		}

		if (id == 1)
		{
			MultiMemoryUnitTester_DRAMSimulator_1__io_vldOut.values[0] = 1;
			MultiMemoryUnitTester_DRAMSimulator_1__io_tagOut = tagMap[address];
    	printf("[Callback] write complete: %d 0x%lx cycle=%lu tag=%lu\n", id, address, clock_cycle, MultiMemoryUnitTester_DRAMSimulator_1__io_tagOut.values[0]);
		}

		if (id == 2)
		{
			MultiMemoryUnitTester_DRAMSimulator_2__io_vldOut.values[0] = 1;
			MultiMemoryUnitTester_DRAMSimulator_2__io_tagOut = tagMap[address];
    	printf("[Callback] write complete: %d 0x%lx cycle=%lu tag=%lu\n", id, address, clock_cycle, MultiMemoryUnitTester_DRAMSimulator_2__io_tagOut.values[0]);
		}

		if (id == 3)
		{
			MultiMemoryUnitTester_DRAMSimulator_3__io_vldOut.values[0] = 1;
			MultiMemoryUnitTester_DRAMSimulator_3__io_tagOut = tagMap[address];
    	printf("[Callback] write complete: %d 0x%lx cycle=%lu tag=%lu\n", id, address, clock_cycle, MultiMemoryUnitTester_DRAMSimulator_3__io_tagOut.values[0]);
		}
  }

  virtual inline void step() {
    mcMem->update();
  	MultiMemoryUnitTester_t *pctrl = (MultiMemoryUnitTester_t *)module;
    module->clock(LIT<1>(0));
    // FIXME: should call twice to get the output for now
    module->clock_lo(LIT<1>(0), false);
		// channel 0
  	if (MultiMemoryUnitTester_DRAMSimulator__io_vldIn.values[0] > 0)
  	{
  	  dat_t<32> transTag = MultiMemoryUnitTester_DRAMSimulator__io_tagIn;
  	  bool isWR = MultiMemoryUnitTester_DRAMSimulator__io_isWr.values[0];
  	  uint64_t addr = MultiMemoryUnitTester_DRAMSimulator__io_addr.values[0];
			addr = addr;

			printf(">>>>>>>>>> addTransaction to channel 0 <<<<<<<<<<, addr = %lx, transTag = %lu \n ", addr, transTag.values[0]);
			uint32_t RdModWr = 1;
			vector<dat_t<1> > masksVec;
	    masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_0);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_1);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_2);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_3);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_4);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_5);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_6);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_7);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_8);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_9);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_10);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_11);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_12);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_13);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_14);
  		masksVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_masksIn_15);

			for (int i = 0; i < masksVec.size(); i ++)
			{
				RdModWr = RdModWr & masksVec[i].values[0];
			}

			bool isRdModWr = (RdModWr == 0);
			if (isRdModWr)
			{
				cout << "doing read_modify_write" << endl;
				// add a read
				bool rmwReadTransSuccess = mcMem->addTransaction(false, addr);
				if (!rmwReadTransSuccess) cout << "error: cannot add transaction in channel 0" << endl;
				rmwTagMap[address] = transTag;
				rmwMasksMap[address] = masksVec;

  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_0);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_1);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_2);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_3);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_4);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_5);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_6);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_7);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_8);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_9);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_10);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_11);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_12);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_13);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_14);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_15);
				rmwDataMap[address] = wdataVec;

				return
			}


  	  tagMap[addr] = transTag;
  	  bool transSuccess = mcMem->addTransaction(isWR, addr);
  	  // TODO: need to take the case where a transaction
  	  // cannot be completed due to size limit
			if (!transSuccess) cout << ">>>>>>>>>> warning: cannot enqueue a transaction to DRAMSim! <<<<<<<<<<" << endl;
  	  if (isWR)
  	  {
//  	  	cout << "channel 0: isWr: writing to addr = " << addr << endl;
//  	    cout << ">>>>>>>>>> isWR, channel 0 <<<<<<<<<<" << endl;
  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_0);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_1);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_2);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_3);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_4);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_5);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_6);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_7);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_8);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_9);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_10);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_11);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_12);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_13);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_14);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator__io_wdata_15);
    	 	dataMap[addr] = wdataVec;




        //
    		// check keys....
//    		cout << "checking keys stored in dataMap" << endl;
//    		for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
//    		{
//    			cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
//    		}
  	  }
  	}
		// channel 1
		if (MultiMemoryUnitTester_DRAMSimulator_1__io_vldIn.values[0] > 0)
  	{
//			cout << ">>>>>>>>>> addTransaction to channel 1 <<<<<<<<<<" << endl;
  	  dat_t<32> transTag = MultiMemoryUnitTester_DRAMSimulator_1__io_tagIn;
  	  bool isWR = MultiMemoryUnitTester_DRAMSimulator_1__io_isWr.values[0];
  	  uint64_t addr = MultiMemoryUnitTester_DRAMSimulator_1__io_addr.values[0];
			addr = addr | 0x400000000;
  	  tagMap[addr] = transTag;
  	  bool transSuccess = mcMem->addTransaction(isWR, addr);
  	  // TODO: need to take the case where a transaction
  	  // cannot be completed due to size limit
			if (!transSuccess) cout << ">>>>>>>>>> warning: cannot enqueue a transaction to DRAMSim! <<<<<<<<<<" << endl;

  	  if (isWR)
  	  {
//  	    cout << ">>>>>>>>>> isWR, channel 1 <<<<<<<<<<" << endl;
//  	  	cout << "channel 1: isWr: writing to addr = " << addr << endl;
  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_0);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_1);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_2);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_3);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_4);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_5);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_6);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_7);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_8);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_9);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_10);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_11);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_12);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_13);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_14);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_1__io_wdata_15);
    	 	dataMap[addr] = wdataVec;
        //
    		// check keys....
//    		cout << "checking keys stored in dataMap" << endl;
//    		for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
//    		{
//    			cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
//    		}
  	  }
  	}
	  // channel 2
		if (MultiMemoryUnitTester_DRAMSimulator_2__io_vldIn.values[0] > 0)
  	{
//			cout << ">>>>>>>>>> addTransaction to channel 2 <<<<<<<<<<" << endl;
  	  dat_t<32> transTag = MultiMemoryUnitTester_DRAMSimulator_2__io_tagIn;
  	  bool isWR = MultiMemoryUnitTester_DRAMSimulator_2__io_isWr.values[0];
  	  uint64_t addr = MultiMemoryUnitTester_DRAMSimulator_2__io_addr.values[0];
			addr = addr | 0x800000000;
  	  tagMap[addr] = transTag;
  	  bool transSuccess = mcMem->addTransaction(isWR, addr);
			if (!transSuccess) cout << ">>>>>>>>>> warning: cannot enqueue a transaction to DRAMSim! <<<<<<<<<<" << endl;
  	  // TODO: need to take the case where a transaction
  	  // cannot be completed due to size limit

  	  if (isWR)
  	  {
//  	    cout << ">>>>>>>>>> isWR, channel 2 <<<<<<<<<<" << endl;
//  	  	cout << "channel 2: isWr: writing to addr = " << addr << endl;
  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_0);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_1);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_2);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_3);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_4);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_5);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_6);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_7);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_8);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_9);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_10);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_11);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_12);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_13);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_14);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_2__io_wdata_15);
    	 	dataMap[addr] = wdataVec;
        //
    		// check keys....
//    		cout << "checking keys stored in dataMap" << endl;
//    		for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
//    		{
//    			cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
//    		}
  	  }
  	}
	  // channel 3
		if (MultiMemoryUnitTester_DRAMSimulator_3__io_vldIn.values[0] > 0)
  	{
			// cout << ">>>>>>>>>> addTransaction to channel 3 <<<<<<<<<<" << endl;
  	  dat_t<32> transTag = MultiMemoryUnitTester_DRAMSimulator_3__io_tagIn;
  	  bool isWR = MultiMemoryUnitTester_DRAMSimulator_3__io_isWr.values[0];
  	  uint64_t addr = MultiMemoryUnitTester_DRAMSimulator_3__io_addr.values[0];
			// TODO: warning: somehow channel addr in hardware is not working... need to fix it
			addr = addr | 0xC00000000;
  	  tagMap[addr] = transTag;
  	  bool transSuccess = mcMem->addTransaction(isWR, addr);
			if (!transSuccess) cout << ">>>>>>>>>> warning: cannot enqueue a transaction to DRAMSim! <<<<<<<<<<" << endl;
  	  // TODO: need to take the case where a transaction
  	  // cannot be completed due to size limit

  	  if (isWR)
  	  {
//  	    cout << ">>>>>>>>>> isWR, channel 3 <<<<<<<<<<" << endl;
//  	  	cout << "channel 3: isWr: writing to addr = " << addr << endl;
  	  	vector<dat_t<32> > wdataVec;
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_0);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_1);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_2);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_3);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_4);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_5);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_6);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_7);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_8);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_9);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_10);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_11);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_12);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_13);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_14);
  	  	wdataVec.push_back(MultiMemoryUnitTester_DRAMSimulator_3__io_wdata_15);
    	 	dataMap[addr] = wdataVec;
        //
    		// check keys....
//    		cout << "checking keys stored in dataMap" << endl;
//    		for (map<uint64_t, vector<dat_t<32> > >::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
//    		{
//    			cout << "key = " << it->first << " size of data  = " << it->second.size() << endl;
//    		}
  	  }
  	}

//		cout << ">>>>>>>>>> end injection  <<<<<<<<<<" << endl;
    // reset all the control signals at falling edge... that way control signals
    // will only be high for one clock cycle
    // TODO: need to set rdyOut as well...
    MultiMemoryUnitTester_DRAMSimulator__io_vldOut.values[0] = 0;
    MultiMemoryUnitTester_DRAMSimulator_1__io_vldOut.values[0] = 0;
    MultiMemoryUnitTester_DRAMSimulator_2__io_vldOut.values[0] = 0;
    MultiMemoryUnitTester_DRAMSimulator_3__io_vldOut.values[0] = 0;
//		cout << ">>>>>>>>>> end reseting control signals <<<<<<<<<<" << endl;
  }

  virtual inline void update() {
    module->clock_lo(LIT<1>(0), false);
  }
};

#endif
