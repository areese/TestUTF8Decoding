// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

public enum AccessorType {
    GETTER(new String[] {"get", "is"}), //
    SETTER(new String[] {"set"}); //

    private final String[] prefixes;

    private AccessorType(String[] prefixes) {
        this.prefixes = prefixes;
    }

    public boolean isBlacklisted(String methodName) {
        for (String m : prefixes) {
            if (methodName.startsWith(m)) {
                return false;
            }
        }
        return true;
    }

}
