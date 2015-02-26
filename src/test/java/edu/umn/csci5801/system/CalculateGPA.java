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
 * Calculate GPA(Test case 14)
 */
public class CalculateGPA {

    private final String studentsFileName = "src/resources/students_phdMissingOneClass.txt";
    private final String coursesFileName = "src/resources/courses.txt";
    private final String usersFileName = "src/resources/users.txt";
    private final String requirementsFileName = "src/resources/requirements.txt";
    private final String studentId = "pocha006";
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
                // overallGPA = result.getDetails().getGPA();
            } else if (result.getName().equals("IN_PROGRAM_GPA_PHD")) {
                Assert.assertTrue(departmentGPA < result.getDetails().getGPA());
                // departmentGPA = result.getDetails().getGPA();
            } else if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                Assert.assertTrue(breadthGPA < result.getDetails().getGPA());
                // breadthGPA = result.getDetails().getGPA();
            }
        }

        simulatedCourses = new ArrayList<CourseTaken>();
        simulatedCourses.add(new CourseTaken(simulatedCourse, new Term(Semester.FALL, 2013), Grade._));
        // simulatedCourses.add(new CourseTaken(new
        // Course("Analysis of Numerical Algorithms", "csci5302", "3",
        // CourseArea.THEORY_ALGORITHMS), new Term(Semester.FALL, 2013),
        // Grade._));
        results = grads.simulateCourses(studentId, simulatedCourses).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("OVERALL_GPA_PHD")) {
                Assert.assertEquals(overallGPA, result.getDetails().getGPA());
            } else if (result.getName().equals("IN_PROGRAM_GPA_PHD")) {
                Assert.assertEquals(departmentGPA, result.getDetails().getGPA());
            } else if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                Assert.assertEquals(breadthGPA, result.getDetails().getGPA());
            }
        }
    }
}
