package edu.umn.csci5801.requirement;

import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.Filter;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * A requirement to check that a student has completed a specific course
 */
public class CourseRequirement extends Requirement {

    /**
     * The filter that will select the required course.
     */
    private final Filter courseFilter;

    /**
     * Constructs a CourseRequirement object that checks that the student has
     * completed the required course.
     * @param requirementName the name for this requirement.
     * @param courseFilter the filter that will select the required course from
     *        the student's course list.
     */
    public CourseRequirement(String requirementName, Filter courseFilter) {
        super(requirementName);
        this.courseFilter = courseFilter;
    }

    /**
     * Checks the student's course list for the required course.
     * @param studentRecord the student information that contains the course
     *        list.
     * @return the result of this requirement check.
     */
    @Override
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        CheckResultDetails details = new CheckResultDetails();
        result.setDetails(details);
        List<CourseTaken> filteredCourses = courseFilter.filter(studentRecord.getCoursesTaken());
        if (filteredCourses.size() == 0) {
            result.addErrorMsg("Required course not taken");
        } else {
            details.setCourses(filteredCourses);
        }
        return result;
    }

}
