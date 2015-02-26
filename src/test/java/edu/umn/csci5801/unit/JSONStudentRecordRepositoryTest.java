package edu.umn.csci5801.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import edu.umn.csci5801.datastore.JSONStudentRecordRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.model.StudentRecord;

/**
 * 
 */
public class JSONStudentRecordRepositoryTest {

    private JSONStudentRecordRepository getRepositoryFromExistingFile(String coursesJsonFilePath,
            String studentRecordJsonFilePath) throws GRADSException {
        // first make sure the files exist
        File coursesJsonFile = new File(coursesJsonFilePath);
        assertTrue(coursesJsonFile.exists());

        File studentRecordJsonFile = new File(studentRecordJsonFilePath);
        assertTrue(studentRecordJsonFile.exists());

        return new JSONStudentRecordRepository(coursesJsonFilePath, studentRecordJsonFilePath);

    }

    @Test
    public void testStudentCourseGoodData() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses.txt", "src/resources/students.txt");

        StudentRecord studentRecord = studentRecordRepository.find("nguy0621");
        assertNotNull(studentRecord);
    }

    @Test(expected = GRADSException.class)
    public void testNoCoursesNoStudents() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses_noCourses.txt", "src/resources/students_noStudents.txt");

        // this should fail with student id not found
        StudentRecord studentRecord = studentRecordRepository.find("nguy0621");
    }

    @Test(expected = GRADSException.class)
    public void testDupStudents() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses.txt", "src/resources/students_dupId.txt");

    }

    @Test(expected = GRADSException.class)
    public void testDupCourses() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses_dupId.txt", "src/resources/students.txt");

    }

    @Test(expected = GRADSException.class)
    public void testCoursesNullId() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses_nullId.txt", "src/resources/students.txt");

    }

    @Test(expected = GRADSException.class)
    public void testStudentsNullId() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses.txt", "src/resources/students_nullId.txt");

    }

    @Test(expected = GRADSException.class)
    public void testStudentsTestFild() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses.txt", "src/resources/students_textFile.txt");

    }

    @Test
    public void testStudentsNoTerm() throws GRADSException {
        JSONStudentRecordRepository studentRecordRepository = getRepositoryFromExistingFile(
                "src/resources/courses.txt", "src/resources/students_nullTerm.txt");
        StudentRecord studentRecord = studentRecordRepository.find("nguy0621");
        assertNotNull(studentRecord);
    }
}
