package com.yahoo.wildwest.jnih;

public class CGenerator extends AbstractCGenerator {

    public CGenerator(Class<?> classToDump, String cFilename) {
        super(classToDump, cFilename);
    }

    private void printIncludes() {
        printHeaderFileIncludes();
        pw.println("#include \"" + shortCFilename + ".h\"");
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
