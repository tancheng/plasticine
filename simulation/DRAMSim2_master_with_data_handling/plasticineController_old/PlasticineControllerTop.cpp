#include "PlasticineControllerTop.h"

void PlasticineControllerTop_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
  PlasticineControllerTop_addrFifo__is_full.randomize(&__rand_seed);
  PlasticineControllerTop_addrFifo__deq_ptr.randomize(&__rand_seed);
  PlasticineControllerTop_addrFifo__enq_ptr.randomize(&__rand_seed);
  PlasticineControllerTop_addrFifo__ram.randomize(&__rand_seed);
  PlasticineControllerTop_isWrFifo__is_full.randomize(&__rand_seed);
  PlasticineControllerTop_isWrFifo__deq_ptr.randomize(&__rand_seed);
  PlasticineControllerTop_isWrFifo__enq_ptr.randomize(&__rand_seed);
  PlasticineControllerTop_isWrFifo__ram.randomize(&__rand_seed);
  PlasticineControllerTop_controller__state.randomize(&__rand_seed);
  clk.len = 1;
  clk.cnt = 0;
  clk.values[0] = 0;
  // values needed to be initialized at first
  PlasticineControllerTop_dramsim__TX_COMP.values[0] = 0;
  PlasticineControllerTop__io_dataOut_15.values[0] = 0;
  PlasticineControllerTop__io_dataOut_14.values[0] = 0;
  PlasticineControllerTop__io_dataOut_13.values[0] = 0;
  PlasticineControllerTop__io_dataOut_12.values[0] = 0;
  PlasticineControllerTop__io_dataOut_11.values[0] = 0;
  PlasticineControllerTop__io_dataOut_10.values[0] = 0;
  PlasticineControllerTop__io_dataOut_9.values[0] = 0;
  PlasticineControllerTop__io_dataOut_8.values[0] = 0;
  PlasticineControllerTop__io_dataOut_7.values[0] = 0;
  PlasticineControllerTop__io_dataOut_6.values[0] = 0;
  PlasticineControllerTop__io_dataOut_5.values[0] = 0;
  PlasticineControllerTop__io_dataOut_4.values[0] = 0;
  PlasticineControllerTop__io_dataOut_3.values[0] = 0;
  PlasticineControllerTop__io_dataOut_2.values[0] = 0;
  PlasticineControllerTop__io_dataOut_1.values[0] = 0;
  PlasticineControllerTop__io_dataOut_0.values[0] = 0;
}


int PlasticineControllerTop_t::clock ( dat_t<1> reset ) {
  uint32_t min = ((uint32_t)1<<31)-1;
  if (clk.cnt < min) min = clk.cnt;
  clk.cnt-=min;
  if (clk.cnt == 0) clock_lo( reset );
  if (!reset.to_bool()) print( std::cerr );
  mod_t::dump( reset );
  if (clk.cnt == 0) clock_hi( reset );
  if (clk.cnt == 0) clk.cnt = clk.len;
  return min;
}


void PlasticineControllerTop_t::print ( FILE* f ) {
}
void PlasticineControllerTop_t::print ( std::ostream& s ) {
}


