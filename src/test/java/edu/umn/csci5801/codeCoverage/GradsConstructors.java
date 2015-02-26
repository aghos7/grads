package edu.umn.csci5801.codeCoverage;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.JSONStudentRecordRepository;
import edu.umn.csci5801.datastore.JSONUserRepository;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.service.ProgressSummaryService;
import edu.umn.csci5801.service.StudentRecordService;
import edu.umn.csci5801.service.UserService;

/**
 * 
 */
public class GradsConstructors {

    @SuppressWarnings("unused")
    private GRADSIntf grads;

    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";
    private String requirementsFileName = "src/resources/requirements.txt";

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#GRADS(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     * @throws GRADSException
     * @throws GRADSDatastoreException
     * @throws GRADSIllegalArgumentException
     */
    @Test
    public void testGRADSStringStringStringString() throws GRADSIllegalArgumentException, GRADSDatastoreException,
            GRADSException {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, requirementsFileName);
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testBadStudentFileName() throws GRADSIllegalArgumentException, GRADSDatastoreException, GRADSException {
        grads = new GRADS(studentsFileName + "1", coursesFileName, usersFileName, requirementsFileName);

    }

    @Test(expected = GRADSDatastoreException.class)
    public void testBadCourseFileName() throws GRADSIllegalArgumentException, GRADSDatastoreException, GRADSException {
        grads = new GRADS(studentsFileName, coursesFileName + "1", usersFileName, requirementsFileName);
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testBadUserFileName() throws GRADSIllegalArgumentException, GRADSDatastoreException, GRADSException {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName + "1", requirementsFileName);
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testBadRequirementsFileName() throws GRADSIllegalArgumentException, GRADSDatastoreException,
            GRADSException {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, requirementsFileName + "1");
    }

    @Test
    public void testBadGRADSOtherConstructors() {
        try {
            grads = new GRADS(new ProgressSummaryService(null), new StudentRecordService(
                    new JSONStudentRecordRepository(coursesFileName, studentsFileName)), new UserService(
                    new JSONUserRepository(usersFileName)));
            fail("This should have failed with a null repository");
        } catch (GRADSIllegalArgumentException e) {
            // Expected
        } catch (GRADSException e) {
            fail("Unexpected exception: ");
            e.printStackTrace();
        }

        try {
            grads = new GRADS(new ProgressSummaryService(new JSONRequirementRepository(requirementsFileName)),
                    new StudentRecordService(null), new UserService(new JSONUserRepository(usersFileName)));
            fail("This should have failed with a null repository");
        } catch (GRADSIllegalArgumentException e) {
            // Expected
        } catch (GRADSException e) {
            fail("Unexpected exception: ");
            e.printStackTrace();
        }

        try {
            grads = new GRADS(new ProgressSummaryService(new JSONRequirementRepository(requirementsFileName)),
                    new StudentRecordService(new JSONStudentRecordRepository(coursesFileName, studentsFileName)),
                    new UserService(null));
            fail("This should have failed with a null repository");
        } catch (GRADSIllegalArgumentException e) {
            // Expected
        } catch (GRADSException e) {
            fail("Unexpected exception: ");
            e.printStackTrace();
        }
    }

}
