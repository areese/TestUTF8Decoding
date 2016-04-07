// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.File;
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
        String cFilename = "generateSample";
        String javaPath = "src/main/java/";

        StringBuilder builtFrom = new StringBuilder();

        // everyone loves c style comments. :)
        builtFrom.append(" * java.class.path=").append(System.getProperty("java.class.path")).append("\n");
        builtFrom.append(" * sun.java.command=").append(System.getProperty("sun.java.command")).append("\n");
        builtFrom.append(" * args:\n");
        builtFrom.append(" * ");
        for (String a : args) {
            builtFrom.append(a).append(" ");
        }
        // remove extra space.
        builtFrom.deleteCharAt(builtFrom.length() - 1);
        builtFrom.append("\n");

        String builtFromString = builtFrom.toString();


        if (args.length > 0) {
            classToDump = Class.forName(args[0]);
        } else {
            classToDump = new DumpTest().getClass();
        }

        for (int i = 1; i < args.length; i++) {
            if ("-cfile".equals(args[i]) && (i + 1) < args.length) {
                cFilename = args[i + 1];
                i++;
            }

            if ("-javapath".equals(args[i]) && (i + 1) < args.length) {
                javaPath = args[i + 1];

                if (!javaPath.endsWith("/")) {
                    javaPath += "/";
                }
                if (!new File(javaPath).isDirectory()) {
                    throw new Exception(javaPath + " is not a directory");
                }

                System.err.println("determined " + javaPath);
                i++;
            }
        }

        // create the c struct
        try (HGenerator c = new HGenerator(builtFromString, classToDump, cFilename)) {
            try (PrintWriter pw = new PrintWriter(new File(cFilename + ".h"))) {
                pw.println(c.generate());
            }
        }

        try (CGenerator c = new CGenerator(builtFromString, classToDump, cFilename)) {
            try (PrintWriter pw = new PrintWriter(new File(cFilename + ".cpp"))) {
                pw.println(c.generate());
            }
        }

        try (JavaGenerator java = new JavaGenerator(builtFromString, javaPath, classToDump)) {
            String path = java.getFileName();
            System.err.println("Writing java to " + path);
            try (PrintWriter pw = new PrintWriter(new File(path))) {
                pw.println(java.generate());
            }
        }


        // create the java read code, we can use the setters we've found

        // some people take constructors.
        // we can do that by making either:
        // a) a list of getLongs()
        // b) a list of longs assigned from getLong

    }

}
