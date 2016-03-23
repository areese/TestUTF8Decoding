package com.yahoo.example.test;
import java.net.InetAddress;
import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

public class SampleInfoGenerated {

    public static MissingFingers initializeSampleInfo() {

        long totalLen = 0;
        // type int is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // attrs int is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // status int is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // expiration long is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // readCount int is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // writeCount int is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // writeTimestamp long is 8 bytes , cast to uint64_t
        totalLen  += 8;

        // ia java.net.InetAddress is 16 bytes , address + length
        totalLen  += 16;

        // org java.lang.String is 16 bytes , address + length
        totalLen  += 16;

        // loc java.lang.String is 16 bytes , address + length
        totalLen  += 16;

        // ccode java.lang.String is 16 bytes , address + length
        totalLen  += 16;

        // desc java.lang.String is 16 bytes , address + length
        totalLen  += 16;

        long address = MUnsafe.unsafe.allocateMemory(totalLen);
        System.out.println("Allocated address " + Long.toHexString(address) + " of length " + Long.toHexString(totalLen));

        long offset = 0;
        // type int is 8 bytes , cast to uint64_t
        offset  += 8;

        // attrs int is 8 bytes , cast to uint64_t
        offset  += 8;

        // status int is 8 bytes , cast to uint64_t
        offset  += 8;

        // expiration long is 8 bytes , cast to uint64_t
        offset  += 8;

        // readCount int is 8 bytes , cast to uint64_t
        offset  += 8;

        // writeCount int is 8 bytes , cast to uint64_t
        offset  += 8;

        // writeTimestamp long is 8 bytes , cast to uint64_t
        offset  += 8;

        // ia java.net.InetAddress is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(16); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += 8;
            MUnsafe.unsafe.putAddress(address + offset, 16);
            offset += 8;
        }

        // org java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(1024); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += 8;
            MUnsafe.unsafe.putAddress(address + offset, 1024);
            offset += 8;
        }

        // loc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(1024); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += 8;
            MUnsafe.unsafe.putAddress(address + offset, 1024);
            offset += 8;
        }

        // ccode java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(1024); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += 8;
            MUnsafe.unsafe.putAddress(address + offset, 1024);
            offset += 8;
        }

        // desc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(1024); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += 8;
            MUnsafe.unsafe.putAddress(address + offset, 1024);
            offset += 8;
        }

        return new MissingFingers(address, totalLen);
    }

    public static com.yahoo.example.test.SampleInfo createSampleInfo(long address, long len) {

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

    long offset = 0;
    type = (int) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read type type int

    attrs = (int) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read attrs type int

    status = (int) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read status type int

    expiration = (long) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read expiration type long

    readCount = (int) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read readCount type int

    writeCount = (int) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read writeCount type int

    writeTimestamp = (long) MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read writeTimestamp type long

    iaAddress = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read iaAddress type java.net.InetAddress

    iaLen = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read iaLen type java.net.InetAddress

    ia = MUnsafe.decodeInetAddressAndFree(iaAddress, iaLen);


    orgAddress = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read orgAddress type java.lang.String

    orgLen = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read orgLen type java.lang.String

    org = MUnsafe.decodeStringAndFree(orgAddress, orgLen);


    locAddress = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read locAddress type java.lang.String

    locLen = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read locLen type java.lang.String

    loc = MUnsafe.decodeStringAndFree(locAddress, locLen);


    ccodeAddress = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read ccodeAddress type java.lang.String

    ccodeLen = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read ccodeLen type java.lang.String

    ccode = MUnsafe.decodeStringAndFree(ccodeAddress, ccodeLen);


    descAddress = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read descAddress type java.lang.String

    descLen = MUnsafe.unsafe.getLong(address + offset);
    offset += 8; // just read descLen type java.lang.String

    desc = MUnsafe.decodeStringAndFree(descAddress, descLen);



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
        desc);

    return newObject;

    }
}

