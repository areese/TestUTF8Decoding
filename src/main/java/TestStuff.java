// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
import com.yahoo.wildwest.jnih.AbstractGenerator;
import java.lang.reflect.Modifier;

class TempClass {
    static final int STATIC_FOO = 1;
    private transient int transientFoo;
    private int noFoo;
}


public class TestStuff {


    public static void main(String[] args) {
        Class c = TempClass.class;

        AbstractGenerator.parseObject(c, (ctype, field, type) -> {
            String fieldName = field.getName();

            System.out.print(fieldName + " ");
            System.out.println("static: " + (Modifier.isStatic(field.getModifiers())));
            System.out.println("transient: " + (Modifier.isTransient(field.getModifiers())));

            if (ctype.isSupportedPrimitive()) {
                System.out.print("isPrimitive");
            } else {
            }
            System.out.println();
        });

    }

}
