// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;

public abstract class AbstractGenerator implements Closeable {
    static final String FOUR_SPACE_TAB = "    ";

    protected final Class<?> objectClass;
    protected final String objectClassName;
    protected final String shortObjectName;
    protected final String javaPath;
    protected final boolean spewDebugging = false;


    public AbstractGenerator(Class<?> classToDump) {
        this.objectClass = classToDump;
        this.objectClassName = this.objectClass.getName();
        String[] temp = objectClassName.split("\\.");
        this.shortObjectName = temp[temp.length - 1];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length - 1; i++) {
            sb.append(temp[i]).append("/");
        }
        javaPath = sb.toString();
    }

    public void printWithTabs(LinePrinter lp, int tabs, String s) {
        for (int i = 0; i < tabs; i++) {
            lp.print(FOUR_SPACE_TAB);
        }
        lp.println(s);
    }

    public void printWith2Tabs(LinePrinter lp, String s) {
        printWithTabs(lp, 2, s);
    }

    public void printWithTab(LinePrinter lp, String s) {
        printWithTabs(lp, 1, s);
    }

    /**
     * Helper function to walk the fields of a class and write out either jni or java wrapper bits we'll need.
     * 
     * @param objectClass Class to operate on, expects primitives + strings, no arrays
     * @param pt lambda to invoke on each field that is a primitive or string.
     */
    public static void parseObject(Class<?> objectClass, ProcessType pt) {
        for (Field field : objectClass.getDeclaredFields()) {

            Class<?> type = field.getType();

            // we can also deal with InetAddress, or at least we can TODO it. It's either 4 bytes of address bytes in a
            // long + len = 0, or it's a long + len pointing at 16 bytes of address bytes.
            // see powersaw, which started this whole mess.
            // we use isInstance to see if it's a String, as isInstance takes Object, and isAssignableFrom takes Class.
            // oops.
            // if (!type.isPrimitive() && !type.isInstance("") && !type.isAssignableFrom(InetAddress.class)
            // || type.isArray()) {
            // continue;
            // }

            CTYPES ctype = CTYPES.getCType(type);
            if (null == ctype) {
                // rather than a magic check above, let the enum decide what we're doing.
                continue;
            }

            pt.process(ctype, field, type);
        }
    }

    @Override
    public void close() throws IOException {
        // pw.close();
        // sw.close();
    }


    public abstract String generate();
}
