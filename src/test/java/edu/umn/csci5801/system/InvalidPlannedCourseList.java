package edu.umn.csci5801.system;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;

/**
 * Set an invalid planned course list(Test case 10)
 */
public class InvalidPlannedCourseList {

    private final String studentsFileName = "src/resources/students_phdMissingOneClass.txt";
    private final String coursesFileName = "src/resources/courses.txt";
    private final String usersFileName = "src/resources/users.txt";
    private final String requirementsFileName = "src/resources/requirements.txt";
    private final String studentId = "pocha006";
    private GRADSIntf grads;

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, requirementsFileName);
        grads.setUser(studentId);
    }

    @Test(expected = GRADSIllegalArgumentException.class)
    public void test_nullSimulatedList() throws Exception {
        grads.simulateCourses(studentId, null);
    }
}