void PlasticineControllerTop_t::dump_init ( FILE* f ) {
  fputs("$timescale 1ps $end\n", f);
  fputs("$scope module PlasticineControllerTop $end\n", f);
  fputs("$var wire 1 \x21 clk $end\n", f);
  fputs("$var wire 1 \x22 reset $end\n", f);
  fputs("$var wire 1 \x28 io_enq_val $end\n", f);
  fputs("$var wire 1 \x39 io_isWR $end\n", f);
  fputs("$var wire 1 \x42 io_enq_rdy $end\n", f);
  fputs("$var wire 32 \x4e io_dataIn_15 $end\n", f);
  fputs("$var wire 32 \x4f io_dataOut_15 $end\n", f);
  fputs("$var wire 32 \x50 io_dataIn_14 $end\n", f);
  fputs("$var wire 32 \x51 io_dataOut_14 $end\n", f);
  fputs("$var wire 32 \x52 io_dataIn_13 $end\n", f);
  fputs("$var wire 32 \x53 io_dataOut_13 $end\n", f);
  fputs("$var wire 32 \x54 io_dataIn_12 $end\n", f);
  fputs("$var wire 32 \x55 io_dataOut_12 $end\n", f);
  fputs("$var wire 32 \x56 io_dataIn_11 $end\n", f);
  fputs("$var wire 32 \x57 io_dataOut_11 $end\n", f);
  fputs("$var wire 32 \x58 io_dataIn_10 $end\n", f);
  fputs("$var wire 32 \x59 io_dataOut_10 $end\n", f);
  fputs("$var wire 32 \x5a io_dataIn_9 $end\n", f);
  fputs("$var wire 32 \x5b io_dataOut_9 $end\n", f);
  fputs("$var wire 32 \x5c io_dataIn_8 $end\n", f);
  fputs("$var wire 32 \x5d io_dataOut_8 $end\n", f);
  fputs("$var wire 32 \x5e io_dataIn_7 $end\n", f);
  fputs("$var wire 32 \x5f io_dataOut_7 $end\n", f);
  fputs("$var wire 32 \x60 io_dataIn_6 $end\n", f);
  fputs("$var wire 32 \x61 io_dataOut_6 $end\n", f);
  fputs("$var wire 32 \x62 io_dataIn_5 $end\n", f);
  fputs("$var wire 32 \x63 io_dataOut_5 $end\n", f);
  fputs("$var wire 32 \x64 io_dataIn_4 $end\n", f);
  fputs("$var wire 32 \x65 io_dataOut_4 $end\n", f);
  fputs("$var wire 32 \x66 io_dataIn_3 $end\n", f);
  fputs("$var wire 32 \x67 io_dataOut_3 $end\n", f);
  fputs("$var wire 32 \x68 io_dataIn_2 $end\n", f);
  fputs("$var wire 32 \x69 io_dataOut_2 $end\n", f);
  fputs("$var wire 32 \x6a io_dataIn_1 $end\n", f);
  fputs("$var wire 32 \x6b io_dataOut_1 $end\n", f);
  fputs("$var wire 32 \x6c io_dataIn_0 $end\n", f);
  fputs("$var wire 32 \x6d io_dataOut_0 $end\n", f);
  fputs("$var wire 64 \x6e io_addr $end\n", f);
  fputs("$scope module controller $end\n", f);
  fputs("$var wire 1 \x25 io_done $end\n", f);
  fputs("$var wire 1 \x3c io_start $end\n", f);
  fputs("$var wire 1 \x3d io_tx_enq $end\n", f);
  fputs("$var wire 1 \x40 io_tx_comp $end\n", f);
  fputs("$var wire 1 \x41 reset $end\n", f);
  fputs("$var wire 2 \x4b state $end\n", f);
  fputs("$upscope $end\n", f);
  fputs("$scope module dramsim $end\n", f);
  fputs("$var wire 1 \x3e TX_ENQ $end\n", f);
  fputs("$var wire 1 \x3f TX_COMP $end\n", f);
  fputs("$upscope $end\n", f);
  fputs("$scope module isWrFifo $end\n", f);
  fputs("$var wire 1 \x2f is_empty $end\n", f);
  fputs("$var wire 1 \x30 io_deq_val $end\n", f);
  fputs("$var wire 1 \x31 io_deq_rdy $end\n", f);
  fputs("$var wire 1 \x32 do_deq $end\n", f);
  fputs("$var wire 1 \x33 io_enq_val $end\n", f);
  fputs("$var wire 1 \x34 io_enq_rdy $end\n", f);
  fputs("$var wire 1 \x35 do_enq $end\n", f);
  fputs("$var wire 1 \x36 is_full_next $end\n", f);
  fputs("$var wire 1 \x37 reset $end\n", f);
  fputs("$var wire 1 \x38 is_full $end\n", f);
  fputs("$var wire 1 \x3a io_enq_dat $end\n", f);
  fputs("$var wire 1 \x3b io_deq_dat $end\n", f);
  fputs("$var wire 2 \x47 enq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x48 deq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x49 deq_ptr $end\n", f);
  fputs("$var wire 2 \x4a enq_ptr $end\n", f);
  fputs("$upscope $end\n", f);
  fputs("$scope module addrFifo $end\n", f);
  fputs("$var wire 1 \x23 is_empty $end\n", f);
  fputs("$var wire 1 \x24 io_deq_val $end\n", f);
  fputs("$var wire 1 \x26 io_deq_rdy $end\n", f);
  fputs("$var wire 1 \x27 do_deq $end\n", f);
  fputs("$var wire 1 \x29 io_enq_val $end\n", f);
  fputs("$var wire 1 \x2a io_enq_rdy $end\n", f);
  fputs("$var wire 1 \x2b do_enq $end\n", f);
  fputs("$var wire 1 \x2c is_full_next $end\n", f);
  fputs("$var wire 1 \x2d reset $end\n", f);
  fputs("$var wire 1 \x2e is_full $end\n", f);
  fputs("$var wire 2 \x43 enq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x44 deq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x45 deq_ptr $end\n", f);
  fputs("$var wire 2 \x46 enq_ptr $end\n", f);
  fputs("$var wire 32 \x4c io_enq_dat $end\n", f);
  fputs("$var wire 32 \x4d io_deq_dat $end\n", f);
  fputs("$upscope $end\n", f);
  fputs("$upscope $end\n", f);
  fputs("$enddefinitions $end\n", f);
  fputs("$dumpvars\n", f);
  fputs("$end\n", f);
  fputs("#0\n", f);
  if (clk.cnt == 0) {
    clk.values[0] = 1;
    dat_dump<1>(f, clk, 0x21);
  }
  dat_t<1> reset = LIT<1>(1);
  dat_dump<1>(f, reset, 0x22);
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_empty, 0x23);
  PlasticineControllerTop_addrFifo__is_empty__prev = PlasticineControllerTop_addrFifo__is_empty;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_val, 0x24);
  PlasticineControllerTop_addrFifo__io_deq_val__prev = PlasticineControllerTop_addrFifo__io_deq_val;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_done, 0x25);
  PlasticineControllerTop_controller__io_done__prev = PlasticineControllerTop_controller__io_done;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_rdy, 0x26);
  PlasticineControllerTop_addrFifo__io_deq_rdy__prev = PlasticineControllerTop_addrFifo__io_deq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__do_deq, 0x27);
  PlasticineControllerTop_addrFifo__do_deq__prev = PlasticineControllerTop_addrFifo__do_deq;
  dat_dump<1>(f, PlasticineControllerTop__io_enq_val, 0x28);
  PlasticineControllerTop__io_enq_val__prev = PlasticineControllerTop__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_val, 0x29);
  PlasticineControllerTop_addrFifo__io_enq_val__prev = PlasticineControllerTop_addrFifo__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_rdy, 0x2a);
  PlasticineControllerTop_addrFifo__io_enq_rdy__prev = PlasticineControllerTop_addrFifo__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__do_enq, 0x2b);
  PlasticineControllerTop_addrFifo__do_enq__prev = PlasticineControllerTop_addrFifo__do_enq;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_full_next, 0x2c);
  PlasticineControllerTop_addrFifo__is_full_next__prev = PlasticineControllerTop_addrFifo__is_full_next;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__reset, 0x2d);
  PlasticineControllerTop_addrFifo__reset__prev = PlasticineControllerTop_addrFifo__reset;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_full, 0x2e);
  PlasticineControllerTop_addrFifo__is_full__prev = PlasticineControllerTop_addrFifo__is_full;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_empty, 0x2f);
  PlasticineControllerTop_isWrFifo__is_empty__prev = PlasticineControllerTop_isWrFifo__is_empty;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_val, 0x30);
  PlasticineControllerTop_isWrFifo__io_deq_val__prev = PlasticineControllerTop_isWrFifo__io_deq_val;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_rdy, 0x31);
  PlasticineControllerTop_isWrFifo__io_deq_rdy__prev = PlasticineControllerTop_isWrFifo__io_deq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__do_deq, 0x32);
  PlasticineControllerTop_isWrFifo__do_deq__prev = PlasticineControllerTop_isWrFifo__do_deq;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_val, 0x33);
  PlasticineControllerTop_isWrFifo__io_enq_val__prev = PlasticineControllerTop_isWrFifo__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_rdy, 0x34);
  PlasticineControllerTop_isWrFifo__io_enq_rdy__prev = PlasticineControllerTop_isWrFifo__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__do_enq, 0x35);
  PlasticineControllerTop_isWrFifo__do_enq__prev = PlasticineControllerTop_isWrFifo__do_enq;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_full_next, 0x36);
  PlasticineControllerTop_isWrFifo__is_full_next__prev = PlasticineControllerTop_isWrFifo__is_full_next;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__reset, 0x37);
  PlasticineControllerTop_isWrFifo__reset__prev = PlasticineControllerTop_isWrFifo__reset;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_full, 0x38);
  PlasticineControllerTop_isWrFifo__is_full__prev = PlasticineControllerTop_isWrFifo__is_full;
  dat_dump<1>(f, PlasticineControllerTop__io_isWR, 0x39);
  PlasticineControllerTop__io_isWR__prev = PlasticineControllerTop__io_isWR;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_dat, 0x3a);
  PlasticineControllerTop_isWrFifo__io_enq_dat__prev = PlasticineControllerTop_isWrFifo__io_enq_dat;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_dat, 0x3b);
  PlasticineControllerTop_isWrFifo__io_deq_dat__prev = PlasticineControllerTop_isWrFifo__io_deq_dat;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_start, 0x3c);
  PlasticineControllerTop_controller__io_start__prev = PlasticineControllerTop_controller__io_start;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_tx_enq, 0x3d);
  PlasticineControllerTop_controller__io_tx_enq__prev = PlasticineControllerTop_controller__io_tx_enq;
  dat_dump<1>(f, PlasticineControllerTop_dramsim__TX_ENQ, 0x3e);
  PlasticineControllerTop_dramsim__TX_ENQ__prev = PlasticineControllerTop_dramsim__TX_ENQ;
  dat_dump<1>(f, PlasticineControllerTop_dramsim__TX_COMP, 0x3f);
  PlasticineControllerTop_dramsim__TX_COMP__prev = PlasticineControllerTop_dramsim__TX_COMP;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_tx_comp, 0x40);
  PlasticineControllerTop_controller__io_tx_comp__prev = PlasticineControllerTop_controller__io_tx_comp;
  dat_dump<1>(f, PlasticineControllerTop_controller__reset, 0x41);
  PlasticineControllerTop_controller__reset__prev = PlasticineControllerTop_controller__reset;
  dat_dump<1>(f, PlasticineControllerTop__io_enq_rdy, 0x42);
  PlasticineControllerTop__io_enq_rdy__prev = PlasticineControllerTop__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__enq_ptr_inc, 0x43);
  PlasticineControllerTop_addrFifo__enq_ptr_inc__prev = PlasticineControllerTop_addrFifo__enq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__deq_ptr_inc, 0x44);
  PlasticineControllerTop_addrFifo__deq_ptr_inc__prev = PlasticineControllerTop_addrFifo__deq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__deq_ptr, 0x45);
  PlasticineControllerTop_addrFifo__deq_ptr__prev = PlasticineControllerTop_addrFifo__deq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__enq_ptr, 0x46);
  PlasticineControllerTop_addrFifo__enq_ptr__prev = PlasticineControllerTop_addrFifo__enq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__enq_ptr_inc, 0x47);
  PlasticineControllerTop_isWrFifo__enq_ptr_inc__prev = PlasticineControllerTop_isWrFifo__enq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__deq_ptr_inc, 0x48);
  PlasticineControllerTop_isWrFifo__deq_ptr_inc__prev = PlasticineControllerTop_isWrFifo__deq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__deq_ptr, 0x49);
  PlasticineControllerTop_isWrFifo__deq_ptr__prev = PlasticineControllerTop_isWrFifo__deq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__enq_ptr, 0x4a);
  PlasticineControllerTop_isWrFifo__enq_ptr__prev = PlasticineControllerTop_isWrFifo__enq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_controller__state, 0x4b);
  PlasticineControllerTop_controller__state__prev = PlasticineControllerTop_controller__state;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_dat, 0x4c);
  PlasticineControllerTop_addrFifo__io_enq_dat__prev = PlasticineControllerTop_addrFifo__io_enq_dat;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_dat, 0x4d);
  PlasticineControllerTop_addrFifo__io_deq_dat__prev = PlasticineControllerTop_addrFifo__io_deq_dat;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_15, 0x4e);
  PlasticineControllerTop__io_dataIn_15__prev = PlasticineControllerTop__io_dataIn_15;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_15, 0x4f);
  PlasticineControllerTop__io_dataOut_15__prev = PlasticineControllerTop__io_dataOut_15;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_14, 0x50);
  PlasticineControllerTop__io_dataIn_14__prev = PlasticineControllerTop__io_dataIn_14;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_14, 0x51);
  PlasticineControllerTop__io_dataOut_14__prev = PlasticineControllerTop__io_dataOut_14;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_13, 0x52);
  PlasticineControllerTop__io_dataIn_13__prev = PlasticineControllerTop__io_dataIn_13;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_13, 0x53);
  PlasticineControllerTop__io_dataOut_13__prev = PlasticineControllerTop__io_dataOut_13;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_12, 0x54);
  PlasticineControllerTop__io_dataIn_12__prev = PlasticineControllerTop__io_dataIn_12;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_12, 0x55);
  PlasticineControllerTop__io_dataOut_12__prev = PlasticineControllerTop__io_dataOut_12;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_11, 0x56);
  PlasticineControllerTop__io_dataIn_11__prev = PlasticineControllerTop__io_dataIn_11;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_11, 0x57);
  PlasticineControllerTop__io_dataOut_11__prev = PlasticineControllerTop__io_dataOut_11;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_10, 0x58);
  PlasticineControllerTop__io_dataIn_10__prev = PlasticineControllerTop__io_dataIn_10;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_10, 0x59);
  PlasticineControllerTop__io_dataOut_10__prev = PlasticineControllerTop__io_dataOut_10;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_9, 0x5a);
  PlasticineControllerTop__io_dataIn_9__prev = PlasticineControllerTop__io_dataIn_9;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_9, 0x5b);
  PlasticineControllerTop__io_dataOut_9__prev = PlasticineControllerTop__io_dataOut_9;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_8, 0x5c);
  PlasticineControllerTop__io_dataIn_8__prev = PlasticineControllerTop__io_dataIn_8;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_8, 0x5d);
  PlasticineControllerTop__io_dataOut_8__prev = PlasticineControllerTop__io_dataOut_8;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_7, 0x5e);
  PlasticineControllerTop__io_dataIn_7__prev = PlasticineControllerTop__io_dataIn_7;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_7, 0x5f);
  PlasticineControllerTop__io_dataOut_7__prev = PlasticineControllerTop__io_dataOut_7;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_6, 0x60);
  PlasticineControllerTop__io_dataIn_6__prev = PlasticineControllerTop__io_dataIn_6;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_6, 0x61);
  PlasticineControllerTop__io_dataOut_6__prev = PlasticineControllerTop__io_dataOut_6;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_5, 0x62);
  PlasticineControllerTop__io_dataIn_5__prev = PlasticineControllerTop__io_dataIn_5;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_5, 0x63);
  PlasticineControllerTop__io_dataOut_5__prev = PlasticineControllerTop__io_dataOut_5;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_4, 0x64);
  PlasticineControllerTop__io_dataIn_4__prev = PlasticineControllerTop__io_dataIn_4;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_4, 0x65);
  PlasticineControllerTop__io_dataOut_4__prev = PlasticineControllerTop__io_dataOut_4;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_3, 0x66);
  PlasticineControllerTop__io_dataIn_3__prev = PlasticineControllerTop__io_dataIn_3;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_3, 0x67);
  PlasticineControllerTop__io_dataOut_3__prev = PlasticineControllerTop__io_dataOut_3;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_2, 0x68);
  PlasticineControllerTop__io_dataIn_2__prev = PlasticineControllerTop__io_dataIn_2;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_2, 0x69);
  PlasticineControllerTop__io_dataOut_2__prev = PlasticineControllerTop__io_dataOut_2;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_1, 0x6a);
  PlasticineControllerTop__io_dataIn_1__prev = PlasticineControllerTop__io_dataIn_1;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_1, 0x6b);
  PlasticineControllerTop__io_dataOut_1__prev = PlasticineControllerTop__io_dataOut_1;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_0, 0x6c);
  PlasticineControllerTop__io_dataIn_0__prev = PlasticineControllerTop__io_dataIn_0;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_0, 0x6d);
  PlasticineControllerTop__io_dataOut_0__prev = PlasticineControllerTop__io_dataOut_0;
  dat_dump<1>(f, PlasticineControllerTop__io_addr, 0x6e);
  PlasticineControllerTop__io_addr__prev = PlasticineControllerTop__io_addr;
  fputs("#1\n", f);
  if (clk.cnt == 0) {
    clk.values[0] = 0;
    dat_dump<1>(f, clk, 0x21);
  }
}


