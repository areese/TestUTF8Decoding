package com.yahoo.wildwest.jnih;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestTypeNames {
    @DataProvider
    public Object[][] typeData() {
        return new Object[][] { //
                        {byte.class, false, "byte"}, //
                        {byte.class, true, "byte"}, //
                        {byte[].class, true, "byte[]"}, //
                        {byte[].class, false, "ByteArray"}, //
                        {String.class, false, "String"}, //
                        {String.class, true, "String"}, //
        };
    }

    @Test(dataProvider = "typeData")
    public void testTypeNames(Class<?> type, boolean isTypeDef, String expected) {
        Assert.assertEquals(JavaGenerator.shortTypeName(type, isTypeDef), expected);
    }

}
