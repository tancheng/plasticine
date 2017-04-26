#include "FringeContext.h"
#include "PrettyPrintMacros.h"

int main(int argc, char *argv[]) {
  ASSERT(argc >= 2, "Usage: %s <arraySize>\n", argv[0]);
  int N = atoi(argv[1]);

  // Create an execution context.
  FringeContext *c1 = new FringeContext("./accel.bit.bin");
  c1->load();
//  time_t tstart = time(0);
  printf("%s", KGRN);

  // Malloc -> memcpy -> memcpy -> read out
  uint32_t *hostbuf = (uint32_t*) malloc (N*sizeof(int));
  uint32_t *hostbuf2 = (uint32_t*) malloc (N*sizeof(int));

  for (int i = 0; i<N; i++) {
    hostbuf[i] = i;
    hostbuf2[i] = 0;
  }

  uint64_t deviceBuf = c1->malloc(N*sizeof(int));
  c1->memcpy(deviceBuf, hostbuf, N*sizeof(int));

  c1->memcpy(hostbuf2, deviceBuf, N*sizeof(int));

  bool fail = false;
  for (int i = 0; i<N; i++) {
    if (hostbuf2[i] != hostbuf[i]) {
      fail = true;
      EPRINTF("ERROR: Mismatch at %d: Expected %u, found %u\n", i, hostbuf[i], hostbuf2[i]);
    }
  }

  if (fail) {
    printf("Test FAILED\n");
  } else {
    printf("Test PASSED\n");
  }

//  c1->setArg(0, 2);
//  c1->setArg(1, 48);
//  c1->setArg(2, 5);
//  c1->run();
//  uint32_t out = c1->getArg(0);

//  std::cout << "out = " << out << std::endl;

  printf("%s", KNRM);
  delete c1;
  return 0;
}

