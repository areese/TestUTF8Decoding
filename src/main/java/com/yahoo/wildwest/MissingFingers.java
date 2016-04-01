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
     * This version will _allocate_ This allocates space of at least byteArraySize. Looking at DirectByteBuffer, it
     * seems that you need to check if things are page aligned, and allocate on a page boundary. So this might allocate
     * more than that size as an unknown implementation detail.
     * 
     * @param byteArraySize size of array to allocate
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

    public long getLength() {
        return length;
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
