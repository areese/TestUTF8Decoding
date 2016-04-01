package com.yahoo.example.test;
import java.net.InetAddress;
import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

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

    @SuppressWarnings("restriction")
    public static MissingFingers initializeSampleInfo() {

        long totalLen = 0;
        // type int is 8 bytes , cast to uint64_t
        totalLen  += INT_FIELD_SIZE;

        // attrs int is 8 bytes , cast to uint64_t
        totalLen  += INT_FIELD_SIZE;

        // status int is 8 bytes , cast to uint64_t
        totalLen  += INT_FIELD_SIZE;

        // expiration long is 8 bytes , cast to uint64_t
        totalLen  += LONG_FIELD_SIZE;

        // readCount int is 8 bytes , cast to uint64_t
        totalLen  += INT_FIELD_SIZE;

        // writeCount int is 8 bytes , cast to uint64_t
        totalLen  += INT_FIELD_SIZE;

        // writeTimestamp long is 8 bytes , cast to uint64_t
        totalLen  += LONG_FIELD_SIZE;

        // ia java.net.InetAddress is 16 bytes , address + length
        totalLen  += INETADDRESS_FIELD_SIZE;

        // org java.lang.String is 16 bytes , address + length
        totalLen  += STRING_FIELD_SIZE;

        // loc java.lang.String is 16 bytes , address + length
        totalLen  += STRING_FIELD_SIZE;

        // ccode java.lang.String is 16 bytes , address + length
        totalLen  += STRING_FIELD_SIZE;

        // desc java.lang.String is 16 bytes , address + length
        totalLen  += STRING_FIELD_SIZE;

        // someBytes [B is 16 bytes , address + length
        totalLen  += BYTEARRAY_FIELD_SIZE;

        long address = MUnsafe.unsafe.allocateMemory(totalLen);

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
            long newAddress = MUnsafe.unsafe.allocateMemory(IA_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, IA_DATA_SIZE);
            offset += LEN_OFFSET;
        }

        // org java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(ORG_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, ORG_DATA_SIZE);
            offset += LEN_OFFSET;
        }

        // loc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(LOC_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, LOC_DATA_SIZE);
            offset += LEN_OFFSET;
        }

        // ccode java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(CCODE_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, CCODE_DATA_SIZE);
            offset += LEN_OFFSET;
        }

        // desc java.lang.String is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(DESC_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, DESC_DATA_SIZE);
            offset += LEN_OFFSET;
        }

        // someBytes [B is 16 bytes, address + length
        {
            long newAddress = MUnsafe.unsafe.allocateMemory(SOMEBYTES_DATA_SIZE); 
            MUnsafe.unsafe.putAddress(address + offset, newAddress);
            offset += ADDRESS_OFFSET;
            MUnsafe.unsafe.putAddress(address + offset, SOMEBYTES_DATA_SIZE);
            offset += LEN_OFFSET;
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
        long someBytesLen;
        long someBytesAddress;
        byte[] someBytes; // [B

        // Now that we've calculated the complete length, and have allocated it.
        // We have to go insert new allocations and lengths for the output buffers
        // Each output buffer has a constant size, which can be tweaked after generation
        long offset = 0;

        type = (int) MUnsafe.unsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read type type int

        attrs = (int) MUnsafe.unsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read attrs type int

        status = (int) MUnsafe.unsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read status type int

        expiration = (long) MUnsafe.unsafe.getLong(address + offset);
        offset += LONG_FIELD_SIZE; // just read expiration type long

        readCount = (int) MUnsafe.unsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read readCount type int

        writeCount = (int) MUnsafe.unsafe.getLong(address + offset);
        offset += INT_FIELD_SIZE; // just read writeCount type int

        writeTimestamp = (long) MUnsafe.unsafe.getLong(address + offset);
        offset += LONG_FIELD_SIZE; // just read writeTimestamp type long

        iaAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read iaAddress type java.net.InetAddress

        iaLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read iaLen type java.net.InetAddress

        ia = MUnsafe.decodeInetAddressAndFree(iaAddress, iaLen);


        orgAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read orgAddress type java.lang.String

        orgLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read orgLen type java.lang.String

        org = MUnsafe.decodeStringAndFree(orgAddress, orgLen);


        locAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read locAddress type java.lang.String

        locLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read locLen type java.lang.String

        loc = MUnsafe.decodeStringAndFree(locAddress, locLen);


        ccodeAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read ccodeAddress type java.lang.String

        ccodeLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read ccodeLen type java.lang.String

        ccode = MUnsafe.decodeStringAndFree(ccodeAddress, ccodeLen);


        descAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read descAddress type java.lang.String

        descLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read descLen type java.lang.String

        desc = MUnsafe.decodeStringAndFree(descAddress, descLen);


        someBytesAddress = MUnsafe.unsafe.getLong(address + offset);
        offset += ADDRESS_OFFSET; // just read someBytesAddress type [B

        someBytesLen = MUnsafe.unsafe.getLong(address + offset);
        offset += LEN_OFFSET; // just read someBytesLen type [B

        someBytes = MUnsafe.decodeByteArrayAndFree(someBytesAddress, someBytesLen);



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

