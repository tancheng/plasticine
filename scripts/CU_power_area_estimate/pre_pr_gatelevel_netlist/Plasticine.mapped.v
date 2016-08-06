
module Pulser ( clk, reset, io_in, io_out );
  input clk, reset, io_in;
  output io_out;
  wire   N0, N1, T1, commandReg, N2, N3;

  \**SEQGEN**  commandReg_reg ( .clear(1'b0), .preset(1'b0), .next_state(N3), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(commandReg), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C19 ( .DATA1(1'b0), .DATA2(io_in), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(N3) );
  GTECH_BUF B_0 ( .A(reset), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_AND2 C22 ( .A(io_in), .B(T1), .Z(io_out) );
  GTECH_XOR2 C23 ( .A(commandReg), .B(io_in), .Z(T1) );
  GTECH_NOT I_0 ( .A(reset), .Z(N2) );
endmodule


module FF_0 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input clk, reset, io_data_in, io_data_init, io_control_enable;
  output io_data_out;
  wire   N0, N1, N2, N3, d, N4, N5, N6;

  \**SEQGEN**  ff_reg ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C23 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C24 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z(N6) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module Depulser ( clk, reset, io_in, io_rst, io_out );
  input clk, reset, io_in, io_rst;
  output io_out;
  wire   N0, N1, T0, N2, T1;

  FF_0 r ( .clk(clk), .reset(reset), .io_data_in(T1), .io_data_init(1'b0), 
        .io_data_out(io_out), .io_control_enable(T0) );
  SELECT_OP C12 ( .DATA1(1'b0), .DATA2(io_in), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(T1) );
  GTECH_BUF B_0 ( .A(io_rst), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_OR2 C15 ( .A(io_in), .B(io_rst), .Z(T0) );
  GTECH_NOT I_0 ( .A(io_rst), .Z(N2) );
endmodule


module MainControlBox ( clk, reset, io_command, io_doneTokenIn, 
        io_startTokenOut, io_statusOut );
  input clk, reset, io_command, io_doneTokenIn;
  output io_startTokenOut, io_statusOut;
  wire   N0, N1, N2, N3, T0, depulser_io_out, commandReg, N4, N5, N6, N7, N8,
         N9;

  Pulser pulser ( .clk(clk), .reset(reset), .io_in(commandReg), .io_out(
        io_startTokenOut) );
  Depulser depulser ( .clk(clk), .reset(reset), .io_in(io_doneTokenIn), 
        .io_rst(T0), .io_out(depulser_io_out) );
  \**SEQGEN**  commandReg_reg ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(commandReg), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  statusReg_reg ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_statusOut), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C30 ( .DATA1(1'b0), .DATA2(io_command), .CONTROL1(N0), .CONTROL2(
        N1), .Z(N6) );
  GTECH_BUF B_0 ( .A(N5), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C31 ( .DATA1(1'b0), .DATA2(depulser_io_out), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(N9) );
  GTECH_BUF B_2 ( .A(N8), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_command), .Z(T0) );
  GTECH_NOT I_1 ( .A(reset), .Z(N4) );
  GTECH_BUF B_4 ( .A(reset), .Z(N5) );
  GTECH_NOT I_2 ( .A(reset), .Z(N7) );
  GTECH_BUF B_5 ( .A(reset), .Z(N8) );
endmodule


module SRAM_3 ( clk, io_raddr, io_wen, io_waddr, io_wdata, io_rdata );
  input [3:0] io_raddr;
  input [3:0] io_waddr;
  input [7:0] io_wdata;
  output [7:0] io_rdata;
  input clk, io_wen;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, T3, N14,
         N15, N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28,
         N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42,
         N43, N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54;
  wire   [3:0] raddr_reg;
  wire   [127:0] mem;

  \**SEQGEN**  mem_reg_15__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[127]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[126]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[125]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[124]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[123]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[122]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[121]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[120]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_14__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[119]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[118]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[117]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[116]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[115]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[114]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[113]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[112]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_13__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[111]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[110]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[109]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[108]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[107]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[106]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[105]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[104]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_12__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[103]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[102]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[101]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[100]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[99]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[98]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[97]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[96]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_11__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[95]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[94]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[93]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[92]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[91]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[90]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[89]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[88]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_10__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[87]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[86]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[85]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[84]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[83]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[82]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[81]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[80]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_9__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[79]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[78]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[77]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[76]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[75]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[74]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[73]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[72]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_8__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[71]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[70]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[69]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[68]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[67]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[66]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[65]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[64]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_7__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[63]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[62]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[61]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[60]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[59]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[58]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[57]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[56]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_6__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[55]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[54]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[53]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[52]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[51]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[50]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[49]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[48]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_5__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[47]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[46]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[45]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[44]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[43]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[42]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[41]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[40]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_4__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[39]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[38]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[37]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[36]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[35]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[34]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[33]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[32]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_3__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[31]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[30]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[29]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[28]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[27]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[26]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[25]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[24]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_2__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[23]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[22]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[21]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[20]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[19]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[18]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[17]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[16]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_1__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[15]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[14]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[13]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[12]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[11]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[10]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[9]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[8]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_0__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[7]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[6]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[5]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[4]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  raddr_reg_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  GTECH_AND2 C619 ( .A(io_waddr[2]), .B(io_waddr[3]), .Z(N31) );
  GTECH_AND2 C620 ( .A(N0), .B(io_waddr[3]), .Z(N32) );
  GTECH_NOT I_0 ( .A(io_waddr[2]), .Z(N0) );
  GTECH_AND2 C621 ( .A(io_waddr[2]), .B(N1), .Z(N33) );
  GTECH_NOT I_1 ( .A(io_waddr[3]), .Z(N1) );
  GTECH_AND2 C622 ( .A(N2), .B(N3), .Z(N34) );
  GTECH_NOT I_2 ( .A(io_waddr[2]), .Z(N2) );
  GTECH_NOT I_3 ( .A(io_waddr[3]), .Z(N3) );
  GTECH_AND2 C623 ( .A(io_waddr[0]), .B(io_waddr[1]), .Z(N35) );
  GTECH_AND2 C624 ( .A(N4), .B(io_waddr[1]), .Z(N36) );
  GTECH_NOT I_4 ( .A(io_waddr[0]), .Z(N4) );
  GTECH_AND2 C625 ( .A(io_waddr[0]), .B(N5), .Z(N37) );
  GTECH_NOT I_5 ( .A(io_waddr[1]), .Z(N5) );
  GTECH_AND2 C626 ( .A(N6), .B(N7), .Z(N38) );
  GTECH_NOT I_6 ( .A(io_waddr[0]), .Z(N6) );
  GTECH_NOT I_7 ( .A(io_waddr[1]), .Z(N7) );
  GTECH_AND2 C627 ( .A(N31), .B(N35), .Z(N39) );
  GTECH_AND2 C628 ( .A(N31), .B(N36), .Z(N40) );
  GTECH_AND2 C629 ( .A(N31), .B(N37), .Z(N41) );
  GTECH_AND2 C630 ( .A(N31), .B(N38), .Z(N42) );
  GTECH_AND2 C631 ( .A(N32), .B(N35), .Z(N43) );
  GTECH_AND2 C632 ( .A(N32), .B(N36), .Z(N44) );
  GTECH_AND2 C633 ( .A(N32), .B(N37), .Z(N45) );
  GTECH_AND2 C634 ( .A(N32), .B(N38), .Z(N46) );
  GTECH_AND2 C635 ( .A(N33), .B(N35), .Z(N47) );
  GTECH_AND2 C636 ( .A(N33), .B(N36), .Z(N48) );
  GTECH_AND2 C637 ( .A(N33), .B(N37), .Z(N49) );
  GTECH_AND2 C638 ( .A(N33), .B(N38), .Z(N50) );
  GTECH_AND2 C639 ( .A(N34), .B(N35), .Z(N51) );
  GTECH_AND2 C640 ( .A(N34), .B(N36), .Z(N52) );
  GTECH_AND2 C641 ( .A(N34), .B(N37), .Z(N53) );
  GTECH_AND2 C642 ( .A(N34), .B(N38), .Z(N54) );
  SELECT_OP C643 ( .DATA1({N39, N40, N41, N42, N43, N44, N45, N46, N47, N48, 
        N49, N50, N51, N52, N53, N54}), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .CONTROL1(N8), .CONTROL2(N9), .Z({N30, N29, N28, N27, N26, N25, N24, 
        N23, N22, N21, N20, N19, N18, N17, N16, N15}) );
  GTECH_BUF B_0 ( .A(io_wen), .Z(N8) );
  GTECH_BUF B_1 ( .A(N14), .Z(N9) );
  MUX_OP C644 ( .D0({mem[0], mem[1], mem[2], mem[3], mem[4], mem[5], mem[6], 
        mem[7]}), .D1({mem[8], mem[9], mem[10], mem[11], mem[12], mem[13], 
        mem[14], mem[15]}), .D2({mem[16], mem[17], mem[18], mem[19], mem[20], 
        mem[21], mem[22], mem[23]}), .D3({mem[24], mem[25], mem[26], mem[27], 
        mem[28], mem[29], mem[30], mem[31]}), .D4({mem[32], mem[33], mem[34], 
        mem[35], mem[36], mem[37], mem[38], mem[39]}), .D5({mem[40], mem[41], 
        mem[42], mem[43], mem[44], mem[45], mem[46], mem[47]}), .D6({mem[48], 
        mem[49], mem[50], mem[51], mem[52], mem[53], mem[54], mem[55]}), .D7({
        mem[56], mem[57], mem[58], mem[59], mem[60], mem[61], mem[62], mem[63]}), .D8({mem[64], mem[65], mem[66], mem[67], mem[68], mem[69], mem[70], mem[71]}), .D9({mem[72], mem[73], mem[74], mem[75], mem[76], mem[77], mem[78], mem[79]}), .D10({mem[80], mem[81], mem[82], mem[83], mem[84], mem[85], mem[86], 
        mem[87]}), .D11({mem[88], mem[89], mem[90], mem[91], mem[92], mem[93], 
        mem[94], mem[95]}), .D12({mem[96], mem[97], mem[98], mem[99], mem[100], 
        mem[101], mem[102], mem[103]}), .D13({mem[104], mem[105], mem[106], 
        mem[107], mem[108], mem[109], mem[110], mem[111]}), .D14({mem[112], 
        mem[113], mem[114], mem[115], mem[116], mem[117], mem[118], mem[119]}), 
        .D15({mem[120], mem[121], mem[122], mem[123], mem[124], mem[125], 
        mem[126], mem[127]}), .S0(N10), .S1(N11), .S2(N12), .S3(N13), .Z({
        io_rdata[0], io_rdata[1], io_rdata[2], io_rdata[3], io_rdata[4], 
        io_rdata[5], io_rdata[6], io_rdata[7]}) );
  GTECH_BUF B_2 ( .A(raddr_reg[0]), .Z(N10) );
  GTECH_BUF B_3 ( .A(raddr_reg[1]), .Z(N11) );
  GTECH_BUF B_4 ( .A(raddr_reg[2]), .Z(N12) );
  GTECH_BUF B_5 ( .A(raddr_reg[3]), .Z(N13) );
  GTECH_NOT I_8 ( .A(io_wen), .Z(T3) );
  GTECH_NOT I_9 ( .A(io_wen), .Z(N14) );
endmodule


module MuxVec_0_3 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_1_0;
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C14 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_1_3 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [7:0] io_ins_1_0;
  input [7:0] io_ins_0_0;
  output [7:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C18 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_2_3 ( io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;

  assign io_out_0[3] = io_ins_0_0[3];
  assign io_out_0[2] = io_ins_0_0[2];
  assign io_out_0[1] = io_ins_0_0[1];
  assign io_out_0[0] = io_ins_0_0[0];

endmodule


module FF_1_3 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module Counter_3 ( clk, reset, io_data_max, io_data_stride, io_data_out, 
        io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_control_reset, io_control_enable, io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, isMax, N7, N8;
  wire   [7:0] T4;
  wire   [7:0] T1;
  wire   [7:0] T2;
  wire   [8:0] newval;

  LEQ_UNS_OP lte_301 ( .A({1'b0, io_data_max}), .B(newval), .Z(isMax) );
  FF_1_3 reg_ ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(io_data_out), 
        .io_control_enable(io_control_enable) );
  ADD_UNS_OP add_297 ( .A(io_data_out), .B(io_data_stride), .Z(newval) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(T4) );
  GTECH_BUF B_0 ( .A(io_control_reset), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C49 ( .DATA1(T2), .DATA2(newval[7:0]), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(isMax), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C50 ( .DATA1(io_data_out), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0}), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_control_saturate), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(io_control_reset), .Z(N6) );
  GTECH_NOT I_1 ( .A(isMax), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_control_saturate), .Z(N8) );
  GTECH_AND2 C62 ( .A(io_control_enable), .B(isMax), .Z(io_control_done) );
endmodule


module CounterRC_3 ( clk, reset, io_config_enable, io_data_max, io_data_stride, 
        io_data_out, io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_config_enable, io_control_reset, io_control_enable,
         io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13,
         config__strideConst, N14, N15, configIn_strideConst, config__maxConst,
         N16, configIn_maxConst, N17, N18, N19, N20, N21, N22, N23, N24, N25,
         N26, N27, N28, N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39,
         N40, N41, N42;
  wire   [7:0] T0;
  wire   [7:0] config__stride;
  wire   [7:0] configIn_stride;
  wire   [7:0] T3;
  wire   [7:0] config__max;
  wire   [7:0] configIn_max;

  Counter_3 counter ( .clk(clk), .reset(reset), .io_data_max(T3), 
        .io_data_stride(T0), .io_data_out(io_data_out), .io_control_reset(
        io_control_reset), .io_control_enable(io_control_enable), 
        .io_control_saturate(io_control_saturate), .io_control_done(
        io_control_done) );
  \**SEQGEN**  config__stride_reg_7_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N26), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_6_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N25), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N24), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N19), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__strideConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N29), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__strideConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N39), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N38), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N37), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N36), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N35), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N34), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N33), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N32), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__maxConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N42), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__maxConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C145 ( .DATA1(config__stride), .DATA2(io_data_stride), .CONTROL1(
        N0), .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__strideConst), .Z(N0) );
  GTECH_BUF B_1 ( .A(N14), .Z(N1) );
  SELECT_OP C146 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__stride), .CONTROL1(N2), .CONTROL2(N3), .Z(
        configIn_stride) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N15), .Z(N3) );
  SELECT_OP C147 ( .DATA1(1'b0), .DATA2(config__strideConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_strideConst) );
  SELECT_OP C148 ( .DATA1(config__max), .DATA2(io_data_max), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T3) );
  GTECH_BUF B_4 ( .A(config__maxConst), .Z(N4) );
  GTECH_BUF B_5 ( .A(N16), .Z(N5) );
  SELECT_OP C149 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__max), .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_max) );
  SELECT_OP C150 ( .DATA1(1'b0), .DATA2(config__maxConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_maxConst) );
  SELECT_OP C151 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1}), 
        .DATA2(configIn_stride), .CONTROL1(N6), .CONTROL2(N7), .Z({N26, N25, 
        N24, N23, N22, N21, N20, N19}) );
  GTECH_BUF B_6 ( .A(N18), .Z(N6) );
  GTECH_BUF B_7 ( .A(N17), .Z(N7) );
  SELECT_OP C152 ( .DATA1(1'b1), .DATA2(configIn_strideConst), .CONTROL1(N8), 
        .CONTROL2(N9), .Z(N29) );
  GTECH_BUF B_8 ( .A(N28), .Z(N8) );
  GTECH_BUF B_9 ( .A(N27), .Z(N9) );
  SELECT_OP C153 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0, 1'b1}), 
        .DATA2(configIn_max), .CONTROL1(N10), .CONTROL2(N11), .Z({N39, N38, 
        N37, N36, N35, N34, N33, N32}) );
  GTECH_BUF B_10 ( .A(N31), .Z(N10) );
  GTECH_BUF B_11 ( .A(N30), .Z(N11) );
  SELECT_OP C154 ( .DATA1(1'b1), .DATA2(configIn_maxConst), .CONTROL1(N12), 
        .CONTROL2(N13), .Z(N42) );
  GTECH_BUF B_12 ( .A(N41), .Z(N12) );
  GTECH_BUF B_13 ( .A(N40), .Z(N13) );
  GTECH_NOT I_0 ( .A(config__strideConst), .Z(N14) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N15) );
  GTECH_NOT I_2 ( .A(config__maxConst), .Z(N16) );
  GTECH_NOT I_3 ( .A(reset), .Z(N17) );
  GTECH_BUF B_14 ( .A(reset), .Z(N18) );
  GTECH_NOT I_4 ( .A(reset), .Z(N27) );
  GTECH_BUF B_15 ( .A(reset), .Z(N28) );
  GTECH_NOT I_5 ( .A(reset), .Z(N30) );
  GTECH_BUF B_16 ( .A(reset), .Z(N31) );
  GTECH_NOT I_6 ( .A(reset), .Z(N40) );
  GTECH_BUF B_17 ( .A(reset), .Z(N41) );
endmodule


module FF_1_2 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module Counter_2 ( clk, reset, io_data_max, io_data_stride, io_data_out, 
        io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_control_reset, io_control_enable, io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, isMax, N7, N8;
  wire   [7:0] T4;
  wire   [7:0] T1;
  wire   [7:0] T2;
  wire   [8:0] newval;

  LEQ_UNS_OP lte_301 ( .A({1'b0, io_data_max}), .B(newval), .Z(isMax) );
  FF_1_2 reg_ ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(io_data_out), 
        .io_control_enable(io_control_enable) );
  ADD_UNS_OP add_297 ( .A(io_data_out), .B(io_data_stride), .Z(newval) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(T4) );
  GTECH_BUF B_0 ( .A(io_control_reset), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C49 ( .DATA1(T2), .DATA2(newval[7:0]), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(isMax), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C50 ( .DATA1(io_data_out), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0}), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_control_saturate), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(io_control_reset), .Z(N6) );
  GTECH_NOT I_1 ( .A(isMax), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_control_saturate), .Z(N8) );
  GTECH_AND2 C62 ( .A(io_control_enable), .B(isMax), .Z(io_control_done) );
endmodule


module CounterRC_2 ( clk, reset, io_config_enable, io_data_max, io_data_stride, 
        io_data_out, io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_config_enable, io_control_reset, io_control_enable,
         io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13,
         config__strideConst, N14, N15, configIn_strideConst, config__maxConst,
         N16, configIn_maxConst, N17, N18, N19, N20, N21, N22, N23, N24, N25,
         N26, N27, N28, N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39,
         N40, N41, N42;
  wire   [7:0] T0;
  wire   [7:0] config__stride;
  wire   [7:0] configIn_stride;
  wire   [7:0] T3;
  wire   [7:0] config__max;
  wire   [7:0] configIn_max;

  Counter_2 counter ( .clk(clk), .reset(reset), .io_data_max(T3), 
        .io_data_stride(T0), .io_data_out(io_data_out), .io_control_reset(
        io_control_reset), .io_control_enable(io_control_enable), 
        .io_control_saturate(io_control_saturate), .io_control_done(
        io_control_done) );
  \**SEQGEN**  config__stride_reg_7_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N26), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_6_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N25), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N24), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N19), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__strideConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N29), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__strideConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N39), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N38), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N37), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N36), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N35), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N34), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N33), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N32), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__maxConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N42), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__maxConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C145 ( .DATA1(config__stride), .DATA2(io_data_stride), .CONTROL1(
        N0), .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__strideConst), .Z(N0) );
  GTECH_BUF B_1 ( .A(N14), .Z(N1) );
  SELECT_OP C146 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__stride), .CONTROL1(N2), .CONTROL2(N3), .Z(
        configIn_stride) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N15), .Z(N3) );
  SELECT_OP C147 ( .DATA1(1'b0), .DATA2(config__strideConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_strideConst) );
  SELECT_OP C148 ( .DATA1(config__max), .DATA2(io_data_max), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T3) );
  GTECH_BUF B_4 ( .A(config__maxConst), .Z(N4) );
  GTECH_BUF B_5 ( .A(N16), .Z(N5) );
  SELECT_OP C149 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__max), .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_max) );
  SELECT_OP C150 ( .DATA1(1'b0), .DATA2(config__maxConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_maxConst) );
  SELECT_OP C151 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1}), 
        .DATA2(configIn_stride), .CONTROL1(N6), .CONTROL2(N7), .Z({N26, N25, 
        N24, N23, N22, N21, N20, N19}) );
  GTECH_BUF B_6 ( .A(N18), .Z(N6) );
  GTECH_BUF B_7 ( .A(N17), .Z(N7) );
  SELECT_OP C152 ( .DATA1(1'b1), .DATA2(configIn_strideConst), .CONTROL1(N8), 
        .CONTROL2(N9), .Z(N29) );
  GTECH_BUF B_8 ( .A(N28), .Z(N8) );
  GTECH_BUF B_9 ( .A(N27), .Z(N9) );
  SELECT_OP C153 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0, 1'b1}), 
        .DATA2(configIn_max), .CONTROL1(N10), .CONTROL2(N11), .Z({N39, N38, 
        N37, N36, N35, N34, N33, N32}) );
  GTECH_BUF B_10 ( .A(N31), .Z(N10) );
  GTECH_BUF B_11 ( .A(N30), .Z(N11) );
  SELECT_OP C154 ( .DATA1(1'b1), .DATA2(configIn_maxConst), .CONTROL1(N12), 
        .CONTROL2(N13), .Z(N42) );
  GTECH_BUF B_12 ( .A(N41), .Z(N12) );
  GTECH_BUF B_13 ( .A(N40), .Z(N13) );
  GTECH_NOT I_0 ( .A(config__strideConst), .Z(N14) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N15) );
  GTECH_NOT I_2 ( .A(config__maxConst), .Z(N16) );
  GTECH_NOT I_3 ( .A(reset), .Z(N17) );
  GTECH_BUF B_14 ( .A(reset), .Z(N18) );
  GTECH_NOT I_4 ( .A(reset), .Z(N27) );
  GTECH_BUF B_15 ( .A(reset), .Z(N28) );
  GTECH_NOT I_5 ( .A(reset), .Z(N30) );
  GTECH_BUF B_16 ( .A(reset), .Z(N31) );
  GTECH_NOT I_6 ( .A(reset), .Z(N40) );
  GTECH_BUF B_17 ( .A(reset), .Z(N41) );
endmodule


