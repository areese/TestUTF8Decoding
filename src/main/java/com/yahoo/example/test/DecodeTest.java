// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.example.testutf8.JniLibraryLoader;
import com.yahoo.wildwest.MUnsafe;

@SuppressWarnings("restriction")
public class DecodeTest {
    static {
        JniLibraryLoader.load();
    }

    public static final native boolean dump(long address, long len);

    public static final native boolean dumpEncoded(long address, long len);

    public static final native void encodeInto(long address, long len);

    public static void main(String[] args) throws Exception {
        // testLong();
        // testInt();
        // testShort();
        // testByte();
        testLotsOfLongs();
    }

    static final long data = 0xDEADBEEFCAFEC010L;

    private static void put(long data, long len) throws Exception {}

    private static void testLong() throws Exception {
        long len = 8;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putLong(address, data);
        System.err.println("Putting " + Long.toHexString(data));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }

    private static void testInt() throws Exception {
        long len = 4;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putInt(address, (int) data);
        System.err.println("Putting " + Long.toHexString(data));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }

    private static void testShort() throws Exception {
        long len = 2;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putShort(address, (short) data);
        System.err.println("Putting " + Long.toHexString(data));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }

    private static void testByte() throws Exception {
        long len = 1;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putByte(address, (byte) data);
        System.err.println("Putting " + Long.toHexString(data));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }

    private static void testLotsOfLongs() throws Exception {
        long len = 120;
        long a = 1; // int
        long b = 2; // int
        long c = 3; // int
        long d = 4; // long
        long e = 5; // int
        long f = 6; // int
        long g = 7; // long
        long hBytes = 8;
        long hLen = 9;
        long iBytes = 10;
        long iLen = 11;
        long jBytes = 12;
        long jLen = 13;
        long kBytes = 14;
        long kLen = 15;

        long[] longs = new long[] {a, //
                        b, //
                        c, //
                        d, //
                        e, //
                        f, //
                        g, //
                        hBytes, //
                        hLen, //
                        iBytes, //
                        iLen, //
                        jBytes, //
                        jLen, //
                        kBytes, //
                        kLen, //

        };

        long scale = MUnsafe.unsafe.arrayIndexScale(long[].class);

        long address = MUnsafe.unsafe.allocateMemory(len);
        for (int i = 0, j = 0; i < longs.length && j < len; i++) {
            // System.err.println("Putting " + longs[i] + " at " + Long.toHexString(address) + j);
            System.err.println("Putting " + Long.toHexString(address + j) + " at " + longs[i]);
            MUnsafe.unsafe.putLong(address + j, longs[i]);
            j += 8;
        }

        if (!dump(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);

    }
}
