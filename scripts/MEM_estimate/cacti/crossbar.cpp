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

#include "crossbar.h"
#include "parameter.h"

using namespace std;


Crossbar::Crossbar(
    int num_in_ports_,
    int num_out_ports_,
    int num_signals_per_port_,
    double c_output_line_load_,
    bool is_dram_)
 :num_in_ports(num_in_ports_),
  num_out_ports(num_out_ports_),
  num_signals_per_port(num_signals_per_port_),
  min_number_gates(2),
  number_gates_output_line_tristate_buffer(0),
  crossbar_input_line_driver(),
  width_output_line_tristate_buffer_n(MAX_NUMBER_GATES_STAGE, 0),
  width_output_line_tristate_buffer_p(MAX_NUMBER_GATES_STAGE, 0),
  c_output_line_load(c_output_line_load_),
  delay(0),
  is_dram(is_dram_), area()
{
  double nand2_gate_cap, input_gate_cap;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double gnand2                = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnor2                 = (1 + 2*pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnmos                 = 1 / (1 + pmos_to_nmos_sizing_r);
  double gpmos                 = pmos_to_nmos_sizing_r / (1 + pmos_to_nmos_sizing_r);
  
  width_output_line_tristate_buffer_n[0] = 2 * g_tp.min_w_nmos_;
  width_output_line_tristate_buffer_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
  nand2_gate_cap = gate_C(width_output_line_tristate_buffer_n[0] + width_output_line_tristate_buffer_p[0], 0, is_dram);
  input_gate_cap = gnor2 * gnmos * nand2_gate_cap / (gnand2 * gpmos);
  width_output_line_tristate_buffer_nor2_n = (1 / (1 + 2 * pmos_to_nmos_sizing_r)) * input_gate_cap / gate_C(1, 0, is_dram);
  
  if (width_output_line_tristate_buffer_nor2_n < g_tp.min_w_nmos_)
  {
    width_output_line_tristate_buffer_nor2_n = g_tp.min_w_nmos_;
  }
  width_output_line_tristate_buffer_nor2_p = 2 * pmos_to_nmos_sizing_r * width_output_line_tristate_buffer_nor2_n;
}


void Crossbar::compute_widths()
{
  // compute widths of tristate buffer driving output line
  double G, B, H, F, f, c_output_line_wire_load, c_load, c_input;
  int j;
  
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double gnand2                = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gpmos                 = pmos_to_nmos_sizing_r / (1 + pmos_to_nmos_sizing_r);
  B = 1;
  G = gnand2 * gpmos;
  c_output_line_wire_load = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_in_ports * g_tp.wire_outside_mat.C_per_um;
  c_load = c_output_line_load + c_output_line_wire_load;
  H = c_load / gate_C(width_output_line_tristate_buffer_n[0] + width_output_line_tristate_buffer_p[0] , 0, is_dram);
  F = G * B * H;
  number_gates_output_line_tristate_buffer = (int) (log(F) / log(fopt));
  if (number_gates_output_line_tristate_buffer%2 != 0)
  {
    ++number_gates_output_line_tristate_buffer;
  }
  if (number_gates_output_line_tristate_buffer < min_number_gates)
  {
    number_gates_output_line_tristate_buffer = min_number_gates;
  }
  f = pow(F, 1.0 / number_gates_output_line_tristate_buffer);
  j = number_gates_output_line_tristate_buffer - 1;
  width_output_line_tristate_buffer_p[j] = gpmos * c_load / (f * gate_C(1, 0, is_dram));
  if (width_output_line_tristate_buffer_p[j] < pmos_to_nmos_sizing_r * g_tp.min_w_nmos_)
  {
    width_output_line_tristate_buffer_p[j] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;
  }
  width_output_line_tristate_buffer_n[j] = width_output_line_tristate_buffer_p[j] / pmos_to_nmos_sizing_r;
  
  if (width_output_line_tristate_buffer_p[j] > pmos_to_nmos_sizing_r * g_tp.max_w_nmos_)
  {
    c_load = gate_C(pmos_to_nmos_sizing_r * g_tp.max_w_nmos_ + g_tp.max_w_nmos_, 0, is_dram);
    F =  gnand2 * c_load / gate_C(width_output_line_tristate_buffer_n[0] + width_output_line_tristate_buffer_p[0], 0, is_dram);
    number_gates_output_line_tristate_buffer = (int) (log(F) / log(fopt)) + 1;
    if (number_gates_output_line_tristate_buffer%2 != 0)
    {
      ++number_gates_output_line_tristate_buffer;
    }
    if (number_gates_output_line_tristate_buffer < min_number_gates)
    {
      number_gates_output_line_tristate_buffer = min_number_gates;
    }
    f = pow(F, 1.0 / (number_gates_output_line_tristate_buffer - 1));
    j = number_gates_output_line_tristate_buffer - 1;
    width_output_line_tristate_buffer_p[j] = pmos_to_nmos_sizing_r * g_tp.max_w_nmos_;
    width_output_line_tristate_buffer_n[j] = width_output_line_tristate_buffer_p[j] / pmos_to_nmos_sizing_r;
  }
  
  for (j = number_gates_output_line_tristate_buffer - 2; j >= 1; --j)
  {
    if (j == number_gates_output_line_tristate_buffer - 2)
    {
      c_input = gate_C(width_output_line_tristate_buffer_p[j+1], 0, is_dram) / f;
    }
    else
    {
      c_input = gate_C(width_output_line_tristate_buffer_n[j+1] + width_output_line_tristate_buffer_p[j+1], 0, is_dram) / f;
    }
    width_output_line_tristate_buffer_n[j] = (1 / (1 + pmos_to_nmos_sizing_r)) * c_input / gate_C(1, 0, is_dram);
    if (width_output_line_tristate_buffer_n[j] < g_tp.min_w_nmos_)
    {
      width_output_line_tristate_buffer_n[j] = g_tp.min_w_nmos_;
    }
    width_output_line_tristate_buffer_p[j]  = pmos_to_nmos_sizing_r  * width_output_line_tristate_buffer_n[j];
  }
  
  // compute widths of buffer driving input line of crossbar
  crossbar_input_line_driver.c_gate_load = num_out_ports * gate_C(width_output_line_tristate_buffer_n[0] +
                                                                  width_output_line_tristate_buffer_p[0] +
                                                                  width_output_line_tristate_buffer_nor2_n +
                                                                  width_output_line_tristate_buffer_nor2_p, 0, is_dram);
  crossbar_input_line_driver.c_wire_load = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_out_ports * g_tp.wire_outside_mat.C_per_um;
  crossbar_input_line_driver.r_wire_load = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_out_ports * g_tp.wire_outside_mat.R_per_um;
  crossbar_input_line_driver.compute_widths(is_dram);
}


double Crossbar::compute_delay(double inrisetime)
{
  double rd, c_load, tf, c_intrinsic, c_wire_load, r_wire_load, this_delay, pmos_to_nmos_sizing_r;
  int j;
  
  pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  
  // delay in input line
  double outrisetime    = crossbar_input_line_driver.compute_delay(inrisetime, is_dram);
  delay                += crossbar_input_line_driver.delay;
  power.readOp.dynamic += crossbar_input_line_driver.power.readOp.dynamic;
  power.readOp.leakage += crossbar_input_line_driver.power.readOp.leakage;
  
  // delay in output line
  for (j = 0; j < number_gates_output_line_tristate_buffer; ++j)
  {
    if (j == 0)
    { //NAND2 gate
      rd = tr_R_on(width_output_line_tristate_buffer_n[j], NCH, 2, is_dram);
      
      if (number_gates_output_line_tristate_buffer == 2)
      {
        c_load = gate_C(width_output_line_tristate_buffer_p[j+1], 0.0, is_dram);//NAND2 driving PMOS output stage
      }
      else
      {
        c_load = gate_C(width_output_line_tristate_buffer_p[j+1], 0.0, is_dram);//NAND2 driving inverter
      }

      c_intrinsic = drain_C_(width_output_line_tristate_buffer_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram) +
                2 * drain_C_(width_output_line_tristate_buffer_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load);
      power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
    else if (j == number_gates_output_line_tristate_buffer - 1)
    {
      //PMOS 
      rd = tr_R_on(width_output_line_tristate_buffer_p[j], PCH, 1, is_dram);
      c_wire_load = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_in_ports * g_tp.wire_outside_mat.C_per_um;
      r_wire_load = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_in_ports * g_tp.wire_outside_mat.R_per_um;
      c_load =  c_wire_load + c_output_line_load;
      c_intrinsic = drain_C_(width_output_line_tristate_buffer_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) +
                    drain_C_(width_output_line_tristate_buffer_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load) + r_wire_load * (c_wire_load / 2 + c_output_line_load +
          drain_C_(width_output_line_tristate_buffer_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram) + 
          drain_C_(width_output_line_tristate_buffer_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram));
      power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
    else
    { //inverter
      rd = tr_R_on(width_output_line_tristate_buffer_n[j], NCH, 1, is_dram);
      if (j == number_gates_output_line_tristate_buffer - 2)
      {//inverter driving PMOS of output stage
        c_load = gate_C(width_output_line_tristate_buffer_p[j+1], 0.0, is_dram);
      }
      else{//inverter driving inverter
        c_load = gate_C(width_output_line_tristate_buffer_n[j+1] + width_output_line_tristate_buffer_p[j+1], 0.0, is_dram);
      }
      c_intrinsic = drain_C_(width_output_line_tristate_buffer_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) +
                    drain_C_(width_output_line_tristate_buffer_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
      tf = rd * (c_intrinsic + c_load);
      power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    }
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    delay += this_delay;
    inrisetime = this_delay/(1.0 - 0.5);
  }    
  outrisetime = inrisetime;
  
  for (j = 0; j < number_gates_output_line_tristate_buffer; ++j)
  {
    if (j == 0)
    { //NAND gate, NOR gate
      power.readOp.leakage += cmos_Ileak(width_output_line_tristate_buffer_n[j],
          width_output_line_tristate_buffer_p[j], is_dram) * 0.5 * g_tp.peri_global.Vdd;
      power.readOp.leakage += cmos_Ileak(width_output_line_tristate_buffer_nor2_n, 
          width_output_line_tristate_buffer_nor2_p, is_dram) * 0.5 * g_tp.peri_global.Vdd;
    }
    else if (j == number_gates_output_line_tristate_buffer - 1)
    { //PMOS and NMOS output stage
      power.readOp.leakage += cmos_Ileak(width_output_line_tristate_buffer_n[j], 
          width_output_line_tristate_buffer_p[j], is_dram) * 0.5 * g_tp.peri_global.Vdd;
    }
    else
    { //inverters in NAND2 and NOR2 paths. Assume that inverters in the NOR2 path 
      //half the width of corresp inverters in NAND2 path (because inverters in NOR2 path
      //drive NMOS of output stage compared to inverters in NAND2 path that drive PMOS of 
      //output stage)
      power.readOp.leakage += cmos_Ileak(width_output_line_tristate_buffer_n[j] + width_output_line_tristate_buffer_n[j] / pmos_to_nmos_sizing_r,
          width_output_line_tristate_buffer_p[j] + width_output_line_tristate_buffer_p[j] / pmos_to_nmos_sizing_r, is_dram) * 
        0.5 * g_tp.peri_global.Vdd;
    }
  }
  return outrisetime;
}


void Crossbar::compute_area()
{
  area.h = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_in_ports;
  area.w = g_tp.wire_outside_mat.pitch * num_signals_per_port * num_out_ports;
}

