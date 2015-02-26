package edu.umn.csci5801.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSAuthorizationException;

/**
 * Generate and verify summary for an invalid student Id(Test case 4)
 */
public class ReportForInvalidStudentIDTest {
    private GRADSIntf grads;

    private String studentId = "IAMINVALID";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void test() throws Exception {
        grads.setUser(gpcId);
        grads.generateProgressSummary(studentId);
    }
}
