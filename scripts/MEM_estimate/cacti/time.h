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

#ifndef __TIME_H__
#define __TIME_H__

#include "area.h"

typedef struct {
  int subbanks;
  double access_time,cycle_time;
  double senseext_scale;
  powerDef total_power;
  int best_Ndwl,best_Ndbl, best_data_deg_bitline_muxing;
  int best_Ndsam_lev_1, best_Ndsam_lev_2;
  double max_leakage_power, max_access_time, max_cycle_time, max_dynamic_power, max_dynamic_energy;
  double min_leakage_power, min_access_time, min_cycle_time, min_dynamic_power, min_dynamic_energy;
  double best_Nspd;
  int best_Ntwl,best_Ntbl, best_tag_deg_bitline_muxing;
  int best_Ntsam_lev_1, best_Ntsam_lev_2;
  double best_Ntspd;
  int best_muxover;
  powerDef total_routing_power;
  powerDef total_power_without_routing, total_power_allbanks;
  double subbank_address_routing_delay;
  powerDef subbank_address_routing_power;
  double decoder_delay_data,decoder_delay_tag;
  powerDef decoder_power_data,decoder_power_tag;
  double dec_data_driver,dec_data_3to8,dec_data_inv;
  double dec_tag_driver,dec_tag_3to8,dec_tag_inv;
  double wordline_delay_data,wordline_delay_tag;
  powerDef wordline_power_data,wordline_power_tag;
  double bitline_delay_data,bitline_delay_tag;
  powerDef bitline_power_data,bitline_power_tag;
  double sense_amp_delay_data,sense_amp_delay_tag;
  powerDef sense_amp_power_data,sense_amp_power_tag;
  double total_out_driver_delay_data;
  powerDef total_out_driver_power_data;
  double compare_part_delay;
  double drive_mux_delay;
  double selb_delay;
  powerDef compare_part_power, drive_mux_power, selb_power;
  double data_output_delay;
  powerDef data_output_power;
  double drive_valid_delay;
  powerDef drive_valid_power;
  double precharge_delay;
  int data_nor_inputs;
  int tag_nor_inputs;
} result_type;


struct mem_array
{
  int    Ndwl;
  int    Ndbl;
  double Nspd;
  int    deg_bitline_muxing;
  int    Ndsam_lev_1;
  int    Ndsam_lev_2;
  double access_time;
  double cycle_time;
  double multisubbank_interleave_cycle_time;
  double area_ram_cells;
  double area;
  powerDef power;
  double delay_senseamp_mux_decoder;
  double delay_before_subarray_output_driver;
  double delay_from_subarray_output_driver_to_output;
  double height;
  double width;

  static bool lt(const mem_array * m1, const mem_array * m2)
  {
    if (m1->Nspd < m2->Nspd) return true;
    else if (m1->Nspd > m2->Nspd) return false;
    else if (m1->Ndwl < m2->Ndwl) return true;
    else if (m1->Ndwl > m2->Ndwl) return false;
    else if (m1->Ndbl < m2->Ndbl) return true;
    else if (m1->Ndbl > m2->Ndbl) return false;
    else if (m1->deg_bitline_muxing < m2->deg_bitline_muxing) return true;
    else if (m1->deg_bitline_muxing > m2->deg_bitline_muxing) return false;
    else if (m1->Ndsam_lev_1 < m2->Ndsam_lev_1) return true;
    else if (m1->Ndsam_lev_1 > m2->Ndsam_lev_1) return false;
    else if (m1->Ndsam_lev_2 < m2->Ndsam_lev_2) return true;
    else return false;
  }

};


typedef struct {
  int    tag_array_index;
  int    data_array_index;
  list<mem_array *>::iterator tag_array_iter;
  list<mem_array *>::iterator data_array_iter;
  double access_time;
  double cycle_time;
  double area;
  double efficiency;
  powerDef total_power;
} solution;


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
    bool is_main_mem);


void do_it(final_results *fin_res);
void init_tech_params(double tech, bool is_tag);


struct calc_time_mt_wrapper_struct
{
  uint32_t tid;
  bool     is_tag;
  bool     pure_ram;
  bool     is_main_mem;
  double   Nspd_min;

  list<mem_array *> data_arr;
  list<mem_array *> tag_arr;
  const ArrayEdgeToBankEdgeHtreeSizing * ptr_arr_edge_to_bank_edge_htree_sizing;
  const BankHtreeSizing * ptr_bank_htree_sizing;
};

void *calc_time_mt_wrapper(void * void_obj);

#endif
