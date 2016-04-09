// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.yahoo.wildwest.NestedMissingFingers;


public class SampleJniTest {
    private static final native void nativeSampleInfo(long address, long len);

    public static SampleInfo createSampleInfo() {
        try (NestedMissingFingers sampleInfo = SampleInfoGenerated.initializeSampleInfo()) {
            nativeSampleInfo(sampleInfo.getAddress(), sampleInfo.getLength());
            return SampleInfoGenerated.createSampleInfo(sampleInfo);
        }
    }

    @Test
    public void simpleTest() {
        JniLibraryLoader.load();
        SampleInfo si = createSampleInfo();
        Assert.assertEquals(si.getType(), 230617054);
        Assert.assertEquals(si.getAttrs(), 230617054);
        Assert.assertEquals(si.getExpiration(), 9208452695088623582L);
        Assert.assertEquals(si.getReadCount(), 230617054);
        Assert.assertEquals(si.getWriteCount(), 230617054);
        Assert.assertEquals(si.getWriteTimestamp(), 9208452695088623582L);
        Assert.assertEquals(si.getLoc(), "locAddress");
        Assert.assertEquals(si.getCcode(), "ccodeAddress");
        Assert.assertEquals(si.getDesc(), "descAddress");
        Assert.assertEquals(si.getOrg(),
                        "// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.     // Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.     // Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms. ");
    }

    public static void main(String[] args) {
        System.err.println(createSampleInfo());
    }
}
