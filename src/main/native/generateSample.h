#ifndef _Included_generatedSample
#define _Included_generatedSample

#include <sys/param.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>

//FIXME: const char* not uint64_t
typedef struct SampleInfoStruct {

    uint64_t type; // int

    uint64_t attrs; // int

    uint64_t status; // int

    uint64_t expiration; // long

    uint64_t readCount; // int

    uint64_t writeCount; // int

    uint64_t writeTimestamp; // long

    void * iaAddress;

    uint64_t iaLen;

    const char* orgAddress;

    uint64_t orgLen;

    const char* locAddress;

    uint64_t locLen;

    const char* ccodeAddress;

    uint64_t ccodeLen;

    const char* descAddress;

    uint64_t descLen;

    // FIXME: why was this added twice?  we had 2 defs of variables.

} SampleInfoStruct;

/**
 * This function was auto-generated
 * Given an allocated long addres, len tuple
 * It will encode in a way compatible with the generated java.
 * everything is 64bit longs with a cast
 * Strings are considered UTF8, and are a tuple of address + length
 * Due to native memory tracking, strings are prealloacted with Unsafe.allocateMemory and assigned an output length
 * Similiar to how a c function would take char *outBuf, size_t bufLen
 * The length coming in says how large the buffer for address is.
 * The length coming out says how many characters including \0 were written
 **/
void encodeIntoJava_SampleInfo(SampleInfoStruct *inputData, long address,
        long addressLength);

#endif
