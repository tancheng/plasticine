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
#ifndef __AREA_H__
#define __AREA_H__

#include "cacti_interface.h"
#include "basic_circuit.h"
#include "parameter.h"
#include <list>

using namespace std;

class Dout_htree_node;
class Decoder;
class PredecoderBlock;
class PredecoderBlockDriver;
class Driver;
class AddrDatainHtreeNode;
class DataoutHtreeNode;
class AddrDatainHtreeAtMatInterval;
class BankHtreeSizing;
class ArrayEdgeToBankEdgeHtreeSizing;


class Area
{
 public:
  double w;
  double h;

  Area():w(0), h(0), area(0) { }
  double get_w() const    { return w; }
  double get_h() const    { return h; }
  double get_area() const
  {
    if (w == 0 && h == 0)
    {
      return area;
    }
    else
    {
      return w*h; 
    }
  }
  void set_w(double w_) { w = w_; }
  void set_h(double h_) { h = h_; }
  void set_area(double a_) { area = a_; }

  Area set_area_fa_subarray(int tag_bits, int num_r_subarray);
  void set_subarraymem_area(int num_r_subarray, int num_c_subarray, const Area & cell, uint32_t ram_cell_tech_type);

  static Area gatearea(int gatetype, int numberofinputs, double widthpmos, double widthnmos, double height_gate);
  static Area bit_mux_sense_amp_precharge_sa_mux_write_driver_write_mux_area(
    int number_cols_subarray,
    int deg_bitline_muxing,
    int Ndsam_lev_1,
    int Ndsam_lev_2,
    double subarray_mem_cell_area_width,
    int RWP,
    int ERP,
    int EWP,
    const Area & cell,
    bool is_dram_);
  static Area subarray_output_driver_area(
    int number_cols_subarray,
    int deg_bitline_muxing,
    int Ndsam_lev_1,
    int Ndsam_lev_2,
    double subarray_mem_cell_area_width,
    const Dout_htree_node & dout_htree_node);

  static Area area_mat(
    bool is_fa,
    bool is_tag,
    int tagbits,
    int num_rows_subarray,
    int num_cols_subarray, 
    int num_subarrays,
    int deg_bitline_muxing, 
    int deg_senseamp_muxing_non_associativity, 
    int Ndsam_lev_1,
    int Ndsam_lev_2,
    int number_addr_bits_mat,
    int number_datain_bits_mat,
    int number_dataout_bits_mat,
    int number_way_select_signals_mat,
    PredecoderBlock & row_predec_blk_1,
    PredecoderBlock & row_predec_blk_2, 
    PredecoderBlock & bit_mux_predec_blk_1,
    PredecoderBlock & bit_mux_predec_blk_2, 
    PredecoderBlock & senseamp_mux_lev_1_predec_blk_1, 
    PredecoderBlock & senseamp_mux_lev_1_predec_blk_2,
    PredecoderBlock & senseamp_mux_lev_2_predec_blk_1, 
    PredecoderBlock & senseamp_mux_lev_2_predec_blk_2,
    PredecoderBlock & dummy_way_select_predec_blk_1,
    Decoder & row_dec,
    Decoder & bit_mux_dec,
    Decoder & senseamp_mux_lev_1_dec,
    Decoder & senseamp_mux_lev_2_dec,
    PredecoderBlockDriver & row_predec_blk_driver_1, 
    PredecoderBlockDriver & row_predec_blk_driver_2,
    PredecoderBlockDriver & bit_mux_predec_blk_driver_1,
    PredecoderBlockDriver & bit_mux_predec_blk_driver_2,
    PredecoderBlockDriver & senseamp_mux_lev_1_predec_blk_driver_1,
    PredecoderBlockDriver & senseamp_mux_lev_1_predec_blk_driver_2, 
    PredecoderBlockDriver & senseamp_mux_lev_2_predec_blk_driver_1,
    PredecoderBlockDriver & senseamp_mux_lev_2_predec_blk_driver_2, 
    PredecoderBlockDriver & way_select_driver_1, 
    const Dout_htree_node & subarray_output_htree_node,
    const Area & cell,
    bool is_dram,
    uint32_t ram_cell_tech_type);

