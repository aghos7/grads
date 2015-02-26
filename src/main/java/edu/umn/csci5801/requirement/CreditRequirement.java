package edu.umn.csci5801.requirement;

import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.Filter;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * A requirement to check the student has received a minimum number of credits.
 */
public class CreditRequirement extends Requirement {
    /**
     * The course filter that will reduce the student's list of courses to only
     * those that this requirement applies.
     */
    private final Filter courseFilter;

    /**
     * The minimum number of credits to satisfy this requirement.
     */
    private final int requiredCredits;

    /**
     * Constructs a CreditRequiremnt object with a specific credit minimum
     * requirement and a filter that makes it so we select the intended set of
     * courses.
     * @param requirementName the name for this requirement.
     * @param requiredCredits the minimum number of credits needed to satisfy
     *        this requirement.
     * @param courseFilter a filter that reduces the student's list of courses
     *        to only those that this requirement applies.
     */
    public CreditRequirement(String requirementName, int requiredCredits, Filter courseFilter) {
        super(requirementName);
        this.requiredCredits = requiredCredits;
        this.courseFilter = courseFilter;
    }

    /**
     * Checks the credit requirement against the student record.
     * @param studentRecord the student inforamtion that contains the course
     *        list used for counting the number of credits.
     * @return the result of this requirement check.
     */
    @Override
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        CheckResultDetails details = new CheckResultDetails();
        List<CourseTaken> filteredCourses = courseFilter.filter(studentRecord.getCoursesTaken());

        int credits = calculateCredits(filteredCourses);
        if (credits < requiredCredits) {
            result.addErrorMsg("The number of credits (" + credits + ") is less than the requirement ("
                    + requiredCredits + ")");
        }
        List<String> otherDetails = new ArrayList<String>();
        otherDetails.add(Integer.toString(credits));
        details.setOther(otherDetails);
        details.setCourses(filteredCourses);
        result.setDetails(details);
        return result;
    }

    /**
     * Helper function that takes a list of courses, applies a course filter,
     * and counts the number of credits for the filtered list of courses.
     * @param courses the student's list of courses
     * @return the number of credits for the given courses.
     */
    private int calculateCredits(List<CourseTaken> courses) {
        int credits = 0;
        for (CourseTaken course : courses) {
            credits += Integer.parseInt(course.getCourse().getNumCredits());
        }
        return credits;
    }
}
