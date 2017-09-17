// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.util.LinkedList;
import java.util.List;

public class ListPrintWriter implements LinePrinter {
    private final List<String> output;

    public ListPrintWriter() {
        this(new LinkedList<>());
    }

    public ListPrintWriter(List<String> output) {
        this.output = output;
    }

    public void println() {
        print("\n");
    }

    public void println(String s) {
        print(s + "\n");
    }

    public void print(String s) {
        output.add(s);
    }

    public List<String> getOutput() {
        return output;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : output) {
            sb.append(s);
        }

        return sb.toString();
    }

}
