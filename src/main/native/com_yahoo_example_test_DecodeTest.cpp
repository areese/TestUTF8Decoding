// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
#include <jni.h>
#include <stdio.h>
#include <stdint.h>
#include "com_yahoo_example_test_DecodeTest.h"

static void dump(jlong address, jlong len) {
    uint8_t *bytes = (uint8_t*) address;
    fprintf(stderr, "addess: %p len: %ld\n", (void*) address, len);
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
    uint32_t origInt = (uint32_t) 0xDEADBEEFCAFEC010L;

    dump(address, len);

    if (len == sizeof(uint64_t)) {
        dump((jlong) & origLong, sizeof(origLong));
        return (*(uint64_t*) address) == origLong ? JNI_TRUE : JNI_FALSE;
    } else if (len == sizeof(uint32_t)) {
        dump((jlong) & origInt, sizeof(origInt));
        return (*(uint32_t*) address) == origInt ? JNI_TRUE : JNI_FALSE;
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
