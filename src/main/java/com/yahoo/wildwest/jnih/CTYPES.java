package com.yahoo.wildwest.jnih;


public enum CTYPES {
    BYTE, //
    INT, //
    LONG, //
    STRING;


    public static CTYPES getCType(Class<?> type) {
        switch (type.getName()) {
            case "byte":
                return CTYPES.BYTE;

            case "int":
                return CTYPES.INT;

            case "long":
                return CTYPES.LONG;

            case "java.lang.String":
                return CTYPES.STRING;

            default:
                return null;
        }
    }
}
