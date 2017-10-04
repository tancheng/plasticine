
SOURCES := $(wildcard *.dot)
PDF_OBJECTS=$(SOURCES:.dot=.gv)
GV_OBJECTS=$(SOURCES:.dot=.pdf)

all: Top.pdf

Top.pdf: $(GV_OBJECTS) $(PDF_OBJECTS)
	pdfunite $(shell ls *.pdf | sort -n) $@

%.gv: %.dot
	fdp -Tdot $< -o $@

%.pdf: %.gv
	neato -n2 -Tpdf $< -o $@

clean:
	rm -f *.gv *.pdf

