package com.yahoo.wildwest;

import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

public class NestedMissingFingers extends MissingFingers {
    private long[] internalAllocations;


    public NestedMissingFingers() {
        this(0, 0, false, 0);
    }

    public NestedMissingFingers(long address, long length) {
        this(address, length, true);
    }

    public NestedMissingFingers(long address, long length, int count) {
        this(address, length, true, count);
    }

    public NestedMissingFingers(long address, long length, boolean allocated) {
        this(address, length, allocated, 0);
    }

    public NestedMissingFingers(long address, long length, boolean allocated, int count) {
        super(address, length, allocated);
        internalAllocations = new long[count];
    }

    @Override
    public void close() {
        // only free if allocated
        if (getAllocated()) {
            for (long address : internalAllocations) {
                MUnsafe.freeMemory(address);
            }
            super.close();
        }
    }
}
