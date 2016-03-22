// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public enum CTYPES {
    BYTE(8, 0), //
    SHORT(8, 0), //
    INT(8, 0), //
    LONG(8, 0), //
    STRING(16, 1024), //
    INETADDRESS(16, 16), // sockaddr_in6 from C, InetAddress in java.
    ;

    public final int fieldOffset;
    public final int allocationSize;

    CTYPES(int fieldOffset, int allocationSize) {
        this.fieldOffset = fieldOffset;
        this.allocationSize = allocationSize;
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
