// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.lang.reflect.Field;

public interface ProcessType {
    void process(CTYPES ctype, Field field, Class<?> type);
}
