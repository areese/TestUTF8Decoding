package com.yahoo.wildwest;

import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

public class MissingHand extends MissingFingers {
    private final long[] childAllocations;


    public MissingHand() {
        this(0, 0, false, null);
    }

    public MissingHand(long address, long length) {
        this(address, length, true);
    }

    public MissingHand(long address, long length, long[] childAllocations) {
        this(address, length, true, childAllocations);
    }

    public MissingHand(long address, long length, boolean allocated) {
        this(address, length, allocated, null);
    }

    public MissingHand(long address, long length, boolean allocated, long[] childAllocations) {
        super(address, length, allocated);
        this.childAllocations = childAllocations;
    }

    @Override
    public void close() {
        // only free if allocated
        if (getAllocated()) {
            for (long address : childAllocations) {
                MUnsafe.freeMemory(address);
            }
            super.close();
        }
    }
}