void PlasticineControllerTop_t::dump ( FILE* f, val_t t, dat_t<1> reset ) {
  if (t == 0L) return dump_init(f);
  fprintf(f, "#%lu\n", t << 1);
  if (clk.cnt == 0)  goto L0;
K0:  if (reset != reset__prev)  goto L1;
K1:  if (PlasticineControllerTop_addrFifo__is_empty != PlasticineControllerTop_addrFifo__is_empty__prev)  goto L2;
K2:  if (PlasticineControllerTop_addrFifo__io_deq_val != PlasticineControllerTop_addrFifo__io_deq_val__prev)  goto L3;
K3:  if (PlasticineControllerTop_controller__io_done != PlasticineControllerTop_controller__io_done__prev)  goto L4;
K4:  if (PlasticineControllerTop_addrFifo__io_deq_rdy != PlasticineControllerTop_addrFifo__io_deq_rdy__prev)  goto L5;
K5:  if (PlasticineControllerTop_addrFifo__do_deq != PlasticineControllerTop_addrFifo__do_deq__prev)  goto L6;
K6:  if (PlasticineControllerTop__io_enq_val != PlasticineControllerTop__io_enq_val__prev)  goto L7;
K7:  if (PlasticineControllerTop_addrFifo__io_enq_val != PlasticineControllerTop_addrFifo__io_enq_val__prev)  goto L8;
K8:  if (PlasticineControllerTop_addrFifo__io_enq_rdy != PlasticineControllerTop_addrFifo__io_enq_rdy__prev)  goto L9;
K9:  if (PlasticineControllerTop_addrFifo__do_enq != PlasticineControllerTop_addrFifo__do_enq__prev)  goto L10;
K10:  if (PlasticineControllerTop_addrFifo__is_full_next != PlasticineControllerTop_addrFifo__is_full_next__prev)  goto L11;
K11:  if (PlasticineControllerTop_addrFifo__reset != PlasticineControllerTop_addrFifo__reset__prev)  goto L12;
K12:  if (PlasticineControllerTop_addrFifo__is_full != PlasticineControllerTop_addrFifo__is_full__prev)  goto L13;
K13:  if (PlasticineControllerTop_isWrFifo__is_empty != PlasticineControllerTop_isWrFifo__is_empty__prev)  goto L14;
K14:  if (PlasticineControllerTop_isWrFifo__io_deq_val != PlasticineControllerTop_isWrFifo__io_deq_val__prev)  goto L15;
K15:  if (PlasticineControllerTop_isWrFifo__io_deq_rdy != PlasticineControllerTop_isWrFifo__io_deq_rdy__prev)  goto L16;
K16:  if (PlasticineControllerTop_isWrFifo__do_deq != PlasticineControllerTop_isWrFifo__do_deq__prev)  goto L17;
K17:  if (PlasticineControllerTop_isWrFifo__io_enq_val != PlasticineControllerTop_isWrFifo__io_enq_val__prev)  goto L18;
K18:  if (PlasticineControllerTop_isWrFifo__io_enq_rdy != PlasticineControllerTop_isWrFifo__io_enq_rdy__prev)  goto L19;
K19:  if (PlasticineControllerTop_isWrFifo__do_enq != PlasticineControllerTop_isWrFifo__do_enq__prev)  goto L20;
K20:  if (PlasticineControllerTop_isWrFifo__is_full_next != PlasticineControllerTop_isWrFifo__is_full_next__prev)  goto L21;
K21:  if (PlasticineControllerTop_isWrFifo__reset != PlasticineControllerTop_isWrFifo__reset__prev)  goto L22;
K22:  if (PlasticineControllerTop_isWrFifo__is_full != PlasticineControllerTop_isWrFifo__is_full__prev)  goto L23;
K23:  if (PlasticineControllerTop__io_isWR != PlasticineControllerTop__io_isWR__prev)  goto L24;
K24:  if (PlasticineControllerTop_isWrFifo__io_enq_dat != PlasticineControllerTop_isWrFifo__io_enq_dat__prev)  goto L25;
K25:  if (PlasticineControllerTop_isWrFifo__io_deq_dat != PlasticineControllerTop_isWrFifo__io_deq_dat__prev)  goto L26;
K26:  if (PlasticineControllerTop_controller__io_start != PlasticineControllerTop_controller__io_start__prev)  goto L27;
K27:  if (PlasticineControllerTop_controller__io_tx_enq != PlasticineControllerTop_controller__io_tx_enq__prev)  goto L28;
K28:  if (PlasticineControllerTop_dramsim__TX_ENQ != PlasticineControllerTop_dramsim__TX_ENQ__prev)  goto L29;
K29:  if (PlasticineControllerTop_dramsim__TX_COMP != PlasticineControllerTop_dramsim__TX_COMP__prev)  goto L30;
K30:  if (PlasticineControllerTop_controller__io_tx_comp != PlasticineControllerTop_controller__io_tx_comp__prev)  goto L31;
K31:  if (PlasticineControllerTop_controller__reset != PlasticineControllerTop_controller__reset__prev)  goto L32;
K32:  if (PlasticineControllerTop__io_enq_rdy != PlasticineControllerTop__io_enq_rdy__prev)  goto L33;
K33:  if (PlasticineControllerTop_addrFifo__enq_ptr_inc != PlasticineControllerTop_addrFifo__enq_ptr_inc__prev)  goto L34;
K34:  if (PlasticineControllerTop_addrFifo__deq_ptr_inc != PlasticineControllerTop_addrFifo__deq_ptr_inc__prev)  goto L35;
K35:  if (PlasticineControllerTop_addrFifo__deq_ptr != PlasticineControllerTop_addrFifo__deq_ptr__prev)  goto L36;
K36:  if (PlasticineControllerTop_addrFifo__enq_ptr != PlasticineControllerTop_addrFifo__enq_ptr__prev)  goto L37;
K37:  if (PlasticineControllerTop_isWrFifo__enq_ptr_inc != PlasticineControllerTop_isWrFifo__enq_ptr_inc__prev)  goto L38;
K38:  if (PlasticineControllerTop_isWrFifo__deq_ptr_inc != PlasticineControllerTop_isWrFifo__deq_ptr_inc__prev)  goto L39;
K39:  if (PlasticineControllerTop_isWrFifo__deq_ptr != PlasticineControllerTop_isWrFifo__deq_ptr__prev)  goto L40;
K40:  if (PlasticineControllerTop_isWrFifo__enq_ptr != PlasticineControllerTop_isWrFifo__enq_ptr__prev)  goto L41;
K41:  if (PlasticineControllerTop_controller__state != PlasticineControllerTop_controller__state__prev)  goto L42;
K42:  if (PlasticineControllerTop_addrFifo__io_enq_dat != PlasticineControllerTop_addrFifo__io_enq_dat__prev)  goto L43;
K43:  if (PlasticineControllerTop_addrFifo__io_deq_dat != PlasticineControllerTop_addrFifo__io_deq_dat__prev)  goto L44;
K44:  if (PlasticineControllerTop__io_dataIn_15 != PlasticineControllerTop__io_dataIn_15__prev)  goto L45;
K45:  if (PlasticineControllerTop__io_dataOut_15 != PlasticineControllerTop__io_dataOut_15__prev)  goto L46;
K46:  if (PlasticineControllerTop__io_dataIn_14 != PlasticineControllerTop__io_dataIn_14__prev)  goto L47;
K47:  if (PlasticineControllerTop__io_dataOut_14 != PlasticineControllerTop__io_dataOut_14__prev)  goto L48;
K48:  if (PlasticineControllerTop__io_dataIn_13 != PlasticineControllerTop__io_dataIn_13__prev)  goto L49;
K49:  if (PlasticineControllerTop__io_dataOut_13 != PlasticineControllerTop__io_dataOut_13__prev)  goto L50;
K50:  if (PlasticineControllerTop__io_dataIn_12 != PlasticineControllerTop__io_dataIn_12__prev)  goto L51;
K51:  if (PlasticineControllerTop__io_dataOut_12 != PlasticineControllerTop__io_dataOut_12__prev)  goto L52;
K52:  if (PlasticineControllerTop__io_dataIn_11 != PlasticineControllerTop__io_dataIn_11__prev)  goto L53;
K53:  if (PlasticineControllerTop__io_dataOut_11 != PlasticineControllerTop__io_dataOut_11__prev)  goto L54;
K54:  if (PlasticineControllerTop__io_dataIn_10 != PlasticineControllerTop__io_dataIn_10__prev)  goto L55;
K55:  if (PlasticineControllerTop__io_dataOut_10 != PlasticineControllerTop__io_dataOut_10__prev)  goto L56;
K56:  if (PlasticineControllerTop__io_dataIn_9 != PlasticineControllerTop__io_dataIn_9__prev)  goto L57;
K57:  if (PlasticineControllerTop__io_dataOut_9 != PlasticineControllerTop__io_dataOut_9__prev)  goto L58;
K58:  if (PlasticineControllerTop__io_dataIn_8 != PlasticineControllerTop__io_dataIn_8__prev)  goto L59;
K59:  if (PlasticineControllerTop__io_dataOut_8 != PlasticineControllerTop__io_dataOut_8__prev)  goto L60;
K60:  if (PlasticineControllerTop__io_dataIn_7 != PlasticineControllerTop__io_dataIn_7__prev)  goto L61;
K61:  if (PlasticineControllerTop__io_dataOut_7 != PlasticineControllerTop__io_dataOut_7__prev)  goto L62;
K62:  if (PlasticineControllerTop__io_dataIn_6 != PlasticineControllerTop__io_dataIn_6__prev)  goto L63;
K63:  if (PlasticineControllerTop__io_dataOut_6 != PlasticineControllerTop__io_dataOut_6__prev)  goto L64;
K64:  if (PlasticineControllerTop__io_dataIn_5 != PlasticineControllerTop__io_dataIn_5__prev)  goto L65;
K65:  if (PlasticineControllerTop__io_dataOut_5 != PlasticineControllerTop__io_dataOut_5__prev)  goto L66;
K66:  if (PlasticineControllerTop__io_dataIn_4 != PlasticineControllerTop__io_dataIn_4__prev)  goto L67;
K67:  if (PlasticineControllerTop__io_dataOut_4 != PlasticineControllerTop__io_dataOut_4__prev)  goto L68;
K68:  if (PlasticineControllerTop__io_dataIn_3 != PlasticineControllerTop__io_dataIn_3__prev)  goto L69;
K69:  if (PlasticineControllerTop__io_dataOut_3 != PlasticineControllerTop__io_dataOut_3__prev)  goto L70;
K70:  if (PlasticineControllerTop__io_dataIn_2 != PlasticineControllerTop__io_dataIn_2__prev)  goto L71;
K71:  if (PlasticineControllerTop__io_dataOut_2 != PlasticineControllerTop__io_dataOut_2__prev)  goto L72;
K72:  if (PlasticineControllerTop__io_dataIn_1 != PlasticineControllerTop__io_dataIn_1__prev)  goto L73;
K73:  if (PlasticineControllerTop__io_dataOut_1 != PlasticineControllerTop__io_dataOut_1__prev)  goto L74;
K74:  if (PlasticineControllerTop__io_dataIn_0 != PlasticineControllerTop__io_dataIn_0__prev)  goto L75;
K75:  if (PlasticineControllerTop__io_dataOut_0 != PlasticineControllerTop__io_dataOut_0__prev)  goto L76;
K76:  if (PlasticineControllerTop__io_addr != PlasticineControllerTop__io_addr__prev)  goto L77;
K77:  fprintf(f, "#%lu\n", (t << 1) + 1);
  if (clk.cnt == 0)  goto Z0;
C0:  return;
L0:
  clk.values[0] = 1;
  dat_dump<1>(f, clk, 0x21);
  goto K0;
L1:
  reset__prev = reset;
  dat_dump<1>(f, reset, 0x22);
  goto K1;
L2:
  PlasticineControllerTop_addrFifo__is_empty__prev = PlasticineControllerTop_addrFifo__is_empty;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_empty, 0x23);
  goto K2;
