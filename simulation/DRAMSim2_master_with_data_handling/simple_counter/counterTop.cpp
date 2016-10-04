#include "counterTop.h"

void counterTop_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
  counterTop__state.randomize(&__rand_seed);
  clk.len = 1;
  clk.cnt = 0;
  clk.values[0] = 0;
}


int counterTop_t::clock ( dat_t<1> reset ) {
  uint32_t min = ((uint32_t)1<<31)-1;
  if (clk.cnt < min) min = clk.cnt;
  clk.cnt-=min;
  if (clk.cnt == 0) clock_lo( reset );
  if (!reset.to_bool()) print( std::cerr );
  if (clk.cnt == 0) clock_hi( reset );
  if (clk.cnt == 0) clk.cnt = clk.len;
  return min;
}


void counterTop_t::print ( FILE* f ) {
}
void counterTop_t::print ( std::ostream& s ) {
}


void counterTop_t::dump_init ( FILE* f ) {
}


void counterTop_t::dump ( FILE* f, val_t t, dat_t<1> reset ) {
}




void counterTop_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  val_t T0;
  T0 = counterTop__state.values[0] == 0x0L;
  val_t T1;
  { T1 = T0 & counterTop__io_start.values[0];}
  val_t T2;
  { T2 = TERNARY(T1, 0x1L, counterTop__state.values[0]);}
  val_t T3;
  { T3 = counterTop__io_start.values[0] ^ 0x1L;}
  val_t T4;
  { T4 = T0 & T3;}
  val_t T5;
  { T5 = TERNARY(T4, 0x0L, T2);}
  val_t T6;
  T6 = counterTop__state.values[0] == 0x1L;
  val_t T7;
  { T7 = T0 ^ 0x1L;}
  val_t T8;
  { T8 = T7 & T6;}
  val_t T9;
  { T9 = TERNARY(T8, 0x2L, T5);}
  val_t T10;
  T10 = counterTop__state.values[0] == 0x2L;
  val_t T11;
  { T11 = T0 | T6;}
  val_t T12;
  { T12 = T11 ^ 0x1L;}
  val_t T13;
  { T13 = T12 & T10;}
  val_t T14;
  { T14 = TERNARY(T13, 0x3L, T9);}
  val_t T15;
  T15 = counterTop__state.values[0] == 0x3L;
  val_t T16;
  { T16 = T11 | T10;}
  val_t T17;
  { T17 = T16 ^ 0x1L;}
  val_t T18;
  { T18 = T17 & T15;}
  val_t T19;
  { T19 = TERNARY(T18, 0x0L, T14);}
  { T20.values[0] = TERNARY(reset.values[0], 0x0L, T19);}
  val_t T21;
  T21 = counterTop__state.values[0] == 0x3L;
  { counterTop__io_done.values[0] = T21;}
}


void counterTop_t::clock_hi ( dat_t<1> reset ) {
  dat_t<2> counterTop__state__shadow = T20;
  counterTop__state = T20;
}


void counterTop_api_t::init_sim_data (  ) {
  sim_data.inputs.clear();
  sim_data.outputs.clear();
  sim_data.signals.clear();
  counterTop_t* mod = dynamic_cast<counterTop_t*>(module);
  assert(mod);
  sim_data.inputs.push_back(new dat_api<1>(&mod->counterTop__io_start));
  sim_data.outputs.push_back(new dat_api<1>(&mod->counterTop__io_done));
  sim_data.signals.push_back(new dat_api<2>(&mod->counterTop__state));
  sim_data.signal_map["counterTop.state"] = 0;
  sim_data.clk_map["clk"] = new clk_api(&mod->clk);
}
