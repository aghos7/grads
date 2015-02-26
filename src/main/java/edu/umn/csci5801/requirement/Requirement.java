package edu.umn.csci5801.requirement;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * An interface for degree and department requirements
 */
public abstract class Requirement {
    /**
     * The name for this requirement.
     */
    protected String requirementName;

    /**
     * Constructs a requirement object with the given name.
     * @param name the name for this requirement.
     */
    public Requirement(String name) {
        this.requirementName = name;
    }

    /**
     * Checks if a student record meets this requirement.
     * @param studentRecord The student record to check
     * @return The requirement check result
     * @throws GRADSException
     */
    public abstract RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException;

    /**
     * Checks if a simulated student record meets this requirement.
     * @param studentRecord The student record to check
     * @return The requirement check result
     * @throws GRADSException
     */
    public RequirementCheckResult check(StudentRecord studentRecord, boolean simulated) throws GRADSException {
        if (!simulated) {
            // Remove courses with a grade of _
            GradeFilter filter = new GradeFilter();
            studentRecord.setCoursesTaken(filter.filter(studentRecord.getCoursesTaken()));
        }
        return check(studentRecord);
    }
}
