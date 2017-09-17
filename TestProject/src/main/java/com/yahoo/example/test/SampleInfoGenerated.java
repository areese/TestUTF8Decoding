package com.yahoo.example.test;
import java.net.InetAddress;

import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingHand;
import com.yahoo.wildwest.BoundsCheckException;


/*
 * This code was auto generated from https://github.com/areese/TestUTF8Decoding
 * Using:
 * java.class.path=/Users/areese/dev/github/areese/TestUTF8Decoding/TestProject/build/classes/main:/Users/areese/dev/github/areese/TestUTF8Decoding/TestProject/build/resources/main:/Users/areese/.m2/repository/com/yahoo/example/codegen/codegen/1.0.0/codegen-1.0.0.jar:/Users/areese/.gradle/caches/modules-2/files-2.1/log4j/log4j/1.2.14/3b254c872b95141751f414e353a25c2ac261b51/log4j-1.2.14.jar:/Users/areese/dev/github/areese/TestUTF8Decoding/build/libs/TestUTF8Decoding.jar:build/classes/main
 * sun.java.command=com.yahoo.wildwest.jnih.ObjectJniH com.yahoo.example.test.SampleInfo -javapath src/main/java -cfile src/main/native/generateSample
 * args:
 * com.yahoo.example.test.SampleInfo -javapath src/main/java -cfile src/main/native/generateSample
 * you can probably run this command to regenerate it
 * java -cp /Users/areese/dev/github/areese/TestUTF8Decoding/TestProject/build/classes/main:/Users/areese/dev/github/areese/TestUTF8Decoding/TestProject/build/resources/main:/Users/areese/.m2/repository/com/yahoo/example/codegen/codegen/1.0.0/codegen-1.0.0.jar:/Users/areese/.gradle/caches/modules-2/files-2.1/log4j/log4j/1.2.14/3b254c872b95141751f414e353a25c2ac261b51/log4j-1.2.14.jar:/Users/areese/dev/github/areese/TestUTF8Decoding/build/libs/TestUTF8Decoding.jar:build/classes/main com.yahoo.wildwest.jnih.ObjectJniH com.yahoo.example.test.SampleInfo -javapath src/main/java -cfile src/main/native/generateSample com.yahoo.example.test.SampleInfo -javapath src/main/java -cfile src/main/native/generateSample
 */

public class SampleInfoGenerated {
    public static final long BYTE_FIELD_SIZE = 8;
    public static final long SHORT_FIELD_SIZE = 8;
    public static final long INT_FIELD_SIZE = 8;
    public static final long LONG_FIELD_SIZE = 8;
    public static final long STRING_FIELD_SIZE = 16;
    public static final long INETADDRESS_FIELD_SIZE = 16;
    public static final long BYTEARRAY_FIELD_SIZE = 16;
    public static final long ADDRESS_OFFSET = 8;
    public static final long LEN_OFFSET = 8;
    public static final long IA_DATA_SIZE = 16;
    public static final long ORG_DATA_SIZE = 1024;
    public static final long LOC_DATA_SIZE = 1024;
    public static final long CCODE_DATA_SIZE = 1024;
    public static final long DESC_DATA_SIZE = 1024;
    public static final long SOMEBYTES_DATA_SIZE = 1024;
    // type int is 8 bytes , cast to uint64_t
    // offset += INT_FIELD_SIZE;

    // attrs int is 8 bytes , cast to uint64_t
    // offset += INT_FIELD_SIZE;

    // status int is 8 bytes , cast to uint64_t
    // offset += INT_FIELD_SIZE;

    // expiration long is 8 bytes , cast to uint64_t
    // offset += LONG_FIELD_SIZE;

    // readCount int is 8 bytes , cast to uint64_t
    // offset += INT_FIELD_SIZE;

    // writeCount int is 8 bytes , cast to uint64_t
    // offset += INT_FIELD_SIZE;

    // writeTimestamp long is 8 bytes , cast to uint64_t
    // offset += LONG_FIELD_SIZE;

    // ia java.net.InetAddress is 16 bytes , address + length
    // offset += INETADDRESS_FIELD_SIZE;
    // allocatedCount++;

    // org java.lang.String is 16 bytes , address + length
    // offset += STRING_FIELD_SIZE;
    // allocatedCount++;

    // loc java.lang.String is 16 bytes , address + length
    // offset += STRING_FIELD_SIZE;
    // allocatedCount++;

    // ccode java.lang.String is 16 bytes , address + length
    // offset += STRING_FIELD_SIZE;
    // allocatedCount++;

    // desc java.lang.String is 16 bytes , address + length
    // offset += STRING_FIELD_SIZE;
    // allocatedCount++;

    // someBytes [B is 16 bytes , address + length
    // offset += BYTEARRAY_FIELD_SIZE;
    // allocatedCount++;

    // total size of our allocation, used for boundsChecking
    static final int ALLOCATED_MEMORY_LENGTH = 152;
    // count of child allocations used for alloc/free of addresses
    static final int CHILD_ALLOCATIONS_COUNT = 6;

    public static void boundsCheck (long address, long offset) throws BoundsCheckException {
        if (offset > ALLOCATED_MEMORY_LENGTH) {
            throw new BoundsCheckException(address, offset, ALLOCATED_MEMORY_LENGTH);
        }
    }

    public static void boundsCheck (long address, long offset, long length) throws BoundsCheckException {
        if (offset > length) {
            throw new BoundsCheckException(address, offset, length);
        }
    }

