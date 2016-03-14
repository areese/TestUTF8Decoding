// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.testutf8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class TestUTF8 {
    public static void main(String[] args) throws Exception {
        String encoded1 =
                        "%E6%8C%91%E6%88%B0%E5%85%A8%E5%8F%B0%E6%9C%80%E4%BD%8E%E5%83%B9"
                                        + "%F0%9F%8D%85%E4%BA%BA%E6%B0%A3";
        String encoded2 =
                        "%E6%8C%91%E6%88%B0%E5%85%A8%E5%8F%B0%E6%9C%80%E4%BD%8E%E5%83%B9"
                                        + "%9F%8D%85%E4%BA%BA%E6%B0%A3";

        System.out.println("test1");
        // testString(encoded1);
        // test 2.
        System.out.println("test2");
        testString(encoded2);

    }

    private static void testString(String encoded) throws Exception {
        String decoded = URLDecoder.decode(encoded, "UTF-8");

        System.out.println("java str:" + decoded);
        testUTF8Decoder(decoded);
        testJava(decoded);
        testJNI(decoded);
    }

    private static void testJNI(String decoded) {
        JNITest.dumpDecodedBytes(decoded);
    }

    private static void testJava(String decoded) throws UnsupportedEncodingException {
        byte[] raw = decoded.getBytes("UTF-8");
        System.out.println("java");
        for (byte b : raw) {
            System.out.print(Integer.toHexString((((int) b) & 0x00FF)));
        }
        System.out.println();
    }

    private static void testUTF8Decoder(String decoded) throws UnsupportedEncodingException, CharacterCodingException {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        CharBuffer in = CharBuffer.allocate(decoded.length());
        in.put(decoded, 0, decoded.length());
        in.flip();
        ByteBuffer encoded = encoder.encode(in);
        if (!encoded.hasArray()) {
            System.out.println("java charset adsadsadsaadfss");

        }

        System.out.println("java charset encoder");
        encoded.flip();
        byte[] array = encoded.array();
        for (byte b : encoded.array()) {
            System.out.print(Integer.toHexString((((int) b) & 0x00FF)));
        }
        System.out.println();
    }
}
