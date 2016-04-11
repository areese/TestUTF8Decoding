// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import com.yahoo.wildwest.MissingHand;


public class SampleJni {
    private static final native void nativeSampleInfo(long address, long len);

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
