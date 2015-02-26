package edu.umn.csci5801.system;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Retrieve courses for a student who has completed courses(Test case 7)
 */
public class RetrieveStudentCompleteCoursesTest {

    private GRADSIntf grads;

    private String studentsFileName = "src/resources/students_Expected_ande2807.txt";
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
        List<Course> courseList = new Gson().fromJson(new FileReader(new File(coursesFileName)),
                new TypeToken<List<Course>>() {
                }.getType());
        Map<String, Course> courseMap = new HashMap<String, Course>();
        for (Course course : courseList) {
            courseMap.put(course.getId(), course);
        }

        Term term = new Term();
        term.setSemester(Semester.SPRING);
        term.setYear(2010);

        List<CourseTaken> expectedCoursesTaken = new ArrayList<CourseTaken>();
        // Breadth Reqs
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
        // Colloquium
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
        // Two 8xxx level courses
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci8101"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci8102"), term, Grade.A));
        // Total 31 credits, at least 16 csci
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
        expectedCoursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));

        String studentId = "ande2807";
        grads.setUser(studentId);

        List<CourseTaken> retrievedCoursesTaken = grads.getTranscript(studentId).getCoursesTaken();

        assertTrue(expectedCoursesTaken.size() == retrievedCoursesTaken.size());
        assertTrue(expectedCoursesTaken.containsAll(retrievedCoursesTaken));
    }
}
