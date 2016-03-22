// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
#include <jni.h>
#include <stdio.h>
#include "com_yahoo_example_test_SampleJniTest.h"

#include "generateSample.h"

/*
 * Class:     com_yahoo_example_test_SampleJniTest
 * Method:    nativeSampleInfo
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_example_test_SampleJniTest_nativeSampleInfo
(JNIEnv *jenv, jclass, jlong address, jlong len) {

    SampleInfoStruct data;

    data.type = 0;
    data.attrs = 1;
    data.status = 2;
    data.expiration = 3;
    data.readCount = 4;
    data.writeCount = 5;
    data.writeTimestamp = 6;

    data.iaAddress = 0;
    data.iaLen = 0;

    data.orgAddress = "orgAddress";
    data.orgLen = strlen("orgAddress");

    data.locAddress = "locAddress";
    data.locLen = strlen("locAddress");

    data.ccodeAddress = "ccodeAddress";
    data.ccodeLen = strlen("ccodeAddress");

    data.descAddress = "descAddress";
    data.descLen = strlen("descAddress");

    encodeIntoJava_SampleInfo(&data, address, len);
}
