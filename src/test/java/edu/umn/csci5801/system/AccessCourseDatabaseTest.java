package edu.umn.csci5801.system;

import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSDatastoreException;

/**
 * GRADS is unable to access course database(Test case 6)
 */
public class AccessCourseDatabaseTest {

    @SuppressWarnings("unused")
    private GRADSIntf grads;

    private String studentsFileName = "src/resources/students_Expected_ande2807.txt";
    private String coursesFileName = "src/resources/courses_badfilename.txt";
    private String usersFileName = "src/resources/users.txt";

    @Test(expected = GRADSDatastoreException.class)
    public void test() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }
}
