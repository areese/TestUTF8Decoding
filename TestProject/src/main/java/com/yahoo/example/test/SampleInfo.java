// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.
package com.yahoo.example.test;

import java.net.InetAddress;

public final class SampleInfo {

    private final int type;
    private final int attrs;
    private final int status;

    private final long expiration;
    private final int readCount;
    private final int writeCount;
    private final long writeTimestamp;

    private final InetAddress ia;

    private final String org;
    private final String loc;
    private final String ccode;
    private final String desc;

    private final byte[] someBytes;

    public SampleInfo(int type, int attrs, int status, long expiration, int readCount, int writeCount,
                    long writeTimestamp, InetAddress ia, String org, String loc, String ccode, String desc,
                    byte[] someBytes) {
        this.type = type;
        this.attrs = attrs;
        this.status = status;
        this.expiration = expiration;
        this.readCount = readCount;
        this.writeCount = writeCount;
        this.writeTimestamp = writeTimestamp;
        this.ia = ia;
        this.org = org;
        this.loc = loc;
        this.ccode = ccode;
        this.desc = desc;
        this.someBytes = someBytes;
    }


    public int getType() {
        return type;
    }


    public int getAttrs() {
        return attrs;
    }


    public int getStatus() {
        return status;
    }


    public long getExpiration() {
        return expiration;
    }


    public int getReadCount() {
        return readCount;
    }


    public int getWriteCount() {
        return writeCount;
    }


    public long getWriteTimestamp() {
        return writeTimestamp;
    }


    public InetAddress getIa() {
        return ia;
    }


    public String getOrg() {
        return org;
    }


    public String getLoc() {
        return loc;
    }


    public String getCcode() {
        return ccode;
    }


    public String getDesc() {
        return desc;
    }


    public byte[] getSomeBytes() {
        return someBytes;
    }

    @Override
    public String toString() {
        return "SampleInfo [type=" + type + ", attrs=" + attrs + ", status=" + status + ", expiration=" + expiration
                        + ", readCount=" + readCount + ", writeCount=" + writeCount + ", writeTimestamp="
                        + writeTimestamp + ", ia=" + ia + ", org=" + org + ", loc=" + loc + ", ccode=" + ccode
                        + ", desc=" + desc + "]";
    }
}
