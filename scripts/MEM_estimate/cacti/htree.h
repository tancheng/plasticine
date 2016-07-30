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
#ifndef __HTREE_H__
#define __HTREE_H__

#include "area.h"
#include "parameter.h"
#include <vector>

using namespace std;


class HtreeNode
{
 public:
  HtreeNode(int num_gate_stage);

  int number_gates;
  int min_number_gates;
  double width_n[MAX_NUMBER_GATES_STAGE];
  double width_p[MAX_NUMBER_GATES_STAGE];
  double c_gate_load;
  double c_wire_load;
  double r_wire_load;
  double f_wire;
  double delay;
  double width_nor2_n;  // only used in DataoutHtreeNode
  double width_nor2_p;  // only used in DataoutHtreeNode
  powerDef power;
};
  

class AddrDatainHtreeNode
{
 public:
  AddrDatainHtreeNode(
      int num_htree_nodes_,
      double htree_seg_length,
      bool is_dram_);

  vector<HtreeNode> node;
  double            delay;
  powerDef          power;
  vector<double>    length_wire_htree_node;  //[MAX_NUMBER_GATES_STAGE];
  bool              is_dram;
  int               num_htree_nodes;
  vector<Area>      area;

  void initialize();
  void compute_widths();
  void compute_areas();
  powerDef compute_power(int htree_node);
  pair<double, double> compute_delays(double inrisetime);  // return <outrisetime, delay>
};


// TODO : following two classes must be combined properly
class DataoutHtreeNode : public AddrDatainHtreeNode
{
 public:
  DataoutHtreeNode(
      int num_htree_nodes_,
      const vector<double> & len_wire_htree_node_,
      bool is_dram_);
  
  const vector<double> & len_wire_htree_node;

  void initialize();
  void compute_widths();
  void compute_areas();
  // here compute_delays compute power as well
  pair<double, double> compute_delays(double inrisetime);  // return <outrisetime, delay>
};


class Dout_htree_node
{
 public:
  Dout_htree_node(const Area & subarray, bool is_dram);

  uint32_t num_gates;
  uint32_t num_gates_min;
  double   width_n[MAX_NUMBER_GATES_STAGE];
  double   width_p[MAX_NUMBER_GATES_STAGE];
  double   width_nor2_n;
  double   width_nor2_p;
  double   C_gate_ld;
  double   C_wire_ld;
  double   R_wire_ld;
  double   f_wire;
  double   delay;
  powerDef power;
};


class AddrDatainHtreeAtMatInterval
{
 public:
  AddrDatainHtreeAtMatInterval(
      double mat_dimension,
      int num_htree_nodes_,
      bool is_dram_);

  double buffer_opt_sizing;
  double buffer_segment_opt_length;
  double buffer_width_n[2];
  double buffer_width_p[2];
  double nand2_buffer_width_n[2];
  double nand2_buffer_width_p[2];
  double tristate_driver_nand2_width_n;
  double tristate_driver_nand2_width_p;
  double tristate_driver_nor2_width_n;
  double tristate_driver_nor2_width_p;
  double time_constant_internal_buffer;
  double time_constant_buffer_driving_buffer;
  double time_constant_buffer_driving_final_seg;
  powerDef power_buffer;
  powerDef power_buffer_driving_nand2_buffer;
  powerDef power_nand2_buffer_driving_buffer;
  powerDef power_nand2_buffer_driving_nand2_buffer;
  powerDef power_nand2_buffer_driving_final_seg;
  powerDef power_tristate_driver_driving_buffer;
  powerDef power_tristate_driver_driving_tristate_driver;
  powerDef power_buffer_driving_tristate_driver;
  powerDef power_tristate_driver_driving_final_seg;
  powerDef power_buffer_driving_final_seg;
  double area_nand2_buffer_driving_final_seg;
  double area_nand2_buffer_driving_nand2_buffer;
  double area_nand2_buffer_driving_buffer;
  double area_buffer;
  double area_buffer_driving_final_seg;
  double area_buffer_driving_nand2_buffer;
  double area_tristate_driver_driving_final_seg;
  double area_tristate_driver_driving_tristate_driver;
  double area_tristate_driver_driving_buffer;
  double mat_dimension;
  double max_delay_between_buffers;

  bool is_dram;
  int  num_htree_nodes;

  void compute_area_drivers(const BankHtreeSizing & bank_htree_sz);
  Area compute_area_addr_datain(double len_htree_seg, int htree_node_num, const BankHtreeSizing & bank_htree_sz) const;
  Area compute_area_dataout_ver(double len_htree_seg, int htree_node_num, const BankHtreeSizing & bank_htree_sz) const;
  pair<double, double> compute_delay_addr_datain(
      double c_output_load,
      int    broadcast,
      double inrisetime,
      const BankHtreeSizing & bank_htree_sz);  // return <outrisetime, delay>
  pair<double, double> compute_delay_dataout_ver(
      double inrisetime,
      const BankHtreeSizing & bank_htree_sz);  // return <outrisetime, delay>
  powerDef compute_power_addr_datain(
      double len_htree_seg, 
      int    htree_node_num,
      const BankHtreeSizing & bank_htree_sz);
  powerDef compute_power_dataout_ver(
      double len_htree_seg, 
      int    htree_node_num,
      const BankHtreeSizing & bank_htree_sz);
};


class ArrayEdgeToBankEdgeHtreeSizing
{
 public:
  ArrayEdgeToBankEdgeHtreeSizing();
  void compute_widths(bool is_dram);
  pair<double, double> compute_delays(double inrisetime, double length, double horizontal_htree_input_load, bool is_dram, powerDef & power) const;  // return <outrisetime, delay>
  struct repeater_solution
  {
    double lcap;
    double wcap;
    double delay;
    powerDef power;
  };

 public:
  double opt_sizing;
  double opt_segment_length;
  double width_inverter_nmos;
  double width_inverter_pmos;
};


class BankHtreeSizing
{
 public:
  BankHtreeSizing();
  void compute_widths(bool is_dram);

  struct buffer_solution
  {
    double lcap;
    double wcap;
    double delay;
    powerDef power;
  };

 public:
  double buffer_opt_sizing;
  double buffer_segment_opt_length;
  double buffer_width_n[2];
  double buffer_width_p[2];
  double nand2_buffer_width_n[2];
  double nand2_buffer_width_p[2];
  double tristate_driver_nand2_width_n;
  double tristate_driver_nand2_width_p;
  double tristate_driver_nor2_width_n;
  double tristate_driver_nor2_width_p;
};


#endif

