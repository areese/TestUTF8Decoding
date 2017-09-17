// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.wildwest.MissingFingers;
import com.yahoo.wildwest.MissingHand;


public class SampleJni {
    static {
        JniLibraryLoader.load();
    }

    private static final native void nativeSampleInfo(long address, long len);

    private static final native void put4Bytes(long address, long len);

    public static void put4Bytes(MissingFingers mf) {
        if (null == mf || !mf.isValidAddress()) {
            return;
        }

        put4Bytes(mf.getAddress(), mf.getLength());
    }

    public static SampleInfo createSampleInfo() {
        try (MissingHand sampleInfo = SampleInfoGenerated.initializeSampleInfo()) {
            nativeSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
            return SampleInfoGenerated.createSampleInfo(sampleInfo);
        }
    }

    public static void main(String[] args) {
        System.err.println(createSampleInfo());
    }
}
