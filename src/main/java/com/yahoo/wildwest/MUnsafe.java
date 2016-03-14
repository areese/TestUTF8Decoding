// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

import sun.misc.Unsafe;
import sun.misc.VM;

@SuppressWarnings("restriction")
public class MUnsafe {
    public static final Unsafe unsafe;
    public static final long charArrayBaseOffset;
    public static final long charArrayIndexScale;
    public static final long byteArrayBaseOffset;
    public static final long byteArrayIndexScale;
    public static final long ISA_HOLDER_OFFSET;
    // public static final long ISA_ADDR_OFFSET;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            charArrayBaseOffset = unsafe.arrayBaseOffset(char[].class);
            charArrayIndexScale = unsafe.arrayIndexScale(char[].class);
            byteArrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);
            byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);

            ISA_HOLDER_OFFSET = unsafe.objectFieldOffset(InetSocketAddress.class.getDeclaredField("holder"));



        } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
            throw new Error(e);
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static long charArrayOffset(int index) {
        return calculateOffset(index, charArrayBaseOffset, charArrayIndexScale);
    }

    public static long charArraySize(char[] from) {
        return from.length * charArrayIndexScale;
    }

    public static long byteArrayOffset(int index) {
        return calculateOffset(index, byteArrayBaseOffset, byteArrayIndexScale);
    }

    public static long byteArraySize(byte[] from) {
        return from.length * byteArrayIndexScale;
    }

    /**
     * The object referred to by o is an array, and the offset is an integer of the form B+N*S, where N is a valid index
     * into the array, and B and S are the values obtained by #arrayBaseOffset and #arrayIndexScale (respectively) from
     * the array's class. The value referred to is the Nth element of the array.
     * 
     * @param index into the array to find the offset for.
     * @param base base from Unsafe to use.
     * @param scale scale from Unsafe to use
     * @return value usable by copyMemory
     **/
    public static long calculateOffset(int index, long base, long scale) {
        return (base + (index * scale));
    }

    // Cribbed from DiretByteBuffer
    public static long allocate(long cap) {
        boolean isDirectMemoryPageAligned = VM.isDirectMemoryPageAligned();
        int pageSize = MUnsafe.unsafe.pageSize();
        long size = Math.max(1L, (long) cap + (isDirectMemoryPageAligned ? pageSize : 0));
        long base = MUnsafe.unsafe.allocateMemory(size);
        MUnsafe.unsafe.setMemory(base, size, (byte) 0);

        long address = 0;
        if (isDirectMemoryPageAligned && (base % pageSize != 0)) {
            // Round up to page boundary
            address = base + pageSize - (base & (pageSize - 1));
        } else {
            address = base;
        }

        return address;
    }

    public static void copyMemory(long destAddress, long totalSize, byte[] from) {
        long fromOffset = unsafe.arrayBaseOffset(byte[].class);
        unsafe.copyMemory(from, fromOffset, null, index(destAddress, 0), totalSize);
    }

    private static long index(long address, int i) {
        return address + ((long) i << 0);
    }
}
