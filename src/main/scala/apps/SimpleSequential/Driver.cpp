#include "FringeContext.h"
#include "PrettyPrintMacros.h"

int main(int argc, char *argv[]) {
  // Create an execution context.
  FringeContext *c1 = new FringeContext("./accel.bit.bin");
  c1->load();
//  time_t tstart = time(0);
  printf("%s", KGRN);
  c1->setArg(0, 2);
  c1->setArg(1, 48);
  c1->setArg(2, 5);
  c1->run();
  uint32_t out = c1->getArg(0);

  std::cout << "out = " << out << std::endl;

  printf("%s", KNRM);
  c1->setArg(0, 2);
  delete c1;
  return 0;
}

