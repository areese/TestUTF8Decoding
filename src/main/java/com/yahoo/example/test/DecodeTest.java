// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.example.testutf8.JniLibraryLoader;
import com.yahoo.wildwest.MUnsafe;

public class DecodeTest {
    static {
        JniLibraryLoader.load();
    }

    public static final native boolean dumpEncoded(long address, long len);

    public static final native void encodeInto(long address, long len);

    public static void main(String[] args) throws Exception {
        testLong();
        testInt();
        testShort();
        testByte();
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
}
