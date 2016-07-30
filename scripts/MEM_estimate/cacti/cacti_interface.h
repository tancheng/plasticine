/*------------------------------------------------------------
 *                              CACTI 5.3
 *         Copyright 2008 Hewlett-Packard Development Corporation
 *                         All Rights Reserved
 *
 * Permission to use, copy, and modify this software and its documentation is
 * hereby granted only under the following terms and conditions.  Both the
 * above copyright notice and this permission notice must appear in all copies
 * of the software, derivative works or modified versions, and any portions
 * thereof, and both notices must appear in supporting documentation.
 *
 * Users of this software agree to the terms and conditions set forth herein, and
 * hereby grant back to Hewlett-Packard Company and its affiliated companies ("HP")
 * a non-exclusive, unrestricted, royalty-free right and license under any changes, 
 * enhancements or extensions  made to the core functions of the software, including 
 * but not limited to those affording compatibility with other hardware or software
 * environments, but excluding applications which incorporate this software.
 * Users further agree to use their best efforts to return to HP any such changes,
 * enhancements or extensions that they make and inform HP of noteworthy uses of
 * this software.  Correspondence should be provided to HP at:
 *
 *                       Director of Intellectual Property Licensing
 *                       Office of Strategy and Technology
 *                       Hewlett-Packard Company
 *                       1501 Page Mill Road
 *                       Palo Alto, California  94304
 *
 * This software may be distributed (but not offered for sale or transferred
 * for compensation) to third parties, provided such third parties agree to
 * abide by the terms and conditions of this notice.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND HP DISCLAIMS ALL
 * WARRANTIES WITH REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS.   IN NO EVENT SHALL HP 
 * CORPORATION BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS
 * SOFTWARE.
 *------------------------------------------------------------*/
#ifndef __CACTI_INTERFACE_H__
#define __CACTI_INTERFACE_H__


class powerComponents
{
 public:
  double dynamic;
  double leakage;

  powerComponents() : dynamic(0), leakage(0) { }
  powerComponents(const powerComponents & obj) { *this = obj; }
  powerComponents & operator=(const powerComponents & rhs)
  {
    dynamic = rhs.dynamic;
    leakage = rhs.leakage;
    return *this;
  }
  void reset() { dynamic = 0; leakage = 0; }
  
  friend powerComponents operator+(const powerComponents & x, const powerComponents & y);
};

class powerDef
{
 public:
  powerComponents readOp;
  powerComponents writeOp;

  powerDef() : readOp(), writeOp() { }
  void reset() { readOp.reset(); writeOp.reset(); }
  
  friend powerDef operator+(const powerDef & x, const powerDef & y);
};



class InputParameter
{
 public:
  InputParameter() {};

  unsigned int cache_sz;
  unsigned int line_sz;
  unsigned int assoc;
  unsigned int nbanks;
  unsigned int out_w;  // == nr_bits_out
  bool     specific_tag;
  unsigned int tag_w;
  unsigned int access_mode;
  unsigned int obj_func_dyn_energy;
  unsigned int obj_func_dyn_power;
  unsigned int obj_func_leak_power;
  unsigned int obj_func_cycle_t;

  double   F_sz_nm;          // feature size in nm
  double   F_sz_um;          // feature size in um
  unsigned int num_rw_ports;
  unsigned int num_rd_ports;
  unsigned int num_wr_ports;
  unsigned int num_se_rd_ports;  // number of single ended read ports
  bool     is_main_mem;
  bool     is_cache;
  bool     rpters_in_htree;  // if there are repeaters in htree segment
  unsigned int ver_htree_wires_over_array;
  unsigned int broadcast_addr_din_over_ver_htrees;
  unsigned int temp;

  double   max_area_t_constraint_perc;
  double   max_acc_t_constraint_perc;
  double   max_perc_diff_in_delay_fr_best_delay_rptr_sol;
  unsigned int ram_cell_tech_type;
  unsigned int peri_global_tech_type;
  unsigned int data_arr_ram_cell_tech_type;
  unsigned int data_arr_peri_global_tech_type;
  unsigned int tag_arr_ram_cell_tech_type;
  unsigned int tag_arr_peri_global_tech_type;

  unsigned int burst_len;
  unsigned int int_prefetch_w;
  unsigned int page_sz_bits;

  unsigned int ic_proj_type;      // inteconnect_projection_type
  unsigned int wire_is_mat_type;  // wire_outside_mat_type
  unsigned int wire_os_mat_type;  // wire_inside_mat_type

