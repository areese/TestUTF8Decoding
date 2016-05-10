package com.yahoo.wildwest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPowersawVariations {


    static final String EXPECTED_IPV4 = "192.168.1.111";
    static final String EXPECTED_IPV6 = "2001:4998:0:1::1007";

    @Test
    public void testIpv4Simple() throws UnknownHostException {
        InetAddress ia = InetAddress.getByName(EXPECTED_IPV4);
        try (MissingFingers mf = InetAddressAccessor.powersaw(ia)) {
            int ret = PowersawValidator.validateIpv4(mf.getAddress(), mf.getLength());
            Assert.assertEquals(ret, 0);
        }
    }

    @Test
    public void testIpv6Simple() throws UnknownHostException {
        InetAddress ia = InetAddress.getByName(EXPECTED_IPV6);
        try (MissingFingers mf = InetAddressAccessor.powersaw(ia)) {
            int ret = PowersawValidator.validateIpv6(mf.getAddress(), mf.getLength());
            Assert.assertEquals(ret, 0);
        }
    }

    @Test
    public void testboth() throws UnknownHostException {
        InetAddress[] addresses =
                        new InetAddress[] {InetAddress.getByName(EXPECTED_IPV4), InetAddress.getByName(EXPECTED_IPV6)};
        try (MissingFingers mf = InetAddressAccessor.powersaw(addresses)) {
            int ret = PowersawValidator.validateAddresses(mf.getAddress(), mf.getLength());
            Assert.assertEquals(ret, 0);
        }
    }

    @Test
    public void testencodeAddrInfoToUnsafe() throws UnknownHostException {
        long expected = 1 + 2 + 4 + 16;
        try (MissingFingers mf = new MissingFingers(expected)) {
            long ret = PowersawValidator.copyAddresses(mf.getAddress(), mf.getLength());

            // check we got the right bits back.
            Assert.assertEquals(ret, expected);
            InetAddress[] addresses = InetAddressAccessor.newAddresses(mf);
        }
    }
}
