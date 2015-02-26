package edu.umn.csci5801.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.datastore.JSONStudentRecordRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;
import edu.umn.csci5801.service.StudentRecordService;

public class StudentRecordServiceTest {

    private StudentRecordService studentRecordService;

    @Before
    public void setup() throws GRADSIllegalArgumentException, GRADSException {
        studentRecordService = new StudentRecordService(new JSONStudentRecordRepository("src/resources/courses.txt",
                "src/resources/students.txt"));
    }

    @Test(expected = GRADSIllegalArgumentException.class)
    public void testGetStudentWithNullId() throws GRADSException {
        studentRecordService.getTranscript(null);
    }

    @Test
    public void testGetStudentWithExistingIdStudent() throws GRADSException {
        String existingId = "nguy0621";
        StudentRecord studentRecord = studentRecordService.getTranscript(existingId);
        assertNotNull(studentRecord);
        assertEquals(existingId, studentRecord.getStudent().getId());
        assertEquals(studentRecord.getDepartment(), Department.COMPUTER_SCIENCE);
        assertEquals(studentRecord.getDegreeSought(), Degree.PHD);
        assertTrue(studentRecord.getTermBegan().getYear() == 2008);
        assertTrue(studentRecord.getAdvisors().size() == 1);
        assertTrue(studentRecord.getCommittee().size() == 3);
        assertTrue(studentRecord.getCoursesTaken().size() == 11);
        assertTrue(studentRecord.getMilestonesSet().size() == 1);
        assertTrue(studentRecord.getNotes().size() == 2);
        assertEquals(studentRecord.getCoursesTaken().get(3).getCourse().getId(), "csci5106");
        assertEquals(studentRecord.getCoursesTaken().get(3).getCourse().getCourseArea(),
                CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE);
    }

    @Test(expected = GRADSException.class)
    public void testGetStudentWithNonExistingId() throws GRADSException {
        studentRecordService.getTranscript("123456");
    }

    @Test
    public void getStudentsByDepartment() throws GRADSIllegalArgumentException, GRADSException {
        // use a new student record service with a different students file
        StudentRecordService studentRecordService = new StudentRecordService(new JSONStudentRecordRepository(
                "src/resources/courses.txt", "src/resources/students_multDepartments.txt"));
        List<String> csStudents = studentRecordService.getStudentsByDepartment(Department.COMPUTER_SCIENCE);
        assertTrue(csStudents.size() == 3);
        assertTrue(csStudents.contains("nguy0621"));
        assertTrue(csStudents.contains("gayxx067"));
        assertTrue(csStudents.contains("student3"));

        List<String> mathStudents = studentRecordService.getStudentsByDepartment(Department.MATH);
        assertTrue(mathStudents.size() == 1);
        assertEquals(mathStudents.get(0), "student1");
    }

    @Test
    public void testAddNote() throws GRADSException {
        String studentId = "gayxx067";
        String newNote = "test note at " + new Date();

        // make sure the student doesn't already have the note and save a copy
        // of the before notes
        List<String> beforeNotes = new ArrayList<String>(studentRecordService.getTranscript(studentId).getNotes());
        assertFalse(beforeNotes.contains(newNote));

        // add the note
        studentRecordService.addNote(studentId, newNote);

        // make sure the student now has the new note
        StudentRecord afterStudentRecord = studentRecordService.getTranscript(studentId);
        assertTrue(afterStudentRecord.getNotes().contains(newNote));
        assertTrue(afterStudentRecord.getNotes().size() == (beforeNotes.size() + 1));

        // reset the student record the way it was
        afterStudentRecord.setNotes(beforeNotes);
        studentRecordService.updateTranscript(afterStudentRecord);
    }

    @Test
    public void testUpdateTranscriptChangeGrade() throws GRADSException {
        String studentId = "nguy0621";
        String courseId = "csci5525";
        Grade newGrade = Grade.B;

        StudentRecord transcript = studentRecordService.getTranscript(studentId);

        CourseTaken courseTaken = findCourse(courseId, transcript.getCoursesTaken());
        assertNotNull(courseTaken);
        assertNotEquals(courseTaken.getGrade(), newGrade);
        Grade oldGrade = courseTaken.getGrade();
        courseTaken.setGrade(newGrade);
        studentRecordService.updateTranscript(transcript);

        transcript = studentRecordService.getTranscript(studentId);
        courseTaken = findCourse(courseId, transcript.getCoursesTaken());
        assertNotNull(courseTaken);
        assertEquals(courseTaken.getGrade(), newGrade);

        // set the grade back
        courseTaken.setGrade(oldGrade);
        studentRecordService.updateTranscript(transcript);
    }

    private CourseTaken findCourse(String courseId, List<CourseTaken> coursesTaken) {
        for (CourseTaken courseTaken : coursesTaken) {
            if (courseTaken.getCourse().getId().equals(courseId)) {
                return courseTaken;
            }
        }
        return null;
    }

    @Test
    public void testUpdateTranscriptAddCourseTaken() throws GRADSException {
        String studentId = "nguy0621";
        String courseId = "csci8980";
        Grade grade = Grade.B;

        StudentRecord transcript = studentRecordService.getTranscript(studentId);

        CourseTaken courseTaken = findCourse(courseId, transcript.getCoursesTaken());
        assertNull(courseTaken);

        // create a new course and add it
        courseTaken = new CourseTaken();
        Course course = new Course();
        course.setId(courseId);
        courseTaken.setCourse(course);
        courseTaken.setGrade(grade);
        courseTaken.setTerm(new Term(Semester.FALL, 2013));
        transcript.getCoursesTaken().add(courseTaken);
        studentRecordService.updateTranscript(transcript);

        transcript = studentRecordService.getTranscript(studentId);
        courseTaken = findCourse(courseId, transcript.getCoursesTaken());
        assertNotNull(courseTaken);
        assertEquals(courseTaken.getGrade(), grade);

        // remove the course
        transcript.getCoursesTaken().remove(courseTaken);
        studentRecordService.updateTranscript(transcript);
    }
}
