CC=g++
INCLUDES=-I../ -I/csrc/

SOURCES=$(wildcard *.cpp)
OBJECTS=$(SOURCES:.cpp=.o)

EXE := $(shell grep -o "public mod_t" *.h -r | cut -f1 -d'.')

all: $(EXE)

$(EXE): $(OBJECTS)
#	$(CC) -o $@ $(OBJECTS)
	$(CC) -g -o $@ $(OBJECTS) -I../ -L../ -ldramsim -Wl,-rpath=../
#	$(CC) -o $@ $(OBJECTS) -I../ -L../ -ldramsim -Wl

%.o: %.cpp
	$(CC) -g -c -o $@  $(INCLUDES) $<

clean:
	rm -f $(OBJECTS) $(EXE) *.txt
	rm dramsim.log
	rm dramsim.[0-9].log

nuke: clean
	rm -f *.h *.cpp
	cp gold/*.cpp .
	cp gold/*.h .