L3:
  PlasticineControllerTop_addrFifo__io_deq_val__prev = PlasticineControllerTop_addrFifo__io_deq_val;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_val, 0x24);
  goto K3;
L4:
  PlasticineControllerTop_controller__io_done__prev = PlasticineControllerTop_controller__io_done;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_done, 0x25);
  goto K4;
L5:
  PlasticineControllerTop_addrFifo__io_deq_rdy__prev = PlasticineControllerTop_addrFifo__io_deq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_rdy, 0x26);
  goto K5;
L6:
  PlasticineControllerTop_addrFifo__do_deq__prev = PlasticineControllerTop_addrFifo__do_deq;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__do_deq, 0x27);
  goto K6;
L7:
  PlasticineControllerTop__io_enq_val__prev = PlasticineControllerTop__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop__io_enq_val, 0x28);
  goto K7;
L8:
  PlasticineControllerTop_addrFifo__io_enq_val__prev = PlasticineControllerTop_addrFifo__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_val, 0x29);
  goto K8;
L9:
  PlasticineControllerTop_addrFifo__io_enq_rdy__prev = PlasticineControllerTop_addrFifo__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_rdy, 0x2a);
  goto K9;
L10:
  PlasticineControllerTop_addrFifo__do_enq__prev = PlasticineControllerTop_addrFifo__do_enq;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__do_enq, 0x2b);
  goto K10;
L11:
  PlasticineControllerTop_addrFifo__is_full_next__prev = PlasticineControllerTop_addrFifo__is_full_next;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_full_next, 0x2c);
  goto K11;
L12:
  PlasticineControllerTop_addrFifo__reset__prev = PlasticineControllerTop_addrFifo__reset;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__reset, 0x2d);
  goto K12;
