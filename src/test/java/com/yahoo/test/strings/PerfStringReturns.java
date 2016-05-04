package com.yahoo.test.strings;

import java.io.IOException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

import com.yahoo.example.testutf8.JniLibraryLoader;

@State(Scope.Benchmark)
public class PerfStringReturns {
    public static void main(String... args) throws RunnerException, IOException {
        org.openjdk.jmh.Main.main(args);
    }

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
