import java.util.*;

/**
 * Implements the Gale-Shapely algorithm that will assign roommates given a list of {@link UniversityStudent}s
 * when it is a good match and possible.
 */
public class GaleShapley {
    /**
     * Will use the Gale Shapley algorithm on the list of students to assign roommates based
     * on preferences.
     * @param students is a list of students that need to be matched with a roommate
     */
    public static void assignRoommates(List<UniversityStudent> students) {
        // Map to hold roommate matches
        Map<UniversityStudent, UniversityStudent> roommatePairs = new HashMap<>();

        // Track student proposals by index (ie. first, second, third choice, etc)
        Map<UniversityStudent, Integer> proposals = new HashMap<>();

        // Queue for unmatched pairs
        Queue<UniversityStudent> unmatchedStudents = new LinkedList<>();

        // Name to student look up
        Map<String, UniversityStudent> studentNames = new HashMap<>();

        // Get student to name matching & initialize proposal number
        for (UniversityStudent student : students) {
            studentNames.put(student.getName(), student);
            proposals.put(student, 0); // Has not proposed anyone yet
        }

        // Check student prefs and add to unmatched queue if they have prefs
        for (UniversityStudent student : students) {
            if (!student.getRoommatePreferences().isEmpty()){
                unmatchedStudents.offer(student); // Safe queue add elem
            }
        }

        // Main matching loop
        while (!unmatchedStudents.isEmpty()) {
            UniversityStudent student1 = unmatchedStudents.poll(); // Get unmatched student

            // Find who student1 should propose
            List<String> student1Prefs = student1.getRoommatePreferences();
            int i = proposals.get(student1); // First proposal
            // Skip student if they already have a roomie or have gone through every proposal
            if (i >= student1Prefs.size() || student1.getRoommate() != null) {
                continue;
            }
            // Get student's preferred roommate
            String preferredRoommate = student1Prefs.get(i);
            proposals.put(student1, i+1); // Go to next proposal

            // Look up the preferred roommate
            UniversityStudent student2 = studentNames.getOrDefault(preferredRoommate, null);

            // Make sure student2 exists
            if (student2 == null) {
                // Add student1 back to unmatched bc their preferred roommate is non-existent
                unmatchedStudents.offer(student1);
                continue;
            }

            // If student 2 exists, see if they already have a roommate or not
            if (student2.getRoommate() == null) {
                // student2 does not have roommate so 1 and 2 can be matched\
                // Set in both directions
                student1.setRoommate(student2);
                student2.setRoommate(student1);
                // Pair in both directions
                roommatePairs.put(student1, student2);
                roommatePairs.put(student2, student1);
            } else {
                // student2 already has a roommate so need to compare preference list
                List<String> student2Prefs = student2.getRoommatePreferences();
                UniversityStudent currRoommate = student2.getRoommate();
                int rankOfStudent1 = student2Prefs.indexOf(student1.getName()); // Gets student2 rank of student1
                if (rankOfStudent1 < 0) {
                    rankOfStudent1 = 1000;
                }

                int rankOfStudent2Roomie = student2Prefs.indexOf(currRoommate.getName());

                // Compare current roommate and student1 to see who has higher priority (lower int values)
                if (rankOfStudent1 < rankOfStudent2Roomie) {
                    // student2 prefs student1 over current roommate
                    // Add old student2 roommate back to queue (can be matched w someone else)
                    unmatchedStudents.offer(currRoommate);
                    // Remove current roommate pair
                    roommatePairs.remove(currRoommate);
                    currRoommate.setRoommate(null);

                    // Set in both directions
                    student1.setRoommate(student2);
                    student2.setRoommate(student1);
                    // Pair in both directions
                    roommatePairs.put(student1, student2);
                    roommatePairs.put(student2, student1);
                } else {
                    // student2 likes their current roommate more so add student1 back to unmatched
                    unmatchedStudents.offer(student1);
                }
            }
        }


    }
}
