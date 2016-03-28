// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.wildwest;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Objects;

@SuppressWarnings("restriction")
public class InetAddressAccessor {
    // public static void main(String[] args) throws Exception {
    // testInet4AddressHolder();
    // testInet6AddressHolder();
    // }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static class Inet4AddressHelper {
        Class inet4AddressClass;
        Constructor<InetAddress> inet4AddressConstructor;
        Class inet4AddressHolderClass;
        Field inet4AddressHolderField;
        Field inet4AddressHolderAddressField;
        MethodHandle inet4GetHolderMH;
        MethodHandle inet4HolderGetAddressMH;

        public Inet4AddressHelper() throws ReflectiveOperationException {
            inet4AddressClass = InetAddress.class;
            inet4AddressConstructor = inet4AddressClass.getDeclaredConstructor((Class[]) null);
            inet4AddressConstructor.setAccessible(true);

            for (Class c : inet4AddressClass.getDeclaredClasses()) {
                if ("java.net.InetAddress$InetAddressHolder".equals(c.getName())) {
                    inet4AddressHolderClass = c;
                }
            }

            inet4AddressHolderField = inet4AddressClass.getDeclaredField("holder");
            inet4AddressHolderField.setAccessible(true);

            InetAddress newInstance = inet4AddressConstructor.newInstance();

            Object inet4AddressHolderObject = inet4AddressHolderField.get(newInstance);

            inet4AddressHolderAddressField = inet4AddressHolderObject.getClass().getDeclaredField("address");
            inet4AddressHolderAddressField.setAccessible(true);

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            inet4GetHolderMH = lookup.unreflectGetter(inet4AddressHolderField);
            inet4HolderGetAddressMH = lookup.unreflectGetter(inet4AddressHolderAddressField);
        }

        int getAddress(Inet4Address address) {
            try {
                Object inet4AddressHolderObject = inet4AddressHolderField.get(address);
                return inet4AddressHolderAddressField.getInt(inet4AddressHolderObject);
            } catch (ReflectiveOperationException e) {
            }
            return 0;
        }

        int getAddressViaMH(Inet4Address address) {
            try {
                Object holder = inet4GetHolderMH.invoke(address);
                return ((Integer) inet4HolderGetAddressMH.invoke(holder)).intValue();
            } catch (Throwable e) {
            }
            return 0;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static class Inet6AddressHelper {
        Class inet6AddressClass;
        Constructor<InetAddress> inet6AddressConstructor;
        Class inet6AddressHolderClass;
        Field inet6AddressHolderField;
        Field inet6AddressHolderAddressField;
        MethodHandle inet6GetHolderMH;
        MethodHandle inet6HolderGetAddressMH;

        public Inet6AddressHelper() throws ReflectiveOperationException {
            inet6AddressClass = Inet6Address.class;
            inet6AddressConstructor = inet6AddressClass.getDeclaredConstructor((Class[]) null);
            inet6AddressConstructor.setAccessible(true);

            for (Class c : inet6AddressClass.getDeclaredClasses()) {
                if ("java.net.InetAddress$Inet6AddressHolder".equals(c.getName())) {
                    inet6AddressHolderClass = c;
                }
            }

            inet6AddressHolderField = inet6AddressClass.getDeclaredField("holder6");
            inet6AddressHolderField.setAccessible(true);

            InetAddress newInstance = inet6AddressConstructor.newInstance();

            Object inet6AddressHolderObject = inet6AddressHolderField.get(newInstance);

            inet6AddressHolderAddressField = inet6AddressHolderObject.getClass().getDeclaredField("ipaddress");
            inet6AddressHolderAddressField.setAccessible(true);

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            inet6GetHolderMH = lookup.unreflectGetter(inet6AddressHolderField);
            inet6HolderGetAddressMH = lookup.unreflectGetter(inet6AddressHolderAddressField);
        }

        byte[] getAddress(Inet6Address address) {
            try {
                Object inet6AddressHolderObject = inet6AddressHolderField.get(address);
                Object value = inet6AddressHolderAddressField.get(inet6AddressHolderObject);
                if (value instanceof byte[]) {
                    return ((byte[]) value);
                }
            } catch (ReflectiveOperationException e) {
            }
            return null;
        }

        byte[] getAddressViaMH(Inet6Address address) {
            try {
                Object holder = inet6GetHolderMH.invoke(address);
                return ((byte[]) inet6HolderGetAddressMH.invoke(holder));
            } catch (Throwable e) {
            }
            return null;
        }
    }

    // static void testInet4AddressHolder() throws ReflectiveOperationException, UnknownHostException {
    // Inet4AddressHelper helper = new Inet4AddressHelper();
    //
    // InetAddress ina = InetAddress.getByName("192.168.1.111");
    //
    // int inet4Address = helper.getAddress((Inet4Address) ina);
    // System.out.println(Integer.toHexString(inet4Address));
    // }
    //
    // static void testInet6AddressHolder() throws ReflectiveOperationException, UnknownHostException {
    // Inet6AddressHelper helper = new Inet6AddressHelper();
    //
    // InetAddress ina = InetAddress.getByName("[2001:4998:0:1::1007]");
    //
    // byte[] inet6Address = helper.getAddress((Inet6Address) ina);
    // for (byte b : inet6Address) {
    // System.out.print(Integer.toHexString(Byte.toUnsignedInt(b)));
    // }
    // System.out.println();
    //
    // }

    static final Inet4AddressHelper inet4Helper;
    static final Inet6AddressHelper inet6Helper;

    static {
        try {
            inet4Helper = new Inet4AddressHelper();
            inet6Helper = new Inet6AddressHelper();
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    public static void load() {

    }

    public static byte[] getAddressViaReflection(Inet6Address address) {
        return inet6Helper.getAddress(address);
    }

    public static long getAddressViaReflection(Inet4Address address) {
        return inet4Helper.getAddress(address);
    }

    public static long getAddressViaMH(Inet4Address address) {
        return inet4Helper.getAddressViaMH(address);
    }

    public static byte[] getAddressViaMH(Inet6Address address) {
        return inet6Helper.getAddressViaMH(address);
    }


    public static MissingFingers powersaw(InetAddress address) {
        Objects.requireNonNull(address);

        long addressLong = 0;
        long len = 0;

        if (address instanceof Inet4Address) {
            addressLong = InetAddressAccessor.getAddressViaMH((Inet4Address) address);
        } else {
            byte[] addressBytes = InetAddressAccessor.getAddressViaMH((Inet6Address) address);
            len = addressBytes.length;

            // and now we schlep the bytes via Unsafe.
            addressLong = MUnsafe.getUnsafe().allocateMemory(len);
            MUnsafe.copyMemory(addressLong, len, addressBytes);
        }

        return new MissingFingers(addressLong, len, (0 != len));
    }


}
