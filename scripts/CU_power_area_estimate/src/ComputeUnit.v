module FF(input clk, input reset,
    input [6:0] io_data_in,
    input [6:0] io_data_init,
    output[6:0] io_data_out,
    input  io_control_enable
);

  reg [6:0] ff;
  wire[6:0] T1;
  wire[6:0] d;
  wire[6:0] T0;

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
    input [6:0] io_data_max,
    input [6:0] io_data_stride,
    output[6:0] io_data_out,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire[6:0] T3;
  wire[7:0] T0;
  wire[7:0] T1;
  wire[7:0] newval;
  wire[7:0] T4;
  wire[7:0] count;
  wire[7:0] T2;
  wire isMax;
  wire[7:0] T5;
  wire[6:0] T6;
  wire[6:0] reg__io_data_out;


  assign T3 = T0[6:0];
  assign T0 = io_control_reset ? 8'h0 : T1;
  assign T1 = isMax ? T2 : newval;
  assign newval = count + T4;
  assign T4 = {1'h0, io_data_stride};
  assign count = {1'h0, reg__io_data_out};
  assign T2 = io_control_saturate ? count : 8'h0;
  assign isMax = T5 <= newval;
  assign T5 = {1'h0, io_data_max};
  assign io_control_done = isMax;
  assign io_data_out = T6;
  assign T6 = count[6:0];
  FF reg_(.clk(clk), .reset(reset),
       .io_data_in( T3 ),
       .io_data_init( 7'h0 ),
       .io_data_out( reg__io_data_out ),
       .io_control_enable( io_control_enable )
  );
endmodule

module CounterRC(input clk, input reset,
    input [6:0] io_data_max,
    input [6:0] io_data_stride,
    output[6:0] io_data_out,
    input  io_control_reset,
    input  io_control_enable,
    input  io_control_saturate,
    output io_control_done
);

  wire[6:0] T0;
  wire[6:0] T2;
  reg  config_stride;
  wire T3;
  reg  config_strideConst;
  wire T4;
  wire[6:0] T1;
  wire[6:0] T5;
  reg [2:0] config_max;
  wire[2:0] T6;
  reg  config_maxConst;
  wire T7;
  wire[6:0] counter_io_data_out;
  wire counter_io_control_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config_stride = {1{$random}};
    config_strideConst = {1{$random}};
    config_max = {1{$random}};
    config_maxConst = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T0 = config_strideConst ? T2 : io_data_stride;
  assign T2 = {6'h0, config_stride};
  assign T3 = reset ? 1'h1 : config_stride;
  assign T4 = reset ? 1'h1 : config_strideConst;
  assign T1 = config_maxConst ? T5 : io_data_max;
  assign T5 = {4'h0, config_max};
  assign T6 = reset ? 3'h5 : config_max;
  assign T7 = reset ? 1'h1 : config_maxConst;
  assign io_control_done = counter_io_control_done;
  assign io_data_out = counter_io_data_out;
  Counter counter(.clk(clk), .reset(reset),
       .io_data_max( T1 ),
       .io_data_stride( T0 ),
       .io_data_out( counter_io_data_out ),
       .io_control_reset( io_control_reset ),
       .io_control_enable( io_control_enable ),
       .io_control_saturate( io_control_saturate ),
       .io_control_done( counter_io_control_done )
  );

  always @(posedge clk) begin
    if(reset) begin
      config_stride <= 1'h1;
    end
    if(reset) begin
      config_strideConst <= 1'h1;
    end
    if(reset) begin
      config_max <= 3'h5;
    end
    if(reset) begin
      config_maxConst <= 1'h1;
    end
  end
endmodule

module CounterChain(input clk, input reset,
    input [6:0] io_data_1_max,
    input [6:0] io_data_1_stride,
    output[6:0] io_data_1_out,
    input [6:0] io_data_0_max,
    input [6:0] io_data_0_stride,
    output[6:0] io_data_0_out,
    input  io_control_1_enable,
    output io_control_1_done,
    input  io_control_0_enable,
    output io_control_0_done
);

  wire T0;
  wire T1;
  reg  config_chain;
  wire T4;
  wire T2;
  wire T3;
  wire[6:0] CounterRC_io_data_out;
  wire CounterRC_io_control_done;
  wire[6:0] CounterRC_1_io_data_out;
  wire CounterRC_1_io_control_done;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config_chain = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T0 = config_chain ? T1 : io_control_1_enable;
  assign T1 = io_control_1_enable & CounterRC_io_control_done;
  assign T4 = reset ? 1'h1 : config_chain;
  assign io_control_0_done = CounterRC_io_control_done;
  assign io_control_1_done = T2;
  assign T2 = config_chain ? T3 : CounterRC_1_io_control_done;
  assign T3 = CounterRC_io_control_done & CounterRC_1_io_control_done;
  assign io_data_0_out = CounterRC_io_data_out;
  assign io_data_1_out = CounterRC_1_io_data_out;
  CounterRC CounterRC(.clk(clk), .reset(reset),
       .io_data_max( io_data_0_max ),
       .io_data_stride( io_data_0_stride ),
       .io_data_out( CounterRC_io_data_out ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( io_control_0_enable ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( CounterRC_io_control_done )
  );
  CounterRC CounterRC_1(.clk(clk), .reset(reset),
       .io_data_max( io_data_1_max ),
       .io_data_stride( io_data_1_stride ),
       .io_data_out( CounterRC_1_io_data_out ),
       .io_control_reset( 1'h0 ),
       .io_control_enable( T0 ),
       .io_control_saturate( 1'h0 ),
       .io_control_done( CounterRC_1_io_control_done )
  );

  always @(posedge clk) begin
    if(reset) begin
      config_chain <= 1'h1;
    end
  end
endmodule

module Mux2(
    input  io_sel,
    input [6:0] io_in0,
    input [6:0] io_in1,
    output[6:0] io_out
);

  wire[6:0] T0;
  wire T1;


  assign io_out = T0;
  assign T0 = T1 ? io_in1 : io_in0;
  assign T1 = io_sel == 1'h1;
endmodule

module MuxN(
    input [6:0] io_ins_1,
    input [6:0] io_ins_0,
    input  io_sel,
    output[6:0] io_out
);

  wire[6:0] Mux2_io_out;


  assign io_out = Mux2_io_out;
  Mux2 Mux2(
       .io_sel( io_sel ),
       .io_in0( io_ins_0 ),
       .io_in1( io_ins_1 ),
       .io_out( Mux2_io_out )
  );
endmodule

module IntFU(
    input [6:0] io_a,
    input [6:0] io_b,
    input [3:0] io_opcode,
    output[6:0] io_out
);

  wire[6:0] T46;
  wire[13:0] T0;
  wire[13:0] T1;
  wire[13:0] T2;
  wire[13:0] T3;
  wire[13:0] T4;
  wire[13:0] T5;
  wire[13:0] T6;
  wire[13:0] T47;
  wire[6:0] T7;
  wire[6:0] T8;
  wire[6:0] T9;
  wire T10;
  wire[6:0] T11;
  wire T12;
  wire T13;
  wire T14;
  wire[13:0] T15;
  wire T16;
  wire T17;
  wire T18;
  wire T19;
  wire[13:0] T48;
  wire[6:0] T20;
  wire T21;
  wire T22;
  wire T23;
  wire T24;
  wire[13:0] T49;
  wire[6:0] T25;
  wire T26;
  wire T27;
  wire T28;
  wire T29;
  wire[13:0] T50;
  wire[6:0] T30;
  wire T31;
  wire T32;
  wire T33;
  wire T34;
  wire[13:0] T51;
  wire T35;
  wire T36;
  wire T37;
  wire T38;
  wire T39;
  wire[13:0] T52;
  wire T40;
  wire T41;
  wire T42;
  wire T43;
  wire[13:0] T53;
  wire T44;
  wire T45;


  assign io_out = T46;
  assign T46 = T0[6:0];
  assign T0 = T44 ? T53 : T1;
  assign T1 = T40 ? T52 : T2;
  assign T2 = T36 ? T51 : T3;
  assign T3 = T31 ? T50 : T4;
  assign T4 = T26 ? T49 : T5;
  assign T5 = T21 ? T48 : T6;
  assign T6 = T16 ? T15 : T47;
  assign T47 = {7'h0, T7};
  assign T7 = T12 ? T11 : T8;
  assign T8 = T10 ? T9 : 7'h0;
  assign T9 = io_a + io_b;
  assign T10 = io_opcode == 4'h0;
  assign T11 = io_a - io_b;
  assign T12 = T14 & T13;
  assign T13 = io_opcode == 4'h1;
  assign T14 = T10 ^ 1'h1;
  assign T15 = io_a * io_b;
  assign T16 = T18 & T17;
  assign T17 = io_opcode == 4'h2;
  assign T18 = T19 ^ 1'h1;
  assign T19 = T10 | T13;
  assign T48 = {7'h0, T20};
  assign T20 = io_a / io_b;
  assign T21 = T23 & T22;
  assign T22 = io_opcode == 4'h3;
  assign T23 = T24 ^ 1'h1;
  assign T24 = T19 | T17;
  assign T49 = {7'h0, T25};
  assign T25 = io_a & io_b;
  assign T26 = T28 & T27;
  assign T27 = io_opcode == 4'h4;
  assign T28 = T29 ^ 1'h1;
  assign T29 = T24 | T22;
  assign T50 = {7'h0, T30};
  assign T30 = io_a | io_b;
  assign T31 = T33 & T32;
  assign T32 = io_opcode == 4'h5;
  assign T33 = T34 ^ 1'h1;
  assign T34 = T29 | T27;
  assign T51 = {13'h0, T35};
  assign T35 = io_a == io_b;
  assign T36 = T38 & T37;
  assign T37 = io_opcode == 4'h6;
  assign T38 = T39 ^ 1'h1;
  assign T39 = T34 | T32;
  assign T52 = {7'h0, io_a};
  assign T40 = T42 & T41;
  assign T41 = io_opcode == 4'h7;
  assign T42 = T43 ^ 1'h1;
  assign T43 = T39 | T37;
  assign T53 = {7'h0, io_b};
  assign T44 = T45 ^ 1'h1;
  assign T45 = T43 | T41;
endmodule

module MuxN_1(
    input [6:0] io_ins_3,
    input [6:0] io_ins_2,
    input [6:0] io_ins_1,
    input [6:0] io_ins_0,
    input [1:0] io_sel,
    output[6:0] io_out
);

  wire T0;
  wire T1;
  wire T2;
  wire[6:0] Mux2_io_out;
  wire[6:0] MuxN_io_out;
  wire[6:0] MuxN_1_io_out;


  assign T0 = io_sel[1];
  assign T1 = io_sel[0];
  assign T2 = io_sel[0];
  assign io_out = Mux2_io_out;
  MuxN MuxN(
       .io_ins_1( io_ins_1 ),
       .io_ins_0( io_ins_0 ),
       .io_sel( T2 ),
       .io_out( MuxN_io_out )
  );
  MuxN MuxN_1(
       .io_ins_1( io_ins_3 ),
       .io_ins_0( io_ins_2 ),
       .io_sel( T1 ),
       .io_out( MuxN_1_io_out )
  );
  Mux2 Mux2(
       .io_sel( T0 ),
       .io_in0( MuxN_io_out ),
       .io_in1( MuxN_1_io_out ),
       .io_out( Mux2_io_out )
  );
endmodule

module RegisterBlock(input clk, input reset,
    input [6:0] io_writeData,
    input [6:0] io_passData_1,
    input [6:0] io_passData_0,
    input [3:0] io_writeSel,
    input [1:0] io_readLocalASel,
    input [1:0] io_readLocalBSel,
    input  io_readRemoteASel,
    input  io_readRemoteBSel,
    output[6:0] io_readLocalA,
    output[6:0] io_readLocalB,
    output[6:0] io_readRemoteA,
    output[6:0] io_readRemoteB,
    output[6:0] io_passDataOut_1,
    output[6:0] io_passDataOut_0
);

  wire[6:0] T0;
  wire T1;
  wire[6:0] T2;
  wire T3;
  wire T4;
  wire T5;
  wire[6:0] FF_io_data_out;
  wire[6:0] FF_1_io_data_out;
  wire[6:0] FF_2_io_data_out;
  wire[6:0] FF_3_io_data_out;
  wire[6:0] readRemoteAMux_io_out;
  wire[6:0] readRemoteBMux_io_out;
  wire[6:0] readLocalAMux_io_out;
  wire[6:0] readLocalBMux_io_out;


  assign T0 = T1 ? io_writeData : io_passData_1;
  assign T1 = io_writeSel[3];
  assign T2 = T3 ? io_writeData : io_passData_0;
  assign T3 = io_writeSel[2];
  assign T4 = io_writeSel[1];
  assign T5 = io_writeSel[0];
  assign io_passDataOut_0 = FF_2_io_data_out;
  assign io_passDataOut_1 = FF_3_io_data_out;
  assign io_readRemoteB = readRemoteBMux_io_out;
  assign io_readRemoteA = readRemoteAMux_io_out;
  assign io_readLocalB = readLocalBMux_io_out;
  assign io_readLocalA = readLocalAMux_io_out;
  FF FF(.clk(clk), .reset(reset),
       .io_data_in( io_writeData ),
       .io_data_init( 7'h0 ),
       .io_data_out( FF_io_data_out ),
       .io_control_enable( T5 )
  );
  FF FF_1(.clk(clk), .reset(reset),
       .io_data_in( io_writeData ),
       .io_data_init( 7'h0 ),
       .io_data_out( FF_1_io_data_out ),
       .io_control_enable( T4 )
  );
  FF FF_2(.clk(clk), .reset(reset),
       .io_data_in( T2 ),
       .io_data_init( 7'h0 ),
       .io_data_out( FF_2_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  FF FF_3(.clk(clk), .reset(reset),
       .io_data_in( T0 ),
       .io_data_init( 7'h0 ),
       .io_data_out( FF_3_io_data_out ),
       .io_control_enable( 1'h1 )
  );
  MuxN_1 readLocalAMux(
       .io_ins_3( FF_3_io_data_out ),
       .io_ins_2( FF_2_io_data_out ),
       .io_ins_1( FF_1_io_data_out ),
       .io_ins_0( FF_io_data_out ),
       .io_sel( io_readLocalASel ),
       .io_out( readLocalAMux_io_out )
  );
  MuxN_1 readLocalBMux(
       .io_ins_3( FF_3_io_data_out ),
       .io_ins_2( FF_2_io_data_out ),
       .io_ins_1( FF_1_io_data_out ),
       .io_ins_0( FF_io_data_out ),
       .io_sel( io_readLocalBSel ),
       .io_out( readLocalBMux_io_out )
  );
  MuxN readRemoteAMux(
       .io_ins_1( FF_3_io_data_out ),
       .io_ins_0( FF_2_io_data_out ),
       .io_sel( io_readRemoteASel ),
       .io_out( readRemoteAMux_io_out )
  );
  MuxN readRemoteBMux(
       .io_ins_1( FF_3_io_data_out ),
       .io_ins_0( FF_2_io_data_out ),
       .io_sel( io_readRemoteBSel ),
       .io_out( readRemoteBMux_io_out )
  );
endmodule

module ComputeUnit(input clk, input reset,
    input  io_enable,
    output io_done,
    output[6:0] io_scalarOut,
    input  io_config_enable,
    output io_rmux0,
    output io_rmux1,
    output[6:0] io_opcode,
    output io_opA_isLocal,
    output[6:0] io_opA_local,
    output[6:0] io_opA_remote,
    output io_opB_isLocal,
    output[6:0] io_opB_local,
    output[6:0] io_opB_remote,
    output[6:0] io_result
);

  reg [1:0] config_pipeStage_0_opB_regLocal;
  wire[1:0] T12;
  wire[1:0] configIn_pipeStage_0_opB_regLocal;
  wire[1:0] T0;
  wire[1:0] configType_pipeStage_0_opB_regLocal;
  reg [1:0] config_pipeStage_0_opA_regLocal;
  wire[1:0] T13;
  wire[1:0] configIn_pipeStage_0_opA_regLocal;
  wire[1:0] T1;
  wire[1:0] configType_pipeStage_0_opA_regLocal;
  reg [3:0] config_pipeStage_0_result;
  wire[3:0] T14;
  wire[3:0] configIn_pipeStage_0_result;
  wire[3:0] T2;
  wire[3:0] configType_pipeStage_0_result;
  reg [3:0] config_pipeStage_0_opcode;
  wire[3:0] T15;
  wire[3:0] configIn_pipeStage_0_opcode;
  wire[3:0] T3;
  wire[3:0] configType_pipeStage_0_opcode;
  wire[6:0] T4;
  reg  config_pipeStage_0_opB_isLocal;
  wire T16;
  wire configIn_pipeStage_0_opB_isLocal;
  wire T5;
  wire configType_pipeStage_0_opB_isLocal;
  wire[6:0] T6;
  reg  config_pipeStage_0_opA_isLocal;
  wire T17;
  wire configIn_pipeStage_0_opA_isLocal;
  wire T7;
  wire configType_pipeStage_0_opA_isLocal;
  reg  config_remoteMux1;
  wire T18;
  wire configIn_remoteMux1;
  wire T8;
  wire configType_remoteMux1;
  reg  config_remoteMux0;
  wire T19;
  wire configIn_remoteMux0;
  wire T9;
  wire configType_remoteMux0;
  wire[6:0] T20;
  wire[6:0] T21;
  reg  config_pipeStage_0_opB_regRemote;
  wire T22;
  wire configIn_pipeStage_0_opB_regRemote;
  wire T10;
  wire configType_pipeStage_0_opB_regRemote;
  wire[6:0] T23;
  wire[6:0] T24;
  reg  config_pipeStage_0_opA_regRemote;
  wire T25;
  wire configIn_pipeStage_0_opA_regRemote;
  wire T11;
  wire configType_pipeStage_0_opA_regRemote;
  wire[6:0] T26;
  wire[6:0] T27;
  wire[6:0] IntFU_io_out;
  wire[6:0] remoteMux0_io_out;
  wire[6:0] remoteMux1_io_out;
  wire[6:0] counterChain_io_data_1_out;
  wire[6:0] counterChain_io_data_0_out;
  wire counterChain_io_control_1_done;
  wire[6:0] RegisterBlock_io_readLocalA;
  wire[6:0] RegisterBlock_io_readLocalB;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    config_pipeStage_0_opB_regLocal = {1{$random}};
    config_pipeStage_0_opA_regLocal = {1{$random}};
    config_pipeStage_0_result = {1{$random}};
    config_pipeStage_0_opcode = {1{$random}};
    config_pipeStage_0_opB_isLocal = {1{$random}};
    config_pipeStage_0_opA_isLocal = {1{$random}};
    config_remoteMux1 = {1{$random}};
    config_remoteMux0 = {1{$random}};
    config_pipeStage_0_opB_regRemote = {1{$random}};
    config_pipeStage_0_opA_regRemote = {1{$random}};
  end
// synthesis translate_on
`endif

`ifndef SYNTHESIS
// synthesis translate_off
  assign configType_pipeStage_0_opB_regLocal = {1{$random}};
  assign configType_pipeStage_0_opA_regLocal = {1{$random}};
  assign configType_pipeStage_0_result = {1{$random}};
  assign configType_pipeStage_0_opcode = {1{$random}};
  assign configType_pipeStage_0_opB_isLocal = {1{$random}};
  assign configType_pipeStage_0_opA_isLocal = {1{$random}};
  assign configType_remoteMux1 = {1{$random}};
  assign configType_remoteMux0 = {1{$random}};
  assign configType_pipeStage_0_opB_regRemote = {1{$random}};
  assign configType_pipeStage_0_opA_regRemote = {1{$random}};
// synthesis translate_on
`endif
  assign T12 = reset ? 2'h0 : configIn_pipeStage_0_opB_regLocal;
  assign configIn_pipeStage_0_opB_regLocal = T0;
  assign T0 = io_config_enable ? configType_pipeStage_0_opB_regLocal : config_pipeStage_0_opB_regLocal;
  assign T13 = reset ? 2'h0 : configIn_pipeStage_0_opA_regLocal;
  assign configIn_pipeStage_0_opA_regLocal = T1;
  assign T1 = io_config_enable ? configType_pipeStage_0_opA_regLocal : config_pipeStage_0_opA_regLocal;
  assign T14 = reset ? 4'h1 : configIn_pipeStage_0_result;
  assign configIn_pipeStage_0_result = T2;
  assign T2 = io_config_enable ? configType_pipeStage_0_result : config_pipeStage_0_result;
  assign T15 = reset ? 4'h2 : configIn_pipeStage_0_opcode;
  assign configIn_pipeStage_0_opcode = T3;
  assign T3 = io_config_enable ? configType_pipeStage_0_opcode : config_pipeStage_0_opcode;
  assign T4 = config_pipeStage_0_opB_isLocal ? RegisterBlock_io_readLocalB : remoteMux1_io_out;
  assign T16 = reset ? 1'h0 : configIn_pipeStage_0_opB_isLocal;
  assign configIn_pipeStage_0_opB_isLocal = T5;
  assign T5 = io_config_enable ? configType_pipeStage_0_opB_isLocal : config_pipeStage_0_opB_isLocal;
  assign T6 = config_pipeStage_0_opA_isLocal ? RegisterBlock_io_readLocalA : remoteMux0_io_out;
  assign T17 = reset ? 1'h0 : configIn_pipeStage_0_opA_isLocal;
  assign configIn_pipeStage_0_opA_isLocal = T7;
  assign T7 = io_config_enable ? configType_pipeStage_0_opA_isLocal : config_pipeStage_0_opA_isLocal;
  assign T18 = reset ? 1'h1 : configIn_remoteMux1;
  assign configIn_remoteMux1 = T8;
  assign T8 = io_config_enable ? configType_remoteMux1 : config_remoteMux1;
  assign T19 = reset ? 1'h0 : configIn_remoteMux0;
  assign configIn_remoteMux0 = T9;
  assign T9 = io_config_enable ? configType_remoteMux0 : config_remoteMux0;
  assign io_result = T20;
  assign T20 = {3'h0, config_pipeStage_0_result};
  assign io_opB_remote = T21;
  assign T21 = {6'h0, config_pipeStage_0_opB_regRemote};
  assign T22 = reset ? 1'h1 : configIn_pipeStage_0_opB_regRemote;
  assign configIn_pipeStage_0_opB_regRemote = T10;
  assign T10 = io_config_enable ? configType_pipeStage_0_opB_regRemote : config_pipeStage_0_opB_regRemote;
  assign io_opB_local = T23;
  assign T23 = {5'h0, config_pipeStage_0_opB_regLocal};
  assign io_opB_isLocal = config_pipeStage_0_opB_isLocal;
  assign io_opA_remote = T24;
  assign T24 = {6'h0, config_pipeStage_0_opA_regRemote};
  assign T25 = reset ? 1'h0 : configIn_pipeStage_0_opA_regRemote;
  assign configIn_pipeStage_0_opA_regRemote = T11;
  assign T11 = io_config_enable ? configType_pipeStage_0_opA_regRemote : config_pipeStage_0_opA_regRemote;
  assign io_opA_local = T26;
  assign T26 = {5'h0, config_pipeStage_0_opA_regLocal};
  assign io_opA_isLocal = config_pipeStage_0_opA_isLocal;
  assign io_opcode = T27;
  assign T27 = {3'h0, config_pipeStage_0_opcode};
  assign io_rmux1 = config_remoteMux1;
  assign io_rmux0 = config_remoteMux0;
  assign io_scalarOut = IntFU_io_out;
  assign io_done = counterChain_io_control_1_done;
  CounterChain counterChain(.clk(clk), .reset(reset),
       .io_data_1_max( 7'h0 ),
       .io_data_1_stride( 7'h0 ),
       .io_data_1_out( counterChain_io_data_1_out ),
       .io_data_0_max( 7'h0 ),
       .io_data_0_stride( 7'h0 ),
       .io_data_0_out( counterChain_io_data_0_out ),
       .io_control_1_enable( io_enable ),
       .io_control_1_done( counterChain_io_control_1_done ),
       .io_control_0_enable( io_enable )
       //.io_control_0_done(  )
  );
  MuxN remoteMux0(
       .io_ins_1( counterChain_io_data_1_out ),
       .io_ins_0( counterChain_io_data_0_out ),
       .io_sel( config_remoteMux0 ),
       .io_out( remoteMux0_io_out )
  );
  MuxN remoteMux1(
       .io_ins_1( counterChain_io_data_1_out ),
       .io_ins_0( counterChain_io_data_0_out ),
       .io_sel( config_remoteMux1 ),
       .io_out( remoteMux1_io_out )
  );
  IntFU IntFU(
       .io_a( T6 ),
       .io_b( T4 ),
       .io_opcode( config_pipeStage_0_opcode ),
       .io_out( IntFU_io_out )
  );
  RegisterBlock RegisterBlock(.clk(clk), .reset(reset),
       .io_writeData( IntFU_io_out ),
       .io_passData_1( counterChain_io_data_1_out ),
       .io_passData_0( counterChain_io_data_0_out ),
       .io_writeSel( config_pipeStage_0_result ),
       .io_readLocalASel( config_pipeStage_0_opA_regLocal ),
       .io_readLocalBSel( config_pipeStage_0_opB_regLocal ),
       //.io_readRemoteASel(  )
       //.io_readRemoteBSel(  )
       .io_readLocalA( RegisterBlock_io_readLocalA ),
       .io_readLocalB( RegisterBlock_io_readLocalB )
       //.io_readRemoteA(  )
       //.io_readRemoteB(  )
       //.io_passDataOut_1(  )
       //.io_passDataOut_0(  )
  );
`ifndef SYNTHESIS
// synthesis translate_off
    assign RegisterBlock.io_readRemoteASel = {1{$random}};
    assign RegisterBlock.io_readRemoteBSel = {1{$random}};
// synthesis translate_on
`endif

  always @(posedge clk) begin
    if(reset) begin
      config_pipeStage_0_opB_regLocal <= 2'h0;
    end else begin
      config_pipeStage_0_opB_regLocal <= configIn_pipeStage_0_opB_regLocal;
    end
    if(reset) begin
      config_pipeStage_0_opA_regLocal <= 2'h0;
    end else begin
      config_pipeStage_0_opA_regLocal <= configIn_pipeStage_0_opA_regLocal;
    end
    if(reset) begin
      config_pipeStage_0_result <= 4'h1;
    end else begin
      config_pipeStage_0_result <= configIn_pipeStage_0_result;
    end
    if(reset) begin
      config_pipeStage_0_opcode <= 4'h2;
    end else begin
      config_pipeStage_0_opcode <= configIn_pipeStage_0_opcode;
    end
    if(reset) begin
      config_pipeStage_0_opB_isLocal <= 1'h0;
    end else begin
      config_pipeStage_0_opB_isLocal <= configIn_pipeStage_0_opB_isLocal;
    end
    if(reset) begin
      config_pipeStage_0_opA_isLocal <= 1'h0;
    end else begin
      config_pipeStage_0_opA_isLocal <= configIn_pipeStage_0_opA_isLocal;
    end
    if(reset) begin
      config_remoteMux1 <= 1'h1;
    end else begin
      config_remoteMux1 <= configIn_remoteMux1;
    end
    if(reset) begin
      config_remoteMux0 <= 1'h0;
    end else begin
      config_remoteMux0 <= configIn_remoteMux0;
    end
    if(reset) begin
      config_pipeStage_0_opB_regRemote <= 1'h1;
    end else begin
      config_pipeStage_0_opB_regRemote <= configIn_pipeStage_0_opB_regRemote;
    end
    if(reset) begin
      config_pipeStage_0_opA_regRemote <= 1'h0;
    end else begin
      config_pipeStage_0_opA_regRemote <= configIn_pipeStage_0_opA_regRemote;
    end
  end
endmodule

