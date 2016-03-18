public class S {
    void f() {

    inttype; // int
    intattrs; // int
    intstatus; // int
    long abuseExpiration; // long
    intreadCount; // int
    intwriteCount; // int
    long writeTimestamp; // long
    long orgLen;
    byte[] orgBytes;
    String org;
    long locLen;
    byte[] locBytes;
    String loc;
    long ccodeLen;
    byte[] ccodeBytes;
    String ccode;
    long descLen;
    byte[] descBytes;
    String desc;
    inttype; // int
type = (int)MUnsafe.unsafe.getLong(address + offset);
    intattrs; // int
attrs = (int)MUnsafe.unsafe.getLong(address + offset);
    intstatus; // int
status = (int)MUnsafe.unsafe.getLong(address + offset);
    long abuseExpiration; // long
    intreadCount; // int
readCount = (int)MUnsafe.unsafe.getLong(address + offset);
    intwriteCount; // int
writeCount = (int)MUnsafe.unsafe.getLong(address + offset);
    long writeTimestamp; // long
orgLen = MUnsafe.unsafe.getLong(address + offset);
    byte[] orgBytes;
    String org;
locLen = MUnsafe.unsafe.getLong(address + offset);
    byte[] locBytes;
    String loc;
ccodeLen = MUnsafe.unsafe.getLong(address + offset);
    byte[] ccodeBytes;
    String ccode;
descLen = MUnsafe.unsafe.getLong(address + offset);
    byte[] descBytes;
    String desc;


}
}
