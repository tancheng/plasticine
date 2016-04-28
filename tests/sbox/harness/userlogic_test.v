`timescale 1 ns / 10 ps

module userlogic_test();
    reg clk;
    reg rst;

    // User logic command, status, and test registers
    reg  [31:0] ul_command = 2;

    // Testbench parameters
    integer write_test_result;
    integer dump_vars;
    reg  [1024*8:1] dump_vars_filename;


    // Used to dump output buffer to file
    integer i, outfile;
    reg  [32:0] out_start_addr = 0;

    // Set up clock
    initial forever begin
        #5 clk = ~clk;
    end

    // Instantiating DUT
    reg [31:0] inorth = 'd100;
    reg [31:0] iwest = 'd200;
    reg [31:0] isouth = 'd300;
    reg [31:0] ieast = 'd400;

    wire [31:0] onorth;
    wire [31:0] owest;
    wire [31:0] osouth;
    wire [31:0] oeast;
    top ul (
        .clk(clk),
        .reset(rst),
        .inorth(inorth),
        .iwest(iwest),
        .isouth(isouth),
        .ieast(ieast),
        .onorth(onorth),
        .owest(owest),
        .osouth(osouth),
        .oeast(oeast)
    );

    // Initialize testbench parameters
    integer result;
    reg  [32:1] num_cycles = 32'hFFFFFFFF;
    reg  [32:1] cycle_count = 0;
    initial begin
        dump_vars = $value$plusargs("dumpvars=%s", dump_vars_filename);
        if ($test$plusargs("cycles")) begin
            result = $value$plusargs("cycles=%d", num_cycles);
        end
        $display("Running userlogic for maximum of %0d cycles", num_cycles);
    end

    initial begin
        // Initialize testbench signals
        clk = 1'b0;
        rst = 1'b1;
        ul_command = {{30{1'b0}}, 1'b1, 1'b0};

        // Create waveform dump
        if (dump_vars) begin
            $dumpfile(dump_vars_filename);
            $dumpvars(0, userlogic_test);
        end

        // Turn off reset after a few cycles
        #20;
        rst = 1'b0;
        ul_command[1] = 1'b0;

        // Run
        #10 ul_command[0] = 1'b1;

        cycle_count = num_cycles;
        while (cycle_count > 0) begin
            cycle_count = cycle_count - 1;
            #10;
        end

        $display("Userlogic ran for %0d cycles", num_cycles - cycle_count);
        $display("Onorth = %d\n", onorth);
        $display("Owest = %d\n", owest);
        $display("Osouth = %d\n", osouth);
        $display("Oeast = %d\n", oeast);
        ul_command[0] = 0'b0;
        $finish;
    end
endmodule
