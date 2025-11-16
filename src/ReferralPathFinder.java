import java.util.*;

/**
 * Finds internship refferals using shortest path in {@link StudentGraph}.
 *
 * Will start at one {@link UniversityStudent} and looks for the most closely related student
 * that has previsouly worked at the target company.
 */
public class ReferralPathFinder {
    /**
     * Creates a new referral pathfinder for the given student graph.
     * @param graph is the StudentGraph for LonghornNetwork
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
    }

    /**
     * Finds a referral path from the starting student to another student who has interned at the
     * same company.
     *
     * @param start is the starting student for searching
     * @param targetCompany is the desired company for the referral
     * @return a list of students in the referral path, if any exist
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        // Method signature only
        return new ArrayList<>();
    }
}
