import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Mainer {

    public static void main(String[] args) throws FileNotFoundException {
    Matcher males = new Matcher();
    Matcher females = new Matcher();
    //Scanner input = new Scanner(new File("Finalized/Hosters.csv"));

    Reader.makeStudents(males, females, "moreFinal/cleanestMentees.csv");
    Reader.addHost(males, females, "moreFinal/finalHost.csv");

    males.greedyMatch();
    females.greedyMatch();

    males.printFinalPairings();
    System.out.println();
    System.out.println();
    females.printFinalPairings();
    /*males.printStatus();
    females.printStatus();*/

    //Writer.write(males, females);
    }
}
