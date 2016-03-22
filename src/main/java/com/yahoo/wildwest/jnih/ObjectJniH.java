// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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
        String cFilename = "generateSample";
        String javaFilename = "generateSample";

        if (args.length > 0) {
            classToDump = Class.forName(args[0]);
        } else {
            classToDump = new DumpTest().getClass();
        }

        for (int i = 1; i < args.length; i++) {
            if ("-cstruct".equals(args[i])) {
                generateCCode = true;
            }

            if ("-java".equals(args[i])) {
                generateJavaCode = true;
            }

            if ("-cfile".equals(args[i]) && (i + 1) < args.length) {
                cFilename = args[i + 1];
            }

            if ("-javafile".equals(args[i]) && (i + 1) < args.length) {
                javaFilename = args[i + 1];
            }
        }

        if (generateCCode) {
            // create the c struct
            try (HGenerator c = new HGenerator(classToDump)) {
                try (PrintWriter pw = new PrintWriter(new File(cFilename + ".h"))) {
                    pw.println(c.generate());
                }
            }

            try (CGenerator c = new CGenerator(classToDump)) {
                try (PrintWriter pw = new PrintWriter(new File(cFilename + ".cpp"))) {
                    pw.println(c.generate());
                }
            }
        }

        if (generateJavaCode) {
            try (PrintWriter pw = new PrintWriter(new File(javaFilename + ".java"))) {
                pw.println("package com.yahoo.example.test;");
                pw.println("import java.net.InetAddress;");
                pw.println("import com.yahoo.wildwest.MUnsafe;");
                pw.println("import com.yahoo.wildwest.MissingFingers;");
                pw.println("public class GenerateSample {");

                try (JavaGenerator java = new JavaGenerator(classToDump)) {
                    pw.println(java.generate());
                }

                pw.println("}");
            }
        }

        // create the java read code, we can use the setters we've found

        // some people take constructors.
        // we can do that by making either:
        // a) a list of getLongs()
        // b) a list of longs assigned from getLong

    }

}
