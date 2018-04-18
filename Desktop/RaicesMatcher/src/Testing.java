import static org.junit.Assert.*;
import org.junit.Test;

public class Testing {

    @Test
    public void test() {
        String comment = "I am doing this because mented";

        System.out.println(comment.matches("(?i).*undoc.*"));

    }

}
