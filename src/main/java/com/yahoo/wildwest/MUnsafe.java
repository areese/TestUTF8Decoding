// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import sun.misc.Unsafe;
import sun.misc.VM;

@SuppressWarnings("restriction")
public class MUnsafe {
	/**
	 * The instance of unsafe everyone can use
	 */
	public static final Unsafe unsafe;

	/**
	 * This is required by unsafe to access a char array, it's the offset
	 */
	public static final long charArrayBaseOffset;

	/**
	 * This is required by unsafe to access a char array, it's the scale for
	 * indexing.
	 */
	public static final long charArrayIndexScale;

	/**
	 * This is required by unsafe to access a byte array, it's the offset
	 */
	public static final long byteArrayBaseOffset;

	/**
	 * This is required by unsafe to access a byte array, it's the scale for
	 * indexing.
	 */
	public static final long byteArrayIndexScale;

	/**
	 * This is the offset into the {@link InetSocketAddress} which references
	 * it's holder field.
	 */
	// public static final long ISA_HOLDER_OFFSET;

	/**
	 * For convience.
	 */
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[] {};

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

			// ISA_HOLDER_OFFSET =
			// unsafe.objectFieldOffset(InetSocketAddress.class.getDeclaredField("holder"));

		} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
			throw new Error(e);
		}
	}

	/**
	 * Access to unsafe
	 * 
	 * @return Unsafe accessor.
	 */
	public static Unsafe getUnsafe() {
		return unsafe;
	}

	/**
	 * Given an index into a char array, return the correct offset to use
	 * 
	 * @param index
	 * @return offset into the array for a specific index.
	 */
	public static long charArrayOffset(int index) {
		return calculateOffset(index, charArrayBaseOffset, charArrayIndexScale);
	}

	/**
	 * Given a char array, calculate the correct amount of memory it will take.
	 * ??? Doesn't take into account null and probably shoult.
	 * 
	 * @param from
	 * @return length to allocate.
	 */
	public static long charArraySize(char[] from) {
		return from.length * charArrayIndexScale;
	}

	public static long byteArrayOffset(int index) {
		return calculateOffset(index, byteArrayBaseOffset, byteArrayIndexScale);
	}

	/**
	 * Given a byte array, calculate the correct amount of memory it will take.
	 * 
	 * @param from
	 * @return length to allocate.
	 */
	public static long byteArraySize(byte[] from) {
		return from.length * byteArrayIndexScale;
	}

	/**
	 * The object referred to by o is an array, and the offset is an integer of
	 * the form B+N*S, where N is a valid index into the array, and B and S are
	 * the values obtained by #arrayBaseOffset and #arrayIndexScale
	 * (respectively) from the array's class. The value referred to is the Nth
	 * element of the array.
	 * 
	 * @param index
	 *            into the array to find the offset for.
	 * @param base
	 *            base from Unsafe to use.
	 * @param scale
	 *            scale from Unsafe to use
	 * @return value usable by copyMemory
	 **/
	public static long calculateOffset(int index, long base, long scale) {
		return (base + (index * scale));
	}

	/**
	 * Allocate memory from unsafe of cap bytes
	 * 
	 * @param cap
	 *            number of bytes to allocate
	 * @return memory that must be free'd with Unsafe.free memory
	 */
	// Cribbed from DiretByteBuffer, apparently you need to check if it should
	// be page aligned and allocate enough if it is.
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

	/**
	 * Copy the contents of a byte array into native memory
	 * 
	 * @param destAddress
	 *            native memory
	 * @param totalSize
	 *            amount to copy
	 * @param from
	 *            where to copy from
	 */
	public static void copyMemory(long destAddress, long totalSize, byte[] from) {
		if (0 == destAddress || 0 == totalSize || null == from || 0 == from.length) {
			return;
		}

		long bytes = Math.min(from.length, totalSize);
		unsafe.copyMemory(from, byteArrayBaseOffset, null, index(destAddress, 0), bytes);
	}

	/**
	 * Copy the contents of a byte array into native memory
	 * 
	 * @param dest
	 *            where to copy to
	 * @param srcAddress
	 *            where to copy from
	 * @param totalSize
	 *            amount to copy
	 */
	public static void copyMemory(byte[] dest, long srcAddress, long totalSize) {
		if (0 == srcAddress || 0 == totalSize || null == dest || 0 == dest.length) {
			return;
		}

		long bytes = Math.min(dest.length, totalSize);
		unsafe.copyMemory(null, index(srcAddress, 0), dest, byteArrayBaseOffset, bytes);
	}

	/**
	 * given an address find it's index
	 * 
	 * @param address
	 *            native memory
	 * @param i
	 *            index
	 * @return index
	 */
	private static long index(long address, int i) {
		return address + ((long) i << 0);
	}

	/**
	 * Free's native memory
	 * 
	 * @param address
	 *            native memory to free
	 */
	public static void freeMemory(long address) {
		if (0 != address) {
			unsafe.freeMemory(address);
			address = 0;
		}
	}

	/**
	 * Given a String, encode it into MissingFingers address,length tuple
	 * 
	 * @param s
	 *            input
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
	 * Given a UTF8 string pointed to by address of len bytes long, decode it,
	 * and then free the memory pointed at by it.
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

	public static InetAddress decodeInetAddress(long srcAddress, long len) {
		// TODO: implement
		return null;
	}

	/**
	 * 
	 * @param srcAddress
	 * @param len
	 * @return Java String, null on null, 0 on empty string
	 */
	public static InetAddress decodeInetAddressAndFree(long srcAddress, long len) {
		try {
			return decodeInetAddress(srcAddress, len);
		} finally {
			if (0 != srcAddress) {
				unsafe.freeMemory(srcAddress);
			}
		}
	}

	public static InetAddress decodeInetAddressAndFree(MissingFingers encodedInetAddress) {
		try {
			if (null == encodedInetAddress) {
				return null;
			}
			return decodeInetAddress(encodedInetAddress.getAddress(), encodedInetAddress.getLength());
		} finally {
			if (null != encodedInetAddress) {
				encodedInetAddress.close();
			}
		}
	}

	/**
	 * 
	 * @param srcAddress
	 * @param len
	 * @return Java byte[], null on null, empty on empty
	 */
	public static byte[] decodeByteArrayAndFree(long srcAddress, long len) {
		try {
			return decodeByteArray(srcAddress, len);
		} finally {
			if (0 != srcAddress) {
				unsafe.freeMemory(srcAddress);
			}
		}
	}

	/**
	 * @param srcAddress
	 * @param len
	 * @return Java ByteArray, null on null, 0 on empty string
	 */
	public static byte[] decodeByteArray(long srcAddress, long len) {
		if (0 == srcAddress) {
			return null;
		}

		if (0 == len) {
			return EMPTY_BYTE_ARRAY;
		}

		byte[] toBytes = new byte[(int) len];
		copyMemory(toBytes, srcAddress, len);

		return toBytes;
	}

	public static byte[] decodeByteArray(MissingFingers encodedByteArray) {
		if (null == encodedByteArray) {
			return null;
		}
		return decodeByteArray(encodedByteArray.getAddress(), encodedByteArray.getLength());
	}

	public static byte[] decodeByteArrayAndFree(MissingFingers encodedByteArray) {
		try {
			if (null == encodedByteArray) {
				return null;
			}
			return decodeByteArray(encodedByteArray.getAddress(), encodedByteArray.getLength());
		} finally {
			if (null != encodedByteArray) {
				encodedByteArray.close();
			}
		}
	}

}
