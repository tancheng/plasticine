#include "MemoryUnit.h"

void MemoryUnit_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
   MemoryUnit_burstTagCounter__io_control_saturate.randomize(&__rand_seed);
  MemoryUnit_burstTagCounter_reg___ff.randomize(&__rand_seed);
  MemoryUnit_burstCounter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_dataFifo_FF__io_data_init.randomize(&__rand_seed);
  MemoryUnit_dataFifo_FF__ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_SRAM_1__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_dataFifo_SRAM_1__mem.randomize(&__rand_seed);
  MemoryUnit_dataFifo_SRAM__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_dataFifo_SRAM__mem.randomize(&__rand_seed);
   MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo_sizeUDC_reg___ff.randomize(&__rand_seed);
  MemoryUnit_dataFifo__config__chainRead.randomize(&__rand_seed);
  MemoryUnit_dataFifo__config__chainWrite.randomize(&__rand_seed);
   MemoryUnit_rwFifo_FF__io_data_init.randomize(&__rand_seed);
  MemoryUnit_rwFifo_FF__ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_SRAM_1__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_rwFifo_SRAM_1__mem.randomize(&__rand_seed);
  MemoryUnit_rwFifo_SRAM__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_rwFifo_SRAM__mem.randomize(&__rand_seed);
   MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo_sizeUDC_reg___ff.randomize(&__rand_seed);
  MemoryUnit_rwFifo__config__chainRead.randomize(&__rand_seed);
  MemoryUnit_rwFifo__config__chainWrite.randomize(&__rand_seed);
   MemoryUnit_sizeFifo_FF__io_data_init.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_FF__ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_SRAM_1__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_SRAM_1__mem.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_SRAM__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_SRAM__mem.randomize(&__rand_seed);
   MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo_sizeUDC_reg___ff.randomize(&__rand_seed);
  MemoryUnit_sizeFifo__config__chainRead.randomize(&__rand_seed);
  MemoryUnit_sizeFifo__config__chainWrite.randomize(&__rand_seed);
   MemoryUnit_addrFifo_FF__io_data_init.randomize(&__rand_seed);
  MemoryUnit_addrFifo_FF__ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_SRAM_1__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_addrFifo_SRAM_1__mem.randomize(&__rand_seed);
  MemoryUnit_addrFifo_SRAM__raddr_reg.randomize(&__rand_seed);
  MemoryUnit_addrFifo_SRAM__mem.randomize(&__rand_seed);
   MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff.randomize(&__rand_seed);
   MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.randomize(&__rand_seed);
  MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo_sizeUDC_reg___ff.randomize(&__rand_seed);
  MemoryUnit_addrFifo__config__chainRead.randomize(&__rand_seed);
  MemoryUnit_addrFifo__config__chainWrite.randomize(&__rand_seed);
  MemoryUnit__config__scatterGather.randomize(&__rand_seed);
  clk.len = 1;
  clk.cnt = 0;
  clk.values[0] = 0;
}


int MemoryUnit_t::clock ( dat_t<1> reset ) {
  uint32_t min = ((uint32_t)1<<31)-1;
  if (clk.cnt < min) min = clk.cnt;
  clk.cnt-=min;
  if (clk.cnt == 0) clock_lo( reset );
  if (!reset.to_bool()) print( std::cerr );
  if (clk.cnt == 0) clock_hi( reset );
  if (clk.cnt == 0) clk.cnt = clk.len;
  return min;
}


void MemoryUnit_t::print ( FILE* f ) {
}
void MemoryUnit_t::print ( std::ostream& s ) {
}


void MemoryUnit_t::dump_init ( FILE* f ) {
}


void MemoryUnit_t::dump ( FILE* f, val_t t, dat_t<1> reset ) {
}




