import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.BST;

import javax.naming.NamingEnumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Matcher {
    private MaxPQ<Host> HOSTS;
    private HashSet<Host> FULLMATCHES;
    private MaxPQ<NewStudent> STUDENTS;
    private LinkedList<NewStudent> UNHOSTED;
    private LinkedList<Host> FinalHost;
    private LinkedList<Host> FinalPairings;

    public Matcher() {
        HOSTS = new MaxPQ<>();
        STUDENTS = new MaxPQ<>();
        FULLMATCHES= new HashSet<>();
        UNHOSTED = new LinkedList<>();
    }

    public LinkedList<Host> getFinals() {
        return FinalPairings;
    }

    public int numStudents() {
        return STUDENTS.size();
    }

    public int numHosts() {
        return HOSTS.size();
    }

    /** inserts student to PQ of people that need to be matched */
    public void addStudent(NewStudent s) {
        STUDENTS.insert(s);
    }

    /** inserts host to PQ based on accommodations they can provide */
    public void addHost(Host h) {
        HOSTS.insert(h);
    }

    public void printFullMatch() {
        if (FULLMATCHES.isEmpty()) {
            System.out.println("no full matches");
        } else {
            Iterator<Host> atCapacity = FULLMATCHES.iterator();
            while (atCapacity.hasNext()) {
                atCapacity.next().printMatches();
                System.out.println();
                System.out.println();
            }
        }
    }

    public LinkedList<NewStudent> needMatch() {
        return UNHOSTED;
    }

    /**
     * Checks to see if the host and student are compatible. If they are compatible
     * then they are matched, otherwise function returns false.
     * @param host: potential host
     * @param student: student in need of accommodation
     * @return true if compatible and false otherwise
     */
    public static boolean canAccommodate(Host host, NewStudent student) {
        boolean[] provided = host.getAccommodations();
        boolean[] required = student.getAccommodations();

        if (provided[0] && provided[1] && provided[2]) {
            Participant.match(host, student);
            return true;
        } else if (provided[0] == required[0] && provided[1] == required[1] && provided[2] == required[2]) {
            Participant.match(host, student);
            return true;
        } else if (host.priority() >= student.priority()) {
            int i = 2;
            while (i >= 0) {
                if (provided[i] == required[i]) {
                        Participant.match(host, student);
                        return true;
                } else if (!provided[i] && required[i]) {
                   return false;
                }
                i -= 1;
            }
        }
        return false;
    }

    /**
     * Matches Host and students by location
     */
    private void locationMatch() {
        BST<String, Host> hosts = initializeBST();
        LinkedList<NewStudent> unmatched = new LinkedList<>();

        Host h;
        for (NewStudent s: STUDENTS) {
            if (hosts.contains(s.homeTown())) {
                h = hosts.get(s.homeTown());
                canAccommodate(h, s);

                // moves h into FULLMATCHES if it reached max capacity
                if (h.isMatched()) {
                    FULLMATCHES.add(h);
                    hosts.delete(h.homeTown());
                }
            } else {
                UNHOSTED.add(s);
            }
        }
        //UNHOSTED = unmatched;
    }

    /** creates BST with the host as value and host's hometown as the key */
    private BST<String, Host> initializeBST() {
        BST<String, Host> hosts = new BST<>();
        Iterator<Host> iter = HOSTS.iterator();

        Host h;
        while (iter.hasNext()) {
            h = iter.next();
            hosts.put(h.homeTown(), h);
        }
        return hosts;
    }

    /**
     * Checks if the host has reached its hosting capacity.
     * If it has then it inserted into FULLMATCHES. Otherwise
     * it is put back in HOSTS.
     * @param h: the host being checked. Assumes it has already been deleted from HOSTS
     */
    private void checkFull(Host h) {
        if (h.isMatched()) {
            FULLMATCHES.add(h);
        } else {
            HOSTS.insert(h);
        }
    }

    /**
     * Takes highest priority student and tries to find accommodation by iterating through
     * potential host. Returns boolean based on success of finding host.
     * WARNING: will crash if it cannot find a compatible host.
     * Possible Solution: break while loop if priority of student is greater than HOSTS.max()
     * @param s: the current student being matched
     * @return true if the student found a host and false otherwise
     */
    private boolean matchStudent(NewStudent s) {
        LinkedList<Host> notMatch = new LinkedList<>();
        Host potentialHost = HOSTS.delMax();

        // Looks for compatible host
        while (!canAccommodate(potentialHost, s) && potentialHost.priority() != 0) {
            notMatch.add(potentialHost);
            potentialHost = HOSTS.delMax();
        }

        // checks if the host can accommodate another student
        checkFull(potentialHost);
        updateHosts(notMatch);
        return s.isMatched();
    }

    /**
     * Re-inserts host not suitable for particular student into HOSTS.
     * @param list: a LinkedList of the host that could not accommodate student.
     */
    private void updateHosts(LinkedList<Host> list) {
        if (!list.isEmpty()) {
            for (Host host: list) {
                HOSTS.insert(host);
            }
        }
    }

    /**
     * Prints out the number of unmatched student and who they are.
     * Also prints out number of Host left and who they are.
     * IMPORTANT: must be called before organize() or will not accurately
     * print.
     * FIX: modify so that it iterates through finalPairings.
     */
    public void printStatus() {
        System.out.println("Fully Matched Host: " + FULLMATCHES.size());
        if (!FULLMATCHES.isEmpty()) {
            for (Host h: FULLMATCHES) {
                h.printMatches();
            }
            System.out.println();
        }

        /*Host host;
        Iterator<Host> areHosting = HOSTS.iterator();
        System.out.println("Remaining Host: " + HOSTS.size());
        while (areHosting.hasNext()) {
            host = areHosting.next();
            host.printMatches();
        }*/
        System.out.println("Remaining Host: " + FinalHost.size());
        for (Host h: FinalHost) {
            h.printMatches();
        }

        System.out.println();

        System.out.println("Unmatched Students: " + UNHOSTED.size());
        if (!UNHOSTED.isEmpty()) {
            for (NewStudent s: UNHOSTED) {

                System.out.println(s.getName());
            }
        }
    }

    /**
     * Moves all of Host into finalized pairing list.
     */
    private void organize() {
        FinalPairings = new LinkedList<>();
        if (!FULLMATCHES.isEmpty()) {
            for (Host h: FULLMATCHES) {
                FinalPairings.add(h);
            }
        }

        while (!HOSTS.isEmpty()) {
            FinalPairings.add(HOSTS.delMax());
        }
    }

    /**
     * Similar to print status but shows student first then the host.
     */
    public void printFinalPairings() {
        for (Host h: FinalPairings) {
            if (h.isHosting()) {
                for (NewStudent s: h.getHosting()) {
                    s.printMatches();
                }
            }
             else {
                h.printMatches();
            }
        }

        if (!UNHOSTED.isEmpty()) {
            System.out.println();
            System.out.println("Students without a host: ");
            for (NewStudent s: UNHOSTED) {
                System.out.println(s.getName());
            }
        }
    }


    /**
     * Moves host that are fully matched into FullyMatches.
     */
    private void filterHost() {
        LinkedList<Host> filtered = new LinkedList<>();
        Host h;

        while (!HOSTS.isEmpty()) {
            h = HOSTS.delMax();
            if (!h.isMatched()) {
                filtered.add(h);
            }
        }

        if (!filtered.isEmpty()) {
            for (Host host: filtered) {
                HOSTS.insert(host);
            }
        }
    }

    /**
     * Final round of matching. Does not take into account location.
     * If host can accommodate then instant match
     */
    private void finalMatch(NewStudent s) {
        for (Host h: FinalHost) {
            if (h.priority() >= s.priority()) {
                Participant.match(h, s);
                if (h.isMatched()) {
                    FULLMATCHES.add(h);
                    FinalHost.remove(h);
                }
                break;
            }
        }
    }

    /**
     * Helper method handles final iteration of matching algorithm
     */
    private void ender() {
        // convert last hosts to linked list
        FinalHost = new LinkedList<>();
        while (!HOSTS.isEmpty()) {
            FinalHost.add(HOSTS.delMax());
        }

        for (NewStudent s : UNHOSTED) {
            if (FinalHost.isEmpty()) {
                break;
            }
            finalMatch(s);
        }

        filterHost();
        filterStudents();
    }

    /**
     * Filters out the students that have a host.
     */
    private void filterStudents() {
        LinkedList<NewStudent> unhosted = new LinkedList();

        for (NewStudent s: UNHOSTED) {
            if (!s.isMatched()) {
                unhosted.add(s);
            }
        }

        UNHOSTED = unhosted;
    }

    public void greedyMatch() {
        System.out.println(totalCap());
        NewStudent currentStudent = STUDENTS.max();

        // matches priority students first
        while (!HOSTS.isEmpty() && !STUDENTS.isEmpty() && currentStudent.priority() > 0 ) {
            currentStudent = STUDENTS.delMax();
            if (!matchStudent(currentStudent)) {
                UNHOSTED.add(currentStudent);
            }
        }

        filterHost();
        locationMatch();
        filterHost();

        if (!HOSTS.isEmpty() && !STUDENTS.isEmpty()) {
            ender();
            //printStatus();
            organize();
            //printFinalPairings();
        } else {
            //printStatus();
            organize();
            //printFinalPairings();
        }
    }

    public int totalCap() {
        int total = 0;
        for (Host h: HOSTS) {
            total += h.getCapacity();
        }
        return total;
    }
}
