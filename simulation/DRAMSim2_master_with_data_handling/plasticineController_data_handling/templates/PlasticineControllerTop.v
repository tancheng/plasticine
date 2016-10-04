module PlasticineController(input clk, input reset,
    input  io_start,
    input  io_tx_comp,
    output io_tx_enq,
    output io_done
);

  wire T0;
  reg [1:0] state;
  wire[1:0] T26;
  wire[1:0] T1;
  wire[1:0] T2;
  wire[1:0] T3;
  wire[1:0] T4;
  wire[1:0] T5;
  wire[1:0] T6;
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

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    state = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_done = T0;
  assign T0 = state == 2'h3;
  assign T26 = reset ? 2'h0 : T1;
  assign T1 = T21 ? 2'h0 : T2;
  assign T2 = T19 ? 2'h2 : T3;
  assign T3 = T14 ? 2'h3 : T4;
  assign T4 = T11 ? 2'h2 : T5;
  assign T5 = T9 ? 2'h0 : T6;
  assign T6 = T7 ? 2'h1 : state;
  assign T7 = T8 & io_start;
  assign T8 = state == 2'h0;
  assign T9 = T8 & T10;
  assign T10 = io_start ^ 1'h1;
  assign T11 = T13 & T12;
  assign T12 = state == 2'h1;
  assign T13 = T8 ^ 1'h1;
  assign T14 = T15 & io_tx_comp;
  assign T15 = T17 & T16;
  assign T16 = state == 2'h2;
  assign T17 = T18 ^ 1'h1;
  assign T18 = T8 | T12;
  assign T19 = T15 & T20;
  assign T20 = io_tx_comp ^ 1'h1;
  assign T21 = T23 & T22;
  assign T22 = state == 2'h3;
  assign T23 = T24 ^ 1'h1;
  assign T24 = T18 | T16;
  assign io_tx_enq = T25;
  assign T25 = state == 2'h1;

  always @(posedge clk) begin
    if(reset) begin
      state <= 2'h0;
    end else if(T21) begin
      state <= 2'h0;
    end else if(T19) begin
      state <= 2'h2;
    end else if(T14) begin
      state <= 2'h3;
    end else if(T11) begin
      state <= 2'h2;
    end else if(T9) begin
      state <= 2'h0;
    end else if(T7) begin
      state <= 2'h1;
    end
  end
endmodule

module PlasticineControllerTop(input clk, input reset,
    input  io_start,
    output io_done,
    input  io_isWR,
    output io_isWROut,
    input [63:0] io_addr,
    output[63:0] io_addrOut
);

  wire controller_io_tx_enq;
  wire controller_io_done;
  wire dramsim_TX_COMP;
  wire dramsim_IS_WR_OUT;
  wire[63:0] dramsim_ADDR_OUT;


  assign io_addrOut = dramsim_ADDR_OUT;
  assign io_isWROut = dramsim_IS_WR_OUT;
  assign io_done = controller_io_done;
  PlasticineController controller(.clk(clk), .reset(reset),
       .io_start( io_start ),
       .io_tx_comp( dramsim_TX_COMP ),
       .io_tx_enq( controller_io_tx_enq ),
       .io_done( controller_io_done )
  );
  DRAMSim2 dramsim(
       .TX_ENQ( controller_io_tx_enq ),
       .TX_COMP( dramsim_TX_COMP ),
       .IS_WR( io_isWR ),
       .IS_WR_OUT( dramsim_IS_WR_OUT ),
       .ADDR( io_addr ),
       .ADDR_OUT( dramsim_ADDR_OUT )
  );
endmodule

