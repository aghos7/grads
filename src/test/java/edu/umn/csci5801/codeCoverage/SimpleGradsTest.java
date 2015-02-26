package edu.umn.csci5801.codeCoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSAuthorizationException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

/**
 * 
 */
public class SimpleGradsTest {

    private GRADSIntf grads;

    private String studentId = "ande2807";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    StudentRecord originalStudentRecord;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(studentId);
        originalStudentRecord = grads.getTranscript(studentId);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        grads.setUser(gpcId);
        grads.updateTranscript(studentId, originalStudentRecord);
    }

    @Test
    public void testHappyPath() {
        try {
            grads.setUser(gpcId);
            assertEquals(gpcId, grads.getUser());

            List<String> myStudentsIds = grads.getStudentIDs();
            List<CourseTaken> simulatedCourses = Arrays.asList(new CourseTaken(new Course("Thesis Credit: Doctoral",
                    "csci8888", "24", null), new Term(Semester.SPRING, 2010), Grade.S));

            assertTrue(myStudentsIds.contains(studentId));

            if (studentId != null) {
                StudentRecord studentRecord = grads.getTranscript(studentId);
                grads.generateProgressSummary(studentId);
                grads.simulateCourses(studentId, simulatedCourses);

                grads.addNote(studentId, gpcId + ": " + new Date());
                grads.updateTranscript(studentId, studentRecord);

                // Gson gson = new GsonBuilder().setPrettyPrinting().create();
                // System.out.println(gson.toJson(studentRecord));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test(expected = GRADSIllegalArgumentException.class)
    public void testAddNullNote() throws Exception {
        String GpcId = "tolas9999";
        grads.setUser(GpcId);
        assertEquals(GpcId, grads.getUser());
        grads.addNote("ande2807", null);
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testAddNoteToInvalidUser() throws Exception {
        String GpcId = "tolas9999";
        grads.setUser(GpcId);
        assertEquals(GpcId, grads.getUser());
        grads.addNote("foobar", "hi");
    }
}
