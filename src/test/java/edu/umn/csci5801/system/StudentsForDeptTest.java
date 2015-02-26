package edu.umn.csci5801.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;

/**
 * Retrieve the list of students for a department(Test case 16)
 */
public class StudentsForDeptTest {
    private GRADSIntf grads;

    private List<String> csciStudentIds = Arrays.asList("nguy0621","gayxx067");
    private List<String> mathStudentIds = Arrays.asList("math0001");
    private String csciGpcId = "tolas9999";
    private String mathGpcId = "smith0001";
    private String studentsFileName = "src/resources/students.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @After
    public void tearDown() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @Test
    public void csciGpcGetStudentIdsTest() throws Exception {
        grads.setUser(csciGpcId);
        List<String> studentIds = grads.getStudentIDs();
        assertNotNull(studentIds);
        for (String mathStudentId : mathStudentIds) {
            assertFalse(studentIds.contains(mathStudentId));
        }
        assertEquals(csciStudentIds.size(), studentIds.size());
        assertTrue(studentIds.containsAll(csciStudentIds));
    }
    
    @Test
    public void mathGpcGetStudentIdsTest() throws Exception {
        grads.setUser(mathGpcId);
        List<String> studentIds = grads.getStudentIDs();
        assertNotNull(studentIds);
        for (String csciStudentId : csciStudentIds) {
            assertFalse(studentIds.contains(csciStudentId));
        }
        assertEquals(mathStudentIds.size(), studentIds.size());
        assertTrue(studentIds.containsAll(mathStudentIds));
    }
}