    public static MissingHand initializeSampleInfo() {

        long[] childAllocations = new long[CHILD_ALLOCATIONS_COUNT];
        int childIndex = 0;
        long address = MUnsafe.allocateMemory(ALLOCATED_MEMORY_LENGTH);

        long offset = 0;
        // type int is 8 bytes , cast to uint64_t
        offset  += INT_FIELD_SIZE;

        // attrs int is 8 bytes , cast to uint64_t
        offset  += INT_FIELD_SIZE;

        // status int is 8 bytes , cast to uint64_t
        offset  += INT_FIELD_SIZE;

        // expiration long is 8 bytes , cast to uint64_t
        offset  += LONG_FIELD_SIZE;

        // readCount int is 8 bytes , cast to uint64_t
        offset  += INT_FIELD_SIZE;

        // writeCount int is 8 bytes , cast to uint64_t
        offset  += INT_FIELD_SIZE;

        // writeTimestamp long is 8 bytes , cast to uint64_t
        offset  += LONG_FIELD_SIZE;

        // ia java.net.InetAddress is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(IA_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, IA_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        // org java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(ORG_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, ORG_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        // loc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(LOC_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, LOC_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        // ccode java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(CCODE_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, CCODE_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        // desc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(DESC_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, DESC_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        // someBytes [B is 16 bytes, address + length
        {
            long newAddress = MUnsafe.allocateMemory(SOMEBYTES_DATA_SIZE);
            MUnsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            boundsCheck(address, offset);
            MUnsafe.putAddress(address + offset, SOMEBYTES_DATA_SIZE);
            offset += LEN_OFFSET;
            boundsCheck(address, offset);
            childAllocations[childIndex++] = newAddress;
        }

        return new MissingHand(address, ALLOCATED_MEMORY_LENGTH, childAllocations);
    }

    public static com.yahoo.example.test.SampleInfo createSampleInfo(MissingHand nested) {

        long address = nested.getAddress();
        long length = nested.getLength();
        int type; // int
        int attrs; // int
        int status; // int
        long expiration; // long
        int readCount; // int
        int writeCount; // int
        long writeTimestamp; // long
        long iaLen;
        long iaAddress;
        InetAddress ia; // java.net.InetAddress
        long orgLen;
        long orgAddress;
        String org; // java.lang.String
        long locLen;
        long locAddress;
        String loc; // java.lang.String
        long ccodeLen;
        long ccodeAddress;
        String ccode; // java.lang.String
        long descLen;
        long descAddress;
        String desc; // java.lang.String
        long someBytesLen;
        long someBytesAddress;
        byte[] someBytes; // [B

        // Now that we've calculated the complete length, and have allocated it.
        // We have to go insert new allocations and lengths for the output buffers
        // Each output buffer has a constant size, which can be tweaked after generation
        long offset = 0;

        type = (int) MUnsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read type type int
        boundsCheck(address, offset, length);

        attrs = (int) MUnsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read attrs type int
        boundsCheck(address, offset, length);

        status = (int) MUnsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read status type int
        boundsCheck(address, offset, length);

        expiration = (long) MUnsafe.getLong(address + offset);
        offset += LONG_FIELD_SIZE; // just read expiration type long
        boundsCheck(address, offset, length);

        readCount = (int) MUnsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read readCount type int
        boundsCheck(address, offset, length);

        writeCount = (int) MUnsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read writeCount type int
        boundsCheck(address, offset, length);

        writeTimestamp = (long) MUnsafe.getLong(address + offset);
        offset += LONG_FIELD_SIZE; // just read writeTimestamp type long
        boundsCheck(address, offset, length);

        iaAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read iaAddress type java.net.InetAddress
        boundsCheck(address, offset, length);

        iaLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read iaLen type java.net.InetAddress
        boundsCheck(address, offset, length);

        ia = MUnsafe.decodeInetAddress(iaAddress, iaLen);


        orgAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read orgAddress type java.lang.String
        boundsCheck(address, offset, length);

        orgLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read orgLen type java.lang.String
        boundsCheck(address, offset, length);

        org = MUnsafe.decodeString(orgAddress, orgLen);


        locAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read locAddress type java.lang.String
        boundsCheck(address, offset, length);

        locLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read locLen type java.lang.String
        boundsCheck(address, offset, length);

        loc = MUnsafe.decodeString(locAddress, locLen);


        ccodeAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read ccodeAddress type java.lang.String
        boundsCheck(address, offset, length);

        ccodeLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read ccodeLen type java.lang.String
        boundsCheck(address, offset, length);

        ccode = MUnsafe.decodeString(ccodeAddress, ccodeLen);


        descAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read descAddress type java.lang.String
        boundsCheck(address, offset, length);

        descLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read descLen type java.lang.String
        boundsCheck(address, offset, length);

        desc = MUnsafe.decodeString(descAddress, descLen);


        someBytesAddress = MUnsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read someBytesAddress type [B
        boundsCheck(address, offset, length);

        someBytesLen = MUnsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read someBytesLen type [B
        boundsCheck(address, offset, length);

        someBytes = MUnsafe.decodeByteArray(someBytesAddress, someBytesLen);



        com.yahoo.example.test.SampleInfo newObject = new com.yahoo.example.test.SampleInfo(
            type, //
            attrs, //
            status, //
            expiration, //
            readCount, //
            writeCount, //
            writeTimestamp, //
            ia, //
            org, //
            loc, //
            ccode, //
            desc, //
            someBytes);

        return newObject;

    }
}