  // parameters derived from input parameters
  bool     fast_access;
  unsigned int block_sz;
  unsigned int tag_assoc;
  unsigned int data_assoc;
  bool     is_seq_acc;
  bool     fully_assoc;
  unsigned int nsets;  // == number_of_sets
};


typedef struct{
  int Ndwl;
  int Ndbl;
  double Nspd;
  int deg_bitline_muxing;
  int Ndsam_lev_1;
  int Ndsam_lev_2;
  int number_activated_mats_horizontal_direction;
  int number_subbanks;
  int page_size_in_bits;
  double delay_route_to_bank;
  double delay_crossbar;
  double delay_addr_din_horizontal_htree;
  double delay_addr_din_vertical_htree;
  double delay_row_predecode_driver_and_block;
  double delay_row_decoder;
  double delay_bitlines;
  double delay_sense_amp;
  double delay_subarray_output_driver;
  double delay_bit_mux_predecode_driver_and_block;
  double delay_bit_mux_decoder;
  double delay_senseamp_mux_lev_1_predecode_driver_and_block;
  double delay_senseamp_mux_lev_1_decoder;
  double delay_senseamp_mux_lev_2_predecode_driver_and_block;
  double delay_senseamp_mux_lev_2_decoder;
  double delay_dout_vertical_htree;
  double delay_dout_horizontal_htree;
  double delay_comparator;
  double access_time;
  double cycle_time;
  double multisubbank_interleave_cycle_time;
  double delay_request_network;
  double delay_inside_mat;
  double delay_reply_network;
  double trcd;
  double cas_latency;
  double precharge_delay;
  powerDef power_routing_to_bank;
  powerDef power_addr_horizontal_htree;
  powerDef power_datain_horizontal_htree;
  powerDef power_dataout_horizontal_htree;
  powerDef power_addr_vertical_htree;
  powerDef power_datain_vertical_htree;
  powerDef power_row_predecoder_drivers;
  powerDef power_row_predecoder_blocks;
  powerDef power_row_decoders;
  powerDef power_bit_mux_predecoder_drivers;
  powerDef power_bit_mux_predecoder_blocks;
  powerDef power_bit_mux_decoders;
  powerDef power_senseamp_mux_lev_1_predecoder_drivers;
  powerDef power_senseamp_mux_lev_1_predecoder_blocks;
  powerDef power_senseamp_mux_lev_1_decoders;
  powerDef power_senseamp_mux_lev_2_predecoder_drivers;
  powerDef power_senseamp_mux_lev_2_predecoder_blocks;
  powerDef power_senseamp_mux_lev_2_decoders;
  powerDef power_bitlines;
  powerDef power_sense_amps;
  powerDef power_prechg_eq_drivers;
  powerDef power_output_drivers_at_subarray;
  powerDef power_dataout_vertical_htree;
  powerDef power_comparators;
  powerDef power_crossbar;
  powerDef total_power;
  double area;
  double all_banks_height;
  double all_banks_width;
  double bank_height;
  double bank_width;
  double subarray_memory_cell_area_height;
  double subarray_memory_cell_area_width;
  double mat_height;
  double mat_width;
  double routing_area_height_within_bank;
  double routing_area_width_within_bank;
  double area_efficiency;
  double perc_power_dyn_routing_to_bank;
  double perc_power_dyn_addr_horizontal_htree;
  double perc_power_dyn_datain_horizontal_htree;
  double perc_power_dyn_dataout_horizontal_htree;
  double perc_power_dyn_addr_vertical_htree;
  double perc_power_dyn_datain_vertical_htree;
  double perc_power_dyn_row_predecoder_drivers;
  double perc_power_dyn_row_predecoder_blocks;
  double perc_power_dyn_row_decoders;
  double perc_power_dyn_bit_mux_predecoder_drivers;
  double perc_power_dyn_bit_mux_predecoder_blocks;
  double perc_power_dyn_bit_mux_decoders;
  double perc_power_dyn_senseamp_mux_lev_1_predecoder_drivers;
  double perc_power_dyn_senseamp_mux_lev_1_predecoder_blocks;
  double perc_power_dyn_senseamp_mux_lev_1_decoders;
  double perc_power_dyn_senseamp_mux_lev_2_predecoder_drivers;
  double perc_power_dyn_senseamp_mux_lev_2_predecoder_blocks;
  double perc_power_dyn_senseamp_mux_lev_2_decoders;
  double perc_power_dyn_bitlines;
  double perc_power_dyn_sense_amps;
  double perc_power_dyn_prechg_eq_drivers;
  double perc_power_dyn_subarray_output_drivers;
  double perc_power_dyn_dataout_vertical_htree;
  double perc_power_dyn_comparators;
  double perc_power_dyn_crossbar;
  double perc_power_dyn_spent_outside_mats;
  double perc_power_leak_routing_to_bank;
  double perc_power_leak_addr_horizontal_htree;
  double perc_power_leak_datain_horizontal_htree;
  double perc_power_leak_dataout_horizontal_htree;
  double perc_power_leak_addr_vertical_htree;
  double perc_power_leak_datain_vertical_htree;
  double perc_power_leak_row_predecoder_drivers;
  double perc_power_leak_row_predecoder_blocks;
  double perc_power_leak_row_decoders;
  double perc_power_leak_bit_mux_predecoder_drivers;
  double perc_power_leak_bit_mux_predecoder_blocks;
  double perc_power_leak_bit_mux_decoders;
  double perc_power_leak_senseamp_mux_lev_1_predecoder_drivers;
  double perc_power_leak_senseamp_mux_lev_1_predecoder_blocks;
  double perc_power_leak_senseamp_mux_lev_1_decoders;
  double perc_power_leak_senseamp_mux_lev_2_predecoder_drivers;
  double perc_power_leak_senseamp_mux_lev_2_predecoder_blocks;
  double perc_power_leak_senseamp_mux_lev_2_decoders;
  double perc_power_leak_bitlines;
  double perc_power_leak_sense_amps;
  double perc_power_leak_prechg_eq_drivers;
  double perc_power_leak_subarray_output_drivers;
  double perc_power_leak_dataout_vertical_htree;
  double perc_power_leak_comparators;
  double perc_power_leak_crossbar;
  double perc_leak_mats;
  double perc_active_mats;
  double refresh_power;
  double dram_refresh_period;
  double dram_array_availability;
  double dyn_read_energy_from_closed_page;
  double dyn_read_energy_from_open_page;
  double leak_power_subbank_closed_page;
  double leak_power_subbank_open_page;
  double leak_power_request_and_reply_networks;
  double activate_energy;
  double read_energy;
  double write_energy;
  double precharge_energy;
} results_mem_array;


