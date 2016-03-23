// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public abstract class AbstractCGenerator extends AbstractGenerator {
    protected final String structName;
    protected final String cFilename;
    protected final String shortCFilename;

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
            switch (ctype) {
                case STRING:
                case INETADDRESS:
                    printWithTab("uint64_t " + field.getName() + "Address;\n");
                    printWithTab("uint64_t " + field.getName() + "Len;\n");
                    break;

                case LONG:
                case INT:
                    // yes we waste 32 bits.
                case SHORT:
                    // yes we waste 48 bits.
                case BYTE:
                    // yes we waste 56 bits.
                    printWithTab("uint64_t " + field.getName() + "; // " + type.getName() + "\n");
                    break;

                default:
                    printWithTab("// TODO : DATASTRUCT " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

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

    protected void createEncodeFunction() {
        // Next we iterate over the object and generate the fields.
        // we'll have to do the c version of java unsafe.putMemory.
        // we can assume that they passed in the struct.
        // or they changed it by hand, and happened to use a struct of a different layout but with the same names.
        // and use memcpy.

        printWithTab("uint64_t offset = 0;");

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    printStringCopy(field.getName(), type.getName());
                    break;

                case INETADDRESS:
                    printInetAddress(field.getName(), type.getName());
                    break;

                case LONG:
                case INT:
                    // yes we waste 32 bits.
                case SHORT:
                    // yes we waste 48 bits.
                case BYTE:
                    // yes we waste 56 bits.
                    // print the variable;
                    printWithTab("{");
                    printPointerVariable(field.getName(), type.getName());
                    printSetPointerVariable(field.getName());
                    printWithTab("}");
                    break;

                default:
                    printWithTab("// TODO : DATASTRUCT " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });
        pw.println("}");
    }


    private void printSetPointerVariable(String name) {
        printWith2Tabs("(*" + name + "Ptr) = " + "inputData." + name + ";");
    }

    /**
     * This function generates the memcpy that's required for a String. Strings are address + length. And the
     * destination is pre allocated by the java code. This means we have to: 1. write into dest address. 2. update
     * length to reflect what we wrote.
     * 
     * @param name of the variable to copy
     */
    private void printStringCopy(String name, String typeName) {
        // string is a memcpy into provided address, followed by update length.
        // at offset, is an address
        printWithTab("{");
        String addressVariableName = name + "Address";
        String lenVariableName = name + "Len";
        String lenPtrVariableName = name + "LenPtr";
        String dereferencedLenPtrVariableName = "(*" + lenPtrVariableName + ")";

        printLenAndPointerVariable(name, typeName);

        // we need to check len, we want the shorter of the to.
        // and we need to set it when we are done.
        printWith2Tabs("// use the shortest of buffersize and input size");
        printWith2Tabs(dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName + ", inputData."
                        + lenVariableName + ");");

        pw.println();

        // now we have address "pointer" and len "pointer" we can use memcpy
        printWith2Tabs("memcpy ((void*) " + addressVariableName + "Ptr, (void*) inputData." + addressVariableName
                        + ", " + dereferencedLenPtrVariableName + ");");

        printWithTab("}");
        pw.println();
    }

    private void printLenAndPointerVariable(String name, String typeName) {
        printPointerVariable(name + "Address", typeName);
        pw.println();

        printPointerVariable(name + "Len", typeName);
        pw.println();
    }

    private void printPointerVariable(String addressVariableName, String typeName) {
        printWith2Tabs("uint64_t *" + addressVariableName + "Ptr = (uint64_t*)(address + offset); // " + typeName);
        printWith2Tabs("offset += 8;");
    }

    /**
     * This function generates the memcpy that's required for a String. Strings are address + length. And the
     * destination is pre allocated by the java code. This means we have to: 1. write into dest address. 2. update
     * length to reflect what we wrote.
     * 
     * @param name of the variable to copy
     */
    private void printInetAddress(String name, String typeName) {
        // string is a memcpy into provided address, followed by update length.
        // at offset, is an address
        String addressVariableName = name + "Address";
        String lenVariableName = name + "Len";
        String lenPtrVariableName = name + "LenPtr";
        String dereferencedLenPtrVariableName = "(*" + lenPtrVariableName + ")";

        printWithTab("{");
        printLenAndPointerVariable(name, typeName);

        // we need to deal with the type here. it's a sockaddr_storage that we need to target.

        // we need to check len, we want the shorter of the to.
        // and we need to set it when we are done.
        // printWithTab(dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName + ", inputData."
        // + lenVariableName + ");");

        pw.println();

        // now we have address "pointer" and len "pointer" we can use memcpy
        // printWithTab("memcpy ((void*) " + addressVariableName + ", (void*) inputData." + addressVariableName + ", "
        // + dereferencedLenPtrVariableName + ");");

        printWithTab("}");
        pw.println();
    }

}
