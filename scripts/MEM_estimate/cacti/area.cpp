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
#include "htree.h"
#include "decoder.h"
#include "parameter.h"
#include "basic_circuit.h"
#include <iostream>
#include <math.h>
#include <assert.h>

using namespace std;


// returns "cam_cell" object
Area Area::set_area_fa_subarray(
    int tag_bits,
    int num_r_subarray)
{
  Area cam_cell;

  double CAM2x2_h_1p = 61 * g_ip.F_sz_um;  // 48.8um in 0.8um process
  double CAM2x2_w_1p = 56 * g_ip.F_sz_um;  // 44.8um in 0.8um process
  double fa_h_incr_per_first_rw_or_wr_port_ = 20 * g_ip.F_sz_um;
  double fa_h_incr_per_later_rw_or_wr_port_ = 20 * g_ip.F_sz_um;
  double fa_h_incr_per_first_rd_port_       = 15 * g_ip.F_sz_um;
  double fa_h_incr_per_later_rd_port_       = 15 * g_ip.F_sz_um;
  double fa_w_incr_per_first_rw_or_wr_port_ = 20 * g_ip.F_sz_um;
  double fa_w_incr_per_later_rw_or_wr_port_ = 12 * g_ip.F_sz_um;
  double fa_w_incr_per_first_rd_port_       = 15 * g_ip.F_sz_um;
  double fa_w_incr_per_later_rd_port_       = 12 * g_ip.F_sz_um;
  double w_contact_        = 2 * g_ip.F_sz_um;
  double overhead_w;
  double overhead_h;

  int h_tag_bits     = (tag_bits + 1)/2;
  const uint32_t & RWP    = g_ip.num_rw_ports;
  const uint32_t & ERP    = g_ip.num_rd_ports;
  const uint32_t & EWP    = g_ip.num_wr_ports;
  //const uint32_t & NSER   = g_ip.num_se_rd_ports;

  if (RWP == 1 && ERP == 0 && EWP == 0)
  {
    overhead_w = 0;
    overhead_h = 0;
  }
  else if (RWP == 1 && ERP == 1 && EWP == 0)
  {
    overhead_w = fa_w_incr_per_first_rd_port_;
    overhead_h = fa_h_incr_per_first_rd_port_;
  }
  else if (RWP == 1 && ERP == 0 && EWP == 1)
  {
    overhead_w = fa_w_incr_per_first_rw_or_wr_port_;
    overhead_h = fa_h_incr_per_first_rw_or_wr_port_;
  }
  else if (RWP + EWP >= 2)
  {
    overhead_w = fa_w_incr_per_first_rw_or_wr_port_ + (RWP + EWP - 2)*fa_w_incr_per_later_rw_or_wr_port_ +
                 ERP*fa_w_incr_per_later_rd_port_;
    overhead_h = fa_h_incr_per_first_rw_or_wr_port_ + (RWP + EWP - 2)*fa_h_incr_per_later_rw_or_wr_port_ +
                 ERP*fa_h_incr_per_later_rd_port_;
  }
  else if (RWP == 0 && EWP == 0)
  {
    overhead_w = fa_w_incr_per_first_rd_port_ + (ERP - 1)*fa_w_incr_per_later_rd_port_;
    overhead_h = fa_h_incr_per_first_rd_port_ + (ERP - 1)*fa_h_incr_per_later_rd_port_;
  }
  else if (RWP == 0 && EWP == 1)
  {
    overhead_w = ERP * fa_w_incr_per_later_rd_port_;
    overhead_h = ERP * fa_h_incr_per_later_rd_port_;
  }
  else if (RWP == 1 && EWP == 0)
  {
    overhead_w = ERP * fa_w_incr_per_later_rd_port_;
    overhead_h = ERP * fa_h_incr_per_later_rd_port_;
  }
  else
  {
    std::cout << "unsupported combination of RWP, ERP, and EWP" << std::endl;
    exit(1);
  }

  h = (CAM2x2_h_1p + 2*overhead_h) * ((num_r_subarray + 1)/2);
  w = 2*(h_tag_bits * ((CAM2x2_w_1p + 2*overhead_w) - w_contact_)) +
      floor(h_tag_bits / sram_num_cells_wl_stitching_)*g_tp.ram_wl_stitching_overhead_;
      // following line is commented out in the latest version of CACTI 5
      //+ (fa_row_NAND_w_ + fa_row_NOR_inv_w_)*(RWP + ERP + EWP);

  cam_cell.h = (CAM2x2_h_1p + 2*overhead_h) / 2.0;
  cam_cell.w = (CAM2x2_w_1p + 2*overhead_w - w_contact_) / 2.0;

  return cam_cell;
}


void Area::set_subarraymem_area(
    int num_r_subarray,
    int num_c_subarray,
    const Area & cell,
    uint32_t ram_cell_tech_type) 
{
  uint32_t ram_num_cells_wl_stitching = 
    (ram_cell_tech_type == lp_dram) ? dram_num_cells_wl_stitching_ :
    ((ram_cell_tech_type == comm_dram) ? comm_dram_num_cells_wl_stitching_ : sram_num_cells_wl_stitching_);

  h = cell.h * num_r_subarray;
  w = cell.w * num_c_subarray + 
      floor(num_c_subarray / ram_num_cells_wl_stitching) * g_tp.ram_wl_stitching_overhead_ +
      (add_ecc_b_ ? cell.w * ceil(num_c_subarray / num_bits_per_ecc_b_) : 0);
}


