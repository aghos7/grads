package edu.umn.csci5801.system;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Set a duplicate planned breadth course that has already been completed(Test
 * case 13)
 */
public class DuplicateBreadthCourseTest {

    private String studentsFileName = "src/resources/students_phdMissingOneClass.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";
    private String requirementsFileName = "src/resources/requirements.txt";
    private String studentId = "pocha006";
    private GRADSIntf grads;

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, requirementsFileName);
        grads.setUser(studentId);
    }

    @Test
    public void test_betterGradeWillIncreaseGPA() throws Exception {
        Course simulatedCourse = new Course("Virtual Reality and 3D Interaction", "csci5619", "3",
                CourseArea.APPLICATIONS);
        List<CourseTaken> simulatedCourses = new ArrayList<CourseTaken>();
        simulatedCourses.add(new CourseTaken(simulatedCourse, new Term(Semester.FALL, 2013), Grade.A));

        Float overallGPA = null;
        Float departmentGPA = null;
        Float breadthGPA = null;
        List<RequirementCheckResult> results = grads.generateProgressSummary(studentId).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("OVERALL_GPA_PHD")) {
                overallGPA = result.getDetails().getGPA();
            } else if (result.getName().equals("IN_PROGRAM_GPA_PHD")) {
                departmentGPA = result.getDetails().getGPA();
            } else if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthGPA = result.getDetails().getGPA();
            }
        }
        Assert.assertNotNull(overallGPA);
        Assert.assertNotNull(departmentGPA);
        Assert.assertNotNull(breadthGPA);

        results = grads.simulateCourses(studentId, simulatedCourses).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("OVERALL_GPA_PHD")) {
                Assert.assertTrue(overallGPA < result.getDetails().getGPA());
            } else if (result.getName().equals("IN_PROGRAM_GPA_PHD")) {
                Assert.assertTrue(departmentGPA < result.getDetails().getGPA());
            } else if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                Assert.assertTrue(breadthGPA < result.getDetails().getGPA());
            }
        }
    }
}
