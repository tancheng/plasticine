# Plasticine Architecture and the Plasma assembler

# Getting Started

### Build
Entry points (that invoke Chisel) for various modules in the design are all in src/main/scala/CommonDriver.scala. The specific entry point for the entire Plasticine architecture is called 'TopGen'. The Makefile uses this starting point to generate the RTL for the entire Plasticine architecture and Fringe, also compiles the generated RTL and testbench using VCS. The RTL and other compiled files are generated into a folder called 'psim'.

``sh
$ make # Builds entire architecture
```

Generating other components individually is often quite valuable for testing, characterization etc. A helper script at 'bin/gen' can be used to generate individual modules. Output products are generated into a hw_out/<dir> directory. VCS is not invoked on this, as there is no testbench for each individual module separately. Example:
``sh
$ bin/gen PCU  # Generates RTL for PCU
```

### Run (Plasma)
The 'bin/pisa' script (TODO: Change to Plasma) executes the entry point for Plasma. Plasma is roughly organized like old DHDL. Applications exist in src/main/scala/apps/<dir> in several traits. The 'head' trait extends a common trait (PISADesign in src/main/scala/pisa/Frontend.scala) that defines the main method. Currently, PISADesign also regenerates the Top module (using the TopGen entry point, by assigning initial values to the configuration registers). This will be removed to use the shift register in the near future. Host driver code for app (Driver.cpp) is expected to be in the <app> folder, and will be copied over to the 'app_out/<gen>' folder. VCS is not invoked, but Makefile is provided (should be invoked manually in the folder)
``sh
$ bin/pisa <app>  # Generates Plasticine RTL with init values for registers defined according to <app>.
cd app_out/<app>
make              # Invokes VCS
```

Simulation is carried out in a manner similar to VCS simulation of Spatial-generated apps; the host process (Driver.cpp) communicates with the hardware (accel.bit.bin, in psim folder) using the FringeContext API. The Fringe API uses UNIX pipes under the hood to communicate between processes.

# PDB: Plasticine Debugger

### Quick Start
``sh
$ bin/pdb
(pdb) setSim(2,2)
(pdb) init("path_to_pisa_file")
(pdb) ..
(pdb) // Start debugging
```
### Useful Commands
``sh
   ## Setting up
   reconfig(pisa_file_path): Reconfigure Plasticine with PISA file
   writeReg(reg: Int, data: Int): Write to scalar register
   readReg(reg: Int): Read (print) from scalar register
   scalars: Print all scalar registers

   ## Execution
   start: Start the design. Writes a '1' to the command register that generates a token (1-cycle pulse)
   c: Continue until a breakpoint is hit, or design finishes
   cycles: Print number of cycles elapsed
   alert(x): Set alert interval (in cycles) during simulation

   ## Debugging
   n:     Execute for one cycle
   s(numCycles: Int = 1): Execyte for 'numCycles' cycles or until a breakpoint is hit
   callback(f: () => Unit): Register a callback (such as printing values) that gets executed after every step during debugging
   poke(signal: Bits, data: Int): Set a signal to a certain value (may not always work)
   peek(signal: UInt): View a signal's current value
   break(signal: UInt): Set breakpoint when signal > 0
   break(signals: Seq[UInt]): Set a breakpoint when any of signals > 0
   bclear(signal: UInt = null): Clear breakpoints on signals if any
   bclear(signals: Seq[UInt]): Clear breakpoints on signals if any
   cu(x: Int, y: Int, options: String*): Show state of cu(x,y).
   cs(x: Int, y: Int): Show state of control switch (x,y)
   getcu(x: Int, y: Int): Return handle to the hardare CU module at (x,y)
   getds(x: Int, y: Int): Return handle to the hardare data switch module at (x,y)
   getcs(x: Int, y: Int): Return handle to the hardare control switch module at (x,y)
   showTop: Dump top unit state
   watchcu(x: Int, y: Int): Break when any of the control IO of cu(x,y) goes high
   watchcs(x: Int, y: Int): Break when any of control IO of control switch(x,y) goes high
   xbar(mod: Crossbar): Prints crossbar
   lut(mod: LUT): Prints LUT
   regs(c: ComputeUnit, d: Int, v: Int): Dump pipeline regs of stage 'd', lane 'v' in 'c'
   stage(c: ComputeUnit, d: Int, verbose: Boolean = false): Dump pipeline stage info of stage 'd' in 'c'
   dv(v: Vec[UInt]): Print a vector of signals
   dout (mod: Module): Print all outputs of a given module

```
