module Plasticine(input clk, input reset,
    input  io_config_enable,
    input  io_command,
    output io_status
);

  wire controlBox_io_startTokenOut;
  wire controlBox_io_statusOut;
  wire cu0_io_tokenOuts_1;
  wire cu0_io_tokenOuts_0;
  wire[7:0] cu0_io_dataOut_0;
  wire cu1_io_tokenOuts_0;
  wire[7:0] cu1_io_dataOut_0;


  assign io_status = controlBox_io_statusOut;
  MainControlBox controlBox(.clk(clk), .reset(reset),
       .io_command( io_command ),
       .io_doneTokenIn( cu1_io_tokenOuts_0 ),
       .io_startTokenOut( controlBox_io_startTokenOut ),
       .io_statusOut( controlBox_io_statusOut )
  );
  ComputeUnit cu0(.clk(clk), .reset(reset),
       .io_config_enable( io_config_enable ),
       .io_tokenIns_1( 1'h0 ),
       .io_tokenIns_0( controlBox_io_startTokenOut ),
       .io_tokenOuts_1( cu0_io_tokenOuts_1 ),
       .io_tokenOuts_0( cu0_io_tokenOuts_0 ),
       //.io_scalarOut(  )
       .io_dataIn_0( cu1_io_dataOut_0 ),
       .io_dataOut_0( cu0_io_dataOut_0 )
  );
  ComputeUnit cu1(.clk(clk), .reset(reset),
       .io_config_enable( io_config_enable ),
       .io_tokenIns_1( cu0_io_tokenOuts_1 ),
       .io_tokenIns_0( cu0_io_tokenOuts_0 ),
       //.io_tokenOuts_1(  )
       .io_tokenOuts_0( cu1_io_tokenOuts_0 ),
       //.io_scalarOut(  )
       .io_dataIn_0( cu0_io_dataOut_0 ),
       .io_dataOut_0( cu1_io_dataOut_0 )
  );
endmodule

module Pulser(input clk, input reset,
    input  io_in,
    output io_out
);

  wire T0;
  wire T1;
  reg  commandReg;
  wire T2;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    commandReg = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_out = T0;
  assign T0 = io_in & T1;
  assign T1 = commandReg ^ io_in;
  assign T2 = reset ? 1'h0 : io_in;

  always @(posedge clk) begin
    if(reset) begin
      commandReg <= 1'h0;
    end else begin
      commandReg <= io_in;
    end
  end
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

module MainControlBox(input clk, input reset,
    input  io_command,
    input  io_doneTokenIn,
    output io_startTokenOut,
    output io_statusOut
);

  wire T0;
  wire T1;
  reg  commandReg;
  wire T2;
  reg  statusReg;
  wire T3;
  wire pulser_io_out;
  wire depulser_io_out;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    commandReg = {1{$random}};
    statusReg = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T0 = T1;
  assign T1 = ~ io_command;
  assign T2 = reset ? 1'h0 : io_command;
  assign io_statusOut = statusReg;
  assign T3 = reset ? 1'h0 : depulser_io_out;
  assign io_startTokenOut = pulser_io_out;
  Pulser pulser(.clk(clk), .reset(reset),
       .io_in( commandReg ),
       .io_out( pulser_io_out )
  );
  Depulser depulser(.clk(clk), .reset(reset),
       .io_in( io_doneTokenIn ),
       .io_rst( T0 ),
       .io_out( depulser_io_out )
  );

  always @(posedge clk) begin
    if(reset) begin
      commandReg <= 1'h0;
    end else begin
      commandReg <= io_command;
    end
    if(reset) begin
      statusReg <= 1'h0;
    end else begin
      statusReg <= depulser_io_out;
    end
  end
endmodule

module SRAM(input clk,
    input [3:0] io_raddr,
    input  io_wen,
    input [3:0] io_waddr,
    input [7:0] io_wdata,
    output[7:0] io_rdata
);

//  wire[7:0] T0;
//  reg [7:0] mem [15:0];
//  wire[7:0] T1;
//  reg [3:0] raddr_reg;
//  wire[3:0] T2;
//  wire T3;
//
//`ifndef SYNTHESIS
//// synthesis translate_off
//  integer initvar;
//  initial begin
//    #0.002;
//    for (initvar = 0; initvar < 16; initvar = initvar+1)
//      mem[initvar] = {1{$random}};
//    raddr_reg = {1{$random}};
//  end
//// synthesis translate_on
//`endif
//
//  assign io_rdata = T0;
//  assign T0 = mem[raddr_reg];
//  assign T2 = T3 ? io_raddr : raddr_reg;
//  assign T3 = io_wen ^ 1'h1;
//
//  always @(posedge clk) begin
//    if (io_wen)
//      mem[io_waddr] <= io_wdata;
//    if(T3) begin
//      raddr_reg <= io_raddr;
//    end
//  end
endmodule

module MuxVec_0(
    input [3:0] io_ins_1_0,
    input [3:0] io_ins_0_0,
    input  io_sel,
    output[3:0] io_out_0
);

  wire[3:0] T0;
  wire T1;


  assign io_out_0 = T0;
  assign T0 = T1 ? io_ins_1_0 : io_ins_0_0;
  assign T1 = io_sel;
endmodule

module MuxVec_1(
    input [7:0] io_ins_1_0,
    input [7:0] io_ins_0_0,
    input  io_sel,
    output[7:0] io_out_0
);

  wire[7:0] T0;
  wire T1;


  assign io_out_0 = T0;
  assign T0 = T1 ? io_ins_1_0 : io_ins_0_0;
  assign T1 = io_sel;
endmodule

module MuxVec_2(
    input [3:0] io_ins_0_0,
    input  io_sel,
    output[3:0] io_out_0
);



  assign io_out_0 = io_ins_0_0;
endmodule

module FF_1(input clk, input reset,
    input [7:0] io_data_in,
    input [7:0] io_data_init,
    output[7:0] io_data_out,
    input  io_control_enable
);

  reg [7:0] ff;
  wire[7:0] T1;
  wire[7:0] d;
  wire[7:0] T0;

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

module Counter(input clk, input reset,
    input [7:0] io_data_max,
    input [7:0] io_data_stride,
    output[7:0] io_data_out,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire[7:0] T4;
  wire[8:0] T0;
  wire[8:0] T1;
  wire[8:0] newval;
  wire[8:0] T5;
  wire[8:0] count;
  wire[8:0] T2;
  wire isMax;
  wire[8:0] T6;
  wire T3;
  wire[7:0] T7;
  wire[7:0] reg__io_data_out;


  assign T4 = T0[7:0];
  assign T0 = io_control_reset ? 9'h0 : T1;
  assign T1 = isMax ? T2 : newval;
  assign newval = count + T5;
  assign T5 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 9'h0;
  assign isMax = T6 <= newval;
  assign T6 = {1'h0, io_data_max};
  assign io_control_done = T3;
  assign T3 = io_control_enable & isMax;
  assign io_data_out = T7;
  assign T7 = count[7:0];
  FF_1 reg_(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 8'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( io_control_enable )
  );
endmodule

module CounterRC(input clk, input reset,
    input  io_config_enable,
    input [7:0] io_data_max,
    input [7:0] io_data_stride,
    output[7:0] io_data_out,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire[7:0] T0;
  reg [7:0] config__stride;
  wire[7:0] T6;
  wire[7:0] configIn_stride;
  wire[7:0] T1;
  wire[7:0] configType_stride;
  reg  config__strideConst;
  wire T7;
  wire configIn_strideConst;
  wire T2;
  wire configType_strideConst;
  wire[7:0] T3;
  reg [7:0] config__max;
  wire[7:0] T8;
  wire[7:0] configIn_max;
  wire[7:0] T4;
  wire[7:0] configType_max;
  reg  config__maxConst;
  wire T9;
  wire configIn_maxConst;
  wire T5;
  wire configType_maxConst;
  wire[7:0] counter_io_data_out;
  wire counter_io_control_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__stride = {1{$random}};
    config__strideConst = {1{$random}};
    config__max = {1{$random}};
    config__maxConst = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_stride = {1{$random}};
  assign configType_strideConst = {1{$random}};
  assign configType_max = {1{$random}};
  assign configType_maxConst = {1{$random}};
// synthesis translate_on
`endif
  assign T0 = config__strideConst ? config__stride : io_data_stride;
  assign T6 = reset ? 8'h1 : configIn_stride;
  assign configIn_stride = T1;
  assign T1 = io_config_enable ? configType_stride : config__stride;
  assign T7 = reset ? 1'h1 : configIn_strideConst;
  assign configIn_strideConst = T2;
  assign T2 = io_config_enable ? configType_strideConst : config__strideConst;
  assign T3 = config__maxConst ? config__max : io_data_max;
  assign T8 = reset ? 8'h5 : configIn_max;
  assign configIn_max = T4;
  assign T4 = io_config_enable ? configType_max : config__max;
  assign T9 = reset ? 1'h1 : configIn_maxConst;
  assign configIn_maxConst = T5;
  assign T5 = io_config_enable ? configType_maxConst : config__maxConst;
  assign io_control_done = counter_io_control_done;
  assign io_data_out = counter_io_data_out;
  Counter counter(.clk(clk), .reset(reset),
       .io_data_max( T3 ),
       .io_data_stride( T0 ),
       .io_data_out( counter_io_data_out ),
       .io_control_reset( io_control_reset ),
       .io_control_enable( io_control_enable ),
       .io_control_saturate( io_control_saturate ),
       .io_control_done( counter_io_control_done )
  );

  always @(posedge clk) begin
    if(reset) begin
      config__stride <= 8'h1;
    end else begin
      config__stride <= configIn_stride;
    end
    if(reset) begin
      config__strideConst <= 1'h1;
    end else begin
      config__strideConst <= configIn_strideConst;
    end
    if(reset) begin
      config__max <= 8'h5;
    end else begin
      config__max <= configIn_max;
    end
    if(reset) begin
      config__maxConst <= 1'h1;
    end else begin
      config__maxConst <= configIn_maxConst;
    end
  end
endmodule

module CounterChain(input clk, input reset,
    input  io_config_enable,
    input [7:0] io_data_1_max,
    input [7:0] io_data_1_stride,
    output[7:0] io_data_1_out,
    input [7:0] io_data_0_max,
    input [7:0] io_data_0_stride,
    output[7:0] io_data_0_out,
    input  io_control_1_enable,
    output io_control_1_done,
    input  io_control_0_enable,
    output io_control_0_done
);

  wire T0;
  wire T1;
  reg  config__chain_0;
  wire T3;
  wire configIn_chain_0;
  wire T2;
  wire configType_chain_0;
  wire[7:0] CounterRC_io_data_out;
  wire CounterRC_io_control_done;
  wire[7:0] CounterRC_1_io_data_out;
  wire CounterRC_1_io_control_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__chain_0 = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_chain_0 = {1{$random}};
// synthesis translate_on
`endif
  assign T0 = config__chain_0 ? T1 : io_control_1_enable;
  assign T1 = io_control_1_enable & CounterRC_io_control_done;
  assign T3 = reset ? 1'h0 : configIn_chain_0;
  assign configIn_chain_0 = T2;
  assign T2 = io_config_enable ? configType_chain_0 : config__chain_0;
  assign io_control_0_done = CounterRC_io_control_done;
  assign io_control_1_done = CounterRC_1_io_control_done;
  assign io_data_0_out = CounterRC_io_data_out;
  assign io_data_1_out = CounterRC_1_io_data_out;
  CounterRC CounterRC(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_data_max( io_data_0_max ),
       .io_data_stride( io_data_0_stride ),
       .io_data_out( CounterRC_io_data_out ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( io_control_0_enable ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( CounterRC_io_control_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign CounterRC.io_config_enable = {1{$random}};
// synthesis translate_on
`endif
  CounterRC CounterRC_1(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_data_max( io_data_1_max ),
       .io_data_stride( io_data_1_stride ),
       .io_data_out( CounterRC_1_io_data_out ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( T0 ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( CounterRC_1_io_control_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign CounterRC_1.io_config_enable = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__chain_0 <= 1'h0;
    end else begin
      config__chain_0 <= configIn_chain_0;
    end
  end
endmodule

module LUT(input clk, input reset,
    input  io_config_enable,
    input  io_ins_1,
    input  io_ins_0,
    output io_out
);

  wire T0;
  wire T1;
  reg  config__table_0;
  wire T11;
  wire configIn_table_0;
  wire T2;
  wire configType_table_0;
  reg  config__table_1;
  wire T12;
  wire configIn_table_1;
  wire T3;
  wire configType_table_1;
  wire T4;
  wire[1:0] T5;
  wire[1:0] sel;
  wire T6;
  reg  config__table_2;
  wire T13;
  wire configIn_table_2;
  wire T7;
  wire configType_table_2;
  reg  config__table_3;
  wire T14;
  wire configIn_table_3;
  wire T8;
  wire configType_table_3;
  wire T9;
  wire T10;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__table_0 = {1{$random}};
    config__table_1 = {1{$random}};
    config__table_2 = {1{$random}};
    config__table_3 = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_table_0 = {1{$random}};
  assign configType_table_1 = {1{$random}};
  assign configType_table_2 = {1{$random}};
  assign configType_table_3 = {1{$random}};
// synthesis translate_on
`endif
  assign io_out = T0;
  assign T0 = T10 ? T6 : T1;
  assign T1 = T4 ? config__table_1 : config__table_0;
  assign T11 = reset ? 1'h0 : configIn_table_0;
  assign configIn_table_0 = T2;
  assign T2 = io_config_enable ? configType_table_0 : config__table_0;
  assign T12 = reset ? 1'h0 : configIn_table_1;
  assign configIn_table_1 = T3;
  assign T3 = io_config_enable ? configType_table_1 : config__table_1;
  assign T4 = T5[0];
  assign T5 = sel;
  assign sel = {io_ins_0, io_ins_1};
  assign T6 = T9 ? config__table_3 : config__table_2;
  assign T13 = reset ? 1'h0 : configIn_table_2;
  assign configIn_table_2 = T7;
  assign T7 = io_config_enable ? configType_table_2 : config__table_2;
  assign T14 = reset ? 1'h0 : configIn_table_3;
  assign configIn_table_3 = T8;
  assign T8 = io_config_enable ? configType_table_3 : config__table_3;
  assign T9 = T5[0];
  assign T10 = T5[1];

  always @(posedge clk) begin
    if(reset) begin
      config__table_0 <= 1'h0;
    end else begin
      config__table_0 <= configIn_table_0;
    end
    if(reset) begin
      config__table_1 <= 1'h0;
    end else begin
      config__table_1 <= configIn_table_1;
    end
    if(reset) begin
      config__table_2 <= 1'h0;
    end else begin
      config__table_2 <= configIn_table_2;
    end
    if(reset) begin
      config__table_3 <= 1'h0;
    end else begin
      config__table_3 <= configIn_table_3;
    end
  end
endmodule

module MuxN_4(
    input  io_ins_2,
    input  io_ins_1,
    input  io_ins_0,
    input [1:0] io_sel,
    output io_out
);

  wire T0;
  wire T1;
  wire T2;
  wire[1:0] T3;
  wire T4;


  assign io_out = T0;
  assign T0 = T4 ? io_ins_2 : T1;
  assign T1 = T2 ? io_ins_1 : io_ins_0;
  assign T2 = T3[0];
  assign T3 = io_sel;
  assign T4 = T3[1];
endmodule

module Crossbar_0(input clk, input reset,
    input  io_config_enable,
    input  io_ins_2,
    input  io_ins_1,
    input  io_ins_0,
    output io_outs_1,
    output io_outs_0
);

  reg [1:0] config__outSelect_1;
  wire[1:0] T2;
  wire[1:0] configIn_outSelect_1;
  wire[1:0] T0;
  wire[1:0] configType_outSelect_1;
  reg [1:0] config__outSelect_0;
  wire[1:0] T3;
  wire[1:0] configIn_outSelect_0;
  wire[1:0] T1;
  wire[1:0] configType_outSelect_0;
  wire MuxN_io_out;
  wire MuxN_1_io_out;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__outSelect_1 = {1{$random}};
    config__outSelect_0 = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_outSelect_1 = {1{$random}};
  assign configType_outSelect_0 = {1{$random}};
// synthesis translate_on
`endif
  assign T2 = reset ? 2'h1 : configIn_outSelect_1;
  assign configIn_outSelect_1 = T0;
  assign T0 = io_config_enable ? configType_outSelect_1 : config__outSelect_1;
  assign T3 = reset ? 2'h1 : configIn_outSelect_0;
  assign configIn_outSelect_0 = T1;
  assign T1 = io_config_enable ? configType_outSelect_0 : config__outSelect_0;
  assign io_outs_0 = MuxN_io_out;
  assign io_outs_1 = MuxN_1_io_out;
  MuxN_4 MuxN(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_0 ),
       .io_out( MuxN_io_out )
  );
  MuxN_4 MuxN_1(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_1 ),
       .io_out( MuxN_1_io_out )
  );

  always @(posedge clk) begin
    if(reset) begin
      config__outSelect_1 <= 2'h1;
    end else begin
      config__outSelect_1 <= configIn_outSelect_1;
    end
    if(reset) begin
      config__outSelect_0 <= 2'h1;
    end else begin
      config__outSelect_0 <= configIn_outSelect_0;
    end
  end
endmodule

module Crossbar_1(input clk, input reset,
    input  io_config_enable,
    input  io_ins_2,
    input  io_ins_1,
    input  io_ins_0,
    output io_outs_3,
    output io_outs_2,
    output io_outs_1,
    output io_outs_0
);

  reg [1:0] config__outSelect_3;
  wire[1:0] T4;
  wire[1:0] configIn_outSelect_3;
  wire[1:0] T0;
  wire[1:0] configType_outSelect_3;
  reg [1:0] config__outSelect_2;
  wire[1:0] T5;
  wire[1:0] configIn_outSelect_2;
  wire[1:0] T1;
  wire[1:0] configType_outSelect_2;
  reg [1:0] config__outSelect_1;
  wire[1:0] T6;
  wire[1:0] configIn_outSelect_1;
  wire[1:0] T2;
  wire[1:0] configType_outSelect_1;
  reg [1:0] config__outSelect_0;
  wire[1:0] T7;
  wire[1:0] configIn_outSelect_0;
  wire[1:0] T3;
  wire[1:0] configType_outSelect_0;
  wire MuxN_io_out;
  wire MuxN_1_io_out;
  wire MuxN_2_io_out;
  wire MuxN_3_io_out;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__outSelect_3 = {1{$random}};
    config__outSelect_2 = {1{$random}};
    config__outSelect_1 = {1{$random}};
    config__outSelect_0 = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_outSelect_3 = {1{$random}};
  assign configType_outSelect_2 = {1{$random}};
  assign configType_outSelect_1 = {1{$random}};
  assign configType_outSelect_0 = {1{$random}};
// synthesis translate_on
`endif
  assign T4 = reset ? 2'h2 : configIn_outSelect_3;
  assign configIn_outSelect_3 = T0;
  assign T0 = io_config_enable ? configType_outSelect_3 : config__outSelect_3;
  assign T5 = reset ? 2'h0 : configIn_outSelect_2;
  assign configIn_outSelect_2 = T1;
  assign T1 = io_config_enable ? configType_outSelect_2 : config__outSelect_2;
  assign T6 = reset ? 2'h1 : configIn_outSelect_1;
  assign configIn_outSelect_1 = T2;
  assign T2 = io_config_enable ? configType_outSelect_1 : config__outSelect_1;
  assign T7 = reset ? 2'h3 : configIn_outSelect_0;
  assign configIn_outSelect_0 = T3;
  assign T3 = io_config_enable ? configType_outSelect_0 : config__outSelect_0;
  assign io_outs_0 = MuxN_io_out;
  assign io_outs_1 = MuxN_1_io_out;
  assign io_outs_2 = MuxN_2_io_out;
  assign io_outs_3 = MuxN_3_io_out;
  MuxN_4 MuxN(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_0 ),
       .io_out( MuxN_io_out )
  );
  MuxN_4 MuxN_1(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_1 ),
       .io_out( MuxN_1_io_out )
  );
  MuxN_4 MuxN_2(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_2 ),
       .io_out( MuxN_2_io_out )
  );
  MuxN_4 MuxN_3(
       .io_ins_2( io_ins_2 ),
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( config__outSelect_3 ),
       .io_out( MuxN_3_io_out )
  );

  always @(posedge clk) begin
    if(reset) begin
      config__outSelect_3 <= 2'h2;
    end else begin
      config__outSelect_3 <= configIn_outSelect_3;
    end
    if(reset) begin
      config__outSelect_2 <= 2'h0;
    end else begin
      config__outSelect_2 <= configIn_outSelect_2;
    end
    if(reset) begin
      config__outSelect_1 <= 2'h1;
    end else begin
      config__outSelect_1 <= configIn_outSelect_1;
    end
    if(reset) begin
      config__outSelect_0 <= 2'h3;
    end else begin
      config__outSelect_0 <= configIn_outSelect_0;
    end
  end
endmodule

module FF_2(input clk, input reset,
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

module UpDownCtr(input clk, input reset,
    input [3:0] io_initval,
    input  io_init,
    input  io_inc,
    input  io_dec,
    output io_gtz
);

  wire T0;
  wire T1;
  wire[3:0] T2;
  wire[3:0] T3;
  wire[3:0] decval;
  wire[3:0] incval;
  wire T4;
  wire[3:0] reg__io_data_out;


  assign T0 = T1 | io_init;
  assign T1 = io_inc ^ io_dec;
  assign T2 = io_init ? io_initval : T3;
  assign T3 = io_inc ? incval : decval;
  assign decval = reg__io_data_out - 4'h1;
  assign incval = reg__io_data_out + 4'h1;
  assign io_gtz = T4;
  assign T4 = 4'h0 < reg__io_data_out;
  FF_2 reg_(.clk(clk), .reset(reset),
       .io_data_in( T2 ),
       .io_data_init( 4'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( T0 )
  );
endmodule

module CUControlBox(input clk, input reset,
    input  io_config_enable,
    input  io_tokenIns_1,
    input  io_tokenIns_0,
    input  io_done_1,
    input  io_done_0,
    output io_tokenOuts_1,
    output io_tokenOuts_0,
    output io_enable_1,
    output io_enable_0
);

  reg [3:0] config__udcInit_1;
  wire[3:0] T2;
  wire[3:0] configIn_udcInit_1;
  wire[3:0] T0;
  wire[3:0] configType_udcInit_1;
  reg [3:0] config__udcInit_0;
  wire[3:0] T3;
  wire[3:0] configIn_udcInit_0;
  wire[3:0] T1;
  wire[3:0] configType_udcInit_0;
  wire LUT_io_out;
  wire LUT_1_io_out;
  wire LUT_2_io_out;
  wire LUT_3_io_out;
  wire decXbar_io_outs_1;
  wire decXbar_io_outs_0;
  wire incXbar_io_outs_3;
  wire incXbar_io_outs_2;
  wire incXbar_io_outs_1;
  wire incXbar_io_outs_0;
  wire UpDownCtr_io_gtz;
  wire UpDownCtr_1_io_gtz;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__udcInit_1 = {1{$random}};
    config__udcInit_0 = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_udcInit_1 = {1{$random}};
  assign configType_udcInit_0 = {1{$random}};
// synthesis translate_on
`endif
  assign T2 = reset ? 4'h0 : configIn_udcInit_1;
  assign configIn_udcInit_1 = T0;
  assign T0 = io_config_enable ? configType_udcInit_1 : config__udcInit_1;
  assign T3 = reset ? 4'h3 : configIn_udcInit_0;
  assign configIn_udcInit_0 = T1;
  assign T1 = io_config_enable ? configType_udcInit_0 : config__udcInit_0;
  assign io_enable_0 = LUT_2_io_out;
  assign io_enable_1 = LUT_3_io_out;
  assign io_tokenOuts_0 = LUT_io_out;
  assign io_tokenOuts_1 = LUT_1_io_out;
  LUT LUT(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_ins_1( io_done_1 ),
       .io_ins_0( io_done_0 ),
       .io_out( LUT_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign LUT.io_config_enable = {1{$random}};
// synthesis translate_on
`endif
  LUT LUT_1(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_ins_1( io_done_1 ),
       .io_ins_0( io_done_0 ),
       .io_out( LUT_1_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign LUT_1.io_config_enable = {1{$random}};
// synthesis translate_on
`endif
  Crossbar_0 decXbar(.clk(clk), .reset(reset),
       .io_config_enable( io_config_enable ),
       //.io_ins_2(  )
       //.io_ins_1(  )
       //.io_ins_0(  )
       .io_outs_1( decXbar_io_outs_1 ),
       .io_outs_0( decXbar_io_outs_0 )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign decXbar.io_ins_2 = {1{$random}};
    assign decXbar.io_ins_1 = {1{$random}};
    assign decXbar.io_ins_0 = {1{$random}};
// synthesis translate_on
`endif
  Crossbar_1 incXbar(.clk(clk), .reset(reset),
       .io_config_enable( io_config_enable ),
       //.io_ins_2(  )
       //.io_ins_1(  )
       //.io_ins_0(  )
       .io_outs_3( incXbar_io_outs_3 ),
       .io_outs_2( incXbar_io_outs_2 ),
       .io_outs_1( incXbar_io_outs_1 ),
       .io_outs_0( incXbar_io_outs_0 )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign incXbar.io_ins_2 = {1{$random}};
    assign incXbar.io_ins_1 = {1{$random}};
    assign incXbar.io_ins_0 = {1{$random}};
// synthesis translate_on
`endif
  UpDownCtr UpDownCtr(.clk(clk), .reset(reset),
       .io_initval( config__udcInit_0 ),
       .io_init( incXbar_io_outs_2 ),
       .io_inc( incXbar_io_outs_0 ),
       .io_dec( decXbar_io_outs_0 ),
       .io_gtz( UpDownCtr_io_gtz )
  );
  UpDownCtr UpDownCtr_1(.clk(clk), .reset(reset),
       .io_initval( config__udcInit_1 ),
       .io_init( incXbar_io_outs_3 ),
       .io_inc( incXbar_io_outs_1 ),
       .io_dec( decXbar_io_outs_1 ),
       .io_gtz( UpDownCtr_1_io_gtz )
  );
  LUT LUT_2(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_ins_1( UpDownCtr_1_io_gtz ),
       .io_ins_0( UpDownCtr_io_gtz ),
       .io_out( LUT_2_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign LUT_2.io_config_enable = {1{$random}};
// synthesis translate_on
`endif
  LUT LUT_3(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_ins_1( UpDownCtr_1_io_gtz ),
       .io_ins_0( UpDownCtr_io_gtz ),
       .io_out( LUT_3_io_out )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign LUT_3.io_config_enable = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config__udcInit_1 <= 4'h0;
    end else begin
      config__udcInit_1 <= configIn_udcInit_1;
    end
    if(reset) begin
      config__udcInit_0 <= 4'h3;
    end else begin
      config__udcInit_0 <= configIn_udcInit_0;
    end
  end
endmodule

module MuxN_0(
    input [7:0] io_ins_3,
    input [7:0] io_ins_2,
    input [7:0] io_ins_1,
    input [7:0] io_ins_0,
    input [1:0] io_sel,
    output[7:0] io_out
);

  wire[7:0] T0;
  wire[7:0] T1;
  wire T2;
  wire[1:0] T3;
  wire[7:0] T4;
  wire T5;
  wire T6;


  assign io_out = T0;
  assign T0 = T6 ? T4 : T1;
  assign T1 = T2 ? io_ins_1 : io_ins_0;
  assign T2 = T3[0];
  assign T3 = io_sel;
  assign T4 = T5 ? io_ins_3 : io_ins_2;
  assign T5 = T3[0];
  assign T6 = T3[1];
endmodule

module MuxN_2(
    input [7:0] io_ins_8,
    input [7:0] io_ins_7,
    input [7:0] io_ins_6,
    input [7:0] io_ins_5,
    input [7:0] io_ins_4,
    input [7:0] io_ins_3,
    input [7:0] io_ins_2,
    input [7:0] io_ins_1,
    input [7:0] io_ins_0,
    input [3:0] io_sel,
    output[7:0] io_out
);

  wire[7:0] T0;
  wire[7:0] T1;
  wire[7:0] T2;
  wire[7:0] T3;
  wire T4;
  wire[3:0] T5;
  wire[7:0] T6;
  wire T7;
  wire T8;
  wire[7:0] T9;
  wire[7:0] T10;
  wire T11;
  wire[7:0] T12;
  wire T13;
  wire T14;
  wire T15;
  wire T16;


  assign io_out = T0;
  assign T0 = T16 ? io_ins_8 : T1;
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
  assign T16 = T5[3];
endmodule

module IntFU(
    input [7:0] io_a,
    input [7:0] io_b,
    input [3:0] io_opcode,
    output[7:0] io_out
);

  wire[7:0] T0;
  wire[7:0] T1;
  wire[7:0] T7;
  wire[15:0] T2;
  wire[7:0] T3;
  wire[7:0] T4;
  wire[7:0] T5;
  wire[7:0] T8;
  wire T6;
  wire[7:0] m_io_out;


  assign T0 = io_a + io_b;
  assign T1 = io_a - io_b;
  assign T7 = T2[7:0];
  assign T2 = io_a * io_b;
  assign T3 = io_a / io_b;
  assign T4 = io_a & io_b;
  assign T5 = io_a | io_b;
  assign T8 = {7'h0, T6};
  assign T6 = io_a == io_b;
  assign io_out = m_io_out;
  MuxN_2 m(
       .io_ins_8( io_b ),
       .io_ins_7( io_a ),
       .io_ins_6( T8 ),
       .io_ins_5( T5 ),
       .io_ins_4( T4 ),
       .io_ins_3( T3 ),
       .io_ins_2( T7 ),
       .io_ins_1( T1 ),
       .io_ins_0( T0 ),
       .io_sel( io_opcode ),
       .io_out( m_io_out )
  );
endmodule

module MuxN_3(
    input [7:0] io_ins_5,
    input [7:0] io_ins_4,
    input [7:0] io_ins_3,
    input [7:0] io_ins_2,
    input [7:0] io_ins_1,
    input [7:0] io_ins_0,
    input [2:0] io_sel,
    output[7:0] io_out
);

  wire[7:0] T0;
  wire[7:0] T1;
  wire[7:0] T2;
  wire T3;
  wire[2:0] T4;
  wire[7:0] T5;
  wire T6;
  wire T7;
  wire[7:0] T8;
  wire T9;
  wire T10;


  assign io_out = T0;
  assign T0 = T10 ? T8 : T1;
  assign T1 = T7 ? T5 : T2;
  assign T2 = T3 ? io_ins_1 : io_ins_0;
  assign T3 = T4[0];
  assign T4 = io_sel;
  assign T5 = T6 ? io_ins_3 : io_ins_2;
  assign T6 = T4[0];
  assign T7 = T4[1];
  assign T8 = T9 ? io_ins_5 : io_ins_4;
  assign T9 = T4[0];
  assign T10 = T4[2];
endmodule

module RegisterBlock(input clk, input reset,
    input [7:0] io_writeData,
    input [7:0] io_passData_3,
    input [7:0] io_passData_2,
    input [7:0] io_passData_1,
    input [7:0] io_passData_0,
    input [5:0] io_writeSel,
    input [2:0] io_readLocalASel,
    input [2:0] io_readLocalBSel,
    input [1:0] io_readRemoteASel,
    input [1:0] io_readRemoteBSel,
    output[7:0] io_readLocalA,
    output[7:0] io_readLocalB,
    output[7:0] io_readRemoteA,
    output[7:0] io_readRemoteB,
    output[7:0] io_passDataOut_3,
    output[7:0] io_passDataOut_2,
    output[7:0] io_passDataOut_1,
    output[7:0] io_passDataOut_0
);

  wire[7:0] T0;
  wire T1;
  wire[7:0] T2;
  wire T3;
  wire[7:0] T4;
  wire T5;
  wire[7:0] T6;
  wire T7;
  wire T8;
  wire T9;
  wire[7:0] FF_io_data_out;
  wire[7:0] FF_1_io_data_out;
  wire[7:0] FF_2_io_data_out;
  wire[7:0] FF_3_io_data_out;
  wire[7:0] FF_4_io_data_out;
  wire[7:0] FF_5_io_data_out;
  wire[7:0] readLocalAMux_io_out;
  wire[7:0] readLocalBMux_io_out;
  wire[7:0] readRemoteAMux_io_out;
  wire[7:0] readRemoteBMux_io_out;


  assign T0 = T1 ? io_writeData : io_passData_3;
  assign T1 = io_writeSel[5];
  assign T2 = T3 ? io_writeData : io_passData_2;
  assign T3 = io_writeSel[4];
  assign T4 = T5 ? io_writeData : io_passData_1;
  assign T5 = io_writeSel[3];
  assign T6 = T7 ? io_writeData : io_passData_0;
  assign T7 = io_writeSel[2];
  assign T8 = io_writeSel[1];
  assign T9 = io_writeSel[0];
  assign io_passDataOut_0 = FF_2_io_data_out;
  assign io_passDataOut_1 = FF_3_io_data_out;
  assign io_passDataOut_2 = FF_4_io_data_out;
  assign io_passDataOut_3 = FF_5_io_data_out;
  assign io_readRemoteB = readRemoteBMux_io_out;
  assign io_readRemoteA = readRemoteAMux_io_out;
  assign io_readLocalB = readLocalBMux_io_out;
  assign io_readLocalA = readLocalAMux_io_out;
  FF_1 FF(.clk(clk), .reset(reset),
       .io_data_in( io_writeData ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_io_data_out ),
       .io_control_enable( T9 )
  );
  FF_1 FF_1(.clk(clk), .reset(reset),
       .io_data_in( io_writeData ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_1_io_data_out ),
       .io_control_enable( T8 )
  );
  FF_1 FF_2(.clk(clk), .reset(reset),
       .io_data_in( T6 ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_2_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  FF_1 FF_3(.clk(clk), .reset(reset),
       .io_data_in( T4 ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_3_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  FF_1 FF_4(.clk(clk), .reset(reset),
       .io_data_in( T2 ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_4_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  FF_1 FF_5(.clk(clk), .reset(reset),
       .io_data_in( T0 ),
       .io_data_init( 8'h0 ),
       .io_data_out( FF_5_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  MuxN_3 readLocalAMux(
       .io_ins_5( FF_5_io_data_out ),
       .io_ins_4( FF_4_io_data_out ),
       .io_ins_3( FF_3_io_data_out ),
       .io_ins_2( FF_2_io_data_out ),
       .io_ins_1( FF_1_io_data_out ),
       .io_ins_0( FF_io_data_out ),
       .io_sel( io_readLocalASel ),
       .io_out( readLocalAMux_io_out )
  );
  MuxN_3 readLocalBMux(
       .io_ins_5( FF_5_io_data_out ),
       .io_ins_4( FF_4_io_data_out ),
       .io_ins_3( FF_3_io_data_out ),
       .io_ins_2( FF_2_io_data_out ),
       .io_ins_1( FF_1_io_data_out ),
       .io_ins_0( FF_io_data_out ),
       .io_sel( io_readLocalBSel ),
       .io_out( readLocalBMux_io_out )
  );
  MuxN_0 readRemoteAMux(
       .io_ins_3( FF_5_io_data_out ),
       .io_ins_2( FF_4_io_data_out ),
       .io_ins_1( FF_3_io_data_out ),
       .io_ins_0( FF_2_io_data_out ),
       .io_sel( io_readRemoteASel ),
       .io_out( readRemoteAMux_io_out )
  );
  MuxN_0 readRemoteBMux(
       .io_ins_3( FF_5_io_data_out ),
       .io_ins_2( FF_4_io_data_out ),
       .io_ins_1( FF_3_io_data_out ),
       .io_ins_0( FF_2_io_data_out ),
       .io_sel( io_readRemoteBSel ),
       .io_out( readRemoteBMux_io_out )
  );
endmodule

module MuxN_1(
    input [7:0] io_ins_2,
    input [7:0] io_ins_1,
    input [7:0] io_ins_0,
    input [1:0] io_sel,
    output[7:0] io_out
);

  wire[7:0] T0;
  wire[7:0] T1;
  wire T2;
  wire[1:0] T3;
  wire T4;


  assign io_out = T0;
  assign T0 = T4 ? io_ins_2 : T1;
  assign T1 = T2 ? io_ins_1 : io_ins_0;
  assign T2 = T3[0];
  assign T3 = io_sel;
  assign T4 = T3[1];
endmodule

module ComputeUnit(input clk, input reset,
    input  io_config_enable,
    input  io_tokenIns_1,
    input  io_tokenIns_0,
    output io_tokenOuts_1,
    output io_tokenOuts_0,
    output[7:0] io_scalarOut,
    input [7:0] io_dataIn_0,
    output[7:0] io_dataOut_0
);

  reg [1:0] config__pipeStage_1_opB_dataSrc;
  wire[1:0] T20;
  wire[1:0] configIn_pipeStage_1_opB_dataSrc;
  wire[1:0] T0;
  wire[1:0] configType_pipeStage_1_opB_dataSrc;
  wire[7:0] T21;
  reg [2:0] config__pipeStage_1_opB_value;
  wire[2:0] T22;
  wire[2:0] configIn_pipeStage_1_opB_value;
  wire[2:0] T1;
  wire[2:0] configType_pipeStage_1_opB_value;
  reg [1:0] config__pipeStage_1_opA_dataSrc;
  wire[1:0] T23;
  wire[1:0] configIn_pipeStage_1_opA_dataSrc;
  wire[1:0] T2;
  wire[1:0] configType_pipeStage_1_opA_dataSrc;
  wire[7:0] T24;
  reg [2:0] config__pipeStage_1_opA_value;
  wire[2:0] T25;
  wire[2:0] configIn_pipeStage_1_opA_value;
  wire[2:0] T3;
  wire[2:0] configType_pipeStage_1_opA_value;
  reg [5:0] config__pipeStage_1_result;
  wire[5:0] T26;
  wire[5:0] configIn_pipeStage_1_result;
  wire[5:0] T4;
  wire[5:0] configType_pipeStage_1_result;
  reg [3:0] config__pipeStage_1_opcode;
  wire[3:0] T27;
  wire[3:0] configIn_pipeStage_1_opcode;
  wire[3:0] T5;
  wire[3:0] configType_pipeStage_1_opcode;
  reg [1:0] config__pipeStage_0_opB_dataSrc;
  wire[1:0] T28;
  wire[1:0] configIn_pipeStage_0_opB_dataSrc;
  wire[1:0] T6;
  wire[1:0] configType_pipeStage_0_opB_dataSrc;
  wire[7:0] T29;
  reg [2:0] config__pipeStage_0_opB_value;
  wire[2:0] T30;
  wire[2:0] configIn_pipeStage_0_opB_value;
  wire[2:0] T7;
  wire[2:0] configType_pipeStage_0_opB_value;
  reg [1:0] config__pipeStage_0_opA_dataSrc;
  wire[1:0] T31;
  wire[1:0] configIn_pipeStage_0_opA_dataSrc;
  wire[1:0] T8;
  wire[1:0] configType_pipeStage_0_opA_dataSrc;
  wire[7:0] T32;
  reg [2:0] config__pipeStage_0_opA_value;
  wire[2:0] T33;
  wire[2:0] configIn_pipeStage_0_opA_value;
  wire[2:0] T9;
  wire[2:0] configType_pipeStage_0_opA_value;
  wire[1:0] T34;
  wire[1:0] T35;
  reg [5:0] config__pipeStage_0_result;
  wire[5:0] T36;
  wire[5:0] configIn_pipeStage_0_result;
  wire[5:0] T10;
  wire[5:0] configType_pipeStage_0_result;
  reg [3:0] config__pipeStage_0_opcode;
  wire[3:0] T37;
  wire[3:0] configIn_pipeStage_0_opcode;
  wire[3:0] T11;
  wire[3:0] configType_pipeStage_0_opcode;
  reg [1:0] config__remoteMux1;
  wire[1:0] T38;
  wire[1:0] configIn_remoteMux1;
  wire[1:0] T12;
  wire[1:0] configType_remoteMux1;
  reg [1:0] config__remoteMux0;
  wire[1:0] T39;
  wire[1:0] configIn_remoteMux0;
  wire[1:0] T13;
  wire[1:0] configType_remoteMux0;
  wire counterEnables_0;
  wire counterEnables_1;
  reg  config__mem1ra;
  wire T40;
  wire configIn_mem1ra;
  wire T14;
  wire configType_mem1ra;
  wire[3:0] T41;
  reg  config__mem1wd;
  wire T42;
  wire configIn_mem1wd;
  wire T15;
  wire configType_mem1wd;
  reg  config__mem1wa;
  wire T43;
  wire configIn_mem1wa;
  wire T16;
  wire configType_mem1wa;
  wire[3:0] T44;
  wire[3:0] T45;
  reg  config__mem0ra;
  wire T46;
  wire configIn_mem0ra;
  wire T17;
  wire configType_mem0ra;
  wire[3:0] T47;
  reg  config__mem0wd;
  wire T48;
  wire configIn_mem0wd;
  wire T18;
  wire configType_mem0wd;
  reg  config__mem0wa;
  wire T49;
  wire configIn_mem0wa;
  wire T19;
  wire configType_mem0wa;
  wire[3:0] T50;
  wire[3:0] T51;
  wire[7:0] mem0_io_rdata;
  wire[3:0] mem0waMux_io_out_0;
  wire[7:0] mem0wdMux_io_out_0;
  wire[3:0] mem0raMux_io_out_0;
  wire[7:0] mem1_io_rdata;
  wire[3:0] mem1waMux_io_out_0;
  wire[7:0] mem1wdMux_io_out_0;
  wire[3:0] mem1raMux_io_out_0;
  wire[7:0] remoteMux0_io_out;
  wire[7:0] remoteMux1_io_out;
  wire[7:0] MuxN_io_out;
  wire[7:0] MuxN_1_io_out;
  wire[7:0] MuxN_2_io_out;
  wire[7:0] MuxN_3_io_out;
  wire[7:0] IntFU_io_out;
  wire[7:0] RegisterBlock_io_readLocalA;
  wire[7:0] RegisterBlock_io_readLocalB;
  wire[7:0] RegisterBlock_io_readRemoteA;
  wire[7:0] RegisterBlock_io_readRemoteB;
  wire[7:0] RegisterBlock_io_passDataOut_3;
  wire[7:0] RegisterBlock_io_passDataOut_2;
  wire[7:0] RegisterBlock_io_passDataOut_1;
  wire[7:0] RegisterBlock_io_passDataOut_0;
  wire[7:0] IntFU_1_io_out;
  wire[7:0] RegisterBlock_1_io_readLocalA;
  wire[7:0] RegisterBlock_1_io_readLocalB;
  wire controlBlock_io_tokenOuts_1;
  wire controlBlock_io_tokenOuts_0;
  wire controlBlock_io_enable_1;
  wire controlBlock_io_enable_0;
  wire[7:0] counterChain_io_data_1_out;
  wire[7:0] counterChain_io_data_0_out;
  wire counterChain_io_control_1_done;
  wire counterChain_io_control_0_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config__pipeStage_1_opB_dataSrc = {1{$random}};
    config__pipeStage_1_opB_value = {1{$random}};
    config__pipeStage_1_opA_dataSrc = {1{$random}};
    config__pipeStage_1_opA_value = {1{$random}};
    config__pipeStage_1_result = {1{$random}};
    config__pipeStage_1_opcode = {1{$random}};
    config__pipeStage_0_opB_dataSrc = {1{$random}};
    config__pipeStage_0_opB_value = {1{$random}};
    config__pipeStage_0_opA_dataSrc = {1{$random}};
    config__pipeStage_0_opA_value = {1{$random}};
    config__pipeStage_0_result = {1{$random}};
    config__pipeStage_0_opcode = {1{$random}};
    config__remoteMux1 = {1{$random}};
    config__remoteMux0 = {1{$random}};
    config__mem1ra = {1{$random}};
    config__mem1wd = {1{$random}};
    config__mem1wa = {1{$random}};
    config__mem0ra = {1{$random}};
    config__mem0wd = {1{$random}};
    config__mem0wa = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_pipeStage_1_opB_dataSrc = {1{$random}};
  assign configType_pipeStage_1_opB_value = {1{$random}};
  assign configType_pipeStage_1_opA_dataSrc = {1{$random}};
  assign configType_pipeStage_1_opA_value = {1{$random}};
  assign configType_pipeStage_1_result = {1{$random}};
  assign configType_pipeStage_1_opcode = {1{$random}};
  assign configType_pipeStage_0_opB_dataSrc = {1{$random}};
  assign configType_pipeStage_0_opB_value = {1{$random}};
  assign configType_pipeStage_0_opA_dataSrc = {1{$random}};
  assign configType_pipeStage_0_opA_value = {1{$random}};
  assign configType_pipeStage_0_result = {1{$random}};
  assign configType_pipeStage_0_opcode = {1{$random}};
  assign configType_remoteMux1 = {1{$random}};
  assign configType_remoteMux0 = {1{$random}};
  assign configType_mem1ra = {1{$random}};
  assign configType_mem1wd = {1{$random}};
  assign configType_mem1wa = {1{$random}};
  assign configType_mem0ra = {1{$random}};
  assign configType_mem0wd = {1{$random}};
  assign configType_mem0wa = {1{$random}};
// synthesis translate_on
`endif
  assign T20 = reset ? 2'h0 : configIn_pipeStage_1_opB_dataSrc;
  assign configIn_pipeStage_1_opB_dataSrc = T0;
  assign T0 = io_config_enable ? configType_pipeStage_1_opB_dataSrc : config__pipeStage_1_opB_dataSrc;
  assign T21 = {5'h0, config__pipeStage_1_opB_value};
  assign T22 = reset ? 3'h1 : configIn_pipeStage_1_opB_value;
  assign configIn_pipeStage_1_opB_value = T1;
  assign T1 = io_config_enable ? configType_pipeStage_1_opB_value : config__pipeStage_1_opB_value;
  assign T23 = reset ? 2'h1 : configIn_pipeStage_1_opA_dataSrc;
  assign configIn_pipeStage_1_opA_dataSrc = T2;
  assign T2 = io_config_enable ? configType_pipeStage_1_opA_dataSrc : config__pipeStage_1_opA_dataSrc;
  assign T24 = {5'h0, config__pipeStage_1_opA_value};
  assign T25 = reset ? 3'h1 : configIn_pipeStage_1_opA_value;
  assign configIn_pipeStage_1_opA_value = T3;
  assign T3 = io_config_enable ? configType_pipeStage_1_opA_value : config__pipeStage_1_opA_value;
  assign T26 = reset ? 6'h2 : configIn_pipeStage_1_result;
  assign configIn_pipeStage_1_result = T4;
  assign T4 = io_config_enable ? configType_pipeStage_1_result : config__pipeStage_1_result;
  assign T27 = reset ? 4'h0 : configIn_pipeStage_1_opcode;
  assign configIn_pipeStage_1_opcode = T5;
  assign T5 = io_config_enable ? configType_pipeStage_1_opcode : config__pipeStage_1_opcode;
  assign T28 = reset ? 2'h1 : configIn_pipeStage_0_opB_dataSrc;
  assign configIn_pipeStage_0_opB_dataSrc = T6;
  assign T6 = io_config_enable ? configType_pipeStage_0_opB_dataSrc : config__pipeStage_0_opB_dataSrc;
  assign T29 = {5'h0, config__pipeStage_0_opB_value};
  assign T30 = reset ? 3'h1 : configIn_pipeStage_0_opB_value;
  assign configIn_pipeStage_0_opB_value = T7;
  assign T7 = io_config_enable ? configType_pipeStage_0_opB_value : config__pipeStage_0_opB_value;
  assign T31 = reset ? 2'h1 : configIn_pipeStage_0_opA_dataSrc;
  assign configIn_pipeStage_0_opA_dataSrc = T8;
  assign T8 = io_config_enable ? configType_pipeStage_0_opA_dataSrc : config__pipeStage_0_opA_dataSrc;
  assign T32 = {5'h0, config__pipeStage_0_opA_value};
  assign T33 = reset ? 3'h0 : configIn_pipeStage_0_opA_value;
  assign configIn_pipeStage_0_opA_value = T9;
  assign T9 = io_config_enable ? configType_pipeStage_0_opA_value : config__pipeStage_0_opA_value;
  assign T34 = config__pipeStage_1_opB_value[1:0];
  assign T35 = config__pipeStage_1_opA_value[1:0];
  assign T36 = reset ? 6'h8 : configIn_pipeStage_0_result;
  assign configIn_pipeStage_0_result = T10;
  assign T10 = io_config_enable ? configType_pipeStage_0_result : config__pipeStage_0_result;
  assign T37 = reset ? 4'h2 : configIn_pipeStage_0_opcode;
  assign configIn_pipeStage_0_opcode = T11;
  assign T11 = io_config_enable ? configType_pipeStage_0_opcode : config__pipeStage_0_opcode;
  assign T38 = reset ? 2'h1 : configIn_remoteMux1;
  assign configIn_remoteMux1 = T12;
  assign T12 = io_config_enable ? configType_remoteMux1 : config__remoteMux1;
  assign T39 = reset ? 2'h0 : configIn_remoteMux0;
  assign configIn_remoteMux0 = T13;
  assign T13 = io_config_enable ? configType_remoteMux0 : config__remoteMux0;
  assign counterEnables_0 = controlBlock_io_enable_0;
  assign counterEnables_1 = controlBlock_io_enable_1;
  assign T40 = reset ? 1'h0 : configIn_mem1ra;
  assign configIn_mem1ra = T14;
  assign T14 = io_config_enable ? configType_mem1ra : config__mem1ra;
  assign T41 = IntFU_io_out[3:0];
  assign T42 = reset ? 1'h0 : configIn_mem1wd;
  assign configIn_mem1wd = T15;
  assign T15 = io_config_enable ? configType_mem1wd : config__mem1wd;
  assign T43 = reset ? 1'h0 : configIn_mem1wa;
  assign configIn_mem1wa = T16;
  assign T16 = io_config_enable ? configType_mem1wa : config__mem1wa;
  assign T44 = IntFU_io_out[3:0];
  assign T45 = IntFU_1_io_out[3:0];
  assign T46 = reset ? 1'h0 : configIn_mem0ra;
  assign configIn_mem0ra = T17;
  assign T17 = io_config_enable ? configType_mem0ra : config__mem0ra;
  assign T47 = IntFU_io_out[3:0];
  assign T48 = reset ? 1'h0 : configIn_mem0wd;
  assign configIn_mem0wd = T18;
  assign T18 = io_config_enable ? configType_mem0wd : config__mem0wd;
  assign T49 = reset ? 1'h0 : configIn_mem0wa;
  assign configIn_mem0wa = T19;
  assign T19 = io_config_enable ? configType_mem0wa : config__mem0wa;
  assign T50 = IntFU_io_out[3:0];
  assign T51 = IntFU_1_io_out[3:0];
  assign io_dataOut_0 = IntFU_1_io_out;
  assign io_scalarOut = IntFU_1_io_out;
  assign io_tokenOuts_0 = controlBlock_io_tokenOuts_0;
  assign io_tokenOuts_1 = controlBlock_io_tokenOuts_1;
  SRAM mem0(.clk(clk),
       .io_raddr( mem0raMux_io_out_0 ),
       .io_wen( 1'h1 ),
       .io_waddr( mem0waMux_io_out_0 ),
       .io_wdata( mem0wdMux_io_out_0 ),
       .io_rdata( mem0_io_rdata )
  );
  MuxVec_0 mem0waMux(
       .io_ins_1_0( T51 ),
       .io_ins_0_0( T50 ),
       .io_sel( config__mem0wa ),
       .io_out_0( mem0waMux_io_out_0 )
  );
  MuxVec_1 mem0wdMux(
       .io_ins_1_0( io_dataIn_0 ),
       .io_ins_0_0( IntFU_1_io_out ),
       .io_sel( config__mem0wd ),
       .io_out_0( mem0wdMux_io_out_0 )
  );
  MuxVec_2 mem0raMux(
       .io_ins_0_0( T47 ),
       .io_sel( config__mem0ra ),
       .io_out_0( mem0raMux_io_out_0 )
  );
  SRAM mem1(.clk(clk),
       .io_raddr( mem1raMux_io_out_0 ),
       .io_wen( 1'h1 ),
       .io_waddr( mem1waMux_io_out_0 ),
       .io_wdata( mem1wdMux_io_out_0 ),
       .io_rdata( mem1_io_rdata )
  );
  MuxVec_0 mem1waMux(
       .io_ins_1_0( T45 ),
       .io_ins_0_0( T44 ),
       .io_sel( config__mem1wa ),
       .io_out_0( mem1waMux_io_out_0 )
  );
  MuxVec_1 mem1wdMux(
       .io_ins_1_0( io_dataIn_0 ),
       .io_ins_0_0( IntFU_1_io_out ),
       .io_sel( config__mem1wd ),
       .io_out_0( mem1wdMux_io_out_0 )
  );
  MuxVec_2 mem1raMux(
       .io_ins_0_0( T41 ),
       .io_sel( config__mem1ra ),
       .io_out_0( mem1raMux_io_out_0 )
  );
  CounterChain counterChain(.clk(clk), .reset(reset),
       //.io_config_enable(  )
       .io_data_1_max( 8'h0 ),
       .io_data_1_stride( 8'h0 ),
       .io_data_1_out( counterChain_io_data_1_out ),
       .io_data_0_max( 8'h0 ),
       .io_data_0_stride( 8'h0 ),
       .io_data_0_out( counterChain_io_data_0_out ),
       .io_control_1_enable( counterEnables_1 ),
       .io_control_1_done( counterChain_io_control_1_done ),
       .io_control_0_enable( counterEnables_0 ),
       .io_control_0_done( counterChain_io_control_0_done )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign counterChain.io_config_enable = {1{$random}};
// synthesis translate_on
`endif
  CUControlBox controlBlock(.clk(clk), .reset(reset),
       .io_config_enable( io_config_enable ),
       .io_tokenIns_1( io_tokenIns_1 ),
       .io_tokenIns_0( io_tokenIns_0 ),
       .io_done_1( counterChain_io_control_1_done ),
       .io_done_0( counterChain_io_control_0_done ),
       .io_tokenOuts_1( controlBlock_io_tokenOuts_1 ),
       .io_tokenOuts_0( controlBlock_io_tokenOuts_0 ),
       .io_enable_1( controlBlock_io_enable_1 ),
       .io_enable_0( controlBlock_io_enable_0 )
  );
  MuxN_0 remoteMux0(
       .io_ins_3( mem1_io_rdata ),
       .io_ins_2( mem0_io_rdata ),
       .io_ins_1( counterChain_io_data_1_out ),
       .io_ins_0( counterChain_io_data_0_out ),
       .io_sel( config__remoteMux0 ),
       .io_out( remoteMux0_io_out )
  );
  MuxN_0 remoteMux1(
       .io_ins_3( mem1_io_rdata ),
       .io_ins_2( mem0_io_rdata ),
       .io_ins_1( counterChain_io_data_1_out ),
       .io_ins_0( counterChain_io_data_0_out ),
       .io_sel( config__remoteMux1 ),
       .io_out( remoteMux1_io_out )
  );
  IntFU IntFU(
       .io_a( MuxN_io_out ),
       .io_b( MuxN_1_io_out ),
       .io_opcode( config__pipeStage_0_opcode ),
       .io_out( IntFU_io_out )
  );
  RegisterBlock RegisterBlock(.clk(clk), .reset(reset),
       .io_writeData( IntFU_io_out ),
       .io_passData_3( mem1_io_rdata ),
       .io_passData_2( mem0_io_rdata ),
       .io_passData_1( counterChain_io_data_1_out ),
       .io_passData_0( counterChain_io_data_0_out ),
       .io_writeSel( config__pipeStage_0_result ),
       .io_readLocalASel( config__pipeStage_0_opA_value ),
       .io_readLocalBSel( config__pipeStage_0_opB_value ),
       .io_readRemoteASel( T35 ),
       .io_readRemoteBSel( T34 ),
       .io_readLocalA( RegisterBlock_io_readLocalA ),
       .io_readLocalB( RegisterBlock_io_readLocalB ),
       .io_readRemoteA( RegisterBlock_io_readRemoteA ),
       .io_readRemoteB( RegisterBlock_io_readRemoteB ),
       .io_passDataOut_3( RegisterBlock_io_passDataOut_3 ),
       .io_passDataOut_2( RegisterBlock_io_passDataOut_2 ),
       .io_passDataOut_1( RegisterBlock_io_passDataOut_1 ),
       .io_passDataOut_0( RegisterBlock_io_passDataOut_0 )
  );
  MuxN_0 MuxN(
       .io_ins_3( mem0_io_rdata ),
       .io_ins_2( T32 ),
       .io_ins_1( remoteMux0_io_out ),
       .io_ins_0( RegisterBlock_io_readLocalA ),
       .io_sel( config__pipeStage_0_opA_dataSrc ),
       .io_out( MuxN_io_out )
  );
  MuxN_0 MuxN_1(
       .io_ins_3( mem1_io_rdata ),
       .io_ins_2( T29 ),
       .io_ins_1( remoteMux1_io_out ),
       .io_ins_0( RegisterBlock_io_readLocalB ),
       .io_sel( config__pipeStage_0_opB_dataSrc ),
       .io_out( MuxN_1_io_out )
  );
  IntFU IntFU_1(
       .io_a( MuxN_2_io_out ),
       .io_b( MuxN_3_io_out ),
       .io_opcode( config__pipeStage_1_opcode ),
       .io_out( IntFU_1_io_out )
  );
  RegisterBlock RegisterBlock_1(.clk(clk), .reset(reset),
       .io_writeData( IntFU_1_io_out ),
       .io_passData_3( RegisterBlock_io_passDataOut_3 ),
       .io_passData_2( RegisterBlock_io_passDataOut_2 ),
       .io_passData_1( RegisterBlock_io_passDataOut_1 ),
       .io_passData_0( RegisterBlock_io_passDataOut_0 ),
       .io_writeSel( config__pipeStage_1_result ),
       .io_readLocalASel( config__pipeStage_1_opA_value ),
       .io_readLocalBSel( config__pipeStage_1_opB_value ),
       //.io_readRemoteASel(  )
       //.io_readRemoteBSel(  )
       .io_readLocalA( RegisterBlock_1_io_readLocalA ),
       .io_readLocalB( RegisterBlock_1_io_readLocalB )
       //.io_readRemoteA(  )
       //.io_readRemoteB(  )
       //.io_passDataOut_3(  )
       //.io_passDataOut_2(  )
       //.io_passDataOut_1(  )
       //.io_passDataOut_0(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign RegisterBlock_1.io_readRemoteASel = {1{$random}};
    assign RegisterBlock_1.io_readRemoteBSel = {1{$random}};
// synthesis translate_on
`endif
  MuxN_1 MuxN_2(
       .io_ins_2( T24 ),
       .io_ins_1( RegisterBlock_io_readRemoteA ),
       .io_ins_0( RegisterBlock_1_io_readLocalA ),
       .io_sel( config__pipeStage_1_opA_dataSrc ),
       .io_out( MuxN_2_io_out )
  );
  MuxN_1 MuxN_3(
       .io_ins_2( T21 ),
       .io_ins_1( RegisterBlock_io_readRemoteB ),
       .io_ins_0( RegisterBlock_1_io_readLocalB ),
       .io_sel( config__pipeStage_1_opB_dataSrc ),
       .io_out( MuxN_3_io_out )
  );

  always @(posedge clk) begin
    if(reset) begin
      config__pipeStage_1_opB_dataSrc <= 2'h0;
    end else begin
      config__pipeStage_1_opB_dataSrc <= configIn_pipeStage_1_opB_dataSrc;
    end
    if(reset) begin
      config__pipeStage_1_opB_value <= 3'h1;
    end else begin
      config__pipeStage_1_opB_value <= configIn_pipeStage_1_opB_value;
    end
    if(reset) begin
      config__pipeStage_1_opA_dataSrc <= 2'h1;
    end else begin
      config__pipeStage_1_opA_dataSrc <= configIn_pipeStage_1_opA_dataSrc;
    end
    if(reset) begin
      config__pipeStage_1_opA_value <= 3'h1;
    end else begin
      config__pipeStage_1_opA_value <= configIn_pipeStage_1_opA_value;
    end
    if(reset) begin
      config__pipeStage_1_result <= 6'h2;
    end else begin
      config__pipeStage_1_result <= configIn_pipeStage_1_result;
    end
    if(reset) begin
      config__pipeStage_1_opcode <= 4'h0;
    end else begin
      config__pipeStage_1_opcode <= configIn_pipeStage_1_opcode;
    end
    if(reset) begin
      config__pipeStage_0_opB_dataSrc <= 2'h1;
    end else begin
      config__pipeStage_0_opB_dataSrc <= configIn_pipeStage_0_opB_dataSrc;
    end
    if(reset) begin
      config__pipeStage_0_opB_value <= 3'h1;
    end else begin
      config__pipeStage_0_opB_value <= configIn_pipeStage_0_opB_value;
    end
    if(reset) begin
      config__pipeStage_0_opA_dataSrc <= 2'h1;
    end else begin
      config__pipeStage_0_opA_dataSrc <= configIn_pipeStage_0_opA_dataSrc;
    end
    if(reset) begin
      config__pipeStage_0_opA_value <= 3'h0;
    end else begin
      config__pipeStage_0_opA_value <= configIn_pipeStage_0_opA_value;
    end
    if(reset) begin
      config__pipeStage_0_result <= 6'h8;
    end else begin
      config__pipeStage_0_result <= configIn_pipeStage_0_result;
    end
    if(reset) begin
      config__pipeStage_0_opcode <= 4'h2;
    end else begin
      config__pipeStage_0_opcode <= configIn_pipeStage_0_opcode;
    end
    if(reset) begin
      config__remoteMux1 <= 2'h1;
    end else begin
      config__remoteMux1 <= configIn_remoteMux1;
    end
    if(reset) begin
      config__remoteMux0 <= 2'h0;
    end else begin
      config__remoteMux0 <= configIn_remoteMux0;
    end
    if(reset) begin
      config__mem1ra <= 1'h0;
    end else begin
      config__mem1ra <= configIn_mem1ra;
    end
    if(reset) begin
      config__mem1wd <= 1'h0;
    end else begin
      config__mem1wd <= configIn_mem1wd;
    end
    if(reset) begin
      config__mem1wa <= 1'h0;
    end else begin
      config__mem1wa <= configIn_mem1wa;
    end
    if(reset) begin
      config__mem0ra <= 1'h0;
    end else begin
      config__mem0ra <= configIn_mem0ra;
    end
    if(reset) begin
      config__mem0wd <= 1'h0;
    end else begin
      config__mem0wd <= configIn_mem0wd;
    end
    if(reset) begin
      config__mem0wa <= 1'h0;
    end else begin
      config__mem0wa <= configIn_mem0wa;
    end
  end
endmodule


