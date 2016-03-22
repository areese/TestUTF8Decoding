package com.yahoo.wildwest.jnih;


public class CGenerator extends AbstractGenerator {
    public CGenerator(Class<?> classToDump) {
        super(classToDump);
    }

    private void createCStruct() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        String structName = shortObjectName + "Struct";
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

                case BYTE:
                    // yes we waste 56 bits.
                        printWithTab("uint64_t " + field.getName() + "; // " + type.getName() + "\n");
                    break;

                default:
                    printWithTab("DATASTRUCT " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });

        pw.println("} " + structName + ";\n");
    }

    @Override
    public String generate() {
        // for c:
        // first write out the struct definition.
        // then we write the decode function.


        // TODO Auto-generated method stub
        return null;
    }


}