Area Area::bit_mux_sense_amp_precharge_sa_mux_write_driver_write_mux_area(
    int number_cols_subarray,
    int deg_bitline_muxing,
    int Ndsam_lev_1,
    int Ndsam_lev_2,
    double subarray_mem_cell_area_width,
    int RWP,
    int ERP,
    int EWP,
    const Area & cell,
    bool is_dram)
{
  int number_sense_amps_subarray;
  double column_mux_transistor_height, sense_amp_mux_height, precharge_circuitry_height,
         height_bit_mux_decode_output_wires, height_senseamp_mux_decode_output_wires;
  double width_write_driver_or_write_mux, height_write_driver_write_mux;
  double cumulative_height, pmos_height, nmos_height, pmos_to_nmos_sizing_r;
  Area sense_amp_area, bit_mux_sense_amp_precharge_sa_mux;
  
  cumulative_height = 0;
  precharge_circuitry_height = 0;
  column_mux_transistor_height = 0;
  height_bit_mux_decode_output_wires = 0;
  sense_amp_mux_height = 0;
  height_senseamp_mux_decode_output_wires = 0;
  pmos_height = 0;
  nmos_height = 0;
  pmos_to_nmos_sizing_r = pmos_to_nmos_sz_ratio(is_dram);

  precharge_circuitry_height =
    width_transistor_after_folding(g_tp.w_pmos_bl_precharge, cell.w / (2 *(RWP + ERP))) + 
    width_transistor_after_folding(g_tp.w_pmos_bl_eq, cell.w / (RWP + ERP));
  cumulative_height += precharge_circuitry_height;

  if (deg_bitline_muxing > 1)
  {
    column_mux_transistor_height = width_transistor_after_folding(g_tp.w_nmos_b_mux, cell.w / (2 *(RWP + ERP)));
    //height_bit_mux_decode_output_wires = deg_bitline_muxing * g_tp.wire_inside_mat.pitch * (RWP + ERP);
    cumulative_height += column_mux_transistor_height + height_bit_mux_decode_output_wires;
  }

  number_sense_amps_subarray = number_cols_subarray / deg_bitline_muxing;
  sense_amp_area = 
    area_sense_amplifier(g_tp.w_sense_p, g_tp.w_sense_n, g_tp.w_sense_en, g_tp.w_iso, cell.w * deg_bitline_muxing / (RWP + ERP));
  cumulative_height += sense_amp_area.h;

  if (Ndsam_lev_1 > 1)
  {
    sense_amp_mux_height = width_transistor_after_folding(
        g_tp.w_nmos_sa_mux, cell.w * deg_bitline_muxing / (RWP + ERP));
    //height_senseamp_mux_decode_output_wires =  Ndsam * wire_inside_mat_pitch * (RWP + ERP);
    cumulative_height += sense_amp_mux_height;
  }
  if (Ndsam_lev_2 > 1)
  {
    sense_amp_mux_height = width_transistor_after_folding(
        g_tp.w_nmos_sa_mux, cell.w * deg_bitline_muxing * Ndsam_lev_1 / (RWP + ERP));
    //height_senseamp_mux_decode_output_wires =  Ndsam * wire_inside_mat_pitch * (RWP + ERP);
    cumulative_height += sense_amp_mux_height;
    // Also add height of inverter-buffer between the two levels (pass-transistors) of sense-amp mux
    pmos_height = 2 * width_transistor_after_folding(
        pmos_to_nmos_sizing_r * g_tp.min_w_nmos_, cell.w * Ndsam_lev_2 / (RWP + ERP));
    nmos_height = 2 * width_transistor_after_folding(
        g_tp.min_w_nmos_, cell.w * Ndsam_lev_2 / (RWP + ERP));
    cumulative_height += pmos_height + nmos_height;
  }

  if (deg_bitline_muxing * Ndsam_lev_1 * Ndsam_lev_2 > 1)
  {
    //height_write_mux_decode_output_wires = deg_bitline_muxing * Ndsam * g_tp.wire_inside_mat.pitch * (RWP + EWP);
    //cumulative_height += height_write_mux_decode_output_wires;
    width_write_driver_or_write_mux = area_write_driver_or_write_mux(is_dram);
    height_write_driver_write_mux = width_transistor_after_folding(2 * width_write_driver_or_write_mux, 
      cell.w * /*deg_bitline_muxing * */ Ndsam_lev_1 * Ndsam_lev_2 / (RWP + EWP));;
  }

  bit_mux_sense_amp_precharge_sa_mux.h = cumulative_height;
  bit_mux_sense_amp_precharge_sa_mux.w = subarray_mem_cell_area_width;
  return bit_mux_sense_amp_precharge_sa_mux;
}


// width_transistor_after_folding() returns the width of a transistor after folding.
// Inputs: Input width of the transistor, maximum width that a transistor can have before it gets folded 
// Output: Width of the folded transistor
double Area::width_transistor_after_folding(double input_width, double threshold_folding_width)
{
  double total_diff_width, width_poly, poly_to_poly_spacing_with_in_between_contact;
  int number_folded_transistors;

  if (input_width <= 0)
  {
    return 0;
  }
  number_folded_transistors = (int) (ceil(input_width / threshold_folding_width));
  poly_to_poly_spacing_with_in_between_contact = g_tp.w_poly_contact + 2 * g_tp.spacing_poly_to_contact;
  width_poly = g_ip.F_sz_um;
  total_diff_width = number_folded_transistors * width_poly +
                     (number_folded_transistors + 1) * poly_to_poly_spacing_with_in_between_contact;

  return total_diff_width;
}


// area_sense_amplifier() computes the area of a sense amplifier.
// Inputs: pointer to the sense amp data structure, pitch at which sense amp has to be laid out
// Output: Area of the sense amplifier
Area Area::area_sense_amplifier(
    double width_latch_pmos,
    double width_latch_nmos,
    double width_enable,
    double width_iso,
    double pitch_sense_amp)
{
  Area sense_amp;
  double height_pmos_transistors, height_nmos_transistors;

  //First compute the height occupied by all PMOS transistors. Calculate the height of each PMOS transistor
  //after folding.  

  height_pmos_transistors = 
    2 * width_transistor_after_folding(width_latch_pmos, pitch_sense_amp) + 
    width_transistor_after_folding(width_iso, pitch_sense_amp) +
    2 * g_tp.MIN_GAP_BET_SAME_TYPE_DIFFS;

  //Now compute the height occupied by all NMOS transistors
  height_nmos_transistors = 
    2 * width_transistor_after_folding(width_latch_nmos, pitch_sense_amp) +
    width_transistor_after_folding(width_enable, pitch_sense_amp) +
    2 * g_tp.MIN_GAP_BET_SAME_TYPE_DIFFS;

  //Calculate total height by considering gap between the p and n diffusion areas. 
  sense_amp.h = height_pmos_transistors + height_nmos_transistors + g_tp.MIN_GAP_BET_P_AND_N_DIFFS;
  sense_amp.w = pitch_sense_amp;
  return sense_amp;
}


