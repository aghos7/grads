package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.filter.CourseFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class CourseFilterTest {

    CourseFilter filter;
    List<CourseTaken> inputList;

    @Before
    public void setup() {
        inputList = new ArrayList<CourseTaken>();
    }

    @Test
    public void emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void dept_singleCourse_validDepartment() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Software Engineering I", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void dept_singleCourse_invalidDepartment() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Underwater Basket Weaving", "BSKT4001"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void reverseDept_singleCourse_invalidDepartment() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("BSKT", true);
        inputList.add(createDummyCourseTaken("Underwater Basket Weaving", "BSKT4001"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void dept_twoCourses_bothValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering II", "CSCI5802"));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("CSCI5802", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void dept_twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT5001"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void reversDept_twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI", true);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT5001"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT5001", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void dept_twoCourses_allInvalid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Underwater Basket Weaving", "BSKT4001"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT5001"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void dept_twoDuplicateCourses_duplicatesAllowed() throws GRADSIllegalArgumentException {
        filter = new CourseFilter("CSCI");
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void level_emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void level_singleCourse_validLevel() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Software Engineering I", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void level_singleCourse_invalidLevel() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Underwater Basket Weaving", "BSKT4001"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void level_twoCourses_bothValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering II", "CSCI5802"));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("CSCI5802", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void level_twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5000"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT2001"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5000", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void level_twoCourses_allInvalid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Underwater Basket Weaving", "BSKT4001"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT2001"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void level_twoDuplicateCourses_duplicatesAllowed() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("CSCI5801", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void level_threeCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(5, 6);
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5000"));
        inputList.add(createDummyCourseTaken("Subsurface Willow Container Fabrication", "BSKT2001"));
        inputList.add(createDummyCourseTaken("Advanced Underwater Basket Weaving", "BSKT7001"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("CSCI5000", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void course_emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(createDummyCourse("Software Engineering I", "CSCI5801"));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void course_singleCourse_validLevel() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(createDummyCourse("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Software Engineering I", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void course_twoCourses_matchedOne() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(createDummyCourse("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering II", "CSCI5802"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Software Engineering I", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void reverseCourse_twoCourses_matchedOtherOne() throws GRADSIllegalArgumentException {
        filter = new CourseFilter(true, createDummyCourse("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering I", "CSCI5801"));
        inputList.add(createDummyCourseTaken("Software Engineering II", "CSCI5802"));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Software Engineering II", filter.filter(inputList).get(0).getCourse().getName());
    }

    Course createDummyCourse(String name, String id) {
        Course course = new Course();
        course.setName(name);
        course.setId(id);
        return course;
    }

    CourseTaken createDummyCourseTaken(String name, String id) {
        Course course = new Course();
        course.setName(name);
        course.setId(id);
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), Grade.A);
    }
}
