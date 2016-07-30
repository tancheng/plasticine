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

#include "area.h"
#include "basic_circuit.h"
#include "parameter.h"
#include "time.h"
#include <iostream>
#include <fstream>

powerComponents operator+(const powerComponents & x, const powerComponents & y)
{
  powerComponents z;

  z.dynamic = x.dynamic + y.dynamic;
  z.leakage = x.leakage + y.leakage;

  return z;
}

powerDef operator+(const powerDef & x, const powerDef & y)
{
  powerDef z;

  z.readOp  = x.readOp  + y.readOp;
  z.writeOp = x.writeOp + y.writeOp;

  return z;
}


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
    int data_arr_peri_global_tech_flavor_in, 
    int tag_arr_ram_cell_tech_flavor_in,
    int tag_arr_peri_global_tech_flavor_in,
    int interconnect_projection_type_in,
    int wire_inside_mat_type_in, 
    int wire_outside_mat_type_in, 
    int REPEATERS_IN_HTREE_SEGMENTS_in, 
    int VERTICAL_HTREE_WIRES_OVER_THE_ARRAY_in,
    int BROADCAST_ADDR_DATAIN_OVER_VERTICAL_HTREES_in, 
    double MAXAREACONSTRAINT_PERC_in, 
    double MAXACCTIMECONSTRAINT_PERC_in,
    double MAX_PERC_DIFF_IN_DELAY_FROM_BEST_DELAY_REPEATER_SOLUTION_in,
    int PAGE_SIZE_BITS_in,
    int BURST_LENGTH_in,
    int INTERNAL_PREFETCH_WIDTH_in)
{
  final_results fin_res;
  fin_res.valid = false;
  
  g_ip.data_arr_ram_cell_tech_type    = data_arr_ram_cell_tech_flavor_in;
  g_ip.data_arr_peri_global_tech_type = data_arr_peri_global_tech_flavor_in;
  g_ip.tag_arr_ram_cell_tech_type     = tag_arr_ram_cell_tech_flavor_in;
  g_ip.tag_arr_peri_global_tech_type  = tag_arr_peri_global_tech_flavor_in;

  g_ip.ic_proj_type     = interconnect_projection_type_in;
  g_ip.wire_is_mat_type = wire_inside_mat_type_in;
  g_ip.wire_os_mat_type = wire_outside_mat_type_in;
  g_ip.max_area_t_constraint_perc                    = MAXAREACONSTRAINT_PERC_in;
  g_ip.max_acc_t_constraint_perc                     = MAXACCTIMECONSTRAINT_PERC_in;
  g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol = MAX_PERC_DIFF_IN_DELAY_FROM_BEST_DELAY_REPEATER_SOLUTION_in;
  g_ip.burst_len      = BURST_LENGTH_in;
  g_ip.int_prefetch_w = INTERNAL_PREFETCH_WIDTH_in;
  g_ip.page_sz_bits   = PAGE_SIZE_BITS_in;

  g_ip.cache_sz            = cache_size;
  g_ip.line_sz             = line_size;
  g_ip.assoc               = associativity;
  g_ip.nbanks              = banks;
  g_ip.out_w               = output_width;
  g_ip.specific_tag        = specific_tag;
  g_ip.tag_w               = tag_width;
  g_ip.access_mode         = access_mode;
  g_ip.obj_func_dyn_energy = obj_func_dynamic_energy;
  g_ip.obj_func_dyn_power  = obj_func_dynamic_power;
  g_ip.obj_func_leak_power = obj_func_leakage_power;
  g_ip.obj_func_cycle_t    = obj_func_cycle_time;
  g_ip.temp = temp;

  g_ip.F_sz_nm         = tech_node;
  g_ip.F_sz_um         = tech_node / 1000;
  g_ip.is_main_mem     = (main_mem != 0) ? true : false;
  g_ip.is_cache        = (cache != 0) ? true : false;
  g_ip.rpters_in_htree = (REPEATERS_IN_HTREE_SEGMENTS_in != 0) ? true : false;
  g_ip.ver_htree_wires_over_array = VERTICAL_HTREE_WIRES_OVER_THE_ARRAY_in;
  g_ip.broadcast_addr_din_over_ver_htrees = BROADCAST_ADDR_DATAIN_OVER_VERTICAL_HTREES_in;
  
  g_ip.num_rw_ports    = rw_ports;
  g_ip.num_rd_ports    = excl_read_ports;
  g_ip.num_wr_ports    = excl_write_ports;
  g_ip.num_se_rd_ports = single_ended_read_ports;
  
 
  int  A;
  bool seq_access  = false;
  bool fast_access = true;
  
  switch (g_ip.access_mode)
  {
    case 0:
      seq_access  = false;
      fast_access = false;
      break;
    case 1:
      seq_access  = true;
      fast_access = false;
      break;
    case 2:
      seq_access  = false;
      fast_access = true;
      break;
  }
  
  uint32_t B = g_ip.line_sz;
  
  if (B < 1)
  {
    cerr << "Block size must >=1" << endl;
    return fin_res;
  }
  else if (B*8 < g_ip.out_w)
  {
    cerr << "Block size must be at least " << g_ip.out_w/8 << endl;
    return fin_res;
  }
  
  if (g_ip.F_sz_um <= 0)
  {
    cerr << "Feature size must be > 0" << endl;
    return fin_res;
  }
  else if (g_ip.F_sz_um > 0.091)
  {
    cerr << "Feature size must be <= 90 nm" << endl;
    return fin_res;
  }
 
  
  uint32_t RWP  = g_ip.num_rw_ports;
  uint32_t ERP  = g_ip.num_rd_ports;
  uint32_t EWP  = g_ip.num_wr_ports;
  uint32_t NSER = g_ip.num_se_rd_ports;
  
  // If multiple banks and multiple ports are specified, then if number of ports is less than or equal to
  // the number of banks, we assume that the multiple ports are implemented via the multiple banks.
  // In such a case we assume that each bank has 1 RWP port. 
  if ((RWP + ERP + EWP) <= g_ip.nbanks)
  {
    RWP  = 1;
    ERP  = 0;
    EWP  = 0;
    NSER = 0;
  }
  else if ((RWP < 0) || (EWP < 0) || (ERP < 0))
  {
    cerr << "Ports must >=0" << endl;
    return fin_res;
  }
  else if (RWP > 2)
  {
    cerr << "Maximum of 2 read/write ports" << endl;
    return fin_res;
  }
  else if ((RWP+ERP+EWP) < 1)
  {
    cerr << "Must have at least one port" << endl;
    return fin_res;
  }
  
  if (is_pow2(g_ip.nbanks) == false)
  {
    cerr << "Number of subbanks should be greater than or equal to 1 and should be a power of 2" << endl;
    return fin_res;
  }
 
  int C = g_ip.cache_sz/g_ip.nbanks;
  if (C < 64)
  {
    cerr << "Cache size must >=64" << endl;
    return fin_res;
  }
  
  if (g_ip.assoc == 0)
  {
    A = C/B;
    g_ip.fully_assoc = true;
  }
  else
  {
    if (g_ip.assoc == 1)
    {
      A=1;
      g_ip.fully_assoc = false;
    }
    else
    {
      g_ip.fully_assoc = false;
      A = g_ip.assoc;
      if (is_pow2(A) == false)
      {
        cerr << "Associativity must be a power of 2" << endl;
        return fin_res;
      }
    }
  }
  
  if (C/(B*A)<=1 && !g_ip.fully_assoc)
  {
    cerr << "Number of sets is too small: " << endl;
    cerr << " Need to either increase cache size, or decrease associativity or block size" << endl;
    cerr << " (or use fully associative cache)" << endl;
    return fin_res;
  }

  g_ip.block_sz = B;
  
  /*dt: testing sequential access mode*/
  if(seq_access)
  {
    g_ip.tag_assoc = A;
    g_ip.data_assoc = 1;
    g_ip.is_seq_acc = true;
  }
  else
  {
    g_ip.tag_assoc = A;
    g_ip.data_assoc = A;
    g_ip.is_seq_acc = false;
  }

  g_ip.fast_access = fast_access;
  
  if (g_ip.fully_assoc)
  {
    g_ip.data_assoc = 1;
  }
  g_ip.num_rw_ports    = RWP;
  g_ip.num_rd_ports    = ERP;
  g_ip.num_wr_ports    = EWP;
  g_ip.num_se_rd_ports = NSER;
  g_ip.nsets           = C/(B*A);

  if (g_ip.temp < 300 || g_ip.temp > 400 || g_ip.temp%10 != 0)
  {
    cerr << g_ip.temp << " Temperature must be between 300 and 400 Kelvin and multiple of 10." << endl;
    return fin_res;
  }
  
  if (g_ip.nsets < 1)
  {
    cerr << "Less than one set..." << endl;
    return fin_res;
  }   

  do_it(&fin_res);

  return fin_res;
}


