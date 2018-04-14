import static org.junit.Assert.*;
import org.junit.Test;

/** tester class for matcher */
public class TestMatcher {

    @Test
    public void testAccommodate() {
        Matcher matcher = new Matcher();

        String[] allFalse = {"false", "false"};
        String[] firstFalse = {"false", "true"};
        String[] lastFalse = {"true", "false"};
        String[] allTrue = {"true", "true"};

        Host host = new Host("alx", "2", lastFalse, "LA", "math");

        NewStudent undo = new NewStudent("test1", lastFalse, "LA", "cs");
        NewStudent none = new NewStudent("test2", allFalse, "LA", "cs");
        NewStudent l = new NewStudent("test3", firstFalse, "LA", "cs");
        NewStudent both = new NewStudent("test4", allTrue, "LA", "cs");

        assertTrue("Undoc student should be accommodated ", Matcher.canAccommodate(host, undo));
        assertFalse("None student should be accommodated ", Matcher.canAccommodate(host, none));
        assertFalse("both student should not be accommodated ", Matcher.canAccommodate(host, both));
        assertFalse("l student should not be accommodated ", Matcher.canAccommodate(host, l));

        assertTrue("alx is hosting at least one person", host.isHosting());

        host.printMatches();
    }

    @Test
    public void otherTest() {
        Matcher matcher = new Matcher();

        String[] allFalse = {"false", "false"};
        String[] firstFalse = {"false", "true"};
        String[] lastFalse = {"true", "false"};
        String[] allTrue = {"true", "true"};

        Host host0 = new Host("edgar", "2", firstFalse, "LA", "math");
        Host host1 = new Host("alx", "3", lastFalse, "LA", "math");
        Host host2 = new Host("alonso", "2", allTrue, "other", "math");
        Host host3 = new Host("kevin", "1", allFalse, "LA", "math");

        NewStudent student1 = new NewStudent("dream", lastFalse, "LA", "cs");
        NewStudent student2 = new NewStudent("none", allFalse, "LA", "cs");
        NewStudent student3 = new NewStudent("milk", firstFalse, "LA", "cs");
        NewStudent student4 = new NewStudent("all", allTrue, "LA", "cs");
        NewStudent student5 = new NewStudent("fromLA", lastFalse, "LA", "cs");
        NewStudent student6 = new NewStudent("other", allFalse, "other", "cs");

        matcher.addHost(host0);
        matcher.addHost(host1);
        matcher.addHost(host2);
        matcher.addHost(host3);

        matcher.addStudent(student1);
        matcher.addStudent(student2);
        matcher.addStudent(student3);
        matcher.addStudent(student4);
        matcher.addStudent(student5);
        matcher.addStudent(student6);

        matcher.greedyMatch();
        //matcher.printFullMatch();
    }

    @Test
    public void testPriority() {
        String[] first= {"True", "True", "True"};
        String[] second = {"False", "True", "True"};
        String[] third = {"False", "True", "False"};
        String[] fourth = {"True", "False", "False"};

        Host host0 = new Host("edgar", "2", first, "LA", "math", "a@dsfa.edu");
        Host host1 = new Host("alx", "3", second, "LA", "math", "a@dsfa.edu");
        Host host2 = new Host("alonso", "2", third, "other", "math", "a@dsfa.edu");
        Host host3 = new Host("kevin", "1", fourth, "LA", "math", "a@dsfa.edu");

        int i = host0.compareTo(host2);

        assertTrue(host0.compareTo(host1) > 0);
        assertTrue(host0.compareTo(host2) > 0);
        assertTrue(host0.compareTo(host3) > 0);
        assertTrue(host1.compareTo(host2) > 0);

    }

}
