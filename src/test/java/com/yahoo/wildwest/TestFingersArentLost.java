package com.yahoo.wildwest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestFingersArentLost {
    @Test
    public void ipv4() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("192.168.1.254");
        MissingFingers powersaw = InetAddressAccessor.powersaw(address);
        Assert.assertEquals(powersaw.getLength(), 0);
        Assert.assertEquals(powersaw.getAddress(), false);
        Assert.assertEquals(powersaw.getAddress(), 0xc0a801fe);
    }

    @Test
    public void ipv6() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("[2001:db8:85a3:8d3:1319:8a2e:370:7348]");
        MissingFingers powersaw = InetAddressAccessor.powersaw(address);
        Assert.assertEquals(powersaw.getAddress(), true);
        Assert.assertEquals(powersaw.getLength(), 16);
        Assert.assertNotEquals(powersaw.getAddress(), 0);
    }
}
