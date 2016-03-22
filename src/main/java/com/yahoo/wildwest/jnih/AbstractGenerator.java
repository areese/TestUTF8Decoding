// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public abstract class AbstractGenerator implements Closeable {
    static final String FOUR_SPACE_TAB = "    ";

    protected final Class<?> objectClass;
    protected final String objectClassName;
    protected final String shortObjectName;

    protected StringWriter sw = new StringWriter();
    protected PrintWriter pw = new PrintWriter(sw);

    public AbstractGenerator(Class<?> classToDump) {
        this.objectClass = classToDump;
        this.objectClassName = this.objectClass.getName();
        String[] temp = objectClassName.split("\\.");
        this.shortObjectName = temp[temp.length - 1];
    }

    public void printWithTab(String s) {
        pw.print(FOUR_SPACE_TAB);
        pw.println(s);
    }

    public void printOffset(int offsetBy, String fieldName, String typeName) {
        printWithTab("offset += " + offsetBy + "; // just read " + fieldName + " type " + typeName);
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

            if (!type.isPrimitive() && !type.isInstance("") || type.isArray()) {
                continue;
            }

            CTYPES ctype = CTYPES.getCType(type);

            pt.process(ctype, field, type);
        }
    }

    @Override
    public void close() throws IOException {
        pw.close();
        sw.close();
    }


    public abstract String generate();
}
