PARAM_FILE=param.tcl

include Makefile.common

# [raghu] Perhaps a better way to do this is described here:
# http://stackoverflow.com/questions/10858261/abort-makefile-if-variable-not-set
ifndef TOP_MODULE
$(error TOP_MODULE is not set)
endif

all: pnr

pnr: synth
	@echo "Running IC Compiler"
	icc_shell -f scripts/icc.tcl | tee icc.log

synth: $(PARAM_FILE)
	@echo "Running Design Compiler"
	dc_shell-t -f scripts/dc.tcl | tee dc.log

$(PARAM_FILE):
	bash scripts/genParam.sh $(PWD) $(TOP_MODULE)

clean:
	rm -f *.log
	rm -f *.svf
	rm -f *.vcd
	rm -f $(TOP_MODULE)sim
	rm -rf $(TOP_MODULE)
	rm -f *.txt
	rm -rf log
	rm -rf alib-52
	rm -rf icc_reports
	rm -rf dc_reports
	rm -rf pt_reports
	rm -rf pre_pr_gatelevel_netlist
	rm -rf post_pr_gatelevel_netlist
	rm -rf work
	rm -rf legalizer_debug_plots net.acts  pna_output

distclean: clean
	rm -f $(PARAM_FILE)