double Area::area_write_driver_or_write_mux(bool is_dram)
{
  double resistance_sram_cell_pull_up_transistor, resistance_access_transistor,
         target_resistance_write_driver_plus_write_mux, width_write_driver_nmos,
         width_write_mux_nmos, width_write_driver_nmos_feature_size;

  //Calculate resistance of SRAM cell pull-up PMOS transistor
  resistance_sram_cell_pull_up_transistor = tr_R_on(g_tp.sram.cell_pmos_w, NCH, 1, is_dram, true);
  resistance_access_transistor = tr_R_on(g_tp.sram.cell_a_w, NCH, 1, is_dram, true);
  target_resistance_write_driver_plus_write_mux = (2 * resistance_sram_cell_pull_up_transistor -
    resistance_access_transistor) / 2;
  width_write_driver_nmos = R_to_w(target_resistance_write_driver_plus_write_mux, NCH, is_dram);
  width_write_mux_nmos    = width_write_driver_nmos;  
  width_write_driver_nmos_feature_size = width_write_driver_nmos / g_ip.F_sz_um;

  return width_write_driver_nmos;
}


double Area::width_diffusion(
    int stackedinputs,
    int number_folded_transistors)
{
  double width_poly = g_ip.F_sz_um;
  double total_diff_width = 2 * (g_tp.w_poly_contact + 2*g_tp.spacing_poly_to_contact) + stackedinputs* width_poly +
                            (stackedinputs - 1)*g_tp.spacing_poly_to_poly;

  if (number_folded_transistors > 1)
  {
    total_diff_width += (number_folded_transistors - 2)*2*(g_tp.w_poly_contact + 2*g_tp.spacing_poly_to_contact) +
                        (number_folded_transistors - 1)*stackedinputs*width_poly +
                        (number_folded_transistors - 1)*(stackedinputs - 1)*g_tp.spacing_poly_to_poly;
  }

  return total_diff_width;
}


Area Area::gatearea(
    int gatetype,
    int numberofinputs,
    double widthpmos,
    double widthnmos, 
    double height_gate)
{
  double height_transistor_region, ratio_p_to_n;
  double width_folded_pmos, width_folded_nmos;
  double poly_to_poly_spacing_with_in_between_contact;
  int    number_folded_pmos, number_folded_nmos;
  double total_ndiff_width, total_pdiff_width;
  Area gate;
  
  height_transistor_region = height_gate - 2 * g_tp.HPOWERRAIL;
  ratio_p_to_n = widthpmos / (widthpmos + widthnmos);
  width_folded_pmos = ratio_p_to_n * (height_transistor_region - g_tp.MIN_GAP_BET_P_AND_N_DIFFS);
  width_folded_nmos = (1 - ratio_p_to_n) * (height_transistor_region - g_tp.MIN_GAP_BET_P_AND_N_DIFFS);
  number_folded_pmos = (int) (ceil(widthpmos / width_folded_pmos));
    number_folded_nmos = (int) (ceil(widthnmos / width_folded_nmos));
  poly_to_poly_spacing_with_in_between_contact = g_tp.w_poly_contact + 2 * g_tp.spacing_poly_to_contact;
  if((number_folded_pmos == 0) && (number_folded_nmos == 0))
  {
    return gate;
  }

  switch (gatetype)
    {
    case INV:
        total_ndiff_width = width_diffusion(1, number_folded_nmos);
        total_pdiff_width = width_diffusion(1, number_folded_pmos);
        break;

    case NOR:
        total_ndiff_width = width_diffusion(1, numberofinputs * number_folded_nmos);
        total_pdiff_width = width_diffusion(numberofinputs, number_folded_pmos);
        break;

    case NAND:
        total_ndiff_width = width_diffusion(numberofinputs, number_folded_nmos);
        total_pdiff_width =  width_diffusion(1, numberofinputs * number_folded_pmos);
        break;
    default:
         printf ("Unknown gate type: %d", gatetype);
         exit(1);
     }
  gate.w = MAX(total_ndiff_width, total_pdiff_width);
  if(width_folded_nmos > widthnmos)
  {
    //means that the height of the gate can 
    //be made smaller than the input height specified, so calculate the height of the gate.
    gate.h = widthnmos + widthpmos + g_tp.MIN_GAP_BET_P_AND_N_DIFFS + 2 * g_tp.HPOWERRAIL;
  }
  else
  {
    gate.h = height_gate;
  }
  return gate;
}


Area Area::subarray_output_driver_area(
    int number_cols_subarray,
    int deg_bitline_muxing,
    int Ndsam_lev_1,
    int Ndsam_lev_2,
    double subarray_mem_cell_area_width,
    const Dout_htree_node & dout_htree_node)
{
  int number_output_drivers_subarray, number_gates_subarray_output_driver;
  double subarray_output_driver_area;
  Area subarray_output_driver, subarray_output_driver_nand2,
       subarray_output_driver_nor2, subarray_output_driver_inv;

  number_output_drivers_subarray      = number_cols_subarray / (deg_bitline_muxing * Ndsam_lev_1 * Ndsam_lev_2);
  number_gates_subarray_output_driver = dout_htree_node.num_gates;
  subarray_output_driver = Area::gatearea(INV, 1, 
    dout_htree_node.width_p[number_gates_subarray_output_driver-1],
    dout_htree_node.width_n[number_gates_subarray_output_driver-1], g_tp.cell_h_def);
  subarray_output_driver_nand2 =  Area::gatearea(NAND, 2,
    dout_htree_node.width_p[number_gates_subarray_output_driver-2], 
    dout_htree_node.width_n[number_gates_subarray_output_driver-2], g_tp.cell_h_def);
  subarray_output_driver_nor2  =  Area::gatearea(NOR, 2, dout_htree_node.width_nor2_p, 
    dout_htree_node.width_nor2_n, g_tp.cell_h_def);
  subarray_output_driver_area = (subarray_output_driver_nand2.get_area() +
                                 subarray_output_driver_nor2.get_area() +
                                 subarray_output_driver.get_area()) * number_output_drivers_subarray;
  for (int j = number_gates_subarray_output_driver - 3; j >= 0; --j)
  {
    subarray_output_driver_inv = Area::gatearea(INV, 1, dout_htree_node.width_p[j],
      dout_htree_node.width_n[j], g_tp.cell_h_def);
    subarray_output_driver_area += subarray_output_driver_inv.get_area() * number_output_drivers_subarray;
  }

  subarray_output_driver.h = subarray_output_driver_area / subarray_mem_cell_area_width;
  subarray_output_driver.w = subarray_mem_cell_area_width;

  return subarray_output_driver;
}