L13:
  PlasticineControllerTop_addrFifo__is_full__prev = PlasticineControllerTop_addrFifo__is_full;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__is_full, 0x2e);
  goto K13;
L14:
  PlasticineControllerTop_isWrFifo__is_empty__prev = PlasticineControllerTop_isWrFifo__is_empty;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_empty, 0x2f);
  goto K14;
L15:
  PlasticineControllerTop_isWrFifo__io_deq_val__prev = PlasticineControllerTop_isWrFifo__io_deq_val;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_val, 0x30);
  goto K15;
L16:
  PlasticineControllerTop_isWrFifo__io_deq_rdy__prev = PlasticineControllerTop_isWrFifo__io_deq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_rdy, 0x31);
  goto K16;
L17:
  PlasticineControllerTop_isWrFifo__do_deq__prev = PlasticineControllerTop_isWrFifo__do_deq;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__do_deq, 0x32);
  goto K17;
L18:
  PlasticineControllerTop_isWrFifo__io_enq_val__prev = PlasticineControllerTop_isWrFifo__io_enq_val;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_val, 0x33);
  goto K18;
L19:
  PlasticineControllerTop_isWrFifo__io_enq_rdy__prev = PlasticineControllerTop_isWrFifo__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_rdy, 0x34);
  goto K19;
L20:
  PlasticineControllerTop_isWrFifo__do_enq__prev = PlasticineControllerTop_isWrFifo__do_enq;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__do_enq, 0x35);
  goto K20;
L21:
  PlasticineControllerTop_isWrFifo__is_full_next__prev = PlasticineControllerTop_isWrFifo__is_full_next;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_full_next, 0x36);
  goto K21;
L22:
  PlasticineControllerTop_isWrFifo__reset__prev = PlasticineControllerTop_isWrFifo__reset;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__reset, 0x37);
  goto K22;
L23:
  PlasticineControllerTop_isWrFifo__is_full__prev = PlasticineControllerTop_isWrFifo__is_full;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__is_full, 0x38);
  goto K23;
L24:
  PlasticineControllerTop__io_isWR__prev = PlasticineControllerTop__io_isWR;
  dat_dump<1>(f, PlasticineControllerTop__io_isWR, 0x39);
  goto K24;
L25:
  PlasticineControllerTop_isWrFifo__io_enq_dat__prev = PlasticineControllerTop_isWrFifo__io_enq_dat;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_enq_dat, 0x3a);
  goto K25;
L26:
  PlasticineControllerTop_isWrFifo__io_deq_dat__prev = PlasticineControllerTop_isWrFifo__io_deq_dat;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__io_deq_dat, 0x3b);
  goto K26;
L27:
  PlasticineControllerTop_controller__io_start__prev = PlasticineControllerTop_controller__io_start;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_start, 0x3c);
  goto K27;
L28:
  PlasticineControllerTop_controller__io_tx_enq__prev = PlasticineControllerTop_controller__io_tx_enq;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_tx_enq, 0x3d);
  goto K28;
L29:
  PlasticineControllerTop_dramsim__TX_ENQ__prev = PlasticineControllerTop_dramsim__TX_ENQ;
  dat_dump<1>(f, PlasticineControllerTop_dramsim__TX_ENQ, 0x3e);
  goto K29;
L30:
  PlasticineControllerTop_dramsim__TX_COMP__prev = PlasticineControllerTop_dramsim__TX_COMP;
  dat_dump<1>(f, PlasticineControllerTop_dramsim__TX_COMP, 0x3f);
  goto K30;
L31:
  PlasticineControllerTop_controller__io_tx_comp__prev = PlasticineControllerTop_controller__io_tx_comp;
  dat_dump<1>(f, PlasticineControllerTop_controller__io_tx_comp, 0x40);
  goto K31;
L32:
  PlasticineControllerTop_controller__reset__prev = PlasticineControllerTop_controller__reset;
  dat_dump<1>(f, PlasticineControllerTop_controller__reset, 0x41);
  goto K32;
L33:
  PlasticineControllerTop__io_enq_rdy__prev = PlasticineControllerTop__io_enq_rdy;
  dat_dump<1>(f, PlasticineControllerTop__io_enq_rdy, 0x42);
  goto K33;
L34:
  PlasticineControllerTop_addrFifo__enq_ptr_inc__prev = PlasticineControllerTop_addrFifo__enq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__enq_ptr_inc, 0x43);
  goto K34;
L35:
  PlasticineControllerTop_addrFifo__deq_ptr_inc__prev = PlasticineControllerTop_addrFifo__deq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__deq_ptr_inc, 0x44);
  goto K35;
L36:
  PlasticineControllerTop_addrFifo__deq_ptr__prev = PlasticineControllerTop_addrFifo__deq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__deq_ptr, 0x45);
  goto K36;
L37:
  PlasticineControllerTop_addrFifo__enq_ptr__prev = PlasticineControllerTop_addrFifo__enq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__enq_ptr, 0x46);
  goto K37;
L38:
  PlasticineControllerTop_isWrFifo__enq_ptr_inc__prev = PlasticineControllerTop_isWrFifo__enq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__enq_ptr_inc, 0x47);
  goto K38;
L39:
  PlasticineControllerTop_isWrFifo__deq_ptr_inc__prev = PlasticineControllerTop_isWrFifo__deq_ptr_inc;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__deq_ptr_inc, 0x48);
  goto K39;
L40:
  PlasticineControllerTop_isWrFifo__deq_ptr__prev = PlasticineControllerTop_isWrFifo__deq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__deq_ptr, 0x49);
  goto K40;
L41:
  PlasticineControllerTop_isWrFifo__enq_ptr__prev = PlasticineControllerTop_isWrFifo__enq_ptr;
  dat_dump<1>(f, PlasticineControllerTop_isWrFifo__enq_ptr, 0x4a);
  goto K41;
L42:
  PlasticineControllerTop_controller__state__prev = PlasticineControllerTop_controller__state;
  dat_dump<1>(f, PlasticineControllerTop_controller__state, 0x4b);
  goto K42;
L43:
  PlasticineControllerTop_addrFifo__io_enq_dat__prev = PlasticineControllerTop_addrFifo__io_enq_dat;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_enq_dat, 0x4c);
  goto K43;
L44:
  PlasticineControllerTop_addrFifo__io_deq_dat__prev = PlasticineControllerTop_addrFifo__io_deq_dat;
  dat_dump<1>(f, PlasticineControllerTop_addrFifo__io_deq_dat, 0x4d);
  goto K44;
L45:
  PlasticineControllerTop__io_dataIn_15__prev = PlasticineControllerTop__io_dataIn_15;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_15, 0x4e);
  goto K45;
L46:
  PlasticineControllerTop__io_dataOut_15__prev = PlasticineControllerTop__io_dataOut_15;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_15, 0x4f);
  goto K46;
L47:
  PlasticineControllerTop__io_dataIn_14__prev = PlasticineControllerTop__io_dataIn_14;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_14, 0x50);
  goto K47;
L48:
  PlasticineControllerTop__io_dataOut_14__prev = PlasticineControllerTop__io_dataOut_14;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_14, 0x51);
  goto K48;
L49:
  PlasticineControllerTop__io_dataIn_13__prev = PlasticineControllerTop__io_dataIn_13;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_13, 0x52);
  goto K49;
L50:
  PlasticineControllerTop__io_dataOut_13__prev = PlasticineControllerTop__io_dataOut_13;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_13, 0x53);
  goto K50;
L51:
  PlasticineControllerTop__io_dataIn_12__prev = PlasticineControllerTop__io_dataIn_12;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_12, 0x54);
  goto K51;
L52:
  PlasticineControllerTop__io_dataOut_12__prev = PlasticineControllerTop__io_dataOut_12;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_12, 0x55);
  goto K52;
L53:
  PlasticineControllerTop__io_dataIn_11__prev = PlasticineControllerTop__io_dataIn_11;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_11, 0x56);
  goto K53;
L54:
  PlasticineControllerTop__io_dataOut_11__prev = PlasticineControllerTop__io_dataOut_11;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_11, 0x57);
  goto K54;
L55:
  PlasticineControllerTop__io_dataIn_10__prev = PlasticineControllerTop__io_dataIn_10;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_10, 0x58);
  goto K55;
L56:
  PlasticineControllerTop__io_dataOut_10__prev = PlasticineControllerTop__io_dataOut_10;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_10, 0x59);
  goto K56;
L57:
  PlasticineControllerTop__io_dataIn_9__prev = PlasticineControllerTop__io_dataIn_9;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_9, 0x5a);
  goto K57;
L58:
  PlasticineControllerTop__io_dataOut_9__prev = PlasticineControllerTop__io_dataOut_9;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_9, 0x5b);
  goto K58;
