package com.yahoo.wildwest.jnih;

public class CGenerator extends AbstractCGenerator {

    public CGenerator(Class<?> classToDump, String cFilename) {
        super(classToDump, cFilename);
    }

    private void printIncludes() {
        printHeaderFileIncludes();
        pw.println("#include \"" + shortCFilename + ".h\"");
    }

    private void printLenAndPointerVariable(String name, String typeName) {
        printPointerVariableWithDereference(name + "Ptr", typeName);
        pw.println();

        printPointerVariable(name + "Len", typeName);
        pw.println();
    }

    private void printPointerVariableWithDereference(String addressVariableName, String typeName) {
        printWith2Tabs(pw, "uint64_t *" + addressVariableName + " = *(uint64_t**)(address + offset); // " + typeName);
        printWith2Tabs(pw, "offset += 8;");
    }

    private void printPointerVariable(String addressVariableName, String typeName) {
        printWith2Tabs(pw, "uint64_t *" + addressVariableName + "Ptr = (uint64_t*)(address + offset); // " + typeName);
        printWith2Tabs(pw, "offset += 8;");
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

        printWithTab(pw, "{");
        printLenAndPointerVariable(name, typeName);

        // we need to deal with the type here. it's a sockaddr_storage that we need to target.

        // we need to check len, we want the shorter of the to.
        // and we need to set it when we are done.
        // printWithTab(pw, dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName +
        // ", inputData->"
        // + lenVariableName + ");");

        pw.println();

        // now we have address "pointer" and len "pointer" we can use memcpy
        // printWithTab(pw, "memcpy ((void*) " + addressVariableName + ", (void*) inputData->" + addressVariableName +
        // ", "
        // + dereferencedLenPtrVariableName + ");");

        printWithTab(pw, "}");
        pw.println();
    }

    protected void createEncodeFunction() {
        printFunctionDef();
        pw.println(" {");
        // Next we iterate over the object and generate the fields.
        // we'll have to do the c version of java unsafe.putMemory.
        // we can assume that they passed in the struct.
        // or they changed it by hand, and happened to use a struct of a different layout but with the same names.
        // and use memcpy.

        printWithTab(pw, "if (0 == address || 0 == addressLength) {");
        printWith2Tabs(pw, "return;");
        printWithTab(pw, "}");
        pw.println();

        printWithTab(pw, "uint64_t offset = 0;");

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case BYTEARRAY:
                case STRING:
                    printCopyBytes(field.getName(), type.getName());
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
                    printWithTab(pw, "{");
                    printPointerVariable(field.getName(), type.getName());
                    printSetPointerVariable(field.getName());
                    printWithTab(pw, "}");
                    break;

                default:
                    printWithTab(pw, "// TODO : DATASTRUCT " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });
        pw.println("}");
    }


    private void printSetPointerVariable(String name) {
        printWith2Tabs(pw, "(*" + name + "Ptr) = " + "inputData->" + name + ";");
    }

    /**
     * This function generates the memcpy that's required for a String. Strings are address + length. And the
     * destination is pre-allocated by the java code. This means we have to: 1. write into dest address. 2. update
     * length to reflect what we wrote.
     * 
     * @param name of the variable to copy
     */
    private void printCopyBytes(String name, String typeName) {
        // string is a memcpy into provided address, followed by update length.
        // at offset, is an address
        printWithTab(pw, "{");

        debugWrittenData("at start");

        String srcAddressVariableName = name + ".voidPtr";
        String dstAddressVariableName = name + "Ptr";
        String lenVariableName = name + ".len";
        String lenPtrVariableName = name + "LenPtr";
        String dereferencedLenPtrVariableName = "(*" + lenPtrVariableName + ")";

        printLenAndPointerVariable(name, typeName);

        // we need to check len, we want the shorter of the to.
        // and we need to set it when we are done.
        printWith2Tabs(pw, "// use the shortest of buffersize and input size");
        printWith2Tabs(pw, dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName
                        + ", inputData->" + lenVariableName + ");");

        pw.println();

        debugWrittenData("Before copy of " + srcAddressVariableName);
        debugEncoding(srcAddressVariableName, dereferencedLenPtrVariableName);


        printWith2Tabs(pw, "if (NULL != " + dstAddressVariableName);
        printWithTabs(pw, 4, " && NULL != inputData->" + srcAddressVariableName);
        printWithTabs(pw, 4, " && NULL != " + lenPtrVariableName + ") {");
        printWithTabs(pw, 4, "memcpy ((void*) " + dstAddressVariableName + ", inputData->" + srcAddressVariableName
                        + ", " + dereferencedLenPtrVariableName + ");");
        printWith2Tabs(pw, "}");
        debugWrittenData("After copy of " + srcAddressVariableName);

        printWithTab(pw, "}");
        pw.println();
    }

    private void debugEncoding(String srcAddressVariableName, String dereferencedLenPtrVariableName) {
        if (spewDebugging) {
            // now we have address "pointer" and len "pointer" we can use memcpy
            printWith2Tabs(pw, "fprintf(stderr, \"encoding to 0x%llx of len 0x%llx\\n\", inputData->"
                            + srcAddressVariableName + ", " + dereferencedLenPtrVariableName + ");");
        }
    }

    private void debugWrittenData(String stage) {
        if (spewDebugging) {
            printWithTabs(pw, 2, "{");
            printWithTabs(pw, 3, "uint64_t *ptr = (uint64_t *)address;");
            printWithTabs(pw, 3, "fprintf(stderr,\"Dumping address at " + stage
                            + " 0x%lx len 0x%lx\\n\", address, addressLength);");
            printWithTabs(pw, 3, "for (long l=0; l<addressLength/sizeof(uint64_t); l++) {");
            // printWith2Tabs(pw, "if (l % 32 == 0) { fprintf(stderr,\"\\n\"); }");
            printWithTabs(pw, 4, "fprintf(stderr,\"%llx\\n\",ptr[l]);");
            printWithTabs(pw, 3, "}");
            printWithTabs(pw, 3, "fprintf(stderr,\"\\n\\n\");");
            printWithTabs(pw, 2, "}");
        }
    }

    @Override
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.

        printIncludes();

        // now we can write the encode function.
        createEncodeFunction();

        return sw.toString();
    }

}
