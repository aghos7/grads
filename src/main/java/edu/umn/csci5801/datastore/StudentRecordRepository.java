package edu.umn.csci5801.datastore;

import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.StudentRecord;

/**
 * An interface for managing student records persistence
 */
public interface StudentRecordRepository {
    /**
     * Find a student record in the repository
     * @param studentId The student id of the student, must not be null
     * @return The student record associated with the specified student id,
     *         never null
     * @throws GRADSException if the studentId is null or cannot be found in the
     *         repository
     */
    StudentRecord find(String studentId) throws GRADSException;

    /**
     * Find the list of all student ids in the specified department
     * @param department The department the students are a member of, must not
     *        be null
     * @return The list of all student ids in the specified department or an
     *         empty list if no students are in the department, never null
     * @throws GRADSIllegalArgumentException if department is null
     */
    List<String> findAllIdsByDepartment(Department department) throws GRADSIllegalArgumentException;

    /**
     * Replaces an existing student record in the repository with the given
     * studentRecord. If the student record does not already exist, an exception
     * is thrown exists
     * @param studentRecord The student record to save
     * @throws GRADSException if the student record does not exist or there is
     *         an error saving
     */
    void save(StudentRecord studentRecord) throws GRADSException;

    /**
     * Adds the course area details to each CourseTaken in the list by looking
     * up the course area in the courses database by course id. If the course
     * already contains a course area, this method will not update it.
     * @param coursesTaken a list of courses to add course area to
     */
    public void addMissingCourseArea(List<CourseTaken> coursesTaken);
}