Area Area::area_comparators(
    int tagbits,
    int number_ways_in_mat,
    double subarray_mem_cell_area_width)
{
  Area comparator, nand2;
  double cumulative_area = 0;

  nand2 = Area::gatearea(NAND, 2, 0, g_tp.w_comp_n, g_tp.cell_h_def); 
  cumulative_area += nand2.get_area() * number_ways_in_mat * tagbits / 4;
  comparator.w = subarray_mem_cell_area_width;
  comparator.h = cumulative_area / comparator.w;
  return comparator;
}


Area Area::area_mat(
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
    uint32_t ram_cell_tech_type)
{
  double height_horizontal_non_cell_area_within_mat, width_vertical_non_cell_area_within_mat;
  double area_mat_center_circuitry, area_rectangle_center_mat, area_row_decoder, width_row_decoder;

  double width_row_predecode_output_wires;
  double height_addr_datain_wires_within_mat, height_bit_mux_sense_amp_precharge_sa_mux;
  double area_efficiency_mat;

  Area data_mat, comparator;
  double height_bit_mux_decode_output_wires, height_senseamp_mux_decode_output_wires;
  double height_write_mux_decode_output_wires;

  height_bit_mux_decode_output_wires = 0;
  height_senseamp_mux_decode_output_wires = 0;
  height_write_mux_decode_output_wires = 0;

  int RWP = g_ip.num_rw_ports;
  int ERP = g_ip.num_rd_ports;
  int EWP = g_ip.num_wr_ports;

  Area subarray_mem_cell;
  if((is_fa) && (is_tag))
  {
    Area cam_cell = subarray_mem_cell.set_area_fa_subarray(tagbits, num_rows_subarray);
  }
  else
  {
    subarray_mem_cell.set_subarraymem_area(num_rows_subarray, num_cols_subarray, cell, ram_cell_tech_type);
  }

  row_predec_blk_driver_1.compute_area();
  row_predec_blk_driver_2.compute_area();
  bit_mux_predec_blk_driver_1.compute_area();
  bit_mux_predec_blk_driver_2.compute_area();
  senseamp_mux_lev_1_predec_blk_driver_1.compute_area();
  senseamp_mux_lev_1_predec_blk_driver_2.compute_area();
  way_select_driver_1.compute_area();
  senseamp_mux_lev_2_predec_blk_driver_1.compute_area();
  senseamp_mux_lev_2_predec_blk_driver_2.compute_area();

  row_predec_blk_1.compute_area();
  row_predec_blk_2.compute_area();
  bit_mux_predec_blk_1.compute_area();
  bit_mux_predec_blk_2.compute_area();
  senseamp_mux_lev_1_predec_blk_1.compute_area();
  senseamp_mux_lev_1_predec_blk_2.compute_area();
  senseamp_mux_lev_2_predec_blk_1.compute_area();
  senseamp_mux_lev_2_predec_blk_2.compute_area();
 
  row_dec.compute_area(cell, is_dram);
  bit_mux_dec.compute_area(cell, is_dram);
  senseamp_mux_lev_1_dec.compute_area(cell, is_dram);
  senseamp_mux_lev_2_dec.compute_area(cell, is_dram);
  area_row_decoder  = row_dec.area.get_area() * num_rows_subarray * (RWP + ERP + EWP);
  width_row_decoder = area_row_decoder / subarray_mem_cell.get_h();
   

  Area bit_mux_sense_amp_precharge_sa_mux_write_driver_write_mux = 
    Area::bit_mux_sense_amp_precharge_sa_mux_write_driver_write_mux_area(
        num_cols_subarray, deg_bitline_muxing, Ndsam_lev_1, Ndsam_lev_2,
        subarray_mem_cell.get_w(), RWP, ERP, EWP, cell, is_dram);
  height_bit_mux_sense_amp_precharge_sa_mux = bit_mux_sense_amp_precharge_sa_mux_write_driver_write_mux.h;
  Area subarray_out_drv = Area::subarray_output_driver_area(
      num_cols_subarray, deg_bitline_muxing,
      Ndsam_lev_1, Ndsam_lev_2, subarray_mem_cell.get_w(), subarray_output_htree_node);

  if(!(is_fa && is_tag))
  {
    subarray_out_drv.h = subarray_out_drv.h * (RWP + ERP);
  }
  else
  {
    subarray_out_drv.h = 0;
  }

  if((!is_fa)&&(is_tag)){
    //tagbits = (4 * num_cols_subarray / (deg_bitline_muxing * Ndsam_lev_1 * Ndsam_lev_2)) / number_dataout_bits_mat;
    comparator = Area::area_comparators(tagbits, number_dataout_bits_mat, subarray_mem_cell.get_w());
    comparator.h = comparator.h * (RWP + ERP);
  }
  
  
  if (!is_fa)
  {
    int branch_effort_predecoder_block_1_output = (int) (pow(2, row_predec_blk_2.number_input_addr_bits));
    int branch_effort_predecoder_block_2_output = (int) (pow(2, row_predec_blk_1.number_input_addr_bits));
    width_row_predecode_output_wires = (branch_effort_predecoder_block_1_output +
                                        branch_effort_predecoder_block_2_output) *
                                       g_tp.wire_inside_mat.pitch * (RWP + ERP + EWP);
  }
  else
  {
    width_row_predecode_output_wires = 0;
  }
   
  height_horizontal_non_cell_area_within_mat = 2 * (height_bit_mux_sense_amp_precharge_sa_mux + 
    subarray_out_drv.h + comparator.h);
  width_vertical_non_cell_area_within_mat = MAX(width_row_predecode_output_wires, 
    2 * width_row_decoder);

  if (deg_bitline_muxing > 1)
  {
    height_bit_mux_decode_output_wires = deg_bitline_muxing * g_tp.wire_inside_mat.pitch * (RWP + ERP);
  }
  if (Ndsam_lev_1 > 1)
  {
    height_senseamp_mux_decode_output_wires =  Ndsam_lev_1 * g_tp.wire_inside_mat.pitch * (RWP + ERP);
  }
  if (Ndsam_lev_2 > 1)
  {
    height_senseamp_mux_decode_output_wires += Ndsam_lev_2 * g_tp.wire_inside_mat.pitch * (RWP + ERP);
  }
  /*if (deg_bitline_muxing * Ndsam > 1)
  {
    height_write_mux_decode_output_wires = deg_bitline_muxing * Ndsam * g_tp.wire_inside_mat.pitch * (RWP + EWP);
  }*/
  
  if (!g_ip.ver_htree_wires_over_array)
  {
    height_addr_datain_wires_within_mat = (number_addr_bits_mat + number_way_select_signals_mat +
                                           number_datain_bits_mat/2 + number_dataout_bits_mat/2) *
                                          g_tp.wire_inside_mat.pitch * (RWP + ERP + EWP);
    //height_horizontal_non_cell_area_within_mat = 2 * height_bit_mux_sense_amp_precharge_sa_mux +
      //MAX(height_addr_datain_wires_within_mat, 2 *  subarray_out_drv.h);
    height_horizontal_non_cell_area_within_mat = 
      2*height_bit_mux_sense_amp_precharge_sa_mux + height_addr_datain_wires_within_mat +
      height_bit_mux_decode_output_wires + height_senseamp_mux_decode_output_wires +
      height_write_mux_decode_output_wires + 2*subarray_out_drv.h;
  }

  area_rectangle_center_mat = height_horizontal_non_cell_area_within_mat *
                              width_vertical_non_cell_area_within_mat;
  area_mat_center_circuitry = (row_predec_blk_driver_1.area.get_area() + 
                               bit_mux_predec_blk_driver_1.area.get_area() +
                               senseamp_mux_lev_1_predec_blk_driver_1.area.get_area() +
                               senseamp_mux_lev_2_predec_blk_driver_1.area.get_area() +
                               way_select_driver_1.area.get_area() +
                               row_predec_blk_driver_2.area.get_area() +
                               bit_mux_predec_blk_driver_2.area.get_area() +
                               senseamp_mux_lev_1_predec_blk_driver_2.area.get_area() + 
                               senseamp_mux_lev_2_predec_blk_driver_2.area.get_area() + 
                               row_predec_blk_1.area.get_area() +
                               bit_mux_predec_blk_1.area.get_area() + 
                               senseamp_mux_lev_1_predec_blk_1.area.get_area() +
                               senseamp_mux_lev_2_predec_blk_1.area.get_area() +
                               row_predec_blk_2.area.get_area() +
                               bit_mux_predec_blk_2.area.get_area() + 
                               senseamp_mux_lev_1_predec_blk_2.area.get_area() +
                               senseamp_mux_lev_2_predec_blk_2.area.get_area() +
                               bit_mux_dec.area.get_area() +
                               senseamp_mux_lev_1_dec.area.get_area() +
                               senseamp_mux_lev_2_dec.area.get_area()) * (RWP + ERP + EWP);
 
  if (!is_fa) 
  {
    data_mat.h = 2 * subarray_mem_cell.get_h() + height_horizontal_non_cell_area_within_mat;
    data_mat.w = 2 * subarray_mem_cell.get_w() + width_vertical_non_cell_area_within_mat;
    data_mat.w = (data_mat.h*data_mat.w + area_mat_center_circuitry) / data_mat.h;
    area_efficiency_mat = subarray_mem_cell.get_area() * 4 * 100 / data_mat.get_area();
  }
  else{
    data_mat.h = 2 * subarray_mem_cell.get_h() + height_horizontal_non_cell_area_within_mat;
    data_mat.w = subarray_mem_cell.get_w() + width_vertical_non_cell_area_within_mat;
    data_mat.w = (data_mat.h*data_mat.w + area_mat_center_circuitry) / data_mat.h;
    area_efficiency_mat = subarray_mem_cell.get_area() * 2 * 100 / data_mat.get_area();
  }
  return(data_mat);
}


