package edu.umn.csci5801.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;

/**
 * A filter to return only the courses that satisfy certain breadth areas.
 */
public class AreaFilter implements Filter {
    /**
     * The set of areas this filter will look for.
     */
    private Set<CourseArea> validAreas;

    /**
     * The constructor that sets up the filter.
     * @param areas the list of areas used to filter the course list.
     */
    public AreaFilter(CourseArea... areas) {
        this.validAreas = new HashSet<CourseArea>(Arrays.asList(areas));
    }

    /**
     * Filter the courses, returning only the courses that satisfy a breadth
     * area specified in the areas set.
     * @param courses the list of courses to filter
     * @return the filtered list of courses
     */
    public List<CourseTaken> filter(List<CourseTaken> courses) {
        List<CourseTaken> filteredList = new ArrayList<CourseTaken>();
        for (CourseTaken course : courses) {
            if (validAreas.contains(course.getCourse().getCourseArea())) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }
}
