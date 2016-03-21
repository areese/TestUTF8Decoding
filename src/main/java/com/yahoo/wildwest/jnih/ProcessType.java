package com.yahoo.wildwest.jnih;

import java.lang.reflect.Field;


public interface ProcessType {
    void process(CTYPES ctype, Field field, Class<?> type);
}