  static Area area_single_bank(
    int number_rows_subarray,
    bool is_tag,
    int number_horizontal_htree_nodes,
    int number_vertical_htree_nodes,
    int number_tristate_horizontal_htree_nodes,
    int number_mats_horizontal_direction,
    int number_mats_vertical_direction,
    int number_activated_mats_horizontal_direction,
    int number_addr_bits_mat,
    int number_way_select_signals_mat,
    int tagbits, 
    int number_datain_bits_mat,
    int number_dataout_bits_mat,
    int number_datain_bits_subbank,
    int number_dataout_bits_subbank,
    const AddrDatainHtreeNode & hor_addr_di_htree_node,
    const AddrDatainHtreeNode & ver_addr_di_htree_node,
    const DataoutHtreeNode    & do_htree_node,
    const AddrDatainHtreeAtMatInterval & hor_addr_di_htree_at_mat_interval,
    const AddrDatainHtreeAtMatInterval & ver_addr_di_htree_at_mat_interval,
    const BankHtreeSizing & bank_htree_sizing,
    const PredecoderBlock& row_predec_blk_1,
    const PredecoderBlock& row_predec_blk_2, 
    const PredecoderBlock& bit_mux_predec_blk_1,
    const PredecoderBlock& bit_mux_predec_blk_2, 
    const PredecoderBlock& senseamp_mux_lev_1_predec_blk_1,
    const PredecoderBlock& senseamp_mux_lev_1_predec_blk_2,
    const PredecoderBlock& senseamp_mux_lev_2_predec_blk_1,
    const PredecoderBlock& senseamp_mux_lev_2_predec_blk_2,
    const Decoder &row_dec,
    const Decoder &bit_mux_dec,
    const Decoder &senseamp_mux_lev_1_dec,
    const Decoder &senseamp_mux_lev_2_dec,
    const PredecoderBlockDriver& row_predec_blk_driver_1, 
    const PredecoderBlockDriver& row_predec_blk_driver_2,
    const PredecoderBlockDriver& bit_mux_predec_blk_driver_1,
    const PredecoderBlockDriver& bit_mux_predec_blk_driver_2,
    const PredecoderBlockDriver& senseamp_mux_lev_1_predec_blk_driver_1,
    const PredecoderBlockDriver& senseamp_mux_lev_1_predec_blk_driver_2,
    const PredecoderBlockDriver& senseamp_mux_lev_2_predec_blk_driver_1,
    const PredecoderBlockDriver& senseamp_mux_lev_2_predec_blk_driver_2,
    powerDef *tot_power,
    powerDef * tot_power_row_predecode_block_drivers,
    powerDef *tot_power_bit_mux_predecode_block_drivers,
    powerDef *tot_power_senseamp_mux_lev_1_predecode_block_drivers,
    powerDef *tot_power_senseamp_mux_lev_2_predecode_block_drivers,
    powerDef *tot_power_row_predecode_blocks,
    powerDef *tot_power_bit_mux_predecode_blocks,
    powerDef *tot_power_senseamp_mux_lev_1_predecode_blocks,
    powerDef *tot_power_senseamp_mux_lev_2_predecode_blocks,
    powerDef *tot_power_row_decoders,
    powerDef *tot_power_bit_mux_decoders,
    powerDef *tot_power_senseamp_mux_lev_1_decoders,
    powerDef *tot_power_senseamp_mux_lev_2_decoders,
    const Area & area_mat);

  static Area area_all_banks(
    int     number_banks,
    double  bank_height,
    double  bank_width, 
    int     number_bits_routed_to_bank,
    double *length_htree_route_to_bank,
    int     number_mats_vertical_direction,
    int     is_main_mem);


 private:
  double area;

  static double width_diffusion(int stackedinputs, int number_folded_transistors);
  static double width_transistor_after_folding(double input_width, double threshold_folding_width);
  static Area area_sense_amplifier(
    double width_latch_pmos,
    double width_latch_nmos,
    double width_enable,
    double width_iso,
    double pitch_sense_amp);
  static double area_write_driver_or_write_mux(bool is_dram_);
  static Area area_comparators(int tagbits, int number_ways_in_mat, double subarray_mem_cell_area_width);
};

#endif
