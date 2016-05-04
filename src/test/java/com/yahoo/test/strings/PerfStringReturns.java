package com.yahoo.test.strings;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.yahoo.example.testutf8.JniLibraryLoader;

@State(Scope.Benchmark)
public class PerfStringReturns {
    @Setup
    public void setup() {
        JniLibraryLoader.load();
    }

    @Benchmark
    public String returnJavaString() {
        return JNIStrings.getString(1);
    }

    @Benchmark
    public String returnUnsafeString() {
        return JNIStrings.getUnsafeString(1);
    }

    @Benchmark
    public String returnEncodedString() {
        return JNIStrings.getEncodedString(1);
    }

}
