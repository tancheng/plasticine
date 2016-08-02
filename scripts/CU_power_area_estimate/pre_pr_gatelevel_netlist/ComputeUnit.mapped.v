
module ComputeUnit ( clk, reset, io_enable, io_done, io_scalarOut, 
        io_config_enable, io_rmux0, io_rmux1, io_opcode, io_opA_isLocal, 
        io_opA_local, io_opA_remote, io_opB_isLocal, io_opB_local, 
        io_opB_remote, io_result );
  output [6:0] io_scalarOut;
  output [6:0] io_opcode;
  output [6:0] io_opA_local;
  output [6:0] io_opA_remote;
  output [6:0] io_opB_local;
  output [6:0] io_opB_remote;
  output [6:0] io_result;
  input clk, reset, io_enable, io_config_enable;
  output io_done, io_rmux0, io_rmux1, io_opA_isLocal, io_opB_isLocal;
  wire   n265, n266, n267, N39, N46, N57, N63, RegisterBlock_FF_io_data_out_0_,
         RegisterBlock_FF_io_data_out_1_, RegisterBlock_FF_io_data_out_2_,
         RegisterBlock_FF_io_data_out_3_, RegisterBlock_FF_io_data_out_4_,
         RegisterBlock_FF_io_data_out_5_, RegisterBlock_FF_N9,
         RegisterBlock_FF_N7, RegisterBlock_FF_N6,
         counterChain_CounterRC_1_counter_reg__N8,
         counterChain_CounterRC_1_counter_reg__N7,
         counterChain_CounterRC_1_counter_reg__N6,
         counterChain_CounterRC_counter_reg__N8,
         counterChain_CounterRC_counter_reg__N7,
         counterChain_CounterRC_counter_reg__N6, n184, n185, n186, n187, n188,
         n189, n191, n192, n193, n194, n195, n196, n197, n198, n199, n200,
         n201, n202, n203, n204, n205, n206, n207, n208, n209, n210, n211,
         n212, n213, n214, n215, n216, n217, n218, n219, n220, n221, n222,
         n223, n224, n225, n226, n227, n228, n229, n230, n231, n232, n233,
         n234, n235, n236, n237, n238, n239, n240, n241, n242, n243, n244,
         n245, n246, n247, n248, n249, n250, n260, n261, n262, n263;
  wire   [2:0] counterChain_io_data_1_out;
  wire   [2:0] counterChain_io_data_0_out;
  assign io_scalarOut[6] = 1'b0;
  assign io_opcode[6] = 1'b0;
  assign io_opcode[5] = 1'b0;
  assign io_opcode[4] = 1'b0;
  assign io_opA_local[6] = 1'b0;
  assign io_opA_local[5] = 1'b0;
  assign io_opA_local[4] = 1'b0;
  assign io_opA_local[3] = 1'b0;
  assign io_opA_local[2] = 1'b0;
  assign io_opA_remote[6] = 1'b0;
  assign io_opA_remote[5] = 1'b0;
  assign io_opA_remote[4] = 1'b0;
  assign io_opA_remote[3] = 1'b0;
  assign io_opA_remote[2] = 1'b0;
  assign io_opA_remote[1] = 1'b0;
  assign io_opB_local[6] = 1'b0;
  assign io_opB_local[5] = 1'b0;
  assign io_opB_local[4] = 1'b0;
  assign io_opB_local[3] = 1'b0;
  assign io_opB_local[2] = 1'b0;
  assign io_opB_remote[6] = 1'b0;
  assign io_opB_remote[5] = 1'b0;
  assign io_opB_remote[4] = 1'b0;
  assign io_opB_remote[3] = 1'b0;
  assign io_opB_remote[2] = 1'b0;
  assign io_opB_remote[1] = 1'b0;
  assign io_result[6] = 1'b0;
  assign io_result[5] = 1'b0;
  assign io_result[4] = 1'b0;
  assign io_opA_remote[0] = 1'b0;
  assign io_rmux0 = 1'b0;
  assign io_opA_isLocal = 1'b0;
  assign io_opB_isLocal = 1'b0;
  assign io_opcode[0] = 1'b0;
  assign io_opcode[2] = 1'b0;
  assign io_opcode[3] = 1'b0;
  assign io_result[1] = 1'b0;
  assign io_result[2] = 1'b0;
  assign io_result[3] = 1'b0;
  assign io_opA_local[0] = 1'b0;
  assign io_opA_local[1] = 1'b0;
  assign io_opB_local[0] = 1'b0;
  assign io_opB_local[1] = 1'b0;

  DFQD1BWP RegisterBlock_FF_ff_reg_1_ ( .D(RegisterBlock_FF_N7), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_1_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_0_ ( .D(RegisterBlock_FF_N6), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_0_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_2_ ( .D(n260), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_2_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_4_ ( .D(n262), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_4_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_5_ ( .D(n261), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_5_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_3_ ( .D(RegisterBlock_FF_N9), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_3_) );
  DFQD1BWP counterChain_CounterRC_1_counter_reg__ff_reg_2_ ( .D(
        counterChain_CounterRC_1_counter_reg__N8), .CP(clk), .Q(
        counterChain_io_data_1_out[2]) );
  DFQD1BWP config_remoteMux1_reg ( .D(N57), .CP(clk), .Q(n265) );
  DFQD1BWP counterChain_CounterRC_1_counter_reg__ff_reg_0_ ( .D(
        counterChain_CounterRC_1_counter_reg__N6), .CP(clk), .Q(
        counterChain_io_data_1_out[0]) );
  DFQD1BWP counterChain_CounterRC_1_counter_reg__ff_reg_1_ ( .D(
        counterChain_CounterRC_1_counter_reg__N7), .CP(clk), .Q(
        counterChain_io_data_1_out[1]) );
  DFQD1BWP counterChain_CounterRC_counter_reg__ff_reg_2_ ( .D(
        counterChain_CounterRC_counter_reg__N8), .CP(clk), .Q(
        counterChain_io_data_0_out[2]) );
  DFQD1BWP counterChain_CounterRC_counter_reg__ff_reg_0_ ( .D(
        counterChain_CounterRC_counter_reg__N6), .CP(clk), .Q(
        counterChain_io_data_0_out[0]) );
  DFQD1BWP counterChain_CounterRC_counter_reg__ff_reg_1_ ( .D(
        counterChain_CounterRC_counter_reg__N7), .CP(clk), .Q(
        counterChain_io_data_0_out[1]) );
  DFQD1BWP config_pipeStage_0_opB_regRemote_reg ( .D(N63), .CP(clk), .Q(n266)
         );
  DFQD1BWP config_pipeStage_0_result_reg_0_ ( .D(N39), .CP(clk), .Q(n267) );
  DFKCNQD1BWP RegisterBlock_FF_3_ff_reg_2_ ( .CN(n263), .D(
        counterChain_io_data_1_out[2]), .CP(clk) );
  DFKCNQD1BWP RegisterBlock_FF_3_ff_reg_0_ ( .CN(n263), .D(
        counterChain_io_data_1_out[0]), .CP(clk) );
  DFKCNQD1BWP RegisterBlock_FF_3_ff_reg_1_ ( .CN(n263), .D(
        counterChain_io_data_1_out[1]), .CP(clk) );
  DFKCNQD1BWP RegisterBlock_FF_2_ff_reg_2_ ( .CN(n263), .D(
        counterChain_io_data_0_out[2]), .CP(clk) );
  DFKCNQD1BWP RegisterBlock_FF_2_ff_reg_1_ ( .CN(n263), .D(
        counterChain_io_data_0_out[1]), .CP(clk) );
  DFKCNQD1BWP RegisterBlock_FF_2_ff_reg_0_ ( .CN(n263), .D(
        counterChain_io_data_0_out[0]), .CP(clk) );
  DFD1BWP config_pipeStage_0_opcode_reg_1_ ( .D(N46), .CP(clk), .Q(n191), .QN(
        n250) );
  INVD1BWP U271 ( .I(io_result[0]), .ZN(n249) );
  NR2D1BWP U272 ( .A1(reset), .A2(io_result[0]), .ZN(n247) );
  NR2D1BWP U273 ( .A1(n249), .A2(reset), .ZN(n246) );
  BUFFD8BWP U274 ( .I(n267), .Z(io_result[0]) );
  OA31D1BWP U275 ( .A1(n224), .A2(n250), .A3(n223), .B(n222), .Z(n225) );
  OA31D1BWP U276 ( .A1(n232), .A2(n231), .A3(n250), .B(n230), .Z(n233) );
  BUFFD8BWP U277 ( .I(n209), .Z(io_scalarOut[3]) );
  INVD8BWP U278 ( .I(n202), .ZN(io_scalarOut[4]) );
  OR2XD1BWP U279 ( .A1(n201), .A2(n250), .Z(n202) );
  BUFFD8BWP U280 ( .I(n265), .Z(io_rmux1) );
  NR2D1BWP U281 ( .A1(n237), .A2(n192), .ZN(n184) );
  MAOI222D1BWP U282 ( .A(n205), .B(n206), .C(n204), .ZN(n185) );
  XOR3D1BWP U283 ( .A1(n195), .A2(n184), .A3(n185), .Z(n200) );
  MAOI222D1BWP U284 ( .A(n195), .B(n184), .C(n185), .ZN(n186) );
  CKND1BWP U285 ( .I(n186), .ZN(n220) );
  CKND2D1BWP U286 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .ZN(n187) );
  OAI211D1BWP U287 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .B(n235), .C(n187), .ZN(n188) );
  IOA21D1BWP U288 ( .A1(counterChain_io_data_0_out[1]), .A2(n236), .B(n188), 
        .ZN(counterChain_CounterRC_counter_reg__N7) );
  CKND1BWP U289 ( .I(counterChain_io_data_1_out[1]), .ZN(n189) );
  AOI32D1BWP U290 ( .A1(n245), .A2(counterChain_io_data_1_out[1]), .A3(n243), 
        .B1(n241), .B2(n189), .ZN(counterChain_CounterRC_1_counter_reg__N7) );
  AN2D8BWP U291 ( .A1(n220), .A2(n191), .Z(io_scalarOut[5]) );
  MUX2D1BWP U292 ( .I0(counterChain_io_data_0_out[2]), .I1(
        counterChain_io_data_1_out[2]), .S(io_rmux1), .Z(n213) );
  MUX2D1BWP U293 ( .I0(counterChain_io_data_0_out[1]), .I1(
        counterChain_io_data_1_out[1]), .S(io_rmux1), .Z(n228) );
  ND4D1BWP U294 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .A3(n213), .A4(n228), .ZN(n194) );
  INVD1BWP U295 ( .I(counterChain_io_data_0_out[2]), .ZN(n237) );
  CKND1BWP U296 ( .I(n213), .ZN(n192) );
  ND2D1BWP U297 ( .A1(counterChain_io_data_0_out[1]), .A2(n213), .ZN(n193) );
  XNR2D1BWP U298 ( .A1(n194), .A2(n193), .ZN(n204) );
  ND2D1BWP U299 ( .A1(counterChain_io_data_0_out[2]), .A2(n228), .ZN(n206) );
  CKND2D1BWP U300 ( .A1(counterChain_io_data_0_out[0]), .A2(n213), .ZN(n197)
         );
  CKND2D1BWP U301 ( .A1(counterChain_io_data_0_out[1]), .A2(n228), .ZN(n196)
         );
  CKND1BWP U302 ( .I(n194), .ZN(n195) );
  AOI21D1BWP U303 ( .A1(n197), .A2(n196), .B(n195), .ZN(n211) );
  CKMUX2D1BWP U304 ( .I0(counterChain_io_data_0_out[0]), .I1(
        counterChain_io_data_1_out[0]), .S(io_rmux1), .Z(n226) );
  CKND1BWP U305 ( .I(n226), .ZN(n224) );
  NR2D1BWP U306 ( .A1(n237), .A2(n224), .ZN(n210) );
  CKND1BWP U307 ( .I(counterChain_io_data_0_out[0]), .ZN(n223) );
  CKND1BWP U308 ( .I(counterChain_io_data_0_out[1]), .ZN(n198) );
  NR2D1BWP U309 ( .A1(n223), .A2(n198), .ZN(n234) );
  AN3XD1BWP U310 ( .A1(n228), .A2(n226), .A3(n234), .Z(n232) );
  CKND1BWP U311 ( .I(n199), .ZN(n205) );
  CKND1BWP U312 ( .I(n200), .ZN(n201) );
  AO22D1BWP U313 ( .A1(n246), .A2(io_scalarOut[4]), .B1(n247), .B2(
        RegisterBlock_FF_io_data_out_4_), .Z(n262) );
  CKND2D1BWP U314 ( .A1(n226), .A2(counterChain_io_data_0_out[0]), .ZN(n227)
         );
  CKND1BWP U315 ( .I(n203), .ZN(n208) );
  XNR3D1BWP U316 ( .A1(n206), .A2(n205), .A3(n204), .ZN(n207) );
  MUX2D1BWP U317 ( .I0(n208), .I1(n207), .S(n191), .Z(n209) );
  AO22D1BWP U318 ( .A1(io_scalarOut[3]), .A2(n246), .B1(n247), .B2(
        RegisterBlock_FF_io_data_out_3_), .Z(RegisterBlock_FF_N9) );
  FA1D1BWP U319 ( .A(n211), .B(n210), .CI(n232), .CO(n199), .S(n216) );
  FICOND1BWP U320 ( .A(counterChain_io_data_0_out[2]), .B(n213), .CI(n212), 
        .CON(n203), .S(n214) );
  ND2D1BWP U321 ( .A1(n214), .A2(n250), .ZN(n215) );
  IOA21D1BWP U322 ( .A1(n191), .A2(n216), .B(n215), .ZN(n217) );
  BUFFD8BWP U323 ( .I(n217), .Z(io_scalarOut[2]) );
  INVD1BWP U324 ( .I(counterChain_io_data_1_out[2]), .ZN(n238) );
  OR2XD1BWP U325 ( .A1(n237), .A2(n238), .Z(n218) );
  INVD8BWP U326 ( .I(n218), .ZN(io_done) );
  INVD1BWP U327 ( .I(reset), .ZN(n263) );
  ND2D1BWP U328 ( .A1(io_enable), .A2(n263), .ZN(n219) );
  NR2D1BWP U329 ( .A1(n219), .A2(counterChain_io_data_0_out[2]), .ZN(n235) );
  NR2D1BWP U330 ( .A1(reset), .A2(io_enable), .ZN(n236) );
  CKMUX2D1BWP U331 ( .I0(n235), .I1(n236), .S(counterChain_io_data_0_out[0]), 
        .Z(counterChain_CounterRC_counter_reg__N6) );
  BUFFD8BWP U332 ( .I(n266), .Z(io_opB_remote[0]) );
  INVD8BWP U333 ( .I(n250), .ZN(io_opcode[1]) );
  XOR2D1BWP U334 ( .A1(n226), .A2(counterChain_io_data_0_out[0]), .Z(n221) );
  ND2D1BWP U335 ( .A1(n221), .A2(n250), .ZN(n222) );
  INVD12BWP U336 ( .I(n225), .ZN(io_scalarOut[0]) );
  AOI22D1BWP U337 ( .A1(counterChain_io_data_0_out[1]), .A2(n226), .B1(n228), 
        .B2(counterChain_io_data_0_out[0]), .ZN(n231) );
  FICIND1BWP U338 ( .CIN(n227), .B(n228), .A(counterChain_io_data_0_out[1]), 
        .CO(n212), .S(n229) );
  ND2D1BWP U339 ( .A1(n229), .A2(n250), .ZN(n230) );
  INVD12BWP U340 ( .I(n233), .ZN(io_scalarOut[1]) );
  AO22D1BWP U341 ( .A1(n235), .A2(n234), .B1(n236), .B2(
        counterChain_io_data_0_out[2]), .Z(
        counterChain_CounterRC_counter_reg__N8) );
  AN4D1BWP U342 ( .A1(io_enable), .A2(n238), .A3(n263), .A4(
        counterChain_io_data_0_out[2]), .Z(n240) );
  ND2D1BWP U343 ( .A1(counterChain_io_data_1_out[0]), .A2(n240), .ZN(n241) );
  CKND1BWP U344 ( .I(counterChain_io_data_1_out[1]), .ZN(n239) );
  AOI21D1BWP U345 ( .A1(n237), .A2(n263), .B(n236), .ZN(n245) );
  OAI22D1BWP U346 ( .A1(n241), .A2(n239), .B1(n245), .B2(n238), .ZN(
        counterChain_CounterRC_1_counter_reg__N8) );
  AO22D1BWP U347 ( .A1(n246), .A2(io_scalarOut[2]), .B1(n247), .B2(
        RegisterBlock_FF_io_data_out_2_), .Z(n260) );
  AO22D1BWP U348 ( .A1(io_scalarOut[5]), .A2(n246), .B1(n247), .B2(
        RegisterBlock_FF_io_data_out_5_), .Z(n261) );
  INVD1BWP U350 ( .I(counterChain_io_data_1_out[0]), .ZN(n244) );
  ND2D1BWP U351 ( .A1(n240), .A2(n244), .ZN(n243) );
  INVD1BWP U352 ( .I(io_rmux1), .ZN(n242) );
  OAI21D1BWP U353 ( .A1(n242), .A2(io_config_enable), .B(n263), .ZN(N57) );
  OAI21D1BWP U354 ( .A1(n245), .A2(n244), .B(n243), .ZN(
        counterChain_CounterRC_1_counter_reg__N6) );
  OAI21D1BWP U355 ( .A1(n250), .A2(io_config_enable), .B(n263), .ZN(N46) );
  AO22D1BWP U357 ( .A1(n247), .A2(RegisterBlock_FF_io_data_out_0_), .B1(n246), 
        .B2(io_scalarOut[0]), .Z(RegisterBlock_FF_N6) );
  AO22D1BWP U358 ( .A1(n247), .A2(RegisterBlock_FF_io_data_out_1_), .B1(n246), 
        .B2(io_scalarOut[1]), .Z(RegisterBlock_FF_N7) );
  INVD1BWP U359 ( .I(io_opB_remote[0]), .ZN(n248) );
  OAI21D1BWP U360 ( .A1(io_config_enable), .A2(n248), .B(n263), .ZN(N63) );
  OAI21D1BWP U361 ( .A1(io_config_enable), .A2(n249), .B(n263), .ZN(N39) );
endmodule

