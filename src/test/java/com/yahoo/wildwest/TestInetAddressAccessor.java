package com.yahoo.wildwest;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.yahoo.wildwest.InetAddressAccessor.Inet4AddressHelper;
import static com.yahoo.wildwest.InetAddressAccessor.Inet6AddressHelper;

public class TestInetAddressAccessor {

    @Test
    public static void testInet4AddressHolder() throws ReflectiveOperationException, UnknownHostException {
        Inet4AddressHelper helper = new Inet4AddressHelper();

        InetAddress ina = InetAddress.getByName("192.168.1.111");

        int inet4Address = helper.getAddress((Inet4Address) ina);
        Assert.assertEquals(inet4Address, 0xc0a8016f);
        System.out.println(Integer.toHexString(inet4Address));
    }

    @Test
    public static void testInet6AddressHolder() throws ReflectiveOperationException, UnknownHostException {
        Inet6AddressHelper helper = new Inet6AddressHelper();

        InetAddress ina = InetAddress.getByName("[2001:4998:0:1::1007]");

        byte[] inet6Address = helper.getAddress((Inet6Address) ina);
        // dumpArray(inet6Address);

        byte[] expectedAddress = new byte[] {//
                        (byte) 0x20, //
                        (byte) 0x01, //
                        (byte) 0x49, //
                        (byte) 0x98, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x01, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x00, //
                        (byte) 0x10, //
                        (byte) 0x07, //
        };
        // dumpArray(expectedAddress);

        Assert.assertEquals(inet6Address, expectedAddress);
    }

    private static void dumpArray(byte[] inet6Address) {
        for (byte b : inet6Address) {
            System.out.print(String.format("%02x", b));
        }
        System.out.println();
    }

}
