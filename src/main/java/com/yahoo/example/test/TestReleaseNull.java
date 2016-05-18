package com.yahoo.example.test;

import com.yahoo.example.testutf8.JniLibraryLoader;

/**
 * This class exists to see if calling Release[String,Byte]ArrayElements actually requires the reference.
 * 
 * @author areese
 *
 */
public class TestReleaseNull {

    static {
        JniLibraryLoader.load();
    }

    public static native void testString(String s);

    public static native void testByte(byte[] b, int mode);

    public static void main(String[] args) {
        testByte("abcdefghi".getBytes(), Integer.valueOf(args[0]));
    }

}
