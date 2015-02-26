package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.filter.DuplicateFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class DuplicateFilterTest {

    DuplicateFilter filter;
    List<CourseTaken> inputList;

    @Before
    public void setup() {
        filter = new DuplicateFilter();
        inputList = new ArrayList<CourseTaken>();
    }

    @Test
    public void emptyCourseList() {
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void singleCourse() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Underwater Basket Weaving", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void twoCourses_bothUnique() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.B));
        Assert.assertEquals(2, filter.filter(inputList).size());

        // A little sleight of hand because the order is not preserved
        if (filter.filter(inputList).get(0).getCourse().getId().equals("BSKT4001")) {
            Assert.assertEquals("BSKT5001", filter.filter(inputList).get(1).getCourse().getId());
        } else {
            Assert.assertEquals("BSKT5001", filter.filter(inputList).get(0).getCourse().getId());
            Assert.assertEquals("BSKT4001", filter.filter(inputList).get(1).getCourse().getId());
        }
    }

    @Test
    public void twoCourses_duplicates() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.C));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.B));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals(Grade.B, filter.filter(inputList).get(0).getGrade());
    }

    @Test
    public void twoCourses_duplicates_sameGrade() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals(Grade.A, filter.filter(inputList).get(0).getGrade());
    }

    @Test
    public void twoCourses_duplicates_reversedGrades() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.B));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals(Grade.A, filter.filter(inputList).get(0).getGrade());
    }

    @Test
    public void twoCourses_duplicates_differentBases() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade._));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.D));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals(Grade.D, filter.filter(inputList).get(0).getGrade());
    }

    @Test
    public void fiveCourses_twoDuplicates() {
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.D));
        inputList.add(createDummyCourse("Software Engineering I", "CSCI5801", Grade._));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.B));
        inputList.add(createDummyCourse("Operating Systems", "CSCI5108", Grade.A));
        inputList.add(createDummyCourse("Operating Systems", "CSCI5108", Grade.B));
        Assert.assertEquals(3, filter.filter(inputList).size());
    }

    CourseTaken createDummyCourse(String name, String id, Grade grade) {
        Course course = new Course();
        course.setName(name);
        course.setId(id);
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), grade);
    }
}
