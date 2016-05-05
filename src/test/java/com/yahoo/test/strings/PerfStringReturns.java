package com.yahoo.test.strings;

import java.io.IOException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

import com.yahoo.example.testutf8.JniLibraryLoader;

@State(Scope.Benchmark)
public class PerfStringReturns {

    @Param({"0", "1", "2", "3"})
    public int arg;


    public static void main(String... args) throws RunnerException, IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup
    public void setup() {
        JniLibraryLoader.load();
    }

    @Benchmark
    public String returnJavaString() {
        return JNIStrings.getString(arg);
    }

    @Benchmark
    public String returnUnsafeString() {
        return JNIStrings.getUnsafeString(arg);
    }

    @Benchmark
    public String returnEncodedString() {
        return JNIStrings.getEncodedString(arg);
    }

}
