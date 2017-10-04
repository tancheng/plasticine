all: accel.bit.bin

accel.bit.bin: vivado.mk
	make -f vivado.mk

vivado.mk:
	cp hw/build/Makefile ./$@
	cp hw/build/*.tcl .
	cp hw/build/*.v .
	cp hw/build/fsbl.elf._ ./fsbl.elf
	cp hw/build/u-boot.elf._ ./u-boot.elf

clean: vivado.mk
	make -f vivado.mk clean
