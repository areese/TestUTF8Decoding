// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
#include <jni.h>
#include <stdio.h>
#include "com_yahoo_example_testutf8_JNITest.h"

/*
 * Class:     com_yahoo_example_testutf8_JNITest
 * Method:    dumpDecodedBytes
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_example_testutf8_JNITest_dumpDecodedBytes
(JNIEnv *jenv, jclass, jstring jstr)
{
    const char* cstr = (const char*)jenv->GetStringUTFChars(jstr, 0);
    size_t len = jenv->GetStringUTFLength(jstr);
    printf ("jni\n");

    for (size_t i=0;i<len;i++) {
        printf("%x",(unsigned char)cstr[i]);
    }
    printf ("\n");

    jenv->ReleaseStringUTFChars(jstr, cstr);
}
