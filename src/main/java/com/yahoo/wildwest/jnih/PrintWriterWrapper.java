// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest.jnih;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class PrintWriterWrapper extends PrintWriter implements LinePrinter {

    public PrintWriterWrapper(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public PrintWriterWrapper(File file) throws FileNotFoundException {
        super(file);
    }

    public PrintWriterWrapper(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintWriterWrapper(OutputStream out) {
        super(out);
    }

    public PrintWriterWrapper(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrintWriterWrapper(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrintWriterWrapper(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintWriterWrapper(Writer out) {
        super(out);
    }

}
