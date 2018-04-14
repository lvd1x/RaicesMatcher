import java.util.LinkedList;

public class Host implements Participant {
    private String name;
    private int capacity;
    private boolean[] accommodations;
    private int priority;
    private String home;
    private String major;
    private String email;
    private LinkedList<NewStudent> hosting;

    /**
     * @param ID: the ID_key of the student.
     * @param capacity: the number of students the person can host;
     *                 decreases as available spots are filled.
     * @param priorities: list contains booleans as string ie: ['true', 'true', 'false']
     *                  of accommodations.
     * @param home: the hometown of the host.
     * @param major: the major of the host.
     */
    public Host(String ID, String capacity, String[] priorities, String home, String major) {
        this.name = ID;
        this.capacity = Integer.parseInt(capacity);
        this.home = home;
        this.major = major;
        this.email = "";
        this.hosting = new LinkedList<>();

        // sets any priority accommodations
        this.accommodations = new boolean[3];
        this.priority = 0;
        try {
            for (int i = 0; i < 3; i++) {
                if (priorities[i].equals("True")) {
                    accommodations[i] = true;
                    priority += (i +1);
                } else {
                    accommodations[i] = false;
                }
            }
        } catch (NullPointerException e) {
            for (int i =0; i < 3; i++) {
                accommodations[i] = false;
            }
        }
    }

    public Host(String ID, String capacity, String[] priorities, String home, String major, String email) {
        this.name = ID;
        this.capacity = Integer.parseInt(capacity);
        this.home = home;
        this.major = major;
        this.email = email;
        this.hosting = new LinkedList<>();

        // sets any priority accommodations
        this.accommodations = new boolean[3];
        this.priority = 0;
        try {
            for (int i = 0; i < 3; i++) {
                if (priorities[i].equals("True")) {
                    accommodations[i] = true;
                    priority += (i +1);
                } else {
                    accommodations[i] = false;
                }
            }
        } catch (NullPointerException e) {
            for (int i =0; i < 3; i++) {
                accommodations[i] = false;
            }
        }
    }

    public String lastName() {
        return name.substring(0, name.indexOf(" ") + 1);
    }

    public String firstName() {
        return name.substring(name.indexOf(" ") + 1, name.length());
    }

    public String getEmail() {
        return email.substring(0, email.lastIndexOf("u") + 1);
    }

    @Override
    public int compareTo(Participant other) {
        int i = this.priority;
        int j = other.priority();
        return this.priority - other.priority();
    }

    /** the level of matching priority, ie, greater priority for more accommodations */
    @Override
    public int priority() {
        return priority;
    }

    /** returns true if the host cannot take in anymore students */
    @Override
    public boolean isMatched() {
        return capacity == 0;
    }

    public boolean isHosting() {
        return !hosting.isEmpty();
    }

    public void match(NewStudent s) {
        hosting.add(s);
        capacity -= 1;
    }

    public void printMatches() {
        if (hosting.isEmpty()) {
            System.out.println(name + " is not hosting anyone.");
        } else {
            String returnString = name + " is hosting: ";
            for (NewStudent student: hosting) {
                returnString += student.getName() + ", ";
            }
            returnString = returnString.substring(0, returnString.length() - 2);
            System.out.println(returnString);
        }
    }

    public LinkedList<NewStudent> getHosting() {
        return hosting;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean[] getAccommodations() {
        return accommodations;
    }

    /** returns the name of the host */
    public String getName() {
        return name;
    }

    /** returns the name of the host's hometown */
    public String homeTown() {
        return home;
    }

    /** returns the major of the host */
    public String getMajor() {
        return this.major;
    }
}
