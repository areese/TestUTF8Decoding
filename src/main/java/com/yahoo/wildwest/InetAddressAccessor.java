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
import java.net.UnknownHostException;
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

    public static InetAddress newAddress(MissingFingers output) throws UnknownHostException {
        return newAddress(output.getAddress(), output.getLength());
    }

    public static InetAddress newAddress(long address, long length) throws UnknownHostException {

        if (0 == address) {
            return null;
        }

        if (0 == length || 4 == length) {
            byte[] bytes = new byte[4];

            if (0 == length) {
                // hacky ipv4.
                // no length. ;), so it's just the ipv4 address
                bytes[0] = (byte) ((address >> 0x18) & 0x0FF);
                bytes[1] = (byte) ((address >> 0x10) & 0x0FF);
                bytes[2] = (byte) ((address >> 0x08) & 0x0FF);
                bytes[3] = (byte) ((address >> 0x00) & 0x0FF);

                return InetAddress.getByAddress(bytes);
            } else {

            }
        } else if (16 == length) {
            // ipv6
        } else {
            throw new IllegalArgumentException("Length must be either 4 (ipv4) or 16 (ipv6), was " + length);
        }

        return null;
    }


}
