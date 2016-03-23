// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public enum CTYPES {
    BYTE(8, 0, 0), //
    SHORT(8, 0, 0), //
    INT(8, 0, 0), //
    LONG(8, 0, 0), //
    STRING(8, 8, 1024), //
    INETADDRESS(8, 8, 16), // sockaddr_in6 from C, InetAddress in java.
    ;

    public final int fieldOffset;
    public final int allocationSize;
    public final String fieldSizeConstantName;
    public final int addressSize; // 8 for everything now.
    public final int lengthSize; // 8 more if it requires a length, so fieldOffset = addressSize + lengthSize

    CTYPES(int addressSize, int lengthSize, int allocationSize) {
        this.addressSize = addressSize;
        this.lengthSize = lengthSize;
        this.fieldOffset = this.addressSize + this.lengthSize;
        this.allocationSize = allocationSize;
        this.fieldSizeConstantName = this.name().toUpperCase() + "_FIELD_SIZE";
    }

    public static CTYPES getCType(Class<?> type) {
        switch (type.getName()) {
            case "byte":
                return CTYPES.BYTE;

            case "short":
                return CTYPES.SHORT;

            case "int":
                return CTYPES.INT;

            case "long":
                return CTYPES.LONG;

            case "java.lang.String":
                return CTYPES.STRING;

            case "java.net.InetAddress":
                return CTYPES.INETADDRESS;

            default:
                return null;
        }
    }
}
