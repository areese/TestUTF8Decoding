package com.yahoo.wildwest.jnih;

public class HGenerator extends AbstractCGenerator {

    public HGenerator(Class<?> classToDump, String cFilename) {
        super(classToDump, cFilename);
    }

    protected void printStartGuard() {
        pw.println("#ifndef _Included_" + shortCFilename);
        pw.println("#define _Included_" + shortCFilename);
        pw.println();
    }

    protected void printEndGuard() {
        pw.println("#endif /* _Included_" + shortCFilename + "*/");
    }


    protected void printAddressUnion() {
        pw.println();
        pw.println("#ifndef _generatedAddressUnion");
        pw.println("#define _generatedAddressUnion");
        pw.println("typedef struct AddressUnion {");
        printWithTab("union {");
        printWith2Tabs("uint64_t address;");
        printWith2Tabs("void *voidPtr;");
        printWith2Tabs("const char *constCharPtr;");
        printWithTab("};");
        // FIXME: we should use this struct instead of the address+len pairs everywhere.
        printWithTab("// uint64_t len");
        pw.println("} AddressUnion;");
        pw.println("#endif /* _generatedAddressUnion */");
        pw.println();
    }

    private void printFunctionDecl() {
        printFunctionHeaderComment();
        pw.println("void encodeIntoJava_" + shortObjectName + "(" + structName
                        + " inputData, long address, long addressLength);");
        pw.println();
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
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.

        printStartGuard();
        printHeaderFileIncludes();
        printAddressUnion();
        createCStruct();
        printFunctionDecl();
        printEndGuard();

        return sw.toString();
    }

}