module CounterChain_1 ( clk, reset, io_config_enable, io_data_1_max, 
        io_data_1_stride, io_data_1_out, io_data_0_max, io_data_0_stride, 
        io_data_0_out, io_control_1_enable, io_control_1_done, 
        io_control_0_enable, io_control_0_done );
  input [7:0] io_data_1_max;
  input [7:0] io_data_1_stride;
  output [7:0] io_data_1_out;
  input [7:0] io_data_0_max;
  input [7:0] io_data_0_stride;
  output [7:0] io_data_0_out;
  input clk, reset, io_config_enable, io_control_1_enable, io_control_0_enable;
  output io_control_1_done, io_control_0_done;
  wire   N0, N1, N2, N3, N4, N5, config__chain_0, N6, T0, T1, configIn_chain_0,
         N7, N8, N9, N10, net1483, net1484;

  CounterRC_3 CounterRC ( .clk(clk), .reset(reset), .io_config_enable(net1484), 
        .io_data_max(io_data_0_max), .io_data_stride(io_data_0_stride), 
        .io_data_out(io_data_0_out), .io_control_reset(1'b0), 
        .io_control_enable(io_control_0_enable), .io_control_saturate(1'b0), 
        .io_control_done(io_control_0_done) );
  CounterRC_2 CounterRC_1 ( .clk(clk), .reset(reset), .io_config_enable(
        net1483), .io_data_max(io_data_1_max), .io_data_stride(
        io_data_1_stride), .io_data_out(io_data_1_out), .io_control_reset(1'b0), .io_control_enable(T0), .io_control_saturate(1'b0), .io_control_done(
        io_control_1_done) );
  \**SEQGEN**  config__chain_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N10), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__chain_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C30 ( .DATA1(T1), .DATA2(io_control_1_enable), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__chain_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C31 ( .DATA1(1'b0), .DATA2(config__chain_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_chain_0) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C32 ( .DATA1(1'b0), .DATA2(configIn_chain_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(N10) );
  GTECH_BUF B_4 ( .A(N9), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(config__chain_0), .Z(N6) );
  GTECH_AND2 C38 ( .A(io_control_1_enable), .B(io_control_0_done), .Z(T1) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N7) );
  GTECH_NOT I_2 ( .A(reset), .Z(N8) );
  GTECH_BUF B_6 ( .A(reset), .Z(N9) );
endmodule


module LUT_7 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module MuxN_4_11 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_10 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module Crossbar_0_1 ( clk, reset, io_config_enable, io_ins_2, io_ins_1, 
        io_ins_0, io_outs_1, io_outs_0 );
  input clk, reset, io_config_enable, io_ins_2, io_ins_1, io_ins_0;
  output io_outs_1, io_outs_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [1:0] configIn_outSelect_1;
  wire   [1:0] config__outSelect_1;
  wire   [1:0] configIn_outSelect_0;
  wire   [1:0] config__outSelect_0;

  MuxN_4_11 MuxN ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_0), .io_out(io_outs_0) );
  MuxN_4_10 MuxN_1 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_1), .io_out(io_outs_1) );
  \**SEQGEN**  config__outSelect_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N7), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N6), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N9), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N8), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C47 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_1) );
  GTECH_BUF B_0 ( .A(io_config_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_0), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_0) );
  SELECT_OP C49 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_1), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C50 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8}) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_4_6 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_7 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_8 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_9 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module Crossbar_1_1 ( clk, reset, io_config_enable, io_ins_2, io_ins_1, 
        io_ins_0, io_outs_3, io_outs_2, io_outs_1, io_outs_0 );
  input clk, reset, io_config_enable, io_ins_2, io_ins_1, io_ins_0;
  output io_outs_3, io_outs_2, io_outs_1, io_outs_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [1:0] configIn_outSelect_3;
  wire   [1:0] config__outSelect_3;
  wire   [1:0] configIn_outSelect_2;
  wire   [1:0] config__outSelect_2;
  wire   [1:0] configIn_outSelect_1;
  wire   [1:0] config__outSelect_1;
  wire   [1:0] configIn_outSelect_0;
  wire   [1:0] config__outSelect_0;

  MuxN_4_9 MuxN ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(io_ins_0), .io_sel(config__outSelect_0), .io_out(io_outs_0) );
  MuxN_4_8 MuxN_1 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_1), .io_out(io_outs_1) );
  MuxN_4_7 MuxN_2 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_2), .io_out(io_outs_2) );
  MuxN_4_6 MuxN_3 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_3), .io_out(io_outs_3) );
  \**SEQGEN**  config__outSelect_3_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N7), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_3[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_3_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N6), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_3[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_2_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N9), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_2[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_2_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N8), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_2[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N11), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N10), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C89 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_3), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_3) );
  GTECH_BUF B_0 ( .A(io_config_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C90 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_2), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_2) );
  SELECT_OP C91 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_1) );
  SELECT_OP C92 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_0), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_0) );
  SELECT_OP C93 ( .DATA1({1'b1, 1'b0}), .DATA2(configIn_outSelect_3), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C94 ( .DATA1({1'b0, 1'b0}), .DATA2(configIn_outSelect_2), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8}) );
  SELECT_OP C95 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_1), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N11, N10}) );
  SELECT_OP C96 ( .DATA1({1'b1, 1'b1}), .DATA2(configIn_outSelect_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12}) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_2_3 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [3:0] io_data_in;
  input [3:0] io_data_init;
  output [3:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [3:0] d;

  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C32 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C33 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module UpDownCtr_3 ( clk, reset, io_initval, io_init, io_inc, io_dec, io_gtz
 );
  input [3:0] io_initval;
  input clk, reset, io_init, io_inc, io_dec;
  output io_gtz;
  wire   N0, N1, N2, N3, T1, T0, N4, N5;
  wire   [3:0] T2;
  wire   [3:0] T3;
  wire   [3:0] incval;
  wire   [3:0] decval;
  wire   [3:0] reg__io_data_out;

  SUB_UNS_OP sub_898 ( .A(reg__io_data_out), .B(1'b1), .Z(decval) );
  ADD_UNS_OP add_899 ( .A(reg__io_data_out), .B(1'b1), .Z(incval) );
  LT_UNS_OP lt_901 ( .A(1'b0), .B(reg__io_data_out), .Z(io_gtz) );
  FF_2_3 reg_ ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0}), .io_data_out(reg__io_data_out), 
        .io_control_enable(T0) );
  SELECT_OP C25 ( .DATA1(io_initval), .DATA2(T3), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(T2) );
  GTECH_BUF B_0 ( .A(io_init), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C26 ( .DATA1(incval), .DATA2(decval), .CONTROL1(N2), .CONTROL2(N3), 
        .Z(T3) );
  GTECH_BUF B_2 ( .A(io_inc), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_OR2 C29 ( .A(T1), .B(io_init), .Z(T0) );
  GTECH_XOR2 C30 ( .A(io_inc), .B(io_dec), .Z(T1) );
  GTECH_NOT I_0 ( .A(io_init), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_inc), .Z(N5) );
endmodule


module LUT_4 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module LUT_5 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module LUT_6 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module FF_2_2 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [3:0] io_data_in;
  input [3:0] io_data_init;
  output [3:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [3:0] d;

  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C32 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C33 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module UpDownCtr_2 ( clk, reset, io_initval, io_init, io_inc, io_dec, io_gtz
 );
  input [3:0] io_initval;
  input clk, reset, io_init, io_inc, io_dec;
  output io_gtz;
  wire   N0, N1, N2, N3, T1, T0, N4, N5;
  wire   [3:0] T2;
  wire   [3:0] T3;
  wire   [3:0] incval;
  wire   [3:0] decval;
  wire   [3:0] reg__io_data_out;

  SUB_UNS_OP sub_898 ( .A(reg__io_data_out), .B(1'b1), .Z(decval) );
  ADD_UNS_OP add_899 ( .A(reg__io_data_out), .B(1'b1), .Z(incval) );
  LT_UNS_OP lt_901 ( .A(1'b0), .B(reg__io_data_out), .Z(io_gtz) );
  FF_2_2 reg_ ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0}), .io_data_out(reg__io_data_out), 
        .io_control_enable(T0) );
  SELECT_OP C25 ( .DATA1(io_initval), .DATA2(T3), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(T2) );
  GTECH_BUF B_0 ( .A(io_init), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C26 ( .DATA1(incval), .DATA2(decval), .CONTROL1(N2), .CONTROL2(N3), 
        .Z(T3) );
  GTECH_BUF B_2 ( .A(io_inc), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_OR2 C29 ( .A(T1), .B(io_init), .Z(T0) );
  GTECH_XOR2 C30 ( .A(io_inc), .B(io_dec), .Z(T1) );
  GTECH_NOT I_0 ( .A(io_init), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_inc), .Z(N5) );
endmodule


module CUControlBox_1 ( clk, reset, io_config_enable, io_tokenIns_1, 
        io_tokenIns_0, io_done_1, io_done_0, io_tokenOuts_1, io_tokenOuts_0, 
        io_enable_1, io_enable_0 );
  input clk, reset, io_config_enable, io_tokenIns_1, io_tokenIns_0, io_done_1,
         io_done_0;
  output io_tokenOuts_1, io_tokenOuts_0, io_enable_1, io_enable_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, decXbar_io_outs_1,
         decXbar_io_outs_0, incXbar_io_outs_3, incXbar_io_outs_2,
         incXbar_io_outs_1, incXbar_io_outs_0, UpDownCtr_io_gtz,
         UpDownCtr_1_io_gtz, N12, N13, N14, N15, N16, N17, N18, N19, N20, N21,
         N22, N23, net1473, net1474, net1475, net1476, net1477, net1478,
         net1479, net1480, net1481, net1482;
  wire   [3:0] configIn_udcInit_1;
  wire   [3:0] config__udcInit_1;
  wire   [3:0] configIn_udcInit_0;
  wire   [3:0] config__udcInit_0;

  LUT_7 LUT ( .clk(clk), .reset(reset), .io_config_enable(net1482), .io_ins_1(
        io_done_1), .io_ins_0(io_done_0), .io_out(io_tokenOuts_0) );
  LUT_6 LUT_1 ( .clk(clk), .reset(reset), .io_config_enable(net1481), 
        .io_ins_1(io_done_1), .io_ins_0(io_done_0), .io_out(io_tokenOuts_1) );
  Crossbar_0_1 decXbar ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_ins_2(net1478), .io_ins_1(net1479), .io_ins_0(
        net1480), .io_outs_1(decXbar_io_outs_1), .io_outs_0(decXbar_io_outs_0)
         );
  Crossbar_1_1 incXbar ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_ins_2(net1475), .io_ins_1(net1476), .io_ins_0(
        net1477), .io_outs_3(incXbar_io_outs_3), .io_outs_2(incXbar_io_outs_2), 
        .io_outs_1(incXbar_io_outs_1), .io_outs_0(incXbar_io_outs_0) );
  UpDownCtr_3 UpDownCtr ( .clk(clk), .reset(reset), .io_initval(
        config__udcInit_0), .io_init(incXbar_io_outs_2), .io_inc(
        incXbar_io_outs_0), .io_dec(decXbar_io_outs_0), .io_gtz(
        UpDownCtr_io_gtz) );
  UpDownCtr_2 UpDownCtr_1 ( .clk(clk), .reset(reset), .io_initval(
        config__udcInit_1), .io_init(incXbar_io_outs_3), .io_inc(
        incXbar_io_outs_1), .io_dec(decXbar_io_outs_1), .io_gtz(
        UpDownCtr_1_io_gtz) );
  LUT_5 LUT_2 ( .clk(clk), .reset(reset), .io_config_enable(net1474), 
        .io_ins_1(UpDownCtr_1_io_gtz), .io_ins_0(UpDownCtr_io_gtz), .io_out(
        io_enable_0) );
  LUT_4 LUT_3 ( .clk(clk), .reset(reset), .io_config_enable(net1473), 
        .io_ins_1(UpDownCtr_1_io_gtz), .io_ins_0(UpDownCtr_io_gtz), .io_out(
        io_enable_1) );
  \**SEQGEN**  config__udcInit_1_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N17), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N16), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C59 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(config__udcInit_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_udcInit_1) );
  GTECH_BUF B_0 ( .A(N9), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C60 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(config__udcInit_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_udcInit_0) );
  GTECH_BUF B_2 ( .A(N11), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C61 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(configIn_udcInit_1), 
        .CONTROL1(N4), .CONTROL2(N5), .Z({N17, N16, N15, N14}) );
  GTECH_BUF B_4 ( .A(N13), .Z(N4) );
  GTECH_BUF B_5 ( .A(N12), .Z(N5) );
  SELECT_OP C62 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b1}), .DATA2(configIn_udcInit_0), 
        .CONTROL1(N6), .CONTROL2(N7), .Z({N23, N22, N21, N20}) );
  GTECH_BUF B_6 ( .A(N19), .Z(N6) );
  GTECH_BUF B_7 ( .A(N18), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N8) );
  GTECH_BUF B_8 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N10) );
  GTECH_BUF B_9 ( .A(io_config_enable), .Z(N11) );
  GTECH_NOT I_2 ( .A(reset), .Z(N12) );
  GTECH_BUF B_10 ( .A(reset), .Z(N13) );
  GTECH_NOT I_3 ( .A(reset), .Z(N18) );
  GTECH_BUF B_11 ( .A(reset), .Z(N19) );
endmodule


