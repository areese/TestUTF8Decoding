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
