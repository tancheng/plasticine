`define W 32
module top (
  input wire clk,
  input wire reset,
  input wire [`W-1:0] inorth,
  input wire [`W-1:0] iwest,
  input wire [`W-1:0] isouth,
  input wire [`W-1:0] ieast,
  output wire [`W-1:0] onorth,
  output wire [`W-1:0] owest,
  output wire [`W-1:0] osouth,
  output wire [`W-1:0] oeast
);

  sbox #(`W) s1(clk, reset, inorth, iwest, isouth, ieast, onorth, owest, osouth, oeast);
endmodule