void output_data_csv(const final_results & fin_res)
{
  fstream file("out.csv", ios::in);
  bool    print_index = file.fail();
  file.close();

  file.open("out.csv", ios::out|ios::app);
  if (file.fail() == true)
  {
    cerr << "File out.csv could not be opened successfully" << endl;
  }
  else
  {
    if (print_index == true)
    {
      file << "Tech node (nm), ";
      file << "Capacity (bytes), ";
      file << "Number of banks, ";
      file << "Associativity, ";
      file << "Output width (bits), ";
      file << "Access time (ns), ";
      file << "Random cycle time (ns), ";
      file << "Multisubbank interleave cycle time (ns), ";
      file << "Delay request network (ns), ";
      file << "Delay inside mat (ns), ";
      file << "Delay reply network (ns), ";
      file << "Tag array access time (ns), ";
      file << "Refresh period (microsec), ";
      file << "DRAM array availability (%), ";
      file << "Dynamic read energy (nJ), ";
      file << "Dynamic write energy (nJ), ";
      file << "Dynamic read power (mW), ";
      file << "Standby leakage per bank(mW), ";
      file << "Leakage per bank with leak power management (mW), ";
      file << "Refresh power as percentage of standby leakage, ";
      file << "Area (mm2), ";
      file << "Ndwl, ";
      file << "Ndbl, ";
      file << "Nspd, ";
      file << "Ndcm, ";
      file << "Ndsam_level_1, ";
      file << "Ndsam_level_2, ";
      file << "Ntwl, ";
      file << "Ntbl, ";
      file << "Ntspd, ";
      file << "Ntcm, ";
      file << "Ntsam_level_1, ";
      file << "Ntsam_level_2, ";
      file << "Area efficiency, ";
      file << "Resistance per unit micron (ohm-micron), ";
      file << "Capacitance per unit micron (fF per micron), ";
      file << "Unit-length wire delay (ps), ";
      file << "FO4 delay (ps), ";
      file << "delay route to bank (including crossb delay) (ps), ";
      file << "Crossbar delay (ps), ";
      file << "Dyn read energy per access from closed page (nJ), ";
      file << "Dyn read energy per access from open page (nJ), ";
      file << "Leak power of an subbank with page closed (mW), ";
      file << "Leak power of a subbank with page  open (mW), ";
      file << "Leak power of request and reply networks (mW), ";
      file << "Number of subbanks, ";
      file << "Page size in bits, ";
      file << "Activate power, ";
      file << "Read power, ";
      file << "Write power, ";
      file << "Precharge power, ";
      file << "tRCD, ";
      file << "CAS latency, ";
      file << "Precharge delay, ";
      file << "Perc dyn energy bitlines, ";
      file << "perc dyn energy wordlines, ";
      file << "perc dyn energy outside mat, ";
      file << "Area opt (perc), ";
      file << "Delay opt (perc), ";
      file << "Repeater opt (perc), ";
      file << "Aspect ratio" << endl;
    }
    file << g_ip.F_sz_nm << ", ";
    file << g_ip.cache_sz << ", ";
    file << g_ip.nbanks << ", ";
    file << g_ip.tag_assoc << ", ";
    file << g_ip.out_w << ", ";
    file << fin_res.access_time*1e+9 << ", ";
    file << fin_res.cycle_time*1e+9 << ", ";
    file << fin_res.data_array.multisubbank_interleave_cycle_time*1e+9 << ", ";
    file << fin_res.data_array.delay_request_network*1e+9 << ", ";
    file << fin_res.data_array.delay_inside_mat*1e+9 <<  ", ";
    file << fin_res.data_array.delay_reply_network*1e+9 << ", ";
    file << fin_res.tag_array.access_time*1e+9 << ", ";
    file << fin_res.data_array.dram_refresh_period*1e+6 << ", ";
    file << fin_res.data_array.dram_array_availability <<  ", ";
    file << fin_res.power.readOp.dynamic*1e+9 << ", ";
    file << fin_res.power.writeOp.dynamic*1e+9 << ", ";
    file << fin_res.power.readOp.dynamic*1000/fin_res.cycle_time << ", ";
    file << fin_res.power.readOp.leakage*1000 << ", ";
    file << fin_res.leak_power_with_sleep_transistors_in_mats*1000 << ", ";
    file << fin_res.data_array.refresh_power / fin_res.data_array.total_power.readOp.leakage << ", ";
    file << fin_res.area << ", ";
    file << fin_res.data_array.Ndwl << ", ";
    file << fin_res.data_array.Ndbl << ", ";
    file << fin_res.data_array.Nspd << ", ";
    file << fin_res.data_array.deg_bitline_muxing << ", ";
    file << fin_res.data_array.Ndsam_lev_1 << ", ";
    file << fin_res.data_array.Ndsam_lev_2 << ", ";
    file << fin_res.tag_array.Ndwl << ", ";
    file << fin_res.tag_array.Ndbl << ", ";
    file << fin_res.tag_array.Nspd << ", ";
    file << fin_res.tag_array.deg_bitline_muxing << ", ";
    file << fin_res.tag_array.Ndsam_lev_1 << ", ";
    file << fin_res.tag_array.Ndsam_lev_2 << ", ";
    file << fin_res.area_efficiency << ", ";
    file << g_tp.wire_inside_mat.R_per_um << ", ";
    file << g_tp.wire_inside_mat.C_per_um / 1e-15 << ", ";
    file << g_tp.unit_len_wire_del / 1e-12 << ", ";
    file << g_tp.FO4 / 1e-12 << ", ";
    file << fin_res.data_array.delay_route_to_bank / 1e-9 << ", ";
    file << fin_res.data_array.delay_crossbar / 1e-9 << ", ";
    file << fin_res.data_array.dyn_read_energy_from_closed_page / 1e-9 << ", ";
    file << fin_res.data_array.dyn_read_energy_from_open_page / 1e-9 << ", ";
    file << fin_res.data_array.leak_power_subbank_closed_page / 1e-3 << ", ";
    file << fin_res.data_array.leak_power_subbank_open_page / 1e-3 << ", ";
    file << fin_res.data_array.leak_power_request_and_reply_networks / 1e-3 << ", ";
    file << fin_res.data_array.number_subbanks << ", " ;
    file << fin_res.data_array.page_size_in_bits << ", " ;
    file << fin_res.data_array.activate_energy * 1e9 << ", " ;
    file << fin_res.data_array.read_energy * 1e9 << ", " ;
    file << fin_res.data_array.write_energy * 1e9 << ", " ;
    file << fin_res.data_array.precharge_energy * 1e9 << ", " ;
    file << fin_res.data_array.trcd * 1e9 << ", " ;
    file << fin_res.data_array.cas_latency * 1e9 << ", " ;
    file << fin_res.data_array.precharge_delay * 1e9 << ", " ;
    file << fin_res.data_array.perc_power_dyn_bitlines << ", " ;
    file << fin_res.data_array.perc_power_dyn_row_decoders << ", " ;
    file << fin_res.data_array.perc_power_dyn_spent_outside_mats << ", " ;
    file << g_ip.max_area_t_constraint_perc << ", ";
    file << g_ip.max_acc_t_constraint_perc  << ", ";
    file << g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol << ", ";
    file << fin_res.data_array.all_banks_height / fin_res.data_array.all_banks_width << endl;
  }
  file.close();
}

