CC=g++
INCLUDES=-I../ -I/csrc/

SOURCES=$(wildcard *.cpp)
OBJECTS=$(SOURCES:.cpp=.o)

EXE := $(shell grep -o "public mod_t" *.h -r | cut -f1 -d'.')
SIM="Simulator"

all: $(SIM)

$(SIM): $(EXE)
	ln -sf $(EXE) $(SIM)

$(EXE): $(OBJECTS)
	$(CC) -o $@ $(OBJECTS)

%.o: %.cpp
	$(CC) -c -o $@  $(INCLUDES) $<


clean:
	rm -f $(OBJECTS) $(EXE) *.txt

nuke: clean
	rm -f *.h *.cpp
	cp gold/*.cpp .
	cp gold/*.h .

