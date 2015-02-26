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
 * Include planned courses that can satisfy multiple breadth requirements(Test
 * case 11)
 */
public class CoursesWithBreadthReqTest {

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

        RequirementCheckResult breadthResult = null;
        RequirementCheckResult breadthResultSimulated = null;
        List<RequirementCheckResult> results = grads.generateProgressSummary(studentId).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResult = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResult);

        results = grads.simulateCourses(studentId, simulatedCourses).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResultSimulated = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResultSimulated);

        Assert.assertTrue(breadthResult.getDetails().getGPA() < breadthResultSimulated.getDetails().getGPA());
    }

    @Test
    public void test_betterGradeWillBePicked() throws Exception {
        Course simulatedCourse = new Course("Virtual Reality and 3D Interaction", "csci5619", "3",
                CourseArea.APPLICATIONS);
        List<CourseTaken> simulatedCourses = new ArrayList<CourseTaken>();
        simulatedCourses.add(new CourseTaken(simulatedCourse, new Term(Semester.FALL, 2013), Grade.A));

        RequirementCheckResult breadthResult = null;
        RequirementCheckResult breadthResultSimulated = null;
        List<RequirementCheckResult> results = grads.generateProgressSummary(studentId).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResult = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResult);

        results = grads.simulateCourses(studentId, simulatedCourses).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResultSimulated = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResultSimulated);

        Course previousApplicationsClass = null;
        for (CourseTaken courseTaken : breadthResult.getDetails().getCourses()) {
            if (courseTaken.getCourse().getCourseArea().equals(CourseArea.APPLICATIONS)) {
                previousApplicationsClass = courseTaken.getCourse();
                break;
            }
        }
        Assert.assertNotNull(previousApplicationsClass);

        for (CourseTaken courseTaken : breadthResultSimulated.getDetails().getCourses()) {
            if (courseTaken.getCourse().getCourseArea().equals(CourseArea.APPLICATIONS)) {
                Assert.assertFalse(previousApplicationsClass.getId().equals(courseTaken.getCourse().getId()));
                break;
            }
        }
    }

    @Test
    public void test_missingClassCausesReqToPass() throws Exception {
        Course simulatedCourse = new Course("Analysis of Numerical Algorithms", "csci5302", "3",
                CourseArea.THEORY_ALGORITHMS);
        List<CourseTaken> simulatedCourses = new ArrayList<CourseTaken>();
        simulatedCourses.add(new CourseTaken(simulatedCourse, new Term(Semester.FALL, 2013), Grade.A));

        RequirementCheckResult breadthResult = null;
        RequirementCheckResult breadthResultSimulated = null;
        List<RequirementCheckResult> results = grads.generateProgressSummary(studentId).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResult = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResult);
        Assert.assertFalse(breadthResult.isPassed());

        results = grads.simulateCourses(studentId, simulatedCourses).getRequirementCheckResults();
        for (RequirementCheckResult result : results) {
            if (result.getName().equals("BREADTH_REQUIREMENT_PHD")) {
                breadthResultSimulated = result;
                break;
            }
        }
        Assert.assertNotNull(breadthResultSimulated);
        Assert.assertTrue(breadthResultSimulated.isPassed());
    }
}
