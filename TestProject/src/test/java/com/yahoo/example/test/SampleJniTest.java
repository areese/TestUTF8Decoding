// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.yahoo.wildwest.BoundsCheckException;
import com.yahoo.wildwest.MissingHand;


public class SampleJniTest {
    @Test
    public void simpleTest() {
        JniLibraryLoader.load();
        SampleInfo si = SampleJni.createSampleInfo();
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

    @Test(expectedExceptions = BoundsCheckException.class)
    public void testBounds() {
        try (MissingHand sampleInfo = SampleInfoGenerated.initializeSampleInfo()) {
            MissingHand mh = new MissingHand(sampleInfo.getAddress(), sampleInfo.getLength() - 10, null);
            SampleInfoGenerated.createSampleInfo(mh);
        }
    }

    public static void main(String[] args) {
        System.err.println(SampleJni.createSampleInfo());
    }
}
