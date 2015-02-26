package edu.umn.csci5801.system;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.CourseTaken;

/**
 * Retrieve courses for a student who has not completed any courses(Test case 8)
 */
public class RetrieveStudentIncompleteCoursesTest {

    private GRADSIntf grads;
    private String studentsFileName = "src/resources/students_noCourses.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        String studentId = "gayxx067";
        grads.setUser(studentId);

        List<CourseTaken> retrievedCoursesTaken = grads.getTranscript(studentId).getCoursesTaken();

        assertNotNull(retrievedCoursesTaken);
        assertTrue(0 == retrievedCoursesTaken.size());
        
    }

}
