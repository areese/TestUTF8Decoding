// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestJavaGenerator {
    private static final String SIMPLE_INIT_FUNCTION =
                    "\n    public static MissingFingers initializeTestJavaGenerator() {\n" + //
                                    "        return new MissingFingers(ALLOCATED_MEMORY_LENGTH);\n" + //
                                    "    }\n\n";

    @Test
    public void testSimple() {
        try (JavaGenerator jg = new JavaGenerator("", "", this.getClass())) {

            Assert.assertEquals(0, jg.getAllocatedCount().get());
            jg.javaCreateInitialize();

            ListPrintWriter initFunction = jg.getInitFunction();
            String output = initFunction.toString();

            Assert.assertEquals(output, SIMPLE_INIT_FUNCTION);
        }

    }
}
