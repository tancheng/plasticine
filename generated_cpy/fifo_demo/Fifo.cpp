#include "Fifo.h"

void Fifo_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
  Fifo__is_full.randomize(&__rand_seed);
  Fifo__deq_ptr.randomize(&__rand_seed);
  Fifo__enq_ptr.randomize(&__rand_seed);
  Fifo__ram.randomize(&__rand_seed);
  clk.len = 1;
  clk.cnt = 0;
  clk.values[0] = 0;
}


int Fifo_t::clock ( dat_t<1> reset ) {
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


void Fifo_t::print ( FILE* f ) {
}
void Fifo_t::print ( std::ostream& s ) {
}


void Fifo_t::dump_init ( FILE* f ) {
  fputs("$timescale 1ps $end\n", f);
  fputs("$scope module Fifo $end\n", f);
  fputs("$var wire 1 \x21 clk $end\n", f);
  fputs("$var wire 1 \x22 reset $end\n", f);
  fputs("$var wire 1 \x23 is_empty $end\n", f);
  fputs("$var wire 1 \x24 io_deq_val $end\n", f);
  fputs("$var wire 1 \x25 io_deq_rdy $end\n", f);
  fputs("$var wire 1 \x26 do_deq $end\n", f);
  fputs("$var wire 1 \x27 io_enq_val $end\n", f);
  fputs("$var wire 1 \x28 io_enq_rdy $end\n", f);
  fputs("$var wire 1 \x29 do_enq $end\n", f);
  fputs("$var wire 1 \x2a is_full_next $end\n", f);
  fputs("$var wire 1 \x2b is_full $end\n", f);
  fputs("$var wire 2 \x2c enq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x2d deq_ptr_inc $end\n", f);
  fputs("$var wire 2 \x2e deq_ptr $end\n", f);
  fputs("$var wire 2 \x2f enq_ptr $end\n", f);
  fputs("$var wire 32 \x30 io_enq_dat $end\n", f);
  fputs("$var wire 32 \x31 io_deq_dat $end\n", f);
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
  dat_dump<1>(f, Fifo__is_empty, 0x23);
  Fifo__is_empty__prev = Fifo__is_empty;
  dat_dump<1>(f, Fifo__io_deq_val, 0x24);
  Fifo__io_deq_val__prev = Fifo__io_deq_val;
  dat_dump<1>(f, Fifo__io_deq_rdy, 0x25);
  Fifo__io_deq_rdy__prev = Fifo__io_deq_rdy;
  dat_dump<1>(f, Fifo__do_deq, 0x26);
  Fifo__do_deq__prev = Fifo__do_deq;
  dat_dump<1>(f, Fifo__io_enq_val, 0x27);
  Fifo__io_enq_val__prev = Fifo__io_enq_val;
  dat_dump<1>(f, Fifo__io_enq_rdy, 0x28);
  Fifo__io_enq_rdy__prev = Fifo__io_enq_rdy;
  dat_dump<1>(f, Fifo__do_enq, 0x29);
  Fifo__do_enq__prev = Fifo__do_enq;
  dat_dump<1>(f, Fifo__is_full_next, 0x2a);
  Fifo__is_full_next__prev = Fifo__is_full_next;
  dat_dump<1>(f, Fifo__is_full, 0x2b);
  Fifo__is_full__prev = Fifo__is_full;
  dat_dump<1>(f, Fifo__enq_ptr_inc, 0x2c);
  Fifo__enq_ptr_inc__prev = Fifo__enq_ptr_inc;
  dat_dump<1>(f, Fifo__deq_ptr_inc, 0x2d);
  Fifo__deq_ptr_inc__prev = Fifo__deq_ptr_inc;
  dat_dump<1>(f, Fifo__deq_ptr, 0x2e);
  Fifo__deq_ptr__prev = Fifo__deq_ptr;
  dat_dump<1>(f, Fifo__enq_ptr, 0x2f);
  Fifo__enq_ptr__prev = Fifo__enq_ptr;
  dat_dump<1>(f, Fifo__io_enq_dat, 0x30);
  Fifo__io_enq_dat__prev = Fifo__io_enq_dat;
  dat_dump<1>(f, Fifo__io_deq_dat, 0x31);
  Fifo__io_deq_dat__prev = Fifo__io_deq_dat;
  fputs("#1\n", f);
  if (clk.cnt == 0) {
    clk.values[0] = 0;
    dat_dump<1>(f, clk, 0x21);
  }
}


void Fifo_t::dump ( FILE* f, val_t t, dat_t<1> reset ) {
  if (t == 0L) return dump_init(f);
  fprintf(f, "#%lu\n", t << 1);
  if (clk.cnt == 0)  goto L0;
K0:  if (reset != reset__prev)  goto L1;
K1:  if (Fifo__is_empty != Fifo__is_empty__prev)  goto L2;
K2:  if (Fifo__io_deq_val != Fifo__io_deq_val__prev)  goto L3;
K3:  if (Fifo__io_deq_rdy != Fifo__io_deq_rdy__prev)  goto L4;
K4:  if (Fifo__do_deq != Fifo__do_deq__prev)  goto L5;
K5:  if (Fifo__io_enq_val != Fifo__io_enq_val__prev)  goto L6;
K6:  if (Fifo__io_enq_rdy != Fifo__io_enq_rdy__prev)  goto L7;
K7:  if (Fifo__do_enq != Fifo__do_enq__prev)  goto L8;
K8:  if (Fifo__is_full_next != Fifo__is_full_next__prev)  goto L9;
K9:  if (Fifo__is_full != Fifo__is_full__prev)  goto L10;
K10:  if (Fifo__enq_ptr_inc != Fifo__enq_ptr_inc__prev)  goto L11;
K11:  if (Fifo__deq_ptr_inc != Fifo__deq_ptr_inc__prev)  goto L12;
K12:  if (Fifo__deq_ptr != Fifo__deq_ptr__prev)  goto L13;
K13:  if (Fifo__enq_ptr != Fifo__enq_ptr__prev)  goto L14;
K14:  if (Fifo__io_enq_dat != Fifo__io_enq_dat__prev)  goto L15;
K15:  if (Fifo__io_deq_dat != Fifo__io_deq_dat__prev)  goto L16;
K16:  fprintf(f, "#%lu\n", (t << 1) + 1);
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
  Fifo__is_empty__prev = Fifo__is_empty;
  dat_dump<1>(f, Fifo__is_empty, 0x23);
  goto K2;
L3:
  Fifo__io_deq_val__prev = Fifo__io_deq_val;
  dat_dump<1>(f, Fifo__io_deq_val, 0x24);
  goto K3;
L4:
  Fifo__io_deq_rdy__prev = Fifo__io_deq_rdy;
  dat_dump<1>(f, Fifo__io_deq_rdy, 0x25);
  goto K4;
L5:
  Fifo__do_deq__prev = Fifo__do_deq;
  dat_dump<1>(f, Fifo__do_deq, 0x26);
  goto K5;
L6:
  Fifo__io_enq_val__prev = Fifo__io_enq_val;
  dat_dump<1>(f, Fifo__io_enq_val, 0x27);
  goto K6;
L7:
  Fifo__io_enq_rdy__prev = Fifo__io_enq_rdy;
  dat_dump<1>(f, Fifo__io_enq_rdy, 0x28);
  goto K7;
L8:
  Fifo__do_enq__prev = Fifo__do_enq;
  dat_dump<1>(f, Fifo__do_enq, 0x29);
  goto K8;
L9:
  Fifo__is_full_next__prev = Fifo__is_full_next;
  dat_dump<1>(f, Fifo__is_full_next, 0x2a);
  goto K9;
L10:
  Fifo__is_full__prev = Fifo__is_full;
  dat_dump<1>(f, Fifo__is_full, 0x2b);
  goto K10;
L11:
  Fifo__enq_ptr_inc__prev = Fifo__enq_ptr_inc;
  dat_dump<1>(f, Fifo__enq_ptr_inc, 0x2c);
  goto K11;
L12:
  Fifo__deq_ptr_inc__prev = Fifo__deq_ptr_inc;
  dat_dump<1>(f, Fifo__deq_ptr_inc, 0x2d);
  goto K12;
L13:
  Fifo__deq_ptr__prev = Fifo__deq_ptr;
  dat_dump<1>(f, Fifo__deq_ptr, 0x2e);
  goto K13;
L14:
  Fifo__enq_ptr__prev = Fifo__enq_ptr;
  dat_dump<1>(f, Fifo__enq_ptr, 0x2f);
  goto K14;
L15:
  Fifo__io_enq_dat__prev = Fifo__io_enq_dat;
  dat_dump<1>(f, Fifo__io_enq_dat, 0x30);
  goto K15;
L16:
  Fifo__io_deq_dat__prev = Fifo__io_deq_dat;
  dat_dump<1>(f, Fifo__io_deq_dat, 0x31);
  goto K16;
Z0:
  clk.values[0] = 0;
  dat_dump<1>(f, clk, 0x21);
  goto C0;
}




void Fifo_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  val_t T0;
  T0 = Fifo__enq_ptr.values[0] == Fifo__deq_ptr.values[0];
  val_t T1;
  { T1 = Fifo__is_full.values[0] ^ 0x1L;}
  { Fifo__is_empty.values[0] = T1 & T0;}
  val_t T2;
  { T2 = Fifo__is_empty.values[0] ^ 0x1L;}
  { Fifo__io_deq_val.values[0] = T2;}
  { Fifo__do_deq.values[0] = Fifo__io_deq_rdy.values[0] & Fifo__io_deq_val.values[0];}
  val_t T3;
  { T3 = Fifo__do_deq.values[0] & Fifo__is_full.values[0];}
  val_t T4;
  { T4 = TERNARY(T3, 0x0L, Fifo__is_full.values[0]);}
  { Fifo__enq_ptr_inc.values[0] = Fifo__enq_ptr.values[0]+0x1L;}
  Fifo__enq_ptr_inc.values[0] = Fifo__enq_ptr_inc.values[0] & 0x3L;
  val_t T5;
  T5 = Fifo__enq_ptr_inc.values[0] == Fifo__deq_ptr.values[0];
  val_t T6;
  { T6 = ~Fifo__do_deq.values[0];}
  T6 = T6 & 0x1L;
  val_t T7;
  { T7 = Fifo__is_full.values[0] ^ 0x1L;}
  { Fifo__io_enq_rdy.values[0] = T7;}
  { Fifo__do_enq.values[0] = Fifo__io_enq_rdy.values[0] & Fifo__io_enq_val.values[0];}
  val_t T8;
  { T8 = Fifo__do_enq.values[0] & T6;}
  val_t T9;
  { T9 = T8 & T5;}
  { Fifo__is_full_next.values[0] = TERNARY(T9, 0x1L, T4);}
  { T10.values[0] = TERNARY(reset.values[0], 0x0L, Fifo__is_full_next.values[0]);}
  { Fifo__deq_ptr_inc.values[0] = Fifo__deq_ptr.values[0]+0x1L;}
  Fifo__deq_ptr_inc.values[0] = Fifo__deq_ptr_inc.values[0] & 0x3L;
  val_t T11;
  { T11 = TERNARY_1(Fifo__do_deq.values[0], Fifo__deq_ptr_inc.values[0], Fifo__deq_ptr.values[0]);}
  { T12.values[0] = TERNARY(reset.values[0], 0x0L, T11);}
  val_t T13;
  { T13 = TERNARY_1(Fifo__do_enq.values[0], Fifo__enq_ptr_inc.values[0], Fifo__enq_ptr.values[0]);}
  { T14.values[0] = TERNARY(reset.values[0], 0x0L, T13);}
  { T15.values[0] = Fifo__io_enq_dat.values[0] | 0x0L << 32;}
  val_t T16;
  { T16 = Fifo__ram.get(Fifo__deq_ptr.values[0], 0);}
  val_t T17;
  { T17 = T16;}
  T17 = T17 & 0xffffffffL;
  { Fifo__io_deq_dat.values[0] = T17;}
}


