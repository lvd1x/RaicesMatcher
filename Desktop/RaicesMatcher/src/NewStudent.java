

public class NewStudent implements Participant {
    private String name;
    private Host host;
    private boolean[] accommodations;
    private boolean isMatched;
    private int priority;
    private String home;
    private String major;

    /**
     * @param ID: the ID_key of the student.
     * @param priorities: list contains booleans as string ie: ['true', 'true', 'false']
     *                  of accommodations.
     * @param home: the hometown of the student.
     * @param major: the intended major of the student.
     */
    public NewStudent(String ID, String[] priorities, String home, String major) {
        this.name = ID;
        this.home = home;
        this.major = major;
        this.isMatched = false;

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

    @Override
    public int compareTo(Participant other) {
        return this.priority - other.priority();
    }

    /** returns the level of priority to match the student */
    @Override
    public int priority() {
        return priority;
    }

    /** returns true if the student has been paired with a host */
    @Override
    public boolean isMatched() {
        return isMatched;
    }

    /** matches the student with a host */
    public void match(Host h) {
        host = h;
        isMatched = true;
    }

    public boolean[] getAccommodations() {
        return accommodations;
    }

    public String getName() {
        return name;
    }

    /** returns the name of the host the student has been paired with */
    public String getHost() {
        return host.getName();
    }

    /** returns the hometown of the student */
    public String homeTown() {
        return home;
    }

    public void printMatches() {
        System.out.println(getName() + " is being hosted by: " + host.getName() + " " +
                host.getEmail().substring(0, host.getEmail().lastIndexOf("u") + 1));
    }

    /** returns the intended major of the student */
    public String getMajor() {
        return major;
    }
}
