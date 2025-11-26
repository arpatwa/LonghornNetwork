import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Testing runner that uses YOUR DataParser, StudentGraph, GaleShapley,
 * ReferralPathFinder, FriendRequestThread, and ChatThread exactly as Main.java does,
 * but runs them against INPUT FILES instead of built-in data.
 *
 * This is what you use to generate output for submission.
 */
public class Testing {

    public static void main(String[] args) {
        String[] files = {
                "testing/testing_checkpointone/inputs/normal_1.txt",
                "testing/testing_checkpointone/inputs/normal_2.txt",
                "testing/testing_checkpointone/inputs/normal_3.txt",
                "testing/testing_checkpointone/inputs/normal_4.txt",
                "testing/testing_checkpointone/inputs/normal_5.txt",
                "testing/testing_checkpointone/inputs/testing_incorrect_format_exception.txt",
                "testing/testing_checkpointone/inputs/testing_invalid_age_exception.txt",
                "testing/testing_checkpointone/inputs/testing_invalid_gpa_exception.txt",
                "testing/testing_checkpointone/inputs/testing_missing_field_exception.txt"
        };

        for (String file : files) {
            System.out.println("\n========================================");
            System.out.println("Running File: " + file );
            System.out.println("========================================");

            try {
                List<UniversityStudent> students = DataParser.parseStudents(file);

                System.out.println("--- Parsed Students ---");
                for (UniversityStudent s : students) {
                    System.out.println(s);
                }

                // STUDENT GRAPH
                StudentGraph graph = new StudentGraph(students);
                graph.displayGraph();

                // GALE SHAPLEY
                GaleShapley.assignRoommates(students);
                System.out.println("\n--- Roommate Assignments ---");
                for (UniversityStudent s : students) {
                    if (s.getRoommate() != null) {
                        System.out.println(s.getName() + " is roommate with " + s.getRoommate().getName());
                    }
                }

                // THREAD TEST (only if 2+ students)
                if (students.size() >= 2) {
                    ExecutorService executor = Executors.newFixedThreadPool(2);
                    UniversityStudent a = students.get(0);
                    UniversityStudent b = students.get(1);

                    executor.submit(new FriendRequestThread(a, b));
                    executor.submit(new ChatThread(a, b, "Hello!"));
                    executor.shutdown();
                    executor.awaitTermination(3, TimeUnit.SECONDS);
                }

                // REFERRAL PATH TEST
                ReferralPathFinder finder = new ReferralPathFinder(graph);
                System.out.println("\n--- Referral Path Tests ---");
                for (UniversityStudent s : students) {
                    for (String internship : s.getPreviousInternships()) {
                        List<UniversityStudent> path = finder.findReferralPath(students.get(0), internship);
                        System.out.println("Path from " + students.get(0).getName() + " to company '" + internship + "': " + path);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("FILE ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("UNEXPECTED ERROR: " + e.getMessage());
            }
        }
    }
}

