package edu.umn.csci5801.system;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.exception.GRADSAuthorizationException;
import edu.umn.csci5801.model.StudentRecord;
/**
 * GPCs have access to students in their department(Test case 18)
 */
public class GpcAccessTest {
    private GRADSIntf grads;

    private String mathStudentId = "math0001";
    private String csciGpcId = "tolas9999";
    private String mathGpcId = "smith0001";
    private String studentsFileName = "src/resources/students.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    StudentRecord mathStudentOriginalRecord;
    
    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(mathGpcId);
        mathStudentOriginalRecord = grads.getTranscript(mathStudentId);
    }

    @After
    public void tearDown() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(mathGpcId);
        grads.updateTranscript(mathStudentId, mathStudentOriginalRecord);
    }
    
    @Test
    public void csciGpcAccessCsciStudentsTest() throws Exception {
        grads.setUser(csciGpcId);
        List<String> studentIds = grads.getStudentIDs();
        assertNotNull(studentIds);
        assertNotEquals(0, studentIds.size());
        String studentId = studentIds.get(0);
        grads.generateProgressSummary(studentId);
        StudentRecord originalRecord = grads.getTranscript(studentId);
        grads.addNote(studentId, "howdy");
        grads.updateTranscript(studentId, originalRecord);
    }
    
    @Test(expected = GRADSAuthorizationException.class)
    public void csciGpcAccessMathStudentProgressSummaryTest() throws Exception {
        grads.setUser(csciGpcId);
        grads.generateProgressSummary(mathStudentId);
    }
    
    @Test(expected = GRADSAuthorizationException.class)
    public void csciGpcAccessMathStudentRecordTest() throws Exception {
        grads.setUser(csciGpcId);
        grads.getTranscript(mathStudentId);
    }
    
    @Test(expected = GRADSAuthorizationException.class)
    public void csciGpcAddNoteToMathStudentTest() throws Exception {
        grads.setUser(csciGpcId);
        grads.addNote(mathStudentId, "howdy");
    }
    
    @Test(expected = GRADSAuthorizationException.class)
    public void csciGpcUpdateMathStudentRecordTest() throws Exception {
        grads.setUser(csciGpcId);
        grads.updateTranscript(mathStudentId, mathStudentOriginalRecord);
    }
}
