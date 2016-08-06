###################################################################

# Created by write_sdc on Fri Aug  5 17:18:54 2016

###################################################################
set sdc_version 2.0

set_units -time ns -resistance kOhm -capacitance pF -voltage V -current mA
set_driving_cell -lib_cell INVD0BWP -library tcbn45gsbwpml -pin ZN [get_ports  \
io_command]
set_driving_cell -lib_cell INVD0BWP -library tcbn45gsbwpml -pin ZN [get_ports  \
io_config_enable]
set_driving_cell -lib_cell INVD0BWP -library tcbn45gsbwpml -pin ZN [get_ports  \
reset]
set_driving_cell -lib_cell INVD0BWP -library tcbn45gsbwpml -pin ZN [get_ports  \
clk]
set_load -pin_load 0.19856 [get_ports io_status]
set_max_transition 0.1 [get_ports io_status]
create_clock [get_ports clk]  -name ideal_clock1  -period 1  -waveform {0 0.5}
set_max_delay 0.2  -from [list [get_ports clk] [get_ports reset] [get_ports io_config_enable]    \
[get_ports io_command]]  -to [get_ports io_status]
