CC=g++
LINK=g++
CROSS_COMPILE=

FRINGE_SRC=${PLASTICINE_HOME}/static/fringeVCS
HOST_SRC=./

SOURCES := $(wildcard ${HOST_SRC}/*.cpp)

INCLUDES +=													\
			-I${FRINGE_SRC}/hw 					 	\
			-I${FRINGE_SRC}/sw 					 	\


OBJECTS=$(SOURCES:.cpp=.o)

DEFINES=$(OBJECTS:.o=.d)

CXXFLAGS=-DPLASTICINE -D__DELITE_CPP_STANDALONE__ -D__USE_STD_STRING__ -std=c++11
LDFLAGS=-Wl,--hash-style=both -lstdc++ -pthread -lpthread -lm

all: pre-build-checks Top

pre-build-checks:
ifndef JAVA_HOME
GUESS=$(shell readlink -f $(shell dirname $(shell readlink -f `which java`))/../../)
$(warning JAVA_HOME is not set, guessing to be ${GUESS}!)
JAVA_HOME=$(GUESS)
endif
ifndef PLASTICINE_HOME
$(error PLASTICINE_HOME is not set !)
endif


Top: $(OBJECTS)
	$(CROSS_COMPILE)$(LINK) $(LDFLAGS) $^ $(LOADLIBES) $(LDLIBS) -o $@ $(LIBS) $(SC_LIBS) 2>&1 | c++filt

%.o: %.cpp
	  $(CROSS_COMPILE)$(CC) $(INCLUDES) $(CXXFLAGS) $(CPPFLAGS) $(OPT_FAST) -c -o $@  $<

## Clean up
clean:
	rm -f $(OBJECTS) $(DEFINES) *.a *.vcd *.vpd *.dat ${TOP} Top

# Set the default Makefile goal to be 'all', else it will default to executing
# the first target in ${TOP}.mk
.DEFAULT_GOAL := all
