#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/param.h>
#include "com_yahoo_test_strings_JNIStrings.h"

int max = 4;
int lengths[] = { 0, 1, 100, 100 };
char strings[4][200] =
        { "1", "0123456789",
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz23456789012345678901234567890123456789",
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz23456789012345678901234567890123456789" };

/*
 * Class:     com_yahoo_test_strings_JNIStrings
 * Method:    jniString
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_yahoo_test_strings_JNIStrings_jniString(
        JNIEnv *jenv, jclass, jint id) {
    if (id >= max) {
        return NULL;
    }

    return jenv->NewStringUTF(strings[id]);
}

/*
 * Class:     com_yahoo_test_strings_JNIStrings
 * Method:    encodedString
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_yahoo_test_strings_JNIStrings_encodedString(
        JNIEnv *jenv, jclass, jint id) {
    const char *srcString = strings[id];
    uint32_t len = strlen(srcString);
    // 4 for the int, 1 for the null, 1 for buffer
    void *ptr = (void*) calloc(4 + len + 2, sizeof(char));
    if (NULL == ptr) {
        return 0;
    }

    uint32_t * intPtr = (uint32_t*) ptr;
    *intPtr = len;

    char *stringPtr = (char*) (intPtr + 1);
    strncpy(stringPtr, srcString, len);

    return (jlong) ptr;
}

/*
 * Class:     com_yahoo_test_strings_JNIStrings
 * Method:    freeString
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_test_strings_JNIStrings_freeString
(JNIEnv *jenv, jclass, jlong address)
{
    if (0 == address) {
        return;
    }

    free((void*)address);
}

/*
 * Class:     com_yahoo_test_strings_JNIStrings
 * Method:    unsafeString
 * Signature: (IJJ)V
 */
JNIEXPORT void JNICALL Java_com_yahoo_test_strings_JNIStrings_unsafeString
(JNIEnv *jenv, jclass, jint id, jlong address, jlong length) {
    if (0 == address || 0 == length) {
        return;
    }

    const char *srcString = strings[id];
    uint32_t len = MIN((uint32_t)length-1, strlen(srcString));

    char *stringPtr = (char*) address;
    strncpy(stringPtr, srcString, len);
}

