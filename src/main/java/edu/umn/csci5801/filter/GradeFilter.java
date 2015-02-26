package edu.umn.csci5801.filter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;

/**
 * A filter that will remove courses that do not meet the set of valid grades.
 */
public class GradeFilter implements Filter {
    /**
     * The grades that this filter will count as valid.
     */
    private Set<Grade> validGrades;

    private static final Grade MAXIMUM_GRADE = Grade.A;

    /**
     * Constructs a grade filter that removes any incomplete courses.  That
     * is, courses with a grade of "_".
     */
    public GradeFilter() throws GRADSIllegalArgumentException {
        this(Grade.N, false);
    }

    /**
     * Constructs a grade filter with only a minimum grade. Any course with a
     * grade below the minimum is removed.
     * @param minimumGrade the minimum grade for a valid course.
     */
    public GradeFilter(Grade minimumGrade) throws GRADSIllegalArgumentException {
        this(minimumGrade, true);
    }

    /**
     * Constructs a grade filter with only a minimum grade. Any course with a
     * grade below the minimum is removed.
     * @param minimumGrade the minimum grade for a valid course.
     * @param includeIncomplete filter out "_" grades?
     */
    public GradeFilter(Grade minimumGrade, boolean includeIncomplete) throws GRADSIllegalArgumentException {
        this(MAXIMUM_GRADE, minimumGrade, includeIncomplete);
    }

    /**
     * Constructs a grade filter with a grade range. Any course with a grade
     * outside of the range is removed.
     * @param minimumGrade the minimum grade for a valid course.
     * @param maximumGrade the maximum grade for a valid course.
     */
    public GradeFilter(Grade maximumGrade, Grade minimumGrade) throws GRADSIllegalArgumentException {
        this(maximumGrade, minimumGrade, true);
    }

    /**
     * Constructs a grade filter with a grade range. Any course with a grade
     * outside of the range is removed.
     * @param minimumGrade the minimum grade for a valid course.
     * @param maximumGrade the maximum grade for a valid course.
     */
    public GradeFilter(Grade maximumGrade, Grade minimumGrade, boolean includeIncomplete) throws GRADSIllegalArgumentException {
        if (minimumGrade.ordinal() < maximumGrade.ordinal()) {
            throw new GRADSIllegalArgumentException("The minimum grade cannot be larger than the maximum grade");
        }
        validGrades = new LinkedHashSet<Grade>();
        for (Grade grade : Grade.values()) {
            // The higher grades are lower in order
            if (grade.ordinal() <= minimumGrade.ordinal() && grade.ordinal() >= maximumGrade.ordinal()) {
                validGrades.add(grade);
            }
        }
        if (includeIncomplete) {
            validGrades.add(Grade._);
        }
    }

    /**
     * Constructs a grade filter with a set of Grades. Any course with a grade
     * in the set is kept.
     * @param grades the set of valid grades
     */
    public GradeFilter(Set<Grade> grades) {
        this.validGrades = grades;
    }

    /**
     * Filter the courses, removing any courses that do not meet the grade
     * criteria
     * @param courses the list of courses to filter
     * @return the filtered list of courses
     */
    public List<CourseTaken> filter(List<CourseTaken> courses) {
        List<CourseTaken> filteredList = new ArrayList<CourseTaken>();
        for (CourseTaken course : courses) {
            Grade grade = course.getGrade();
            if (validGrades.contains(grade)) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }
}
