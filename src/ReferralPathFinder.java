import java.util.*;

/**
 * Finds internship referrals using the shortest path in {@link StudentGraph}.
 *
 * Will start at one {@link UniversityStudent} and looks for the most closely related student
 * that has previously worked at the target company.
 */
public class ReferralPathFinder {
    /** Longhorn Network graph of student data */
    private final StudentGraph graph;
    /**
     * Creates a new referral pathfinder for the given student graph.
     * @param graph is the StudentGraph for LonghornNetwork
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    /**
     * Finds a referral path from the starting student to another student who has interned at the
     * same company.
     *
     * @param startingStudent is the starting student for searching
     * @param targetCompany is the desired company for the referral
     * @return a list of students in the referral path, if any exist
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent startingStudent, String targetCompany) {
        // Check if startingStudent is real, return empty list if not
        if (startingStudent == null) return new ArrayList<>();
        // Same for targetCompany
        if (targetCompany == null) return new ArrayList<>();

        // If startingStudent has already interned at the company return that student
        if (startingStudent.getPreviousInternships().contains(targetCompany)) {
            List<UniversityStudent> list = new ArrayList<UniversityStudent>();
            list.add(startingStudent);
            return list;
        }

        // Core mapping set up
        Map<UniversityStudent, Integer> distance = new HashMap<>(); // STores best known cost to each student
        Map<UniversityStudent, UniversityStudent> previous = new HashMap<>(); // Who have we gone to already/who led me to next student
        PriorityQueue<UniversityStudent> pqueue = new PriorityQueue<>(Comparator.comparingInt(distance::get)); // Explore the best path first
        Set<UniversityStudent> visited = new HashSet<>(); // Nodes we have alr been to

        UniversityStudent targetReferral = null;

        // Init starting distances all to large num except startingStudent
        for (UniversityStudent student : graph.getAllNodes()) {
            distance.put(student, Integer.MAX_VALUE);
        }
        distance.put(startingStudent, 0);
        pqueue.add(startingStudent); // Always get unvisited student w smallest distance

        // Main loop
        while(!pqueue.isEmpty()) {
            UniversityStudent curr = pqueue.poll();
            if (visited.contains(curr)) continue; // Skip if alr visited the node
            visited.add(curr);
            // Is curr a good referral?
            List<String> currInternships = curr.getPreviousInternships();
            if (currInternships.contains(targetCompany)) {
                // Found best path
                targetReferral = curr;
                break;
            }
            // Check neighbors of curr
            for (StudentGraph.Edge edge : graph.getNeighbors(curr)) {
                UniversityStudent neighbor = edge.neighbor;
                Integer weight = edge.weight;
                if (visited.contains(neighbor)) continue; // SKip if alr visited neighbor
                // Inver the weight bc opposite of Dijkstra's
                Integer invertedWeight = 10 - weight; // Small is better
                // Get new distance
                Integer newCost = distance.get(curr) + invertedWeight;

                // Compare the costs
                if (newCost < distance.get(neighbor)) {
                    distance.put(neighbor, newCost);
                    previous.put(neighbor, curr);
                    pqueue.add(neighbor);
                }

            }
        }
        // Reconstruct path
        if (targetReferral == null) return new ArrayList<>();

        List<UniversityStudent> newPath = new ArrayList<>();
        UniversityStudent currCopy = targetReferral;
        while(currCopy != null) {
            // Add all nodes to final path
            newPath.add(currCopy);
            currCopy = previous.get(currCopy);
        }

        Collections.reverse(newPath); // Reverse
        return newPath;

    }
}
