import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.BST;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Matcher2 {
    private MaxPQ<Host> HOSTS;
    private HashSet<Host> FULLHOST;
    private MaxPQ<NewStudent> STUDENTS;
    private LinkedList<NewStudent> UNHOSTED;
    private LinkedList<Host> FinalHost;
    private LinkedList<Host> FinalPairings;

    public Matcher2() {
        HOSTS = new MaxPQ<>();
        STUDENTS = new MaxPQ<>();
        FULLHOST = new HashSet<>();
        UNHOSTED = new LinkedList<>();
        FinalHost = new LinkedList<>();
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

    public LinkedList<NewStudent> needMatch() {
        return UNHOSTED;
    }

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
                if (!provided[i] && required[i]) {
                    return false;
                } else {
                    i--;
                }
            }
        }
        return true;
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
        FinalHost = filtered;
    }

    /**
     * Checks if the host has reached its hosting capacity.
     * If it has then it inserted into FULLMATCHES. Otherwise
     * it is put back in HOSTS.
     * @param h: the host being checked. Assumes it has already been deleted from HOSTS
     */
    private void checkFull(Host h) {
        if (h.isMatched()) {
            FULLHOST.add(h);
        } else {
            HOSTS.insert(h);
        }
    }

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

    private void putBackHost(LinkedList<Host> list) {
        if (!list.isEmpty()) {
            for (Host host: list) {
                HOSTS.insert(host);
            }
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
    private void matchStudent(NewStudent s) {
        LinkedList<Host> incompatible = new LinkedList<>();
        Host potentialHost = HOSTS.delMax();

        // Looks for compatible host
        while (potentialHost.priority() >= s.priority()) {
            if (canAccommodate(potentialHost, s)) {
                Participant.match(potentialHost, s);
                break;
            } else {
                incompatible.add(potentialHost);
                potentialHost = HOSTS.delMax();
            }
        }

        // checks if the host can accommodate another student
        checkFull(potentialHost);
        if (!s.isMatched()) {
            UNHOSTED.add(s);
        }
        putBackHost(incompatible);
    }

    private void locationMatch(NewStudent s) {
        for (int i = 0; i < FinalHost.size(); i++) {
            if (FinalHost.get(i).homeTown().equals(s.homeTown())) {
                Participant.match(FinalHost.get(i), s);
                if (FinalHost.get(i).isMatched()) {
                    FULLHOST.add(FinalHost.remove(i));
                }
                break;
            }
        }
    }

    /** creates BST with the host as value and host's hometown as the key */
    private void moveHost() {
        while (!HOSTS.isEmpty()) {
            FinalHost.add(HOSTS.delMax());
        }
    }

    private void finalMatch(NewStudent s) {
        for (Host h: FinalHost) {
            if (h.priority() >= s.priority()) {
                Participant.match(h, s);
                if (h.isMatched()) {
                    FULLHOST.add(h);
                    FinalHost.remove(h);
                }
                break;
            }
        }
    }

    private void organize() {
        FinalPairings = new LinkedList<>();
        if (!FULLHOST.isEmpty()) {
            for (Host h: FULLHOST) {
                FinalPairings.add(h);
            }
        }

        if (!FinalHost.isEmpty()) {
            for (Host h: FinalHost) {
                FinalPairings.add(h);
            }
        }
    }

    public void greedyMatch() {
        NewStudent currentStudent;

        while (!HOSTS.isEmpty() && !STUDENTS.isEmpty()) {
            currentStudent = STUDENTS.delMax();
            if (currentStudent.priority() == 0) {
                break;
            } else {
                matchStudent(currentStudent);
            }
        }

        moveHost();
        LinkedList<NewStudent> unhosted = new LinkedList<>();
        while (!UNHOSTED.isEmpty()) {
            currentStudent = UNHOSTED.removeFirst();
            locationMatch(currentStudent);
            if (!currentStudent.isMatched()) {
                unhosted.add(currentStudent);
            }
        }

        UNHOSTED = unhosted;

        unhosted = new LinkedList<>();
        if (!UNHOSTED.isEmpty() && !FinalHost.isEmpty()) {
            while (!UNHOSTED.isEmpty()) {
                currentStudent = UNHOSTED.remove();
                finalMatch(currentStudent);
                if (!currentStudent.isMatched()) {
                    unhosted.add(currentStudent);
                }
            }
            organize();
        } else {
            organize();
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
