module sbox #(parameter WIDTH=32) (
  input wire clk,
  input wire reset,
  input wire [WIDTH-1:0] inorth,
  input wire [WIDTH-1:0] iwest,
  input wire [WIDTH-1:0] isouth,
  input wire [WIDTH-1:0] ieast,
  output wire [WIDTH-1:0] onorth,
  output wire [WIDTH-1:0] owest,
  output wire [WIDTH-1:0] osouth,
  output wire [WIDTH-1:0] oeast
);

  // Configuration
  wire cfg = 1'b1;
  wire [1:0] dn = 2'b00;
  wire [1:0] dw = 2'b01;
  wire [1:0] ds = 2'b10;
  wire [1:0] de = 2'b11;

  wire [1:0] qn;
  wire [1:0] qw;
  wire [1:0] qs;
  wire [1:0] qe;
  dffare #(2) cnorth (clk, reset, cfg, dn, qn);
  dffare #(2) cwest (clk, reset, cfg, dw, qw);
  dffare #(2) csouth (clk, reset, cfg, ds, qs);
  dffare #(2) ceast (clk, reset, cfg, de, qe);

  // Datapath
  mux41 n(qn, inorth, iwest, isouth, ieast, onorth);
  mux41 w(qw, inorth, iwest, isouth, ieast, owest);
  mux41 s(qs, inorth, iwest, isouth, ieast, osouth);
  mux41 e(qe, inorth, iwest, isouth, ieast, oeast);
endmodule
