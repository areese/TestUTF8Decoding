package com.yahoo.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FourBytesTest {
    @Test
    public void simple() {
        FourBytes four = new FourBytes(new byte[4]);
        Assert.assertEquals(four.getFourBytes().length, 4);
    }

    @Test
    public void testJni() {
        FourBytes four = FourBytes.fromJni();
        Assert.assertEquals(four.getFourBytes().length, 4);
        byte[] expected = {(byte) 0xDE, (byte) 0xAD, (byte) 0xF0, (byte) 0x0D};
        Assert.assertEquals(four.getFourBytes(), expected);
    }


}
