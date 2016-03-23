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
        printPointerVariableWithDereference(name + "Address", typeName);
        pw.println();

        printPointerVariable(name + "Len", typeName);
        pw.println();
    }

    private void printPointerVariableWithDereference(String addressVariableName, String typeName) {
        printWith2Tabs("uint64_t *" + addressVariableName + "Ptr = *(uint64_t**)(address + offset); // " + typeName);
        printWith2Tabs("offset += 8;");
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
        // printWithTab(dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName + ", inputData->"
        // + lenVariableName + ");");

        pw.println();

        // now we have address "pointer" and len "pointer" we can use memcpy
        // printWithTab("memcpy ((void*) " + addressVariableName + ", (void*) inputData->" + addressVariableName + ", "
        // + dereferencedLenPtrVariableName + ");");

        printWithTab("}");
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
        printWith2Tabs("(*" + name + "Ptr) = " + "inputData->" + name + ";");
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
        printDumpWrittenData("at start");

        String addressVariableName = name + "Address";
        String lenVariableName = name + "Len";
        String lenPtrVariableName = name + "LenPtr";
        String dereferencedLenPtrVariableName = "(*" + lenPtrVariableName + ")";

        printLenAndPointerVariable(name, typeName);

        // we need to check len, we want the shorter of the to.
        // and we need to set it when we are done.
        printWith2Tabs("// use the shortest of buffersize and input size");
        printWith2Tabs(dereferencedLenPtrVariableName + " = MIN( " + dereferencedLenPtrVariableName + ", inputData->"
                        + lenVariableName + ");");

        pw.println();

        printDumpWrittenData("Before copy of " + addressVariableName);

        // now we have address "pointer" and len "pointer" we can use memcpy
        printWith2Tabs("fprintf(stderr, \"encoding to 0x%lx of len 0x%lx\\n\", " + addressVariableName + "Ptr, "
                        + dereferencedLenPtrVariableName + ");");

        printWith2Tabs("memcpy ((void*) " + addressVariableName + "Ptr, (void*) inputData->" + addressVariableName
                        + ", " + dereferencedLenPtrVariableName + ");");

        printDumpWrittenData("After copy of " + addressVariableName);

        printWithTab("}");
        pw.println();
    }

    private void printDumpWritingTo(String stage, String address, String addressLength) {
        printWithTabs(3, "fprintf(stderr,\"Writing bytes to 0x%llx len 0x%llx at " + stage + "\\n\", " + address + ", "
                        + addressLength + ");");
    }

    private void printDumpWrittenData(String stage) {
        printWithTabs(2, "{");
        printWithTabs(3, "uint64_t *ptr = (uint64_t *)address;");
        printWithTabs(3, "fprintf(stderr,\"Dumping address at " + stage
                        + " 0x%llx len 0x%llx\\n\", address, addressLength);");
        printWithTabs(3, "for (long l=0; l<addressLength/sizeof(uint64_t); l++) {");
        // printWith2Tabs("if (l % 32 == 0) { fprintf(stderr,\"\\n\"); }");
        printWithTabs(4, "fprintf(stderr,\"%llx\\n\",ptr[l]);");
        printWithTabs(3, "}");
        printWithTabs(3, "fprintf(stderr,\"\\n\\n\");");
        printWithTabs(2, "}");
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
