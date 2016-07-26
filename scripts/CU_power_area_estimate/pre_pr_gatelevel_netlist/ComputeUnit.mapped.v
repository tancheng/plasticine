
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
  wire   n261, n262, n263, n264, N39, N46, N57, N63,
         RegisterBlock_FF_io_data_out_0_, RegisterBlock_FF_io_data_out_1_,
         RegisterBlock_FF_io_data_out_2_, RegisterBlock_FF_io_data_out_3_,
         RegisterBlock_FF_io_data_out_4_, RegisterBlock_FF_io_data_out_5_,
         RegisterBlock_FF_N11, RegisterBlock_FF_N10, RegisterBlock_FF_N9,
         RegisterBlock_FF_N8, RegisterBlock_FF_N7, RegisterBlock_FF_N6,
         RegisterBlock_FF_3_N8, RegisterBlock_FF_3_N7, RegisterBlock_FF_3_N6,
         RegisterBlock_FF_2_N8, RegisterBlock_FF_2_N7, RegisterBlock_FF_2_N6,
         counterChain_CounterRC_1_counter_reg__N8,
         counterChain_CounterRC_1_counter_reg__N7,
         counterChain_CounterRC_1_counter_reg__N6,
         counterChain_CounterRC_counter_reg__N8,
         counterChain_CounterRC_counter_reg__N7,
         counterChain_CounterRC_counter_reg__N6, n184, n185, n186, n187, n188,
         n190, n191, n192, n193, n194, n195, n196, n197, n198, n199, n200,
         n201, n202, n203, n204, n205, n206, n207, n208, n209, n210, n211,
         n212, n213, n214, n215, n216, n217, n218, n219, n220, n221, n222,
         n223, n224, n225, n226, n227, n228, n229, n230, n231, n232, n233,
         n234, n235, n236, n237, n238, n239, n240, n241, n242, n243, n244,
         n245, n246, n247, n248, n249, n250;
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

  DFQD4BWP RegisterBlock_FF_2_ff_reg_1_ ( .D(RegisterBlock_FF_2_N7), .CP(clk)
         );
  DFQD4BWP RegisterBlock_FF_3_ff_reg_1_ ( .D(RegisterBlock_FF_3_N7), .CP(clk)
         );
  DFQD4BWP RegisterBlock_FF_2_ff_reg_2_ ( .D(RegisterBlock_FF_2_N8), .CP(clk)
         );
  DFQD4BWP RegisterBlock_FF_3_ff_reg_2_ ( .D(RegisterBlock_FF_3_N8), .CP(clk)
         );
  DFQD4BWP RegisterBlock_FF_2_ff_reg_0_ ( .D(RegisterBlock_FF_2_N6), .CP(clk)
         );
  DFQD4BWP RegisterBlock_FF_3_ff_reg_0_ ( .D(RegisterBlock_FF_3_N6), .CP(clk)
         );
  DFQD1BWP RegisterBlock_FF_ff_reg_1_ ( .D(RegisterBlock_FF_N7), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_1_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_0_ ( .D(RegisterBlock_FF_N6), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_0_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_2_ ( .D(RegisterBlock_FF_N8), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_2_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_3_ ( .D(RegisterBlock_FF_N9), .CP(clk), .Q(
        RegisterBlock_FF_io_data_out_3_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_4_ ( .D(RegisterBlock_FF_N10), .CP(clk), 
        .Q(RegisterBlock_FF_io_data_out_4_) );
  DFQD1BWP RegisterBlock_FF_ff_reg_5_ ( .D(RegisterBlock_FF_N11), .CP(clk), 
        .Q(RegisterBlock_FF_io_data_out_5_) );
  DFQD1BWP counterChain_CounterRC_1_counter_reg__ff_reg_2_ ( .D(
        counterChain_CounterRC_1_counter_reg__N8), .CP(clk), .Q(
        counterChain_io_data_1_out[2]) );
  DFQD1BWP config_remoteMux1_reg ( .D(N57), .CP(clk), .Q(n261) );
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
  DFQD1BWP config_pipeStage_0_opB_regRemote_reg ( .D(N63), .CP(clk), .Q(n263)
         );
  DFQD1BWP config_pipeStage_0_result_reg_0_ ( .D(N39), .CP(clk), .Q(n264) );
  DFQD1BWP config_pipeStage_0_opcode_reg_1_ ( .D(N46), .CP(clk), .Q(n262) );
  MUX2D1BWP U271 ( .I0(n230), .I1(n229), .S(n262), .Z(n231) );
  INVD1BWP U272 ( .I(n263), .ZN(n201) );
  AO22D1BWP U273 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_3_), .B1(n247), 
        .B2(io_scalarOut[3]), .Z(RegisterBlock_FF_N9) );
  CKND2D1BWP U274 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .ZN(n184) );
  OAI211D1BWP U275 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .B(n234), .C(n184), .ZN(n185) );
  IOA21D1BWP U276 ( .A1(counterChain_io_data_0_out[1]), .A2(n235), .B(n185), 
        .ZN(counterChain_CounterRC_counter_reg__N7) );
  CKND1BWP U277 ( .I(counterChain_io_data_1_out[1]), .ZN(n186) );
  AOI32D1BWP U278 ( .A1(n239), .A2(counterChain_io_data_1_out[1]), .A3(n240), 
        .B1(n237), .B2(n186), .ZN(counterChain_CounterRC_1_counter_reg__N7) );
  OR2XD1BWP U279 ( .A1(n246), .A2(n243), .Z(n187) );
  AN2XD1BWP U280 ( .A1(n199), .A2(n198), .Z(n188) );
  AN2D8BWP U281 ( .A1(n208), .A2(n262), .Z(io_scalarOut[5]) );
  OA31D1BWP U282 ( .A1(n222), .A2(n221), .A3(n200), .B(n220), .Z(n190) );
  OA31D1BWP U283 ( .A1(n215), .A2(n244), .A3(n200), .B(n214), .Z(n191) );
  OR2XD1BWP U284 ( .A1(n212), .A2(n200), .Z(n192) );
  INVD1BWP U285 ( .I(n262), .ZN(n200) );
  BUFFD8BWP U286 ( .I(n261), .Z(io_rmux1) );
  MUX2D1BWP U287 ( .I0(counterChain_io_data_0_out[2]), .I1(
        counterChain_io_data_1_out[2]), .S(io_rmux1), .Z(n224) );
  ND2D1BWP U288 ( .A1(counterChain_io_data_0_out[0]), .A2(n224), .ZN(n195) );
  MUX2D1BWP U289 ( .I0(counterChain_io_data_0_out[1]), .I1(
        counterChain_io_data_1_out[1]), .S(io_rmux1), .Z(n218) );
  ND2D1BWP U290 ( .A1(counterChain_io_data_0_out[1]), .A2(n218), .ZN(n194) );
  ND4D1BWP U291 ( .A1(counterChain_io_data_0_out[1]), .A2(
        counterChain_io_data_0_out[0]), .A3(n224), .A4(n218), .ZN(n204) );
  INVD1BWP U292 ( .I(n204), .ZN(n193) );
  AOI21D1BWP U293 ( .A1(n195), .A2(n194), .B(n193), .ZN(n206) );
  INVD1BWP U294 ( .I(counterChain_io_data_0_out[2]), .ZN(n246) );
  MUX2D1BWP U295 ( .I0(counterChain_io_data_0_out[0]), .I1(
        counterChain_io_data_1_out[0]), .S(io_rmux1), .Z(n216) );
  INVD1BWP U296 ( .I(n216), .ZN(n215) );
  NR2D1BWP U297 ( .A1(n246), .A2(n215), .ZN(n205) );
  INVD1BWP U298 ( .I(counterChain_io_data_0_out[0]), .ZN(n244) );
  INVD1BWP U299 ( .I(counterChain_io_data_0_out[1]), .ZN(n245) );
  NR2D1BWP U300 ( .A1(n244), .A2(n245), .ZN(n233) );
  AN3XD1BWP U301 ( .A1(n233), .A2(n218), .A3(n216), .Z(n222) );
  CKND2D1BWP U302 ( .A1(n262), .A2(n196), .ZN(n199) );
  ND2D1BWP U303 ( .A1(n216), .A2(counterChain_io_data_0_out[0]), .ZN(n217) );
  CKND2D1BWP U304 ( .A1(n197), .A2(n200), .ZN(n198) );
  INVD8BWP U305 ( .I(n188), .ZN(io_scalarOut[2]) );
  INVD8BWP U306 ( .I(n200), .ZN(io_opcode[1]) );
  INVD1BWP U307 ( .I(counterChain_io_data_1_out[2]), .ZN(n243) );
  INVD12BWP U308 ( .I(n187), .ZN(io_done) );
  INVD8BWP U309 ( .I(n201), .ZN(io_opB_remote[0]) );
  INVD1BWP U310 ( .I(n224), .ZN(n202) );
  NR2D1BWP U311 ( .A1(n246), .A2(n202), .ZN(n210) );
  ND2D1BWP U312 ( .A1(counterChain_io_data_0_out[1]), .A2(n224), .ZN(n203) );
  XNR2D1BWP U313 ( .A1(n204), .A2(n203), .ZN(n226) );
  ND2D1BWP U314 ( .A1(counterChain_io_data_0_out[2]), .A2(n218), .ZN(n228) );
  FA1D1BWP U315 ( .A(n206), .B(n205), .CI(n222), .CO(n207), .S(n196) );
  INVD1BWP U316 ( .I(n207), .ZN(n227) );
  MAOI222D1BWP U317 ( .A(n226), .B(n228), .C(n227), .ZN(n209) );
  FA1D1BWP U318 ( .A(n193), .B(n210), .CI(n209), .CO(n208), .S(n211) );
  INVD1BWP U319 ( .I(n211), .ZN(n212) );
  INVD12BWP U320 ( .I(n192), .ZN(io_scalarOut[4]) );
  XOR2D1BWP U321 ( .A1(n216), .A2(counterChain_io_data_0_out[0]), .Z(n213) );
  ND2D1BWP U322 ( .A1(n213), .A2(n200), .ZN(n214) );
  INVD12BWP U323 ( .I(n191), .ZN(io_scalarOut[0]) );
  AOI22D1BWP U324 ( .A1(counterChain_io_data_0_out[1]), .A2(n216), .B1(
        counterChain_io_data_0_out[0]), .B2(n218), .ZN(n221) );
  FICIND1BWP U325 ( .CIN(n217), .B(n218), .A(counterChain_io_data_0_out[1]), 
        .CO(n223), .S(n219) );
  ND2D1BWP U326 ( .A1(n219), .A2(n200), .ZN(n220) );
  INVD12BWP U327 ( .I(n190), .ZN(io_scalarOut[1]) );
  FICOND1BWP U328 ( .A(counterChain_io_data_0_out[2]), .B(n224), .CI(n223), 
        .CON(n225), .S(n197) );
  INVD1BWP U329 ( .I(n225), .ZN(n230) );
  XNR3D1BWP U330 ( .A1(n228), .A2(n227), .A3(n226), .ZN(n229) );
  BUFFD8BWP U331 ( .I(n231), .Z(io_scalarOut[3]) );
  BUFFD8BWP U332 ( .I(n264), .Z(io_result[0]) );
  NR2D1BWP U333 ( .A1(reset), .A2(io_result[0]), .ZN(n248) );
  INVD1BWP U334 ( .I(io_result[0]), .ZN(n250) );
  NR2D1BWP U335 ( .A1(n250), .A2(reset), .ZN(n247) );
  AO22D2BWP U336 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_2_), .B1(n247), 
        .B2(io_scalarOut[2]), .Z(RegisterBlock_FF_N8) );
  AO22D1BWP U337 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_4_), .B1(n247), 
        .B2(io_scalarOut[4]), .Z(RegisterBlock_FF_N10) );
  AO22D1BWP U338 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_5_), .B1(n247), 
        .B2(io_scalarOut[5]), .Z(RegisterBlock_FF_N11) );
  INVD1BWP U339 ( .I(reset), .ZN(n249) );
  AN4D1BWP U340 ( .A1(io_enable), .A2(n243), .A3(n249), .A4(
        counterChain_io_data_0_out[2]), .Z(n236) );
  ND2D1BWP U341 ( .A1(counterChain_io_data_1_out[0]), .A2(n236), .ZN(n237) );
  INVD1BWP U342 ( .I(counterChain_io_data_1_out[1]), .ZN(n242) );
  NR2D1BWP U343 ( .A1(reset), .A2(io_enable), .ZN(n235) );
  AOI21D1BWP U344 ( .A1(n246), .A2(n249), .B(n235), .ZN(n240) );
  OAI22D1BWP U345 ( .A1(n237), .A2(n242), .B1(n240), .B2(n243), .ZN(
        counterChain_CounterRC_1_counter_reg__N8) );
  ND2D1BWP U346 ( .A1(io_enable), .A2(n249), .ZN(n232) );
  NR2D1BWP U347 ( .A1(n232), .A2(counterChain_io_data_0_out[2]), .ZN(n234) );
  AO22D1BWP U348 ( .A1(n233), .A2(n234), .B1(n235), .B2(
        counterChain_io_data_0_out[2]), .Z(
        counterChain_CounterRC_counter_reg__N8) );
  MUX2D1BWP U349 ( .I0(n234), .I1(n235), .S(counterChain_io_data_0_out[0]), 
        .Z(counterChain_CounterRC_counter_reg__N6) );
  INVD1BWP U351 ( .I(counterChain_io_data_1_out[0]), .ZN(n241) );
  ND2D1BWP U352 ( .A1(n236), .A2(n241), .ZN(n239) );
  INVD1BWP U353 ( .I(io_rmux1), .ZN(n238) );
  OAI21D1BWP U354 ( .A1(io_config_enable), .A2(n238), .B(n249), .ZN(N57) );
  OAI21D1BWP U355 ( .A1(n240), .A2(n241), .B(n239), .ZN(
        counterChain_CounterRC_1_counter_reg__N6) );
  OAI21D1BWP U356 ( .A1(io_config_enable), .A2(n200), .B(n249), .ZN(N46) );
  NR2D1BWP U357 ( .A1(n241), .A2(reset), .ZN(RegisterBlock_FF_3_N6) );
  NR2D1BWP U358 ( .A1(n242), .A2(reset), .ZN(RegisterBlock_FF_3_N7) );
  NR2D1BWP U359 ( .A1(n243), .A2(reset), .ZN(RegisterBlock_FF_3_N8) );
  NR2D1BWP U360 ( .A1(n244), .A2(reset), .ZN(RegisterBlock_FF_2_N6) );
  NR2D1BWP U361 ( .A1(n245), .A2(reset), .ZN(RegisterBlock_FF_2_N7) );
  NR2D1BWP U362 ( .A1(n246), .A2(reset), .ZN(RegisterBlock_FF_2_N8) );
  AO22D1BWP U364 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_0_), .B1(n247), 
        .B2(io_scalarOut[0]), .Z(RegisterBlock_FF_N6) );
  AO22D1BWP U365 ( .A1(n248), .A2(RegisterBlock_FF_io_data_out_1_), .B1(n247), 
        .B2(io_scalarOut[1]), .Z(RegisterBlock_FF_N7) );
  OAI21D1BWP U366 ( .A1(io_config_enable), .A2(n201), .B(n249), .ZN(N63) );
  OAI21D1BWP U367 ( .A1(io_config_enable), .A2(n250), .B(n249), .ZN(N39) );
endmodule

