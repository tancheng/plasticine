#include "MemoryUnit.h"
#include "clockLoGlobals.h"
#include "globals.h"
void x00clockLo(MemoryUnit_t *ptr, dat_t<1> reset, bool assert_fire) {
  { MemoryUnit_burstTagCounter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_burstTagCounter__io_data_stride.values[0] = 0x1L;}

  { T0 = MemoryUnit_burstTagCounter__io_data_stride.values[0] | 0x0L << 5;}
  { MemoryUnit_burstTagCounter_reg___io_data_out.values[0] = MemoryUnit_burstTagCounter_reg___ff.values[0];}
  { MemoryUnit_burstTagCounter__count.values[0] = MemoryUnit_burstTagCounter_reg___io_data_out.values[0] | 0x0L << 5;}
  { MemoryUnit_burstTagCounter__newval.values[0] = MemoryUnit_burstTagCounter__count.values[0]+T0;}
  MemoryUnit_burstTagCounter__newval.values[0] = MemoryUnit_burstTagCounter__newval.values[0] & 0x3fL;
  { val_t __r = ptr->__rand_val(); MemoryUnit_burstTagCounter__io_control_saturate.values[0] = __r;}
  MemoryUnit_burstTagCounter__io_control_saturate.values[0] = MemoryUnit_burstTagCounter__io_control_saturate.values[0] & 0x1L;

  { T1 = TERNARY(MemoryUnit_burstTagCounter__io_control_saturate.values[0], MemoryUnit_burstTagCounter__count.values[0], 0x0L);}
  { MemoryUnit_burstTagCounter__io_data_max.values[0] = 0x10L;}

  { T2 = MemoryUnit_burstTagCounter__io_data_max.values[0] | 0x0L << 5;}
  MemoryUnit_burstTagCounter__isMax.values[0] = T2<=MemoryUnit_burstTagCounter__newval.values[0];
  { MemoryUnit_burstTagCounter__next.values[0] = TERNARY_1(MemoryUnit_burstTagCounter__isMax.values[0], T1, MemoryUnit_burstTagCounter__newval.values[0]);}
  { MemoryUnit_burstTagCounter__io_control_reset.values[0] = 0x0L;}

  { T3 = TERNARY(MemoryUnit_burstTagCounter__io_control_reset.values[0], 0x0L, MemoryUnit_burstTagCounter__next.values[0]);}

  { T4 = T3;}
  T4 = T4 & 0x1fL;
  { MemoryUnit_burstTagCounter_reg___io_data_in.values[0] = T4;}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC__io_out.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0];}
  MemoryUnit_sizeFifo__empty.values[0] = MemoryUnit_sizeFifo_sizeUDC__io_out.values[0] == 0x0L;
  { MemoryUnit_sizeFifo__io_empty.values[0] = MemoryUnit_sizeFifo__empty.values[0];}
  { MemoryUnit__burstVld.values[0] = ~MemoryUnit_sizeFifo__io_empty.values[0];}
  MemoryUnit__burstVld.values[0] = MemoryUnit__burstVld.values[0] & 0x1L;
  { MemoryUnit_burstTagCounter__io_control_enable.values[0] = MemoryUnit__burstVld.values[0];}

  { T5 = MemoryUnit_burstTagCounter__io_control_reset.values[0] | MemoryUnit_burstTagCounter__io_control_enable.values[0];}
  { MemoryUnit_burstTagCounter_reg___io_control_enable.values[0] = T5;}

  { T6 = TERNARY_1(MemoryUnit_burstTagCounter_reg___io_control_enable.values[0], MemoryUnit_burstTagCounter_reg___io_data_in.values[0], MemoryUnit_burstTagCounter_reg___ff.values[0]);}
  { MemoryUnit_burstTagCounter_reg___d.values[0] = T6;}
  { MemoryUnit_burstTagCounter__reset.values[0] = reset.values[0];}
  { MemoryUnit_burstTagCounter_reg___reset.values[0] = MemoryUnit_burstTagCounter__reset.values[0];}
  { T7.values[0] = TERNARY(MemoryUnit_burstTagCounter_reg___reset.values[0], MemoryUnit_burstTagCounter_reg___io_data_init.values[0], MemoryUnit_burstTagCounter_reg___d.values[0]);}

  { T8 = MemoryUnit_burstTagCounter__next.values[0];}
  T8 = T8 & 0x1fL;
  { MemoryUnit_burstTagCounter__io_data_next.values[0] = T8;}

  { T9 = MemoryUnit_burstTagCounter__io_control_enable.values[0] & MemoryUnit_burstTagCounter__isMax.values[0];}
  { MemoryUnit_burstTagCounter__io_control_done.values[0] = T9;}
  { MemoryUnit_burstCounter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_burstCounter__io_data_stride.values[0] = 0x1L;}

  { T10 = MemoryUnit_burstCounter__io_data_stride.values[0] | 0x0L << 16;}
  { MemoryUnit_burstCounter_reg___io_data_out.values[0] = MemoryUnit_burstCounter_reg___ff.values[0];}
  { MemoryUnit_burstCounter__count.values[0] = MemoryUnit_burstCounter_reg___io_data_out.values[0] | 0x0L << 16;}
  { MemoryUnit_burstCounter__newval.values[0] = MemoryUnit_burstCounter__count.values[0]+T10;}
  MemoryUnit_burstCounter__newval.values[0] = MemoryUnit_burstCounter__newval.values[0] & 0x1ffffL;
  { MemoryUnit_burstCounter__io_control_saturate.values[0] = 0x0L;}

  { T11 = TERNARY(MemoryUnit_burstCounter__io_control_saturate.values[0], MemoryUnit_burstCounter__count.values[0], 0x0L);}

  { T12 = MemoryUnit_sizeFifo_SRAM__mem.get(MemoryUnit_sizeFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_sizeFifo_SRAM__io_rdata.values[0] = T12;}
  { MemoryUnit_sizeFifo_MuxN__io_ins_0.values[0] = MemoryUnit_sizeFifo_SRAM__io_rdata.values[0];}

  { T13 = MemoryUnit_sizeFifo_SRAM_1__mem.get(MemoryUnit_sizeFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0] = T13;}
  { MemoryUnit_sizeFifo_MuxN__io_ins_1.values[0] = MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_sizeFifo_FF__io_data_out.values[0] = MemoryUnit_sizeFifo_FF__ff.values[0];}
  { MemoryUnit_sizeFifo_MuxN__io_sel.values[0] = MemoryUnit_sizeFifo_FF__io_data_out.values[0];}

  { T14 = TERNARY_1(MemoryUnit_sizeFifo_MuxN__io_sel.values[0], MemoryUnit_sizeFifo_MuxN__io_ins_1.values[0], MemoryUnit_sizeFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_sizeFifo_MuxN__io_out.values[0] = T14;}
  { MemoryUnit_sizeFifo__io_deq_0.values[0] = MemoryUnit_sizeFifo_MuxN__io_out.values[0];}

  { T15 = MemoryUnit_sizeFifo__io_deq_0.values[0];}
  T15 = T15 & 0x3fL;

  T16 = T15 != 0x0L;

  { T17 = T16 | 0x0L << 1;}

  { T18 = MemoryUnit_sizeFifo__io_deq_0.values[0] >> 6;}
  T18 = T18 & 0x3ffL;
  { MemoryUnit__sizeInBursts.values[0] = T18+T17;}
  MemoryUnit__sizeInBursts.values[0] = MemoryUnit__sizeInBursts.values[0] & 0x3ffL;

  { T19 = TERNARY(MemoryUnit__config__scatterGather.values[0], 0x1L, MemoryUnit__sizeInBursts.values[0]);}

  { T20 = T19 | 0x0L << 10;}
  { MemoryUnit_burstCounter__io_data_max.values[0] = T20;}

  { T21 = MemoryUnit_burstCounter__io_data_max.values[0] | 0x0L << 16;}
  MemoryUnit_burstCounter__isMax.values[0] = T21<=MemoryUnit_burstCounter__newval.values[0];
  { MemoryUnit_burstCounter__next.values[0] = TERNARY_1(MemoryUnit_burstCounter__isMax.values[0], T11, MemoryUnit_burstCounter__newval.values[0]);}
  { MemoryUnit_burstCounter__io_control_reset.values[0] = 0x0L;}

  { T22 = TERNARY(MemoryUnit_burstCounter__io_control_reset.values[0], 0x0L, MemoryUnit_burstCounter__next.values[0]);}

  { T23 = T22;}
  T23 = T23 & 0xffffL;
  { MemoryUnit_burstCounter_reg___io_data_in.values[0] = T23;}
  { MemoryUnit_burstCounter__io_control_enable.values[0] = MemoryUnit__burstVld.values[0];}

  { T24 = MemoryUnit_burstCounter__io_control_reset.values[0] | MemoryUnit_burstCounter__io_control_enable.values[0];}
  { MemoryUnit_burstCounter_reg___io_control_enable.values[0] = T24;}

  { T25 = TERNARY_1(MemoryUnit_burstCounter_reg___io_control_enable.values[0], MemoryUnit_burstCounter_reg___io_data_in.values[0], MemoryUnit_burstCounter_reg___ff.values[0]);}
  { MemoryUnit_burstCounter_reg___d.values[0] = T25;}
  { MemoryUnit_burstCounter__reset.values[0] = reset.values[0];}
  { MemoryUnit_burstCounter_reg___reset.values[0] = MemoryUnit_burstCounter__reset.values[0];}
  { T26.values[0] = TERNARY(MemoryUnit_burstCounter_reg___reset.values[0], MemoryUnit_burstCounter_reg___io_data_init.values[0], MemoryUnit_burstCounter_reg___d.values[0]);}

  { T27 = MemoryUnit_burstCounter__next.values[0];}
  T27 = T27 & 0xffffL;
  { MemoryUnit_burstCounter__io_data_next.values[0] = T27;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_dataFifo_FF__io_data_init.values[0] = MemoryUnit_dataFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__count.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T28 = MemoryUnit_dataFifo_rptr_CounterRC_counter__count.values[0];}
  T28 = T28 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_out.values[0] = T28;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_0_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T29 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__count.values[0]+T29;}
  MemoryUnit_dataFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T30 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T31 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax.values[0] = T31<=MemoryUnit_dataFifo_rptr_CounterRC_counter__newval.values[0];
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax.values[0], T30, MemoryUnit_dataFifo_rptr_CounterRC_counter__newval.values[0]);}

  { T32 = MemoryUnit_dataFifo_rptr_CounterRC_counter__next.values[0];}
  T32 = T32 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_next.values[0] = T32;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_0_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_dataFifo_sizeUDC__io_out.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0];}
  MemoryUnit_dataFifo__empty.values[0] = MemoryUnit_dataFifo_sizeUDC__io_out.values[0] == 0x0L;

  { T33 = ~MemoryUnit_dataFifo__empty.values[0];}
  T33 = T33 & 0x1L;

  { T34 = MemoryUnit_rwFifo_SRAM__mem.get(MemoryUnit_rwFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_rwFifo_SRAM__io_rdata.values[0] = T34;}
  { MemoryUnit_rwFifo_MuxN__io_ins_0.values[0] = MemoryUnit_rwFifo_SRAM__io_rdata.values[0];}

  { T35 = MemoryUnit_rwFifo_SRAM_1__mem.get(MemoryUnit_rwFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0] = T35;}
  { MemoryUnit_rwFifo_MuxN__io_ins_1.values[0] = MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_rwFifo_FF__io_data_out.values[0] = MemoryUnit_rwFifo_FF__ff.values[0];}
  { MemoryUnit_rwFifo_MuxN__io_sel.values[0] = MemoryUnit_rwFifo_FF__io_data_out.values[0];}

  { T36 = TERNARY_1(MemoryUnit_rwFifo_MuxN__io_sel.values[0], MemoryUnit_rwFifo_MuxN__io_ins_1.values[0], MemoryUnit_rwFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_rwFifo_MuxN__io_out.values[0] = T36;}
  { MemoryUnit_rwFifo__io_deq_0.values[0] = MemoryUnit_rwFifo_MuxN__io_out.values[0];}

  { T37 = MemoryUnit_rwFifo__io_deq_0.values[0] & MemoryUnit__burstVld.values[0];}

  { T38 = T37;}
  { MemoryUnit_dataFifo__io_deqVld.values[0] = T38;}
  { MemoryUnit_dataFifo__readEn.values[0] = MemoryUnit_dataFifo__io_deqVld.values[0] & T33;}

  { T39 = TERNARY_1(MemoryUnit_dataFifo__readEn.values[0], MemoryUnit_dataFifo_rptr__io_data_0_next.values[0], MemoryUnit_dataFifo_rptr__io_data_0_out.values[0]);}

  T40 = (T39 >> 0) & 1;
  { MemoryUnit_dataFifo_FF__io_data_in.values[0] = T40;}
  { MemoryUnit_dataFifo_FF__io_control_enable.values[0] = 0x1L;}

  { T41 = TERNARY(MemoryUnit_dataFifo_FF__io_control_enable.values[0], MemoryUnit_dataFifo_FF__io_data_in.values[0], MemoryUnit_dataFifo_FF__ff.values[0]);}
  { MemoryUnit_dataFifo_FF__d.values[0] = T41;}
  { MemoryUnit_dataFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_dataFifo_FF__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { T42.values[0] = TERNARY_1(MemoryUnit_dataFifo_FF__reset.values[0], MemoryUnit_dataFifo_FF__io_data_init.values[0], MemoryUnit_dataFifo_FF__d.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T43 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count.values[0];}
  T43 = T43 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T43;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_1_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T44 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count.values[0]+T44;}
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T45 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T46 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax.values[0] = T46<=MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax.values[0], T45, MemoryUnit_dataFifo_rptr_CounterRC_1_counter__newval.values[0]);}

  { T47 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next.values[0];}
  T47 = T47 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T47;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_data_1_next.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_dataFifo__readEn.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_enable.values[0];}

  { T48 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_rptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0] = T48;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_0_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0];}

  { T49 = TERNARY_1(MemoryUnit_dataFifo_rptr__io_control_0_done.values[0], MemoryUnit_dataFifo_rptr__io_data_1_next.values[0], MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}
  { MemoryUnit_dataFifo__nextHeadLocalAddr.values[0] = TERNARY_1(MemoryUnit_dataFifo__config__chainRead.values[0], T49, MemoryUnit_dataFifo_rptr__io_data_1_next.values[0]);}

  { T50 = TERNARY_1(MemoryUnit_dataFifo__readEn.values[0], MemoryUnit_dataFifo__nextHeadLocalAddr.values[0], MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}

  { T51 = T50;}
  T51 = T51 & 0x7L;
  { MemoryUnit_dataFifo_SRAM_1__io_raddr.values[0] = T51;}
  { MemoryUnit_dataFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_wdata_1.values[0];}
  { MemoryUnit_dataFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_wdata_0.values[0];}

  { T52 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], MemoryUnit_dataFifo__io_enq_0.values[0], MemoryUnit_dataFifo__io_enq_1.values[0]);}
  { MemoryUnit_dataFifo_SRAM_1__io_wdata.values[0] = T52;}
  { MemoryUnit_dataFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__count.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T53 = MemoryUnit_dataFifo_wptr_CounterRC_counter__count.values[0];}
  T53 = T53 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_out.values[0] = T53;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_data_out.values[0];}

  T54 = MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] == 0x1L;

  { T55 = MemoryUnit_dataFifo__io_enqVld.values[0] & T54;}

  { T56 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], T55, MemoryUnit_dataFifo__io_enqVld.values[0]);}
  { MemoryUnit_dataFifo_SRAM_1__io_wen.values[0] = T56;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T57 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count.values[0];}
  T57 = T57 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T57;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_1_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_out.values[0];}

  { T58 = MemoryUnit_dataFifo_wptr__io_data_1_out.values[0];}
  T58 = T58 & 0x7L;
  { MemoryUnit_dataFifo_SRAM_1__io_waddr.values[0] = T58;}

  { T59 = TERNARY_1(MemoryUnit_dataFifo__readEn.values[0], MemoryUnit_dataFifo__nextHeadLocalAddr.values[0], MemoryUnit_dataFifo_rptr__io_data_1_out.values[0]);}

  { T60 = T59;}
  T60 = T60 & 0x7L;
  { MemoryUnit_dataFifo_SRAM__io_raddr.values[0] = T60;}
  { MemoryUnit_dataFifo_SRAM__io_wdata.values[0] = MemoryUnit_dataFifo__io_enq_0.values[0];}

  T61 = MemoryUnit_dataFifo_wptr__io_data_0_out.values[0] == 0x0L;

  { T62 = MemoryUnit_dataFifo__io_enqVld.values[0] & T61;}

  { T63 = TERNARY_1(MemoryUnit_dataFifo__config__chainWrite.values[0], T62, MemoryUnit_dataFifo__io_enqVld.values[0]);}
  { MemoryUnit_dataFifo_SRAM__io_wen.values[0] = T63;}

  { T64 = MemoryUnit_dataFifo_wptr__io_data_1_out.values[0];}
  T64 = T64 & 0x7L;
  { MemoryUnit_dataFifo_SRAM__io_waddr.values[0] = T64;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_enable.values[0];}

  { T65 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_rptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T65;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T66 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T66;}

  { T67 = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T67;}

  { T68 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__d.values[0] = T68;}
  { MemoryUnit_dataFifo_rptr__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_dataFifo_rptr__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T69.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T70 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_1_counter__next.values[0]);}

  { T71 = T70;}
  T71 = T71 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T71;}

  { T72 = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T72;}

  { T73 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___d.values[0] = T73;}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T74.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_dataFifo_rptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T75 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T75;}

  { T76 = MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T76;}

  { T77 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__d.values[0] = T77;}
  { MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_dataFifo_rptr__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T78.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T79 = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_rptr_CounterRC_counter__next.values[0]);}

  { T80 = T79;}
  T80 = T80 & 0xfL;
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T80;}

  { T81 = MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T81;}

  { T82 = TERNARY_1(MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___d.values[0] = T82;}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_counter__reset.values[0];}
  { T83.values[0] = TERNARY(MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_dataFifo_rptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T84 = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T84;}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}

  { T85 = MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_dataFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_rptr_CounterRC__io_control_waitOut.values[0] = T85;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_dataFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_rptr__io_control_1_done.values[0] = MemoryUnit_dataFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T86 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count.values[0]+T86;}
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T87 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax.values[0] = T87<=MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T88 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__count.values[0]+T88;}
  MemoryUnit_dataFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T89 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax.values[0] = T89<=MemoryUnit_dataFifo_wptr_CounterRC_counter__newval.values[0];

  { T90 = TERNARY(MemoryUnit_dataFifo__config__chainWrite.values[0], 0x1L, 0x2L);}

  { T91 = T90 | 0x0L << 2;}
  { MemoryUnit_dataFifo_sizeUDC__io_strideInc.values[0] = T91;}
  { MemoryUnit_dataFifo_sizeUDC__incval.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_dataFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_dataFifo_sizeUDC__incval.values[0] = MemoryUnit_dataFifo_sizeUDC__incval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_sizeUDC__io_max.values[0] = 0x10L;}

  T92 = MemoryUnit_dataFifo_sizeUDC__io_max.values[0]<MemoryUnit_dataFifo_sizeUDC__incval.values[0];
  { MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0] = T92;}

  { T93 = ~MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0];}
  T93 = T93 & 0x1L;
  { MemoryUnit_dataFifo__writeEn.values[0] = MemoryUnit_dataFifo__io_enqVld.values[0] & T93;}
  { MemoryUnit_dataFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_dataFifo__writeEn.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_enable.values[0];}

  { T94 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0] = T94;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_enable.values[0];}

  { T95 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T95;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T96 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T96;}

  { T97 = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T97;}

  { T98 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__d.values[0] = T98;}
  { MemoryUnit_dataFifo_wptr__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_dataFifo_wptr__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T99.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T100 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__isMax.values[0], T100, MemoryUnit_dataFifo_wptr_CounterRC_1_counter__newval.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T101 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next.values[0]);}

  { T102 = T101;}
  T102 = T102 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T102;}

  { T103 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T103;}

  { T104 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___d.values[0] = T104;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T105.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_dataFifo_wptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T106 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T106;}

  { T107 = MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T107;}

  { T108 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__d.values[0] = T108;}
  { MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_dataFifo_wptr__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T109.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T110 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_counter__isMax.values[0], T110, MemoryUnit_dataFifo_wptr_CounterRC_counter__newval.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T111 = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_dataFifo_wptr_CounterRC_counter__next.values[0]);}

  { T112 = T111;}
  T112 = T112 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T112;}

  { T113 = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T113;}

  { T114 = TERNARY_1(MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___d.values[0] = T114;}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__reset.values[0];}
  { T115.values[0] = TERNARY(MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_dataFifo_wptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T116 = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T116;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}

  { T117 = MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_dataFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_control_waitOut.values[0] = T117;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;

  { T118 = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__next.values[0];}
  T118 = T118 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T118;}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_1_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_data_next.values[0];}

  { T119 = MemoryUnit_dataFifo_wptr_CounterRC_counter__next.values[0];}
  T119 = T119 & 0xfL;
  { MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_next.values[0] = T119;}
  { MemoryUnit_dataFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr__io_data_0_next.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr__io_control_1_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_dataFifo_wptr__io_control_0_done.values[0] = MemoryUnit_dataFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}

  { T120 = TERNARY(MemoryUnit_dataFifo__config__chainRead.values[0], 0x1L, 0x2L);}

  { T121 = T120 | 0x0L << 2;}
  { MemoryUnit_dataFifo_sizeUDC__io_strideDec.values[0] = T121;}
  { MemoryUnit_dataFifo_sizeUDC__decval.values[0] = MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_dataFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_dataFifo_sizeUDC__decval.values[0] = MemoryUnit_dataFifo_sizeUDC__decval.values[0] & 0x1fL;
  { MemoryUnit_dataFifo_sizeUDC__io_inc.values[0] = MemoryUnit_dataFifo__writeEn.values[0];}

  { T122 = TERNARY_1(MemoryUnit_dataFifo_sizeUDC__io_inc.values[0], MemoryUnit_dataFifo_sizeUDC__incval.values[0], MemoryUnit_dataFifo_sizeUDC__decval.values[0]);}
  { MemoryUnit_dataFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_dataFifo_sizeUDC__io_init.values[0] = 0x0L;}

  { T123 = TERNARY(MemoryUnit_dataFifo_sizeUDC__io_init.values[0], MemoryUnit_dataFifo_sizeUDC__io_initval.values[0], T122);}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_data_in.values[0] = T123;}
  { MemoryUnit_dataFifo_sizeUDC__io_dec.values[0] = MemoryUnit_dataFifo__readEn.values[0];}

  { T124 = MemoryUnit_dataFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_dataFifo_sizeUDC__io_dec.values[0];}

  { T125 = T124 | MemoryUnit_dataFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___io_control_enable.values[0] = T125;}

  { T126 = TERNARY_1(MemoryUnit_dataFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_dataFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_dataFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_dataFifo_sizeUDC_reg___d.values[0] = T126;}
  { MemoryUnit_dataFifo_sizeUDC__reset.values[0] = MemoryUnit_dataFifo__reset.values[0];}
  { MemoryUnit_dataFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_dataFifo_sizeUDC__reset.values[0];}
  { T127.values[0] = TERNARY(MemoryUnit_dataFifo_sizeUDC_reg___reset.values[0], MemoryUnit_dataFifo_sizeUDC_reg___io_data_init.values[0], MemoryUnit_dataFifo_sizeUDC_reg___d.values[0]);}

  T128 = 0x0L<MemoryUnit_dataFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_dataFifo_sizeUDC__io_gtz.values[0] = T128;}
  { MemoryUnit_dataFifo__io_empty.values[0] = MemoryUnit_dataFifo__empty.values[0];}
  { MemoryUnit_dataFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}

  { T129 = MemoryUnit_dataFifo__io_config_data.values[0] | 0x0L << 1;}

  { T130 = 0x0L-T129;}
  T130 = T130 & 0x3L;

  T131 = (T130 >> 0) & 1;
  { MemoryUnit_dataFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}

  { T132 = TERNARY_1(MemoryUnit_dataFifo__io_config_enable.values[0], T131, MemoryUnit_dataFifo__config__chainRead.values[0]);}
  { MemoryUnit_dataFifo__configIn_chainRead.values[0] = T132;}
  { T133.values[0] = TERNARY(MemoryUnit_dataFifo__reset.values[0], 0x1L, MemoryUnit_dataFifo__configIn_chainRead.values[0]);}

  T134 = (T130 >> 1) & 1;

  { T135 = TERNARY_1(MemoryUnit_dataFifo__io_config_enable.values[0], T134, MemoryUnit_dataFifo__config__chainWrite.values[0]);}
  { MemoryUnit_dataFifo__configIn_chainWrite.values[0] = T135;}
  { T136.values[0] = TERNARY(MemoryUnit_dataFifo__reset.values[0], 0x1L, MemoryUnit_dataFifo__configIn_chainWrite.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_rwFifo_FF__io_data_init.values[0] = MemoryUnit_rwFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__count.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T137 = MemoryUnit_rwFifo_rptr_CounterRC_counter__count.values[0];}
  T137 = T137 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_out.values[0] = T137;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_0_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T138 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__count.values[0]+T138;}
  MemoryUnit_rwFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T139 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T140 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax.values[0] = T140<=MemoryUnit_rwFifo_rptr_CounterRC_counter__newval.values[0];
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax.values[0], T139, MemoryUnit_rwFifo_rptr_CounterRC_counter__newval.values[0]);}

  { T141 = MemoryUnit_rwFifo_rptr_CounterRC_counter__next.values[0];}
  T141 = T141 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_next.values[0] = T141;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_0_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_rwFifo_sizeUDC__io_out.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0];}
  MemoryUnit_rwFifo__empty.values[0] = MemoryUnit_rwFifo_sizeUDC__io_out.values[0] == 0x0L;

  { T142 = ~MemoryUnit_rwFifo__empty.values[0];}
  T142 = T142 & 0x1L;

  { T143 = MemoryUnit_burstCounter__io_control_enable.values[0] & MemoryUnit_burstCounter__isMax.values[0];}
  { MemoryUnit_burstCounter__io_control_done.values[0] = T143;}
  { MemoryUnit_rwFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  { MemoryUnit_rwFifo__readEn.values[0] = MemoryUnit_rwFifo__io_deqVld.values[0] & T142;}

  { T144 = TERNARY_1(MemoryUnit_rwFifo__readEn.values[0], MemoryUnit_rwFifo_rptr__io_data_0_next.values[0], MemoryUnit_rwFifo_rptr__io_data_0_out.values[0]);}

  T145 = (T144 >> 0) & 1;
  { MemoryUnit_rwFifo_FF__io_data_in.values[0] = T145;}
  { MemoryUnit_rwFifo_FF__io_control_enable.values[0] = 0x1L;}

  { T146 = TERNARY(MemoryUnit_rwFifo_FF__io_control_enable.values[0], MemoryUnit_rwFifo_FF__io_data_in.values[0], MemoryUnit_rwFifo_FF__ff.values[0]);}
  { MemoryUnit_rwFifo_FF__d.values[0] = T146;}
  { MemoryUnit_rwFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_rwFifo_FF__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { T147.values[0] = TERNARY_1(MemoryUnit_rwFifo_FF__reset.values[0], MemoryUnit_rwFifo_FF__io_data_init.values[0], MemoryUnit_rwFifo_FF__d.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T148 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count.values[0];}
  T148 = T148 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T148;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_1_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T149 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count.values[0]+T149;}
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T150 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T151 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax.values[0] = T151<=MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax.values[0], T150, MemoryUnit_rwFifo_rptr_CounterRC_1_counter__newval.values[0]);}

  { T152 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next.values[0];}
  T152 = T152 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T152;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_data_1_next.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_rwFifo__readEn.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_enable.values[0];}

  { T153 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_rptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0] = T153;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_0_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0];}

  { T154 = TERNARY_1(MemoryUnit_rwFifo_rptr__io_control_0_done.values[0], MemoryUnit_rwFifo_rptr__io_data_1_next.values[0], MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}
  { MemoryUnit_rwFifo__nextHeadLocalAddr.values[0] = TERNARY_1(MemoryUnit_rwFifo__config__chainRead.values[0], T154, MemoryUnit_rwFifo_rptr__io_data_1_next.values[0]);}

  { T155 = TERNARY_1(MemoryUnit_rwFifo__readEn.values[0], MemoryUnit_rwFifo__nextHeadLocalAddr.values[0], MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}

  { T156 = T155;}
  T156 = T156 & 0x7L;
  { MemoryUnit_rwFifo_SRAM_1__io_raddr.values[0] = T156;}
  { MemoryUnit_rwFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_isWr.values[0];}
  { MemoryUnit_rwFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_isWr.values[0];}

  { T157 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], MemoryUnit_rwFifo__io_enq_0.values[0], MemoryUnit_rwFifo__io_enq_1.values[0]);}
  { MemoryUnit_rwFifo_SRAM_1__io_wdata.values[0] = T157;}
  { MemoryUnit_rwFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__count.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T158 = MemoryUnit_rwFifo_wptr_CounterRC_counter__count.values[0];}
  T158 = T158 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_out.values[0] = T158;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_data_out.values[0];}

  T159 = MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] == 0x1L;

  { T160 = MemoryUnit_rwFifo__io_enqVld.values[0] & T159;}

  { T161 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], T160, MemoryUnit_rwFifo__io_enqVld.values[0]);}
  { MemoryUnit_rwFifo_SRAM_1__io_wen.values[0] = T161;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T162 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count.values[0];}
  T162 = T162 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T162;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_1_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_out.values[0];}

  { T163 = MemoryUnit_rwFifo_wptr__io_data_1_out.values[0];}
  T163 = T163 & 0x7L;
  { MemoryUnit_rwFifo_SRAM_1__io_waddr.values[0] = T163;}

  { T164 = TERNARY_1(MemoryUnit_rwFifo__readEn.values[0], MemoryUnit_rwFifo__nextHeadLocalAddr.values[0], MemoryUnit_rwFifo_rptr__io_data_1_out.values[0]);}

  { T165 = T164;}
  T165 = T165 & 0x7L;
  { MemoryUnit_rwFifo_SRAM__io_raddr.values[0] = T165;}
  { MemoryUnit_rwFifo_SRAM__io_wdata.values[0] = MemoryUnit_rwFifo__io_enq_0.values[0];}

  T166 = MemoryUnit_rwFifo_wptr__io_data_0_out.values[0] == 0x0L;

  { T167 = MemoryUnit_rwFifo__io_enqVld.values[0] & T166;}

  { T168 = TERNARY_1(MemoryUnit_rwFifo__config__chainWrite.values[0], T167, MemoryUnit_rwFifo__io_enqVld.values[0]);}
  { MemoryUnit_rwFifo_SRAM__io_wen.values[0] = T168;}

  { T169 = MemoryUnit_rwFifo_wptr__io_data_1_out.values[0];}
  T169 = T169 & 0x7L;
  { MemoryUnit_rwFifo_SRAM__io_waddr.values[0] = T169;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_enable.values[0];}

  { T170 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_rptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T170;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T171 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T171;}

  { T172 = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T172;}

  { T173 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__d.values[0] = T173;}
  { MemoryUnit_rwFifo_rptr__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_rwFifo_rptr__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T174.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T175 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_1_counter__next.values[0]);}

  { T176 = T175;}
  T176 = T176 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T176;}

  { T177 = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T177;}

  { T178 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___d.values[0] = T178;}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T179.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_rwFifo_rptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T180 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T180;}

  { T181 = MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T181;}

  { T182 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__d.values[0] = T182;}
  { MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_rwFifo_rptr__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T183.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T184 = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_rptr_CounterRC_counter__next.values[0]);}

  { T185 = T184;}
  T185 = T185 & 0xfL;
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T185;}

  { T186 = MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T186;}

  { T187 = TERNARY_1(MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___d.values[0] = T187;}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_counter__reset.values[0];}
  { T188.values[0] = TERNARY(MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_rwFifo_rptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T189 = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T189;}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}

  { T190 = MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_rwFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_rptr_CounterRC__io_control_waitOut.values[0] = T190;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_rwFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_rptr__io_control_1_done.values[0] = MemoryUnit_rwFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T191 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count.values[0]+T191;}
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T192 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax.values[0] = T192<=MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T193 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__count.values[0]+T193;}
  MemoryUnit_rwFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T194 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax.values[0] = T194<=MemoryUnit_rwFifo_wptr_CounterRC_counter__newval.values[0];

  { T195 = TERNARY(MemoryUnit_rwFifo__config__chainWrite.values[0], 0x1L, 0x2L);}

  { T196 = T195 | 0x0L << 2;}
  { MemoryUnit_rwFifo_sizeUDC__io_strideInc.values[0] = T196;}
  { MemoryUnit_rwFifo_sizeUDC__incval.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_rwFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_rwFifo_sizeUDC__incval.values[0] = MemoryUnit_rwFifo_sizeUDC__incval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_sizeUDC__io_max.values[0] = 0x10L;}

  T197 = MemoryUnit_rwFifo_sizeUDC__io_max.values[0]<MemoryUnit_rwFifo_sizeUDC__incval.values[0];
  { MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0] = T197;}

  { T198 = ~MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0];}
  T198 = T198 & 0x1L;
  { MemoryUnit_rwFifo__writeEn.values[0] = MemoryUnit_rwFifo__io_enqVld.values[0] & T198;}
  { MemoryUnit_rwFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_rwFifo__writeEn.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_enable.values[0];}

  { T199 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0] = T199;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_enable.values[0];}

  { T200 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T200;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T201 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T201;}

  { T202 = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T202;}

  { T203 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__d.values[0] = T203;}
  { MemoryUnit_rwFifo_wptr__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_rwFifo_wptr__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T204.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T205 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__isMax.values[0], T205, MemoryUnit_rwFifo_wptr_CounterRC_1_counter__newval.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T206 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next.values[0]);}

  { T207 = T206;}
  T207 = T207 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T207;}

  { T208 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T208;}

  { T209 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___d.values[0] = T209;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T210.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_rwFifo_wptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T211 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T211;}

  { T212 = MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T212;}

  { T213 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__d.values[0] = T213;}
  { MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_rwFifo_wptr__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T214.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T215 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_counter__isMax.values[0], T215, MemoryUnit_rwFifo_wptr_CounterRC_counter__newval.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T216 = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_rwFifo_wptr_CounterRC_counter__next.values[0]);}

  { T217 = T216;}
  T217 = T217 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T217;}

  { T218 = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T218;}

  { T219 = TERNARY_1(MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___d.values[0] = T219;}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__reset.values[0];}
  { T220.values[0] = TERNARY(MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_rwFifo_wptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T221 = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T221;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}

  { T222 = MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_rwFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_control_waitOut.values[0] = T222;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;

  { T223 = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__next.values[0];}
  T223 = T223 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T223;}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_1_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_data_next.values[0];}

  { T224 = MemoryUnit_rwFifo_wptr_CounterRC_counter__next.values[0];}
  T224 = T224 & 0xfL;
  { MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_next.values[0] = T224;}
  { MemoryUnit_rwFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr__io_data_0_next.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr__io_control_1_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_rwFifo_wptr__io_control_0_done.values[0] = MemoryUnit_rwFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}

  { T225 = TERNARY(MemoryUnit_rwFifo__config__chainRead.values[0], 0x1L, 0x2L);}

  { T226 = T225 | 0x0L << 2;}
  { MemoryUnit_rwFifo_sizeUDC__io_strideDec.values[0] = T226;}
  { MemoryUnit_rwFifo_sizeUDC__decval.values[0] = MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_rwFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_rwFifo_sizeUDC__decval.values[0] = MemoryUnit_rwFifo_sizeUDC__decval.values[0] & 0x1fL;
  { MemoryUnit_rwFifo_sizeUDC__io_inc.values[0] = MemoryUnit_rwFifo__writeEn.values[0];}

  { T227 = TERNARY_1(MemoryUnit_rwFifo_sizeUDC__io_inc.values[0], MemoryUnit_rwFifo_sizeUDC__incval.values[0], MemoryUnit_rwFifo_sizeUDC__decval.values[0]);}
  { MemoryUnit_rwFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_rwFifo_sizeUDC__io_init.values[0] = 0x0L;}

  { T228 = TERNARY(MemoryUnit_rwFifo_sizeUDC__io_init.values[0], MemoryUnit_rwFifo_sizeUDC__io_initval.values[0], T227);}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_data_in.values[0] = T228;}
  { MemoryUnit_rwFifo_sizeUDC__io_dec.values[0] = MemoryUnit_rwFifo__readEn.values[0];}

  { T229 = MemoryUnit_rwFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_rwFifo_sizeUDC__io_dec.values[0];}

  { T230 = T229 | MemoryUnit_rwFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___io_control_enable.values[0] = T230;}

  { T231 = TERNARY_1(MemoryUnit_rwFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_rwFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_rwFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_rwFifo_sizeUDC_reg___d.values[0] = T231;}
  { MemoryUnit_rwFifo_sizeUDC__reset.values[0] = MemoryUnit_rwFifo__reset.values[0];}
  { MemoryUnit_rwFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_rwFifo_sizeUDC__reset.values[0];}
  { T232.values[0] = TERNARY(MemoryUnit_rwFifo_sizeUDC_reg___reset.values[0], MemoryUnit_rwFifo_sizeUDC_reg___io_data_init.values[0], MemoryUnit_rwFifo_sizeUDC_reg___d.values[0]);}

  T233 = 0x0L<MemoryUnit_rwFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_rwFifo_sizeUDC__io_gtz.values[0] = T233;}
  { MemoryUnit_rwFifo__io_deq_1.values[0] = MemoryUnit_rwFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_rwFifo__io_full.values[0] = MemoryUnit_rwFifo_sizeUDC__io_isMax.values[0];}
  { MemoryUnit_rwFifo__io_empty.values[0] = MemoryUnit_rwFifo__empty.values[0];}
  { MemoryUnit_rwFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}

  { T234 = MemoryUnit_rwFifo__io_config_data.values[0] | 0x0L << 1;}

  { T235 = 0x0L-T234;}
  T235 = T235 & 0x3L;

  T236 = (T235 >> 0) & 1;
  { MemoryUnit_rwFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}

  { T237 = TERNARY_1(MemoryUnit_rwFifo__io_config_enable.values[0], T236, MemoryUnit_rwFifo__config__chainRead.values[0]);}
  { MemoryUnit_rwFifo__configIn_chainRead.values[0] = T237;}
  { T238.values[0] = TERNARY(MemoryUnit_rwFifo__reset.values[0], 0x1L, MemoryUnit_rwFifo__configIn_chainRead.values[0]);}

  T239 = (T235 >> 1) & 1;

  { T240 = TERNARY_1(MemoryUnit_rwFifo__io_config_enable.values[0], T239, MemoryUnit_rwFifo__config__chainWrite.values[0]);}
  { MemoryUnit_rwFifo__configIn_chainWrite.values[0] = T240;}
  { T241.values[0] = TERNARY(MemoryUnit_rwFifo__reset.values[0], 0x1L, MemoryUnit_rwFifo__configIn_chainWrite.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_sizeFifo_FF__io_data_init.values[0] = MemoryUnit_sizeFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__count.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T242 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__count.values[0];}
  T242 = T242 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_out.values[0] = T242;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_0_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T243 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__count.values[0]+T243;}
  MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T244 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T245 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax.values[0] = T245<=MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval.values[0];
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax.values[0], T244, MemoryUnit_sizeFifo_rptr_CounterRC_counter__newval.values[0]);}

  { T246 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__next.values[0];}
  T246 = T246 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_next.values[0] = T246;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_0_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_data_next.values[0];}

  { T247 = ~MemoryUnit_sizeFifo__empty.values[0];}
  T247 = T247 & 0x1L;
  { MemoryUnit_sizeFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo__readEn.values[0] = MemoryUnit_sizeFifo__io_deqVld.values[0] & T247;}

  { T248 = TERNARY_1(MemoryUnit_sizeFifo__readEn.values[0], MemoryUnit_sizeFifo_rptr__io_data_0_next.values[0], MemoryUnit_sizeFifo_rptr__io_data_0_out.values[0]);}

  T249 = (T248 >> 0) & 1;
  { MemoryUnit_sizeFifo_FF__io_data_in.values[0] = T249;}
  { MemoryUnit_sizeFifo_FF__io_control_enable.values[0] = 0x1L;}

  { T250 = TERNARY(MemoryUnit_sizeFifo_FF__io_control_enable.values[0], MemoryUnit_sizeFifo_FF__io_data_in.values[0], MemoryUnit_sizeFifo_FF__ff.values[0]);}
  { MemoryUnit_sizeFifo_FF__d.values[0] = T250;}
  { MemoryUnit_sizeFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_sizeFifo_FF__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { T251.values[0] = TERNARY_1(MemoryUnit_sizeFifo_FF__reset.values[0], MemoryUnit_sizeFifo_FF__io_data_init.values[0], MemoryUnit_sizeFifo_FF__d.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T252 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count.values[0];}
  T252 = T252 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T252;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T253 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count.values[0]+T253;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T254 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T255 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax.values[0] = T255<=MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax.values[0], T254, MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__newval.values[0]);}

  { T256 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next.values[0];}
  T256 = T256 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T256;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_sizeFifo__readEn.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_enable.values[0];}

  { T257 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_rptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0] = T257;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_0_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0];}

  { T258 = TERNARY_1(MemoryUnit_sizeFifo_rptr__io_control_0_done.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}
  { MemoryUnit_sizeFifo__nextHeadLocalAddr.values[0] = TERNARY_1(MemoryUnit_sizeFifo__config__chainRead.values[0], T258, MemoryUnit_sizeFifo_rptr__io_data_1_next.values[0]);}

  { T259 = TERNARY_1(MemoryUnit_sizeFifo__readEn.values[0], MemoryUnit_sizeFifo__nextHeadLocalAddr.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}

  { T260 = T259;}
  T260 = T260 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM_1__io_raddr.values[0] = T260;}
  { MemoryUnit_sizeFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_size.values[0];}
  { MemoryUnit_sizeFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_size.values[0];}

  { T261 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], MemoryUnit_sizeFifo__io_enq_0.values[0], MemoryUnit_sizeFifo__io_enq_1.values[0]);}
  { MemoryUnit_sizeFifo_SRAM_1__io_wdata.values[0] = T261;}

  { T262 = ~MemoryUnit__config__scatterGather.values[0];}
  T262 = T262 & 0x1L;

  { T263 = MemoryUnit__io_interconnect_vldIn.values[0] & T262;}
  { MemoryUnit_sizeFifo__io_enqVld.values[0] = T263;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__count.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T264 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__count.values[0];}
  T264 = T264 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_out.values[0] = T264;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_data_out.values[0];}

  T265 = MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] == 0x1L;

  { T266 = MemoryUnit_sizeFifo__io_enqVld.values[0] & T265;}

  { T267 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], T266, MemoryUnit_sizeFifo__io_enqVld.values[0]);}
  { MemoryUnit_sizeFifo_SRAM_1__io_wen.values[0] = T267;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T268 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count.values[0];}
  T268 = T268 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T268;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_out.values[0];}

  { T269 = MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0];}
  T269 = T269 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM_1__io_waddr.values[0] = T269;}

  { T270 = TERNARY_1(MemoryUnit_sizeFifo__readEn.values[0], MemoryUnit_sizeFifo__nextHeadLocalAddr.values[0], MemoryUnit_sizeFifo_rptr__io_data_1_out.values[0]);}

  { T271 = T270;}
  T271 = T271 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM__io_raddr.values[0] = T271;}
  { MemoryUnit_sizeFifo_SRAM__io_wdata.values[0] = MemoryUnit_sizeFifo__io_enq_0.values[0];}

  T272 = MemoryUnit_sizeFifo_wptr__io_data_0_out.values[0] == 0x0L;

  { T273 = MemoryUnit_sizeFifo__io_enqVld.values[0] & T272;}

  { T274 = TERNARY_1(MemoryUnit_sizeFifo__config__chainWrite.values[0], T273, MemoryUnit_sizeFifo__io_enqVld.values[0]);}
  { MemoryUnit_sizeFifo_SRAM__io_wen.values[0] = T274;}

  { T275 = MemoryUnit_sizeFifo_wptr__io_data_1_out.values[0];}
  T275 = T275 & 0x7L;
  { MemoryUnit_sizeFifo_SRAM__io_waddr.values[0] = T275;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_enable.values[0];}

  { T276 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T276;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T277 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T277;}

  { T278 = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T278;}

  { T279 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__d.values[0] = T279;}
  { MemoryUnit_sizeFifo_rptr__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_sizeFifo_rptr__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T280.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T281 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__next.values[0]);}

  { T282 = T281;}
  T282 = T282 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T282;}

  { T283 = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T283;}

  { T284 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___d.values[0] = T284;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T285.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T286 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T286;}

  { T287 = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T287;}

  { T288 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__d.values[0] = T288;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_sizeFifo_rptr__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T289.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T290 = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_rptr_CounterRC_counter__next.values[0]);}

  { T291 = T290;}
  T291 = T291 & 0xfL;
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T291;}

  { T292 = MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T292;}

  { T293 = TERNARY_1(MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___d.values[0] = T293;}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_counter__reset.values[0];}
  { T294.values[0] = TERNARY(MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_sizeFifo_rptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T295 = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T295;}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}

  { T296 = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_rptr_CounterRC__io_control_waitOut.values[0] = T296;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_rptr__io_control_1_done.values[0] = MemoryUnit_sizeFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T297 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count.values[0]+T297;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T298 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax.values[0] = T298<=MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T299 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__count.values[0]+T299;}
  MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T300 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax.values[0] = T300<=MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval.values[0];

  { T301 = TERNARY(MemoryUnit_sizeFifo__config__chainWrite.values[0], 0x1L, 0x2L);}

  { T302 = T301 | 0x0L << 2;}
  { MemoryUnit_sizeFifo_sizeUDC__io_strideInc.values[0] = T302;}
  { MemoryUnit_sizeFifo_sizeUDC__incval.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_sizeFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_sizeFifo_sizeUDC__incval.values[0] = MemoryUnit_sizeFifo_sizeUDC__incval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_sizeUDC__io_max.values[0] = 0x10L;}

  T303 = MemoryUnit_sizeFifo_sizeUDC__io_max.values[0]<MemoryUnit_sizeFifo_sizeUDC__incval.values[0];
  { MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0] = T303;}

  { T304 = ~MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0];}
  T304 = T304 & 0x1L;
  { MemoryUnit_sizeFifo__writeEn.values[0] = MemoryUnit_sizeFifo__io_enqVld.values[0] & T304;}
  { MemoryUnit_sizeFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_sizeFifo__writeEn.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_enable.values[0];}

  { T305 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0] = T305;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_enable.values[0];}

  { T306 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T306;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T307 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T307;}

  { T308 = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T308;}

  { T309 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__d.values[0] = T309;}
  { MemoryUnit_sizeFifo_wptr__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_sizeFifo_wptr__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T310.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T311 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__isMax.values[0], T311, MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__newval.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T312 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next.values[0]);}

  { T313 = T312;}
  T313 = T313 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T313;}

  { T314 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T314;}

  { T315 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___d.values[0] = T315;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T316.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T317 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T317;}

  { T318 = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T318;}

  { T319 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__d.values[0] = T319;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_sizeFifo_wptr__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T320.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T321 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_counter__isMax.values[0], T321, MemoryUnit_sizeFifo_wptr_CounterRC_counter__newval.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T322 = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_sizeFifo_wptr_CounterRC_counter__next.values[0]);}

  { T323 = T322;}
  T323 = T323 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T323;}

  { T324 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T324;}

  { T325 = TERNARY_1(MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___d.values[0] = T325;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__reset.values[0];}
  { T326.values[0] = TERNARY(MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_sizeFifo_wptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T327 = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T327;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}

  { T328 = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_sizeFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_control_waitOut.values[0] = T328;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;

  { T329 = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__next.values[0];}
  T329 = T329 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T329;}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_1_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_data_next.values[0];}

  { T330 = MemoryUnit_sizeFifo_wptr_CounterRC_counter__next.values[0];}
  T330 = T330 & 0xfL;
  { MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_next.values[0] = T330;}
  { MemoryUnit_sizeFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_data_0_next.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_control_1_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_wptr__io_control_0_done.values[0] = MemoryUnit_sizeFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}

  { T331 = TERNARY(MemoryUnit_sizeFifo__config__chainRead.values[0], 0x1L, 0x2L);}

  { T332 = T331 | 0x0L << 2;}
  { MemoryUnit_sizeFifo_sizeUDC__io_strideDec.values[0] = T332;}
  { MemoryUnit_sizeFifo_sizeUDC__decval.values[0] = MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_sizeFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_sizeFifo_sizeUDC__decval.values[0] = MemoryUnit_sizeFifo_sizeUDC__decval.values[0] & 0x1fL;
  { MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0] = MemoryUnit_sizeFifo__writeEn.values[0];}

  { T333 = TERNARY_1(MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0], MemoryUnit_sizeFifo_sizeUDC__incval.values[0], MemoryUnit_sizeFifo_sizeUDC__decval.values[0]);}
  { MemoryUnit_sizeFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_sizeFifo_sizeUDC__io_init.values[0] = 0x0L;}

  { T334 = TERNARY(MemoryUnit_sizeFifo_sizeUDC__io_init.values[0], MemoryUnit_sizeFifo_sizeUDC__io_initval.values[0], T333);}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_data_in.values[0] = T334;}
  { MemoryUnit_sizeFifo_sizeUDC__io_dec.values[0] = MemoryUnit_sizeFifo__readEn.values[0];}

  { T335 = MemoryUnit_sizeFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_sizeFifo_sizeUDC__io_dec.values[0];}

  { T336 = T335 | MemoryUnit_sizeFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___io_control_enable.values[0] = T336;}

  { T337 = TERNARY_1(MemoryUnit_sizeFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_sizeFifo_sizeUDC_reg___d.values[0] = T337;}
  { MemoryUnit_sizeFifo_sizeUDC__reset.values[0] = MemoryUnit_sizeFifo__reset.values[0];}
  { MemoryUnit_sizeFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_sizeFifo_sizeUDC__reset.values[0];}
  { T338.values[0] = TERNARY(MemoryUnit_sizeFifo_sizeUDC_reg___reset.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___io_data_init.values[0], MemoryUnit_sizeFifo_sizeUDC_reg___d.values[0]);}

  T339 = 0x0L<MemoryUnit_sizeFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_sizeFifo_sizeUDC__io_gtz.values[0] = T339;}
  { MemoryUnit_sizeFifo__io_deq_1.values[0] = MemoryUnit_sizeFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_sizeFifo__io_full.values[0] = MemoryUnit_sizeFifo_sizeUDC__io_isMax.values[0];}
  { MemoryUnit_sizeFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}

  { T340 = MemoryUnit_sizeFifo__io_config_data.values[0] | 0x0L << 1;}

  { T341 = 0x0L-T340;}
  T341 = T341 & 0x3L;

  T342 = (T341 >> 0) & 1;
  { MemoryUnit_sizeFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}

  { T343 = TERNARY_1(MemoryUnit_sizeFifo__io_config_enable.values[0], T342, MemoryUnit_sizeFifo__config__chainRead.values[0]);}
  { MemoryUnit_sizeFifo__configIn_chainRead.values[0] = T343;}
  { T344.values[0] = TERNARY(MemoryUnit_sizeFifo__reset.values[0], 0x1L, MemoryUnit_sizeFifo__configIn_chainRead.values[0]);}

  T345 = (T341 >> 1) & 1;

  { T346 = TERNARY_1(MemoryUnit_sizeFifo__io_config_enable.values[0], T345, MemoryUnit_sizeFifo__config__chainWrite.values[0]);}
  { MemoryUnit_sizeFifo__configIn_chainWrite.values[0] = T346;}
  { T347.values[0] = TERNARY(MemoryUnit_sizeFifo__reset.values[0], 0x1L, MemoryUnit_sizeFifo__configIn_chainWrite.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_FF__io_data_init.values[0] = __r;}
  MemoryUnit_addrFifo_FF__io_data_init.values[0] = MemoryUnit_addrFifo_FF__io_data_init.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__count.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T348 = MemoryUnit_addrFifo_rptr_CounterRC_counter__count.values[0];}
  T348 = T348 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_out.values[0] = T348;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_0_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T349 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__count.values[0]+T349;}
  MemoryUnit_addrFifo_rptr_CounterRC_counter__newval.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T350 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T351 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax.values[0] = T351<=MemoryUnit_addrFifo_rptr_CounterRC_counter__newval.values[0];
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax.values[0], T350, MemoryUnit_addrFifo_rptr_CounterRC_counter__newval.values[0]);}

  { T352 = MemoryUnit_addrFifo_rptr_CounterRC_counter__next.values[0];}
  T352 = T352 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_next.values[0] = T352;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_data_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_0_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___ff.values[0];}
  { MemoryUnit_addrFifo_sizeUDC__io_out.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0];}
  MemoryUnit_addrFifo__empty.values[0] = MemoryUnit_addrFifo_sizeUDC__io_out.values[0] == 0x0L;

  { T353 = ~MemoryUnit_addrFifo__empty.values[0];}
  T353 = T353 & 0x1L;
  { MemoryUnit_addrFifo__io_deqVld.values[0] = MemoryUnit_burstCounter__io_control_done.values[0];}
  { MemoryUnit_addrFifo__readEn.values[0] = MemoryUnit_addrFifo__io_deqVld.values[0] & T353;}

  { T354 = TERNARY_1(MemoryUnit_addrFifo__readEn.values[0], MemoryUnit_addrFifo_rptr__io_data_0_next.values[0], MemoryUnit_addrFifo_rptr__io_data_0_out.values[0]);}

  T355 = (T354 >> 0) & 1;
  { MemoryUnit_addrFifo_FF__io_data_in.values[0] = T355;}
  { MemoryUnit_addrFifo_FF__io_control_enable.values[0] = 0x1L;}

  { T356 = TERNARY(MemoryUnit_addrFifo_FF__io_control_enable.values[0], MemoryUnit_addrFifo_FF__io_data_in.values[0], MemoryUnit_addrFifo_FF__ff.values[0]);}
  { MemoryUnit_addrFifo_FF__d.values[0] = T356;}
  { MemoryUnit_addrFifo__reset.values[0] = reset.values[0];}
  { MemoryUnit_addrFifo_FF__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { T357.values[0] = TERNARY_1(MemoryUnit_addrFifo_FF__reset.values[0], MemoryUnit_addrFifo_FF__io_data_init.values[0], MemoryUnit_addrFifo_FF__d.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T358 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count.values[0];}
  T358 = T358 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_out.values[0] = T358;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_1_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T359 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count.values[0]+T359;}
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T360 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T361 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax.values[0] = T361<=MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax.values[0], T360, MemoryUnit_addrFifo_rptr_CounterRC_1_counter__newval.values[0]);}

  { T362 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next.values[0];}
  T362 = T362 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_next.values[0] = T362;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_data_1_next.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_data_next.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_0_enable.values[0] = MemoryUnit_addrFifo__readEn.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr__io_control_0_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_enable.values[0];}

  { T363 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_rptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0] = T363;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_0_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0];}

  { T364 = TERNARY_1(MemoryUnit_addrFifo_rptr__io_control_0_done.values[0], MemoryUnit_addrFifo_rptr__io_data_1_next.values[0], MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}
  { MemoryUnit_addrFifo__nextHeadLocalAddr.values[0] = TERNARY_1(MemoryUnit_addrFifo__config__chainRead.values[0], T364, MemoryUnit_addrFifo_rptr__io_data_1_next.values[0]);}

  { T365 = TERNARY_1(MemoryUnit_addrFifo__readEn.values[0], MemoryUnit_addrFifo__nextHeadLocalAddr.values[0], MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}

  { T366 = T365;}
  T366 = T366 & 0x7L;
  { MemoryUnit_addrFifo_SRAM_1__io_raddr.values[0] = T366;}
  { MemoryUnit_addrFifo__io_enq_1.values[0] = MemoryUnit__io_interconnect_addr_1.values[0];}
  { MemoryUnit_addrFifo__io_enq_0.values[0] = MemoryUnit__io_interconnect_addr_0.values[0];}

  { T367 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], MemoryUnit_addrFifo__io_enq_0.values[0], MemoryUnit_addrFifo__io_enq_1.values[0]);}
  { MemoryUnit_addrFifo_SRAM_1__io_wdata.values[0] = T367;}
  { MemoryUnit_addrFifo__io_enqVld.values[0] = MemoryUnit__io_interconnect_vldIn.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__count.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T368 = MemoryUnit_addrFifo_wptr_CounterRC_counter__count.values[0];}
  T368 = T368 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_out.values[0] = T368;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_data_out.values[0];}

  T369 = MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] == 0x1L;

  { T370 = MemoryUnit_addrFifo__io_enqVld.values[0] & T369;}

  { T371 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], T370, MemoryUnit_addrFifo__io_enqVld.values[0]);}
  { MemoryUnit_addrFifo_SRAM_1__io_wen.values[0] = T371;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_out.values[0] | 0x0L << 4;}

  { T372 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count.values[0];}
  T372 = T372 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_out.values[0] = T372;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_1_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_out.values[0];}

  { T373 = MemoryUnit_addrFifo_wptr__io_data_1_out.values[0];}
  T373 = T373 & 0x7L;
  { MemoryUnit_addrFifo_SRAM_1__io_waddr.values[0] = T373;}

  { T374 = TERNARY_1(MemoryUnit_addrFifo__readEn.values[0], MemoryUnit_addrFifo__nextHeadLocalAddr.values[0], MemoryUnit_addrFifo_rptr__io_data_1_out.values[0]);}

  { T375 = T374;}
  T375 = T375 & 0x7L;
  { MemoryUnit_addrFifo_SRAM__io_raddr.values[0] = T375;}
  { MemoryUnit_addrFifo_SRAM__io_wdata.values[0] = MemoryUnit_addrFifo__io_enq_0.values[0];}

  T376 = MemoryUnit_addrFifo_wptr__io_data_0_out.values[0] == 0x0L;

  { T377 = MemoryUnit_addrFifo__io_enqVld.values[0] & T376;}

  { T378 = TERNARY_1(MemoryUnit_addrFifo__config__chainWrite.values[0], T377, MemoryUnit_addrFifo__io_enqVld.values[0]);}
  { MemoryUnit_addrFifo_SRAM__io_wen.values[0] = T378;}

  { T379 = MemoryUnit_addrFifo_wptr__io_data_1_out.values[0];}
  T379 = T379 & 0x7L;
  { MemoryUnit_addrFifo_SRAM__io_waddr.values[0] = T379;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_enable.values[0];}

  { T380 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_rptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0] = T380;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T381 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0] = T381;}

  { T382 = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T382;}

  { T383 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__d.values[0] = T383;}
  { MemoryUnit_addrFifo_rptr__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0] = MemoryUnit_addrFifo_rptr__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__reset.values[0];}
  { T384.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T385 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_1_counter__next.values[0]);}

  { T386 = T385;}
  T386 = T386 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0] = T386;}

  { T387 = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T387;}

  { T388 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___d.values[0] = T388;}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__reset.values[0];}
  { T389.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_addrFifo_rptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T390 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_in.values[0] = T390;}

  { T391 = MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0] = T391;}

  { T392 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__d.values[0] = T392;}
  { MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0] = MemoryUnit_addrFifo_rptr__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser__reset.values[0];}
  { T393.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T394 = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_rptr_CounterRC_counter__next.values[0]);}

  { T395 = T394;}
  T395 = T395 & 0xfL;
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_in.values[0] = T395;}

  { T396 = MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0] = T396;}

  { T397 = TERNARY_1(MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___d.values[0] = T397;}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter__reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_counter__reset.values[0];}
  { T398.values[0] = TERNARY(MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_addrFifo_rptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T399 = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitOut.values[0] = T399;}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_waitOut.values[0];}

  { T400 = MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_addrFifo_rptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_rptr_CounterRC__io_control_waitOut.values[0] = T400;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_addrFifo_rptr_CounterRC__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_rptr__io_control_1_done.values[0] = MemoryUnit_addrFifo_rptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] = 0x1L;}

  { T401 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count.values[0]+T401;}
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_max.values[0] = 0x8L;}

  { T402 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax.values[0] = T402<=MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval.values[0];
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_stride.values[0] = 0x1L;}

  { T403 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_stride.values[0] | 0x0L << 4;}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__count.values[0]+T403;}
  MemoryUnit_addrFifo_wptr_CounterRC_counter__newval.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__newval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_max.values[0] = 0x2L;}

  { T404 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_max.values[0] | 0x0L << 4;}
  MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax.values[0] = T404<=MemoryUnit_addrFifo_wptr_CounterRC_counter__newval.values[0];

  { T405 = TERNARY(MemoryUnit_addrFifo__config__chainWrite.values[0], 0x1L, 0x2L);}

  { T406 = T405 | 0x0L << 2;}
  { MemoryUnit_addrFifo_sizeUDC__io_strideInc.values[0] = T406;}
  { MemoryUnit_addrFifo_sizeUDC__incval.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0]+MemoryUnit_addrFifo_sizeUDC__io_strideInc.values[0];}
  MemoryUnit_addrFifo_sizeUDC__incval.values[0] = MemoryUnit_addrFifo_sizeUDC__incval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_sizeUDC__io_max.values[0] = 0x10L;}

  T407 = MemoryUnit_addrFifo_sizeUDC__io_max.values[0]<MemoryUnit_addrFifo_sizeUDC__incval.values[0];
  { MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0] = T407;}

  { T408 = ~MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0];}
  T408 = T408 & 0x1L;
  { MemoryUnit_addrFifo__writeEn.values[0] = MemoryUnit_addrFifo__io_enqVld.values[0] & T408;}
  { MemoryUnit_addrFifo_wptr__io_control_0_enable.values[0] = MemoryUnit_addrFifo__writeEn.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr__io_control_0_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_enable.values[0];}

  { T409 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0] = T409;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_enable.values[0];}

  { T410 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0] & MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0] = T410;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0] & 0x1L;

  { T411 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0] = T411;}

  { T412 = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_in.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0] = T412;}

  { T413 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__d.values[0] = T413;}
  { MemoryUnit_addrFifo_wptr__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0] = MemoryUnit_addrFifo_wptr__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__reset.values[0];}
  { T414.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_init.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__d.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0] = 0x0L;}

  { T415 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter__count.values[0], 0x0L);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next.values[0] = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__isMax.values[0], T415, MemoryUnit_addrFifo_wptr_CounterRC_1_counter__newval.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] = 0x0L;}

  { T416 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next.values[0]);}

  { T417 = T416;}
  T417 = T417 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0] = T417;}

  { T418 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0] = T418;}

  { T419 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___d.values[0] = T419;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__reset.values[0];}
  { T420.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___io_data_init.values[0], MemoryUnit_addrFifo_wptr_CounterRC_1_counter_reg___d.values[0]);}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_isMax.values[0] & 0x1L;
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_done.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0] & 0x1L;

  { T421 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_in.values[0] = T421;}

  { T422 = MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_in.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_rst.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0] = T422;}

  { T423 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__d.values[0] = T423;}
  { MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0] = MemoryUnit_addrFifo_wptr__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser__reset.values[0];}
  { T424.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_init.values[0], MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__d.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_init.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_saturate.values[0] = 0x0L;}

  { T425 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_saturate.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter__count.values[0], 0x0L);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__next.values[0] = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_counter__isMax.values[0], T425, MemoryUnit_addrFifo_wptr_CounterRC_counter__newval.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0] = 0x0L;}

  { T426 = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0], 0x0L, MemoryUnit_addrFifo_wptr_CounterRC_counter__next.values[0]);}

  { T427 = T426;}
  T427 = T427 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_in.values[0] = T427;}

  { T428 = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_reset.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_counter__io_control_enable.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0] = T428;}

  { T429 = TERNARY_1(MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_control_enable.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_in.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___d.values[0] = T429;}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__reset.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___reset.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__reset.values[0];}
  { T430.values[0] = TERNARY(MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___reset.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___io_data_init.values[0], MemoryUnit_addrFifo_wptr_CounterRC_counter_reg___d.values[0]);}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__ff.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_out.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_depulser_r__io_data_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitIn.values[0] = 0x0L;}

  { T431 = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitIn.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_1_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitOut.values[0] = T431;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitIn.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_waitOut.values[0];}

  { T432 = MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitIn.values[0] | MemoryUnit_addrFifo_wptr_CounterRC_depulser__io_out.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_control_waitOut.values[0] = T432;}
  { val_t __r = ptr->__rand_val(); MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] = __r;}
  MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_isMax.values[0] & 0x1L;

  { T433 = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__next.values[0];}
  T433 = T433 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_next.values[0] = T433;}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_1_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_data_next.values[0];}

  { T434 = MemoryUnit_addrFifo_wptr_CounterRC_counter__next.values[0];}
  T434 = T434 & 0xfL;
  { MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_next.values[0] = T434;}
  { MemoryUnit_addrFifo_wptr_CounterRC__io_data_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_counter__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr__io_data_0_next.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_data_next.values[0];}
  { MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1_counter__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr__io_control_1_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC_1__io_control_done.values[0];}
  { MemoryUnit_addrFifo_wptr__io_control_0_done.values[0] = MemoryUnit_addrFifo_wptr_CounterRC__io_control_done.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_init.values[0] = 0x0L;}

  { T435 = TERNARY(MemoryUnit_addrFifo__config__chainRead.values[0], 0x1L, 0x2L);}

  { T436 = T435 | 0x0L << 2;}
  { MemoryUnit_addrFifo_sizeUDC__io_strideDec.values[0] = T436;}
  { MemoryUnit_addrFifo_sizeUDC__decval.values[0] = MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0]-MemoryUnit_addrFifo_sizeUDC__io_strideDec.values[0];}
  MemoryUnit_addrFifo_sizeUDC__decval.values[0] = MemoryUnit_addrFifo_sizeUDC__decval.values[0] & 0x1fL;
  { MemoryUnit_addrFifo_sizeUDC__io_inc.values[0] = MemoryUnit_addrFifo__writeEn.values[0];}

  { T437 = TERNARY_1(MemoryUnit_addrFifo_sizeUDC__io_inc.values[0], MemoryUnit_addrFifo_sizeUDC__incval.values[0], MemoryUnit_addrFifo_sizeUDC__decval.values[0]);}
  { MemoryUnit_addrFifo_sizeUDC__io_initval.values[0] = 0x0L;}
  { MemoryUnit_addrFifo_sizeUDC__io_init.values[0] = 0x0L;}

  { T438 = TERNARY(MemoryUnit_addrFifo_sizeUDC__io_init.values[0], MemoryUnit_addrFifo_sizeUDC__io_initval.values[0], T437);}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_data_in.values[0] = T438;}
  { MemoryUnit_addrFifo_sizeUDC__io_dec.values[0] = MemoryUnit_addrFifo__readEn.values[0];}

  { T439 = MemoryUnit_addrFifo_sizeUDC__io_inc.values[0] ^ MemoryUnit_addrFifo_sizeUDC__io_dec.values[0];}

  { T440 = T439 | MemoryUnit_addrFifo_sizeUDC__io_init.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___io_control_enable.values[0] = T440;}

  { T441 = TERNARY_1(MemoryUnit_addrFifo_sizeUDC_reg___io_control_enable.values[0], MemoryUnit_addrFifo_sizeUDC_reg___io_data_in.values[0], MemoryUnit_addrFifo_sizeUDC_reg___ff.values[0]);}
  { MemoryUnit_addrFifo_sizeUDC_reg___d.values[0] = T441;}
  { MemoryUnit_addrFifo_sizeUDC__reset.values[0] = MemoryUnit_addrFifo__reset.values[0];}
  { MemoryUnit_addrFifo_sizeUDC_reg___reset.values[0] = MemoryUnit_addrFifo_sizeUDC__reset.values[0];}
  { T442.values[0] = TERNARY(MemoryUnit_addrFifo_sizeUDC_reg___reset.values[0], MemoryUnit_addrFifo_sizeUDC_reg___io_data_init.values[0], MemoryUnit_addrFifo_sizeUDC_reg___d.values[0]);}

  T443 = 0x0L<MemoryUnit_addrFifo_sizeUDC_reg___io_data_out.values[0];
  { MemoryUnit_addrFifo_sizeUDC__io_gtz.values[0] = T443;}

  { T444 = MemoryUnit_addrFifo_SRAM_1__mem.get(MemoryUnit_addrFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0] = T444;}
  { MemoryUnit_addrFifo__io_deq_1.values[0] = MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_addrFifo__io_empty.values[0] = MemoryUnit_addrFifo__empty.values[0];}
  { MemoryUnit_addrFifo__io_config_data.values[0] = MemoryUnit__io_config_data.values[0];}

  { T445 = MemoryUnit_addrFifo__io_config_data.values[0] | 0x0L << 1;}

  { T446 = 0x0L-T445;}
  T446 = T446 & 0x3L;

  T447 = (T446 >> 0) & 1;
  { MemoryUnit_addrFifo__io_config_enable.values[0] = MemoryUnit__io_config_enable.values[0];}

  { T448 = TERNARY_1(MemoryUnit_addrFifo__io_config_enable.values[0], T447, MemoryUnit_addrFifo__config__chainRead.values[0]);}
  { MemoryUnit_addrFifo__configIn_chainRead.values[0] = T448;}
  { T449.values[0] = TERNARY(MemoryUnit_addrFifo__reset.values[0], 0x1L, MemoryUnit_addrFifo__configIn_chainRead.values[0]);}

  T450 = (T446 >> 1) & 1;

  { T451 = TERNARY_1(MemoryUnit_addrFifo__io_config_enable.values[0], T450, MemoryUnit_addrFifo__config__chainWrite.values[0]);}
  { MemoryUnit_addrFifo__configIn_chainWrite.values[0] = T451;}
  { T452.values[0] = TERNARY(MemoryUnit_addrFifo__reset.values[0], 0x1L, MemoryUnit_addrFifo__configIn_chainWrite.values[0]);}
  { MemoryUnit_dataFifo__io_full.values[0] = MemoryUnit_dataFifo_sizeUDC__io_isMax.values[0];}

  { T453 = ~MemoryUnit_dataFifo__io_full.values[0];}
  T453 = T453 & 0x1L;
  { MemoryUnit_addrFifo__io_full.values[0] = MemoryUnit_addrFifo_sizeUDC__io_isMax.values[0];}

  { T454 = ~MemoryUnit_addrFifo__io_full.values[0];}
  T454 = T454 & 0x1L;

  { T455 = T454 & T453;}
  { MemoryUnit__io_interconnect_rdyOut.values[0] = T455;}
  { MemoryUnit__io_interconnect_vldOut.values[0] = MemoryUnit__io_dram_vldIn.values[0];}
  { MemoryUnit__io_interconnect_rdata_1.values[0] = MemoryUnit__io_dram_rdata_1.values[0];}
  { MemoryUnit__io_interconnect_rdata_0.values[0] = MemoryUnit__io_dram_rdata_0.values[0];}
  { val_t __r = ptr->__rand_val(); MemoryUnit__io_dram_rdyOut.values[0] = __r;}
  MemoryUnit__io_dram_rdyOut.values[0] = MemoryUnit__io_dram_rdyOut.values[0] & 0x1L;
  { MemoryUnit__io_dram_vldOut.values[0] = MemoryUnit__burstVld.values[0];}

  { T456 = MemoryUnit_dataFifo_SRAM_1__mem.get(MemoryUnit_dataFifo_SRAM_1__raddr_reg.values[0], 0);}
  { MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0] = T456;}
  { MemoryUnit_dataFifo__io_deq_1.values[0] = MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit__io_dram_wdata_1.values[0] = MemoryUnit_dataFifo__io_deq_1.values[0];}

  { T457 = MemoryUnit_dataFifo_SRAM__mem.get(MemoryUnit_dataFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_dataFifo_SRAM__io_rdata.values[0] = T457;}
  { MemoryUnit_dataFifo_MuxN__io_ins_0.values[0] = MemoryUnit_dataFifo_SRAM__io_rdata.values[0];}
  { MemoryUnit_dataFifo_MuxN__io_ins_1.values[0] = MemoryUnit_dataFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_dataFifo_FF__io_data_out.values[0] = MemoryUnit_dataFifo_FF__ff.values[0];}
  { MemoryUnit_dataFifo_MuxN__io_sel.values[0] = MemoryUnit_dataFifo_FF__io_data_out.values[0];}

  { T458 = TERNARY_1(MemoryUnit_dataFifo_MuxN__io_sel.values[0], MemoryUnit_dataFifo_MuxN__io_ins_1.values[0], MemoryUnit_dataFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_dataFifo_MuxN__io_out.values[0] = T458;}
  { MemoryUnit_dataFifo__io_deq_0.values[0] = MemoryUnit_dataFifo_MuxN__io_out.values[0];}
  { MemoryUnit__io_dram_wdata_0.values[0] = MemoryUnit_dataFifo__io_deq_0.values[0];}
  { MemoryUnit__io_dram_isWr.values[0] = MemoryUnit_rwFifo__io_deq_0.values[0];}

  { T459 = MemoryUnit_burstCounter__count.values[0];}
  T459 = T459 & 0xffffL;
  { MemoryUnit_burstCounter__io_data_out.values[0] = T459;}

  { T460 = MemoryUnit_addrFifo_SRAM__mem.get(MemoryUnit_addrFifo_SRAM__raddr_reg.values[0], 0);}
  { MemoryUnit_addrFifo_SRAM__io_rdata.values[0] = T460;}
  { MemoryUnit_addrFifo_MuxN__io_ins_0.values[0] = MemoryUnit_addrFifo_SRAM__io_rdata.values[0];}
  { MemoryUnit_addrFifo_MuxN__io_ins_1.values[0] = MemoryUnit_addrFifo_SRAM_1__io_rdata.values[0];}
  { MemoryUnit_addrFifo_FF__io_data_out.values[0] = MemoryUnit_addrFifo_FF__ff.values[0];}
  { MemoryUnit_addrFifo_MuxN__io_sel.values[0] = MemoryUnit_addrFifo_FF__io_data_out.values[0];}

  { T461 = TERNARY_1(MemoryUnit_addrFifo_MuxN__io_sel.values[0], MemoryUnit_addrFifo_MuxN__io_ins_1.values[0], MemoryUnit_addrFifo_MuxN__io_ins_0.values[0]);}
  { MemoryUnit_addrFifo_MuxN__io_out.values[0] = T461;}
  { MemoryUnit_addrFifo__io_deq_0.values[0] = MemoryUnit_addrFifo_MuxN__io_out.values[0];}
  { MemoryUnit__burstAddrs_0.values[0] = MemoryUnit_addrFifo__io_deq_0.values[0] >> 6;}
  MemoryUnit__burstAddrs_0.values[0] = MemoryUnit__burstAddrs_0.values[0] & 0x3ffL;

  { T462 = MemoryUnit__burstAddrs_0.values[0] | 0x0L << 10;}

  { T463 = T462+MemoryUnit_burstCounter__io_data_out.values[0];}
  T463 = T463 & 0xffffL;
  { MemoryUnit__io_dram_addr.values[0] = T463;}

  { T464 = MemoryUnit_burstTagCounter__count.values[0];}
  T464 = T464 & 0x1fL;
  { MemoryUnit_burstTagCounter__io_data_out.values[0] = T464;}

  { T465 = MemoryUnit_burstTagCounter__io_data_out.values[0] | 0x0L << 5;}

  { T466 = TERNARY_1(MemoryUnit__config__scatterGather.values[0], MemoryUnit__burstAddrs_0.values[0], T465);}

  { T467 = T466 | 0x0L << 10;}
  { MemoryUnit__io_dram_tagOut.values[0] = T467;}

  { T468 = TERNARY_1(MemoryUnit__io_config_enable.values[0], MemoryUnit__io_config_data.values[0], MemoryUnit__config__scatterGather.values[0]);}
  { MemoryUnit__configIn_scatterGather.values[0] = T468;}
  { T469.values[0] = TERNARY(reset.values[0], 0x0L, MemoryUnit__configIn_scatterGather.values[0]);}
}
