#include "FringeContext.h"

int main(int argc, char *argv[]) {
  // Create an execution context.
  FringeContext *c1 = new FringeContext("./accel.bit.bin");
  c1->load();
//  time_t tstart = time(0);

  c1->setArg(2, 10);
  c1->run();
  uint32_t out = c1->getArg(0);

  std::cout << "out = " << out << std::endl;
//  time_t tend = time(0);
//  double elapsed = difftime(tend, tstart);
//  std::cout << "Kernel done, test run time = " << elapsed << " ms" << std::endl;
  // results in ()
  delete c1;
  return 0;
}

