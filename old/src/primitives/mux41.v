module mux41 #(parameter WIDTH=32) (
  input wire [1:0] sel,
  input wire [WIDTH-1:0] i0,
  input wire [WIDTH-1:0] i1,
  input wire [WIDTH-1:0] i2,
  input wire [WIDTH-1:0] i3,
  output reg [WIDTH-1:0] out
);
  always @(*) begin
    case (sel)
      2'b00: out = i0;
      2'b01: out = i1;
      2'b10: out = i2;
      2'b11: out = i3;
      default: out = 'b0;
    endcase
  end
endmodule
