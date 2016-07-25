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
#include "decoder.h"
#include "parameter.h"
#include <iostream>
#include <math.h>
#include <assert.h>

using namespace std;


Decoder::Decoder(
    int    _num_dec_signals,
    bool   _flag_way_select,
    double _C_ld_dec_out,
    double _R_wire_dec_out)
 :exist(false),
  C_ld_dec_out(_C_ld_dec_out),
  R_wire_dec_out(_R_wire_dec_out),
  num_gates_min(2),
  delay(0),
  power(),
  area()
{
  for (int i = 0; i < MAX_NUMBER_GATES_STAGE; i++)
  {
    w_dec_n[i] = 0;
    w_dec_p[i] = 0;
  }

  int num_addr_bits_dec = _log2(_num_dec_signals);

  if (num_addr_bits_dec < 4)
  {
    if (_flag_way_select)
    {
      exist = true;
      num_in_signals = 2;
    }
    else
    {
      num_in_signals = 0;
    }
  }
  else
  {
    exist = true;

    if (_flag_way_select)
    {
      num_in_signals = 3;
    }
    else
    {
      num_in_signals = 2;
    }
  }
}


void Decoder::compute_widths(
    bool fully_assoc,
    bool is_dram_,
    bool is_wl_tr_)
{
  double F;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram_, is_wl_tr_);
  double gnand2     = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnand3     = (3 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);

  if (exist)
  {
    if (num_in_signals == 2 || fully_assoc)
    {
      w_dec_n[0] = 2 * g_tp.min_w_nmos_;
      w_dec_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = gnand2;
    }
    else
    {
      assert(num_in_signals == 3);
      w_dec_n[0] = 3 * g_tp.min_w_nmos_;
      w_dec_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = gnand3;
    }
    F *= C_ld_dec_out / (gate_C(w_dec_n[0], 0, is_dram_, false, is_wl_tr_) +
                         gate_C(w_dec_p[0], 0, is_dram_, false, is_wl_tr_));
    num_gates = logical_effort(
        num_gates_min,
        num_in_signals == 2 ? gnand2 : gnand3,
        F,
        w_dec_n,
        w_dec_p,
        C_ld_dec_out,
        2,
        is_dram_,
        is_wl_tr_);
  }
}


