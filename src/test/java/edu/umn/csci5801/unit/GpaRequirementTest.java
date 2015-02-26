package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.filter.CourseFilter;
import edu.umn.csci5801.filter.DuplicateFilter;
import edu.umn.csci5801.filter.FilterSet;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;
import edu.umn.csci5801.requirement.GpaRequirement;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class GpaRequirementTest {

    private StudentRecord student;
    private List<CourseTaken> courses;

    @Before
    public void setup() {
        student = new StudentRecord();
        courses = new ArrayList<CourseTaken>();
        student.setCoursesTaken(courses);
    }

    // Assuming that an empty set of courses does not pass!
    @Test
    public void emptyCourseList() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void singleCourse_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(4.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void singleCourse_equalPassed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.B));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void singleCourse_notPassed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.F));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void twoCourses_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.A));
        courses.add(createDummyCourseTaken("", "3", Grade.B));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.5, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void twoCourses_passed2() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.C));
        courses.add(createDummyCourseTaken("", "3", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void twoCourses_passed3() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.B));
        courses.add(createDummyCourseTaken("", "3", Grade.B));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void twoCourses_differingCredits_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.A));
        courses.add(createDummyCourseTaken("", "1", Grade.F));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void twoCourses_notPassed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new FilterSet());
        courses.add(createDummyCourseTaken("", "3", Grade.B));
        courses.add(createDummyCourseTaken("", "3", Grade.C));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void threeCourses_withDepartmentFilter_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new CourseFilter("CSCI"));
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.A));
        courses.add(createDummyCourseTaken("BSKT1001", "3", Grade.D));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.5, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void fourCourses_withBasisFilter_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(2.5), new GradeFilter(Grade.A, Grade.F));
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.A));
        courses.add(createDummyCourseTaken("BSKT1001", "3", Grade.D));
        courses.add(createDummyCourseTaken("BSKT1002", "3", Grade.S));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(2.666, result.getDetails().getGPA().floatValue(), 0.01);
    }

    @Test
    public void fiveCourses_withDuplicateFilter_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.0), new DuplicateFilter());
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.F));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.F));
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.A));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.A));
        courses.add(createDummyCourseTaken("BSKT1001", "3", Grade.D));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(3.0, result.getDetails().getGPA().floatValue(), 0);
    }

    @Test
    public void fiveCourses_withDuplicateAndDeptFilter_passed() throws Exception {
        GpaRequirement req = new GpaRequirement("GPA Req Test", new Float(3.5), new FilterSet(new DuplicateFilter(),
                new CourseFilter("CSCI")));
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.F));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.F));
        courses.add(createDummyCourseTaken("CSCI5801", "3", Grade.A));
        courses.add(createDummyCourseTaken("CSCI5802", "3", Grade.A));
        courses.add(createDummyCourseTaken("BSKT1001", "3", Grade.D));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(4.0, result.getDetails().getGPA().floatValue(), 0);
    }

    Course createDummyCourse(String id, String credits) {
        return new Course(id, id, credits, null);
    }

    CourseTaken createDummyCourseTaken(String id, String credits, Grade grade) {
        return new CourseTaken(createDummyCourse(id, credits), new Term(Semester.FALL, new Integer(2013)), grade);
    }

    CourseTaken createDummyCourseTaken(Course course, Grade grade) {
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), grade);
    }
}