module MuxN_0_15 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_2_3 ( io_ins_8, io_ins_7, io_ins_6, io_ins_5, io_ins_4, io_ins_3, 
        io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_8;
  input [7:0] io_ins_7;
  input [7:0] io_ins_6;
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [3:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T1;
  wire   [7:0] T9;
  wire   [7:0] T2;
  wire   [7:0] T6;
  wire   [7:0] T3;
  wire   [7:0] T12;
  wire   [7:0] T10;

  SELECT_OP C109 ( .DATA1(io_ins_8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[3]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C110 ( .DATA1(T9), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[2]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C111 ( .DATA1(T6), .DATA2(T3), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[1]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C112 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T3) );
  GTECH_BUF B_6 ( .A(io_sel[0]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C113 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  SELECT_OP C114 ( .DATA1(T12), .DATA2(T10), .CONTROL1(N4), .CONTROL2(N5), .Z(
        T9) );
  SELECT_OP C115 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T10) );
  SELECT_OP C116 ( .DATA1(io_ins_7), .DATA2(io_ins_6), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T12) );
  GTECH_NOT I_0 ( .A(io_sel[3]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_sel[2]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_sel[1]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_sel[0]), .Z(N11) );
endmodule


module IntFU_3 ( io_a, io_b, io_opcode, io_out );
  input [7:0] io_a;
  input [7:0] io_b;
  input [3:0] io_opcode;
  output [7:0] io_out;
  wire   net1506, net1507, net1508, net1509, net1510, net1511, net1512,
         net1513;
  wire   [7:0] T0;
  wire   [7:0] T1;
  wire   [7:0] T7;
  wire   [7:0] T3;
  wire   [7:0] T4;
  wire   [7:0] T5;
  wire   [0:0] T8;

  ADD_UNS_OP add_1177 ( .A(io_a), .B(io_b), .Z(T0) );
  SUB_UNS_OP sub_1178 ( .A(io_a), .B(io_b), .Z(T1) );
  MULT_UNS_OP mult_1180 ( .A(io_a), .B(io_b), .Z({net1506, net1507, net1508, 
        net1509, net1510, net1511, net1512, net1513, T7}) );
  DIV_UNS_OP div_1181 ( .A(io_a), .B(io_b), .QUOTIENT(T3) );
  EQ_UNS_OP eq_1185 ( .A(io_a), .B(io_b), .Z(T8[0]) );
  MuxN_2_3 m ( .io_ins_8(io_b), .io_ins_7(io_a), .io_ins_6({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, T8[0]}), .io_ins_5(T5), .io_ins_4(T4), 
        .io_ins_3(T3), .io_ins_2(T7), .io_ins_1(T1), .io_ins_0(T0), .io_sel(
        io_opcode), .io_out(io_out) );
  GTECH_AND2 C23 ( .A(io_a[7]), .B(io_b[7]), .Z(T4[7]) );
  GTECH_AND2 C24 ( .A(io_a[6]), .B(io_b[6]), .Z(T4[6]) );
  GTECH_AND2 C25 ( .A(io_a[5]), .B(io_b[5]), .Z(T4[5]) );
  GTECH_AND2 C26 ( .A(io_a[4]), .B(io_b[4]), .Z(T4[4]) );
  GTECH_AND2 C27 ( .A(io_a[3]), .B(io_b[3]), .Z(T4[3]) );
  GTECH_AND2 C28 ( .A(io_a[2]), .B(io_b[2]), .Z(T4[2]) );
  GTECH_AND2 C29 ( .A(io_a[1]), .B(io_b[1]), .Z(T4[1]) );
  GTECH_AND2 C30 ( .A(io_a[0]), .B(io_b[0]), .Z(T4[0]) );
  GTECH_OR2 C31 ( .A(io_a[7]), .B(io_b[7]), .Z(T5[7]) );
  GTECH_OR2 C32 ( .A(io_a[6]), .B(io_b[6]), .Z(T5[6]) );
  GTECH_OR2 C33 ( .A(io_a[5]), .B(io_b[5]), .Z(T5[5]) );
  GTECH_OR2 C34 ( .A(io_a[4]), .B(io_b[4]), .Z(T5[4]) );
  GTECH_OR2 C35 ( .A(io_a[3]), .B(io_b[3]), .Z(T5[3]) );
  GTECH_OR2 C36 ( .A(io_a[2]), .B(io_b[2]), .Z(T5[2]) );
  GTECH_OR2 C37 ( .A(io_a[1]), .B(io_b[1]), .Z(T5[1]) );
  GTECH_OR2 C38 ( .A(io_a[0]), .B(io_b[0]), .Z(T5[0]) );
endmodule


module FF_1_27 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_3_7 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module MuxN_0_6 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_7 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module FF_1_22 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_23 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_24 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_25 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_26 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_3_6 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module RegisterBlock_3 ( clk, reset, io_writeData, io_passData_3, 
        io_passData_2, io_passData_1, io_passData_0, io_writeSel, 
        io_readLocalASel, io_readLocalBSel, io_readRemoteASel, 
        io_readRemoteBSel, io_readLocalA, io_readLocalB, io_readRemoteA, 
        io_readRemoteB, io_passDataOut_3, io_passDataOut_2, io_passDataOut_1, 
        io_passDataOut_0 );
  input [7:0] io_writeData;
  input [7:0] io_passData_3;
  input [7:0] io_passData_2;
  input [7:0] io_passData_1;
  input [7:0] io_passData_0;
  input [5:0] io_writeSel;
  input [2:0] io_readLocalASel;
  input [2:0] io_readLocalBSel;
  input [1:0] io_readRemoteASel;
  input [1:0] io_readRemoteBSel;
  output [7:0] io_readLocalA;
  output [7:0] io_readLocalB;
  output [7:0] io_readRemoteA;
  output [7:0] io_readRemoteB;
  output [7:0] io_passDataOut_3;
  output [7:0] io_passDataOut_2;
  output [7:0] io_passDataOut_1;
  output [7:0] io_passDataOut_0;
  input clk, reset;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T0;
  wire   [7:0] T2;
  wire   [7:0] T4;
  wire   [7:0] T6;
  wire   [7:0] FF_io_data_out;
  wire   [7:0] FF_1_io_data_out;

  FF_1_27 FF ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_io_data_out), .io_control_enable(io_writeSel[0]) );
  FF_1_26 FF_1 ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_1_io_data_out), .io_control_enable(io_writeSel[1]) );
  FF_1_25 FF_2 ( .clk(clk), .reset(reset), .io_data_in(T6), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_0), .io_control_enable(1'b1) );
  FF_1_24 FF_3 ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_1), .io_control_enable(1'b1) );
  FF_1_23 FF_4 ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_2), .io_control_enable(1'b1) );
  FF_1_22 FF_5 ( .clk(clk), .reset(reset), .io_data_in(T0), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_3), .io_control_enable(1'b1) );
  MuxN_3_7 readLocalAMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalASel), .io_out(io_readLocalA) );
  MuxN_3_6 readLocalBMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalBSel), .io_out(io_readLocalB) );
  MuxN_0_7 readRemoteAMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteASel), .io_out(io_readRemoteA)
         );
  MuxN_0_6 readRemoteBMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteBSel), .io_out(io_readRemoteB)
         );
  SELECT_OP C57 ( .DATA1(io_writeData), .DATA2(io_passData_3), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(io_writeSel[5]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C58 ( .DATA1(io_writeData), .DATA2(io_passData_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T2) );
  GTECH_BUF B_2 ( .A(io_writeSel[4]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C59 ( .DATA1(io_writeData), .DATA2(io_passData_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T4) );
  GTECH_BUF B_4 ( .A(io_writeSel[3]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C60 ( .DATA1(io_writeData), .DATA2(io_passData_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  GTECH_BUF B_6 ( .A(io_writeSel[2]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_writeSel[5]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_writeSel[4]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_writeSel[3]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_writeSel[2]), .Z(N11) );
endmodule


module MuxN_1_3 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T1;

  SELECT_OP C31 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C32 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module SRAM_2 ( clk, io_raddr, io_wen, io_waddr, io_wdata, io_rdata );
  input [3:0] io_raddr;
  input [3:0] io_waddr;
  input [7:0] io_wdata;
  output [7:0] io_rdata;
  input clk, io_wen;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, T3, N14,
         N15, N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28,
         N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42,
         N43, N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54;
  wire   [3:0] raddr_reg;
  wire   [127:0] mem;

  \**SEQGEN**  mem_reg_15__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[127]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[126]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[125]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[124]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[123]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[122]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[121]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[120]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_14__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[119]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[118]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[117]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[116]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[115]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[114]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[113]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[112]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_13__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[111]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[110]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[109]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[108]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[107]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[106]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[105]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[104]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_12__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[103]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[102]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[101]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[100]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[99]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[98]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[97]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[96]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_11__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[95]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[94]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[93]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[92]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[91]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[90]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[89]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[88]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_10__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[87]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[86]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[85]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[84]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[83]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[82]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[81]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[80]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_9__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[79]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[78]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[77]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[76]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[75]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[74]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[73]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[72]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_8__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[71]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[70]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[69]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[68]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[67]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[66]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[65]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[64]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_7__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[63]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[62]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[61]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[60]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[59]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[58]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[57]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[56]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_6__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[55]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[54]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[53]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[52]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[51]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[50]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[49]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[48]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_5__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[47]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[46]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[45]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[44]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[43]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[42]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[41]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[40]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_4__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[39]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[38]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[37]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[36]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[35]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[34]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[33]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[32]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_3__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[31]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[30]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[29]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[28]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[27]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[26]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[25]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[24]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_2__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[23]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[22]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[21]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[20]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[19]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[18]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[17]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[16]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_1__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[15]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[14]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[13]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[12]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[11]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[10]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[9]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[8]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_0__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[7]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[6]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[5]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[4]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  raddr_reg_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  GTECH_AND2 C619 ( .A(io_waddr[2]), .B(io_waddr[3]), .Z(N31) );
  GTECH_AND2 C620 ( .A(N0), .B(io_waddr[3]), .Z(N32) );
  GTECH_NOT I_0 ( .A(io_waddr[2]), .Z(N0) );
  GTECH_AND2 C621 ( .A(io_waddr[2]), .B(N1), .Z(N33) );
  GTECH_NOT I_1 ( .A(io_waddr[3]), .Z(N1) );
  GTECH_AND2 C622 ( .A(N2), .B(N3), .Z(N34) );
  GTECH_NOT I_2 ( .A(io_waddr[2]), .Z(N2) );
  GTECH_NOT I_3 ( .A(io_waddr[3]), .Z(N3) );
  GTECH_AND2 C623 ( .A(io_waddr[0]), .B(io_waddr[1]), .Z(N35) );
  GTECH_AND2 C624 ( .A(N4), .B(io_waddr[1]), .Z(N36) );
  GTECH_NOT I_4 ( .A(io_waddr[0]), .Z(N4) );
  GTECH_AND2 C625 ( .A(io_waddr[0]), .B(N5), .Z(N37) );
  GTECH_NOT I_5 ( .A(io_waddr[1]), .Z(N5) );
  GTECH_AND2 C626 ( .A(N6), .B(N7), .Z(N38) );
  GTECH_NOT I_6 ( .A(io_waddr[0]), .Z(N6) );
  GTECH_NOT I_7 ( .A(io_waddr[1]), .Z(N7) );
  GTECH_AND2 C627 ( .A(N31), .B(N35), .Z(N39) );
  GTECH_AND2 C628 ( .A(N31), .B(N36), .Z(N40) );
  GTECH_AND2 C629 ( .A(N31), .B(N37), .Z(N41) );
  GTECH_AND2 C630 ( .A(N31), .B(N38), .Z(N42) );
  GTECH_AND2 C631 ( .A(N32), .B(N35), .Z(N43) );
  GTECH_AND2 C632 ( .A(N32), .B(N36), .Z(N44) );
  GTECH_AND2 C633 ( .A(N32), .B(N37), .Z(N45) );
  GTECH_AND2 C634 ( .A(N32), .B(N38), .Z(N46) );
  GTECH_AND2 C635 ( .A(N33), .B(N35), .Z(N47) );
  GTECH_AND2 C636 ( .A(N33), .B(N36), .Z(N48) );
  GTECH_AND2 C637 ( .A(N33), .B(N37), .Z(N49) );
  GTECH_AND2 C638 ( .A(N33), .B(N38), .Z(N50) );
  GTECH_AND2 C639 ( .A(N34), .B(N35), .Z(N51) );
  GTECH_AND2 C640 ( .A(N34), .B(N36), .Z(N52) );
  GTECH_AND2 C641 ( .A(N34), .B(N37), .Z(N53) );
  GTECH_AND2 C642 ( .A(N34), .B(N38), .Z(N54) );
  SELECT_OP C643 ( .DATA1({N39, N40, N41, N42, N43, N44, N45, N46, N47, N48, 
        N49, N50, N51, N52, N53, N54}), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .CONTROL1(N8), .CONTROL2(N9), .Z({N30, N29, N28, N27, N26, N25, N24, 
        N23, N22, N21, N20, N19, N18, N17, N16, N15}) );
  GTECH_BUF B_0 ( .A(io_wen), .Z(N8) );
  GTECH_BUF B_1 ( .A(N14), .Z(N9) );
  MUX_OP C644 ( .D0({mem[0], mem[1], mem[2], mem[3], mem[4], mem[5], mem[6], 
        mem[7]}), .D1({mem[8], mem[9], mem[10], mem[11], mem[12], mem[13], 
        mem[14], mem[15]}), .D2({mem[16], mem[17], mem[18], mem[19], mem[20], 
        mem[21], mem[22], mem[23]}), .D3({mem[24], mem[25], mem[26], mem[27], 
        mem[28], mem[29], mem[30], mem[31]}), .D4({mem[32], mem[33], mem[34], 
        mem[35], mem[36], mem[37], mem[38], mem[39]}), .D5({mem[40], mem[41], 
        mem[42], mem[43], mem[44], mem[45], mem[46], mem[47]}), .D6({mem[48], 
        mem[49], mem[50], mem[51], mem[52], mem[53], mem[54], mem[55]}), .D7({
        mem[56], mem[57], mem[58], mem[59], mem[60], mem[61], mem[62], mem[63]}), .D8({mem[64], mem[65], mem[66], mem[67], mem[68], mem[69], mem[70], mem[71]}), .D9({mem[72], mem[73], mem[74], mem[75], mem[76], mem[77], mem[78], mem[79]}), .D10({mem[80], mem[81], mem[82], mem[83], mem[84], mem[85], mem[86], 
        mem[87]}), .D11({mem[88], mem[89], mem[90], mem[91], mem[92], mem[93], 
        mem[94], mem[95]}), .D12({mem[96], mem[97], mem[98], mem[99], mem[100], 
        mem[101], mem[102], mem[103]}), .D13({mem[104], mem[105], mem[106], 
        mem[107], mem[108], mem[109], mem[110], mem[111]}), .D14({mem[112], 
        mem[113], mem[114], mem[115], mem[116], mem[117], mem[118], mem[119]}), 
        .D15({mem[120], mem[121], mem[122], mem[123], mem[124], mem[125], 
        mem[126], mem[127]}), .S0(N10), .S1(N11), .S2(N12), .S3(N13), .Z({
        io_rdata[0], io_rdata[1], io_rdata[2], io_rdata[3], io_rdata[4], 
        io_rdata[5], io_rdata[6], io_rdata[7]}) );
  GTECH_BUF B_2 ( .A(raddr_reg[0]), .Z(N10) );
  GTECH_BUF B_3 ( .A(raddr_reg[1]), .Z(N11) );
  GTECH_BUF B_4 ( .A(raddr_reg[2]), .Z(N12) );
  GTECH_BUF B_5 ( .A(raddr_reg[3]), .Z(N13) );
  GTECH_NOT I_8 ( .A(io_wen), .Z(T3) );
  GTECH_NOT I_9 ( .A(io_wen), .Z(N14) );
endmodule


module MuxVec_0_2 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_1_0;
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C14 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_1_2 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [7:0] io_ins_1_0;
  input [7:0] io_ins_0_0;
  output [7:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C18 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_2_2 ( io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;

  assign io_out_0[3] = io_ins_0_0[3];
  assign io_out_0[2] = io_ins_0_0[2];
  assign io_out_0[1] = io_ins_0_0[1];
  assign io_out_0[0] = io_ins_0_0[0];

endmodule


module MuxN_0_12 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_13 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_14 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_2_2 ( io_ins_8, io_ins_7, io_ins_6, io_ins_5, io_ins_4, io_ins_3, 
        io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_8;
  input [7:0] io_ins_7;
  input [7:0] io_ins_6;
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [3:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T1;
  wire   [7:0] T9;
  wire   [7:0] T2;
  wire   [7:0] T6;
  wire   [7:0] T3;
  wire   [7:0] T12;
  wire   [7:0] T10;

  SELECT_OP C109 ( .DATA1(io_ins_8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[3]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C110 ( .DATA1(T9), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[2]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C111 ( .DATA1(T6), .DATA2(T3), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[1]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C112 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T3) );
  GTECH_BUF B_6 ( .A(io_sel[0]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C113 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  SELECT_OP C114 ( .DATA1(T12), .DATA2(T10), .CONTROL1(N4), .CONTROL2(N5), .Z(
        T9) );
  SELECT_OP C115 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T10) );
  SELECT_OP C116 ( .DATA1(io_ins_7), .DATA2(io_ins_6), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T12) );
  GTECH_NOT I_0 ( .A(io_sel[3]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_sel[2]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_sel[1]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_sel[0]), .Z(N11) );
endmodule


module IntFU_2 ( io_a, io_b, io_opcode, io_out );
  input [7:0] io_a;
  input [7:0] io_b;
  input [3:0] io_opcode;
  output [7:0] io_out;
  wire   net2333, net2334, net2335, net2336, net2337, net2338, net2339,
         net2340;
  wire   [7:0] T0;
  wire   [7:0] T1;
  wire   [7:0] T7;
  wire   [7:0] T3;
  wire   [7:0] T4;
  wire   [7:0] T5;
  wire   [0:0] T8;

  ADD_UNS_OP add_1177 ( .A(io_a), .B(io_b), .Z(T0) );
  SUB_UNS_OP sub_1178 ( .A(io_a), .B(io_b), .Z(T1) );
  MULT_UNS_OP mult_1180 ( .A(io_a), .B(io_b), .Z({net2333, net2334, net2335, 
        net2336, net2337, net2338, net2339, net2340, T7}) );
  DIV_UNS_OP div_1181 ( .A(io_a), .B(io_b), .QUOTIENT(T3) );
  EQ_UNS_OP eq_1185 ( .A(io_a), .B(io_b), .Z(T8[0]) );
  MuxN_2_2 m ( .io_ins_8(io_b), .io_ins_7(io_a), .io_ins_6({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, T8[0]}), .io_ins_5(T5), .io_ins_4(T4), 
        .io_ins_3(T3), .io_ins_2(T7), .io_ins_1(T1), .io_ins_0(T0), .io_sel(
        io_opcode), .io_out(io_out) );
  GTECH_AND2 C23 ( .A(io_a[7]), .B(io_b[7]), .Z(T4[7]) );
  GTECH_AND2 C24 ( .A(io_a[6]), .B(io_b[6]), .Z(T4[6]) );
  GTECH_AND2 C25 ( .A(io_a[5]), .B(io_b[5]), .Z(T4[5]) );
  GTECH_AND2 C26 ( .A(io_a[4]), .B(io_b[4]), .Z(T4[4]) );
  GTECH_AND2 C27 ( .A(io_a[3]), .B(io_b[3]), .Z(T4[3]) );
  GTECH_AND2 C28 ( .A(io_a[2]), .B(io_b[2]), .Z(T4[2]) );
  GTECH_AND2 C29 ( .A(io_a[1]), .B(io_b[1]), .Z(T4[1]) );
  GTECH_AND2 C30 ( .A(io_a[0]), .B(io_b[0]), .Z(T4[0]) );
  GTECH_OR2 C31 ( .A(io_a[7]), .B(io_b[7]), .Z(T5[7]) );
  GTECH_OR2 C32 ( .A(io_a[6]), .B(io_b[6]), .Z(T5[6]) );
  GTECH_OR2 C33 ( .A(io_a[5]), .B(io_b[5]), .Z(T5[5]) );
  GTECH_OR2 C34 ( .A(io_a[4]), .B(io_b[4]), .Z(T5[4]) );
  GTECH_OR2 C35 ( .A(io_a[3]), .B(io_b[3]), .Z(T5[3]) );
  GTECH_OR2 C36 ( .A(io_a[2]), .B(io_b[2]), .Z(T5[2]) );
  GTECH_OR2 C37 ( .A(io_a[1]), .B(io_b[1]), .Z(T5[1]) );
  GTECH_OR2 C38 ( .A(io_a[0]), .B(io_b[0]), .Z(T5[0]) );
endmodule


module MuxN_0_4 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_5 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module FF_1_16 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_17 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_18 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_19 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_20 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_21 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_3_4 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module MuxN_3_5 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module RegisterBlock_2 ( clk, reset, io_writeData, io_passData_3, 
        io_passData_2, io_passData_1, io_passData_0, io_writeSel, 
        io_readLocalASel, io_readLocalBSel, io_readRemoteASel, 
        io_readRemoteBSel, io_readLocalA, io_readLocalB, io_readRemoteA, 
        io_readRemoteB, io_passDataOut_3, io_passDataOut_2, io_passDataOut_1, 
        io_passDataOut_0 );
  input [7:0] io_writeData;
  input [7:0] io_passData_3;
  input [7:0] io_passData_2;
  input [7:0] io_passData_1;
  input [7:0] io_passData_0;
  input [5:0] io_writeSel;
  input [2:0] io_readLocalASel;
  input [2:0] io_readLocalBSel;
  input [1:0] io_readRemoteASel;
  input [1:0] io_readRemoteBSel;
  output [7:0] io_readLocalA;
  output [7:0] io_readLocalB;
  output [7:0] io_readRemoteA;
  output [7:0] io_readRemoteB;
  output [7:0] io_passDataOut_3;
  output [7:0] io_passDataOut_2;
  output [7:0] io_passDataOut_1;
  output [7:0] io_passDataOut_0;
  input clk, reset;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T0;
  wire   [7:0] T2;
  wire   [7:0] T4;
  wire   [7:0] T6;
  wire   [7:0] FF_io_data_out;
  wire   [7:0] FF_1_io_data_out;

  FF_1_21 FF ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_io_data_out), .io_control_enable(io_writeSel[0]) );
  FF_1_20 FF_1 ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_1_io_data_out), .io_control_enable(io_writeSel[1]) );
  FF_1_19 FF_2 ( .clk(clk), .reset(reset), .io_data_in(T6), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_0), .io_control_enable(1'b1) );
  FF_1_18 FF_3 ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_1), .io_control_enable(1'b1) );
  FF_1_17 FF_4 ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_2), .io_control_enable(1'b1) );
  FF_1_16 FF_5 ( .clk(clk), .reset(reset), .io_data_in(T0), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_3), .io_control_enable(1'b1) );
  MuxN_3_5 readLocalAMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalASel), .io_out(io_readLocalA) );
  MuxN_3_4 readLocalBMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalBSel), .io_out(io_readLocalB) );
  MuxN_0_5 readRemoteAMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteASel), .io_out(io_readRemoteA)
         );
  MuxN_0_4 readRemoteBMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteBSel), .io_out(io_readRemoteB)
         );
  SELECT_OP C57 ( .DATA1(io_writeData), .DATA2(io_passData_3), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(io_writeSel[5]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C58 ( .DATA1(io_writeData), .DATA2(io_passData_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T2) );
  GTECH_BUF B_2 ( .A(io_writeSel[4]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C59 ( .DATA1(io_writeData), .DATA2(io_passData_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T4) );
  GTECH_BUF B_4 ( .A(io_writeSel[3]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C60 ( .DATA1(io_writeData), .DATA2(io_passData_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  GTECH_BUF B_6 ( .A(io_writeSel[2]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_writeSel[5]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_writeSel[4]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_writeSel[3]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_writeSel[2]), .Z(N11) );
endmodule


module MuxN_1_2 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T1;

  SELECT_OP C31 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C32 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module ComputeUnit_1 ( clk, reset, io_config_enable, io_tokenIns_1, 
        io_tokenIns_0, io_tokenOuts_1, io_tokenOuts_0, io_scalarOut, 
        io_dataIn_0, io_dataOut_0 );
  output [7:0] io_scalarOut;
  input [7:0] io_dataIn_0;
  output [7:0] io_dataOut_0;
  input clk, reset, io_config_enable, io_tokenIns_1, io_tokenIns_0;
  output io_tokenOuts_1, io_tokenOuts_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, N14, N15,
         N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28, N29,
         N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42, N43,
         N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54, N55, N56, N57,
         N58, N59, N60, N61, N62, N63, N64, N65, N66, N67, N68, N69, N70, N71,
         N72, N73, N74, N75, N76, N77, N78, N79, N80, N81, N82, N83, N84, N85,
         N86, N87, N88, N89, N90, N91, N92, N93, N94, N95, N96, N97, N98, N99,
         N100, N101, N102, N103, N104, N105, N106, N107, counterEnables_0,
         counterEnables_1, configIn_mem1ra, N108, N109, config__mem1ra,
         configIn_mem1wd, N110, N111, config__mem1wd, configIn_mem1wa, N112,
         N113, config__mem1wa, configIn_mem0ra, N114, N115, config__mem0ra,
         configIn_mem0wd, N116, N117, config__mem0wd, configIn_mem0wa, N118,
         N119, config__mem0wa, counterChain_io_control_1_done,
         counterChain_io_control_0_done, N120, N121, N122, N123, N124, N125,
         N126, N127, N128, N129, N130, N131, N132, N133, N134, N135, N136,
         N137, N138, N139, N140, N141, N142, N143, N144, N145, N146, N147,
         N148, N149, N150, N151, N152, N153, N154, N155, N156, N157, N158,
         N159, N160, N161, N162, N163, N164, N165, N166, N167, N168, N169,
         N170, N171, N172, N173, N174, N175, N176, N177, N178, N179, N180,
         N181, N182, N183, N184, N185, N186, N187, N188, N189, N190, N191,
         N192, N193, N194, N195, N196, N197, N198, N199, N200, N201, N202,
         N203, N204, N205, N206, N207, N208, N209, net1490, net1491, net1492,
         net1493, net1494, SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2,
         SYNOPSYS_UNCONNECTED_3, SYNOPSYS_UNCONNECTED_4,
         SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6,
         SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8,
         SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10,
         SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12,
         SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14,
         SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16,
         SYNOPSYS_UNCONNECTED_17, SYNOPSYS_UNCONNECTED_18,
         SYNOPSYS_UNCONNECTED_19, SYNOPSYS_UNCONNECTED_20,
         SYNOPSYS_UNCONNECTED_21, SYNOPSYS_UNCONNECTED_22,
         SYNOPSYS_UNCONNECTED_23, SYNOPSYS_UNCONNECTED_24,
         SYNOPSYS_UNCONNECTED_25, SYNOPSYS_UNCONNECTED_26,
         SYNOPSYS_UNCONNECTED_27, SYNOPSYS_UNCONNECTED_28,
         SYNOPSYS_UNCONNECTED_29, SYNOPSYS_UNCONNECTED_30,
         SYNOPSYS_UNCONNECTED_31, SYNOPSYS_UNCONNECTED_32,
         SYNOPSYS_UNCONNECTED_33, SYNOPSYS_UNCONNECTED_34,
         SYNOPSYS_UNCONNECTED_35, SYNOPSYS_UNCONNECTED_36,
         SYNOPSYS_UNCONNECTED_37, SYNOPSYS_UNCONNECTED_38,
         SYNOPSYS_UNCONNECTED_39, SYNOPSYS_UNCONNECTED_40,
         SYNOPSYS_UNCONNECTED_41, SYNOPSYS_UNCONNECTED_42,
         SYNOPSYS_UNCONNECTED_43, SYNOPSYS_UNCONNECTED_44,
         SYNOPSYS_UNCONNECTED_45, SYNOPSYS_UNCONNECTED_46,
         SYNOPSYS_UNCONNECTED_47, SYNOPSYS_UNCONNECTED_48;
  wire   [1:0] configIn_pipeStage_1_opB_dataSrc;
  wire   [1:0] config__pipeStage_1_opB_dataSrc;
  wire   [2:0] T21;
  wire   [2:0] configIn_pipeStage_1_opB_value;
  wire   [1:0] configIn_pipeStage_1_opA_dataSrc;
  wire   [1:0] config__pipeStage_1_opA_dataSrc;
  wire   [2:0] T24;
  wire   [2:0] configIn_pipeStage_1_opA_value;
  wire   [5:0] configIn_pipeStage_1_result;
  wire   [5:0] config__pipeStage_1_result;
  wire   [3:0] configIn_pipeStage_1_opcode;
  wire   [3:0] config__pipeStage_1_opcode;
  wire   [1:0] configIn_pipeStage_0_opB_dataSrc;
  wire   [1:0] config__pipeStage_0_opB_dataSrc;
  wire   [2:0] T29;
  wire   [2:0] configIn_pipeStage_0_opB_value;
  wire   [1:0] configIn_pipeStage_0_opA_dataSrc;
  wire   [1:0] config__pipeStage_0_opA_dataSrc;
  wire   [2:0] T32;
  wire   [2:0] configIn_pipeStage_0_opA_value;
  wire   [5:0] configIn_pipeStage_0_result;
  wire   [5:0] config__pipeStage_0_result;
  wire   [3:0] configIn_pipeStage_0_opcode;
  wire   [3:0] config__pipeStage_0_opcode;
  wire   [1:0] configIn_remoteMux1;
  wire   [1:0] config__remoteMux1;
  wire   [1:0] configIn_remoteMux0;
  wire   [1:0] config__remoteMux0;
  wire   [3:0] T41;
  wire   [7:4] IntFU_io_out;
  wire   [3:0] mem0raMux_io_out_0;
  wire   [3:0] mem0waMux_io_out_0;
  wire   [7:0] mem0wdMux_io_out_0;
  wire   [7:0] mem0_io_rdata;
  wire   [3:0] mem1raMux_io_out_0;
  wire   [3:0] mem1waMux_io_out_0;
  wire   [7:0] mem1wdMux_io_out_0;
  wire   [7:0] mem1_io_rdata;
  wire   [7:0] counterChain_io_data_1_out;
  wire   [7:0] counterChain_io_data_0_out;
  wire   [7:0] remoteMux0_io_out;
  wire   [7:0] remoteMux1_io_out;
  wire   [7:0] MuxN_io_out;
  wire   [7:0] MuxN_1_io_out;
  wire   [7:0] RegisterBlock_io_readLocalA;
  wire   [7:0] RegisterBlock_io_readLocalB;
  wire   [7:0] RegisterBlock_io_readRemoteA;
  wire   [7:0] RegisterBlock_io_readRemoteB;
  wire   [7:0] RegisterBlock_io_passDataOut_3;
  wire   [7:0] RegisterBlock_io_passDataOut_2;
  wire   [7:0] RegisterBlock_io_passDataOut_1;
  wire   [7:0] RegisterBlock_io_passDataOut_0;
  wire   [7:0] MuxN_2_io_out;
  wire   [7:0] MuxN_3_io_out;
  wire   [7:0] RegisterBlock_1_io_readLocalA;
  wire   [7:0] RegisterBlock_1_io_readLocalB;
  assign io_scalarOut[7] = io_dataOut_0[7];
  assign io_scalarOut[6] = io_dataOut_0[6];
  assign io_scalarOut[5] = io_dataOut_0[5];
  assign io_scalarOut[4] = io_dataOut_0[4];
  assign io_scalarOut[3] = io_dataOut_0[3];
  assign io_scalarOut[2] = io_dataOut_0[2];
  assign io_scalarOut[1] = io_dataOut_0[1];
  assign io_scalarOut[0] = io_dataOut_0[0];

  SRAM_3 mem0 ( .clk(clk), .io_raddr(mem0raMux_io_out_0), .io_wen(1'b1), 
        .io_waddr(mem0waMux_io_out_0), .io_wdata(mem0wdMux_io_out_0), 
        .io_rdata(mem0_io_rdata) );
  MuxVec_0_3 mem0waMux ( .io_ins_1_0(io_dataOut_0[3:0]), .io_ins_0_0(T41), 
        .io_sel(config__mem0wa), .io_out_0(mem0waMux_io_out_0) );
  MuxVec_1_3 mem0wdMux ( .io_ins_1_0(io_dataIn_0), .io_ins_0_0(io_dataOut_0), 
        .io_sel(config__mem0wd), .io_out_0(mem0wdMux_io_out_0) );
  MuxVec_2_3 mem0raMux ( .io_ins_0_0(T41), .io_sel(config__mem0ra), .io_out_0(
        mem0raMux_io_out_0) );
  SRAM_2 mem1 ( .clk(clk), .io_raddr(mem1raMux_io_out_0), .io_wen(1'b1), 
        .io_waddr(mem1waMux_io_out_0), .io_wdata(mem1wdMux_io_out_0), 
        .io_rdata(mem1_io_rdata) );
  MuxVec_0_2 mem1waMux ( .io_ins_1_0(io_dataOut_0[3:0]), .io_ins_0_0(T41), 
        .io_sel(config__mem1wa), .io_out_0(mem1waMux_io_out_0) );
  MuxVec_1_2 mem1wdMux ( .io_ins_1_0(io_dataIn_0), .io_ins_0_0(io_dataOut_0), 
        .io_sel(config__mem1wd), .io_out_0(mem1wdMux_io_out_0) );
  MuxVec_2_2 mem1raMux ( .io_ins_0_0(T41), .io_sel(config__mem1ra), .io_out_0(
        mem1raMux_io_out_0) );
  CounterChain_1 counterChain ( .clk(clk), .reset(reset), .io_config_enable(
        net1494), .io_data_1_max({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0}), .io_data_1_stride({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0}), .io_data_1_out(counterChain_io_data_1_out), .io_data_0_max({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_0_stride({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_0_out(
        counterChain_io_data_0_out), .io_control_1_enable(counterEnables_1), 
        .io_control_1_done(counterChain_io_control_1_done), 
        .io_control_0_enable(counterEnables_0), .io_control_0_done(
        counterChain_io_control_0_done) );
  CUControlBox_1 controlBlock ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_tokenIns_1(io_tokenIns_1), .io_tokenIns_0(
        io_tokenIns_0), .io_done_1(counterChain_io_control_1_done), 
        .io_done_0(counterChain_io_control_0_done), .io_tokenOuts_1(
        io_tokenOuts_1), .io_tokenOuts_0(io_tokenOuts_0), .io_enable_1(
        counterEnables_1), .io_enable_0(counterEnables_0) );
  MuxN_0_15 remoteMux0 ( .io_ins_3(mem1_io_rdata), .io_ins_2(mem0_io_rdata), 
        .io_ins_1(counterChain_io_data_1_out), .io_ins_0(
        counterChain_io_data_0_out), .io_sel(config__remoteMux0), .io_out(
        remoteMux0_io_out) );
  MuxN_0_14 remoteMux1 ( .io_ins_3(mem1_io_rdata), .io_ins_2(mem0_io_rdata), 
        .io_ins_1(counterChain_io_data_1_out), .io_ins_0(
        counterChain_io_data_0_out), .io_sel(config__remoteMux1), .io_out(
        remoteMux1_io_out) );
  IntFU_3 IntFU ( .io_a(MuxN_io_out), .io_b(MuxN_1_io_out), .io_opcode(
        config__pipeStage_0_opcode), .io_out({IntFU_io_out, T41}) );
  RegisterBlock_3 RegisterBlock ( .clk(clk), .reset(reset), .io_writeData({
        IntFU_io_out, T41}), .io_passData_3(mem1_io_rdata), .io_passData_2(
        mem0_io_rdata), .io_passData_1(counterChain_io_data_1_out), 
        .io_passData_0(counterChain_io_data_0_out), .io_writeSel(
        config__pipeStage_0_result), .io_readLocalASel(T32), 
        .io_readLocalBSel(T29), .io_readRemoteASel(T24[1:0]), 
        .io_readRemoteBSel(T21[1:0]), .io_readLocalA(
        RegisterBlock_io_readLocalA), .io_readLocalB(
        RegisterBlock_io_readLocalB), .io_readRemoteA(
        RegisterBlock_io_readRemoteA), .io_readRemoteB(
        RegisterBlock_io_readRemoteB), .io_passDataOut_3(
        RegisterBlock_io_passDataOut_3), .io_passDataOut_2(
        RegisterBlock_io_passDataOut_2), .io_passDataOut_1(
        RegisterBlock_io_passDataOut_1), .io_passDataOut_0(
        RegisterBlock_io_passDataOut_0) );
  MuxN_0_13 MuxN ( .io_ins_3(mem0_io_rdata), .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, T32}), .io_ins_1(remoteMux0_io_out), .io_ins_0(
        RegisterBlock_io_readLocalA), .io_sel(config__pipeStage_0_opA_dataSrc), 
        .io_out(MuxN_io_out) );
  MuxN_0_12 MuxN_1 ( .io_ins_3(mem1_io_rdata), .io_ins_2({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, T29}), .io_ins_1(remoteMux1_io_out), .io_ins_0(
        RegisterBlock_io_readLocalB), .io_sel(config__pipeStage_0_opB_dataSrc), 
        .io_out(MuxN_1_io_out) );
  IntFU_2 IntFU_1 ( .io_a(MuxN_2_io_out), .io_b(MuxN_3_io_out), .io_opcode(
        config__pipeStage_1_opcode), .io_out(io_dataOut_0) );
  RegisterBlock_2 RegisterBlock_1 ( .clk(clk), .reset(reset), .io_writeData(
        io_dataOut_0), .io_passData_3(RegisterBlock_io_passDataOut_3), 
        .io_passData_2(RegisterBlock_io_passDataOut_2), .io_passData_1(
        RegisterBlock_io_passDataOut_1), .io_passData_0(
        RegisterBlock_io_passDataOut_0), .io_writeSel(
        config__pipeStage_1_result), .io_readLocalASel(T24), 
        .io_readLocalBSel(T21), .io_readRemoteASel({net1490, net1491}), 
        .io_readRemoteBSel({net1492, net1493}), .io_readLocalA(
        RegisterBlock_1_io_readLocalA), .io_readLocalB(
        RegisterBlock_1_io_readLocalB), .io_readRemoteA({
        SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2, SYNOPSYS_UNCONNECTED_3, 
        SYNOPSYS_UNCONNECTED_4, SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6, 
        SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8}), .io_readRemoteB({
        SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10, 
        SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12, 
        SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14, 
        SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16}), .io_passDataOut_3(
        {SYNOPSYS_UNCONNECTED_17, SYNOPSYS_UNCONNECTED_18, 
        SYNOPSYS_UNCONNECTED_19, SYNOPSYS_UNCONNECTED_20, 
        SYNOPSYS_UNCONNECTED_21, SYNOPSYS_UNCONNECTED_22, 
        SYNOPSYS_UNCONNECTED_23, SYNOPSYS_UNCONNECTED_24}), .io_passDataOut_2(
        {SYNOPSYS_UNCONNECTED_25, SYNOPSYS_UNCONNECTED_26, 
        SYNOPSYS_UNCONNECTED_27, SYNOPSYS_UNCONNECTED_28, 
        SYNOPSYS_UNCONNECTED_29, SYNOPSYS_UNCONNECTED_30, 
        SYNOPSYS_UNCONNECTED_31, SYNOPSYS_UNCONNECTED_32}), .io_passDataOut_1(
        {SYNOPSYS_UNCONNECTED_33, SYNOPSYS_UNCONNECTED_34, 
        SYNOPSYS_UNCONNECTED_35, SYNOPSYS_UNCONNECTED_36, 
        SYNOPSYS_UNCONNECTED_37, SYNOPSYS_UNCONNECTED_38, 
        SYNOPSYS_UNCONNECTED_39, SYNOPSYS_UNCONNECTED_40}), .io_passDataOut_0(
        {SYNOPSYS_UNCONNECTED_41, SYNOPSYS_UNCONNECTED_42, 
        SYNOPSYS_UNCONNECTED_43, SYNOPSYS_UNCONNECTED_44, 
        SYNOPSYS_UNCONNECTED_45, SYNOPSYS_UNCONNECTED_46, 
        SYNOPSYS_UNCONNECTED_47, SYNOPSYS_UNCONNECTED_48}) );
  MuxN_1_3 MuxN_2 ( .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, T24}), .io_ins_1(
        RegisterBlock_io_readRemoteA), .io_ins_0(RegisterBlock_1_io_readLocalA), .io_sel(config__pipeStage_1_opA_dataSrc), .io_out(MuxN_2_io_out) );
  MuxN_1_2 MuxN_3 ( .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, T21}), .io_ins_1(
        RegisterBlock_io_readRemoteB), .io_ins_0(RegisterBlock_1_io_readLocalB), .io_sel(config__pipeStage_1_opB_dataSrc), .io_out(MuxN_3_io_out) );
  \**SEQGEN**  config__pipeStage_1_opB_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N123), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opB_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N122), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opB_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N128), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N127), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N126), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N132), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opA_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N131), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opA_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N137), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N136), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N135), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N145), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[5]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N144), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[4]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N143), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N142), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N141), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N140), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N151), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N150), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N149), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N148), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N155), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opB_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N154), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opB_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N160), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N159), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N158), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N164), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opA_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N163), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opA_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N169), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N168), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N167), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N177), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[5]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N176), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[4]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N175), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N174), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N173), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N172), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N183), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N182), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N181), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N180), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N187), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N186), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N191), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N190), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1ra_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N194), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1ra), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1wd_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N197), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1wd), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1wa_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N200), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1wa), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0ra_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N203), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0ra), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0wd_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N206), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0wd), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0wa_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N209), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0wa), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C455 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opB_dataSrc), .CONTROL1(N0), .CONTROL2(N1), .Z(
        configIn_pipeStage_1_opB_dataSrc) );
  GTECH_BUF B_0 ( .A(N81), .Z(N0) );
  GTECH_BUF B_1 ( .A(N80), .Z(N1) );
  SELECT_OP C456 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T21), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_pipeStage_1_opB_value) );
  GTECH_BUF B_2 ( .A(N83), .Z(N2) );
  GTECH_BUF B_3 ( .A(N82), .Z(N3) );
  SELECT_OP C457 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opA_dataSrc), .CONTROL1(N4), .CONTROL2(N5), .Z(
        configIn_pipeStage_1_opA_dataSrc) );
  GTECH_BUF B_4 ( .A(N85), .Z(N4) );
  GTECH_BUF B_5 ( .A(N84), .Z(N5) );
  SELECT_OP C458 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T24), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(configIn_pipeStage_1_opA_value) );
  GTECH_BUF B_6 ( .A(N87), .Z(N6) );
  GTECH_BUF B_7 ( .A(N86), .Z(N7) );
  SELECT_OP C459 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_result), .CONTROL1(N8), .CONTROL2(N9), .Z(
        configIn_pipeStage_1_result) );
  GTECH_BUF B_8 ( .A(N89), .Z(N8) );
  GTECH_BUF B_9 ( .A(N88), .Z(N9) );
  SELECT_OP C460 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opcode), .CONTROL1(N10), .CONTROL2(N11), .Z(
        configIn_pipeStage_1_opcode) );
  GTECH_BUF B_10 ( .A(N91), .Z(N10) );
  GTECH_BUF B_11 ( .A(N90), .Z(N11) );
  SELECT_OP C461 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opB_dataSrc), .CONTROL1(N12), .CONTROL2(N13), .Z(
        configIn_pipeStage_0_opB_dataSrc) );
  GTECH_BUF B_12 ( .A(N93), .Z(N12) );
  GTECH_BUF B_13 ( .A(N92), .Z(N13) );
  SELECT_OP C462 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T29), .CONTROL1(N14), 
        .CONTROL2(N15), .Z(configIn_pipeStage_0_opB_value) );
  GTECH_BUF B_14 ( .A(N95), .Z(N14) );
  GTECH_BUF B_15 ( .A(N94), .Z(N15) );
  SELECT_OP C463 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opA_dataSrc), .CONTROL1(N16), .CONTROL2(N17), .Z(
        configIn_pipeStage_0_opA_dataSrc) );
  GTECH_BUF B_16 ( .A(N97), .Z(N16) );
  GTECH_BUF B_17 ( .A(N96), .Z(N17) );
  SELECT_OP C464 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T32), .CONTROL1(N18), 
        .CONTROL2(N19), .Z(configIn_pipeStage_0_opA_value) );
  GTECH_BUF B_18 ( .A(N99), .Z(N18) );
  GTECH_BUF B_19 ( .A(N98), .Z(N19) );
  SELECT_OP C465 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_result), .CONTROL1(N20), .CONTROL2(N21), .Z(
        configIn_pipeStage_0_result) );
  GTECH_BUF B_20 ( .A(N101), .Z(N20) );
  GTECH_BUF B_21 ( .A(N100), .Z(N21) );
  SELECT_OP C466 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opcode), .CONTROL1(N22), .CONTROL2(N23), .Z(
        configIn_pipeStage_0_opcode) );
  GTECH_BUF B_22 ( .A(N103), .Z(N22) );
  GTECH_BUF B_23 ( .A(N102), .Z(N23) );
  SELECT_OP C467 ( .DATA1({1'b0, 1'b0}), .DATA2(config__remoteMux1), 
        .CONTROL1(N24), .CONTROL2(N25), .Z(configIn_remoteMux1) );
  GTECH_BUF B_24 ( .A(N105), .Z(N24) );
  GTECH_BUF B_25 ( .A(N104), .Z(N25) );
  SELECT_OP C468 ( .DATA1({1'b0, 1'b0}), .DATA2(config__remoteMux0), 
        .CONTROL1(N26), .CONTROL2(N27), .Z(configIn_remoteMux0) );
  GTECH_BUF B_26 ( .A(N107), .Z(N26) );
  GTECH_BUF B_27 ( .A(N106), .Z(N27) );
  SELECT_OP C469 ( .DATA1(1'b0), .DATA2(config__mem1ra), .CONTROL1(N28), 
        .CONTROL2(N29), .Z(configIn_mem1ra) );
  GTECH_BUF B_28 ( .A(N109), .Z(N28) );
  GTECH_BUF B_29 ( .A(N108), .Z(N29) );
  SELECT_OP C470 ( .DATA1(1'b0), .DATA2(config__mem1wd), .CONTROL1(N30), 
        .CONTROL2(N31), .Z(configIn_mem1wd) );
  GTECH_BUF B_30 ( .A(N111), .Z(N30) );
  GTECH_BUF B_31 ( .A(N110), .Z(N31) );
  SELECT_OP C471 ( .DATA1(1'b0), .DATA2(config__mem1wa), .CONTROL1(N32), 
        .CONTROL2(N33), .Z(configIn_mem1wa) );
  GTECH_BUF B_32 ( .A(N113), .Z(N32) );
  GTECH_BUF B_33 ( .A(N112), .Z(N33) );
  SELECT_OP C472 ( .DATA1(1'b0), .DATA2(config__mem0ra), .CONTROL1(N34), 
        .CONTROL2(N35), .Z(configIn_mem0ra) );
  GTECH_BUF B_34 ( .A(N115), .Z(N34) );
  GTECH_BUF B_35 ( .A(N114), .Z(N35) );
  SELECT_OP C473 ( .DATA1(1'b0), .DATA2(config__mem0wd), .CONTROL1(N36), 
        .CONTROL2(N37), .Z(configIn_mem0wd) );
  GTECH_BUF B_36 ( .A(N117), .Z(N36) );
  GTECH_BUF B_37 ( .A(N116), .Z(N37) );
  SELECT_OP C474 ( .DATA1(1'b0), .DATA2(config__mem0wa), .CONTROL1(N38), 
        .CONTROL2(N39), .Z(configIn_mem0wa) );
  GTECH_BUF B_38 ( .A(N119), .Z(N38) );
  GTECH_BUF B_39 ( .A(N118), .Z(N39) );
  SELECT_OP C475 ( .DATA1({1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_1_opB_dataSrc), .CONTROL1(N40), .CONTROL2(N41), .Z(
        {N123, N122}) );
  GTECH_BUF B_40 ( .A(N121), .Z(N40) );
  GTECH_BUF B_41 ( .A(N120), .Z(N41) );
  SELECT_OP C476 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opB_value), .CONTROL1(N42), .CONTROL2(N43), .Z({
        N128, N127, N126}) );
  GTECH_BUF B_42 ( .A(N125), .Z(N42) );
  GTECH_BUF B_43 ( .A(N124), .Z(N43) );
  SELECT_OP C477 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opA_dataSrc), .CONTROL1(N44), .CONTROL2(N45), .Z(
        {N132, N131}) );
  GTECH_BUF B_44 ( .A(N130), .Z(N44) );
  GTECH_BUF B_45 ( .A(N129), .Z(N45) );
  SELECT_OP C478 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opA_value), .CONTROL1(N46), .CONTROL2(N47), .Z({
        N137, N136, N135}) );
  GTECH_BUF B_46 ( .A(N134), .Z(N46) );
  GTECH_BUF B_47 ( .A(N133), .Z(N47) );
  SELECT_OP C479 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0}), .DATA2(
        configIn_pipeStage_1_result), .CONTROL1(N48), .CONTROL2(N49), .Z({N145, 
        N144, N143, N142, N141, N140}) );
  GTECH_BUF B_48 ( .A(N139), .Z(N48) );
  GTECH_BUF B_49 ( .A(N138), .Z(N49) );
  SELECT_OP C480 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_1_opcode), .CONTROL1(N50), .CONTROL2(N51), .Z({N151, 
        N150, N149, N148}) );
  GTECH_BUF B_50 ( .A(N147), .Z(N50) );
  GTECH_BUF B_51 ( .A(N146), .Z(N51) );
  SELECT_OP C481 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opB_dataSrc), .CONTROL1(N52), .CONTROL2(N53), .Z(
        {N155, N154}) );
  GTECH_BUF B_52 ( .A(N153), .Z(N52) );
  GTECH_BUF B_53 ( .A(N152), .Z(N53) );
  SELECT_OP C482 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opB_value), .CONTROL1(N54), .CONTROL2(N55), .Z({
        N160, N159, N158}) );
  GTECH_BUF B_54 ( .A(N157), .Z(N54) );
  GTECH_BUF B_55 ( .A(N156), .Z(N55) );
  SELECT_OP C483 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opA_dataSrc), .CONTROL1(N56), .CONTROL2(N57), .Z(
        {N164, N163}) );
  GTECH_BUF B_56 ( .A(N162), .Z(N56) );
  GTECH_BUF B_57 ( .A(N161), .Z(N57) );
  SELECT_OP C484 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_0_opA_value), .CONTROL1(N58), .CONTROL2(N59), .Z({
        N169, N168, N167}) );
  GTECH_BUF B_58 ( .A(N166), .Z(N58) );
  GTECH_BUF B_59 ( .A(N165), .Z(N59) );
  SELECT_OP C485 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_0_result), .CONTROL1(N60), .CONTROL2(N61), .Z({N177, 
        N176, N175, N174, N173, N172}) );
  GTECH_BUF B_60 ( .A(N171), .Z(N60) );
  GTECH_BUF B_61 ( .A(N170), .Z(N61) );
  SELECT_OP C486 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b0}), .DATA2(
        configIn_pipeStage_0_opcode), .CONTROL1(N62), .CONTROL2(N63), .Z({N183, 
        N182, N181, N180}) );
  GTECH_BUF B_62 ( .A(N179), .Z(N62) );
  GTECH_BUF B_63 ( .A(N178), .Z(N63) );
  SELECT_OP C487 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_remoteMux1), 
        .CONTROL1(N64), .CONTROL2(N65), .Z({N187, N186}) );
  GTECH_BUF B_64 ( .A(N185), .Z(N64) );
  GTECH_BUF B_65 ( .A(N184), .Z(N65) );
  SELECT_OP C488 ( .DATA1({1'b0, 1'b0}), .DATA2(configIn_remoteMux0), 
        .CONTROL1(N66), .CONTROL2(N67), .Z({N191, N190}) );
  GTECH_BUF B_66 ( .A(N189), .Z(N66) );
  GTECH_BUF B_67 ( .A(N188), .Z(N67) );
  SELECT_OP C489 ( .DATA1(1'b0), .DATA2(configIn_mem1ra), .CONTROL1(N68), 
        .CONTROL2(N69), .Z(N194) );
  GTECH_BUF B_68 ( .A(N193), .Z(N68) );
  GTECH_BUF B_69 ( .A(N192), .Z(N69) );
  SELECT_OP C490 ( .DATA1(1'b0), .DATA2(configIn_mem1wd), .CONTROL1(N70), 
        .CONTROL2(N71), .Z(N197) );
  GTECH_BUF B_70 ( .A(N196), .Z(N70) );
  GTECH_BUF B_71 ( .A(N195), .Z(N71) );
  SELECT_OP C491 ( .DATA1(1'b0), .DATA2(configIn_mem1wa), .CONTROL1(N72), 
        .CONTROL2(N73), .Z(N200) );
  GTECH_BUF B_72 ( .A(N199), .Z(N72) );
  GTECH_BUF B_73 ( .A(N198), .Z(N73) );
  SELECT_OP C492 ( .DATA1(1'b0), .DATA2(configIn_mem0ra), .CONTROL1(N74), 
        .CONTROL2(N75), .Z(N203) );
  GTECH_BUF B_74 ( .A(N202), .Z(N74) );
  GTECH_BUF B_75 ( .A(N201), .Z(N75) );
  SELECT_OP C493 ( .DATA1(1'b0), .DATA2(configIn_mem0wd), .CONTROL1(N76), 
        .CONTROL2(N77), .Z(N206) );
  GTECH_BUF B_76 ( .A(N205), .Z(N76) );
  GTECH_BUF B_77 ( .A(N204), .Z(N77) );
  SELECT_OP C494 ( .DATA1(1'b0), .DATA2(configIn_mem0wa), .CONTROL1(N78), 
        .CONTROL2(N79), .Z(N209) );
  GTECH_BUF B_78 ( .A(N208), .Z(N78) );
  GTECH_BUF B_79 ( .A(N207), .Z(N79) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N80) );
  GTECH_BUF B_80 ( .A(io_config_enable), .Z(N81) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N82) );
  GTECH_BUF B_81 ( .A(io_config_enable), .Z(N83) );
  GTECH_NOT I_2 ( .A(io_config_enable), .Z(N84) );
  GTECH_BUF B_82 ( .A(io_config_enable), .Z(N85) );
  GTECH_NOT I_3 ( .A(io_config_enable), .Z(N86) );
  GTECH_BUF B_83 ( .A(io_config_enable), .Z(N87) );
  GTECH_NOT I_4 ( .A(io_config_enable), .Z(N88) );
  GTECH_BUF B_84 ( .A(io_config_enable), .Z(N89) );
  GTECH_NOT I_5 ( .A(io_config_enable), .Z(N90) );
  GTECH_BUF B_85 ( .A(io_config_enable), .Z(N91) );
  GTECH_NOT I_6 ( .A(io_config_enable), .Z(N92) );
  GTECH_BUF B_86 ( .A(io_config_enable), .Z(N93) );
  GTECH_NOT I_7 ( .A(io_config_enable), .Z(N94) );
  GTECH_BUF B_87 ( .A(io_config_enable), .Z(N95) );
  GTECH_NOT I_8 ( .A(io_config_enable), .Z(N96) );
  GTECH_BUF B_88 ( .A(io_config_enable), .Z(N97) );
  GTECH_NOT I_9 ( .A(io_config_enable), .Z(N98) );
  GTECH_BUF B_89 ( .A(io_config_enable), .Z(N99) );
  GTECH_NOT I_10 ( .A(io_config_enable), .Z(N100) );
  GTECH_BUF B_90 ( .A(io_config_enable), .Z(N101) );
  GTECH_NOT I_11 ( .A(io_config_enable), .Z(N102) );
  GTECH_BUF B_91 ( .A(io_config_enable), .Z(N103) );
  GTECH_NOT I_12 ( .A(io_config_enable), .Z(N104) );
  GTECH_BUF B_92 ( .A(io_config_enable), .Z(N105) );
  GTECH_NOT I_13 ( .A(io_config_enable), .Z(N106) );
  GTECH_BUF B_93 ( .A(io_config_enable), .Z(N107) );
  GTECH_NOT I_14 ( .A(io_config_enable), .Z(N108) );
  GTECH_BUF B_94 ( .A(io_config_enable), .Z(N109) );
  GTECH_NOT I_15 ( .A(io_config_enable), .Z(N110) );
  GTECH_BUF B_95 ( .A(io_config_enable), .Z(N111) );
  GTECH_NOT I_16 ( .A(io_config_enable), .Z(N112) );
  GTECH_BUF B_96 ( .A(io_config_enable), .Z(N113) );
  GTECH_NOT I_17 ( .A(io_config_enable), .Z(N114) );
  GTECH_BUF B_97 ( .A(io_config_enable), .Z(N115) );
  GTECH_NOT I_18 ( .A(io_config_enable), .Z(N116) );
  GTECH_BUF B_98 ( .A(io_config_enable), .Z(N117) );
  GTECH_NOT I_19 ( .A(io_config_enable), .Z(N118) );
  GTECH_BUF B_99 ( .A(io_config_enable), .Z(N119) );
  GTECH_NOT I_20 ( .A(reset), .Z(N120) );
  GTECH_BUF B_100 ( .A(reset), .Z(N121) );
  GTECH_NOT I_21 ( .A(reset), .Z(N124) );
  GTECH_BUF B_101 ( .A(reset), .Z(N125) );
  GTECH_NOT I_22 ( .A(reset), .Z(N129) );
  GTECH_BUF B_102 ( .A(reset), .Z(N130) );
  GTECH_NOT I_23 ( .A(reset), .Z(N133) );
  GTECH_BUF B_103 ( .A(reset), .Z(N134) );
  GTECH_NOT I_24 ( .A(reset), .Z(N138) );
  GTECH_BUF B_104 ( .A(reset), .Z(N139) );
  GTECH_NOT I_25 ( .A(reset), .Z(N146) );
  GTECH_BUF B_105 ( .A(reset), .Z(N147) );
  GTECH_NOT I_26 ( .A(reset), .Z(N152) );
  GTECH_BUF B_106 ( .A(reset), .Z(N153) );
  GTECH_NOT I_27 ( .A(reset), .Z(N156) );
  GTECH_BUF B_107 ( .A(reset), .Z(N157) );
  GTECH_NOT I_28 ( .A(reset), .Z(N161) );
  GTECH_BUF B_108 ( .A(reset), .Z(N162) );
  GTECH_NOT I_29 ( .A(reset), .Z(N165) );
  GTECH_BUF B_109 ( .A(reset), .Z(N166) );
  GTECH_NOT I_30 ( .A(reset), .Z(N170) );
  GTECH_BUF B_110 ( .A(reset), .Z(N171) );
  GTECH_NOT I_31 ( .A(reset), .Z(N178) );
  GTECH_BUF B_111 ( .A(reset), .Z(N179) );
  GTECH_NOT I_32 ( .A(reset), .Z(N184) );
  GTECH_BUF B_112 ( .A(reset), .Z(N185) );
  GTECH_NOT I_33 ( .A(reset), .Z(N188) );
  GTECH_BUF B_113 ( .A(reset), .Z(N189) );
  GTECH_NOT I_34 ( .A(reset), .Z(N192) );
  GTECH_BUF B_114 ( .A(reset), .Z(N193) );
  GTECH_NOT I_35 ( .A(reset), .Z(N195) );
  GTECH_BUF B_115 ( .A(reset), .Z(N196) );
  GTECH_NOT I_36 ( .A(reset), .Z(N198) );
  GTECH_BUF B_116 ( .A(reset), .Z(N199) );
  GTECH_NOT I_37 ( .A(reset), .Z(N201) );
  GTECH_BUF B_117 ( .A(reset), .Z(N202) );
  GTECH_NOT I_38 ( .A(reset), .Z(N204) );
  GTECH_BUF B_118 ( .A(reset), .Z(N205) );
  GTECH_NOT I_39 ( .A(reset), .Z(N207) );
  GTECH_BUF B_119 ( .A(reset), .Z(N208) );
endmodule


module SRAM_0 ( clk, io_raddr, io_wen, io_waddr, io_wdata, io_rdata );
  input [3:0] io_raddr;
  input [3:0] io_waddr;
  input [7:0] io_wdata;
  output [7:0] io_rdata;
  input clk, io_wen;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, T3, N14,
         N15, N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28,
         N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42,
         N43, N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54;
  wire   [3:0] raddr_reg;
  wire   [127:0] mem;

  \**SEQGEN**  mem_reg_15__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[127]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[126]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[125]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[124]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[123]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[122]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[121]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[120]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_14__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[119]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[118]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[117]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[116]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[115]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[114]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[113]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[112]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_13__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[111]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[110]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[109]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[108]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[107]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[106]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[105]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[104]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_12__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[103]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[102]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[101]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[100]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[99]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[98]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[97]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[96]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_11__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[95]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[94]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[93]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[92]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[91]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[90]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[89]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[88]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_10__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[87]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[86]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[85]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[84]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[83]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[82]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[81]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[80]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_9__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[79]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[78]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[77]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[76]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[75]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[74]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[73]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[72]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_8__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[71]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[70]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[69]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[68]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[67]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[66]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[65]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[64]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_7__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[63]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[62]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[61]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[60]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[59]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[58]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[57]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[56]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_6__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[55]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[54]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[53]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[52]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[51]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[50]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[49]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[48]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_5__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[47]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[46]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[45]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[44]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[43]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[42]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[41]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[40]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_4__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[39]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[38]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[37]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[36]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[35]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[34]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[33]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[32]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_3__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[31]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[30]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[29]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[28]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[27]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[26]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[25]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[24]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_2__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[23]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[22]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[21]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[20]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[19]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[18]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[17]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[16]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_1__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[15]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[14]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[13]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[12]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[11]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[10]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[9]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[8]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_0__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[7]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[6]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[5]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[4]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  raddr_reg_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  GTECH_AND2 C619 ( .A(io_waddr[2]), .B(io_waddr[3]), .Z(N31) );
  GTECH_AND2 C620 ( .A(N0), .B(io_waddr[3]), .Z(N32) );
  GTECH_NOT I_0 ( .A(io_waddr[2]), .Z(N0) );
  GTECH_AND2 C621 ( .A(io_waddr[2]), .B(N1), .Z(N33) );
  GTECH_NOT I_1 ( .A(io_waddr[3]), .Z(N1) );
  GTECH_AND2 C622 ( .A(N2), .B(N3), .Z(N34) );
  GTECH_NOT I_2 ( .A(io_waddr[2]), .Z(N2) );
  GTECH_NOT I_3 ( .A(io_waddr[3]), .Z(N3) );
  GTECH_AND2 C623 ( .A(io_waddr[0]), .B(io_waddr[1]), .Z(N35) );
  GTECH_AND2 C624 ( .A(N4), .B(io_waddr[1]), .Z(N36) );
  GTECH_NOT I_4 ( .A(io_waddr[0]), .Z(N4) );
  GTECH_AND2 C625 ( .A(io_waddr[0]), .B(N5), .Z(N37) );
  GTECH_NOT I_5 ( .A(io_waddr[1]), .Z(N5) );
  GTECH_AND2 C626 ( .A(N6), .B(N7), .Z(N38) );
  GTECH_NOT I_6 ( .A(io_waddr[0]), .Z(N6) );
  GTECH_NOT I_7 ( .A(io_waddr[1]), .Z(N7) );
  GTECH_AND2 C627 ( .A(N31), .B(N35), .Z(N39) );
  GTECH_AND2 C628 ( .A(N31), .B(N36), .Z(N40) );
  GTECH_AND2 C629 ( .A(N31), .B(N37), .Z(N41) );
  GTECH_AND2 C630 ( .A(N31), .B(N38), .Z(N42) );
  GTECH_AND2 C631 ( .A(N32), .B(N35), .Z(N43) );
  GTECH_AND2 C632 ( .A(N32), .B(N36), .Z(N44) );
  GTECH_AND2 C633 ( .A(N32), .B(N37), .Z(N45) );
  GTECH_AND2 C634 ( .A(N32), .B(N38), .Z(N46) );
  GTECH_AND2 C635 ( .A(N33), .B(N35), .Z(N47) );
  GTECH_AND2 C636 ( .A(N33), .B(N36), .Z(N48) );
  GTECH_AND2 C637 ( .A(N33), .B(N37), .Z(N49) );
  GTECH_AND2 C638 ( .A(N33), .B(N38), .Z(N50) );
  GTECH_AND2 C639 ( .A(N34), .B(N35), .Z(N51) );
  GTECH_AND2 C640 ( .A(N34), .B(N36), .Z(N52) );
  GTECH_AND2 C641 ( .A(N34), .B(N37), .Z(N53) );
  GTECH_AND2 C642 ( .A(N34), .B(N38), .Z(N54) );
  SELECT_OP C643 ( .DATA1({N39, N40, N41, N42, N43, N44, N45, N46, N47, N48, 
        N49, N50, N51, N52, N53, N54}), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .CONTROL1(N8), .CONTROL2(N9), .Z({N30, N29, N28, N27, N26, N25, N24, 
        N23, N22, N21, N20, N19, N18, N17, N16, N15}) );
  GTECH_BUF B_0 ( .A(io_wen), .Z(N8) );
  GTECH_BUF B_1 ( .A(N14), .Z(N9) );
  MUX_OP C644 ( .D0({mem[0], mem[1], mem[2], mem[3], mem[4], mem[5], mem[6], 
        mem[7]}), .D1({mem[8], mem[9], mem[10], mem[11], mem[12], mem[13], 
        mem[14], mem[15]}), .D2({mem[16], mem[17], mem[18], mem[19], mem[20], 
        mem[21], mem[22], mem[23]}), .D3({mem[24], mem[25], mem[26], mem[27], 
        mem[28], mem[29], mem[30], mem[31]}), .D4({mem[32], mem[33], mem[34], 
        mem[35], mem[36], mem[37], mem[38], mem[39]}), .D5({mem[40], mem[41], 
        mem[42], mem[43], mem[44], mem[45], mem[46], mem[47]}), .D6({mem[48], 
        mem[49], mem[50], mem[51], mem[52], mem[53], mem[54], mem[55]}), .D7({
        mem[56], mem[57], mem[58], mem[59], mem[60], mem[61], mem[62], mem[63]}), .D8({mem[64], mem[65], mem[66], mem[67], mem[68], mem[69], mem[70], mem[71]}), .D9({mem[72], mem[73], mem[74], mem[75], mem[76], mem[77], mem[78], mem[79]}), .D10({mem[80], mem[81], mem[82], mem[83], mem[84], mem[85], mem[86], 
        mem[87]}), .D11({mem[88], mem[89], mem[90], mem[91], mem[92], mem[93], 
        mem[94], mem[95]}), .D12({mem[96], mem[97], mem[98], mem[99], mem[100], 
        mem[101], mem[102], mem[103]}), .D13({mem[104], mem[105], mem[106], 
        mem[107], mem[108], mem[109], mem[110], mem[111]}), .D14({mem[112], 
        mem[113], mem[114], mem[115], mem[116], mem[117], mem[118], mem[119]}), 
        .D15({mem[120], mem[121], mem[122], mem[123], mem[124], mem[125], 
        mem[126], mem[127]}), .S0(N10), .S1(N11), .S2(N12), .S3(N13), .Z({
        io_rdata[0], io_rdata[1], io_rdata[2], io_rdata[3], io_rdata[4], 
        io_rdata[5], io_rdata[6], io_rdata[7]}) );
  GTECH_BUF B_2 ( .A(raddr_reg[0]), .Z(N10) );
  GTECH_BUF B_3 ( .A(raddr_reg[1]), .Z(N11) );
  GTECH_BUF B_4 ( .A(raddr_reg[2]), .Z(N12) );
  GTECH_BUF B_5 ( .A(raddr_reg[3]), .Z(N13) );
  GTECH_NOT I_8 ( .A(io_wen), .Z(T3) );
  GTECH_NOT I_9 ( .A(io_wen), .Z(N14) );
endmodule


module SRAM_1 ( clk, io_raddr, io_wen, io_waddr, io_wdata, io_rdata );
  input [3:0] io_raddr;
  input [3:0] io_waddr;
  input [7:0] io_wdata;
  output [7:0] io_rdata;
  input clk, io_wen;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, T3, N14,
         N15, N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28,
         N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42,
         N43, N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54;
  wire   [3:0] raddr_reg;
  wire   [127:0] mem;

  \**SEQGEN**  mem_reg_15__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[127]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[126]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[125]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[124]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[123]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[122]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[121]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_15__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[120]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N30) );
  \**SEQGEN**  mem_reg_14__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[119]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[118]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[117]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[116]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[115]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[114]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[113]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_14__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[112]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N29) );
  \**SEQGEN**  mem_reg_13__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[111]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[110]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[109]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[108]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[107]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[106]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[105]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_13__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[104]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N28) );
  \**SEQGEN**  mem_reg_12__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[103]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[102]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[101]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[100]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[99]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[98]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[97]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_12__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[96]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N27) );
  \**SEQGEN**  mem_reg_11__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[95]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[94]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[93]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[92]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[91]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[90]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[89]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_11__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[88]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N26) );
  \**SEQGEN**  mem_reg_10__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[87]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[86]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[85]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[84]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[83]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[82]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[81]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_10__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[80]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N25) );
  \**SEQGEN**  mem_reg_9__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[79]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[78]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[77]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[76]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[75]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[74]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[73]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_9__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[72]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N24) );
  \**SEQGEN**  mem_reg_8__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[71]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[70]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[69]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[68]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[67]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[66]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[65]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_8__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[64]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N23) );
  \**SEQGEN**  mem_reg_7__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[63]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[62]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[61]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[60]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[59]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[58]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[57]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_7__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[56]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N22) );
  \**SEQGEN**  mem_reg_6__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[55]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[54]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[53]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[52]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[51]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[50]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[49]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_6__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[48]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N21) );
  \**SEQGEN**  mem_reg_5__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[47]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[46]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[45]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[44]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[43]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[42]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[41]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_5__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[40]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N20) );
  \**SEQGEN**  mem_reg_4__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[39]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[38]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[37]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[36]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[35]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[34]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[33]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_4__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[32]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N19) );
  \**SEQGEN**  mem_reg_3__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[31]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[30]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[29]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[28]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[27]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[26]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[25]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_3__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[24]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N18) );
  \**SEQGEN**  mem_reg_2__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[23]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[22]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[21]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[20]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[19]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[18]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[17]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_2__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[16]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N17) );
  \**SEQGEN**  mem_reg_1__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[15]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[14]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[13]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[12]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[11]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[10]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[9]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_1__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[8]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N16) );
  \**SEQGEN**  mem_reg_0__7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[7]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[7]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[6]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[6]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[5]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[5]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[4]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[4]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  mem_reg_0__0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_wdata[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        mem[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(N15) );
  \**SEQGEN**  raddr_reg_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[3]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[3]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[2]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[2]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[1]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[1]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  \**SEQGEN**  raddr_reg_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        io_raddr[0]), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        raddr_reg[0]), .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(
        1'b0), .synch_enable(T3) );
  GTECH_AND2 C619 ( .A(io_waddr[2]), .B(io_waddr[3]), .Z(N31) );
  GTECH_AND2 C620 ( .A(N0), .B(io_waddr[3]), .Z(N32) );
  GTECH_NOT I_0 ( .A(io_waddr[2]), .Z(N0) );
  GTECH_AND2 C621 ( .A(io_waddr[2]), .B(N1), .Z(N33) );
  GTECH_NOT I_1 ( .A(io_waddr[3]), .Z(N1) );
  GTECH_AND2 C622 ( .A(N2), .B(N3), .Z(N34) );
  GTECH_NOT I_2 ( .A(io_waddr[2]), .Z(N2) );
  GTECH_NOT I_3 ( .A(io_waddr[3]), .Z(N3) );
  GTECH_AND2 C623 ( .A(io_waddr[0]), .B(io_waddr[1]), .Z(N35) );
  GTECH_AND2 C624 ( .A(N4), .B(io_waddr[1]), .Z(N36) );
  GTECH_NOT I_4 ( .A(io_waddr[0]), .Z(N4) );
  GTECH_AND2 C625 ( .A(io_waddr[0]), .B(N5), .Z(N37) );
  GTECH_NOT I_5 ( .A(io_waddr[1]), .Z(N5) );
  GTECH_AND2 C626 ( .A(N6), .B(N7), .Z(N38) );
  GTECH_NOT I_6 ( .A(io_waddr[0]), .Z(N6) );
  GTECH_NOT I_7 ( .A(io_waddr[1]), .Z(N7) );
  GTECH_AND2 C627 ( .A(N31), .B(N35), .Z(N39) );
  GTECH_AND2 C628 ( .A(N31), .B(N36), .Z(N40) );
  GTECH_AND2 C629 ( .A(N31), .B(N37), .Z(N41) );
  GTECH_AND2 C630 ( .A(N31), .B(N38), .Z(N42) );
  GTECH_AND2 C631 ( .A(N32), .B(N35), .Z(N43) );
  GTECH_AND2 C632 ( .A(N32), .B(N36), .Z(N44) );
  GTECH_AND2 C633 ( .A(N32), .B(N37), .Z(N45) );
  GTECH_AND2 C634 ( .A(N32), .B(N38), .Z(N46) );
  GTECH_AND2 C635 ( .A(N33), .B(N35), .Z(N47) );
  GTECH_AND2 C636 ( .A(N33), .B(N36), .Z(N48) );
  GTECH_AND2 C637 ( .A(N33), .B(N37), .Z(N49) );
  GTECH_AND2 C638 ( .A(N33), .B(N38), .Z(N50) );
  GTECH_AND2 C639 ( .A(N34), .B(N35), .Z(N51) );
  GTECH_AND2 C640 ( .A(N34), .B(N36), .Z(N52) );
  GTECH_AND2 C641 ( .A(N34), .B(N37), .Z(N53) );
  GTECH_AND2 C642 ( .A(N34), .B(N38), .Z(N54) );
  SELECT_OP C643 ( .DATA1({N39, N40, N41, N42, N43, N44, N45, N46, N47, N48, 
        N49, N50, N51, N52, N53, N54}), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .CONTROL1(N8), .CONTROL2(N9), .Z({N30, N29, N28, N27, N26, N25, N24, 
        N23, N22, N21, N20, N19, N18, N17, N16, N15}) );
  GTECH_BUF B_0 ( .A(io_wen), .Z(N8) );
  GTECH_BUF B_1 ( .A(N14), .Z(N9) );
  MUX_OP C644 ( .D0({mem[0], mem[1], mem[2], mem[3], mem[4], mem[5], mem[6], 
        mem[7]}), .D1({mem[8], mem[9], mem[10], mem[11], mem[12], mem[13], 
        mem[14], mem[15]}), .D2({mem[16], mem[17], mem[18], mem[19], mem[20], 
        mem[21], mem[22], mem[23]}), .D3({mem[24], mem[25], mem[26], mem[27], 
        mem[28], mem[29], mem[30], mem[31]}), .D4({mem[32], mem[33], mem[34], 
        mem[35], mem[36], mem[37], mem[38], mem[39]}), .D5({mem[40], mem[41], 
        mem[42], mem[43], mem[44], mem[45], mem[46], mem[47]}), .D6({mem[48], 
        mem[49], mem[50], mem[51], mem[52], mem[53], mem[54], mem[55]}), .D7({
        mem[56], mem[57], mem[58], mem[59], mem[60], mem[61], mem[62], mem[63]}), .D8({mem[64], mem[65], mem[66], mem[67], mem[68], mem[69], mem[70], mem[71]}), .D9({mem[72], mem[73], mem[74], mem[75], mem[76], mem[77], mem[78], mem[79]}), .D10({mem[80], mem[81], mem[82], mem[83], mem[84], mem[85], mem[86], 
        mem[87]}), .D11({mem[88], mem[89], mem[90], mem[91], mem[92], mem[93], 
        mem[94], mem[95]}), .D12({mem[96], mem[97], mem[98], mem[99], mem[100], 
        mem[101], mem[102], mem[103]}), .D13({mem[104], mem[105], mem[106], 
        mem[107], mem[108], mem[109], mem[110], mem[111]}), .D14({mem[112], 
        mem[113], mem[114], mem[115], mem[116], mem[117], mem[118], mem[119]}), 
        .D15({mem[120], mem[121], mem[122], mem[123], mem[124], mem[125], 
        mem[126], mem[127]}), .S0(N10), .S1(N11), .S2(N12), .S3(N13), .Z({
        io_rdata[0], io_rdata[1], io_rdata[2], io_rdata[3], io_rdata[4], 
        io_rdata[5], io_rdata[6], io_rdata[7]}) );
  GTECH_BUF B_2 ( .A(raddr_reg[0]), .Z(N10) );
  GTECH_BUF B_3 ( .A(raddr_reg[1]), .Z(N11) );
  GTECH_BUF B_4 ( .A(raddr_reg[2]), .Z(N12) );
  GTECH_BUF B_5 ( .A(raddr_reg[3]), .Z(N13) );
  GTECH_NOT I_8 ( .A(io_wen), .Z(T3) );
  GTECH_NOT I_9 ( .A(io_wen), .Z(N14) );
endmodule


module MuxVec_0_0 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_1_0;
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C14 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_0_1 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_1_0;
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C14 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_1_0 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [7:0] io_ins_1_0;
  input [7:0] io_ins_0_0;
  output [7:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C18 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_1_1 ( io_ins_1_0, io_ins_0_0, io_sel, io_out_0 );
  input [7:0] io_ins_1_0;
  input [7:0] io_ins_0_0;
  output [7:0] io_out_0;
  input io_sel;
  wire   N0, N1, N2;

  SELECT_OP C18 ( .DATA1(io_ins_1_0), .DATA2(io_ins_0_0), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(io_out_0) );
  GTECH_BUF B_0 ( .A(io_sel), .Z(N0) );
  GTECH_BUF B_1 ( .A(N2), .Z(N1) );
  GTECH_NOT I_0 ( .A(io_sel), .Z(N2) );
endmodule


module MuxVec_2_0 ( io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;

  assign io_out_0[3] = io_ins_0_0[3];
  assign io_out_0[2] = io_ins_0_0[2];
  assign io_out_0[1] = io_ins_0_0[1];
  assign io_out_0[0] = io_ins_0_0[0];

endmodule


module MuxVec_2_1 ( io_ins_0_0, io_sel, io_out_0 );
  input [3:0] io_ins_0_0;
  output [3:0] io_out_0;
  input io_sel;

  assign io_out_0[3] = io_ins_0_0[3];
  assign io_out_0[2] = io_ins_0_0[2];
  assign io_out_0[1] = io_ins_0_0[1];
  assign io_out_0[0] = io_ins_0_0[0];

endmodule


module FF_1_0 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module Counter_0 ( clk, reset, io_data_max, io_data_stride, io_data_out, 
        io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_control_reset, io_control_enable, io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, isMax, N7, N8;
  wire   [7:0] T4;
  wire   [7:0] T1;
  wire   [7:0] T2;
  wire   [8:0] newval;

  LEQ_UNS_OP lte_301 ( .A({1'b0, io_data_max}), .B(newval), .Z(isMax) );
  FF_1_0 reg_ ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(io_data_out), 
        .io_control_enable(io_control_enable) );
  ADD_UNS_OP add_297 ( .A(io_data_out), .B(io_data_stride), .Z(newval) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(T4) );
  GTECH_BUF B_0 ( .A(io_control_reset), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C49 ( .DATA1(T2), .DATA2(newval[7:0]), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(isMax), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C50 ( .DATA1(io_data_out), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0}), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_control_saturate), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(io_control_reset), .Z(N6) );
  GTECH_NOT I_1 ( .A(isMax), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_control_saturate), .Z(N8) );
  GTECH_AND2 C62 ( .A(io_control_enable), .B(isMax), .Z(io_control_done) );
endmodule


module CounterRC_0 ( clk, reset, io_config_enable, io_data_max, io_data_stride, 
        io_data_out, io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_config_enable, io_control_reset, io_control_enable,
         io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13,
         config__strideConst, N14, N15, configIn_strideConst, config__maxConst,
         N16, configIn_maxConst, N17, N18, N19, N20, N21, N22, N23, N24, N25,
         N26, N27, N28, N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39,
         N40, N41, N42;
  wire   [7:0] T0;
  wire   [7:0] config__stride;
  wire   [7:0] configIn_stride;
  wire   [7:0] T3;
  wire   [7:0] config__max;
  wire   [7:0] configIn_max;

  Counter_0 counter ( .clk(clk), .reset(reset), .io_data_max(T3), 
        .io_data_stride(T0), .io_data_out(io_data_out), .io_control_reset(
        io_control_reset), .io_control_enable(io_control_enable), 
        .io_control_saturate(io_control_saturate), .io_control_done(
        io_control_done) );
  \**SEQGEN**  config__stride_reg_7_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N26), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_6_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N25), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N24), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N19), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__strideConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N29), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__strideConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N39), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N38), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N37), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N36), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N35), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N34), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N33), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N32), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__maxConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N42), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__maxConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C145 ( .DATA1(config__stride), .DATA2(io_data_stride), .CONTROL1(
        N0), .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__strideConst), .Z(N0) );
  GTECH_BUF B_1 ( .A(N14), .Z(N1) );
  SELECT_OP C146 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__stride), .CONTROL1(N2), .CONTROL2(N3), .Z(
        configIn_stride) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N15), .Z(N3) );
  SELECT_OP C147 ( .DATA1(1'b0), .DATA2(config__strideConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_strideConst) );
  SELECT_OP C148 ( .DATA1(config__max), .DATA2(io_data_max), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T3) );
  GTECH_BUF B_4 ( .A(config__maxConst), .Z(N4) );
  GTECH_BUF B_5 ( .A(N16), .Z(N5) );
  SELECT_OP C149 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__max), .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_max) );
  SELECT_OP C150 ( .DATA1(1'b0), .DATA2(config__maxConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_maxConst) );
  SELECT_OP C151 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1}), 
        .DATA2(configIn_stride), .CONTROL1(N6), .CONTROL2(N7), .Z({N26, N25, 
        N24, N23, N22, N21, N20, N19}) );
  GTECH_BUF B_6 ( .A(N18), .Z(N6) );
  GTECH_BUF B_7 ( .A(N17), .Z(N7) );
  SELECT_OP C152 ( .DATA1(1'b1), .DATA2(configIn_strideConst), .CONTROL1(N8), 
        .CONTROL2(N9), .Z(N29) );
  GTECH_BUF B_8 ( .A(N28), .Z(N8) );
  GTECH_BUF B_9 ( .A(N27), .Z(N9) );
  SELECT_OP C153 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0, 1'b1}), 
        .DATA2(configIn_max), .CONTROL1(N10), .CONTROL2(N11), .Z({N39, N38, 
        N37, N36, N35, N34, N33, N32}) );
  GTECH_BUF B_10 ( .A(N31), .Z(N10) );
  GTECH_BUF B_11 ( .A(N30), .Z(N11) );
  SELECT_OP C154 ( .DATA1(1'b1), .DATA2(configIn_maxConst), .CONTROL1(N12), 
        .CONTROL2(N13), .Z(N42) );
  GTECH_BUF B_12 ( .A(N41), .Z(N12) );
  GTECH_BUF B_13 ( .A(N40), .Z(N13) );
  GTECH_NOT I_0 ( .A(config__strideConst), .Z(N14) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N15) );
  GTECH_NOT I_2 ( .A(config__maxConst), .Z(N16) );
  GTECH_NOT I_3 ( .A(reset), .Z(N17) );
  GTECH_BUF B_14 ( .A(reset), .Z(N18) );
  GTECH_NOT I_4 ( .A(reset), .Z(N27) );
  GTECH_BUF B_15 ( .A(reset), .Z(N28) );
  GTECH_NOT I_5 ( .A(reset), .Z(N30) );
  GTECH_BUF B_16 ( .A(reset), .Z(N31) );
  GTECH_NOT I_6 ( .A(reset), .Z(N40) );
  GTECH_BUF B_17 ( .A(reset), .Z(N41) );
