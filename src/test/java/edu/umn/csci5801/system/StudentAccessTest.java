package edu.umn.csci5801.system;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSAuthorizationException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Students have access only to their own student record(Test case 17)
 */
public class StudentAccessTest {
    private GRADSIntf grads;

    private String validStudentId = "ande2807";
    private String otherStudentId = "wall0660";
    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(validStudentId);
    }

    @After
    public void tearDown() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @Test
    public void validStudentAccessTest() throws Exception {
        ProgressSummary progressSummary = grads.generateProgressSummary(validStudentId);
        assertNotNull(progressSummary);

        List<CourseTaken> coursesToSimulate = Arrays.asList(new CourseTaken(new Course("Thesis Credit: Doctoral",
                "csci8888", "24", null), new Term(Semester.SPRING, 2010), Grade.S));
        progressSummary = grads.simulateCourses(validStudentId, coursesToSimulate);
        assertNotNull(progressSummary);
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentUpdateOwnTranscriptTest() throws Exception {
        grads.updateTranscript(validStudentId, grads.getTranscript(validStudentId));
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentAddOwnNoteTest() throws Exception {
        grads.addNote(validStudentId, "Naughty Naughty");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentGetDepartmentStudentIdsTest() throws Exception {
        grads.getStudentIDs();
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentGetOtherStudentTranscriptTest() throws Exception {
        grads.getTranscript(otherStudentId);
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentUpdateOtherStudentTranscriptTest() throws Exception {
        grads.updateTranscript(validStudentId, grads.getTranscript(otherStudentId));
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentAddOtherStudentNoteTest() throws Exception {
        grads.addNote(otherStudentId, "Naughty Naughty");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentGenerateOtherStudentProgressSummaryTest() throws Exception {
        grads.generateProgressSummary(otherStudentId);
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void studentSimulateOtherStudentTest() throws Exception {
        List<CourseTaken> coursesToSimulate = Arrays.asList(new CourseTaken(new Course("Thesis Credit: Doctoral",
                "csci8888", "24", null), new Term(Semester.SPRING, 2010), Grade.S));
        grads.simulateCourses(otherStudentId, coursesToSimulate);
    }
}
