package edu.umn.csci5801.datastore;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;

/**
 * An implementation of the StudentRecordRepository interface which uses a JSON
 * file to persist student records.
 */
public class JSONStudentRecordRepository implements StudentRecordRepository {

    // save the path to the student record json file so we can save student
    // records back to the file
    private final String studentRecordJsonFilePath;

    // a map of the string course id to it's Course object, used for fast lookup
    // of courses
    private Map<String, Course> courses;

    // a map of the string student id to it's StudentRecord object, used for
    // fast lookup of student records
    private Map<String, StudentRecord> studentRecordsById;

    /**
     * Constructs a new student record repository with the give JSON file
     * @param coursesJsonFilePath path to the JSON file with courses
     * @param studentRecordJsonFilePath path to the JSON file with student
     *        records
     * @throws GRADSException if the arguments are null or file cannot be read
     *         or has invalid data
     */
    public JSONStudentRecordRepository(String coursesJsonFilePath, String studentRecordJsonFilePath)
            throws GRADSException {
        if (coursesJsonFilePath == null) {
            throw new GRADSIllegalArgumentException(
                    "coursesJsonFilePath must not be null when constructing a JSONStudentRecordRepository");
        }
        if (studentRecordJsonFilePath == null) {
            throw new GRADSIllegalArgumentException(
                    "studentRecordJsonFilePath must not be null when constructing a JSONStudentRecordRepository");
        }
        this.studentRecordJsonFilePath = studentRecordJsonFilePath;
        loadCourses(coursesJsonFilePath);
        loadStudentRecords(studentRecordJsonFilePath);
    }

    /**
     * Reads the JSON file and loads the map of all possible courses
     * @param coursesJsonFilePath path to the JSON file
     * @throws GRADSDatastoreException if the file cannot be read or has invalid
     *         data
     */
    private void loadCourses(String jsonFilePath) throws GRADSDatastoreException {
        List<Course> courseList;
        try {
            // read the JSON file into a List of Course
            courseList = new Gson().fromJson(new FileReader(new File(jsonFilePath)), new TypeToken<List<Course>>() {
            }.getType());
        } catch (Exception e) {
            throw new GRADSDatastoreException("Error reading JSON file " + jsonFilePath + " - " + e.getMessage());
        }

        // loop through the courses, validate each one, and add them to the map
        courses = new HashMap<String, Course>();
        for (Course course : courseList) {
            if (course.getId() == null || course.getId().length() == 0) {
                throw new GRADSDatastoreException("Invalid course JSON file " + jsonFilePath
                        + " - course found with no course id");
            } else if (courses.containsKey(course.getId())) {
                throw new GRADSDatastoreException("Invalid user JSON file " + jsonFilePath
                        + " - courses found with duplicate id " + course.getId());
            }
            courses.put(course.getId(), course);
        }
    }

    /**
     * Reads the JSON file and loads the map of all possible student records
     * @param jsonFilePath path to the JSON file
     * @throws GRADSDatastoreException if the file cannot be read or has invalid
     *         data
     */
    private void loadStudentRecords(String jsonFilePath) throws GRADSDatastoreException {
        List<StudentRecord> persistedStudentRecords;
        try {
            // read the JSON file into a List of StudentRecord
            persistedStudentRecords = new Gson().fromJson(new FileReader(new File(jsonFilePath)),
                    new TypeToken<List<StudentRecord>>() {
                    }.getType());
        } catch (Exception e) {
            throw new GRADSDatastoreException("Error reading JSON file " + jsonFilePath + " - " + e.getMessage());
        }

        // loop through the student records, validate each one, and add them to
        // the map
        // use a LinkedHashMap to keep the students in the same order
        studentRecordsById = new LinkedHashMap<String, StudentRecord>();
        for (StudentRecord studentRecord : persistedStudentRecords) {
            Student student = studentRecord.getStudent();
            if (student.getId() == null || student.getId().length() == 0) {
                throw new GRADSDatastoreException("Invalid user JSON file " + jsonFilePath
                        + " - student record found with no student id");
            } else if (studentRecordsById.containsKey(student.getId())) {
                throw new GRADSDatastoreException("Invalid user JSON file " + jsonFilePath
                        + " - student records found with duplicate ids");
            }

            // replace the student record course area with the course area from
            // the courses file
            if (studentRecord.getCoursesTaken() != null) {
                addMissingCourseArea(studentRecord.getCoursesTaken());
            } else {
                studentRecord.setCoursesTaken(new ArrayList<CourseTaken>());
            }

            if (studentRecord.getMilestonesSet() == null) {
                studentRecord.setMilestonesSet(new ArrayList<MilestoneSet>());
            }

            studentRecordsById.put(student.getId(), studentRecord);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMissingCourseArea(List<CourseTaken> coursesTaken) {
        for (CourseTaken courseTaken : coursesTaken) {
            if (courseTaken.getCourse() != null && courses.containsKey(courseTaken.getCourse().getId())) {
                courseTaken.getCourse().setCourseArea(courses.get(courseTaken.getCourse().getId()).getCourseArea());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentRecord find(String studentId) throws GRADSException {
        if (studentId == null || !studentRecordsById.containsKey(studentId)) {
            throw new GRADSIllegalArgumentException("Invalid student id: " + studentId);
        }
        return Utilities.cloneThroughJson(studentRecordsById.get(studentId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findAllIdsByDepartment(Department department) throws GRADSIllegalArgumentException {
        if (department == null) {
            throw new GRADSIllegalArgumentException("department must not be null for the findAllIdsByDepartment method");
        }
        List<String> studentIdsInDepartment = new ArrayList<String>();
        for (StudentRecord studentRecord : studentRecordsById.values()) {
            if (department.equals(studentRecord.getDepartment())) {
                studentIdsInDepartment.add(studentRecord.getStudent().getId());
            }
        }
        return studentIdsInDepartment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(StudentRecord studentRecord) throws GRADSException {
        if (studentRecord.getStudent() == null) {
            throw new GRADSIllegalArgumentException("studentRecord argument must not contain a null student object");
        }
        String studentId = studentRecord.getStudent().getId();
        if (studentId == null || studentId.length() == 0) {
            throw new GRADSIllegalArgumentException("studentRecord argument must not contain a null student id");
        }
        // don't save student records of unknown students
        if (!studentRecordsById.containsKey(studentId)) {
            throw new GRADSDatastoreException("Cannot save studentRecord with student id " + studentId
                    + " which does not exist");
        }

        studentRecordsById.put(studentId, studentRecord);

        // create the json text
        String jsonText = new GsonBuilder().setPrettyPrinting().create().toJson(studentRecordsById.values());
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(studentRecordJsonFilePath);
            fileWriter.write(jsonText);
            fileWriter.close();
        } catch (IOException e) {
            throw new GRADSDatastoreException("Error writing JSON file " + studentRecordJsonFilePath + " - "
                    + e.getMessage());
        }

        // reload the student records from the newly written student records
        loadStudentRecords(studentRecordJsonFilePath);
    }
}
