// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
#include <jni.h>
#include <stdio.h>
#include <stdint.h>
#include "com_yahoo_example_test_DecodeTest.h"

static void dump(jlong address, jlong len) {
    uint8_t *bytes = (uint8_t*) address;
    fprintf(stderr, "addess: %p len: %ld\n", bytes, len);
    fprintf(stderr, "        ");
    for (jlong i = 0; i < len; i++) {
        fprintf(stderr, "%02x", bytes[i]);
    }
    fprintf(stderr, "\n");
}

/*
 * Class:     com_yahoo_example_test_DecodeTest
 * Method:    dumpEncoded
 * Signature: (JJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_yahoo_example_test_DecodeTest_dumpEncoded(
        JNIEnv *jenv, jclass, jlong address, jlong len) {
    uint64_t origLong = 0xDEADBEEFCAFEC010L;
    uint32_t origInt = (uint32_t) origLong;
    uint16_t origShort = (uint32_t) origLong;
    uint8_t origByte = (uint32_t) origLong;

    dump(address, len);

    switch (len) {
    case 8:
        fprintf(stderr, "value:  %0lx\n", *((jlong*) address));
        dump((jlong) & origLong, sizeof(origLong));
        return (*(uint64_t*) address) == origLong ? JNI_TRUE : JNI_FALSE;

    case 4:
        fprintf(stderr, "value:  %0x\n", *((jint*) address));
        dump((jlong) & origInt, sizeof(origInt));
        return (*(uint32_t*) address) == origInt ? JNI_TRUE : JNI_FALSE;

    case 2:
        fprintf(stderr, "value:  %0x\n", *((jshort*) address));
        dump((jlong) & origShort, sizeof(origShort));
        return (*(uint16_t*) address) == origShort ? JNI_TRUE : JNI_FALSE;

    case 1:
        fprintf(stderr, "value:  %0x\n", *((jbyte*) address));
        dump((jlong) & origByte, sizeof(origByte));
        return (*(uint8_t*) address) == origByte ? JNI_TRUE : JNI_FALSE;

    default:
        return JNI_FALSE;
    }

    return JNI_FALSE;
}

/*
 * Class:     com_yahoo_example_test_DecodeTest
 * Method:    encodeInto
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_example_test_DecodeTest_encodeInto
(JNIEnv *jenv, jclass, jlong address, jlong len)
{

}

/*
 * Class:     com_yahoo_example_test_DecodeTest
 * Method:    dump
 * Signature: (JJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_yahoo_example_test_DecodeTest_dump(
        JNIEnv *jenv, jclass, jlong address, jlong len) {
    uint64_t *bytes = (uint64_t*) address;
    fprintf(stderr, "addess: %p len: %ld\n", bytes, len);
    for (jlong i = 0; i < len / 8; i++) {
        fprintf(stderr, "Getting %p at %0llx\n", &bytes[i], bytes[i]);
    }
    fprintf(stderr, "\n");

    return JNI_TRUE;
}
