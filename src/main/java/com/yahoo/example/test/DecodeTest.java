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
    }

    private static void testLong() throws Exception {
        long l = 0xDEADBEEFCAFEC010L;
        long len = 8;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putLong(address, l);
        System.err.println("Putting " + Long.toHexString(l));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }


    private static void testInt() throws Exception {
        int l = (int) 0xDEADBEEFCAFEC010L;
        long len = 4;
        long address = MUnsafe.unsafe.allocateMemory(len);
        MUnsafe.unsafe.putLong(address, l);
        System.err.println("Putting " + Long.toHexString(l));
        if (!dumpEncoded(address, len)) {
            throw new Exception();
        }
        MUnsafe.unsafe.freeMemory(address);
    }
}
