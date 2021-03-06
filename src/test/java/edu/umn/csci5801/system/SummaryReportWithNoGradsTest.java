package edu.umn.csci5801.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.RequirementRepository;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;

/**
 * Generate and verify summary report for a student with no graduation
 * requirements complete(Test case 1)
 */
public class SummaryReportWithNoGradsTest {

    private GRADSIntf grads;

    // student ids to test
    private final String[] studentIds = { "chri3310", "nguy0621" };

    private Map<Degree, Integer> degreeToRequirementCount;

    private final String studentsFileName = "src/resources/students_noReqComplete.txt";
    private final String coursesFileName = "src/resources/courses.txt";
    private final String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);

        // initialize the requirement sizes
        RequirementRepository requirementRepository = new JSONRequirementRepository(GRADS.DEFAULT_REQUIREMENTS_PATH);
        degreeToRequirementCount = new HashMap<Degree, Integer>();
        degreeToRequirementCount.put(Degree.MS_A, requirementRepository.find(Degree.MS_A, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.MS_B, requirementRepository.find(Degree.MS_B, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.MS_C, requirementRepository.find(Degree.MS_C, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.PHD, requirementRepository.find(Degree.PHD, Department.COMPUTER_SCIENCE)
                .size());

    }

    @Test
    public void test() throws Exception {
        for (String studentId : studentIds) {
            grads.setUser(studentId);
            ProgressSummary progressSummary = grads.generateProgressSummary(studentId);
            assertTrue("RequirementCheckResults size does not match requirements size", progressSummary
                    .getRequirementCheckResults().size() == degreeToRequirementCount.get(progressSummary
                    .getDegreeSought()));
            for (RequirementCheckResult requirementCheckResult : progressSummary.getRequirementCheckResults()) {
                // make sure all requirements fail except for DEGREE_SOUGHT
                // which is always true
                if (!requirementCheckResult.getName().equals("DEGREE_SOUGHT")) {
                    assertFalse("Requirement " + requirementCheckResult.getName() + " expected to fail",
                            requirementCheckResult.isPassed());
                    assertTrue("Requirement " + requirementCheckResult.getName() + " failed with no errors",
                            requirementCheckResult.getErrorMsgs().size() > 0);
                }
            }
        }
    }
}
