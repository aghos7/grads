package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.filter.CourseFilter;
import edu.umn.csci5801.filter.FilterSet;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;
import edu.umn.csci5801.requirement.CreditRequirement;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class CreditRequirementTest {

    private StudentRecord student;
    private List<CourseTaken> courses;

    @Before
    public void setup() {
        student = new StudentRecord();
        courses = new ArrayList<CourseTaken>();
        student.setCoursesTaken(courses);
    }

    @Test
    public void emptyCourseList_pass() throws Exception {
        CreditRequirement req = new CreditRequirement("Credit Req Test", 0, new FilterSet());
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void emptyCourseList_fail() throws Exception {
        CreditRequirement req = new CreditRequirement("Credit Req Test", 1, new FilterSet());
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void singleCourse_notEnough() throws Exception {
        CreditRequirement req = new CreditRequirement("Credit Req Test", 5, new FilterSet());
        courses.add(new CourseTaken(new Course("Software Engineering 1", "CSCI5801", "3", null), new Term(
                Semester.FALL, 2013), Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void singleCourse_enough() throws Exception {
        CreditRequirement req = new CreditRequirement("Credit Req Test", 3, new FilterSet());
        courses.add(new CourseTaken(new Course("Software Engineering 1", "CSCI5801", "3", null), new Term(
                Semester.FALL, 2013), Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals("3", result.getDetails().getOther().get(0));
    }

    @Test
    public void threeCourses_withDepartmentFilter_passed() throws Exception {
        CreditRequirement req = new CreditRequirement("Credit Req Test", 4, new CourseFilter("CSCI"));
        courses.add(new CourseTaken(new Course("Software Engineering 1", "CSCI5801", "3", null), new Term(
                Semester.FALL, 2013), Grade.A));
        courses.add(new CourseTaken(new Course("Software Engineering 2", "CSCI5802", "3", null), new Term(
                Semester.FALL, 2013), Grade.A));
        courses.add(new CourseTaken(new Course("Underwater Basket Weaving", "BSKT1001", "3", null), new Term(
                Semester.FALL, 2013), Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals("6", result.getDetails().getOther().get(0));
    }
}
