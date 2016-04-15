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

    @DataProvider
    public Object[][] badAddresses() {
        return new Object[][] {//
                        {1L, 1L}, //
                        {1L, 5L}, //
                        {1L, 17L}, //
        };
    }

    @Test(dataProvider = "addresses")
    public void testNewAddress(long inAddress, long inLength, InetAddress expected) throws UnknownHostException {
        InetAddress actual = InetAddressAccessor.newAddress(inAddress, inLength);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "badAddresses", expectedExceptions = IllegalArgumentException.class)
    public void testNewAddressBadd(long inAddress, long inLength) throws UnknownHostException {
        InetAddressAccessor.newAddress(inAddress, inLength);
    }

    @SuppressWarnings("unused")
    private static void dumpArray(byte[] inet6Address) {
        for (byte b : inet6Address) {
            System.out.print(String.format("%02x", b));
        }
        System.out.println();
    }


    @DataProvider
    public Object[][] v4v6addresses() {
        return new Object[][] {//
                        {ipv4Inet}, //
                        {ipv6Inet}, //
        };
    }

    @Test(dataProvider = "v4v6addresses")
    public void testPowerSaw(InetAddress address) throws UnknownHostException {
        // we should be able to:
        // 1. take an address out.
        // 2. restore it back.

        try (MissingFingers powersaw = InetAddressAccessor.powersaw(address)) {
            InetAddress result = InetAddressAccessor.newAddress(powersaw);
            Assert.assertEquals(result, address);
        }
    }

    @Test
    public void testPowersawArray() {
        InetAddress[] addresses = new InetAddress[] {ipv4Inet, ipv6Inet};
        try (MissingFingers powersaw = InetAddressAccessor.powersaw(addresses)) {
            System.out.println(Long.toHexString(powersaw.getAddress()));
            // need to make sure that the types are where we expect them.
            long base = powersaw.getAddress();
            for (int i = 0; i < powersaw.getLength(); i++) {
                System.out.print(Integer.toHexString((0x0FF & ((int) MUnsafe.getByte(base + i)))) + " ");
            }
            System.out.println();

            final int countOffset = 0;
            final int type1Offset = 1;
            final int type2Offset = 6;


            byte count = MUnsafe.getByte(powersaw.getAddress() + countOffset);
            Assert.assertEquals(count, 2);

            byte type1 = MUnsafe.getByte(powersaw.getAddress() + type1Offset);
            Assert.assertEquals(type1, InetAddressAccessor.AF_INET);

            byte type2 = MUnsafe.getByte(powersaw.getAddress() + type2Offset);
            Assert.assertEquals(0x0FF & (int)type2, (int)InetAddressAccessor.AF_INET6);
        }
    }
}
