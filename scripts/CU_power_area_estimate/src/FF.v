module FF(input clk, input reset,
    input [3:0] io_data_in,
    input [3:0] io_data_init,
    output[3:0] io_data_out,
    input  io_control_enable
);

  reg [3:0] ff;
  wire[3:0] T1;
  wire[3:0] d;
  wire[3:0] T0;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    ff = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_data_out = ff;
  assign T1 = reset ? io_data_init : d;
  assign d = T0;
  assign T0 = io_control_enable ? io_data_in : ff;

  always @(posedge clk) begin
    if(reset) begin
      ff <= io_data_init;
    end else begin
      ff <= d;
    end
  end
endmodule

