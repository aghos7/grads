package edu.umn.csci5801.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

/**
 * GPCs can amend the student records in their department(Test case 19)
 */
public class AmendStudentRecordTest {

    private GRADSIntf grads;

    private String studentId = "nguy0621";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students.txt";
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

        StudentRecord amendToRecord = grads.getTranscript(studentId);
        Term term = new Term();
        term.setSemester(Semester.SPRING);
        term.setYear(2013);

        CourseTaken amendCourse = new CourseTaken(new Course("Thesis Credit: Doctoral", "csci8888", "12", null), term,
                Grade.S);
        amendCourse.getTerm().setSemester(amendCourse.getTerm().getSemester().next());
        assertFalse(amendToRecord.getCoursesTaken().contains(amendCourse));
        amendToRecord.getCoursesTaken().add(amendCourse);

        CourseTaken amendCourse2 = new CourseTaken(new Course("Thesis Credit: Doctoral", "csci8888", "10", null), term,
                Grade.S);
        amendCourse2.getTerm().setSemester(amendCourse2.getTerm().getSemester().next());
        term = term.getNext();
        amendToRecord.getCoursesTaken().add(amendCourse2);

        CourseTaken amendCourse3 = new CourseTaken(new Course("Thesis Credit: Doctoral", "csci8888", "8", null), term,
                Grade.S);
        amendCourse3.getTerm().setSemester(amendCourse3.getTerm().getSemester().next());
        term = term.getNext(3);
        assertFalse(amendToRecord.getCoursesTaken().contains(amendCourse3));
        amendToRecord.getCoursesTaken().add(amendCourse3);

        MilestoneSet amendMilestoneSet = new MilestoneSet();
        amendMilestoneSet.setMilestone(Milestone.THESIS_APPROVED);
        amendMilestoneSet.setTerm(new Term(Semester.FALL, 2013));

        assertFalse(amendToRecord.getMilestonesSet().contains(amendMilestoneSet));

        amendToRecord.getMilestonesSet().add(amendMilestoneSet);

        String amendNote = "I'm a new note";
        assertFalse(amendToRecord.getNotes().contains(amendNote));

        amendToRecord.getNotes().add(amendNote);

        grads.updateTranscript(studentId, amendToRecord);

        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);

        StudentRecord persistedRecord = grads.getTranscript(studentId);

        assertEquals(amendToRecord, persistedRecord);
    }

}
