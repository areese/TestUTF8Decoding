// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.yahoo.wildwest.MUnsafe;

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
            String fieldName = field.getName();
            switch (ctype) {
                case STRING:
                case INETADDRESS:
                    printNonPrimitiveVariable(fieldName);
                    printWithTab(shortTypeName(type.getName()) + " " + fieldName + "; // " + type.getName());
                    break;

                case LONG:
                case INT:
                case SHORT:
                case BYTE:
                    printWithTab(type.getName() + " " + fieldName + "; // " + type.getName());
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

        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.
        parseObject(objectClass, (ctype, field, type) -> {
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
                case INETADDRESS:
                    printNonPrimitiveReadVariables(fieldName, type.getName());
                    printDecode(fieldName, type.getName());
                    pw.println();
                    break;

                case LONG:
                case INT:
                case SHORT:
                case BYTE:
                    printWithTab(fieldName + " = (" + type.getName() + ") " + GET_LONG_VALUE_STRING);
                    printOffset(ctype.fieldOffset, fieldName, type.getName());
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

    private void printDecode(String fieldName, String typeName) {
        printWithTab(fieldName + " = MUnsafe.decode" + shortTypeName(typeName) + "AndFree(" + fieldName + "Address, "
                        + fieldName + "Len);");
    }

    private String shortTypeName(String typeName) {
        String[] s = typeName.split("\\.");
        return s[s.length - 1];
    }

    /**
     * This generates the createObject function which is used to decode the jni representation from address, len into a
     * Java Object.
     */
    public void javaCreateObject() {
        printWithTab("public " + objectClassName + " create" + shortObjectName + "(long address, long len) {");
        pw.println();
        setupJavaVariablesBlock();
        createBitSpitter();
        createConstructorInvocation();
        pw.println();
        printWithTab("}");
    }


    private void writeLenCalc(String varName, String fieldName, String typeName, int size, String extra) {
        printWith2Tabs("// " + fieldName + " " + typeName + " is " + size + " bytes " + extra);
        printWith2Tabs(varName + " += " + size + ";");
    }

    /**
     * This generates the initializeObject function which creates the memory space the jni will have to write to for
     * later inflation. This is tricky, because we have to use Unsafe.allocatememory (If we didn't we couldn't use free
     * in the case NMT was enabled, or weird things would happen)
     */
    public void javaCreateInitialize() {
        pw.println();
        printWithTab("public MissingFingers initialize" + shortObjectName + "() {");
        pw.println();
        // assume address, len
        printWith2Tabs("long totalLen = 0;");

        // we're going to iterate twice.
        // The first time is to figure out total length of the block.
        // The second time is to write the address and length combo's for each spot.
        // The problem is, the max length is going to be crap.
        // We're picking 1k, and maybe ccode is only ever 4 bytes.
        // We're picking 1k, and maybe desc is always 10k.
        // So that's a hand edit. Or maybe an annotation we should add.
        // But this was to boilerplate things and modify, not completely headlessly generate today.


        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.

        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            String extra = "";
            switch (ctype) {
                case STRING:
                case INETADDRESS:
                    extra = ", address + length";
                    break;

                case LONG:
                case INT:
                case SHORT:
                case BYTE:
                    extra = ", cast to uint64_t";
                    break;
            }

            writeLenCalc("totalLen ", fieldName, type.getName(), ctype.fieldOffset, extra);
            pw.println();
        });

        // Now, we can allocate, and then loop back and drop in new allocations for each of these.
        // prob should make some CONSTANTS, so there is a single place to change each field size.
        // maybe we could make a single CONSTANTS class, that would contain those...

        // System.out.println("field " + ctype + " " + fieldName + " " + f.isAccessible());
        // fields.add(f);

        printWith2Tabs("long address = MUnsafe.unsafe.allocateMemory(totalLen);");

        // we need to iterate through and write out the allocates and puts for non primitives.
        pw.println();
        printWith2Tabs("long offset = 0;");
        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            switch (ctype) {
                case STRING:
                case INETADDRESS:
                    writePutAddress(fieldName, type.getName(), ctype);
                    break;

                case LONG:
                case INT:
                case SHORT:
                case BYTE:
                    writeLenCalc("offset ", fieldName, type.getName(), ctype.fieldOffset, ", cast to uint64_t");
                    break;
            }

            pw.println();
        });

        printWith2Tabs("return new MissingFingers(address, totalLen);");
        printWithTab("}");
        pw.println();
    }

    private void writePutAddress(String fieldName, String typeName, CTYPES ctype) {
        printWith2Tabs("// " + fieldName + " " + typeName + " is " + ctype.fieldOffset + " bytes, address + length");
        printWith2Tabs("{");
        printWithTabs(3, "long newAddress = MUnsafe.unsafe.allocateMemory(" + ctype.allocationSize + "); ");
        printWithTabs(3, "MUnsafe.unsafe.putAddress(address + offset, newAddress);");
        printWithTabs(3, "offset += 8;");
        printWithTabs(3, "MUnsafe.unsafe.putAddress(address + offset, " + ctype.allocationSize + ");");
        printWithTabs(3, "offset += 8;");
        printWith2Tabs("}");
    }

    public String generate() {
        javaCreateInitialize();
        javaCreateObject();
        return sw.toString();
    }

}
