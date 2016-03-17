package com.yahoo.wildwest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public String getFields(Class objectClass) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        StringBuilder structString = new StringBuilder();

        structString.append("struct " + objectClass.getName() + "Struct {\n");

        for (Field f : objectClass.getDeclaredFields()) {
            String fieldName = f.getName();

            Class<?> type = f.getType();

            if (!type.isPrimitive() && !type.isArray() && !type.isInstance("")) {
                continue;
            }

            CTYPES ctype = getCType(type);

            switch (ctype) {
                case STRING:
                    structString.append("    uint64_t " + f.getName() + "Bytes;\n");
                    structString.append("    uint64_t " + f.getName() + "Len;\n");


                    break;

                case LONG:
                case INT:

                    structString.append("    uint64_t " + f.getName() + "; // " + type.getName() + "\n");
                    break;

                default:
                    structString.append("    DATASTRUCT " + f.getName() + "; // " + type.getName() + "\n");
                    break;

            }

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
        }

        structString.append("};\n");

        return structString.toString();

        // return fields;
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

        // find the getters
        // List<Method> getters = ojh.findGetters(classToDump);
        String fields = ojh.getFields(classToDump);
        System.out.println(fields);
    }
}
