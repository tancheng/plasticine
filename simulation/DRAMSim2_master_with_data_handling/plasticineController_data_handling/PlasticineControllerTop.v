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

module IsWrFifo(input clk, input reset,
    input  io_enq_val,
    output io_enq_rdy,
    output io_deq_val,
    input  io_deq_rdy,
    input  io_enq_dat,
    output io_deq_dat
);

  wire T14;
  wire[63:0] T0;
  reg [63:0] ram [3:0];
  wire[63:0] T1;
  wire[63:0] T15;
  wire do_enq;
  reg [1:0] enq_ptr;
  wire[1:0] T16;
  wire[1:0] T2;
  wire[1:0] enq_ptr_inc;
  reg [1:0] deq_ptr;
  wire[1:0] T17;
  wire[1:0] T3;
  wire[1:0] deq_ptr_inc;
  wire do_deq;
  wire T4;
  wire is_empty;
  wire T5;
  wire T6;
  reg  is_full;
  wire T18;
  wire is_full_next;
  wire T7;
  wire T8;
  wire T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    for (initvar = 0; initvar < 4; initvar = initvar+1)
      ram[initvar] = {2{$random}};
    enq_ptr = {1{$random}};
    deq_ptr = {1{$random}};
    is_full = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_deq_dat = T14;
  assign T14 = T0[0];
  assign T0 = ram[deq_ptr];
  assign T15 = {63'h0, io_enq_dat};
  assign do_enq = io_enq_rdy & io_enq_val;
  assign T16 = reset ? 2'h0 : T2;
  assign T2 = do_enq ? enq_ptr_inc : enq_ptr;
  assign enq_ptr_inc = enq_ptr + 2'h1;
  assign T17 = reset ? 2'h0 : T3;
  assign T3 = do_deq ? deq_ptr_inc : deq_ptr;
  assign deq_ptr_inc = deq_ptr + 2'h1;
  assign do_deq = io_deq_rdy & io_deq_val;
  assign io_deq_val = T4;
  assign T4 = is_empty ^ 1'h1;
  assign is_empty = T6 & T5;
  assign T5 = enq_ptr == deq_ptr;
  assign T6 = is_full ^ 1'h1;
  assign T18 = reset ? 1'h0 : is_full_next;
  assign is_full_next = T9 ? 1'h1 : T7;
  assign T7 = T8 ? 1'h0 : is_full;
  assign T8 = do_deq & is_full;
  assign T9 = T11 & T10;
  assign T10 = enq_ptr_inc == deq_ptr;
  assign T11 = do_enq & T12;
  assign T12 = ~ do_deq;
  assign io_enq_rdy = T13;
  assign T13 = is_full ^ 1'h1;

  always @(posedge clk) begin
    if (do_enq)
      ram[enq_ptr] <= T15;
    if(reset) begin
      enq_ptr <= 2'h0;
    end else if(do_enq) begin
      enq_ptr <= enq_ptr_inc;
    end
    if(reset) begin
      deq_ptr <= 2'h0;
    end else if(do_deq) begin
      deq_ptr <= deq_ptr_inc;
    end
    if(reset) begin
      is_full <= 1'h0;
    end else if(T9) begin
      is_full <= 1'h1;
    end else if(T8) begin
      is_full <= 1'h0;
    end
  end
endmodule

module AddrFifo(input clk, input reset,
    input  io_enq_val,
    output io_enq_rdy,
    output io_deq_val,
    input  io_deq_rdy,
    input [31:0] io_enq_dat,
    output[31:0] io_deq_dat
);

  wire[31:0] T14;
  wire[63:0] T0;
  reg [63:0] ram [3:0];
  wire[63:0] T1;
  wire[63:0] T15;
  wire do_enq;
  reg [1:0] enq_ptr;
  wire[1:0] T16;
  wire[1:0] T2;
  wire[1:0] enq_ptr_inc;
  reg [1:0] deq_ptr;
  wire[1:0] T17;
  wire[1:0] T3;
  wire[1:0] deq_ptr_inc;
  wire do_deq;
  wire T4;
  wire is_empty;
  wire T5;
  wire T6;
  reg  is_full;
  wire T18;
  wire is_full_next;
  wire T7;
  wire T8;
  wire T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    for (initvar = 0; initvar < 4; initvar = initvar+1)
      ram[initvar] = {2{$random}};
    enq_ptr = {1{$random}};
    deq_ptr = {1{$random}};
    is_full = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_deq_dat = T14;
  assign T14 = T0[31:0];
  assign T0 = ram[deq_ptr];
  assign T15 = {32'h0, io_enq_dat};
  assign do_enq = io_enq_rdy & io_enq_val;
  assign T16 = reset ? 2'h0 : T2;
  assign T2 = do_enq ? enq_ptr_inc : enq_ptr;
  assign enq_ptr_inc = enq_ptr + 2'h1;
  assign T17 = reset ? 2'h0 : T3;
  assign T3 = do_deq ? deq_ptr_inc : deq_ptr;
  assign deq_ptr_inc = deq_ptr + 2'h1;
  assign do_deq = io_deq_rdy & io_deq_val;
  assign io_deq_val = T4;
  assign T4 = is_empty ^ 1'h1;
  assign is_empty = T6 & T5;
  assign T5 = enq_ptr == deq_ptr;
  assign T6 = is_full ^ 1'h1;
  assign T18 = reset ? 1'h0 : is_full_next;
  assign is_full_next = T9 ? 1'h1 : T7;
  assign T7 = T8 ? 1'h0 : is_full;
  assign T8 = do_deq & is_full;
  assign T9 = T11 & T10;
  assign T10 = enq_ptr_inc == deq_ptr;
  assign T11 = do_enq & T12;
  assign T12 = ~ do_deq;
  assign io_enq_rdy = T13;
  assign T13 = is_full ^ 1'h1;

  always @(posedge clk) begin
    if (do_enq)
      ram[enq_ptr] <= T15;
    if(reset) begin
      enq_ptr <= 2'h0;
    end else if(do_enq) begin
      enq_ptr <= enq_ptr_inc;
    end
    if(reset) begin
      deq_ptr <= 2'h0;
    end else if(do_deq) begin
      deq_ptr <= deq_ptr_inc;
    end
    if(reset) begin
      is_full <= 1'h0;
    end else if(T9) begin
      is_full <= 1'h1;
    end else if(T8) begin
      is_full <= 1'h0;
    end
  end
endmodule

module PlasticineControllerTop(input clk, input reset,
    input  io_enq_val,
    output io_enq_rdy,
    input  io_isWR,
    input [63:0] io_addr
);

  wire[31:0] T1;
  wire T0;
  wire controller_io_tx_enq;
  wire controller_io_done;
  wire dramsim_TX_COMP;
  wire isWrFifo_io_deq_val;
  wire addrFifo_io_deq_val;


`ifndef SYNTHESIS
// synthesis translate_off
//  assign io_enq_rdy = {1{$random}};
// synthesis translate_on
`endif
  assign T1 = io_addr[31:0];
  assign T0 = isWrFifo_io_deq_val & addrFifo_io_deq_val;
  PlasticineController controller(.clk(clk), .reset(reset),
       .io_start( T0 ),
       .io_tx_comp( dramsim_TX_COMP ),
       .io_tx_enq( controller_io_tx_enq ),
       .io_done( controller_io_done )
  );
  DRAMSim2 dramsim(
       .TX_ENQ( controller_io_tx_enq ),
       .TX_COMP( dramsim_TX_COMP ),
       .IS_WR( io_isWR ),
       .ADDR( io_addr )
  );
  IsWrFifo isWrFifo(.clk(clk), .reset(reset),
       .io_enq_val( io_enq_val ),
       //.io_enq_rdy(  )
       .io_deq_val( isWrFifo_io_deq_val ),
       .io_deq_rdy( controller_io_done ),
       .io_enq_dat( io_isWR )
       //.io_deq_dat(  )
  );
  AddrFifo addrFifo(.clk(clk), .reset(reset),
       .io_enq_val( io_enq_val ),
       //.io_enq_rdy(  )
       .io_deq_val( addrFifo_io_deq_val ),
       .io_deq_rdy( controller_io_done ),
       .io_enq_dat( T1 )
       //.io_deq_dat(  )
  );
endmodule

