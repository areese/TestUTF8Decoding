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

    private void printFunctionDecl() {
        printFunctionDef();
        pw.println(";");
        pw.println();

    }

}
