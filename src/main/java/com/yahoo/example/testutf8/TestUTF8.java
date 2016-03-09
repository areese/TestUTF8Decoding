// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.testutf8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TestUTF8 {
    public static void main(String[] args) throws Exception {
        String encoded =
                        "%E6%8C%91%E6%88%B0%E5%85%A8%E5%8F%B0%E6%9C%80%E4%BD%8E%E5%83%B9%F0%9F%8D%85%E4%BA%BA%E6%B0%A3";
        String decoded = URLDecoder.decode(encoded, "UTF-8");

        testJava(decoded);
        testJNI(decoded);
    }

    private static void testJNI(String decoded) throws UnsupportedEncodingException {
        byte[] raw = decoded.getBytes("UTF-8");
        System.out.println("java");
        for (byte b : raw) {
            System.out.print(Integer.toHexString(Byte.toUnsignedInt(b)));
        }
        System.out.println();
    }

    private static void testJava(String utf8encoded) {
        JNITest.dumpDecodedBytes(utf8encoded);
    }
}
