
all:
	sbt compile

clean:
	sbt clean
	rm -rf generated

wave_%:
	@echo 'Generating waveform for $(patsubst wave_%,%,$@)'
	gtkwave -f generated/$(patsubst wave_%,%,$@)/$(patsubst wave_%Test,%,$@).vcd &
