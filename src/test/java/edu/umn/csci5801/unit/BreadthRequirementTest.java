package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;
import edu.umn.csci5801.requirement.BreadthRequirement;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class BreadthRequirementTest {

    private StudentRecord student;
    private List<CourseTaken> courses;
    private Map<CourseArea, Integer> coursesPerArea;

    @Before
    public void setup() {
        student = new StudentRecord();
        courses = new ArrayList<CourseTaken>();
        student.setCoursesTaken(courses);
        coursesPerArea = new HashMap<CourseArea, Integer>();
    }

    @Test
    public void oneCourseFromOneArea_pass() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 1, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void oneCourseFromOneArea_pass_duplicateCourseFixesBadGrade() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.D));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 1, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void oneCourseFromOneArea_fail_lowGpa() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 1, (float) 3.5);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromOneArea_fail_wrongArea() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 1, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromOneArea_fail_wrongTotal() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromOneAreaTwoTotal_pass() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5161", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void oneCourseFromTwoAreas_pass() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void oneCourseFromTwoAreas_fail_lowGpa() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.5);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromTwoAreas_pass_incompleteCourseDoesNotChangeGPA() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade._));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.5);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
        Assert.assertEquals(new Float(4.0), result.getDetails().getGPA());
    }

    @Test
    public void oneCourseFromTwoAreas_fail_wrongArea() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 2, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromTwoAreas_fail_wrongTotal() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 3, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromEachArea_pass() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5471", CourseArea.THEORY_ALGORITHMS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.C));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 3, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void oneCourseFromEachArea_fail_lowGpa() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5471", CourseArea.THEORY_ALGORITHMS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 3, (float) 3.5);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromEachArea_fail_wrongTotal() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5471", CourseArea.THEORY_ALGORITHMS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 4, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromEachArea_fail_notEnoughInArea() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5471", CourseArea.THEORY_ALGORITHMS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 2);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 3, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void oneCourseFromEachArea_pass_fiveTotal() throws Exception {
        courses.add(createDummyCourseTaken("CSCI5103", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5271", CourseArea.APPLICATIONS, Grade.C));
        courses.add(createDummyCourseTaken("CSCI5471", CourseArea.THEORY_ALGORITHMS, Grade.B));
        courses.add(createDummyCourseTaken("CSCI5525", CourseArea.THEORY_ALGORITHMS, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5801", CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, Grade.A));
        courses.add(createDummyCourseTaken("CSCI5802", null, Grade.D));
        coursesPerArea.put(CourseArea.APPLICATIONS, 1);
        coursesPerArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesPerArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        BreadthRequirement req = new BreadthRequirement("Breadth Req Test", coursesPerArea, 3, (float) 3.0);
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    Course createDummyCourse(String id, CourseArea area) {
        Course course = new Course();
        course.setName(id);
        course.setId(id);
        course.setCourseArea(area);
        course.setNumCredits("3");
        return course;
    }

    CourseTaken createDummyCourseTaken(String id, CourseArea area, Grade grade) {
        return new CourseTaken(createDummyCourse(id, area), new Term(Semester.FALL, new Integer(2013)), grade);
    }

    CourseTaken createDummyCourseTaken(Course course, Grade grade) {
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), grade);
    }

}
