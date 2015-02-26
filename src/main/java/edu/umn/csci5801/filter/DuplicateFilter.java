package edu.umn.csci5801.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;

/**
 * A filter that removes duplicate courses, choosing the course with the better
 * grade.
 */
public class DuplicateFilter implements Filter {
    /**
     * Filter out duplicate courses, choosing the course with the better grade.
     * @param courses the list of courses to filter
     * @return the filtered list of courses
     */
    public List<CourseTaken> filter(List<CourseTaken> courses) {
        HashMap<String, CourseTaken> uniqueCourses = new HashMap<String, CourseTaken>();
        for (CourseTaken course : courses) {
            String courseId = course.getCourse().getId();
            if (uniqueCourses.containsKey(courseId)) {
                Grade existingGrade = uniqueCourses.get(courseId).getGrade();
                if (existingGrade.numericValue() < course.getGrade().numericValue()) {
                    uniqueCourses.put(courseId, course);
                }
            } else {
                uniqueCourses.put(courseId, course);
            }
        }
        List<CourseTaken> filteredList = new ArrayList<CourseTaken>();
        filteredList.addAll(uniqueCourses.values());
        return filteredList;
    }
}