void MemoryUnit_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  { MemoryUnit_burstTagCounter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_burstTagCounter__io_data_stride.values[0] = 0x1L;}
  val_t T0;
  { T0 = MemoryUnit_burstTagCounter__io_data_stride.values[0] | 0x0L << 5;}
  { MemoryUnit_burstTagCounter_reg___io_data_out.values[0] = MemoryUnit_burstTagCounter_reg___ff.values[0];}
  val_t MemoryUnit_burstTagCounter__count;
  { MemoryUnit_burstTagCounter__count = MemoryUnit_burstTagCounter_reg___io_data_out.values[0] | 0x0L << 5;}
  val_t MemoryUnit_burstTagCounter__newval;
  { MemoryUnit_burstTagCounter__newval = MemoryUnit_burstTagCounter__count+T0;}
  MemoryUnit_burstTagCounter__newval = MemoryUnit_burstTagCounter__newval & 0x3fL;
  { val_t __r = this->__rand_val(); MemoryUnit_burstTagCounter__io_control_saturate.values[0] = __r;}
  MemoryUnit_burstTagCounter__io_control_saturate.values[0] = MemoryUnit_burstTagCounter__io_control_saturate.values[0] & 0x1L;
  val_t T1;
  { T1 = TERNARY(MemoryUnit_burstTagCounter__io_control_saturate.values[0], MemoryUnit_burstTagCounter__count, 0x0L);}
  { MemoryUnit_burstTagCounter__io_data_max.values[0] = 0x10L;}
  val_t T2;
  { T2 = MemoryUnit_burstTagCounter__io_data_max.values[0] | 0x0L << 5;}
  val_t MemoryUnit_burstTagCounter__isMax;
  MemoryUnit_burstTagCounter__isMax = T2<=MemoryUnit_burstTagCounter__newval;
  val_t MemoryUnit_burstTagCounter__next;
  { MemoryUnit_burstTagCounter__next = TERNARY_1(MemoryUnit_burstTagCounter__isMax, T1, MemoryUnit_burstTagCounter__newval);}
  { MemoryUnit_burstTagCounter__io_control_reset.values[0] = 0x0L;}
  val_t T3;
  { T3 = TERNARY(MemoryUnit_burstTagCounter__io_control_reset.values[0], 0x0L, MemoryUnit_burstTagCounter__next);}
  val_t T4;
  { T4 = T3;}
  T4 = T4 & 0x1fL;
  { MemoryUnit_burstTagCounter_reg___io_data_in.values[0] = T4;}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC__io_out.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0];}
  val_t MemoryUnit_sizeFifo__empty;
  MemoryUnit_sizeFifo__empty = MemoryUnit_sizeFifo_sizeUDC__io_out.values[0] == 0x0L;
  { MemoryUnit_sizeFifo__io_empty.values[0] = MemoryUnit_sizeFifo__empty;}
  val_t MemoryUnit__burstVld;
  { MemoryUnit__burstVld = ~MemoryUnit_sizeFifo__io_empty.values[0];}
  MemoryUnit__burstVld = MemoryUnit__burstVld & 0x1L;
  { MemoryUnit_burstTagCounter__io_control_enable.values[0] = MemoryUnit__burstVld;}
  val_t T5;
  { T5 = MemoryUnit_burstTagCounter__io_control_reset.values[0] | MemoryUnit_burstTagCounter__io_control_enable.values[0];}
  { MemoryUnit_burstTagCounter_reg___io_control_enable.values[0] = T5;}
  val_t T6;
  { T6 = TERNARY_1(MemoryUnit_burstTagCounter_reg___io_control_enable.values[0], MemoryUnit_burstTagCounter_reg___io_data_in.values[0], MemoryUnit_burstTagCounter_reg___ff.values[0]);}
  { MemoryUnit_burstTagCounter__reset.values[0] = reset.values[0];}
  { MemoryUnit_burstTagCounter_reg___reset.values[0] = MemoryUnit_burstTagCounter__reset.values[0];}
  { T7.values[0] = TERNARY(MemoryUnit_burstTagCounter_reg___reset.values[0], MemoryUnit_burstTagCounter_reg___io_data_init.values[0], T6);}
  val_t T8;
  { T8 = MemoryUnit_burstTagCounter__next;}
  T8 = T8 & 0x1fL;
  { MemoryUnit_burstTagCounter__io_data_next.values[0] = T8;}
  val_t T9;
  { T9 = MemoryUnit_burstTagCounter__io_control_enable.values[0] & MemoryUnit_burstTagCounter__isMax;}
  { MemoryUnit_burstTagCounter__io_control_done.values[0] = T9;}
  { MemoryUnit_burstCounter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_burstCounter__io_data_stride.values[0] = 0x1L;}
  val_t T10;
  { T10 = MemoryUnit_burstCounter__io_data_stride.values[0] | 0x0L << 16;}
  { MemoryUnit_burstCounter_reg___io_data_out.values[0] = MemoryUnit_burstCounter_reg___ff.values[0];}
  val_t MemoryUnit_burstCounter__count;
  { MemoryUnit_burstCounter__count = MemoryUnit_burstCounter_reg___io_data_out.values[0] | 0x0L << 16;}
  val_t MemoryUnit_burstCounter__newval;
  { MemoryUnit_burstCounter__newval = MemoryUnit_burstCounter__count+T10;}
  MemoryUnit_burstCounter__newval = MemoryUnit_burstCounter__newval & 0x1ffffL;
  { MemoryUnit_burstCounter__io_control_saturate.values[0] = 0x0L;}
  val_t T11;
  { T11 = TERNARY(MemoryUnit_burstCounter__io_control_saturate.values[0], MemoryUnit_burstCounter__count, 0x0L);}
  val_t T12;
  { T12 = MemoryUnit_sizeFifo_SRAM__mem.get(MemoryUnit_sizeFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_sizeFifo_SRAM__io_rdata.values[0] = T12;}
  { MemoryUnit_sizeFifo_MuxN__io_ins_0.values[0] = MemoryUnit_sizeFifo_SRAM__io_rdata.values[0];}
  val_t T13;
  { T13 = MemoryUnit_sizeFifo_SRAM_1__mem.get(MemoryUnit_sizeFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0] = T13;}
  { MemoryUnit_sizeFifo_MuxN__io_ins_1.values[0] = MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_sizeFifo_FF__io_data_out.values[0] = MemoryUnit_sizeFifo_FF__ff.values[0];}
  { MemoryUnit_sizeFifo_MuxN__io_sel.values[0] = MemoryUnit_sizeFifo_FF__io_data_out.values[0];}
  val_t T14;
  { T14 = TERNARY_1(MemoryUnit_sizeFifo_MuxN__io_sel.values[0], MemoryUnit_sizeFifo_MuxN__io_ins_1.values[0], MemoryUnit_sizeFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_sizeFifo_MuxN__io_out.values[0] = T14;}
  { MemoryUnit_sizeFifo__io_deq_0.values[0] = MemoryUnit_sizeFifo_MuxN__io_out.values[0];}
  val_t T15;
  { T15 = MemoryUnit_sizeFifo__io_deq_0.values[0];}
  T15 = T15 & 0x3fL;
  val_t T16;
  T16 = T15 != 0x0L;
  val_t T17;
  { T17 = T16 | 0x0L << 1;}
  val_t T18;
  { T18 = MemoryUnit_sizeFifo__io_deq_0.values[0] >> 6;}
  T18 = T18 & 0x3ffL;
  val_t MemoryUnit__sizeInBursts;
  { MemoryUnit__sizeInBursts = T18+T17;}
  MemoryUnit__sizeInBursts = MemoryUnit__sizeInBursts & 0x3ffL;
  val_t T19;
  { T19 = TERNARY(MemoryUnit__config__scatterGather.values[0], 0x1L, MemoryUnit__sizeInBursts);}
  val_t T20;
  { T20 = T19 | 0x0L << 10;}
  { MemoryUnit_burstCounter__io_data_max.values[0] = T20;}
  val_t T21;
  { T21 = MemoryUnit_burstCounter__io_data_max.values[0] | 0x0L << 16;}
  val_t MemoryUnit_burstCounter__isMax;
  MemoryUnit_burstCounter__isMax = T21<=MemoryUnit_burstCounter__newval;
  val_t MemoryUnit_burstCounter__next;
  { MemoryUnit_burstCounter__next = TERNARY_1(MemoryUnit_burstCounter__isMax, T11, MemoryUnit_burstCounter__newval);}
  { MemoryUnit_burstCounter__io_control_reset.values[0] = 0x0L;}
  val_t T22;
  { T22 = TERNARY(MemoryUnit_burstCounter__io_control_reset.values[0], 0x0L, MemoryUnit_burstCounter__next);}
  val_t T23;
  { T23 = T22;}
  T23 = T23 & 0xffffL;
  { MemoryUnit_burstCounter_reg___io_data_in.values[0] = T23;}
  { MemoryUnit_burstCounter__io_control_enable.values[0] = MemoryUnit__burstVld;}
  val_t T24;
  { T24 = MemoryUnit_burstCounter__io_control_reset.values[0] | MemoryUnit_burstCounter__io_control_enable.values[0];}
  { MemoryUnit_burstCounter_reg___io_control_enable.values[0] = T24;}
  val_t T25;
  { T25 = TERNARY_1(MemoryUnit_burstCounter_reg___io_control_enable.values[0], MemoryUnit_burstCounter_reg___io_data_in.values[0], MemoryUnit_burstCounter_reg___ff.values[0]);}
  { MemoryUnit_burstCounter__reset.values[0] = reset.values[0];}
  { MemoryUnit_burstCounter_reg___reset.values[0] = MemoryUnit_burstCounter__reset.values[0];}
  { T26.values[0] = TERNARY(MemoryUnit_burstCounter_reg___reset.values[0], MemoryUnit_burstCounter_reg___io_data_init.values[0], T25);}
  val_t T27;
  { T27 = MemoryUnit_burstCounter__next;}
  T27 = T27 & 0xffffL;
  { MemoryUnit_burstCounter__io_data_next.values[0] = T27;}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_dataFifo_FF__io_data_init.values[0] = MemoryUnit_dataFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_counter__count;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__count = MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T28;
  { T28 = MemoryUnit_dataFifo_rptr_CounterRC_counter__count;}
  T28 = T28 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_out.values[0] = T28;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_0_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T29;
  { T29 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_counter__newval;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__newval = MemoryUnit_dataFifo_rptr_CounterRC_counter__count+T29;}
  MemoryUnit_dataFifo_rptr_CounterRC_counter__newval = MemoryUnit_dataFifo_rptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T30;
  { T30 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter__count, 0x0L);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T31;
  { T31 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax;
  MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax = T31<=MemoryUnit_dataFifo_rptr_CounterRC_counter__newval;
  val_t MemoryUnit_dataFifo_rptr_CounterRC_counter__next;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax, T30, MemoryUnit_dataFifo_rptr_CounterRC_counter__newval);}
  val_t T32;
  { T32 = MemoryUnit_dataFifo_rptr_CounterRC_counter__next;}
  T32 = T32 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_next.values[0] = T32;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_0_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_dataFifo_sizeUDC__io_out.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0];}
  val_t MemoryUnit_dataFifo__empty;
  MemoryUnit_dataFifo__empty = MemoryUnit_dataFifo_sizeUDC__io_out.values[0] == 0x0L;
  val_t T33;
  { T33 = ~MemoryUnit_dataFifo__empty;}
  T33 = T33 & 0x1L;
  val_t T34;
  { T34 = MemoryUnit_rwFifo_SRAM__mem.get(MemoryUnit_rwFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_rwFifo_SRAM__io_rdata.values[0] = T34;}
  { MemoryUnit_rwFifo_MuxN__io_ins_0.values[0] = MemoryUnit_rwFifo_SRAM__io_rdata.values[0];}
  val_t T35;
  { T35 = MemoryUnit_rwFifo_SRAM_1__mem.get(MemoryUnit_rwFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0] = T35;}
  { MemoryUnit_rwFifo_MuxN__io_ins_1.values[0] = MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_rwFifo_FF__io_data_out.values[0] = MemoryUnit_rwFifo_FF__ff.values[0];}
  { MemoryUnit_rwFifo_MuxN__io_sel.values[0] = MemoryUnit_rwFifo_FF__io_data_out.values[0];}
  val_t T36;
  { T36 = TERNARY_1(MemoryUnit_rwFifo_MuxN__io_sel.values[0], MemoryUnit_rwFifo_MuxN__io_ins_1.values[0], MemoryUnit_rwFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_rwFifo_MuxN__io_out.values[0] = T36;}
  { MemoryUnit_rwFifo__io_deq_0.values[0] = MemoryUnit_rwFifo_MuxN__io_out.values[0];}
  val_t T37;
  { T37 = MemoryUnit_rwFifo__io_deq_0.values[0] & MemoryUnit__burstVld;}
  val_t T38;
  { T38 = T37;}
  { MemoryUnit_dataFifo__io_deqVld.values[0] = T38;}
  val_t MemoryUnit_dataFifo__readEn;
  { MemoryUnit_dataFifo__readEn = MemoryUnit_dataFifo__io_deqVld.values[0] & T33;}
  val_t T39;
  { T39 = TERNARY_1(MemoryUnit_dataFifo__readEn, MemoryUnit_dataFifo_rptr__io_data_0_next.values[0], MemoryUnit_dataFifo_rptr__io_data_0_out.values[0]);}
  val_t T40;
  T40 = (T39 >> 0) & 1;
  { MemoryUnit_dataFifo_FF__io_data_in.values[0] = T40;}
  { MemoryUnit_dataFifo_FF__io_control_enable.values[0] = 0x1L;}
  val_t T41;
  { T41 = TERNARY(MemoryUnit_dataFifo_FF__io_control_enable.values[0], MemoryUnit_dataFifo_FF__io_data_in.values[0], MemoryUnit_dataFifo_FF__ff.values[0]);}
  { MemoryUnit_dataFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_dataFifo_FF__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { T42.values[0] = TERNARY_1(MemoryUnit_dataFifo_FF__reset.values[0], MemoryUnit_dataFifo_FF__io_data_init.values[0], T41);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count = MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T43;
  { T43 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count;}
  T43 = T43 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T43;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_1_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T44;
  { T44 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count+T44;}
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T45;
  { T45 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count, 0x0L);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T46;
  { T46 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax;
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax = T46<=MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval;
  val_t MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax, T45, MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval);}
  val_t T47;
  { T47 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next;}
  T47 = T47 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T47;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_1_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_dataFifo__readEn;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_enable.values[0];}
  val_t T48;
  { T48 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax;}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0] = T48;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_0_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0];}
  val_t T49;
  { T49 = TERNARY_1(MemoryUnit_dataFifo_rptr__io_control_0_done.values[0], MemoryUnit_dataFifo_rptr__io_data_1_next.values[0], MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}
  val_t MemoryUnit_dataFifo__nextHeadLocalAddr;
  { MemoryUnit_dataFifo__nextHeadLocalAddr = TERNARY_1(MemoryUnit_dataFifo__config__chainRead.values[0], T49, MemoryUnit_dataFifo_rptr__io_data_1_next.values[0]);}
  val_t T50;
  { T50 = TERNARY_1(MemoryUnit_dataFifo__readEn, MemoryUnit_dataFifo__nextHeadLocalAddr, MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}
  val_t T51;
  { T51 = T50;}
  T51 = T51 & 0x7L;
  { MemoryUnit_dataFifo_SRAM_1__io_raddr.values[0] = T51;}
  { MemoryUnit_dataFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_wdata_1.values[0];}
  { MemoryUnit_dataFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_wdata_0.values[0];}
  val_t T52;
  { T52 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], MemoryUnit_dataFifo__io_enq_0.values[0], MemoryUnit_dataFifo__io_enq_1.values[0]);}
  { MemoryUnit_dataFifo_SRAM_1__io_wdata.values[0] = T52;}
  { MemoryUnit_dataFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_counter__count;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__count = MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T53;
  { T53 = MemoryUnit_dataFifo_wptr_CounterRC_counter__count;}
  T53 = T53 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_out.values[0] = T53;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_data_out.values[0];}
  val_t T54;
  T54 = MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] == 0x1L;
  val_t T55;
  { T55 = MemoryUnit_dataFifo__io_enqVld.values[0] & T54;}
  val_t T56;
  { T56 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], T55, MemoryUnit_dataFifo__io_enqVld.values[0]);}
  { MemoryUnit_dataFifo_SRAM_1__io_wen.values[0] = T56;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count = MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T57;
  { T57 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count;}
  T57 = T57 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T57;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_1_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_out.values[0];}
  val_t T58;
  { T58 = MemoryUnit_dataFifo_wptr__io_data_1_out.values[0];}
  T58 = T58 & 0x7L;
  { MemoryUnit_dataFifo_SRAM_1__io_waddr.values[0] = T58;}
  val_t T59;
  { T59 = TERNARY_1(MemoryUnit_dataFifo__readEn, MemoryUnit_dataFifo__nextHeadLocalAddr, MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}
  val_t T60;
  { T60 = T59;}
  T60 = T60 & 0x7L;
  { MemoryUnit_dataFifo_SRAM__io_raddr.values[0] = T60;}
  { MemoryUnit_dataFifo_SRAM__io_wdata.values[0] = MemoryUnit_dataFifo__io_enq_0.values[0];}
  val_t T61;
  T61 = MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] == 0x0L;
  val_t T62;
  { T62 = MemoryUnit_dataFifo__io_enqVld.values[0] & T61;}
  val_t T63;
  { T63 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], T62, MemoryUnit_dataFifo__io_enqVld.values[0]);}
  { MemoryUnit_dataFifo_SRAM__io_wen.values[0] = T63;}
  val_t T64;
  { T64 = MemoryUnit_dataFifo_wptr__io_data_1_out.values[0];}
  T64 = T64 & 0x7L;
  { MemoryUnit_dataFifo_SRAM__io_waddr.values[0] = T64;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_enable.values[0];}
  val_t T65;
  { T65 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T65;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T66;
  { T66 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T66;}
  val_t T67;
  { T67 = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T67;}
  val_t T68;
  { T68 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_rptr__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_dataFifo_rptr__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T69.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], T68);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T70;
  { T70 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next);}
  val_t T71;
  { T71 = T70;}
  T71 = T71 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T71;}
  val_t T72;
  { T72 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T72;}
  val_t T73;
  { T73 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T74.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], T73);}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T75;
  { T75 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T75;}
  val_t T76;
  { T76 = MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T76;}
  val_t T77;
  { T77 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_dataFifo_rptr__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T78.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], T77);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T79;
  { T79 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_counter__next);}
  val_t T80;
  { T80 = T79;}
  T80 = T80 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T80;}
  val_t T81;
  { T81 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T81;}
  val_t T82;
  { T82 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__reset.values[0];}
  { T83.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], T82);}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T84;
  { T84 = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T84;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T85;
  { T85 = MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitOut.values[0] = T85;}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_1_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T86;
  { T86 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count+T86;}
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T87;
  { T87 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax;
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax = T87<=MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T88;
  { T88 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_counter__newval;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__newval = MemoryUnit_dataFifo_wptr_CounterRC_counter__count+T88;}
  MemoryUnit_dataFifo_wptr_CounterRC_counter__newval = MemoryUnit_dataFifo_wptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T89;
  { T89 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax;
  MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax = T89<=MemoryUnit_dataFifo_wptr_CounterRC_counter__newval;
  val_t T90;
  { T90 = TERNARY(MemoryUnit_dataFifo__config__chainWrite.values[0], 0x1L, 0x2L);}
  val_t T91;
  { T91 = T90 | 0x0L << 2;}
  { MemoryUnit_dataFifo_sizeUDC__io_strideInc.values[0] = T91;}
  val_t MemoryUnit_dataFifo_sizeUDC__incval;
  { MemoryUnit_dataFifo_sizeUDC__incval = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_dataFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_dataFifo_sizeUDC__incval = MemoryUnit_dataFifo_sizeUDC__incval & 0x1fL;
  { MemoryUnit_dataFifo_sizeUDC__io_max.values[0] = 0x10L;}
  val_t T92;
  T92 = MemoryUnit_dataFifo_sizeUDC__io_max.values[0]<MemoryUnit_dataFifo_sizeUDC__incval;
  { MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0] = T92;}
  val_t T93;
  { T93 = ~MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0];}
  T93 = T93 & 0x1L;
  val_t MemoryUnit_dataFifo__writeEn;
  { MemoryUnit_dataFifo__writeEn = MemoryUnit_dataFifo__io_enqVld.values[0] & T93;}
  { MemoryUnit_dataFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_dataFifo__writeEn;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_enable.values[0];}
  val_t T94;
  { T94 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax;}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0] = T94;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_enable.values[0];}
  val_t T95;
  { T95 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T95;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T96;
  { T96 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T96;}
  val_t T97;
  { T97 = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T97;}
  val_t T98;
  { T98 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_wptr__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_dataFifo_wptr__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T99.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], T98);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T100;
  { T100 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count, 0x0L);}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax, T100, MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T101;
  { T101 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next);}
  val_t T102;
  { T102 = T101;}
  T102 = T102 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T102;}
  val_t T103;
  { T103 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T103;}
  val_t T104;
  { T104 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T105.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], T104);}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T106;
  { T106 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T106;}
  val_t T107;
  { T107 = MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T107;}
  val_t T108;
  { T108 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_dataFifo_wptr__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T109.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], T108);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T110;
  { T110 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter__count, 0x0L);}
  val_t MemoryUnit_dataFifo_wptr_CounterRC_counter__next;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax, T110, MemoryUnit_dataFifo_wptr_CounterRC_counter__newval);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T111;
  { T111 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_counter__next);}
  val_t T112;
  { T112 = T111;}
  T112 = T112 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T112;}
  val_t T113;
  { T113 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T113;}
  val_t T114;
  { T114 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__reset.values[0];}
  { T115.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], T114);}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T116;
  { T116 = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T116;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T117;
  { T117 = MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitOut.values[0] = T117;}
  { val_t __r = this->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  val_t T118;
  { T118 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next;}
  T118 = T118 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T118;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_1_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_next.values[0];}
  val_t T119;
  { T119 = MemoryUnit_dataFifo_wptr_CounterRC_counter__next;}
  T119 = T119 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_next.values[0] = T119;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_0_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr__io_control_1_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr__io_control_0_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}
  val_t T120;
  { T120 = TERNARY(MemoryUnit_dataFifo__config__chainRead.values[0], 0x1L, 0x2L);}
  val_t T121;
  { T121 = T120 | 0x0L << 2;}
  { MemoryUnit_dataFifo_sizeUDC__io_strideDec.values[0] = T121;}
  val_t MemoryUnit_dataFifo_sizeUDC__decval;
  { MemoryUnit_dataFifo_sizeUDC__decval = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_dataFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_dataFifo_sizeUDC__decval = MemoryUnit_dataFifo_sizeUDC__decval & 0x1fL;
  { MemoryUnit_dataFifo_sizeUDC__io_inc.values[0] = MemoryUnit_dataFifo__writeEn;}
  val_t T122;
  { T122 = TERNARY_1(MemoryUnit_dataFifo_sizeUDC__io_inc.values[0], MemoryUnit_dataFifo_sizeUDC__incval, MemoryUnit_dataFifo_sizeUDC__decval);}
  { MemoryUnit_dataFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_sizeUDC__io_init.values[0] = 0x0L;}
  val_t T123;
  { T123 = TERNARY(MemoryUnit_dataFifo_sizeUDC__io_init.values[0], MemoryUnit_dataFifo_sizeUDC__io_initval.values[0], T122);}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_in.values[0] = T123;}
  { MemoryUnit_dataFifo_sizeUDC__io_dec.values[0] = MemoryUnit_dataFifo__readEn;}
  val_t T124;
  { T124 = MemoryUnit_dataFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_dataFifo_sizeUDC__io_dec.values[0];}
  val_t T125;
  { T125 = T124 | MemoryUnit_dataFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_control_enable.values[0] = T125;}
  val_t T126;
  { T126 = TERNARY_1(MemoryUnit_dataFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_dataFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_dataFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_sizeUDC__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_dataFifo_sizeUDC__reset.values[0];}
  { T127.values[0] = TERNARY(MemoryUnit_dataFifo_sizeUDC_reg___reset.values[0], MemoryUnit_dataFifo_sizeUDC_reg___io_data_init.values[0], T126);}
  val_t T128;
  T128 = 0x0L<MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_dataFifo_sizeUDC__io_gtz.values[0] = T128;}
  { MemoryUnit_dataFifo__io_empty.values[0] = MemoryUnit_dataFifo__empty;}
  { MemoryUnit_dataFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}
  val_t T129;
  { T129 = MemoryUnit_dataFifo__io_config_data.values[0] | 0x0L << 1;}
  val_t T130;
  { T130 = 0x0L-T129;}
  T130 = T130 & 0x3L;
  val_t T131;
  T131 = (T130 >> 0) & 1;
  { MemoryUnit_dataFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}
  val_t T132;
  { T132 = TERNARY_1(MemoryUnit_dataFifo__io_config_enable.values[0], T131, MemoryUnit_dataFifo__config__chainRead.values[0]);}
  { T133.values[0] = TERNARY(MemoryUnit_dataFifo__reset.values[0], 0x1L, T132);}
  val_t T134;
  T134 = (T130 >> 1) & 1;
  val_t T135;
  { T135 = TERNARY_1(MemoryUnit_dataFifo__io_config_enable.values[0], T134, MemoryUnit_dataFifo__config__chainWrite.values[0]);}
  { T136.values[0] = TERNARY(MemoryUnit_dataFifo__reset.values[0], 0x1L, T135);}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_rwFifo_FF__io_data_init.values[0] = MemoryUnit_rwFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_counter__count;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__count = MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T137;
  { T137 = MemoryUnit_rwFifo_rptr_CounterRC_counter__count;}
  T137 = T137 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_out.values[0] = T137;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_0_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T138;
  { T138 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_counter__newval;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__newval = MemoryUnit_rwFifo_rptr_CounterRC_counter__count+T138;}
  MemoryUnit_rwFifo_rptr_CounterRC_counter__newval = MemoryUnit_rwFifo_rptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T139;
  { T139 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter__count, 0x0L);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T140;
  { T140 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax;
  MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax = T140<=MemoryUnit_rwFifo_rptr_CounterRC_counter__newval;
  val_t MemoryUnit_rwFifo_rptr_CounterRC_counter__next;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax, T139, MemoryUnit_rwFifo_rptr_CounterRC_counter__newval);}
  val_t T141;
  { T141 = MemoryUnit_rwFifo_rptr_CounterRC_counter__next;}
  T141 = T141 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_next.values[0] = T141;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_0_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_rwFifo_sizeUDC__io_out.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0];}
  val_t MemoryUnit_rwFifo__empty;
  MemoryUnit_rwFifo__empty = MemoryUnit_rwFifo_sizeUDC__io_out.values[0] == 0x0L;
  val_t T142;
  { T142 = ~MemoryUnit_rwFifo__empty;}
  T142 = T142 & 0x1L;
  val_t T143;
  { T143 = MemoryUnit_burstCounter__io_control_enable.values[0] & MemoryUnit_burstCounter__isMax;}
  { MemoryUnit_burstCounter__io_control_done.values[0] = T143;}
  { MemoryUnit_rwFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  val_t MemoryUnit_rwFifo__readEn;
  { MemoryUnit_rwFifo__readEn = MemoryUnit_rwFifo__io_deqVld.values[0] & T142;}
  val_t T144;
  { T144 = TERNARY_1(MemoryUnit_rwFifo__readEn, MemoryUnit_rwFifo_rptr__io_data_0_next.values[0], MemoryUnit_rwFifo_rptr__io_data_0_out.values[0]);}
  val_t T145;
  T145 = (T144 >> 0) & 1;
  { MemoryUnit_rwFifo_FF__io_data_in.values[0] = T145;}
  { MemoryUnit_rwFifo_FF__io_control_enable.values[0] = 0x1L;}
  val_t T146;
  { T146 = TERNARY(MemoryUnit_rwFifo_FF__io_control_enable.values[0], MemoryUnit_rwFifo_FF__io_data_in.values[0], MemoryUnit_rwFifo_FF__ff.values[0]);}
  { MemoryUnit_rwFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_rwFifo_FF__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { T147.values[0] = TERNARY_1(MemoryUnit_rwFifo_FF__reset.values[0], MemoryUnit_rwFifo_FF__io_data_init.values[0], T146);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count = MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T148;
  { T148 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count;}
  T148 = T148 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T148;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_1_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T149;
  { T149 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count+T149;}
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T150;
  { T150 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count, 0x0L);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T151;
  { T151 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax;
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax = T151<=MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval;
  val_t MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax, T150, MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval);}
  val_t T152;
  { T152 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next;}
  T152 = T152 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T152;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_1_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_rwFifo__readEn;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_enable.values[0];}
  val_t T153;
  { T153 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax;}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0] = T153;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_0_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0];}
  val_t T154;
  { T154 = TERNARY_1(MemoryUnit_rwFifo_rptr__io_control_0_done.values[0], MemoryUnit_rwFifo_rptr__io_data_1_next.values[0], MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}
  val_t MemoryUnit_rwFifo__nextHeadLocalAddr;
  { MemoryUnit_rwFifo__nextHeadLocalAddr = TERNARY_1(MemoryUnit_rwFifo__config__chainRead.values[0], T154, MemoryUnit_rwFifo_rptr__io_data_1_next.values[0]);}
  val_t T155;
  { T155 = TERNARY_1(MemoryUnit_rwFifo__readEn, MemoryUnit_rwFifo__nextHeadLocalAddr, MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}
  val_t T156;
  { T156 = T155;}
  T156 = T156 & 0x7L;
  { MemoryUnit_rwFifo_SRAM_1__io_raddr.values[0] = T156;}
  { MemoryUnit_rwFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_isWr.values[0];}
  { MemoryUnit_rwFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_isWr.values[0];}
  val_t T157;
  { T157 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], MemoryUnit_rwFifo__io_enq_0.values[0], MemoryUnit_rwFifo__io_enq_1.values[0]);}
  { MemoryUnit_rwFifo_SRAM_1__io_wdata.values[0] = T157;}
  { MemoryUnit_rwFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_counter__count;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__count = MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T158;
  { T158 = MemoryUnit_rwFifo_wptr_CounterRC_counter__count;}
  T158 = T158 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_out.values[0] = T158;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_data_out.values[0];}
  val_t T159;
  T159 = MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] == 0x1L;
  val_t T160;
  { T160 = MemoryUnit_rwFifo__io_enqVld.values[0] & T159;}
  val_t T161;
  { T161 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], T160, MemoryUnit_rwFifo__io_enqVld.values[0]);}
  { MemoryUnit_rwFifo_SRAM_1__io_wen.values[0] = T161;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count = MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T162;
  { T162 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count;}
  T162 = T162 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T162;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_1_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_out.values[0];}
  val_t T163;
  { T163 = MemoryUnit_rwFifo_wptr__io_data_1_out.values[0];}
  T163 = T163 & 0x7L;
  { MemoryUnit_rwFifo_SRAM_1__io_waddr.values[0] = T163;}
  val_t T164;
  { T164 = TERNARY_1(MemoryUnit_rwFifo__readEn, MemoryUnit_rwFifo__nextHeadLocalAddr, MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}
  val_t T165;
  { T165 = T164;}
  T165 = T165 & 0x7L;
  { MemoryUnit_rwFifo_SRAM__io_raddr.values[0] = T165;}
  { MemoryUnit_rwFifo_SRAM__io_wdata.values[0] = MemoryUnit_rwFifo__io_enq_0.values[0];}
  val_t T166;
  T166 = MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] == 0x0L;
  val_t T167;
  { T167 = MemoryUnit_rwFifo__io_enqVld.values[0] & T166;}
  val_t T168;
  { T168 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], T167, MemoryUnit_rwFifo__io_enqVld.values[0]);}
  { MemoryUnit_rwFifo_SRAM__io_wen.values[0] = T168;}
  val_t T169;
  { T169 = MemoryUnit_rwFifo_wptr__io_data_1_out.values[0];}
  T169 = T169 & 0x7L;
  { MemoryUnit_rwFifo_SRAM__io_waddr.values[0] = T169;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_enable.values[0];}
  val_t T170;
  { T170 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T170;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T171;
  { T171 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T171;}
  val_t T172;
  { T172 = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T172;}
  val_t T173;
  { T173 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_rptr__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_rwFifo_rptr__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T174.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], T173);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T175;
  { T175 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next);}
  val_t T176;
  { T176 = T175;}
  T176 = T176 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T176;}
  val_t T177;
  { T177 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T177;}
  val_t T178;
  { T178 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T179.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], T178);}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T180;
  { T180 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T180;}
  val_t T181;
  { T181 = MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T181;}
  val_t T182;
  { T182 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_rwFifo_rptr__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T183.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], T182);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T184;
  { T184 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_counter__next);}
  val_t T185;
  { T185 = T184;}
  T185 = T185 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T185;}
  val_t T186;
  { T186 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T186;}
  val_t T187;
  { T187 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__reset.values[0];}
  { T188.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], T187);}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T189;
  { T189 = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T189;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T190;
  { T190 = MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitOut.values[0] = T190;}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_1_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T191;
  { T191 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count+T191;}
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T192;
  { T192 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax;
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax = T192<=MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T193;
  { T193 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_counter__newval;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__newval = MemoryUnit_rwFifo_wptr_CounterRC_counter__count+T193;}
  MemoryUnit_rwFifo_wptr_CounterRC_counter__newval = MemoryUnit_rwFifo_wptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T194;
  { T194 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax;
  MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax = T194<=MemoryUnit_rwFifo_wptr_CounterRC_counter__newval;
  val_t T195;
  { T195 = TERNARY(MemoryUnit_rwFifo__config__chainWrite.values[0], 0x1L, 0x2L);}
  val_t T196;
  { T196 = T195 | 0x0L << 2;}
  { MemoryUnit_rwFifo_sizeUDC__io_strideInc.values[0] = T196;}
  val_t MemoryUnit_rwFifo_sizeUDC__incval;
  { MemoryUnit_rwFifo_sizeUDC__incval = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_rwFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_rwFifo_sizeUDC__incval = MemoryUnit_rwFifo_sizeUDC__incval & 0x1fL;
  { MemoryUnit_rwFifo_sizeUDC__io_max.values[0] = 0x10L;}
  val_t T197;
  T197 = MemoryUnit_rwFifo_sizeUDC__io_max.values[0]<MemoryUnit_rwFifo_sizeUDC__incval;
  { MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0] = T197;}
  val_t T198;
  { T198 = ~MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0];}
  T198 = T198 & 0x1L;
  val_t MemoryUnit_rwFifo__writeEn;
  { MemoryUnit_rwFifo__writeEn = MemoryUnit_rwFifo__io_enqVld.values[0] & T198;}
  { MemoryUnit_rwFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_rwFifo__writeEn;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_enable.values[0];}
  val_t T199;
  { T199 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax;}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0] = T199;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_enable.values[0];}
  val_t T200;
  { T200 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T200;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T201;
  { T201 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T201;}
  val_t T202;
  { T202 = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T202;}
  val_t T203;
  { T203 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_wptr__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_rwFifo_wptr__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T204.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], T203);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T205;
  { T205 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count, 0x0L);}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax, T205, MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T206;
  { T206 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next);}
  val_t T207;
  { T207 = T206;}
  T207 = T207 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T207;}
  val_t T208;
  { T208 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T208;}
  val_t T209;
  { T209 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T210.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], T209);}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T211;
  { T211 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T211;}
  val_t T212;
  { T212 = MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T212;}
  val_t T213;
  { T213 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_rwFifo_wptr__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T214.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], T213);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T215;
  { T215 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter__count, 0x0L);}
  val_t MemoryUnit_rwFifo_wptr_CounterRC_counter__next;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax, T215, MemoryUnit_rwFifo_wptr_CounterRC_counter__newval);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T216;
  { T216 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_counter__next);}
  val_t T217;
  { T217 = T216;}
  T217 = T217 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T217;}
  val_t T218;
  { T218 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T218;}
  val_t T219;
  { T219 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__reset.values[0];}
  { T220.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], T219);}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T221;
  { T221 = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T221;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T222;
  { T222 = MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitOut.values[0] = T222;}
  { val_t __r = this->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  val_t T223;
  { T223 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next;}
  T223 = T223 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T223;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_1_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_next.values[0];}
  val_t T224;
  { T224 = MemoryUnit_rwFifo_wptr_CounterRC_counter__next;}
  T224 = T224 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_next.values[0] = T224;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_0_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr__io_control_1_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr__io_control_0_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}
  val_t T225;
  { T225 = TERNARY(MemoryUnit_rwFifo__config__chainRead.values[0], 0x1L, 0x2L);}
  val_t T226;
  { T226 = T225 | 0x0L << 2;}
  { MemoryUnit_rwFifo_sizeUDC__io_strideDec.values[0] = T226;}
  val_t MemoryUnit_rwFifo_sizeUDC__decval;
  { MemoryUnit_rwFifo_sizeUDC__decval = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_rwFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_rwFifo_sizeUDC__decval = MemoryUnit_rwFifo_sizeUDC__decval & 0x1fL;
  { MemoryUnit_rwFifo_sizeUDC__io_inc.values[0] = MemoryUnit_rwFifo__writeEn;}
  val_t T227;
  { T227 = TERNARY_1(MemoryUnit_rwFifo_sizeUDC__io_inc.values[0], MemoryUnit_rwFifo_sizeUDC__incval, MemoryUnit_rwFifo_sizeUDC__decval);}
  { MemoryUnit_rwFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_sizeUDC__io_init.values[0] = 0x0L;}
  val_t T228;
  { T228 = TERNARY(MemoryUnit_rwFifo_sizeUDC__io_init.values[0], MemoryUnit_rwFifo_sizeUDC__io_initval.values[0], T227);}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_in.values[0] = T228;}
  { MemoryUnit_rwFifo_sizeUDC__io_dec.values[0] = MemoryUnit_rwFifo__readEn;}
  val_t T229;
  { T229 = MemoryUnit_rwFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_rwFifo_sizeUDC__io_dec.values[0];}
  val_t T230;
  { T230 = T229 | MemoryUnit_rwFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_control_enable.values[0] = T230;}
  val_t T231;
  { T231 = TERNARY_1(MemoryUnit_rwFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_rwFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_rwFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_sizeUDC__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_rwFifo_sizeUDC__reset.values[0];}
  { T232.values[0] = TERNARY(MemoryUnit_rwFifo_sizeUDC_reg___reset.values[0], MemoryUnit_rwFifo_sizeUDC_reg___io_data_init.values[0], T231);}
  val_t T233;
  T233 = 0x0L<MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_rwFifo_sizeUDC__io_gtz.values[0] = T233;}
  { MemoryUnit_rwFifo__io_deq_1.values[0] = MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_rwFifo__io_full.values[0] = MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0];}
  { MemoryUnit_rwFifo__io_empty.values[0] = MemoryUnit_rwFifo__empty;}
  { MemoryUnit_rwFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}
  val_t T234;
  { T234 = MemoryUnit_rwFifo__io_config_data.values[0] | 0x0L << 1;}
  val_t T235;
  { T235 = 0x0L-T234;}
  T235 = T235 & 0x3L;
  val_t T236;
  T236 = (T235 >> 0) & 1;
  { MemoryUnit_rwFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}
  val_t T237;
  { T237 = TERNARY_1(MemoryUnit_rwFifo__io_config_enable.values[0], T236, MemoryUnit_rwFifo__config__chainRead.values[0]);}
  { T238.values[0] = TERNARY(MemoryUnit_rwFifo__reset.values[0], 0x1L, T237);}
  val_t T239;
  T239 = (T235 >> 1) & 1;
  val_t T240;
  { T240 = TERNARY_1(MemoryUnit_rwFifo__io_config_enable.values[0], T239, MemoryUnit_rwFifo__config__chainWrite.values[0]);}
  { T241.values[0] = TERNARY(MemoryUnit_rwFifo__reset.values[0], 0x1L, T240);}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_sizeFifo_FF__io_data_init.values[0] = MemoryUnit_sizeFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_counter__count;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__count = MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T242;
  { T242 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__count;}
  T242 = T242 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_out.values[0] = T242;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_0_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T243;
  { T243 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval = MemoryUnit_sizeFifo_rptr_CounterRC_counter__count+T243;}
  MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval = MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T244;
  { T244 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter__count, 0x0L);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T245;
  { T245 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax;
  MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax = T245<=MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval;
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_counter__next;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax, T244, MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval);}
  val_t T246;
  { T246 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__next;}
  T246 = T246 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_next.values[0] = T246;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_0_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_data_next.values[0];}
  val_t T247;
  { T247 = ~MemoryUnit_sizeFifo__empty;}
  T247 = T247 & 0x1L;
  { MemoryUnit_sizeFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  val_t MemoryUnit_sizeFifo__readEn;
  { MemoryUnit_sizeFifo__readEn = MemoryUnit_sizeFifo__io_deqVld.values[0] & T247;}
  val_t T248;
  { T248 = TERNARY_1(MemoryUnit_sizeFifo__readEn, MemoryUnit_sizeFifo_rptr__io_data_0_next.values[0], MemoryUnit_sizeFifo_rptr__io_data_0_out.values[0]);}
  val_t T249;
  T249 = (T248 >> 0) & 1;
  { MemoryUnit_sizeFifo_FF__io_data_in.values[0] = T249;}
  { MemoryUnit_sizeFifo_FF__io_control_enable.values[0] = 0x1L;}
  val_t T250;
  { T250 = TERNARY(MemoryUnit_sizeFifo_FF__io_control_enable.values[0], MemoryUnit_sizeFifo_FF__io_data_in.values[0], MemoryUnit_sizeFifo_FF__ff.values[0]);}
  { MemoryUnit_sizeFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_sizeFifo_FF__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { T251.values[0] = TERNARY_1(MemoryUnit_sizeFifo_FF__reset.values[0], MemoryUnit_sizeFifo_FF__io_data_init.values[0], T250);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T252;
  { T252 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count;}
  T252 = T252 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T252;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T253;
  { T253 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count+T253;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T254;
  { T254 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count, 0x0L);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T255;
  { T255 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax;
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax = T255<=MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval;
  val_t MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax, T254, MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval);}
  val_t T256;
  { T256 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next;}
  T256 = T256 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T256;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_sizeFifo__readEn;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_enable.values[0];}
  val_t T257;
  { T257 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0] = T257;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_0_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0];}
  val_t T258;
  { T258 = TERNARY_1(MemoryUnit_sizeFifo_rptr__io_control_0_done.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}
  val_t MemoryUnit_sizeFifo__nextHeadLocalAddr;
  { MemoryUnit_sizeFifo__nextHeadLocalAddr = TERNARY_1(MemoryUnit_sizeFifo__config__chainRead.values[0], T258, MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0]);}
  val_t T259;
  { T259 = TERNARY_1(MemoryUnit_sizeFifo__readEn, MemoryUnit_sizeFifo__nextHeadLocalAddr, MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}
  val_t T260;
  { T260 = T259;}
  T260 = T260 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM_1__io_raddr.values[0] = T260;}
  { MemoryUnit_sizeFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_size.values[0];}
  { MemoryUnit_sizeFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_size.values[0];}
  val_t T261;
  { T261 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], MemoryUnit_sizeFifo__io_enq_0.values[0], MemoryUnit_sizeFifo__io_enq_1.values[0]);}
  { MemoryUnit_sizeFifo_SRAM_1__io_wdata.values[0] = T261;}
  val_t T262;
  { T262 = ~MemoryUnit__config__scatterGather.values[0];}
  T262 = T262 & 0x1L;
  val_t T263;
  { T263 = MemoryUnit__io_interconnect_vldIn.values[0] & T262;}
  { MemoryUnit_sizeFifo__io_enqVld.values[0] = T263;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_counter__count;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__count = MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T264;
  { T264 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__count;}
  T264 = T264 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_out.values[0] = T264;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_data_out.values[0];}
  val_t T265;
  T265 = MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] == 0x1L;
  val_t T266;
  { T266 = MemoryUnit_sizeFifo__io_enqVld.values[0] & T265;}
  val_t T267;
  { T267 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], T266, MemoryUnit_sizeFifo__io_enqVld.values[0]);}
  { MemoryUnit_sizeFifo_SRAM_1__io_wen.values[0] = T267;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T268;
  { T268 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count;}
  T268 = T268 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T268;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_out.values[0];}
  val_t T269;
  { T269 = MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0];}
  T269 = T269 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM_1__io_waddr.values[0] = T269;}
  val_t T270;
  { T270 = TERNARY_1(MemoryUnit_sizeFifo__readEn, MemoryUnit_sizeFifo__nextHeadLocalAddr, MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}
  val_t T271;
  { T271 = T270;}
  T271 = T271 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM__io_raddr.values[0] = T271;}
  { MemoryUnit_sizeFifo_SRAM__io_wdata.values[0] = MemoryUnit_sizeFifo__io_enq_0.values[0];}
  val_t T272;
  T272 = MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] == 0x0L;
  val_t T273;
  { T273 = MemoryUnit_sizeFifo__io_enqVld.values[0] & T272;}
  val_t T274;
  { T274 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], T273, MemoryUnit_sizeFifo__io_enqVld.values[0]);}
  { MemoryUnit_sizeFifo_SRAM__io_wen.values[0] = T274;}
  val_t T275;
  { T275 = MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0];}
  T275 = T275 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM__io_waddr.values[0] = T275;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_enable.values[0];}
  val_t T276;
  { T276 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T276;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T277;
  { T277 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T277;}
  val_t T278;
  { T278 = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T278;}
  val_t T279;
  { T279 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_sizeFifo_rptr__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T280.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], T279);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T281;
  { T281 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next);}
  val_t T282;
  { T282 = T281;}
  T282 = T282 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T282;}
  val_t T283;
  { T283 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T283;}
  val_t T284;
  { T284 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T285.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], T284);}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T286;
  { T286 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T286;}
  val_t T287;
  { T287 = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T287;}
  val_t T288;
  { T288 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_sizeFifo_rptr__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T289.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], T288);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T290;
  { T290 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_counter__next);}
  val_t T291;
  { T291 = T290;}
  T291 = T291 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T291;}
  val_t T292;
  { T292 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T292;}
  val_t T293;
  { T293 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__reset.values[0];}
  { T294.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], T293);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T295;
  { T295 = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T295;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T296;
  { T296 = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitOut.values[0] = T296;}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_1_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T297;
  { T297 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count+T297;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T298;
  { T298 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax;
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax = T298<=MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T299;
  { T299 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval = MemoryUnit_sizeFifo_wptr_CounterRC_counter__count+T299;}
  MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval = MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T300;
  { T300 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax;
  MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax = T300<=MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval;
  val_t T301;
  { T301 = TERNARY(MemoryUnit_sizeFifo__config__chainWrite.values[0], 0x1L, 0x2L);}
  val_t T302;
  { T302 = T301 | 0x0L << 2;}
  { MemoryUnit_sizeFifo_sizeUDC__io_strideInc.values[0] = T302;}
  val_t MemoryUnit_sizeFifo_sizeUDC__incval;
  { MemoryUnit_sizeFifo_sizeUDC__incval = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_sizeFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_sizeFifo_sizeUDC__incval = MemoryUnit_sizeFifo_sizeUDC__incval & 0x1fL;
  { MemoryUnit_sizeFifo_sizeUDC__io_max.values[0] = 0x10L;}
  val_t T303;
  T303 = MemoryUnit_sizeFifo_sizeUDC__io_max.values[0]<MemoryUnit_sizeFifo_sizeUDC__incval;
  { MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0] = T303;}
  val_t T304;
  { T304 = ~MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0];}
  T304 = T304 & 0x1L;
  val_t MemoryUnit_sizeFifo__writeEn;
  { MemoryUnit_sizeFifo__writeEn = MemoryUnit_sizeFifo__io_enqVld.values[0] & T304;}
  { MemoryUnit_sizeFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_sizeFifo__writeEn;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_enable.values[0];}
  val_t T305;
  { T305 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0] = T305;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_enable.values[0];}
  val_t T306;
  { T306 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T306;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T307;
  { T307 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T307;}
  val_t T308;
  { T308 = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T308;}
  val_t T309;
  { T309 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_sizeFifo_wptr__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T310.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], T309);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T311;
  { T311 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count, 0x0L);}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax, T311, MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T312;
  { T312 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next);}
  val_t T313;
  { T313 = T312;}
  T313 = T313 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T313;}
  val_t T314;
  { T314 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T314;}
  val_t T315;
  { T315 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T316.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], T315);}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T317;
  { T317 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T317;}
  val_t T318;
  { T318 = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T318;}
  val_t T319;
  { T319 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_sizeFifo_wptr__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T320.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], T319);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T321;
  { T321 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter__count, 0x0L);}
  val_t MemoryUnit_sizeFifo_wptr_CounterRC_counter__next;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax, T321, MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T322;
  { T322 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_counter__next);}
  val_t T323;
  { T323 = T322;}
  T323 = T323 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T323;}
  val_t T324;
  { T324 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T324;}
  val_t T325;
  { T325 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__reset.values[0];}
  { T326.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], T325);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T327;
  { T327 = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T327;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T328;
  { T328 = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitOut.values[0] = T328;}
  { val_t __r = this->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  val_t T329;
  { T329 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next;}
  T329 = T329 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T329;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_1_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_next.values[0];}
  val_t T330;
  { T330 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__next;}
  T330 = T330 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_next.values[0] = T330;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_0_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_control_1_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_control_0_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}
  val_t T331;
  { T331 = TERNARY(MemoryUnit_sizeFifo__config__chainRead.values[0], 0x1L, 0x2L);}
  val_t T332;
  { T332 = T331 | 0x0L << 2;}
  { MemoryUnit_sizeFifo_sizeUDC__io_strideDec.values[0] = T332;}
  val_t MemoryUnit_sizeFifo_sizeUDC__decval;
  { MemoryUnit_sizeFifo_sizeUDC__decval = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_sizeFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_sizeFifo_sizeUDC__decval = MemoryUnit_sizeFifo_sizeUDC__decval & 0x1fL;
  { MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0] = MemoryUnit_sizeFifo__writeEn;}
  val_t T333;
  { T333 = TERNARY_1(MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0], MemoryUnit_sizeFifo_sizeUDC__incval, MemoryUnit_sizeFifo_sizeUDC__decval);}
  { MemoryUnit_sizeFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_sizeUDC__io_init.values[0] = 0x0L;}
  val_t T334;
  { T334 = TERNARY(MemoryUnit_sizeFifo_sizeUDC__io_init.values[0], MemoryUnit_sizeFifo_sizeUDC__io_initval.values[0], T333);}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_in.values[0] = T334;}
  { MemoryUnit_sizeFifo_sizeUDC__io_dec.values[0] = MemoryUnit_sizeFifo__readEn;}
  val_t T335;
  { T335 = MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_sizeFifo_sizeUDC__io_dec.values[0];}
  val_t T336;
  { T336 = T335 | MemoryUnit_sizeFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_control_enable.values[0] = T336;}
  val_t T337;
  { T337 = TERNARY_1(MemoryUnit_sizeFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_sizeUDC__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_sizeFifo_sizeUDC__reset.values[0];}
  { T338.values[0] = TERNARY(MemoryUnit_sizeFifo_sizeUDC_reg___reset.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___io_data_init.values[0], T337);}
  val_t T339;
  T339 = 0x0L<MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_sizeFifo_sizeUDC__io_gtz.values[0] = T339;}
  { MemoryUnit_sizeFifo__io_deq_1.values[0] = MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_sizeFifo__io_full.values[0] = MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0];}
  { MemoryUnit_sizeFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}
  val_t T340;
  { T340 = MemoryUnit_sizeFifo__io_config_data.values[0] | 0x0L << 1;}
  val_t T341;
  { T341 = 0x0L-T340;}
  T341 = T341 & 0x3L;
  val_t T342;
  T342 = (T341 >> 0) & 1;
  { MemoryUnit_sizeFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}
  val_t T343;
  { T343 = TERNARY_1(MemoryUnit_sizeFifo__io_config_enable.values[0], T342, MemoryUnit_sizeFifo__config__chainRead.values[0]);}
  { T344.values[0] = TERNARY(MemoryUnit_sizeFifo__reset.values[0], 0x1L, T343);}
  val_t T345;
  T345 = (T341 >> 1) & 1;
  val_t T346;
  { T346 = TERNARY_1(MemoryUnit_sizeFifo__io_config_enable.values[0], T345, MemoryUnit_sizeFifo__config__chainWrite.values[0]);}
  { T347.values[0] = TERNARY(MemoryUnit_sizeFifo__reset.values[0], 0x1L, T346);}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_addrFifo_FF__io_data_init.values[0] = MemoryUnit_addrFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_counter__count;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__count = MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T348;
  { T348 = MemoryUnit_addrFifo_rptr_CounterRC_counter__count;}
  T348 = T348 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_out.values[0] = T348;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_0_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T349;
  { T349 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_counter__newval;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__newval = MemoryUnit_addrFifo_rptr_CounterRC_counter__count+T349;}
  MemoryUnit_addrFifo_rptr_CounterRC_counter__newval = MemoryUnit_addrFifo_rptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T350;
  { T350 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter__count, 0x0L);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T351;
  { T351 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax;
  MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax = T351<=MemoryUnit_addrFifo_rptr_CounterRC_counter__newval;
  val_t MemoryUnit_addrFifo_rptr_CounterRC_counter__next;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax, T350, MemoryUnit_addrFifo_rptr_CounterRC_counter__newval);}
  val_t T352;
  { T352 = MemoryUnit_addrFifo_rptr_CounterRC_counter__next;}
  T352 = T352 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_next.values[0] = T352;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_0_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_addrFifo_sizeUDC__io_out.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0];}
  val_t MemoryUnit_addrFifo__empty;
  MemoryUnit_addrFifo__empty = MemoryUnit_addrFifo_sizeUDC__io_out.values[0] == 0x0L;
  val_t T353;
  { T353 = ~MemoryUnit_addrFifo__empty;}
  T353 = T353 & 0x1L;
  { MemoryUnit_addrFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  val_t MemoryUnit_addrFifo__readEn;
  { MemoryUnit_addrFifo__readEn = MemoryUnit_addrFifo__io_deqVld.values[0] & T353;}
  val_t T354;
  { T354 = TERNARY_1(MemoryUnit_addrFifo__readEn, MemoryUnit_addrFifo_rptr__io_data_0_next.values[0], MemoryUnit_addrFifo_rptr__io_data_0_out.values[0]);}
  val_t T355;
  T355 = (T354 >> 0) & 1;
  { MemoryUnit_addrFifo_FF__io_data_in.values[0] = T355;}
  { MemoryUnit_addrFifo_FF__io_control_enable.values[0] = 0x1L;}
  val_t T356;
  { T356 = TERNARY(MemoryUnit_addrFifo_FF__io_control_enable.values[0], MemoryUnit_addrFifo_FF__io_data_in.values[0], MemoryUnit_addrFifo_FF__ff.values[0]);}
  { MemoryUnit_addrFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_addrFifo_FF__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { T357.values[0] = TERNARY_1(MemoryUnit_addrFifo_FF__reset.values[0], MemoryUnit_addrFifo_FF__io_data_init.values[0], T356);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count = MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T358;
  { T358 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count;}
  T358 = T358 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T358;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_1_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T359;
  { T359 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count+T359;}
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T360;
  { T360 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count, 0x0L);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T361;
  { T361 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax;
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax = T361<=MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval;
  val_t MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax, T360, MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval);}
  val_t T362;
  { T362 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next;}
  T362 = T362 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T362;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_1_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_addrFifo__readEn;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_enable.values[0];}
  val_t T363;
  { T363 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax;}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0] = T363;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_0_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0];}
  val_t T364;
  { T364 = TERNARY_1(MemoryUnit_addrFifo_rptr__io_control_0_done.values[0], MemoryUnit_addrFifo_rptr__io_data_1_next.values[0], MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}
  val_t MemoryUnit_addrFifo__nextHeadLocalAddr;
  { MemoryUnit_addrFifo__nextHeadLocalAddr = TERNARY_1(MemoryUnit_addrFifo__config__chainRead.values[0], T364, MemoryUnit_addrFifo_rptr__io_data_1_next.values[0]);}
  val_t T365;
  { T365 = TERNARY_1(MemoryUnit_addrFifo__readEn, MemoryUnit_addrFifo__nextHeadLocalAddr, MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}
  val_t T366;
  { T366 = T365;}
  T366 = T366 & 0x7L;
  { MemoryUnit_addrFifo_SRAM_1__io_raddr.values[0] = T366;}
  { MemoryUnit_addrFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_addr_1.values[0];}
  { MemoryUnit_addrFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_addr_0.values[0];}
  val_t T367;
  { T367 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], MemoryUnit_addrFifo__io_enq_0.values[0], MemoryUnit_addrFifo__io_enq_1.values[0]);}
  { MemoryUnit_addrFifo_SRAM_1__io_wdata.values[0] = T367;}
  { MemoryUnit_addrFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_counter__count;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__count = MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T368;
  { T368 = MemoryUnit_addrFifo_wptr_CounterRC_counter__count;}
  T368 = T368 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_out.values[0] = T368;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_data_out.values[0];}
  val_t T369;
  T369 = MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] == 0x1L;
  val_t T370;
  { T370 = MemoryUnit_addrFifo__io_enqVld.values[0] & T369;}
  val_t T371;
  { T371 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], T370, MemoryUnit_addrFifo__io_enqVld.values[0]);}
  { MemoryUnit_addrFifo_SRAM_1__io_wen.values[0] = T371;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count = MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}
  val_t T372;
  { T372 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count;}
  T372 = T372 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T372;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_1_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_out.values[0];}
  val_t T373;
  { T373 = MemoryUnit_addrFifo_wptr__io_data_1_out.values[0];}
  T373 = T373 & 0x7L;
  { MemoryUnit_addrFifo_SRAM_1__io_waddr.values[0] = T373;}
  val_t T374;
  { T374 = TERNARY_1(MemoryUnit_addrFifo__readEn, MemoryUnit_addrFifo__nextHeadLocalAddr, MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}
  val_t T375;
  { T375 = T374;}
  T375 = T375 & 0x7L;
  { MemoryUnit_addrFifo_SRAM__io_raddr.values[0] = T375;}
  { MemoryUnit_addrFifo_SRAM__io_wdata.values[0] = MemoryUnit_addrFifo__io_enq_0.values[0];}
  val_t T376;
  T376 = MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] == 0x0L;
  val_t T377;
  { T377 = MemoryUnit_addrFifo__io_enqVld.values[0] & T376;}
  val_t T378;
  { T378 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], T377, MemoryUnit_addrFifo__io_enqVld.values[0]);}
  { MemoryUnit_addrFifo_SRAM__io_wen.values[0] = T378;}
  val_t T379;
  { T379 = MemoryUnit_addrFifo_wptr__io_data_1_out.values[0];}
  T379 = T379 & 0x7L;
  { MemoryUnit_addrFifo_SRAM__io_waddr.values[0] = T379;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_enable.values[0];}
  val_t T380;
  { T380 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T380;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T381;
  { T381 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T381;}
  val_t T382;
  { T382 = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T382;}
  val_t T383;
  { T383 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_rptr__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_addrFifo_rptr__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T384.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], T383);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T385;
  { T385 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next);}
  val_t T386;
  { T386 = T385;}
  T386 = T386 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T386;}
  val_t T387;
  { T387 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T387;}
  val_t T388;
  { T388 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T389.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], T388);}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T390;
  { T390 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T390;}
  val_t T391;
  { T391 = MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T391;}
  val_t T392;
  { T392 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_addrFifo_rptr__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T393.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], T392);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T394;
  { T394 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_counter__next);}
  val_t T395;
  { T395 = T394;}
  T395 = T395 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T395;}
  val_t T396;
  { T396 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T396;}
  val_t T397;
  { T397 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__reset.values[0];}
  { T398.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], T397);}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T399;
  { T399 = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T399;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T400;
  { T400 = MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitOut.values[0] = T400;}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_1_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}
  val_t T401;
  { T401 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count+T401;}
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval & 0x1fL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}
  val_t T402;
  { T402 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax;
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax = T402<=MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}
  val_t T403;
  { T403 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_counter__newval;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__newval = MemoryUnit_addrFifo_wptr_CounterRC_counter__count+T403;}
  MemoryUnit_addrFifo_wptr_CounterRC_counter__newval = MemoryUnit_addrFifo_wptr_CounterRC_counter__newval & 0x1fL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}
  val_t T404;
  { T404 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax;
  MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax = T404<=MemoryUnit_addrFifo_wptr_CounterRC_counter__newval;
  val_t T405;
  { T405 = TERNARY(MemoryUnit_addrFifo__config__chainWrite.values[0], 0x1L, 0x2L);}
  val_t T406;
  { T406 = T405 | 0x0L << 2;}
  { MemoryUnit_addrFifo_sizeUDC__io_strideInc.values[0] = T406;}
  val_t MemoryUnit_addrFifo_sizeUDC__incval;
  { MemoryUnit_addrFifo_sizeUDC__incval = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_addrFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_addrFifo_sizeUDC__incval = MemoryUnit_addrFifo_sizeUDC__incval & 0x1fL;
  { MemoryUnit_addrFifo_sizeUDC__io_max.values[0] = 0x10L;}
  val_t T407;
  T407 = MemoryUnit_addrFifo_sizeUDC__io_max.values[0]<MemoryUnit_addrFifo_sizeUDC__incval;
  { MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0] = T407;}
  val_t T408;
  { T408 = ~MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0];}
  T408 = T408 & 0x1L;
  val_t MemoryUnit_addrFifo__writeEn;
  { MemoryUnit_addrFifo__writeEn = MemoryUnit_addrFifo__io_enqVld.values[0] & T408;}
  { MemoryUnit_addrFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_addrFifo__writeEn;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_enable.values[0];}
  val_t T409;
  { T409 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax;}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0] = T409;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_enable.values[0];}
  val_t T410;
  { T410 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T410;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;
  val_t T411;
  { T411 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T411;}
  val_t T412;
  { T412 = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T412;}
  val_t T413;
  { T413 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_wptr__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_addrFifo_wptr__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T414.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], T413);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T415;
  { T415 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count, 0x0L);}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax, T415, MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}
  val_t T416;
  { T416 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next);}
  val_t T417;
  { T417 = T416;}
  T417 = T417 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T417;}
  val_t T418;
  { T418 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T418;}
  val_t T419;
  { T419 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T420.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], T419);}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;
  val_t T421;
  { T421 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T421;}
  val_t T422;
  { T422 = MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T422;}
  val_t T423;
  { T423 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_addrFifo_wptr__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T424.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], T423);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}
  val_t T425;
  { T425 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter__count, 0x0L);}
  val_t MemoryUnit_addrFifo_wptr_CounterRC_counter__next;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__next = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax, T425, MemoryUnit_addrFifo_wptr_CounterRC_counter__newval);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}
  val_t T426;
  { T426 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_counter__next);}
  val_t T427;
  { T427 = T426;}
  T427 = T427 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T427;}
  val_t T428;
  { T428 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T428;}
  val_t T429;
  { T429 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__reset.values[0];}
  { T430.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], T429);}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}
  val_t T431;
  { T431 = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T431;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}
  val_t T432;
  { T432 = MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitOut.values[0] = T432;}
  { val_t __r = this->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  val_t T433;
  { T433 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next;}
  T433 = T433 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T433;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_1_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_next.values[0];}
  val_t T434;
  { T434 = MemoryUnit_addrFifo_wptr_CounterRC_counter__next;}
  T434 = T434 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_next.values[0] = T434;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_0_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr__io_control_1_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr__io_control_0_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}
  val_t T435;
  { T435 = TERNARY(MemoryUnit_addrFifo__config__chainRead.values[0], 0x1L, 0x2L);}
  val_t T436;
  { T436 = T435 | 0x0L << 2;}
  { MemoryUnit_addrFifo_sizeUDC__io_strideDec.values[0] = T436;}
  val_t MemoryUnit_addrFifo_sizeUDC__decval;
  { MemoryUnit_addrFifo_sizeUDC__decval = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_addrFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_addrFifo_sizeUDC__decval = MemoryUnit_addrFifo_sizeUDC__decval & 0x1fL;
  { MemoryUnit_addrFifo_sizeUDC__io_inc.values[0] = MemoryUnit_addrFifo__writeEn;}
  val_t T437;
  { T437 = TERNARY_1(MemoryUnit_addrFifo_sizeUDC__io_inc.values[0], MemoryUnit_addrFifo_sizeUDC__incval, MemoryUnit_addrFifo_sizeUDC__decval);}
  { MemoryUnit_addrFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_sizeUDC__io_init.values[0] = 0x0L;}
  val_t T438;
  { T438 = TERNARY(MemoryUnit_addrFifo_sizeUDC__io_init.values[0], MemoryUnit_addrFifo_sizeUDC__io_initval.values[0], T437);}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_in.values[0] = T438;}
  { MemoryUnit_addrFifo_sizeUDC__io_dec.values[0] = MemoryUnit_addrFifo__readEn;}
  val_t T439;
  { T439 = MemoryUnit_addrFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_addrFifo_sizeUDC__io_dec.values[0];}
  val_t T440;
  { T440 = T439 | MemoryUnit_addrFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_control_enable.values[0] = T440;}
  val_t T441;
  { T441 = TERNARY_1(MemoryUnit_addrFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_addrFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_addrFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_sizeUDC__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_addrFifo_sizeUDC__reset.values[0];}
  { T442.values[0] = TERNARY(MemoryUnit_addrFifo_sizeUDC_reg___reset.values[0], MemoryUnit_addrFifo_sizeUDC_reg___io_data_init.values[0], T441);}
  val_t T443;
  T443 = 0x0L<MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_addrFifo_sizeUDC__io_gtz.values[0] = T443;}
  val_t T444;
  { T444 = MemoryUnit_addrFifo_SRAM_1__mem.get(MemoryUnit_addrFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0] = T444;}
  { MemoryUnit_addrFifo__io_deq_1.values[0] = MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_addrFifo__io_empty.values[0] = MemoryUnit_addrFifo__empty;}
  { MemoryUnit_addrFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}
  val_t T445;
  { T445 = MemoryUnit_addrFifo__io_config_data.values[0] | 0x0L << 1;}
  val_t T446;
  { T446 = 0x0L-T445;}
  T446 = T446 & 0x3L;
  val_t T447;
  T447 = (T446 >> 0) & 1;
  { MemoryUnit_addrFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}
  val_t T448;
  { T448 = TERNARY_1(MemoryUnit_addrFifo__io_config_enable.values[0], T447, MemoryUnit_addrFifo__config__chainRead.values[0]);}
  { T449.values[0] = TERNARY(MemoryUnit_addrFifo__reset.values[0], 0x1L, T448);}
  val_t T450;
  T450 = (T446 >> 1) & 1;
  val_t T451;
  { T451 = TERNARY_1(MemoryUnit_addrFifo__io_config_enable.values[0], T450, MemoryUnit_addrFifo__config__chainWrite.values[0]);}
  { T452.values[0] = TERNARY(MemoryUnit_addrFifo__reset.values[0], 0x1L, T451);}
  { MemoryUnit_dataFifo__io_full.values[0] = MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0];}
  val_t T453;
  { T453 = ~MemoryUnit_dataFifo__io_full.values[0];}
  T453 = T453 & 0x1L;
  { MemoryUnit_addrFifo__io_full.values[0] = MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0];}
  val_t T454;
  { T454 = ~MemoryUnit_addrFifo__io_full.values[0];}
  T454 = T454 & 0x1L;
  val_t T455;
  { T455 = T454 & T453;}
  { MemoryUnit__io_interconnect_rdyOut.values[0] = T455;}
  { MemoryUnit__io_interconnect_vldOut.values[0] = MemoryUnit__io_dram_vldIn.values[0];}
  { MemoryUnit__io_interconnect_rdata_1.values[0] = MemoryUnit__io_dram_rdata_1.values[0];}
  { MemoryUnit__io_interconnect_rdata_0.values[0] = MemoryUnit__io_dram_rdata_0.values[0];}
  { val_t __r = this->__rand_val(); MemoryUnit__io_dram_rdyOut.values[0] = __r;}
  MemoryUnit__io_dram_rdyOut.values[0] = MemoryUnit__io_dram_rdyOut.values[0] & 0x1L;
  { MemoryUnit__io_dram_vldOut.values[0] = MemoryUnit__burstVld;}
  val_t T456;
  { T456 = MemoryUnit_dataFifo_SRAM_1__mem.get(MemoryUnit_dataFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0] = T456;}
  { MemoryUnit_dataFifo__io_deq_1.values[0] = MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit__io_dram_wdata_1.values[0] = MemoryUnit_dataFifo__io_deq_1.values[0];}
  val_t T457;
  { T457 = MemoryUnit_dataFifo_SRAM__mem.get(MemoryUnit_dataFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_dataFifo_SRAM__io_rdata.values[0] = T457;}
  { MemoryUnit_dataFifo_MuxN__io_ins_0.values[0] = MemoryUnit_dataFifo_SRAM__io_rdata.values[0];}
  { MemoryUnit_dataFifo_MuxN__io_ins_1.values[0] = MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_dataFifo_FF__io_data_out.values[0] = MemoryUnit_dataFifo_FF__ff.values[0];}
  { MemoryUnit_dataFifo_MuxN__io_sel.values[0] = MemoryUnit_dataFifo_FF__io_data_out.values[0];}
  val_t T458;
  { T458 = TERNARY_1(MemoryUnit_dataFifo_MuxN__io_sel.values[0], MemoryUnit_dataFifo_MuxN__io_ins_1.values[0], MemoryUnit_dataFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_dataFifo_MuxN__io_out.values[0] = T458;}
  { MemoryUnit_dataFifo__io_deq_0.values[0] = MemoryUnit_dataFifo_MuxN__io_out.values[0];}
  { MemoryUnit__io_dram_wdata_0.values[0] = MemoryUnit_dataFifo__io_deq_0.values[0];}
  { MemoryUnit__io_dram_isWr.values[0] = MemoryUnit_rwFifo__io_deq_0.values[0];}
  val_t T459;
  { T459 = MemoryUnit_burstCounter__count;}
  T459 = T459 & 0xffffL;
  { MemoryUnit_burstCounter__io_data_out.values[0] = T459;}
  val_t T460;
  { T460 = MemoryUnit_addrFifo_SRAM__mem.get(MemoryUnit_addrFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_addrFifo_SRAM__io_rdata.values[0] = T460;}
  { MemoryUnit_addrFifo_MuxN__io_ins_0.values[0] = MemoryUnit_addrFifo_SRAM__io_rdata.values[0];}
  { MemoryUnit_addrFifo_MuxN__io_ins_1.values[0] = MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_addrFifo_FF__io_data_out.values[0] = MemoryUnit_addrFifo_FF__ff.values[0];}
  { MemoryUnit_addrFifo_MuxN__io_sel.values[0] = MemoryUnit_addrFifo_FF__io_data_out.values[0];}
  val_t T461;
  { T461 = TERNARY_1(MemoryUnit_addrFifo_MuxN__io_sel.values[0], MemoryUnit_addrFifo_MuxN__io_ins_1.values[0], MemoryUnit_addrFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_addrFifo_MuxN__io_out.values[0] = T461;}
  { MemoryUnit_addrFifo__io_deq_0.values[0] = MemoryUnit_addrFifo_MuxN__io_out.values[0];}
  val_t MemoryUnit__burstAddrs_0;
  { MemoryUnit__burstAddrs_0 = MemoryUnit_addrFifo__io_deq_0.values[0] >> 6;}
  MemoryUnit__burstAddrs_0 = MemoryUnit__burstAddrs_0 & 0x3ffL;
  val_t T462;
  { T462 = MemoryUnit__burstAddrs_0 | 0x0L << 10;}
  val_t T463;
  { T463 = T462+MemoryUnit_burstCounter__io_data_out.values[0];}
  T463 = T463 & 0xffffL;
  { MemoryUnit__io_dram_addr.values[0] = T463;}
  val_t T464;
  { T464 = MemoryUnit_burstTagCounter__count;}
  T464 = T464 & 0x1fL;
  { MemoryUnit_burstTagCounter__io_data_out.values[0] = T464;}
  val_t T465;
  { T465 = MemoryUnit_burstTagCounter__io_data_out.values[0] | 0x0L << 5;}
  val_t T466;
  { T466 = TERNARY_1(MemoryUnit__config__scatterGather.values[0], MemoryUnit__burstAddrs_0, T465);}
  val_t T467;
  { T467 = T466 | 0x0L << 10;}
  { MemoryUnit__io_dram_tagOut.values[0] = T467;}
  val_t T468;
  { T468 = TERNARY_1(MemoryUnit__io_config_enable.values[0], MemoryUnit__io_config_data.values[0], MemoryUnit__config__scatterGather.values[0]);}
  { T469.values[0] = TERNARY(reset.values[0], 0x0L, T468);}
}


void MemoryUnit_t::clock_hi ( dat_t<1> reset ) {
  dat_t<5> MemoryUnit_burstTagCounter_reg___ff__shadow = T7;
  dat_t<16> MemoryUnit_burstCounter_reg___ff__shadow = T26;
  dat_t<1> MemoryUnit_dataFifo_FF__ff__shadow = T42;
  dat_t<3> MemoryUnit_dataFifo_SRAM_1__raddr_reg__shadow = MemoryUnit_dataFifo_SRAM_1__io_raddr;
  { if (MemoryUnit_dataFifo_SRAM_1__io_wen.values[0]) MemoryUnit_dataFifo_SRAM_1__mem.put(MemoryUnit_dataFifo_SRAM_1__io_waddr.values[0], 0, MemoryUnit_dataFifo_SRAM_1__io_wdata.values[0]);}
  dat_t<3> MemoryUnit_dataFifo_SRAM__raddr_reg__shadow = MemoryUnit_dataFifo_SRAM__io_raddr;
  { if (MemoryUnit_dataFifo_SRAM__io_wen.values[0]) MemoryUnit_dataFifo_SRAM__mem.put(MemoryUnit_dataFifo_SRAM__io_waddr.values[0], 0, MemoryUnit_dataFifo_SRAM__io_wdata.values[0]);}
  dat_t<1> MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff__shadow = T69;
  dat_t<4> MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff__shadow = T74;
  dat_t<1> MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff__shadow = T78;
  dat_t<4> MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff__shadow = T83;
  dat_t<1> MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff__shadow = T99;
  dat_t<4> MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff__shadow = T105;
  dat_t<1> MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff__shadow = T109;
  dat_t<4> MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff__shadow = T115;
  dat_t<5> MemoryUnit_dataFifo_sizeUDC_reg___ff__shadow = T127;
  dat_t<1> MemoryUnit_dataFifo__config__chainRead__shadow = T133;
  dat_t<1> MemoryUnit_dataFifo__config__chainWrite__shadow = T136;
  dat_t<1> MemoryUnit_rwFifo_FF__ff__shadow = T147;
  dat_t<3> MemoryUnit_rwFifo_SRAM_1__raddr_reg__shadow = MemoryUnit_rwFifo_SRAM_1__io_raddr;
  { if (MemoryUnit_rwFifo_SRAM_1__io_wen.values[0]) MemoryUnit_rwFifo_SRAM_1__mem.put(MemoryUnit_rwFifo_SRAM_1__io_waddr.values[0], 0, MemoryUnit_rwFifo_SRAM_1__io_wdata.values[0]);}
  dat_t<3> MemoryUnit_rwFifo_SRAM__raddr_reg__shadow = MemoryUnit_rwFifo_SRAM__io_raddr;
  { if (MemoryUnit_rwFifo_SRAM__io_wen.values[0]) MemoryUnit_rwFifo_SRAM__mem.put(MemoryUnit_rwFifo_SRAM__io_waddr.values[0], 0, MemoryUnit_rwFifo_SRAM__io_wdata.values[0]);}
  dat_t<1> MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff__shadow = T174;
  dat_t<4> MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff__shadow = T179;
  dat_t<1> MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff__shadow = T183;
  dat_t<4> MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff__shadow = T188;
  dat_t<1> MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff__shadow = T204;
  dat_t<4> MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff__shadow = T210;
  dat_t<1> MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff__shadow = T214;
  dat_t<4> MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff__shadow = T220;
  dat_t<5> MemoryUnit_rwFifo_sizeUDC_reg___ff__shadow = T232;
  dat_t<1> MemoryUnit_rwFifo__config__chainRead__shadow = T238;
  dat_t<1> MemoryUnit_rwFifo__config__chainWrite__shadow = T241;
  dat_t<1> MemoryUnit_sizeFifo_FF__ff__shadow = T251;
  dat_t<3> MemoryUnit_sizeFifo_SRAM_1__raddr_reg__shadow = MemoryUnit_sizeFifo_SRAM_1__io_raddr;
  { if (MemoryUnit_sizeFifo_SRAM_1__io_wen.values[0]) MemoryUnit_sizeFifo_SRAM_1__mem.put(MemoryUnit_sizeFifo_SRAM_1__io_waddr.values[0], 0, MemoryUnit_sizeFifo_SRAM_1__io_wdata.values[0]);}
  dat_t<3> MemoryUnit_sizeFifo_SRAM__raddr_reg__shadow = MemoryUnit_sizeFifo_SRAM__io_raddr;
  { if (MemoryUnit_sizeFifo_SRAM__io_wen.values[0]) MemoryUnit_sizeFifo_SRAM__mem.put(MemoryUnit_sizeFifo_SRAM__io_waddr.values[0], 0, MemoryUnit_sizeFifo_SRAM__io_wdata.values[0]);}
  dat_t<1> MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff__shadow = T280;
  dat_t<4> MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff__shadow = T285;
  dat_t<1> MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff__shadow = T289;
  dat_t<4> MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff__shadow = T294;
  dat_t<1> MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff__shadow = T310;
  dat_t<4> MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff__shadow = T316;
  dat_t<1> MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff__shadow = T320;
  dat_t<4> MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff__shadow = T326;
  dat_t<5> MemoryUnit_sizeFifo_sizeUDC_reg___ff__shadow = T338;
  dat_t<1> MemoryUnit_sizeFifo__config__chainRead__shadow = T344;
  dat_t<1> MemoryUnit_sizeFifo__config__chainWrite__shadow = T347;
  dat_t<1> MemoryUnit_addrFifo_FF__ff__shadow = T357;
  dat_t<3> MemoryUnit_addrFifo_SRAM_1__raddr_reg__shadow = MemoryUnit_addrFifo_SRAM_1__io_raddr;
  { if (MemoryUnit_addrFifo_SRAM_1__io_wen.values[0]) MemoryUnit_addrFifo_SRAM_1__mem.put(MemoryUnit_addrFifo_SRAM_1__io_waddr.values[0], 0, MemoryUnit_addrFifo_SRAM_1__io_wdata.values[0]);}
  dat_t<3> MemoryUnit_addrFifo_SRAM__raddr_reg__shadow = MemoryUnit_addrFifo_SRAM__io_raddr;
  { if (MemoryUnit_addrFifo_SRAM__io_wen.values[0]) MemoryUnit_addrFifo_SRAM__mem.put(MemoryUnit_addrFifo_SRAM__io_waddr.values[0], 0, MemoryUnit_addrFifo_SRAM__io_wdata.values[0]);}
  dat_t<1> MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff__shadow = T384;
  dat_t<4> MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff__shadow = T389;
  dat_t<1> MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff__shadow = T393;
  dat_t<4> MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff__shadow = T398;
  dat_t<1> MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff__shadow = T414;
  dat_t<4> MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff__shadow = T420;
  dat_t<1> MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff__shadow = T424;
  dat_t<4> MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff__shadow = T430;
  dat_t<5> MemoryUnit_addrFifo_sizeUDC_reg___ff__shadow = T442;
  dat_t<1> MemoryUnit_addrFifo__config__chainRead__shadow = T449;
  dat_t<1> MemoryUnit_addrFifo__config__chainWrite__shadow = T452;
  dat_t<1> MemoryUnit__config__scatterGather__shadow = T469;
  MemoryUnit_burstTagCounter_reg___ff = T7;
  MemoryUnit_burstCounter_reg___ff = T26;
  MemoryUnit_dataFifo_FF__ff = T42;
  MemoryUnit_dataFifo_SRAM_1__raddr_reg = MemoryUnit_dataFifo_SRAM_1__io_raddr;
  MemoryUnit_dataFifo_SRAM__raddr_reg = MemoryUnit_dataFifo_SRAM__io_raddr;
  MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff = T69;
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff = T74;
  MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff = T78;
  MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff = T83;
  MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff = T99;
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff = T105;
  MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff = T109;
  MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff = T115;
  MemoryUnit_dataFifo_sizeUDC_reg___ff = T127;
  MemoryUnit_dataFifo__config__chainRead = T133;
  MemoryUnit_dataFifo__config__chainWrite = T136;
  MemoryUnit_rwFifo_FF__ff = T147;
  MemoryUnit_rwFifo_SRAM_1__raddr_reg = MemoryUnit_rwFifo_SRAM_1__io_raddr;
  MemoryUnit_rwFifo_SRAM__raddr_reg = MemoryUnit_rwFifo_SRAM__io_raddr;
  MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff = T174;
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff = T179;
  MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff = T183;
  MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff = T188;
  MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff = T204;
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff = T210;
  MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff = T214;
  MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff = T220;
  MemoryUnit_rwFifo_sizeUDC_reg___ff = T232;
  MemoryUnit_rwFifo__config__chainRead = T238;
  MemoryUnit_rwFifo__config__chainWrite = T241;
  MemoryUnit_sizeFifo_FF__ff = T251;
  MemoryUnit_sizeFifo_SRAM_1__raddr_reg = MemoryUnit_sizeFifo_SRAM_1__io_raddr;
  MemoryUnit_sizeFifo_SRAM__raddr_reg = MemoryUnit_sizeFifo_SRAM__io_raddr;
  MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff = T280;
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff = T285;
  MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff = T289;
  MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff = T294;
  MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff = T310;
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff = T316;
  MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff = T320;
  MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff = T326;
  MemoryUnit_sizeFifo_sizeUDC_reg___ff = T338;
  MemoryUnit_sizeFifo__config__chainRead = T344;
  MemoryUnit_sizeFifo__config__chainWrite = T347;
  MemoryUnit_addrFifo_FF__ff = T357;
  MemoryUnit_addrFifo_SRAM_1__raddr_reg = MemoryUnit_addrFifo_SRAM_1__io_raddr;
  MemoryUnit_addrFifo_SRAM__raddr_reg = MemoryUnit_addrFifo_SRAM__io_raddr;
  MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff = T384;
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff = T389;
  MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff = T393;
  MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff = T398;
  MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff = T414;
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff = T420;
  MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff = T424;
  MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff = T430;
  MemoryUnit_addrFifo_sizeUDC_reg___ff = T442;
  MemoryUnit_addrFifo__config__chainRead = T449;
  MemoryUnit_addrFifo__config__chainWrite = T452;
  MemoryUnit__config__scatterGather = T469;
}


void MemoryUnit_api_t::init_sim_data (  ) {
  sim_data.inputs.clear();
  sim_data.outputs.clear();
  sim_data.signals.clear();
  MemoryUnit_t* mod = dynamic_cast<MemoryUnit_t*>(module);
  assert(mod);
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_config_data));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_config_enable));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_interconnect_rdyIn));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_interconnect_vldIn));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_wdata_1));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_wdata_0));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_interconnect_isWr));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_addr_1));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_addr_0));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_size));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_dram_rdyIn));
  sim_data.inputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_dram_vldIn));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_rdata_1));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_rdata_0));
  sim_data.inputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_tagIn));
  sim_data.outputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_interconnect_rdyOut));
  sim_data.outputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_interconnect_vldOut));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_rdata_1));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_interconnect_rdata_0));
  sim_data.outputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_dram_rdyOut));
  sim_data.outputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_dram_vldOut));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_wdata_1));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_wdata_0));
  sim_data.outputs.push_back(new dat_api<1>(&mod->MemoryUnit__io_dram_isWr));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_addr));
  sim_data.outputs.push_back(new dat_api<16>(&mod->MemoryUnit__io_dram_tagOut));
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.io_data_init"] = 0;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter__io_data_stride));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_data_stride"] = 1;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.io_data_out"] = 2;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter__io_data_max));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_data_max"] = 3;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter__io_control_reset));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_control_reset"] = 4;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.io_data_in"] = 5;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.io_data_out"] = 6;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_out"] = 7;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_empty));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_empty"] = 8;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter__io_control_enable));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_control_enable"] = 9;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.io_control_enable"] = 10;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter__reset));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reset"] = 11;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter_reg___reset));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.reset"] = 12;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter_reg___ff));
  sim_data.signal_map["MemoryUnit.burstTagCounter.reg_.ff"] = 13;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter__io_data_next));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_data_next"] = 14;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstTagCounter__io_control_done));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_control_done"] = 15;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.io_data_init"] = 16;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter__io_data_stride));
  sim_data.signal_map["MemoryUnit.burstCounter.io_data_stride"] = 17;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.io_data_out"] = 18;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.burstCounter.io_control_saturate"] = 19;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM__io_rdata));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.io_rdata"] = 20;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_MuxN__io_ins_0));
  sim_data.signal_map["MemoryUnit.sizeFifo.MuxN.io_ins_0"] = 21;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM_1__io_rdata));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.io_rdata"] = 22;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_MuxN__io_ins_1));
  sim_data.signal_map["MemoryUnit.sizeFifo.MuxN.io_ins_1"] = 23;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_FF__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.FF.io_data_out"] = 24;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_MuxN__io_sel));
  sim_data.signal_map["MemoryUnit.sizeFifo.MuxN.io_sel"] = 25;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_MuxN__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.MuxN.io_out"] = 26;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo__io_deq_0));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_deq_0"] = 27;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter__io_data_max));
  sim_data.signal_map["MemoryUnit.burstCounter.io_data_max"] = 28;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter__io_control_reset));
  sim_data.signal_map["MemoryUnit.burstCounter.io_control_reset"] = 29;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.io_data_in"] = 30;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter__io_control_enable));
  sim_data.signal_map["MemoryUnit.burstCounter.io_control_enable"] = 31;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.io_control_enable"] = 32;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter__reset));
  sim_data.signal_map["MemoryUnit.burstCounter.reset"] = 33;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter_reg___reset));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.reset"] = 34;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter_reg___ff));
  sim_data.signal_map["MemoryUnit.burstCounter.reg_.ff"] = 35;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter__io_data_next));
  sim_data.signal_map["MemoryUnit.burstCounter.io_data_next"] = 36;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.io_data_out"] = 37;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_data_out"] = 38;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_data_out"] = 39;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_data_0_out"] = 40;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_data_stride"] = 41;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_control_saturate"] = 42;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_data_max"] = 43;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_data_next"] = 44;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_data_next"] = 45;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_data_0_next"] = 46;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.io_data_out"] = 47;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_out"] = 48;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM__io_rdata));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.io_rdata"] = 49;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_MuxN__io_ins_0));
  sim_data.signal_map["MemoryUnit.rwFifo.MuxN.io_ins_0"] = 50;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM_1__io_rdata));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.io_rdata"] = 51;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_MuxN__io_ins_1));
  sim_data.signal_map["MemoryUnit.rwFifo.MuxN.io_ins_1"] = 52;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_FF__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.FF.io_data_out"] = 53;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_MuxN__io_sel));
  sim_data.signal_map["MemoryUnit.rwFifo.MuxN.io_sel"] = 54;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_MuxN__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.MuxN.io_out"] = 55;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_deq_0));
  sim_data.signal_map["MemoryUnit.rwFifo.io_deq_0"] = 56;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_deqVld));
  sim_data.signal_map["MemoryUnit.dataFifo.io_deqVld"] = 57;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_FF__io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.FF.io_data_in"] = 58;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_FF__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.FF.io_control_enable"] = 59;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.reset"] = 60;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_FF__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.FF.reset"] = 61;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_FF__ff));
  sim_data.signal_map["MemoryUnit.dataFifo.FF.ff"] = 62;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.io_data_out"] = 63;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_data_out"] = 64;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_data_out"] = 65;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_data_1_out"] = 66;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_data_stride"] = 67;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_control_saturate"] = 68;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_data_max"] = 69;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_data_next"] = 70;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_data_next"] = 71;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_data_1_next"] = 72;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_control_0_enable"] = 73;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_control_enable"] = 74;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_control_enable"] = 75;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_control_done"] = 76;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_control_done"] = 77;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_control_0_done"] = 78;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM_1__io_raddr));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.io_raddr"] = 79;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM_1__raddr_reg));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.raddr_reg"] = 80;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo__io_enq_1));
  sim_data.signal_map["MemoryUnit.dataFifo.io_enq_1"] = 81;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo__io_enq_0));
  sim_data.signal_map["MemoryUnit.dataFifo.io_enq_0"] = 82;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM_1__io_wdata));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.io_wdata"] = 83;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_enqVld));
  sim_data.signal_map["MemoryUnit.dataFifo.io_enqVld"] = 84;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.io_data_out"] = 85;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_data_out"] = 86;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_data_out"] = 87;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_data_0_out"] = 88;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_SRAM_1__io_wen));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.io_wen"] = 89;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.io_data_out"] = 90;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_data_out"] = 91;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_data_out"] = 92;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_data_1_out"] = 93;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM_1__io_waddr));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.io_waddr"] = 94;
  std::string MemoryUnit_dataFifo_SRAM_1__mem_path = "MemoryUnit.dataFifo.SRAM_1.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM_1__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_dataFifo_SRAM_1__mem_path+"["+itos(i,false)+"]"] = 95+i;
  }
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM__io_raddr));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.io_raddr"] = 103;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM__raddr_reg));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.raddr_reg"] = 104;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM__io_wdata));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.io_wdata"] = 105;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_SRAM__io_wen));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.io_wen"] = 106;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_dataFifo_SRAM__io_waddr));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.io_waddr"] = 107;
  std::string MemoryUnit_dataFifo_SRAM__mem_path = "MemoryUnit.dataFifo.SRAM.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_dataFifo_SRAM__mem_path+"["+itos(i,false)+"]"] = 108+i;
  }
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.io_data_init"] = 116;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_control_enable"] = 117;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_control_enable"] = 118;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_control_done"] = 119;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.io_in"] = 120;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.io_data_in"] = 121;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.io_control_enable"] = 122;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.reset"] = 123;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.reset"] = 124;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.reset"] = 125;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.reset"] = 126;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.ff"] = 127;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.io_data_init"] = 128;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.io_control_reset"] = 129;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.io_data_in"] = 130;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.io_control_enable"] = 131;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reset"] = 132;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.reset"] = 133;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.counter.reg_.ff"] = 134;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.io_data_init"] = 135;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.io_in"] = 136;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.io_data_in"] = 137;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.io_control_enable"] = 138;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.reset"] = 139;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.reset"] = 140;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.reset"] = 141;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.ff"] = 142;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.io_data_init"] = 143;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.io_control_reset"] = 144;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.io_data_in"] = 145;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.io_control_enable"] = 146;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reset"] = 147;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.reset"] = 148;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.counter.reg_.ff"] = 149;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.r.io_data_out"] = 150;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.depulser.io_out"] = 151;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.r.io_data_out"] = 152;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.depulser.io_out"] = 153;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_control_waitIn"] = 154;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_control_waitOut"] = 155;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_control_waitIn"] = 156;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC.io_control_waitOut"] = 157;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.CounterRC_1.io_control_done"] = 158;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_rptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.dataFifo.rptr.io_control_1_done"] = 159;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.io_data_init"] = 160;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_data_stride"] = 161;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_data_max"] = 162;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_data_stride"] = 163;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_data_max"] = 164;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC__io_strideInc));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_strideInc"] = 165;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC__io_max));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_max"] = 166;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__io_isMax));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_isMax"] = 167;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_control_0_enable"] = 168;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_control_enable"] = 169;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_control_enable"] = 170;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_control_done"] = 171;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_control_done"] = 172;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_control_enable"] = 173;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_control_enable"] = 174;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_control_done"] = 175;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.io_in"] = 176;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.io_data_in"] = 177;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.io_control_enable"] = 178;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.reset"] = 179;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.reset"] = 180;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.reset"] = 181;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.reset"] = 182;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.ff"] = 183;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.io_data_init"] = 184;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_control_saturate"] = 185;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_control_reset"] = 186;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.io_data_in"] = 187;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.io_control_enable"] = 188;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reset"] = 189;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.reset"] = 190;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.reg_.ff"] = 191;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.io_data_init"] = 192;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.io_in"] = 193;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.io_data_in"] = 194;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.io_control_enable"] = 195;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.reset"] = 196;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.reset"] = 197;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.reset"] = 198;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.ff"] = 199;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.io_data_init"] = 200;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_control_saturate"] = 201;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_control_reset"] = 202;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.io_data_in"] = 203;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.io_control_enable"] = 204;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reset"] = 205;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.reset"] = 206;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.reg_.ff"] = 207;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.r.io_data_out"] = 208;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.depulser.io_out"] = 209;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.r.io_data_out"] = 210;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.depulser.io_out"] = 211;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_control_waitIn"] = 212;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_control_waitOut"] = 213;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_control_waitIn"] = 214;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_control_waitOut"] = 215;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.counter.io_data_next"] = 216;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_data_next"] = 217;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_data_1_next"] = 218;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.counter.io_data_next"] = 219;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC.io_data_next"] = 220;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_dataFifo_wptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_data_0_next"] = 221;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.CounterRC_1.io_control_done"] = 222;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_control_1_done"] = 223;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_wptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.dataFifo.wptr.io_control_0_done"] = 224;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.io_data_init"] = 225;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC__io_strideDec));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_strideDec"] = 226;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__io_inc));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_inc"] = 227;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC__io_initval));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_initval"] = 228;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__io_init));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_init"] = 229;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.io_data_in"] = 230;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__io_dec));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_dec"] = 231;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.io_control_enable"] = 232;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__reset));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reset"] = 233;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___reset));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.reset"] = 234;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_dataFifo_sizeUDC_reg___ff));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.reg_.ff"] = 235;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_sizeUDC__io_gtz));
  sim_data.signal_map["MemoryUnit.dataFifo.sizeUDC.io_gtz"] = 236;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_empty));
  sim_data.signal_map["MemoryUnit.dataFifo.io_empty"] = 237;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_config_data));
  sim_data.signal_map["MemoryUnit.dataFifo.io_config_data"] = 238;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_config_enable));
  sim_data.signal_map["MemoryUnit.dataFifo.io_config_enable"] = 239;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__config__chainRead));
  sim_data.signal_map["MemoryUnit.dataFifo.config__chainRead"] = 240;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__config__chainWrite));
  sim_data.signal_map["MemoryUnit.dataFifo.config__chainWrite"] = 241;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.io_data_out"] = 242;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_data_out"] = 243;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_data_out"] = 244;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_data_0_out"] = 245;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_data_stride"] = 246;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_control_saturate"] = 247;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_data_max"] = 248;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_data_next"] = 249;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_data_next"] = 250;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_data_0_next"] = 251;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.io_data_out"] = 252;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_out"] = 253;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_burstCounter__io_control_done));
  sim_data.signal_map["MemoryUnit.burstCounter.io_control_done"] = 254;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_deqVld));
  sim_data.signal_map["MemoryUnit.rwFifo.io_deqVld"] = 255;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_FF__io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.FF.io_data_in"] = 256;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_FF__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.FF.io_control_enable"] = 257;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.reset"] = 258;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_FF__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.FF.reset"] = 259;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_FF__ff));
  sim_data.signal_map["MemoryUnit.rwFifo.FF.ff"] = 260;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.io_data_out"] = 261;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_data_out"] = 262;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_data_out"] = 263;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_data_1_out"] = 264;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_data_stride"] = 265;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_control_saturate"] = 266;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_data_max"] = 267;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_data_next"] = 268;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_data_next"] = 269;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_data_1_next"] = 270;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_control_0_enable"] = 271;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_control_enable"] = 272;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_control_enable"] = 273;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_control_done"] = 274;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_control_done"] = 275;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_control_0_done"] = 276;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM_1__io_raddr));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.io_raddr"] = 277;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM_1__raddr_reg));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.raddr_reg"] = 278;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_enq_1));
  sim_data.signal_map["MemoryUnit.rwFifo.io_enq_1"] = 279;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_enq_0));
  sim_data.signal_map["MemoryUnit.rwFifo.io_enq_0"] = 280;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM_1__io_wdata));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.io_wdata"] = 281;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_enqVld));
  sim_data.signal_map["MemoryUnit.rwFifo.io_enqVld"] = 282;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.io_data_out"] = 283;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_data_out"] = 284;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_data_out"] = 285;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_data_0_out"] = 286;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM_1__io_wen));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.io_wen"] = 287;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.io_data_out"] = 288;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_data_out"] = 289;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_data_out"] = 290;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_data_1_out"] = 291;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM_1__io_waddr));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM_1.io_waddr"] = 292;
  std::string MemoryUnit_rwFifo_SRAM_1__mem_path = "MemoryUnit.rwFifo.SRAM_1.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM_1__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_rwFifo_SRAM_1__mem_path+"["+itos(i,false)+"]"] = 293+i;
  }
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM__io_raddr));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.io_raddr"] = 301;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM__raddr_reg));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.raddr_reg"] = 302;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM__io_wdata));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.io_wdata"] = 303;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM__io_wen));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.io_wen"] = 304;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_rwFifo_SRAM__io_waddr));
  sim_data.signal_map["MemoryUnit.rwFifo.SRAM.io_waddr"] = 305;
  std::string MemoryUnit_rwFifo_SRAM__mem_path = "MemoryUnit.rwFifo.SRAM.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_SRAM__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_rwFifo_SRAM__mem_path+"["+itos(i,false)+"]"] = 306+i;
  }
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.io_data_init"] = 314;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_control_enable"] = 315;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_control_enable"] = 316;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_control_done"] = 317;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.io_in"] = 318;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.io_data_in"] = 319;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.io_control_enable"] = 320;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.reset"] = 321;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.reset"] = 322;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.reset"] = 323;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.reset"] = 324;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.ff"] = 325;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.io_data_init"] = 326;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.io_control_reset"] = 327;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.io_data_in"] = 328;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.io_control_enable"] = 329;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reset"] = 330;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.reset"] = 331;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.counter.reg_.ff"] = 332;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.io_data_init"] = 333;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.io_in"] = 334;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.io_data_in"] = 335;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.io_control_enable"] = 336;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.reset"] = 337;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.reset"] = 338;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.reset"] = 339;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.ff"] = 340;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.io_data_init"] = 341;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.io_control_reset"] = 342;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.io_data_in"] = 343;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.io_control_enable"] = 344;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reset"] = 345;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.reset"] = 346;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.counter.reg_.ff"] = 347;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.r.io_data_out"] = 348;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.depulser.io_out"] = 349;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.r.io_data_out"] = 350;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.depulser.io_out"] = 351;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_control_waitIn"] = 352;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_control_waitOut"] = 353;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_control_waitIn"] = 354;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC.io_control_waitOut"] = 355;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.CounterRC_1.io_control_done"] = 356;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_rptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.rwFifo.rptr.io_control_1_done"] = 357;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.io_data_init"] = 358;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_data_stride"] = 359;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_data_max"] = 360;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_data_stride"] = 361;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_data_max"] = 362;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC__io_strideInc));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_strideInc"] = 363;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC__io_max));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_max"] = 364;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__io_isMax));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_isMax"] = 365;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_control_0_enable"] = 366;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_control_enable"] = 367;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_control_enable"] = 368;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_control_done"] = 369;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_control_done"] = 370;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_control_enable"] = 371;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_control_enable"] = 372;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_control_done"] = 373;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.io_in"] = 374;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.io_data_in"] = 375;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.io_control_enable"] = 376;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.reset"] = 377;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.reset"] = 378;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.reset"] = 379;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.reset"] = 380;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.ff"] = 381;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.io_data_init"] = 382;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_control_saturate"] = 383;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_control_reset"] = 384;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.io_data_in"] = 385;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.io_control_enable"] = 386;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reset"] = 387;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.reset"] = 388;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.reg_.ff"] = 389;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.io_data_init"] = 390;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.io_in"] = 391;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.io_data_in"] = 392;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.io_control_enable"] = 393;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.reset"] = 394;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.reset"] = 395;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.reset"] = 396;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.ff"] = 397;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.io_data_init"] = 398;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_control_saturate"] = 399;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_control_reset"] = 400;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.io_data_in"] = 401;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.io_control_enable"] = 402;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reset"] = 403;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.reset"] = 404;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.reg_.ff"] = 405;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.r.io_data_out"] = 406;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.depulser.io_out"] = 407;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.r.io_data_out"] = 408;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.depulser.io_out"] = 409;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_control_waitIn"] = 410;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_control_waitOut"] = 411;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_control_waitIn"] = 412;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_control_waitOut"] = 413;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.counter.io_data_next"] = 414;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_data_next"] = 415;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_data_1_next"] = 416;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.counter.io_data_next"] = 417;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC.io_data_next"] = 418;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_rwFifo_wptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_data_0_next"] = 419;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.CounterRC_1.io_control_done"] = 420;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_control_1_done"] = 421;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_wptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.rwFifo.wptr.io_control_0_done"] = 422;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.io_data_init"] = 423;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC__io_strideDec));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_strideDec"] = 424;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__io_inc));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_inc"] = 425;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC__io_initval));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_initval"] = 426;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__io_init));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_init"] = 427;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.io_data_in"] = 428;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__io_dec));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_dec"] = 429;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.io_control_enable"] = 430;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__reset));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reset"] = 431;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___reset));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.reset"] = 432;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_rwFifo_sizeUDC_reg___ff));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.reg_.ff"] = 433;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo_sizeUDC__io_gtz));
  sim_data.signal_map["MemoryUnit.rwFifo.sizeUDC.io_gtz"] = 434;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_deq_1));
  sim_data.signal_map["MemoryUnit.rwFifo.io_deq_1"] = 435;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_full));
  sim_data.signal_map["MemoryUnit.rwFifo.io_full"] = 436;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_empty));
  sim_data.signal_map["MemoryUnit.rwFifo.io_empty"] = 437;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_config_data));
  sim_data.signal_map["MemoryUnit.rwFifo.io_config_data"] = 438;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__io_config_enable));
  sim_data.signal_map["MemoryUnit.rwFifo.io_config_enable"] = 439;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__config__chainRead));
  sim_data.signal_map["MemoryUnit.rwFifo.config__chainRead"] = 440;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_rwFifo__config__chainWrite));
  sim_data.signal_map["MemoryUnit.rwFifo.config__chainWrite"] = 441;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.io_data_out"] = 442;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_data_out"] = 443;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_data_out"] = 444;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_data_0_out"] = 445;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_data_stride"] = 446;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_control_saturate"] = 447;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_data_max"] = 448;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_data_next"] = 449;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_data_next"] = 450;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_data_0_next"] = 451;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_deqVld));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_deqVld"] = 452;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_FF__io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.FF.io_data_in"] = 453;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_FF__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.FF.io_control_enable"] = 454;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.reset"] = 455;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_FF__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.FF.reset"] = 456;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_FF__ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.FF.ff"] = 457;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.io_data_out"] = 458;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_data_out"] = 459;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_data_out"] = 460;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_data_1_out"] = 461;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_data_stride"] = 462;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_control_saturate"] = 463;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_data_max"] = 464;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_data_next"] = 465;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_data_next"] = 466;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_data_1_next"] = 467;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_control_0_enable"] = 468;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_control_enable"] = 469;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_control_enable"] = 470;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_control_done"] = 471;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_control_done"] = 472;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_control_0_done"] = 473;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM_1__io_raddr));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.io_raddr"] = 474;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM_1__raddr_reg));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.raddr_reg"] = 475;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo__io_enq_1));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_enq_1"] = 476;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo__io_enq_0));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_enq_0"] = 477;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM_1__io_wdata));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.io_wdata"] = 478;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_enqVld));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_enqVld"] = 479;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.io_data_out"] = 480;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_data_out"] = 481;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_data_out"] = 482;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_data_0_out"] = 483;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_SRAM_1__io_wen));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.io_wen"] = 484;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.io_data_out"] = 485;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_data_out"] = 486;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_data_out"] = 487;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_data_1_out"] = 488;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM_1__io_waddr));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM_1.io_waddr"] = 489;
  std::string MemoryUnit_sizeFifo_SRAM_1__mem_path = "MemoryUnit.sizeFifo.SRAM_1.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM_1__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_sizeFifo_SRAM_1__mem_path+"["+itos(i,false)+"]"] = 490+i;
  }
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM__io_raddr));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.io_raddr"] = 498;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM__raddr_reg));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.raddr_reg"] = 499;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM__io_wdata));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.io_wdata"] = 500;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_SRAM__io_wen));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.io_wen"] = 501;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_sizeFifo_SRAM__io_waddr));
  sim_data.signal_map["MemoryUnit.sizeFifo.SRAM.io_waddr"] = 502;
  std::string MemoryUnit_sizeFifo_SRAM__mem_path = "MemoryUnit.sizeFifo.SRAM.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo_SRAM__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_sizeFifo_SRAM__mem_path+"["+itos(i,false)+"]"] = 503+i;
  }
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.io_data_init"] = 511;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_control_enable"] = 512;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_control_enable"] = 513;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_control_done"] = 514;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.io_in"] = 515;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.io_data_in"] = 516;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.io_control_enable"] = 517;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.reset"] = 518;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.reset"] = 519;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.reset"] = 520;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.reset"] = 521;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.ff"] = 522;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.io_data_init"] = 523;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.io_control_reset"] = 524;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.io_data_in"] = 525;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.io_control_enable"] = 526;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reset"] = 527;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.reset"] = 528;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.counter.reg_.ff"] = 529;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.io_data_init"] = 530;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.io_in"] = 531;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.io_data_in"] = 532;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.io_control_enable"] = 533;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.reset"] = 534;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.reset"] = 535;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.reset"] = 536;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.ff"] = 537;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.io_data_init"] = 538;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.io_control_reset"] = 539;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.io_data_in"] = 540;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.io_control_enable"] = 541;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reset"] = 542;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.reset"] = 543;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.counter.reg_.ff"] = 544;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.r.io_data_out"] = 545;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.depulser.io_out"] = 546;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.r.io_data_out"] = 547;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.depulser.io_out"] = 548;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_control_waitIn"] = 549;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_control_waitOut"] = 550;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_control_waitIn"] = 551;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC.io_control_waitOut"] = 552;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.CounterRC_1.io_control_done"] = 553;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_rptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.rptr.io_control_1_done"] = 554;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.io_data_init"] = 555;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_data_stride"] = 556;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_data_max"] = 557;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_data_stride"] = 558;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_data_max"] = 559;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_strideInc));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_strideInc"] = 560;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_max));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_max"] = 561;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_isMax));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_isMax"] = 562;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_control_0_enable"] = 563;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_control_enable"] = 564;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_control_enable"] = 565;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_control_done"] = 566;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_control_done"] = 567;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_control_enable"] = 568;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_control_enable"] = 569;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_control_done"] = 570;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.io_in"] = 571;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.io_data_in"] = 572;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.io_control_enable"] = 573;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.reset"] = 574;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.reset"] = 575;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.reset"] = 576;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.reset"] = 577;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.ff"] = 578;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.io_data_init"] = 579;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_control_saturate"] = 580;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_control_reset"] = 581;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.io_data_in"] = 582;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.io_control_enable"] = 583;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reset"] = 584;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.reset"] = 585;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.reg_.ff"] = 586;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.io_data_init"] = 587;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.io_in"] = 588;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.io_data_in"] = 589;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.io_control_enable"] = 590;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.reset"] = 591;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.reset"] = 592;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.reset"] = 593;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.ff"] = 594;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.io_data_init"] = 595;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_control_saturate"] = 596;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_control_reset"] = 597;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.io_data_in"] = 598;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.io_control_enable"] = 599;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reset"] = 600;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.reset"] = 601;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.reg_.ff"] = 602;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.r.io_data_out"] = 603;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.depulser.io_out"] = 604;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.r.io_data_out"] = 605;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.depulser.io_out"] = 606;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_control_waitIn"] = 607;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_control_waitOut"] = 608;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_control_waitIn"] = 609;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_control_waitOut"] = 610;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.counter.io_data_next"] = 611;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_data_next"] = 612;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_data_1_next"] = 613;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.counter.io_data_next"] = 614;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC.io_data_next"] = 615;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_sizeFifo_wptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_data_0_next"] = 616;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.CounterRC_1.io_control_done"] = 617;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_control_1_done"] = 618;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_wptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.sizeFifo.wptr.io_control_0_done"] = 619;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.io_data_init"] = 620;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_strideDec));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_strideDec"] = 621;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_inc));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_inc"] = 622;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_initval));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_initval"] = 623;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_init));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_init"] = 624;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.io_data_in"] = 625;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_dec));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_dec"] = 626;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.io_control_enable"] = 627;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reset"] = 628;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___reset));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.reset"] = 629;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_sizeFifo_sizeUDC_reg___ff));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.reg_.ff"] = 630;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo_sizeUDC__io_gtz));
  sim_data.signal_map["MemoryUnit.sizeFifo.sizeUDC.io_gtz"] = 631;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_sizeFifo__io_deq_1));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_deq_1"] = 632;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_full));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_full"] = 633;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_config_data));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_config_data"] = 634;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__io_config_enable));
  sim_data.signal_map["MemoryUnit.sizeFifo.io_config_enable"] = 635;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__config__chainRead));
  sim_data.signal_map["MemoryUnit.sizeFifo.config__chainRead"] = 636;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_sizeFifo__config__chainWrite));
  sim_data.signal_map["MemoryUnit.sizeFifo.config__chainWrite"] = 637;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.io_data_out"] = 638;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_data_out"] = 639;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_data_out"] = 640;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_data_0_out"] = 641;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_data_stride"] = 642;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_control_saturate"] = 643;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_data_max"] = 644;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_data_next"] = 645;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_data_next"] = 646;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_data_0_next"] = 647;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.io_data_out"] = 648;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_out"] = 649;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_deqVld));
  sim_data.signal_map["MemoryUnit.addrFifo.io_deqVld"] = 650;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_FF__io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.FF.io_data_in"] = 651;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_FF__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.FF.io_control_enable"] = 652;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.reset"] = 653;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_FF__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.FF.reset"] = 654;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_FF__ff));
  sim_data.signal_map["MemoryUnit.addrFifo.FF.ff"] = 655;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.io_data_out"] = 656;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_data_out"] = 657;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_data_out"] = 658;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_data_1_out"] = 659;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_data_stride"] = 660;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_control_saturate"] = 661;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_data_max"] = 662;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_data_next"] = 663;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_data_next"] = 664;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_data_1_next"] = 665;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_control_0_enable"] = 666;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_control_enable"] = 667;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_control_enable"] = 668;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_control_done"] = 669;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_control_done"] = 670;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_control_0_done"] = 671;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM_1__io_raddr));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.io_raddr"] = 672;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM_1__raddr_reg));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.raddr_reg"] = 673;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo__io_enq_1));
  sim_data.signal_map["MemoryUnit.addrFifo.io_enq_1"] = 674;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo__io_enq_0));
  sim_data.signal_map["MemoryUnit.addrFifo.io_enq_0"] = 675;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM_1__io_wdata));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.io_wdata"] = 676;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_enqVld));
  sim_data.signal_map["MemoryUnit.addrFifo.io_enqVld"] = 677;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.io_data_out"] = 678;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_data_out"] = 679;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_data_out"] = 680;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr__io_data_0_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_data_0_out"] = 681;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_SRAM_1__io_wen));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.io_wen"] = 682;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.io_data_out"] = 683;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_data_out"] = 684;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_data_out"] = 685;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr__io_data_1_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_data_1_out"] = 686;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM_1__io_waddr));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.io_waddr"] = 687;
  std::string MemoryUnit_addrFifo_SRAM_1__mem_path = "MemoryUnit.addrFifo.SRAM_1.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM_1__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_addrFifo_SRAM_1__mem_path+"["+itos(i,false)+"]"] = 688+i;
  }
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM__io_raddr));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.io_raddr"] = 696;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM__raddr_reg));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.raddr_reg"] = 697;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM__io_wdata));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.io_wdata"] = 698;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_SRAM__io_wen));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.io_wen"] = 699;
  sim_data.signals.push_back(new dat_api<3>(&mod->MemoryUnit_addrFifo_SRAM__io_waddr));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.io_waddr"] = 700;
  std::string MemoryUnit_addrFifo_SRAM__mem_path = "MemoryUnit.addrFifo.SRAM.mem";
  for (size_t i = 0 ; i < 8 ; i++) {
    sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM__mem.contents[i]));
    sim_data.signal_map[MemoryUnit_addrFifo_SRAM__mem_path+"["+itos(i,false)+"]"] = 701+i;
  }
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.io_data_init"] = 709;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_control_enable"] = 710;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_control_enable"] = 711;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_control_done"] = 712;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.io_in"] = 713;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.io_data_in"] = 714;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.io_control_enable"] = 715;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.reset"] = 716;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.reset"] = 717;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.reset"] = 718;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.reset"] = 719;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.ff"] = 720;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.io_data_init"] = 721;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.io_control_reset"] = 722;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.io_data_in"] = 723;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.io_control_enable"] = 724;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reset"] = 725;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.reset"] = 726;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.counter.reg_.ff"] = 727;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.io_data_init"] = 728;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.io_in"] = 729;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.io_data_in"] = 730;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.io_control_enable"] = 731;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.reset"] = 732;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.reset"] = 733;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.reset"] = 734;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.ff"] = 735;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.io_data_init"] = 736;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.io_control_reset"] = 737;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.io_data_in"] = 738;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.io_control_enable"] = 739;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reset"] = 740;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.reset"] = 741;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.counter.reg_.ff"] = 742;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.r.io_data_out"] = 743;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.depulser.io_out"] = 744;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.r.io_data_out"] = 745;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.depulser.io_out"] = 746;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_control_waitIn"] = 747;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_control_waitOut"] = 748;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_control_waitIn"] = 749;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC.io_control_waitOut"] = 750;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.CounterRC_1.io_control_done"] = 751;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_rptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.addrFifo.rptr.io_control_1_done"] = 752;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.io_data_init"] = 753;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_data_stride"] = 754;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_data_max"] = 755;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_stride));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_data_stride"] = 756;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_max));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_data_max"] = 757;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC__io_strideInc));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_strideInc"] = 758;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC__io_max));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_max"] = 759;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__io_isMax));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_isMax"] = 760;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr__io_control_0_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_control_0_enable"] = 761;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_control_enable"] = 762;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_control_enable"] = 763;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_control_done"] = 764;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_control_done"] = 765;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_control_enable"] = 766;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_control_enable"] = 767;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_control_done"] = 768;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.io_in"] = 769;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.io_data_in"] = 770;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.io_control_enable"] = 771;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.reset"] = 772;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.reset"] = 773;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.reset"] = 774;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.reset"] = 775;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.ff"] = 776;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.io_data_init"] = 777;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_control_saturate"] = 778;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_control_reset"] = 779;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.io_data_in"] = 780;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.io_control_enable"] = 781;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reset"] = 782;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.reset"] = 783;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.reg_.ff"] = 784;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.io_data_init"] = 785;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.io_in"] = 786;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.io_data_in"] = 787;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.io_control_enable"] = 788;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.reset"] = 789;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.reset"] = 790;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.reset"] = 791;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.ff"] = 792;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.io_data_init"] = 793;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_saturate));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_control_saturate"] = 794;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_control_reset"] = 795;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.io_data_in"] = 796;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.io_control_enable"] = 797;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reset"] = 798;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___reset));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.reset"] = 799;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.reg_.ff"] = 800;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.r.io_data_out"] = 801;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.depulser.io_out"] = 802;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.r.io_data_out"] = 803;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.depulser.io_out"] = 804;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_control_waitIn"] = 805;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_control_waitOut"] = 806;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitIn));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_control_waitIn"] = 807;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitOut));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_control_waitOut"] = 808;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.counter.io_data_next"] = 809;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_data_next"] = 810;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr__io_data_1_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_data_1_next"] = 811;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.counter.io_data_next"] = 812;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr_CounterRC__io_data_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC.io_data_next"] = 813;
  sim_data.signals.push_back(new dat_api<4>(&mod->MemoryUnit_addrFifo_wptr__io_data_0_next));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_data_0_next"] = 814;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.CounterRC_1.io_control_done"] = 815;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr__io_control_1_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_control_1_done"] = 816;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_wptr__io_control_0_done));
  sim_data.signal_map["MemoryUnit.addrFifo.wptr.io_control_0_done"] = 817;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___io_data_init));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.io_data_init"] = 818;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC__io_strideDec));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_strideDec"] = 819;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__io_inc));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_inc"] = 820;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC__io_initval));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_initval"] = 821;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__io_init));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_init"] = 822;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___io_data_in));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.io_data_in"] = 823;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__io_dec));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_dec"] = 824;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___io_control_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.io_control_enable"] = 825;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__reset));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reset"] = 826;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___reset));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.reset"] = 827;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_addrFifo_sizeUDC_reg___ff));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.reg_.ff"] = 828;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_sizeUDC__io_gtz));
  sim_data.signal_map["MemoryUnit.addrFifo.sizeUDC.io_gtz"] = 829;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM_1__io_rdata));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM_1.io_rdata"] = 830;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo__io_deq_1));
  sim_data.signal_map["MemoryUnit.addrFifo.io_deq_1"] = 831;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_empty));
  sim_data.signal_map["MemoryUnit.addrFifo.io_empty"] = 832;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_config_data));
  sim_data.signal_map["MemoryUnit.addrFifo.io_config_data"] = 833;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_config_enable));
  sim_data.signal_map["MemoryUnit.addrFifo.io_config_enable"] = 834;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__config__chainRead));
  sim_data.signal_map["MemoryUnit.addrFifo.config__chainRead"] = 835;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__config__chainWrite));
  sim_data.signal_map["MemoryUnit.addrFifo.config__chainWrite"] = 836;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo__io_full));
  sim_data.signal_map["MemoryUnit.dataFifo.io_full"] = 837;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo__io_full));
  sim_data.signal_map["MemoryUnit.addrFifo.io_full"] = 838;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM_1__io_rdata));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM_1.io_rdata"] = 839;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo__io_deq_1));
  sim_data.signal_map["MemoryUnit.dataFifo.io_deq_1"] = 840;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_SRAM__io_rdata));
  sim_data.signal_map["MemoryUnit.dataFifo.SRAM.io_rdata"] = 841;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_MuxN__io_ins_0));
  sim_data.signal_map["MemoryUnit.dataFifo.MuxN.io_ins_0"] = 842;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_MuxN__io_ins_1));
  sim_data.signal_map["MemoryUnit.dataFifo.MuxN.io_ins_1"] = 843;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_FF__io_data_out));
  sim_data.signal_map["MemoryUnit.dataFifo.FF.io_data_out"] = 844;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_dataFifo_MuxN__io_sel));
  sim_data.signal_map["MemoryUnit.dataFifo.MuxN.io_sel"] = 845;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo_MuxN__io_out));
  sim_data.signal_map["MemoryUnit.dataFifo.MuxN.io_out"] = 846;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_dataFifo__io_deq_0));
  sim_data.signal_map["MemoryUnit.dataFifo.io_deq_0"] = 847;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_burstCounter__io_data_out));
  sim_data.signal_map["MemoryUnit.burstCounter.io_data_out"] = 848;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_SRAM__io_rdata));
  sim_data.signal_map["MemoryUnit.addrFifo.SRAM.io_rdata"] = 849;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_MuxN__io_ins_0));
  sim_data.signal_map["MemoryUnit.addrFifo.MuxN.io_ins_0"] = 850;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_MuxN__io_ins_1));
  sim_data.signal_map["MemoryUnit.addrFifo.MuxN.io_ins_1"] = 851;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_FF__io_data_out));
  sim_data.signal_map["MemoryUnit.addrFifo.FF.io_data_out"] = 852;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit_addrFifo_MuxN__io_sel));
  sim_data.signal_map["MemoryUnit.addrFifo.MuxN.io_sel"] = 853;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo_MuxN__io_out));
  sim_data.signal_map["MemoryUnit.addrFifo.MuxN.io_out"] = 854;
  sim_data.signals.push_back(new dat_api<16>(&mod->MemoryUnit_addrFifo__io_deq_0));
  sim_data.signal_map["MemoryUnit.addrFifo.io_deq_0"] = 855;
  sim_data.signals.push_back(new dat_api<5>(&mod->MemoryUnit_burstTagCounter__io_data_out));
  sim_data.signal_map["MemoryUnit.burstTagCounter.io_data_out"] = 856;
  sim_data.signals.push_back(new dat_api<1>(&mod->MemoryUnit__config__scatterGather));
  sim_data.signal_map["MemoryUnit.config__scatterGather"] = 857;
  sim_data.clk_map["clk"] = new clk_api(&mod->clk);
}
