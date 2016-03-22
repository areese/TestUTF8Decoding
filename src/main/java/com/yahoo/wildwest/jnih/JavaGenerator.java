// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
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

    private void setupJavaVariablesBlock() {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        parseObject(objectClass, (ctype, field, type) -> {
            switch (ctype) {
                case STRING:
                    printNonPrimitiveVariable(field.getName());
                    printWithTab("String " + field.getName() + ";");
                    break;

                case INETADDRESS:
                    printNonPrimitiveVariable(field.getName());
                    printWithTab("InetAddress " + field.getName() + ";");
                    break;

                case LONG:
                    printWithTab("long " + field.getName() + "; // " + type.getName());
                    break;

                case INT:
                    printWithTab("int " + field.getName() + "; // " + type.getName());
                    break;

                case SHORT:
                    printWithTab("short " + field.getName() + "; // " + type.getName());
                    break;

                case BYTE:
                    printWithTab("byte " + field.getName() + "; // " + type.getName());
                    break;

                default:
                    printWithTab("// TOOD: support " + type.getName());
                    break;
            }
        });

        pw.println();
    }

    private void printNonPrimitiveVariable(String fieldName) {
        printWithTab("long " + fieldName + "Len;");
        printWithTab("long " + fieldName + "Address;");
    }

    private void createConstructorInvocation() {

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
        printWithTab("return newObject;");
    }

    private void createBitSpitter() {
        // assume address, len
        printWithTab("long offset = 0;");

        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.
        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            switch (ctype) {
                case STRING:
                    printNonPrimitiveReadVariables(fieldName, type.getName());
                    printDecodeString(fieldName);
                    pw.println();
                    break;

                case INETADDRESS:
                    printNonPrimitiveReadVariables(fieldName, type.getName());
                    printWithTab(fieldName + " = null;");
                    printWithTab("// TODO: decode InetAddress");
                    pw.println();
                    break;

                case LONG:
                    printWithTab(fieldName + " = " + GET_LONG_VALUE_STRING);
                    printOffset(8, fieldName, type.getName());
                    break;

                case INT:
                    printWithTab(fieldName + " = (int) " + GET_LONG_VALUE_STRING);
                    printOffset(8, fieldName, type.getName());
                    break;


                case SHORT:
                    printWithTab(fieldName + " = (short) " + GET_LONG_VALUE_STRING);
                    printOffset(8, fieldName, type.getName());
                    break;

                case BYTE:
                    printWithTab(fieldName + " = (byte) " + GET_LONG_VALUE_STRING);
                    printOffset(8, fieldName, type.getName());
                    break;

            }

            pw.println();

            // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
            // fields.add(f);
                    });

        pw.println();
    }

    private void printNonPrimitiveReadVariables(String fieldName, String typeName) {
        printWithTab(fieldName + "Address = " + GET_LONG_VALUE_STRING);
        printOffset(8, fieldName + "Address", typeName);
        pw.println();

        printWithTab(fieldName + "Len = " + GET_LONG_VALUE_STRING);
        printOffset(8, fieldName + "Len", typeName);
        pw.println();
    }

    private void printDecodeString(String fieldName) {
        printWithTab(fieldName + " = MUnsafe.decodeStringAndFree(" + fieldName + "Address, " + fieldName + "Len);");
    }

    /**
     * This generates the createObject function which is used to decode the jni representation from address, len into a
     * Java Object.
     */
    public void javaCreateObject() {
        pw.println();
        setupJavaVariablesBlock();
        createBitSpitter();
        createConstructorInvocation();
        pw.println();
    }

    public String generate() {
        pw.println("public " + objectClassName + " create" + shortObjectName + "(long address, long len) {");
        javaCreateObject();
        pw.println("}");
        return sw.toString();
    }

}
