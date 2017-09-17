This started out as example code for odd decoding of utf-8 in java vs jni
This is because of:
http://banachowski.com/deprogramming/2012/02/working-around-jni-utf-8-strings/

It now also contains code that will help generate code for shclepping things between jni and java.
Mostly to work around the UTF-8 issue of strings above while at the same time not passing byte[]


This has forked into a way to take a given class, and generate java and c code to copy the bytes back and forth.


The example is:
java -cp build/libs/TestUTF8Decoding.jar:TestProject/build/classes/main/  com.yahoo.wildwest.jnih.ObjectJniH com.yahoo.example.test.SampleInfo -javapath TestProject/src/main/java/  -cfile TestProject/src/main/native/generateSample

This will generate:

TestProject//src/main/java/com/yahoo/example/test/SampleInfo.java
TestProject//src/main/java/com/yahoo/example/test/SampleInfoGenerated.java
TestProject//src/main/native/generateSample.cpp
TestProject//src/main/native/generateSample.h

Those can then be used to copy bytes back and forth.

TestProject is a contained example, to demonstrate this code.
There is still other bits that are needed, this generates the bits that make it easier to cooperate.
There is a lot of speed to be gathered by just allocating memory and copying a struct into it in C, instead of creating 10 or 15 java 
objects from the jni.

