package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.filter.AreaFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class AreaFilterTest {

    AreaFilter filter;
    List<CourseTaken> inputList;

    @Before
    public void setup() {
        filter = new AreaFilter(CourseArea.THEORY_ALGORITHMS);
        inputList = new ArrayList<CourseTaken>();
    }

    @Test
    public void emptyCourseList() {
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void singleCourse_validArea() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", CourseArea.THEORY_ALGORITHMS));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Underwater Basket Weaving", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void singleCourse_invalidArea() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001",
                CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void twoCourses_bothValid() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", CourseArea.THEORY_ALGORITHMS));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001",
                CourseArea.THEORY_ALGORITHMS));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("BSKT5001", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void twoCourses_oneValid() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", CourseArea.THEORY_ALGORITHMS));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001",
                CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void twoCourses_allInvalid() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001",
                CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE));
        inputList
                .add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", CourseArea.APPLICATIONS));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void twoDuplicateCourses_duplicatesAllowed() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", CourseArea.THEORY_ALGORITHMS));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", CourseArea.THEORY_ALGORITHMS));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(1).getCourse().getId());
    }

    CourseTaken createDummyCourse(String name, String id, CourseArea area) {
        return new CourseTaken(new Course(name, id, "3", area), new Term(Semester.FALL, new Integer(2013)), Grade.A);
    }
}
