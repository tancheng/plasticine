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

#include <time.h>
#include <math.h>
#include "basic_circuit.h"

#include "time.h"
#include "const.h"
#include "area.h"
#include "decoder.h"
#include "htree.h"
#include "crossbar.h"
#include "parameter.h"

#include <pthread.h>
#include <iostream>
#include <algorithm>

using namespace std;

const uint32_t nthreads = NTHREADS;

inline static double _abs(double a)
{
  return (a > 0) ? a : (0-a);
}


void *calc_time_mt_wrapper(void * void_obj)
{
  calc_time_mt_wrapper_struct * calc_obj = (calc_time_mt_wrapper_struct *) void_obj;
  uint32_t tid                   = calc_obj->tid;
  list<mem_array *> & data_arr   = calc_obj->data_arr;
  list<mem_array *> & tag_arr    = calc_obj->tag_arr;
  bool is_tag                    = calc_obj->is_tag;
  bool pure_ram                  = calc_obj->pure_ram;
  bool is_main_mem               = calc_obj->is_main_mem;
  double Nspd_min                = calc_obj->Nspd_min;
  const ArrayEdgeToBankEdgeHtreeSizing * arr_edge_to_bank_edge_htree_sizing = (calc_obj->ptr_arr_edge_to_bank_edge_htree_sizing);
  const BankHtreeSizing * bank_htree_sizing = (calc_obj->ptr_bank_htree_sizing);
  data_arr.clear();
  data_arr.push_back(new mem_array);
  tag_arr.clear();
  tag_arr.push_back(new mem_array);

  uint32_t Ndwl_niter = _log2(MAXDATAN) + 1;
  uint32_t Ndbl_niter = _log2(MAXDATAN) + 1;
  uint32_t Ndcm_niter = _log2(MAX_COL_MUX) + 1;
  uint32_t niter      = Ndwl_niter * Ndbl_niter * Ndcm_niter;

  bool is_valid_partition;
  
  for (double Nspd = Nspd_min; Nspd <= MAXDATASPD; Nspd *= 2)
  {
    for (uint32_t iter = tid; iter < niter; iter += nthreads)
    {
      // reconstruct Ndwl, Ndbl, Ndcm
      unsigned int Ndwl = 1 << (iter / (Ndbl_niter * Ndcm_niter));
      unsigned int Ndbl = 1 << ((iter / (Ndcm_niter))%Ndbl_niter);
      unsigned int Ndcm = 1 << (iter % Ndcm_niter); 
      for(unsigned int Ndsam_lev_1 = 1; Ndsam_lev_1 <= MAX_COL_MUX; Ndsam_lev_1 *= 2)
      {
        for(unsigned int Ndsam_lev_2 = 1; Ndsam_lev_2 <= MAX_COL_MUX; Ndsam_lev_2 *= 2)
        {
          if (is_tag == true)
          {
            is_valid_partition = calculate_time(is_tag, pure_ram, Nspd, Ndwl, 
                Ndbl, Ndcm, Ndsam_lev_1, Ndsam_lev_2,
                tag_arr.back(), 0, NULL, NULL,
                *arr_edge_to_bank_edge_htree_sizing,
                *bank_htree_sizing,
                is_main_mem);
          }
          // If it's a fully-associative cache, the data array partition parameters are identical to that of
          // the tag array, so compute data array partition properties also here.
          if (is_tag == false || g_ip.fully_assoc)
          {
            is_valid_partition = calculate_time(is_tag/*false*/, pure_ram, Nspd, Ndwl, 
                Ndbl, Ndcm, Ndsam_lev_1, Ndsam_lev_2,
                data_arr.back(), 0, NULL, NULL,
                *arr_edge_to_bank_edge_htree_sizing,
                *bank_htree_sizing,
                is_main_mem);
          }
          
          if(is_valid_partition)
          {
            if (is_tag == true)
            {
              tag_arr.push_back(new mem_array);
            }
            if (is_tag == false || g_ip.fully_assoc)
            {
              data_arr.push_back(new mem_array);
            }
          }
        }
      }
    }
  }
  data_arr.pop_back();
  tag_arr.pop_back();

  pthread_exit(NULL);
}


// returns <delay, risetime>
pair<double, double> get_max_delay_before_decoder(
    const PredecoderBlockDriver & predec_blk_drv1,
    const PredecoderBlock       & predec_blk1,
    pair<double, double>          input_pair1,
    const PredecoderBlockDriver & predec_blk_drv2,
    const PredecoderBlock       & predec_blk2,
    pair<double, double>          input_pair2)
{
  pair<double, double> ret_val;
  double delay;

  //outrisetime_nand2_decode_path_1 = input_pair1.first;
  //outrisetime_nand3_decode_path_1 = input_pair1.second;
  //outrisetime_nand2_decode_path_2 = input_pair2.first;
  //outrisetime_nand3_decode_path_2 = input_pair2.second;

  delay = predec_blk_drv1.delay_nand2_path + predec_blk1.delay_nand2_path;
  ret_val.first  = delay;
  ret_val.second = input_pair1.first;
  delay = predec_blk_drv1.delay_nand3_path + predec_blk1.delay_nand3_path;
  if (ret_val.first < delay)
  {
    ret_val.first  = delay;
    ret_val.second = input_pair1.second;
  }
  delay = predec_blk_drv2.delay_nand2_path + predec_blk2.delay_nand2_path;
  if (ret_val.first < delay)
  {
    ret_val.first  = delay;
    ret_val.second = input_pair2.first;
  }
  delay = predec_blk_drv2.delay_nand3_path + predec_blk2.delay_nand3_path;
  if (ret_val.first < delay)
  {
    ret_val.first  = delay;
    ret_val.second = input_pair2.second;
  }

  return ret_val;
}


