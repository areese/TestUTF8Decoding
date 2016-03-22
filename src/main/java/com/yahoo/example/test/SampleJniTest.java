// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.wildwest.MissingFingers;

public class SampleJniTest {
    private static final native void nativeSampleInfo(long address, long len);

    public static SampleInfo createSampleInfo() {
        MissingFingers sampleInfo = GenerateSample.initializeSampleInfo();
        nativeSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
        return GenerateSample.createSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
    }
}
