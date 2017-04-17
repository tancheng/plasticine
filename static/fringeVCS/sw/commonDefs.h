#ifndef __COMMON_DEFS_H
#define __COMMON_DEFS_H

#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <assert.h>

// Some helper macros
#define EPRINTF(...) fprintf(stderr, __VA_ARGS__)
#define ASSERT(cond, ...) \
  if (!(cond)) { \
    EPRINTF("\n");        \
    EPRINTF(__VA_ARGS__); \
    EPRINTF("\n");        \
    EPRINTF("Assertion (%s) failed in %s, %d\n", #cond, __FILE__, __LINE__); \
    assert(0);  \
  }


int getFileSize(const char *filename) {
  ASSERT(filename, "filename is NULL");
  FILE *ifile = fopen(filename, "rb");
  ASSERT(ifile, "Could not open file %s for reading\n", filename);
  fseek(ifile, 0L, SEEK_END);
  size_t sz = ftell(ifile);
  rewind(ifile);
  fclose(ifile);
}

int fileToBuf(unsigned char *buf, const char *filename, uint32_t max_bytes)
{
  ASSERT(buf, "buf is NULL");
  ASSERT(filename, "filename is NULL");
  ASSERT(max_bytes > 0, "Trying to read invalid (%u) number of bytes from %s\n", max_bytes, filename);

  FILE *ifile = fopen(filename, "rb");
  int bytesRead = 0;
  ASSERT(ifile, "Could not open file %s for reading\n", filename);
  bytesRead = fread(buf, sizeof(char), max_bytes, ifile);
  ASSERT(bytesRead > 0, "Read invalid (%d) number of bytes from %s\n", bytesRead, filename);
  fclose(ifile);
  return bytesRead;
}

#endif // __COMMON_DEFS_H
