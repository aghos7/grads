package edu.umn.csci5801.service;

import java.util.List;

import edu.umn.csci5801.datastore.StudentRecordRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.StudentRecord;

/**
 * Contains any business logic for handling StudentRecords
 * @note Provides a layer of abstraction between main application and data
 *       storage
 */
public class StudentRecordService {

    /**
     * The student record repository
     */
    private final StudentRecordRepository repository;

    /**
     * Constructs a student record service with the given repository
     * @param repository The repository to use, must not be null
     * @throws GRADSIllegalArgumentException repository is null
     */
    public StudentRecordService(StudentRecordRepository repository) throws GRADSIllegalArgumentException {
        if (repository == null) {
            throw new GRADSIllegalArgumentException("Repository must not be null");
        }
        this.repository = repository;
    }

    /**
     * Gets the raw student record data for a given studentId.
     * @param studentId the identifier of the student.
     * @return the student record data, never null
     * @throws GRADSException if the form data could not be retrieved.
     */
    public StudentRecord getTranscript(String studentId) throws GRADSException {
        return repository.find(studentId);
    }

    /**
     * Saves a new set of student data to the records data. The student record
     * must already exist
     * @param studentRecord the new student record
     * @throws GRADSException if the transcript data could not be saved
     */
    public void updateTranscript(StudentRecord studentRecord) throws GRADSException {
        repository.save(studentRecord);
    }

    /**
     * Appends a note to a student record.
     * @param studentId the student ID to add a note to.
     * @param note the note to append
     * @throws GRADSException if the note could not be saved
     */
    public void addNote(String studentId, String note) throws GRADSException {
        StudentRecord studentRecord = repository.find(studentId);
        studentRecord.getNotes().add(note);
        repository.save(studentRecord);
    }

    /**
     * Retrieves a list of all student ids in the specified department
     * @param department The department the students are a member of
     * @return The list of all student ids in the specified department an empty
     *         list if no students are in the department, never null
     * @throws GRADSException
     */
    public List<String> getStudentsByDepartment(Department department) throws GRADSException {
        return repository.findAllIdsByDepartment(department);
    }

    /**
     * Adds the course area details to each CourseTaken in the list by looking
     * up the course area in the courses database by course id. If the course
     * already contains a course area, this method will not update it.
     * @param coursesTaken a list of courses to add course area to
     */
    public void addMissingCourseArea(List<CourseTaken> coursesTaken) {
        repository.addMissingCourseArea(coursesTaken);
    }
}