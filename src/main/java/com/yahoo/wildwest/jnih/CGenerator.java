package com.yahoo.wildwest.jnih;


public class CGenerator extends AbstractGenerator {
    private String structName;

    public CGenerator(Class<?> classToDump) {
        super(classToDump);
        structName = shortObjectName + "Struct";;
    }

    private void createCStruct() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        printWithTab("typedef struct " + structName + " {\n");

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    printWithTab("uint64_t " + field.getName() + "BytesAddress;\n");
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

    private void createEncodeFunction() {
        printFunctionHeaderComment();
        pw.println("void encodeIntoJava_" + shortObjectName + "(" + structName
                        + " inputData, long toAddress, long addressLength) {");

        // Next we iterate over the object and generate the fields.
        // we'll have to do the c version of java unsafe.putMemory.
    }



    private void printFunctionHeaderComment() {
        pw.println("/**");
        pw.println("* This function was auto-generated");
        pw.println("* Given an allocated long addres, len tuple");
        pw.println(" * It will encode in a way compatible with the generated java.");
        pw.println("* everything is 64bit longs with a cast");
        pw.println("* Strings are considered UTF8, and are a tuple of address + length");
        pw.println("* Due to native memory tracking, strings are prealloacted with Unsafe.allocateMemory and assigned an output length");
        pw.println("* Similiar to how a c function would take char *outBuf, size_t bufLen");
        pw.println("* The length coming in says how large the buffer for address is.");
        pw.println("* The length coming out says how many characters including \\0 were written");
        pw.println("**/");
    }

    @Override
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.
        createCStruct();

        // now we can write the encode function.
        createEncodeFunction();

        // TODO Auto-generated method stub
        return null;
    }


}
