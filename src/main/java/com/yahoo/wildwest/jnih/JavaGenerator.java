package com.yahoo.wildwest.jnih;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JavaGenerator extends AbstractGenerator {

    static final String GET_LONG_VALUE_STRING = "MUnsafe.unsafe.getLong(address + offset);";

    private final Set<String> blacklistedMethods = generateBlackList();

    public JavaGenerator(Class<?> classToDump) {
        super(classToDump);
    }

    private static Set<String> generateBlackList() {
        Set<String> blacklistedMethods = new HashSet<>();
        for (Method m : Object.class.getMethods()) {
            blacklistedMethods.add(m.getName());
        }

        for (Method m : Class.class.getMethods()) {
            blacklistedMethods.add(m.getName());
        }

        return blacklistedMethods;
    }


    boolean isBlacklisted(String methodName, Class<?> returnType, AccessorType methodType) {
        if (blacklistedMethods.contains(methodName)) {
            System.err.println(methodName + " is from Object");
            return true;
        }

        if (returnType.isPrimitive() && returnType.equals(Void.TYPE)) {
            System.err.println(methodName + " returns void");
            return true;
        }

        if (null != methodType) {
            return methodType.isBlacklisted(methodName);
        }

        return false;
    }

    public List<Method> findGetters() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        List<Method> getters = new LinkedList<>();

        for (Method m : objectClass.getMethods()) {
            String methodName = m.getName();

            if (isBlacklisted(methodName, m.getReturnType(), AccessorType.GETTER)) {
                continue;
            }

            System.out.println("added " + methodName);
            getters.add(m);
        }

        return getters;
    }


    public List<Method> findSetters() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        List<Method> setters = new LinkedList<>();

        for (Method m : objectClass.getMethods()) {
            String methodName = m.getName();

            if (isBlacklisted(methodName, m.getReturnType(), AccessorType.SETTER)) {
                continue;
            }

            System.out.println("added " + methodName);
            setters.add(m);
        }

        return setters;
    }

    private void setupJavaVariablesBlock(PrintWriter pw) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    printWithTab(pw, "long " + field.getName() + "Len;");
                    printWithTab(pw, "long " + field.getName() + "Bytes;");
                    printWithTab(pw, "String " + field.getName() + ";");
                    break;

                case LONG:
                    printWithTab(pw, "long " + field.getName() + "; // " + type.getName());
                    break;

                case INT:
                    printWithTab(pw, "int " + field.getName() + "; // " + type.getName());
                    break;

                case SHORT:
                    printWithTab(pw, "short " + field.getName() + "; // " + type.getName());
                    break;

                case BYTE:
                    printWithTab(pw, "byte " + field.getName() + "; // " + type.getName());
                    break;

                default:
                    printWithTab(pw, "// TOOD: support " + type.getName());
                    break;
            }
        });

        pw.println();
    }

    private void createConstructorInvocation(PrintWriter pw) {

        StringBuilder constructorString = new StringBuilder();
        // really shouldn't name things so terribly
        constructorString.append(FOUR_SPACE_TAB + objectClassName + " newObject = new " + objectClassName + "(");
        constructorString.append("\n");

        String trailer = ", // \n";

        parseObject(objectClass, (ctype, field, type) -> {
            // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
            // wasting bits.
                        constructorString.append(FOUR_SPACE_TAB + FOUR_SPACE_TAB + field.getName()).append(trailer);
                    });

        // remove the extra comma
        int index = constructorString.lastIndexOf(",");
        if (-1 != index) {
            constructorString.delete(index, constructorString.length());
        }
        constructorString.append(");\n");

        pw.println(constructorString.toString());
        printWithTab(pw, "return newObject;");
    }

    private void createBitSpitter(PrintWriter pw) {
        // assume address, len
        printWithTab(pw, "long offset = 0;");

        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.
        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            int offsetBy = 0;
            switch (ctype) {
                case STRING:
                    offsetBy = 8;
                    printWithTab(pw, fieldName + "BytesAddress = " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName + "BytesAddress", type.getName());
                    pw.println();

                    printWithTab(pw, fieldName + "Len = " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName + "Len", type.getName());
                    pw.println();

                    printDecodeString(pw, fieldName);
                    pw.println();
                    break;

                case LONG:
                    offsetBy = 8;
                    printWithTab(pw, fieldName + " = " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName, type.getName());
                    break;

                case INT:
                    offsetBy = 8;
                    printWithTab(pw, fieldName + " = (int) " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName, type.getName());
                    break;


                case SHORT:
                    offsetBy = 8;
                    printWithTab(pw, fieldName + " = (short) " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName, type.getName());
                    break;

                case BYTE:
                    offsetBy = 8;
                    printWithTab(pw, fieldName + " = (byte) " + GET_LONG_VALUE_STRING);
                    printOffset(pw, offsetBy, fieldName, type.getName());
                    break;

            }

            pw.println();

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
                    });

        pw.println();
    }

    private void printDecodeString(PrintWriter pw, String fieldName) {
        printWithTab(pw, fieldName + " = MUnsafe.decodeString(" + fieldName + "BytesAddress, " + fieldName + "Len);");
        // printWithTab(pw, "if (null != " + fieldName + "BytesArray && null != " + fieldName
        // + "Len) {");
        // printWithTab(pw, FOUR_SPACE_TAB + fieldName + " = new String(" + fieldName
        // + "BytesArray, 0, " + fieldName + "Len, StandardCharsets.UTF_8);");
        // printWithTab(pw, "} else {");
        // printWithTab(pw, FOUR_SPACE_TAB + fieldName + " = null;");
        // printWithTab(pw, "}");
    }

    public String generate() {
        pw.println("public " + objectClassName + " create" + shortObjectName + "(long address, long len) {");
        setupJavaVariablesBlock(pw);
        createBitSpitter(pw);
        createConstructorInvocation(pw);
        pw.println("}");
        return sw.toString();
    }

}