Area Area::area_single_bank(
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
    const Area & area_mat)
{
  Area bank, area_hor_addr_di_htree_node, area_ver_addr_di_htree_node, area_do_htree_node;
  double  area_tag_comparators, area_active_horizontal_addr_datain_dataout_routing,
    area_active_vertical_addr_datain_routing, area_active_dataout_routing, 
    area_active_vertical_addr_datain_dataout_routing, width_horizontal_addr_datain_dataout_routing,
    width_vertical_addr_datain_routing, width_vertical_dataout_routing,
    height_active_area_horizontal_addr_datain_dataout_routing, 
    height_wire_area_horizontal_addr_datain_dataout_routing,  
    height_horizontal_addr_datain_dataout_routing, height_vertical_addr_datain_routing,
    height_vertical_dataout_routing, width_wire_area_vertical_addr_datain_routing,
    width_wire_area_vertical_dataout_routing, width_wire_area_vertical_addr_datain_dataout_routing,
    width_active_area_vertical_addr_datain_dataout_routing, width_vertical_addr_datain_dataout_routing;
  double asp_ratio_temp, length_htree_segment;
  int htree_seg_multiplier, i;
  int number_mats;

  int RWP  = g_ip.num_rw_ports;
  int ERP  = g_ip.num_rd_ports;
  int EWP  = g_ip.num_wr_ports;
  //int NSER = g_ip.num_se_rd_ports;


  area_tag_comparators = 0;
  area_active_horizontal_addr_datain_dataout_routing = 0;
  area_active_vertical_addr_datain_routing = 0;
  area_active_dataout_routing = 0;
  area_active_vertical_addr_datain_dataout_routing = 0;
  height_active_area_horizontal_addr_datain_dataout_routing = 0;
  height_wire_area_horizontal_addr_datain_dataout_routing = 0;
  width_horizontal_addr_datain_dataout_routing = 0;
  height_horizontal_addr_datain_dataout_routing = 0;  
  width_vertical_addr_datain_routing = 0;
  height_vertical_addr_datain_routing = 0;
  width_vertical_dataout_routing = 0;
  height_vertical_dataout_routing = 0;
  width_active_area_vertical_addr_datain_dataout_routing = 0;
  width_wire_area_vertical_addr_datain_routing = 0;
  width_wire_area_vertical_dataout_routing = 0;
  width_wire_area_vertical_addr_datain_dataout_routing = 0;
  width_vertical_addr_datain_dataout_routing = 0;

  number_mats = number_mats_horizontal_direction * number_mats_vertical_direction;
  
  length_htree_segment = pow(2, number_horizontal_htree_nodes - 1) * 
    hor_addr_di_htree_at_mat_interval.mat_dimension / 2;
  
  for(i = 0; i < number_tristate_horizontal_htree_nodes; ++i)
  {
    if (g_ip.rpters_in_htree == false)
    {
      area_hor_addr_di_htree_node = hor_addr_di_htree_node.area[i];
    }
    else
    {
      area_hor_addr_di_htree_node = hor_addr_di_htree_at_mat_interval.compute_area_addr_datain(
          length_htree_segment, i, bank_htree_sizing);
    }
    height_wire_area_horizontal_addr_datain_dataout_routing += 
      (number_addr_bits_mat + number_way_select_signals_mat) * g_tp.wire_outside_mat.pitch * (RWP + ERP + EWP) +
      (number_datain_bits_subbank  * g_tp.wire_outside_mat.pitch) * (RWP + EWP) +
      (number_dataout_bits_subbank * g_tp.wire_outside_mat.pitch) * (RWP + ERP);
    length_htree_segment /= 2;
  }

  htree_seg_multiplier = 1;
  for(i = 0; i < number_horizontal_htree_nodes - number_tristate_horizontal_htree_nodes; ++i)
  {
    if (g_ip.rpters_in_htree == false)
    {
      area_hor_addr_di_htree_node = hor_addr_di_htree_node.area[i];
    }
    else
    {
      area_hor_addr_di_htree_node = hor_addr_di_htree_at_mat_interval.compute_area_addr_datain(
          length_htree_segment, i, bank_htree_sizing);
    }
    /*area_active_horizontal_addr_datain_dataout_routing += 
      (number_addr_bits_mat + number_way_select_signals_mat) * area_hor_addr_di_htree_node.get_area() *
      htree_seg_multiplier * (RWP + ERP + EWP) + number_datain_bits_mat * number_mats_horizontal_direction * 
      area_hor_addr_di_htree_node.get_area() * (RWP + EWP) + number_dataout_bits_mat * 
      number_mats_horizontal_direction * area_hor_addr_di_htree_node.get_area() * (RWP + ERP);*/
    area_active_horizontal_addr_datain_dataout_routing = 0;
    height_wire_area_horizontal_addr_datain_dataout_routing += 
      (number_addr_bits_mat + number_way_select_signals_mat) * g_tp.wire_outside_mat.pitch * (RWP + ERP + EWP) +
      (number_datain_bits_subbank * g_tp.wire_outside_mat.pitch / htree_seg_multiplier) * (RWP + EWP) +
      (number_dataout_bits_subbank * g_tp.wire_outside_mat.pitch / htree_seg_multiplier) * (RWP + ERP);
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }
  
  
  //If tag array, add area occupied by comparators
  //if(is_tag){
    //tag_comparators = area_comparators(tagbits, parameters->tag_associativity);
    //area_tag_comparators = tag_comparators.area * (RWP + ERP);
  //}
  area_active_horizontal_addr_datain_dataout_routing += area_tag_comparators;

  width_horizontal_addr_datain_dataout_routing = number_mats_horizontal_direction * area_mat.w;
  height_active_area_horizontal_addr_datain_dataout_routing = area_active_horizontal_addr_datain_dataout_routing / 
      width_horizontal_addr_datain_dataout_routing;
  height_horizontal_addr_datain_dataout_routing = MAX(height_active_area_horizontal_addr_datain_dataout_routing,
    height_wire_area_horizontal_addr_datain_dataout_routing);

  htree_seg_multiplier = 1; 
  length_htree_segment = pow(2, number_vertical_htree_nodes - 1) * 
    ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for (i = 0; i < number_vertical_htree_nodes; ++i)
  {
    if (g_ip.rpters_in_htree == false)
    {
      area_ver_addr_di_htree_node = ver_addr_di_htree_node.area[i];
    }
    else
    {
      area_ver_addr_di_htree_node = ver_addr_di_htree_at_mat_interval.compute_area_addr_datain(
          length_htree_segment, i, bank_htree_sizing);
    }
    area_active_vertical_addr_datain_routing += ((number_addr_bits_mat + number_datain_bits_mat + 
      number_way_select_signals_mat) * number_mats_horizontal_direction *  
      htree_seg_multiplier * area_ver_addr_di_htree_node.get_area()) * (RWP + EWP);
    width_wire_area_vertical_addr_datain_routing += ((number_addr_bits_mat + number_way_select_signals_mat + 
      number_datain_bits_mat) * number_mats_horizontal_direction * g_tp.wire_outside_mat.pitch) * 
      (RWP + ERP);
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }
  if(number_mats_vertical_direction > 1){
    area_active_vertical_addr_datain_routing *= 2; 
  }
  
  width_vertical_addr_datain_routing = number_mats_horizontal_direction * area_mat.w;
  height_vertical_addr_datain_routing = area_active_vertical_addr_datain_routing / width_vertical_addr_datain_routing;

  if (!g_ip.ver_htree_wires_over_array) 
  {//vertical H-tree wires don't go over the array
    height_vertical_addr_datain_routing = 0;
  }

  htree_seg_multiplier = 1;
  length_htree_segment = pow(2, number_vertical_htree_nodes - 1) * 
    ver_addr_di_htree_at_mat_interval.mat_dimension / 2;
  for(i = 0; i < number_vertical_htree_nodes - 1; ++i){
    if (g_ip.rpters_in_htree == false)
    {
      area_do_htree_node = do_htree_node.area[i];
    }
    else
    {
      area_do_htree_node = ver_addr_di_htree_at_mat_interval.compute_area_dataout_ver(
          length_htree_segment, i, bank_htree_sizing);
    }
    area_active_dataout_routing += (number_dataout_bits_mat * number_mats_horizontal_direction * 
      htree_seg_multiplier * area_do_htree_node.get_area()) * (RWP + ERP);
    width_wire_area_vertical_dataout_routing += (number_dataout_bits_mat * 
      number_mats_horizontal_direction * g_tp.wire_outside_mat.pitch) * (RWP + ERP);
    htree_seg_multiplier *= 2;
    length_htree_segment /= 2;
  }

  if(number_mats_vertical_direction > 1)
  {
    area_active_dataout_routing *= 2; 
  }

  width_vertical_dataout_routing = number_mats_horizontal_direction * area_mat.w;
  height_vertical_dataout_routing = area_active_dataout_routing / width_vertical_dataout_routing;
  area_active_vertical_addr_datain_dataout_routing = area_active_vertical_addr_datain_routing + 
    area_active_dataout_routing;
  width_active_area_vertical_addr_datain_dataout_routing = area_active_vertical_addr_datain_dataout_routing /
    (number_mats_vertical_direction * area_mat.h);
  width_wire_area_vertical_addr_datain_dataout_routing = width_wire_area_vertical_addr_datain_routing +
    width_wire_area_vertical_dataout_routing;
  
  if (!g_ip.ver_htree_wires_over_array)
  {
    height_vertical_dataout_routing = 0;
    width_vertical_addr_datain_dataout_routing = MAX(width_active_area_vertical_addr_datain_dataout_routing,
      width_wire_area_vertical_addr_datain_dataout_routing);
  }

  bank.h = number_mats_vertical_direction * area_mat.h + height_horizontal_addr_datain_dataout_routing + 
    height_vertical_addr_datain_routing + height_vertical_dataout_routing;
  if (!g_ip.ver_htree_wires_over_array)
  {
    bank.w = number_mats_horizontal_direction * area_mat.w + width_vertical_addr_datain_dataout_routing;    
  }
  else
  {
    bank.w = number_mats_horizontal_direction * area_mat.w;
  }
  //result->totalarea = bank.get_area();
  asp_ratio_temp = bank.h / bank.w;
  //result->aspect_ratio_total =(asp_ratio_temp  > 1.0) ? (asp_ratio_temp) : 1.0 / asp_ratio_temp;

  tot_power_row_predecode_block_drivers->readOp.leakage = 
    (row_predec_blk_driver_1.power_nand2_path.readOp.leakage + 
     row_predec_blk_driver_1.power_nand3_path.readOp.leakage + 
     row_predec_blk_driver_2.power_nand2_path.readOp.leakage + 
     row_predec_blk_driver_2.power_nand3_path.readOp.leakage) * number_mats;

  tot_power_bit_mux_predecode_block_drivers->readOp.leakage =
    (bit_mux_predec_blk_driver_1.power_nand2_path.readOp.leakage + 
     bit_mux_predec_blk_driver_1.power_nand3_path.readOp.leakage +
     bit_mux_predec_blk_driver_2.power_nand2_path.readOp.leakage + 
     bit_mux_predec_blk_driver_2.power_nand3_path.readOp.leakage) * number_mats;

  tot_power_senseamp_mux_lev_1_predecode_block_drivers->readOp.leakage = 
    (senseamp_mux_lev_1_predec_blk_driver_1.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_1_predec_blk_driver_1.power_nand3_path.readOp.leakage + 
     senseamp_mux_lev_1_predec_blk_driver_2.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_1_predec_blk_driver_2.power_nand3_path.readOp.leakage) * number_mats;

  tot_power_senseamp_mux_lev_2_predecode_block_drivers->readOp.leakage = 
    (senseamp_mux_lev_2_predec_blk_driver_1.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_2_predec_blk_driver_1.power_nand3_path.readOp.leakage + 
     senseamp_mux_lev_2_predec_blk_driver_2.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_2_predec_blk_driver_2.power_nand3_path.readOp.leakage) * number_mats;

  tot_power->readOp.leakage +=
    tot_power_row_predecode_block_drivers->readOp.leakage +  
    tot_power_bit_mux_predecode_block_drivers->readOp.leakage + 
    tot_power_senseamp_mux_lev_1_predecode_block_drivers->readOp.leakage +
    tot_power_senseamp_mux_lev_2_predecode_block_drivers->readOp.leakage;

  tot_power_row_predecode_blocks->readOp.leakage =
    (row_predec_blk_1.power_nand2_path.readOp.leakage + 
     row_predec_blk_1.power_nand3_path.readOp.leakage + row_predec_blk_1.power_L2.readOp.leakage +
     row_predec_blk_2.power_nand2_path.readOp.leakage+ row_predec_blk_2.power_nand3_path.readOp.leakage + 
     row_predec_blk_2.power_L2.readOp.leakage) * number_mats;

  tot_power_bit_mux_predecode_blocks->readOp.leakage =
    (bit_mux_predec_blk_1.power_nand2_path.readOp.leakage + 
     bit_mux_predec_blk_1.power_nand3_path.readOp.leakage + bit_mux_predec_blk_1.power_L2.readOp.leakage +
     bit_mux_predec_blk_2.power_nand2_path.readOp.leakage + bit_mux_predec_blk_2.power_nand3_path.readOp.leakage + 
     bit_mux_predec_blk_2.power_L2.readOp.leakage) * number_mats;

  tot_power_senseamp_mux_lev_1_predecode_blocks->readOp.leakage =
    (senseamp_mux_lev_1_predec_blk_1.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_1_predec_blk_1.power_nand3_path.readOp.leakage +
     senseamp_mux_lev_1_predec_blk_1.power_L2.readOp.leakage +
     senseamp_mux_lev_1_predec_blk_2.power_nand2_path.readOp.leakage +
     senseamp_mux_lev_1_predec_blk_2.power_nand3_path.readOp.leakage + 
     senseamp_mux_lev_1_predec_blk_2.power_L2.readOp.leakage) * number_mats;

  tot_power_senseamp_mux_lev_2_predecode_blocks->readOp.leakage =
    (senseamp_mux_lev_2_predec_blk_1.power_nand2_path.readOp.leakage + 
     senseamp_mux_lev_2_predec_blk_1.power_nand3_path.readOp.leakage +
     senseamp_mux_lev_2_predec_blk_1.power_L2.readOp.leakage +
     senseamp_mux_lev_2_predec_blk_2.power_nand2_path.readOp.leakage +
     senseamp_mux_lev_2_predec_blk_2.power_nand3_path.readOp.leakage + 
     senseamp_mux_lev_2_predec_blk_2.power_L2.readOp.leakage) * number_mats;

  tot_power->readOp.leakage += 
    tot_power_row_predecode_blocks->readOp.leakage + 
    tot_power_bit_mux_predecode_blocks->readOp.leakage + 
    tot_power_senseamp_mux_lev_1_predecode_blocks->readOp.leakage +
    tot_power_senseamp_mux_lev_2_predecode_blocks->readOp.leakage;

  tot_power_row_decoders->readOp.leakage = row_dec.power.readOp.leakage * number_rows_subarray * 4 * number_mats;
  tot_power_bit_mux_decoders->readOp.leakage = bit_mux_dec.power.readOp.leakage * number_mats;
  tot_power_senseamp_mux_lev_1_decoders->readOp.leakage = senseamp_mux_lev_1_dec.power.readOp.leakage * number_mats;
  tot_power_senseamp_mux_lev_2_decoders->readOp.leakage = senseamp_mux_lev_2_dec.power.readOp.leakage * number_mats;

  tot_power->readOp.leakage +=
    tot_power_row_decoders->readOp.leakage +
    tot_power_bit_mux_decoders->readOp.leakage +
    tot_power_senseamp_mux_lev_1_decoders->readOp.leakage +
    tot_power_senseamp_mux_lev_2_decoders->readOp.leakage;

  return(bank);
}


