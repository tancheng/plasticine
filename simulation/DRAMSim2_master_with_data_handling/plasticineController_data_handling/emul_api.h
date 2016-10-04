// Header for Chisel emulator API
#ifndef __IS_EMULATOR_API__
#define __IS_EMULATOR_API__

#include "sim_api.h"
#include "emulator.h"
#include "PlasticineControllerTop.h"
#include <DRAMSim.h>
// for data handling
#include <map>

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
	cout << "init emul" << endl;
    module = m;
//    memMap = new std::map<uint32_t, std::vector<uint32_t>* >();
   
    // adding DRAMSim logic
  	TransactionCompleteCB *read_cb = new Callback<emul_api_t, void, unsigned, uint64_t, uint64_t>(this, &emul_api_t::read_complete);
  	TransactionCompleteCB *write_cb = new Callback<emul_api_t, void, unsigned, uint64_t, uint64_t>(this, &emul_api_t::write_complete);
    const char* str = "vis_test";
    std::string s = str;
  	mcMem = getMemorySystemInstance("ini/DDR3_micron_32M_8B_x4_sg125.ini", "system.ini", "..", "plasticineController", 32768);
  	
  	uint64_t cpuClkFreqHz = 622222222;
  	mcMem->setCPUClockSpeed(cpuClkFreqHz);
  	mcMem->RegisterCallbacks(read_cb, write_cb, power_callback);
    is_exit = false;
    isReadCallbackComplete = false;
  }

  inline bool exit() { return is_exit; }

protected:
  mod_t* module;
  MultiChannelMemorySystem* mcMem;
  std::map<uint32_t, std::vector<uint32_t>* > memMap;
  bool isReadCallbackComplete;

