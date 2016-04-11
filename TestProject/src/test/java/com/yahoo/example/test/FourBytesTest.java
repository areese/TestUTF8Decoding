package com.yahoo.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FourBytesTest {
    @Test
    public void simple() {
        FourBytes four = new FourBytes(new byte[4]);
        Assert.assertEquals(four.getFourBytes().length, 4);
    }
    
    
}
