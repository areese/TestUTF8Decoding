// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
#include <jni.h>
#include <stdio.h>
#include "com_yahoo_example_test_SampleJni.h"

#include "generateSample.h"

#define TEST_LONG 0x7FCAFEF00DBEEFDE
#define SUPER_LONG_STRING "// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms. \
    // Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms. \
    // Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms. \
"

//#define TEST_INT  0x7FCAFEF00DBEEFDE

/*
 * Class:     com_yahoo_example_test_SampleJni
 * Method:    nativeSampleInfo
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_example_test_SampleJni_nativeSampleInfo
(JNIEnv *jenv, jclass, jlong address, jlong len) {

    if (0 == address || 0 == len) {
        return;
    }

    SampleInfoStruct data;

    data.type = TEST_LONG;
    data.attrs = TEST_LONG;
    data.status = TEST_LONG;
    data.expiration = TEST_LONG;
    data.readCount = TEST_LONG;
    data.writeCount = TEST_LONG;
    data.writeTimestamp = TEST_LONG;

    data.ia.constCharPtr = 0;
    data.ia.len = 0;

    data.org.constCharPtr = SUPER_LONG_STRING;
    data.org.len = strlen(SUPER_LONG_STRING);

    data.loc.constCharPtr = "locAddress";
    data.loc.len = strlen("locAddress");

    data.ccode.constCharPtr = "ccodeAddress";
    data.ccode.len = strlen("ccodeAddress");

    data.desc.constCharPtr = "descAddress";
    data.desc.len = strlen("descAddress");

    encodeIntoJava_SampleInfo(&data, address, len);
}

/*
 * Class:     com_yahoo_example_test_SampleJni
 * Method:    put4Bytes
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_example_test_SampleJni_put4Bytes
(JNIEnv *jenv, jclass, jlong address, jlong len) {

    if (0 == address || 4 != len) {
        return;
    }

    char fourBytes[]= {0xDE,0xAD,0xF0,0x0D};

    memcpy((void*)address, fourBytes, len);

}