L59:
  PlasticineControllerTop__io_dataIn_8__prev = PlasticineControllerTop__io_dataIn_8;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_8, 0x5c);
  goto K59;
L60:
  PlasticineControllerTop__io_dataOut_8__prev = PlasticineControllerTop__io_dataOut_8;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_8, 0x5d);
  goto K60;
L61:
  PlasticineControllerTop__io_dataIn_7__prev = PlasticineControllerTop__io_dataIn_7;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_7, 0x5e);
  goto K61;
L62:
  PlasticineControllerTop__io_dataOut_7__prev = PlasticineControllerTop__io_dataOut_7;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_7, 0x5f);
  goto K62;
L63:
  PlasticineControllerTop__io_dataIn_6__prev = PlasticineControllerTop__io_dataIn_6;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_6, 0x60);
  goto K63;
L64:
  PlasticineControllerTop__io_dataOut_6__prev = PlasticineControllerTop__io_dataOut_6;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_6, 0x61);
  goto K64;
L65:
  PlasticineControllerTop__io_dataIn_5__prev = PlasticineControllerTop__io_dataIn_5;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_5, 0x62);
  goto K65;
L66:
  PlasticineControllerTop__io_dataOut_5__prev = PlasticineControllerTop__io_dataOut_5;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_5, 0x63);
  goto K66;
L67:
  PlasticineControllerTop__io_dataIn_4__prev = PlasticineControllerTop__io_dataIn_4;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_4, 0x64);
  goto K67;
L68:
  PlasticineControllerTop__io_dataOut_4__prev = PlasticineControllerTop__io_dataOut_4;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_4, 0x65);
  goto K68;
L69:
  PlasticineControllerTop__io_dataIn_3__prev = PlasticineControllerTop__io_dataIn_3;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_3, 0x66);
  goto K69;
L70:
  PlasticineControllerTop__io_dataOut_3__prev = PlasticineControllerTop__io_dataOut_3;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_3, 0x67);
  goto K70;
L71:
  PlasticineControllerTop__io_dataIn_2__prev = PlasticineControllerTop__io_dataIn_2;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_2, 0x68);
  goto K71;
L72:
  PlasticineControllerTop__io_dataOut_2__prev = PlasticineControllerTop__io_dataOut_2;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_2, 0x69);
  goto K72;
L73:
  PlasticineControllerTop__io_dataIn_1__prev = PlasticineControllerTop__io_dataIn_1;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_1, 0x6a);
  goto K73;
L74:
  PlasticineControllerTop__io_dataOut_1__prev = PlasticineControllerTop__io_dataOut_1;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_1, 0x6b);
  goto K74;
L75:
  PlasticineControllerTop__io_dataIn_0__prev = PlasticineControllerTop__io_dataIn_0;
  dat_dump<1>(f, PlasticineControllerTop__io_dataIn_0, 0x6c);
  goto K75;
L76:
  PlasticineControllerTop__io_dataOut_0__prev = PlasticineControllerTop__io_dataOut_0;
  dat_dump<1>(f, PlasticineControllerTop__io_dataOut_0, 0x6d);
  goto K76;
L77:
  PlasticineControllerTop__io_addr__prev = PlasticineControllerTop__io_addr;
  dat_dump<1>(f, PlasticineControllerTop__io_addr, 0x6e);
  goto K77;
Z0:
  clk.values[0] = 0;
  dat_dump<1>(f, clk, 0x21);
  goto C0;
}




