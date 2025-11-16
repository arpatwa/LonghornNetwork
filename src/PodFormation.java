import java.util.*;

/**
 * Forms small groups of students based on connection strength.
 */
public class PodFormation {
    private StudentGraph graph;

    /**
     * Creates a new pod formation helper for given graph.
     * @param graph is the {@link StudentGraph} that has all students and edges
     */
    public PodFormation(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    /**
     * Creates pods of given size using the student graph.
     * @param podSize is the number of students per pod
     */
    public void formPods(int podSize) {
        // Method signature only
    }
}
