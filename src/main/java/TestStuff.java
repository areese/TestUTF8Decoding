// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.

public class TestStuff {
    public static void main(String[] args) {
        Class c = byte[].class;
        System.out.println(c.getName());
        System.out.println(c.isPrimitive());
        System.out.println(c.isArray());
        System.out.println(c.getComponentType());
    }

}
