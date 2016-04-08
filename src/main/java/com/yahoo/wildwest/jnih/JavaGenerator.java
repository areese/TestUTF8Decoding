// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JavaGenerator extends AbstractGenerator {

    static final String GET_LONG_VALUE_STRING = "MUnsafe.unsafe.getLong(address + offset);";

    private final Set<String> blacklistedMethods = generateBlackList();

    private final String generatedClassName;
    private final String javaPath;
    private final String fileName;


    /**
     * A couple of lists of strings that compose the body of the class. one for each function and one for the constants
     */
    private final ListPrintWriter classHeader = new ListPrintWriter();
    private final ListPrintWriter constants = new ListPrintWriter();
    private final ListPrintWriter initFunction = new ListPrintWriter();
    private final ListPrintWriter createFunction = new ListPrintWriter();
    private final ListPrintWriter createFunctionMissingFingers = new ListPrintWriter();
    private final ListPrintWriter classFooter = new ListPrintWriter();

    private final ListPrintWriter[] parts =
                    {classHeader, constants, initFunction, createFunction, createFunctionMissingFingers, classFooter};

    public JavaGenerator(String builtFromString, String basePath, Class<?> classToDump) {
        super(builtFromString, classToDump);

        String[] temp = objectClassName.split("\\.");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length - 1; i++) {
            sb.append(temp[i]).append("/");
        }

        this.javaPath = basePath + sb.toString();

        this.generatedClassName = shortObjectName + "Generated";
        this.fileName = javaPath + generatedClassName + ".java";
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

    private List<Method> findGetters() {
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


    private List<Method> findSetters() {
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

    private void setupJavaVariablesBlock(ListPrintWriter lp) {
        // first we need to find all of it's fields, since we're generating code.
        // I'm only looking for getters. If you don't have getters, it won't be written.
        // List<Field> fields = new LinkedList<>();

        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            if (ctype.isSupportedPrimitive()) {
                printWith2Tabs(lp, type.getName() + " " + fieldName + "; // " + type.getName());
            } else {
                printNonPrimitiveVariable(lp, fieldName);
                printWith2Tabs(lp, shortTypeName(type, true) + " " + fieldName + "; // " + type.getName());
            }
        });

        lp.println();
    }

    private void printNonPrimitiveVariable(LinePrinter lp, String fieldName) {
        printWith2Tabs(lp, "long " + fieldName + "Len;");
        printWith2Tabs(lp, "long " + fieldName + "Address;");
    }

    private void createConstructorInvocation(LinePrinter lp) {

        StringBuilder constructorString = new StringBuilder();
        // really shouldn't name things so terribly
        constructorString.append(FOUR_SPACE_TAB + FOUR_SPACE_TAB + objectClassName + " newObject = new "
                        + objectClassName + "(");
        constructorString.append("\n");

        String trailer = ", //\n";

        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.
        parseObject(objectClass, (ctype, field, type) -> {
            constructorString.append(FOUR_SPACE_TAB + FOUR_SPACE_TAB + FOUR_SPACE_TAB + field.getName())
                            .append(trailer);
        });

        // remove the extra comma
        int index = constructorString.lastIndexOf(",");
        if (-1 != index) {
            constructorString.delete(index, constructorString.length());
        }
        constructorString.append(");\n");

        lp.println(constructorString.toString());
        printWith2Tabs(lp, "return newObject;");
    }

    public void printOffset(LinePrinter lp, String fieldSizeConstantName, String fieldName, String typeName) {
        printWith2Tabs(lp, "offset += " + fieldSizeConstantName + "; // just read " + fieldName + " type " + typeName);
    }

    private void createBitSpitter(LinePrinter lp) {

        printWith2Tabs(lp, "// Now that we've calculated the complete length, and have allocated it.");
        printWith2Tabs(lp, "// We have to go insert new allocations and lengths for the output buffers");
        printWith2Tabs(lp, "// Each output buffer has a constant size, which can be tweaked after generation");

        // assume address, len
        printWith2Tabs(lp, "long offset = 0;");
        lp.println();

        // how many bytes do we skip? Strings are long,long so 16, everything else is 8 byte longs until we stop
        // wasting bits.
        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            if (ctype.isSupportedPrimitive()) {
                printWith2Tabs(lp, fieldName + " = (" + type.getName() + ") " + GET_LONG_VALUE_STRING);
                printOffset(lp, ctype.fieldSizeConstantName, fieldName, type.getName());
            } else {
                printNonPrimitiveReadVariables(lp, fieldName, type.getName());
                printDecode(lp, fieldName, type);
                lp.println();
            }

            lp.println();

        });

        lp.println();
    }

    private void printNonPrimitiveReadVariables(LinePrinter lp, String fieldName, String typeName) {
        printWith2Tabs(lp, fieldName + "Address = " + GET_LONG_VALUE_STRING);
        printOffset(lp, "ADDRESS_OFFSET", fieldName + "Address", typeName);
        lp.println();

        printWith2Tabs(lp, fieldName + "Len = " + GET_LONG_VALUE_STRING);
        printOffset(lp, "LEN_OFFSET", fieldName + "Len", typeName);
        lp.println();
    }

    private void printDecode(LinePrinter lp, String fieldName, Class<?> type) {
        printWith2Tabs(lp, fieldName + " = MUnsafe.decode" + shortTypeName(type, false) + "AndFree(" + fieldName
                        + "Address, " + fieldName + "Len);");
    }

    static String shortTypeName(Class<?> type, boolean isTypeDef) {
        if (type.isArray()) {
            // oops.
            String typeName = type.getComponentType().toString();
            if (isTypeDef) {
                return typeName + "[]";
            } else {
                return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1) + "Array";
            }
        }

        String typeName = type.getName();
        String[] s = typeName.split("\\.");
        return s[s.length - 1];
    }

    /**
     * This generates the createObject function which is used to decode the jni representation from address, len into a
     * Java Object.
     */
    public void javaCreateObject() {
        printWithTab(createFunction, "public static " + objectClassName + " create" + shortObjectName
                        + "(long address, long len) {");
        createFunction.println();
        setupJavaVariablesBlock(createFunction);
        createBitSpitter(createFunction);
        createConstructorInvocation(createFunction);
        createFunction.println();
        printWithTab(createFunction, "}");
    }


    /**
     * This generates the createObject function which is used to decode the jni representation from address, len into a
     * Java Object.
     */
    public void javaCreateObjectMissingFingers() {
        createFunctionMissingFingers.println();
        printWithTab(createFunctionMissingFingers,
                        "public static " + objectClassName + " create" + shortObjectName + "(MissingFingers mf) {");
        printWith2Tabs(createFunctionMissingFingers,
                        "return create" + shortObjectName + "(mf.getAddress(), mf.getLength());");
        printWithTab(createFunctionMissingFingers, "}");
    }

    private void writeLenCalc(LinePrinter lp, String varName, String fieldName, String typeName, CTYPES ctype,
                    String extra) {
        printWith2Tabs(lp, "// " + fieldName + " " + typeName + " is " + ctype.fieldOffset + " bytes " + extra);
        printWith2Tabs(lp, varName + " += " + ctype.fieldSizeConstantName + ";");
    }

    /**
     * This generates the initializeObject function which creates the memory space the jni will have to write to for
     * later inflation. This is tricky, because we have to use Unsafe.allocatememory (If we didn't we couldn't use free
     * in the case NMT was enabled, or weird things would happen)
     */
    public void javaCreateInitialize() {
        initFunction.println();
        printWithTab(initFunction, "public static MissingFingers initialize" + shortObjectName + "() {");
        initFunction.println();
        // assume address, len
        printWith2Tabs(initFunction, "long totalLen = 0;");

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
            if (ctype.isSupportedPrimitive()) {
                extra = ", cast to uint64_t";
            } else {
                extra = ", address + length";
            }

            writeLenCalc(initFunction, "totalLen ", fieldName, type.getName(), ctype, extra);
            initFunction.println();
        });

        printWith2Tabs(initFunction, "long address = MUnsafe.unsafe.allocateMemory(totalLen);");
        printDumpAddressDetails(initFunction, "address", "totalLen");

        // we need to iterate through and write out the allocates and puts for non primitives.
        initFunction.println();
        printWith2Tabs(initFunction, "long offset = 0;");
        parseObject(objectClass, (ctype, field, type) -> {
            String fieldName = field.getName();
            if (ctype.isSupportedPrimitive()) {
                writeLenCalc(initFunction, "offset ", fieldName, type.getName(), ctype, ", cast to uint64_t");
            } else {
                writePutAddress(initFunction, fieldName, type.getName(), ctype);
            }

            initFunction.println();
        });

        printWith2Tabs(initFunction, "return new MissingFingers(address, totalLen);");
        printWithTab(initFunction, "}");
        initFunction.println();
    }

    private void printDumpAddressDetails(LinePrinter lp, String address, String totalLen) {
        if (spewDebugging) {
            printWith2Tabs(lp, "System.out.println(\"Allocated " + address + " \" + Long.toHexString(" + address
                            + ") + \" of length \" + Long.toHexString(" + totalLen + "));");
        }
    }

    private void writePutAddress(ListPrintWriter lp, String fieldName, String typeName, CTYPES ctype) {
        printWith2Tabs(lp,
                        "// " + fieldName + " " + typeName + " is " + ctype.fieldOffset + " bytes, address + length");

        String fieldSizeConstant = fieldName.toUpperCase() + ctype.dataSizeConstantAppender;

        // special case write out a constant.
        printWithTab(constants, generateConstant(fieldSizeConstant, ctype.allocationSize));

        printWith2Tabs(lp, "{");
        printWithTabs(lp, 3, "long newAddress = MUnsafe.unsafe.allocateMemory(" + fieldSizeConstant + ");");
        printWithTabs(lp, 3, "MUnsafe.unsafe.putAddress(address + offset, newAddress);");
        printWithTabs(lp, 3, "offset += ADDRESS_OFFSET;");
        printWithTabs(lp, 3, "MUnsafe.unsafe.putAddress(address + offset, " + fieldSizeConstant + ");");
        printWithTabs(lp, 3, "offset += LEN_OFFSET;");
        printWith2Tabs(lp, "}");
    }

    public static String generateConstant(String variable, long value) {
        return "public static final long " + variable.toUpperCase() + " = " + value + ";";
    }

    private void printClassHeader() {
        classHeader.println("package com.yahoo.example.test;");
        classHeader.println("import java.net.InetAddress;");
        classHeader.println("import com.yahoo.wildwest.MUnsafe;");
        classHeader.println("import com.yahoo.wildwest.MissingFingers;");
        classHeader.println();
        printGeneratedFromHeader(classHeader);
        classHeader.println("@SuppressWarnings(\"restriction\")");
        classHeader.println("public class " + generatedClassName + " {");
    }

    private void printClassFooter() {
        classFooter.println("}");
    }

    public String getFileName() {
        return fileName;
    }

    private void generateConstants() {
        for (CTYPES c : CTYPES.values()) {
            printWithTab(constants, generateConstant(c.fieldSizeConstantName, c.fieldOffset));
        }

        printWithTab(constants, generateConstant("ADDRESS_OFFSET", 8));
        printWithTab(constants, generateConstant("LEN_OFFSET", 8));
    }

    public String generate() {
        printClassHeader();
        generateConstants();
        javaCreateInitialize();
        javaCreateObject();
        javaCreateObjectMissingFingers();
        printClassFooter();

        StringBuilder partsBuilder = new StringBuilder();
        for (ListPrintWriter lp : parts) {
            partsBuilder.append(lp.toString());
        }

        return partsBuilder.toString();

    }

}
