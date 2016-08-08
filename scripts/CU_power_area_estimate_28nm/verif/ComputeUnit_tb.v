module ComputeUnit_tb;
	reg clk, reset, io_enable, io_config_enable;
	wire io_done;
	wire [6:0] io_scalarOut;
	wire io_rmux0;
	wire io_rmux1;
	wire [6:0] io_opcode;
	wire io_opA_isLocal;
	wire [6:0] io_opA_local;
	wire [6:0] io_opA_remote;
	wire io_opB_isLocal;
	wire [6:0] io_opB_local;
	wire [6:0] io_opB_remote;
	wire [6:0] io_result;

	ComputeUnit CU0(.clk (clk),
			        .reset (reset),
			        .io_enable (io_enable),
			        .io_config_enable (io_config_enable),
			        .io_done (io_done),
			        .io_scalarOut (io_scalarOut),
			        .io_rmux0 (io_rmux0),
			        .io_rmux1 (io_rmux1),
			        .io_opcode (io_opcode),
			        .io_opA_isLocal (io_opA_isLocal),
			        .io_opA_local (io_opA_local),
			        .io_opA_remote (io_opA_remote),
			        .io_opB_isLocal (io_opB_isLocal),
			        .io_opB_local (io_opB_local),
			        .io_opB_remote (io_opB_remote),
			        .io_result (io_result));
	initial begin
		clk = 0;
		reset = 0;
		io_enable = 1;
		io_config_enable = 1;
		#10 reset = 1;
		#20 reset = 0;
	end

	always
		#5 clk = !clk;
	initial begin
		$dumpfile ("trace.vcd");
		$dumpvars;
	end
	initial begin
		$display("time, \t clk, \t reset, \t io_enable, \t io_config_enable, \t io_result");
		$monitor("%d,\t%b, \t%b, \t%b, \t%b", $time, clk, reset, io_enable, io_config_enable, io_result);
	end

	initial
		#100 $finish;
endmodule