Area Area::area_all_banks(
    int     number_banks,
    double  bank_height,
    double  bank_width,
    int     number_bits_routed_to_bank,
    double *length_htree_route_to_bank,
    int     number_mats_vertical_direction,
    int     is_main_mem)
{
  double curr_pitch_routed_tracks, curr_htree_vertical_seg_length, curr_htree_horizontal_seg_length;
  Area all_banks;
  int i, number_segments_htree, first_seg_vertical, number_banks_horizontal_direction,
    number_banks_vertical_direction, curr_horizontal_routed_tracks_instances, 
    curr_vertical_routed_tracks_instances, curr_seg_vertical,
    number_vertical_segments_htree, number_horizontal_segments_htree;
  
  *length_htree_route_to_bank = 0;

  double pitch_of_routed_tracks_one_bank = number_bits_routed_to_bank * g_tp.wire_outside_mat.pitch; 

  if (number_banks > 1)
  {
    number_segments_htree = _log2(number_banks);
    first_seg_vertical = (number_segments_htree%2 == 0) ? 0 : 1;

    //if(logbasefour(number_banks) == floor(logbasefour((double) (number_banks))))
    if (_log2(number_banks) != _log2(number_banks-1) &&
        _log2(number_banks)%2 == 0)
    {
      number_banks_horizontal_direction = (int)(sqrt(number_banks));
    }
    else
    {
      number_banks_horizontal_direction = (int)(sqrt(number_banks/2)) * 2;
    }
    number_banks_vertical_direction = number_banks / number_banks_horizontal_direction;
    all_banks.h = number_banks_vertical_direction * bank_height;
    all_banks.w = number_banks_horizontal_direction * bank_width;
    if(first_seg_vertical)
    {
      number_vertical_segments_htree = (int) (number_segments_htree / 2) + 1;
    }
    else
    {
      number_vertical_segments_htree = number_segments_htree / 2;
    }
    number_horizontal_segments_htree = number_segments_htree - number_vertical_segments_htree;

    if(number_mats_vertical_direction == 1)
    {
      number_vertical_segments_htree = number_vertical_segments_htree - 1;
      number_segments_htree = number_segments_htree - 1;
    }
  
    curr_pitch_routed_tracks = number_banks * pitch_of_routed_tracks_one_bank;
    curr_horizontal_routed_tracks_instances = 1;
    curr_vertical_routed_tracks_instances = 1;
    curr_seg_vertical = first_seg_vertical;
    curr_htree_vertical_seg_length = number_banks_vertical_direction * bank_height / 2;
    curr_htree_horizontal_seg_length = number_banks_horizontal_direction * bank_width / 2;
    
    for (i = 0; i < number_segments_htree; ++i)
    {
      if (curr_seg_vertical)
      {
        if (is_main_mem)
        {
          all_banks.w += number_bits_routed_to_bank *
                         curr_vertical_routed_tracks_instances *
                         g_tp.wire_outside_mat.pitch;
        }
        else
        {
          all_banks.w += curr_pitch_routed_tracks * curr_vertical_routed_tracks_instances;
        }
        *length_htree_route_to_bank += curr_htree_vertical_seg_length;
        curr_seg_vertical = 0;
        curr_htree_vertical_seg_length = curr_htree_vertical_seg_length / 2;
        curr_vertical_routed_tracks_instances = curr_vertical_routed_tracks_instances * 2;
      }
      else
      {
        if (is_main_mem)
        {
          all_banks.h += number_bits_routed_to_bank *
                         curr_horizontal_routed_tracks_instances * 
                         g_tp.wire_outside_mat.pitch;		
        }
        else
        {
          all_banks.h += curr_pitch_routed_tracks * curr_horizontal_routed_tracks_instances;
        }
        curr_seg_vertical = 1;
        *length_htree_route_to_bank += curr_htree_horizontal_seg_length;
        curr_htree_horizontal_seg_length = curr_htree_horizontal_seg_length / 2;
        curr_horizontal_routed_tracks_instances = curr_horizontal_routed_tracks_instances * 2;
      }
      curr_pitch_routed_tracks = curr_pitch_routed_tracks / 2;
    }
  }
  else
  { //number_banks == 1;
    all_banks.w = bank_width;
    all_banks.h = bank_height;
  }
  return(all_banks);
}

