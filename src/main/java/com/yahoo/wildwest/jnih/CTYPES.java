// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public enum CTYPES {
    BYTE(8, 0, 0, true), //
    SHORT(8, 0, 0, true), //
    INT(8, 0, 0, true), //
    LONG(8, 0, 0, true), //
    STRING(8, 8, 1024, false), //
    INETADDRESS(8, 8, 16, false), // sockaddr_in6 from C, InetAddress in java.
    BYTEARRAY(8, 8, 1024, false), // byte arrays are the same as strings.
    ;

    public final int fieldOffset;
    public final int allocationSize;
    public final String fieldSizeConstantName;
    public final int addressSize; // 8 for everything now.
    public final int lengthSize; // 8 more if it requires a length, so fieldOffset = addressSize + lengthSize
    public final String dataSizeConstantAppender;
    private final boolean isPrimitive;

    CTYPES(int addressSize, int lengthSize, int allocationSize, boolean isPrimitive) {
        this.addressSize = addressSize;
        this.lengthSize = lengthSize;
        this.fieldOffset = this.addressSize + this.lengthSize;
        this.allocationSize = allocationSize;
        this.fieldSizeConstantName = this.name().toUpperCase() + "_FIELD_SIZE";
        this.dataSizeConstantAppender = "_DATA_SIZE";
        this.isPrimitive = isPrimitive;
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

            case "[B":
                return CTYPES.BYTEARRAY;

            case "java.lang.String":
                return CTYPES.STRING;

            case "java.net.InetAddress":
                return CTYPES.INETADDRESS;

            default:
                return null;
        }
    }

    public boolean isSupportedPrimitive() {
        return isPrimitive;
    }
}
