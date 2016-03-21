This started out as example code for odd decoding of utf-8 in java vs jni
This is because of:
http://banachowski.com/deprogramming/2012/02/working-around-jni-utf-8-strings/

It now also contains code that will help generate code for shclepping things between jni and java.
Mostly to work around the UTF-8 issue of strings above while at the same time not passing byte[]