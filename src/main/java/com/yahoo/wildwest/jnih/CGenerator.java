package com.yahoo.wildwest.jnih;


public class CGenerator extends AbstractGenerator {
    public CGenerator(Class<?> classToDump) {
        super(classToDump);
    }

    public String createCStruct() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        StringBuilder structString = new StringBuilder();
        String structName = shortObjectName + "Struct";
        structString.append("typedef struct " + structName + " {\n");

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    structString.append(FOUR_SPACE_TAB + "uint64_t " + field.getName() + "BytesAddress;\n");
                    structString.append(FOUR_SPACE_TAB + "uint64_t " + field.getName() + "Len;\n");
                    break;

                case LONG:
                case INT:
                    // yes we waste 32 bits.

                case BYTE:
                    // yes we waste 56 bits.
                        structString.append(FOUR_SPACE_TAB + "uint64_t " + field.getName() + "; // " + type.getName()
                                        + "\n");
                    break;

                default:
                    structString.append(FOUR_SPACE_TAB + "DATASTRUCT " + field.getName() + "; // " + type.getName()
                                    + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });

        structString.append("} " + structName + ";\n");

        return structString.toString();

        // return fields;
    }

    @Override
    public String generate() {
        // TODO Auto-generated method stub
        return null;
    }


}
