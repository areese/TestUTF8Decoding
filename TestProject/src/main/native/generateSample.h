
/*
 * This code was auto generated from https://github.com/areese/TestUTF8Decoding
 * Using:
 * java.class.path=build/libs/TestUTF8Decoding.jar:TestProject/build/classes/main/
 * sun.java.command=com.yahoo.wildwest.jnih.ObjectJniH com.yahoo.example.test.SampleInfo -javapath TestProject/src/main/java/ -cfile TestProject/src/main/native/generateSample
 * args:
 * com.yahoo.example.test.SampleInfo -javapath TestProject/src/main/java/ -cfile TestProject/src/main/native/generateSample
 */

#ifndef _Included_generateSample
#define _Included_generateSample

#include <sys/param.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>


#ifndef _generatedAddressUnion
#define _generatedAddressUnion
typedef struct AddressUnion {
    union {
        uint64_t address;
        void *voidPtr;
        const char *constCharPtr;
    };
    uint64_t len;
} AddressUnion;
#endif /* _generatedAddressUnion */

typedef struct SampleInfoStruct {

    uint64_t type; // int
    uint64_t attrs; // int
    uint64_t status; // int
    uint64_t expiration; // long
    uint64_t readCount; // int
    uint64_t writeCount; // int
    uint64_t writeTimestamp; // long
    AddressUnion ia; // java.net.InetAddress
    AddressUnion org; // java.lang.String
    AddressUnion loc; // java.lang.String
    AddressUnion ccode; // java.lang.String
    AddressUnion desc; // java.lang.String
    AddressUnion someBytes; // [B
} SampleInfoStruct;

/**
 * This function was auto-generated
 * Given an allocated long addres, len tuple
 * It will encode in a way compatible with the generated java.
 * everything is 64bit longs with a cast
 * Strings are considered UTF8, and are a tuple of address + length
 * Due to native memory tracking, strings are prealloacted with Unsafe.allocateMemory and assigned an output length
 * Similar to how a c function would take char *outBuf, size_t bufLen
 * The length coming in says how large the buffer for address is.
 * The length coming out says how many characters including \0 were written
**/
void encodeIntoJava_SampleInfo(SampleInfoStruct *inputData, long address, long addressLength);

#endif /* _Included_generateSample*/

