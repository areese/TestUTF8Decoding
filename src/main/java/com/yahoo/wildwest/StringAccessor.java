// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * This class exists to allow easier access to sending Strings to JNI. per Oracle support and
 * http://banachowski.com/deprogramming/2012/02/working-around-jni-utf-8-strings/, we are probably better off encoding
 * UTF8 in java, then using copyMemory to send things along
 * 
 * Results are returned as long[]{address, length}
 * 
 * @author areese
 *
 */
public class StringAccessor {

    /**
     * Idea is to take a string, and use a bytebuffer from the pool to encode it to UTF-8 Then to generate a set of
     * longs to pass to the JNI that is real utf-8 encoded data.
     * 
     */


    // need to determine if we should have an option for a threadlocal version of this.

    // CharsetEncoder encoder = UTF_8.newEncoder();
    // assuming we use TLS, we'll need to reset it.
    // encoder.reset();

    // figure out the biggest this String could be.
    // Assuming we use ByteBufferPools we'll need to figure out the max size
    // and request that from the pool.
    // int maxBufferRequired = Math.round(encoder.maxBytesPerChar() * input.length());

    // encoder.encode(input.)

    public static MissingFingers encodeUTF8(String input) {
        // java.lang.String is probably smarter than we are.
        // need to benchmark the competition here.
        byte[] bytes = input.getBytes(UTF_8);
        return new MissingFingers(bytes);
    }
}
