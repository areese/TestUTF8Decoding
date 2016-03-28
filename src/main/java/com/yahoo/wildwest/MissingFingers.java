package com.yahoo.wildwest;

import java.io.Closeable;

public class MissingFingers implements Closeable {
    private long address;
    private long length;
    private boolean allocated;

    public MissingFingers() {
        this.address = 0;
        this.length = 0;
        this.allocated = false;
    }

    public MissingFingers(long address, long length) {
        this(address, length, true);
    }

    public MissingFingers(long address, long length, boolean allocated) {
        this.address = address;
        this.length = length;
        this.allocated = allocated;
    }

    /**
     * This version will _allocate_
     * 
     * @param byteArraySize
     */
    public MissingFingers(long byteArraySize) {
        this(MUnsafe.allocate(byteArraySize), byteArraySize, true);
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
        // only free if allocated
        if (allocated) {
            MUnsafe.freeMemory(address);
        }
    }

    public boolean getAllocated() {
        return allocated;
    }

}
