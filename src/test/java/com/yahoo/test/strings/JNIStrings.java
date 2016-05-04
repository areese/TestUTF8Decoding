package com.yahoo.test.strings;

import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

public class JNIStrings {
    // 0,1,2,3 == 1,10,100,1000 chars
    static final int[] ids = new int[] {1, 10, 100, 100};
    static final String[] strings = new String[] {"1", "0123456789",
                    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz23456789012345678901234567890123456789",
                    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz23456789012345678901234567890123456789"};

    static final native String jniString(int id);

    /**
     * 2 bytes are length,the rest are null terminated string
     * 
     * @param id of string to return
     * @return string
     */
    static final native long encodedString(int id);

    /**
     * Used to free the string returned from encodedString
     * 
     * @param address
     */
    static final native void freeString(long address);

    /**
     * 2 bytes are length,the rest are null terminated string
     * 
     * @param id of string to return
     * @param address unsafe allocated
     * @param length length to write to
     */
    static final native void unsafeString(int id, long address, long length);

    public static final String getUnsafeString(int id) {
        try (MissingFingers mf = new MissingFingers(ids[id] + 2)) {
            unsafeString(id, mf.getAddress(), mf.getLength());
            return MUnsafe.decodeString(mf);
        }
    }

    public static final String getString(int id) {
        return jniString(id);
    }

    public static final String getEncodedString(int id) {
        long address = encodedString(id);
        String ret = MUnsafe.decodeStringWithLength(address);
        freeString(address);
        return ret;
    }
}
