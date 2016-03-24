
public class TestStuff {
    public static void main(String[] args) {
        Class c = byte[].class;
        System.out.println(c.getName());
        System.out.println(c.isPrimitive());
        System.out.println(c.isArray());
        System.out.println(c.getComponentType());
    }

}
