// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.example.testutf8.JniLibraryLoader;
import com.yahoo.wildwest.MissingFingers;

public class SampleJniTest {
    private static final native void nativeSampleInfo(long address, long len);

    public static SampleInfo createSampleInfo() {
        MissingFingers sampleInfo = SampleInfoGenerated.initializeSampleInfo();
        nativeSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
        return SampleInfoGenerated.createSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
    }

    public static void main(String[] args) {
        JniLibraryLoader.load();
        SampleInfo si = createSampleInfo();
        System.out.println(si);
    }
}
