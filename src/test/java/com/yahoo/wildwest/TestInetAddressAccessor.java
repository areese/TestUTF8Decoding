package com.yahoo.wildwest;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.yahoo.wildwest.InetAddressAccessor.Inet4AddressHelper;
import com.yahoo.wildwest.InetAddressAccessor.Inet6AddressHelper;

public class TestInetAddressAccessor {

    static final String ipv4AddressString = "192.168.1.111";
    static final int ipv4Address = 0xc0a8016f;
    static final InetAddress ipv4Inet;

    static final String ipv6AddressString = "[2001:4998:0:1::1007]";
    static final InetAddress ipv6Inet;
    static final byte[] ipv6Address = new byte[] {//
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

    static {
        try {
            ipv4Inet = InetAddress.getByName(ipv4AddressString);
            ipv6Inet = InetAddress.getByName(ipv6AddressString);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public static void testInet4AddressHolder() throws ReflectiveOperationException, UnknownHostException {
        Inet4AddressHelper helper = new Inet4AddressHelper();

        int inet4Address = helper.getAddress((Inet4Address) ipv4Inet);
        Assert.assertEquals(inet4Address, ipv4Address);
        System.out.println(Integer.toHexString(inet4Address));
    }

    @Test
    public static void testInet6AddressHolder() throws ReflectiveOperationException, UnknownHostException {
        Inet6AddressHelper helper = new Inet6AddressHelper();

        byte[] inet6Address = helper.getAddress((Inet6Address) ipv6Inet);
        // dumpArray(inet6Address);
        // dumpArray(ipv6Address);

        Assert.assertEquals(inet6Address, ipv6Address);
    }


    @DataProvider
    public Object[][] addresses() {
        return new Object[][] {//
                        {0L, 0L, null}, //
                        {ipv4Address, 0L, ipv4Inet}, //
        };
    }

    @Test(dataProvider = "addresses")
    public void testNewAddress(long inAddress, long inLength, InetAddress expected) throws UnknownHostException {
        InetAddress actual = InetAddressAccessor.newAddress(inAddress, inLength);
        Assert.assertEquals(actual, expected);
    }

    private static void dumpArray(byte[] inet6Address) {
        for (byte b : inet6Address) {
            System.out.print(String.format("%02x", b));
        }
        System.out.println();
    }

}
