package edu.umn.csci5801.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;

/**
 * A course filter
 */
public class CourseFilter implements Filter {
    /**
     * The generated regular expression used to filter courses. Assumes that
     * course ids follow the format: [A-Z]{3,4}\d{4}
     */
    private String courseRegex = "";

    /**
     * Reverse the match. That is, keep the courses that don't match the filter.
     */
    private boolean reverse;

    /**
     * Filter based on specific courses
     * @param reversed Reverse the filter so we keep the courses that don't
     *        match
     * @param courses The courses to filter with
     */
    public CourseFilter(boolean reversed, Course... courses) throws GRADSIllegalArgumentException {
        this(courses);
        reverse = reversed;
    }

    /**
     * Filter based on specific courses
     * @param courses The courses to filter with
     */
    public CourseFilter(Course... courses) throws GRADSIllegalArgumentException {
        for (Course course : courses) {
            if (course.getId().length() == 0) {
                throw new GRADSIllegalArgumentException("Courses cannot have an empty course id");
            }
            if (courseRegex.length() == 0) {
                courseRegex = course.getId();
            } else {
                courseRegex = courseRegex.concat("|" + course.getId());
            }
        }
    }

    /**
     * Filter based on a specific department.
     * @param department The department to filter with
     * @param reversed Reverse the filter so we keep the courses that don't
     *        match
     */
    public CourseFilter(String department, boolean reversed) throws GRADSIllegalArgumentException {
        this(department);
        reverse = reversed;
    }

    /**
     * Filter based on a specific department.
     * @param department The department to filter with
     */
    public CourseFilter(String department) throws GRADSIllegalArgumentException {
        if (department.length() == 0) {
            throw new GRADSIllegalArgumentException("Departments cannot be empty");
        }
        courseRegex = "^" + department + "\\d{4}$";
    }

    /**
     * Filter based on a specific course level. This will filter any courses at
     * the given level or higher.
     * @param minimumLevel the minimum course level
     */
    public CourseFilter(int minimumLevel) throws GRADSIllegalArgumentException {
        this(minimumLevel, 9);
    }

    /**
     * Filter based on a specific course level. This will filter any courses
     * within the given range.
     * @param minimumLevel the minimum course level to take
     * @param minimumLevel the maximum course level to take
     */
    public CourseFilter(int minimumLevel, int maximumLevel) throws GRADSIllegalArgumentException {
        if (minimumLevel > maximumLevel) {
            throw new GRADSIllegalArgumentException("minimumLevel cannot be larger than maximumLevel");
        }
        if (minimumLevel <= 0 || minimumLevel > 9) {
            throw new GRADSIllegalArgumentException("minimumLevel must be between 1 and 9");
        }
        if (maximumLevel <= 0 || maximumLevel > 9) {
            throw new GRADSIllegalArgumentException("maximumLevel must be between 1 and 9");
        }
        courseRegex = "^[a-zA-Z]{3,4}[" + minimumLevel + "-" + maximumLevel + "]\\d{3}$";
    }

    /**
     * Filter the courses, removing any courses that do not meet the course
     * criteria.
     * @param courses the list of courses to filter
     * @return the filtered list of courses
     */
    @Override
    public List<CourseTaken> filter(List<CourseTaken> courses) {
        List<CourseTaken> filteredList = new ArrayList<CourseTaken>();
        for (CourseTaken course : courses) {
            Matcher m = Pattern.compile(courseRegex, Pattern.CASE_INSENSITIVE).matcher(course.getCourse().getId());
            if ((reverse && !m.matches()) || (!reverse && m.matches())) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }
}