private:
  static void power_callback(double a, double b, double c, double d)
  {
	  printf("power callback: %0.3f, %0.3f, %0.3f, %0.3f\n",a,b,c,d);
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
	cout << ">>>>>>>>>>RESET<<<<<<<<<<" << endl;
    module->clock(LIT<1>(1));
    // FIXME: should call twice to get the output for now
    module->clock_lo(LIT<1>(0), false);
  }



  int stepCount;
  virtual inline void start() {
  	cout << ">>>>>>>>>>START<<<<<<<<<<" << endl;

	stepCount = 0;
	//pctrl = new PlasticineControllerTop_t();
  }

  bool is_exit;
  virtual inline void finish() {
    mcMem->printStats(true);
    module->clock(LIT<1>(0)); // to vcd-dump the last cycle
    is_exit = true;
  }


  void read_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
  	PlasticineControllerTop_t *pctrl = (PlasticineControllerTop_t *)module;
    pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1;
    isReadCallbackComplete = true;
  	printf("[Callback] read complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
  }

  void write_complete(unsigned id, uint64_t address, uint64_t clock_cycle)
  {
  	PlasticineControllerTop_t *pctrl = (PlasticineControllerTop_t *)module;
    pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1;
  	printf("[Callback] write complete: %d 0x%lx cycle=%lu\n", id, address, clock_cycle);
  }

  virtual inline void step() {
    stepCount ++;
    mcMem->update();
  	PlasticineControllerTop_t *pctrl = (PlasticineControllerTop_t *)module;
  	int state = (int)pctrl->PlasticineControllerTop_controller__state.values[0];
  	cout << "state = " << state << endl;
    module->clock(LIT<1>(0));
    // FIXME: should call twice to get the output for now
    module->clock_lo(LIT<1>(0), false);
    if (pctrl->PlasticineControllerTop_dramsim__TX_ENQ.values[0] > 0)
    {
      bool isWR = pctrl->PlasticineControllerTop_isWrFifo__io_deq_dat.values[0];
      uint64_t addr = pctrl->PlasticineControllerTop_addrFifo__io_deq_dat.values[0];
      mcMem->addTransaction(isWR, addr);
      cout << "addr = " << pctrl->PlasticineControllerTop_isWrFifo__io_deq_dat.values[0] << endl;
    }

  	if (state == 3)
  	{
      pctrl->PlasticineControllerTop_dramsim__TX_COMP.values[0] = 0;
  	}

    // case for writing
    if (pctrl->PlasticineControllerTop__io_isWR.values[0] == 1 && pctrl->PlasticineControllerTop__io_enq_val.values[0] == 1)
    {
      uint32_t addr = pctrl->PlasticineControllerTop__io_addr.values[0];
      if (memMap.count(addr))
      {
        std::vector<uint32_t>* burstData = memMap[addr];
        burstData->at(0) = pctrl->PlasticineControllerTop__io_dataIn_0.values[0];
        burstData->at(1) = pctrl->PlasticineControllerTop__io_dataIn_1.values[0];
        burstData->at(2) = pctrl->PlasticineControllerTop__io_dataIn_2.values[0];
        burstData->at(3) = pctrl->PlasticineControllerTop__io_dataIn_3.values[0];
        burstData->at(4) = pctrl->PlasticineControllerTop__io_dataIn_4.values[0];
        burstData->at(5) = pctrl->PlasticineControllerTop__io_dataIn_5.values[0];
        burstData->at(6) = pctrl->PlasticineControllerTop__io_dataIn_6.values[0];
        burstData->at(7) = pctrl->PlasticineControllerTop__io_dataIn_7.values[0];
        burstData->at(8) = pctrl->PlasticineControllerTop__io_dataIn_8.values[0];
        burstData->at(9) = pctrl->PlasticineControllerTop__io_dataIn_9.values[0];
        burstData->at(10) = pctrl->PlasticineControllerTop__io_dataIn_10.values[0];
        burstData->at(11) = pctrl->PlasticineControllerTop__io_dataIn_11.values[0];
        burstData->at(12) = pctrl->PlasticineControllerTop__io_dataIn_12.values[0];
        burstData->at(13) = pctrl->PlasticineControllerTop__io_dataIn_13.values[0];
        burstData->at(14) = pctrl->PlasticineControllerTop__io_dataIn_14.values[0];
        burstData->at(15) = pctrl->PlasticineControllerTop__io_dataIn_15.values[0];
      }
      else
      {
        // TODO: need to free this part of memory
        std::vector<uint32_t>* burstData = new std::vector<uint32_t>();
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_0.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_1.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_2.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_3.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_4.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_5.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_6.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_7.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_8.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_9.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_10.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_11.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_12.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_13.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_14.values[0]);
        burstData->push_back(pctrl->PlasticineControllerTop__io_dataIn_15.values[0]);
        memMap[addr] = burstData;
      }
    }

    // case for reading
    if (pctrl->PlasticineControllerTop_controller__io_done.values[0] == 1 && isReadCallbackComplete)
    {
      std::vector<uint32_t> *burstData = memMap[pctrl->PlasticineControllerTop_addrFifo__io_deq_dat.values[0]];
      // TODO: need to handle the case where an entry is not found
      pctrl->PlasticineControllerTop__io_dataOut_0.values[0] = burstData->at(0);
      pctrl->PlasticineControllerTop__io_dataOut_1.values[0] = burstData->at(1);
      pctrl->PlasticineControllerTop__io_dataOut_2.values[0] = burstData->at(2);
      pctrl->PlasticineControllerTop__io_dataOut_3.values[0] = burstData->at(3);
      pctrl->PlasticineControllerTop__io_dataOut_4.values[0] = burstData->at(4);
      pctrl->PlasticineControllerTop__io_dataOut_5.values[0] = burstData->at(5);
      pctrl->PlasticineControllerTop__io_dataOut_6.values[0] = burstData->at(6);
      pctrl->PlasticineControllerTop__io_dataOut_7.values[0] = burstData->at(7);
      pctrl->PlasticineControllerTop__io_dataOut_8.values[0] = burstData->at(8);
      pctrl->PlasticineControllerTop__io_dataOut_9.values[0] = burstData->at(9);
      pctrl->PlasticineControllerTop__io_dataOut_10.values[0] = burstData->at(10);
      pctrl->PlasticineControllerTop__io_dataOut_11.values[0] = burstData->at(11);
      pctrl->PlasticineControllerTop__io_dataOut_12.values[0] = burstData->at(12);
      pctrl->PlasticineControllerTop__io_dataOut_13.values[0] = burstData->at(13);
      pctrl->PlasticineControllerTop__io_dataOut_14.values[0] = burstData->at(14);
      pctrl->PlasticineControllerTop__io_dataOut_15.values[0] = burstData->at(15);
    }
  }

  virtual inline void update() {
    module->clock_lo(LIT<1>(0), false);
  }
};

#endif
