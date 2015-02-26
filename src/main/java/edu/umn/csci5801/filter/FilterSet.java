package edu.umn.csci5801.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.umn.csci5801.model.CourseTaken;

/**
 * A filter set is a collection of individual filters.
 */
public class FilterSet implements Filter {
    /**
     * The list of filters.
     */
    private List<Filter> filters;

    /**
     * Constructs a filter set with one or more filters
     * @param filters the filters to add to this filter set
     */
    public FilterSet(Filter... filters) {
        this.filters = Arrays.asList(filters);
    }

    /**
     * Applies all filters in this set to the list of courses. The order should
     * not be important.
     * @param courses the list of courses to filter
     * @return the filtered list of courses
     */
    public List<CourseTaken> filter(List<CourseTaken> courses) {
        List<CourseTaken> filteredList = new ArrayList<CourseTaken>(courses);
        for (Filter filter : filters) {
            filteredList = filter.filter(filteredList);
        }
        return filteredList;
    }
}
