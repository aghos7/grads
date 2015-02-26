package edu.umn.csci5801.system;

import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSDatastoreException;

/**
 * GRADS is unable to access the student record database(Test case 5)
 */
public class AccessStudentRecordTest {

    @SuppressWarnings("unused")
    private GRADSIntf grads;

    private String studentsFileName = "src/resources/students_badfilename.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Test(expected = GRADSDatastoreException.class)
    public void test() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }
}
