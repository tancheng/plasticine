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

#include "htree.h"
#include "parameter.h"
#include <iostream>
#include <math.h>
#include <assert.h>
#include <list>

using namespace std;


HtreeNode::HtreeNode(int num_gates_stage)
 :number_gates(0),
  min_number_gates(2),
  c_gate_load(0),
  c_wire_load(0),
  r_wire_load(0),
  f_wire(0),
  delay(0),
  width_nor2_n(0),
  width_nor2_p(0),
  power()
{
}


AddrDatainHtreeNode::AddrDatainHtreeNode(
    int    num_htree_nodes_,
    double htree_seg_length,
    bool   is_dram_)
 :node(MAX_NUMBER_HTREE_NODES, HtreeNode(MAX_NUMBER_GATES_STAGE)),
  delay(0),
  power(),
  length_wire_htree_node(MAX_NUMBER_HTREE_NODES, 0),
  is_dram(is_dram_),
  num_htree_nodes(num_htree_nodes_),
  area(MAX_NUMBER_HTREE_NODES, Area())
{
  if(num_htree_nodes == 0)
  {
    num_htree_nodes = 1;
  }
  assert(num_htree_nodes <= MAX_NUMBER_HTREE_NODES);

  int multiplier = 1;
  for (int i = num_htree_nodes - 1; i >= 0; --i)
  {
    length_wire_htree_node[i] = multiplier * htree_seg_length;
    node[i].r_wire_load = g_tp.wire_inside_mat.R_per_um * length_wire_htree_node[i];
    node[i].c_wire_load = g_tp.wire_inside_mat.C_per_um * length_wire_htree_node[i];
    node[i].f_wire      = node[i].r_wire_load * node[i].c_wire_load / (2 * g_tp.kinv);
    multiplier *= 2;
  }
}


void AddrDatainHtreeNode::initialize()
{
  double input_gate_cap, F, c_load;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double gnand2 = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);

  double unit_gate_C = gate_C(1, 0, is_dram);

  // calculate width of input gate of each H-tree segment
  node[0].width_n[0] = 2 * g_tp.min_w_nmos_;
  node[0].width_p[0] = pmos_to_nmos_sizing_r * g_tp.min_w_nmos_;

  for (int i = num_htree_nodes - 1; i > 0; --i)
  {
    if (i == num_htree_nodes - 1)
    {
      node[i].c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
    }
    else
    {
      node[i].c_gate_load = 2 * gate_C(node[i+1].width_n[0] + node[i+1].width_p[0], 0, is_dram);
    }
    c_load = node[i].c_gate_load + node[i].c_wire_load;
    F      = MAX((node[i-1].r_wire_load*gnand2*c_load/(2 * g_tp.kinv * fopt)) * (1 + sqrt(1 + 2*fopt/node[i-1].f_wire)), pow(fopt, 2));
    input_gate_cap = gnand2 * c_load / F;
    node[i].width_n[0] = (input_gate_cap / 2) / unit_gate_C;
    node[i].width_n[i] = MAX(node[i].width_n[0], 2*g_tp.min_w_nmos_);
    if(node[i].width_n[0] > g_tp.max_w_nmos_)
    { // two-stage buffer with 2nd stage composed of g_tp.max_w_nmos_. 
      c_load = gate_C(g_tp.max_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.max_w_nmos_, 0, is_dram);
      input_gate_cap = gnand2 * c_load / fopt;
      node[i].width_n[0] = (2/(2 + pmos_to_nmos_sizing_r)) * input_gate_cap / unit_gate_C;
    }
    node[i].width_p[0] = (pmos_to_nmos_sizing_r / (2 + pmos_to_nmos_sizing_r)) * input_gate_cap / unit_gate_C;
  }
  if(num_htree_nodes > 1)
  {
    node[0].c_gate_load = gate_C(node[1].width_n[0] + node[1].width_p[0], 0, is_dram);
  }
  else
  {
    node[0].c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r*g_tp.min_w_nmos_, 0, is_dram);
  }
}


void AddrDatainHtreeNode::compute_widths()
{
  int i;
  double c_load, F;
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double gnand2 = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  
  for (i = 0; i < num_htree_nodes; ++i)
  {
    c_load = node[i].c_gate_load + node[i].c_wire_load;
    F      = gnand2 * c_load / gate_C(node[i].width_n[0] + node[i].width_p[0], 0, is_dram);
    node[i].number_gates = logical_effort(
        node[i].min_number_gates,
        gnand2,
        F,
        node[i].width_n,
        node[i].width_p,
        c_load,
        pmos_to_nmos_sizing_r,
        is_dram,
        false,
        false);
  }
}


void AddrDatainHtreeNode::compute_areas()
{
  Area inv, nand2;

  for (int i = 0; i < num_htree_nodes; ++i)
  {
    double cumulative_area = 0;  
    nand2 = Area::gatearea(NAND, 2, node[i].width_p[0], node[i].width_n[0], g_tp.cell_h_def);
    cumulative_area += nand2.get_area();  

    for (int j = 1; j < node[i].number_gates; ++j)
    {
      inv = Area::gatearea(INV,  1, node[i].width_p[j], node[i].width_n[j], g_tp.cell_h_def);
      cumulative_area += inv.get_area();
    }
    area[i].set_area(cumulative_area);
  }
}


pair<double, double> AddrDatainHtreeNode::compute_delays(double inrisetime)
{
  pair<double, double> ret_val;   // <outrisetime, delay>
  ret_val.first  = 0;
  ret_val.second = 0;

  double rd, c_load, c_intrinsic, tf, this_delay;

  for (int i = 0; i < num_htree_nodes ; ++i)
  {
    for (int j = 0; j < node[i].number_gates; ++j)
    {
      if(j == 0)
      { // NAND2 gate
        rd = tr_R_on(node[i].width_n[j], NCH, 2, is_dram);
        c_load = gate_C(node[i].width_n[j+1] + node[i].width_p[j+1], 0.0, is_dram);
        c_intrinsic = drain_C_(node[i].width_p[j], PCH,1,1, g_tp.cell_h_def, is_dram)*2 +
                      drain_C_(node[i].width_n[j], NCH,2,1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load);
      }
      else if (j == node[i].number_gates - 1)
      {
        rd = tr_R_on(node[i].width_n[j], NCH, 1, is_dram);
        c_load = node[i].c_gate_load + node[i].c_wire_load;
        c_intrinsic = drain_C_(node[i].width_p[j], PCH,1,1, g_tp.cell_h_def, is_dram) +
                      drain_C_(node[i].width_n[j], NCH,1,1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load) + node[i].r_wire_load * (node[i].c_wire_load / 2 + node[i].c_gate_load);
      }
      else
      { // inverter
        rd = tr_R_on(node[i].width_n[j], NCH, 1, is_dram);
        c_load = gate_C(node[i].width_n[j+1] + node[i].width_p[j+1], 0.0, is_dram);
        c_intrinsic = drain_C_(node[i].width_p[j], PCH,1,1, g_tp.cell_h_def, is_dram) +
                      drain_C_(node[i].width_n[j], NCH,1,1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load);
      }
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += this_delay;
      inrisetime = this_delay/(1.0 - 0.5);
    }
  }
  ret_val.first = inrisetime;
  return ret_val;
}


