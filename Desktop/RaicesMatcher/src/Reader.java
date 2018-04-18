import javax.naming.Name;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class Reader {

    public Reader() {
    }

    private static void skipToName(Scanner input) {
        input.next();
        input.next();
    }

    private static void skipCols(Scanner input, int skip) {
        for (int i = 0; i < skip; i++) {
            input.next();
        }
    }

    public static void addHost(Matcher male, Matcher female, String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        input.useDelimiter(",");

        String name;
        String major;
        String hometown;
        String phone = "";
        String email;
        String gender;
        String comments = "";
        String capacity;
        String[] accoms = new String[3];
        int i = 0;

        input.nextLine();
        while (input.hasNextLine()) {
            /*i++;
            System.out.println(i);
            //input.nextLine();*/

            skipToName(input);
            name = input.next();

            //System.out.println(name);

            input.next();
            major = input.next();
            //input.next();
            hometown = input.next();
            phone = input.next();
            email = input.next();

            gender = input.next();
            if (gender.matches("(?i).*she.*") || gender.matches("(?i).*female.*")) {
                gender = "female";
            } else {
                gender = "male";
            }

            skipCols(input, 2);
            capacity = input.next();
            //String[] temp = setAccoms(input);
            //capacity = temp[3];
            //accoms = Arrays.copyOfRange(temp, 0, 3);
            accoms = setAccoms(input);

            if (gender.equals("male")) {
                male.addHost(new Host(name, capacity, accoms, hometown, major, email));
            } else {
                female.addHost(new Host(name, capacity, accoms, hometown, major, email));
            }

            input.nextLine();
        }
    }

    private static String[] setAccoms(Scanner input) {
        String[] accoms = {"false", "false", "false"};
        
        //skipCols(input, 3);

        accoms[2] = input.next();
        if (accoms[2].matches("(?i).*yes.*") || accoms[2].matches("(?i).*true.*")) {
            accoms[2] = "True";
        } else {
            accoms[2] = "False";
        }

        input.next();

        accoms[1] = input.next();
        if (accoms[1].matches("(?i).*yes.*") || accoms[1].matches("(?i).*true.*")) {
            accoms[1] = "True";
        } else {
            accoms[1] = "False";
        }

        accoms[0] = input.next();
        if (accoms[0].matches("(?i).*undoc.*")) {
            accoms[0] = "True";
        } else {
            accoms[0] = "False";
        }

        return accoms;
    }

    public static void makeStudents(Matcher males, Matcher females, String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        input.useDelimiter(",");
        input.nextLine();

        String name = "";
        String major = "";
        String hometown = "";
        String phone = "";
        String email = "";
        String gender = "";
        String comments = "";
        String[] accoms = {"false", "false", "false"};

        while (input.hasNextLine()) {
            name = input.next();
            name = input.next() + " " + name;

            phone = input.next();

            hometown = input.next();
            hometown = hometown.substring(4, hometown.length());

            gender = input.next();

            email = input.next();

            comments = input.next();


            if (comments.matches("(?i).*undoc.*")) {
                accoms[0] = "True";
            } else {
                accoms[0] = "false";
            }
            if (comments.matches("(?i).*lgbt.*")) {
                accoms[2] = "True";
            } else {
                accoms[2] = "false";
            }

            // places student in correct matcher
            if (gender.equals("M")) {
                males.addStudent(new NewStudent(name, accoms, hometown, major));
            } else {
                females.addStudent(new NewStudent(name, accoms, hometown, major));
            }

            input.nextLine();
        }
    }

    public static void addStudent(Matcher males, Matcher females, String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        input.useDelimiter(",");
        input.nextLine();

        String name = "";
        String major = "";
        String hometown = "";
        String phone = "";
        String email = "";
        String gender = "";
        String comments = "";
        String[] accoms = {"false", "false", "false"};

        while (input.hasNextLine()) {
            input.next();
            name = input.next();
            name = input.next() + ", " + name;

            skipCols(input, 3);

            phone = input.next();
            skipCols(input, 10);

            hometown = input.next();
            skipCols(input, 2);

            gender = input.next();
            skipCols(input, 6);

            email = input.next();
            input.next();

            comments = input.next();

            if (comments.matches("(?i).*undoc.*")) {
                accoms[0] = "True";
            }
            if (comments.matches("(?i).*lgbt.*")) {
                accoms[2] = "True";
            }

            // places student in correct matcher
            if (gender.equals("M")) {
                males.addStudent(new NewStudent(name, accoms, hometown, major));
            } else {
                females.addStudent(new NewStudent(name, accoms, hometown, major));
            }

            input.nextLine();
        }
    }

}
