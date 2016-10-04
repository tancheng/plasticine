
#tell the linker the rpath so that we don't have to muck with LD_LIBRARY_PATH, etc
#	$(CXX) -g -o dramsim_test dramsim_test.cpp -I../ -L../ -ldramsim -Wl,-rpath=../
dramsim_test: dramsim_test.cpp
	$(CXX) -g -o dramsim_test dramsim_test.cpp -I../ -L../ -ldramsim -Wl,-rpath=../
clean:
	rm dramsim_test
