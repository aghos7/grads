package edu.umn.csci5801.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.StudentRecord;

/**
 * Add notes to a student record(Test case 15)
 */
public class NotesToStudentRecordTest {

    private GRADSIntf grads;

    private String studentId = "chri3310";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    StudentRecord originalStudentRecord;

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(studentId);
        originalStudentRecord = grads.getTranscript(studentId);
    }

    @After
    public void tearDown() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);
        grads.updateTranscript(studentId, originalStudentRecord);
    }

    @Test
    public void test() throws Exception {
        grads.setUser(gpcId);
        StudentRecord record = grads.getTranscript(studentId);
        assertNotNull(record);
        assertNotNull(record.getNotes());
        assertEquals(0, record.getNotes().size());

        List<String> notesToAmend = Arrays.asList("note 1", "Some random note");
        record.getNotes().add(notesToAmend.get(0));
        grads.updateTranscript(studentId, record);

        StudentRecord oneNoteRecord = grads.getTranscript(studentId);
        assertNotSame(record, oneNoteRecord);
        assertNotNull(oneNoteRecord);
        assertNotNull(oneNoteRecord.getNotes());
        assertEquals(1, oneNoteRecord.getNotes().size());
        assertEquals(oneNoteRecord.getNotes().get(0), notesToAmend.get(0));

        oneNoteRecord.getNotes().add(notesToAmend.get(1));
        grads.updateTranscript(studentId, oneNoteRecord);

        StudentRecord twoNoteRecord = grads.getTranscript(studentId);
        assertNotSame(record, twoNoteRecord);
        assertNotSame(oneNoteRecord, twoNoteRecord);
        assertNotNull(twoNoteRecord);
        assertNotNull(twoNoteRecord.getNotes());
        assertEquals(2, twoNoteRecord.getNotes().size());
        assertEquals(twoNoteRecord.getNotes(), notesToAmend);
    }
}
