module FF_3(input clk, input reset,
    input [9:0] io_data_in,
    input [9:0] io_data_init,
    output[9:0] io_data_out,
    input  io_control_enable
);

  reg [9:0] ff;
  wire[9:0] T1;
  wire[9:0] d;
  wire[9:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module UpDownCtr(input clk, input reset,
    input [9:0] io_initval,
    input [9:0] io_max,
    input [9:0] io_strideInc,
    input [9:0] io_strideDec,
    input  io_init,
    input  io_inc,
    input  io_dec,
    output io_gtz,
    output io_isMax,
    output[9:0] io_out
);

  wire T0;
  wire T1;
  wire[9:0] T2;
  wire[9:0] T3;
  wire[9:0] decval;
  wire[9:0] incval;
  wire T4;
  wire T5;
  wire[9:0] reg__io_data_out;


  assign T0 = T1 | io_init;
  assign T1 = io_inc ^ io_dec;
  assign T2 = io_init ? io_initval : T3;
  assign T3 = io_inc ? incval : decval;
  assign decval = reg__io_data_out - io_strideDec;
  assign incval = reg__io_data_out + io_strideInc;
  assign io_out = reg__io_data_out;
  assign io_isMax = T4;
  assign T4 = io_max < incval;
  assign io_gtz = T5;
  assign T5 = 10'h0 < reg__io_data_out;
  FF_3 reg_(.clk(clk), .reset(reset),
       .io_data_in( T2 ),
       .io_data_init( 10'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module FF_4(input clk, input reset,
    input [5:0] io_data_in,
    input [5:0] io_data_init,
    output[5:0] io_data_out,
    input  io_control_enable
);

  reg [5:0] ff;
  wire[5:0] T1;
  wire[5:0] d;
  wire[5:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module Counter_2(input clk, input reset,
    input [5:0] io_data_max,
    input [5:0] io_data_stride,
    output[5:0] io_data_out,
    output[5:0] io_data_next,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire T0;
  wire[5:0] T4;
  wire[6:0] T1;
  wire[6:0] next;
  wire[6:0] newval;
  wire[6:0] T5;
  wire[6:0] count;
  wire[6:0] T2;
  wire isMax;
  wire[6:0] T6;
  wire T3;
  wire[5:0] T7;
  wire[5:0] T8;
  wire[5:0] reg__io_data_out;


  assign T0 = io_control_reset | io_control_enable;
  assign T4 = T1[5:0];
  assign T1 = io_control_reset ? 7'h0 : next;
  assign next = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 7'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_next = T7;
  assign T7 = next[5:0];
  assign io_data_out = T8;
  assign T8 = count[5:0];
  FF_4 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 6'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module FF_5(input clk, input reset,
    input  io_data_in,
    input  io_data_init,
    output io_data_out,
    input  io_control_enable
);

  reg  ff;
  wire T1;
  wire d;
  wire T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module Depulser(input clk, input reset,
    input  io_in,
    input  io_rst,
    output io_out
);

  wire T0;
  wire T1;
  wire r_io_data_out;


  assign T0 = io_in | io_rst;
  assign T1 = io_rst ? 1'h0 : io_in;
  assign io_out = r_io_data_out;
  FF_5 r(.clk(clk), .reset(reset),
       .io_data_in( T1 ),
       .io_data_init( 1'h0 ),
       .io_data_out( r_io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module CounterRC_0(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [5:0] io_data_max,
    input [5:0] io_data_stride,
    output[5:0] io_data_out,
    output[5:0] io_data_next,
    input  io_control_enable,
    input  io_control_waitIn,
    output io_control_waitOut,
    output io_control_done
    //output io_control_isMax
);

  wire T0;
  wire[5:0] counter_io_data_out;
  wire[5:0] counter_io_data_next;
  wire counter_io_control_done;
  wire depulser_io_out;


`ifndef SYNTHESIS
// synthesis translate_off
//  assign io_control_isMax = {1{$random}};
// synthesis translate_on
`endif
  assign io_control_done = counter_io_control_done;
  assign io_control_waitOut = T0;
  assign T0 = io_control_waitIn | depulser_io_out;
  assign io_data_next = counter_io_data_next;
  assign io_data_out = counter_io_data_out;
  Counter_2 counter(.clk(clk), .reset(reset),
       .io_data_max( 6'h10 ),
       .io_data_stride( 6'h1 ),
       .io_data_out( counter_io_data_out ),
       .io_data_next( counter_io_data_next ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( io_control_enable ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( counter_io_control_done )
  );
  Depulser depulser(.clk(clk), .reset(reset),
       .io_in( counter_io_control_done ),
       //.io_rst(  )
       .io_out( depulser_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign depulser.io_rst = {1{$random}};
// synthesis translate_on
`endif
endmodule

module CounterRC_1(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [5:0] io_data_max,
    input [5:0] io_data_stride,
    output[5:0] io_data_out,
    output[5:0] io_data_next,
    input  io_control_enable,
    input  io_control_waitIn,
    output io_control_waitOut,
    output io_control_done
    //output io_control_isMax
);

  wire T0;
  wire[5:0] counter_io_data_out;
  wire[5:0] counter_io_data_next;
  wire counter_io_control_done;
  wire depulser_io_out;


`ifndef SYNTHESIS
// synthesis translate_off
//  assign io_control_isMax = {1{$random}};
// synthesis translate_on
`endif
  assign io_control_done = counter_io_control_done;
  assign io_control_waitOut = T0;
  assign T0 = io_control_waitIn | depulser_io_out;
  assign io_data_next = counter_io_data_next;
  assign io_data_out = counter_io_data_out;
  Counter_2 counter(.clk(clk), .reset(reset),
       .io_data_max( 6'h20 ),
       .io_data_stride( 6'h1 ),
       .io_data_out( counter_io_data_out ),
       .io_data_next( counter_io_data_next ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( io_control_enable ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( counter_io_control_done )
  );
  Depulser depulser(.clk(clk), .reset(reset),
       .io_in( counter_io_control_done ),
       //.io_rst(  )
       .io_out( depulser_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign depulser.io_rst = {1{$random}};
// synthesis translate_on
`endif
endmodule

module CounterChain_0(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [5:0] io_data_1_max,
    input [5:0] io_data_1_stride,
    output[5:0] io_data_1_out,
    output[5:0] io_data_1_next,
    input [5:0] io_data_0_max,
    input [5:0] io_data_0_stride,
    output[5:0] io_data_0_out,
    output[5:0] io_data_0_next,
    input  io_control_1_enable,
    output io_control_1_done,
    input  io_control_0_enable,
    output io_control_0_done
);

  wire[5:0] CounterRC_io_data_out;
  wire[5:0] CounterRC_io_data_next;
  wire CounterRC_io_control_done;
  wire[5:0] CounterRC_1_io_data_out;
  wire[5:0] CounterRC_1_io_data_next;
  wire CounterRC_1_io_control_waitOut;
  wire CounterRC_1_io_control_done;


  assign io_control_0_done = CounterRC_io_control_done;
  assign io_control_1_done = CounterRC_1_io_control_done;
  assign io_data_0_next = CounterRC_io_data_next;
  assign io_data_0_out = CounterRC_io_data_out;
  assign io_data_1_next = CounterRC_1_io_data_next;
  assign io_data_1_out = CounterRC_1_io_data_out;
  CounterRC_0 CounterRC(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_data_max( io_data_0_max ),
       .io_data_stride( io_data_0_stride ),
       .io_data_out( CounterRC_io_data_out ),
       .io_data_next( CounterRC_io_data_next ),
       .io_control_enable( io_control_0_enable ),
       .io_control_waitIn( CounterRC_1_io_control_waitOut ),
       //.io_control_waitOut(  )
       .io_control_done( CounterRC_io_control_done )
       //.io_control_isMax(  )
  );
  CounterRC_1 CounterRC_1(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_data_max( io_data_1_max ),
       .io_data_stride( io_data_1_stride ),
       .io_data_out( CounterRC_1_io_data_out ),
       .io_data_next( CounterRC_1_io_data_next ),
       .io_control_enable( CounterRC_io_control_done ),
       .io_control_waitIn( 1'h0 ),
       .io_control_waitOut( CounterRC_1_io_control_waitOut ),
       .io_control_done( CounterRC_1_io_control_done )
       //.io_control_isMax(  )
  );
endmodule

module MuxN_0(
    input [31:0] io_ins_15,
    input [31:0] io_ins_14,
    input [31:0] io_ins_13,
    input [31:0] io_ins_12,
    input [31:0] io_ins_11,
    input [31:0] io_ins_10,
    input [31:0] io_ins_9,
    input [31:0] io_ins_8,
    input [31:0] io_ins_7,
    input [31:0] io_ins_6,
    input [31:0] io_ins_5,
    input [31:0] io_ins_4,
    input [31:0] io_ins_3,
    input [31:0] io_ins_2,
    input [31:0] io_ins_1,
    input [31:0] io_ins_0,
    input [3:0] io_sel,
    output[31:0] io_out
);

  wire[31:0] T0;
  wire[31:0] T1;
  wire[31:0] T2;
  wire[31:0] T3;
  wire T4;
  wire[3:0] T5;
  wire[31:0] T6;
  wire T7;
  wire T8;
  wire[31:0] T9;
  wire[31:0] T10;
  wire T11;
  wire[31:0] T12;
  wire T13;
  wire T14;
  wire T15;
  wire[31:0] T16;
  wire[31:0] T17;
  wire[31:0] T18;
  wire T19;
  wire[31:0] T20;
  wire T21;
  wire T22;
  wire[31:0] T23;
  wire[31:0] T24;
  wire T25;
  wire[31:0] T26;
  wire T27;
  wire T28;
  wire T29;
  wire T30;


  assign io_out = T0;
  assign T0 = T30 ? T16 : T1;
  assign T1 = T15 ? T9 : T2;
  assign T2 = T8 ? T6 : T3;
  assign T3 = T4 ? io_ins_1 : io_ins_0;
  assign T4 = T5[0];
  assign T5 = io_sel;
  assign T6 = T7 ? io_ins_3 : io_ins_2;
  assign T7 = T5[0];
  assign T8 = T5[1];
  assign T9 = T14 ? T12 : T10;
  assign T10 = T11 ? io_ins_5 : io_ins_4;
  assign T11 = T5[0];
  assign T12 = T13 ? io_ins_7 : io_ins_6;
  assign T13 = T5[0];
  assign T14 = T5[1];
  assign T15 = T5[2];
  assign T16 = T29 ? T23 : T17;
  assign T17 = T22 ? T20 : T18;
  assign T18 = T19 ? io_ins_9 : io_ins_8;
  assign T19 = T5[0];
  assign T20 = T21 ? io_ins_11 : io_ins_10;
  assign T21 = T5[0];
  assign T22 = T5[1];
  assign T23 = T28 ? T26 : T24;
  assign T24 = T25 ? io_ins_13 : io_ins_12;
  assign T25 = T5[0];
  assign T26 = T27 ? io_ins_15 : io_ins_14;
  assign T27 = T5[0];
  assign T28 = T5[1];
  assign T29 = T5[2];
  assign T30 = T5[3];
endmodule

module FF_0(input clk, input reset,
    input [3:0] io_data_in,
    input [3:0] io_data_init,
    output[3:0] io_data_out,
    input  io_control_enable
);

  reg [3:0] ff;
  wire[3:0] T1;
  wire[3:0] d;
  wire[3:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module FIFO_0(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [31:0] io_enq_15,
    input [31:0] io_enq_14,
    input [31:0] io_enq_13,
    input [31:0] io_enq_12,
    input [31:0] io_enq_11,
    input [31:0] io_enq_10,
    input [31:0] io_enq_9,
    input [31:0] io_enq_8,
    input [31:0] io_enq_7,
    input [31:0] io_enq_6,
    input [31:0] io_enq_5,
    input [31:0] io_enq_4,
    input [31:0] io_enq_3,
    input [31:0] io_enq_2,
    input [31:0] io_enq_1,
    input [31:0] io_enq_0,
    input  io_enqVld,
    output[31:0] io_deq_15,
    output[31:0] io_deq_14,
    output[31:0] io_deq_13,
    output[31:0] io_deq_12,
    output[31:0] io_deq_11,
    output[31:0] io_deq_10,
    output[31:0] io_deq_9,
    output[31:0] io_deq_8,
    output[31:0] io_deq_7,
    output[31:0] io_deq_6,
    output[31:0] io_deq_5,
    output[31:0] io_deq_4,
    output[31:0] io_deq_3,
    output[31:0] io_deq_2,
    output[31:0] io_deq_1,
    output[31:0] io_deq_0,
    input  io_deqVld,
    output io_full,
    output io_empty
);

  wire[3:0] T93;
  wire[5:0] T0;
  wire readEn;
  wire T1;
  wire empty;
  wire[3:0] T2;
  reg  config__chainRead;
  wire T94;
  wire configIn_chainRead;
  wire T3;
  wire T4;
  wire[1:0] T5;
  wire[1:0] T95;
  wire[31:0] T6;
  reg  config__chainWrite;
  wire T96;
  wire configIn_chainWrite;
  wire T7;
  wire T8;
  wire[4:0] T97;
  wire T9;
  wire T10;
  wire T11;
  wire[4:0] T98;
  wire[5:0] T12;
  wire[5:0] nextHeadLocalAddr;
  wire[5:0] T13;
  wire[31:0] T14;
  wire[4:0] T99;
  wire T15;
  wire T16;
  wire T17;
  wire[4:0] T100;
  wire[5:0] T18;
  wire[31:0] T19;
  wire[4:0] T101;
  wire T20;
  wire T21;
  wire T22;
  wire[4:0] T102;
  wire[5:0] T23;
  wire[31:0] T24;
  wire[4:0] T103;
  wire T25;
  wire T26;
  wire T27;
  wire[4:0] T104;
  wire[5:0] T28;
  wire[31:0] T29;
  wire[4:0] T105;
  wire T30;
  wire T31;
  wire T32;
  wire[4:0] T106;
  wire[5:0] T33;
  wire[31:0] T34;
  wire[4:0] T107;
  wire T35;
  wire T36;
  wire T37;
  wire[4:0] T108;
  wire[5:0] T38;
  wire[31:0] T39;
  wire[4:0] T109;
  wire T40;
  wire T41;
  wire T42;
  wire[4:0] T110;
  wire[5:0] T43;
  wire[31:0] T44;
  wire[4:0] T111;
  wire T45;
  wire T46;
  wire T47;
  wire[4:0] T112;
  wire[5:0] T48;
  wire[31:0] T49;
  wire[4:0] T113;
  wire T50;
  wire T51;
  wire T52;
  wire[4:0] T114;
  wire[5:0] T53;
  wire[31:0] T54;
  wire[4:0] T115;
  wire T55;
  wire T56;
  wire T57;
  wire[4:0] T116;
  wire[5:0] T58;
  wire[31:0] T59;
  wire[4:0] T117;
  wire T60;
  wire T61;
  wire T62;
  wire[4:0] T118;
  wire[5:0] T63;
  wire[31:0] T64;
  wire[4:0] T119;
  wire T65;
  wire T66;
  wire T67;
  wire[4:0] T120;
  wire[5:0] T68;
  wire[31:0] T69;
  wire[4:0] T121;
  wire T70;
  wire T71;
  wire T72;
  wire[4:0] T122;
  wire[5:0] T73;
  wire[31:0] T74;
  wire[4:0] T123;
  wire T75;
  wire T76;
  wire T77;
  wire[4:0] T124;
  wire[5:0] T78;
  wire[31:0] T79;
  wire[4:0] T125;
  wire T80;
  wire T81;
  wire T82;
  wire[4:0] T126;
  wire[5:0] T83;
  wire[4:0] T127;
  wire T84;
  wire T85;
  wire T86;
  wire[4:0] T128;
  wire[5:0] T87;
  wire T88;
  wire T89;
  wire writeEn;
  wire T90;
  wire[9:0] T129;
  wire[4:0] T91;
  wire[9:0] T130;
  wire[4:0] T92;
  wire[31:0] SRAM_io_rdata;
  wire[31:0] SRAM_1_io_rdata;
  wire[31:0] SRAM_2_io_rdata;
  wire[31:0] SRAM_3_io_rdata;
  wire[31:0] SRAM_4_io_rdata;
  wire[31:0] SRAM_5_io_rdata;
  wire[31:0] SRAM_6_io_rdata;
  wire[31:0] SRAM_7_io_rdata;
  wire[31:0] SRAM_8_io_rdata;
  wire[31:0] SRAM_9_io_rdata;
  wire[31:0] SRAM_10_io_rdata;
  wire[31:0] SRAM_11_io_rdata;
  wire[31:0] SRAM_12_io_rdata;
  wire[31:0] SRAM_13_io_rdata;
  wire[31:0] SRAM_14_io_rdata;
  wire[31:0] SRAM_15_io_rdata;
  wire[31:0] MuxN_io_out;
  wire[3:0] FF_io_data_out;
  wire sizeUDC_io_isMax;
  wire[9:0] sizeUDC_io_out;
  wire[5:0] wptr_io_data_1_out;
  wire[5:0] wptr_io_data_0_out;
  wire[5:0] rptr_io_data_1_out;
  wire[5:0] rptr_io_data_1_next;
  wire[5:0] rptr_io_data_0_out;
  wire[5:0] rptr_io_data_0_next;
  wire rptr_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chainRead = {1{$random}};
    config__chainWrite = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T93 = T0[3:0];
  assign T0 = readEn ? rptr_io_data_0_next : rptr_io_data_0_out;
  assign readEn = io_deqVld & T1;
  assign T1 = ~ empty;
  assign empty = sizeUDC_io_out == 10'h0;
  assign T2 = config__chainRead ? FF_io_data_out : 4'h0;
  assign T94 = reset ? 1'h1 : configIn_chainRead;
  assign configIn_chainRead = T3;
  assign T3 = io_config_enable ? T4 : config__chainRead;
  assign T4 = T5[0];
  assign T5 = 2'h0 - T95;
  assign T95 = {1'h0, io_config_data};
  assign T6 = config__chainWrite ? io_enq_0 : io_enq_15;
  assign T96 = reset ? 1'h1 : configIn_chainWrite;
  assign configIn_chainWrite = T7;
  assign T7 = io_config_enable ? T8 : config__chainWrite;
  assign T8 = T5[1];
  assign T97 = wptr_io_data_1_out[4:0];
  assign T9 = config__chainWrite ? T10 : io_enqVld;
  assign T10 = io_enqVld & T11;
  assign T11 = wptr_io_data_0_out == 6'hf;
  assign T98 = T12[4:0];
  assign T12 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign nextHeadLocalAddr = config__chainRead ? T13 : rptr_io_data_1_next;
  assign T13 = rptr_io_control_0_done ? rptr_io_data_1_next : rptr_io_data_1_out;
  assign T14 = config__chainWrite ? io_enq_0 : io_enq_14;
  assign T99 = wptr_io_data_1_out[4:0];
  assign T15 = config__chainWrite ? T16 : io_enqVld;
  assign T16 = io_enqVld & T17;
  assign T17 = wptr_io_data_0_out == 6'he;
  assign T100 = T18[4:0];
  assign T18 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T19 = config__chainWrite ? io_enq_0 : io_enq_13;
  assign T101 = wptr_io_data_1_out[4:0];
  assign T20 = config__chainWrite ? T21 : io_enqVld;
  assign T21 = io_enqVld & T22;
  assign T22 = wptr_io_data_0_out == 6'hd;
  assign T102 = T23[4:0];
  assign T23 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T24 = config__chainWrite ? io_enq_0 : io_enq_12;
  assign T103 = wptr_io_data_1_out[4:0];
  assign T25 = config__chainWrite ? T26 : io_enqVld;
  assign T26 = io_enqVld & T27;
  assign T27 = wptr_io_data_0_out == 6'hc;
  assign T104 = T28[4:0];
  assign T28 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T29 = config__chainWrite ? io_enq_0 : io_enq_11;
  assign T105 = wptr_io_data_1_out[4:0];
  assign T30 = config__chainWrite ? T31 : io_enqVld;
  assign T31 = io_enqVld & T32;
  assign T32 = wptr_io_data_0_out == 6'hb;
  assign T106 = T33[4:0];
  assign T33 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T34 = config__chainWrite ? io_enq_0 : io_enq_10;
  assign T107 = wptr_io_data_1_out[4:0];
  assign T35 = config__chainWrite ? T36 : io_enqVld;
  assign T36 = io_enqVld & T37;
  assign T37 = wptr_io_data_0_out == 6'ha;
  assign T108 = T38[4:0];
  assign T38 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T39 = config__chainWrite ? io_enq_0 : io_enq_9;
  assign T109 = wptr_io_data_1_out[4:0];
  assign T40 = config__chainWrite ? T41 : io_enqVld;
  assign T41 = io_enqVld & T42;
  assign T42 = wptr_io_data_0_out == 6'h9;
  assign T110 = T43[4:0];
  assign T43 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T44 = config__chainWrite ? io_enq_0 : io_enq_8;
  assign T111 = wptr_io_data_1_out[4:0];
  assign T45 = config__chainWrite ? T46 : io_enqVld;
  assign T46 = io_enqVld & T47;
  assign T47 = wptr_io_data_0_out == 6'h8;
  assign T112 = T48[4:0];
  assign T48 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T49 = config__chainWrite ? io_enq_0 : io_enq_7;
  assign T113 = wptr_io_data_1_out[4:0];
  assign T50 = config__chainWrite ? T51 : io_enqVld;
  assign T51 = io_enqVld & T52;
  assign T52 = wptr_io_data_0_out == 6'h7;
  assign T114 = T53[4:0];
  assign T53 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T54 = config__chainWrite ? io_enq_0 : io_enq_6;
  assign T115 = wptr_io_data_1_out[4:0];
  assign T55 = config__chainWrite ? T56 : io_enqVld;
  assign T56 = io_enqVld & T57;
  assign T57 = wptr_io_data_0_out == 6'h6;
  assign T116 = T58[4:0];
  assign T58 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T59 = config__chainWrite ? io_enq_0 : io_enq_5;
  assign T117 = wptr_io_data_1_out[4:0];
  assign T60 = config__chainWrite ? T61 : io_enqVld;
  assign T61 = io_enqVld & T62;
  assign T62 = wptr_io_data_0_out == 6'h5;
  assign T118 = T63[4:0];
  assign T63 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T64 = config__chainWrite ? io_enq_0 : io_enq_4;
  assign T119 = wptr_io_data_1_out[4:0];
  assign T65 = config__chainWrite ? T66 : io_enqVld;
  assign T66 = io_enqVld & T67;
  assign T67 = wptr_io_data_0_out == 6'h4;
  assign T120 = T68[4:0];
  assign T68 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T69 = config__chainWrite ? io_enq_0 : io_enq_3;
  assign T121 = wptr_io_data_1_out[4:0];
  assign T70 = config__chainWrite ? T71 : io_enqVld;
  assign T71 = io_enqVld & T72;
  assign T72 = wptr_io_data_0_out == 6'h3;
  assign T122 = T73[4:0];
  assign T73 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T74 = config__chainWrite ? io_enq_0 : io_enq_2;
  assign T123 = wptr_io_data_1_out[4:0];
  assign T75 = config__chainWrite ? T76 : io_enqVld;
  assign T76 = io_enqVld & T77;
  assign T77 = wptr_io_data_0_out == 6'h2;
  assign T124 = T78[4:0];
  assign T78 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T79 = config__chainWrite ? io_enq_0 : io_enq_1;
  assign T125 = wptr_io_data_1_out[4:0];
  assign T80 = config__chainWrite ? T81 : io_enqVld;
  assign T81 = io_enqVld & T82;
  assign T82 = wptr_io_data_0_out == 6'h1;
  assign T126 = T83[4:0];
  assign T83 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T127 = wptr_io_data_1_out[4:0];
  assign T84 = config__chainWrite ? T85 : io_enqVld;
  assign T85 = io_enqVld & T86;
  assign T86 = wptr_io_data_0_out == 6'h0;
  assign T128 = T87[4:0];
  assign T87 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T88 = readEn & config__chainRead;
  assign T89 = writeEn & config__chainWrite;
  assign writeEn = io_enqVld & T90;
  assign T90 = ~ sizeUDC_io_isMax;
  assign T129 = {5'h0, T91};
  assign T91 = config__chainRead ? 5'h1 : 5'h10;
  assign T130 = {5'h0, T92};
  assign T92 = config__chainWrite ? 5'h1 : 5'h10;
  assign io_empty = empty;
  assign io_full = sizeUDC_io_isMax;
  assign io_deq_0 = MuxN_io_out;
  assign io_deq_1 = SRAM_1_io_rdata;
  assign io_deq_2 = SRAM_2_io_rdata;
  assign io_deq_3 = SRAM_3_io_rdata;
  assign io_deq_4 = SRAM_4_io_rdata;
  assign io_deq_5 = SRAM_5_io_rdata;
  assign io_deq_6 = SRAM_6_io_rdata;
  assign io_deq_7 = SRAM_7_io_rdata;
  assign io_deq_8 = SRAM_8_io_rdata;
  assign io_deq_9 = SRAM_9_io_rdata;
  assign io_deq_10 = SRAM_10_io_rdata;
  assign io_deq_11 = SRAM_11_io_rdata;
  assign io_deq_12 = SRAM_12_io_rdata;
  assign io_deq_13 = SRAM_13_io_rdata;
  assign io_deq_14 = SRAM_14_io_rdata;
  assign io_deq_15 = SRAM_15_io_rdata;
  UpDownCtr sizeUDC(.clk(clk), .reset(reset),
       .io_initval( 10'h0 ),
       .io_max( 10'h200 ),
       .io_strideInc( T130 ),
       .io_strideDec( T129 ),
       .io_init( 1'h0 ),
       .io_inc( writeEn ),
       .io_dec( readEn ),
       //.io_gtz(  )
       .io_isMax( sizeUDC_io_isMax ),
       .io_out( sizeUDC_io_out )
  );
  CounterChain_0 wptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( wptr_io_data_1_out ),
       //.io_data_1_next(  )
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( wptr_io_data_0_out ),
       //.io_data_0_next(  )
       .io_control_1_enable( writeEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T89 )
       //.io_control_0_done(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign wptr.io_config_data = {1{$random}};
    assign wptr.io_config_enable = {1{$random}};
    assign wptr.io_data_1_max = {1{$random}};
    assign wptr.io_data_1_stride = {1{$random}};
    assign wptr.io_data_0_max = {1{$random}};
    assign wptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  CounterChain_0 rptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( rptr_io_data_1_out ),
       .io_data_1_next( rptr_io_data_1_next ),
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( rptr_io_data_0_out ),
       .io_data_0_next( rptr_io_data_0_next ),
       .io_control_1_enable( readEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T88 ),
       .io_control_0_done( rptr_io_control_0_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign rptr.io_config_data = {1{$random}};
    assign rptr.io_config_enable = {1{$random}};
    assign rptr.io_data_1_max = {1{$random}};
    assign rptr.io_data_1_stride = {1{$random}};
    assign rptr.io_data_0_max = {1{$random}};
    assign rptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  SRAM SRAM(.clk(clk),
       .io_raddr( T128 ),
       .io_wen( T84 ),
       .io_waddr( T127 ),
       .io_wdata( io_enq_0 ),
       .io_rdata( SRAM_io_rdata )
  );
  SRAM SRAM_1(.clk(clk),
       .io_raddr( T126 ),
       .io_wen( T80 ),
       .io_waddr( T125 ),
       .io_wdata( T79 ),
       .io_rdata( SRAM_1_io_rdata )
  );
  SRAM SRAM_2(.clk(clk),
       .io_raddr( T124 ),
       .io_wen( T75 ),
       .io_waddr( T123 ),
       .io_wdata( T74 ),
       .io_rdata( SRAM_2_io_rdata )
  );
  SRAM SRAM_3(.clk(clk),
       .io_raddr( T122 ),
       .io_wen( T70 ),
       .io_waddr( T121 ),
       .io_wdata( T69 ),
       .io_rdata( SRAM_3_io_rdata )
  );
  SRAM SRAM_4(.clk(clk),
       .io_raddr( T120 ),
       .io_wen( T65 ),
       .io_waddr( T119 ),
       .io_wdata( T64 ),
       .io_rdata( SRAM_4_io_rdata )
  );
  SRAM SRAM_5(.clk(clk),
       .io_raddr( T118 ),
       .io_wen( T60 ),
       .io_waddr( T117 ),
       .io_wdata( T59 ),
       .io_rdata( SRAM_5_io_rdata )
  );
  SRAM SRAM_6(.clk(clk),
       .io_raddr( T116 ),
       .io_wen( T55 ),
       .io_waddr( T115 ),
       .io_wdata( T54 ),
       .io_rdata( SRAM_6_io_rdata )
  );
  SRAM SRAM_7(.clk(clk),
       .io_raddr( T114 ),
       .io_wen( T50 ),
       .io_waddr( T113 ),
       .io_wdata( T49 ),
       .io_rdata( SRAM_7_io_rdata )
  );
  SRAM SRAM_8(.clk(clk),
       .io_raddr( T112 ),
       .io_wen( T45 ),
       .io_waddr( T111 ),
       .io_wdata( T44 ),
       .io_rdata( SRAM_8_io_rdata )
  );
  SRAM SRAM_9(.clk(clk),
       .io_raddr( T110 ),
       .io_wen( T40 ),
       .io_waddr( T109 ),
       .io_wdata( T39 ),
       .io_rdata( SRAM_9_io_rdata )
  );
  SRAM SRAM_10(.clk(clk),
       .io_raddr( T108 ),
       .io_wen( T35 ),
       .io_waddr( T107 ),
       .io_wdata( T34 ),
       .io_rdata( SRAM_10_io_rdata )
  );
  SRAM SRAM_11(.clk(clk),
       .io_raddr( T106 ),
       .io_wen( T30 ),
       .io_waddr( T105 ),
       .io_wdata( T29 ),
       .io_rdata( SRAM_11_io_rdata )
  );
  SRAM SRAM_12(.clk(clk),
       .io_raddr( T104 ),
       .io_wen( T25 ),
       .io_waddr( T103 ),
       .io_wdata( T24 ),
       .io_rdata( SRAM_12_io_rdata )
  );
  SRAM SRAM_13(.clk(clk),
       .io_raddr( T102 ),
       .io_wen( T20 ),
       .io_waddr( T101 ),
       .io_wdata( T19 ),
       .io_rdata( SRAM_13_io_rdata )
  );
  SRAM SRAM_14(.clk(clk),
       .io_raddr( T100 ),
       .io_wen( T15 ),
       .io_waddr( T99 ),
       .io_wdata( T14 ),
       .io_rdata( SRAM_14_io_rdata )
  );
  SRAM SRAM_15(.clk(clk),
       .io_raddr( T98 ),
       .io_wen( T9 ),
       .io_waddr( T97 ),
       .io_wdata( T6 ),
       .io_rdata( SRAM_15_io_rdata )
  );
  MuxN_0 MuxN(
       .io_ins_15( SRAM_15_io_rdata ),
       .io_ins_14( SRAM_14_io_rdata ),
       .io_ins_13( SRAM_13_io_rdata ),
       .io_ins_12( SRAM_12_io_rdata ),
       .io_ins_11( SRAM_11_io_rdata ),
       .io_ins_10( SRAM_10_io_rdata ),
       .io_ins_9( SRAM_9_io_rdata ),
       .io_ins_8( SRAM_8_io_rdata ),
       .io_ins_7( SRAM_7_io_rdata ),
       .io_ins_6( SRAM_6_io_rdata ),
       .io_ins_5( SRAM_5_io_rdata ),
       .io_ins_4( SRAM_4_io_rdata ),
       .io_ins_3( SRAM_3_io_rdata ),
       .io_ins_2( SRAM_2_io_rdata ),
       .io_ins_1( SRAM_1_io_rdata ),
       .io_ins_0( SRAM_io_rdata ),
       .io_sel( T2 ),
       .io_out( MuxN_io_out )
  );
  FF_0 FF(.clk(clk), .reset(reset),
       .io_data_in( T93 ),
       //.io_data_init(  )
       .io_data_out( FF_io_data_out ),
       .io_control_enable( 1'h1 )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign FF.io_data_init = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__chainRead <= 1'h1;
    end else begin
      config__chainRead <= configIn_chainRead;
    end
    if(reset) begin
      config__chainWrite <= 1'h1;
    end else begin
      config__chainWrite <= configIn_chainWrite;
    end
  end
endmodule

module MuxN_1(
    input  io_ins_15,
    input  io_ins_14,
    input  io_ins_13,
    input  io_ins_12,
    input  io_ins_11,
    input  io_ins_10,
    input  io_ins_9,
    input  io_ins_8,
    input  io_ins_7,
    input  io_ins_6,
    input  io_ins_5,
    input  io_ins_4,
    input  io_ins_3,
    input  io_ins_2,
    input  io_ins_1,
    input  io_ins_0,
    input [3:0] io_sel,
    output io_out
);

  wire T0;
  wire T1;
  wire T2;
  wire T3;
  wire T4;
  wire[3:0] T5;
  wire T6;
  wire T7;
  wire T8;
  wire T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;
  wire T14;
  wire T15;
  wire T16;
  wire T17;
  wire T18;
  wire T19;
  wire T20;
  wire T21;
  wire T22;
  wire T23;
  wire T24;
  wire T25;
  wire T26;
  wire T27;
  wire T28;
  wire T29;
  wire T30;


  assign io_out = T0;
  assign T0 = T30 ? T16 : T1;
  assign T1 = T15 ? T9 : T2;
  assign T2 = T8 ? T6 : T3;
  assign T3 = T4 ? io_ins_1 : io_ins_0;
  assign T4 = T5[0];
  assign T5 = io_sel;
  assign T6 = T7 ? io_ins_3 : io_ins_2;
  assign T7 = T5[0];
  assign T8 = T5[1];
  assign T9 = T14 ? T12 : T10;
  assign T10 = T11 ? io_ins_5 : io_ins_4;
  assign T11 = T5[0];
  assign T12 = T13 ? io_ins_7 : io_ins_6;
  assign T13 = T5[0];
  assign T14 = T5[1];
  assign T15 = T5[2];
  assign T16 = T29 ? T23 : T17;
  assign T17 = T22 ? T20 : T18;
  assign T18 = T19 ? io_ins_9 : io_ins_8;
  assign T19 = T5[0];
  assign T20 = T21 ? io_ins_11 : io_ins_10;
  assign T21 = T5[0];
  assign T22 = T5[1];
  assign T23 = T28 ? T26 : T24;
  assign T24 = T25 ? io_ins_13 : io_ins_12;
  assign T25 = T5[0];
  assign T26 = T27 ? io_ins_15 : io_ins_14;
  assign T27 = T5[0];
  assign T28 = T5[1];
  assign T29 = T5[2];
  assign T30 = T5[3];
endmodule

module FIFO_1(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input  io_enq_15,
    input  io_enq_14,
    input  io_enq_13,
    input  io_enq_12,
    input  io_enq_11,
    input  io_enq_10,
    input  io_enq_9,
    input  io_enq_8,
    input  io_enq_7,
    input  io_enq_6,
    input  io_enq_5,
    input  io_enq_4,
    input  io_enq_3,
    input  io_enq_2,
    input  io_enq_1,
    input  io_enq_0,
    input  io_enqVld,
    output io_deq_15,
    output io_deq_14,
    output io_deq_13,
    output io_deq_12,
    output io_deq_11,
    output io_deq_10,
    output io_deq_9,
    output io_deq_8,
    output io_deq_7,
    output io_deq_6,
    output io_deq_5,
    output io_deq_4,
    output io_deq_3,
    output io_deq_2,
    output io_deq_1,
    output io_deq_0,
    input  io_deqVld,
    output io_full,
    output io_empty
);

  wire[3:0] T93;
  wire[5:0] T0;
  wire readEn;
  wire T1;
  wire empty;
  wire[3:0] T2;
  reg  config__chainRead;
  wire T94;
  wire configIn_chainRead;
  wire T3;
  wire T4;
  wire[1:0] T5;
  wire[1:0] T95;
  wire T6;
  reg  config__chainWrite;
  wire T96;
  wire configIn_chainWrite;
  wire T7;
  wire T8;
  wire[4:0] T97;
  wire T9;
  wire T10;
  wire T11;
  wire[4:0] T98;
  wire[5:0] T12;
  wire[5:0] nextHeadLocalAddr;
  wire[5:0] T13;
  wire T14;
  wire[4:0] T99;
  wire T15;
  wire T16;
  wire T17;
  wire[4:0] T100;
  wire[5:0] T18;
  wire T19;
  wire[4:0] T101;
  wire T20;
  wire T21;
  wire T22;
  wire[4:0] T102;
  wire[5:0] T23;
  wire T24;
  wire[4:0] T103;
  wire T25;
  wire T26;
  wire T27;
  wire[4:0] T104;
  wire[5:0] T28;
  wire T29;
  wire[4:0] T105;
  wire T30;
  wire T31;
  wire T32;
  wire[4:0] T106;
  wire[5:0] T33;
  wire T34;
  wire[4:0] T107;
  wire T35;
  wire T36;
  wire T37;
  wire[4:0] T108;
  wire[5:0] T38;
  wire T39;
  wire[4:0] T109;
  wire T40;
  wire T41;
  wire T42;
  wire[4:0] T110;
  wire[5:0] T43;
  wire T44;
  wire[4:0] T111;
  wire T45;
  wire T46;
  wire T47;
  wire[4:0] T112;
  wire[5:0] T48;
  wire T49;
  wire[4:0] T113;
  wire T50;
  wire T51;
  wire T52;
  wire[4:0] T114;
  wire[5:0] T53;
  wire T54;
  wire[4:0] T115;
  wire T55;
  wire T56;
  wire T57;
  wire[4:0] T116;
  wire[5:0] T58;
  wire T59;
  wire[4:0] T117;
  wire T60;
  wire T61;
  wire T62;
  wire[4:0] T118;
  wire[5:0] T63;
  wire T64;
  wire[4:0] T119;
  wire T65;
  wire T66;
  wire T67;
  wire[4:0] T120;
  wire[5:0] T68;
  wire T69;
  wire[4:0] T121;
  wire T70;
  wire T71;
  wire T72;
  wire[4:0] T122;
  wire[5:0] T73;
  wire T74;
  wire[4:0] T123;
  wire T75;
  wire T76;
  wire T77;
  wire[4:0] T124;
  wire[5:0] T78;
  wire T79;
  wire[4:0] T125;
  wire T80;
  wire T81;
  wire T82;
  wire[4:0] T126;
  wire[5:0] T83;
  wire[4:0] T127;
  wire T84;
  wire T85;
  wire T86;
  wire[4:0] T128;
  wire[5:0] T87;
  wire T88;
  wire T89;
  wire writeEn;
  wire T90;
  wire[9:0] T129;
  wire[4:0] T91;
  wire[9:0] T130;
  wire[4:0] T92;
  wire SRAM_io_rdata;
  wire SRAM_1_io_rdata;
  wire SRAM_2_io_rdata;
  wire SRAM_3_io_rdata;
  wire SRAM_4_io_rdata;
  wire SRAM_5_io_rdata;
  wire SRAM_6_io_rdata;
  wire SRAM_7_io_rdata;
  wire SRAM_8_io_rdata;
  wire SRAM_9_io_rdata;
  wire SRAM_10_io_rdata;
  wire SRAM_11_io_rdata;
  wire SRAM_12_io_rdata;
  wire SRAM_13_io_rdata;
  wire SRAM_14_io_rdata;
  wire SRAM_15_io_rdata;
  wire MuxN_io_out;
  wire[3:0] FF_io_data_out;
  wire sizeUDC_io_isMax;
  wire[9:0] sizeUDC_io_out;
  wire[5:0] wptr_io_data_1_out;
  wire[5:0] wptr_io_data_0_out;
  wire[5:0] rptr_io_data_1_out;
  wire[5:0] rptr_io_data_1_next;
  wire[5:0] rptr_io_data_0_out;
  wire[5:0] rptr_io_data_0_next;
  wire rptr_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chainRead = {1{$random}};
    config__chainWrite = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T93 = T0[3:0];
  assign T0 = readEn ? rptr_io_data_0_next : rptr_io_data_0_out;
  assign readEn = io_deqVld & T1;
  assign T1 = ~ empty;
  assign empty = sizeUDC_io_out == 10'h0;
  assign T2 = config__chainRead ? FF_io_data_out : 4'h0;
  assign T94 = reset ? 1'h1 : configIn_chainRead;
  assign configIn_chainRead = T3;
  assign T3 = io_config_enable ? T4 : config__chainRead;
  assign T4 = T5[0];
  assign T5 = 2'h0 - T95;
  assign T95 = {1'h0, io_config_data};
  assign T6 = config__chainWrite ? io_enq_0 : io_enq_15;
  assign T96 = reset ? 1'h1 : configIn_chainWrite;
  assign configIn_chainWrite = T7;
  assign T7 = io_config_enable ? T8 : config__chainWrite;
  assign T8 = T5[1];
  assign T97 = wptr_io_data_1_out[4:0];
  assign T9 = config__chainWrite ? T10 : io_enqVld;
  assign T10 = io_enqVld & T11;
  assign T11 = wptr_io_data_0_out == 6'hf;
  assign T98 = T12[4:0];
  assign T12 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign nextHeadLocalAddr = config__chainRead ? T13 : rptr_io_data_1_next;
  assign T13 = rptr_io_control_0_done ? rptr_io_data_1_next : rptr_io_data_1_out;
  assign T14 = config__chainWrite ? io_enq_0 : io_enq_14;
  assign T99 = wptr_io_data_1_out[4:0];
  assign T15 = config__chainWrite ? T16 : io_enqVld;
  assign T16 = io_enqVld & T17;
  assign T17 = wptr_io_data_0_out == 6'he;
  assign T100 = T18[4:0];
  assign T18 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T19 = config__chainWrite ? io_enq_0 : io_enq_13;
  assign T101 = wptr_io_data_1_out[4:0];
  assign T20 = config__chainWrite ? T21 : io_enqVld;
  assign T21 = io_enqVld & T22;
  assign T22 = wptr_io_data_0_out == 6'hd;
  assign T102 = T23[4:0];
  assign T23 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T24 = config__chainWrite ? io_enq_0 : io_enq_12;
  assign T103 = wptr_io_data_1_out[4:0];
  assign T25 = config__chainWrite ? T26 : io_enqVld;
  assign T26 = io_enqVld & T27;
  assign T27 = wptr_io_data_0_out == 6'hc;
  assign T104 = T28[4:0];
  assign T28 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T29 = config__chainWrite ? io_enq_0 : io_enq_11;
  assign T105 = wptr_io_data_1_out[4:0];
  assign T30 = config__chainWrite ? T31 : io_enqVld;
  assign T31 = io_enqVld & T32;
  assign T32 = wptr_io_data_0_out == 6'hb;
  assign T106 = T33[4:0];
  assign T33 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T34 = config__chainWrite ? io_enq_0 : io_enq_10;
  assign T107 = wptr_io_data_1_out[4:0];
  assign T35 = config__chainWrite ? T36 : io_enqVld;
  assign T36 = io_enqVld & T37;
  assign T37 = wptr_io_data_0_out == 6'ha;
  assign T108 = T38[4:0];
  assign T38 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T39 = config__chainWrite ? io_enq_0 : io_enq_9;
  assign T109 = wptr_io_data_1_out[4:0];
  assign T40 = config__chainWrite ? T41 : io_enqVld;
  assign T41 = io_enqVld & T42;
  assign T42 = wptr_io_data_0_out == 6'h9;
  assign T110 = T43[4:0];
  assign T43 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T44 = config__chainWrite ? io_enq_0 : io_enq_8;
  assign T111 = wptr_io_data_1_out[4:0];
  assign T45 = config__chainWrite ? T46 : io_enqVld;
  assign T46 = io_enqVld & T47;
  assign T47 = wptr_io_data_0_out == 6'h8;
  assign T112 = T48[4:0];
  assign T48 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T49 = config__chainWrite ? io_enq_0 : io_enq_7;
  assign T113 = wptr_io_data_1_out[4:0];
  assign T50 = config__chainWrite ? T51 : io_enqVld;
  assign T51 = io_enqVld & T52;
  assign T52 = wptr_io_data_0_out == 6'h7;
  assign T114 = T53[4:0];
  assign T53 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T54 = config__chainWrite ? io_enq_0 : io_enq_6;
  assign T115 = wptr_io_data_1_out[4:0];
  assign T55 = config__chainWrite ? T56 : io_enqVld;
  assign T56 = io_enqVld & T57;
  assign T57 = wptr_io_data_0_out == 6'h6;
  assign T116 = T58[4:0];
  assign T58 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T59 = config__chainWrite ? io_enq_0 : io_enq_5;
  assign T117 = wptr_io_data_1_out[4:0];
  assign T60 = config__chainWrite ? T61 : io_enqVld;
  assign T61 = io_enqVld & T62;
  assign T62 = wptr_io_data_0_out == 6'h5;
  assign T118 = T63[4:0];
  assign T63 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T64 = config__chainWrite ? io_enq_0 : io_enq_4;
  assign T119 = wptr_io_data_1_out[4:0];
  assign T65 = config__chainWrite ? T66 : io_enqVld;
  assign T66 = io_enqVld & T67;
  assign T67 = wptr_io_data_0_out == 6'h4;
  assign T120 = T68[4:0];
  assign T68 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T69 = config__chainWrite ? io_enq_0 : io_enq_3;
  assign T121 = wptr_io_data_1_out[4:0];
  assign T70 = config__chainWrite ? T71 : io_enqVld;
  assign T71 = io_enqVld & T72;
  assign T72 = wptr_io_data_0_out == 6'h3;
  assign T122 = T73[4:0];
  assign T73 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T74 = config__chainWrite ? io_enq_0 : io_enq_2;
  assign T123 = wptr_io_data_1_out[4:0];
  assign T75 = config__chainWrite ? T76 : io_enqVld;
  assign T76 = io_enqVld & T77;
  assign T77 = wptr_io_data_0_out == 6'h2;
  assign T124 = T78[4:0];
  assign T78 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T79 = config__chainWrite ? io_enq_0 : io_enq_1;
  assign T125 = wptr_io_data_1_out[4:0];
  assign T80 = config__chainWrite ? T81 : io_enqVld;
  assign T81 = io_enqVld & T82;
  assign T82 = wptr_io_data_0_out == 6'h1;
  assign T126 = T83[4:0];
  assign T83 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T127 = wptr_io_data_1_out[4:0];
  assign T84 = config__chainWrite ? T85 : io_enqVld;
  assign T85 = io_enqVld & T86;
  assign T86 = wptr_io_data_0_out == 6'h0;
  assign T128 = T87[4:0];
  assign T87 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T88 = readEn & config__chainRead;
  assign T89 = writeEn & config__chainWrite;
  assign writeEn = io_enqVld & T90;
  assign T90 = ~ sizeUDC_io_isMax;
  assign T129 = {5'h0, T91};
  assign T91 = config__chainRead ? 5'h1 : 5'h10;
  assign T130 = {5'h0, T92};
  assign T92 = config__chainWrite ? 5'h1 : 5'h10;
  assign io_empty = empty;
  assign io_full = sizeUDC_io_isMax;
  assign io_deq_0 = MuxN_io_out;
  assign io_deq_1 = SRAM_1_io_rdata;
  assign io_deq_2 = SRAM_2_io_rdata;
  assign io_deq_3 = SRAM_3_io_rdata;
  assign io_deq_4 = SRAM_4_io_rdata;
  assign io_deq_5 = SRAM_5_io_rdata;
  assign io_deq_6 = SRAM_6_io_rdata;
  assign io_deq_7 = SRAM_7_io_rdata;
  assign io_deq_8 = SRAM_8_io_rdata;
  assign io_deq_9 = SRAM_9_io_rdata;
  assign io_deq_10 = SRAM_10_io_rdata;
  assign io_deq_11 = SRAM_11_io_rdata;
  assign io_deq_12 = SRAM_12_io_rdata;
  assign io_deq_13 = SRAM_13_io_rdata;
  assign io_deq_14 = SRAM_14_io_rdata;
  assign io_deq_15 = SRAM_15_io_rdata;
  UpDownCtr sizeUDC(.clk(clk), .reset(reset),
       .io_initval( 10'h0 ),
       .io_max( 10'h200 ),
       .io_strideInc( T130 ),
       .io_strideDec( T129 ),
       .io_init( 1'h0 ),
       .io_inc( writeEn ),
       .io_dec( readEn ),
       //.io_gtz(  )
       .io_isMax( sizeUDC_io_isMax ),
       .io_out( sizeUDC_io_out )
  );
  CounterChain_0 wptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( wptr_io_data_1_out ),
       //.io_data_1_next(  )
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( wptr_io_data_0_out ),
       //.io_data_0_next(  )
       .io_control_1_enable( writeEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T89 )
       //.io_control_0_done(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign wptr.io_config_data = {1{$random}};
    assign wptr.io_config_enable = {1{$random}};
    assign wptr.io_data_1_max = {1{$random}};
    assign wptr.io_data_1_stride = {1{$random}};
    assign wptr.io_data_0_max = {1{$random}};
    assign wptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  CounterChain_0 rptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( rptr_io_data_1_out ),
       .io_data_1_next( rptr_io_data_1_next ),
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( rptr_io_data_0_out ),
       .io_data_0_next( rptr_io_data_0_next ),
       .io_control_1_enable( readEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T88 ),
       .io_control_0_done( rptr_io_control_0_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign rptr.io_config_data = {1{$random}};
    assign rptr.io_config_enable = {1{$random}};
    assign rptr.io_data_1_max = {1{$random}};
    assign rptr.io_data_1_stride = {1{$random}};
    assign rptr.io_data_0_max = {1{$random}};
    assign rptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  SRAM SRAM(.clk(clk),
       .io_raddr( T128 ),
       .io_wen( T84 ),
       .io_waddr( T127 ),
       .io_wdata( io_enq_0 ),
       .io_rdata( SRAM_io_rdata )
  );
  SRAM SRAM_1(.clk(clk),
       .io_raddr( T126 ),
       .io_wen( T80 ),
       .io_waddr( T125 ),
       .io_wdata( T79 ),
       .io_rdata( SRAM_1_io_rdata )
  );
  SRAM SRAM_2(.clk(clk),
       .io_raddr( T124 ),
       .io_wen( T75 ),
       .io_waddr( T123 ),
       .io_wdata( T74 ),
       .io_rdata( SRAM_2_io_rdata )
  );
  SRAM SRAM_3(.clk(clk),
       .io_raddr( T122 ),
       .io_wen( T70 ),
       .io_waddr( T121 ),
       .io_wdata( T69 ),
       .io_rdata( SRAM_3_io_rdata )
  );
  SRAM SRAM_4(.clk(clk),
       .io_raddr( T120 ),
       .io_wen( T65 ),
       .io_waddr( T119 ),
       .io_wdata( T64 ),
       .io_rdata( SRAM_4_io_rdata )
  );
  SRAM SRAM_5(.clk(clk),
       .io_raddr( T118 ),
       .io_wen( T60 ),
       .io_waddr( T117 ),
       .io_wdata( T59 ),
       .io_rdata( SRAM_5_io_rdata )
  );
  SRAM SRAM_6(.clk(clk),
       .io_raddr( T116 ),
       .io_wen( T55 ),
       .io_waddr( T115 ),
       .io_wdata( T54 ),
       .io_rdata( SRAM_6_io_rdata )
  );
  SRAM SRAM_7(.clk(clk),
       .io_raddr( T114 ),
       .io_wen( T50 ),
       .io_waddr( T113 ),
       .io_wdata( T49 ),
       .io_rdata( SRAM_7_io_rdata )
  );
  SRAM SRAM_8(.clk(clk),
       .io_raddr( T112 ),
       .io_wen( T45 ),
       .io_waddr( T111 ),
       .io_wdata( T44 ),
       .io_rdata( SRAM_8_io_rdata )
  );
  SRAM SRAM_9(.clk(clk),
       .io_raddr( T110 ),
       .io_wen( T40 ),
       .io_waddr( T109 ),
       .io_wdata( T39 ),
       .io_rdata( SRAM_9_io_rdata )
  );
  SRAM SRAM_10(.clk(clk),
       .io_raddr( T108 ),
       .io_wen( T35 ),
       .io_waddr( T107 ),
       .io_wdata( T34 ),
       .io_rdata( SRAM_10_io_rdata )
  );
  SRAM SRAM_11(.clk(clk),
       .io_raddr( T106 ),
       .io_wen( T30 ),
       .io_waddr( T105 ),
       .io_wdata( T29 ),
       .io_rdata( SRAM_11_io_rdata )
  );
  SRAM SRAM_12(.clk(clk),
       .io_raddr( T104 ),
       .io_wen( T25 ),
       .io_waddr( T103 ),
       .io_wdata( T24 ),
       .io_rdata( SRAM_12_io_rdata )
  );
  SRAM SRAM_13(.clk(clk),
       .io_raddr( T102 ),
       .io_wen( T20 ),
       .io_waddr( T101 ),
       .io_wdata( T19 ),
       .io_rdata( SRAM_13_io_rdata )
  );
  SRAM SRAM_14(.clk(clk),
       .io_raddr( T100 ),
       .io_wen( T15 ),
       .io_waddr( T99 ),
       .io_wdata( T14 ),
       .io_rdata( SRAM_14_io_rdata )
  );
  SRAM SRAM_15(.clk(clk),
       .io_raddr( T98 ),
       .io_wen( T9 ),
       .io_waddr( T97 ),
       .io_wdata( T6 ),
       .io_rdata( SRAM_15_io_rdata )
  );
  MuxN_1 MuxN(
       .io_ins_15( SRAM_15_io_rdata ),
       .io_ins_14( SRAM_14_io_rdata ),
       .io_ins_13( SRAM_13_io_rdata ),
       .io_ins_12( SRAM_12_io_rdata ),
       .io_ins_11( SRAM_11_io_rdata ),
       .io_ins_10( SRAM_10_io_rdata ),
       .io_ins_9( SRAM_9_io_rdata ),
       .io_ins_8( SRAM_8_io_rdata ),
       .io_ins_7( SRAM_7_io_rdata ),
       .io_ins_6( SRAM_6_io_rdata ),
       .io_ins_5( SRAM_5_io_rdata ),
       .io_ins_4( SRAM_4_io_rdata ),
       .io_ins_3( SRAM_3_io_rdata ),
       .io_ins_2( SRAM_2_io_rdata ),
       .io_ins_1( SRAM_1_io_rdata ),
       .io_ins_0( SRAM_io_rdata ),
       .io_sel( T2 ),
       .io_out( MuxN_io_out )
  );
  FF_0 FF(.clk(clk), .reset(reset),
       .io_data_in( T93 ),
       //.io_data_init(  )
       .io_data_out( FF_io_data_out ),
       .io_control_enable( 1'h1 )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign FF.io_data_init = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__chainRead <= 1'h1;
    end else begin
      config__chainRead <= configIn_chainRead;
    end
    if(reset) begin
      config__chainWrite <= 1'h1;
    end else begin
      config__chainWrite <= configIn_chainWrite;
    end
  end
endmodule

module CounterChain_1(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [5:0] io_data_1_max,
    input [5:0] io_data_1_stride,
    output[5:0] io_data_1_out,
    output[5:0] io_data_1_next,
    input [5:0] io_data_0_max,
    input [5:0] io_data_0_stride,
    output[5:0] io_data_0_out,
    output[5:0] io_data_0_next,
    input  io_control_1_enable,
    output io_control_1_done,
    input  io_control_0_enable,
    output io_control_0_done
);

  wire[5:0] CounterRC_io_data_out;
  wire[5:0] CounterRC_io_data_next;
  wire CounterRC_io_control_done;
  wire[5:0] CounterRC_1_io_data_out;
  wire[5:0] CounterRC_1_io_data_next;
  wire CounterRC_1_io_control_done;


  assign io_control_0_done = CounterRC_io_control_done;
  assign io_control_1_done = CounterRC_1_io_control_done;
  assign io_data_0_next = CounterRC_io_data_next;
  assign io_data_0_out = CounterRC_io_data_out;
  assign io_data_1_next = CounterRC_1_io_data_next;
  assign io_data_1_out = CounterRC_1_io_data_out;
  CounterRC_0 CounterRC(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_data_max( io_data_0_max ),
       .io_data_stride( io_data_0_stride ),
       .io_data_out( CounterRC_io_data_out ),
       .io_data_next( CounterRC_io_data_next ),
       .io_control_enable( io_control_0_enable ),
       .io_control_waitIn( 1'h0 ),
       //.io_control_waitOut(  )
       .io_control_done( CounterRC_io_control_done )
       //.io_control_isMax(  )
  );
  CounterRC_1 CounterRC_1(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_data_max( io_data_1_max ),
       .io_data_stride( io_data_1_stride ),
       .io_data_out( CounterRC_1_io_data_out ),
       .io_data_next( CounterRC_1_io_data_next ),
       .io_control_enable( io_control_1_enable ),
       .io_control_waitIn( 1'h0 ),
       //.io_control_waitOut(  )
       .io_control_done( CounterRC_1_io_control_done )
       //.io_control_isMax(  )
  );
endmodule

module FIFO_2(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [31:0] io_enq_15,
    input [31:0] io_enq_14,
    input [31:0] io_enq_13,
    input [31:0] io_enq_12,
    input [31:0] io_enq_11,
    input [31:0] io_enq_10,
    input [31:0] io_enq_9,
    input [31:0] io_enq_8,
    input [31:0] io_enq_7,
    input [31:0] io_enq_6,
    input [31:0] io_enq_5,
    input [31:0] io_enq_4,
    input [31:0] io_enq_3,
    input [31:0] io_enq_2,
    input [31:0] io_enq_1,
    input [31:0] io_enq_0,
    input  io_enqVld,
    output[31:0] io_deq_15,
    output[31:0] io_deq_14,
    output[31:0] io_deq_13,
    output[31:0] io_deq_12,
    output[31:0] io_deq_11,
    output[31:0] io_deq_10,
    output[31:0] io_deq_9,
    output[31:0] io_deq_8,
    output[31:0] io_deq_7,
    output[31:0] io_deq_6,
    output[31:0] io_deq_5,
    output[31:0] io_deq_4,
    output[31:0] io_deq_3,
    output[31:0] io_deq_2,
    output[31:0] io_deq_1,
    output[31:0] io_deq_0,
    input  io_deqVld,
    output io_full,
    output io_empty
);

  wire[3:0] T93;
  wire[5:0] T0;
  wire readEn;
  wire T1;
  wire empty;
  wire[3:0] T2;
  reg  config__chainRead;
  wire T94;
  wire configIn_chainRead;
  wire T3;
  wire T4;
  wire[1:0] T5;
  wire[1:0] T95;
  wire[31:0] T6;
  reg  config__chainWrite;
  wire T96;
  wire configIn_chainWrite;
  wire T7;
  wire T8;
  wire[4:0] T97;
  wire T9;
  wire T10;
  wire T11;
  wire[4:0] T98;
  wire[5:0] T12;
  wire[5:0] nextHeadLocalAddr;
  wire[5:0] T13;
  wire[31:0] T14;
  wire[4:0] T99;
  wire T15;
  wire T16;
  wire T17;
  wire[4:0] T100;
  wire[5:0] T18;
  wire[31:0] T19;
  wire[4:0] T101;
  wire T20;
  wire T21;
  wire T22;
  wire[4:0] T102;
  wire[5:0] T23;
  wire[31:0] T24;
  wire[4:0] T103;
  wire T25;
  wire T26;
  wire T27;
  wire[4:0] T104;
  wire[5:0] T28;
  wire[31:0] T29;
  wire[4:0] T105;
  wire T30;
  wire T31;
  wire T32;
  wire[4:0] T106;
  wire[5:0] T33;
  wire[31:0] T34;
  wire[4:0] T107;
  wire T35;
  wire T36;
  wire T37;
  wire[4:0] T108;
  wire[5:0] T38;
  wire[31:0] T39;
  wire[4:0] T109;
  wire T40;
  wire T41;
  wire T42;
  wire[4:0] T110;
  wire[5:0] T43;
  wire[31:0] T44;
  wire[4:0] T111;
  wire T45;
  wire T46;
  wire T47;
  wire[4:0] T112;
  wire[5:0] T48;
  wire[31:0] T49;
  wire[4:0] T113;
  wire T50;
  wire T51;
  wire T52;
  wire[4:0] T114;
  wire[5:0] T53;
  wire[31:0] T54;
  wire[4:0] T115;
  wire T55;
  wire T56;
  wire T57;
  wire[4:0] T116;
  wire[5:0] T58;
  wire[31:0] T59;
  wire[4:0] T117;
  wire T60;
  wire T61;
  wire T62;
  wire[4:0] T118;
  wire[5:0] T63;
  wire[31:0] T64;
  wire[4:0] T119;
  wire T65;
  wire T66;
  wire T67;
  wire[4:0] T120;
  wire[5:0] T68;
  wire[31:0] T69;
  wire[4:0] T121;
  wire T70;
  wire T71;
  wire T72;
  wire[4:0] T122;
  wire[5:0] T73;
  wire[31:0] T74;
  wire[4:0] T123;
  wire T75;
  wire T76;
  wire T77;
  wire[4:0] T124;
  wire[5:0] T78;
  wire[31:0] T79;
  wire[4:0] T125;
  wire T80;
  wire T81;
  wire T82;
  wire[4:0] T126;
  wire[5:0] T83;
  wire[4:0] T127;
  wire T84;
  wire T85;
  wire T86;
  wire[4:0] T128;
  wire[5:0] T87;
  wire T88;
  wire T89;
  wire writeEn;
  wire T90;
  wire[9:0] T129;
  wire[4:0] T91;
  wire[9:0] T130;
  wire[4:0] T92;
  wire[31:0] SRAM_io_rdata;
  wire[31:0] SRAM_1_io_rdata;
  wire[31:0] SRAM_2_io_rdata;
  wire[31:0] SRAM_3_io_rdata;
  wire[31:0] SRAM_4_io_rdata;
  wire[31:0] SRAM_5_io_rdata;
  wire[31:0] SRAM_6_io_rdata;
  wire[31:0] SRAM_7_io_rdata;
  wire[31:0] SRAM_8_io_rdata;
  wire[31:0] SRAM_9_io_rdata;
  wire[31:0] SRAM_10_io_rdata;
  wire[31:0] SRAM_11_io_rdata;
  wire[31:0] SRAM_12_io_rdata;
  wire[31:0] SRAM_13_io_rdata;
  wire[31:0] SRAM_14_io_rdata;
  wire[31:0] SRAM_15_io_rdata;
  wire[31:0] MuxN_io_out;
  wire[3:0] FF_io_data_out;
  wire sizeUDC_io_isMax;
  wire[9:0] sizeUDC_io_out;
  wire[5:0] wptr_io_data_1_out;
  wire[5:0] wptr_io_data_0_out;
  wire[5:0] rptr_io_data_1_out;
  wire[5:0] rptr_io_data_1_next;
  wire[5:0] rptr_io_data_0_out;
  wire[5:0] rptr_io_data_0_next;
  wire rptr_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chainRead = {1{$random}};
    config__chainWrite = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T93 = T0[3:0];
  assign T0 = readEn ? rptr_io_data_0_next : rptr_io_data_0_out;
  assign readEn = io_deqVld & T1;
  assign T1 = ~ empty;
  assign empty = sizeUDC_io_out == 10'h0;
  assign T2 = config__chainRead ? FF_io_data_out : 4'h0;
  assign T94 = reset ? 1'h0 : configIn_chainRead;
  assign configIn_chainRead = T3;
  assign T3 = io_config_enable ? T4 : config__chainRead;
  assign T4 = T5[0];
  assign T5 = 2'h0 - T95;
  assign T95 = {1'h0, io_config_data};
  assign T6 = config__chainWrite ? io_enq_0 : io_enq_15;
  assign T96 = reset ? 1'h0 : configIn_chainWrite;
  assign configIn_chainWrite = T7;
  assign T7 = io_config_enable ? T8 : config__chainWrite;
  assign T8 = T5[1];
  assign T97 = wptr_io_data_1_out[4:0];
  assign T9 = config__chainWrite ? T10 : io_enqVld;
  assign T10 = io_enqVld & T11;
  assign T11 = wptr_io_data_0_out == 6'hf;
  assign T98 = T12[4:0];
  assign T12 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign nextHeadLocalAddr = config__chainRead ? T13 : rptr_io_data_1_next;
  assign T13 = rptr_io_control_0_done ? rptr_io_data_1_next : rptr_io_data_1_out;
  assign T14 = config__chainWrite ? io_enq_0 : io_enq_14;
  assign T99 = wptr_io_data_1_out[4:0];
  assign T15 = config__chainWrite ? T16 : io_enqVld;
  assign T16 = io_enqVld & T17;
  assign T17 = wptr_io_data_0_out == 6'he;
  assign T100 = T18[4:0];
  assign T18 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T19 = config__chainWrite ? io_enq_0 : io_enq_13;
  assign T101 = wptr_io_data_1_out[4:0];
  assign T20 = config__chainWrite ? T21 : io_enqVld;
  assign T21 = io_enqVld & T22;
  assign T22 = wptr_io_data_0_out == 6'hd;
  assign T102 = T23[4:0];
  assign T23 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T24 = config__chainWrite ? io_enq_0 : io_enq_12;
  assign T103 = wptr_io_data_1_out[4:0];
  assign T25 = config__chainWrite ? T26 : io_enqVld;
  assign T26 = io_enqVld & T27;
  assign T27 = wptr_io_data_0_out == 6'hc;
  assign T104 = T28[4:0];
  assign T28 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T29 = config__chainWrite ? io_enq_0 : io_enq_11;
  assign T105 = wptr_io_data_1_out[4:0];
  assign T30 = config__chainWrite ? T31 : io_enqVld;
  assign T31 = io_enqVld & T32;
  assign T32 = wptr_io_data_0_out == 6'hb;
  assign T106 = T33[4:0];
  assign T33 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T34 = config__chainWrite ? io_enq_0 : io_enq_10;
  assign T107 = wptr_io_data_1_out[4:0];
  assign T35 = config__chainWrite ? T36 : io_enqVld;
  assign T36 = io_enqVld & T37;
  assign T37 = wptr_io_data_0_out == 6'ha;
  assign T108 = T38[4:0];
  assign T38 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T39 = config__chainWrite ? io_enq_0 : io_enq_9;
  assign T109 = wptr_io_data_1_out[4:0];
  assign T40 = config__chainWrite ? T41 : io_enqVld;
  assign T41 = io_enqVld & T42;
  assign T42 = wptr_io_data_0_out == 6'h9;
  assign T110 = T43[4:0];
  assign T43 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T44 = config__chainWrite ? io_enq_0 : io_enq_8;
  assign T111 = wptr_io_data_1_out[4:0];
  assign T45 = config__chainWrite ? T46 : io_enqVld;
  assign T46 = io_enqVld & T47;
  assign T47 = wptr_io_data_0_out == 6'h8;
  assign T112 = T48[4:0];
  assign T48 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T49 = config__chainWrite ? io_enq_0 : io_enq_7;
  assign T113 = wptr_io_data_1_out[4:0];
  assign T50 = config__chainWrite ? T51 : io_enqVld;
  assign T51 = io_enqVld & T52;
  assign T52 = wptr_io_data_0_out == 6'h7;
  assign T114 = T53[4:0];
  assign T53 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T54 = config__chainWrite ? io_enq_0 : io_enq_6;
  assign T115 = wptr_io_data_1_out[4:0];
  assign T55 = config__chainWrite ? T56 : io_enqVld;
  assign T56 = io_enqVld & T57;
  assign T57 = wptr_io_data_0_out == 6'h6;
  assign T116 = T58[4:0];
  assign T58 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T59 = config__chainWrite ? io_enq_0 : io_enq_5;
  assign T117 = wptr_io_data_1_out[4:0];
  assign T60 = config__chainWrite ? T61 : io_enqVld;
  assign T61 = io_enqVld & T62;
  assign T62 = wptr_io_data_0_out == 6'h5;
  assign T118 = T63[4:0];
  assign T63 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T64 = config__chainWrite ? io_enq_0 : io_enq_4;
  assign T119 = wptr_io_data_1_out[4:0];
  assign T65 = config__chainWrite ? T66 : io_enqVld;
  assign T66 = io_enqVld & T67;
  assign T67 = wptr_io_data_0_out == 6'h4;
  assign T120 = T68[4:0];
  assign T68 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T69 = config__chainWrite ? io_enq_0 : io_enq_3;
  assign T121 = wptr_io_data_1_out[4:0];
  assign T70 = config__chainWrite ? T71 : io_enqVld;
  assign T71 = io_enqVld & T72;
  assign T72 = wptr_io_data_0_out == 6'h3;
  assign T122 = T73[4:0];
  assign T73 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T74 = config__chainWrite ? io_enq_0 : io_enq_2;
  assign T123 = wptr_io_data_1_out[4:0];
  assign T75 = config__chainWrite ? T76 : io_enqVld;
  assign T76 = io_enqVld & T77;
  assign T77 = wptr_io_data_0_out == 6'h2;
  assign T124 = T78[4:0];
  assign T78 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T79 = config__chainWrite ? io_enq_0 : io_enq_1;
  assign T125 = wptr_io_data_1_out[4:0];
  assign T80 = config__chainWrite ? T81 : io_enqVld;
  assign T81 = io_enqVld & T82;
  assign T82 = wptr_io_data_0_out == 6'h1;
  assign T126 = T83[4:0];
  assign T83 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T127 = wptr_io_data_1_out[4:0];
  assign T84 = config__chainWrite ? T85 : io_enqVld;
  assign T85 = io_enqVld & T86;
  assign T86 = wptr_io_data_0_out == 6'h0;
  assign T128 = T87[4:0];
  assign T87 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign T88 = readEn & config__chainRead;
  assign T89 = writeEn & config__chainWrite;
  assign writeEn = io_enqVld & T90;
  assign T90 = ~ sizeUDC_io_isMax;
  assign T129 = {5'h0, T91};
  assign T91 = config__chainRead ? 5'h1 : 5'h10;
  assign T130 = {5'h0, T92};
  assign T92 = config__chainWrite ? 5'h1 : 5'h10;
  assign io_empty = empty;
  assign io_full = sizeUDC_io_isMax;
  assign io_deq_0 = MuxN_io_out;
  assign io_deq_1 = SRAM_1_io_rdata;
  assign io_deq_2 = SRAM_2_io_rdata;
  assign io_deq_3 = SRAM_3_io_rdata;
  assign io_deq_4 = SRAM_4_io_rdata;
  assign io_deq_5 = SRAM_5_io_rdata;
  assign io_deq_6 = SRAM_6_io_rdata;
  assign io_deq_7 = SRAM_7_io_rdata;
  assign io_deq_8 = SRAM_8_io_rdata;
  assign io_deq_9 = SRAM_9_io_rdata;
  assign io_deq_10 = SRAM_10_io_rdata;
  assign io_deq_11 = SRAM_11_io_rdata;
  assign io_deq_12 = SRAM_12_io_rdata;
  assign io_deq_13 = SRAM_13_io_rdata;
  assign io_deq_14 = SRAM_14_io_rdata;
  assign io_deq_15 = SRAM_15_io_rdata;
  UpDownCtr sizeUDC(.clk(clk), .reset(reset),
       .io_initval( 10'h0 ),
       .io_max( 10'h200 ),
       .io_strideInc( T130 ),
       .io_strideDec( T129 ),
       .io_init( 1'h0 ),
       .io_inc( writeEn ),
       .io_dec( readEn ),
       //.io_gtz(  )
       .io_isMax( sizeUDC_io_isMax ),
       .io_out( sizeUDC_io_out )
  );
  CounterChain_1 wptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( wptr_io_data_1_out ),
       //.io_data_1_next(  )
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( wptr_io_data_0_out ),
       //.io_data_0_next(  )
       .io_control_1_enable( writeEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T89 )
       //.io_control_0_done(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign wptr.io_config_data = {1{$random}};
    assign wptr.io_config_enable = {1{$random}};
    assign wptr.io_data_1_max = {1{$random}};
    assign wptr.io_data_1_stride = {1{$random}};
    assign wptr.io_data_0_max = {1{$random}};
    assign wptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  CounterChain_1 rptr(.clk(clk), .reset(reset),
       //.io_config_data(  )
       //.io_config_enable(  )
       //.io_data_1_max(  )
       //.io_data_1_stride(  )
       .io_data_1_out( rptr_io_data_1_out ),
       .io_data_1_next( rptr_io_data_1_next ),
       //.io_data_0_max(  )
       //.io_data_0_stride(  )
       .io_data_0_out( rptr_io_data_0_out ),
       .io_data_0_next( rptr_io_data_0_next ),
       .io_control_1_enable( readEn ),
       //.io_control_1_done(  )
       .io_control_0_enable( T88 ),
       .io_control_0_done( rptr_io_control_0_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign rptr.io_config_data = {1{$random}};
    assign rptr.io_config_enable = {1{$random}};
    assign rptr.io_data_1_max = {1{$random}};
    assign rptr.io_data_1_stride = {1{$random}};
    assign rptr.io_data_0_max = {1{$random}};
    assign rptr.io_data_0_stride = {1{$random}};
// synthesis translate_on
`endif
  SRAM SRAM(.clk(clk),
       .io_raddr( T128 ),
       .io_wen( T84 ),
       .io_waddr( T127 ),
       .io_wdata( io_enq_0 ),
       .io_rdata( SRAM_io_rdata )
  );
  SRAM SRAM_1(.clk(clk),
       .io_raddr( T126 ),
       .io_wen( T80 ),
       .io_waddr( T125 ),
       .io_wdata( T79 ),
       .io_rdata( SRAM_1_io_rdata )
  );
  SRAM SRAM_2(.clk(clk),
       .io_raddr( T124 ),
       .io_wen( T75 ),
       .io_waddr( T123 ),
       .io_wdata( T74 ),
       .io_rdata( SRAM_2_io_rdata )
  );
  SRAM SRAM_3(.clk(clk),
       .io_raddr( T122 ),
       .io_wen( T70 ),
       .io_waddr( T121 ),
       .io_wdata( T69 ),
       .io_rdata( SRAM_3_io_rdata )
  );
  SRAM SRAM_4(.clk(clk),
       .io_raddr( T120 ),
       .io_wen( T65 ),
       .io_waddr( T119 ),
       .io_wdata( T64 ),
       .io_rdata( SRAM_4_io_rdata )
  );
  SRAM SRAM_5(.clk(clk),
       .io_raddr( T118 ),
       .io_wen( T60 ),
       .io_waddr( T117 ),
       .io_wdata( T59 ),
       .io_rdata( SRAM_5_io_rdata )
  );
  SRAM SRAM_6(.clk(clk),
       .io_raddr( T116 ),
       .io_wen( T55 ),
       .io_waddr( T115 ),
       .io_wdata( T54 ),
       .io_rdata( SRAM_6_io_rdata )
  );
  SRAM SRAM_7(.clk(clk),
       .io_raddr( T114 ),
       .io_wen( T50 ),
       .io_waddr( T113 ),
       .io_wdata( T49 ),
       .io_rdata( SRAM_7_io_rdata )
  );
  SRAM SRAM_8(.clk(clk),
       .io_raddr( T112 ),
       .io_wen( T45 ),
       .io_waddr( T111 ),
       .io_wdata( T44 ),
       .io_rdata( SRAM_8_io_rdata )
  );
  SRAM SRAM_9(.clk(clk),
       .io_raddr( T110 ),
       .io_wen( T40 ),
       .io_waddr( T109 ),
       .io_wdata( T39 ),
       .io_rdata( SRAM_9_io_rdata )
  );
  SRAM SRAM_10(.clk(clk),
       .io_raddr( T108 ),
       .io_wen( T35 ),
       .io_waddr( T107 ),
       .io_wdata( T34 ),
       .io_rdata( SRAM_10_io_rdata )
  );
  SRAM SRAM_11(.clk(clk),
       .io_raddr( T106 ),
       .io_wen( T30 ),
       .io_waddr( T105 ),
       .io_wdata( T29 ),
       .io_rdata( SRAM_11_io_rdata )
  );
  SRAM SRAM_12(.clk(clk),
       .io_raddr( T104 ),
       .io_wen( T25 ),
       .io_waddr( T103 ),
       .io_wdata( T24 ),
       .io_rdata( SRAM_12_io_rdata )
  );
  SRAM SRAM_13(.clk(clk),
       .io_raddr( T102 ),
       .io_wen( T20 ),
       .io_waddr( T101 ),
       .io_wdata( T19 ),
       .io_rdata( SRAM_13_io_rdata )
  );
  SRAM SRAM_14(.clk(clk),
       .io_raddr( T100 ),
       .io_wen( T15 ),
       .io_waddr( T99 ),
       .io_wdata( T14 ),
       .io_rdata( SRAM_14_io_rdata )
  );
  SRAM SRAM_15(.clk(clk),
       .io_raddr( T98 ),
       .io_wen( T9 ),
       .io_waddr( T97 ),
       .io_wdata( T6 ),
       .io_rdata( SRAM_15_io_rdata )
  );
  MuxN_0 MuxN(
       .io_ins_15( SRAM_15_io_rdata ),
       .io_ins_14( SRAM_14_io_rdata ),
       .io_ins_13( SRAM_13_io_rdata ),
       .io_ins_12( SRAM_12_io_rdata ),
       .io_ins_11( SRAM_11_io_rdata ),
       .io_ins_10( SRAM_10_io_rdata ),
       .io_ins_9( SRAM_9_io_rdata ),
       .io_ins_8( SRAM_8_io_rdata ),
       .io_ins_7( SRAM_7_io_rdata ),
       .io_ins_6( SRAM_6_io_rdata ),
       .io_ins_5( SRAM_5_io_rdata ),
       .io_ins_4( SRAM_4_io_rdata ),
       .io_ins_3( SRAM_3_io_rdata ),
       .io_ins_2( SRAM_2_io_rdata ),
       .io_ins_1( SRAM_1_io_rdata ),
       .io_ins_0( SRAM_io_rdata ),
       .io_sel( T2 ),
       .io_out( MuxN_io_out )
  );
  FF_0 FF(.clk(clk), .reset(reset),
       .io_data_in( T93 ),
       //.io_data_init(  )
       .io_data_out( FF_io_data_out ),
       .io_control_enable( 1'h1 )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign FF.io_data_init = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__chainRead <= 1'h0;
    end else begin
      config__chainRead <= configIn_chainRead;
    end
    if(reset) begin
      config__chainWrite <= 1'h0;
    end else begin
      config__chainWrite <= configIn_chainWrite;
    end
  end
endmodule

module FF_1(input clk, input reset,
    input [31:0] io_data_in,
    input [31:0] io_data_init,
    output[31:0] io_data_out,
    input  io_control_enable
);

  reg [31:0] ff;
  wire[31:0] T1;
  wire[31:0] d;
  wire[31:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module Counter_0(input clk, input reset,
    input [31:0] io_data_max,
    input [31:0] io_data_stride,
    output[31:0] io_data_out,
    output[31:0] io_data_next,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire T0;
  wire[31:0] T4;
  wire[32:0] T1;
  wire[32:0] next;
  wire[32:0] newval;
  wire[32:0] T5;
  wire[32:0] count;
  wire[32:0] T2;
  wire isMax;
  wire[32:0] T6;
  wire T3;
  wire[31:0] T7;
  wire[31:0] T8;
  wire[31:0] reg__io_data_out;


  assign T0 = io_control_reset | io_control_enable;
  assign T4 = T1[31:0];
  assign T1 = io_control_reset ? 33'h0 : next;
  assign next = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 33'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_next = T7;
  assign T7 = next[31:0];
  assign io_data_out = T8;
  assign T8 = count[31:0];
  FF_1 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 32'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module FF_2(input clk, input reset,
    input [4:0] io_data_in,
    input [4:0] io_data_init,
    output[4:0] io_data_out,
    input  io_control_enable
);

  reg [4:0] ff;
  wire[4:0] T1;
  wire[4:0] d;
  wire[4:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

module Counter_1(input clk, input reset,
    input [4:0] io_data_max,
    input [4:0] io_data_stride,
    output[4:0] io_data_out,
    output[4:0] io_data_next,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire T0;
  wire[4:0] T4;
  wire[5:0] T1;
  wire[5:0] next;
  wire[5:0] newval;
  wire[5:0] T5;
  wire[5:0] count;
  wire[5:0] T2;
  wire isMax;
  wire[5:0] T6;
  wire T3;
  wire[4:0] T7;
  wire[4:0] T8;
  wire[4:0] reg__io_data_out;


  assign T0 = io_control_reset | io_control_enable;
  assign T4 = T1[4:0];
  assign T1 = io_control_reset ? 6'h0 : next;
  assign next = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 6'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_next = T7;
  assign T7 = next[4:0];
  assign io_data_out = T8;
  assign T8 = count[4:0];
  FF_2 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 5'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module MemoryUnit(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input  io_interconnect_rdyIn,
    output io_interconnect_rdyOut,
    input  io_interconnect_vldIn,
    output io_interconnect_vldOut,
    input [31:0] io_interconnect_wdata_15,
    input [31:0] io_interconnect_wdata_14,
    input [31:0] io_interconnect_wdata_13,
    input [31:0] io_interconnect_wdata_12,
    input [31:0] io_interconnect_wdata_11,
    input [31:0] io_interconnect_wdata_10,
    input [31:0] io_interconnect_wdata_9,
    input [31:0] io_interconnect_wdata_8,
    input [31:0] io_interconnect_wdata_7,
    input [31:0] io_interconnect_wdata_6,
    input [31:0] io_interconnect_wdata_5,
    input [31:0] io_interconnect_wdata_4,
    input [31:0] io_interconnect_wdata_3,
    input [31:0] io_interconnect_wdata_2,
    input [31:0] io_interconnect_wdata_1,
    input [31:0] io_interconnect_wdata_0,
    input  io_interconnect_isWr,
    output[31:0] io_interconnect_rdata_15,
    output[31:0] io_interconnect_rdata_14,
    output[31:0] io_interconnect_rdata_13,
    output[31:0] io_interconnect_rdata_12,
    output[31:0] io_interconnect_rdata_11,
    output[31:0] io_interconnect_rdata_10,
    output[31:0] io_interconnect_rdata_9,
    output[31:0] io_interconnect_rdata_8,
    output[31:0] io_interconnect_rdata_7,
    output[31:0] io_interconnect_rdata_6,
    output[31:0] io_interconnect_rdata_5,
    output[31:0] io_interconnect_rdata_4,
    output[31:0] io_interconnect_rdata_3,
    output[31:0] io_interconnect_rdata_2,
    output[31:0] io_interconnect_rdata_1,
    output[31:0] io_interconnect_rdata_0,
    input [31:0] io_interconnect_addr_15,
    input [31:0] io_interconnect_addr_14,
    input [31:0] io_interconnect_addr_13,
    input [31:0] io_interconnect_addr_12,
    input [31:0] io_interconnect_addr_11,
    input [31:0] io_interconnect_addr_10,
    input [31:0] io_interconnect_addr_9,
    input [31:0] io_interconnect_addr_8,
    input [31:0] io_interconnect_addr_7,
    input [31:0] io_interconnect_addr_6,
    input [31:0] io_interconnect_addr_5,
    input [31:0] io_interconnect_addr_4,
    input [31:0] io_interconnect_addr_3,
    input [31:0] io_interconnect_addr_2,
    input [31:0] io_interconnect_addr_1,
    input [31:0] io_interconnect_addr_0,
    input [31:0] io_interconnect_size,
    input  io_interconnect_dataVldIn,
    //input  io_dram_rdyIn
    //output io_dram_rdyOut
    input  io_dram_vldIn,
    output io_dram_vldOut,
    output[31:0] io_dram_wdata_15,
    output[31:0] io_dram_wdata_14,
    output[31:0] io_dram_wdata_13,
    output[31:0] io_dram_wdata_12,
    output[31:0] io_dram_wdata_11,
    output[31:0] io_dram_wdata_10,
    output[31:0] io_dram_wdata_9,
    output[31:0] io_dram_wdata_8,
    output[31:0] io_dram_wdata_7,
    output[31:0] io_dram_wdata_6,
    output[31:0] io_dram_wdata_5,
    output[31:0] io_dram_wdata_4,
    output[31:0] io_dram_wdata_3,
    output[31:0] io_dram_wdata_2,
    output[31:0] io_dram_wdata_1,
    output[31:0] io_dram_wdata_0,
    output io_dram_isWr,
    input [31:0] io_dram_rdata_15,
    input [31:0] io_dram_rdata_14,
    input [31:0] io_dram_rdata_13,
    input [31:0] io_dram_rdata_12,
    input [31:0] io_dram_rdata_11,
    input [31:0] io_dram_rdata_10,
    input [31:0] io_dram_rdata_9,
    input [31:0] io_dram_rdata_8,
    input [31:0] io_dram_rdata_7,
    input [31:0] io_dram_rdata_6,
    input [31:0] io_dram_rdata_5,
    input [31:0] io_dram_rdata_4,
    input [31:0] io_dram_rdata_3,
    input [31:0] io_dram_rdata_2,
    input [31:0] io_dram_rdata_1,
    input [31:0] io_dram_rdata_0,
    output[31:0] io_dram_addr,
    input [31:0] io_dram_tagIn,
    output[31:0] io_dram_tagOut,
    output[31:0] io_tagInSimOut
);

  wire burstVld;
  wire[31:0] T14;
  wire[25:0] T0;
  wire[25:0] sizeInBursts;
  wire[25:0] T15;
  wire T1;
  wire[5:0] T2;
  wire[25:0] T3;
  reg  config__scatterGather;
  wire T16;
  wire configIn_scatterGather;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire T8;
  wire[31:0] T17;
  wire[25:0] T9;
  wire[25:0] T18;
  wire[25:0] burstAddrs_0;
  wire[31:0] T10;
  wire[31:0] T19;
  wire T11;
  wire T12;
  wire T13;
  wire[31:0] burstCounter_io_data_out;
  wire burstCounter_io_control_done;
  wire[4:0] burstTagCounter_io_data_out;
  wire[31:0] addrFifo_io_deq_0;
  wire addrFifo_io_full;
  wire[31:0] sizeFifo_io_deq_0;
  wire sizeFifo_io_empty;
  wire rwFifo_io_deq_0;
  wire[31:0] dataFifo_io_deq_15;
  wire[31:0] dataFifo_io_deq_14;
  wire[31:0] dataFifo_io_deq_13;
  wire[31:0] dataFifo_io_deq_12;
  wire[31:0] dataFifo_io_deq_11;
  wire[31:0] dataFifo_io_deq_10;
  wire[31:0] dataFifo_io_deq_9;
  wire[31:0] dataFifo_io_deq_8;
  wire[31:0] dataFifo_io_deq_7;
  wire[31:0] dataFifo_io_deq_6;
  wire[31:0] dataFifo_io_deq_5;
  wire[31:0] dataFifo_io_deq_4;
  wire[31:0] dataFifo_io_deq_3;
  wire[31:0] dataFifo_io_deq_2;
  wire[31:0] dataFifo_io_deq_1;
  wire[31:0] dataFifo_io_deq_0;
  wire dataFifo_io_full;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__scatterGather = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
//  assign io_dram_rdyOut = {1{$random}};
// synthesis translate_on
`endif
  assign burstVld = ~ sizeFifo_io_empty;
  assign T14 = {6'h0, T0};
  assign T0 = config__scatterGather ? 26'h1 : sizeInBursts;
  assign sizeInBursts = T3 + T15;
  assign T15 = {25'h0, T1};
  assign T1 = T2 != 6'h0;
  assign T2 = sizeFifo_io_deq_0[5:0];
  assign T3 = sizeFifo_io_deq_0[31:6];
  assign T16 = reset ? 1'h0 : configIn_scatterGather;
  assign configIn_scatterGather = T4;
  assign T4 = io_config_enable ? io_config_data : config__scatterGather;
  assign T5 = T6;
  assign T6 = rwFifo_io_deq_0 & burstVld;
  assign T7 = io_interconnect_vldIn & T8;
  assign T8 = ~ config__scatterGather;
  assign io_tagInSimOut = io_dram_tagIn;
  assign io_dram_tagOut = T17;
  assign T17 = {6'h0, T9};
  assign T9 = config__scatterGather ? burstAddrs_0 : T18;
  assign T18 = {21'h0, burstTagCounter_io_data_out};
  assign burstAddrs_0 = addrFifo_io_deq_0[31:6];
  assign io_dram_addr = T10;
  assign T10 = T19 + burstCounter_io_data_out;
  assign T19 = {6'h0, burstAddrs_0};
  assign io_dram_isWr = rwFifo_io_deq_0;
  assign io_dram_wdata_0 = dataFifo_io_deq_0;
  assign io_dram_wdata_1 = dataFifo_io_deq_1;
  assign io_dram_wdata_2 = dataFifo_io_deq_2;
  assign io_dram_wdata_3 = dataFifo_io_deq_3;
  assign io_dram_wdata_4 = dataFifo_io_deq_4;
  assign io_dram_wdata_5 = dataFifo_io_deq_5;
  assign io_dram_wdata_6 = dataFifo_io_deq_6;
  assign io_dram_wdata_7 = dataFifo_io_deq_7;
  assign io_dram_wdata_8 = dataFifo_io_deq_8;
  assign io_dram_wdata_9 = dataFifo_io_deq_9;
  assign io_dram_wdata_10 = dataFifo_io_deq_10;
  assign io_dram_wdata_11 = dataFifo_io_deq_11;
  assign io_dram_wdata_12 = dataFifo_io_deq_12;
  assign io_dram_wdata_13 = dataFifo_io_deq_13;
  assign io_dram_wdata_14 = dataFifo_io_deq_14;
  assign io_dram_wdata_15 = dataFifo_io_deq_15;
  assign io_dram_vldOut = burstVld;
  assign io_interconnect_rdata_0 = io_dram_rdata_0;
  assign io_interconnect_rdata_1 = io_dram_rdata_1;
  assign io_interconnect_rdata_2 = io_dram_rdata_2;
  assign io_interconnect_rdata_3 = io_dram_rdata_3;
  assign io_interconnect_rdata_4 = io_dram_rdata_4;
  assign io_interconnect_rdata_5 = io_dram_rdata_5;
  assign io_interconnect_rdata_6 = io_dram_rdata_6;
  assign io_interconnect_rdata_7 = io_dram_rdata_7;
  assign io_interconnect_rdata_8 = io_dram_rdata_8;
  assign io_interconnect_rdata_9 = io_dram_rdata_9;
  assign io_interconnect_rdata_10 = io_dram_rdata_10;
  assign io_interconnect_rdata_11 = io_dram_rdata_11;
  assign io_interconnect_rdata_12 = io_dram_rdata_12;
  assign io_interconnect_rdata_13 = io_dram_rdata_13;
  assign io_interconnect_rdata_14 = io_dram_rdata_14;
  assign io_interconnect_rdata_15 = io_dram_rdata_15;
  assign io_interconnect_vldOut = io_dram_vldIn;
  assign io_interconnect_rdyOut = T11;
  assign T11 = T13 & T12;
  assign T12 = ~ dataFifo_io_full;
  assign T13 = ~ addrFifo_io_full;
  FIFO_0 addrFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_15( io_interconnect_addr_15 ),
       .io_enq_14( io_interconnect_addr_14 ),
       .io_enq_13( io_interconnect_addr_13 ),
       .io_enq_12( io_interconnect_addr_12 ),
       .io_enq_11( io_interconnect_addr_11 ),
       .io_enq_10( io_interconnect_addr_10 ),
       .io_enq_9( io_interconnect_addr_9 ),
       .io_enq_8( io_interconnect_addr_8 ),
       .io_enq_7( io_interconnect_addr_7 ),
       .io_enq_6( io_interconnect_addr_6 ),
       .io_enq_5( io_interconnect_addr_5 ),
       .io_enq_4( io_interconnect_addr_4 ),
       .io_enq_3( io_interconnect_addr_3 ),
       .io_enq_2( io_interconnect_addr_2 ),
       .io_enq_1( io_interconnect_addr_1 ),
       .io_enq_0( io_interconnect_addr_0 ),
       .io_enqVld( io_interconnect_vldIn ),
       //.io_deq_15(  )
       //.io_deq_14(  )
       //.io_deq_13(  )
       //.io_deq_12(  )
       //.io_deq_11(  )
       //.io_deq_10(  )
       //.io_deq_9(  )
       //.io_deq_8(  )
       //.io_deq_7(  )
       //.io_deq_6(  )
       //.io_deq_5(  )
       //.io_deq_4(  )
       //.io_deq_3(  )
       //.io_deq_2(  )
       //.io_deq_1(  )
       .io_deq_0( addrFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done ),
       .io_full( addrFifo_io_full )
       //.io_empty(  )
  );
  FIFO_0 sizeFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_15( io_interconnect_size ),
       .io_enq_14( io_interconnect_size ),
       .io_enq_13( io_interconnect_size ),
       .io_enq_12( io_interconnect_size ),
       .io_enq_11( io_interconnect_size ),
       .io_enq_10( io_interconnect_size ),
       .io_enq_9( io_interconnect_size ),
       .io_enq_8( io_interconnect_size ),
       .io_enq_7( io_interconnect_size ),
       .io_enq_6( io_interconnect_size ),
       .io_enq_5( io_interconnect_size ),
       .io_enq_4( io_interconnect_size ),
       .io_enq_3( io_interconnect_size ),
       .io_enq_2( io_interconnect_size ),
       .io_enq_1( io_interconnect_size ),
       .io_enq_0( io_interconnect_size ),
       .io_enqVld( T7 ),
       //.io_deq_15(  )
       //.io_deq_14(  )
       //.io_deq_13(  )
       //.io_deq_12(  )
       //.io_deq_11(  )
       //.io_deq_10(  )
       //.io_deq_9(  )
       //.io_deq_8(  )
       //.io_deq_7(  )
       //.io_deq_6(  )
       //.io_deq_5(  )
       //.io_deq_4(  )
       //.io_deq_3(  )
       //.io_deq_2(  )
       //.io_deq_1(  )
       .io_deq_0( sizeFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done ),
       //.io_full(  )
       .io_empty( sizeFifo_io_empty )
  );
  FIFO_1 rwFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_15( io_interconnect_isWr ),
       .io_enq_14( io_interconnect_isWr ),
       .io_enq_13( io_interconnect_isWr ),
       .io_enq_12( io_interconnect_isWr ),
       .io_enq_11( io_interconnect_isWr ),
       .io_enq_10( io_interconnect_isWr ),
       .io_enq_9( io_interconnect_isWr ),
       .io_enq_8( io_interconnect_isWr ),
       .io_enq_7( io_interconnect_isWr ),
       .io_enq_6( io_interconnect_isWr ),
       .io_enq_5( io_interconnect_isWr ),
       .io_enq_4( io_interconnect_isWr ),
       .io_enq_3( io_interconnect_isWr ),
       .io_enq_2( io_interconnect_isWr ),
       .io_enq_1( io_interconnect_isWr ),
       .io_enq_0( io_interconnect_isWr ),
       .io_enqVld( io_interconnect_vldIn ),
       //.io_deq_15(  )
       //.io_deq_14(  )
       //.io_deq_13(  )
       //.io_deq_12(  )
       //.io_deq_11(  )
       //.io_deq_10(  )
       //.io_deq_9(  )
       //.io_deq_8(  )
       //.io_deq_7(  )
       //.io_deq_6(  )
       //.io_deq_5(  )
       //.io_deq_4(  )
       //.io_deq_3(  )
       //.io_deq_2(  )
       //.io_deq_1(  )
       .io_deq_0( rwFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done )
       //.io_full(  )
       //.io_empty(  )
  );
  FIFO_2 dataFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_15( io_interconnect_wdata_15 ),
       .io_enq_14( io_interconnect_wdata_14 ),
       .io_enq_13( io_interconnect_wdata_13 ),
       .io_enq_12( io_interconnect_wdata_12 ),
       .io_enq_11( io_interconnect_wdata_11 ),
       .io_enq_10( io_interconnect_wdata_10 ),
       .io_enq_9( io_interconnect_wdata_9 ),
       .io_enq_8( io_interconnect_wdata_8 ),
       .io_enq_7( io_interconnect_wdata_7 ),
       .io_enq_6( io_interconnect_wdata_6 ),
       .io_enq_5( io_interconnect_wdata_5 ),
       .io_enq_4( io_interconnect_wdata_4 ),
       .io_enq_3( io_interconnect_wdata_3 ),
       .io_enq_2( io_interconnect_wdata_2 ),
       .io_enq_1( io_interconnect_wdata_1 ),
       .io_enq_0( io_interconnect_wdata_0 ),
       .io_enqVld( io_interconnect_dataVldIn ),
       .io_deq_15( dataFifo_io_deq_15 ),
       .io_deq_14( dataFifo_io_deq_14 ),
       .io_deq_13( dataFifo_io_deq_13 ),
       .io_deq_12( dataFifo_io_deq_12 ),
       .io_deq_11( dataFifo_io_deq_11 ),
       .io_deq_10( dataFifo_io_deq_10 ),
       .io_deq_9( dataFifo_io_deq_9 ),
       .io_deq_8( dataFifo_io_deq_8 ),
       .io_deq_7( dataFifo_io_deq_7 ),
       .io_deq_6( dataFifo_io_deq_6 ),
       .io_deq_5( dataFifo_io_deq_5 ),
       .io_deq_4( dataFifo_io_deq_4 ),
       .io_deq_3( dataFifo_io_deq_3 ),
       .io_deq_2( dataFifo_io_deq_2 ),
       .io_deq_1( dataFifo_io_deq_1 ),
       .io_deq_0( dataFifo_io_deq_0 ),
       .io_deqVld( T5 ),
       .io_full( dataFifo_io_full )
       //.io_empty(  )
  );
  Counter_0 burstCounter(.clk(clk), .reset(reset),
       .io_data_max( T14 ),
       .io_data_stride( 32'h1 ),
       .io_data_out( burstCounter_io_data_out ),
       //.io_data_next(  )
       .io_control_reset( 1'h0 ),
       .io_control_enable( burstVld ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( burstCounter_io_control_done )
  );
  Counter_1 burstTagCounter(.clk(clk), .reset(reset),
       .io_data_max( 5'h10 ),
       .io_data_stride( 5'h1 ),
       .io_data_out( burstTagCounter_io_data_out ),
       //.io_data_next(  )
       .io_control_reset( 1'h0 ),
       .io_control_enable( burstVld )
       //.io_control_saturate(  )
       //.io_control_done(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign burstTagCounter.io_control_saturate = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__scatterGather <= 1'h0;
    end else begin
      config__scatterGather <= configIn_scatterGather;
    end
  end
endmodule

module MemoryTester(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input  io_interconnect_rdyIn,
    output io_interconnect_rdyOut,
    input  io_interconnect_vldIn,
    output io_interconnect_vldOut,
    input [31:0] io_interconnect_wdata_15,
    input [31:0] io_interconnect_wdata_14,
    input [31:0] io_interconnect_wdata_13,
    input [31:0] io_interconnect_wdata_12,
    input [31:0] io_interconnect_wdata_11,
    input [31:0] io_interconnect_wdata_10,
    input [31:0] io_interconnect_wdata_9,
    input [31:0] io_interconnect_wdata_8,
    input [31:0] io_interconnect_wdata_7,
    input [31:0] io_interconnect_wdata_6,
    input [31:0] io_interconnect_wdata_5,
    input [31:0] io_interconnect_wdata_4,
    input [31:0] io_interconnect_wdata_3,
    input [31:0] io_interconnect_wdata_2,
    input [31:0] io_interconnect_wdata_1,
    input [31:0] io_interconnect_wdata_0,
    input  io_interconnect_isWr,
    output[31:0] io_interconnect_rdata_15,
    output[31:0] io_interconnect_rdata_14,
    output[31:0] io_interconnect_rdata_13,
    output[31:0] io_interconnect_rdata_12,
    output[31:0] io_interconnect_rdata_11,
    output[31:0] io_interconnect_rdata_10,
    output[31:0] io_interconnect_rdata_9,
    output[31:0] io_interconnect_rdata_8,
    output[31:0] io_interconnect_rdata_7,
    output[31:0] io_interconnect_rdata_6,
    output[31:0] io_interconnect_rdata_5,
    output[31:0] io_interconnect_rdata_4,
    output[31:0] io_interconnect_rdata_3,
    output[31:0] io_interconnect_rdata_2,
    output[31:0] io_interconnect_rdata_1,
    output[31:0] io_interconnect_rdata_0,
    input [31:0] io_interconnect_addr_15,
    input [31:0] io_interconnect_addr_14,
    input [31:0] io_interconnect_addr_13,
    input [31:0] io_interconnect_addr_12,
    input [31:0] io_interconnect_addr_11,
    input [31:0] io_interconnect_addr_10,
    input [31:0] io_interconnect_addr_9,
    input [31:0] io_interconnect_addr_8,
    input [31:0] io_interconnect_addr_7,
    input [31:0] io_interconnect_addr_6,
    input [31:0] io_interconnect_addr_5,
    input [31:0] io_interconnect_addr_4,
    input [31:0] io_interconnect_addr_3,
    input [31:0] io_interconnect_addr_2,
    input [31:0] io_interconnect_addr_1,
    input [31:0] io_interconnect_addr_0,
    input [31:0] io_interconnect_size,
    input  io_interconnect_dataVldIn,
    input  io_dram_rdyIn,
    output io_dram_rdyOut,
    input  io_dram_vldIn,
    output io_dram_vldOut,
    output[31:0] io_dram_wdata_15,
    output[31:0] io_dram_wdata_14,
    output[31:0] io_dram_wdata_13,
    output[31:0] io_dram_wdata_12,
    output[31:0] io_dram_wdata_11,
    output[31:0] io_dram_wdata_10,
    output[31:0] io_dram_wdata_9,
    output[31:0] io_dram_wdata_8,
    output[31:0] io_dram_wdata_7,
    output[31:0] io_dram_wdata_6,
    output[31:0] io_dram_wdata_5,
    output[31:0] io_dram_wdata_4,
    output[31:0] io_dram_wdata_3,
    output[31:0] io_dram_wdata_2,
    output[31:0] io_dram_wdata_1,
    output[31:0] io_dram_wdata_0,
    output io_dram_isWr,
    input [31:0] io_dram_rdata_15,
    input [31:0] io_dram_rdata_14,
    input [31:0] io_dram_rdata_13,
    input [31:0] io_dram_rdata_12,
    input [31:0] io_dram_rdata_11,
    input [31:0] io_dram_rdata_10,
    input [31:0] io_dram_rdata_9,
    input [31:0] io_dram_rdata_8,
    input [31:0] io_dram_rdata_7,
    input [31:0] io_dram_rdata_6,
    input [31:0] io_dram_rdata_5,
    input [31:0] io_dram_rdata_4,
    input [31:0] io_dram_rdata_3,
    input [31:0] io_dram_rdata_2,
    input [31:0] io_dram_rdata_1,
    input [31:0] io_dram_rdata_0,
    output[31:0] io_dram_addr,
    input [31:0] io_dram_tagIn,
    output[31:0] io_dram_tagOut,
    input [31:0] io_tagInSimIn
);

  wire DRAMSimulator_io_vldOut;
  wire[31:0] DRAMSimulator_io_rdata_15;
  wire[31:0] DRAMSimulator_io_rdata_14;
  wire[31:0] DRAMSimulator_io_rdata_13;
  wire[31:0] DRAMSimulator_io_rdata_12;
  wire[31:0] DRAMSimulator_io_rdata_11;
  wire[31:0] DRAMSimulator_io_rdata_10;
  wire[31:0] DRAMSimulator_io_rdata_9;
  wire[31:0] DRAMSimulator_io_rdata_8;
  wire[31:0] DRAMSimulator_io_rdata_7;
  wire[31:0] DRAMSimulator_io_rdata_6;
  wire[31:0] DRAMSimulator_io_rdata_5;
  wire[31:0] DRAMSimulator_io_rdata_4;
  wire[31:0] DRAMSimulator_io_rdata_3;
  wire[31:0] DRAMSimulator_io_rdata_2;
  wire[31:0] DRAMSimulator_io_rdata_1;
  wire[31:0] DRAMSimulator_io_rdata_0;
  wire[31:0] DRAMSimulator_io_tagOut;
  wire mu_io_interconnect_rdyOut;
  wire mu_io_interconnect_vldOut;
  wire[31:0] mu_io_interconnect_rdata_15;
  wire[31:0] mu_io_interconnect_rdata_14;
  wire[31:0] mu_io_interconnect_rdata_13;
  wire[31:0] mu_io_interconnect_rdata_12;
  wire[31:0] mu_io_interconnect_rdata_11;
  wire[31:0] mu_io_interconnect_rdata_10;
  wire[31:0] mu_io_interconnect_rdata_9;
  wire[31:0] mu_io_interconnect_rdata_8;
  wire[31:0] mu_io_interconnect_rdata_7;
  wire[31:0] mu_io_interconnect_rdata_6;
  wire[31:0] mu_io_interconnect_rdata_5;
  wire[31:0] mu_io_interconnect_rdata_4;
  wire[31:0] mu_io_interconnect_rdata_3;
  wire[31:0] mu_io_interconnect_rdata_2;
  wire[31:0] mu_io_interconnect_rdata_1;
  wire[31:0] mu_io_interconnect_rdata_0;
  wire mu_io_dram_vldOut;
  wire[31:0] mu_io_dram_wdata_15;
  wire[31:0] mu_io_dram_wdata_14;
  wire[31:0] mu_io_dram_wdata_13;
  wire[31:0] mu_io_dram_wdata_12;
  wire[31:0] mu_io_dram_wdata_11;
  wire[31:0] mu_io_dram_wdata_10;
  wire[31:0] mu_io_dram_wdata_9;
  wire[31:0] mu_io_dram_wdata_8;
  wire[31:0] mu_io_dram_wdata_7;
  wire[31:0] mu_io_dram_wdata_6;
  wire[31:0] mu_io_dram_wdata_5;
  wire[31:0] mu_io_dram_wdata_4;
  wire[31:0] mu_io_dram_wdata_3;
  wire[31:0] mu_io_dram_wdata_2;
  wire[31:0] mu_io_dram_wdata_1;
  wire[31:0] mu_io_dram_wdata_0;
  wire mu_io_dram_isWr;
  wire[31:0] mu_io_dram_addr;
  wire[31:0] mu_io_dram_tagOut;


`ifndef SYNTHESIS
// synthesis translate_off
//  assign io_dram_rdyOut = {1{$random}};
// synthesis translate_on
`endif
  assign io_dram_tagOut = mu_io_dram_tagOut;
  assign io_dram_addr = mu_io_dram_addr;
  assign io_dram_isWr = mu_io_dram_isWr;
  assign io_dram_wdata_0 = mu_io_dram_wdata_0;
  assign io_dram_wdata_1 = mu_io_dram_wdata_1;
  assign io_dram_wdata_2 = mu_io_dram_wdata_2;
  assign io_dram_wdata_3 = mu_io_dram_wdata_3;
  assign io_dram_wdata_4 = mu_io_dram_wdata_4;
  assign io_dram_wdata_5 = mu_io_dram_wdata_5;
  assign io_dram_wdata_6 = mu_io_dram_wdata_6;
  assign io_dram_wdata_7 = mu_io_dram_wdata_7;
  assign io_dram_wdata_8 = mu_io_dram_wdata_8;
  assign io_dram_wdata_9 = mu_io_dram_wdata_9;
  assign io_dram_wdata_10 = mu_io_dram_wdata_10;
  assign io_dram_wdata_11 = mu_io_dram_wdata_11;
  assign io_dram_wdata_12 = mu_io_dram_wdata_12;
  assign io_dram_wdata_13 = mu_io_dram_wdata_13;
  assign io_dram_wdata_14 = mu_io_dram_wdata_14;
  assign io_dram_wdata_15 = mu_io_dram_wdata_15;
  assign io_dram_vldOut = mu_io_dram_vldOut;
  assign io_interconnect_rdata_0 = mu_io_interconnect_rdata_0;
  assign io_interconnect_rdata_1 = mu_io_interconnect_rdata_1;
  assign io_interconnect_rdata_2 = mu_io_interconnect_rdata_2;
  assign io_interconnect_rdata_3 = mu_io_interconnect_rdata_3;
  assign io_interconnect_rdata_4 = mu_io_interconnect_rdata_4;
  assign io_interconnect_rdata_5 = mu_io_interconnect_rdata_5;
  assign io_interconnect_rdata_6 = mu_io_interconnect_rdata_6;
  assign io_interconnect_rdata_7 = mu_io_interconnect_rdata_7;
  assign io_interconnect_rdata_8 = mu_io_interconnect_rdata_8;
  assign io_interconnect_rdata_9 = mu_io_interconnect_rdata_9;
  assign io_interconnect_rdata_10 = mu_io_interconnect_rdata_10;
  assign io_interconnect_rdata_11 = mu_io_interconnect_rdata_11;
  assign io_interconnect_rdata_12 = mu_io_interconnect_rdata_12;
  assign io_interconnect_rdata_13 = mu_io_interconnect_rdata_13;
  assign io_interconnect_rdata_14 = mu_io_interconnect_rdata_14;
  assign io_interconnect_rdata_15 = mu_io_interconnect_rdata_15;
  assign io_interconnect_vldOut = mu_io_interconnect_vldOut;
  assign io_interconnect_rdyOut = mu_io_interconnect_rdyOut;
  MemoryUnit mu(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_interconnect_rdyIn( io_interconnect_rdyIn ),
       .io_interconnect_rdyOut( mu_io_interconnect_rdyOut ),
       .io_interconnect_vldIn( io_interconnect_vldIn ),
       .io_interconnect_vldOut( mu_io_interconnect_vldOut ),
       .io_interconnect_wdata_15( io_interconnect_wdata_15 ),
       .io_interconnect_wdata_14( io_interconnect_wdata_14 ),
       .io_interconnect_wdata_13( io_interconnect_wdata_13 ),
       .io_interconnect_wdata_12( io_interconnect_wdata_12 ),
       .io_interconnect_wdata_11( io_interconnect_wdata_11 ),
       .io_interconnect_wdata_10( io_interconnect_wdata_10 ),
       .io_interconnect_wdata_9( io_interconnect_wdata_9 ),
       .io_interconnect_wdata_8( io_interconnect_wdata_8 ),
       .io_interconnect_wdata_7( io_interconnect_wdata_7 ),
       .io_interconnect_wdata_6( io_interconnect_wdata_6 ),
       .io_interconnect_wdata_5( io_interconnect_wdata_5 ),
       .io_interconnect_wdata_4( io_interconnect_wdata_4 ),
       .io_interconnect_wdata_3( io_interconnect_wdata_3 ),
       .io_interconnect_wdata_2( io_interconnect_wdata_2 ),
       .io_interconnect_wdata_1( io_interconnect_wdata_1 ),
       .io_interconnect_wdata_0( io_interconnect_wdata_0 ),
       .io_interconnect_isWr( io_interconnect_isWr ),
       .io_interconnect_rdata_15( mu_io_interconnect_rdata_15 ),
       .io_interconnect_rdata_14( mu_io_interconnect_rdata_14 ),
       .io_interconnect_rdata_13( mu_io_interconnect_rdata_13 ),
       .io_interconnect_rdata_12( mu_io_interconnect_rdata_12 ),
       .io_interconnect_rdata_11( mu_io_interconnect_rdata_11 ),
       .io_interconnect_rdata_10( mu_io_interconnect_rdata_10 ),
       .io_interconnect_rdata_9( mu_io_interconnect_rdata_9 ),
       .io_interconnect_rdata_8( mu_io_interconnect_rdata_8 ),
       .io_interconnect_rdata_7( mu_io_interconnect_rdata_7 ),
       .io_interconnect_rdata_6( mu_io_interconnect_rdata_6 ),
       .io_interconnect_rdata_5( mu_io_interconnect_rdata_5 ),
       .io_interconnect_rdata_4( mu_io_interconnect_rdata_4 ),
       .io_interconnect_rdata_3( mu_io_interconnect_rdata_3 ),
       .io_interconnect_rdata_2( mu_io_interconnect_rdata_2 ),
       .io_interconnect_rdata_1( mu_io_interconnect_rdata_1 ),
       .io_interconnect_rdata_0( mu_io_interconnect_rdata_0 ),
       .io_interconnect_addr_15( io_interconnect_addr_15 ),
       .io_interconnect_addr_14( io_interconnect_addr_14 ),
       .io_interconnect_addr_13( io_interconnect_addr_13 ),
       .io_interconnect_addr_12( io_interconnect_addr_12 ),
       .io_interconnect_addr_11( io_interconnect_addr_11 ),
       .io_interconnect_addr_10( io_interconnect_addr_10 ),
       .io_interconnect_addr_9( io_interconnect_addr_9 ),
       .io_interconnect_addr_8( io_interconnect_addr_8 ),
       .io_interconnect_addr_7( io_interconnect_addr_7 ),
       .io_interconnect_addr_6( io_interconnect_addr_6 ),
       .io_interconnect_addr_5( io_interconnect_addr_5 ),
       .io_interconnect_addr_4( io_interconnect_addr_4 ),
       .io_interconnect_addr_3( io_interconnect_addr_3 ),
       .io_interconnect_addr_2( io_interconnect_addr_2 ),
       .io_interconnect_addr_1( io_interconnect_addr_1 ),
       .io_interconnect_addr_0( io_interconnect_addr_0 ),
       .io_interconnect_size( io_interconnect_size ),
       .io_interconnect_dataVldIn( io_interconnect_dataVldIn ),
       //.io_dram_rdyIn(  )
       //.io_dram_rdyOut(  )
       .io_dram_vldIn( DRAMSimulator_io_vldOut ),
       .io_dram_vldOut( mu_io_dram_vldOut ),
       .io_dram_wdata_15( mu_io_dram_wdata_15 ),
       .io_dram_wdata_14( mu_io_dram_wdata_14 ),
       .io_dram_wdata_13( mu_io_dram_wdata_13 ),
       .io_dram_wdata_12( mu_io_dram_wdata_12 ),
       .io_dram_wdata_11( mu_io_dram_wdata_11 ),
       .io_dram_wdata_10( mu_io_dram_wdata_10 ),
       .io_dram_wdata_9( mu_io_dram_wdata_9 ),
       .io_dram_wdata_8( mu_io_dram_wdata_8 ),
       .io_dram_wdata_7( mu_io_dram_wdata_7 ),
       .io_dram_wdata_6( mu_io_dram_wdata_6 ),
       .io_dram_wdata_5( mu_io_dram_wdata_5 ),
       .io_dram_wdata_4( mu_io_dram_wdata_4 ),
       .io_dram_wdata_3( mu_io_dram_wdata_3 ),
       .io_dram_wdata_2( mu_io_dram_wdata_2 ),
       .io_dram_wdata_1( mu_io_dram_wdata_1 ),
       .io_dram_wdata_0( mu_io_dram_wdata_0 ),
       .io_dram_isWr( mu_io_dram_isWr ),
       .io_dram_rdata_15( DRAMSimulator_io_rdata_15 ),
       .io_dram_rdata_14( DRAMSimulator_io_rdata_14 ),
       .io_dram_rdata_13( DRAMSimulator_io_rdata_13 ),
       .io_dram_rdata_12( DRAMSimulator_io_rdata_12 ),
       .io_dram_rdata_11( DRAMSimulator_io_rdata_11 ),
       .io_dram_rdata_10( DRAMSimulator_io_rdata_10 ),
       .io_dram_rdata_9( DRAMSimulator_io_rdata_9 ),
       .io_dram_rdata_8( DRAMSimulator_io_rdata_8 ),
       .io_dram_rdata_7( DRAMSimulator_io_rdata_7 ),
       .io_dram_rdata_6( DRAMSimulator_io_rdata_6 ),
       .io_dram_rdata_5( DRAMSimulator_io_rdata_5 ),
       .io_dram_rdata_4( DRAMSimulator_io_rdata_4 ),
       .io_dram_rdata_3( DRAMSimulator_io_rdata_3 ),
       .io_dram_rdata_2( DRAMSimulator_io_rdata_2 ),
       .io_dram_rdata_1( DRAMSimulator_io_rdata_1 ),
       .io_dram_rdata_0( DRAMSimulator_io_rdata_0 ),
       .io_dram_addr( mu_io_dram_addr ),
       .io_dram_tagIn( DRAMSimulator_io_tagOut ),
       .io_dram_tagOut( mu_io_dram_tagOut )
       //.io_tagInSimOut(  )
  );
  DRAMSimulator DRAMSimulator(
       //.io_rdyIn(  )
       //.io_rdyOut(  )
       .io_vldIn( mu_io_dram_vldOut ),
       .io_vldOut( DRAMSimulator_io_vldOut ),
       .io_wdata_15( mu_io_dram_wdata_15 ),
       .io_wdata_14( mu_io_dram_wdata_14 ),
       .io_wdata_13( mu_io_dram_wdata_13 ),
       .io_wdata_12( mu_io_dram_wdata_12 ),
       .io_wdata_11( mu_io_dram_wdata_11 ),
       .io_wdata_10( mu_io_dram_wdata_10 ),
       .io_wdata_9( mu_io_dram_wdata_9 ),
       .io_wdata_8( mu_io_dram_wdata_8 ),
       .io_wdata_7( mu_io_dram_wdata_7 ),
       .io_wdata_6( mu_io_dram_wdata_6 ),
       .io_wdata_5( mu_io_dram_wdata_5 ),
       .io_wdata_4( mu_io_dram_wdata_4 ),
       .io_wdata_3( mu_io_dram_wdata_3 ),
       .io_wdata_2( mu_io_dram_wdata_2 ),
       .io_wdata_1( mu_io_dram_wdata_1 ),
       .io_wdata_0( mu_io_dram_wdata_0 ),
       .io_isWr( mu_io_dram_isWr ),
       .io_rdata_15( DRAMSimulator_io_rdata_15 ),
       .io_rdata_14( DRAMSimulator_io_rdata_14 ),
       .io_rdata_13( DRAMSimulator_io_rdata_13 ),
       .io_rdata_12( DRAMSimulator_io_rdata_12 ),
       .io_rdata_11( DRAMSimulator_io_rdata_11 ),
       .io_rdata_10( DRAMSimulator_io_rdata_10 ),
       .io_rdata_9( DRAMSimulator_io_rdata_9 ),
       .io_rdata_8( DRAMSimulator_io_rdata_8 ),
       .io_rdata_7( DRAMSimulator_io_rdata_7 ),
       .io_rdata_6( DRAMSimulator_io_rdata_6 ),
       .io_rdata_5( DRAMSimulator_io_rdata_5 ),
       .io_rdata_4( DRAMSimulator_io_rdata_4 ),
       .io_rdata_3( DRAMSimulator_io_rdata_3 ),
       .io_rdata_2( DRAMSimulator_io_rdata_2 ),
       .io_rdata_1( DRAMSimulator_io_rdata_1 ),
       .io_rdata_0( DRAMSimulator_io_rdata_0 ),
       .io_addr( mu_io_dram_addr ),
       .io_tagIn( mu_io_dram_tagOut ),
       .io_tagOut( DRAMSimulator_io_tagOut )
       //.io_addrSimOut(  )
       //.io_tagInSimOut(  )
       //.io_isWrSimOut(  )
       //.io_wdataSimOut_15(  )
       //.io_wdataSimOut_14(  )
       //.io_wdataSimOut_13(  )
       //.io_wdataSimOut_12(  )
       //.io_wdataSimOut_11(  )
       //.io_wdataSimOut_10(  )
       //.io_wdataSimOut_9(  )
       //.io_wdataSimOut_8(  )
       //.io_wdataSimOut_7(  )
       //.io_wdataSimOut_6(  )
       //.io_wdataSimOut_5(  )
       //.io_wdataSimOut_4(  )
       //.io_wdataSimOut_3(  )
       //.io_wdataSimOut_2(  )
       //.io_wdataSimOut_1(  )
       //.io_wdataSimOut_0(  )
       //.io_vldInSimOut(  )
       //.io_rdyInSimOut(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign DRAMSimulator.io_rdyIn = {1{$random}};
// synthesis translate_on
`endif
endmodule