endmodule


module FF_1_1 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module Counter_1 ( clk, reset, io_data_max, io_data_stride, io_data_out, 
        io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_control_reset, io_control_enable, io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, isMax, N7, N8;
  wire   [7:0] T4;
  wire   [7:0] T1;
  wire   [7:0] T2;
  wire   [8:0] newval;

  LEQ_UNS_OP lte_301 ( .A({1'b0, io_data_max}), .B(newval), .Z(isMax) );
  FF_1_1 reg_ ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(io_data_out), 
        .io_control_enable(io_control_enable) );
  ADD_UNS_OP add_297 ( .A(io_data_out), .B(io_data_stride), .Z(newval) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(T4) );
  GTECH_BUF B_0 ( .A(io_control_reset), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C49 ( .DATA1(T2), .DATA2(newval[7:0]), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(isMax), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C50 ( .DATA1(io_data_out), .DATA2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0}), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_control_saturate), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(io_control_reset), .Z(N6) );
  GTECH_NOT I_1 ( .A(isMax), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_control_saturate), .Z(N8) );
  GTECH_AND2 C62 ( .A(io_control_enable), .B(isMax), .Z(io_control_done) );
endmodule


module CounterRC_1 ( clk, reset, io_config_enable, io_data_max, io_data_stride, 
        io_data_out, io_control_reset, io_control_enable, io_control_saturate, 
        io_control_done );
  input [7:0] io_data_max;
  input [7:0] io_data_stride;
  output [7:0] io_data_out;
  input clk, reset, io_config_enable, io_control_reset, io_control_enable,
         io_control_saturate;
  output io_control_done;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13,
         config__strideConst, N14, N15, configIn_strideConst, config__maxConst,
         N16, configIn_maxConst, N17, N18, N19, N20, N21, N22, N23, N24, N25,
         N26, N27, N28, N29, N30, N31, N32, N33, N34, N35, N36, N37, N38, N39,
         N40, N41, N42;
  wire   [7:0] T0;
  wire   [7:0] config__stride;
  wire   [7:0] configIn_stride;
  wire   [7:0] T3;
  wire   [7:0] config__max;
  wire   [7:0] configIn_max;

  Counter_1 counter ( .clk(clk), .reset(reset), .io_data_max(T3), 
        .io_data_stride(T0), .io_data_out(io_data_out), .io_control_reset(
        io_control_reset), .io_control_enable(io_control_enable), 
        .io_control_saturate(io_control_saturate), .io_control_done(
        io_control_done) );
  \**SEQGEN**  config__stride_reg_7_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N26), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_6_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N25), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N24), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__stride_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N19), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__stride[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__strideConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N29), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__strideConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N39), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[7]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N38), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[6]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N37), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[5]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N36), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[4]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N35), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N34), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N33), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__max_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(
        N32), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__max[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__maxConst_reg ( .clear(1'b0), .preset(1'b0), 
        .next_state(N42), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__maxConst), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C145 ( .DATA1(config__stride), .DATA2(io_data_stride), .CONTROL1(
        N0), .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__strideConst), .Z(N0) );
  GTECH_BUF B_1 ( .A(N14), .Z(N1) );
  SELECT_OP C146 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__stride), .CONTROL1(N2), .CONTROL2(N3), .Z(
        configIn_stride) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N15), .Z(N3) );
  SELECT_OP C147 ( .DATA1(1'b0), .DATA2(config__strideConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_strideConst) );
  SELECT_OP C148 ( .DATA1(config__max), .DATA2(io_data_max), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T3) );
  GTECH_BUF B_4 ( .A(config__maxConst), .Z(N4) );
  GTECH_BUF B_5 ( .A(N16), .Z(N5) );
  SELECT_OP C149 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .DATA2(config__max), .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_max) );
  SELECT_OP C150 ( .DATA1(1'b0), .DATA2(config__maxConst), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_maxConst) );
  SELECT_OP C151 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1}), 
        .DATA2(configIn_stride), .CONTROL1(N6), .CONTROL2(N7), .Z({N26, N25, 
        N24, N23, N22, N21, N20, N19}) );
  GTECH_BUF B_6 ( .A(N18), .Z(N6) );
  GTECH_BUF B_7 ( .A(N17), .Z(N7) );
  SELECT_OP C152 ( .DATA1(1'b1), .DATA2(configIn_strideConst), .CONTROL1(N8), 
        .CONTROL2(N9), .Z(N29) );
  GTECH_BUF B_8 ( .A(N28), .Z(N8) );
  GTECH_BUF B_9 ( .A(N27), .Z(N9) );
  SELECT_OP C153 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0, 1'b1}), 
        .DATA2(configIn_max), .CONTROL1(N10), .CONTROL2(N11), .Z({N39, N38, 
        N37, N36, N35, N34, N33, N32}) );
  GTECH_BUF B_10 ( .A(N31), .Z(N10) );
  GTECH_BUF B_11 ( .A(N30), .Z(N11) );
  SELECT_OP C154 ( .DATA1(1'b1), .DATA2(configIn_maxConst), .CONTROL1(N12), 
        .CONTROL2(N13), .Z(N42) );
  GTECH_BUF B_12 ( .A(N41), .Z(N12) );
  GTECH_BUF B_13 ( .A(N40), .Z(N13) );
  GTECH_NOT I_0 ( .A(config__strideConst), .Z(N14) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N15) );
  GTECH_NOT I_2 ( .A(config__maxConst), .Z(N16) );
  GTECH_NOT I_3 ( .A(reset), .Z(N17) );
  GTECH_BUF B_14 ( .A(reset), .Z(N18) );
  GTECH_NOT I_4 ( .A(reset), .Z(N27) );
  GTECH_BUF B_15 ( .A(reset), .Z(N28) );
  GTECH_NOT I_5 ( .A(reset), .Z(N30) );
  GTECH_BUF B_16 ( .A(reset), .Z(N31) );
  GTECH_NOT I_6 ( .A(reset), .Z(N40) );
  GTECH_BUF B_17 ( .A(reset), .Z(N41) );
