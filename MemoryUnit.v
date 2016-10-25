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

module UpDownCtr(input clk, input reset,
    input [4:0] io_initval,
    input [4:0] io_max,
    input [4:0] io_strideInc,
    input [4:0] io_strideDec,
    input  io_init,
    input  io_inc,
    input  io_dec,
    output io_gtz,
    output io_isMax,
    output[4:0] io_out
);

  wire T0;
  wire T1;
  wire[4:0] T2;
  wire[4:0] T3;
  wire[4:0] decval;
  wire[4:0] incval;
  wire T4;
  wire T5;
  wire[4:0] reg__io_data_out;


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
  assign T5 = 5'h0 < reg__io_data_out;
  FF_2 reg_(.clk(clk), .reset(reset),
       .io_data_in( T2 ),
       .io_data_init( 5'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module FF_3(input clk, input reset,
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

module Counter_2(input clk, input reset,
    input [3:0] io_data_max,
    input [3:0] io_data_stride,
    output[3:0] io_data_out,
    output[3:0] io_data_next,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire T0;
  wire[3:0] T4;
  wire[4:0] T1;
  wire[4:0] next;
  wire[4:0] newval;
  wire[4:0] T5;
  wire[4:0] count;
  wire[4:0] T2;
  wire isMax;
  wire[4:0] T6;
  wire T3;
  wire[3:0] T7;
  wire[3:0] T8;
  wire[3:0] reg__io_data_out;


  assign T0 = io_control_reset | io_control_enable;
  assign T4 = T1[3:0];
  assign T1 = io_control_reset ? 5'h0 : next;
  assign next = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 5'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_next = T7;
  assign T7 = next[3:0];
  assign io_data_out = T8;
  assign T8 = count[3:0];
  FF_3 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 4'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module FF_0(input clk, input reset,
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
  FF_0 r(.clk(clk), .reset(reset),
       .io_data_in( T1 ),
       .io_data_init( 1'h0 ),
       .io_data_out( r_io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module CounterRC_0(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [3:0] io_data_max,
    input [3:0] io_data_stride,
    output[3:0] io_data_out,
    output[3:0] io_data_next,
    input  io_control_enable,
    input  io_control_waitIn,
    output io_control_waitOut,
    output io_control_done
    //output io_control_isMax
);

  wire T0;
  wire[3:0] counter_io_data_out;
  wire[3:0] counter_io_data_next;
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
       .io_data_max( 4'h2 ),
       .io_data_stride( 4'h1 ),
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
    input [3:0] io_data_max,
    input [3:0] io_data_stride,
    output[3:0] io_data_out,
    output[3:0] io_data_next,
    input  io_control_enable,
    input  io_control_waitIn,
    output io_control_waitOut,
    output io_control_done
    //output io_control_isMax
);

  wire T0;
  wire[3:0] counter_io_data_out;
  wire[3:0] counter_io_data_next;
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
       .io_data_max( 4'h8 ),
       .io_data_stride( 4'h1 ),
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

module CounterChain(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [3:0] io_data_1_max,
    input [3:0] io_data_1_stride,
    output[3:0] io_data_1_out,
    output[3:0] io_data_1_next,
    input [3:0] io_data_0_max,
    input [3:0] io_data_0_stride,
    output[3:0] io_data_0_out,
    output[3:0] io_data_0_next,
    input  io_control_1_enable,
    output io_control_1_done,
    input  io_control_0_enable,
    output io_control_0_done
);

  wire[3:0] CounterRC_io_data_out;
  wire[3:0] CounterRC_io_data_next;
  wire CounterRC_io_control_done;
  wire[3:0] CounterRC_1_io_data_out;
  wire[3:0] CounterRC_1_io_data_next;
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
    input [15:0] io_ins_1,
    input [15:0] io_ins_0,
    input  io_sel,
    output[15:0] io_out
);

  wire[15:0] T0;
  wire T1;


  assign io_out = T0;
  assign T0 = T1 ? io_ins_1 : io_ins_0;
  assign T1 = io_sel;
endmodule

module FIFO_0(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input [15:0] io_enq_1,
    input [15:0] io_enq_0,
    input  io_enqVld,
    output[15:0] io_deq_1,
    output[15:0] io_deq_0,
    input  io_deqVld,
    output io_full,
    output io_empty
);

  wire T20;
  wire[3:0] T0;
  wire readEn;
  wire T1;
  wire empty;
  wire[15:0] T2;
  reg  config__chainWrite;
  wire T21;
  wire configIn_chainWrite;
  wire T3;
  wire T4;
  wire[1:0] T5;
  wire[1:0] T22;
  wire[2:0] T23;
  wire T6;
  wire T7;
  wire T8;
  wire[2:0] T24;
  wire[3:0] T9;
  wire[3:0] nextHeadLocalAddr;
  wire[3:0] T10;
  reg  config__chainRead;
  wire T25;
  wire configIn_chainRead;
  wire T11;
  wire T12;
  wire[2:0] T26;
  wire T13;
  wire T14;
  wire T15;
  wire[2:0] T27;
  wire[3:0] T16;
  wire writeEn;
  wire T17;
  wire[4:0] T28;
  wire[1:0] T18;
  wire[4:0] T29;
  wire[1:0] T19;
  wire[15:0] SRAM_io_rdata;
  wire[15:0] SRAM_1_io_rdata;
  wire[15:0] MuxN_io_out;
  wire FF_io_data_out;
  wire sizeUDC_io_isMax;
  wire[4:0] sizeUDC_io_out;
  wire[3:0] wptr_io_data_1_out;
  wire[3:0] wptr_io_data_0_out;
  wire[3:0] rptr_io_data_1_out;
  wire[3:0] rptr_io_data_1_next;
  wire[3:0] rptr_io_data_0_out;
  wire[3:0] rptr_io_data_0_next;
  wire rptr_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chainWrite = {1{$random}};
    config__chainRead = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T20 = T0[0];
  assign T0 = readEn ? rptr_io_data_0_next : rptr_io_data_0_out;
  assign readEn = io_deqVld & T1;
  assign T1 = ~ empty;
  assign empty = sizeUDC_io_out == 5'h0;
  assign T2 = config__chainWrite ? io_enq_0 : io_enq_1;
  assign T21 = reset ? 1'h1 : configIn_chainWrite;
  assign configIn_chainWrite = T3;
  assign T3 = io_config_enable ? T4 : config__chainWrite;
  assign T4 = T5[1];
  assign T5 = 2'h0 - T22;
  assign T22 = {1'h0, io_config_data};
  assign T23 = wptr_io_data_1_out[2:0];
  assign T6 = config__chainWrite ? T7 : io_enqVld;
  assign T7 = io_enqVld & T8;
  assign T8 = wptr_io_data_0_out == 4'h1;
  assign T24 = T9[2:0];
  assign T9 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign nextHeadLocalAddr = config__chainRead ? T10 : rptr_io_data_1_next;
  assign T10 = rptr_io_control_0_done ? rptr_io_data_1_next : rptr_io_data_1_out;
  assign T25 = reset ? 1'h1 : configIn_chainRead;
  assign configIn_chainRead = T11;
  assign T11 = io_config_enable ? T12 : config__chainRead;
  assign T12 = T5[0];
  assign T26 = wptr_io_data_1_out[2:0];
  assign T13 = config__chainWrite ? T14 : io_enqVld;
  assign T14 = io_enqVld & T15;
  assign T15 = wptr_io_data_0_out == 4'h0;
  assign T27 = T16[2:0];
  assign T16 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign writeEn = io_enqVld & T17;
  assign T17 = ~ sizeUDC_io_isMax;
  assign T28 = {3'h0, T18};
  assign T18 = config__chainRead ? 2'h1 : 2'h2;
  assign T29 = {3'h0, T19};
  assign T19 = config__chainWrite ? 2'h1 : 2'h2;
  assign io_empty = empty;
  assign io_full = sizeUDC_io_isMax;
  assign io_deq_0 = MuxN_io_out;
  assign io_deq_1 = SRAM_1_io_rdata;
  UpDownCtr sizeUDC(.clk(clk), .reset(reset),
       .io_initval( 5'h0 ),
       .io_max( 5'h10 ),
       .io_strideInc( T29 ),
       .io_strideDec( T28 ),
       .io_init( 1'h0 ),
       .io_inc( writeEn ),
       .io_dec( readEn ),
       //.io_gtz(  )
       .io_isMax( sizeUDC_io_isMax ),
       .io_out( sizeUDC_io_out )
  );
  CounterChain wptr(.clk(clk), .reset(reset),
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
       .io_control_0_enable( writeEn )
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
  CounterChain rptr(.clk(clk), .reset(reset),
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
       .io_control_0_enable( readEn ),
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
       .io_raddr( T27 ),
       .io_wen( T13 ),
       .io_waddr( T26 ),
       .io_wdata( io_enq_0 ),
       .io_rdata( SRAM_io_rdata )
  );
  SRAM SRAM_1(.clk(clk),
       .io_raddr( T24 ),
       .io_wen( T6 ),
       .io_waddr( T23 ),
       .io_wdata( T2 ),
       .io_rdata( SRAM_1_io_rdata )
  );
  MuxN_0 MuxN(
       .io_ins_1( SRAM_1_io_rdata ),
       .io_ins_0( SRAM_io_rdata ),
       .io_sel( FF_io_data_out ),
       .io_out( MuxN_io_out )
  );
  FF_0 FF(.clk(clk), .reset(reset),
       .io_data_in( T20 ),
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
      config__chainWrite <= 1'h1;
    end else begin
      config__chainWrite <= configIn_chainWrite;
    end
    if(reset) begin
      config__chainRead <= 1'h1;
    end else begin
      config__chainRead <= configIn_chainRead;
    end
  end
endmodule

module MuxN_1(
    input  io_ins_1,
    input  io_ins_0,
    input  io_sel,
    output io_out
);

  wire T0;
  wire T1;


  assign io_out = T0;
  assign T0 = T1 ? io_ins_1 : io_ins_0;
  assign T1 = io_sel;
endmodule

module FIFO_1(input clk, input reset,
    input  io_config_data,
    input  io_config_enable,
    input  io_enq_1,
    input  io_enq_0,
    input  io_enqVld,
    output io_deq_1,
    output io_deq_0,
    input  io_deqVld,
    output io_full,
    output io_empty
);

  wire T20;
  wire[3:0] T0;
  wire readEn;
  wire T1;
  wire empty;
  wire T2;
  reg  config__chainWrite;
  wire T21;
  wire configIn_chainWrite;
  wire T3;
  wire T4;
  wire[1:0] T5;
  wire[1:0] T22;
  wire[2:0] T23;
  wire T6;
  wire T7;
  wire T8;
  wire[2:0] T24;
  wire[3:0] T9;
  wire[3:0] nextHeadLocalAddr;
  wire[3:0] T10;
  reg  config__chainRead;
  wire T25;
  wire configIn_chainRead;
  wire T11;
  wire T12;
  wire[2:0] T26;
  wire T13;
  wire T14;
  wire T15;
  wire[2:0] T27;
  wire[3:0] T16;
  wire writeEn;
  wire T17;
  wire[4:0] T28;
  wire[1:0] T18;
  wire[4:0] T29;
  wire[1:0] T19;
  wire SRAM_io_rdata;
  wire SRAM_1_io_rdata;
  wire MuxN_io_out;
  wire FF_io_data_out;
  wire sizeUDC_io_isMax;
  wire[4:0] sizeUDC_io_out;
  wire[3:0] wptr_io_data_1_out;
  wire[3:0] wptr_io_data_0_out;
  wire[3:0] rptr_io_data_1_out;
  wire[3:0] rptr_io_data_1_next;
  wire[3:0] rptr_io_data_0_out;
  wire[3:0] rptr_io_data_0_next;
  wire rptr_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chainWrite = {1{$random}};
    config__chainRead = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T20 = T0[0];
  assign T0 = readEn ? rptr_io_data_0_next : rptr_io_data_0_out;
  assign readEn = io_deqVld & T1;
  assign T1 = ~ empty;
  assign empty = sizeUDC_io_out == 5'h0;
  assign T2 = config__chainWrite ? io_enq_0 : io_enq_1;
  assign T21 = reset ? 1'h1 : configIn_chainWrite;
  assign configIn_chainWrite = T3;
  assign T3 = io_config_enable ? T4 : config__chainWrite;
  assign T4 = T5[1];
  assign T5 = 2'h0 - T22;
  assign T22 = {1'h0, io_config_data};
  assign T23 = wptr_io_data_1_out[2:0];
  assign T6 = config__chainWrite ? T7 : io_enqVld;
  assign T7 = io_enqVld & T8;
  assign T8 = wptr_io_data_0_out == 4'h1;
  assign T24 = T9[2:0];
  assign T9 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign nextHeadLocalAddr = config__chainRead ? T10 : rptr_io_data_1_next;
  assign T10 = rptr_io_control_0_done ? rptr_io_data_1_next : rptr_io_data_1_out;
  assign T25 = reset ? 1'h1 : configIn_chainRead;
  assign configIn_chainRead = T11;
  assign T11 = io_config_enable ? T12 : config__chainRead;
  assign T12 = T5[0];
  assign T26 = wptr_io_data_1_out[2:0];
  assign T13 = config__chainWrite ? T14 : io_enqVld;
  assign T14 = io_enqVld & T15;
  assign T15 = wptr_io_data_0_out == 4'h0;
  assign T27 = T16[2:0];
  assign T16 = readEn ? nextHeadLocalAddr : rptr_io_data_1_out;
  assign writeEn = io_enqVld & T17;
  assign T17 = ~ sizeUDC_io_isMax;
  assign T28 = {3'h0, T18};
  assign T18 = config__chainRead ? 2'h1 : 2'h2;
  assign T29 = {3'h0, T19};
  assign T19 = config__chainWrite ? 2'h1 : 2'h2;
  assign io_empty = empty;
  assign io_full = sizeUDC_io_isMax;
  assign io_deq_0 = MuxN_io_out;
  assign io_deq_1 = SRAM_1_io_rdata;
  UpDownCtr sizeUDC(.clk(clk), .reset(reset),
       .io_initval( 5'h0 ),
       .io_max( 5'h10 ),
       .io_strideInc( T29 ),
       .io_strideDec( T28 ),
       .io_init( 1'h0 ),
       .io_inc( writeEn ),
       .io_dec( readEn ),
       //.io_gtz(  )
       .io_isMax( sizeUDC_io_isMax ),
       .io_out( sizeUDC_io_out )
  );
  CounterChain wptr(.clk(clk), .reset(reset),
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
       .io_control_0_enable( writeEn )
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
  CounterChain rptr(.clk(clk), .reset(reset),
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
       .io_control_0_enable( readEn ),
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
       .io_raddr( T27 ),
       .io_wen( T13 ),
       .io_waddr( T26 ),
       .io_wdata( io_enq_0 ),
       .io_rdata( SRAM_io_rdata )
  );
  SRAM SRAM_1(.clk(clk),
       .io_raddr( T24 ),
       .io_wen( T6 ),
       .io_waddr( T23 ),
       .io_wdata( T2 ),
       .io_rdata( SRAM_1_io_rdata )
  );
  MuxN_1 MuxN(
       .io_ins_1( SRAM_1_io_rdata ),
       .io_ins_0( SRAM_io_rdata ),
       .io_sel( FF_io_data_out ),
       .io_out( MuxN_io_out )
  );
  FF_0 FF(.clk(clk), .reset(reset),
       .io_data_in( T20 ),
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
      config__chainWrite <= 1'h1;
    end else begin
      config__chainWrite <= configIn_chainWrite;
    end
    if(reset) begin
      config__chainRead <= 1'h1;
    end else begin
      config__chainRead <= configIn_chainRead;
    end
  end
endmodule

module FF_1(input clk, input reset,
    input [15:0] io_data_in,
    input [15:0] io_data_init,
    output[15:0] io_data_out,
    input  io_control_enable
);

  reg [15:0] ff;
  wire[15:0] T1;
  wire[15:0] d;
  wire[15:0] T0;

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
    input [15:0] io_data_max,
    input [15:0] io_data_stride,
    output[15:0] io_data_out,
    output[15:0] io_data_next,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire T0;
  wire[15:0] T4;
  wire[16:0] T1;
  wire[16:0] next;
  wire[16:0] newval;
  wire[16:0] T5;
  wire[16:0] count;
  wire[16:0] T2;
  wire isMax;
  wire[16:0] T6;
  wire T3;
  wire[15:0] T7;
  wire[15:0] T8;
  wire[15:0] reg__io_data_out;


  assign T0 = io_control_reset | io_control_enable;
  assign T4 = T1[15:0];
  assign T1 = io_control_reset ? 17'h0 : next;
  assign next = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 17'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_next = T7;
  assign T7 = next[15:0];
  assign io_data_out = T8;
  assign T8 = count[15:0];
  FF_1 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 16'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
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
    input [15:0] io_interconnect_wdata_1,
    input [15:0] io_interconnect_wdata_0,
    input  io_interconnect_isWr,
    output[15:0] io_interconnect_rdata_1,
    output[15:0] io_interconnect_rdata_0,
    input [15:0] io_interconnect_addr_1,
    input [15:0] io_interconnect_addr_0,
    input [15:0] io_interconnect_size,
    input  io_dram_rdyIn,
    output io_dram_rdyOut,
    input  io_dram_vldIn,
    output io_dram_vldOut,
    output[15:0] io_dram_wdata_1,
    output[15:0] io_dram_wdata_0,
    output io_dram_isWr,
    input [15:0] io_dram_rdata_1,
    input [15:0] io_dram_rdata_0,
    output[15:0] io_dram_addr,
    input [15:0] io_dram_tagIn,
    output[15:0] io_dram_tagOut
);

  wire burstVld;
  wire[15:0] T14;
  wire[9:0] T0;
  wire[9:0] sizeInBursts;
  wire[9:0] T15;
  wire T1;
  wire[5:0] T2;
  wire[9:0] T3;
  reg  config__scatterGather;
  wire T16;
  wire configIn_scatterGather;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire T8;
  wire[15:0] T17;
  wire[9:0] T9;
  wire[9:0] T18;
  wire[9:0] burstAddrs_0;
  wire[15:0] T10;
  wire[15:0] T19;
  wire T11;
  wire T12;
  wire T13;
  wire[15:0] burstCounter_io_data_out;
  wire burstCounter_io_control_done;
  wire[4:0] burstTagCounter_io_data_out;
  wire[15:0] addrFifo_io_deq_0;
  wire addrFifo_io_full;
  wire[15:0] sizeFifo_io_deq_0;
  wire sizeFifo_io_empty;
  wire rwFifo_io_deq_0;
  wire[15:0] dataFifo_io_deq_1;
  wire[15:0] dataFifo_io_deq_0;
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
  assign T0 = config__scatterGather ? 10'h1 : sizeInBursts;
  assign sizeInBursts = T3 + T15;
  assign T15 = {9'h0, T1};
  assign T1 = T2 != 6'h0;
  assign T2 = sizeFifo_io_deq_0[5:0];
  assign T3 = sizeFifo_io_deq_0[15:6];
  assign T16 = reset ? 1'h0 : configIn_scatterGather;
  assign configIn_scatterGather = T4;
  assign T4 = io_config_enable ? io_config_data : config__scatterGather;
  assign T5 = T6;
  assign T6 = rwFifo_io_deq_0 & burstVld;
  assign T7 = io_interconnect_vldIn & T8;
  assign T8 = ~ config__scatterGather;
  assign io_dram_tagOut = T17;
  assign T17 = {6'h0, T9};
  assign T9 = config__scatterGather ? burstAddrs_0 : T18;
  assign T18 = {5'h0, burstTagCounter_io_data_out};
  assign burstAddrs_0 = addrFifo_io_deq_0[15:6];
  assign io_dram_addr = T10;
  assign T10 = T19 + burstCounter_io_data_out;
  assign T19 = {6'h0, burstAddrs_0};
  assign io_dram_isWr = rwFifo_io_deq_0;
  assign io_dram_wdata_0 = dataFifo_io_deq_0;
  assign io_dram_wdata_1 = dataFifo_io_deq_1;
  assign io_dram_vldOut = burstVld;
  assign io_interconnect_rdata_0 = io_dram_rdata_0;
  assign io_interconnect_rdata_1 = io_dram_rdata_1;
  assign io_interconnect_vldOut = io_dram_vldIn;
  assign io_interconnect_rdyOut = T11;
  assign T11 = T13 & T12;
  assign T12 = ~ dataFifo_io_full;
  assign T13 = ~ addrFifo_io_full;
  FIFO_0 addrFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_1( io_interconnect_addr_1 ),
       .io_enq_0( io_interconnect_addr_0 ),
       .io_enqVld( io_interconnect_vldIn ),
       //.io_deq_1(  )
       .io_deq_0( addrFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done ),
       .io_full( addrFifo_io_full )
       //.io_empty(  )
  );
  FIFO_0 sizeFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_1( io_interconnect_size ),
       .io_enq_0( io_interconnect_size ),
       .io_enqVld( T7 ),
       //.io_deq_1(  )
       .io_deq_0( sizeFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done ),
       //.io_full(  )
       .io_empty( sizeFifo_io_empty )
  );
  FIFO_1 rwFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_1( io_interconnect_isWr ),
       .io_enq_0( io_interconnect_isWr ),
       .io_enqVld( io_interconnect_vldIn ),
       //.io_deq_1(  )
       .io_deq_0( rwFifo_io_deq_0 ),
       .io_deqVld( burstCounter_io_control_done )
       //.io_full(  )
       //.io_empty(  )
  );
  FIFO_0 dataFifo(.clk(clk), .reset(reset),
       .io_config_data( io_config_data ),
       .io_config_enable( io_config_enable ),
       .io_enq_1( io_interconnect_wdata_1 ),
       .io_enq_0( io_interconnect_wdata_0 ),
       .io_enqVld( io_interconnect_vldIn ),
       .io_deq_1( dataFifo_io_deq_1 ),
       .io_deq_0( dataFifo_io_deq_0 ),
       .io_deqVld( T5 ),
       .io_full( dataFifo_io_full )
       //.io_empty(  )
  );
  Counter_0 burstCounter(.clk(clk), .reset(reset),
       .io_data_max( T14 ),
       .io_data_stride( 16'h1 ),
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

