// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.testutf8;

public class JNITest {
    static {
        JniLibraryLoader.load();
    }

    public static final native void dumpDecodedBytes(String utf8encoded);
}
