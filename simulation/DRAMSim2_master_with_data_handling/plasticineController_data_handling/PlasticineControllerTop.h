#ifndef __PlasticineControllerTop__
#define __PlasticineControllerTop__

#include "emulator.h"

class PlasticineControllerTop_t : public mod_t {
 private:
  val_t __rand_seed;
  void __srand(val_t seed) { __rand_seed = seed; }
  val_t __rand_val() { return ::__rand_val(&__rand_seed); }
 public:
  dat_t<1> PlasticineControllerTop_addrFifo__is_empty;
  dat_t<1> PlasticineControllerTop_addrFifo__io_deq_val;
  dat_t<1> PlasticineControllerTop_controller__io_done;
  dat_t<1> PlasticineControllerTop_addrFifo__io_deq_rdy;
  dat_t<1> PlasticineControllerTop_addrFifo__do_deq;
  dat_t<1> PlasticineControllerTop__io_enq_val;
  dat_t<1> PlasticineControllerTop_addrFifo__io_enq_val;
  dat_t<1> PlasticineControllerTop_addrFifo__io_enq_rdy;
  dat_t<1> PlasticineControllerTop_addrFifo__do_enq;
  dat_t<1> PlasticineControllerTop_addrFifo__is_full_next;
  dat_t<1> reset;
  dat_t<1> PlasticineControllerTop_addrFifo__reset;
  dat_t<1> T11;
  dat_t<1> PlasticineControllerTop_addrFifo__is_full;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_empty;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_val;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_rdy;
  dat_t<1> PlasticineControllerTop_isWrFifo__do_deq;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_val;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_rdy;
  dat_t<1> PlasticineControllerTop_isWrFifo__do_enq;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_full_next;
  dat_t<1> PlasticineControllerTop_isWrFifo__reset;
  dat_t<1> T30;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_full;
  dat_t<1> PlasticineControllerTop__io_isWR;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_dat;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_dat;
  dat_t<1> PlasticineControllerTop_controller__io_start;
  dat_t<1> PlasticineControllerTop_controller__io_tx_enq;
  dat_t<1> PlasticineControllerTop_dramsim__TX_ENQ;
  dat_t<1> PlasticineControllerTop_dramsim__TX_COMP;
  dat_t<1> PlasticineControllerTop_controller__io_tx_comp;
  dat_t<1> PlasticineControllerTop_controller__reset;
  dat_t<1> PlasticineControllerTop__io_enq_rdy;
  dat_t<2> PlasticineControllerTop_addrFifo__enq_ptr_inc;
  dat_t<2> PlasticineControllerTop_addrFifo__deq_ptr_inc;
  dat_t<2> T13;
  dat_t<2> PlasticineControllerTop_addrFifo__deq_ptr;
  dat_t<2> T15;
  dat_t<2> PlasticineControllerTop_addrFifo__enq_ptr;
  dat_t<2> PlasticineControllerTop_isWrFifo__enq_ptr_inc;
  dat_t<2> PlasticineControllerTop_isWrFifo__deq_ptr_inc;
  dat_t<2> T32;
  dat_t<2> PlasticineControllerTop_isWrFifo__deq_ptr;
  dat_t<2> T34;
  dat_t<2> PlasticineControllerTop_isWrFifo__enq_ptr;
  dat_t<2> T64;
  dat_t<2> PlasticineControllerTop_controller__state;
  dat_t<32> PlasticineControllerTop_addrFifo__io_enq_dat;
  dat_t<32> PlasticineControllerTop_addrFifo__io_deq_dat;
  dat_t<32> PlasticineControllerTop__io_dataIn_15;
  dat_t<32> PlasticineControllerTop__io_dataOut_15;
  dat_t<32> PlasticineControllerTop__io_dataIn_14;
  dat_t<32> PlasticineControllerTop__io_dataOut_14;
  dat_t<32> PlasticineControllerTop__io_dataIn_13;
  dat_t<32> PlasticineControllerTop__io_dataOut_13;
  dat_t<32> PlasticineControllerTop__io_dataIn_12;
  dat_t<32> PlasticineControllerTop__io_dataOut_12;
  dat_t<32> PlasticineControllerTop__io_dataIn_11;
  dat_t<32> PlasticineControllerTop__io_dataOut_11;
  dat_t<32> PlasticineControllerTop__io_dataIn_10;
  dat_t<32> PlasticineControllerTop__io_dataOut_10;
  dat_t<32> PlasticineControllerTop__io_dataIn_9;
  dat_t<32> PlasticineControllerTop__io_dataOut_9;
  dat_t<32> PlasticineControllerTop__io_dataIn_8;
  dat_t<32> PlasticineControllerTop__io_dataOut_8;
  dat_t<32> PlasticineControllerTop__io_dataIn_7;
  dat_t<32> PlasticineControllerTop__io_dataOut_7;
  dat_t<32> PlasticineControllerTop__io_dataIn_6;
  dat_t<32> PlasticineControllerTop__io_dataOut_6;
  dat_t<32> PlasticineControllerTop__io_dataIn_5;
  dat_t<32> PlasticineControllerTop__io_dataOut_5;
  dat_t<32> PlasticineControllerTop__io_dataIn_4;
  dat_t<32> PlasticineControllerTop__io_dataOut_4;
  dat_t<32> PlasticineControllerTop__io_dataIn_3;
  dat_t<32> PlasticineControllerTop__io_dataOut_3;
  dat_t<32> PlasticineControllerTop__io_dataIn_2;
  dat_t<32> PlasticineControllerTop__io_dataOut_2;
  dat_t<32> PlasticineControllerTop__io_dataIn_1;
  dat_t<32> PlasticineControllerTop__io_dataOut_1;
  dat_t<32> PlasticineControllerTop__io_dataIn_0;
  dat_t<32> PlasticineControllerTop__io_dataOut_0;
  dat_t<64> PlasticineControllerTop__io_addr;
  dat_t<64> T17;
  dat_t<64> T35;
  mem_t<64,4> PlasticineControllerTop_addrFifo__ram;
  mem_t<64,4> PlasticineControllerTop_isWrFifo__ram;
  dat_t<1> PlasticineControllerTop_addrFifo__is_empty__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__io_deq_val__prev;
  dat_t<1> PlasticineControllerTop_controller__io_done__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__io_deq_rdy__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__do_deq__prev;
  dat_t<1> PlasticineControllerTop__io_enq_val__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__io_enq_val__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__io_enq_rdy__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__do_enq__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__is_full_next__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__reset__prev;
  dat_t<1> PlasticineControllerTop_addrFifo__is_full__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_empty__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_val__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_rdy__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__do_deq__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_val__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_rdy__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__do_enq__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_full_next__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__reset__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__is_full__prev;
  dat_t<1> PlasticineControllerTop__io_isWR__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_enq_dat__prev;
  dat_t<1> PlasticineControllerTop_isWrFifo__io_deq_dat__prev;
  dat_t<1> PlasticineControllerTop_controller__io_start__prev;
  dat_t<1> PlasticineControllerTop_controller__io_tx_enq__prev;
  dat_t<1> PlasticineControllerTop_dramsim__TX_ENQ__prev;
  dat_t<1> PlasticineControllerTop_dramsim__TX_COMP__prev;
  dat_t<1> PlasticineControllerTop_controller__io_tx_comp__prev;
  dat_t<1> PlasticineControllerTop_controller__reset__prev;
  dat_t<1> PlasticineControllerTop__io_enq_rdy__prev;
  dat_t<2> PlasticineControllerTop_addrFifo__enq_ptr_inc__prev;
  dat_t<2> PlasticineControllerTop_addrFifo__deq_ptr_inc__prev;
  dat_t<2> PlasticineControllerTop_addrFifo__deq_ptr__prev;
  dat_t<2> PlasticineControllerTop_addrFifo__enq_ptr__prev;
  dat_t<2> PlasticineControllerTop_isWrFifo__enq_ptr_inc__prev;
  dat_t<2> PlasticineControllerTop_isWrFifo__deq_ptr_inc__prev;
  dat_t<2> PlasticineControllerTop_isWrFifo__deq_ptr__prev;
  dat_t<2> PlasticineControllerTop_isWrFifo__enq_ptr__prev;
  dat_t<2> PlasticineControllerTop_controller__state__prev;
  dat_t<32> PlasticineControllerTop_addrFifo__io_enq_dat__prev;
  dat_t<32> PlasticineControllerTop_addrFifo__io_deq_dat__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_15__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_15__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_14__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_14__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_13__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_13__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_12__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_12__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_11__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_11__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_10__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_10__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_9__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_9__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_8__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_8__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_7__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_7__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_6__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_6__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_5__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_5__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_4__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_4__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_3__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_3__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_2__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_2__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_1__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_1__prev;
  dat_t<32> PlasticineControllerTop__io_dataIn_0__prev;
  dat_t<32> PlasticineControllerTop__io_dataOut_0__prev;
  dat_t<64> PlasticineControllerTop__io_addr__prev;
  clk_t clk;
  dat_t<1> reset__prev;

  void init ( val_t rand_init = 0 );
  void clock_lo ( dat_t<1> reset, bool assert_fire=true );
  void clock_hi ( dat_t<1> reset );
  int clock ( dat_t<1> reset );
  void print ( FILE* f );
  void print ( std::ostream& s );
  void dump ( FILE* f, val_t t, dat_t<1> reset=LIT<1>(0) );
  void dump_init ( FILE* f );

};

#include "emul_api.h"
class PlasticineControllerTop_api_t : public emul_api_t {
 public:
  PlasticineControllerTop_api_t(mod_t* m) : emul_api_t(m) { }
  void init_sim_data();
};

#endif
