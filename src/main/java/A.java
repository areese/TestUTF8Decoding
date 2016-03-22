import java.lang.reflect.Field;
import java.net.Inet6Address;
import java.net.InetAddress;


public class A {
    private InetAddress ia;

    public static void main(String[] args) throws Exception {

        Field f = A.class.getDeclaredField("ia");
        Class<?> type = f.getType();
        System.out.println(type.isAssignableFrom(InetAddress.class));
        System.out.println(type.isAssignableFrom(Inet6Address.class));
        System.out.println(type.isInstance(InetAddress.class));
        InetAddress ia = InetAddress.getByAddress(new byte[] {1, 2, 3, 4});
        System.out.println(type.isInstance(ia));

    }
}
