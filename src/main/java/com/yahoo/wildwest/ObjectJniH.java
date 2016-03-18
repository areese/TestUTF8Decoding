package com.yahoo.wildwest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.yahoo.example.test.DumpTest;

/**
 * Given an SIMPLE object on the classpath Generate all of the stub code to copy it into a long/long (address, length)
 * combo that can be passed to jni and the static c function to decode it.
 * 
 * @author areese
 *
 */
public class ObjectJniH {
    static class Descriptor {
        static String STRUCT = "" + "typedef struct DATASTRUCT {\n" + //
                        "    union {\n" + //
                        "        uint8_t byteArray[8];\n" + //
                        "        uint8_t byteVal;\n" + //
                        "        uint16_t shortArray[4];\n" + //
                        "        uint16_t shortVal;\n" + //
                        "        uint32_t intArray[4];\n" + //
                        "        uint32_t intVal;\n" + //
                        "        uint64_t longArray[1];\n" + //
                        "        uint64_t longVal;\n" + //
                        "    };\n" + //
                        "} DATASTRUCT;\n"; //
    }

    enum CTYPES {
        BYTE, INT, LONG, STRING;
    }

    private Set<String> blacklistedMethods = new HashSet<>();

    public ObjectJniH() {
        for (Method m : Object.class.getMethods()) {
            blacklistedMethods.add(m.getName());
        }

        for (Method m : Class.class.getMethods()) {
            blacklistedMethods.add(m.getName());
        }
    }

    public List<Method> findGetters(Class objectClass) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        List<Method> getters = new LinkedList<>();

        for (Method m : objectClass.getMethods()) {
            String methodName = m.getName();

            if (blacklistedMethods.contains(methodName)) {
                System.err.println(methodName + " is from Object");
                continue;
            }

            if (m.getReturnType().isPrimitive() && m.getReturnType().equals(Void.TYPE)) {
                System.err.println(methodName + " returns void");
                continue;
            }

            if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
                System.err.println(methodName + " is not a getter");
                continue;
            }

            System.out.println("added " + methodName);
            getters.add(m);
        }

        return getters;
    }


    public List<Method> findSetters(Class objectClass) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        List<Method> setters = new LinkedList<>();

        for (Method m : objectClass.getMethods()) {
            String methodName = m.getName();

            if (blacklistedMethods.contains(methodName)) {
                System.err.println(methodName + " is from Object");
                continue;
            }

            if (!methodName.startsWith("set")) {
                System.err.println(methodName + " is not a setter");
                continue;
            }

            System.out.println("added " + methodName);
            setters.add(m);
        }

        return setters;
    }

    static interface ProcessType {
        void process(CTYPES ctype, Field field, Class<?> type);
    }

    public String createCStruct(Class objectClass) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        StringBuilder structString = new StringBuilder();

        String[] temp = objectClass.getName().split("\\.");
        String structName = temp[temp.length - 1] + "Struct";


        structString.append("typedef struct " + structName + " {\n");

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    structString.append("    uint64_t " + field.getName() + "Bytes;\n");
                    structString.append("    uint64_t " + field.getName() + "Len;\n");
                    break;

                case LONG:
                case INT:

                    structString.append("    uint64_t " + field.getName() + "; // " + type.getName() + "\n");
                    break;

                default:
                    structString.append("    DATASTRUCT " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
                    });

        structString.append("} " + structName + ";\n");

        return structString.toString();

        // return fields;
    }


    public String createJavaConstructor(Class objectClass) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        StringBuilder variablesString = new StringBuilder();

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    variablesString.append("    long " + field.getName() + "Len;\n");
                    variablesString.append("    byte[] " + field.getName() + "Bytes;\n");
                    // = new byte["+ field.getName() + "Len];\n");

                        variablesString.append("    String " + field.getName() + ";\n");
                    // variablesString.append(" = new String(" + field.getName()
                    // + "Bytes, StandardCharsets.UTF_8);\n");
                        break;

                case LONG:
                    variablesString.append("    long " + field.getName() + "; // " + type.getName() + "\n");
                    break;

                case INT:
                    variablesString.append("    int " + field.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        });

        StringBuilder getsBitsString = new StringBuilder();
        // assume address, len

        getsBitsString.append("long offset = 0;\n");

        String getValueString = "MUnsafe.unsafe.getLong(address + offset);";

        parseObject(objectClass, (ctype, field, type) -> {
            // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
            // wasting bits.
                        int offsetBy = 0;
                        switch (ctype) {
                            case STRING:
                                offsetBy = 16;
                                // variablesString.append("" + field.getName() + "Len = " + getValueString + "\n");
                                // // this won't end well. crap.
                                // // it's probably shit.
                                // variablesString.append("    byte[] " + field.getName() + "Bytes;\n");
                                // // = new byte["+ field.getName() + "Len];\n");
                                //
                                // variablesString.append("    String " + field.getName() + ";\n");
                                // // variablesString.append(" = new String(" + field.getName()
                                // // + "Bytes, StandardCharsets.UTF_8);\n");
                                break;

                            case LONG:
                                offsetBy = 8;
                                getsBitsString.append(field.getName() + " = " + getValueString + "\n");
                                break;

                            case INT:
                                offsetBy = 8;
                                getsBitsString.append(field.getName() + " = (int)" + getValueString + "\n");
                                break;

                        }

                        getsBitsString.append("offset += " + offsetBy + "; // just read " + field.getName() + " type "
                                        + type.getName() + "\n");

                        // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
                        // fields.add(f);
                    });


        StringBuilder constructorString = new StringBuilder();
        // really shouldn't name things so terribly
        constructorString.append(objectClass.getName() + " newObject = new " + objectClass.getName() + "(");

        parseObject(objectClass, (ctype, field, type) -> {
            // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
            // wasting bits.
                        constructorString.append(field.getName()).append(",");
                    });

        // remove the extra comma
        constructorString.deleteCharAt(constructorString.length()-1);
        constructorString.append(");\n");


        return variablesString.toString() + "\n" + getsBitsString.toString() + "\n" + constructorString.toString()
                        + "\n";

        // return fields;
    }

    public void parseObject(Class objectClass, ProcessType pt) {
        for (Field field : objectClass.getDeclaredFields()) {

            Class<?> type = field.getType();

            if (!type.isPrimitive() && !type.isArray() && !type.isInstance("")) {
                continue;
            }

            CTYPES ctype = getCType(type);

            pt.process(ctype, field, type);
            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        }
    }


    private CTYPES getCType(Class<?> type) {
        switch (type.getName()) {
            case "byte":
                return CTYPES.BYTE;

            case "int":
                return CTYPES.INT;

            case "long":
                return CTYPES.LONG;

            case "java.lang.String":
                return CTYPES.STRING;

            default:
                return null;
        }
    }

    public static void main(String[] args) throws Exception {
        ObjectJniH ojh = new ObjectJniH();

        Class classToDump;
        if (args.length > 0) {
            classToDump = Class.forName(args[0]);
        } else {
            classToDump = new DumpTest().getClass();
        }

        // create the c struct
        String cstruct = ojh.createCStruct(classToDump);
        System.out.println(cstruct);

        String javaString = ojh.createJavaConstructor(classToDump);
        System.out.println(javaString);


        // create the java read code, we can use the setters we've found

        // some people take constructors.
        // we can do that by making either:
        // a) a list of getLongs()
        // b) a list of longs assigned from getLong

    }
}