void Decoder::compute_area(const Area & cell, bool is_dram_)
{
  Area nand2, nand3, inv;
  double cumulative_area  = 0;
  double cumulative_power = 0;
  
  if (exist)
  { // First check if this decoder exists
    // The height of a row-decoder-driver cell is fixed to be 4 * cell.h;
    nand2 = Area::gatearea(NAND, 2, w_dec_p[0], w_dec_n[0], 4 * cell.h);
    nand3 = Area::gatearea(NAND, 3, w_dec_p[0], w_dec_n[0], 4 * cell.h); 
    if (num_in_signals == 2)
    {
      cumulative_area  += nand2.get_area();  
      cumulative_power += cmos_Ileak(w_dec_n[0], w_dec_p[0], is_dram_) * NAND2_LEAK_STACK_FACTOR;
    }
    else if(num_in_signals == 3)
    {
      cumulative_area  += nand3.get_area();    
      cumulative_power += cmos_Ileak(w_dec_n[0], w_dec_p[0], is_dram_) * NAND3_LEAK_STACK_FACTOR;
    }

    for (int i = 1; i < num_gates; ++i)
    {
      inv = Area::gatearea(INV, 1, w_dec_p[i], w_dec_n[i], 4 * cell.h); 
      cumulative_area  += inv.get_area();  
      cumulative_power += cmos_Ileak(w_dec_n[i], w_dec_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    power.readOp.leakage = cumulative_power * g_tp.peri_global.Vdd;
    area.set_area(cumulative_area);
  }
}


double Decoder::compute_delays(
    double inrisetime,
    const  Area & cell,
    bool   is_dram,
    bool   is_wl_tr)
{
  double ret_val = 0;  // outrisetime
  int    i;
  double rd, tf, this_delay, c_load, c_intrinsic, vdd_wordline;
  
  if((is_wl_tr)&&(is_dram))
  {
    vdd_wordline = g_tp.vpp;
  }
  else if(is_wl_tr)
  {
    vdd_wordline = g_tp.sram_cell.Vdd;
  }
  else
  {
    vdd_wordline = g_tp.peri_global.Vdd;
  }
  
  if (exist)
  { //First check whether a decoder is required at all

    rd = tr_R_on(w_dec_n[0], NCH, num_in_signals, is_dram, false, is_wl_tr);
    c_load = gate_C(w_dec_n[1] + w_dec_p[1], 0.0, is_dram, false, is_wl_tr);
    c_intrinsic = drain_C_(w_dec_p[0], PCH, 1, 1, 4 * cell.h, is_dram, false, is_wl_tr) * num_in_signals + 
                  drain_C_(w_dec_p[0], NCH, num_in_signals, 1, 4 * cell.h, is_dram, false, is_wl_tr);
    tf = rd * (c_intrinsic + c_load);
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    delay += this_delay;
    inrisetime = this_delay / (1.0 - 0.5);  
    power.readOp.dynamic += (c_load + c_intrinsic) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;

    for (i = 1; i < num_gates - 1; ++i)
    {
      rd = tr_R_on(w_dec_n[i], NCH, 1, is_dram, false, is_wl_tr);
      c_load = gate_C(w_dec_p[i+1] + w_dec_n[i+1], 0.0, is_dram, false, is_wl_tr);
      c_intrinsic = drain_C_(w_dec_p[i], PCH, 1, 1, 4 * cell.h, is_dram, false, is_wl_tr) + 
                    drain_C_(w_dec_n[i], NCH, 1, 1, 4 * cell.h, is_dram, false, is_wl_tr);
      tf = rd * (c_intrinsic + c_load);
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      delay += this_delay;
      inrisetime = this_delay / (1.0 - 0.5);
      power.readOp.dynamic += (c_load + c_intrinsic) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
    //Add delay of final inverter that drives the wordline 
    i = num_gates - 1;
    c_load = C_ld_dec_out;
    rd = tr_R_on(w_dec_n[i], NCH, 1, is_dram, false, is_wl_tr);
    c_intrinsic = drain_C_(w_dec_p[i], PCH, 1, 1, 4 * cell.h, is_dram, false, is_wl_tr) + 
                  drain_C_(w_dec_n[i], NCH, 1, 1, 4 * cell.h, is_dram, false, is_wl_tr);
    tf = rd * (c_intrinsic + c_load) + R_wire_dec_out * c_load / 2;
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    delay += this_delay;
    ret_val = this_delay / (1.0 - 0.5);
    power.readOp.dynamic += c_load * vdd_wordline * vdd_wordline +
                            c_intrinsic * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  }
  return ret_val;
}


PredecoderBlock::PredecoderBlock()
 :exist(false),
  number_input_addr_bits(0),
  c_load_predecoder_block_output(0),
  r_wire_predecoder_block_output(0),
  branch_effort_nand2_gate_output(0),
  branch_effort_nand3_gate_output(0),
  flag_two_unique_paths(0),
  flag_L2_gate(0),
  number_inputs_L1_gate(0),
  number_gates_L1_nand2_path(0),
  number_gates_L1_nand3_path(0),
  number_gates_L2(0),
  min_number_gates_L1(2),
  min_number_gates_L2(2),
  number_L1_parallel_instances_nand2(0),
  number_L1_parallel_instances_nand3(0),
  number_L2_parallel_instances(0),
  delay_nand2_path(0),
  delay_nand3_path(0),
  power_nand2_path(),
  power_nand3_path(),
  power_L2(),
  area()
{
}


void PredecoderBlock::initialize(
    int num_dec_signals,
    PredecoderBlock & blk1,
    PredecoderBlock & blk2,
    const Decoder & dec,
    double C_wire_predec_blk_out,
    double R_wire_predec_blk_out,
    int    num_dec_gates_driven_per_predec_out,
    bool   is_dram_)
{
  int    branch_effort_predec_blk1_out, branch_effort_predec_blk2_out;
  double C_ld_dec_gate;
  int    num_addr_bits_dec = _log2(num_dec_signals);
  blk1.is_dram_ = is_dram_;
  blk2.is_dram_ = is_dram_;

  switch (num_addr_bits_dec)
  {
    case 0:
      return;
    case 1:
    case 2:
    case 3:
      // Just one predecoder block is required with NAND2 gates. No decoder required.
      // The first level of predecoding directly drives the decoder output load
      blk1.exist = true;
      blk1.number_input_addr_bits = num_addr_bits_dec;
      blk2.number_input_addr_bits = 0;
      blk1.r_wire_predecoder_block_output = dec.R_wire_dec_out;
      blk2.r_wire_predecoder_block_output = 0;
      blk1.c_load_predecoder_block_output = dec.C_ld_dec_out;  
      blk2.c_load_predecoder_block_output = 0;  
      break;
    default:
      blk1.exist = true;
      blk2.exist = true;
      blk1.number_input_addr_bits   = (num_addr_bits_dec + 1) / 2;
      blk2.number_input_addr_bits   = num_addr_bits_dec - blk1.number_input_addr_bits;
      branch_effort_predec_blk1_out = (1 << blk2.number_input_addr_bits);
      branch_effort_predec_blk2_out = (1 << blk1.number_input_addr_bits);
      C_ld_dec_gate = num_dec_gates_driven_per_predec_out * 
                      gate_C(dec.w_dec_n[0] + dec.w_dec_p[0], 0, is_dram_, false, false);
      blk1.r_wire_predecoder_block_output = R_wire_predec_blk_out;
      blk2.r_wire_predecoder_block_output = R_wire_predec_blk_out;
      blk1.c_load_predecoder_block_output = branch_effort_predec_blk1_out * C_ld_dec_gate + C_wire_predec_blk_out;
      blk2.c_load_predecoder_block_output = branch_effort_predec_blk2_out * C_ld_dec_gate + C_wire_predec_blk_out;
      break;
  }
}


void PredecoderBlock::compute_widths()
{
  double F, c_load_nand3_path, c_load_nand2_path;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram_);
  double gnand2 = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnand3 = (3 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);

  if (exist == false) return;

  branch_effort_nand2_gate_output = 1;
  branch_effort_nand3_gate_output = 1;
  number_inputs_L1_gate  = 0;

  switch (number_input_addr_bits)
  {
    case 1:
      flag_two_unique_paths          = 0;
      number_inputs_L1_gate = 2;
      flag_L2_gate         = 0;
      break;
    case 2:
      flag_two_unique_paths          = 0;
      number_inputs_L1_gate = 2;
      flag_L2_gate         = 0;
      break;
    case 3:
      flag_two_unique_paths          = 0;
      number_inputs_L1_gate = 3;
      flag_L2_gate         = 0;    
      break;
    case 4:
      flag_two_unique_paths          = 0;
      flag_L2_gate         = 2;  
      number_inputs_L1_gate = 2;
      branch_effort_nand2_gate_output= 4;
      break;
    case 5:
      flag_two_unique_paths          = 1;
      flag_L2_gate         = 2;  
      branch_effort_nand2_gate_output= 8;
      branch_effort_nand3_gate_output= 4;
      break;
    case 6:
      flag_two_unique_paths          = 0;
      number_inputs_L1_gate = 3;
      flag_L2_gate         = 2;  
      branch_effort_nand3_gate_output= 8;
      break;
    case 7:
      flag_two_unique_paths          = 1;
      flag_L2_gate         = 3;  
      branch_effort_nand2_gate_output= 32;
      branch_effort_nand3_gate_output= 16;
      break;
    case 8:
      flag_two_unique_paths          = 1;
      flag_L2_gate         = 3;  
      branch_effort_nand2_gate_output= 64;
      branch_effort_nand3_gate_output= 32;
      break;
    case 9:
      flag_two_unique_paths          = 0;
      number_inputs_L1_gate = 3;
      flag_L2_gate         = 3;
      branch_effort_nand3_gate_output= 64;
      break;
  }

  // Find number of gates and sizing in second level of predecoder (if there is a second level)
  if (flag_L2_gate)
  {
    if (flag_L2_gate == 2)
    { // 2nd level is a NAND2 gate
      width_L2_n[0] = 2 * g_tp.min_w_nmos_;
      F = gnand2;
    }
    else
    { // 2nd level is a NAND3 gate
      width_L2_n[0] = 3 * g_tp.min_w_nmos_;
      F = gnand3;
    }
    width_L2_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
    F *= c_load_predecoder_block_output / 
         (gate_C(width_L2_n[0], 0, is_dram_) + gate_C(width_L2_p[0], 0, is_dram_));
    number_gates_L2 = logical_effort(
        min_number_gates_L2,
        flag_L2_gate == 2 ? gnand2 : gnand3,
        F,
        width_L2_n,
        width_L2_p,
        c_load_predecoder_block_output, 2,
        is_dram_, false);
    
    // Now find number of gates and widths in first level of predecoder
    if ((flag_two_unique_paths)||(number_inputs_L1_gate == 2))
    { // Whenever flag_two_unique_paths is TRUE, it means first level of decoder employs
      // both NAND2 and NAND3 gates. Or when number_inputs_L1_gate is 2, it means
      // a NAND2 gate is used in the first level of the predecoder
      c_load_nand2_path = branch_effort_nand2_gate_output * 
                          (gate_C(width_L2_n[0], 0, is_dram_) + 
                           gate_C(width_L2_p[0], 0, is_dram_));
      width_L1_nand2_path_n[0] = 2 * g_tp.min_w_nmos_;
      width_L1_nand2_path_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = gnand2 * c_load_nand2_path / 
          (gate_C(width_L1_nand2_path_n[0], 0, is_dram_) +
           gate_C(width_L1_nand2_path_p[0], 0, is_dram_));
      number_gates_L1_nand2_path = logical_effort(
          min_number_gates_L1,
          gnand2,
          F,
          width_L1_nand2_path_n,
          width_L1_nand2_path_p,
          c_load_nand2_path, 2,
          is_dram_, false);
    }
    
    //Now find widths of gates along path in which first gate is a NAND3
    if ((flag_two_unique_paths)||(number_inputs_L1_gate == 3))
    { // Whenever flag_two_unique_paths is TRUE, it means first level of decoder employs
      // both NAND2 and NAND3 gates. Or when number_inputs_L1_gate is 3, it means
      // a NAND3 gate is used in the first level of the predecoder
      c_load_nand3_path = branch_effort_nand3_gate_output * 
                    (gate_C(width_L2_n[0], 0, is_dram_) + 
                     gate_C(width_L2_p[0], 0, is_dram_));
      width_L1_nand3_path_n[0] = 3 * g_tp.min_w_nmos_;
      width_L1_nand3_path_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = gnand3 * c_load_nand3_path / 
          (gate_C(width_L1_nand3_path_n[0], 0, is_dram_) +
           gate_C(width_L1_nand3_path_p[0], 0, is_dram_));
      number_gates_L1_nand3_path = logical_effort(
          min_number_gates_L1,
          gnand3,
          F,
          width_L1_nand3_path_n,
          width_L1_nand3_path_p,
          c_load_nand3_path, 2,
          is_dram_, false);
    }  
  }
  else
  { //Find number of gates and widths in first level of predecoder block when there is no second level 
    if(number_inputs_L1_gate == 2)
    {
      width_L1_nand2_path_n[0] = 2 * g_tp.min_w_nmos_;
      width_L1_nand2_path_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = c_load_predecoder_block_output / 
          (gate_C(width_L1_nand2_path_n[0], 0, is_dram_) +
           gate_C(width_L1_nand2_path_p[0], 0, is_dram_));
      number_gates_L1_nand2_path = logical_effort(
          min_number_gates_L1,
          gnand2,
          F,
          width_L1_nand2_path_n,
          width_L1_nand2_path_p,
          c_load_predecoder_block_output, 2,
          is_dram_, false);
    }
    else if (number_inputs_L1_gate == 3)
    {
      width_L1_nand3_path_n[0] = 3 * g_tp.min_w_nmos_;
      width_L1_nand3_path_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
      F = c_load_predecoder_block_output / 
          (gate_C(width_L1_nand3_path_n[0], 0, is_dram_) +
           gate_C(width_L1_nand3_path_p[0], 0, is_dram_));
      number_gates_L1_nand3_path = logical_effort(
          min_number_gates_L1,
          gnand3,
          F,
          width_L1_nand3_path_n,
          width_L1_nand3_path_p,
          c_load_predecoder_block_output, 2,
          is_dram_, false);
    }
  }
}


void PredecoderBlock::compute_area()
{
  int    i;
  double cumulative_area_L1_nand2_path, cumulative_area_L1_nand3_path, 
         cumulative_area_L1, cumulative_area_L2;
  double leakage_L1_nand2_path, leakage_L1_nand3_path, leakage_L2;
  Area   nand2, nand3, inv;

  number_L1_parallel_instances_nand2 = 0;
  number_L1_parallel_instances_nand3 = 0;
  number_L2_parallel_instances = 0;
  cumulative_area_L1_nand2_path = 0;
  cumulative_area_L1_nand3_path = 0;
  cumulative_area_L2 = 0;
  leakage_L1_nand2_path = 0;
  leakage_L1_nand3_path = 0;
  leakage_L2 = 0;

  if(exist)
  { //First check whether a predecoder block is needed
    nand2 = Area::gatearea(NAND, 2, width_L1_nand2_path_p[0], 
                           width_L1_nand2_path_n[0], g_tp.cell_h_def); 
    nand3 = Area::gatearea(NAND, 3, width_L1_nand3_path_p[0], 
                           width_L1_nand3_path_n[0], g_tp.cell_h_def); 
    cumulative_area_L1_nand2_path += nand2.get_area();
    cumulative_area_L1_nand3_path += nand3.get_area();
    leakage_L1_nand2_path += cmos_Ileak(width_L1_nand2_path_n[0],
        width_L1_nand2_path_p[0], is_dram_) * NAND2_LEAK_STACK_FACTOR;
    leakage_L1_nand3_path += cmos_Ileak(width_L1_nand3_path_n[0],
        width_L1_nand3_path_p[0], is_dram_) * NAND3_LEAK_STACK_FACTOR;

    if (number_input_addr_bits == 1)
    {//2 NAND2 gates
      number_L1_parallel_instances_nand2 = 2;
      number_L2_parallel_instances = 0;
    }
    else if(number_input_addr_bits == 2)
    {//4 NAND2 gates
      number_L1_parallel_instances_nand2 = 4;
      number_L2_parallel_instances = 0;
    }
    else if(number_input_addr_bits == 3)
    {//8 NAND3 gates
      number_L1_parallel_instances_nand3 = 8;
      number_L2_parallel_instances = 0;
    }
    else if(number_input_addr_bits == 4)
    {//4 + 4 NAND2 gates
      number_L1_parallel_instances_nand2 = 8;
      number_L2_parallel_instances = 16;
    }
    else if (number_input_addr_bits == 5)
    {//4 NAND2 gates, 8 NAND3 gates
      number_L1_parallel_instances_nand2 = 4;
      number_L1_parallel_instances_nand3 = 8;
        number_L2_parallel_instances = 32;
    }
    else if(number_input_addr_bits == 6)
    {//8 + 8 NAND3 gates
      number_L1_parallel_instances_nand3 = 16;
      number_L2_parallel_instances = 64;
    }
    else if(number_input_addr_bits == 7)
    {//4 + 4 NAND2 gates, 8 NAND3 gates
      number_L1_parallel_instances_nand2 = 8;
      number_L1_parallel_instances_nand3 = 8;
      number_L2_parallel_instances = 128;
    }
    else if(number_input_addr_bits == 8)
    {//4 NAND2 gates, 8 + 8 NAND3 gates
      number_L1_parallel_instances_nand2 = 4;
      number_L1_parallel_instances_nand3 = 16;
      number_L2_parallel_instances = 256;
    }
    else if(number_input_addr_bits == 9)
    {//8 + 8 + 8 NAND3 gates
      number_L1_parallel_instances_nand3 = 24;
      number_L2_parallel_instances = 512;
    }

    for(i = 1; i < number_gates_L1_nand2_path; ++i)
    {
      inv = Area::gatearea(INV, 1, width_L1_nand2_path_p[i], 
                           width_L1_nand2_path_n[i], g_tp.cell_h_def); 
      cumulative_area_L1_nand2_path += inv.get_area();  
      leakage_L1_nand2_path += 
        cmos_Ileak(width_L1_nand2_path_n[i], width_L1_nand2_path_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    cumulative_area_L1_nand2_path *= number_L1_parallel_instances_nand2;
    leakage_L1_nand2_path *= number_L1_parallel_instances_nand2;

    for(i = 1; i < number_gates_L1_nand3_path; ++i)
    {
      inv = Area::gatearea(INV, 1, width_L1_nand3_path_p[i], 
                           width_L1_nand3_path_n[i], g_tp.cell_h_def); 
      cumulative_area_L1_nand3_path += inv.get_area();  
      leakage_L1_nand3_path += 
        cmos_Ileak(width_L1_nand3_path_n[i], width_L1_nand3_path_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    cumulative_area_L1_nand3_path *= number_L1_parallel_instances_nand3;
    leakage_L1_nand3_path *= number_L1_parallel_instances_nand3;
    cumulative_area_L1 = cumulative_area_L1_nand2_path + cumulative_area_L1_nand3_path;
    
    cumulative_area_L2 = 0;
    if (flag_L2_gate)
    {
      if (flag_L2_gate == 2)
      {
        nand2 = Area::gatearea(NAND, 2, width_L2_p[0], width_L2_n[0], g_tp.cell_h_def); 
        cumulative_area_L2 += nand2.get_area();
        leakage_L2 += cmos_Ileak(width_L2_n[0], width_L2_p[0], is_dram_) * NAND2_LEAK_STACK_FACTOR;
      }
      else if (flag_L2_gate == 3)
      {
        nand3 = Area::gatearea(NAND, 3, width_L2_p[0], width_L2_n[0], g_tp.cell_h_def); 
        cumulative_area_L2 += nand3.get_area();
        leakage_L2 += cmos_Ileak(width_L2_n[0], width_L2_p[0], is_dram_) * NAND3_LEAK_STACK_FACTOR;
      }
    }
    for(i = 1; i < number_gates_L2; ++i)
    {
      inv = Area::gatearea(INV, 1, width_L2_p[i], width_L2_n[i], g_tp.cell_h_def); 
      cumulative_area_L2 += inv.get_area();  
      leakage_L2 += cmos_Ileak(width_L2_n[i], width_L2_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    cumulative_area_L2 *= number_L2_parallel_instances;
    leakage_L2         *= number_L2_parallel_instances;

    power_nand2_path.readOp.leakage   = leakage_L1_nand2_path * g_tp.peri_global.Vdd;
    power_nand3_path.readOp.leakage   = leakage_L1_nand3_path * g_tp.peri_global.Vdd;
    power_L2.readOp.leakage = leakage_L2 * g_tp.peri_global.Vdd; 
    area.set_area(cumulative_area_L1 + cumulative_area_L2);
  }
}


pair<double, double> PredecoderBlock::compute_delays(
    pair<double, double> inrisetime)  // <nand2, nand3>
{
  pair<double, double> ret_val;
  ret_val.first  = 0;  // outrisetime_nand2_path 
  ret_val.second = 0;  // outrisetime_nand3_path

  double inrisetime_nand2_path = inrisetime.first;
  double inrisetime_nand3_path = inrisetime.second;
  int    i;
  double rd, c_load, c_intrinsic, tf, this_delay;
  //First check whether a predecoder block is required
  if (exist)
  {
    //Find delay in first level of predecoder block
    //First find delay in path 
    if ((flag_two_unique_paths) || (number_inputs_L1_gate == 2))
    {
      //First gate is a NAND2 gate
      rd = tr_R_on(width_L1_nand2_path_n[0], NCH, 2, is_dram_);
      c_load = gate_C(width_L1_nand2_path_n[1] + width_L1_nand2_path_p[1], 0.0, is_dram_);
      c_intrinsic = 2 * drain_C_(width_L1_nand2_path_p[0], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_L1_nand2_path_n[0], NCH, 2, 1, g_tp.cell_h_def, is_dram_);
      tf = rd * (c_intrinsic + c_load);
      this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
      delay_nand2_path += this_delay;
      inrisetime_nand2_path = this_delay / (1.0 - 0.5);
      power_nand2_path.readOp.dynamic += (c_load + c_intrinsic) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      
      //Add delays of all but the last inverter in the chain
      for (i = 1; i < number_gates_L1_nand2_path - 1; ++i)
      {
        rd = tr_R_on(width_L1_nand2_path_n[i], NCH, 1, is_dram_);
        c_load = gate_C(width_L1_nand2_path_n[i+1] +
                        width_L1_nand2_path_p[i+1], 0.0, is_dram_);
        c_intrinsic = drain_C_(width_L1_nand2_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand2_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
        delay_nand2_path += this_delay;
        inrisetime_nand2_path = this_delay / (1.0 - 0.5);
        power_nand2_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      
      //Add delay of the last inverter
      i = number_gates_L1_nand2_path - 1;
      rd = tr_R_on(width_L1_nand2_path_n[i], NCH, 1, is_dram_);
      if (flag_L2_gate) {
        c_load = branch_effort_nand2_gate_output*(gate_C(width_L2_n[0], 0, is_dram_) + 
                 gate_C(width_L2_p[0], 0, is_dram_));
        c_intrinsic = drain_C_(width_L1_nand2_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand2_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
        delay_nand2_path += this_delay;
        inrisetime_nand2_path = this_delay / (1.0 - 0.5);
        power_nand2_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      else
      { //First level directly drives decoder output load
        c_load = c_load_predecoder_block_output;
        c_intrinsic = drain_C_(width_L1_nand2_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand2_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load) + r_wire_predecoder_block_output * c_load / 2; 
        this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
        delay_nand2_path += this_delay;
        ret_val.first = this_delay / (1.0 - 0.5);    
        power_nand2_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
    }
    
    if ((flag_two_unique_paths)||(number_inputs_L1_gate == 3))
    { //Check if the number of gates in the first level is more than 1. 
      //First gate is a NAND3 gate
      rd = tr_R_on(width_L1_nand3_path_n[0], NCH, 3, is_dram_);
      c_load = gate_C(width_L1_nand3_path_n[1] + width_L1_nand3_path_p[1], 0.0, is_dram_);
      c_intrinsic = 3 * drain_C_(width_L1_nand3_path_p[0], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_L1_nand3_path_n[0], NCH, 3, 1, g_tp.cell_h_def, is_dram_);
      tf = rd * (c_intrinsic + c_load);
      this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
      delay_nand3_path += this_delay;
      inrisetime_nand3_path = this_delay / (1.0 - 0.5);
      power_nand3_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;

      //Add delays of all but the last inverter in the chain
      for (i = 1; i < number_gates_L1_nand3_path - 1; ++i)
      {
        rd = tr_R_on(width_L1_nand3_path_n[i], NCH, 1, is_dram_);
        c_load = gate_C(width_L1_nand3_path_n[i+1] + width_L1_nand3_path_p[i+1], 0.0, is_dram_);
        c_intrinsic = drain_C_(width_L1_nand3_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand3_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
        delay_nand3_path += this_delay;
        inrisetime_nand3_path = this_delay / (1.0 - 0.5);
        power_nand3_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      
      //Add delay of the last inverter
      i = number_gates_L1_nand3_path - 1;
      rd = tr_R_on(width_L1_nand3_path_n[i], NCH, 1, is_dram_);
      if (flag_L2_gate) 
      {
        c_load = branch_effort_nand3_gate_output * (gate_C(width_L2_n[0], 0, is_dram_) + 
                                                    gate_C(width_L2_p[0], 0, is_dram_));
        c_intrinsic = drain_C_(width_L1_nand3_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand3_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
        delay_nand3_path += this_delay;
        inrisetime_nand3_path = this_delay / (1.0 - 0.5);
        power_nand3_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      else
      { //First level directly drives decoder output load
        c_load = c_load_predecoder_block_output;
        c_intrinsic = drain_C_(width_L1_nand3_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                      drain_C_(width_L1_nand3_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load) + r_wire_predecoder_block_output * c_load / 2; 
        this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
        delay_nand3_path += this_delay;
        ret_val.second = this_delay / (1.0 - 0.5);  
        power_nand3_path.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
    }  

    // Find delay through second level 
    if (flag_L2_gate)
    {
      if (flag_L2_gate == 2)
      {
        rd = tr_R_on(width_L2_n[0], NCH, 2, is_dram_);
        c_load = gate_C(width_L2_n[1] + width_L2_p[1], 0.0, is_dram_);
        c_intrinsic = 2 * drain_C_(width_L2_p[0], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                          drain_C_(width_L2_n[0], NCH, 2, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
        delay_nand2_path += this_delay;
        inrisetime_nand2_path = this_delay / (1.0 - 0.5);
        power_L2.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      else
      { //flag_L2_gate = 3)
        rd = tr_R_on(width_L2_n[0], NCH, 3, is_dram_);
        c_load = gate_C(width_L2_n[1] + width_L2_p[1], 0.0, is_dram_);
        c_intrinsic = 3 * drain_C_(width_L2_p[0], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                          drain_C_(width_L2_n[0], NCH, 3, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
        delay_nand3_path += this_delay;
        inrisetime_nand3_path = this_delay / (1.0 - 0.5);
        power_L2.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      
      for (i = 1; i < number_gates_L2 - 1; ++i)
      {
        rd = tr_R_on(width_L2_n[i], NCH, 1, is_dram_);
        c_load = gate_C(width_L2_n[i+1] +
                        width_L2_p[i+1], 0.0, is_dram_);
        c_intrinsic = drain_C_(width_L2_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) +
                      drain_C_(width_L2_p[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
        tf = rd * (c_intrinsic + c_load);
        this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
        delay_nand2_path += this_delay;
        inrisetime_nand2_path = this_delay / (1.0 - 0.5);
        this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
        delay_nand3_path += this_delay;
        inrisetime_nand3_path = this_delay / (1.0 - 0.5);
        power_L2.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      //Add delay of final inverter that drives the wordline decoders
      i = number_gates_L2 - 1;
      c_load = c_load_predecoder_block_output;
      rd = tr_R_on(width_L2_n[i], NCH, 1, is_dram_);
      c_intrinsic = drain_C_(width_L2_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_L2_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
      tf = rd * (c_intrinsic + c_load) + r_wire_predecoder_block_output * c_load / 2;
      this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
      delay_nand2_path += this_delay;
      ret_val.first = this_delay / (1.0 - 0.5);  
      this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
      delay_nand3_path += this_delay;
      ret_val.second = this_delay / (1.0 - 0.5);  
      power_L2.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
  }
  return ret_val;
}


PredecoderBlockDriver::PredecoderBlockDriver()
 :flag_driver_exists(0),
  flag_driving_decoder_output(0),
  number_input_addr_bits(0),
  number_gates_nand2_path(0),
  number_gates_nand3_path(0),
  min_number_gates(2),
  number_parallel_instances_driving_1_nand2_load(0),
  number_parallel_instances_driving_2_nand2_load(0),
  number_parallel_instances_driving_4_nand2_load(0),
  number_parallel_instances_driving_2_nand3_load(0),
  number_parallel_instances_driving_8_nand3_load(0),
  number_parallel_instances_nand3_path(0),
  c_load_nand2_path_predecode_block_driver_output(0),
  c_load_nand3_path_predecode_block_driver_output(0),
  r_load_nand2_path_predecode_block_driver_output(0),
  r_load_nand3_path_predecode_block_driver_output(0),
  delay_nand2_path(0),
  delay_nand3_path(0),
  power_nand2_path(),
  power_nand3_path(),
  area()
{
  for (int i = 0; i < MAX_NUMBER_GATES_STAGE; i++)
  {
    width_nand2_path_n[i] = 0;
    width_nand2_path_p[i] = 0;
    width_nand3_path_n[i] = 0;
    width_nand3_path_p[i] = 0;
  }
}


void PredecoderBlockDriver::compute_widths(
    const PredecoderBlock & predec_blk,
    const Decoder &ptr_dec,
    int way_select)
{
  // The predecode block driver accepts as input the address bits from the h-tree network. For 
  // each addr bit it then generates addr and addrbar as outputs. For now ignore the effect of
  // inversion to generate addrbar and simply treat addrbar as addr.

  double F;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram_);

  if(flag_driver_exists)
  {  
    if(flag_driving_decoder_output)
    {
      number_parallel_instances_driving_1_nand2_load = 1;
      number_parallel_instances_driving_2_nand2_load = 0;
      number_parallel_instances_driving_4_nand2_load = 0;
      number_parallel_instances_driving_2_nand3_load = 0;
      number_parallel_instances_driving_8_nand3_load = 0;
      width_nand2_path_n[0] = g_tp.min_w_nmos_;
      width_nand2_path_p[0] = pmos_to_nmos_sizing_r * width_nand2_path_n[0];
      F = c_load_nand2_path_predecode_block_driver_output / 
          gate_C(width_nand2_path_n[0] + width_nand2_path_p[0], 0, is_dram_);
      number_gates_nand2_path = logical_effort(
          min_number_gates,
          0,
          F,
          width_nand2_path_n,
          width_nand2_path_p,
          c_load_nand2_path_predecode_block_driver_output, 2,
          is_dram_, false, true);
    }
    else
    {
      if(way_select == 0)
      {
        if (predec_blk.number_input_addr_bits == 1)
        { //2 NAND2 gates
          number_parallel_instances_driving_2_nand2_load =  1;
          c_load_nand2_path_predecode_block_driver_output = 
            2 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
        }
        if (predec_blk.number_input_addr_bits == 2)
        { //4 NAND2 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 2;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 0;
          c_load_nand2_path_predecode_block_driver_output = 
            4 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 3){//8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 0;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 3;
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 4){//4 + 4 NAND2 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 4;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 0;
          c_load_nand2_path_predecode_block_driver_output = 
            4 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 5){//4 NAND2 gates, 8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 2;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 3;
          c_load_nand2_path_predecode_block_driver_output =
            4 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 6){//8 + 8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 0;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 6;
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 7){//4 + 4 NAND2 gates, 8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 4;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 3;
          c_load_nand2_path_predecode_block_driver_output =
            4 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 8){//4 NAND2 gates, 8 + 8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 2;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 6;
          c_load_nand2_path_predecode_block_driver_output =
            4 * gate_C(predec_blk.width_L1_nand2_path_n[0] +
            predec_blk.width_L1_nand2_path_p[0], 0, is_dram_);
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
        if(predec_blk.number_input_addr_bits == 9){//8 + 8 + 8 NAND3 gates
          number_parallel_instances_driving_1_nand2_load = 0;
          number_parallel_instances_driving_2_nand2_load = 0;
          number_parallel_instances_driving_4_nand2_load = 0;
          number_parallel_instances_driving_2_nand3_load = 0;
          number_parallel_instances_driving_8_nand3_load = 9;
          c_load_nand3_path_predecode_block_driver_output = 
            8 * gate_C(predec_blk.width_L1_nand3_path_n[0] +
            predec_blk.width_L1_nand3_path_p[0], 0, is_dram_);
        }
      }

      if ((predec_blk.flag_two_unique_paths) || 
          (predec_blk.number_inputs_L1_gate == 2) ||
          (number_input_addr_bits == 0) ||
          ((way_select)&&(ptr_dec.num_in_signals == 2)))
      { //this means that way_select is driving NAND2 in decoder. 
        width_nand2_path_n[0] = g_tp.min_w_nmos_;
        width_nand2_path_p[0] = pmos_to_nmos_sizing_r * width_nand2_path_n[0];
        F = c_load_nand2_path_predecode_block_driver_output / 
          gate_C(width_nand2_path_n[0] + width_nand2_path_p[0], 0, is_dram_);
        number_gates_nand2_path = logical_effort(
            min_number_gates,
            1,
            F,
            width_nand2_path_n,
            width_nand2_path_p,
            c_load_nand2_path_predecode_block_driver_output, 2,
            is_dram_, false, true);
      }
      if ((predec_blk.flag_two_unique_paths) ||
          (predec_blk.number_inputs_L1_gate == 3) ||
          ((way_select)&&(ptr_dec.num_in_signals == 3)))
      { //this means that way_select is driving NAND3 in decoder. 
        width_nand3_path_n[0] = g_tp.min_w_nmos_;
        width_nand3_path_p[0] = pmos_to_nmos_sizing_r * width_nand3_path_n[0];
        F = c_load_nand3_path_predecode_block_driver_output / 
          gate_C(width_nand3_path_n[0] + width_nand3_path_p[0], 0, is_dram_);
        number_gates_nand3_path = logical_effort(
            min_number_gates,
            1,
            F,
            width_nand3_path_n,
            width_nand3_path_p,
            c_load_nand3_path_predecode_block_driver_output, 2,
            is_dram_, false, true);
      }
    }
  }
}


void PredecoderBlockDriver::initialize(
    int num_dec_signals,
    int flag_way_select,
    int way_select,
    PredecoderBlockDriver & blk_drv1,
    PredecoderBlockDriver & blk_drv2,
    const PredecoderBlock & blk1,
    const PredecoderBlock & blk2,
    const Decoder & dec,
    bool is_dram_)
{
  blk_drv1.number_input_addr_bits = blk1.number_input_addr_bits;
  blk_drv2.number_input_addr_bits = blk2.number_input_addr_bits;
  blk_drv1.is_dram_ = is_dram_;
  blk_drv2.is_dram_ = is_dram_;

  if (flag_way_select && (way_select > 1))
  {
    blk_drv1.flag_driver_exists = 1;
    blk_drv1.number_input_addr_bits = way_select; 
    if (dec.num_in_signals == 2)
    {
      blk_drv1.c_load_nand2_path_predecode_block_driver_output = 
        gate_C(dec.w_dec_n[0] + dec.w_dec_p[0], 0, is_dram_);
      blk_drv1.number_parallel_instances_driving_2_nand2_load = blk_drv1.number_input_addr_bits;
    }
    else if(dec.num_in_signals == 3)
    {
      blk_drv1.c_load_nand3_path_predecode_block_driver_output = 
        gate_C(dec.w_dec_n[0] + dec.w_dec_p[0], 0, is_dram_);
      blk_drv1.number_parallel_instances_driving_2_nand3_load = blk_drv1.number_input_addr_bits;
    }
    blk_drv1.r_load_nand2_path_predecode_block_driver_output = 0;
    blk_drv1.c_load_nand3_path_predecode_block_driver_output = 0;
    blk_drv1.r_load_nand3_path_predecode_block_driver_output = 0;
    blk_drv2.c_load_nand2_path_predecode_block_driver_output = 0;
    blk_drv2.r_load_nand2_path_predecode_block_driver_output = 0;
    blk_drv2.c_load_nand3_path_predecode_block_driver_output = 0;
    blk_drv2.r_load_nand3_path_predecode_block_driver_output = 0;
  }
  if(!flag_way_select)
  {
    /*if(number_addr_bits_decode == 0){//means that there is no decoding required, however the 
      //load still needs to be driven by a chain of inverters. Use only one predecode block
      //driver
      blk_drv1.flag_driver_exists = 1;
      blk_drv1.flag_driving_decoder_output = 1;
      blk_drv1.c_load_nand2_path_predecode_block_driver_output = 
        dec->c_load_decoder_output;
      blk_drv1.r_load_nand2_path_predecode_block_driver_output =
        dec->r_wire_decoder_output;
      blk_drv1.c_load_nand3_path_predecode_block_driver_output = 0;
      blk_drv1.r_load_nand3_path_predecode_block_driver_output = 0;
      blk_drv2.c_load_nand2_path_predecode_block_driver_output = 0;
      blk_drv2.r_load_nand2_path_predecode_block_driver_output = 0;
      blk_drv2.c_load_nand3_path_predecode_block_driver_output = 0;
      blk_drv2.r_load_nand3_path_predecode_block_driver_output = 0;
    }
    else{*/
      if(blk1.exist){
        blk_drv1.flag_driver_exists = 1;
      }
      if(blk2.exist){
        blk_drv2.flag_driver_exists = 1;
      }
    //}
  }
}


//v5.0: Making the area model sensitive to the the widths that have been computed by the 
//delay model. 
void PredecoderBlockDriver::compute_area()
{
  Area inv;
  double cumulative_area_nand2_path, cumulative_area_nand3_path;
  double leakage_nand2_path, leakage_nand3_path;

  cumulative_area_nand2_path = 0;
  cumulative_area_nand3_path = 0;
  leakage_nand2_path = 0;
  leakage_nand3_path = 0;
      
  if (flag_driver_exists)
  { // first check whether a predecoder block driver is needed
    for(int i = 0; i < number_gates_nand2_path; ++i)
    {
      inv = Area::gatearea(INV, 1, width_nand2_path_p[i], width_nand2_path_n[i], g_tp.cell_h_def); 
      cumulative_area_nand2_path += inv.get_area();  
      leakage_nand2_path += cmos_Ileak(width_nand2_path_n[i], width_nand2_path_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    cumulative_area_nand2_path *= (number_parallel_instances_driving_1_nand2_load +
                                   number_parallel_instances_driving_2_nand2_load +
                                   number_parallel_instances_driving_4_nand2_load);
    leakage_nand2_path *= (number_parallel_instances_driving_1_nand2_load +
                           number_parallel_instances_driving_2_nand2_load +
                           number_parallel_instances_driving_4_nand2_load);

    for (int i = 0; i < number_gates_nand3_path; ++i)
    {
      inv = Area::gatearea(INV, 1, width_nand3_path_p[i], width_nand3_path_n[i], g_tp.cell_h_def); 
      cumulative_area_nand3_path += inv.get_area();  
      leakage_nand3_path += cmos_Ileak(width_nand3_path_n[i], width_nand3_path_p[i], is_dram_) * INV_LEAK_STACK_FACTOR;
    }
    cumulative_area_nand3_path *= (number_parallel_instances_driving_2_nand3_load +
                                   number_parallel_instances_driving_8_nand3_load);
    leakage_nand3_path *= (number_parallel_instances_driving_2_nand3_load +
                           number_parallel_instances_driving_8_nand3_load);
    power_nand2_path.readOp.leakage = leakage_nand2_path * g_tp.peri_global.Vdd;
    power_nand3_path.readOp.leakage = leakage_nand3_path * g_tp.peri_global.Vdd;
    area.set_area(cumulative_area_nand2_path + cumulative_area_nand3_path);
  }    
}


pair<double, double> PredecoderBlockDriver::compute_delays(
    double inrisetime_nand2_path,
    double inrisetime_nand3_path)
{
  pair<double, double> ret_val;
  ret_val.first  = 0;  // outrisetime_nand2_path 
  ret_val.second = 0;  // outrisetime_nand3_path
  int i;
  double rd, c_gate_load, c_load, c_intrinsic, tf, this_delay;
  
  if (flag_driver_exists)
  {
    for (i = 0; i < number_gates_nand2_path - 1; ++i)
    {
      rd = tr_R_on(width_nand2_path_n[i], NCH, 1, is_dram_);
      c_gate_load = gate_C(width_nand2_path_p[i+1] + width_nand2_path_n[i+1], 0.0, is_dram_);
      c_intrinsic = drain_C_(width_nand2_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) +
                    drain_C_(width_nand2_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
      tf = rd * (c_intrinsic + c_gate_load);
      this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
      delay_nand2_path += this_delay;
      inrisetime_nand2_path = this_delay / (1.0 - 0.5);
      power_nand2_path.readOp.dynamic += (c_gate_load + c_intrinsic) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
    
    // Final inverter drives the predecoder block or the decoder output load 
    if (number_gates_nand2_path != 0)
    {
      i = number_gates_nand2_path - 1;
      rd = tr_R_on(width_nand2_path_n[i], NCH, 1, is_dram_);
      c_intrinsic = drain_C_(width_nand2_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_nand2_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
      c_load = c_load_nand2_path_predecode_block_driver_output;
      tf = rd * (c_intrinsic + c_load) + r_load_nand2_path_predecode_block_driver_output*c_load/ 2;
      this_delay = horowitz(inrisetime_nand2_path, tf, 0.5, 0.5, RISE);
      delay_nand2_path += this_delay;
      ret_val.first = this_delay / (1.0 - 0.5);  
      power_nand2_path.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
  
    for (i = 0; i < number_gates_nand3_path - 1; ++i)
    {
      rd = tr_R_on(width_nand3_path_n[i], NCH, 1, is_dram_);
      c_gate_load = gate_C(width_nand3_path_p[i+1] + width_nand3_path_n[i+1], 0.0, is_dram_);
      c_intrinsic = drain_C_(width_nand3_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_nand3_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
      tf = rd * (c_intrinsic + c_gate_load);
      this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
      delay_nand3_path += this_delay;
      inrisetime_nand3_path = this_delay / (1.0 - 0.5);
      power_nand3_path.readOp.dynamic += (c_gate_load + c_intrinsic) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }

    // Final inverter drives the predecoder block or the decoder output load 
    if (number_gates_nand3_path != 0)
    {
      i = number_gates_nand3_path - 1;
      rd = tr_R_on(width_nand3_path_n[i], NCH, 1, is_dram_);
      c_intrinsic = drain_C_(width_nand3_path_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) + 
                    drain_C_(width_nand3_path_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
      c_load = c_load_nand3_path_predecode_block_driver_output;
      tf = rd*(c_intrinsic + c_load) + r_load_nand3_path_predecode_block_driver_output*c_load / 2;
      this_delay = horowitz(inrisetime_nand3_path, tf, 0.5, 0.5, RISE);
      delay_nand3_path += this_delay;
      ret_val.second = this_delay / (1.0 - 0.5);  
      power_nand3_path.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
  }
  return ret_val;
}


double PredecoderBlockDriver::get_readOp_dynamic_power(int num_act_mats_hor_dir)
{
  return (num_addr_bits_nand2_path()*power_nand2_path.readOp.dynamic +
          num_addr_bits_nand3_path()*power_nand3_path.readOp.dynamic) * num_act_mats_hor_dir;
}


Driver::Driver()
 :number_gates(0),
  min_number_gates(2),
  c_gate_load(0),
  c_wire_load(0),
  r_wire_load(0),
  delay(0),
  power()
{
  for (int i = 0; i < MAX_NUMBER_GATES_STAGE; i++)
  {
    width_n[i] = 0;
    width_p[i] = 0;
  }
}


void Driver::compute_widths(bool is_dram_)
{
  double c_load, F;

  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram_);
  c_load = c_gate_load + c_wire_load;
  width_n[0] = g_tp.min_w_nmos_;
  width_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
  F = c_load / gate_C(width_n[0] + width_p[0], 0, is_dram_);
  number_gates = logical_effort(
      min_number_gates,
      1,
      F,
      width_n,
      width_p,
      c_load,
      2,
      is_dram_, false);
}


double Driver::compute_delay(
    double inrisetime,
    bool is_dram_)
{
  int    i;
  double rd, c_load, c_intrinsic, tf;
  double this_delay = 0;

  for (i = 0; i < number_gates - 1; ++i)
  {
    rd = tr_R_on(width_n[i], NCH, 1, is_dram_);
    c_load = gate_C(width_n[i+1] + width_p[i+1], 0.0, is_dram_);
    c_intrinsic = drain_C_(width_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) +  
                  drain_C_(width_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
    tf = rd * (c_intrinsic + c_load);
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    delay += this_delay;
    inrisetime = this_delay / (1.0 - 0.5);
    power.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    power.readOp.leakage += cmos_Ileak(width_n[i], width_p[i], is_dram_) *
                            0.5 * g_tp.peri_global.Vdd;
  }

  i = number_gates - 1;
  c_load = c_gate_load + c_wire_load;
  rd = tr_R_on(width_n[i], NCH, 1, is_dram_);
  c_intrinsic = drain_C_(width_p[i], PCH, 1, 1, g_tp.cell_h_def, is_dram_) +  
                drain_C_(width_n[i], NCH, 1, 1, g_tp.cell_h_def, is_dram_);
  tf = rd * (c_intrinsic + c_load) + r_wire_load * (c_wire_load / 2 + c_gate_load);
  this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
  delay += this_delay;
  power.readOp.dynamic += (c_intrinsic + c_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  power.readOp.leakage += cmos_Ileak(width_n[i], width_p[i], is_dram_) *
                          0.5 * g_tp.peri_global.Vdd;

  return this_delay / (1.0 - 0.5);
}

