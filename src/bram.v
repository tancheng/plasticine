module bram
#(
    parameter SIZE = 1024,
    parameter AWIDTH = 10,
    parameter DWIDTH = 32)
(
    input clk,
    input [AWIDTH-1:0] addr,
    input we,
    input [DWIDTH-1:0] din,
    output reg [DWIDTH-1:0] dout
);

    reg [DWIDTH-1:0] mem [0:SIZE-1];

    always @(posedge clk) begin
        if (we)
            mem[addr] <= din;
        dout <= mem[addr];
    end

endmodule
