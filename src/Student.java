import java.util.*;

/**
 * Class that represents a single student in the Longhorn Network.
 * Stores key attributes of each student to help with roommate matching
 * and referrals for jobs.
 */
public abstract class Student {
    protected String name;
    protected int age;
    protected String gender;
    protected int year;
    protected String major;
    protected double gpa;
    protected List<String> roommatePreferences;
    protected List<String> previousInternships;

    /**
     * Computed the strength of connection between this student and another student.
     * @param other is the student to compare to
     * @return an integer that gives the connection strength of the two students
     */
    public abstract int calculateConnectionStrength(Student other);
}
