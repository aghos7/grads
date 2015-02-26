package edu.umn.csci5801.requirement;

import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.Filter;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * A requirement to calculate and check the student's GPA.
 */
public class GpaRequirement extends Requirement {
    /**
     * The course filter that will reduce the student's list of courses to only
     * those that this requirement applies.
     */
    private final Filter courseFilter;

    /**
     * The minimum GPA needed to satisfy this requirement.
     */
    private final float requiredGpa;

    /**
     * Constructs a GpaRequiremnt object with a specific GPA requirement and a
     * filter that makes it so we select the intended set of courses.
     * @param requirementName the name for this requirement.
     * @param requiredGpa the minimum GPA needed to satisfy this requirement.
     * @param courseFilter a filter that reduces the student's list of courses
     *        to only those that this requirement applies.
     */
    public GpaRequirement(String requirementName, float requiredGpa, Filter courseFilter) {
        super(requirementName);
        this.requiredGpa = requiredGpa;
        this.courseFilter = courseFilter;
    }

    /**
     * Checks the GPA requirement against the student record.
     * @param studentRecord the student inforamtion that contains the course
     *        list used for calculating the GPA.
     * @return the result of this requirement check.
     */
    @Override
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        List<CourseTaken> filteredCourses = courseFilter.filter(studentRecord.getCoursesTaken());

        float gpa = calculateGpa(filteredCourses);
        if (Double.isNaN(gpa)) {
            result.addErrorMsg("No applicable courses found to calculate a GPA.");
        } else if (gpa < requiredGpa) {
            result.addErrorMsg("The calculated GPA (" + gpa + ") is less than the requirement (" + requiredGpa + ")");
        }
        CheckResultDetails details = new CheckResultDetails();
        details.setGPA(gpa);
        details.setCourses(filteredCourses);
        result.setDetails(details);
        return result;
    }

    /**
     * Helper function that takes a list of courses and calcuates the GPA.
     * @param courses the list of courses
     * @return the GPA for the given courses.
     */
    public static float calculateGpa(List<CourseTaken> courses) {
        if (courses.size() == 0) {
            return Float.NaN;
        }
        float gradeSum = 0;
        int creditSum = 0;
        for (CourseTaken course : courses) {
            int credits = Integer.parseInt(course.getCourse().getNumCredits());
            gradeSum += credits * course.getGrade().numericValue();
            creditSum += credits;
        }

        return gradeSum / creditSum;
    }
}
