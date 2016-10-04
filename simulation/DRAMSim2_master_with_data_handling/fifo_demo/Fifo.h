#ifndef __Fifo__
#define __Fifo__

#include "emulator.h"

class Fifo_t : public mod_t {
 private:
  val_t __rand_seed;
  void __srand(val_t seed) { __rand_seed = seed; }
  val_t __rand_val() { return ::__rand_val(&__rand_seed); }
 public:
  dat_t<1> Fifo__is_empty;
  dat_t<1> Fifo__io_deq_val;
  dat_t<1> Fifo__io_deq_rdy;
  dat_t<1> Fifo__do_deq;
  dat_t<1> Fifo__io_enq_val;
  dat_t<1> Fifo__io_enq_rdy;
  dat_t<1> Fifo__do_enq;
  dat_t<1> Fifo__is_full_next;
  dat_t<1> reset;
  dat_t<1> T10;
  dat_t<1> Fifo__is_full;
  dat_t<2> Fifo__enq_ptr_inc;
  dat_t<2> Fifo__deq_ptr_inc;
  dat_t<2> T12;
  dat_t<2> Fifo__deq_ptr;
  dat_t<2> T14;
  dat_t<2> Fifo__enq_ptr;
  dat_t<32> Fifo__io_enq_dat;
  dat_t<32> Fifo__io_deq_dat;
  dat_t<64> T15;
  mem_t<64,4> Fifo__ram;
  dat_t<1> Fifo__is_empty__prev;
  dat_t<1> Fifo__io_deq_val__prev;
  dat_t<1> Fifo__io_deq_rdy__prev;
  dat_t<1> Fifo__do_deq__prev;
  dat_t<1> Fifo__io_enq_val__prev;
  dat_t<1> Fifo__io_enq_rdy__prev;
  dat_t<1> Fifo__do_enq__prev;
  dat_t<1> Fifo__is_full_next__prev;
  dat_t<1> Fifo__is_full__prev;
  dat_t<2> Fifo__enq_ptr_inc__prev;
  dat_t<2> Fifo__deq_ptr_inc__prev;
  dat_t<2> Fifo__deq_ptr__prev;
  dat_t<2> Fifo__enq_ptr__prev;
  dat_t<32> Fifo__io_enq_dat__prev;
  dat_t<32> Fifo__io_deq_dat__prev;
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
class Fifo_api_t : public emul_api_t {
 public:
  Fifo_api_t(mod_t* m) : emul_api_t(m) { }
  void init_sim_data();
};

#endif