powerDef AddrDatainHtreeNode::compute_power(int htree_node)
{
  double      c_load, c_intrinsic;
  powerDef    pow;
  HtreeNode & curr_node = node[htree_node];

  for (int j = 0; j < curr_node.number_gates; ++j)
  {
    if(j == 0)
    { // NAND2 gate
      c_load = gate_C(curr_node.width_n[j+1] + curr_node.width_p[j+1], 0.0, is_dram);
      c_intrinsic = drain_C_(curr_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) * 2 + 
                    drain_C_(curr_node.width_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram);
    }
    else if (j == curr_node.number_gates - 1)
    {
      c_load = curr_node.c_gate_load + curr_node.c_wire_load;
      c_intrinsic = drain_C_(curr_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                    drain_C_(curr_node.width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
    }
    else{//inverter
      c_load = gate_C(curr_node.width_n[j+1] + curr_node.width_p[j+1], 0.0, is_dram);
      c_intrinsic = drain_C_(curr_node.width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                    drain_C_(curr_node.width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
    }
    pow.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
    pow.readOp.leakage += cmos_Ileak(curr_node.width_n[j], curr_node.width_p[j], is_dram) *
                          0.5 * g_tp.peri_global.Vdd;
  }

  return pow;
}


DataoutHtreeNode::DataoutHtreeNode(
    int num_htree_nodes_,
    const vector<double> & len_wire_htree_node_,
    bool is_dram_)
 :AddrDatainHtreeNode(num_htree_nodes_, 0, is_dram_),
  len_wire_htree_node(len_wire_htree_node_)
{
}


void DataoutHtreeNode::initialize()
{
  double input_gate_cap, F, c_load, nand2_gate_cap;

  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double unit_gate_C           = gate_C(1, 0, is_dram);
  double gnand2                = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnor2                 = (1 + 2*pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnmos                 = 1 / (1 + pmos_to_nmos_sizing_r);
  double gpmos                 = pmos_to_nmos_sizing_r / (1 + pmos_to_nmos_sizing_r);

  for (int i = num_htree_nodes - 2; i >= 0; --i)
  {
    node[i].min_number_gates = 2;
    node[i].r_wire_load = g_tp.wire_outside_mat.R_per_um * len_wire_htree_node[i];
    node[i].c_wire_load = g_tp.wire_outside_mat.C_per_um * len_wire_htree_node[i];
    node[i].f_wire      = node[i].r_wire_load * node[i].c_wire_load / (2 * g_tp.kinv);
  }

  for (int i = 0; i < num_htree_nodes - 1; ++i)
  {
    if (i != 0 && i == num_htree_nodes - 2)
    {
      node[i].width_n[0]  = 2 * g_tp.min_w_nmos_;
      node[i].c_gate_load = gate_C(
          node[i-1].width_n[0] + node[i-1].width_p[0] +
          node[i-1].width_nor2_n + node[i-1].width_nor2_p , 0, is_dram);
    }
    else
    {
      if(i == 0)
      {
        node[i].c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
      }
      else
      {
        node[i].c_gate_load = gate_C(
            node[i-1].width_n[0] + node[i-1].width_p[0] +
            node[i-1].width_nor2_n + node[i-1].width_nor2_p , 0, is_dram);
      }
      c_load = node[i].c_gate_load + node[i].c_wire_load;
      F      = (node[i+1].r_wire_load * gnand2 * gpmos * c_load / (2 * g_tp.kinv * fopt)) *
               (1 + sqrt(1 + 2 * fopt / node[i+1].f_wire));
      F      = MAX(F, pow(fopt, 2));
      input_gate_cap = gnand2 * gpmos * c_load / F;
      node[i].width_n[0] = (2 / (2 + pmos_to_nmos_sizing_r)) * input_gate_cap / unit_gate_C;
      node[i].width_n[0] = MAX(node[i].width_n[0], 2 * g_tp.min_w_nmos_);
      if(node[i].width_n[0] > g_tp.max_w_nmos_)
      {
        // two-stage buffer with 2nd stage composed of pmos_to_nmos_sizing_r * g_tp.max_w_nmos_.
        // first stage NAND2 width calculated as follows
        c_load = gate_C(pmos_to_nmos_sizing_r * g_tp.max_w_nmos_, 0, is_dram);
        input_gate_cap = gnand2 * c_load / fopt;
        node[i].width_n[0] = (2 / (2 + pmos_to_nmos_sizing_r)) * input_gate_cap / unit_gate_C;
      }
    }
    node[i].width_p[0] = (pmos_to_nmos_sizing_r / 2) * node[i].width_n[0];
    nand2_gate_cap = gate_C(node[i].width_n[0] + node[i].width_p[0], 0, is_dram);
    input_gate_cap = gnor2 * gnmos * nand2_gate_cap / (gnand2 * gpmos);
    node[i].width_nor2_n = MAX((1 / (1 + 2*pmos_to_nmos_sizing_r)) * input_gate_cap / unit_gate_C, g_tp.min_w_nmos_);
    node[i].width_nor2_p = 2 * pmos_to_nmos_sizing_r * node[i].width_nor2_n;
  }
}


void DataoutHtreeNode::compute_widths()
{
  int i, j;
  double G, c_load, H, f, B, F, c_input;

  
  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double unit_gate_C           = gate_C(1, 0, is_dram);
  double gnand2                = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gpmos                 = pmos_to_nmos_sizing_r / (1 + pmos_to_nmos_sizing_r);

  B = 1;
  G = gnand2 * gpmos;

  for (i = num_htree_nodes - 2; i >= 0; --i)
  {
    c_load = node[i].c_gate_load + node[i].c_wire_load;
    H = c_load / gate_C(node[i].width_n[0] + node[i].width_p[0] , 0, is_dram);
    F = G * B * H;
    node[i].number_gates = (int) (log(F) / log(fopt));
    node[i].number_gates+= (node[i].number_gates % 2) ? 1 : 0;
    node[i].number_gates = MAX(node[i].number_gates, node[i].min_number_gates);
    f = pow(F, 1.0 / node[i].number_gates);
    j = node[i].number_gates - 1;
    node[i].width_p[j] = MAX(gpmos * c_load /(f * unit_gate_C), pmos_to_nmos_sizing_r * g_tp.min_w_nmos_);
    node[i].width_n[j] = node[i].width_p[j] / pmos_to_nmos_sizing_r;

    if (node[i].width_p[j] > pmos_to_nmos_sizing_r * g_tp.max_w_nmos_)
    {
      c_load = gate_C(pmos_to_nmos_sizing_r * g_tp.max_w_nmos_ + g_tp.max_w_nmos_, 0, is_dram);
      F =  gnand2 * c_load / gate_C(node[i].width_n[0] + node[i].width_p[0] , 0, is_dram);
      node[i].number_gates = (int) (log(F) / log(fopt)) + 1;
      node[i].number_gates+= (node[i].number_gates % 2) ? 1 : 0;
      node[i].number_gates = MAX(node[i].number_gates, node[i].min_number_gates);
      f = pow(F, 1.0 / (node[i].number_gates - 1));
      j = node[i].number_gates - 1;
      node[i].width_n[j] = g_tp.max_w_nmos_;
      node[i].width_p[j] = node[i].width_n[j] * pmos_to_nmos_sizing_r;
    }

    for (j = node[i].number_gates - 2; j >= 1; --j)
    {
      if (j == node[i].number_gates - 2)
      {
        c_input = gate_C(node[i].width_p[j+1], 0, is_dram) / f;
      }
      else
      {
        c_input = gate_C(node[i].width_n[j+1] + node[i].width_p[j+1], 0, is_dram) / f;
      }
      node[i].width_n[j] = (1.0 / (1.0 + pmos_to_nmos_sizing_r)) * (c_input / unit_gate_C);
      node[i].width_n[j] = MAX(node[i].width_n[j], g_tp.min_w_nmos_);
      node[i].width_p[j] = pmos_to_nmos_sizing_r * node[i].width_n[j];
    }
  }
}


void DataoutHtreeNode::compute_areas()
{
  for (int i = 0; i < num_htree_nodes; i++)
  {
    int j;
    Area inv, nand2, nor2;
    double cumulative_area = 0;
    
    j     = node[i].number_gates - 1;
    inv   = Area::gatearea(INV,  1, node[i].width_p[j], node[i].width_n[j], g_tp.cell_h_def);
    cumulative_area += inv.get_area();
    j     = node[i].number_gates - 2;
    nand2 = Area::gatearea(NAND, 2, node[i].width_p[j], node[i].width_n[j], g_tp.cell_h_def);
    cumulative_area += nand2.get_area();
    nor2  = Area::gatearea(NOR,  2, node[i].width_nor2_p, node[i].width_nor2_n, g_tp.cell_h_def);
    cumulative_area += nor2.get_area();
    for (j = 0; j <= node[i].number_gates - 3; ++j)
    {
      inv = Area::gatearea(INV,  1, node[i].width_p[j], node[i].width_n[j], g_tp.cell_h_def);
      cumulative_area += inv.get_area();
    }
    area[i].set_area(cumulative_area);
  }
}


pair<double, double> DataoutHtreeNode::compute_delays(double inrisetime)
{
  int i, j;
  pair<double, double> ret_val(0, 0);   // <outrisetime, delay>, power is a member variable
  double rd, c_load, c_intrinsic, tf, this_delay;

  for (i = num_htree_nodes - 2; i >= 0; --i)
  {
    for (j = 0; j < node[i].number_gates; ++j)
    {
      if (j == 0)
      { //NAND2 gate
        rd = tr_R_on(node[i].width_n[j], NCH, 2, is_dram);
        if (node[i].number_gates == 2)
        {
          c_load = gate_C(node[i].width_p[j+1], 0.0, is_dram); //NAND2 driving PMOS output stage
        }
        else
        {
          c_load = gate_C(node[i].width_p[j+1], 0.0, is_dram); //NAND2 driving inverter
        }
        c_intrinsic = drain_C_(node[i].width_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram) + 
                      2 * drain_C_(node[i].width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load);
        power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      else if(j == node[i].number_gates - 1)
      { //PMOS 
        rd = tr_R_on(node[i].width_p[j], PCH, 1, is_dram);
        c_load = node[i].c_wire_load + node[i].c_gate_load;
        c_intrinsic = drain_C_(node[i].width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                      drain_C_(node[i].width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load) + node[i].r_wire_load * 
             (node[i].c_wire_load / 2 + node[i].c_gate_load +
              drain_C_(node[i].width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram) + 
              drain_C_(node[i].width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram));
        power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }
      else
      { //inverter
        rd = tr_R_on(node[i].width_n[j], NCH, 1, is_dram);
        if(j == node[i].number_gates - 2)
        { //inverter driving PMOS of output stage
          c_load = gate_C(node[i].width_p[j+1], 0.0, is_dram);
        }
        else
        { //inverter driving inverter
          c_load = gate_C(node[i].width_n[j+1] + node[i].width_p[j+1], 0.0, is_dram);
        }        
        c_intrinsic = drain_C_(node[i].width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) +
                      drain_C_(node[i].width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
        tf = rd * (c_intrinsic + c_load);
        power.readOp.dynamic += (c_intrinsic + c_load) * 0.5 * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
      }

      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += this_delay;
      inrisetime = this_delay/(1.0 - 0.5);
    }
  }
  ret_val.first = inrisetime;

  int htree_node_multiplier = 1;
  for (i = 0; i < num_htree_nodes - 1; ++i)
  {
    for (j = 0; j < node[i].number_gates; ++j)
    {
      if(j == 0)
      { //NAND gate, NOR gate
        power.readOp.leakage += cmos_Ileak(node[i].width_n[j], node[i].width_p[j], is_dram) *
                                0.5 * g_tp.peri_global.Vdd * htree_node_multiplier;
        power.readOp.leakage += cmos_Ileak(node[i].width_nor2_n, node[i].width_nor2_p, is_dram) *
                                0.5 * g_tp.peri_global.Vdd * htree_node_multiplier;
      }
      else if (j == node[i].number_gates - 1)
      { //PMOS and NMOS output stage
        power.readOp.leakage += cmos_Ileak(node[i].width_n[j], node[i].width_p[j], is_dram) *
                                0.5 * g_tp.peri_global.Vdd * htree_node_multiplier;
      }
      else
      { // inverters in NAND2 and NOR2 paths. Assume that inverters in the NOR2 path 
        // half the width of corresp inverters in NAND2 path (because inverters in NOR2 path
        // drive NMOS of output stage compared to inverters in NAND2 path that drive PMOS of 
        // output stage)
        power.readOp.leakage += cmos_Ileak(node[i].width_n[j] + node[i].width_n[j]/2,
                                           node[i].width_p[j] + node[i].width_p[j]/2, is_dram) * 
                                0.5 * g_tp.peri_global.Vdd * htree_node_multiplier;
      }
      htree_node_multiplier *= 2;
    }
  }

  return ret_val;
}


Dout_htree_node::Dout_htree_node(const Area & subarray, bool is_dram)
{
  uint32_t j;
  double G, H, F, f, C_input, B, nand2_gate_cap, C_load;
  double pton_sz_r  = pmos_to_nmos_sz_ratio(is_dram);
  double min_w_nmos = g_tp.min_w_nmos_;
  double min_w_pmos = g_tp.min_w_nmos_ * pton_sz_r;
  double gnand2     = (2 + pton_sz_r) / (1 + pton_sz_r);
  double gnor2      = (1 + 2*pton_sz_r) / (1 + pton_sz_r);
  double gnmos      = 1 / (1 + pton_sz_r);
  double gpmos      = pton_sz_r / (1 + pton_sz_r);
  
  num_gates_min  = 2;
  // minimum-size NAND2 + Minimum-size NOR2
  C_gate_ld  = gate_C(2*min_w_nmos + min_w_pmos + min_w_nmos + 2*min_w_pmos, 0, is_dram);
  R_wire_ld  = g_tp.wire_outside_mat.R_per_um * subarray.get_h();
  C_wire_ld  = g_tp.wire_outside_mat.C_per_um * subarray.get_h();

  width_n[0] = 2 * min_w_nmos;
  width_p[0] = min_w_pmos;
  nand2_gate_cap = gate_C(width_n[0] + width_p[0], 0, is_dram);
  C_input        = gnor2 * gnmos * nand2_gate_cap / (gnand2 * gpmos);
  width_nor2_n   = MAX((1/(1 + 2*pton_sz_r)) * C_input / gate_C(1, 0, is_dram), min_w_nmos);
  width_nor2_p   = 2 * pton_sz_r * width_nor2_n;

  B          = 1;
  G          = gnand2 * gpmos;
  C_load     = C_gate_ld + C_wire_ld;
  H          = C_load / gate_C(width_n[0] + width_p[0] , 0, is_dram);
  F          = G * B * H;
  num_gates  = (int) (log(F) / log(fopt));
  num_gates += (num_gates%2 != 0) ? 1 : 0;
  num_gates  = MAX(num_gates, num_gates_min);
  f          = pow(F, 1.0 / num_gates);
  j          = num_gates - 1;
  width_p[j] = gpmos * C_load / (f * gate_C(1, 0, is_dram));
  width_p[j] = MAX(width_p[j], min_w_pmos);
  width_n[j] = width_p[j] / pton_sz_r;

  if (width_p[j] > pton_sz_r * g_tp.max_w_nmos_)
  {
    C_load     = gate_C(pton_sz_r * g_tp.max_w_nmos_ + g_tp.max_w_nmos_, 0, is_dram);
    F          = gnand2 * C_load / gate_C(width_n[0] +  width_p[0], 0, is_dram);
    num_gates  = (int) (log(F) / log(fopt)) + 1;
    num_gates += (num_gates%2 != 0) ? 1 : 0;
    num_gates  = MAX(num_gates, num_gates_min);
    f          = pow(F, 1.0 / (num_gates - 1));
    j          = num_gates - 1;
    width_p[j] = pton_sz_r * g_tp.max_w_nmos_;
    width_n[j] = width_p[j] / pton_sz_r;
  }

  for (j = num_gates - 2; j >= 1; --j)
  {
    if (j == num_gates - 2)
    {
      C_input = gate_C(width_p[j+1], 0, is_dram) / f;
    }
    else
    {
      C_input = gate_C(width_n[j+1] + width_p[j+1], 0, is_dram) / f;
    }
    width_n[j] = (1/(1 + pton_sz_r)) * C_input / gate_C(1, 0, is_dram);
    width_n[j] = MAX(width_n[j], min_w_nmos);
    width_p[j] = pton_sz_r * width_n[j];
  }

  assert(num_gates <= MAX_NUMBER_GATES_STAGE);
}


AddrDatainHtreeAtMatInterval::AddrDatainHtreeAtMatInterval(
    double mat_dimension,
    int    num_htree_nodes_,
    bool   is_dram_)
 :tristate_driver_nand2_width_n(0),
  tristate_driver_nand2_width_p(0),
  tristate_driver_nor2_width_n(0),
  tristate_driver_nor2_width_p(0),
  time_constant_internal_buffer(0),
  time_constant_buffer_driving_buffer(0),
  time_constant_buffer_driving_final_seg(0),
  power_buffer(),
  power_buffer_driving_nand2_buffer(),
  power_nand2_buffer_driving_buffer(),
  power_nand2_buffer_driving_nand2_buffer(),
  power_nand2_buffer_driving_final_seg(),
  power_tristate_driver_driving_buffer(),
  power_tristate_driver_driving_tristate_driver(),
  power_buffer_driving_tristate_driver(),
  power_tristate_driver_driving_final_seg(),
  power_buffer_driving_final_seg(),
  area_nand2_buffer_driving_final_seg(0),
  area_nand2_buffer_driving_nand2_buffer(0),
  area_nand2_buffer_driving_buffer(0),
  area_buffer(0),
  area_buffer_driving_final_seg(0),
  area_buffer_driving_nand2_buffer(0),
  area_tristate_driver_driving_final_seg(0),
  area_tristate_driver_driving_tristate_driver(0),
  area_tristate_driver_driving_buffer(0),
  mat_dimension(mat_dimension),
  max_delay_between_buffers(0),
  is_dram(is_dram_),
  num_htree_nodes(num_htree_nodes_)
{
}


void AddrDatainHtreeAtMatInterval::compute_area_drivers(const BankHtreeSizing & bank_htree_sz)
{
  Area inv1, inv2, nand2, tri_nand2, tri_nor2;

  inv1      = Area::gatearea(INV,  1, bank_htree_sz.buffer_width_p[0], bank_htree_sz.buffer_width_n[0], g_tp.cell_h_def);
  inv2      = Area::gatearea(INV,  1, bank_htree_sz.buffer_width_p[1], bank_htree_sz.buffer_width_n[1], g_tp.cell_h_def);
  nand2     = Area::gatearea(NAND, 2, bank_htree_sz.nand2_buffer_width_p[0], bank_htree_sz.nand2_buffer_width_n[0], g_tp.cell_h_def);
  tri_nand2 = Area::gatearea(NAND, 2, bank_htree_sz.tristate_driver_nand2_width_p, bank_htree_sz.tristate_driver_nand2_width_p, g_tp.cell_h_def);
  tri_nor2  = Area::gatearea(NOR,  2, bank_htree_sz.tristate_driver_nor2_width_p, bank_htree_sz.tristate_driver_nor2_width_p, g_tp.cell_h_def);

  area_buffer = inv1.get_area() + inv2.get_area();
  area_nand2_buffer_driving_final_seg          = nand2.get_area() + inv2.get_area();
  area_nand2_buffer_driving_nand2_buffer       = area_nand2_buffer_driving_final_seg;
  area_nand2_buffer_driving_buffer             = area_nand2_buffer_driving_final_seg;
  area_buffer_driving_final_seg                = area_buffer;
  area_buffer_driving_nand2_buffer             = area_buffer;
  area_tristate_driver_driving_final_seg       = tri_nand2.get_area() + tri_nor2.get_area() + inv2.get_area();
  area_tristate_driver_driving_tristate_driver = area_tristate_driver_driving_final_seg;
  area_tristate_driver_driving_buffer          = area_tristate_driver_driving_final_seg;
}


Area AddrDatainHtreeAtMatInterval::compute_area_addr_datain(
    double len_htree_seg,
    int htree_node_num,
    const BankHtreeSizing & bank_htree_sz) const
{
  Area area_ret;

  double cumulative_area = 0;
  if (len_htree_seg < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)
  {
    if (htree_node_num == num_htree_nodes - 1)
    {
      cumulative_area += area_nand2_buffer_driving_final_seg;
    }
    else
    {
      cumulative_area += area_nand2_buffer_driving_nand2_buffer;
    }
  }
  else
  {
    int num_buffers = (int)(floor(len_htree_seg / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)));
    //double buffer_segment_length = len_htree_seg / num_buffers;
    num_buffers -= 1;
    cumulative_area += area_nand2_buffer_driving_buffer;
    cumulative_area += (num_buffers - 1) * area_buffer;
    if(htree_node_num == num_htree_nodes - 1)
    {
      cumulative_area += area_buffer_driving_final_seg;
    }
    else
    {
      cumulative_area += area_buffer_driving_nand2_buffer;
    }
  }
  area_ret.set_area(cumulative_area);
  return area_ret;
}


Area AddrDatainHtreeAtMatInterval::compute_area_dataout_ver(
    double len_htree_seg,
    int htree_node_num,
    const BankHtreeSizing & bank_htree_sz) const
{
  Area area_ret;

  double cumulative_area = 0;
  if(len_htree_seg < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)
  {
    if(htree_node_num == 0)
    {
      cumulative_area += area_tristate_driver_driving_final_seg;
    }
    else
    {
      cumulative_area += area_tristate_driver_driving_tristate_driver;
    }
  }
  else
  {
    int num_buffers = (int)(floor(len_htree_seg / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)));
    //double buffer_segment_length = len_htree_seg / num_buffers;
    num_buffers -= 1;
    cumulative_area += area_tristate_driver_driving_buffer;
    cumulative_area += (num_buffers - 1) * area_buffer;
    if(htree_node_num == 0)
    {
      cumulative_area += area_buffer_driving_final_seg;
    }
    else
    {
      cumulative_area += area_buffer_driving_nand2_buffer;
    }
  }
  area_ret.set_area(cumulative_area);
  return area_ret;
}


pair<double, double> AddrDatainHtreeAtMatInterval::compute_delay_addr_datain(
    double c_output_load,
    int    broadcast,
    double inrisetime,
    const BankHtreeSizing & bank_htree_sz)
{ // return value <outrisetime, delay>
  pair<double, double> ret_val(0, 0);   // <outrisetime, delay>, power is a member variable
  
  int j, number_buffers;
  double c_gate_load, c_wire_load, c_intrinsic, rd, tf, this_delay,
    buffer_out_resistance, length, buffer_segment_length, r_wire,
    time_constant_internal_nand2_buffer, time_constant_buffer_driving_nand2_buffer, 
    time_constant_nand2_buffer_driving_buffer, time_constant_nand2_buffer_driving_nand2_buffer, 
    time_constant_nand2_buffer_driving_final_seg, delay_internal_buffer, delay_internal_nand2_buffer, pmos_to_nmos_sizing_r;

  pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  //Calculate delay of buffer
  j = 0;
  rd = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_n[j+1] + bank_htree_sz.buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_internal_buffer = rd * (c_intrinsic + c_gate_load);
    power_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[j], bank_htree_sz.buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  buffer_out_resistance = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_n[j-1] + bank_htree_sz.buffer_width_p[j-1], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_buffer_driving_buffer = buffer_out_resistance * (c_intrinsic + c_gate_load);
  power_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[j], bank_htree_sz.buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;

  //Calculate delay of NAND2 buffer driving buffer and delay of NAND2 buffer driving final segment
  //of H-tree
  j = 0;
  rd = tr_R_on(bank_htree_sz.nand2_buffer_width_n[j], NCH, 2, is_dram);
  c_gate_load = gate_C(bank_htree_sz.nand2_buffer_width_n[j+1] + bank_htree_sz.nand2_buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = 2 * drain_C_(bank_htree_sz.nand2_buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.nand2_buffer_width_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram);
  time_constant_internal_nand2_buffer = rd * (c_intrinsic + c_gate_load);
  power_nand2_buffer_driving_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_nand2_buffer_driving_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  rd = tr_R_on(bank_htree_sz.nand2_buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_n[0] + bank_htree_sz.buffer_width_p[0], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.nand2_buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.nand2_buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_nand2_buffer_driving_buffer = rd * (c_intrinsic + c_gate_load);
  c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0.0, is_dram);
  time_constant_nand2_buffer_driving_final_seg = rd * (c_intrinsic + c_gate_load);
  power_nand2_buffer_driving_buffer.readOp.dynamic += (c_intrinsic + 
    c_gate_load) * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_nand2_buffer_driving_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], 
    bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 0.5 * g_tp.peri_global.Vdd;


  //Calculate delay of buffer driving NAND2 buffer and delay of buffer driving final segment of H-tree
  j = 0;
  c_gate_load = gate_C(bank_htree_sz.buffer_width_n[j+1] + bank_htree_sz.buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  power_buffer_driving_nand2_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer_driving_nand2_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  rd = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = 2 * gate_C(bank_htree_sz.nand2_buffer_width_n[0] + bank_htree_sz.nand2_buffer_width_p[0], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_buffer_driving_nand2_buffer = rd * (c_intrinsic + c_gate_load);
  power_buffer_driving_nand2_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer_driving_nand2_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
  time_constant_buffer_driving_final_seg = rd * (c_intrinsic + c_gate_load);
  power_buffer_driving_final_seg.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer_driving_final_seg.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) *
    0.5 * g_tp.peri_global.Vdd;

  //Calculate delay of NAND2 buffer driving NAND2 buffer
  j = 0;
  c_gate_load = gate_C(bank_htree_sz.nand2_buffer_width_n[j+1] + bank_htree_sz.nand2_buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = 2 * drain_C_(bank_htree_sz.nand2_buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.nand2_buffer_width_n[j], NCH, 2, 1, g_tp.cell_h_def, is_dram);
  power_nand2_buffer_driving_nand2_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_nand2_buffer_driving_nand2_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  rd = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = 2 * gate_C(bank_htree_sz.nand2_buffer_width_n[0] + bank_htree_sz.nand2_buffer_width_p[0], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_nand2_buffer_driving_nand2_buffer = rd * (c_intrinsic + c_gate_load);
  power_nand2_buffer_driving_nand2_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_nand2_buffer_driving_nand2_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.nand2_buffer_width_n[j], bank_htree_sz.nand2_buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  
  length = pow(2, num_htree_nodes - 1) * mat_dimension / 2;
  for (j = 0; j < num_htree_nodes; ++j)
  {
    if(length < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length){
      c_wire_load = length * g_tp.wire_outside_mat.C_per_um;
      r_wire = length * g_tp.wire_outside_mat.R_per_um;
      delay_internal_nand2_buffer = horowitz(inrisetime, time_constant_internal_nand2_buffer, 0.5
          , 0.5, RISE);
      inrisetime = delay_internal_nand2_buffer / (1.0 - 0.5);
      if(j == num_htree_nodes - 1){//NAND2 buffer is driving input of gate in mat
        c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_nand2_buffer_driving_final_seg;
      }
      else{//NAND2 buffer is driving NAND2 buffer
        c_gate_load = gate_C(bank_htree_sz.nand2_buffer_width_n[0] + bank_htree_sz.nand2_buffer_width_p[0], 0.0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_nand2_buffer_driving_nand2_buffer;
      }
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += delay_internal_nand2_buffer + this_delay;
      if((delay_internal_nand2_buffer + this_delay) > 
        max_delay_between_buffers){
          max_delay_between_buffers = delay_internal_nand2_buffer + this_delay;
      }
      inrisetime = this_delay / (1.0 - 0.5);
    }
    else{
      number_buffers = (int)(floor(length / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)));
      buffer_segment_length = length / number_buffers;
      number_buffers -= 1;
      c_wire_load = buffer_segment_length * g_tp.wire_outside_mat.C_per_um;
      r_wire = buffer_segment_length * g_tp.wire_outside_mat.R_per_um;
      //Calculate delay of NAND2 buffer driving buffer
      delay_internal_nand2_buffer = horowitz(inrisetime, time_constant_internal_nand2_buffer, 0.5,
        0.5, RISE);
      inrisetime = delay_internal_nand2_buffer / (1.0 - 0.5);
      c_gate_load = gate_C(bank_htree_sz.buffer_width_n[0] + bank_htree_sz.buffer_width_p[0], 0.0, is_dram);
      tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
        c_gate_load) + time_constant_nand2_buffer_driving_buffer;
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += delay_internal_nand2_buffer + this_delay;
      if((delay_internal_nand2_buffer + this_delay) > 
        max_delay_between_buffers){
          max_delay_between_buffers = delay_internal_nand2_buffer + this_delay;
      }
      inrisetime = this_delay / (1.0 - 0.5);

      //Calculate delay through all but the last buffer
      delay_internal_buffer = horowitz(inrisetime, time_constant_internal_buffer, 0.5, 0.5, RISE);
      inrisetime = delay_internal_buffer / (1.0 - 0.5);
      tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
        c_gate_load) + time_constant_buffer_driving_buffer;
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += (number_buffers - 1) * (delay_internal_buffer + this_delay);
      inrisetime = this_delay / (1.0 - 0.5);

      //Calculate delay through the last buffer
      delay_internal_buffer = horowitz(inrisetime, time_constant_internal_buffer, 0.5, 0.5, RISE);
      inrisetime = delay_internal_buffer / (1.0 - 0.5);
      if(j == num_htree_nodes - 1){
        c_gate_load = gate_C(c_output_load, 0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_buffer_driving_final_seg;
        this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      }
      else{
        c_gate_load = 2 * gate_C(bank_htree_sz.nand2_buffer_width_n[0] + bank_htree_sz.nand2_buffer_width_p[0], 0.0, is_dram);  
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_buffer_driving_nand2_buffer;
        this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      }
      ret_val.second += (this_delay + delay_internal_buffer);
      if((delay_internal_buffer + this_delay) > 
        max_delay_between_buffers){
          max_delay_between_buffers = delay_internal_buffer + this_delay;
      }
      inrisetime = this_delay / (1.0 - 0.5);
    }
    length /= 2;
  }
  ret_val.first = inrisetime;

  return ret_val;
}


pair<double, double> AddrDatainHtreeAtMatInterval::compute_delay_dataout_ver(
    double inrisetime,
    const BankHtreeSizing & bank_htree_sz)
{ // return value <outrisetime, delay>
  pair<double, double> ret_val(0, 0);   // <outrisetime, delay>, power is a member variable

  int j, i, number_buffers;
  double rd, c_gate_load, c_intrinsic, c_wire_load, tf, this_delay, 
    r_wire, buffer_out_resistance, length, buffer_segment_length,
    time_constant_internal_tristate_driver, time_constant_tristate_driver_driving_tristate_driver, 
    time_constant_tristate_driver_driving_buffer, time_constant_buffer_driving_tristate_driver, 
    time_constant_tristate_driver_driving_final_seg, delay_internal_tristate_driver, delay_internal_buffer, pmos_to_nmos_sizing_r;
 
  pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  //Calculate delay of tristate driver driving buffer and delay of tristate driver driving final segment
  j = 0;
  rd = tr_R_on(bank_htree_sz.tristate_driver_nand2_width_n, NCH, 2, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = 2 * drain_C_(bank_htree_sz.tristate_driver_nand2_width_p, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.tristate_driver_nand2_width_n, NCH, 2, 1, g_tp.cell_h_def, is_dram);
  time_constant_internal_tristate_driver = rd * (c_intrinsic + c_gate_load);
  power_tristate_driver_driving_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_tristate_driver_driving_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.tristate_driver_nand2_width_n, tristate_driver_nand2_width_p, is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  power_tristate_driver_driving_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.tristate_driver_nor2_width_n, tristate_driver_nor2_width_p, is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  buffer_out_resistance = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load =  gate_C(bank_htree_sz.buffer_width_n[0] + bank_htree_sz.buffer_width_p[0], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_tristate_driver_driving_buffer = rd * (c_intrinsic + c_gate_load);
  c_gate_load =  gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0.0, is_dram);
  time_constant_tristate_driver_driving_final_seg = rd * (c_intrinsic + c_gate_load);
  power_tristate_driver_driving_buffer.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_tristate_driver_driving_buffer.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[1], bank_htree_sz.buffer_width_p[1], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;

  //Calculate delay of tristate driver driving tristate driver
  j = 0;
  rd = tr_R_on(bank_htree_sz.tristate_driver_nand2_width_n, NCH, 2, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = 2 * drain_C_(bank_htree_sz.tristate_driver_nand2_width_p, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.tristate_driver_nand2_width_n, NCH, 2, 1, g_tp.cell_h_def, is_dram);
  power_tristate_driver_driving_tristate_driver.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_tristate_driver_driving_tristate_driver.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.tristate_driver_nand2_width_n, tristate_driver_nand2_width_p, is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  power_tristate_driver_driving_tristate_driver.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.tristate_driver_nor2_width_n, tristate_driver_nor2_width_p, is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  rd = tr_R_on(bank_htree_sz.buffer_width_p[j], PCH, 1, is_dram);
  c_gate_load =  gate_C(bank_htree_sz.tristate_driver_nand2_width_n + 
    bank_htree_sz.tristate_driver_nand2_width_p +
    bank_htree_sz.tristate_driver_nor2_width_n + 
    bank_htree_sz.tristate_driver_nor2_width_p, 0.0, is_dram) +
    drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
    drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_tristate_driver_driving_tristate_driver = rd * (c_intrinsic + c_gate_load);
  power_tristate_driver_driving_tristate_driver.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_tristate_driver_driving_tristate_driver.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[1], bank_htree_sz.buffer_width_p[1], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  

  //Calculate delay of buffer driving tristate driver
  j = 0;
  rd = tr_R_on(bank_htree_sz.buffer_width_n[j], NCH, 1, is_dram);
  c_gate_load = gate_C(bank_htree_sz.buffer_width_n[j+1] + bank_htree_sz.buffer_width_p[j+1], 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.tristate_driver_nand2_width_p, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.tristate_driver_nand2_width_n, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  power_buffer_driving_tristate_driver.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer_driving_tristate_driver.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[j], bank_htree_sz.buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;
  j = 1;
  rd = tr_R_on(bank_htree_sz.buffer_width_p[j], PCH, 1, is_dram);
  c_gate_load =  gate_C(bank_htree_sz.tristate_driver_nand2_width_n + 
    bank_htree_sz.tristate_driver_nand2_width_p +
    bank_htree_sz.tristate_driver_nor2_width_n + 
    bank_htree_sz.tristate_driver_nor2_width_p, 0.0, is_dram);
  c_intrinsic = drain_C_(bank_htree_sz.buffer_width_p[j], PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                drain_C_(bank_htree_sz.buffer_width_n[j], NCH, 1, 1, g_tp.cell_h_def, is_dram);
  time_constant_buffer_driving_tristate_driver = rd * (c_intrinsic + c_gate_load);
  power_buffer_driving_tristate_driver.readOp.dynamic += (c_intrinsic + c_gate_load) * 
    g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
  power_buffer_driving_tristate_driver.readOp.leakage += 
    cmos_Ileak(bank_htree_sz.buffer_width_n[j], bank_htree_sz.buffer_width_p[j], is_dram) * 
    0.5 * g_tp.peri_global.Vdd;

  ret_val.second = 0;
  length = mat_dimension;
  for (i = num_htree_nodes - 2; i >= 0; --i)
  {
    if (length < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)
    {
      c_wire_load = length * g_tp.wire_outside_mat.C_per_um;
      r_wire = length * g_tp.wire_outside_mat.R_per_um;
      delay_internal_tristate_driver = horowitz(inrisetime, time_constant_internal_tristate_driver, 0.5, 0.5, RISE);
      inrisetime = delay_internal_tristate_driver / (1.0 - 0.5);
      if (i == 0)
      {
        c_gate_load = gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
        tf = buffer_out_resistance * (c_wire_load + c_gate_load) + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_tristate_driver_driving_final_seg;
      }
      else
      {
        c_gate_load =  gate_C(bank_htree_sz.tristate_driver_nand2_width_n + 
          bank_htree_sz.tristate_driver_nand2_width_p +
          bank_htree_sz.tristate_driver_nor2_width_n + 
          bank_htree_sz.tristate_driver_nor2_width_p, 0.0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_tristate_driver_driving_tristate_driver;
      }
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += (this_delay + delay_internal_tristate_driver);
      if ((delay_internal_tristate_driver + this_delay) > max_delay_between_buffers)
      {
          max_delay_between_buffers = delay_internal_tristate_driver + this_delay;
      }
      inrisetime = this_delay / (1.0 - 0.5);
    }
    else
    {
      number_buffers = (int)(floor(length / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * 
        bank_htree_sz.buffer_segment_opt_length)));
      buffer_segment_length = length / number_buffers;
      number_buffers -= 1;
      //Calculate delay of tristate driver driving buffer
      c_wire_load = buffer_segment_length * g_tp.wire_outside_mat.C_per_um;
      r_wire = buffer_segment_length * g_tp.wire_outside_mat.R_per_um;
      delay_internal_tristate_driver = horowitz(inrisetime, time_constant_internal_tristate_driver, 0.5,
        0.5, RISE);
      inrisetime = delay_internal_tristate_driver / (1.0 - 0.5);
      c_gate_load = gate_C(bank_htree_sz.buffer_width_n[0] + bank_htree_sz.buffer_width_p[0], 0.0, is_dram);
      tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
        c_gate_load) + time_constant_tristate_driver_driving_buffer;
        this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      inrisetime = this_delay / (1.0 - 0.5);
      ret_val.second += this_delay + delay_internal_tristate_driver;
      if ((delay_internal_tristate_driver + this_delay) > max_delay_between_buffers)
      {
          max_delay_between_buffers = delay_internal_tristate_driver + this_delay;
      }

      //Calculate delay through all but the last buffer
      delay_internal_buffer = horowitz(inrisetime, time_constant_internal_buffer, 0.5, 0.5, RISE);
      inrisetime = delay_internal_buffer / (1.0 - 0.5);
      tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
        c_gate_load) + time_constant_buffer_driving_buffer;
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += (number_buffers - 1) * (delay_internal_buffer + this_delay);
      inrisetime = this_delay / (1.0 - 0.5);

      //Calculate delay through the last buffer
      if (i == 0)
      {
        c_gate_load = 2 * gate_C(g_tp.min_w_nmos_ + pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, 0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_buffer_driving_final_seg;
      }
      else
      {
        c_gate_load =  gate_C(bank_htree_sz.tristate_driver_nand2_width_n + 
          bank_htree_sz.tristate_driver_nand2_width_p +
          bank_htree_sz.tristate_driver_nor2_width_n + 
          bank_htree_sz.tristate_driver_nor2_width_p, 0.0, is_dram);
        tf = buffer_out_resistance * c_wire_load + r_wire * (c_wire_load / 2 + 
          c_gate_load) + time_constant_buffer_driving_tristate_driver;
      }
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += this_delay + delay_internal_buffer;
      if((delay_internal_buffer + this_delay) > max_delay_between_buffers)
      {
          max_delay_between_buffers = delay_internal_buffer + this_delay;
      }
      inrisetime = this_delay / (1.0 - 0.5);
    }
    length *= 2;
  }

  return ret_val;
}


powerDef AddrDatainHtreeAtMatInterval::compute_power_addr_datain(
    double len_htree_seg, 
    int    htree_node_num,
    const BankHtreeSizing & bank_htree_sz)
{
  powerDef power;
  int number_buffers;
  double buffer_segment_length, c_wire_load;
  if (len_htree_seg < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)
  {
    c_wire_load = len_htree_seg * g_tp.wire_outside_mat.C_per_um;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    if (htree_node_num == num_htree_nodes - 1)
    {
      power.readOp.dynamic += power_nand2_buffer_driving_final_seg.readOp.dynamic;
      power.readOp.leakage += power_nand2_buffer_driving_final_seg.readOp.leakage;
    }
    else
    {
      power.readOp.dynamic += power_nand2_buffer_driving_nand2_buffer.readOp.dynamic;
      power.readOp.leakage += power_nand2_buffer_driving_nand2_buffer.readOp.leakage;
    }
  }
  else
  {
    number_buffers = (int)(floor(len_htree_seg / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)));
    buffer_segment_length = len_htree_seg / number_buffers;
    number_buffers -= 1;
    c_wire_load = buffer_segment_length * g_tp.wire_outside_mat.C_per_um;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power.readOp.dynamic += power_nand2_buffer_driving_buffer.readOp.dynamic;
    power.readOp.leakage += power_nand2_buffer_driving_buffer.readOp.leakage;
    power.readOp.dynamic += (number_buffers - 1) * c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power.readOp.dynamic += (number_buffers - 1) * power_buffer.readOp.dynamic;
    power.readOp.leakage += (number_buffers - 1) * power_buffer.readOp.leakage;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    if (htree_node_num == num_htree_nodes - 1)
    {
      power.readOp.dynamic += power_buffer_driving_final_seg.readOp.dynamic;
      power.readOp.leakage += power_buffer_driving_final_seg.readOp.leakage;
    }
    else
    {
      power.readOp.dynamic += power_buffer_driving_nand2_buffer.readOp.dynamic;
      power.readOp.leakage += power_buffer_driving_nand2_buffer.readOp.leakage;
    }
  }

  return power;
}


powerDef AddrDatainHtreeAtMatInterval::compute_power_dataout_ver(
    double len_htree_seg,
    int    htree_node_num,
    const BankHtreeSizing & bank_htree_sz)
{
  powerDef power;
  int number_buffers;
  double c_wire_load, buffer_segment_length;

  if (len_htree_seg < 2 * BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)
  {
    c_wire_load = len_htree_seg * g_tp.wire_outside_mat.C_per_um;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    if(htree_node_num == 0)
    {
      power.readOp.dynamic += power_tristate_driver_driving_final_seg.readOp.dynamic;
      power.readOp.leakage += power_tristate_driver_driving_final_seg.readOp.leakage;
    }
    else
    {
      power.readOp.dynamic += power_tristate_driver_driving_tristate_driver.readOp.dynamic;
      power.readOp.leakage += power_tristate_driver_driving_tristate_driver.readOp.leakage;
    }
  }
  else
  {
    number_buffers = (int)(floor(len_htree_seg / (BUFFER_SEPARATION_LENGTH_MULTIPLIER * bank_htree_sz.buffer_segment_opt_length)));
    buffer_segment_length = len_htree_seg / number_buffers;
    number_buffers -= 1;
    c_wire_load = buffer_segment_length * g_tp.wire_outside_mat.C_per_um;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power.readOp.dynamic += power_tristate_driver_driving_buffer.readOp.dynamic;
    power.readOp.leakage += power_tristate_driver_driving_buffer.readOp.leakage;
    power.readOp.dynamic += (number_buffers - 1) * c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power.readOp.dynamic += (number_buffers - 1) * power_buffer.readOp.dynamic;
    power.readOp.leakage += (number_buffers - 1) * power_buffer.readOp.leakage;
    power.readOp.dynamic += c_wire_load * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    if (htree_node_num == 0)
    {
      power.readOp.dynamic += power_tristate_driver_driving_final_seg.readOp.dynamic;
      power.readOp.leakage += power_tristate_driver_driving_final_seg.readOp.leakage;
    }
    else
    {
      power.readOp.dynamic += power_tristate_driver_driving_tristate_driver.readOp.dynamic;
      power.readOp.leakage += power_tristate_driver_driving_tristate_driver.readOp.leakage;
    }
  }

  return power;
}


ArrayEdgeToBankEdgeHtreeSizing::ArrayEdgeToBankEdgeHtreeSizing()
 :opt_sizing(0),
  opt_segment_length(0),
  width_inverter_nmos(0),
  width_inverter_pmos(0)
{
}



void ArrayEdgeToBankEdgeHtreeSizing::compute_widths(bool is_dram)
{
  double d, R_v, C_g, C_d, b, a, r, k, R_w, C_w, FO4, lcap, wcap, lopt, wopt,
         delay_per_micron, best_delay, perc_diff_from_dyn_energy_best_delay,
         perc_diff_in_delay_from_best_delay_repeater_solution, dyn_energy_best_delay, delay_given_length,
         dyn_energy, best_perc_diff_from_dyn_energy_best_delay, delay_per_micron_1,
         dyn_energy_best_delay_1, best_delay_new_wcap;
  double pmos_to_nmos_sizing_r;
  list<repeater_solution> solution;
  
  opt_sizing = 0;
  pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  d = 0;
  R_v = tr_R_on(1, NCH, 1, is_dram);
  C_g = gate_C(1, 0, is_dram);
  C_d = drain_C_(1, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  a = 3;
  b = C_d * a / C_g;
  r = 1;
  k = 0.69;
  R_w = g_tp.wire_outside_mat.R_per_um;
  C_w = g_tp.wire_outside_mat.C_per_um;
  lcap = 1;
  FO4 = R_v * a * (C_d + 4 * C_g);    
  
  lopt = sqrt((18.9 * d + 2 * k * r * (a + b)) / k) * sqrt(R_v * C_g / (R_w * C_w));
  wopt = sqrt(r / a) * sqrt(R_v * C_w / (R_w * C_g));
  lcap = 1.0;
  wcap = 1.0;
  
  double sqrt_fo4_rw_cw = sqrt(FO4 * R_w * C_w);
  double delay_coeff_1  = sqrt(d + (k*r*(a+b)/9.45)) * sqrt(k/2);
  double delay_coeff_2  = k * sqrt(a * r / 9.45);
  double energy_coeff_1 = sqrt(r/a)*sqrt(k/(18.9*d + 2*k*r*(a+b)))*(a+b);
  double energy_coeff_2 = C_w * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  delay_per_micron_1 = (delay_coeff_1 * (1 / lcap + lcap) +
                        delay_coeff_2 * (1 / wcap + wcap));
  delay_per_micron =  delay_per_micron_1 * sqrt_fo4_rw_cw;
  best_delay = delay_per_micron;
  dyn_energy_best_delay_1 = 1 + energy_coeff_1 * wcap / lcap;
  dyn_energy_best_delay = dyn_energy_best_delay_1 * energy_coeff_2;

  //Now find a solution that is better from an energy-delay perspective. First find solutions that are 
  //worse in delay but within MAX_PERC_DIFF_IN_DELAY_FROM_BEST_DELAY_REPEATER_SOLUTION of the best 
  //delay solution
  for (lcap = 1.0; lcap < 10; lcap += 0.1)
  {
    for (wcap = 1.0; wcap > 0; wcap -= 0.05)
    {
      delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                          delay_coeff_2 * (1 / wcap + wcap)) * sqrt_fo4_rw_cw;
      delay_given_length = delay_per_micron;
      perc_diff_in_delay_from_best_delay_repeater_solution = (delay_given_length - best_delay) * 100 / best_delay;
      if(perc_diff_in_delay_from_best_delay_repeater_solution <= g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol)
      {
        dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
        solution.push_back(repeater_solution());
        solution.back().lcap = lcap;
        solution.back().wcap = wcap;
        solution.back().delay = delay_given_length;
        solution.back().power.readOp.dynamic = dyn_energy;
        solution.back().power.readOp.leakage = 0;  
      }
      else
      {
        break;
      }
    }
    if (wcap == 1.0)
    {
      break;
    }
  }
  
  list<repeater_solution>::iterator best_solution_iter = solution.end();
  best_perc_diff_from_dyn_energy_best_delay = 0;
  for (list<repeater_solution>::iterator iter = solution.begin(); iter != solution.end(); ++iter)
  {
    perc_diff_from_dyn_energy_best_delay = (double)((dyn_energy_best_delay - 
          iter->power.readOp.dynamic) * 100 / dyn_energy_best_delay);
    if (perc_diff_from_dyn_energy_best_delay >= best_perc_diff_from_dyn_energy_best_delay)
    {
      best_perc_diff_from_dyn_energy_best_delay =	perc_diff_from_dyn_energy_best_delay;	
      best_solution_iter = iter;
    }
  }
  width_inverter_nmos = best_solution_iter->wcap * wopt;
  if (width_inverter_nmos > g_tp.max_w_nmos_)
  {
    width_inverter_nmos = g_tp.max_w_nmos_;
    //Now wcap has changed and needs to be calculated based on the above value of 
    //width_inverter_nmos. So recalculate optimal lcap for the new wcap.
    //Now it's possible that with the new wcap, the solution does not satisfy the 
    //g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol constraint. In that case, choose the best delay 
    //solution possible with the new wcap
    wcap = width_inverter_nmos / wopt;
    //Now find out if the g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol can be satisfied
    //with the new wcap and if so find appropriate solution.
    solution.clear();
    for (lcap = 1.0; lcap < 10.1; lcap += 0.1)
    {
      delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                          delay_coeff_2 * (1 / wcap + wcap)) * sqrt_fo4_rw_cw;
      delay_given_length = delay_per_micron;
      perc_diff_in_delay_from_best_delay_repeater_solution = (delay_given_length - best_delay) * 100 / best_delay;
      if (perc_diff_in_delay_from_best_delay_repeater_solution <= g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol)
      {
        dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
        solution.push_back(repeater_solution());
        solution.back().lcap = lcap;
        solution.back().wcap = wcap;
        solution.back().delay = delay_given_length;
        solution.back().power.readOp.dynamic = dyn_energy;
        solution.back().power.readOp.leakage = 0;  
      }
      else
      {
        break;
      }
    }
    if (solution.empty() == true)
    { //this means that it is not possible to satisfy the 
      //g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol with the new wcap, so 
      //we simply find the solution that gives best delay with the new wcap. 
      best_delay_new_wcap = BIGNUM;
      solution.push_back(repeater_solution());
      for (lcap = 1.0; lcap < 10.1; lcap += 0.1)
      {
        delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                            delay_coeff_2 * (1 / wcap + wcap)) * sqrt(FO4 * R_w * C_w);
        delay_given_length = delay_per_micron;
        if (delay_given_length < best_delay_new_wcap)
        {
          dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
          best_delay_new_wcap = delay_given_length;
          solution.back().lcap = lcap;
          solution.back().wcap = wcap;
          solution.back().delay = delay_given_length;
          solution.back().power.readOp.dynamic = dyn_energy;
          solution.back().power.readOp.leakage = 0;
          best_solution_iter = solution.begin();
        }
        else
        {
          break;
        }
      }
    }
    else
    {
      best_perc_diff_from_dyn_energy_best_delay = 0;
      for (list<repeater_solution>::iterator iter = solution.begin(); iter != solution.end(); ++iter)
      {
        perc_diff_from_dyn_energy_best_delay = (double)((dyn_energy_best_delay - 
              iter->power.readOp.dynamic) * 100 / dyn_energy_best_delay);
        if (perc_diff_from_dyn_energy_best_delay >= best_perc_diff_from_dyn_energy_best_delay)
        {
          best_perc_diff_from_dyn_energy_best_delay =  perc_diff_from_dyn_energy_best_delay;  
          best_solution_iter = iter;
        }
      }
    }
  }
  width_inverter_pmos = pmos_to_nmos_sizing_r * width_inverter_nmos;
  opt_sizing          = width_inverter_nmos / g_tp.min_w_nmos_;
  opt_segment_length  = best_solution_iter->lcap * lopt;
}


pair<double, double> ArrayEdgeToBankEdgeHtreeSizing::compute_delays(
    double inrisetime,
    double length,
    double horizontal_htree_input_load,
    bool   is_dram,
    powerDef & power) const
{
  double rd, c_intrinsic, c_gate_load, c_wire_load, r_wire, tf, this_delay;
  powerDef power_repeater;
  int opt_number_repeaters;
  
  pair<double, double> ret_val;
  ret_val.first  = 0;  // outrisetime
  ret_val.second = 0;  // delay
  
  if (length > 0)
  {		
    opt_number_repeaters = (int) (ceil(length / opt_segment_length));

    // calculate delay of first inverter driving second inverter (repeater stage)
    rd = tr_R_on(width_inverter_nmos, NCH, 1, is_dram);
    c_intrinsic = drain_C_(width_inverter_pmos, PCH, 1, 1, g_tp.cell_h_def, is_dram) + 
                  drain_C_(width_inverter_nmos, NCH, 1, 1, g_tp.cell_h_def, is_dram);
    c_gate_load = gate_C(width_inverter_nmos + width_inverter_pmos, 0.0, is_dram);
    c_wire_load = opt_segment_length * g_tp.wire_outside_mat.C_per_um;
    r_wire      = opt_segment_length * g_tp.wire_outside_mat.R_per_um;
    tf = rd * (c_intrinsic + c_wire_load + c_gate_load) + r_wire * (c_wire_load / 2 + c_gate_load);
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    power_repeater.readOp.dynamic = (c_intrinsic + c_wire_load + c_gate_load) * 
      g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power_repeater.readOp.leakage  = cmos_Ileak(width_inverter_nmos, width_inverter_pmos, is_dram) * 
      0.5 * g_tp.peri_global.Vdd;
    if (opt_number_repeaters > 1)
    {
      ret_val.second += this_delay;
      inrisetime = this_delay/(1.0 - 0.5);
      power.readOp.dynamic += power_repeater.readOp.dynamic;
      power.readOp.leakage += power_repeater.readOp.leakage;
    }
    
    // calculate delay through all except last but one inverter
    if (opt_number_repeaters > 2)
    {
      this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
      ret_val.second += (opt_number_repeaters - 2) * this_delay;
      power.readOp.dynamic += power_repeater.readOp.dynamic * (opt_number_repeaters - 2);
      power.readOp.leakage += power_repeater.readOp.leakage * (opt_number_repeaters - 2);
    }

    // calculate delay of last inverter driving input of Horizontal H-tree within bank
    c_gate_load = horizontal_htree_input_load;
    tf = rd * (c_intrinsic + c_wire_load + c_gate_load) + r_wire * (c_wire_load / 2 + c_gate_load);
    this_delay = horowitz(inrisetime, tf, 0.5, 0.5, RISE);
    ret_val.second += this_delay;
    ret_val.first  = this_delay/(1.0 - 0.5);
    power.readOp.dynamic += (c_intrinsic + c_wire_load + c_gate_load) * 
      g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 0.5;
    power.readOp.leakage  += cmos_Ileak(width_inverter_nmos, width_inverter_pmos, is_dram) * 
      0.5 * g_tp.peri_global.Vdd;
    power.writeOp.dynamic = power.readOp.dynamic;
  }

  return ret_val;
}


BankHtreeSizing::BankHtreeSizing()
 :buffer_opt_sizing(0),
  buffer_segment_opt_length(0),
  tristate_driver_nand2_width_n(0),
  tristate_driver_nand2_width_p(0),
  tristate_driver_nor2_width_n(0),
  tristate_driver_nor2_width_p(0)
{
}


void BankHtreeSizing::compute_widths(bool is_dram)
{
  double d, R_v, C_g, C_d, b, a, r, k, R_w, C_w, FO4, lcap, wcap, lopt, wopt, F2point5, e,
    internal_fanout, wopt_featuresize, delay_per_micron, delay_given_length, best_delay, dyn_energy_best_delay,
    dyn_energy, perc_diff_in_delay_from_best_delay_repeater_solution, 
    best_perc_diff_from_dyn_energy_best_delay,perc_diff_from_dyn_energy_best_delay, best_delay_new_wcap;
  double c_input_nand2, c_input_nor2;
  double dyn_energy_best_delay_1;

  list<buffer_solution> solution;
  list<buffer_solution>::iterator iter, best_solution_iter;


  double pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);
  double gnand2     = (2 + pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double gnor2      = (1 + 2*pmos_to_nmos_sizing_r) / (1 + pmos_to_nmos_sizing_r);
  double unit_gate_C = gate_C(1, 0, is_dram);
  // compute sizing of buffer repeaters using Ron Ho's sizing
  R_v = tr_R_on(1, NCH, 1, is_dram);
  C_g = unit_gate_C;
  C_d = drain_C_(1, NCH, 1, 1, g_tp.cell_h_def, is_dram);
  a = 3;
  internal_fanout = 2.5;
  b = (C_d / C_g) * a * internal_fanout;
  r = 1 / internal_fanout;
  k = 0.69;
  e = a * (C_d / C_g + internal_fanout);
  FO4 = R_v * a * (C_d + 4 * C_g);
  F2point5 = R_v * a * (C_d + 2.5 * C_g);
  d = F2point5 / FO4;
  R_w = g_tp.wire_outside_mat.R_per_um;
  C_w = g_tp.wire_outside_mat.C_per_um;
  lopt = sqrt((18.9 * d + 2 * k * r * (a + b)) / k) * sqrt(R_v * C_g / (R_w * C_w));
  wopt = sqrt(r / a) * sqrt(R_v * C_w / (R_w * C_g));
  wopt_featuresize = wopt / g_ip.F_sz_um;
  lcap = 1.0;
  wcap = 1.0;

  double sqrt_fo4_rw_cw = sqrt(FO4 * R_w * C_w);
  double k_r_ab_945     = (k * r * (a + b) / 9.45);
  double delay_coeff_1  = sqrt(d + k_r_ab_945) * sqrt(k/2);
  double delay_coeff_2  = k* sqrt(a*r/9.45);
  double energy_coeff_1 = sqrt(r/a)*sqrt(k/(18.9*d + 2*k*r*(a+b)))*(a+b);
  double energy_coeff_2 = C_w * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd;
  delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                      delay_coeff_2 * (1 / wcap + wcap)) * sqrt_fo4_rw_cw;
  best_delay = delay_per_micron;
  dyn_energy_best_delay = C_w * g_tp.peri_global.Vdd * g_tp.peri_global.Vdd * 
    (1 + sqrt(r / a) * sqrt(k / (18.9 * d + 2 * k * r * (a + b + e))) * (a + b + e) * wcap / lcap);
  dyn_energy_best_delay_1 = 1 + energy_coeff_1 * wcap / lcap;
  dyn_energy_best_delay = dyn_energy_best_delay_1 * energy_coeff_2;

  // now find a solution that is better from an energy-delay perspective.
  // find the solution that reduces energy by about OPT_PERC_DIFF_IN_ENERGY_FROM_BEST_DELAY_REPEATER_SOLUTION.
  // we first find solutions that are worse in delay but within
  // g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol of the best delay delay. 
  for (lcap = 1.0; lcap < 10.1; lcap += 0.1)
  {
    for (wcap = 1.0; wcap > 0; wcap -= 0.025)
    {
      delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                          delay_coeff_2 * (1 / wcap + wcap)) * sqrt_fo4_rw_cw;
      delay_given_length = delay_per_micron;
      perc_diff_in_delay_from_best_delay_repeater_solution = (delay_given_length - best_delay) * 100 / best_delay;
      if (perc_diff_in_delay_from_best_delay_repeater_solution <=
          g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol)
      {
        dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
        solution.push_back(buffer_solution());
        solution.back().lcap = lcap;
        solution.back().wcap = wcap;
        solution.back().delay = delay_given_length;
        solution.back().power.readOp.dynamic = dyn_energy;
        solution.back().power.readOp.leakage = 0;  
      }
      else
      {
        break;
      }
    }
    if (wcap == 1.0)
    {
      break;
    }
  }

  best_perc_diff_from_dyn_energy_best_delay = 0;
  for (iter = solution.begin(); iter != solution.end(); ++iter)
  {
    perc_diff_from_dyn_energy_best_delay = (double)((dyn_energy_best_delay - 
      iter->power.readOp.dynamic) * 100 / dyn_energy_best_delay);
    if (perc_diff_from_dyn_energy_best_delay >= best_perc_diff_from_dyn_energy_best_delay)
    {
      best_perc_diff_from_dyn_energy_best_delay =  perc_diff_from_dyn_energy_best_delay;
      best_solution_iter = iter;
    }
  }
  
  buffer_opt_sizing = best_solution_iter->wcap * wopt / (g_tp.min_w_nmos_);
  buffer_segment_opt_length = best_solution_iter->lcap * lopt;
  
  buffer_width_n[0] = best_solution_iter->wcap * wopt;
  buffer_width_n[1] = internal_fanout * buffer_width_n[0];

  if (buffer_width_n[1] > g_tp.max_w_nmos_)
  {
    buffer_width_n[1] = g_tp.max_w_nmos_;
    buffer_width_n[0] = g_tp.max_w_nmos_ / internal_fanout;
    // now wcap has changed and needs to be calculated based on the above value of 
    // buffer_width_n[0]. So recalculate optimal lcap for the new wcap.
    // wow it's possible that with the new wcap, the solution does not satisfy the 
    // g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol constraint.
    // in that case, choose the best delay solution possible with the new wcap
    wcap = buffer_width_n[0] / wopt;
    // now find out if the g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol
    // can be satisfied with the new wcap and if so find appropriate solution.

    solution.clear();
    for (lcap = 1.0; lcap < 10.1; lcap += 0.1)
    {
      delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                          delay_coeff_2 * (1 / wcap + wcap)) * sqrt_fo4_rw_cw;
      delay_given_length = delay_per_micron;
      perc_diff_in_delay_from_best_delay_repeater_solution = (delay_given_length - best_delay) * 100 / best_delay;
      if (perc_diff_in_delay_from_best_delay_repeater_solution <=
          g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol)
      {
        dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
        solution.push_back(buffer_solution());
        solution.back().lcap = lcap;
        solution.back().wcap = wcap;
        solution.back().delay = delay_given_length;
        solution.back().power.readOp.dynamic = dyn_energy;
        solution.back().power.readOp.leakage = 0;  
      }
      else
      {
        break;
      }
    }
    if (solution.empty() == true)
    {
      // this means that it is not possible to satisfy the 
      // g_ip.max_perc_diff_in_delay_fr_best_delay_rptr_sol with the new wcap, so 
      // we simply find the solution that gives best delay with the new wcap.
      best_delay_new_wcap = BIGNUM;
      solution.push_back(buffer_solution());
      for (lcap = 1.0; lcap < 10.1; lcap += 0.1)
      {
        delay_per_micron = (delay_coeff_1 * (1 / lcap + lcap) +
                            delay_coeff_2 * (1 / wcap + wcap)) * sqrt(FO4 * R_w * C_w);
        delay_given_length = delay_per_micron;
        if (delay_given_length < best_delay_new_wcap)
        {
          dyn_energy = energy_coeff_2 * (1 + energy_coeff_1*wcap/lcap);
          best_delay_new_wcap = delay_given_length;
          solution.back().lcap = lcap;
          solution.back().wcap = wcap;
          solution.back().delay = delay_given_length;
          solution.back().power.readOp.dynamic = dyn_energy;
          solution.back().power.readOp.leakage = 0;
          best_solution_iter = solution.begin();
        }
        else
        {
          break;
        }
      }
    }
    else
    {
      best_perc_diff_from_dyn_energy_best_delay = 0;
      for (iter = solution.begin(); iter != solution.end(); ++iter)
      {
        perc_diff_from_dyn_energy_best_delay = (double)((dyn_energy_best_delay - 
          iter->power.readOp.dynamic) * 100 / dyn_energy_best_delay);
        if (perc_diff_from_dyn_energy_best_delay >= best_perc_diff_from_dyn_energy_best_delay)
        {
          best_perc_diff_from_dyn_energy_best_delay =  perc_diff_from_dyn_energy_best_delay;  
          best_solution_iter = iter;
        }
      }
    }
    buffer_opt_sizing = wcap * wopt / (g_tp.min_w_nmos_);
    buffer_segment_opt_length = best_solution_iter->lcap * lopt;
    buffer_width_n[0] = wcap * wopt;
    buffer_width_n[1] = internal_fanout * buffer_width_n[0];
  }
  
  buffer_width_p[0] = pmos_to_nmos_sizing_r * buffer_width_n[0];
  buffer_width_p[1] = pmos_to_nmos_sizing_r * buffer_width_n[1];

  nand2_buffer_width_n[1] = buffer_width_n[1];
  nand2_buffer_width_p[1] = buffer_width_p[1];

  c_input_nand2 = gnand2 * gate_C(buffer_width_n[1] + buffer_width_n[1], 0, is_dram) / internal_fanout;
  nand2_buffer_width_n[0] = (2 / (2 + pmos_to_nmos_sizing_r)) * c_input_nand2 / unit_gate_C;
  nand2_buffer_width_p[0] = (pmos_to_nmos_sizing_r / (2 + pmos_to_nmos_sizing_r)) * c_input_nand2 / unit_gate_C;

  c_input_nand2 = gnand2 * gate_C(buffer_width_p[1], 0, is_dram) / internal_fanout;
  tristate_driver_nand2_width_n = (c_input_nand2 / 2) / unit_gate_C;
  tristate_driver_nand2_width_p = tristate_driver_nand2_width_n;
  // internal fanout on NOR2 path is assumed to be 3
  c_input_nor2 = gnor2 * gate_C(buffer_width_n[1], 0, is_dram) / 3.0;
  tristate_driver_nor2_width_n = (1.0 / ( 1 + 2 * pmos_to_nmos_sizing_r)) * c_input_nor2 / unit_gate_C;
  tristate_driver_nor2_width_p = (2 * pmos_to_nmos_sizing_r / (1 + 2 * pmos_to_nmos_sizing_r)) *
                                 c_input_nor2 / unit_gate_C;
}

