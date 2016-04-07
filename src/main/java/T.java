import java.util.Properties;
import java.util.Set;

public class T {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Properties props = System.getProperties();
        for (String key : (Set<String>) ((Set) props.keySet())) {
            System.out.println(key + "=" + props.getProperty(key));
        }
    }
}
