// By: Alx Pareja


public interface Participant extends Comparable<Participant> {

    /** returns the priority of the participant based on
     * number of accommodations that it can fulfill
     */
    int priority();

    void printMatches();

    /** returns true if the participant is completely matched */
    boolean isMatched();

    /** returns the array of accommodations */
    boolean[] getAccommodations();

    /**
     * The function matches the host and student together
     * while updating the appropriate values accordingly.
     * @param h: the host that is being matched
     * @param s: the student that is being matched
     */
    static void match(Host h, NewStudent s) {
        h.match(s);
        s.match(h);
    }

}
