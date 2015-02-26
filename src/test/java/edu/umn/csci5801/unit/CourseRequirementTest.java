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
import edu.umn.csci5801.requirement.CourseRequirement;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class CourseRequirementTest {

    private StudentRecord student;
    private List<CourseTaken> courses;

    @Before
    public void setup() {
        student = new StudentRecord();
        courses = new ArrayList<CourseTaken>();
        student.setCoursesTaken(courses);
    }

    @Test
    public void emptyCourseList() throws Exception {
        CourseRequirement req = new CourseRequirement("Course Req Test", new FilterSet());
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void singleCourse_emptyFilter_passed() throws Exception {
        Course course = createDummyCourse("Software Engineering I", "CSCI5801");
        CourseRequirement req = new CourseRequirement("Course Req Test", new FilterSet());
        courses.add(createDummyCourseTaken(course, Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void singleCourse_matchingFilter_passed() throws Exception {
        Course course = createDummyCourse("Software Engineering I", "CSCI5801");
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter(course));
        courses.add(createDummyCourseTaken(course, Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void singleCourse_nonMatchingFilter_notPassed() throws Exception {
        Course course1 = createDummyCourse("Software Engineering I", "CSCI5801");
        Course course2 = createDummyCourse("Software Engineering II", "CSCI5802");
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter(course1));
        courses.add(createDummyCourseTaken(course2, Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void twoCourses_duplicates_matchingFilter_passed() throws Exception {
        Course course = createDummyCourse("Software Engineering I", "CSCI5801");
        CourseRequirement req = new CourseRequirement("Course Req Test", new FilterSet(new CourseFilter(course),
                new GradeFilter(Grade.C)));
        courses.add(createDummyCourseTaken(course, Grade.D));
        courses.add(createDummyCourseTaken(course, Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void threeCourses_duplicates_matchingFilter_passed() throws Exception {
        Course course = createDummyCourse("Software Engineering I", "CSCI5801");
        CourseRequirement req = new CourseRequirement("Course Req Test", new FilterSet(new CourseFilter(course),
                new DuplicateFilter(), new GradeFilter(Grade.C)));
        courses.add(createDummyCourseTaken(course, Grade.D));
        courses.add(createDummyCourseTaken(course, Grade.A));
        courses.add(createDummyCourseTaken(course, Grade.B));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void singleCourse_matchingDeptFilter_passed() throws Exception {
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter("CSCI"));
        courses.add(createDummyCourseTaken("Software Engineering I", "CSCI5801", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void singleCourse_nonMatchingDeptFilter_notPassed() throws Exception {
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter("CSCI"));
        courses.add(createDummyCourseTaken("Mathemagic Land", "MCKY3001", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    @Test
    public void singleCourse_matchingLevelFilter_passed() throws Exception {
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter(5));
        courses.add(createDummyCourseTaken("Software Engineering I", "CSCI5801", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void singleCourse_nonMatchingLevelFilter_notPassed() throws Exception {
        CourseRequirement req = new CourseRequirement("Course Req Test", new CourseFilter(5));
        courses.add(createDummyCourseTaken("Mathemagic Land", "MCKY3001", Grade.A));
        RequirementCheckResult result = req.check(student);
        Assert.assertFalse(result.isPassed());
    }

    Course createDummyCourse(String name, String id) {
        Course course = new Course();
        course.setName(name);
        course.setId(id);
        return course;
    }

    CourseTaken createDummyCourseTaken(String name, String id, Grade grade) {
        return new CourseTaken(createDummyCourse(name, id), new Term(Semester.FALL, new Integer(2013)), grade);
    }

    CourseTaken createDummyCourseTaken(Course course, Grade grade) {
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), grade);
    }
}
