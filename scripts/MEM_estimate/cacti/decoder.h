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
#ifndef __DECODER_H__
#define __DECODER_H__

#include "area.h"
#include "parameter.h"
#include <vector>

using namespace std;


class Decoder
{
 public:
  Decoder(int _num_dec_signals, bool _flag_way_select, double _C_ld_dec_out, double _R_wire_dec_out);

  bool   exist;
  int    num_in_signals;
  double C_ld_dec_out;
  double R_wire_dec_out;
  int    num_gates;
  int    num_gates_min;
  double w_dec_n[MAX_NUMBER_GATES_STAGE];
  double w_dec_p[MAX_NUMBER_GATES_STAGE];
  double delay;
  powerDef power;

  Area area;

  void compute_widths(bool fully_assoc, bool is_dram_, bool is_wl_tr_);
  void compute_area(const Area & cell, bool is_dram_);
  double compute_delays(
      double inrisetime,
      const  Area & cell,
      bool   is_dram,
      bool   is_wl_tr);  // return outrisetime
};


class PredecoderBlock
{
 public:
  PredecoderBlock();

  bool exist;
  int number_input_addr_bits;
  double c_load_predecoder_block_output;
  double r_wire_predecoder_block_output;
  int branch_effort_nand2_gate_output;
  int branch_effort_nand3_gate_output;
  int flag_two_unique_paths;
  int flag_L2_gate;
  int number_inputs_L1_gate;
  int number_gates_L1_nand2_path;
  int number_gates_L1_nand3_path;
  int number_gates_L2;
  int min_number_gates_L1;
  int min_number_gates_L2;
  int number_L1_parallel_instances_nand2;
  int number_L1_parallel_instances_nand3;
  int number_L2_parallel_instances;
  double width_L1_nand2_path_n[MAX_NUMBER_GATES_STAGE];
  double width_L1_nand2_path_p[MAX_NUMBER_GATES_STAGE];
  double width_L1_nand3_path_n[MAX_NUMBER_GATES_STAGE];
  double width_L1_nand3_path_p[MAX_NUMBER_GATES_STAGE];
  double width_L2_n[MAX_NUMBER_GATES_STAGE];
  double width_L2_p[MAX_NUMBER_GATES_STAGE];
  double delay_nand2_path;
  double delay_nand3_path;
  powerDef power_nand2_path;
  powerDef power_nand3_path;
  powerDef power_L2;

  Area area;
  bool is_dram_;

  void compute_widths();
  static void initialize(
      int num_dec_signals,
      PredecoderBlock & blk1,
      PredecoderBlock & blk2,
      const Decoder & dec,
      double C_wire_predec_blk_out,
      double R_wire_predec_blk_out,
      int    num_dec_gates_driven_per_predec_out,
      bool   is_dram_);
  void compute_area();
  pair<double, double> compute_delays(pair<double, double> inrisetime); // <nand2, nand3>
  // return <outrise_nand2, outrise_nand3>
};


class PredecoderBlockDriver
{
 public:
  PredecoderBlockDriver();

  int flag_driver_exists;
  int flag_driving_decoder_output;
  int number_input_addr_bits;
  int number_gates_nand2_path;
  int number_gates_nand3_path;
  int min_number_gates;
  int number_parallel_instances_driving_1_nand2_load;
  int number_parallel_instances_driving_2_nand2_load;
  int number_parallel_instances_driving_4_nand2_load;
  int number_parallel_instances_driving_2_nand3_load;
  int number_parallel_instances_driving_8_nand3_load;
  int number_parallel_instances_nand3_path;
  double c_load_nand2_path_predecode_block_driver_output;
  double c_load_nand3_path_predecode_block_driver_output;
  double r_load_nand2_path_predecode_block_driver_output;
  double r_load_nand3_path_predecode_block_driver_output;
  double width_nand2_path_n[MAX_NUMBER_GATES_STAGE];
  double width_nand2_path_p[MAX_NUMBER_GATES_STAGE];
  double width_nand3_path_n[MAX_NUMBER_GATES_STAGE];
  double width_nand3_path_p[MAX_NUMBER_GATES_STAGE];
  double delay_nand2_path;
  double delay_nand3_path;
  powerDef power_nand2_path;
  powerDef power_nand3_path;

  Area area;
  bool is_dram_;

  void compute_widths(
      const PredecoderBlock & predec_blk,
      const Decoder &ptr_dec,
      int way_select);
  static void initialize(
      int num_dec_signals,
      int flag_way_select,
      int way_select,
      PredecoderBlockDriver & blk_drv1,
      PredecoderBlockDriver & blk_drv2,
      const PredecoderBlock & blk1,
      const PredecoderBlock & blk2,
      const Decoder & dec,
      bool is_dram_);
  void compute_area();
  pair<double, double> compute_delays(
      double inrisetime_nand2_path,
      double inrisetime_nand3_path);  // return <outrise_nand2, outrise_nand3>

  inline int num_addr_bits_nand2_path()
  {
    return number_parallel_instances_driving_1_nand2_load +
           number_parallel_instances_driving_2_nand2_load +
           number_parallel_instances_driving_4_nand2_load;
  }
  inline int num_addr_bits_nand3_path()
  {
    return number_parallel_instances_driving_2_nand3_load +
           number_parallel_instances_driving_8_nand3_load;
  }
  double get_readOp_dynamic_power(int num_act_mats_hor_dir);
};


class Driver
{
 public:
  Driver();

  int    number_gates;
  int    min_number_gates;
  double width_n[MAX_NUMBER_GATES_STAGE];
  double width_p[MAX_NUMBER_GATES_STAGE];
  double c_gate_load;
  double c_wire_load;
  double r_wire_load;
  double delay;
  powerDef power;

  void   compute_widths(bool is_dram_);
  double compute_delay(
      double inrisetime,
      bool is_dram_);
};


#endif
