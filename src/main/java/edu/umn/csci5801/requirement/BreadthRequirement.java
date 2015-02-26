package edu.umn.csci5801.requirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.AreaFilter;
import edu.umn.csci5801.filter.DuplicateFilter;
import edu.umn.csci5801.filter.Filter;
import edu.umn.csci5801.filter.FilterSet;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * A requirement that checks the student record for satisfying the breadth requirement
 */
public class BreadthRequirement extends Requirement {
    /**
     * A map of breadth areas to the number of courses required in that area.
     */
    private final Map<CourseArea, Integer> requiredCoursesPerArea;

    /**
     * The total number of courses required in any breadth area.
     */
    private final int requiredCoursesTotal;

    /**
     * The minimum required GPA for the courses chosen for the breadth requirement.
     */
    private final float requiredGpa;

    /**
     * Constructs a BreadthRequirement object which checks the student record to
     * see if the student has completed the breadth requirement.
     * @param requirementName The name for this requirement.
     * @param coursesPerArea A map of breadth areas to the number of courses required in that area.
     * @param coursesTotal The total number of courses required in any breadth area.
     * @param gpa The minimum required GPA for the courses chosen for the breadth requirement.
     */
    public BreadthRequirement(String requirementName, Map<CourseArea, Integer> coursesPerArea, int coursesTotal,
            float gpa) {
        super(requirementName);
        this.requiredCoursesPerArea = coursesPerArea;
        this.requiredCoursesTotal = coursesTotal;
        this.requiredGpa = gpa;
    }

    /** 
     * Checks the student's course list for the required breadth courses, and
     * calculates the GPA for the chosen courses.
     * @param studentRecord the student information that contains the course
     *        list.
     * @return the result of this requirement check.
     */
    @Override
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        CheckResultDetails details = new CheckResultDetails();
        result.setDetails(details);

        // Step 1: Remove duplicates (saving the best grades)
        Filter filter = new FilterSet(new DuplicateFilter(), new GradeFilter(Grade.F));
        List<CourseTaken> allCourses = filter.filter(studentRecord.getCoursesTaken());
        List<CourseTaken> unpickedCourses = new ArrayList<CourseTaken>();
        List<CourseTaken> pickedCourses = new ArrayList<CourseTaken>();

        // Step 2: Pick the best courses for each area
        for (CourseArea area : requiredCoursesPerArea.keySet()) {
            // Filter by course
            filter = new AreaFilter(area);
            List<CourseTaken> areaCourses = sortCoursesByGrade(filter.filter(allCourses));
            List<CourseTaken> chosenAreaCourses = new ArrayList<CourseTaken>();

            for (CourseTaken course : areaCourses) {
                if (chosenAreaCourses.size() < requiredCoursesPerArea.get(area)) {
                    chosenAreaCourses.add(course);
                    pickedCourses.add(course);
                } else {
                    unpickedCourses.add(course);
                }
            }

            if (chosenAreaCourses.size() < requiredCoursesPerArea.get(area)) {
                result.addErrorMsg("Not enough courses for area " + area);
            }
        }

        // Step 3: Pick remaining courses to meet the total required in the
        // breadth area
        sortCoursesByGrade(unpickedCourses);
        for (CourseTaken course : unpickedCourses) {
            if (pickedCourses.size() < requiredCoursesTotal) {
                pickedCourses.add(course);
            }
        }
        if (pickedCourses.size() < requiredCoursesTotal) {
            result.addErrorMsg("Not enough courses for breadth requirement");
        }
        details.setCourses(pickedCourses);

        // Step 4: Check the GPA for the chosen courses
        GradeFilter gradeFilter = new GradeFilter(Grade.F, false); // Filter out _ grades
        pickedCourses = gradeFilter.filter(pickedCourses);
        float gpa = GpaRequirement.calculateGpa(pickedCourses);
        if (gpa < requiredGpa) {
            result.addErrorMsg("The GPA for the selected breadth courses (" + gpa + ") is less than the requirement ("
                    + requiredGpa + ")");
        }
        details.setGPA(gpa);

        // Step 5: Done.
        return result;
    }

    /**
     * A helper method that sorts a list of CourseTaken objects by their Grade.
     * @param courses the list to sort.
     * @return the sorted list.
     */
    private List<CourseTaken> sortCoursesByGrade(List<CourseTaken> courses) {
        Collections.sort(courses, new Comparator<CourseTaken>() {
            @Override
            public int compare(CourseTaken c1, CourseTaken c2) {
                return c1.getGrade().ordinal() - c2.getGrade().ordinal();
            }
        });
        return courses;
    }
}
