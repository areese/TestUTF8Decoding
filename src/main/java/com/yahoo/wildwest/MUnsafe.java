// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import sun.misc.Unsafe;
import sun.misc.VM;

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
        int pageSize = unsafe.pageSize();
        long size = Math.max(1L, (long) cap + (isDirectMemoryPageAligned ? pageSize : 0));
        long base = unsafe.allocateMemory(size);
        unsafe.setMemory(base, size, (byte) 0);

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
        unsafe.copyMemory(from, byteArrayBaseOffset, null, index(destAddress, 0), totalSize);
    }

    public static void copyMemory(byte[] dest, long srcAddress, long totalSize) {
        unsafe.copyMemory(null, index(srcAddress, 0), dest, byteArrayBaseOffset, totalSize);
    }

    private static long index(long address, int i) {
        return address + ((long) i << 0);
    }

    public static void freeMemory(long address) {
        if (0 != address) {
            unsafe.freeMemory(address);
            address = 0;
        }
    }

    /**
     * Given a String, encode it into MissingFingers address,length tuple
     * 
     * @param s input
     * @return MissingFingers, allocated address + length
     */
    public static MissingFingers encodeString(String s) {
        if (null == s || 0 == s.length() || s.isEmpty()) {
            // null begets null
            return new MissingFingers(0, 0);
        }

        byte[] fromBytes = s.getBytes(StandardCharsets.UTF_8);

        // gotta null terminate
        long totalSize = byteArraySize(fromBytes) + 1;
        long destAddress = unsafe.allocateMemory(totalSize);
        copyMemory(destAddress, totalSize, fromBytes);

        return new MissingFingers(destAddress, totalSize);
    }

    /**
     * Given a UTF8 string pointed to by address of len bytes long, decode it, and then free the memory pointed at by
     * it.
     * 
     * @param srcAddress
     * @param len
     * @return Java String, null on null, 0 on empty string
     */
    public static String decodeStringAndFree(long srcAddress, long len) {
        try {
            return decodeString(srcAddress, len);
        } finally {
            if (0 != srcAddress) {
                unsafe.freeMemory(srcAddress);
            }
        }
    }

    /**
     * Given a UTF8 string pointed to by address of len bytes long, decode it
     * 
     * @param srcAddress
     * @param len
     * @return Java String, null on null, 0 on empty string
     */
    public static String decodeString(long srcAddress, long len) {
        if (0 == srcAddress) {
            return null;
        }

        if (0 == len) {
            return "";
        }

        byte[] toBytes = new byte[(int) len];
        copyMemory(toBytes, srcAddress, len);

        // strip off null terminator
        if (0 == toBytes[(int) len - 1]) {
            len -= 1;
        }

        return new String(toBytes, 0, (int) len, StandardCharsets.UTF_8);
    }

    public static String decodeString(MissingFingers encodedString) {
        if (null == encodedString) {
            return null;
        }
        return decodeString(encodedString.getAddress(), encodedString.getLength());
    }

    public static String decodeStringAndFree(MissingFingers encodedString) {
        try {
            if (null == encodedString) {
                return null;
            }
            return decodeString(encodedString.getAddress(), encodedString.getLength());
        } finally {
            if (null != encodedString) {
                encodedString.close();
            }
        }
    }

}
