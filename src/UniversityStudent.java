import java.util.*;

/**
 * An implementation of a {@link Student} in Longhorn Network.
 *
 * Will represent a single student who participates in the Longhorn Network.
 */
public class UniversityStudent extends Student {
    /** The current student's roommate*/
    private UniversityStudent roommate;
    private Set<UniversityStudent> friends;
    private Map<UniversityStudent, List<String>> chats;

    /**
     * Default UniversityStudent constructor.
     *
     * @param name is the student's name
     * @param age how old student is
     * @param gender student's gender
     * @param year student's grade level
     * @param major student's major
     * @param gpa student's GPA
     * @param roommatePrefs student's roommate preferences
     * @param prevInternships student's previous internships/jobs
     */
    UniversityStudent(String name, int age, String gender, int year,
                      String major, double gpa, List<String> roommatePrefs, List<String> prevInternships) {
        // Constructor initialize fields
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.friends = new HashSet<>();
        this.chats = new HashMap<>();
        // Defensive copy each list in case
        this.roommatePreferences = new ArrayList<>(roommatePrefs);
        this.previousInternships = new ArrayList<>(prevInternships);
    }

    /**
     * @return student name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return student gpa
     */
    public double getGPA() {
        return this.gpa;
    }

    /**
     * @return student major
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * @return student's previous internships
     */
    public List<String> getPreviousInternships() {
        return previousInternships;
    }

    /**
     * @return student year in university
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @return student gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * @return student age
     */
    public int getAge() {
        return this.age;
    }

    public synchronized void addFriend(UniversityStudent friend) {
        this.friends.add(friend);
    }

    public synchronized void addChat(UniversityStudent friend, String message) {
        if (!chats.containsKey(friend)) {
            chats.put(friend, new ArrayList<>());
        }
        chats.get(friend).add(message);
    }

    /**
     * Adds chats between 2 students
     * @param friend who wants to get chats
     * @return list of chats
     */
    public synchronized List<String> getChats(UniversityStudent friend) {
        // Get chats between this and the specified friend
        return new ArrayList<>(chats.getOrDefault(friend, new ArrayList<>()));
    }


    /**
     * Will get student roommate
     * @return the roommate of the student
     */
    public synchronized UniversityStudent getRoommate() {
        return this.roommate;
    }

    /**
     * Gets roommate prefs for current student
     * @return roommate prefs
     */
    public synchronized List<String> getRoommatePreferences() {
        return new ArrayList<>(this.roommatePreferences);
    }

    /**
     * Get a student's friends
     * @return list of friends
     */
    public synchronized List<UniversityStudent> getFriends() {
        return new ArrayList<>(this.friends);
    }

    /**
     * Sets a student's roommate
     * @param roommate the person who is the roommate of the student
     */
    public synchronized void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }

    /**
     * Output student data.
     * @return a string of student data
     */
    public String toString() {
        return "University Student{name='" + this.name + '\'' + ", age=" + this.age + ", gender='" + this.gender + '\''
                + ", year=" + this.year + ", major='" + this.major + '\'' + ", GPA=" + this.gpa +
                ", roommatePreferences=" + this.roommatePreferences + ", previousInternships="
                + this.previousInternships +"}";
    }

    /**
     * Computes the strength of connection between this student and another student.
     * @param other is the student to compare to
     * @return an integer that gives the connection strength of the two students
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        int connectionStrength = 0;
        if (!(other instanceof UniversityStudent)) return 0;

        // Boxing
        UniversityStudent o = (UniversityStudent) other;

        // Check conditions and calc score
        // Same age: +1
        if (this.getAge() == o.getAge()) {
            connectionStrength += 1;
        }

        // Same major +2
        if (this.getMajor().equals(o.getMajor())) {
            connectionStrength += 2;
        }

        // Shared internships +3 for each
        for (String internship: this.getPreviousInternships()) {
            if (o.getPreviousInternships().contains(internship)) {
                connectionStrength += 3;
            }
        }

        // Roommates
        if (this.roommate!=null && this.getRoommate().equals(o.getRoommate())) {
            connectionStrength += 4;
        }
        return connectionStrength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversityStudent that = (UniversityStudent) o; // casting
        return Objects.equals(this.getName(), that.getName()); // No 2 students can have the same name
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

}

