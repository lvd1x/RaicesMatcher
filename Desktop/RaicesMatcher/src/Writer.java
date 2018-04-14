import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import java.io.File;

public class Writer {
    private static final String COMMA = ",";
    private static final String NEWLINE = "\n";

    private static final String FILE_HEADER = "First,Last,Host First,Host Last,Host Email";

    public static void results(FileWriter writer, LinkedList<Host> matches) {
        try {
            for (Host h: matches) {
                for (NewStudent s: h.getHosting()) {
                    writer.append(s.firstName());
                    writer.append(COMMA);
                    writer.append(s.lastName());
                    writer.append(COMMA);
                    writer.append(h.firstName());
                    writer.append(COMMA);
                    writer.append(h.lastName());
                    writer.append(COMMA);
                    writer.append(h.getEmail());
                    writer.append(NEWLINE);
                }
            }
        } catch (Exception e) {
            System.out.println("fuck");
        }
    }

    public static void unpaired(FileWriter writer, LinkedList<NewStudent> students) {
        try {
            writer.append(NEWLINE);
            for (NewStudent s: students) {
                writer.append(s.firstName());
                writer.append(COMMA);
                writer.append(s.lastName());
                writer.append(NEWLINE);
            }
        } catch (Exception e) {
            System.out.println("fuck");
        }
    }

    public static void write(Matcher matcher1, Matcher matcher2) {
        LinkedList<Host> matches1 = matcher1.getFinals();
        LinkedList<Host> matches2 = matcher2.getFinals();
        FileWriter writer = null;

        try {
            writer = new FileWriter("input/SeniorPairings.txt");

            writer.append(FILE_HEADER);
            writer.append(NEWLINE);

            results(writer, matches1);
            results(writer, matches2);
            writer.append(NEWLINE);
            writer.append("Unmatched Students");
            unpaired(writer, matcher1.needMatch());
            unpaired(writer, matcher1.needMatch());

            /*for (Host h: matches) {
                for (NewStudent s: h.getHosting()) {
                    writer.append(s.firstName());
                    writer.append(COMMA);
                    writer.append(s.lastName());
                    writer.append(COMMA);
                    writer.append(h.firstName());
                    writer.append(COMMA);
                    writer.append(h.lastName());
                    writer.append(COMMA);
                    writer.append(h.getEmail());
                    writer.append(NEWLINE);
                }
            }*/

        } catch (Exception e) {
            System.out.println("Fucking error");
        } finally {
            try {
                writer.flush();
                writer.close();

            } catch (Exception e) {
                System.out.println("FUCKK");
            }
        }
    }

    /**
     * Takes in scanner of ID keys file and Sets up Hashmap where the ID
     * is the key and value is the actual name of participant
     * @param input: Scanner of ID key file.
     * @return: Hashmap of <IDKey, Name>
     */
    private static HashMap<String, String[]> getKeys(Scanner input) {
        HashMap<String, String[]> idKeys = new HashMap<>();
        String id;
        //String[] values = new String[2];
        input.useDelimiter(",");
        input.nextLine();

        input.next();

        while (input.hasNextLine()) {
            //input.next();
            String[] values = new String[2];
            id = input.next();
            values[0] = input.next();
            values[1] = input.next();
            idKeys.put(id, values);

            //input.next();
            //input.next();
        }

       /* while (input.hasNextLine()) {
            input.next();
            idKeys.put(input.next(), input.next());
            input.nextLine();
        }*/
        return idKeys;
    }

    /**
     * Reads file input and creates matcher along with student and host
     * instances. Uses keys to replace ID with actual name, then runs greedy matching algorithm.
     * @param input: A scanner of the file that holds student input.
     * @param keys: A scanner that holds the Names and ID keys of the participants
     */
    private static void match(Matcher matcher,Scanner input, Scanner keys) {
        HashMap<String, String[]> idKeys = getKeys(keys);
        //Matcher matcher = new Matcher();
        input.useDelimiter(",");

        String name;
        String isHost;
        String capacity;
        String home;
        String major;
        String email;
        String[] values;
        String[] accoms = new String[3];

        input.nextLine();

        while (input.hasNextLine()) {
            input.next();
            values = idKeys.get(input.next());
            name = values[0];
            email = values[1];
            isHost = input.next();
            capacity = input.next();
            accoms[2] = input.next();
            accoms[1] = input.next();
            accoms[0] = input.next();
            home = input.next();
            major = input.next();
            input.nextLine();

            if (isHost.equals("True")) {
                matcher.addHost(new Host(name, capacity, accoms, home, major, email));
            } else {
                matcher.addStudent(new NewStudent(name, accoms, home, major));
            }
        }

        /*System.out.println("Total number of host: " + matcher.numHosts());
        System.out.println("Total number of students: " + matcher.numStudents());*/

        System.out.println("total cap: " + matcher.totalCap());
        System.out.println("total host: " + matcher.numHosts());
        System.out.println("total students: " + matcher.numStudents());
        matcher.greedyMatch();
        //matcher.printFinalPairings();
        System.out.println();
        System.out.println();
        System.out.println();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Matcher matcher1 = new Matcher();
        Matcher matcher2 = new Matcher();

        Scanner girls = new Scanner(new File("input/females.txt"));
        Scanner boys = new Scanner(new File("input/males.txt"));
        Scanner keys = new Scanner(new File("input/ID_Keys.txt"));
        Scanner keyss = new Scanner(new File("input/ID_Keys.txt"));

        match(matcher1, girls, keys);
        //matcher1.printStatus();
        match(matcher2, boys, keyss);
        matcher2.printStatus();
        write(matcher1, matcher2);
        //matcher.printFinalPairings();
    }
}
