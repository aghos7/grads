package edu.umn.csci5801.filter;

import java.util.List;

import edu.umn.csci5801.model.CourseTaken;

/**
 * A filter takes a list of courses and removes courses that do not meet some
 * criteria.
 */
public interface Filter {
    /**
     * Filter out the courses that do not meet the criteria.
     * @param courses the list of courses to filter.
     * @return the filtered list of courses.
     */
    public List<CourseTaken> filter(List<CourseTaken> courses);
}
