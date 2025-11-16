import java.util.*;

/**
 * Represents the social graph of many {@link UniversityStudent}s in Longhorn Network.
 *
 * Each student is a node in the graph and each edge between students will be weighted by their
 * connection strength.
 */
public class StudentGraph {
    /**
     * Adjacency list that maps each student to edges.
     */
    private final Map<UniversityStudent, List<Edge>> adjList = new HashMap<>();

    /**
     * Constructs a StudentGraph form the given list of students.
     * Will add each student as a node into the graph, compute connection strength between
     * pairs of students, and add weighted/undirected edges for non-zero strengths.
     *
     * @param students is the list of students to include in the graph
     */
    public StudentGraph(List<UniversityStudent> students) {
        // Build graph using calc Connection strength and addEdge
    }

    /**
     * Adds the given student into the StudentGraph if they are not in there already.
     *
     * @param student to be added
     */
    public void addStudent(UniversityStudent student) {

    }

    /**
     * Adds a weighted edge between 2 students.
     * Graph is undirected so the weight is stored for student1 and student2
     * @param student1 one endpoint of the weighted connection edge
     * @param student2 other endpoint of the weighted connection edge
     * @param weight is the connection strength between student1 and student2
     */
    public void addEdge(UniversityStudent student1, UniversityStudent student2, int weight) {
        // Call from constructor logic

    }

    /**
     * Return the list of adjacent edges to the specified student.
     *
     * @param student is who to get the neighbors of
     * @return a list of the student's neighbors, if any
     */
    public List<Edge> getNeighbors(UniversityStudent student) {
        return null;
    }

    /**
     * Returns all students in the graph.
     * @return a set of all students (nodes) that are in the StudentGraph
     */
    public Set<UniversityStudent> getAllNode(){
        return null;
    }

    /**
     * Prints text version of the Longhorn Network StudentGraph.
     * Needed becasue called in main.
     */
    public void displayGraph(){
    }

    /**
     * Represents the weight connection edge between 2 students in the graph.
     */
    public static class Edge{
        /** Student who shares same edge */
        public final UniversityStudent neighbor;
        /** Connection strength of the edge/students */
        public final int weight;

        /**
         *Constructs a new edge to neighbor with given weight.
         * @param neighbor is the student who shares the edge
         * @param weight is the connection weight
         */
        public Edge(UniversityStudent neighbor, int weight){
            this.neighbor = neighbor;
            this.weight = weight ;
        }
    }



}