void Fifo_t::clock_hi ( dat_t<1> reset ) {
  dat_t<1> Fifo__is_full__shadow = T10;
  dat_t<2> Fifo__deq_ptr__shadow = T12;
  dat_t<2> Fifo__enq_ptr__shadow = T14;
  { if (Fifo__do_enq.values[0]) Fifo__ram.put(Fifo__enq_ptr.values[0], 0, T15.values[0]);}
  Fifo__is_full = T10;
  Fifo__deq_ptr = T12;
  Fifo__enq_ptr = T14;
}


void Fifo_api_t::init_sim_data (  ) {
  sim_data.inputs.clear();
  sim_data.outputs.clear();
  sim_data.signals.clear();
  Fifo_t* mod = dynamic_cast<Fifo_t*>(module);
  assert(mod);
  sim_data.inputs.push_back(new dat_api<1>(&mod->Fifo__io_enq_val));
  sim_data.inputs.push_back(new dat_api<1>(&mod->Fifo__io_deq_rdy));
  sim_data.inputs.push_back(new dat_api<32>(&mod->Fifo__io_enq_dat));
  sim_data.outputs.push_back(new dat_api<1>(&mod->Fifo__io_enq_rdy));
  sim_data.outputs.push_back(new dat_api<1>(&mod->Fifo__io_deq_val));
  sim_data.outputs.push_back(new dat_api<32>(&mod->Fifo__io_deq_dat));
  sim_data.signals.push_back(new dat_api<1>(&mod->Fifo__is_empty));
  sim_data.signal_map["Fifo.is_empty"] = 0;
  sim_data.signals.push_back(new dat_api<1>(&mod->Fifo__do_deq));
  sim_data.signal_map["Fifo.do_deq"] = 1;
  sim_data.signals.push_back(new dat_api<2>(&mod->Fifo__enq_ptr_inc));
  sim_data.signal_map["Fifo.enq_ptr_inc"] = 2;
  sim_data.signals.push_back(new dat_api<1>(&mod->Fifo__do_enq));
  sim_data.signal_map["Fifo.do_enq"] = 3;
  sim_data.signals.push_back(new dat_api<1>(&mod->Fifo__is_full_next));
  sim_data.signal_map["Fifo.is_full_next"] = 4;
  sim_data.signals.push_back(new dat_api<1>(&mod->Fifo__is_full));
  sim_data.signal_map["Fifo.is_full"] = 5;
  sim_data.signals.push_back(new dat_api<2>(&mod->Fifo__deq_ptr_inc));
  sim_data.signal_map["Fifo.deq_ptr_inc"] = 6;
  sim_data.signals.push_back(new dat_api<2>(&mod->Fifo__deq_ptr));
  sim_data.signal_map["Fifo.deq_ptr"] = 7;
  sim_data.signals.push_back(new dat_api<2>(&mod->Fifo__enq_ptr));
  sim_data.signal_map["Fifo.enq_ptr"] = 8;
  std::string Fifo__ram_path = "Fifo.ram";
  for (size_t i = 0 ; i < 4 ; i++) {
    sim_data.signals.push_back(new dat_api<64>(&mod->Fifo__ram.contents[i]));
    sim_data.signal_map[Fifo__ram_path+"["+itos(i,false)+"]"] = 9+i;
  }
  sim_data.clk_map["clk"] = new clk_api(&mod->clk);
}
