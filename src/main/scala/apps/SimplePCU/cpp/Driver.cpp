#include "FringeContext.h"
#include "PrettyPrintMacros.h"

int main(int argc, char *argv[]) {
//  ASSERT(argc >= 2, "Usage: %s <arraySize>\n", argv[0]);
//  int N = atoi(argv[1]);

  // Create an execution context.
  FringeContext *c1 = new FringeContext("./accel.bit.bin");
  c1->load();

//  printf("%s", KGRN);
//
//  // Host buffers
//  uint32_t *A = (uint32_t*) malloc (N*sizeof(int));
//  uint32_t *B = (uint32_t*) malloc (N*sizeof(int));
//  for (int i = 0; i< N; i++) {
//    A[i] = i;
//    B[i] = i;
//  }
//
//  // Device buffer initialization
//  uint64_t deviceA = c1->malloc(N*sizeof(int));
//  uint64_t deviceB = c1->malloc(N*sizeof(int));
//  c1->memcpy(deviceA, A, N*sizeof(int));
//  c1->memcpy(deviceB, B, N*sizeof(int));
//
//  // Set addresses and size
//  EPRINTF("deviceA: %lx\n", deviceA);
//  EPRINTF("deviceB: %lx\n", deviceB);
//  EPRINTF("N      :  %x\n", N);
//  c1->setArg(0, deviceA);
//  c1->setArg(1, deviceB);
//  c1->setArg(2, N);
//
//  c1->run();
//  uint32_t out = c1->getArg(0);
//
//  uint32_t gold = 0;
//  for (int i = 0; i<N; i++) {
//    gold += A[i] * B[i];
//  }
//
//  bool fail = (gold != out);
//
//  if (fail) {
//    printf("Test FAILED: Expected %u, observed %u\n", gold, out);
//  } else {
//    printf("Test PASSED\n");
//  }
//
//
//  printf("%s", KNRM);
  delete c1;
  return 0;
}

