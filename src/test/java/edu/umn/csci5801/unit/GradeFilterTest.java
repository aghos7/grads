package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;

/**
 * Tests for {@link Foo}.
 */
@RunWith(JUnit4.class)
public class GradeFilterTest {

    GradeFilter filter;
    List<CourseTaken> inputList;

    @Before
    public void setup() {
        inputList = new ArrayList<CourseTaken>();
    }

    @Test
    public void emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void singleCourse_validGrade() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Underwater Basket Weaving", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void singleCourse_equalGrade() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.C));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("Underwater Basket Weaving", filter.filter(inputList).get(0).getCourse().getName());
    }

    @Test
    public void singleCourse_invalidGrade() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.D));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void twoCourses_bothValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.B));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals("BSKT5001", filter.filter(inputList).get(1).getCourse().getId());
    }

    @Test
    public void twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.F));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.C));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT5001", filter.filter(inputList).get(0).getCourse().getId());
    }

    @Test
    public void twoCourses_allInvalid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.F));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.D));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void twoDuplicateCourses_duplicatesAllowed() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.C));
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.B));
        Assert.assertEquals(2, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
        Assert.assertEquals(Grade.C, filter.filter(inputList).get(0).getGrade());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(1).getCourse().getId());
        Assert.assertEquals(Grade.B, filter.filter(inputList).get(1).getGrade());
    }

    @Test
    public void singleCourse_gradeS() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.S));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void singleCourse_gradeN() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.N));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void singleCourse_grade_() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.C);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade._));
        Assert.assertEquals(1, filter.filter(inputList).size());
        Assert.assertEquals("BSKT4001", filter.filter(inputList).get(0).getCourse().getId());
    }

    /* Basis tests */
    @Test
    public void abcdf_emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void abcdf_singleCourse_validBasis() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getName(), "Underwater Basket Weaving");
    }

    @Test
    public void abcdf_singleCourse_invalidBasis() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.S));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void abcdf_twoCourses_bothValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.B));
        Assert.assertEquals(filter.filter(inputList).size(), 2);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT4001");
        Assert.assertEquals(filter.filter(inputList).get(1).getCourse().getId(), "BSKT5001");
    }

    @Test
    public void abcdf_twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.N));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.F));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT5001");
    }

    @Test
    public void abcdf_twoCourses_allInvalid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.S));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.N));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void abcdf_singleCourse_grade_() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.A, Grade.F);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade._));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT4001");
    }

    @Test(expected = GRADSIllegalArgumentException.class)
    public void abcdf_invalidParamters() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.D, Grade.B);
    }

    @Test
    public void sn_emptyCourseList() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void sn_singleCourse_validBasis() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.S));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getName(), "Underwater Basket Weaving");
    }

    @Test
    public void sn_singleCourse_invalidBasis() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.C));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void sn_twoCourses_bothValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.N));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.S));
        Assert.assertEquals(filter.filter(inputList).size(), 2);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT4001");
        Assert.assertEquals(filter.filter(inputList).get(1).getCourse().getId(), "BSKT5001");
    }

    @Test
    public void sn_twoCourses_oneValid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.N));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.F));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT4001");
    }

    @Test
    public void sn_twoCourses_allInvalid() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.D));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT5001", Grade.A));
        Assert.assertTrue(filter.filter(inputList).isEmpty());
    }

    @Test
    public void sn_singleCourse_grade_() throws GRADSIllegalArgumentException {
        filter = new GradeFilter(Grade.S, Grade.N);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade._));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT4001");
    }

    @Test
    public void incompleteFilter_twoCourses() throws GRADSIllegalArgumentException {
        filter = new GradeFilter();
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade._));
        inputList.add(createDummyCourse("Advanced Underwater Basket Weaving", "BSKT5001", Grade.D));
        Assert.assertEquals(filter.filter(inputList).size(), 1);
        Assert.assertEquals(filter.filter(inputList).get(0).getCourse().getId(), "BSKT5001");
    }

    @Test
    public void setOfGradesFilter_sevenCourses_keepsThree() throws GRADSIllegalArgumentException {
        HashSet<Grade> grades = new HashSet<Grade>();
        grades.add(Grade.A);
        grades.add(Grade.C);
        grades.add(Grade.S);
        filter = new GradeFilter(grades);
        inputList.add(createDummyCourse("Underwater Basket Weaving", "BSKT4001", Grade.A));
        inputList.add(createDummyCourse("Advanced Underwater Basket Weaving", "BSKT5001", Grade.B));
        inputList.add(createDummyCourse("Subsurface Willow Container Fabrication", "BSKT6001", Grade.C));
        inputList.add(createDummyCourse("Software Engineering I", "CSCI5801", Grade.D));
        inputList.add(createDummyCourse("Software Engineering II", "CSCI5802", Grade.F));
        inputList.add(createDummyCourse("Computer Science Colluquium", "CSCI8970", Grade.S));
        inputList.add(createDummyCourse("Thesis Credits: Master's", "CSCI8777", Grade.N));
        Assert.assertEquals(filter.filter(inputList).size(), 3);
    }

    CourseTaken createDummyCourse(String name, String id, Grade grade) {
        Course course = new Course();
        course.setName(name);
        course.setId(id);
        return new CourseTaken(course, new Term(Semester.FALL, new Integer(2013)), grade);
    }
}
