// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.IOException;
import java.io.StringWriter;

public abstract class AbstractCGenerator extends AbstractGenerator {
    protected final String structName;
    protected final String cFilename;
    protected final String shortCFilename;

    protected StringWriter sw = new StringWriter();
    protected PrintWriterWrapper pw = new PrintWriterWrapper(sw);

    public AbstractCGenerator(Class<?> classToDump, String cFilename) {
        super(classToDump);
        this.structName = shortObjectName + "Struct";
        this.cFilename = cFilename;
        String[] t = cFilename.split("/");
        this.shortCFilename = (t.length == 0) ? t[0] : t[t.length - 1];
    }

    protected void createCStruct() {
        pw.println("typedef struct " + structName + " {\n");

        parseObject(objectClass, (ctype, field, type) -> {
            String typeValue;
            switch (ctype) {
                case STRING:
                case BYTEARRAY:
                case INETADDRESS:
                    typeValue = "AddressUnion";
                    break;

                case LONG:
                case INT:
                    // yes we waste 32 bits.
                case SHORT:
                    // yes we waste 48 bits.
                case BYTE:
                    // yes we waste 56 bits.
                    typeValue = "uint64_t";
                    break;

                default:
                    typeValue = "// TODO : DATASTRUCT ";
                    break;

            }
            printWithTab(pw, typeValue + " " + field.getName() + "; // " + type.getName());

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });

        pw.println("} " + structName + ";\n");
    }

    protected void printHeaderFileIncludes() {
        pw.println("#include <sys/param.h>");
        pw.println("#include <stdio.h>");
        pw.println("#include <stdint.h>");
        pw.println("#include <string.h>");
        pw.println();
    }

    protected void printFunctionDef() {
        printFunctionHeaderComment();
        pw.print("void encodeIntoJava_" + shortObjectName + "(" + structName
                        + " *inputData, long address, long addressLength)");
    }

    private void printFunctionHeaderComment() {
        pw.println("/**");
        pw.println(" * This function was auto-generated");
        pw.println(" * Given an allocated long addres, len tuple");
        pw.println(" * It will encode in a way compatible with the generated java.");
        pw.println(" * everything is 64bit longs with a cast");
        pw.println(" * Strings are considered UTF8, and are a tuple of address + length");
        pw.println(" * Due to native memory tracking, strings are prealloacted with Unsafe.allocateMemory and assigned an output length");
        pw.println(" * Similiar to how a c function would take char *outBuf, size_t bufLen");
        pw.println(" * The length coming in says how large the buffer for address is.");
        pw.println(" * The length coming out says how many characters including \\0 were written");
        pw.println("**/");
    }


    @Override
    public void close() throws IOException {
        pw.close();
        sw.close();
    }

}
