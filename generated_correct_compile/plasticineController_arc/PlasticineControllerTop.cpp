#include "PlasticineControllerTop.h"

void PlasticineControllerTop_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
  PlasticineControllerTop_controller__state.randomize(&__rand_seed);
  clk.len = 1;
  clk.cnt = 0;
  clk.values[0] = 0;
}


int PlasticineControllerTop_t::clock ( dat_t<1> reset ) {
  uint32_t min = ((uint32_t)1<<31)-1;
  if (clk.cnt < min) min = clk.cnt;
  clk.cnt-=min;
  if (clk.cnt == 0) clock_lo( reset );
  if (!reset.to_bool()) print( std::cerr );
  if (clk.cnt == 0) clock_hi( reset );
  if (clk.cnt == 0) clk.cnt = clk.len;
  return min;
}


void PlasticineControllerTop_t::print ( FILE* f ) {
}
void PlasticineControllerTop_t::print ( std::ostream& s ) {
}


void PlasticineControllerTop_t::dump_init ( FILE* f ) {
}


void PlasticineControllerTop_t::dump ( FILE* f, val_t t, dat_t<1> reset ) {
}




void PlasticineControllerTop_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  { PlasticineControllerTop_controller__io_start.values[0] = PlasticineControllerTop__io_start.values[0];}
  val_t T0;
  T0 = PlasticineControllerTop_controller__state.values[0] == 0x0L;
  val_t T1;
  { T1 = T0 & PlasticineControllerTop_controller__io_start.values[0];}
  val_t T2;
  { T2 = TERNARY(T1, 0x1L, PlasticineControllerTop_controller__state.values[0]);}
  val_t T3;
  { T3 = PlasticineControllerTop_controller__io_start.values[0] ^ 0x1L;}
  val_t T4;
  { T4 = T0 & T3;}
  val_t T5;
  { T5 = TERNARY(T4, 0x0L, T2);}
  val_t T6;
  T6 = PlasticineControllerTop_controller__state.values[0] == 0x1L;
  val_t T7;
  { T7 = T0 ^ 0x1L;}
  val_t T8;
  { T8 = T7 & T6;}
  val_t T9;
  { T9 = TERNARY(T8, 0x2L, T5);}
  val_t T10;
  T10 = PlasticineControllerTop_controller__state.values[0] == 0x1L;
  { PlasticineControllerTop_controller__io_tx_enq.values[0] = T10;}
  { PlasticineControllerTop_dramsim__TX_ENQ.values[0] = PlasticineControllerTop_controller__io_tx_enq.values[0];}
//  { PlasticineControllerTop_dramsim__TX_COMP.values[0] = PlasticineControllerTop_dramsim__TX_ENQ.values[0]; }
  { PlasticineControllerTop_dramsim__TX_COMP.values[0] = 0; }
//  { PlasticineControllerTop_dramsim__TX_COMP.values[0] = 1; }
  { PlasticineControllerTop_controller__io_tx_comp.values[0] = PlasticineControllerTop_dramsim__TX_COMP.values[0];}
  val_t T11;
  T11 = PlasticineControllerTop_controller__state.values[0] == 0x2L;
  val_t T12;
  { T12 = T0 | T6;}
  val_t T13;
  { T13 = T12 ^ 0x1L;}
  val_t T14;
  { T14 = T13 & T11;}
  val_t T15;
  { T15 = T14 & PlasticineControllerTop_controller__io_tx_comp.values[0];}
  val_t T16;
  { T16 = TERNARY(T15, 0x3L, T9);}
  val_t T17;
  { T17 = PlasticineControllerTop_controller__io_tx_comp.values[0] ^ 0x1L;}
  val_t T18;
  { T18 = T14 & T17;}
  val_t T19;
  { T19 = TERNARY(T18, 0x2L, T16);}
  val_t T20;
  T20 = PlasticineControllerTop_controller__state.values[0] == 0x3L;
  val_t T21;
  { T21 = T12 | T11;}
  val_t T22;
  { T22 = T21 ^ 0x1L;}
  val_t T23;
  { T23 = T22 & T20;}
  val_t T24;
  { T24 = TERNARY(T23, 0x0L, T19);}
  { PlasticineControllerTop_controller__reset.values[0] = reset.values[0];}
  { T25.values[0] = TERNARY(PlasticineControllerTop_controller__reset.values[0], 0x0L, T24);}
  val_t T26;
  T26 = PlasticineControllerTop_controller__state.values[0] == 0x3L;
  { PlasticineControllerTop_controller__io_done.values[0] = T26;}
  { PlasticineControllerTop__io_done.values[0] = PlasticineControllerTop_controller__io_done.values[0];}
}


void PlasticineControllerTop_t::clock_hi ( dat_t<1> reset ) {
  dat_t<2> PlasticineControllerTop_controller__state__shadow = T25;
  PlasticineControllerTop_controller__state = T25;
}
