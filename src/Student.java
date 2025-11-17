import java.util.*;

/**
 * Class that represents a single student in the Longhorn Network.
 * Stores key attributes of each student to help with roommate matching
 * and referrals for jobs.
 */
public abstract class Student {
    /** This student's name*/
    protected String name;
    /** This student's age*/
    protected int age;
    /** This student's gender*/
    protected String gender;
    /** This student's grade level*/
    protected int year;
    /** This student's major*/
    protected String major;
    /** This student's GPA*/
    protected double gpa;
    /** This student's roommate prefs*/
    protected List<String> roommatePreferences;
    /** This student's previous internships/jobs*/
    protected List<String> previousInternships;

    /**
     * Computed the strength of connection between this student and another student.
     * @param other is the student to compare to
     * @return an integer that gives the connection strength of the two students
     */
    public abstract int calculateConnectionStrength(Student other);
}
