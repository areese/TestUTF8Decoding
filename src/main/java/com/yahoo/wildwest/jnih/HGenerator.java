package com.yahoo.wildwest.jnih;

public class HGenerator extends AbstractCGenerator {

    public HGenerator(Class<?> classToDump, String cFilename) {
        super(classToDump, cFilename);
    }


    @Override
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.
        createCStruct();
        return sw.toString();
    }

}
