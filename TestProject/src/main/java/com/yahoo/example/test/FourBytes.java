package com.yahoo.example.test;

import com.yahoo.wildwest.MUnsafe;
import com.yahoo.wildwest.MissingFingers;

public class FourBytes {
    private final byte[] fourBytes;

    public FourBytes(byte[] fourBytes) {
        this.fourBytes = fourBytes;
    }

    public byte[] getFourBytes() {
        return fourBytes;
    }

    public static FourBytes fromJni() {
        try (MissingFingers mf = new MissingFingers(4)) {

            SampleJni.put4Bytes(mf);

            byte[] decodeByteArray = MUnsafe.decodeByteArray(mf);

            return new FourBytes(decodeByteArray);
        }
    }

}
