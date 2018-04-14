import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    /**
     * Takes in scanner of ID keys file and Sets up Hashmap where the ID
     * is the key and value is the actual name of participant
     * @param input: Scanner of ID key file.
     * @return: Hashmap of <IDKey, Name>
     */
    private static HashMap<String, String> getKeys(Scanner input) {
        HashMap<String, String> idKeys = new HashMap<>();
        /*String name;
        String[] values = new String[2];*/
        input.useDelimiter(",");
        input.nextLine();

        /*while (input.hasNextLine()) {
            input.next();

            name = input.next();
            values[0] = input.next();
            values[1] = input.next();
            idKeys.put(name, values);
            input.nextLine();
        }*/

        while (input.hasNextLine()) {
            input.next();
            idKeys.put(input.next(), input.next());
            input.nextLine();
        }
        return idKeys;
    }

    /**
     * Reads file input and creates matcher along with student and host
     * instances. Uses keys to replace ID with actual name, then runs greedy matching algorithm.
     * @param input: A scanner of the file that holds student input.
     * @param keys: A scanner that holds the Names and ID keys of the participants
     */
    private static void match(Matcher matcher, Scanner input, Scanner keys) {
        HashMap<String, String> idKeys = getKeys(keys);
        input.useDelimiter(",");

        String name;
        String isHost;
        String capacity;
        String home;
        String major;
        String[] values = new String[2];
        String[] accoms = new String[3];

        input.nextLine();

        while (input.hasNextLine()) {
            input.next();
            name = idKeys.get(input.next());
            isHost = input.next();
            capacity = input.next();
            accoms[2] = input.next();
            accoms[1] = input.next();
            accoms[0] = input.next();
            home = input.next();
            major = input.next();
            input.nextLine();

            if (isHost.equals("True")) {
                matcher.addHost(new Host(name, capacity, accoms, home, major));
            } else {
                matcher.addStudent(new NewStudent(name, accoms, home, major));
            }
        }

        System.out.println("Total number of host: " + matcher.numHosts());
        System.out.println("Total number of students: " + matcher.numStudents());

        matcher.greedyMatch();
        System.out.println();
        System.out.println();
        System.out.println();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Matcher matcher = new Matcher();

        Scanner girls = new Scanner(new File("input/female.txt"));
        Scanner boys = new Scanner(new File("input/male.txt"));
        Scanner keys = new Scanner(new File("input/ID_Key.txt"));
        Scanner keyss = new Scanner(new File("input/ID_Key.txt"));

        //match(matcher, girls, keys);
        match(matcher, boys, keyss);
        matcher.printFinalPairings();
    }
}
