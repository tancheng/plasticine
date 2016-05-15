module alu #(parameter WIDTH=32) (
  input wire clk,
  input wire reset,
  input wire [WIDTH-1:0] in1,
  input wire [WIDTH-1:0] in2,
  output wire [WIDTH-1:0] out
);

  // Configuration
  wire cfg = 1'b1;
  wire [1:0] d = 2'b00;

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
