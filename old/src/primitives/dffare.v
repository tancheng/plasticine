// dffare: D flip-flop with active high enable and asynchronous reset
// Parametrized width; default of 1
module dffare #(parameter WIDTH = 1) (
    input clk,
    input r,
    input en,
    input [WIDTH-1:0] d,
    output reg [WIDTH-1:0] q
);

    always @(posedge clk or posedge r)
        if (r)
            q <= {WIDTH{1'b0}};
        else if (en)
            q <= d;
        else
            q <= q;

endmodule