void PlasticineControllerTop_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  val_t T0;
  T0 = PlasticineControllerTop_addrFifo__enq_ptr.values[0] == PlasticineControllerTop_addrFifo__deq_ptr.values[0];
  val_t T1;
  { T1 = PlasticineControllerTop_addrFifo__is_full.values[0] ^ 0x1L;}
  { PlasticineControllerTop_addrFifo__is_empty.values[0] = T1 & T0;}
  val_t T2;
  { T2 = PlasticineControllerTop_addrFifo__is_empty.values[0] ^ 0x1L;}
  { PlasticineControllerTop_addrFifo__io_deq_val.values[0] = T2;}
  val_t T3;
  T3 = PlasticineControllerTop_controller__state.values[0] == 0x3L;
  { PlasticineControllerTop_controller__io_done.values[0] = T3;}
  { PlasticineControllerTop_addrFifo__io_deq_rdy.values[0] = PlasticineControllerTop_controller__io_done.values[0];}
  { PlasticineControllerTop_addrFifo__do_deq.values[0] = PlasticineControllerTop_addrFifo__io_deq_rdy.values[0] & PlasticineControllerTop_addrFifo__io_deq_val.values[0];}
  val_t T4;
  { T4 = PlasticineControllerTop_addrFifo__do_deq.values[0] & PlasticineControllerTop_addrFifo__is_full.values[0];}
  val_t T5;
  { T5 = TERNARY(T4, 0x0L, PlasticineControllerTop_addrFifo__is_full.values[0]);}
  { PlasticineControllerTop_addrFifo__enq_ptr_inc.values[0] = PlasticineControllerTop_addrFifo__enq_ptr.values[0]+0x1L;}
  PlasticineControllerTop_addrFifo__enq_ptr_inc.values[0] = PlasticineControllerTop_addrFifo__enq_ptr_inc.values[0] & 0x3L;
  val_t T6;
  T6 = PlasticineControllerTop_addrFifo__enq_ptr_inc.values[0] == PlasticineControllerTop_addrFifo__deq_ptr.values[0];
  val_t T7;
  { T7 = ~PlasticineControllerTop_addrFifo__do_deq.values[0];}
  T7 = T7 & 0x1L;
  { PlasticineControllerTop_addrFifo__io_enq_val.values[0] = PlasticineControllerTop__io_enq_val.values[0];}
  val_t T8;
  { T8 = PlasticineControllerTop_addrFifo__is_full.values[0] ^ 0x1L;}
  { PlasticineControllerTop_addrFifo__io_enq_rdy.values[0] = T8;}
  { PlasticineControllerTop_addrFifo__do_enq.values[0] = PlasticineControllerTop_addrFifo__io_enq_rdy.values[0] & PlasticineControllerTop_addrFifo__io_enq_val.values[0];}
  val_t T9;
  { T9 = PlasticineControllerTop_addrFifo__do_enq.values[0] & T7;}
  val_t T10;
  { T10 = T9 & T6;}
  { PlasticineControllerTop_addrFifo__is_full_next.values[0] = TERNARY(T10, 0x1L, T5);}
  { PlasticineControllerTop_addrFifo__reset.values[0] = reset.values[0];}
  { T11.values[0] = TERNARY(PlasticineControllerTop_addrFifo__reset.values[0], 0x0L, PlasticineControllerTop_addrFifo__is_full_next.values[0]);}
  { PlasticineControllerTop_addrFifo__deq_ptr_inc.values[0] = PlasticineControllerTop_addrFifo__deq_ptr.values[0]+0x1L;}
  PlasticineControllerTop_addrFifo__deq_ptr_inc.values[0] = PlasticineControllerTop_addrFifo__deq_ptr_inc.values[0] & 0x3L;
  val_t T12;
  { T12 = TERNARY_1(PlasticineControllerTop_addrFifo__do_deq.values[0], PlasticineControllerTop_addrFifo__deq_ptr_inc.values[0], PlasticineControllerTop_addrFifo__deq_ptr.values[0]);}
  { T13.values[0] = TERNARY(PlasticineControllerTop_addrFifo__reset.values[0], 0x0L, T12);}
  val_t T14;
  { T14 = TERNARY_1(PlasticineControllerTop_addrFifo__do_enq.values[0], PlasticineControllerTop_addrFifo__enq_ptr_inc.values[0], PlasticineControllerTop_addrFifo__enq_ptr.values[0]);}
  { T15.values[0] = TERNARY(PlasticineControllerTop_addrFifo__reset.values[0], 0x0L, T14);}
  val_t T16;
  { T16 = PlasticineControllerTop__io_addr.values[0];}
  T16 = T16 & 0xffffffffL;
  { PlasticineControllerTop_addrFifo__io_enq_dat.values[0] = T16;}
  { T17.values[0] = PlasticineControllerTop_addrFifo__io_enq_dat.values[0] | 0x0L << 32;}
  val_t T18;
  { T18 = PlasticineControllerTop_addrFifo__ram.get(PlasticineControllerTop_addrFifo__deq_ptr.values[0], 0);}
  val_t T19;
  { T19 = T18;}
  T19 = T19 & 0xffffffffL;
  { PlasticineControllerTop_addrFifo__io_deq_dat.values[0] = T19;}
  val_t T20;
  T20 = PlasticineControllerTop_isWrFifo__enq_ptr.values[0] == PlasticineControllerTop_isWrFifo__deq_ptr.values[0];
  val_t T21;
  { T21 = PlasticineControllerTop_isWrFifo__is_full.values[0] ^ 0x1L;}
  { PlasticineControllerTop_isWrFifo__is_empty.values[0] = T21 & T20;}
  val_t T22;
  { T22 = PlasticineControllerTop_isWrFifo__is_empty.values[0] ^ 0x1L;}
  { PlasticineControllerTop_isWrFifo__io_deq_val.values[0] = T22;}
  { PlasticineControllerTop_isWrFifo__io_deq_rdy.values[0] = PlasticineControllerTop_controller__io_done.values[0];}
  { PlasticineControllerTop_isWrFifo__do_deq.values[0] = PlasticineControllerTop_isWrFifo__io_deq_rdy.values[0] & PlasticineControllerTop_isWrFifo__io_deq_val.values[0];}
  val_t T23;
  { T23 = PlasticineControllerTop_isWrFifo__do_deq.values[0] & PlasticineControllerTop_isWrFifo__is_full.values[0];}
  val_t T24;
  { T24 = TERNARY(T23, 0x0L, PlasticineControllerTop_isWrFifo__is_full.values[0]);}
  { PlasticineControllerTop_isWrFifo__enq_ptr_inc.values[0] = PlasticineControllerTop_isWrFifo__enq_ptr.values[0]+0x1L;}
  PlasticineControllerTop_isWrFifo__enq_ptr_inc.values[0] = PlasticineControllerTop_isWrFifo__enq_ptr_inc.values[0] & 0x3L;
  val_t T25;
  T25 = PlasticineControllerTop_isWrFifo__enq_ptr_inc.values[0] == PlasticineControllerTop_isWrFifo__deq_ptr.values[0];
  val_t T26;
  { T26 = ~PlasticineControllerTop_isWrFifo__do_deq.values[0];}
  T26 = T26 & 0x1L;
  { PlasticineControllerTop_isWrFifo__io_enq_val.values[0] = PlasticineControllerTop__io_enq_val.values[0];}
  val_t T27;
  { T27 = PlasticineControllerTop_isWrFifo__is_full.values[0] ^ 0x1L;}
  { PlasticineControllerTop_isWrFifo__io_enq_rdy.values[0] = T27;}
  { PlasticineControllerTop_isWrFifo__do_enq.values[0] = PlasticineControllerTop_isWrFifo__io_enq_rdy.values[0] & PlasticineControllerTop_isWrFifo__io_enq_val.values[0];}
  val_t T28;
  { T28 = PlasticineControllerTop_isWrFifo__do_enq.values[0] & T26;}
  val_t T29;
  { T29 = T28 & T25;}
  { PlasticineControllerTop_isWrFifo__is_full_next.values[0] = TERNARY(T29, 0x1L, T24);}
  { PlasticineControllerTop_isWrFifo__reset.values[0] = reset.values[0];}
  { T30.values[0] = TERNARY(PlasticineControllerTop_isWrFifo__reset.values[0], 0x0L, PlasticineControllerTop_isWrFifo__is_full_next.values[0]);}
  { PlasticineControllerTop_isWrFifo__deq_ptr_inc.values[0] = PlasticineControllerTop_isWrFifo__deq_ptr.values[0]+0x1L;}
  PlasticineControllerTop_isWrFifo__deq_ptr_inc.values[0] = PlasticineControllerTop_isWrFifo__deq_ptr_inc.values[0] & 0x3L;
  val_t T31;
  { T31 = TERNARY_1(PlasticineControllerTop_isWrFifo__do_deq.values[0], PlasticineControllerTop_isWrFifo__deq_ptr_inc.values[0], PlasticineControllerTop_isWrFifo__deq_ptr.values[0]);}
  { T32.values[0] = TERNARY(PlasticineControllerTop_isWrFifo__reset.values[0], 0x0L, T31);}
  val_t T33;
  { T33 = TERNARY_1(PlasticineControllerTop_isWrFifo__do_enq.values[0], PlasticineControllerTop_isWrFifo__enq_ptr_inc.values[0], PlasticineControllerTop_isWrFifo__enq_ptr.values[0]);}
  { T34.values[0] = TERNARY(PlasticineControllerTop_isWrFifo__reset.values[0], 0x0L, T33);}
  { PlasticineControllerTop_isWrFifo__io_enq_dat.values[0] = PlasticineControllerTop__io_isWR.values[0];}
  { T35.values[0] = PlasticineControllerTop_isWrFifo__io_enq_dat.values[0] | 0x0L << 1;}
  val_t T36;
  { T36 = PlasticineControllerTop_isWrFifo__ram.get(PlasticineControllerTop_isWrFifo__deq_ptr.values[0], 0);}
  val_t T37;
  T37 = (T36 >> 0) & 1;
  { PlasticineControllerTop_isWrFifo__io_deq_dat.values[0] = T37;}
  val_t T38;
  { T38 = PlasticineControllerTop_isWrFifo__io_deq_val.values[0] & PlasticineControllerTop_addrFifo__io_deq_val.values[0];}
  { PlasticineControllerTop_controller__io_start.values[0] = T38;}
  val_t T39;
  T39 = PlasticineControllerTop_controller__state.values[0] == 0x0L;
  val_t T40;
  { T40 = T39 & PlasticineControllerTop_controller__io_start.values[0];}
  val_t T41;
  { T41 = TERNARY(T40, 0x1L, PlasticineControllerTop_controller__state.values[0]);}
  val_t T42;
  { T42 = PlasticineControllerTop_controller__io_start.values[0] ^ 0x1L;}
  val_t T43;
  { T43 = T39 & T42;}
  val_t T44;
  { T44 = TERNARY(T43, 0x0L, T41);}
  val_t T45;
  T45 = PlasticineControllerTop_controller__state.values[0] == 0x1L;
  val_t T46;
  { T46 = T39 ^ 0x1L;}
  val_t T47;
  { T47 = T46 & T45;}
  val_t T48;
  { T48 = TERNARY(T47, 0x2L, T44);}
  val_t T49;
  T49 = PlasticineControllerTop_controller__state.values[0] == 0x1L;
  { PlasticineControllerTop_controller__io_tx_enq.values[0] = T49;}
  { PlasticineControllerTop_dramsim__TX_ENQ.values[0] = PlasticineControllerTop_controller__io_tx_enq.values[0];}
//  { PlasticineControllerTop_dramsim__TX_COMP.values[0] = PlasticineControllerTop_dramsim__TX_ENQ.values[0];}
  { PlasticineControllerTop_controller__io_tx_comp.values[0] = PlasticineControllerTop_dramsim__TX_COMP.values[0];}
  val_t T50;
  T50 = PlasticineControllerTop_controller__state.values[0] == 0x2L;
  val_t T51;
  { T51 = T39 | T45;}
  val_t T52;
  { T52 = T51 ^ 0x1L;}
  val_t T53;
  { T53 = T52 & T50;}
  val_t T54;
  { T54 = T53 & PlasticineControllerTop_controller__io_tx_comp.values[0];}
  val_t T55;
  { T55 = TERNARY(T54, 0x3L, T48);}
  val_t T56;
  { T56 = PlasticineControllerTop_controller__io_tx_comp.values[0] ^ 0x1L;}
  val_t T57;
  { T57 = T53 & T56;}
  val_t T58;
  { T58 = TERNARY(T57, 0x2L, T55);}
  val_t T59;
  T59 = PlasticineControllerTop_controller__state.values[0] == 0x3L;
  val_t T60;
  { T60 = T51 | T50;}
  val_t T61;
  { T61 = T60 ^ 0x1L;}
  val_t T62;
  { T62 = T61 & T59;}
  val_t T63;
  { T63 = TERNARY(T62, 0x0L, T58);}
  { PlasticineControllerTop_controller__reset.values[0] = reset.values[0];}
  { T64.values[0] = TERNARY(PlasticineControllerTop_controller__reset.values[0], 0x0L, T63);}
  { val_t __r = this->__rand_val(); PlasticineControllerTop__io_enq_rdy.values[0] = __r;}
  PlasticineControllerTop__io_enq_rdy.values[0] = PlasticineControllerTop__io_enq_rdy.values[0] & 0x1L;