endmodule


module CounterChain_0 ( clk, reset, io_config_enable, io_data_1_max, 
        io_data_1_stride, io_data_1_out, io_data_0_max, io_data_0_stride, 
        io_data_0_out, io_control_1_enable, io_control_1_done, 
        io_control_0_enable, io_control_0_done );
  input [7:0] io_data_1_max;
  input [7:0] io_data_1_stride;
  output [7:0] io_data_1_out;
  input [7:0] io_data_0_max;
  input [7:0] io_data_0_stride;
  output [7:0] io_data_0_out;
  input clk, reset, io_config_enable, io_control_1_enable, io_control_0_enable;
  output io_control_1_done, io_control_0_done;
  wire   N0, N1, N2, N3, N4, N5, config__chain_0, N6, T0, T1, configIn_chain_0,
         N7, N8, N9, N10, net1471, net1472;

  CounterRC_1 CounterRC ( .clk(clk), .reset(reset), .io_config_enable(net1472), 
        .io_data_max(io_data_0_max), .io_data_stride(io_data_0_stride), 
        .io_data_out(io_data_0_out), .io_control_reset(1'b0), 
        .io_control_enable(io_control_0_enable), .io_control_saturate(1'b0), 
        .io_control_done(io_control_0_done) );
  CounterRC_0 CounterRC_1 ( .clk(clk), .reset(reset), .io_config_enable(
        net1471), .io_data_max(io_data_1_max), .io_data_stride(
        io_data_1_stride), .io_data_out(io_data_1_out), .io_control_reset(1'b0), .io_control_enable(T0), .io_control_saturate(1'b0), .io_control_done(
        io_control_1_done) );
  \**SEQGEN**  config__chain_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N10), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__chain_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C30 ( .DATA1(T1), .DATA2(io_control_1_enable), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(config__chain_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C31 ( .DATA1(1'b0), .DATA2(config__chain_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_chain_0) );
  GTECH_BUF B_2 ( .A(io_config_enable), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C32 ( .DATA1(1'b0), .DATA2(configIn_chain_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(N10) );
  GTECH_BUF B_4 ( .A(N9), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  GTECH_NOT I_0 ( .A(config__chain_0), .Z(N6) );
  GTECH_AND2 C38 ( .A(io_control_1_enable), .B(io_control_0_done), .Z(T1) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N7) );
  GTECH_NOT I_2 ( .A(reset), .Z(N8) );
  GTECH_BUF B_6 ( .A(reset), .Z(N9) );
endmodule


module LUT_0 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module LUT_1 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module LUT_2 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module LUT_3 ( clk, reset, io_config_enable, io_ins_1, io_ins_0, io_out );
  input clk, reset, io_config_enable, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, T6, T1, config__table_1,
         config__table_0, configIn_table_0, N9, configIn_table_1, N10,
         config__table_3, config__table_2, configIn_table_2, configIn_table_3,
         N11, N12, N13, N14, N15;

  \**SEQGEN**  config__table_0_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_0), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_1_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_1), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_2_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_2), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__table_3_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__table_3), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C95 ( .DATA1(T6), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_ins_0), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C96 ( .DATA1(config__table_1), .DATA2(config__table_0), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_ins_1), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C97 ( .DATA1(1'b0), .DATA2(config__table_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_0) );
  GTECH_BUF B_4 ( .A(io_config_enable), .Z(N4) );
  GTECH_BUF B_5 ( .A(N9), .Z(N5) );
  SELECT_OP C98 ( .DATA1(1'b0), .DATA2(config__table_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_1) );
  SELECT_OP C99 ( .DATA1(config__table_3), .DATA2(config__table_2), .CONTROL1(
        N2), .CONTROL2(N3), .Z(T6) );
  SELECT_OP C100 ( .DATA1(1'b0), .DATA2(config__table_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_2) );
  SELECT_OP C101 ( .DATA1(1'b0), .DATA2(config__table_3), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(configIn_table_3) );
  SELECT_OP C102 ( .DATA1(1'b0), .DATA2(configIn_table_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N12) );
  GTECH_BUF B_6 ( .A(reset), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C103 ( .DATA1(1'b0), .DATA2(configIn_table_1), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N13) );
  SELECT_OP C104 ( .DATA1(1'b0), .DATA2(configIn_table_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N14) );
  SELECT_OP C105 ( .DATA1(1'b0), .DATA2(configIn_table_3), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(N15) );
  GTECH_NOT I_0 ( .A(io_ins_0), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_ins_1), .Z(N10) );
  GTECH_NOT I_3 ( .A(reset), .Z(N11) );
endmodule


module MuxN_4_4 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_5 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module Crossbar_0_0 ( clk, reset, io_config_enable, io_ins_2, io_ins_1, 
        io_ins_0, io_outs_1, io_outs_0 );
  input clk, reset, io_config_enable, io_ins_2, io_ins_1, io_ins_0;
  output io_outs_1, io_outs_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [1:0] configIn_outSelect_1;
  wire   [1:0] config__outSelect_1;
  wire   [1:0] configIn_outSelect_0;
  wire   [1:0] config__outSelect_0;

  MuxN_4_5 MuxN ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(io_ins_0), .io_sel(config__outSelect_0), .io_out(io_outs_0) );
  MuxN_4_4 MuxN_1 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_1), .io_out(io_outs_1) );
  \**SEQGEN**  config__outSelect_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N7), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N6), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N9), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N8), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C47 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_1) );
  GTECH_BUF B_0 ( .A(io_config_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C48 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_0), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_0) );
  SELECT_OP C49 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_1), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C50 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8}) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_4_0 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_1 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_2 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_4_3 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [1:0] io_sel;
  input io_ins_2, io_ins_1, io_ins_0;
  output io_out;
  wire   N0, N1, N2, N3, N4, T1, N5;

  SELECT_OP C17 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C18 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module Crossbar_1_0 ( clk, reset, io_config_enable, io_ins_2, io_ins_1, 
        io_ins_0, io_outs_3, io_outs_2, io_outs_1, io_outs_0 );
  input clk, reset, io_config_enable, io_ins_2, io_ins_1, io_ins_0;
  output io_outs_3, io_outs_2, io_outs_1, io_outs_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [1:0] configIn_outSelect_3;
  wire   [1:0] config__outSelect_3;
  wire   [1:0] configIn_outSelect_2;
  wire   [1:0] config__outSelect_2;
  wire   [1:0] configIn_outSelect_1;
  wire   [1:0] config__outSelect_1;
  wire   [1:0] configIn_outSelect_0;
  wire   [1:0] config__outSelect_0;

  MuxN_4_3 MuxN ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(io_ins_0), .io_sel(config__outSelect_0), .io_out(io_outs_0) );
  MuxN_4_2 MuxN_1 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_1), .io_out(io_outs_1) );
  MuxN_4_1 MuxN_2 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_2), .io_out(io_outs_2) );
  MuxN_4_0 MuxN_3 ( .io_ins_2(io_ins_2), .io_ins_1(io_ins_1), .io_ins_0(
        io_ins_0), .io_sel(config__outSelect_3), .io_out(io_outs_3) );
  \**SEQGEN**  config__outSelect_3_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N7), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_3[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_3_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N6), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_3[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_2_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N9), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_2[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_2_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N8), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_2[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N11), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N10), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N13), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__outSelect_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N12), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__outSelect_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C89 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_3), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_3) );
  GTECH_BUF B_0 ( .A(io_config_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C90 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_2), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_2) );
  SELECT_OP C91 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_1) );
  SELECT_OP C92 ( .DATA1({1'b0, 1'b0}), .DATA2(config__outSelect_0), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_outSelect_0) );
  SELECT_OP C93 ( .DATA1({1'b1, 1'b0}), .DATA2(configIn_outSelect_3), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C94 ( .DATA1({1'b0, 1'b0}), .DATA2(configIn_outSelect_2), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8}) );
  SELECT_OP C95 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_outSelect_1), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N11, N10}) );
  SELECT_OP C96 ( .DATA1({1'b1, 1'b1}), .DATA2(configIn_outSelect_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12}) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_2_0 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [3:0] io_data_in;
  input [3:0] io_data_init;
  output [3:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [3:0] d;

  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C32 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C33 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module UpDownCtr_0 ( clk, reset, io_initval, io_init, io_inc, io_dec, io_gtz
 );
  input [3:0] io_initval;
  input clk, reset, io_init, io_inc, io_dec;
  output io_gtz;
  wire   N0, N1, N2, N3, T1, T0, N4, N5;
  wire   [3:0] T2;
  wire   [3:0] T3;
  wire   [3:0] incval;
  wire   [3:0] decval;
  wire   [3:0] reg__io_data_out;

  SUB_UNS_OP sub_898 ( .A(reg__io_data_out), .B(1'b1), .Z(decval) );
  ADD_UNS_OP add_899 ( .A(reg__io_data_out), .B(1'b1), .Z(incval) );
  LT_UNS_OP lt_901 ( .A(1'b0), .B(reg__io_data_out), .Z(io_gtz) );
  FF_2_0 reg_ ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0}), .io_data_out(reg__io_data_out), 
        .io_control_enable(T0) );
  SELECT_OP C25 ( .DATA1(io_initval), .DATA2(T3), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(T2) );
  GTECH_BUF B_0 ( .A(io_init), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C26 ( .DATA1(incval), .DATA2(decval), .CONTROL1(N2), .CONTROL2(N3), 
        .Z(T3) );
  GTECH_BUF B_2 ( .A(io_inc), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_OR2 C29 ( .A(T1), .B(io_init), .Z(T0) );
  GTECH_XOR2 C30 ( .A(io_inc), .B(io_dec), .Z(T1) );
  GTECH_NOT I_0 ( .A(io_init), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_inc), .Z(N5) );
endmodule


module FF_2_1 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [3:0] io_data_in;
  input [3:0] io_data_init;
  output [3:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9;
  wire   [3:0] d;

  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C32 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C33 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module UpDownCtr_1 ( clk, reset, io_initval, io_init, io_inc, io_dec, io_gtz
 );
  input [3:0] io_initval;
  input clk, reset, io_init, io_inc, io_dec;
  output io_gtz;
  wire   N0, N1, N2, N3, T1, T0, N4, N5;
  wire   [3:0] T2;
  wire   [3:0] T3;
  wire   [3:0] incval;
  wire   [3:0] decval;
  wire   [3:0] reg__io_data_out;

  SUB_UNS_OP sub_898 ( .A(reg__io_data_out), .B(1'b1), .Z(decval) );
  ADD_UNS_OP add_899 ( .A(reg__io_data_out), .B(1'b1), .Z(incval) );
  LT_UNS_OP lt_901 ( .A(1'b0), .B(reg__io_data_out), .Z(io_gtz) );
  FF_2_1 reg_ ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0}), .io_data_out(reg__io_data_out), 
        .io_control_enable(T0) );
  SELECT_OP C25 ( .DATA1(io_initval), .DATA2(T3), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(T2) );
  GTECH_BUF B_0 ( .A(io_init), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C26 ( .DATA1(incval), .DATA2(decval), .CONTROL1(N2), .CONTROL2(N3), 
        .Z(T3) );
  GTECH_BUF B_2 ( .A(io_inc), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_OR2 C29 ( .A(T1), .B(io_init), .Z(T0) );
  GTECH_XOR2 C30 ( .A(io_inc), .B(io_dec), .Z(T1) );
  GTECH_NOT I_0 ( .A(io_init), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_inc), .Z(N5) );
endmodule


module CUControlBox_0 ( clk, reset, io_config_enable, io_tokenIns_1, 
        io_tokenIns_0, io_done_1, io_done_0, io_tokenOuts_1, io_tokenOuts_0, 
        io_enable_1, io_enable_0 );
  input clk, reset, io_config_enable, io_tokenIns_1, io_tokenIns_0, io_done_1,
         io_done_0;
  output io_tokenOuts_1, io_tokenOuts_0, io_enable_1, io_enable_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, decXbar_io_outs_1,
         decXbar_io_outs_0, incXbar_io_outs_3, incXbar_io_outs_2,
         incXbar_io_outs_1, incXbar_io_outs_0, UpDownCtr_io_gtz,
         UpDownCtr_1_io_gtz, N12, N13, N14, N15, N16, N17, N18, N19, N20, N21,
         N22, N23, net1461, net1462, net1463, net1464, net1465, net1466,
         net1467, net1468, net1469, net1470;
  wire   [3:0] configIn_udcInit_1;
  wire   [3:0] config__udcInit_1;
  wire   [3:0] configIn_udcInit_0;
  wire   [3:0] config__udcInit_0;

  LUT_3 LUT ( .clk(clk), .reset(reset), .io_config_enable(net1470), .io_ins_1(
        io_done_1), .io_ins_0(io_done_0), .io_out(io_tokenOuts_0) );
  LUT_2 LUT_1 ( .clk(clk), .reset(reset), .io_config_enable(net1469), 
        .io_ins_1(io_done_1), .io_ins_0(io_done_0), .io_out(io_tokenOuts_1) );
  Crossbar_0_0 decXbar ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_ins_2(net1466), .io_ins_1(net1467), .io_ins_0(
        net1468), .io_outs_1(decXbar_io_outs_1), .io_outs_0(decXbar_io_outs_0)
         );
  Crossbar_1_0 incXbar ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_ins_2(net1463), .io_ins_1(net1464), .io_ins_0(
        net1465), .io_outs_3(incXbar_io_outs_3), .io_outs_2(incXbar_io_outs_2), 
        .io_outs_1(incXbar_io_outs_1), .io_outs_0(incXbar_io_outs_0) );
  UpDownCtr_1 UpDownCtr ( .clk(clk), .reset(reset), .io_initval(
        config__udcInit_0), .io_init(incXbar_io_outs_2), .io_inc(
        incXbar_io_outs_0), .io_dec(decXbar_io_outs_0), .io_gtz(
        UpDownCtr_io_gtz) );
  UpDownCtr_0 UpDownCtr_1 ( .clk(clk), .reset(reset), .io_initval(
        config__udcInit_1), .io_init(incXbar_io_outs_3), .io_inc(
        incXbar_io_outs_1), .io_dec(decXbar_io_outs_1), .io_gtz(
        UpDownCtr_1_io_gtz) );
  LUT_1 LUT_2 ( .clk(clk), .reset(reset), .io_config_enable(net1462), 
        .io_ins_1(UpDownCtr_1_io_gtz), .io_ins_0(UpDownCtr_io_gtz), .io_out(
        io_enable_0) );
  LUT_0 LUT_3 ( .clk(clk), .reset(reset), .io_config_enable(net1461), 
        .io_ins_1(UpDownCtr_1_io_gtz), .io_ins_0(UpDownCtr_io_gtz), .io_out(
        io_enable_1) );
  \**SEQGEN**  config__udcInit_1_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N17), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N16), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N15), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N14), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N23), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[3]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N22), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N21), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__udcInit_0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N20), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__udcInit_0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C59 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(config__udcInit_1), 
        .CONTROL1(N0), .CONTROL2(N1), .Z(configIn_udcInit_1) );
  GTECH_BUF B_0 ( .A(N9), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C60 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(config__udcInit_0), 
        .CONTROL1(N2), .CONTROL2(N3), .Z(configIn_udcInit_0) );
  GTECH_BUF B_2 ( .A(N11), .Z(N2) );
  GTECH_BUF B_3 ( .A(N10), .Z(N3) );
  SELECT_OP C61 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(configIn_udcInit_1), 
        .CONTROL1(N4), .CONTROL2(N5), .Z({N17, N16, N15, N14}) );
  GTECH_BUF B_4 ( .A(N13), .Z(N4) );
  GTECH_BUF B_5 ( .A(N12), .Z(N5) );
  SELECT_OP C62 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b1}), .DATA2(configIn_udcInit_0), 
        .CONTROL1(N6), .CONTROL2(N7), .Z({N23, N22, N21, N20}) );
  GTECH_BUF B_6 ( .A(N19), .Z(N6) );
  GTECH_BUF B_7 ( .A(N18), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N8) );
  GTECH_BUF B_8 ( .A(io_config_enable), .Z(N9) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N10) );
  GTECH_BUF B_9 ( .A(io_config_enable), .Z(N11) );
  GTECH_NOT I_2 ( .A(reset), .Z(N12) );
  GTECH_BUF B_10 ( .A(reset), .Z(N13) );
  GTECH_NOT I_3 ( .A(reset), .Z(N18) );
  GTECH_BUF B_11 ( .A(reset), .Z(N19) );
