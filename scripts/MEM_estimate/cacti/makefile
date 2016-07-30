TARGET = cacti

.PHONY: all depend clean $(TARGET)

ifndef NTHREADS
  NTHREADS = 4
endif

ifeq ($(TAG), dbg)
  FLAGS = -m32 -ggdb -g -Wall -O0 -DNTHREADS=$(NTHREADS)
else
  FLAGS = -m32 -O3 -DNDEBUG  -DNTHREADS=$(NTHREADS)
#  FLAGS = -O3 -DNDEBUG -msse2 -mfpmath=sse 
endif

CC    = g++
CPP   = g++
LIBS  = -lm

SRCS = main.c time.c io.c technology.c basic_circuit.c 
OBJS = $(patsubst %.c,%.o,$(SRCS))
CPP_SRCS = parameter.cpp area.cpp crossbar.cpp htree.cpp decoder.cpp
CPP_OBJS = $(patsubst %.cpp,%.cc.o,$(CPP_SRCS))
PYTHONLIB_SRCS = $(patsubst main.c, ,$(SRCS)) cacti_wrap.c
PYTHONLIB_OBJS = $(patsubst %.c,%.o,$(PYTHONLIB_SRCS)) 
INCLUDES       = -I /usr/include/python2.4 -I /usr/lib/python2.4/config

all: $(TARGET)

pythonlib: $(PYTHONLIB_OBJS) $(CPP_OBJS)
	$(CC) -shared $(FLAGS) $(PYTHONLIB_OBJS) $(CPP_OBJS) -L /usr/lib/python2.4/config -lpython2.4 -o _cacti.so

$(TARGET): $(OBJS) $(CPP_OBJS)
	$(CPP) $(FLAGS) $(OBJS) $(CPP_OBJS) -o $@ $(LIBS) -pthread

cacti_wrap.o: cacti_wrap.c
	$(CC) $(FLAGS) -c $< -o $@ $(INCLUDES)

cacti_wrap.c: cacti.i
	swig -classic -python -c++ -o $@ $< 

$(OBJS): %.o: %.c
	$(CC) $(FLAGS) -c $< -o $@

$(CPP_OBJS): %.cc.o: %.cpp
	$(CPP) $(FLAGS) -c $< -o $@

#%.o: %.c %.cpp
#	$(CC) $(FLAGS) -c $< -o $@

clean:
	rm -rf *.o _cacti.so cacti.py $(TARGET) cacti_wrap.c

depend:
	makedepend -p"./" -f makefile $(SRCS)


# DO NOT DELETE

