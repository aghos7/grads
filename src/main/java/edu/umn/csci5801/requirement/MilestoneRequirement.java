package edu.umn.csci5801.requirement;

import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

/**
 * A requirement to check the student has completed a certain milestone.
 */
public class MilestoneRequirement extends Requirement {

    /**
     * The specific milestone that is needed to satisfy this requirement.
     */
    private final Milestone requiredMilestone;

    /**
     * Constructs a MilestoneRequirement object with a specific milestone to
     * look for.
     * @param requirementName the name for this requirement.
     * @param milestone The specific milestone for this requirement.
     */
    public MilestoneRequirement(Milestone milestone) {
        super(milestone.toString());
        requiredMilestone = milestone;
    }

    /**
     * Checks if the student has completed a certain milestone.
     * @param studentRecord The student that contains the milestone list.
     * @return the result of this requirement check.
     */
    @Override
    public RequirementCheckResult check(StudentRecord studentRecord) throws GRADSException {
        RequirementCheckResult result = new RequirementCheckResult(requirementName);
        CheckResultDetails details = new CheckResultDetails();
        result.setDetails(details);
        List<String> otherDetails = new ArrayList<String>();
        details.setOther(otherDetails);
        for (MilestoneSet milestone : studentRecord.getMilestonesSet()) {
            if (requiredMilestone.equals(milestone.getMilestone())) {
                otherDetails.add("Completed " + milestone.getTerm());
                return result;
            }
        }
        result.addErrorMsg("Milestone " + requiredMilestone + " not set");
        return result;
    }
}