endmodule


module MuxN_0_8 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_9 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_10 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_11 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_2_0 ( io_ins_8, io_ins_7, io_ins_6, io_ins_5, io_ins_4, io_ins_3, 
        io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_8;
  input [7:0] io_ins_7;
  input [7:0] io_ins_6;
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [3:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T1;
  wire   [7:0] T9;
  wire   [7:0] T2;
  wire   [7:0] T6;
  wire   [7:0] T3;
  wire   [7:0] T12;
  wire   [7:0] T10;

  SELECT_OP C109 ( .DATA1(io_ins_8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[3]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C110 ( .DATA1(T9), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[2]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C111 ( .DATA1(T6), .DATA2(T3), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[1]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C112 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T3) );
  GTECH_BUF B_6 ( .A(io_sel[0]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C113 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  SELECT_OP C114 ( .DATA1(T12), .DATA2(T10), .CONTROL1(N4), .CONTROL2(N5), .Z(
        T9) );
  SELECT_OP C115 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T10) );
  SELECT_OP C116 ( .DATA1(io_ins_7), .DATA2(io_ins_6), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T12) );
  GTECH_NOT I_0 ( .A(io_sel[3]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_sel[2]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_sel[1]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_sel[0]), .Z(N11) );
endmodule


module IntFU_0 ( io_a, io_b, io_opcode, io_out );
  input [7:0] io_a;
  input [7:0] io_b;
  input [3:0] io_opcode;
  output [7:0] io_out;
  wire   net1863, net1864, net1865, net1866, net1867, net1868, net1869,
         net1870;
  wire   [7:0] T0;
  wire   [7:0] T1;
  wire   [7:0] T7;
  wire   [7:0] T3;
  wire   [7:0] T4;
  wire   [7:0] T5;
  wire   [0:0] T8;

  ADD_UNS_OP add_1177 ( .A(io_a), .B(io_b), .Z(T0) );
  SUB_UNS_OP sub_1178 ( .A(io_a), .B(io_b), .Z(T1) );
  MULT_UNS_OP mult_1180 ( .A(io_a), .B(io_b), .Z({net1863, net1864, net1865, 
        net1866, net1867, net1868, net1869, net1870, T7}) );
  DIV_UNS_OP div_1181 ( .A(io_a), .B(io_b), .QUOTIENT(T3) );
  EQ_UNS_OP eq_1185 ( .A(io_a), .B(io_b), .Z(T8[0]) );
  MuxN_2_0 m ( .io_ins_8(io_b), .io_ins_7(io_a), .io_ins_6({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, T8[0]}), .io_ins_5(T5), .io_ins_4(T4), 
        .io_ins_3(T3), .io_ins_2(T7), .io_ins_1(T1), .io_ins_0(T0), .io_sel(
        io_opcode), .io_out(io_out) );
  GTECH_AND2 C23 ( .A(io_a[7]), .B(io_b[7]), .Z(T4[7]) );
  GTECH_AND2 C24 ( .A(io_a[6]), .B(io_b[6]), .Z(T4[6]) );
  GTECH_AND2 C25 ( .A(io_a[5]), .B(io_b[5]), .Z(T4[5]) );
  GTECH_AND2 C26 ( .A(io_a[4]), .B(io_b[4]), .Z(T4[4]) );
  GTECH_AND2 C27 ( .A(io_a[3]), .B(io_b[3]), .Z(T4[3]) );
  GTECH_AND2 C28 ( .A(io_a[2]), .B(io_b[2]), .Z(T4[2]) );
  GTECH_AND2 C29 ( .A(io_a[1]), .B(io_b[1]), .Z(T4[1]) );
  GTECH_AND2 C30 ( .A(io_a[0]), .B(io_b[0]), .Z(T4[0]) );
  GTECH_OR2 C31 ( .A(io_a[7]), .B(io_b[7]), .Z(T5[7]) );
  GTECH_OR2 C32 ( .A(io_a[6]), .B(io_b[6]), .Z(T5[6]) );
  GTECH_OR2 C33 ( .A(io_a[5]), .B(io_b[5]), .Z(T5[5]) );
  GTECH_OR2 C34 ( .A(io_a[4]), .B(io_b[4]), .Z(T5[4]) );
  GTECH_OR2 C35 ( .A(io_a[3]), .B(io_b[3]), .Z(T5[3]) );
  GTECH_OR2 C36 ( .A(io_a[2]), .B(io_b[2]), .Z(T5[2]) );
  GTECH_OR2 C37 ( .A(io_a[1]), .B(io_b[1]), .Z(T5[1]) );
  GTECH_OR2 C38 ( .A(io_a[0]), .B(io_b[0]), .Z(T5[0]) );
endmodule


module MuxN_2_1 ( io_ins_8, io_ins_7, io_ins_6, io_ins_5, io_ins_4, io_ins_3, 
        io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_8;
  input [7:0] io_ins_7;
  input [7:0] io_ins_6;
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [3:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T1;
  wire   [7:0] T9;
  wire   [7:0] T2;
  wire   [7:0] T6;
  wire   [7:0] T3;
  wire   [7:0] T12;
  wire   [7:0] T10;

  SELECT_OP C109 ( .DATA1(io_ins_8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[3]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C110 ( .DATA1(T9), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[2]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C111 ( .DATA1(T6), .DATA2(T3), .CONTROL1(N4), .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[1]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C112 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T3) );
  GTECH_BUF B_6 ( .A(io_sel[0]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  SELECT_OP C113 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  SELECT_OP C114 ( .DATA1(T12), .DATA2(T10), .CONTROL1(N4), .CONTROL2(N5), .Z(
        T9) );
  SELECT_OP C115 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T10) );
  SELECT_OP C116 ( .DATA1(io_ins_7), .DATA2(io_ins_6), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T12) );
  GTECH_NOT I_0 ( .A(io_sel[3]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_sel[2]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_sel[1]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_sel[0]), .Z(N11) );
endmodule


module IntFU_1 ( io_a, io_b, io_opcode, io_out );
  input [7:0] io_a;
  input [7:0] io_b;
  input [3:0] io_opcode;
  output [7:0] io_out;
  wire   net2098, net2099, net2100, net2101, net2102, net2103, net2104,
         net2105;
  wire   [7:0] T0;
  wire   [7:0] T1;
  wire   [7:0] T7;
  wire   [7:0] T3;
  wire   [7:0] T4;
  wire   [7:0] T5;
  wire   [0:0] T8;

  ADD_UNS_OP add_1177 ( .A(io_a), .B(io_b), .Z(T0) );
  SUB_UNS_OP sub_1178 ( .A(io_a), .B(io_b), .Z(T1) );
  MULT_UNS_OP mult_1180 ( .A(io_a), .B(io_b), .Z({net2098, net2099, net2100, 
        net2101, net2102, net2103, net2104, net2105, T7}) );
  DIV_UNS_OP div_1181 ( .A(io_a), .B(io_b), .QUOTIENT(T3) );
  EQ_UNS_OP eq_1185 ( .A(io_a), .B(io_b), .Z(T8[0]) );
  MuxN_2_1 m ( .io_ins_8(io_b), .io_ins_7(io_a), .io_ins_6({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, T8[0]}), .io_ins_5(T5), .io_ins_4(T4), 
        .io_ins_3(T3), .io_ins_2(T7), .io_ins_1(T1), .io_ins_0(T0), .io_sel(
        io_opcode), .io_out(io_out) );
  GTECH_AND2 C23 ( .A(io_a[7]), .B(io_b[7]), .Z(T4[7]) );
  GTECH_AND2 C24 ( .A(io_a[6]), .B(io_b[6]), .Z(T4[6]) );
  GTECH_AND2 C25 ( .A(io_a[5]), .B(io_b[5]), .Z(T4[5]) );
  GTECH_AND2 C26 ( .A(io_a[4]), .B(io_b[4]), .Z(T4[4]) );
  GTECH_AND2 C27 ( .A(io_a[3]), .B(io_b[3]), .Z(T4[3]) );
  GTECH_AND2 C28 ( .A(io_a[2]), .B(io_b[2]), .Z(T4[2]) );
  GTECH_AND2 C29 ( .A(io_a[1]), .B(io_b[1]), .Z(T4[1]) );
  GTECH_AND2 C30 ( .A(io_a[0]), .B(io_b[0]), .Z(T4[0]) );
  GTECH_OR2 C31 ( .A(io_a[7]), .B(io_b[7]), .Z(T5[7]) );
  GTECH_OR2 C32 ( .A(io_a[6]), .B(io_b[6]), .Z(T5[6]) );
  GTECH_OR2 C33 ( .A(io_a[5]), .B(io_b[5]), .Z(T5[5]) );
  GTECH_OR2 C34 ( .A(io_a[4]), .B(io_b[4]), .Z(T5[4]) );
  GTECH_OR2 C35 ( .A(io_a[3]), .B(io_b[3]), .Z(T5[3]) );
  GTECH_OR2 C36 ( .A(io_a[2]), .B(io_b[2]), .Z(T5[2]) );
  GTECH_OR2 C37 ( .A(io_a[1]), .B(io_b[1]), .Z(T5[1]) );
  GTECH_OR2 C38 ( .A(io_a[0]), .B(io_b[0]), .Z(T5[0]) );
endmodule


module MuxN_0_0 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_1 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module FF_1_4 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_5 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_6 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_7 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_8 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_9 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_3_0 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module MuxN_3_1 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module RegisterBlock_0 ( clk, reset, io_writeData, io_passData_3, 
        io_passData_2, io_passData_1, io_passData_0, io_writeSel, 
        io_readLocalASel, io_readLocalBSel, io_readRemoteASel, 
        io_readRemoteBSel, io_readLocalA, io_readLocalB, io_readRemoteA, 
        io_readRemoteB, io_passDataOut_3, io_passDataOut_2, io_passDataOut_1, 
        io_passDataOut_0 );
  input [7:0] io_writeData;
  input [7:0] io_passData_3;
  input [7:0] io_passData_2;
  input [7:0] io_passData_1;
  input [7:0] io_passData_0;
  input [5:0] io_writeSel;
  input [2:0] io_readLocalASel;
  input [2:0] io_readLocalBSel;
  input [1:0] io_readRemoteASel;
  input [1:0] io_readRemoteBSel;
  output [7:0] io_readLocalA;
  output [7:0] io_readLocalB;
  output [7:0] io_readRemoteA;
  output [7:0] io_readRemoteB;
  output [7:0] io_passDataOut_3;
  output [7:0] io_passDataOut_2;
  output [7:0] io_passDataOut_1;
  output [7:0] io_passDataOut_0;
  input clk, reset;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T0;
  wire   [7:0] T2;
  wire   [7:0] T4;
  wire   [7:0] T6;
  wire   [7:0] FF_io_data_out;
  wire   [7:0] FF_1_io_data_out;

  FF_1_9 FF ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_io_data_out), .io_control_enable(io_writeSel[0]) );
  FF_1_8 FF_1 ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_1_io_data_out), .io_control_enable(io_writeSel[1]) );
  FF_1_7 FF_2 ( .clk(clk), .reset(reset), .io_data_in(T6), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_0), .io_control_enable(1'b1) );
  FF_1_6 FF_3 ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_1), .io_control_enable(1'b1) );
  FF_1_5 FF_4 ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_2), .io_control_enable(1'b1) );
  FF_1_4 FF_5 ( .clk(clk), .reset(reset), .io_data_in(T0), .io_data_init({1'b0, 
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_3), .io_control_enable(1'b1) );
  MuxN_3_1 readLocalAMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalASel), .io_out(io_readLocalA) );
  MuxN_3_0 readLocalBMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalBSel), .io_out(io_readLocalB) );
  MuxN_0_1 readRemoteAMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteASel), .io_out(io_readRemoteA)
         );
  MuxN_0_0 readRemoteBMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteBSel), .io_out(io_readRemoteB)
         );
  SELECT_OP C57 ( .DATA1(io_writeData), .DATA2(io_passData_3), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(io_writeSel[5]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C58 ( .DATA1(io_writeData), .DATA2(io_passData_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T2) );
  GTECH_BUF B_2 ( .A(io_writeSel[4]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C59 ( .DATA1(io_writeData), .DATA2(io_passData_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T4) );
  GTECH_BUF B_4 ( .A(io_writeSel[3]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C60 ( .DATA1(io_writeData), .DATA2(io_passData_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  GTECH_BUF B_6 ( .A(io_writeSel[2]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_writeSel[5]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_writeSel[4]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_writeSel[3]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_writeSel[2]), .Z(N11) );
endmodule


module MuxN_0_2 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_0_3 ( io_ins_3, io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T4;
  wire   [7:0] T1;

  SELECT_OP C44 ( .DATA1(T4), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  SELECT_OP C46 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T4) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module FF_1_10 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_11 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_12 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_13 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_14 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module FF_1_15 ( clk, reset, io_data_in, io_data_init, io_data_out, 
        io_control_enable );
  input [7:0] io_data_in;
  input [7:0] io_data_init;
  output [7:0] io_data_out;
  input clk, reset, io_control_enable;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13;
  wire   [7:0] d;

  \**SEQGEN**  ff_reg_7_ ( .clear(1'b0), .preset(1'b0), .next_state(N13), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[7]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_6_ ( .clear(1'b0), .preset(1'b0), .next_state(N12), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[6]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_5_ ( .clear(1'b0), .preset(1'b0), .next_state(N11), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[5]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_4_ ( .clear(1'b0), .preset(1'b0), .next_state(N10), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[4]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_3_ ( .clear(1'b0), .preset(1'b0), .next_state(N9), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[3]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_2_ ( .clear(1'b0), .preset(1'b0), .next_state(N8), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[2]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_1_ ( .clear(1'b0), .preset(1'b0), .next_state(N7), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[1]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  \**SEQGEN**  ff_reg_0_ ( .clear(1'b0), .preset(1'b0), .next_state(N6), 
        .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(io_data_out[0]), 
        .synch_clear(1'b0), .synch_preset(1'b0), .synch_toggle(1'b0), 
        .synch_enable(1'b1) );
  SELECT_OP C44 ( .DATA1(io_data_in), .DATA2(io_data_out), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(d) );
  GTECH_BUF B_0 ( .A(io_control_enable), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C45 ( .DATA1(io_data_init), .DATA2(d), .CONTROL1(N2), .CONTROL2(N3), .Z({N13, N12, N11, N10, N9, N8, N7, N6}) );
  GTECH_BUF B_2 ( .A(reset), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_control_enable), .Z(N4) );
  GTECH_NOT I_1 ( .A(reset), .Z(N5) );
endmodule


module MuxN_3_2 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module MuxN_3_3 ( io_ins_5, io_ins_4, io_ins_3, io_ins_2, io_ins_1, io_ins_0, 
        io_sel, io_out );
  input [7:0] io_ins_5;
  input [7:0] io_ins_4;
  input [7:0] io_ins_3;
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [2:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8;
  wire   [7:0] T8;
  wire   [7:0] T1;
  wire   [7:0] T5;
  wire   [7:0] T2;

  SELECT_OP C70 ( .DATA1(T8), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), .Z(
        io_out) );
  GTECH_BUF B_0 ( .A(io_sel[2]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N6), .Z(N1) );
  SELECT_OP C71 ( .DATA1(T5), .DATA2(T2), .CONTROL1(N2), .CONTROL2(N3), .Z(T1)
         );
  GTECH_BUF B_2 ( .A(io_sel[1]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N7), .Z(N3) );
  SELECT_OP C72 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T2) );
  GTECH_BUF B_4 ( .A(io_sel[0]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N8), .Z(N5) );
  SELECT_OP C73 ( .DATA1(io_ins_3), .DATA2(io_ins_2), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T5) );
  SELECT_OP C74 ( .DATA1(io_ins_5), .DATA2(io_ins_4), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T8) );
  GTECH_NOT I_0 ( .A(io_sel[2]), .Z(N6) );
  GTECH_NOT I_1 ( .A(io_sel[1]), .Z(N7) );
  GTECH_NOT I_2 ( .A(io_sel[0]), .Z(N8) );
endmodule


module RegisterBlock_1 ( clk, reset, io_writeData, io_passData_3, 
        io_passData_2, io_passData_1, io_passData_0, io_writeSel, 
        io_readLocalASel, io_readLocalBSel, io_readRemoteASel, 
        io_readRemoteBSel, io_readLocalA, io_readLocalB, io_readRemoteA, 
        io_readRemoteB, io_passDataOut_3, io_passDataOut_2, io_passDataOut_1, 
        io_passDataOut_0 );
  input [7:0] io_writeData;
  input [7:0] io_passData_3;
  input [7:0] io_passData_2;
  input [7:0] io_passData_1;
  input [7:0] io_passData_0;
  input [5:0] io_writeSel;
  input [2:0] io_readLocalASel;
  input [2:0] io_readLocalBSel;
  input [1:0] io_readRemoteASel;
  input [1:0] io_readRemoteBSel;
  output [7:0] io_readLocalA;
  output [7:0] io_readLocalB;
  output [7:0] io_readRemoteA;
  output [7:0] io_readRemoteB;
  output [7:0] io_passDataOut_3;
  output [7:0] io_passDataOut_2;
  output [7:0] io_passDataOut_1;
  output [7:0] io_passDataOut_0;
  input clk, reset;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11;
  wire   [7:0] T0;
  wire   [7:0] T2;
  wire   [7:0] T4;
  wire   [7:0] T6;
  wire   [7:0] FF_io_data_out;
  wire   [7:0] FF_1_io_data_out;

  FF_1_15 FF ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_io_data_out), .io_control_enable(io_writeSel[0]) );
  FF_1_14 FF_1 ( .clk(clk), .reset(reset), .io_data_in(io_writeData), 
        .io_data_init({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), 
        .io_data_out(FF_1_io_data_out), .io_control_enable(io_writeSel[1]) );
  FF_1_13 FF_2 ( .clk(clk), .reset(reset), .io_data_in(T6), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_0), .io_control_enable(1'b1) );
  FF_1_12 FF_3 ( .clk(clk), .reset(reset), .io_data_in(T4), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_1), .io_control_enable(1'b1) );
  FF_1_11 FF_4 ( .clk(clk), .reset(reset), .io_data_in(T2), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_2), .io_control_enable(1'b1) );
  FF_1_10 FF_5 ( .clk(clk), .reset(reset), .io_data_in(T0), .io_data_init({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_out(
        io_passDataOut_3), .io_control_enable(1'b1) );
  MuxN_3_3 readLocalAMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalASel), .io_out(io_readLocalA) );
  MuxN_3_2 readLocalBMux ( .io_ins_5(io_passDataOut_3), .io_ins_4(
        io_passDataOut_2), .io_ins_3(io_passDataOut_1), .io_ins_2(
        io_passDataOut_0), .io_ins_1(FF_1_io_data_out), .io_ins_0(
        FF_io_data_out), .io_sel(io_readLocalBSel), .io_out(io_readLocalB) );
  MuxN_0_3 readRemoteAMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteASel), .io_out(io_readRemoteA)
         );
  MuxN_0_2 readRemoteBMux ( .io_ins_3(io_passDataOut_3), .io_ins_2(
        io_passDataOut_2), .io_ins_1(io_passDataOut_1), .io_ins_0(
        io_passDataOut_0), .io_sel(io_readRemoteBSel), .io_out(io_readRemoteB)
         );
  SELECT_OP C57 ( .DATA1(io_writeData), .DATA2(io_passData_3), .CONTROL1(N0), 
        .CONTROL2(N1), .Z(T0) );
  GTECH_BUF B_0 ( .A(io_writeSel[5]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N8), .Z(N1) );
  SELECT_OP C58 ( .DATA1(io_writeData), .DATA2(io_passData_2), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T2) );
  GTECH_BUF B_2 ( .A(io_writeSel[4]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N9), .Z(N3) );
  SELECT_OP C59 ( .DATA1(io_writeData), .DATA2(io_passData_1), .CONTROL1(N4), 
        .CONTROL2(N5), .Z(T4) );
  GTECH_BUF B_4 ( .A(io_writeSel[3]), .Z(N4) );
  GTECH_BUF B_5 ( .A(N10), .Z(N5) );
  SELECT_OP C60 ( .DATA1(io_writeData), .DATA2(io_passData_0), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(T6) );
  GTECH_BUF B_6 ( .A(io_writeSel[2]), .Z(N6) );
  GTECH_BUF B_7 ( .A(N11), .Z(N7) );
  GTECH_NOT I_0 ( .A(io_writeSel[5]), .Z(N8) );
  GTECH_NOT I_1 ( .A(io_writeSel[4]), .Z(N9) );
  GTECH_NOT I_2 ( .A(io_writeSel[3]), .Z(N10) );
  GTECH_NOT I_3 ( .A(io_writeSel[2]), .Z(N11) );
endmodule


module MuxN_1_0 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T1;

  SELECT_OP C31 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C32 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module MuxN_1_1 ( io_ins_2, io_ins_1, io_ins_0, io_sel, io_out );
  input [7:0] io_ins_2;
  input [7:0] io_ins_1;
  input [7:0] io_ins_0;
  input [1:0] io_sel;
  output [7:0] io_out;
  wire   N0, N1, N2, N3, N4, N5;
  wire   [7:0] T1;

  SELECT_OP C31 ( .DATA1(io_ins_2), .DATA2(T1), .CONTROL1(N0), .CONTROL2(N1), 
        .Z(io_out) );
  GTECH_BUF B_0 ( .A(io_sel[1]), .Z(N0) );
  GTECH_BUF B_1 ( .A(N4), .Z(N1) );
  SELECT_OP C32 ( .DATA1(io_ins_1), .DATA2(io_ins_0), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(T1) );
  GTECH_BUF B_2 ( .A(io_sel[0]), .Z(N2) );
  GTECH_BUF B_3 ( .A(N5), .Z(N3) );
  GTECH_NOT I_0 ( .A(io_sel[1]), .Z(N4) );
  GTECH_NOT I_1 ( .A(io_sel[0]), .Z(N5) );
endmodule


module ComputeUnit_0 ( clk, reset, io_config_enable, io_tokenIns_1, 
        io_tokenIns_0, io_tokenOuts_1, io_tokenOuts_0, io_scalarOut, 
        io_dataIn_0, io_dataOut_0 );
  output [7:0] io_scalarOut;
  input [7:0] io_dataIn_0;
  output [7:0] io_dataOut_0;
  input clk, reset, io_config_enable, io_tokenIns_1, io_tokenIns_0;
  output io_tokenOuts_1, io_tokenOuts_0;
  wire   N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12, N13, N14, N15,
         N16, N17, N18, N19, N20, N21, N22, N23, N24, N25, N26, N27, N28, N29,
         N30, N31, N32, N33, N34, N35, N36, N37, N38, N39, N40, N41, N42, N43,
         N44, N45, N46, N47, N48, N49, N50, N51, N52, N53, N54, N55, N56, N57,
         N58, N59, N60, N61, N62, N63, N64, N65, N66, N67, N68, N69, N70, N71,
         N72, N73, N74, N75, N76, N77, N78, N79, N80, N81, N82, N83, N84, N85,
         N86, N87, N88, N89, N90, N91, N92, N93, N94, N95, N96, N97, N98, N99,
         N100, N101, N102, N103, N104, N105, N106, N107, counterEnables_0,
         counterEnables_1, configIn_mem1ra, N108, N109, config__mem1ra,
         configIn_mem1wd, N110, N111, config__mem1wd, configIn_mem1wa, N112,
         N113, config__mem1wa, configIn_mem0ra, N114, N115, config__mem0ra,
         configIn_mem0wd, N116, N117, config__mem0wd, configIn_mem0wa, N118,
         N119, config__mem0wa, counterChain_io_control_1_done,
         counterChain_io_control_0_done, N120, N121, N122, N123, N124, N125,
         N126, N127, N128, N129, N130, N131, N132, N133, N134, N135, N136,
         N137, N138, N139, N140, N141, N142, N143, N144, N145, N146, N147,
         N148, N149, N150, N151, N152, N153, N154, N155, N156, N157, N158,
         N159, N160, N161, N162, N163, N164, N165, N166, N167, N168, N169,
         N170, N171, N172, N173, N174, N175, N176, N177, N178, N179, N180,
         N181, N182, N183, N184, N185, N186, N187, N188, N189, N190, N191,
         N192, N193, N194, N195, N196, N197, N198, N199, N200, N201, N202,
         N203, N204, N205, N206, N207, N208, N209, net1485, net1486, net1487,
         net1488, net1489, SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2,
         SYNOPSYS_UNCONNECTED_3, SYNOPSYS_UNCONNECTED_4,
         SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6,
         SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8,
         SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10,
         SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12,
         SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14,
         SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16,
         SYNOPSYS_UNCONNECTED_17, SYNOPSYS_UNCONNECTED_18,
         SYNOPSYS_UNCONNECTED_19, SYNOPSYS_UNCONNECTED_20,
         SYNOPSYS_UNCONNECTED_21, SYNOPSYS_UNCONNECTED_22,
         SYNOPSYS_UNCONNECTED_23, SYNOPSYS_UNCONNECTED_24,
         SYNOPSYS_UNCONNECTED_25, SYNOPSYS_UNCONNECTED_26,
         SYNOPSYS_UNCONNECTED_27, SYNOPSYS_UNCONNECTED_28,
         SYNOPSYS_UNCONNECTED_29, SYNOPSYS_UNCONNECTED_30,
         SYNOPSYS_UNCONNECTED_31, SYNOPSYS_UNCONNECTED_32,
         SYNOPSYS_UNCONNECTED_33, SYNOPSYS_UNCONNECTED_34,
         SYNOPSYS_UNCONNECTED_35, SYNOPSYS_UNCONNECTED_36,
         SYNOPSYS_UNCONNECTED_37, SYNOPSYS_UNCONNECTED_38,
         SYNOPSYS_UNCONNECTED_39, SYNOPSYS_UNCONNECTED_40,
         SYNOPSYS_UNCONNECTED_41, SYNOPSYS_UNCONNECTED_42,
         SYNOPSYS_UNCONNECTED_43, SYNOPSYS_UNCONNECTED_44,
         SYNOPSYS_UNCONNECTED_45, SYNOPSYS_UNCONNECTED_46,
         SYNOPSYS_UNCONNECTED_47, SYNOPSYS_UNCONNECTED_48;
  wire   [1:0] configIn_pipeStage_1_opB_dataSrc;
  wire   [1:0] config__pipeStage_1_opB_dataSrc;
  wire   [2:0] T21;
  wire   [2:0] configIn_pipeStage_1_opB_value;
  wire   [1:0] configIn_pipeStage_1_opA_dataSrc;
  wire   [1:0] config__pipeStage_1_opA_dataSrc;
  wire   [2:0] T24;
  wire   [2:0] configIn_pipeStage_1_opA_value;
  wire   [5:0] configIn_pipeStage_1_result;
  wire   [5:0] config__pipeStage_1_result;
  wire   [3:0] configIn_pipeStage_1_opcode;
  wire   [3:0] config__pipeStage_1_opcode;
  wire   [1:0] configIn_pipeStage_0_opB_dataSrc;
  wire   [1:0] config__pipeStage_0_opB_dataSrc;
  wire   [2:0] T29;
  wire   [2:0] configIn_pipeStage_0_opB_value;
  wire   [1:0] configIn_pipeStage_0_opA_dataSrc;
  wire   [1:0] config__pipeStage_0_opA_dataSrc;
  wire   [2:0] T32;
  wire   [2:0] configIn_pipeStage_0_opA_value;
  wire   [5:0] configIn_pipeStage_0_result;
  wire   [5:0] config__pipeStage_0_result;
  wire   [3:0] configIn_pipeStage_0_opcode;
  wire   [3:0] config__pipeStage_0_opcode;
  wire   [1:0] configIn_remoteMux1;
  wire   [1:0] config__remoteMux1;
  wire   [1:0] configIn_remoteMux0;
  wire   [1:0] config__remoteMux0;
  wire   [3:0] T41;
  wire   [7:4] IntFU_io_out;
  wire   [3:0] mem0raMux_io_out_0;
  wire   [3:0] mem0waMux_io_out_0;
  wire   [7:0] mem0wdMux_io_out_0;
  wire   [7:0] mem0_io_rdata;
  wire   [3:0] mem1raMux_io_out_0;
  wire   [3:0] mem1waMux_io_out_0;
  wire   [7:0] mem1wdMux_io_out_0;
  wire   [7:0] mem1_io_rdata;
  wire   [7:0] counterChain_io_data_1_out;
  wire   [7:0] counterChain_io_data_0_out;
  wire   [7:0] remoteMux0_io_out;
  wire   [7:0] remoteMux1_io_out;
  wire   [7:0] MuxN_io_out;
  wire   [7:0] MuxN_1_io_out;
  wire   [7:0] RegisterBlock_io_readLocalA;
  wire   [7:0] RegisterBlock_io_readLocalB;
  wire   [7:0] RegisterBlock_io_readRemoteA;
  wire   [7:0] RegisterBlock_io_readRemoteB;
  wire   [7:0] RegisterBlock_io_passDataOut_3;
  wire   [7:0] RegisterBlock_io_passDataOut_2;
  wire   [7:0] RegisterBlock_io_passDataOut_1;
  wire   [7:0] RegisterBlock_io_passDataOut_0;
  wire   [7:0] MuxN_2_io_out;
  wire   [7:0] MuxN_3_io_out;
  wire   [7:0] RegisterBlock_1_io_readLocalA;
  wire   [7:0] RegisterBlock_1_io_readLocalB;
  assign io_scalarOut[7] = io_dataOut_0[7];
  assign io_scalarOut[6] = io_dataOut_0[6];
  assign io_scalarOut[5] = io_dataOut_0[5];
  assign io_scalarOut[4] = io_dataOut_0[4];
  assign io_scalarOut[3] = io_dataOut_0[3];
  assign io_scalarOut[2] = io_dataOut_0[2];
  assign io_scalarOut[1] = io_dataOut_0[1];
  assign io_scalarOut[0] = io_dataOut_0[0];

  SRAM_1 mem0 ( .clk(clk), .io_raddr(mem0raMux_io_out_0), .io_wen(1'b1), 
        .io_waddr(mem0waMux_io_out_0), .io_wdata(mem0wdMux_io_out_0), 
        .io_rdata(mem0_io_rdata) );
  MuxVec_0_1 mem0waMux ( .io_ins_1_0(io_dataOut_0[3:0]), .io_ins_0_0(T41), 
        .io_sel(config__mem0wa), .io_out_0(mem0waMux_io_out_0) );
  MuxVec_1_1 mem0wdMux ( .io_ins_1_0(io_dataIn_0), .io_ins_0_0(io_dataOut_0), 
        .io_sel(config__mem0wd), .io_out_0(mem0wdMux_io_out_0) );
  MuxVec_2_1 mem0raMux ( .io_ins_0_0(T41), .io_sel(config__mem0ra), .io_out_0(
        mem0raMux_io_out_0) );
  SRAM_0 mem1 ( .clk(clk), .io_raddr(mem1raMux_io_out_0), .io_wen(1'b1), 
        .io_waddr(mem1waMux_io_out_0), .io_wdata(mem1wdMux_io_out_0), 
        .io_rdata(mem1_io_rdata) );
  MuxVec_0_0 mem1waMux ( .io_ins_1_0(io_dataOut_0[3:0]), .io_ins_0_0(T41), 
        .io_sel(config__mem1wa), .io_out_0(mem1waMux_io_out_0) );
  MuxVec_1_0 mem1wdMux ( .io_ins_1_0(io_dataIn_0), .io_ins_0_0(io_dataOut_0), 
        .io_sel(config__mem1wd), .io_out_0(mem1wdMux_io_out_0) );
  MuxVec_2_0 mem1raMux ( .io_ins_0_0(T41), .io_sel(config__mem1ra), .io_out_0(
        mem1raMux_io_out_0) );
  CounterChain_0 counterChain ( .clk(clk), .reset(reset), .io_config_enable(
        net1489), .io_data_1_max({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0}), .io_data_1_stride({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0}), .io_data_1_out(counterChain_io_data_1_out), .io_data_0_max({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_0_stride({
        1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .io_data_0_out(
        counterChain_io_data_0_out), .io_control_1_enable(counterEnables_1), 
        .io_control_1_done(counterChain_io_control_1_done), 
        .io_control_0_enable(counterEnables_0), .io_control_0_done(
        counterChain_io_control_0_done) );
  CUControlBox_0 controlBlock ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_tokenIns_1(io_tokenIns_1), .io_tokenIns_0(
        io_tokenIns_0), .io_done_1(counterChain_io_control_1_done), 
        .io_done_0(counterChain_io_control_0_done), .io_tokenOuts_1(
        io_tokenOuts_1), .io_tokenOuts_0(io_tokenOuts_0), .io_enable_1(
        counterEnables_1), .io_enable_0(counterEnables_0) );
  MuxN_0_11 remoteMux0 ( .io_ins_3(mem1_io_rdata), .io_ins_2(mem0_io_rdata), 
        .io_ins_1(counterChain_io_data_1_out), .io_ins_0(
        counterChain_io_data_0_out), .io_sel(config__remoteMux0), .io_out(
        remoteMux0_io_out) );
  MuxN_0_10 remoteMux1 ( .io_ins_3(mem1_io_rdata), .io_ins_2(mem0_io_rdata), 
        .io_ins_1(counterChain_io_data_1_out), .io_ins_0(
        counterChain_io_data_0_out), .io_sel(config__remoteMux1), .io_out(
        remoteMux1_io_out) );
  IntFU_1 IntFU ( .io_a(MuxN_io_out), .io_b(MuxN_1_io_out), .io_opcode(
        config__pipeStage_0_opcode), .io_out({IntFU_io_out, T41}) );
  RegisterBlock_1 RegisterBlock ( .clk(clk), .reset(reset), .io_writeData({
        IntFU_io_out, T41}), .io_passData_3(mem1_io_rdata), .io_passData_2(
        mem0_io_rdata), .io_passData_1(counterChain_io_data_1_out), 
        .io_passData_0(counterChain_io_data_0_out), .io_writeSel(
        config__pipeStage_0_result), .io_readLocalASel(T32), 
        .io_readLocalBSel(T29), .io_readRemoteASel(T24[1:0]), 
        .io_readRemoteBSel(T21[1:0]), .io_readLocalA(
        RegisterBlock_io_readLocalA), .io_readLocalB(
        RegisterBlock_io_readLocalB), .io_readRemoteA(
        RegisterBlock_io_readRemoteA), .io_readRemoteB(
        RegisterBlock_io_readRemoteB), .io_passDataOut_3(
        RegisterBlock_io_passDataOut_3), .io_passDataOut_2(
        RegisterBlock_io_passDataOut_2), .io_passDataOut_1(
        RegisterBlock_io_passDataOut_1), .io_passDataOut_0(
        RegisterBlock_io_passDataOut_0) );
  MuxN_0_9 MuxN ( .io_ins_3(mem0_io_rdata), .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 
        1'b0, T32}), .io_ins_1(remoteMux0_io_out), .io_ins_0(
        RegisterBlock_io_readLocalA), .io_sel(config__pipeStage_0_opA_dataSrc), 
        .io_out(MuxN_io_out) );
  MuxN_0_8 MuxN_1 ( .io_ins_3(mem1_io_rdata), .io_ins_2({1'b0, 1'b0, 1'b0, 
        1'b0, 1'b0, T29}), .io_ins_1(remoteMux1_io_out), .io_ins_0(
        RegisterBlock_io_readLocalB), .io_sel(config__pipeStage_0_opB_dataSrc), 
        .io_out(MuxN_1_io_out) );
  IntFU_0 IntFU_1 ( .io_a(MuxN_2_io_out), .io_b(MuxN_3_io_out), .io_opcode(
        config__pipeStage_1_opcode), .io_out(io_dataOut_0) );
  RegisterBlock_0 RegisterBlock_1 ( .clk(clk), .reset(reset), .io_writeData(
        io_dataOut_0), .io_passData_3(RegisterBlock_io_passDataOut_3), 
        .io_passData_2(RegisterBlock_io_passDataOut_2), .io_passData_1(
        RegisterBlock_io_passDataOut_1), .io_passData_0(
        RegisterBlock_io_passDataOut_0), .io_writeSel(
        config__pipeStage_1_result), .io_readLocalASel(T24), 
        .io_readLocalBSel(T21), .io_readRemoteASel({net1485, net1486}), 
        .io_readRemoteBSel({net1487, net1488}), .io_readLocalA(
        RegisterBlock_1_io_readLocalA), .io_readLocalB(
        RegisterBlock_1_io_readLocalB), .io_readRemoteA({
        SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2, SYNOPSYS_UNCONNECTED_3, 
        SYNOPSYS_UNCONNECTED_4, SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6, 
        SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8}), .io_readRemoteB({
        SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10, 
        SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12, 
        SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14, 
        SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16}), .io_passDataOut_3(
        {SYNOPSYS_UNCONNECTED_17, SYNOPSYS_UNCONNECTED_18, 
        SYNOPSYS_UNCONNECTED_19, SYNOPSYS_UNCONNECTED_20, 
        SYNOPSYS_UNCONNECTED_21, SYNOPSYS_UNCONNECTED_22, 
        SYNOPSYS_UNCONNECTED_23, SYNOPSYS_UNCONNECTED_24}), .io_passDataOut_2(
        {SYNOPSYS_UNCONNECTED_25, SYNOPSYS_UNCONNECTED_26, 
        SYNOPSYS_UNCONNECTED_27, SYNOPSYS_UNCONNECTED_28, 
        SYNOPSYS_UNCONNECTED_29, SYNOPSYS_UNCONNECTED_30, 
        SYNOPSYS_UNCONNECTED_31, SYNOPSYS_UNCONNECTED_32}), .io_passDataOut_1(
        {SYNOPSYS_UNCONNECTED_33, SYNOPSYS_UNCONNECTED_34, 
        SYNOPSYS_UNCONNECTED_35, SYNOPSYS_UNCONNECTED_36, 
        SYNOPSYS_UNCONNECTED_37, SYNOPSYS_UNCONNECTED_38, 
        SYNOPSYS_UNCONNECTED_39, SYNOPSYS_UNCONNECTED_40}), .io_passDataOut_0(
        {SYNOPSYS_UNCONNECTED_41, SYNOPSYS_UNCONNECTED_42, 
        SYNOPSYS_UNCONNECTED_43, SYNOPSYS_UNCONNECTED_44, 
        SYNOPSYS_UNCONNECTED_45, SYNOPSYS_UNCONNECTED_46, 
        SYNOPSYS_UNCONNECTED_47, SYNOPSYS_UNCONNECTED_48}) );
  MuxN_1_1 MuxN_2 ( .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, T24}), .io_ins_1(
        RegisterBlock_io_readRemoteA), .io_ins_0(RegisterBlock_1_io_readLocalA), .io_sel(config__pipeStage_1_opA_dataSrc), .io_out(MuxN_2_io_out) );
  MuxN_1_0 MuxN_3 ( .io_ins_2({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, T21}), .io_ins_1(
        RegisterBlock_io_readRemoteB), .io_ins_0(RegisterBlock_1_io_readLocalB), .io_sel(config__pipeStage_1_opB_dataSrc), .io_out(MuxN_3_io_out) );
  \**SEQGEN**  config__pipeStage_1_opB_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N123), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opB_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N122), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opB_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N128), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N127), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opB_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N126), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T21[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N132), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opA_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N131), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_1_opA_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N137), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N136), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opA_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N135), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T24[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N145), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[5]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N144), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[4]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N143), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N142), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N141), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_result_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N140), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_result[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N151), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N150), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N149), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_1_opcode_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N148), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_1_opcode[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N155), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opB_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N154), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opB_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N160), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N159), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opB_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N158), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T29[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_dataSrc_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N164), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opA_dataSrc[1]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_dataSrc_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N163), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(config__pipeStage_0_opA_dataSrc[0]), .synch_clear(1'b0), 
        .synch_preset(1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_2_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N169), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[2]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_1_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N168), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opA_value_reg_0_ ( .clear(1'b0), .preset(
        1'b0), .next_state(N167), .clocked_on(clk), .data_in(1'b0), .enable(
        1'b0), .Q(T32[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_5_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N177), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[5]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_4_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N176), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[4]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N175), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N174), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N173), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_result_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N172), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_result[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_3_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N183), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[3]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_2_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N182), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[2]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N181), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[1]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__pipeStage_0_opcode_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N180), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__pipeStage_0_opcode[0]), .synch_clear(1'b0), .synch_preset(
        1'b0), .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux1_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N187), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux1[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux1_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N186), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux1[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux0_reg_1_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N191), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux0[1]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__remoteMux0_reg_0_ ( .clear(1'b0), .preset(1'b0), 
        .next_state(N190), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), 
        .Q(config__remoteMux0[0]), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1ra_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N194), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1ra), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1wd_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N197), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1wd), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem1wa_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N200), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem1wa), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0ra_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N203), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0ra), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0wd_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N206), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0wd), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  \**SEQGEN**  config__mem0wa_reg ( .clear(1'b0), .preset(1'b0), .next_state(
        N209), .clocked_on(clk), .data_in(1'b0), .enable(1'b0), .Q(
        config__mem0wa), .synch_clear(1'b0), .synch_preset(1'b0), 
        .synch_toggle(1'b0), .synch_enable(1'b1) );
  SELECT_OP C455 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opB_dataSrc), .CONTROL1(N0), .CONTROL2(N1), .Z(
        configIn_pipeStage_1_opB_dataSrc) );
  GTECH_BUF B_0 ( .A(N81), .Z(N0) );
  GTECH_BUF B_1 ( .A(N80), .Z(N1) );
  SELECT_OP C456 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T21), .CONTROL1(N2), 
        .CONTROL2(N3), .Z(configIn_pipeStage_1_opB_value) );
  GTECH_BUF B_2 ( .A(N83), .Z(N2) );
  GTECH_BUF B_3 ( .A(N82), .Z(N3) );
  SELECT_OP C457 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opA_dataSrc), .CONTROL1(N4), .CONTROL2(N5), .Z(
        configIn_pipeStage_1_opA_dataSrc) );
  GTECH_BUF B_4 ( .A(N85), .Z(N4) );
  GTECH_BUF B_5 ( .A(N84), .Z(N5) );
  SELECT_OP C458 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T24), .CONTROL1(N6), 
        .CONTROL2(N7), .Z(configIn_pipeStage_1_opA_value) );
  GTECH_BUF B_6 ( .A(N87), .Z(N6) );
  GTECH_BUF B_7 ( .A(N86), .Z(N7) );
  SELECT_OP C459 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_result), .CONTROL1(N8), .CONTROL2(N9), .Z(
        configIn_pipeStage_1_result) );
  GTECH_BUF B_8 ( .A(N89), .Z(N8) );
  GTECH_BUF B_9 ( .A(N88), .Z(N9) );
  SELECT_OP C460 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_1_opcode), .CONTROL1(N10), .CONTROL2(N11), .Z(
        configIn_pipeStage_1_opcode) );
  GTECH_BUF B_10 ( .A(N91), .Z(N10) );
  GTECH_BUF B_11 ( .A(N90), .Z(N11) );
  SELECT_OP C461 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opB_dataSrc), .CONTROL1(N12), .CONTROL2(N13), .Z(
        configIn_pipeStage_0_opB_dataSrc) );
  GTECH_BUF B_12 ( .A(N93), .Z(N12) );
  GTECH_BUF B_13 ( .A(N92), .Z(N13) );
  SELECT_OP C462 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T29), .CONTROL1(N14), 
        .CONTROL2(N15), .Z(configIn_pipeStage_0_opB_value) );
  GTECH_BUF B_14 ( .A(N95), .Z(N14) );
  GTECH_BUF B_15 ( .A(N94), .Z(N15) );
  SELECT_OP C463 ( .DATA1({1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opA_dataSrc), .CONTROL1(N16), .CONTROL2(N17), .Z(
        configIn_pipeStage_0_opA_dataSrc) );
  GTECH_BUF B_16 ( .A(N97), .Z(N16) );
  GTECH_BUF B_17 ( .A(N96), .Z(N17) );
  SELECT_OP C464 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(T32), .CONTROL1(N18), 
        .CONTROL2(N19), .Z(configIn_pipeStage_0_opA_value) );
  GTECH_BUF B_18 ( .A(N99), .Z(N18) );
  GTECH_BUF B_19 ( .A(N98), .Z(N19) );
  SELECT_OP C465 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_result), .CONTROL1(N20), .CONTROL2(N21), .Z(
        configIn_pipeStage_0_result) );
  GTECH_BUF B_20 ( .A(N101), .Z(N20) );
  GTECH_BUF B_21 ( .A(N100), .Z(N21) );
  SELECT_OP C466 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        config__pipeStage_0_opcode), .CONTROL1(N22), .CONTROL2(N23), .Z(
        configIn_pipeStage_0_opcode) );
  GTECH_BUF B_22 ( .A(N103), .Z(N22) );
  GTECH_BUF B_23 ( .A(N102), .Z(N23) );
  SELECT_OP C467 ( .DATA1({1'b0, 1'b0}), .DATA2(config__remoteMux1), 
        .CONTROL1(N24), .CONTROL2(N25), .Z(configIn_remoteMux1) );
  GTECH_BUF B_24 ( .A(N105), .Z(N24) );
  GTECH_BUF B_25 ( .A(N104), .Z(N25) );
  SELECT_OP C468 ( .DATA1({1'b0, 1'b0}), .DATA2(config__remoteMux0), 
        .CONTROL1(N26), .CONTROL2(N27), .Z(configIn_remoteMux0) );
  GTECH_BUF B_26 ( .A(N107), .Z(N26) );
  GTECH_BUF B_27 ( .A(N106), .Z(N27) );
  SELECT_OP C469 ( .DATA1(1'b0), .DATA2(config__mem1ra), .CONTROL1(N28), 
        .CONTROL2(N29), .Z(configIn_mem1ra) );
  GTECH_BUF B_28 ( .A(N109), .Z(N28) );
  GTECH_BUF B_29 ( .A(N108), .Z(N29) );
  SELECT_OP C470 ( .DATA1(1'b0), .DATA2(config__mem1wd), .CONTROL1(N30), 
        .CONTROL2(N31), .Z(configIn_mem1wd) );
  GTECH_BUF B_30 ( .A(N111), .Z(N30) );
  GTECH_BUF B_31 ( .A(N110), .Z(N31) );
  SELECT_OP C471 ( .DATA1(1'b0), .DATA2(config__mem1wa), .CONTROL1(N32), 
        .CONTROL2(N33), .Z(configIn_mem1wa) );
  GTECH_BUF B_32 ( .A(N113), .Z(N32) );
  GTECH_BUF B_33 ( .A(N112), .Z(N33) );
  SELECT_OP C472 ( .DATA1(1'b0), .DATA2(config__mem0ra), .CONTROL1(N34), 
        .CONTROL2(N35), .Z(configIn_mem0ra) );
  GTECH_BUF B_34 ( .A(N115), .Z(N34) );
  GTECH_BUF B_35 ( .A(N114), .Z(N35) );
  SELECT_OP C473 ( .DATA1(1'b0), .DATA2(config__mem0wd), .CONTROL1(N36), 
        .CONTROL2(N37), .Z(configIn_mem0wd) );
  GTECH_BUF B_36 ( .A(N117), .Z(N36) );
  GTECH_BUF B_37 ( .A(N116), .Z(N37) );
  SELECT_OP C474 ( .DATA1(1'b0), .DATA2(config__mem0wa), .CONTROL1(N38), 
        .CONTROL2(N39), .Z(configIn_mem0wa) );
  GTECH_BUF B_38 ( .A(N119), .Z(N38) );
  GTECH_BUF B_39 ( .A(N118), .Z(N39) );
  SELECT_OP C475 ( .DATA1({1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_1_opB_dataSrc), .CONTROL1(N40), .CONTROL2(N41), .Z(
        {N123, N122}) );
  GTECH_BUF B_40 ( .A(N121), .Z(N40) );
  GTECH_BUF B_41 ( .A(N120), .Z(N41) );
  SELECT_OP C476 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opB_value), .CONTROL1(N42), .CONTROL2(N43), .Z({
        N128, N127, N126}) );
  GTECH_BUF B_42 ( .A(N125), .Z(N42) );
  GTECH_BUF B_43 ( .A(N124), .Z(N43) );
  SELECT_OP C477 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opA_dataSrc), .CONTROL1(N44), .CONTROL2(N45), .Z(
        {N132, N131}) );
  GTECH_BUF B_44 ( .A(N130), .Z(N44) );
  GTECH_BUF B_45 ( .A(N129), .Z(N45) );
  SELECT_OP C478 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_1_opA_value), .CONTROL1(N46), .CONTROL2(N47), .Z({
        N137, N136, N135}) );
  GTECH_BUF B_46 ( .A(N134), .Z(N46) );
  GTECH_BUF B_47 ( .A(N133), .Z(N47) );
  SELECT_OP C479 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0, 1'b1, 1'b0}), .DATA2(
        configIn_pipeStage_1_result), .CONTROL1(N48), .CONTROL2(N49), .Z({N145, 
        N144, N143, N142, N141, N140}) );
  GTECH_BUF B_48 ( .A(N139), .Z(N48) );
  GTECH_BUF B_49 ( .A(N138), .Z(N49) );
  SELECT_OP C480 ( .DATA1({1'b0, 1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_1_opcode), .CONTROL1(N50), .CONTROL2(N51), .Z({N151, 
        N150, N149, N148}) );
  GTECH_BUF B_50 ( .A(N147), .Z(N50) );
  GTECH_BUF B_51 ( .A(N146), .Z(N51) );
  SELECT_OP C481 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opB_dataSrc), .CONTROL1(N52), .CONTROL2(N53), .Z(
        {N155, N154}) );
  GTECH_BUF B_52 ( .A(N153), .Z(N52) );
  GTECH_BUF B_53 ( .A(N152), .Z(N53) );
  SELECT_OP C482 ( .DATA1({1'b0, 1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opB_value), .CONTROL1(N54), .CONTROL2(N55), .Z({
        N160, N159, N158}) );
  GTECH_BUF B_54 ( .A(N157), .Z(N54) );
  GTECH_BUF B_55 ( .A(N156), .Z(N55) );
  SELECT_OP C483 ( .DATA1({1'b0, 1'b1}), .DATA2(
        configIn_pipeStage_0_opA_dataSrc), .CONTROL1(N56), .CONTROL2(N57), .Z(
        {N164, N163}) );
  GTECH_BUF B_56 ( .A(N162), .Z(N56) );
  GTECH_BUF B_57 ( .A(N161), .Z(N57) );
  SELECT_OP C484 ( .DATA1({1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_0_opA_value), .CONTROL1(N58), .CONTROL2(N59), .Z({
        N169, N168, N167}) );
  GTECH_BUF B_58 ( .A(N166), .Z(N58) );
  GTECH_BUF B_59 ( .A(N165), .Z(N59) );
  SELECT_OP C485 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b0, 1'b0, 1'b0}), .DATA2(
        configIn_pipeStage_0_result), .CONTROL1(N60), .CONTROL2(N61), .Z({N177, 
        N176, N175, N174, N173, N172}) );
  GTECH_BUF B_60 ( .A(N171), .Z(N60) );
  GTECH_BUF B_61 ( .A(N170), .Z(N61) );
  SELECT_OP C486 ( .DATA1({1'b0, 1'b0, 1'b1, 1'b0}), .DATA2(
        configIn_pipeStage_0_opcode), .CONTROL1(N62), .CONTROL2(N63), .Z({N183, 
        N182, N181, N180}) );
  GTECH_BUF B_62 ( .A(N179), .Z(N62) );
  GTECH_BUF B_63 ( .A(N178), .Z(N63) );
  SELECT_OP C487 ( .DATA1({1'b0, 1'b1}), .DATA2(configIn_remoteMux1), 
        .CONTROL1(N64), .CONTROL2(N65), .Z({N187, N186}) );
  GTECH_BUF B_64 ( .A(N185), .Z(N64) );
  GTECH_BUF B_65 ( .A(N184), .Z(N65) );
  SELECT_OP C488 ( .DATA1({1'b0, 1'b0}), .DATA2(configIn_remoteMux0), 
        .CONTROL1(N66), .CONTROL2(N67), .Z({N191, N190}) );
  GTECH_BUF B_66 ( .A(N189), .Z(N66) );
  GTECH_BUF B_67 ( .A(N188), .Z(N67) );
  SELECT_OP C489 ( .DATA1(1'b0), .DATA2(configIn_mem1ra), .CONTROL1(N68), 
        .CONTROL2(N69), .Z(N194) );
  GTECH_BUF B_68 ( .A(N193), .Z(N68) );
  GTECH_BUF B_69 ( .A(N192), .Z(N69) );
  SELECT_OP C490 ( .DATA1(1'b0), .DATA2(configIn_mem1wd), .CONTROL1(N70), 
        .CONTROL2(N71), .Z(N197) );
  GTECH_BUF B_70 ( .A(N196), .Z(N70) );
  GTECH_BUF B_71 ( .A(N195), .Z(N71) );
  SELECT_OP C491 ( .DATA1(1'b0), .DATA2(configIn_mem1wa), .CONTROL1(N72), 
        .CONTROL2(N73), .Z(N200) );
  GTECH_BUF B_72 ( .A(N199), .Z(N72) );
  GTECH_BUF B_73 ( .A(N198), .Z(N73) );
  SELECT_OP C492 ( .DATA1(1'b0), .DATA2(configIn_mem0ra), .CONTROL1(N74), 
        .CONTROL2(N75), .Z(N203) );
  GTECH_BUF B_74 ( .A(N202), .Z(N74) );
  GTECH_BUF B_75 ( .A(N201), .Z(N75) );
  SELECT_OP C493 ( .DATA1(1'b0), .DATA2(configIn_mem0wd), .CONTROL1(N76), 
        .CONTROL2(N77), .Z(N206) );
  GTECH_BUF B_76 ( .A(N205), .Z(N76) );
  GTECH_BUF B_77 ( .A(N204), .Z(N77) );
  SELECT_OP C494 ( .DATA1(1'b0), .DATA2(configIn_mem0wa), .CONTROL1(N78), 
        .CONTROL2(N79), .Z(N209) );
  GTECH_BUF B_78 ( .A(N208), .Z(N78) );
  GTECH_BUF B_79 ( .A(N207), .Z(N79) );
  GTECH_NOT I_0 ( .A(io_config_enable), .Z(N80) );
  GTECH_BUF B_80 ( .A(io_config_enable), .Z(N81) );
  GTECH_NOT I_1 ( .A(io_config_enable), .Z(N82) );
  GTECH_BUF B_81 ( .A(io_config_enable), .Z(N83) );
  GTECH_NOT I_2 ( .A(io_config_enable), .Z(N84) );
  GTECH_BUF B_82 ( .A(io_config_enable), .Z(N85) );
  GTECH_NOT I_3 ( .A(io_config_enable), .Z(N86) );
  GTECH_BUF B_83 ( .A(io_config_enable), .Z(N87) );
  GTECH_NOT I_4 ( .A(io_config_enable), .Z(N88) );
  GTECH_BUF B_84 ( .A(io_config_enable), .Z(N89) );
  GTECH_NOT I_5 ( .A(io_config_enable), .Z(N90) );
  GTECH_BUF B_85 ( .A(io_config_enable), .Z(N91) );
  GTECH_NOT I_6 ( .A(io_config_enable), .Z(N92) );
  GTECH_BUF B_86 ( .A(io_config_enable), .Z(N93) );
  GTECH_NOT I_7 ( .A(io_config_enable), .Z(N94) );
  GTECH_BUF B_87 ( .A(io_config_enable), .Z(N95) );
  GTECH_NOT I_8 ( .A(io_config_enable), .Z(N96) );
  GTECH_BUF B_88 ( .A(io_config_enable), .Z(N97) );
  GTECH_NOT I_9 ( .A(io_config_enable), .Z(N98) );
  GTECH_BUF B_89 ( .A(io_config_enable), .Z(N99) );
  GTECH_NOT I_10 ( .A(io_config_enable), .Z(N100) );
  GTECH_BUF B_90 ( .A(io_config_enable), .Z(N101) );
  GTECH_NOT I_11 ( .A(io_config_enable), .Z(N102) );
  GTECH_BUF B_91 ( .A(io_config_enable), .Z(N103) );
  GTECH_NOT I_12 ( .A(io_config_enable), .Z(N104) );
  GTECH_BUF B_92 ( .A(io_config_enable), .Z(N105) );
  GTECH_NOT I_13 ( .A(io_config_enable), .Z(N106) );
  GTECH_BUF B_93 ( .A(io_config_enable), .Z(N107) );
  GTECH_NOT I_14 ( .A(io_config_enable), .Z(N108) );
  GTECH_BUF B_94 ( .A(io_config_enable), .Z(N109) );
  GTECH_NOT I_15 ( .A(io_config_enable), .Z(N110) );
  GTECH_BUF B_95 ( .A(io_config_enable), .Z(N111) );
  GTECH_NOT I_16 ( .A(io_config_enable), .Z(N112) );
  GTECH_BUF B_96 ( .A(io_config_enable), .Z(N113) );
  GTECH_NOT I_17 ( .A(io_config_enable), .Z(N114) );
  GTECH_BUF B_97 ( .A(io_config_enable), .Z(N115) );
  GTECH_NOT I_18 ( .A(io_config_enable), .Z(N116) );
  GTECH_BUF B_98 ( .A(io_config_enable), .Z(N117) );
  GTECH_NOT I_19 ( .A(io_config_enable), .Z(N118) );
  GTECH_BUF B_99 ( .A(io_config_enable), .Z(N119) );
  GTECH_NOT I_20 ( .A(reset), .Z(N120) );
  GTECH_BUF B_100 ( .A(reset), .Z(N121) );
  GTECH_NOT I_21 ( .A(reset), .Z(N124) );
  GTECH_BUF B_101 ( .A(reset), .Z(N125) );
  GTECH_NOT I_22 ( .A(reset), .Z(N129) );
  GTECH_BUF B_102 ( .A(reset), .Z(N130) );
  GTECH_NOT I_23 ( .A(reset), .Z(N133) );
  GTECH_BUF B_103 ( .A(reset), .Z(N134) );
  GTECH_NOT I_24 ( .A(reset), .Z(N138) );
  GTECH_BUF B_104 ( .A(reset), .Z(N139) );
  GTECH_NOT I_25 ( .A(reset), .Z(N146) );
  GTECH_BUF B_105 ( .A(reset), .Z(N147) );
  GTECH_NOT I_26 ( .A(reset), .Z(N152) );
  GTECH_BUF B_106 ( .A(reset), .Z(N153) );
  GTECH_NOT I_27 ( .A(reset), .Z(N156) );
  GTECH_BUF B_107 ( .A(reset), .Z(N157) );
  GTECH_NOT I_28 ( .A(reset), .Z(N161) );
  GTECH_BUF B_108 ( .A(reset), .Z(N162) );
  GTECH_NOT I_29 ( .A(reset), .Z(N165) );
  GTECH_BUF B_109 ( .A(reset), .Z(N166) );
  GTECH_NOT I_30 ( .A(reset), .Z(N170) );
  GTECH_BUF B_110 ( .A(reset), .Z(N171) );
  GTECH_NOT I_31 ( .A(reset), .Z(N178) );
  GTECH_BUF B_111 ( .A(reset), .Z(N179) );
  GTECH_NOT I_32 ( .A(reset), .Z(N184) );
  GTECH_BUF B_112 ( .A(reset), .Z(N185) );
  GTECH_NOT I_33 ( .A(reset), .Z(N188) );
  GTECH_BUF B_113 ( .A(reset), .Z(N189) );
  GTECH_NOT I_34 ( .A(reset), .Z(N192) );
  GTECH_BUF B_114 ( .A(reset), .Z(N193) );
  GTECH_NOT I_35 ( .A(reset), .Z(N195) );
  GTECH_BUF B_115 ( .A(reset), .Z(N196) );
  GTECH_NOT I_36 ( .A(reset), .Z(N198) );
  GTECH_BUF B_116 ( .A(reset), .Z(N199) );
  GTECH_NOT I_37 ( .A(reset), .Z(N201) );
  GTECH_BUF B_117 ( .A(reset), .Z(N202) );
  GTECH_NOT I_38 ( .A(reset), .Z(N204) );
  GTECH_BUF B_118 ( .A(reset), .Z(N205) );
  GTECH_NOT I_39 ( .A(reset), .Z(N207) );
  GTECH_BUF B_119 ( .A(reset), .Z(N208) );
endmodule


module Plasticine ( clk, reset, io_config_enable, io_command, io_status );
  input clk, reset, io_config_enable, io_command;
  output io_status;
  wire   cu1_io_tokenOuts_0, controlBox_io_startTokenOut, cu0_io_tokenOuts_1,
         cu0_io_tokenOuts_0, SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2,
         SYNOPSYS_UNCONNECTED_3, SYNOPSYS_UNCONNECTED_4,
         SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6,
         SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8,
         SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10,
         SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12,
         SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14,
         SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16;
  wire   [7:0] cu1_io_dataOut_0;
  wire   [7:0] cu0_io_dataOut_0;

  MainControlBox controlBox ( .clk(clk), .reset(reset), .io_command(io_command), .io_doneTokenIn(cu1_io_tokenOuts_0), .io_startTokenOut(
        controlBox_io_startTokenOut), .io_statusOut(io_status) );
  ComputeUnit_1 cu0 ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_tokenIns_1(1'b0), .io_tokenIns_0(
        controlBox_io_startTokenOut), .io_tokenOuts_1(cu0_io_tokenOuts_1), 
        .io_tokenOuts_0(cu0_io_tokenOuts_0), .io_scalarOut({
        SYNOPSYS_UNCONNECTED_1, SYNOPSYS_UNCONNECTED_2, SYNOPSYS_UNCONNECTED_3, 
        SYNOPSYS_UNCONNECTED_4, SYNOPSYS_UNCONNECTED_5, SYNOPSYS_UNCONNECTED_6, 
        SYNOPSYS_UNCONNECTED_7, SYNOPSYS_UNCONNECTED_8}), .io_dataIn_0(
        cu1_io_dataOut_0), .io_dataOut_0(cu0_io_dataOut_0) );
  ComputeUnit_0 cu1 ( .clk(clk), .reset(reset), .io_config_enable(
        io_config_enable), .io_tokenIns_1(cu0_io_tokenOuts_1), .io_tokenIns_0(
        cu0_io_tokenOuts_0), .io_tokenOuts_0(cu1_io_tokenOuts_0), 
        .io_scalarOut({SYNOPSYS_UNCONNECTED_9, SYNOPSYS_UNCONNECTED_10, 
        SYNOPSYS_UNCONNECTED_11, SYNOPSYS_UNCONNECTED_12, 
        SYNOPSYS_UNCONNECTED_13, SYNOPSYS_UNCONNECTED_14, 
        SYNOPSYS_UNCONNECTED_15, SYNOPSYS_UNCONNECTED_16}), .io_dataIn_0(
        cu0_io_dataOut_0), .io_dataOut_0(cu1_io_dataOut_0) );
endmodule

