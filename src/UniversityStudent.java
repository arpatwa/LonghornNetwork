import java.util.*;

/**
 * An implementation of a {@link Student} in Longhorn Network.
 *
 * Will represent a single student who participates in the Longhorn Network.
 */
public class UniversityStudent extends Student {

    private UniversityStudent roommate;

    UniversityStudent(String name, int age, String gender, int year,
                      String major, double gpa, List<String> roommatePrefs, List<String> prevInternships) {
        // Constructor
    }
    /**
     * Computes the strength of connection between this student and another student.
     * @param other is the student to compare to
     * @return an integer that gives the connection strength of the two students
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        return 0;
    }
    // TODO: Constructor and additional methods to be implemented

    /**
     * Will get student roommate
     * @return the roommate of the student
     */
    public UniversityStudent getRoommate() {
        return null;
    }

    /**
     * Sets a student's roommate
     * @param roommate the person who is the roommate of the student
     */
    public void setRoommate(UniversityStudent roommate) {

    }

    /**
     * Output student data.
     * @return a string of student data
     */
    public String toString() {
        return null;
    }



}

