// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public class HGenerator extends AbstractCGenerator {

    public HGenerator(String builtFromString, Class<?> classToDump, String cFilename) {
        super(builtFromString, classToDump, cFilename);
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
        printWithTab(pw, "union {");
        printWith2Tabs(pw, "uint64_t address;");
        printWith2Tabs(pw, "void *voidPtr;");
        printWith2Tabs(pw, "const char *constCharPtr;");
        printWithTab(pw, "};");
        printWithTab(pw, "uint64_t len;");
        pw.println("} AddressUnion;");
        pw.println("#endif /* _generatedAddressUnion */");
        pw.println();
    }



    @Override
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.

        printGeneratedFromHeader(pw);

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
