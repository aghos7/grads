package edu.umn.csci5801.codeCoverage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.datastore.Utilities;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

/**
 * 
 */
public class CodeCoverageTest {

    private GRADSIntf grads;

    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        StudentRecord expectedRecord = new StudentRecord();
        expectedRecord.setStudent(new Student("Lucas", "Anderson", "ande2807"));
        expectedRecord.setDepartment(Department.COMPUTER_SCIENCE);
        expectedRecord.setDegreeSought(Degree.MS_C);
        expectedRecord.setTermBegan(new Term(Semester.FALL, 2010));
        expectedRecord.setAdvisors(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));
        expectedRecord.setCommittee(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));

        List<Course> courseList = new Gson().fromJson(new FileReader(new File(coursesFileName)),
                new TypeToken<List<Course>>() {
                }.getType());
        Map<String, Course> courseMap = new HashMap<String, Course>();
        for (Course course : courseList) {
            courseMap.put(course.getId(), course);
        }
        Term term = new Term(Semester.SPRING, 2010);

        List<CourseTaken> coursesTaken = new ArrayList<CourseTaken>();
        // Breadth Reqs
        coursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
        // Colloquium
        coursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
        // Two 8xxx level courses
        coursesTaken.add(new CourseTaken(courseMap.get("csci8101"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci8102"), term, Grade.A));
        // Total 31 credits, at least 16 csci
        coursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
        coursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));
        expectedRecord.setCoursesTaken(coursesTaken);

        // Set needed milestones
        List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
        milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
        milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
        milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_SUBMITTED, term));
        milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_APPROVED, term));
        milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
        expectedRecord.setMilestonesSet(milestonesSet);

        expectedRecord.setNotes(Arrays.asList("note1", "note2", "note3"));
        StudentRecord expectedCopy = Utilities.cloneThroughJson(expectedRecord);

        String GpcId = "tolas9999";
        grads.setUser(GpcId);

        String studentId = "ande2807";
        StudentRecord retrievedRecord = grads.getTranscript(studentId);
        assertEquals(expectedRecord.getDepartment(), retrievedRecord.getDepartment());
        assertEquals(expectedRecord.getDegreeSought(), retrievedRecord.getDegreeSought());
        assertEquals(expectedRecord.getTermBegan(), retrievedRecord.getTermBegan());

        assertEquals(expectedRecord.getAdvisors(), retrievedRecord.getAdvisors());
        assertEquals(expectedRecord.getCommittee(), retrievedRecord.getCommittee());
        assertEquals(expectedRecord.getMilestonesSet(), retrievedRecord.getMilestonesSet());
        assertEquals(expectedRecord.getNotes(), retrievedRecord.getNotes());

        assertTrue(expectedRecord.equals(retrievedRecord));
        assertFalse(expectedRecord.equals(null));
        assertTrue(expectedRecord.equals(expectedRecord));
        assertEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());

        // Null some stuff out
        retrievedRecord.setAdvisors(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setCommittee(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setCoursesTaken(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setDegreeSought(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setMilestonesSet(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setDepartment(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setStudent(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);

        retrievedRecord.setTermBegan(null);
        assertFalse(expectedRecord.equals(retrievedRecord));
        assertFalse(retrievedRecord.equals(expectedRecord));
        assertNotEquals(expectedRecord.hashCode(), retrievedRecord.hashCode());
        retrievedRecord = grads.getTranscript(studentId);
        assertFalse(retrievedRecord.equals(new Integer(3)));

        // Exercise the equals function on Student
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord)); // Not
                                                                          // the
                                                                          // same
                                                                          // type
                                                                          // of
                                                                          // object
        assertTrue(retrievedRecord.getStudent().equals(retrievedRecord.getStudent())); // objects
                                                                                       // ==
        retrievedRecord.getStudent().setId(null);
        assertNotEquals(expectedRecord.getStudent().hashCode(), retrievedRecord.getStudent().hashCode());
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        expectedRecord.getStudent().setId(null);
        assertTrue(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        retrievedRecord.getStudent().setId("bob");
        expectedRecord.getStudent().setId("steve");
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));

        // Change their names
        expectedRecord = Utilities.cloneThroughJson(expectedCopy);
        retrievedRecord = grads.getTranscript(studentId);
        assertEquals(retrievedRecord.getStudent().getFirstName(), retrievedRecord.getStudent().getFirstName());
        assertEquals(retrievedRecord.getStudent().getLastName(), retrievedRecord.getStudent().getLastName());
        retrievedRecord.getStudent().setFirstName("bob");
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        retrievedRecord.getStudent().setFirstName(null);
        assertNotEquals(expectedRecord.getStudent().hashCode(), retrievedRecord.getStudent().hashCode());
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        expectedRecord.getStudent().setFirstName(null);
        assertTrue(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        // Last names
        expectedRecord = Utilities.cloneThroughJson(expectedCopy);
        retrievedRecord = grads.getTranscript(studentId);
        retrievedRecord.getStudent().setLastName("bob");
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        retrievedRecord.getStudent().setLastName(null);
        assertNotEquals(expectedRecord.getStudent().hashCode(), retrievedRecord.getStudent().hashCode());
        assertFalse(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
        expectedRecord.getStudent().setLastName(null);
        assertTrue(retrievedRecord.getStudent().equals(expectedRecord.getStudent()));
    }
}