static void delay_comparator(
    int tagbits,
    int A,
    double inputtime,
    double *outputtime,
    double *delay,
    powerDef *power,
    bool is_dram,
    double cell_h)
{
  double Req, Ceq, tf, st1del, st2del, st3del, nextinputtime, m;
  double c1, c2, r1, r2, tstep, a, b, c, lkgCurrent;
  double Tcomparatorni;

  *delay = 0;
  power->readOp.dynamic  = 0;
  power->readOp.leakage  = 0;
  power->writeOp.dynamic = 0;
  power->writeOp.leakage = 0;

  tagbits = tagbits / 4;//Assuming there are 4 quarter comparators. input tagbits is already
  //a multiple of 4.

  /* First Inverter */
  Ceq = gate_C(g_tp.w_comp_inv_n2+g_tp.w_comp_inv_p2, 0, is_dram) +
        drain_C_(g_tp.w_comp_inv_p1, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
        drain_C_(g_tp.w_comp_inv_n1, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  Req = tr_R_on(g_tp.w_comp_inv_p1, PCH, 1, is_dram);
  tf = Req*Ceq;
  st1del = horowitz(inputtime,tf,VTHCOMPINV,VTHCOMPINV,FALL);
  nextinputtime = st1del/VTHCOMPINV;
  power->readOp.dynamic += 0.5 * Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 4 * A; 
  //For each degree of associativity 
  //there are 4 such quarter comparators
  lkgCurrent = 0.5 * cmos_Ileak(g_tp.w_comp_inv_n1, g_tp.w_comp_inv_p1, is_dram) * 4 * A;

  /* Second Inverter */
  Ceq = gate_C(g_tp.w_comp_inv_n3+g_tp.w_comp_inv_p3, 0, is_dram) +
        drain_C_(g_tp.w_comp_inv_p2, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
        drain_C_(g_tp.w_comp_inv_n2, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  Req = tr_R_on(g_tp.w_comp_inv_n2, NCH, 1, is_dram);
  tf = Req*Ceq;
  st2del = horowitz(nextinputtime,tf,VTHCOMPINV,VTHCOMPINV,RISE);
  nextinputtime = st2del/(1.0-VTHCOMPINV);
  power->readOp.dynamic += 0.5 * Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 4 * A;
  lkgCurrent += 0.5 * cmos_Ileak(g_tp.w_comp_inv_n2, g_tp.w_comp_inv_p2, is_dram) * 4 * A;

  /* Third Inverter */
  Ceq = gate_C(g_tp.w_eval_inv_n+g_tp.w_eval_inv_p, 0, is_dram) +
        drain_C_(g_tp.w_comp_inv_p3, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
        drain_C_(g_tp.w_comp_inv_n3, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  Req = tr_R_on(g_tp.w_comp_inv_p3, PCH, 1, is_dram);
  tf = Req*Ceq;
  st3del = horowitz(nextinputtime,tf,VTHCOMPINV,VTHEVALINV,FALL);
  nextinputtime = st3del/(VTHEVALINV);
  power->readOp.dynamic += 0.5 * Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 4 * A;
  lkgCurrent += 0.5 * cmos_Ileak(g_tp.w_comp_inv_n3, g_tp.w_comp_inv_p3, is_dram) * 4 * A;

  /* Final Inverter (virtual ground driver) discharging compare part */
  r1 = tr_R_on(g_tp.w_comp_n,NCH,2, is_dram);
  r2 = tr_R_on(g_tp.w_eval_inv_n,NCH,1, is_dram); /* was switch */
  c2 = (tagbits)*(drain_C_(g_tp.w_comp_n,NCH,1, 1, g_tp.cell_h_def, is_dram) +
                  drain_C_(g_tp.w_comp_n,NCH,2, 1, g_tp.cell_h_def, is_dram)) +
        drain_C_(g_tp.w_eval_inv_p,PCH,1, 1, g_tp.cell_h_def, is_dram) +
        drain_C_(g_tp.w_eval_inv_n,NCH,1, 1, g_tp.cell_h_def, is_dram);
  c1 = (tagbits)*(drain_C_(g_tp.w_comp_n,NCH,1, 1, g_tp.cell_h_def, is_dram) +
                  drain_C_(g_tp.w_comp_n,NCH,2, 1, g_tp.cell_h_def, is_dram)) +
       drain_C_(g_tp.w_comp_p,PCH,1, 1, g_tp.cell_h_def, is_dram) +
       gate_C(WmuxdrvNANDn+WmuxdrvNANDp,0, is_dram);
  power->readOp.dynamic += 0.5 * c2 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 4 * A;
  power->readOp.dynamic += c1 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd *  (A - 1);
  lkgCurrent += 0.5 * cmos_Ileak(g_tp.w_eval_inv_n,g_tp.w_eval_inv_p, is_dram) * 4 * A;
  lkgCurrent += 0.2 * 0.5 * cmos_Ileak(g_tp.w_comp_n, g_tp.w_comp_p, is_dram) * 4 * A;//stack factor of 0.2

  /* time to go to threshold of mux driver */
  tstep = (r2*c2+(r1+r2)*c1)*log(1.0/VTHMUXNAND);
  /* take into account non-zero input rise time */
  m = g_tp.peri_global.Vdd/nextinputtime;

  if((tstep) <= (0.5*(g_tp.peri_global.Vdd-g_tp.peri_global.Vth)/m)) 
  {
    a = m;
    b = 2*((g_tp.peri_global.Vdd*VTHEVALINV)-g_tp.peri_global.Vth);
    c = -2*(tstep)*(g_tp.peri_global.Vdd-g_tp.peri_global.Vth)+1/m*((g_tp.peri_global.Vdd*VTHEVALINV)-g_tp.peri_global.Vth)*((g_tp.peri_global.Vdd*VTHEVALINV)-g_tp.peri_global.Vth);
    Tcomparatorni = (-b+sqrt(b*b-4*a*c))/(2*a);
  }
  else
  {
    Tcomparatorni = (tstep) + (g_tp.peri_global.Vdd+g_tp.peri_global.Vth)/(2*m) - (g_tp.peri_global.Vdd*VTHEVALINV)/m;
  }
  *outputtime = Tcomparatorni/(1.0-VTHMUXNAND);
  *delay = Tcomparatorni+st1del+st2del+st3del;
  power->readOp.leakage = lkgCurrent * g_tp.peri_global.Vdd;
}



double objective_function(
    int flag_opt_for_dynamic_energy,
    int flag_opt_for_dynamic_power,
    int flag_opt_for_leakage_power,
    int flag_opt_for_cycle_time,
    double dynamic_energy_weight,
    double dynamic_power_weight, 
    double leakage_power_weight,
    double cycle_time_weight,
    double dyn_energy_wrt_min_dynamic_energy, 
    double dyn_power_wrt_min_dyn_power,
    double leak_power_wrt_min_leak_power, 
    double cycle_time_wrt_min_cycle_time)
{
    return dyn_energy_wrt_min_dynamic_energy*flag_opt_for_dynamic_energy*dynamic_energy_weight +
           dyn_power_wrt_min_dyn_power*flag_opt_for_dynamic_power*dynamic_power_weight + 
           leak_power_wrt_min_leak_power*flag_opt_for_leakage_power*leakage_power_weight + 
           cycle_time_wrt_min_cycle_time*flag_opt_for_cycle_time*cycle_time_weight;
}



double bitline_delay(
    int num_r_subarray,
    int num_c_subarray,
    int num_subarrays,
    double inrisetime,
    double *outrisetime,
    powerDef *power, 
    double *per_bitline_read_energy,
    int deg_bl_muxing,
    int deg_senseamp_muxing,
    int num_act_mats_hor_dir, 
    double *writeback_delay,
    int RWP,
    int ERP,
    int EWP,
    const Area & cell,
    bool  is_dram,
    double Cbitmetal,
    double Rbitmetal,
    double & Vbitsense,
    uint32_t ram_cell_tech_type)
{
  double Tbit, tau, v_bitline_precharge, v_th_mem_cell, v_wordline;
  double m, tstep;
  double dynRdEnergy = 0.0, dynWriteEnergy = 0.0;
  double Icell, Iport;
  double Cbitrow_drain_capacitance, R_cell_pull_down, R_cell_acc, Cbitline, 
         Rbitline, C_drain_bit_mux, R_bit_mux, C_drain_sense_amp_iso, R_sense_amp_iso, 
         C_sense_amp_latch, C_drain_sense_amp_mux, r_dev;
  double leakage_power_cc_inverters_sram_cell, 
         leakage_power_access_transistors_read_write_or_write_only_port_sram_cell,
         leakage_power_read_only_port_sram_cell, fraction;
  
  *writeback_delay  = 0;
  leakage_power_cc_inverters_sram_cell = 0;
  leakage_power_access_transistors_read_write_or_write_only_port_sram_cell = 0;
  leakage_power_read_only_port_sram_cell = 0;
  
  if (is_dram)
  {
    v_bitline_precharge = g_tp.dram.Vbitpre;
    v_th_mem_cell = g_tp.dram_acc.Vth;
    v_wordline = g_tp.vpp;
    Cbitrow_drain_capacitance = drain_C_(g_tp.dram.cell_a_w, NCH, 1, 0, cell.w,  true, true) / 2.0;  /*due to shared contact*/ 
    //The access transistor is not folded. So we just need to specify a theshold value for the
    //folding width that is equal to or greater than Wmemcella. 
    R_cell_acc = tr_R_on(g_tp.dram.cell_a_w, NCH, 1, true, true);
    if (ram_cell_tech_type == 4)
    {
      Cbitline = num_r_subarray * Cbitmetal;
    }
    else
    {
      Cbitline = num_r_subarray / 2 * Cbitrow_drain_capacitance + num_r_subarray * Cbitmetal;
    }
    Rbitline = 0;
    Rbitline = num_r_subarray * Rbitmetal;
    r_dev = g_tp.dram_cell_Vdd / g_tp.dram_cell_I_on + Rbitline / 2;
  }
  else
  { //SRAM
    v_bitline_precharge = g_tp.sram.Vbitpre;
    v_th_mem_cell = g_tp.sram_cell.Vth;
    v_wordline = g_tp.sram_cell.Vdd;
    Cbitrow_drain_capacitance = drain_C_(g_tp.sram.cell_a_w, NCH, 1, 0, cell.w, false, true) / 2.0;  /* due to shared contact */
    R_cell_pull_down = tr_R_on(g_tp.sram.cell_nmos_w, NCH, 1, false, true);
    R_cell_acc = tr_R_on(g_tp.sram.cell_a_w, NCH, 1, false, true);
    Cbitline = num_r_subarray * (Cbitrow_drain_capacitance + Cbitmetal);
    //Leakage current of an SRAM cell
    Iport = cmos_Ileak(g_tp.sram.cell_a_w, 0,  false, true); 
    Icell = cmos_Ileak(g_tp.sram.cell_nmos_w, g_tp.sram.cell_pmos_w, false, true);
    leakage_power_cc_inverters_sram_cell = Icell * g_tp.sram_cell.Vdd;
    leakage_power_access_transistors_read_write_or_write_only_port_sram_cell = Iport * g_tp.sram_cell.Vdd;
    leakage_power_read_only_port_sram_cell = 
        leakage_power_access_transistors_read_write_or_write_only_port_sram_cell * NAND2_LEAK_STACK_FACTOR;
  }

  
  Rbitline = num_r_subarray * Rbitmetal;
  C_drain_bit_mux = drain_C_(g_tp.w_nmos_b_mux, NCH, 1, 0, cell.w / (2 *(RWP + ERP + RWP)), is_dram);
  R_bit_mux = tr_R_on(g_tp.w_nmos_b_mux, NCH, 1, is_dram);
  C_drain_sense_amp_iso = drain_C_(g_tp.w_iso, PCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram);
  R_sense_amp_iso = tr_R_on(g_tp.w_iso, PCH, 1, is_dram);
  C_sense_amp_latch = gate_C(g_tp.w_sense_p + g_tp.w_sense_n, 0, is_dram) +
                      drain_C_(g_tp.w_sense_n, NCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram) + 
                      drain_C_(g_tp.w_sense_p, PCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram);
  C_drain_sense_amp_mux = drain_C_(g_tp.w_nmos_sa_mux, NCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram);
  if (is_dram) 
  {
    fraction = Vbitsense / ((g_tp.dram_cell_Vdd/2) * g_tp.dram_cell_C /(g_tp.dram_cell_C + Cbitline));
    //fraction = 1;
    tstep = 2.3 * fraction * r_dev * (g_tp.dram_cell_C * (Cbitline + 2 * C_drain_sense_amp_iso + 
      C_sense_amp_latch + C_drain_sense_amp_mux)) / (g_tp.dram_cell_C + (Cbitline + 2 * 
      C_drain_sense_amp_iso + C_sense_amp_latch + C_drain_sense_amp_mux));
    *writeback_delay = tstep;
    dynRdEnergy +=  (Cbitline + 2 * C_drain_sense_amp_iso + C_sense_amp_latch + C_drain_sense_amp_mux) * 
                    (g_tp.dram_cell_Vdd / 2) * g_tp.dram_cell_Vdd * num_c_subarray * 4 * num_act_mats_hor_dir;
    dynWriteEnergy += (Cbitline + 2 * C_drain_sense_amp_iso + C_sense_amp_latch ) * 
                    (g_tp.dram_cell_Vdd / 2) * g_tp.dram_cell_Vdd * num_c_subarray * 4 * num_act_mats_hor_dir;
    *per_bitline_read_energy = (Cbitline + 2 * C_drain_sense_amp_iso + C_sense_amp_latch + C_drain_sense_amp_mux) *
                               (g_tp.dram_cell_Vdd / 2) * g_tp.dram_cell_Vdd;
  }
  else
  {
    Vbitsense = (0.05 * g_tp.sram_cell.Vdd > VBITSENSEMIN) ? 0.05 * g_tp.sram_cell.Vdd : VBITSENSEMIN;
    if (deg_bl_muxing > 1)
    {
      tau = (R_cell_pull_down + R_cell_acc) * (Cbitline + 2 * C_drain_bit_mux + 
        2 * C_drain_sense_amp_iso + C_sense_amp_latch + C_drain_sense_amp_mux) + Rbitline * 
        (Cbitline / 2 + 2 * C_drain_bit_mux +   2 * C_drain_sense_amp_iso + C_sense_amp_latch +
        C_drain_sense_amp_mux)+ R_bit_mux * (C_drain_bit_mux + 2 * C_drain_sense_amp_iso +
        C_sense_amp_latch + C_drain_sense_amp_mux) + R_sense_amp_iso * (C_drain_sense_amp_iso + C_sense_amp_latch + 
        C_drain_sense_amp_mux);
      dynRdEnergy += (Cbitline + 2 * C_drain_bit_mux) * 2 * Vbitsense * g_tp.sram_cell.Vdd * 
        num_c_subarray * 4 * num_act_mats_hor_dir;
      dynRdEnergy += (2 * C_drain_sense_amp_iso + C_sense_amp_latch +  C_drain_sense_amp_mux) * 
        2 * Vbitsense * g_tp.sram_cell.Vdd * (num_c_subarray * 4 / deg_bl_muxing) *
        num_act_mats_hor_dir;
      dynWriteEnergy += (((num_c_subarray * 4 / deg_bl_muxing) / deg_senseamp_muxing) *
                         num_act_mats_hor_dir * Cbitline + 2 * C_drain_bit_mux) * g_tp.sram_cell.Vdd * g_tp.sram_cell.Vdd;
    }
    else
    { //deg_bl_muxing == 1
      tau = (R_cell_pull_down + R_cell_acc) * (Cbitline + C_drain_sense_amp_iso +
        C_sense_amp_latch + C_drain_sense_amp_mux) + Rbitline * Cbitline / 2 + 
        R_sense_amp_iso * (C_drain_sense_amp_iso + C_sense_amp_latch +   C_drain_sense_amp_mux);
      dynRdEnergy += (Cbitline + 2 * C_drain_sense_amp_iso + C_sense_amp_latch + 
        C_drain_sense_amp_mux) * 2 * Vbitsense * g_tp.sram_cell.Vdd * num_c_subarray * 4 * 
        num_act_mats_hor_dir;
      dynWriteEnergy += (((num_c_subarray * 4 / deg_bl_muxing) / deg_senseamp_muxing) *
                         num_act_mats_hor_dir * Cbitline) * g_tp.sram_cell.Vdd * g_tp.sram_cell.Vdd;

    }
    tstep = tau * log(v_bitline_precharge / (v_bitline_precharge - Vbitsense));
    //power->readOp.leakage = (Icell + Iport) * g_tp.sram_cell.Vdd;
    power->readOp.leakage = leakage_power_cc_inverters_sram_cell + 
      leakage_power_access_transistors_read_write_or_write_only_port_sram_cell + 
      leakage_power_access_transistors_read_write_or_write_only_port_sram_cell * 
      (RWP + EWP -1) + leakage_power_read_only_port_sram_cell * ERP;
  }
  
  /* take input rise time into account */
  m = v_wordline / inrisetime;
  if (tstep <= (0.5 * (v_wordline - v_th_mem_cell) / m))
  {
    Tbit = sqrt(2 * tstep * (v_wordline - v_th_mem_cell)/ m);
  }
  else
  {
    Tbit = tstep + (v_wordline - v_th_mem_cell) / (2 * m);
  }

  power->readOp.dynamic = dynRdEnergy;
  power->writeOp.dynamic = dynWriteEnergy;
  *outrisetime = 0;
  return(Tbit);
}


void delay_sense_amplifier(
    int num_c_subarray,
    int RWP,
    int ERP,
    double inrisetime,
    double *outrisetime,
    powerDef *power, 
    int deg_bl_muxing,
    int number_mats,
    int num_act_mats_hor_dir,
    double *delay,
    double *leak_power_sense_amps_closed_page_state, 
    double *leak_power_sense_amps_open_page_state,
    const Area & cell,
    bool  is_dram,
    bool  is_tag,
    double Vbitsense)
{
  double c_load;
  int    number_sense_amps_subarray;
  double IsenseEn, IsenseN, IsenseP, Iiso;
  double lkgIdlePh, lkgReadPh, lkgWritePh;
  double lkgRead, lkgIdle;
  double tau;
  number_sense_amps_subarray = num_c_subarray / deg_bl_muxing;//in a subarray

  //Bitline circuitry leakage. 
  Iiso = simplified_pmos_leakage(g_tp.w_iso, is_dram);
  IsenseEn = simplified_nmos_leakage(g_tp.w_sense_en, is_dram);
  IsenseN  = simplified_nmos_leakage(g_tp.w_sense_n, is_dram);
  IsenseP  = simplified_pmos_leakage(g_tp.w_sense_p, is_dram);
  
  lkgIdlePh = IsenseEn;//+ 2*IoBufP;
  lkgWritePh = Iiso + IsenseEn;// + 2*IoBufP + 2*Ipch;
  lkgReadPh = Iiso + IsenseN + IsenseP;//+ IoBufN + IoBufP + 2*IsPch ;
  lkgRead = lkgReadPh * number_sense_amps_subarray * 4 * num_act_mats_hor_dir + 
            lkgIdlePh * number_sense_amps_subarray * 4 * (number_mats - num_act_mats_hor_dir);
  lkgIdle = lkgIdlePh * number_sense_amps_subarray * 4 * number_mats;
  *leak_power_sense_amps_closed_page_state = lkgIdlePh * g_tp.peri_global.Vdd * number_sense_amps_subarray * 4;
  *leak_power_sense_amps_open_page_state   = lkgReadPh * g_tp.peri_global.Vdd * number_sense_amps_subarray * 4;
  // sense amplifier has to drive logic in "data out driver" and sense precharge load.
  // load seen by sense amp. New delay model for sense amp that is sensitive to both the output time 
  //constant as well as the magnitude of input differential voltage.
  *delay = 0;
  *outrisetime = 0;
  power->reset();
  c_load = gate_C(g_tp.w_sense_p + g_tp.w_sense_n, 0, is_dram) +
           drain_C_(g_tp.w_sense_n, NCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram) + 
           drain_C_(g_tp.w_sense_p, PCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram) +
           drain_C_(g_tp.w_iso,PCH,1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram) + 
           drain_C_(g_tp.w_nmos_sa_mux, NCH, 1, 0, cell.w * deg_bl_muxing / (RWP + ERP), is_dram);
  tau = c_load / g_tp.gm_sense_amp_latch;
  *delay = tau * log(g_tp.peri_global.Vdd / Vbitsense);
  power->readOp.dynamic = c_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * number_sense_amps_subarray * 4 * 
                          num_act_mats_hor_dir;
  power->readOp.leakage = lkgIdle * g_tp.peri_global.Vdd;
}


void delay_output_driver_at_subarray(
    int deg_bitline_muxing,
    int deg_sense_amp_muxing_level_1,
    int deg_sense_amp_muxing_level_2,
    int RWP,
    int ERP,
    double inrisetime,
    double *outrisetime,
    powerDef *power,
    const Dout_htree_node & htree_node,
    int number_mats,
    double *delay,
    double *delay_final_stage_subarray_output_driver,
    bool   is_dram,
    const Area & cell)
{
  uint32_t j;
  int flag_final_stage_subarray_output_driver;
  double c_load, rd, tf, this_delay, c_intrinsic, p_to_n_sizing_r;

  power->reset();
  *delay = 0;
  *delay_final_stage_subarray_output_driver = 0;
  flag_final_stage_subarray_output_driver = 0;
  
  p_to_n_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  
  // delay of signal through pass-transistor of first level of sense-amp mux to input of inverter-buffer.
  rd = tr_R_on(g_tp.w_nmos_sa_mux, NCH, 1, is_dram);
  c_load = deg_sense_amp_muxing_level_1 * drain_C_(g_tp.w_nmos_sa_mux, NCH, 1, 0, cell.w * deg_bitline_muxing / (RWP + ERP), is_dram) +
           gate_C(g_tp.min_w_nmos_ + p_to_n_sizing_r * g_tp.min_w_nmos_, 0.0, is_dram);
  tf = rd * c_load;
  this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  *delay += this_delay;
  inrisetime = this_delay/(1.0 - 0.5);
  power->readOp.dynamic += c_load * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  power->readOp.leakage += 0;  //for now, let leakage of the pass transistor be 0
  
  //Delay of signal through inverter-buffer to second level of sense-amp mux.
  //Internal delay of buffer
  rd = tr_R_on(g_tp.min_w_nmos_, NCH, 1, is_dram);
  c_load = drain_C_(g_tp.min_w_nmos_, NCH, 1, 1, g_tp.cell_h_def, is_dram) +
           drain_C_(p_to_n_sizing_r * g_tp.min_w_nmos_, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
           gate_C(g_tp.min_w_nmos_ + p_to_n_sizing_r * g_tp.min_w_nmos_, 0.0, is_dram);
  tf = rd * c_load;
  this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  *delay += this_delay;
  inrisetime = this_delay/(1.0 - 0.5);
  power->readOp.dynamic += c_load * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  power->readOp.leakage += cmos_Ileak(g_tp.min_w_nmos_, p_to_n_sizing_r * g_tp.min_w_nmos_, is_dram) * 0.5 * g_tp.peri_global.Vdd;
  //Inverter driving drain of pass transistor of second level of sense-amp mux.
  rd = tr_R_on(g_tp.min_w_nmos_, NCH, 1, is_dram);
  c_load = drain_C_(g_tp.min_w_nmos_, NCH, 1, 1, g_tp.cell_h_def, is_dram) +
           drain_C_(p_to_n_sizing_r * g_tp.min_w_nmos_, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
           drain_C_(g_tp.w_nmos_sa_mux, NCH, 1, 0, cell.w * deg_bitline_muxing * deg_sense_amp_muxing_level_1 / (RWP + ERP), is_dram);
  tf = rd * c_load;
  this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  *delay += this_delay;
  inrisetime = this_delay/(1.0 - 0.5);
  power->readOp.dynamic += c_load * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  power->readOp.leakage += cmos_Ileak(g_tp.min_w_nmos_, p_to_n_sizing_r * g_tp.min_w_nmos_, is_dram) * 0.5 * g_tp.peri_global.Vdd;

  //delay of signal through pass-transistor to input of subarray output driver.
  rd = tr_R_on(g_tp.w_nmos_sa_mux, NCH, 1, is_dram);
  c_load = deg_sense_amp_muxing_level_2 * drain_C_(g_tp.w_nmos_sa_mux, NCH, 1, 0, cell.w * deg_bitline_muxing * deg_sense_amp_muxing_level_1 / (RWP + ERP), is_dram) +
           gate_C(htree_node.width_n[0] + htree_node.width_p[0] + htree_node.width_nor2_n + htree_node.width_nor2_p, 0.0, is_dram);
  tf = rd * c_load;
  this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  *delay += this_delay;
  inrisetime = this_delay/(1.0 - 0.5);
  power->readOp.dynamic += c_load * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  power->readOp.leakage += 0;//for now, let leakage of the pass transistor be 0

  for (j = 0; j < htree_node.num_gates; ++j)
  {
    if(j == 0)
    { //NAND2 gate
      rd = tr_R_on(htree_node.width_n[j], NCH, 2, is_dram);
      if (htree_node.num_gates ==2)
      {
        c_load = gate_C(htree_node.width_p[j+1], 0.0, is_dram);//NAND2 drives PMOS of output stage
      }
      else
      {
        c_load = gate_C(htree_node.width_n[j+1] + htree_node.width_p[j+1], 0.0, is_dram);//NAND2 drives inverter
      }
      c_intrinsic = drain_C_(htree_node.width_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram) + 
                2 * drain_C_(htree_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load);
      power->readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      power->readOp.leakage += cmos_Ileak(htree_node.width_n[j], htree_node.width_p[j],  is_dram) * 0.5 * g_tp.peri_global.Vdd;
      power->readOp.leakage += cmos_Ileak(htree_node.width_nor2_n, htree_node.width_nor2_p, is_dram) * 0.5 * g_tp.peri_global.Vdd;
    }
    else if (j == htree_node.num_gates - 1)
    { //PMOS
      flag_final_stage_subarray_output_driver = 1;
      rd = tr_R_on(htree_node.width_p[j], PCH, 1, is_dram);
      c_load = htree_node.C_wire_ld + htree_node.C_gate_ld;
      c_intrinsic = drain_C_(htree_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                    drain_C_(htree_node.width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load) + htree_node.R_wire_ld * 
           (htree_node.C_wire_ld / 2 + htree_node.C_gate_ld +
            drain_C_(htree_node.width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram) + 
            drain_C_(htree_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram));
      power->readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      power->readOp.leakage += cmos_Ileak(htree_node.width_n[j], htree_node.width_p[j], is_dram) * 0.5 * g_tp.peri_global.Vdd;
      flag_final_stage_subarray_output_driver = 1;
    }
    else
    { //inverter
      rd = tr_R_on(htree_node.width_n[j], NCH, 1, is_dram);
      if (j == htree_node.num_gates - 2)
      { //inverter driving PMOS of output stage
        c_load = gate_C(htree_node.width_p[j+1], 0.0, is_dram);
      }
      else
      { //inverter driving inverter
        c_load = gate_C(htree_node.width_n[j+1] + htree_node.width_p[j+1], 0.0, is_dram);
      }
      c_intrinsic = drain_C_(htree_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                    drain_C_(htree_node.width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load);
      power->readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      power->readOp.leakage += cmos_Ileak(htree_node.width_n[j] + htree_node.width_n[j] / 2, 
          htree_node.width_p[j] + htree_node.width_p[j] /2, is_dram) * 0.5 * g_tp.peri_global.Vdd;
    }
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    *delay += this_delay;
    if (flag_final_stage_subarray_output_driver)
    {
      *delay_final_stage_subarray_output_driver = this_delay;
    }
    inrisetime = this_delay/(1.0 - 0.5);
  }
  
  *outrisetime = inrisetime;
}


void delay_routing_to_bank(
    int number_repeaters_htree_route_to_bank,
    double length_htree_route_to_bank,
    double sizing_repeater_htree_route_to_bank,
    double inrisetime,
    double *outrisetime,  
    double *delay,
    powerDef *power,
    bool   is_dram,
    const Area & cell)
{
  double rd, c_intrinsic, c_load, r_wire, tf, this_delay, p_to_n_sizing_r;
  int i;
  *delay = 0;
  
  p_to_n_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  //Add delay of cascade of inverters that drives the first repeater. FIX this.
  if (length_htree_route_to_bank > 0)
  {
    for (i = 0; i < number_repeaters_htree_route_to_bank; ++i)
    {
      rd = tr_R_on(sizing_repeater_htree_route_to_bank * g_tp.min_w_nmos_, NCH, 1, is_dram);
      c_intrinsic = drain_C_(sizing_repeater_htree_route_to_bank * p_to_n_sizing_r * g_tp.min_w_nmos_, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                    drain_C_(sizing_repeater_htree_route_to_bank * g_tp.min_w_nmos_, NCH, 1, 1, g_tp.cell_h_def, is_dram);
      c_load = sizing_repeater_htree_route_to_bank * gate_C(g_tp.min_w_nmos_ +
          p_to_n_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
      //The last repeater actually sees the load at the input of the bank as well. FIX this.
      //if(i == number_repeaters_htree_route_to_bank - 1){
      //c_load = ptr_intcnt_seg->c_gate_load;
      //}

      r_wire = (length_htree_route_to_bank / number_repeaters_htree_route_to_bank) * g_tp.wire_outside_mat.R_per_um;
      tf = rd * c_intrinsic + (rd + r_wire) * c_load;
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      *delay += this_delay;
      inrisetime = this_delay / (1.0 - 0.5);  
      power->readOp.dynamic += (c_intrinsic + c_load) * 0.5 *  g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      power->readOp.leakage += cmos_Ileak(
          sizing_repeater_htree_route_to_bank * g_tp.min_w_nmos_,
          sizing_repeater_htree_route_to_bank * p_to_n_sizing_r * g_tp.min_w_nmos_,  is_dram) * 0.5 * g_tp.peri_global.Vdd;
    }
    //From Ron Ho's thesis, energy-delay optimal repeaters improve dynamic energy approximately by 
    //30% at the expense of approx 10% increase in delay. We simply apply this correction at the end here. 
    //Assuming that leakage also improves by 30%. Doing it like this not right. FIX this - arrive at these numbers by deriving sizing, number of
    //repeaters....
    //*delay = 1.1 * (*delay);
    //power->readOp.dynamic = 0.7 * power->readOp.dynamic;
    //power->readOp.leakage = 0.7 * power->readOp.leakage;
  }
  *outrisetime = inrisetime;
}


void delay_fa_tag(
    int tagbits,
    int Ntbl,
    int num_r_subarray,
    double *delay,
    powerDef *power,
    const Area & cam_cell,
    bool is_dram,
    const Area & cell)
{
  double Tagdrive1, Tagdrive2, Tag1, Tag2, Tag3, outrisetime;
  double Ceq, Rwire, tf, nextinputtime, c_intrinsic, rd, Cwire, c_gate_load;
     
  double Wdecdrivep, Wdecdriven, Wfadriven, Wfadrivep, Wfadrive2n, Wfadrive2p, Wfadecdrive1n, Wfadecdrive1p, 
    Wfadecdrive2n, Wfadecdrive2p, Wfadecdriven, Wfadecdrivep, Wfaprechn, Wfaprechp,
    Wdummyn, Wdummyinvn, Wdummyinvp, Wfainvn, Wfainvp, Waddrnandn, Waddrnandp,
    Wfanandn, Wfanandp, Wfanorn, Wfanorp, Wdecnandn, Wdecnandp;

  double FACwordmetal, FACbitmetal, FARbitmetal, FARwordmetal, dynPower;
  int Htagbits;

  FACwordmetal = cam_cell.get_w() * g_tp.wire_local.C_per_um;
  FACbitmetal  = cam_cell.get_h() * g_tp.wire_local.C_per_um;
  FARwordmetal = cam_cell.get_w() * g_tp.wire_local.R_per_um;
  FARbitmetal  = cam_cell.get_h() * g_tp.wire_local.R_per_um;

  dynPower = 0.0;

  Wdecdrivep    =  450 * g_ip.F_sz_um;//this was 360 micron for the 0.8 micron process
  Wdecdriven    =  300 * g_ip.F_sz_um;//this was 240 micron for the 0.8 micron process
  Wfadriven     = 62.5 * g_ip.F_sz_um;//this was  50 micron for the 0.8 micron process
  Wfadrivep     =  125 * g_ip.F_sz_um;//this was 100 micron for the 0.8 micron process
  Wfadrive2n    =  250 * g_ip.F_sz_um;//this was 200 micron for the 0.8 micron process
  Wfadrive2p    =  500 * g_ip.F_sz_um;//this was 400 micron for the 0.8 micron process
  Wfadecdrive1n = 6.25 * g_ip.F_sz_um;//this was   5 micron for the 0.8 micron process
  Wfadecdrive1p = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process
  Wfadecdrive2n =   25 * g_ip.F_sz_um;//this was  20 micron for the 0.8 micron process
  Wfadecdrive2p =   50 * g_ip.F_sz_um;//this was  40 micron for the 0.8 micron process  
  Wfadecdriven  = 62.5 * g_ip.F_sz_um;//this was  50 micron for the 0.8 micron process
  Wfadecdrivep  =  125 * g_ip.F_sz_um;//this was 100 micron for the 0.8 micron process
  Wfaprechn     =  7.5 * g_ip.F_sz_um;//this was   6 micron for the 0.8 micron process
  Wfaprechp     = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process
  Wdummyn       = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process
  Wdummyinvn    =   75 * g_ip.F_sz_um;//this was  60 micron for the 0.8 micron process  
  Wdummyinvp    =  100 * g_ip.F_sz_um;//this was  80 micron for the 0.8 micron process
  Wfainvn       = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process  
  Wfainvp       =   25 * g_ip.F_sz_um;//this was  20 micron for the 0.8 micron process  
  Waddrnandn    = 62.5 * g_ip.F_sz_um;//this was  50 micron for the 0.8 micron process  
  Waddrnandp    = 62.5 * g_ip.F_sz_um;//this was  50 micron for the 0.8 micron process  
  Wfanandn      =   25 * g_ip.F_sz_um;//this was  20 micron for the 0.8 micron process  
  Wfanandp      = 37.5 * g_ip.F_sz_um;//this was  30 micron for the 0.8 micron process
  Wfanorn       = 6.25 * g_ip.F_sz_um;//this was   5 micron for the 0.8 micron process  
  Wfanorp       = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process
  Wdecnandn     = 12.5 * g_ip.F_sz_um;//this was  10 micron for the 0.8 micron process  
  Wdecnandp     = 37.5 * g_ip.F_sz_um;//this was  30 micron for the 0.8 micron process

  Htagbits = (int)(ceil ((double) (tagbits) / 2.0));

  //please refer to CACTI TR 2 and 3 for the implementation.
  /* First stage, From the driver(am and an) to the comparators in all the rows including the dummy row, 
     Assuming that comparators in both the normal matching line and the dummy matching line have the same sizing */ 
  nextinputtime = 0;
  Ceq = drain_C_(Wfadecdrive2p, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
        drain_C_(Wfadecdrive2n, NCH, 1, 1, g_tp.cell_h_def, is_dram) +
        gate_C(Wfadecdrivep + Wfadecdriven, 0, is_dram);
  tf  = Ceq * tr_R_on(Wfadecdrive2n, NCH, 1, is_dram);
  Tagdrive1     = horowitz(nextinputtime, tf, VSINV, VTHFA1, FALL);
  nextinputtime = Tagdrive1 / VTHFA1;
  dynPower     += Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * tagbits * Ntbl;
  
  rd = tr_R_on(Wfadecdrivep, PCH, 1, is_dram);
  c_intrinsic = drain_C_(Wfadecdrivep, PCH, 1, 1, g_tp.cell_h_def, is_dram) +
                drain_C_(Wfadecdriven, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  c_gate_load = gate_C(Wdummyn, 0, is_dram) * 2 * (num_r_subarray + 1);
  Cwire = FACbitmetal * 2 * (num_r_subarray + 1);
  Rwire = FARbitmetal * (num_r_subarray + 1);
  tf = rd * (c_intrinsic + Ceq) + Rwire * (Cwire / 2 + c_gate_load);
  Tagdrive2 = horowitz(nextinputtime, tf, VTHFA1, VTHFA2, RISE);
  nextinputtime = Tagdrive2 / (1 - VTHFA2);
  dynPower += (c_intrinsic + Cwire + c_gate_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * tagbits * Ntbl;

  /* second stage, from the trasistors in the comparators(both normal row and dummy row) to the NAND gates that combins both half*/
  rd =  tr_R_on(Wdummyn, NCH, 2, is_dram);
  c_intrinsic = Htagbits*2*drain_C_(Wdummyn, NCH, 2, 1, g_tp.cell_h_def, is_dram) +
                drain_C_(Wfaprechp, PCH, 1, 1, g_tp.cell_h_def, is_dram);
  Cwire = FACwordmetal * Htagbits;
  Rwire = FARwordmetal * Htagbits;
  c_gate_load = gate_C(Waddrnandn + Waddrnandp, 0, is_dram);
  tf = rd * (c_intrinsic + Ceq) + Rwire * (Cwire / 2 + c_gate_load);
  Tag1 = horowitz(nextinputtime, tf, VTHFA2, VTHFA3, FALL);
  nextinputtime = Tag1 / VTHFA3;
  dynPower += Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * num_r_subarray * Ntbl;

  /* third stage, from the NAND2 gates to the drivers in the dummy row */
  rd = tr_R_on(Waddrnandn, NCH, 2, is_dram);
  c_intrinsic = drain_C_(Waddrnandn, NCH, 2, 1, g_tp.cell_h_def, is_dram) +
                drain_C_(Waddrnandp, PCH, 1, 1, g_tp.cell_h_def, is_dram)*2;
  c_gate_load = gate_C(Wdummyinvn + Wdummyinvp, 0, is_dram);
  tf = rd * (c_intrinsic + c_gate_load);
  Tag2 = horowitz(nextinputtime, tf, VTHFA3, VTHFA4, RISE);
  nextinputtime = Tag2 / (1 - VTHFA4);
  dynPower += Ceq * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * num_r_subarray * Ntbl;

  /* fourth stage, from the driver in dummy matchline to the NOR2 gate which drives the wordline of the data portion  */
  rd = tr_R_on(Wdummyinvn, NCH, 1, is_dram);
  c_intrinsic = drain_C_(Wdummyinvn, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  Cwire = FACwordmetal * Htagbits +  FACbitmetal * num_r_subarray;
  Rwire = FARwordmetal * Htagbits +  FARbitmetal * num_r_subarray;
  c_gate_load = gate_C(Wfanorn + Wfanorp, 0, is_dram);
  tf = rd * (c_intrinsic + Cwire) + Rwire * (Cwire / 2 + c_gate_load);
  Tag3 = horowitz (nextinputtime, tf, VTHFA4, VTHFA5, FALL);
  outrisetime = Tag3 / VTHFA5;
  dynPower += (c_intrinsic + Cwire + c_gate_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;

  *delay = Tagdrive1 + Tagdrive2 + Tag1 + Tag2 + Tag3;
  power->readOp.dynamic = dynPower;
}




/*======================================================================*/

bool calculate_time(
    bool is_tag,
    int pure_ram,
    double Nspd,
    unsigned int Ndwl, 
    unsigned int Ndbl,
    unsigned int Ndcm,
    unsigned int Ndsam_lev_1,
    unsigned int Ndsam_lev_2,
    mem_array *ptr_array,
    int flag_results_populate,
    results_mem_array *ptr_results,
    final_results *ptr_fin_res,
    const ArrayEdgeToBankEdgeHtreeSizing & arr_edge_to_bank_edge_htree_sizing,
    const BankHtreeSizing & bank_htree_sizing,
    bool is_main_mem)
{
  uint32_t ram_cell_tech_type  = (is_tag) ? g_ip.tag_arr_ram_cell_tech_type : g_ip.data_arr_ram_cell_tech_type;
  bool     is_dram             = ((ram_cell_tech_type == lp_dram) || (ram_cell_tech_type == comm_dram));

  Area cell;

  powerDef tot_power_addr_vertical_htree, tot_power_datain_vertical_htree,
           tot_power_bitlines, tot_power_sense_amps, tot_power_subarray_output_drivers, 
           tot_power_dataout_vertical_htree, tot_power_comparators, tot_power_crossbar;

  powerDef tot_power, tot_power_row_predecode_block_drivers,
           tot_power_bit_mux_predecode_block_drivers, 
           tot_power_senseamp_mux_lev_1_predecode_block_drivers,
           tot_power_senseamp_mux_lev_2_predecode_block_drivers,
           tot_power_row_predecode_blocks, tot_power_bit_mux_predecode_blocks,
           tot_power_senseamp_mux_lev_1_predecode_blocks,
           tot_power_senseamp_mux_lev_2_predecode_blocks,
           tot_power_row_decoders, 
           tot_power_bit_mux_decoders,
           tot_power_senseamp_mux_lev_1_decoders, 
           tot_power_senseamp_mux_lev_2_decoders, 
           tot_power_bitlines_precharge_eq_driver;

  
  double access_time = 0;
  double delay_bitline = 0.0;
  powerDef sense_amp_data_power,  bitline_data_power;
  double cycle_time = 0.0;
  double outrisetime = 0.0, inrisetime = 0.0;
  double Tpre;
  double writeback_delay;
  int    num_r_subarray, num_c_subarray, deg_bl_muxing, deg_sa_mux_l1_non_assoc;

  double c_wl_drv_ld, R_wire_wl_drv_out, R_wire_bit_mux_dec_out, R_wire_sa_mux_dec_out;
  double row_dec_outrisetime, bit_mux_dec_outrisetime,
         senseamp_mux_lev_1_dec_outrisetime, senseamp_mux_lev_2_dec_outrisetime;
  double delay_addr_din_vertical_htree, delay_dout_vertical_htree, delay_before_subarray_output_driver,
         delay_from_subarray_output_driver_to_output;
  
  double max_delay_before_row_decoder, max_delay_before_bit_mux_decoder, 
         max_delay_before_senseamp_mux_lev_1_decoder,
         max_delay_before_senseamp_mux_lev_2_decoder,
         delay_within_mat_before_row_decoder,
         delay_within_mat_before_bit_mux_decoder,
         delay_within_mat_before_senseamp_mux_lev_1_decoder,
         delay_within_mat_before_senseamp_mux_lev_2_decoder;
  double row_dec_inrisetime, bit_mux_dec_inrisetime,
         senseamp_mux_lev_1_dec_inrisetime,
         senseamp_mux_lev_2_dec_inrisetime;
  double delay_data_access_row_path, delay_data_access_col_path,
         delay_data_access_senseamp_mux_lev_1_path, delay_data_access_senseamp_mux_lev_2_path,
         temp_delay_data_access_path, delay_data_access_path; 

  powerDef power_addr_datain_htree, power_dout_htree, power_output_drivers_at_subarray;

  int number_addr_bits_mat, num_di_b_mat, num_do_b_mat, 
      num_subarrays, number_mats, number_sense_amps_subarray,
      number_output_drivers_subarray,
      number_way_select_signals_mat = 0;

  double delay_dout_horizontal_htree;

  int num_v_htree_nodes, num_h_htree_nodes, num_tristate_h_htree_nodes,
      num_mats_h_dir, num_mats_v_dir, 
      num_act_mats_hor_dir, way_select = 0;
  int number_addr_bits_row_decode, number_subbanks, 
      number_subbanks_decode, number_addr_bits_routed_to_bank,
      num_comp_b_routed_to_bank, number_data_bits_routed_to_bank, 
      number_bits_routed_to_bank,
      num_do_b_subbank, num_di_b_subbank;
  
  double length_htree_route_to_bank;
  
  double ver_htree_seg_len, hor_htree_seg_len, delay_addr_din_horizontal_htree;
  powerDef power_addr_datain_horizontal_htree;
  powerDef power_addr_datain_horizontal_htree_node, power_addr_datain_vertical_htree_node, power_dout_vertical_htree_node;
  
  
  int    tagbits,  k, htree_seg_multiplier;
  double Cbitrow_drain_cap, Cbitline;
  double delay_route_to_bank, wordline_data, delay_subarray_output_driver, delay_final_stage_subarray_output_driver,
         delay_sense_amp,  multisubbank_interleave_cycle_time;
  powerDef tot_power_addr_horizontal_htree, tot_power_datain_horizontal_htree,
           tot_power_dataout_horizontal_htree, tot_power_routing_to_bank;
  double comparator_delay;
  powerDef comparator_power;
  
  double area_all_dataramcells;
  
  double c_load, rd, c_intrinsic, tf, wordline_reset_delay;
  double dummy_precharge_outrisetime;
  
  double r_bitline_precharge, r_bitline, bitline_restore_delay;
  
  double temp, dram_array_availability;
  
  double leakage_power_cc_inverters_sram_cell, leakage_power_access_transistors_read_write_port_sram_cell,
         leakage_power_read_only_port_sram_cell, length_htree_segment;

  double horizontal_htree_input_load, htree_output_load;
  int    number_redundant_mats;
  double per_bitline_read_energy;
  double t_rcd = 0, cas_latency = 0, precharge_delay = 0;
  double dyn_read_energy_from_closed_page;
  double dyn_read_energy_from_open_page;
  double leak_power_subbank_closed_page;
  double leak_power_subbank_open_page;
  double leak_power_request_and_reply_networks;
  double leak_power_sense_amps_closed_page_state;
  double leak_power_sense_amps_open_page_state;
  double dyn_read_energy_remaining_words_in_burst;
  
  int    number_addr_bits_routed_to_bank_for_activate, number_addr_bits_routed_to_bank_for_read_or_write,
         number_addr_bits_routed_to_mat_for_activate,  number_addr_bits_routed_to_mat_for_read_or_write;
  double activate_energy, routing_to_bank_for_activate_energy, hor_htree_routing_e_for_act,
         vertical_htree_routing_energy_for_activate, read_energy, routing_addr_to_bank_for_read_or_write_energy,
         routing_datain_bits_to_bank_energy_for_write, horizontal_addr_htree_routing_energy_for_read_or_write, 
         vertical_addr_htree_routing_energy_for_read_or_write, vertical_dataout_htree_routing_energy_for_read,
         horizontal_dataout_htree_energy_for_read, write_energy, horizontal_datain_htree_routing_energy_for_write,
         vertical_datain_htree_routing_energy_for_write, precharge_energy;
  double delay_fa_decoder, p_to_n_sizing_r;
  powerDef power_fa_decoder;
  double delay_request_network, delay_inside_mat, delay_reply_network;
  
  unsigned int capacity_per_die = g_ip.cache_sz / NUMBER_STACKED_DIE_LAYERS;  // capacity per stacked die layer
  
  const TechnologyParameter::InterconnectType & wire_local       = g_tp.wire_local;
  const TechnologyParameter::InterconnectType & wire_inside_mat  = g_tp.wire_inside_mat;
  const TechnologyParameter::InterconnectType & wire_outside_mat = g_tp.wire_outside_mat;
  
  bool fully_assoc = (g_ip.fully_assoc) ? true : false;

  if (fully_assoc)
  { // fully-assocative cache
    if (Ndwl != 1 ||            //Ndwl is fixed to 1 for FA 
        Ndcm != 1 ||            //Ndcm is fixed to 1 for FA
        Nspd < 1 || Nspd > 1 || //Nspd is fixed to 1 for FA
        Ndsam_lev_1 != 1 ||     //Ndsam_lev_1 is fixed to one
        Ndsam_lev_2 != 1 ||     //Ndsam_lev_2 is fixed to one
        Ndbl < 2)
    {
      return false;
    }
  }

  if ((is_dram) && (!is_tag) && (Ndcm > 1))
  {
    return false;  // For DRAM data array, Ndcm has to be 1
  }
  // If it's not an FA tag/data array, Ndwl should be at least two and Ndbl should be
  // at least two because an array is assumed to have at least one mat. And a mat
  // is formed out of two horizontal subarrays and two vertical subarrays
  if((!fully_assoc)&&(Ndwl == 1 || Ndbl == 1))
  {
    return false;
  }

  tagbits = 0; // if data array, let tagbits = 0
  if(is_tag)
  {
    if (g_ip.specific_tag)
    {
      tagbits = g_ip.tag_w;
    }
    else
    {
      if (fully_assoc)
      {
        tagbits = ADDRESS_BITS + EXTRA_TAG_BITS - _log2(g_ip.block_sz);
      }
      else
      {
        tagbits = ADDRESS_BITS + EXTRA_TAG_BITS - _log2(capacity_per_die) + 
                  _log2(g_ip.tag_assoc*2 - 1) - _log2(g_ip.nbanks);
      }
    }
    tagbits = (((tagbits + 3) >> 2) << 2);

    if (fully_assoc)
    {
      num_r_subarray = (int)(capacity_per_die / (g_ip.block_sz * Ndbl));
      num_c_subarray = (int)((tagbits * Nspd / Ndwl) + EPSILON);
    }
    else
    {
      num_r_subarray = (int)(capacity_per_die / (g_ip.nbanks *
        g_ip.block_sz * g_ip.tag_assoc * Ndbl * Nspd) + EPSILON);
      num_c_subarray = (int)((tagbits * g_ip.tag_assoc * Nspd / Ndwl) + EPSILON);
    }
    //burst_length = 1;
  }
  else
  {
    if (fully_assoc)
    {
      num_r_subarray = (int) (capacity_per_die) / (g_ip.block_sz * Ndbl);
      num_c_subarray = 8 * g_ip.block_sz;
    }
    else
    {
      num_r_subarray = (int)(capacity_per_die / (g_ip.nbanks * 
            g_ip.block_sz * g_ip.data_assoc * Ndbl * Nspd) + EPSILON);
      num_c_subarray = (int)((8 * g_ip.block_sz * g_ip.data_assoc * Nspd / Ndwl) + EPSILON);
    }
    //burst_length = g_ip.block_sz * 8 / g_ip.out_w;
  }    
  
  if ((!fully_assoc)&&(num_r_subarray < MINSUBARRAYROWS)) {return false;}
  if (num_r_subarray == 0) {return false;}
  if (num_r_subarray > MAXSUBARRAYROWS) {return false;}
  if (num_c_subarray < MINSUBARRAYCOLS) {return false;}
  if (num_c_subarray > MAXSUBARRAYCOLS) {return false;}
  
  num_subarrays = Ndwl * Ndbl;  
  
  // calculate wire parameters
  if(is_tag)
  {
    cell.h = g_tp.sram.b_h + 2 * wire_local.pitch * (g_ip.num_rw_ports - 1 + g_ip.num_rd_ports);
    cell.w = g_tp.sram.b_w + 2 * wire_local.pitch * (g_ip.num_rw_ports - 1 + 
                                                     (g_ip.num_rd_ports - g_ip.num_se_rd_ports)) + 
             wire_local.pitch * g_ip.num_se_rd_ports;
  }
  else
  {
    if(is_dram)
    {
      cell.h = g_tp.dram.b_h;
      cell.w = g_tp.dram.b_w;
    }
    else
    {
      cell.h = g_tp.sram.b_h + 2 * wire_local.pitch * (g_ip.num_wr_ports + 
               g_ip.num_rw_ports - 1 + g_ip.num_rd_ports);
      cell.w = g_tp.sram.b_w + 2 * wire_local.pitch * (g_ip.num_rw_ports - 1 + 
               (g_ip.num_rd_ports - g_ip.num_se_rd_ports) + 
               g_ip.num_wr_ports) + g_tp.wire_local.pitch * g_ip.num_se_rd_ports;
    }
  }

  double c_b_metal = cell.h * wire_local.C_per_um;
  double c_w_metal = cell.w * wire_local.C_per_um;
  double r_b_metal = cell.h * wire_local.R_per_um;
  double r_w_metal = cell.w * wire_local.R_per_um;
  double v_b_sense = 0;
  double dram_refresh_period = 0;  // in second

  if (is_dram)
  {
    deg_bl_muxing = 1;
    if (ram_cell_tech_type == comm_dram)
    {
      Cbitline  = num_r_subarray * c_b_metal;
      v_b_sense = (g_tp.dram_cell_Vdd/2) * g_tp.dram_cell_C / (g_tp.dram_cell_C + Cbitline);
      if (v_b_sense < VBITSENSEMIN)
      {
        return false;
      }
      v_b_sense = VBITSENSEMIN;  // in any case, we fix sense amp input signal to a constant value
      dram_refresh_period = 64e-3;
    }
    else
    {
      Cbitrow_drain_cap = drain_C_(g_tp.dram.cell_a_w, NCH, 1, 0, cell.w, true, true) / 2.0;
      Cbitline  = num_r_subarray * (Cbitrow_drain_cap + c_b_metal);
      v_b_sense = (g_tp.dram_cell_Vdd/2) * g_tp.dram_cell_C /(g_tp.dram_cell_C + Cbitline);
      if (v_b_sense < VBITSENSEMIN)
      {
        return false; //Sense amp input signal is smaller that minimum allowable sense amp input signal
      }
      v_b_sense = VBITSENSEMIN; // in any case, we fix sense amp input signal to a constant value
      //v_storage_worst = g_tp.dram_cell_Vdd / 2 - VBITSENSEMIN * (g_tp.dram_cell_C + Cbitline) / g_tp.dram_cell_C;
      //dram_refresh_period = 1.1 * g_tp.dram_cell_C * v_storage_worst / g_tp.dram_cell_I_off_worst_case_len_temp;
      dram_refresh_period = 0.9 * g_tp.dram_cell_C * VDD_STORAGE_LOSS_FRACTION_WORST * g_tp.dram_cell_Vdd / g_tp.dram_cell_I_off_worst_case_len_temp;
    }
  }
  else
  { //SRAM
    deg_bl_muxing = Ndcm;
    // "/ 2.0" below is due to the fact that two adjacent access transistors share drain
    // contacts in a physical layout
    Cbitrow_drain_cap = drain_C_(g_tp.sram.cell_a_w, NCH, 1, 0, cell.w, false, true) / 2.0;
    Cbitline = num_r_subarray * (Cbitrow_drain_cap + c_b_metal);
    dram_refresh_period = 0;
  }

  if (fully_assoc)
  {
    num_mats_h_dir = 1;
    num_mats_v_dir = MAX(Ndbl / 2, 1);
    number_mats = num_mats_h_dir * num_mats_v_dir;
    num_do_b_mat = 8 * g_ip.block_sz;
  }
  else
  {
    num_mats_h_dir = Ndwl / 2;
    num_mats_v_dir = Ndbl / 2;
    number_mats = num_mats_h_dir * num_mats_v_dir;
    num_do_b_mat = MAX(4 * num_c_subarray / (deg_bl_muxing * Ndsam_lev_1 * Ndsam_lev_2), 1);
  }
  if(!(fully_assoc&&is_tag) && (num_do_b_mat < 4))
  {
    return false;
  }
   

  if (!is_tag)
  {
    if (is_main_mem == true)
    {
      num_do_b_subbank = g_ip.int_prefetch_w * g_ip.out_w;
      deg_sa_mux_l1_non_assoc = Ndsam_lev_1;
    }
    else
    {
      if (g_ip.fast_access == true)
      {
        num_do_b_subbank = g_ip.out_w * g_ip.data_assoc;
        deg_sa_mux_l1_non_assoc = Ndsam_lev_1;
      }
      else
      {
        if (!fully_assoc)
        {
          num_do_b_subbank = g_ip.out_w;
          deg_sa_mux_l1_non_assoc = Ndsam_lev_1 / g_ip.data_assoc;
          if (deg_sa_mux_l1_non_assoc < 1)
          {
            return false;
          }
        }
        else
        {
          num_do_b_subbank = 8 * g_ip.block_sz;
          deg_sa_mux_l1_non_assoc = 1;
        }
      }
    }
  }
  else
  {
    num_do_b_subbank = tagbits * g_ip.tag_assoc;
    if (fully_assoc == false && (num_do_b_mat < tagbits))
    {
      return false;
    }
    deg_sa_mux_l1_non_assoc = Ndsam_lev_1;
    //num_do_b_mat = g_ip.tag_assoc / num_mats_h_dir;
  }

  if (fully_assoc)
  {
    num_act_mats_hor_dir = 1;
  }
  else
  {
    num_act_mats_hor_dir = num_do_b_subbank / num_do_b_mat;
    if (num_act_mats_hor_dir == 0)
    {
      return false;
    }
  }

    
  if(is_tag)
  {
    if(fully_assoc)
    {
      num_do_b_mat = 0;
      num_do_b_subbank = 0;
    }
    else
    {
      num_do_b_mat = g_ip.tag_assoc / num_act_mats_hor_dir;
      num_do_b_subbank = num_act_mats_hor_dir * num_do_b_mat;
    }
  }

  if ((g_ip.is_cache == false && is_main_mem == true) || (PAGE_MODE == 1 && is_dram))
  {
    if (num_act_mats_hor_dir * num_do_b_mat * Ndsam_lev_1 * Ndsam_lev_2 != g_ip.page_sz_bits)
    {
      return false;
    }
  }

  if (is_tag == false && g_ip.is_cache == true &&
      num_act_mats_hor_dir*num_do_b_mat*Ndsam_lev_1*Ndsam_lev_2 < (g_ip.out_w * g_ip.burst_len * g_ip.data_assoc))
  {
    return false;
  }

  if (num_act_mats_hor_dir > num_mats_h_dir) 
  {
    return false;
  }

  if(!is_tag)
  {
    if(g_ip.fast_access == true)
    {
      num_di_b_mat = num_do_b_mat / g_ip.data_assoc;
    }
    else
    {
      num_di_b_mat = num_do_b_mat;
    }
  }
  else
  {
    num_di_b_mat = tagbits;
  }
  num_di_b_subbank = num_di_b_mat * num_act_mats_hor_dir;

  if (fully_assoc)
  {
    number_addr_bits_row_decode = 0;
  }
  else
  {
    number_addr_bits_row_decode = _log2(num_r_subarray);
  }

  number_subbanks        = number_mats / num_act_mats_hor_dir;
  number_subbanks_decode = _log2(number_subbanks);

  if (is_dram && is_main_mem)
  {
    number_addr_bits_mat = MAX((unsigned int) number_addr_bits_row_decode,
                               _log2(deg_bl_muxing) + _log2(deg_sa_mux_l1_non_assoc) + _log2(Ndsam_lev_2));
  }
  else
  {
    number_addr_bits_mat = number_addr_bits_row_decode + _log2(deg_bl_muxing) +
                           _log2(deg_sa_mux_l1_non_assoc) + _log2(Ndsam_lev_2);
  }
  number_addr_bits_routed_to_bank = number_addr_bits_mat + number_subbanks_decode;
  num_comp_b_routed_to_bank = 0;
  
  if((!is_tag)&&(!pure_ram))
  {
    num_comp_b_routed_to_bank = g_ip.data_assoc;
  }

  if(is_tag)
  {
    number_data_bits_routed_to_bank = tagbits + g_ip.data_assoc;//input to tag
    //array= tagbits, output from tag array = g_ip.data_assoc number of comparator
    //bits for set-associative cache. g_ip.data_assoc = 1 valid bit for direct-mapped
    //cache or sequential-access cache.
  }
  else
  {
    number_data_bits_routed_to_bank = g_ip.out_w * 2;//datain and dataout
  }
  number_bits_routed_to_bank = number_addr_bits_routed_to_bank + number_data_bits_routed_to_bank +
                               num_comp_b_routed_to_bank;
  
  number_sense_amps_subarray = num_c_subarray / deg_bl_muxing;
  number_output_drivers_subarray = number_sense_amps_subarray / (Ndsam_lev_1 * Ndsam_lev_2);
  if ((!is_tag) && (g_ip.data_assoc > 1) && (!g_ip.fast_access))
  {
    way_select = g_ip.data_assoc;
    number_way_select_signals_mat = g_ip.data_assoc;
  }    
  
  if (is_dram)
  {
    c_wl_drv_ld = (gate_C_pass(g_tp.dram.cell_a_w, g_tp.dram.b_w, true, true) + c_w_metal) * num_c_subarray;
  }
  else
  {
    c_wl_drv_ld = (gate_C_pass(g_tp.sram.cell_a_w, (g_tp.sram.b_w-2*g_tp.sram.cell_a_w)/2.0, false, true)*2 +
                   c_w_metal) * num_c_subarray;
  }

  // add ECC adjustment to all data signals that traverse on H-trees.
  if (add_ecc_b_ == true)
  {
    num_do_b_mat += (int) (ceil(num_do_b_mat / num_bits_per_ecc_b_));
    num_di_b_mat += (int) (ceil(num_di_b_mat / num_bits_per_ecc_b_));
    number_data_bits_routed_to_bank += (int) (ceil(number_data_bits_routed_to_bank / num_bits_per_ecc_b_));
    number_bits_routed_to_bank      += (int) (ceil(number_bits_routed_to_bank  / num_bits_per_ecc_b_));
    num_di_b_subbank += (int) (ceil(num_di_b_subbank / num_bits_per_ecc_b_));
    num_do_b_subbank += (int) (ceil(num_do_b_subbank / num_bits_per_ecc_b_));
  }
  
  double refresh_power = 0;
  hor_htree_routing_e_for_act = 0;
  horizontal_addr_htree_routing_energy_for_read_or_write = 0;
  horizontal_datain_htree_routing_energy_for_write = 0;
  horizontal_dataout_htree_energy_for_read = 0;
  vertical_htree_routing_energy_for_activate = 0;
  vertical_addr_htree_routing_energy_for_read_or_write = 0;
  vertical_datain_htree_routing_energy_for_write = 0;

  inrisetime = 0;
  rd = tr_R_on(1, NCH, 1, is_dram);
  p_to_n_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  c_load = gate_C(1 + p_to_n_sizing_r, 0.0, is_dram);
  tf = rd * c_load;
  g_tp.kinv = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  double KLOAD = 1;
  c_load = KLOAD * (drain_C_(1, NCH, 1, 0, g_tp.cell_h_def, is_dram) + 
                    drain_C_(p_to_n_sizing_r, PCH, 1, 0, g_tp.cell_h_def, is_dram) +
                    gate_C(4 * (1 + p_to_n_sizing_r), 0.0, is_dram));
  tf = rd * c_load;
  g_tp.FO4 = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  c_load = KLOAD * (drain_C_(1, NCH, 1, 0, g_tp.cell_h_def, is_dram) + 
                    drain_C_(p_to_n_sizing_r, PCH, 1, 0, g_tp.cell_h_def, is_dram) +
                    gate_C(1 * (1 + p_to_n_sizing_r), 0.0, is_dram));
  tf = rd * c_load;
  g_tp.unit_len_wire_del = g_tp.wire_inside_mat.R_per_um * g_tp.wire_inside_mat.C_per_um / 2;
  //normalized_unit_length_wire_delay = unit_length_wire_delay / FO4;
  //FO1 = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
   
  Area subarray;
  Area cam_cell;

  if(fully_assoc && is_tag)
  {
    cam_cell              = subarray.set_area_fa_subarray(tagbits, num_r_subarray);
    area_all_dataramcells = cam_cell.get_h() * cam_cell.get_w() * num_r_subarray * num_c_subarray *
                            num_subarrays * g_ip.nbanks;
  }
  else
  {
    subarray.set_subarraymem_area(num_r_subarray, num_c_subarray, cell, ram_cell_tech_type);
    area_all_dataramcells = cell.get_h() * cell.get_w() * num_r_subarray * num_c_subarray *
                            num_subarrays * g_ip.nbanks;
  }

  // num_v_htree_nodes = 1 when num_mats_v_dir = 1 or 2
  num_v_htree_nodes = (num_mats_v_dir <= 1) ? 1 : _log2(num_mats_v_dir / 2) + 1;
  num_h_htree_nodes = _log2(num_mats_h_dir) + 1;
  num_tristate_h_htree_nodes = num_h_htree_nodes - (_log2(num_act_mats_hor_dir) + 1);

  Dout_htree_node subarray_out_htree_node(subarray, is_dram);

  double C_ld_bit_mux_dec_out      = 0;  // c_load_bit_mux_decoder_output
  double C_ld_sa_mux_lev_1_dec_out = 0;  // c_load_senseamp_mux_decoder_output
  double C_ld_sa_mux_lev_2_dec_out = 0;  // c_load_senseamp_mux_decoder_output
  if (deg_bl_muxing > 1)
  {
    C_ld_bit_mux_dec_out =
      (2 * 4 * num_c_subarray / deg_bl_muxing)*gate_C(g_tp.w_nmos_b_mux, 0, is_dram) +  // 4 subarrays per mat, 2 transistor per cell
      2*num_c_subarray*wire_inside_mat.C_per_um*cell.get_w(); 
  }
  
  if (Ndsam_lev_1 > 1)
  {
    C_ld_sa_mux_lev_1_dec_out =
      (4 * number_sense_amps_subarray / Ndsam_lev_1)*gate_C(g_tp.w_nmos_sa_mux, 0, is_dram) +
      2*num_c_subarray*wire_inside_mat.C_per_um*cell.get_w();
  }
  if (Ndsam_lev_2 > 1)
  {
    C_ld_sa_mux_lev_2_dec_out =
      (4 * number_sense_amps_subarray / (Ndsam_lev_1*Ndsam_lev_2))*gate_C(g_tp.w_nmos_sa_mux, 0, is_dram) +
      2*num_c_subarray*wire_inside_mat.C_per_um*cell.get_w();
  }

  R_wire_wl_drv_out      = num_c_subarray * r_w_metal;
  R_wire_bit_mux_dec_out = 2 * num_c_subarray * wire_inside_mat.R_per_um * cell.w / 2;
  R_wire_sa_mux_dec_out  = 2 * num_c_subarray * wire_inside_mat.R_per_um * cell.w / 2;


  // initialize row decoder, bitline mux decoder, and senseamp mux decoder circuits
  int num_dec_signals = (fully_assoc) ? 1 : num_r_subarray;
  Decoder _row_dec(num_dec_signals, false, c_wl_drv_ld, R_wire_wl_drv_out);
  if (fully_assoc && (!is_tag))
  {
    _row_dec.exist = true;
  }
  Decoder _bit_mux_dec(deg_bl_muxing, false, C_ld_bit_mux_dec_out, R_wire_bit_mux_dec_out);
  Decoder _senseamp_mux_lev_1_dec(deg_sa_mux_l1_non_assoc, way_select ? true : false,
                                  C_ld_sa_mux_lev_1_dec_out, R_wire_sa_mux_dec_out);
  Decoder _senseamp_mux_lev_2_dec(Ndsam_lev_2, false, C_ld_sa_mux_lev_2_dec_out, R_wire_sa_mux_dec_out);

  // compute transistor widths in row decoder, bitline mux decoder, and senseamp mux decoder circuits
  _row_dec.compute_widths(fully_assoc, is_dram, true);
  _bit_mux_dec.compute_widths(fully_assoc, is_dram, false);
  _senseamp_mux_lev_1_dec.compute_widths(fully_assoc, is_dram, false);
  _senseamp_mux_lev_2_dec.compute_widths(fully_assoc, is_dram, false);
  

  PredecoderBlock r_predec_blk1, r_predec_blk2;
  PredecoderBlock b_mux_predec_blk1, b_mux_predec_blk2;
  PredecoderBlock senseamp_mux_predec_blk1, senseamp_mux_predec_blk2;
  PredecoderBlock senseamp_mux_lev_1_predec_blk1, senseamp_mux_lev_2_predec_blk1;
  PredecoderBlock senseamp_mux_lev_1_predec_blk2, senseamp_mux_lev_2_predec_blk2;
  PredecoderBlock dummy_way_sel_predec_blk1, dummy_way_sel_predec_blk2;

  // initialize row, bitline-mux, and senseamp-mux predecode blocks
  PredecoderBlock::initialize(
      (fully_assoc) ? 1 : num_r_subarray,
      r_predec_blk1, r_predec_blk2, _row_dec,
      2 * num_r_subarray * wire_inside_mat.C_per_um * cell.get_h(),  // C_wire_r_predec_blk_out
      num_r_subarray * wire_inside_mat.R_per_um * cell.get_h() / 2,  // R_wire_r_predec_blk_out
      4,                                                             // because of wordline decoders in 4 subarrays
      is_dram);
  PredecoderBlock::initialize(
      deg_bl_muxing,
      b_mux_predec_blk1, b_mux_predec_blk2, _bit_mux_dec,
      0,                                                             // C_wire_b_mux_predec_blk_out
      0,                                                             // R_wire_b_mux_predec_blk_out
      1,                                                             // num_dec_gates_driven_per_perdec_out
      is_dram);
  PredecoderBlock::initialize(
      deg_sa_mux_l1_non_assoc,
      senseamp_mux_lev_1_predec_blk1, senseamp_mux_lev_1_predec_blk2, _senseamp_mux_lev_1_dec,
      0,                                                             // C_wire_senseamp_mux_predec_blk_out
      0,                                                             // R_wire_senseamp_mux_predec_blk_out
      1,                                                             // num_dec_gates_driven_per_perdec_out
      is_dram);
  PredecoderBlock::initialize(
      1, 
      dummy_way_sel_predec_blk1, dummy_way_sel_predec_blk2, _senseamp_mux_lev_1_dec,
      0,
      0,
      0,
      is_dram);
  PredecoderBlock::initialize(
      Ndsam_lev_2,
      senseamp_mux_lev_2_predec_blk1, senseamp_mux_lev_2_predec_blk2, _senseamp_mux_lev_2_dec,
      0,
      0,
      1,                                                             // num_dec_gates_driven_per_perdec_out
      is_dram);


  // compute widths of transistors in row, bitline mux, and senseamp mux predecode blocks
  r_predec_blk1.compute_widths();
  r_predec_blk2.compute_widths();
  b_mux_predec_blk1.compute_widths();
  b_mux_predec_blk2.compute_widths();
  senseamp_mux_lev_1_predec_blk1.compute_widths();
  senseamp_mux_lev_1_predec_blk2.compute_widths();
  senseamp_mux_lev_2_predec_blk1.compute_widths();
  senseamp_mux_lev_2_predec_blk2.compute_widths();


  PredecoderBlockDriver r_predec_blk_drv1, r_predec_blk_drv2;
  PredecoderBlockDriver b_mux_predec_blk_drv1, b_mux_predec_blk_drv2;
  PredecoderBlockDriver senseamp_mux_lev_1_predec_blk_drv1, senseamp_mux_lev_1_predec_blk_drv2;
  PredecoderBlockDriver senseamp_mux_lev_2_predec_blk_drv1, senseamp_mux_lev_2_predec_blk_drv2;
  PredecoderBlockDriver way_sel_drv1, dummy_way_sel_predec_blk_drv2;

  // initialize row, bitline mux, and senseamp mux predecode block drivers
  // these drivers are at the leaves of the vertical H-trees of the array
  PredecoderBlockDriver::initialize(
      (fully_assoc) ? 1 : num_r_subarray, 0, 0,
      r_predec_blk_drv1, r_predec_blk_drv2,
      r_predec_blk1, r_predec_blk2,
      _row_dec, is_dram);
  PredecoderBlockDriver::initialize(
      deg_bl_muxing, 0, 0,
      b_mux_predec_blk_drv1, b_mux_predec_blk_drv2,
      b_mux_predec_blk1, b_mux_predec_blk2,
      _bit_mux_dec, is_dram);
  PredecoderBlockDriver::initialize(
      deg_sa_mux_l1_non_assoc, 0, 0,
      senseamp_mux_lev_1_predec_blk_drv1, senseamp_mux_lev_1_predec_blk_drv2,
      senseamp_mux_lev_1_predec_blk1,     senseamp_mux_lev_1_predec_blk2,
      _senseamp_mux_lev_1_dec, is_dram);
  PredecoderBlockDriver::initialize(
      1, 1, way_select,
      way_sel_drv1, dummy_way_sel_predec_blk_drv2,
      dummy_way_sel_predec_blk1, dummy_way_sel_predec_blk2,
      _senseamp_mux_lev_1_dec, is_dram);
  PredecoderBlockDriver::initialize(
      Ndsam_lev_2, 0, 0,
      senseamp_mux_lev_2_predec_blk_drv1, senseamp_mux_lev_2_predec_blk_drv2,
      senseamp_mux_lev_2_predec_blk1,     senseamp_mux_lev_2_predec_blk2,
      _senseamp_mux_lev_2_dec, is_dram);

  // compute widths of transistors in row, bitline mux, and senseamp mux predecode block drivers
  // as well as way-select drivers
  r_predec_blk_drv1.compute_widths(r_predec_blk1, _row_dec, 0);
  r_predec_blk_drv2.compute_widths(r_predec_blk2, _row_dec, 0);
  b_mux_predec_blk_drv1.compute_widths(b_mux_predec_blk1, _bit_mux_dec, 0);
  b_mux_predec_blk_drv2.compute_widths(b_mux_predec_blk2, _bit_mux_dec, 0);
  senseamp_mux_lev_1_predec_blk_drv1.compute_widths(senseamp_mux_lev_1_predec_blk1, _senseamp_mux_lev_1_dec, 0);
  senseamp_mux_lev_2_predec_blk_drv2.compute_widths(senseamp_mux_lev_1_predec_blk2, _senseamp_mux_lev_1_dec, 0);
  way_sel_drv1.compute_widths(dummy_way_sel_predec_blk1, _senseamp_mux_lev_1_dec, way_select);
  senseamp_mux_lev_1_predec_blk_drv1.compute_widths(senseamp_mux_lev_2_predec_blk1, _senseamp_mux_lev_2_dec, 0);
  senseamp_mux_lev_1_predec_blk_drv2.compute_widths(senseamp_mux_lev_2_predec_blk2, _senseamp_mux_lev_2_dec, 0);


  Driver bl_precharge_eq_drv;
  bl_precharge_eq_drv.c_gate_load = num_c_subarray *
    gate_C(2 * g_tp.w_pmos_bl_precharge + g_tp.w_pmos_bl_eq, 0, is_dram, false, false);
  bl_precharge_eq_drv.c_wire_load = num_c_subarray * cell.w * wire_outside_mat.C_per_um;
  bl_precharge_eq_drv.r_wire_load = num_c_subarray * cell.w * wire_outside_mat.R_per_um;
  // compute widths of transistors in driver that drives bitline precharge and equalization transistors
  bl_precharge_eq_drv.compute_widths(is_dram);

  // calculate area of a mat
  Area mat = Area::area_mat(
      fully_assoc,
      is_tag,
      tagbits,
      num_r_subarray,
      num_c_subarray, 
      num_subarrays,
      deg_bl_muxing,
      deg_sa_mux_l1_non_assoc,
      Ndsam_lev_1,
      Ndsam_lev_2,
      number_addr_bits_mat,
      num_di_b_mat,
      num_do_b_mat,
      number_way_select_signals_mat,
      r_predec_blk1,
      r_predec_blk2,
      b_mux_predec_blk1,
      b_mux_predec_blk2, 
      senseamp_mux_lev_1_predec_blk1,
      senseamp_mux_lev_1_predec_blk2,
      senseamp_mux_lev_2_predec_blk1,
      senseamp_mux_lev_2_predec_blk2,
      dummy_way_sel_predec_blk1,
      _row_dec,
      _bit_mux_dec,
      _senseamp_mux_lev_1_dec,
      _senseamp_mux_lev_2_dec,
      r_predec_blk_drv1,
      r_predec_blk_drv2,
      b_mux_predec_blk_drv1,
      b_mux_predec_blk_drv2,
      senseamp_mux_lev_1_predec_blk_drv1,
      senseamp_mux_lev_1_predec_blk_drv2,
      senseamp_mux_lev_2_predec_blk_drv1,
      senseamp_mux_lev_2_predec_blk_drv2,
      way_sel_drv1,
      subarray_out_htree_node,
      cell,
      is_dram,
      ram_cell_tech_type);
  hor_htree_seg_len = mat.w / 2;
  ver_htree_seg_len = mat.h / 2;


  AddrDatainHtreeNode hor_addr_di_htree_node(num_h_htree_nodes, hor_htree_seg_len, is_dram);
  AddrDatainHtreeNode ver_addr_di_htree_node(num_v_htree_nodes, ver_htree_seg_len, is_dram);
  DataoutHtreeNode do_htree_node(
      num_v_htree_nodes,
      ver_addr_di_htree_node.length_wire_htree_node,
      is_dram);
  AddrDatainHtreeAtMatInterval hor_addr_di_htree_at_mat_interval(
      2 * hor_htree_seg_len,
      num_h_htree_nodes,
      is_dram);
  AddrDatainHtreeAtMatInterval ver_addr_di_htree_at_mat_interval(
      2 * ver_htree_seg_len,
      num_v_htree_nodes,
      is_dram);

  if (g_ip.rpters_in_htree == false)
  {
    hor_addr_di_htree_node.initialize();
    hor_addr_di_htree_node.compute_widths();
    hor_addr_di_htree_node.compute_areas();
    ver_addr_di_htree_node.initialize();
    ver_addr_di_htree_node.compute_widths();
    ver_addr_di_htree_node.compute_areas();
    do_htree_node.initialize();
    do_htree_node.compute_widths();
    do_htree_node.compute_areas();
  }
  else
  {
    hor_addr_di_htree_at_mat_interval.compute_area_drivers(bank_htree_sizing);
    ver_addr_di_htree_at_mat_interval.compute_area_drivers(bank_htree_sizing);
  }


  // calculate area of a bank
  Area bank = Area::area_single_bank(
      num_r_subarray,
      is_tag,
      num_h_htree_nodes,
      num_v_htree_nodes, 
      num_tristate_h_htree_nodes,
      num_mats_h_dir, 
      num_mats_v_dir, 
      num_act_mats_hor_dir,
      number_addr_bits_mat,
      number_way_select_signals_mat,
      tagbits, 
      num_di_b_mat,
      num_do_b_mat,
      num_di_b_subbank,
      num_do_b_subbank,
      hor_addr_di_htree_node,
      ver_addr_di_htree_node,
      do_htree_node,
      hor_addr_di_htree_at_mat_interval,
      ver_addr_di_htree_at_mat_interval,
      bank_htree_sizing,
      r_predec_blk1, 
      r_predec_blk2,
      b_mux_predec_blk1, 
      b_mux_predec_blk2, 
      senseamp_mux_lev_1_predec_blk1,
      senseamp_mux_lev_1_predec_blk2, 
      senseamp_mux_lev_2_predec_blk1,
      senseamp_mux_lev_2_predec_blk2, 
      _row_dec,
      _bit_mux_dec,
      _senseamp_mux_lev_1_dec,
      _senseamp_mux_lev_2_dec,
      r_predec_blk_drv1,
      r_predec_blk_drv2,
      b_mux_predec_blk_drv1,
      b_mux_predec_blk_drv2,
      senseamp_mux_lev_1_predec_blk_drv1, 
      senseamp_mux_lev_1_predec_blk_drv2,
      senseamp_mux_lev_2_predec_blk_drv1, 
      senseamp_mux_lev_2_predec_blk_drv2,
      &tot_power, 
      &tot_power_row_predecode_block_drivers, 
      &tot_power_bit_mux_predecode_block_drivers, 
      &tot_power_senseamp_mux_lev_1_predecode_block_drivers,
      &tot_power_senseamp_mux_lev_2_predecode_block_drivers,
      &tot_power_row_predecode_blocks,
      &tot_power_bit_mux_predecode_blocks,
      &tot_power_senseamp_mux_lev_1_predecode_blocks,
      &tot_power_senseamp_mux_lev_2_predecode_blocks,
      &tot_power_row_decoders,
      &tot_power_bit_mux_decoders,
      &tot_power_senseamp_mux_lev_1_decoders,
      &tot_power_senseamp_mux_lev_2_decoders,
      mat);

  // Calculate area of all banks
  Area all_banks = Area::area_all_banks(
      g_ip.nbanks,
      bank.h,
      bank.w, 
      number_bits_routed_to_bank,
      &length_htree_route_to_bank,
      num_mats_v_dir,
      is_main_mem);
  
  number_redundant_mats = g_ip.nbanks * number_mats / NUMBER_MATS_PER_REDUNDANT_MAT;
  if (number_redundant_mats > 0)
  {
    // Arrange redundant mats horizontally
    all_banks.h = (all_banks.get_area() + mat.get_area()*number_redundant_mats) / all_banks.w;
  }

 
  inrisetime = 0;
  horizontal_htree_input_load = gate_C(bank_htree_sizing.nand2_buffer_width_n[0] +
      bank_htree_sizing.nand2_buffer_width_p[0], 0, is_dram);
  delay_route_to_bank = 0;
  
  if (is_tag == false)
  {
    length_htree_route_to_bank += LENGTH_INTERCONNECT_FROM_BANK_TO_CROSSBAR;
  }

  pair<double, double> tmp_pair;

  powerDef power_routing_to_bank;

  tmp_pair = arr_edge_to_bank_edge_htree_sizing.compute_delays(
      inrisetime,
      length_htree_route_to_bank,
      horizontal_htree_input_load,
      is_dram,
      power_routing_to_bank);

  outrisetime         = tmp_pair.first;
  delay_route_to_bank = tmp_pair.second;

  double c_crossbar_output_line_load =
    gate_C(arr_edge_to_bank_edge_htree_sizing.opt_sizing * g_tp.min_w_nmos_ + 
           p_to_n_sizing_r * arr_edge_to_bank_edge_htree_sizing.opt_sizing * g_tp.min_w_nmos_, 0, is_dram);

  Crossbar crossbar(
      NUMBER_INPUT_PORTS_CROSSBAR,
      NUMBER_OUTPUT_PORTS_CROSSBAR,
      NUMBER_SIGNALS_PER_PORT_CROSSBAR,
      c_crossbar_output_line_load,
      is_dram);
  
  if ((IS_CROSSBAR)&&(!is_tag))
  {
    crossbar.compute_widths();
    inrisetime  = 0;
    outrisetime = crossbar.compute_delay(inrisetime);
    crossbar.compute_area();
  }
  
  delay_route_to_bank += g_tp.FO4 * (NUMBER_STACKED_DIE_LAYERS - 1) + crossbar.delay;

  
  inrisetime = 0;
  if (g_ip.rpters_in_htree == false)
  {
    tmp_pair = hor_addr_di_htree_node.compute_delays(inrisetime);
    outrisetime = tmp_pair.first;
    delay_addr_din_horizontal_htree = tmp_pair.second;
    tmp_pair = ver_addr_di_htree_node.compute_delays(inrisetime);
    outrisetime = tmp_pair.first;
    delay_addr_din_vertical_htree = tmp_pair.second;
  }
  else
  {    
    htree_output_load = gate_C(bank_htree_sizing.nand2_buffer_width_n[0] +
                               bank_htree_sizing.nand2_buffer_width_p[0], 0, is_dram);
    tmp_pair = hor_addr_di_htree_at_mat_interval.compute_delay_addr_datain(
        htree_output_load, 1, inrisetime, bank_htree_sizing);
    outrisetime = tmp_pair.first;
    delay_addr_din_horizontal_htree = tmp_pair.second;
    inrisetime = 0;
    htree_output_load = gate_C(g_tp.min_w_nmos_ + p_to_n_sizing_r * g_tp.min_w_nmos_, 0, is_dram);

    tmp_pair = ver_addr_di_htree_at_mat_interval.compute_delay_addr_datain(
        htree_output_load, g_ip.broadcast_addr_din_over_ver_htrees, inrisetime, bank_htree_sizing);
    outrisetime = tmp_pair.first;
    delay_addr_din_vertical_htree = tmp_pair.second;
    inrisetime = 0;
  }

  delay_dout_horizontal_htree = delay_addr_din_horizontal_htree;

  if ((fully_assoc)&&(is_tag))
  { // fully-associative cache tag decoder delay
    delay_fa_tag(
        tagbits,
        Ndbl,
        num_r_subarray,
        &delay_fa_decoder,
        &power_fa_decoder,
        cam_cell,
        is_dram,
        cell);
  }

  pair<double, double> tmp_pair1, tmp_pair2;
  tmp_pair1 = r_predec_blk_drv1.compute_delays(inrisetime, inrisetime);
  tmp_pair1 = r_predec_blk1.compute_delays(tmp_pair1);
  tmp_pair2 = r_predec_blk_drv2.compute_delays(inrisetime, inrisetime);
  tmp_pair2 = r_predec_blk2.compute_delays(tmp_pair2);
  tmp_pair1 = get_max_delay_before_decoder(
      r_predec_blk_drv1, r_predec_blk1, tmp_pair1,
      r_predec_blk_drv2, r_predec_blk2, tmp_pair2);
  max_delay_before_row_decoder = tmp_pair1.first + delay_route_to_bank +
                                 delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree;
  row_dec_inrisetime           = tmp_pair1.second;

  tmp_pair1 = b_mux_predec_blk_drv1.compute_delays(inrisetime, inrisetime);
  tmp_pair1 = b_mux_predec_blk1.compute_delays(tmp_pair1);
  tmp_pair2 = b_mux_predec_blk_drv2.compute_delays(inrisetime, inrisetime);
  tmp_pair2 = b_mux_predec_blk2.compute_delays(tmp_pair2);
  tmp_pair1 = get_max_delay_before_decoder(
      b_mux_predec_blk_drv1, b_mux_predec_blk1, tmp_pair1,
      b_mux_predec_blk_drv2, b_mux_predec_blk2, tmp_pair2);
  max_delay_before_bit_mux_decoder = tmp_pair1.first + delay_route_to_bank +
                                    delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree;
  bit_mux_dec_inrisetime           = tmp_pair1.second;
  
  
  tmp_pair1 = senseamp_mux_lev_1_predec_blk_drv1.compute_delays(inrisetime, inrisetime);
  tmp_pair1 = senseamp_mux_lev_1_predec_blk1.compute_delays(tmp_pair1);
  tmp_pair2 = senseamp_mux_lev_1_predec_blk_drv2.compute_delays(inrisetime, inrisetime);
  tmp_pair2 = senseamp_mux_lev_1_predec_blk2.compute_delays(tmp_pair2);
  tmp_pair1 = get_max_delay_before_decoder(
      senseamp_mux_lev_1_predec_blk_drv1, senseamp_mux_lev_1_predec_blk1, tmp_pair1,
      senseamp_mux_lev_1_predec_blk_drv2, senseamp_mux_lev_1_predec_blk2, tmp_pair2);
  max_delay_before_senseamp_mux_lev_1_decoder = tmp_pair1.first + delay_route_to_bank +
                                                delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree;
  senseamp_mux_lev_1_dec_inrisetime           = tmp_pair1.second;

  tmp_pair1 = senseamp_mux_lev_2_predec_blk_drv1.compute_delays(inrisetime, inrisetime);
  tmp_pair1 = senseamp_mux_lev_2_predec_blk1.compute_delays(tmp_pair1);
  tmp_pair2 = senseamp_mux_lev_2_predec_blk_drv2.compute_delays(inrisetime, inrisetime);
  tmp_pair2 = senseamp_mux_lev_2_predec_blk2.compute_delays(tmp_pair2);
  tmp_pair1 = get_max_delay_before_decoder(
      senseamp_mux_lev_2_predec_blk_drv1, senseamp_mux_lev_2_predec_blk1, tmp_pair1,
      senseamp_mux_lev_2_predec_blk_drv2, senseamp_mux_lev_2_predec_blk2, tmp_pair2);
  max_delay_before_senseamp_mux_lev_2_decoder = tmp_pair1.first + delay_route_to_bank +
                                                delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree;
  senseamp_mux_lev_2_dec_inrisetime           = tmp_pair1.second;


  row_dec_outrisetime = _row_dec.compute_delays(row_dec_inrisetime, cell, is_dram, true);
  bit_mux_dec_outrisetime = _bit_mux_dec.compute_delays(bit_mux_dec_inrisetime, cell, is_dram, false);
  senseamp_mux_lev_1_dec_outrisetime = _senseamp_mux_lev_1_dec.compute_delays(senseamp_mux_lev_1_dec_inrisetime, cell, is_dram, false);
  senseamp_mux_lev_2_dec_outrisetime = _senseamp_mux_lev_2_dec.compute_delays(senseamp_mux_lev_2_dec_inrisetime, cell, is_dram, false);
 
  Tpre = max_delay_before_row_decoder + _row_dec.delay;
  
  leakage_power_cc_inverters_sram_cell = 0;
  leakage_power_access_transistors_read_write_port_sram_cell = 0;
  leakage_power_read_only_port_sram_cell = 0;
  delay_bitline = bitline_delay(
      num_r_subarray,
      num_c_subarray,
      num_subarrays, 
      row_dec_outrisetime,
      &outrisetime,
      &bitline_data_power,
      &per_bitline_read_energy,
      deg_bl_muxing,
      Ndsam_lev_1 * Ndsam_lev_2,
      num_act_mats_hor_dir,
      &writeback_delay, 
      g_ip.num_rw_ports,
      g_ip.num_rd_ports,
      g_ip.num_wr_ports,
      cell,
      is_dram,
      c_b_metal,
      r_b_metal,
      v_b_sense,
      ram_cell_tech_type);
  
  wordline_data = _row_dec.delay;
  inrisetime    = outrisetime;

  delay_sense_amplifier(
      num_c_subarray,
      g_ip.num_rw_ports,
      g_ip.num_rd_ports,
      inrisetime,
      &outrisetime,
      &sense_amp_data_power, 
      deg_bl_muxing,
      number_mats,
      num_act_mats_hor_dir, 
      &delay_sense_amp,
      &leak_power_sense_amps_closed_page_state,
      &leak_power_sense_amps_open_page_state,
      cell,
      is_dram,
      is_tag,
      v_b_sense);
  inrisetime = outrisetime;
  delay_output_driver_at_subarray(
      deg_bl_muxing,
      Ndsam_lev_1,
      Ndsam_lev_2,
      g_ip.num_rw_ports,
      g_ip.num_rd_ports,
      inrisetime,
      &outrisetime,
      &power_output_drivers_at_subarray, 
      subarray_out_htree_node,
      number_mats,
      &delay_subarray_output_driver,
      &delay_final_stage_subarray_output_driver,
      is_dram,
      cell);

  if (g_ip.rpters_in_htree == false)
  {
    tmp_pair                  = do_htree_node.compute_delays(inrisetime);
    outrisetime               = tmp_pair.first;
    delay_dout_vertical_htree = tmp_pair.second;
    power_dout_htree          = do_htree_node.power;
    inrisetime                = 0;
  }
  else
  {
    tmp_pair    = ver_addr_di_htree_at_mat_interval.compute_delay_dataout_ver(inrisetime, bank_htree_sizing);
    outrisetime = tmp_pair.first;
    delay_dout_vertical_htree = tmp_pair.second;
    inrisetime  = 0;
  }
  
  delay_data_access_row_path = 
    max_delay_before_row_decoder + _row_dec.delay + delay_bitline + 
    delay_sense_amp + delay_subarray_output_driver + delay_dout_vertical_htree + 
    delay_dout_horizontal_htree + delay_route_to_bank;
  delay_data_access_col_path = max_delay_before_bit_mux_decoder + _bit_mux_dec.delay +  delay_sense_amp +  
    delay_subarray_output_driver + delay_dout_vertical_htree + delay_dout_horizontal_htree + 
    delay_route_to_bank;

  delay_data_access_senseamp_mux_lev_1_path = 
    max_delay_before_senseamp_mux_lev_1_decoder + _senseamp_mux_lev_1_dec.delay + 
    delay_subarray_output_driver + delay_dout_vertical_htree + delay_dout_horizontal_htree + delay_route_to_bank;
  delay_data_access_senseamp_mux_lev_2_path =
    max_delay_before_senseamp_mux_lev_2_decoder + _senseamp_mux_lev_2_dec.delay + 
    delay_subarray_output_driver + delay_dout_vertical_htree + delay_dout_horizontal_htree + delay_route_to_bank;
  
  temp_delay_data_access_path = MAX(delay_data_access_row_path, delay_data_access_col_path);
  delay_data_access_path = MAX(temp_delay_data_access_path, 
                               MAX(delay_data_access_senseamp_mux_lev_1_path,
                                   delay_data_access_senseamp_mux_lev_2_path));
  delay_before_subarray_output_driver = delay_data_access_path - 
    (delay_subarray_output_driver + delay_dout_vertical_htree + delay_dout_horizontal_htree + 
     delay_route_to_bank);
  delay_from_subarray_output_driver_to_output = delay_subarray_output_driver + 
    delay_dout_vertical_htree + delay_dout_horizontal_htree + delay_route_to_bank;
  
  if ((is_tag)&&(fully_assoc))
  { //delay of fully-associative tag decoder
    access_time = delay_route_to_bank + delay_addr_din_horizontal_htree + 
                  delay_addr_din_vertical_htree + delay_fa_decoder;
  }
  else if (fully_assoc)
  { //delay of fully-associative data array
    access_time = _row_dec.delay + delay_bitline + delay_sense_amp +  delay_subarray_output_driver +
                  delay_dout_vertical_htree + delay_dout_horizontal_htree + delay_route_to_bank;
  }
  else
  {
    access_time = delay_data_access_path;
  }

  if (is_main_mem)
  {
    t_rcd = max_delay_before_row_decoder + _row_dec.delay + delay_bitline + delay_sense_amp;
    cas_latency = MAX(max_delay_before_senseamp_mux_lev_1_decoder + _senseamp_mux_lev_1_dec.delay,
                      max_delay_before_senseamp_mux_lev_2_decoder + _senseamp_mux_lev_2_dec.delay) + 
                  delay_subarray_output_driver + delay_dout_vertical_htree +
                  delay_dout_horizontal_htree  + delay_route_to_bank;
    access_time = t_rcd + cas_latency;
  }


  if ((is_tag)&&(!fully_assoc))
  {
    delay_comparator(
        tagbits,
        g_ip.tag_assoc,
        inrisetime,
        &outrisetime,
        &comparator_delay,
        &comparator_power,
        is_dram,
        cell.h);
    access_time += comparator_delay;
  }
  else
  {
    comparator_delay = 0;
  }


  if (!((is_tag)&&(fully_assoc)))
  {
    dummy_precharge_outrisetime = bl_precharge_eq_drv.compute_delay(0, is_dram);
    k = _row_dec.num_gates - 1;
    rd = tr_R_on(_row_dec.w_dec_n[k], NCH, 1, is_dram, false, true);
    c_intrinsic = drain_C_(_row_dec.w_dec_p[k], PCH, 1, 1, 4*cell.h, is_dram, false, true) +
                  drain_C_(_row_dec.w_dec_n[k], NCH, 1, 1, 4*cell.h, is_dram, false, true);
    c_load = _row_dec.C_ld_dec_out;
    tf = rd * (c_intrinsic + c_load) + _row_dec.R_wire_dec_out * c_load / 2;
    wordline_reset_delay = horowitz(0, tf, 0.5, 0.5, RISE);
    r_bitline_precharge = tr_R_on(g_tp.w_pmos_bl_precharge, PCH, 1, is_dram, false, false);
    r_bitline = num_r_subarray * r_b_metal;

    if (is_dram)
    {
      bitline_restore_delay = bl_precharge_eq_drv.delay + 
        2.3 * (r_bitline_precharge * Cbitline + r_bitline * Cbitline / 2);
      temp = wordline_data + delay_bitline + delay_sense_amp + writeback_delay +
        wordline_reset_delay + bitline_restore_delay;//temp stores random cycle time
    }
    else
    {
      bitline_restore_delay = bl_precharge_eq_drv.delay + 
        log((g_tp.sram.Vbitpre - 0.1 * v_b_sense) / (g_tp.sram.Vbitpre - v_b_sense))*(r_bitline_precharge * Cbitline +
        r_bitline * Cbitline / 2);
      temp = wordline_data + delay_bitline + wordline_reset_delay + delay_sense_amp + bitline_restore_delay;
    }
  }
  else
  {
    temp = delay_fa_decoder;
  }

  delay_within_mat_before_row_decoder = max_delay_before_row_decoder - 
    (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
  delay_within_mat_before_bit_mux_decoder = max_delay_before_bit_mux_decoder - 
    (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
  delay_within_mat_before_senseamp_mux_lev_1_decoder = max_delay_before_senseamp_mux_lev_1_decoder - 
    (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
  delay_within_mat_before_senseamp_mux_lev_2_decoder = max_delay_before_senseamp_mux_lev_2_decoder - 
    (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
  
  
  temp = MAX(temp, delay_within_mat_before_row_decoder);
  temp = MAX(temp, delay_within_mat_before_bit_mux_decoder);
  temp = MAX(temp, delay_within_mat_before_senseamp_mux_lev_1_decoder);
  temp = MAX(temp, delay_within_mat_before_senseamp_mux_lev_2_decoder);
  temp = MAX(temp, hor_addr_di_htree_at_mat_interval.max_delay_between_buffers);
  temp = MAX(temp, ver_addr_di_htree_at_mat_interval.max_delay_between_buffers);
  cycle_time = temp;

  temp = MAX(max_delay_before_row_decoder, delay_subarray_output_driver +
                                           delay_dout_vertical_htree +
                                           delay_dout_horizontal_htree +
                                           delay_route_to_bank);
  multisubbank_interleave_cycle_time = temp;

  delay_request_network = max_delay_before_row_decoder;
  delay_inside_mat      = wordline_data + delay_bitline + delay_sense_amp;
  delay_reply_network   = delay_subarray_output_driver + delay_dout_vertical_htree +
                          delay_dout_horizontal_htree + delay_route_to_bank;
  
  if (is_main_mem)
  {
    multisubbank_interleave_cycle_time = delay_route_to_bank;
    precharge_delay = delay_route_to_bank + delay_addr_din_horizontal_htree + 
                      delay_addr_din_vertical_htree + writeback_delay +
                      wordline_reset_delay + bitline_restore_delay;
    cycle_time = access_time + precharge_delay;
  }
  

  if (is_dram)
  {
    dram_array_availability = (1 - num_r_subarray * cycle_time / dram_refresh_period) * 100;
  }
  else
  {
    dram_array_availability = 0;
  }

  number_addr_bits_routed_to_bank_for_activate = number_addr_bits_row_decode + number_subbanks_decode;
  number_addr_bits_routed_to_bank_for_read_or_write = number_addr_bits_mat - number_addr_bits_row_decode + number_subbanks_decode;
  number_addr_bits_routed_to_mat_for_activate = number_addr_bits_row_decode;
  number_addr_bits_routed_to_mat_for_read_or_write = number_addr_bits_mat - number_addr_bits_row_decode;
  // calculate dynamic energy per access. Note that the name of the variables have "power" 
  // in them but readOp.dynamic is actually energy. readop.leakage is power. 
  if (!is_tag)
  {
    tot_power_routing_to_bank.readOp.dynamic = power_routing_to_bank.readOp.dynamic *
      (number_addr_bits_routed_to_bank + number_data_bits_routed_to_bank / 2 +
      num_comp_b_routed_to_bank);
    tot_power_routing_to_bank.writeOp.dynamic = tot_power_routing_to_bank.readOp.dynamic;
    tot_power_crossbar.readOp.dynamic = num_do_b_subbank * crossbar.power.readOp.dynamic;
    routing_to_bank_for_activate_energy = number_addr_bits_routed_to_bank_for_activate * power_routing_to_bank.readOp.dynamic;
    routing_addr_to_bank_for_read_or_write_energy = number_addr_bits_routed_to_bank_for_read_or_write * power_routing_to_bank.readOp.dynamic;
    routing_datain_bits_to_bank_energy_for_write = (number_data_bits_routed_to_bank / 2) * power_routing_to_bank.readOp.dynamic;
  }
  else
  {
    tot_power_routing_to_bank.readOp.dynamic = power_routing_to_bank.readOp.dynamic *
      (number_addr_bits_routed_to_bank + tagbits + g_ip.data_assoc);
    tot_power_routing_to_bank.writeOp.dynamic = power_routing_to_bank.writeOp.dynamic *
      (number_addr_bits_routed_to_bank + tagbits);
  }
  tot_power.readOp.dynamic += tot_power_routing_to_bank.readOp.dynamic;
  tot_power.readOp.dynamic += tot_power_crossbar.readOp.dynamic;

  //Add energy consumed in the horizontal H-trees within a bank
  htree_seg_multiplier = 1;
  length_htree_segment = pow(2, num_h_htree_nodes - 1) * 
    hor_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for (k = 0; k < num_h_htree_nodes; ++k)
  {
    power_addr_datain_horizontal_htree_node = hor_addr_di_htree_node.compute_power(k);
    if (g_ip.rpters_in_htree == true)
    {
      power_addr_datain_horizontal_htree_node = hor_addr_di_htree_at_mat_interval.compute_power_addr_datain(
          length_htree_segment, k, bank_htree_sizing);
    }
    tot_power_addr_horizontal_htree.readOp.dynamic +=
      (number_addr_bits_mat + number_way_select_signals_mat) * 
      power_addr_datain_horizontal_htree_node.readOp.dynamic * htree_seg_multiplier;
    tot_power_datain_horizontal_htree.readOp.dynamic  += g_ip.burst_len * num_di_b_mat * 
      num_mats_h_dir *  power_addr_datain_horizontal_htree_node.readOp.dynamic;
    tot_power_dataout_horizontal_htree.readOp.dynamic += g_ip.burst_len * num_do_b_mat * 
      num_mats_h_dir *  power_addr_datain_horizontal_htree_node.readOp.dynamic;
    hor_htree_routing_e_for_act += number_addr_bits_routed_to_mat_for_activate * power_addr_datain_horizontal_htree_node.readOp.dynamic * htree_seg_multiplier;
    horizontal_addr_htree_routing_energy_for_read_or_write += number_addr_bits_routed_to_mat_for_read_or_write * power_addr_datain_horizontal_htree_node.readOp.dynamic * htree_seg_multiplier;
    horizontal_datain_htree_routing_energy_for_write += num_di_b_mat * power_addr_datain_horizontal_htree_node.readOp.dynamic * htree_seg_multiplier;
    horizontal_dataout_htree_energy_for_read += num_do_b_mat * power_addr_datain_horizontal_htree_node.readOp.dynamic * htree_seg_multiplier;
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }
  
  tot_power.readOp.dynamic += tot_power_addr_horizontal_htree.readOp.dynamic +
                              tot_power_dataout_horizontal_htree.readOp.dynamic;  

  // add energy consumed in address/datain vertical htree
  htree_seg_multiplier = 1;
  length_htree_segment = pow(2, num_v_htree_nodes - 1) * 
    ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for (k = 0; k < num_v_htree_nodes; ++k)
  {
    power_addr_datain_vertical_htree_node = ver_addr_di_htree_node.compute_power(k);
    if (g_ip.rpters_in_htree == true)
    {
      power_addr_datain_vertical_htree_node = ver_addr_di_htree_at_mat_interval.compute_power_addr_datain(
          length_htree_segment, k, bank_htree_sizing);
    }
    tot_power_addr_vertical_htree.readOp.dynamic +=  
      (number_addr_bits_mat + number_way_select_signals_mat) * 
      power_addr_datain_vertical_htree_node.readOp.dynamic * htree_seg_multiplier * num_act_mats_hor_dir;
    tot_power_datain_vertical_htree.readOp.dynamic += g_ip.burst_len * num_di_b_mat * 
      power_addr_datain_vertical_htree_node.readOp.dynamic * htree_seg_multiplier * num_act_mats_hor_dir;
    vertical_htree_routing_energy_for_activate += number_addr_bits_routed_to_mat_for_activate * 
      power_addr_datain_vertical_htree_node.readOp.dynamic * htree_seg_multiplier * num_act_mats_hor_dir;
    vertical_addr_htree_routing_energy_for_read_or_write += number_addr_bits_routed_to_mat_for_read_or_write * 
      power_addr_datain_vertical_htree_node.readOp.dynamic * htree_seg_multiplier * num_act_mats_hor_dir;
    vertical_datain_htree_routing_energy_for_write += num_di_b_mat * 
      power_addr_datain_vertical_htree_node.readOp.dynamic * htree_seg_multiplier * num_act_mats_hor_dir;

    if (g_ip.broadcast_addr_din_over_ver_htrees)
    {
      htree_seg_multiplier *= 2;
    }
    else
    {
      htree_seg_multiplier = 1;
    }
    length_htree_segment /= 2;
  }

  if ((num_mats_v_dir > 1)&&(g_ip.broadcast_addr_din_over_ver_htrees))
  {
    tot_power_addr_vertical_htree.readOp.dynamic *= 2; 
    tot_power_datain_vertical_htree.readOp.dynamic *= 2;
  }
  
  tot_power.readOp.dynamic += tot_power_addr_vertical_htree.readOp.dynamic;

  //Add energy consumed in predecoder drivers
  tot_power_row_predecode_block_drivers.readOp.dynamic =
    r_predec_blk_drv1.get_readOp_dynamic_power(num_act_mats_hor_dir) +
    r_predec_blk_drv2.get_readOp_dynamic_power(num_act_mats_hor_dir);
  tot_power_bit_mux_predecode_block_drivers.readOp.dynamic  = 
    b_mux_predec_blk_drv1.get_readOp_dynamic_power(num_act_mats_hor_dir) +
    b_mux_predec_blk_drv2.get_readOp_dynamic_power(num_act_mats_hor_dir);
  tot_power_senseamp_mux_lev_1_predecode_block_drivers.readOp.dynamic = 
    senseamp_mux_lev_1_predec_blk_drv1.get_readOp_dynamic_power(num_act_mats_hor_dir) +
    senseamp_mux_lev_1_predec_blk_drv2.get_readOp_dynamic_power(num_act_mats_hor_dir);
  tot_power_senseamp_mux_lev_2_predecode_block_drivers.readOp.dynamic = 
    senseamp_mux_lev_2_predec_blk_drv1.get_readOp_dynamic_power(num_act_mats_hor_dir) +
    senseamp_mux_lev_2_predec_blk_drv2.get_readOp_dynamic_power(num_act_mats_hor_dir);
  tot_power.readOp.dynamic += 
    tot_power_row_predecode_block_drivers.readOp.dynamic +
    tot_power_bit_mux_predecode_block_drivers.readOp.dynamic +
    tot_power_senseamp_mux_lev_1_predecode_block_drivers.readOp.dynamic +
    tot_power_senseamp_mux_lev_2_predecode_block_drivers.readOp.dynamic;

  //Add energy consumed in predecode blocks
  tot_power_row_predecode_blocks.readOp.dynamic =
    (r_predec_blk1.power_nand2_path.readOp.dynamic   +
     r_predec_blk1.power_nand3_path.readOp.dynamic   +
     r_predec_blk1.power_L2.readOp.dynamic +
     r_predec_blk2.power_nand2_path.readOp.dynamic   + 
     r_predec_blk2.power_nand3_path.readOp.dynamic   +
     r_predec_blk2.power_L2.readOp.dynamic) * num_act_mats_hor_dir;
  tot_power_bit_mux_predecode_blocks.readOp.dynamic =
    (b_mux_predec_blk1.power_nand2_path.readOp.dynamic   +
     b_mux_predec_blk1.power_nand3_path.readOp.dynamic   +
     b_mux_predec_blk1.power_L2.readOp.dynamic +
     b_mux_predec_blk2.power_nand2_path.readOp.dynamic   +
     b_mux_predec_blk2.power_nand3_path.readOp.dynamic   +
     b_mux_predec_blk2.power_L2.readOp.dynamic) * num_act_mats_hor_dir;
  tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.dynamic =
    (senseamp_mux_lev_1_predec_blk1.power_nand2_path.readOp.dynamic   +
     senseamp_mux_lev_1_predec_blk1.power_nand3_path.readOp.dynamic   +
     senseamp_mux_lev_1_predec_blk1.power_L2.readOp.dynamic +
     senseamp_mux_lev_1_predec_blk2.power_nand2_path.readOp.dynamic   + 
     senseamp_mux_lev_1_predec_blk2.power_nand3_path.readOp.dynamic   +
     senseamp_mux_lev_1_predec_blk2.power_L2.readOp.dynamic) * num_act_mats_hor_dir;
  tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.dynamic =
    (senseamp_mux_lev_2_predec_blk1.power_nand2_path.readOp.dynamic   +
     senseamp_mux_lev_2_predec_blk1.power_nand3_path.readOp.dynamic   +
     senseamp_mux_lev_2_predec_blk1.power_L2.readOp.dynamic +
     senseamp_mux_lev_2_predec_blk2.power_nand2_path.readOp.dynamic   + 
     senseamp_mux_lev_2_predec_blk2.power_nand3_path.readOp.dynamic   +
     senseamp_mux_lev_2_predec_blk2.power_L2.readOp.dynamic) * num_act_mats_hor_dir;
  tot_power.readOp.dynamic += 
    tot_power_row_predecode_blocks.readOp.dynamic +
    tot_power_bit_mux_predecode_blocks.readOp.dynamic +
    tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.dynamic +
    tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.dynamic;

  // add energy consumed in decoders
  if ((fully_assoc)&&(is_tag))
  { //Fully-associative cache tag array decoding energy is stored in tot_power_row_decoders
    tot_power_row_decoders.readOp.dynamic = power_fa_decoder.readOp.dynamic;
  }
  else if (fully_assoc)
  {
    tot_power_row_decoders.readOp.dynamic = _row_dec.power.readOp.dynamic * num_act_mats_hor_dir;
  }
  else
  {
    tot_power_row_decoders.readOp.dynamic = _row_dec.power.readOp.dynamic * 4 * num_act_mats_hor_dir;
  }
  
  if(fully_assoc)
  { //Fully-associative data array
    tot_power_bitlines_precharge_eq_driver.readOp.dynamic = bl_precharge_eq_drv.power.readOp.dynamic *
        num_act_mats_hor_dir;
  }
  else
  {
    tot_power_bitlines_precharge_eq_driver.readOp.dynamic = bl_precharge_eq_drv.power.readOp.dynamic *
        4 * num_act_mats_hor_dir;
  }

  //If DRAM, add contribution of power spent in row predecoder drivers, blocks and decoders to refresh power
  if (is_dram)
  {
    refresh_power += (tot_power_row_predecode_block_drivers.readOp.dynamic +
                      tot_power_row_predecode_blocks.readOp.dynamic + _row_dec.power.readOp.dynamic) * 
                     num_r_subarray * num_subarrays / dram_refresh_period;
    refresh_power += per_bitline_read_energy * num_c_subarray * num_r_subarray * num_subarrays / dram_refresh_period;
    refresh_power += tot_power_bitlines_precharge_eq_driver.readOp.dynamic;
    refresh_power += sense_amp_data_power.readOp.dynamic;
  }

  tot_power_bit_mux_decoders.readOp.dynamic = _bit_mux_dec.power.readOp.dynamic * num_act_mats_hor_dir;
  tot_power_senseamp_mux_lev_1_decoders.readOp.dynamic =
    _senseamp_mux_lev_1_dec.power.readOp.dynamic * num_act_mats_hor_dir;
  tot_power_senseamp_mux_lev_2_decoders.readOp.dynamic =
    _senseamp_mux_lev_2_dec.power.readOp.dynamic * num_act_mats_hor_dir;
  tot_power.readOp.dynamic += 
    tot_power_row_decoders.readOp.dynamic + tot_power_bit_mux_decoders.readOp.dynamic +
    tot_power_senseamp_mux_lev_1_decoders.readOp.dynamic + tot_power_senseamp_mux_lev_2_decoders.readOp.dynamic;
  
  if (!(fully_assoc&&is_tag))
  { //If fully associative cache and tag array, don't add the following components of energy
    //Add energy consumed in bitlines
    tot_power_bitlines.readOp.dynamic = bitline_data_power.readOp.dynamic;
    tot_power_bitlines.writeOp.dynamic = bitline_data_power.writeOp.dynamic;
    tot_power.readOp.dynamic += tot_power_bitlines.readOp.dynamic;

    //Add energy consumed in the precharge and equalization driver
    tot_power.readOp.dynamic += tot_power_bitlines_precharge_eq_driver.readOp.dynamic;

    //Add energy consumed in sense amplifiers
    tot_power_sense_amps.readOp.dynamic = sense_amp_data_power.readOp.dynamic;
    tot_power.readOp.dynamic += tot_power_sense_amps.readOp.dynamic;

    //Add energy consumed in subarray output driver circuitry
    tot_power_subarray_output_drivers.readOp.dynamic = power_output_drivers_at_subarray.readOp.dynamic * 
      num_do_b_mat * num_act_mats_hor_dir;
    tot_power.readOp.dynamic += tot_power_subarray_output_drivers.readOp.dynamic;
  }

  //Add energy consumed in vertical dataout htree
  if (g_ip.rpters_in_htree == false)
  {
    tot_power_dataout_vertical_htree.readOp.dynamic = g_ip.burst_len * num_do_b_mat * 
      num_act_mats_hor_dir * power_dout_htree.readOp.dynamic;
  }
  else
  {
    length_htree_segment = pow(2, num_v_htree_nodes - 1) * 
      ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
    for(k = 0; k < num_v_htree_nodes; ++k)
    {
      power_dout_vertical_htree_node = 
        ver_addr_di_htree_at_mat_interval.compute_power_dataout_ver(length_htree_segment, k, bank_htree_sizing);
      tot_power_dataout_vertical_htree.readOp.dynamic += g_ip.burst_len * num_do_b_mat * 
        power_dout_vertical_htree_node.readOp.dynamic * num_act_mats_hor_dir;
      vertical_dataout_htree_routing_energy_for_read =
        num_do_b_mat * power_dout_vertical_htree_node.readOp.dynamic * num_act_mats_hor_dir;
      length_htree_segment /= 2;
    }
  }
  tot_power.readOp.dynamic += tot_power_dataout_vertical_htree.readOp.dynamic;

  tot_power_comparators.readOp.dynamic = comparator_power.readOp.dynamic * num_act_mats_hor_dir;
  tot_power.readOp.dynamic += tot_power_comparators.readOp.dynamic;

  //Calculate total write energy per access
  if(is_dram)
  {
    tot_power.writeOp.dynamic = tot_power.readOp.dynamic - tot_power_bitlines.readOp.dynamic +
      tot_power_bitlines.writeOp.dynamic - tot_power_routing_to_bank.readOp.dynamic +
      tot_power_routing_to_bank.writeOp.dynamic - tot_power_dataout_horizontal_htree.readOp.dynamic +
      tot_power_datain_horizontal_htree.readOp.dynamic - tot_power_dataout_vertical_htree.readOp.dynamic +
      tot_power_datain_vertical_htree.readOp.dynamic;
  }
  else
  {
    tot_power.writeOp.dynamic = tot_power.readOp.dynamic - tot_power_bitlines.readOp.dynamic +
      tot_power_bitlines.writeOp.dynamic - tot_power_sense_amps.readOp.dynamic - tot_power_routing_to_bank.readOp.dynamic +
      tot_power_routing_to_bank.writeOp.dynamic - tot_power_dataout_horizontal_htree.readOp.dynamic +
      tot_power_datain_horizontal_htree.readOp.dynamic - tot_power_dataout_vertical_htree.readOp.dynamic +
      tot_power_datain_vertical_htree.readOp.dynamic;
  }
 
  dyn_read_energy_from_closed_page = tot_power.readOp.dynamic;
  dyn_read_energy_from_open_page   = tot_power.readOp.dynamic - tot_power_row_predecode_blocks.readOp.dynamic -
    tot_power_row_decoders.readOp.dynamic -	tot_power_bitlines_precharge_eq_driver.readOp.dynamic -
    tot_power_sense_amps.readOp.dynamic - tot_power_bitlines.readOp.dynamic;
  
  dyn_read_energy_remaining_words_in_burst = 
    (MAX((g_ip.burst_len / g_ip.int_prefetch_w), 1) - 1) * 
    (tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_1_decoders.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_decoders.readOp.dynamic +
     tot_power_subarray_output_drivers.readOp.dynamic +
     tot_power_dataout_vertical_htree.readOp.dynamic +
     tot_power_routing_to_bank.readOp.dynamic);
  
  dyn_read_energy_from_closed_page += dyn_read_energy_remaining_words_in_burst;
  dyn_read_energy_from_open_page   += dyn_read_energy_remaining_words_in_burst;
  
  if (!is_tag)
  {
    tot_power.readOp.dynamic  = dyn_read_energy_from_closed_page;
    if(is_dram)
    {
      tot_power.writeOp.dynamic = dyn_read_energy_from_closed_page - dyn_read_energy_remaining_words_in_burst -
        tot_power_bitlines.readOp.dynamic + tot_power_bitlines.writeOp.dynamic +
        (tot_power_routing_to_bank.writeOp.dynamic  - tot_power_routing_to_bank.readOp.dynamic -
         tot_power_dataout_horizontal_htree.readOp.dynamic + tot_power_datain_horizontal_htree.readOp.dynamic -
         tot_power_dataout_vertical_htree.readOp.dynamic + tot_power_datain_vertical_htree.readOp.dynamic) * 
        (MAX((g_ip.burst_len / g_ip.int_prefetch_w), 1) - 1);
    }
    else
    {
      tot_power.writeOp.dynamic = dyn_read_energy_from_closed_page - dyn_read_energy_remaining_words_in_burst -
        tot_power_bitlines.readOp.dynamic + tot_power_bitlines.writeOp.dynamic - tot_power_sense_amps.readOp.dynamic +
        (tot_power_routing_to_bank.writeOp.dynamic  - tot_power_routing_to_bank.readOp.dynamic -
         tot_power_dataout_horizontal_htree.readOp.dynamic + tot_power_datain_horizontal_htree.readOp.dynamic -
         tot_power_dataout_vertical_htree.readOp.dynamic + tot_power_datain_vertical_htree.readOp.dynamic) * 
        (MAX((g_ip.burst_len / g_ip.int_prefetch_w), 1) - 1);
    }
  }
  
  activate_energy = 
    routing_to_bank_for_activate_energy + hor_htree_routing_e_for_act +
    tot_power_row_predecode_block_drivers.readOp.dynamic + tot_power_row_predecode_blocks.readOp.dynamic +
    tot_power_row_decoders.readOp.dynamic + tot_power_sense_amps.readOp.dynamic;
  read_energy = 
    g_ip.burst_len * 
    (routing_addr_to_bank_for_read_or_write_energy + horizontal_addr_htree_routing_energy_for_read_or_write +
     vertical_addr_htree_routing_energy_for_read_or_write + 
     tot_power_senseamp_mux_lev_1_predecode_block_drivers.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_predecode_block_drivers.readOp.dynamic +
     tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_1_decoders.readOp.dynamic + 
     tot_power_senseamp_mux_lev_2_decoders.readOp.dynamic +
     tot_power_subarray_output_drivers.readOp.dynamic +
     vertical_dataout_htree_routing_energy_for_read +
     horizontal_dataout_htree_energy_for_read +
     routing_datain_bits_to_bank_energy_for_write);
  write_energy =
    g_ip.burst_len *
    (routing_addr_to_bank_for_read_or_write_energy +
     horizontal_addr_htree_routing_energy_for_read_or_write +
     vertical_addr_htree_routing_energy_for_read_or_write +
     routing_datain_bits_to_bank_energy_for_write +
     horizontal_datain_htree_routing_energy_for_write +
     vertical_datain_htree_routing_energy_for_write +
     tot_power_senseamp_mux_lev_1_predecode_block_drivers.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_predecode_block_drivers.readOp.dynamic +
     tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.dynamic +
     tot_power_senseamp_mux_lev_1_decoders.readOp.dynamic +
     tot_power_senseamp_mux_lev_2_decoders.readOp.dynamic);
  precharge_energy =  bitline_data_power.readOp.dynamic + tot_power_bitlines_precharge_eq_driver.readOp.dynamic;


  // calculate leakage power
  tot_power_routing_to_bank.readOp.leakage +=
    power_routing_to_bank.readOp.leakage * number_bits_routed_to_bank *
    (g_ip.num_rw_ports + g_ip.num_rd_ports + g_ip.num_wr_ports);
  if (is_main_mem)
  {
    tot_power_routing_to_bank.readOp.leakage /= (int) g_ip.nbanks;
  }
  tot_power.readOp.leakage += tot_power_routing_to_bank.readOp.leakage;

  tot_power_crossbar.readOp.leakage = 
    crossbar.power.readOp.leakage * NUMBER_INPUT_PORTS_CROSSBAR *
    NUMBER_OUTPUT_PORTS_CROSSBAR * NUMBER_SIGNALS_PER_PORT_CROSSBAR;
  tot_power.readOp.leakage += tot_power_crossbar.readOp.leakage;
    
  htree_seg_multiplier = 1;
  length_htree_segment = pow(2, num_h_htree_nodes - 1) * hor_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for (k = 0; k < num_h_htree_nodes; ++k)
  {
    power_addr_datain_horizontal_htree_node = hor_addr_di_htree_node.compute_power(k);
    if (g_ip.rpters_in_htree == true)
    {
      power_addr_datain_horizontal_htree_node = hor_addr_di_htree_at_mat_interval.compute_power_addr_datain(
          length_htree_segment, k, bank_htree_sizing);
    }
    tot_power_addr_horizontal_htree.readOp.leakage +=
      (number_addr_bits_mat + number_way_select_signals_mat) * 
      power_addr_datain_horizontal_htree_node.readOp.leakage * htree_seg_multiplier *
      (g_ip.num_rw_ports + g_ip.num_rd_ports + g_ip.num_wr_ports);
    tot_power_datain_horizontal_htree.readOp.leakage += 
      num_di_b_mat * num_mats_h_dir * power_addr_datain_horizontal_htree_node.readOp.leakage *
      (g_ip.num_rw_ports + g_ip.num_wr_ports);
    tot_power_dataout_horizontal_htree.readOp.leakage += num_do_b_mat * 
      num_mats_h_dir * power_addr_datain_horizontal_htree_node.readOp.leakage *
      (g_ip.num_rw_ports + g_ip.num_rd_ports);
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }

  tot_power.readOp.leakage += tot_power_addr_horizontal_htree.readOp.leakage +
  tot_power_datain_horizontal_htree.readOp.leakage +
  tot_power_dataout_horizontal_htree.readOp.leakage;

  htree_seg_multiplier = 1;
  length_htree_segment = pow(2, num_v_htree_nodes - 1) * ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for (k = 0; k < num_v_htree_nodes; ++k)
  {
    power_addr_datain_vertical_htree_node = ver_addr_di_htree_node.compute_power(k);
    if (g_ip.rpters_in_htree == true)
    {
      power_addr_datain_vertical_htree_node = ver_addr_di_htree_at_mat_interval.compute_power_addr_datain(
          length_htree_segment, k, bank_htree_sizing);
    }
    tot_power_addr_vertical_htree.readOp.leakage +=
      (number_addr_bits_mat + number_way_select_signals_mat) * 
      power_addr_datain_vertical_htree_node.readOp.leakage * 
      htree_seg_multiplier * num_mats_h_dir *
      (g_ip.num_rw_ports + g_ip.num_rd_ports + g_ip.num_wr_ports);
    tot_power_datain_vertical_htree.readOp.leakage += 
      num_di_b_mat * power_addr_datain_vertical_htree_node.readOp.leakage *
      htree_seg_multiplier * num_mats_h_dir *
      (g_ip.num_rw_ports + g_ip.num_wr_ports);
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }

  if(num_mats_v_dir > 1)
  {
    tot_power_addr_vertical_htree.readOp.leakage *= 2; 
    tot_power_datain_vertical_htree.readOp.leakage *= 2;
  }
  
  tot_power.readOp.leakage += tot_power_addr_vertical_htree.readOp.leakage +
    tot_power_datain_vertical_htree.readOp.leakage;

  if(g_ip.rpters_in_htree == false)
  {
    if(num_mats_v_dir > 1)
    {
      tot_power_dataout_vertical_htree.readOp.leakage = num_do_b_mat * 
        num_mats_h_dir *  2 * power_dout_htree.readOp.leakage *
        (g_ip.num_rw_ports + g_ip.num_rd_ports);
    }
    else
    {//num_mats_v_dir = 1
      tot_power_dataout_vertical_htree.readOp.leakage = num_do_b_mat * 
        num_mats_h_dir *  1 * power_dout_htree.readOp.leakage * 
        (g_ip.num_rw_ports + g_ip.num_rd_ports);
    }
  }
  else
  {
    htree_seg_multiplier = 1;
    length_htree_segment = pow(2, num_v_htree_nodes - 1) * 
      ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
    for (k = 0; k < num_v_htree_nodes; ++k)
    {
      power_dout_vertical_htree_node = ver_addr_di_htree_at_mat_interval.compute_power_dataout_ver(
          length_htree_segment, k, bank_htree_sizing);
      tot_power_dataout_vertical_htree.readOp.leakage += num_do_b_mat * 
        power_dout_vertical_htree_node.readOp.leakage * htree_seg_multiplier * 
        num_mats_h_dir;
      htree_seg_multiplier *= 2;
      length_htree_segment /= 2;
    }
    if(num_mats_v_dir > 1){
      tot_power_dataout_vertical_htree.readOp.leakage *=   2 * (g_ip.num_rw_ports + 
        g_ip.num_rd_ports);
    }
    else{//num_mats_v_dir = 1
      tot_power_dataout_vertical_htree.readOp.leakage *=   1 * (g_ip.num_rw_ports + 
        g_ip.num_rd_ports);
    }
  }
    tot_power.readOp.leakage += tot_power_dataout_vertical_htree.readOp.leakage;

  if (!(fully_assoc&&is_tag))
  { // if fully associative cache and tag array, don't add the following components of leakage power
    tot_power_bitlines.readOp.leakage = bitline_data_power.readOp.leakage *
      num_r_subarray * num_c_subarray * num_subarrays; //This is actually leakage in the memory cells.
    tot_power.readOp.leakage += tot_power_bitlines.readOp.leakage;

    tot_power_bitlines_precharge_eq_driver.readOp.leakage = 
      bl_precharge_eq_drv.power.readOp.leakage * num_subarrays;
    tot_power.readOp.leakage += tot_power_bitlines_precharge_eq_driver.readOp.leakage;

    tot_power_sense_amps.readOp.leakage = sense_amp_data_power.readOp.leakage * 
      (g_ip.num_rw_ports + g_ip.num_rd_ports);
    tot_power.readOp.leakage += tot_power_sense_amps.readOp.leakage;

    tot_power_subarray_output_drivers.readOp.leakage = power_output_drivers_at_subarray.readOp.leakage *
      number_output_drivers_subarray * num_subarrays * 
      (g_ip.num_rw_ports + g_ip.num_rd_ports);
    tot_power.readOp.leakage += tot_power_subarray_output_drivers.readOp.leakage;
  }

  tot_power_comparators.readOp.leakage = 
    comparator_power.readOp.leakage * num_do_b_mat * number_mats *
    (g_ip.num_rw_ports + g_ip.num_rd_ports);
  tot_power.readOp.leakage += tot_power_comparators.readOp.leakage;

  // if DRAM, add refresh power to total leakage
  if (is_dram)
  {
    tot_power.readOp.leakage += refresh_power;
  }
  
  if (is_main_mem)
  {
    tot_power.readOp.leakage += MAIN_MEM_PER_CHIP_STANDBY_CURRENT_mA * 1e-3 * g_tp.peri_global.Vdd / g_ip.nbanks;
  }
  
  leak_power_subbank_closed_page = 
    ((tot_power_row_predecode_blocks.readOp.leakage +
      tot_power_bit_mux_predecode_blocks.readOp.leakage + 
      tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.leakage +
      tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.leakage +
      tot_power_row_decoders.readOp.leakage +
      tot_power_bit_mux_decoders.readOp.leakage +
      tot_power_senseamp_mux_lev_1_decoders.readOp.leakage +
      tot_power_senseamp_mux_lev_2_decoders.readOp.leakage) / number_mats +
     leak_power_sense_amps_closed_page_state) * num_act_mats_hor_dir;
  
  leak_power_subbank_open_page =
    ((tot_power_row_predecode_blocks.readOp.leakage +
      tot_power_bit_mux_predecode_blocks.readOp.leakage + 
      tot_power_senseamp_mux_lev_1_predecode_blocks.readOp.leakage +
      tot_power_senseamp_mux_lev_2_predecode_blocks.readOp.leakage +
      tot_power_row_decoders.readOp.leakage +
      tot_power_bit_mux_decoders.readOp.leakage +
      tot_power_senseamp_mux_lev_1_decoders.readOp.leakage +
      tot_power_senseamp_mux_lev_2_decoders.readOp.leakage) / number_mats +
     leak_power_sense_amps_open_page_state) * num_act_mats_hor_dir;
  
  leak_power_request_and_reply_networks =
    tot_power_routing_to_bank.readOp.leakage + tot_power_crossbar.readOp.leakage +
    tot_power_addr_horizontal_htree.readOp.leakage + tot_power_datain_horizontal_htree.readOp.leakage +
    tot_power_dataout_horizontal_htree.readOp.leakage + tot_power_addr_vertical_htree.readOp.leakage +
    tot_power_datain_vertical_htree.readOp.leakage + tot_power_dataout_vertical_htree.readOp.leakage;


  if(flag_results_populate)
  { //For the final solution, populate the ptr_results data structure
    ptr_results->Ndwl = Ndwl;
    ptr_results->Ndbl = Ndbl;
    ptr_results->Nspd = Nspd;
    ptr_results->deg_bitline_muxing = deg_bl_muxing;
    ptr_results->Ndsam_lev_1 = Ndsam_lev_1;
    ptr_results->Ndsam_lev_2 = Ndsam_lev_2;
    ptr_results->number_activated_mats_horizontal_direction = num_act_mats_hor_dir;
    ptr_results->number_subbanks   = number_mats / num_act_mats_hor_dir;
    ptr_results->page_size_in_bits = num_do_b_mat * num_act_mats_hor_dir * Ndsam_lev_1 * Ndsam_lev_2;
    ptr_results->delay_route_to_bank = delay_route_to_bank;
    ptr_results->delay_crossbar = crossbar.delay;
    ptr_results->delay_addr_din_horizontal_htree = delay_addr_din_horizontal_htree;
    ptr_results->delay_addr_din_vertical_htree = delay_addr_din_vertical_htree;
    ptr_results->delay_row_predecode_driver_and_block = max_delay_before_row_decoder -
      (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
    ptr_results->delay_row_decoder = _row_dec.delay;
    ptr_results->delay_bitlines = delay_bitline;
    ptr_results->delay_sense_amp = delay_sense_amp;
    ptr_results->delay_subarray_output_driver = delay_subarray_output_driver;
    ptr_results->delay_bit_mux_predecode_driver_and_block = max_delay_before_bit_mux_decoder -
      (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
    ptr_results->delay_bit_mux_decoder = _bit_mux_dec.delay;
    ptr_results->delay_senseamp_mux_lev_1_predecode_driver_and_block = max_delay_before_senseamp_mux_lev_1_decoder -
      (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
    ptr_results->delay_senseamp_mux_lev_1_decoder = _senseamp_mux_lev_1_dec.delay;
    ptr_results->delay_senseamp_mux_lev_2_predecode_driver_and_block = max_delay_before_senseamp_mux_lev_2_decoder -
      (delay_route_to_bank + delay_addr_din_horizontal_htree + delay_addr_din_vertical_htree);
    ptr_results->delay_senseamp_mux_lev_2_decoder = _senseamp_mux_lev_2_dec.delay;
    ptr_results->delay_dout_vertical_htree = delay_dout_vertical_htree;
    ptr_results->delay_dout_horizontal_htree = delay_dout_horizontal_htree;
    ptr_results->delay_comparator = comparator_delay;
    ptr_results->access_time = access_time;
    ptr_results->cycle_time = cycle_time;
    ptr_results->multisubbank_interleave_cycle_time = multisubbank_interleave_cycle_time;
    ptr_results->delay_request_network = delay_request_network;
    ptr_results->delay_inside_mat = delay_inside_mat;
    ptr_results->delay_reply_network = delay_reply_network;
    ptr_results->trcd = t_rcd;
    ptr_results->cas_latency = cas_latency;
    ptr_results->precharge_delay = precharge_delay;
    ptr_results->dram_refresh_period = dram_refresh_period;
    ptr_results->dram_array_availability = dram_array_availability;
    ptr_results->power_routing_to_bank = tot_power_routing_to_bank;
    ptr_results->power_addr_horizontal_htree = tot_power_addr_horizontal_htree;
    ptr_results->power_datain_horizontal_htree = tot_power_datain_horizontal_htree;
    ptr_results->power_dataout_horizontal_htree = tot_power_dataout_horizontal_htree;
    ptr_results->power_addr_vertical_htree = tot_power_addr_vertical_htree;
    ptr_results->power_datain_vertical_htree = tot_power_datain_vertical_htree;
    ptr_results->power_row_predecoder_drivers = tot_power_row_predecode_block_drivers;
    ptr_results->power_row_predecoder_blocks = tot_power_row_predecode_blocks;
    ptr_results->power_row_decoders = tot_power_row_decoders;
    ptr_results->power_bit_mux_predecoder_drivers = tot_power_bit_mux_predecode_block_drivers;
    ptr_results->power_bit_mux_predecoder_blocks = tot_power_bit_mux_predecode_blocks;
    ptr_results->power_bit_mux_decoders = tot_power_bit_mux_decoders;
    ptr_results->power_senseamp_mux_lev_1_predecoder_drivers = tot_power_senseamp_mux_lev_1_predecode_block_drivers;
    ptr_results->power_senseamp_mux_lev_1_predecoder_blocks = tot_power_senseamp_mux_lev_1_predecode_blocks;
    ptr_results->power_senseamp_mux_lev_1_decoders = tot_power_senseamp_mux_lev_1_decoders;
    ptr_results->power_senseamp_mux_lev_2_predecoder_drivers = tot_power_senseamp_mux_lev_2_predecode_block_drivers;
    ptr_results->power_senseamp_mux_lev_2_predecoder_blocks = tot_power_senseamp_mux_lev_2_predecode_blocks;
    ptr_results->power_senseamp_mux_lev_2_decoders = tot_power_senseamp_mux_lev_2_decoders;
    ptr_results->power_bitlines = tot_power_bitlines;
    ptr_results->power_sense_amps = tot_power_sense_amps;
    ptr_results->power_prechg_eq_drivers = tot_power_bitlines_precharge_eq_driver;
    ptr_results->power_output_drivers_at_subarray = tot_power_subarray_output_drivers;
    ptr_results->power_dataout_vertical_htree = tot_power_dataout_vertical_htree;
    ptr_results->power_comparators = tot_power_comparators;
    ptr_results->power_crossbar    = tot_power_crossbar;
    ptr_results->total_power = tot_power;
    ptr_results->dyn_read_energy_from_closed_page = dyn_read_energy_from_closed_page;
    ptr_results->dyn_read_energy_from_open_page = dyn_read_energy_from_open_page;
    ptr_results->leak_power_subbank_closed_page = leak_power_subbank_closed_page;
    ptr_results->leak_power_subbank_open_page = leak_power_subbank_open_page;
    ptr_results->leak_power_request_and_reply_networks = leak_power_request_and_reply_networks;
    ptr_results->area = all_banks.h * all_banks.w * 1e-6;//in mm2
    ptr_results->all_banks_height = all_banks.h;
    ptr_results->all_banks_width = all_banks.w;    
    ptr_results->bank_height = bank.h;
    ptr_results->bank_width = bank.w;
    ptr_results->subarray_memory_cell_area_height = subarray.get_h();
    ptr_results->subarray_memory_cell_area_width  = subarray.get_w();
    ptr_results->mat_height = mat.h;
    ptr_results->mat_width = mat.w;
    ptr_results->routing_area_height_within_bank = bank.h - num_mats_v_dir * mat.h;
    ptr_results->routing_area_width_within_bank = bank.w - num_mats_h_dir *  mat.w;
    ptr_results->area_efficiency = area_all_dataramcells * 100 / (all_banks.h * all_banks.w);
    ptr_results->perc_power_dyn_routing_to_bank = ptr_results->power_routing_to_bank.readOp.dynamic * 100 / 
      ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_addr_horizontal_htree = 
      ptr_results->power_addr_horizontal_htree.readOp.dynamic * 100 / 
      ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_dataout_horizontal_htree = 
      ptr_results->power_dataout_horizontal_htree.readOp.dynamic * 100 / 
      ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_addr_vertical_htree = 
      ptr_results->power_addr_vertical_htree.readOp.dynamic * 100 /  ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_row_predecoder_drivers =
      ptr_results->power_row_predecoder_drivers.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_row_predecoder_blocks =
      ptr_results->power_row_predecoder_blocks.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_row_decoders =
      ptr_results->power_row_decoders.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_bit_mux_predecoder_drivers =
      ptr_results->power_bit_mux_predecoder_drivers.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_bit_mux_predecoder_blocks =
      ptr_results->power_bit_mux_predecoder_blocks.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_bit_mux_decoders =
      ptr_results->power_bit_mux_decoders.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_1_predecoder_drivers = 
      ptr_results->power_senseamp_mux_lev_1_predecoder_drivers.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_1_predecoder_blocks =
      ptr_results->power_senseamp_mux_lev_1_predecoder_blocks.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_1_decoders =
      ptr_results->power_senseamp_mux_lev_1_decoders.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_2_predecoder_drivers = 
      ptr_results->power_senseamp_mux_lev_2_predecoder_drivers.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_2_predecoder_blocks =
      ptr_results->power_senseamp_mux_lev_2_predecoder_blocks.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_senseamp_mux_lev_2_decoders =
      ptr_results->power_senseamp_mux_lev_2_decoders.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_bitlines = 
      ptr_results->power_bitlines.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_sense_amps =
      ptr_results->power_sense_amps.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_prechg_eq_drivers = 
      ptr_results->power_prechg_eq_drivers.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;   
    ptr_results->perc_power_dyn_subarray_output_drivers =
      ptr_results->power_output_drivers_at_subarray.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_dataout_vertical_htree =
      ptr_results->power_dataout_vertical_htree.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_comparators =
      ptr_results->power_comparators.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->perc_power_dyn_crossbar =
      ptr_results->power_crossbar.readOp.dynamic * 100 / ptr_fin_res->power.readOp.dynamic;
    ptr_results->activate_energy = activate_energy;
    ptr_results->read_energy = read_energy;
    ptr_results->write_energy = write_energy;
    ptr_results->precharge_energy = precharge_energy;
    
    ptr_results->perc_power_dyn_spent_outside_mats = 
      ptr_results->perc_power_dyn_routing_to_bank + ptr_results->perc_power_dyn_addr_horizontal_htree +
      ptr_results->perc_power_dyn_addr_vertical_htree + ptr_results->perc_power_dyn_dataout_vertical_htree +
      ptr_results->perc_power_dyn_dataout_horizontal_htree;

    ptr_results->perc_power_leak_routing_to_bank = 
      ptr_results->power_routing_to_bank.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_addr_horizontal_htree = 
      ptr_results->power_addr_horizontal_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_datain_horizontal_htree = 
      ptr_results->power_datain_horizontal_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_dataout_horizontal_htree = 
      ptr_results->power_dataout_horizontal_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_addr_vertical_htree = 
      ptr_results->power_addr_vertical_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_datain_vertical_htree = 
      ptr_results->power_datain_vertical_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_row_predecoder_drivers =
      ptr_results->power_row_predecoder_drivers.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_row_predecoder_blocks =
      ptr_results->power_row_predecoder_blocks.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_row_decoders =
      ptr_results->power_row_decoders.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_bit_mux_predecoder_drivers =
      ptr_results->power_bit_mux_predecoder_drivers.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_bit_mux_predecoder_blocks =
      ptr_results->power_bit_mux_predecoder_blocks.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_bit_mux_decoders =
      ptr_results->power_bit_mux_decoders.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_1_predecoder_drivers = 
      ptr_results->power_senseamp_mux_lev_1_predecoder_drivers.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_1_predecoder_blocks =
      ptr_results->power_senseamp_mux_lev_1_predecoder_blocks.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_1_decoders =
      ptr_results->power_senseamp_mux_lev_1_decoders.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_2_predecoder_drivers = 
      ptr_results->power_senseamp_mux_lev_2_predecoder_drivers.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_2_predecoder_blocks =
      ptr_results->power_senseamp_mux_lev_2_predecoder_blocks.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_senseamp_mux_lev_2_decoders =
      ptr_results->power_senseamp_mux_lev_2_decoders.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_bitlines = 
      ptr_results->power_bitlines.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_sense_amps =
      ptr_results->power_sense_amps.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_prechg_eq_drivers = 
      ptr_results->power_prechg_eq_drivers.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;    
    ptr_results->perc_power_leak_subarray_output_drivers =
      ptr_results->power_output_drivers_at_subarray.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_dataout_vertical_htree =
      ptr_results->power_dataout_vertical_htree.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_comparators =
      ptr_results->power_comparators.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->perc_power_leak_crossbar =
      ptr_results->power_crossbar.readOp.leakage * 100 / ptr_fin_res->power.readOp.leakage;
    ptr_results->refresh_power = refresh_power;
    ptr_results->perc_leak_mats =
      ptr_results->perc_power_leak_row_predecoder_drivers +
      ptr_results->perc_power_leak_row_predecoder_blocks +
      ptr_results->perc_power_leak_row_decoders +
      ptr_results->perc_power_leak_bit_mux_predecoder_drivers +
      ptr_results->perc_power_leak_bit_mux_predecoder_blocks +
      ptr_results->perc_power_leak_bit_mux_decoders +
      ptr_results->perc_power_leak_senseamp_mux_lev_1_predecoder_drivers +
      ptr_results->perc_power_leak_senseamp_mux_lev_2_predecoder_drivers +
      ptr_results->perc_power_leak_senseamp_mux_lev_1_predecoder_blocks +
      ptr_results->perc_power_leak_senseamp_mux_lev_2_predecoder_blocks +
      ptr_results->perc_power_leak_senseamp_mux_lev_1_decoders +
      ptr_results->perc_power_leak_senseamp_mux_lev_2_decoders +
      ptr_results->perc_power_leak_bitlines +
      ptr_results->perc_power_leak_sense_amps +
      ptr_results->perc_power_leak_subarray_output_drivers +
      ptr_results->perc_power_leak_comparators;
    ptr_results->perc_active_mats = (double) (num_act_mats_hor_dir) * 100 / (double) (number_mats);
  }
  else
  {
    ptr_array->Ndwl = Ndwl;
    ptr_array->Ndbl = Ndbl;
    ptr_array->Nspd = Nspd;
    ptr_array->deg_bitline_muxing = deg_bl_muxing;
    ptr_array->Ndsam_lev_1 = Ndsam_lev_1;
    ptr_array->Ndsam_lev_2 = Ndsam_lev_2;
    ptr_array->access_time = access_time;
    ptr_array->cycle_time = cycle_time;
    ptr_array->multisubbank_interleave_cycle_time = multisubbank_interleave_cycle_time;
    ptr_array->area_ram_cells = area_all_dataramcells;
    ptr_array->area   = all_banks.h * all_banks.w;
    ptr_array->height = all_banks.h;
    ptr_array->width  = all_banks.w;
    ptr_array->power  = tot_power;
    ptr_array->delay_senseamp_mux_decoder =
      MAX(max_delay_before_senseamp_mux_lev_1_decoder + _senseamp_mux_lev_1_dec.delay,
          max_delay_before_senseamp_mux_lev_2_decoder + _senseamp_mux_lev_2_dec.delay);
    ptr_array->delay_before_subarray_output_driver = delay_before_subarray_output_driver;
    ptr_array->delay_from_subarray_output_driver_to_output = delay_from_subarray_output_driver_to_output;
  }

  return true;
}


void do_it(final_results *fin_res)
{
  bool   is_dram = false;
  bool   is_valid_partition;
  int    pure_ram;
  double max_efficiency, min_efficiency, area, efficiency;
  int    weight_dynamic_energy, weight_leakage_power, weight_dynamic_power, weight_cycle_time;
  double min_acc_time, min_acc_time_in_best_area_solutions,
         percent_diff_efficiency_wrt_max_efficiency, percent_diff_acc_time_wrt_min_acc_time, 
         perc_away_from_aspect_ratio, aspect_ratio, perc_wasted_area;
  double best_acc_time_tag_arr;
  double stacked_die_allot_area;

  ArrayEdgeToBankEdgeHtreeSizing array_edge_to_bank_edge_htree_sizing;
  BankHtreeSizing bank_htree_sizing;

  list<mem_array *> tag_arr (0);
  list<mem_array *> data_arr(0);
  list<mem_array *>::iterator best_acc_time_tag_arr_iter;
  list<mem_array *>::iterator miter;
  list<solution *>::iterator siter;
  list<solution *> best_sol(1, new solution);
  list<solution *> best_delay_solution(0);  // best delay solution within best area solutions
  
  pure_ram    = !g_ip.is_cache;
  result_type result;
  result.cycle_time = BIGNUM;
  result.access_time = BIGNUM;
  result.total_power.readOp.dynamic = result.total_power.readOp.leakage
                                    = result.total_power.writeOp.dynamic
                                    = result.total_power.writeOp.leakage
                                    = BIGNUM;
  result.max_access_time = 0;
  result.min_access_time = BIGNUM;
  min_acc_time = BIGNUM;
  min_acc_time_in_best_area_solutions = BIGNUM;
  result.max_cycle_time = 0;
  result.min_cycle_time = BIGNUM;
  result.max_leakage_power = 0;
  result.min_leakage_power = BIGNUM;
  result.max_dynamic_power = 0;
  result.min_dynamic_power = BIGNUM;
  result.max_dynamic_energy = 0;
  result.min_dynamic_energy = BIGNUM;
  max_efficiency = 1.0 / BIGNUM;
  min_efficiency = BIGNUM;
  best_acc_time_tag_arr = BIGNUM;

  fin_res->tag_array.access_time = 0;
  fin_res->tag_array.Ndwl = 0;
  fin_res->tag_array.Ndbl = 0;
  fin_res->tag_array.Nspd = 0;
  fin_res->tag_array.deg_bitline_muxing = 0;
  fin_res->tag_array.Ndsam_lev_1 = 0;
  fin_res->tag_array.Ndsam_lev_2 = 0;


  // distribute calculate_time() execution into multiple threads
  calc_time_mt_wrapper_struct * calc_array = new calc_time_mt_wrapper_struct[nthreads];
  pthread_t threads[nthreads];

  for (uint32_t t = 0; t < nthreads; t++)
  {
    calc_array[t].tid         = t;
    calc_array[t].pure_ram    = pure_ram;
  }

  bool     is_tag;
  uint32_t ram_cell_tech_type;

  //If it's a cache, first calculate the area, delay and power for all tag array partitions.
  if (!pure_ram)
  { //cache
    is_tag              = true;
    ram_cell_tech_type  = g_ip.tag_arr_ram_cell_tech_type;
    is_dram             = ((ram_cell_tech_type == 3) || (ram_cell_tech_type == 4));
    init_tech_params(g_ip.F_sz_um, is_tag);
    array_edge_to_bank_edge_htree_sizing.compute_widths(is_dram);
    bank_htree_sizing.compute_widths(is_dram);
    
    for (uint32_t t = 0; t < nthreads; t++)
    {
      calc_array[t].is_tag      = is_tag;
      calc_array[t].is_main_mem = false;
      calc_array[t].ptr_arr_edge_to_bank_edge_htree_sizing = & array_edge_to_bank_edge_htree_sizing;
      calc_array[t].ptr_bank_htree_sizing = & bank_htree_sizing;
      calc_array[t].Nspd_min    = 0.125;
      pthread_create(&threads[t], NULL, calc_time_mt_wrapper, (void *)(&(calc_array[t])));
    }

    for (uint32_t t = 0; t < nthreads; t++)
    {
      pthread_join(threads[t], NULL);
    }

    for (uint32_t t = 0; t < nthreads; t++)
    {
      calc_array[t].data_arr.sort(mem_array::lt);
      data_arr.merge(calc_array[t].data_arr, mem_array::lt);
      calc_array[t].tag_arr.sort(mem_array::lt);
      tag_arr.merge(calc_array[t].tag_arr, mem_array::lt);
    }
  }


  //Calculate the area, delay and power for all data array partitions (for cache or plain RAM).
  if(!g_ip.fully_assoc)
  {
    is_tag              = false;
    ram_cell_tech_type  = g_ip.data_arr_ram_cell_tech_type;
    is_dram             = ((ram_cell_tech_type == 3) || (ram_cell_tech_type == 4));
    init_tech_params(g_ip.F_sz_um, is_tag);
    array_edge_to_bank_edge_htree_sizing.compute_widths(is_dram);
    bank_htree_sizing.compute_widths(is_dram);

    for (uint32_t t = 0; t < nthreads; t++)
    {
      calc_array[t].is_tag      = is_tag;
      calc_array[t].is_main_mem = g_ip.is_main_mem;
      calc_array[t].ptr_arr_edge_to_bank_edge_htree_sizing = & array_edge_to_bank_edge_htree_sizing;
      calc_array[t].ptr_bank_htree_sizing = & bank_htree_sizing;
      calc_array[t].Nspd_min    = (double)(g_ip.out_w)/(double)(g_ip.block_sz*8);
      pthread_create(&threads[t], NULL, calc_time_mt_wrapper, (void *)(&(calc_array[t])));
    }

    for (uint32_t t = 0; t < nthreads; t++)
    {
      pthread_join(threads[t], NULL);
    }

    data_arr.clear();
    for (uint32_t t = 0; t < nthreads; t++)
    {
      calc_array[t].data_arr.sort(mem_array::lt);
      data_arr.merge(calc_array[t].data_arr, mem_array::lt);
    }

    if (!pure_ram)
    {
      for (miter = tag_arr.begin(); miter != tag_arr.end(); ++miter)
      {
        // find the tag array with the best access time
        double acc_time_tag_arr = (*miter)->access_time;
        if (acc_time_tag_arr < best_acc_time_tag_arr)
        {
          best_acc_time_tag_arr = acc_time_tag_arr;
          best_acc_time_tag_arr_iter = miter;
        }
      }

      // find best area for cache
      for (miter = data_arr.begin(); miter != data_arr.end(); ++miter)
      {
        area = (*best_acc_time_tag_arr_iter)->area + (*miter)->area;
        efficiency = ((*best_acc_time_tag_arr_iter)->area_ram_cells + (*miter)->area_ram_cells) * 100 / area;
        if (efficiency > max_efficiency)
        {
          max_efficiency = efficiency;
        }
      }
    }
    else
    { //Pure RAM, find best area.
      for (miter = data_arr.begin(); miter != data_arr.end(); ++miter)
      {
        area = (*miter)->area;
        efficiency = (*miter)->area_ram_cells * 100 / area;
        if (efficiency > max_efficiency)
        {
          max_efficiency = efficiency;
        }
      }
    }
  }

  // find all solutions that are within MAXAREACONSTRAINT_PERC of the best area solution.
  if (g_ip.fully_assoc)
  { //fully-associative cache
    list<mem_array *>::iterator t_i, d_i;
    for (t_i = tag_arr.begin(), d_i = data_arr.begin(); t_i != tag_arr.end(); ++t_i, ++d_i)
    {
      area = (*t_i)->area + (*d_i)->area;
      efficiency = ((*t_i)->area_ram_cells + (*d_i)->area_ram_cells) * 100 / area;
      if (efficiency > max_efficiency)
      {
        max_efficiency = efficiency;
      }
    }
    for (t_i = tag_arr.begin(), d_i = data_arr.begin(); t_i != tag_arr.end(); ++t_i, ++d_i)
    {
      best_sol.back()->area = (*t_i)->area + (*d_i)->area;
      best_sol.back()->access_time = (*t_i)->access_time + (*d_i)->access_time;
      best_sol.back()->cycle_time = MAX((*t_i)->cycle_time, (*d_i)->cycle_time);
      best_sol.back()->efficiency = ((*t_i)->area_ram_cells + (*d_i)->area_ram_cells) * 100 / best_sol.back()->area;
      percent_diff_efficiency_wrt_max_efficiency = (max_efficiency - best_sol.back()->efficiency) * 100 / max_efficiency;

      aspect_ratio = (*t_i)->height / (*t_i)->width;
      perc_away_from_aspect_ratio = _abs((aspect_ratio - STACKED_DIE_LAYER_ASPECT_RATIO)* 100 / STACKED_DIE_LAYER_ASPECT_RATIO);
      stacked_die_allot_area = STACKED_DIE_LAYER_ALLOTED_AREA_mm2;
      perc_wasted_area = 0;
      
      if (stacked_die_allot_area != 0)
      {
        perc_wasted_area = _abs((stacked_die_allot_area - best_sol.back()->area * 1e-6) * 100 / stacked_die_allot_area);
      }
      if ((is_dram == false && best_sol.back()->cycle_time <= TARGET_CYCLE_TIME_ns * 1e-9) || is_dram == true)
      {
        if (((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) && 
             (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO > 100) && 
             (best_sol.back()->area * 1e-6 <= STACKED_DIE_LAYER_ALLOTED_AREA_mm2) &&
             (perc_wasted_area <= MAX_PERCENT_AWAY_FROM_ALLOTED_AREA) &&
             (best_sol.back()->efficiency >= MIN_AREA_EFFICIENCY)) ||
            ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) && 
             (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO <= 100) && 
             (perc_away_from_aspect_ratio <= MAX_PERCENT_AWAY_FROM_ASPECT_RATIO)) ||
            ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 == 0) && 
             (percent_diff_efficiency_wrt_max_efficiency <= g_ip.max_area_t_constraint_perc)))
        {
          
          best_sol.back()->tag_array_iter  = t_i;
          best_sol.back()->data_array_iter = d_i;
          best_sol.back()->total_power = (*t_i)->power + (*d_i)->power;
          if (best_sol.back()->access_time < min_acc_time_in_best_area_solutions)
          {
            min_acc_time_in_best_area_solutions = best_sol.back()->access_time;
          }
          best_sol.push_back(new solution);
        }
      }
    }
  }
  else if(!pure_ram) 
  { //cache
    for (miter = data_arr.begin(); miter != data_arr.end(); ++miter)
    {
      mem_array * tag_array = (*best_acc_time_tag_arr_iter);
      best_sol.back()->area = tag_array->area + (*miter)->area ;
      best_sol.back()->cycle_time = MAX((*miter)->cycle_time, tag_array->cycle_time);
      best_sol.back()->efficiency = (tag_array->area_ram_cells + (*miter)->area_ram_cells) * 100 / best_sol.back()->area;
      percent_diff_efficiency_wrt_max_efficiency = 
        (max_efficiency - best_sol.back()->efficiency) * 100 / max_efficiency;

      aspect_ratio = (*miter)->height / (*miter)->width;
      perc_away_from_aspect_ratio = _abs((aspect_ratio - STACKED_DIE_LAYER_ASPECT_RATIO)* 100 / STACKED_DIE_LAYER_ASPECT_RATIO);
      stacked_die_allot_area = STACKED_DIE_LAYER_ALLOTED_AREA_mm2;
      perc_wasted_area = 0;
      
      if (stacked_die_allot_area != 0)
      {	
        perc_wasted_area = _abs((stacked_die_allot_area - best_sol.back()->area * 1e-6) * 100 / stacked_die_allot_area);
      }
      if ((is_dram == false && best_sol.back()->cycle_time <= TARGET_CYCLE_TIME_ns * 1e-9) || is_dram == true)
      {
        if(((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) && 
            (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO > 100) && 
            (best_sol.back()->area * 1e-6 <= STACKED_DIE_LAYER_ALLOTED_AREA_mm2) &&
            (perc_wasted_area <= MAX_PERCENT_AWAY_FROM_ALLOTED_AREA) &&
            (best_sol.back()->efficiency > 30)) ||
           ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) &&
            (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO <= 100) && 
            (perc_away_from_aspect_ratio <= MAX_PERCENT_AWAY_FROM_ASPECT_RATIO)) ||
           ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 == 0) &&
            (percent_diff_efficiency_wrt_max_efficiency <= g_ip.max_area_t_constraint_perc)))
        {
          best_sol.back()->tag_array_iter  = best_acc_time_tag_arr_iter;
          best_sol.back()->data_array_iter = miter;
          if(g_ip.is_seq_acc)
          { //Sequential access
            best_sol.back()->access_time = tag_array->access_time + (*miter)->access_time;
          }
          else if(g_ip.fast_access)
          {
            best_sol.back()->access_time = MAX(tag_array->access_time, (*miter)->access_time);
          }
          else
          { //normal access
            if (g_ip.tag_assoc > 1)
            { //set-associative cache
              best_sol.back()->access_time = MAX(tag_array->access_time + (*miter)->delay_senseamp_mux_decoder,
                  (*miter)->delay_before_subarray_output_driver) +
                  (*miter)->delay_from_subarray_output_driver_to_output;
            }
            else
            { //direct-mapped cache
              best_sol.back()->access_time = MAX(tag_array->access_time, (*miter)->access_time);
            }
          }
          best_sol.back()->total_power = tag_array->power + (*miter)->power;
          if (best_sol.back()->access_time < min_acc_time_in_best_area_solutions)
          {
            min_acc_time_in_best_area_solutions = best_sol.back()->access_time;
          }
          best_sol.push_back(new solution);
        }
      }
    }
  }
  else
  { //plain RAM
    for (miter = data_arr.begin(); miter != data_arr.end(); ++miter)
    {
      best_sol.back()->area = (*miter)->area;
      best_sol.back()->efficiency =  (*miter)->area_ram_cells * 100 / best_sol.back()->area;
      percent_diff_efficiency_wrt_max_efficiency = 
        (max_efficiency - best_sol.back()->efficiency) * 100 / max_efficiency;

      aspect_ratio = (*miter)->height / (*miter)->width;
      perc_away_from_aspect_ratio = _abs((aspect_ratio - STACKED_DIE_LAYER_ASPECT_RATIO)* 100 / STACKED_DIE_LAYER_ASPECT_RATIO);
      if (((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) && 
           (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO > 100) &&
           ((*miter)->area * 1e-6 <= STACKED_DIE_LAYER_ALLOTED_AREA_mm2)) ||
          ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 > 0) &&
           (MAX_PERCENT_AWAY_FROM_ASPECT_RATIO <= 100) &&
           (perc_away_from_aspect_ratio <= MAX_PERCENT_AWAY_FROM_ASPECT_RATIO)) ||
          ((STACKED_DIE_LAYER_ALLOTED_AREA_mm2 == 0) &&
           (percent_diff_efficiency_wrt_max_efficiency <= g_ip.max_area_t_constraint_perc)))
      {
        best_sol.back()->data_array_iter = miter;
        best_sol.back()->access_time = (*miter)->access_time;
        best_sol.back()->cycle_time  = (*miter)->cycle_time;
        best_sol.back()->total_power = (*miter)->power;
        if (best_sol.back()->access_time < min_acc_time_in_best_area_solutions)
        {
          min_acc_time_in_best_area_solutions = best_sol.back()->access_time;
        }
        best_sol.push_back(new solution);
      }
    }
  }
  best_sol.pop_back();

  //Inside the best area solutions, find all solutions that are within MAXACCTIMECONSTRAINT_PERC of 
  //min_acc_time_in_best_area.
  //We apply the objective function only to these solutions. 
  for (siter = best_sol.begin(); siter != best_sol.end(); ++siter)
  {
    percent_diff_acc_time_wrt_min_acc_time = 
      ((*siter)->access_time - min_acc_time_in_best_area_solutions) * 100 / min_acc_time_in_best_area_solutions;
    if (percent_diff_acc_time_wrt_min_acc_time <= g_ip.max_acc_t_constraint_perc)
    {
      best_delay_solution.push_back(*siter);
    }
  }
  
  
  //Find the values for best dynamic energy, dynamic power, best leakage power and best cycle time.
  for (siter = best_delay_solution.begin(); siter != best_delay_solution.end(); ++siter)
  {
    if (result.min_dynamic_energy > (*siter)->total_power.readOp.dynamic)
    {
      result.min_dynamic_energy = (*siter)->total_power.readOp.dynamic;
    }
    if (result.min_dynamic_power > (*siter)->total_power.readOp.dynamic / (*siter)->cycle_time)
    {
      result.min_dynamic_power = (*siter)->total_power.readOp.dynamic / (*siter)->cycle_time;
    }
    if (result.min_leakage_power > (*siter)->total_power.readOp.leakage)
    {
      result.min_leakage_power = (*siter)->total_power.readOp.leakage;
    }
    if (result.min_cycle_time > (*siter)->cycle_time)
    {
      result.min_cycle_time = (*siter)->cycle_time;
    }
  }
  
  weight_dynamic_energy = 1;
  weight_leakage_power  = 1;
  weight_dynamic_power  = 1;
  weight_cycle_time     = 1;
  
  for (siter = best_delay_solution.begin(); siter != best_delay_solution.end(); ++siter)
  {
    if ((objective_function(
            g_ip.obj_func_dyn_energy,
            g_ip.obj_func_dyn_power,
            g_ip.obj_func_leak_power, 
            g_ip.obj_func_cycle_t,
            weight_dynamic_energy,
            weight_dynamic_power,
            weight_leakage_power,
            weight_cycle_time,
            result.total_power.readOp.dynamic / result.min_dynamic_energy,
            (result.total_power.readOp.dynamic / result.cycle_time) / result.min_dynamic_power, 
            result.total_power.readOp.leakage / result.min_leakage_power, 
            result.cycle_time / result.min_cycle_time) >
         objective_function(
           g_ip.obj_func_dyn_energy,
           g_ip.obj_func_dyn_power,
           g_ip.obj_func_leak_power, 
           g_ip.obj_func_cycle_t,
           weight_dynamic_energy,
           weight_dynamic_power,
           weight_leakage_power, 
           weight_cycle_time,
           (*siter)->total_power.readOp.dynamic / result.min_dynamic_energy,
           ((*siter)->total_power.readOp.dynamic / (*siter)->cycle_time) / result.min_dynamic_power, 
           (*siter)->total_power.readOp.leakage / result.min_leakage_power, 
           (*siter)->cycle_time / result.min_cycle_time)) || siter == best_delay_solution.begin())
    {
      result.total_power       = (*siter)->total_power;
      fin_res->area            = (*siter)->area * 1e-6;
      result.cycle_time        = (*siter)->cycle_time;
      result.access_time       = (*siter)->access_time;
      fin_res->area_efficiency = (*siter)->efficiency;
      list<mem_array *>::iterator d_i = (*siter)->data_array_iter;
      result.best_Nspd = (*d_i)->Nspd;
      result.best_Ndwl = (*d_i)->Ndwl;
      result.best_Ndbl = (*d_i)->Ndbl;
      result.best_data_deg_bitline_muxing = (*d_i)->deg_bitline_muxing;
      result.best_Ndsam_lev_1 = (*d_i)->Ndsam_lev_1;
      result.best_Ndsam_lev_2 = (*d_i)->Ndsam_lev_2;
      if (!pure_ram)
      {
        list<mem_array *>::iterator t_i = (*siter)->tag_array_iter;
        result.best_Ntspd = (*t_i)->Nspd;
        result.best_Ntwl  = (*t_i)->Ndwl;
        result.best_Ntbl  = (*t_i)->Ndbl;
        result.best_tag_deg_bitline_muxing = (*t_i)->deg_bitline_muxing;
        result.best_Ntsam_lev_1 = (*t_i)->Ndsam_lev_1;
        result.best_Ntsam_lev_2 = (*t_i)->Ndsam_lev_2;
      }
    }
  }
  
  fin_res->access_time     = result.access_time;
  fin_res->cycle_time      = result.cycle_time;
  fin_res->power           = result.total_power;

  if (!pure_ram)
  {
    is_tag              = true;
    ram_cell_tech_type  = g_ip.tag_arr_ram_cell_tech_type;
    is_dram             = ((ram_cell_tech_type == 3) || (ram_cell_tech_type == 4));
    init_tech_params(g_ip.F_sz_um, is_tag);
    array_edge_to_bank_edge_htree_sizing.compute_widths(is_dram);
    bank_htree_sizing.compute_widths(is_dram);
    is_valid_partition = calculate_time(
        true, pure_ram, result.best_Ntspd, result.best_Ntwl, 
        result.best_Ntbl, result.best_tag_deg_bitline_muxing,
        result.best_Ntsam_lev_1, result.best_Ntsam_lev_2,
        NULL, 1, &fin_res->tag_array, fin_res,
        array_edge_to_bank_edge_htree_sizing,
        bank_htree_sizing,
        false);

    is_tag              = false;
    ram_cell_tech_type  = g_ip.data_arr_ram_cell_tech_type;
    is_dram             = ((ram_cell_tech_type == 3) || (ram_cell_tech_type == 4));
    init_tech_params(g_ip.F_sz_um, is_tag);
    array_edge_to_bank_edge_htree_sizing.compute_widths(is_dram);
    bank_htree_sizing.compute_widths(is_dram);
    is_valid_partition = calculate_time(
        false, pure_ram, result.best_Nspd, result.best_Ndwl, 
        result.best_Ndbl, result.best_data_deg_bitline_muxing,
        result.best_Ndsam_lev_1, result.best_Ndsam_lev_2,
        NULL, 1, &fin_res->data_array, fin_res,
        array_edge_to_bank_edge_htree_sizing,
        bank_htree_sizing,
        g_ip.is_main_mem);
  }
  else
  {
    is_tag              = false;
    ram_cell_tech_type  = g_ip.data_arr_ram_cell_tech_type;
    is_dram             = ((ram_cell_tech_type == 3) || (ram_cell_tech_type == 4));
    init_tech_params(g_ip.F_sz_um, is_tag);
    array_edge_to_bank_edge_htree_sizing.compute_widths(is_dram);
    bank_htree_sizing.compute_widths(is_dram);
    is_valid_partition = calculate_time(
        false, pure_ram, result.best_Nspd, result.best_Ndwl,
        result.best_Ndbl, result.best_data_deg_bitline_muxing,
        result.best_Ndsam_lev_1, result.best_Ndsam_lev_2,
        NULL, 1, &fin_res->data_array, fin_res,
        array_edge_to_bank_edge_htree_sizing,
        bank_htree_sizing,
        g_ip.is_main_mem);
    fin_res->tag_array.perc_leak_mats = 0;
    fin_res->tag_array.perc_active_mats = 0;
  }
  fin_res->leak_power_with_sleep_transistors_in_mats =
    (100 - fin_res->data_array.perc_leak_mats - fin_res->tag_array.perc_leak_mats) * fin_res->power.readOp.leakage / 100 +
    fin_res->data_array.perc_active_mats * (fin_res->data_array.perc_leak_mats * fin_res->power.readOp.leakage / 100) / 100 + 
    fin_res->tag_array.perc_active_mats  * (fin_res->tag_array.perc_leak_mats  * fin_res->power.readOp.leakage / 100) / 100  +
    ((100 - fin_res->data_array.perc_active_mats) * (fin_res->data_array.perc_leak_mats * fin_res->power.readOp.leakage / 100) / 100) / MAT_LEAKAGE_REDUCTION_DUE_TO_SLEEP_TRANSISTORS_FACTOR +
    ((100 - fin_res->tag_array.perc_active_mats)  * (fin_res->tag_array.perc_leak_mats  * fin_res->power.readOp.leakage / 100) / 100) / MAT_LEAKAGE_REDUCTION_DUE_TO_SLEEP_TRANSISTORS_FACTOR;

    
  if (fin_res)
  {
    fin_res->cache_ht = MAX(fin_res->tag_array.all_banks_height, fin_res->data_array.all_banks_height);
    fin_res->cache_len = fin_res->tag_array.all_banks_width + fin_res->data_array.all_banks_width;
  }

  fin_res->valid = is_valid_partition;
  fin_res->ip    = g_ip;
  fin_res->vdd_periph_global = g_tp.peri_global.Vdd; 
}

