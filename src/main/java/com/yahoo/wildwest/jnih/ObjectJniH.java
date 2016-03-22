// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import com.yahoo.example.test.DumpTest;

/**
 * Given an SIMPLE object on the classpath Generate all of the stub code to copy it into a long/long (address, length)
 * combo that can be passed to jni and the static c function to decode it.
 * 
 * @author areese
 *
 */
public class ObjectJniH {

    public static void main(String[] args) throws Exception {

        Class<?> classToDump;
        boolean generateCCode = false;
        boolean generateJavaCode = false;
        boolean printLazyClass = true;

        if (args.length > 0) {
            classToDump = Class.forName(args[0]);
        } else {
            classToDump = new DumpTest().getClass();
        }

        if (args.length > 1) {
            if ("-cstruct".equals(args[1])) {
                generateCCode = true;
            }
            if ("-java".equals(args[1])) {
                generateJavaCode = true;
            }
        }

        if (generateCCode) {
            // create the c struct
            try (CGenerator c = new CGenerator(classToDump)) {
                System.out.println(c.generate());
            }
        }

        if (generateJavaCode) {
            if (printLazyClass) {
                System.out.println("package com.yahoo.example.test;");
                System.out.println("import java.net.InetAddress;");
                System.out.println("import com.yahoo.wildwest.MUnsafe;");
                System.out.println("import com.yahoo.wildwest.MissingFingers;");
                System.out.println("public class GenerateSample {");
            }

            try (JavaGenerator java = new JavaGenerator(classToDump)) {
                System.out.println(java.generate());
            }

            if (printLazyClass) {
                System.out.println("}");
            }
        }

        // create the java read code, we can use the setters we've found

        // some people take constructors.
        // we can do that by making either:
        // a) a list of getLongs()
        // b) a list of longs assigned from getLong

    }

}
