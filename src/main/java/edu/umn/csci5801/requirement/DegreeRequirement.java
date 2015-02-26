package edu.umn.csci5801.requirement;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * A requirement that ensures the student has chosen a degree
 */
public class DegreeRequirement extends Requirement {
    /**
     * Constructs a degree requirement object with the given name.
     * @param name the name for this requirement.
     */
    public DegreeRequirement() {
        super("DEGREE_SOUGHT");
    }

    /**
     * Checks if a student has declared a degree
     * @param studentRecord The student record to check
     * @return The requirement check result
     * @throws GRADSException
     */
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        if (studentRecord.getDegreeSought() != null) {
            CheckResultDetails details = new CheckResultDetails();
            List<String> otherDetails = new ArrayList<String>();
            otherDetails.add(studentRecord.getDegreeSought().toString());
            details.setOther(otherDetails);
            result.setDetails(details);
        } else {
            result.addErrorMsg("No degree set");
        }
        return result;
    }
}
