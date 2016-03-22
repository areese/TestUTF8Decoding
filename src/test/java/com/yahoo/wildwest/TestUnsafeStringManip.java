// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestUnsafeStringManip {
    @Test
    public void testSimpleEncodeDecode() {
        // long and all ascii
        String simple =
                        "Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.";

        try (MissingFingers encodedString = MUnsafe.encodeString(simple)) {
            Assert.assertNotNull(encodedString);
            Assert.assertNotEquals(encodedString.getAddress(), 0);
            // it's ascii, so it should be null terminated
            Assert.assertEquals(encodedString.getLength(), simple.length() + 1);

            String decoded1 = MUnsafe.decodeString(encodedString);
            String decoded2 = MUnsafe.decodeString(encodedString.getAddress(), encodedString.getLength());
            Assert.assertNotNull(decoded2);
            Assert.assertEquals(decoded1, decoded2);
            Assert.assertEquals(decoded1, simple);
        }
    }
}