typedef struct{
  results_mem_array tag_array;
  results_mem_array data_array;
  double access_time;
  double cycle_time;
  double area;
  double area_efficiency;
  powerDef power;
  double leak_power_with_sleep_transistors_in_mats;
  InputParameter ip;
  double cache_ht;
  double cache_len;
  char file_n[100];
  double vdd_periph_global;
  bool valid;
} final_results;


final_results cacti_interface(
  int cache_size,
  int line_size,
  int associativity,
  int rw_ports,
  int excl_read_ports,
  int excl_write_ports,
  int single_ended_read_ports,
  int banks,
  double tech_node,
  int output_width,
  int specific_tag,
  int tag_width,
  int access_mode,
  int cache,
  int main_mem,
  int obj_func_dynamic_energy,
  int obj_func_dynamic_power,
  int obj_func_leakage_power,
  int obj_func_cycle_time,
  int temp,
  int data_arr_ram_cell_tech_flavor_in, 
  int data_arr_periph_global_tech_flavor_in, 
  int tag_arr_ram_cell_tech_flavor_in, 
  int tag_arr_periph_global_tech_flavor_in, 
  int interconnect_projection_type_in,
  int wire_inside_mat_type_in, 
  int wire_outside_mat_type_in, 
  int REPEATERS_IN_HTREE_SEGMENTS_in, 
  int VERTICAL_HTREE_WIRES_OVER_THE_ARRAY_in,
  int BROADCAST_ADDR_DATAIN_OVER_VERTICAL_HTREES_in, 
  double MIN_PERCENT_WITHIN_BEST_AREA_in, 
  double MIN_PERCENT_WITHIN_BEST_DELAY_in,
  double MAX_PERC_DIFF_IN_DELAY_FROM_BEST_DELAY_REPEATER_SOLUTION_in,
  int PAGE_SIZE_BITS_in,
  int BURST_SIZE_in,
  int INTERNAL_PREFETCH_WIDTH_in);

#endif
