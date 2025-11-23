import java.io.*;
import java.util.*;

/**
 * Class that reads student data from an input file and converts
 * each record into a {@link UniversityStudent}.
 */
public class DataParser {
    /**
     * Parses the give input text file into a list of {@link UniversityStudent}s.
     * Each line of the file represents demographic, roommate, and internship data for each person.
     *
     * @param filename is the path to the input file of student data
     * @return a list of parsed student objects
     * @throws IOException if an error happens while reading the input file
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {
        // Create return list of University Students
        List<UniversityStudent> students = new ArrayList<>();

        // Read and parse input file of student data
        // Variables initialized
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String name = ""; String gender  = ""; String major = "";
        Integer age =null; Integer year = null;
        Double gpa = null;
        List<String> roommatePreferences = new ArrayList<>(); List<String> previousInternships = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            line=line.trim();
            if (line.isEmpty()) continue; // Skip lines w nothing in it

            // Check for start of student data
            if (line.equals("Student:")) {
                if (!name.isEmpty()) {
                    // Check if all other fields are full
                    if (gender.isEmpty() || major.isEmpty() || age == null || year == null ||
                            gpa == null  || roommatePreferences.isEmpty()) {
                        throw new IllegalArgumentException("Missing required field for student: " + name);
                    }

                    // Create new student bc onto the next one
                    UniversityStudent student = new UniversityStudent(name, age, gender, year,
                            major, gpa, roommatePreferences, previousInternships);
                    students.add(student);

                    // Reset student vars for next input student
                    name = ""; gender = ""; major = "";
                    age = 0; year = 0;
                    gpa = 0.0;
                    roommatePreferences = new ArrayList<>(); previousInternships = new ArrayList<>();
                }
                continue; // After we know to make a Student, go to the next line for data
            }

            // If there isn't a Student: line check for colon and continue by case
            int index;
            index = line.indexOf(":");
            if (index < 0) {
                // Colon not found so format is wrong
                throw new IllegalArgumentException("Parsing error: Incorrect format in line: '" +line +"'. Expected format 'Name: <value>'.");
            }
            // Split line into data type and data value
            String type = line.substring(0, index).trim();
            String value = line.substring(index + 1).trim();

            // Go through each data type and add data where its needed
            switch (type){
                case "Name":
                    name = value;
                    break;

                case "Gender":
                    gender = value;
                    break;

                case "Age":
                    try {
                        age = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        age = 0;
                        throw new IllegalArgumentException("Number format error: Invalid number format for age: '" +
                                value +"' in student entry for " + name +".") ;
                    }
                    break;

                case "Year":
                    try {
                        year = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        year = 0;
                        throw new IllegalArgumentException("Number format error: Invalid number format for year: '" +
                                value +"' in student entry for " + name +".");
                    }
                    break;

                case "Major":
                    major = value;
                    break;

                case "GPA":
                    try {
                        gpa = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Number format error: Invalid number format for GPA: '" +
                                value +"' in student entry for " + name +".");
                    }
                    break;

                case "RoommatePreferences":
                    if (!value.equals("None")) {
                        // Add new prev internships by comma separator
                        String[] roomiePrefs = value.split(",");
                        for (String roomieName : roomiePrefs) {
                            if (!roomieName.trim().isEmpty()) {
                                roommatePreferences.add(roomieName.trim());
                            }
                        }
                    }
                    break;


                case "PreviousInternships":
                    if (!value.equals("None")) {
                        // Add new prev internships by comma separator
                            String[] prevInternships = value.split(",");
                            for (String internship : prevInternships) {
                                if (!internship.trim().isEmpty()) {
                                    previousInternships.add(internship.trim());
                                }
                            }
                    }
                    break;

                // For when the command is not of correct data types
                default:
                    break;

            }
        }

        // Adding last student when no new next line (next line is empty)
        if (!name.isEmpty()) {
            // Check if all other fields are full
            if (gender.isEmpty() || major.isEmpty() || age == null || year == null ||
                    gpa == null  || roommatePreferences.isEmpty()) {
                throw new IllegalArgumentException("Missing required field in student entry for " + name +".");
            }

            // Create new student bc onto the next one
            UniversityStudent student = new UniversityStudent(name, age, gender, year,
                    major, gpa, roommatePreferences, previousInternships);
            students.add(student);
        }

        reader.close();
        return students;
    }
}
