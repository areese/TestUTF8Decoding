// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import java.io.Closeable;

public class MissingFingers implements Closeable {
    private long address;
    private long length;

    public MissingFingers() {
        this.address = 0;
        this.length = 0;
    }

    public MissingFingers(long address, long length) {
        this.address = address;
        this.length = length;
    }

    /**
     * This version will _allocate_
     * 
     * @param byteArraySize
     */
    public MissingFingers(long byteArraySize) {
        this.length = byteArraySize;
        this.address = MUnsafe.allocate(this.length);
    }

    /**
     * Given a byte array, _allocate_ and _copy_ the bytes. This is suitable for passing to the jni afterwards.
     * 
     * @param from bytes to copy
     */
    public MissingFingers(byte[] from) {
        this(MUnsafe.byteArraySize(from));
        MUnsafe.copyMemory(address, length, from);
    }

    public long getAddress() {
        return address;
    }

    /**
     * You probably shouldn't ever call this. It should only be an address from unsafe.
     * 
     * @param address
     */
    public void setAddress(long address) {
        this.address = address;
    }

    public long getLength() {
        return length;
    }

    /**
     * You probably shouldn't ever call this. It should only be an address from unsafe.
     * 
     * @param address
     */
    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public void close() {
        MUnsafe.freeMemory(address);
    }

}
