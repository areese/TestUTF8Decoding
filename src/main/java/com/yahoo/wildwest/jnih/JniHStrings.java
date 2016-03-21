package com.yahoo.wildwest.jnih;

public class JniHStrings {
    static String STRUCT = "" + "typedef struct DATASTRUCT {\n" + //
                    "    union {\n" + //
                    "        uint8_t byteArray[8];\n" + //
                    "        uint8_t byteVal;\n" + //
                    "        uint16_t shortArray[4];\n" + //
                    "        uint16_t shortVal;\n" + //
                    "        uint32_t intArray[4];\n" + //
                    "        uint32_t intVal;\n" + //
                    "        uint64_t longArray[1];\n" + //
                    "        uint64_t longVal;\n" + //
                    "    };\n" + //
                    "} DATASTRUCT;\n"; //
}