//  { PlasticineControllerTop__io_dataOut_15.values[0] = PlasticineControllerTop__io_dataIn_15.values[0];}
//  { PlasticineControllerTop__io_dataOut_14.values[0] = PlasticineControllerTop__io_dataIn_14.values[0];}
//  { PlasticineControllerTop__io_dataOut_13.values[0] = PlasticineControllerTop__io_dataIn_13.values[0];}
//  { PlasticineControllerTop__io_dataOut_12.values[0] = PlasticineControllerTop__io_dataIn_12.values[0];}
//  { PlasticineControllerTop__io_dataOut_11.values[0] = PlasticineControllerTop__io_dataIn_11.values[0];}
//  { PlasticineControllerTop__io_dataOut_10.values[0] = PlasticineControllerTop__io_dataIn_10.values[0];}
//  { PlasticineControllerTop__io_dataOut_9.values[0] = PlasticineControllerTop__io_dataIn_9.values[0];}
//  { PlasticineControllerTop__io_dataOut_8.values[0] = PlasticineControllerTop__io_dataIn_8.values[0];}
//  { PlasticineControllerTop__io_dataOut_7.values[0] = PlasticineControllerTop__io_dataIn_7.values[0];}
//  { PlasticineControllerTop__io_dataOut_6.values[0] = PlasticineControllerTop__io_dataIn_6.values[0];}
//  { PlasticineControllerTop__io_dataOut_5.values[0] = PlasticineControllerTop__io_dataIn_5.values[0];}
//  { PlasticineControllerTop__io_dataOut_4.values[0] = PlasticineControllerTop__io_dataIn_4.values[0];}
//  { PlasticineControllerTop__io_dataOut_3.values[0] = PlasticineControllerTop__io_dataIn_3.values[0];}
//  { PlasticineControllerTop__io_dataOut_2.values[0] = PlasticineControllerTop__io_dataIn_2.values[0];}
//  { PlasticineControllerTop__io_dataOut_1.values[0] = PlasticineControllerTop__io_dataIn_1.values[0];}
//  { PlasticineControllerTop__io_dataOut_0.values[0] = PlasticineControllerTop__io_dataIn_0.values[0];}
}


void PlasticineControllerTop_t::clock_hi ( dat_t<1> reset ) {
  dat_t<1> PlasticineControllerTop_addrFifo__is_full__shadow = T11;
  dat_t<2> PlasticineControllerTop_addrFifo__deq_ptr__shadow = T13;
  dat_t<2> PlasticineControllerTop_addrFifo__enq_ptr__shadow = T15;
  { if (PlasticineControllerTop_addrFifo__do_enq.values[0]) PlasticineControllerTop_addrFifo__ram.put(PlasticineControllerTop_addrFifo__enq_ptr.values[0], 0, T17.values[0]);}
  dat_t<1> PlasticineControllerTop_isWrFifo__is_full__shadow = T30;
  dat_t<2> PlasticineControllerTop_isWrFifo__deq_ptr__shadow = T32;
  dat_t<2> PlasticineControllerTop_isWrFifo__enq_ptr__shadow = T34;
  { if (PlasticineControllerTop_isWrFifo__do_enq.values[0]) PlasticineControllerTop_isWrFifo__ram.put(PlasticineControllerTop_isWrFifo__enq_ptr.values[0], 0, T35.values[0]);}
  dat_t<2> PlasticineControllerTop_controller__state__shadow = T64;
  PlasticineControllerTop_addrFifo__is_full = T11;
  PlasticineControllerTop_addrFifo__deq_ptr = T13;
  PlasticineControllerTop_addrFifo__enq_ptr = T15;
  PlasticineControllerTop_isWrFifo__is_full = T30;
  PlasticineControllerTop_isWrFifo__deq_ptr = T32;
  PlasticineControllerTop_isWrFifo__enq_ptr = T34;
  PlasticineControllerTop_controller__state = T64;
}


void PlasticineControllerTop_api_t::init_sim_data (  ) {
  sim_data.inputs.clear();
  sim_data.outputs.clear();
  sim_data.signals.clear();
  PlasticineControllerTop_t* mod = dynamic_cast<PlasticineControllerTop_t*>(module);
  assert(mod);
  sim_data.inputs.push_back(new dat_api<1>(&mod->PlasticineControllerTop__io_enq_val));
  sim_data.inputs.push_back(new dat_api<1>(&mod->PlasticineControllerTop__io_isWR));
  sim_data.inputs.push_back(new dat_api<64>(&mod->PlasticineControllerTop__io_addr));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_15));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_14));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_13));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_12));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_11));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_10));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_9));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_8));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_7));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_6));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_5));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_4));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_3));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_2));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_1));
  sim_data.inputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataIn_0));
  sim_data.outputs.push_back(new dat_api<1>(&mod->PlasticineControllerTop__io_enq_rdy));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_15));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_14));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_13));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_12));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_11));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_10));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_9));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_8));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_7));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_6));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_5));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_4));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_3));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_2));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_1));
  sim_data.outputs.push_back(new dat_api<32>(&mod->PlasticineControllerTop__io_dataOut_0));
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__is_empty));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.is_empty"] = 0;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__io_deq_val));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_deq_val"] = 1;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_controller__io_done));
  sim_data.signal_map["PlasticineControllerTop.controller.io_done"] = 2;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__io_deq_rdy));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_deq_rdy"] = 3;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__do_deq));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.do_deq"] = 4;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_addrFifo__enq_ptr_inc));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.enq_ptr_inc"] = 5;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__io_enq_val));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_enq_val"] = 6;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__io_enq_rdy));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_enq_rdy"] = 7;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__do_enq));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.do_enq"] = 8;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__is_full_next));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.is_full_next"] = 9;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__reset));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.reset"] = 10;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_addrFifo__is_full));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.is_full"] = 11;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_addrFifo__deq_ptr_inc));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.deq_ptr_inc"] = 12;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_addrFifo__deq_ptr));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.deq_ptr"] = 13;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_addrFifo__enq_ptr));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.enq_ptr"] = 14;
  sim_data.signals.push_back(new dat_api<32>(&mod->PlasticineControllerTop_addrFifo__io_enq_dat));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_enq_dat"] = 15;
  std::string PlasticineControllerTop_addrFifo__ram_path = "PlasticineControllerTop.addrFifo.ram";
  for (size_t i = 0 ; i < 4 ; i++) {
    sim_data.signals.push_back(new dat_api<64>(&mod->PlasticineControllerTop_addrFifo__ram.contents[i]));
    sim_data.signal_map[PlasticineControllerTop_addrFifo__ram_path+"["+itos(i,false)+"]"] = 16+i;
  }
  sim_data.signals.push_back(new dat_api<32>(&mod->PlasticineControllerTop_addrFifo__io_deq_dat));
  sim_data.signal_map["PlasticineControllerTop.addrFifo.io_deq_dat"] = 20;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__is_empty));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.is_empty"] = 21;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_deq_val));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_deq_val"] = 22;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_deq_rdy));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_deq_rdy"] = 23;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__do_deq));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.do_deq"] = 24;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_isWrFifo__enq_ptr_inc));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.enq_ptr_inc"] = 25;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_enq_val));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_enq_val"] = 26;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_enq_rdy));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_enq_rdy"] = 27;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__do_enq));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.do_enq"] = 28;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__is_full_next));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.is_full_next"] = 29;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__reset));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.reset"] = 30;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__is_full));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.is_full"] = 31;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_isWrFifo__deq_ptr_inc));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.deq_ptr_inc"] = 32;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_isWrFifo__deq_ptr));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.deq_ptr"] = 33;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_isWrFifo__enq_ptr));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.enq_ptr"] = 34;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_enq_dat));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_enq_dat"] = 35;
  std::string PlasticineControllerTop_isWrFifo__ram_path = "PlasticineControllerTop.isWrFifo.ram";
  for (size_t i = 0 ; i < 4 ; i++) {
    sim_data.signals.push_back(new dat_api<64>(&mod->PlasticineControllerTop_isWrFifo__ram.contents[i]));
    sim_data.signal_map[PlasticineControllerTop_isWrFifo__ram_path+"["+itos(i,false)+"]"] = 36+i;
  }
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_isWrFifo__io_deq_dat));
  sim_data.signal_map["PlasticineControllerTop.isWrFifo.io_deq_dat"] = 40;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_controller__io_start));
  sim_data.signal_map["PlasticineControllerTop.controller.io_start"] = 41;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_controller__io_tx_enq));
  sim_data.signal_map["PlasticineControllerTop.controller.io_tx_enq"] = 42;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_dramsim__TX_ENQ));
  sim_data.signal_map["PlasticineControllerTop.dramsim.TX_ENQ"] = 43;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_dramsim__TX_COMP));
  sim_data.signal_map["PlasticineControllerTop.dramsim.TX_COMP"] = 44;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_controller__io_tx_comp));
  sim_data.signal_map["PlasticineControllerTop.controller.io_tx_comp"] = 45;
  sim_data.signals.push_back(new dat_api<1>(&mod->PlasticineControllerTop_controller__reset));
  sim_data.signal_map["PlasticineControllerTop.controller.reset"] = 46;
  sim_data.signals.push_back(new dat_api<2>(&mod->PlasticineControllerTop_controller__state));
  sim_data.signal_map["PlasticineControllerTop.controller.state"] = 47;
  sim_data.clk_map["clk"] = new clk_api(&mod->clk);
}
